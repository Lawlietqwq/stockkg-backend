package com.example.kg.service.impl;

import com.example.cache.DistributedCache;
import com.example.common.exception.CommonException;
import com.example.kg.common.enums.VerifyChainEnum;
import com.example.kg.dao.HolderRepository;
import com.example.kg.dao.RelationRepository;
import com.example.kg.dao.StockRepository;
import com.example.kg.dto.StockQueryDTO;
import com.example.kg.entity.neo.Holder;
import com.example.kg.entity.neo.Stock;
import com.example.kg.entity.neo.StockRelationship;
import com.example.kg.service.StockService;
import com.example.kg.util.POJOUtil;
import com.example.kg.vo.*;
import com.example.patterns.chain.AbstractChainContext;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.kg.common.constant.Constant.SPLICING_OPERATOR;
import static com.example.kg.common.constant.RedisKeyConstant.*;
import static com.example.kg.common.constant.RedisKeyConstant.QUERY_HOLDER;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private final HolderRepository holderRepository;

    private final StockRepository stockRepository;

    private final RelationRepository relationRepository;

    private final DistributedCache distributedCache;

    private final RedissonClient redissonClient;

    private final AbstractChainContext<StockQueryDTO> stockQueryAbstractChainContext;

    /**
     * 根据股票代码和更新日期进行查询股票节点和相连股东节点
     * @param requestParam
     * @return
     */
    @Override
    public StockVO getStockVOByTsCode(StockQueryDTO requestParam) {
        //职责链检查入参有效性，检查缓存中是否存在股票节点和更新时间的映射数据
        stockQueryAbstractChainContext.handler(VerifyChainEnum.STOCK_VERIFY_CHAIN.name(), requestParam);
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        Object mapping =  stringRedisTemplate.opsForHash().get(QUERY_STOCK_MAPPING, requestParam.getTsCode());
        StockVO stockVO = new StockVO();
        if(mapping==null) throw new CommonException("没有股票信息");
        else{
            String str = (String) mapping;
            //缓存中有股票节点信息
            if(str.length()!=0){
                String[] dateList = str.split(SPLICING_OPERATOR);
                if(Arrays.stream(dateList).mapToInt(Integer::parseInt).noneMatch(each -> each == requestParam.getEndDate())){
                    throw new CommonException("不是股票更新时间");
                }else{
                    //endDate正确匹配,继续查缓存
                    Object result = stringRedisTemplate.opsForHash().get(QUERY_STOCK, requestParam.getEndDate());
                    //缓存中查询到
                    if(result!=null){
                        stockVO = (StockVO) result;
                        return stockVO;
                    }
                }
            }
        }
        //缓存没查到查询neo4j数据库
        RLock lock = redissonClient.getLock(QUERY_STOCK_LOCK);
        lock.lock();
        try {
            //双重检查
            Object result = stringRedisTemplate.opsForHash().get(QUERY_STOCK, requestParam.getEndDate());
            //缓存中查询到
            if(result!=null){
                stockVO = (StockVO) result;
                return stockVO;
            }
            //查询图数据库
            Stock stock = stockRepository.findByTsCode(requestParam.getTsCode(), requestParam.getEndDate());
            List<Holder> holders = stockRepository.findStartNodes(stock);
            stockVO.setStockNodeVO(POJOUtil.getStockNode(stock));
            stockVO.setHolders(POJOUtil.getHolderNodeList(holders));
            List<StockRelationship> list = relationRepository.getStockRelationships(stock.getId());
            stockVO.setRelationList(POJOUtil.getRelationList(list));
            //写回缓存
            stringRedisTemplate.opsForHash().put(QUERY_STOCK, requestParam.getEndDate(), stockVO);
        }finally {
            lock.unlock();
        }
        return stockVO;
    }

    /**
     * 根据股票代码和更新日期进行查询股票节点和相连股东节点以及股东节点的一跳邻居
     * @param requestParam
     * @return
     */
    @Override
    public StockOneHopVO getOneHopStockVO(StockQueryDTO requestParam) {
        stockQueryAbstractChainContext.handler(VerifyChainEnum.STOCK_VERIFY_CHAIN.name(), requestParam);
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        Object mapping =  stringRedisTemplate.opsForHash().get(QUERY_STOCK_MAPPING, requestParam.getTsCode());
        StockOneHopVO stockVO = new StockOneHopVO();
        if(mapping==null) throw new CommonException("没有股票信息");
        else{
            String str = (String) mapping;
            //缓存中有股票节点信息
            if(str.length()!=0){
                String[] dateList = str.split(SPLICING_OPERATOR);
                if(Arrays.stream(dateList).mapToInt(Integer::parseInt).noneMatch(each -> each == requestParam.getEndDate())){
                    throw new CommonException("不是股东更新时间");
                }else{
                    //endDate正确匹配,继续查缓存
                    Object result = stringRedisTemplate.opsForHash().get(QUERY_STOCK_ONE_HOP, requestParam.getEndDate());
                    //缓存中查询到
                    if(result!=null){
                        stockVO = (StockOneHopVO) result;
                        return stockVO;
                    }
                }
            }
        }
        //缓存没查到查询neo4j数据库
        RLock lock = redissonClient.getLock(QUERY_STOCK_ONE_HOP_LOCK);
        lock.lock();
        try {
            //双重检查
            Object result = stringRedisTemplate.opsForHash().get(QUERY_STOCK_ONE_HOP, requestParam.getEndDate());
            //缓存中查询到
            if(result!=null){
                stockVO = (StockOneHopVO) result;
                return stockVO;
            }
            //查询图数据库
            Stock stock = stockRepository.findByTsCode(requestParam.getTsCode(), requestParam.getEndDate());
            List<Holder> holders = stockRepository.findStartNodes(stock);
            List<StockNodeVO> oneHopStocks = new ArrayList<>();
            List<StockRelationship> neighborRelations = relationRepository.getStockRelationships(stock.getId());
            List<RelationVO> oneHop = new ArrayList<>();
            for(Holder holder: holders){
                List<Stock> stocks = holderRepository.findEndNodes(holder.getName(), requestParam.getEndDate());
                List<StockRelationship> list = relationRepository.getHolderRelationships(requestParam.getEndDate(), holder.getName());
                for(StockRelationship relation: list){
                    if(relation.getTsCode().equals(stock.getTsCode())) continue;
                    RelationVO relationVO = POJOUtil.getRelation(relation);
                    oneHop.add(relationVO);
                }

                for(Stock stock1: stocks){
                    if(stock1.getTsCode().equals(stock.getTsCode())) continue;
                    StockNodeVO stockNodeVO = POJOUtil.getStockNode(stock1);
                    oneHopStocks.add(stockNodeVO);
                }
            }
            stockVO.setStockNodeVO(POJOUtil.getStockNode(stock));
            stockVO.setHolders(POJOUtil.getHolderNodeList(holders));
            stockVO.setOneHopStocks(oneHopStocks);
            oneHop.addAll(POJOUtil.getRelationList(neighborRelations));
            stockVO.setRelationList(oneHop);
            //写回缓存
            stringRedisTemplate.opsForHash().put(QUERY_STOCK_ONE_HOP, requestParam.getEndDate(), stockVO);
        }finally {
            lock.unlock();
        }
        return stockVO;
    }

    /**
     * 获得每个股票节点最早的更新时间
     * @param tsCode
     * @return
     */
    @Override
    public int getFirstDate(String tsCode) {
        return stockRepository.findFirstAnnDate(tsCode);
    }

    /**
     * 获取所有行业信息
     * @return
     */
    @Override
    public Set<String> getIndustries() {
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        return stringRedisTemplate.opsForSet().members("industry");
    }

    /**
     * 根据行业的所有股票代码
     * @param industry
     * @return
     */
    @Override
    public Set<Object> getTsCode(String industry) {
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        return stringRedisTemplate.opsForHash().keys(industry);
    }

    /**
     * 查询所有股票代码
     * @return
     */
    @Override
    public Set<String> getAllCodes() {
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        return stringRedisTemplate.opsForSet().members("codes");
    }

    /**
     * 前端多选列表
     * @return
     */
    @Override
    public List<OptionVO> getOptions() {
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        Set<String> industries = stringRedisTemplate.opsForSet().members("industry");
        List<OptionVO> optionVOS = new ArrayList<>();
        Iterator<String> iter = industries.iterator();
        while(iter.hasNext()){
            List<Map<String, String>> children = new ArrayList<>();
            OptionVO optionVO = new OptionVO();
            String industry = iter.next();
            Set<Object> codes = stringRedisTemplate.opsForHash().keys(industry);
            for(Object o: codes){
                Map<String, String> childMap = new HashMap<>();
                if (o instanceof String){
                    String code = (String) o;
                    childMap.put("value", code);
                    childMap.put("label", code);
                }
                children.add(childMap);
            }
            optionVO.setValue(industry);
            optionVO.setLabel(industry);
            optionVO.setChildren(children);
            optionVOS.add(optionVO);
        }
        return optionVOS;
    }
}
