package com.example.kg.filter.node;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.cache.DistributedCache;
import com.example.common.exception.CommonException;
import com.example.kg.dto.StockQueryDTO;
import com.example.kg.entity.mysql.StockMappingDO;
import com.example.kg.mapper.StockMappingMapper;
import com.example.kg.filter.StockVerifyFilter;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.kg.common.constant.Constant.SPLICING_OPERATOR;
import static com.example.kg.common.constant.RedisKeyConstant.*;


@Component
@RequiredArgsConstructor
public class StockMappingDateFilter implements StockVerifyFilter<StockQueryDTO> {
    private final StockMappingMapper stockMappingMapper;
    private final DistributedCache distributedCache;
    private final RedissonClient redissonClient;

    @Override
    public void handler(StockQueryDTO requestParam) {
        // 验证mapping缓存是否存在
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
        Object actualExist = hashOperations.get(
                QUERY_STOCK_MAPPING,
                requestParam.getTsCode()
        );
        //已加载缓存且缓存中有待查找key
        if(actualExist != null) return;
        RLock lock = redissonClient.getLock(QUERY_STOCK_MAPPING_LOCK);
        lock.lock();
        try {
            //进行双重检查
            if (distributedCache.hasKey(QUERY_STOCK_MAPPING)) {
                actualExist = hashOperations.get(
                        QUERY_STOCK_MAPPING,
                        requestParam.getTsCode()
                );
                if (actualExist==null) {
                    throw new CommonException("不存在该股东");
                }
                return;
            }
            LambdaQueryWrapper<StockMappingDO> queryWrapper = Wrappers.lambdaQuery(StockMappingDO.class)
                    .eq(StockMappingDO::getTsCode, requestParam.getTsCode());
            List<StockMappingDO> stockMappingDOS = stockMappingMapper.selectList(queryWrapper);
            StringBuilder endDateList = new StringBuilder();
            for (StockMappingDO each : stockMappingDOS) {
                endDateList.append(each.getEndDate()).append(SPLICING_OPERATOR);
            }
            //股票没有拓扑连边
            if(endDateList.length() == 0) return;
            endDateList.deleteCharAt(endDateList.charAt(endDateList.length()-1));
            hashOperations.put(QUERY_STOCK_MAPPING, requestParam.getTsCode(), endDateList.toString());
            actualExist = hashOperations.get(
                    QUERY_STOCK_MAPPING,
                    requestParam.getTsCode()
            );
            if (actualExist == null) {
                throw new CommonException("不存在该股东");
            }
            //判断endDate是否在mapping表中
            if(stockMappingDOS.stream().noneMatch(a -> a.getEndDate() == requestParam.getEndDate())){
                throw new CommonException("不是股东更新时间");
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getOrder() {
        return 10;
    }
}