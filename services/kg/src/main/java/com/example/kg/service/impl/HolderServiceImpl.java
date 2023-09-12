package com.example.kg.service.impl;

import cn.hutool.json.JSONUtil;
import com.example.cache.DistributedCache;
import com.example.common.exception.CommonException;
import com.example.common.tools.JsonUtil;
import com.example.kg.common.enums.VerifyChainEnum;
import com.example.kg.dao.HolderRepository;
import com.example.kg.dao.RelationRepository;
import com.example.kg.dto.HolderQueryDTO;
import com.example.kg.dto.StockQueryDTO;
import com.example.kg.entity.neo.Holder;
import com.example.kg.entity.neo.Stock;
import com.example.kg.service.HolderService;
import com.example.kg.util.POJOUtil;
import com.example.kg.vo.HolderVO;
import com.example.patterns.chain.AbstractChainContext;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.neo4j.ogm.session.SessionFactory;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.example.kg.common.constant.Constant.SPLICING_OPERATOR;
import static com.example.kg.common.constant.RedisKeyConstant.*;

@Service
@RequiredArgsConstructor
public class HolderServiceImpl implements HolderService {
    private final HolderRepository holderRepository;

    private final RelationRepository relationRepository;

    private final DistributedCache distributedCache;

    private final RedissonClient redissonClient;

    private final AbstractChainContext<HolderQueryDTO> holderQueryAbstractChainContext;

    /**
     * 根据股东名称获取股东和相连股票节点
     * @param requestParam
     * @return
     */
    @Override
    public HolderVO getHolderVOByName(HolderQueryDTO requestParam) throws CommonException{
        //职责链检查入参有效性，检查缓存中是否存在股东节点和更新时间的映射数据
        holderQueryAbstractChainContext.handler(VerifyChainEnum.HOLDER_VERIFY_CHAIN.name(), requestParam);
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        Object mapping =  stringRedisTemplate.opsForHash().get(QUERY_HOLDER_MAPPING, requestParam.getName());
        HolderVO holderVO = new HolderVO();
        if(mapping==null) throw new CommonException("没有股东信息");
        else{
            String str = (String) mapping;
            //缓存中有股东节点信息
            if(str.length()!=0){
                String[] dateList = str.split(SPLICING_OPERATOR);
                if(Arrays.stream(dateList).mapToInt(Integer::parseInt).noneMatch(each -> each == requestParam.getEndDate())){
                    throw new CommonException("不是股东更新时间");
                }else{
                    //endDate正确匹配,继续查缓存
                    Object result = stringRedisTemplate.opsForHash().get(QUERY_HOLDER, requestParam.getEndDate());
                    //缓存中查询到
                    if(result!=null){
                        holderVO = (HolderVO) result;
                        return holderVO;
                    }
                }
            }
        }
        //缓存没查到查询图数据库
        RLock lock = redissonClient.getLock(QUERY_HOLDER_LOCK);
        lock.lock();
        try {
            //双重检查
            Object result = stringRedisTemplate.opsForHash().get(QUERY_HOLDER, requestParam.getEndDate());
            //缓存中查询到
            if(result!=null){
                holderVO = (HolderVO) result;
                return holderVO;
            }
            //查询图数据库
            Holder holder = holderRepository.findByName(requestParam.getName());
            List<Stock> stocks = holderRepository.findEndNodes(requestParam.getName(), requestParam.getEndDate());
            holderVO.setHolderNodeVO(POJOUtil.getHolderNode(holder));
            holderVO.setStocks(POJOUtil.getStockNodeList(stocks));
            holderVO.setRelationList(POJOUtil.getRelationList(relationRepository.getHolderRelationships(requestParam.getEndDate(), requestParam.getName())));
            //写回缓存
            stringRedisTemplate.opsForHash().put(QUERY_HOLDER, requestParam.getEndDate(), holderVO);
        }finally {
            lock.unlock();
        }
        return holderVO;
    }}
