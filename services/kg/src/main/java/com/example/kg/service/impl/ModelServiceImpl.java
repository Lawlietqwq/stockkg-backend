package com.example.kg.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.cache.DistributedCache;
import com.example.common.exception.CommonException;
import com.example.common.tools.BeanUtil;
import com.example.common.tools.DateUtil;
import com.example.database.idgenerator.tools.SnowflakeIdUtil;
import com.example.kg.dto.ModelDTO;
import com.example.kg.dto.ModelRemoteDTO;
import com.example.kg.entity.mysql.HolderMappingDO;
import com.example.kg.entity.mysql.ModelDO;
import com.example.kg.mapper.ModelMapper;
import com.example.kg.remote.ModelRemoteService;
import com.example.kg.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.text.ParseException;
import java.util.*;

import static com.example.kg.common.constant.RedisKeyConstant.*;

@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {

    @Reference
    private final ModelRemoteService modelRemoteService;

    private final ModelMapper modelMapper;

    private final DistributedCache distributedCache;

    private final RedissonClient redissonClient;

    /**
     * 更新模型参数
     * @return
     */
    @Override
    public ModelRemoteDTO updateModel() {
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        //判断锁是否被占用
        if(distributedCache.hasKey(MODIFY_LOCK)){
            throw new CommonException("模型参数正在更新"); 
        }
        //判断是否已经被更新参数
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        String date = stringStringValueOperations.get(LAST_MODIFIED);
        Date preDate = null;
        try {
            preDate = DateUtil.strToDate(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        //更新时间在一周以内
        if(!DateUtil.dateDiff(preDate, new Date(), 7))
            throw new CommonException("模型参数已经最新");

        ModelRemoteDTO modelRemoteDTO = new ModelRemoteDTO();
        RLock lock = redissonClient.getLock(MODIFY_LOCK);
        lock.lock();
        try {
            //进行双重检查
            date = stringStringValueOperations.get(LAST_MODIFIED);
            preDate = null;
            try {
                preDate = DateUtil.strToDate(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            //更新时间在一周以内
            if(!DateUtil.dateDiff(preDate, new Date(), 7))
                throw new CommonException("模型参数已经最新");

            //根据自定义雪花算法计算出id
            Long id = SnowflakeIdUtil.nextId();
            modelRemoteDTO = modelRemoteService.updateModel(id);
            if (modelRemoteDTO.getUpdateTime() == null) {
                throw new CommonException("模型更新失败");
            }
            //更新时间
            stringStringValueOperations.set(LAST_MODIFIED, DateUtil.dateToStr(modelRemoteDTO.getUpdateTime()));

        } finally {
            lock.unlock();
        }
        return modelRemoteDTO;
    }

    /**
     * 获取模型最新排名
     *
     * @return
     */
    @Override
    public Map<Object,Object> getModelResult(String date) {
        // 验证缓存是否存在
        Map<Object, Object> map = new HashMap<>();
        String key = QUERY_MODEL + date;
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
        if(distributedCache.hasKey(key)){
            map = hashOperations.entries(
                    QUERY_MODEL + date
            );
            return map;
        }
        Date tradingDate = null;
        try {
            tradingDate = DateUtil.strToDate(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        RLock lock = redissonClient.getLock(QUERY_MODEL_LOCK);
        lock.lock();
        try {
            if(distributedCache.hasKey(key)){
                map = hashOperations.entries(
                        QUERY_MODEL + date
                );
                return map;
            }
            LambdaQueryWrapper<ModelDO> queryWrapper = Wrappers.lambdaQuery(ModelDO.class)
                    .eq(ModelDO::getTradingDate, tradingDate);
            for(ModelDO each:modelMapper.selectList(queryWrapper)){
                map.put(each.getIndustry(), each.getRank());
            }
            //更新缓存
            hashOperations.putAll(key, map);
        } finally {
            lock.unlock();
        }

        return map;
    }
}
