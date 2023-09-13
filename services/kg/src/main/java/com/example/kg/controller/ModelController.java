package com.example.kg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.example.common.result.Result;
import com.example.common.result.ResultTypeEnum;
import com.example.kg.dto.ModelRemoteDTO;
import com.example.kg.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("model")
public class ModelController {
    @Reference
    private ModelService modelService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 申请参数获取更新
     * @return
     */
    @RequestMapping("/update")
    public Result<ModelRemoteDTO> getModelService() {
        return Result.success(modelService.updateModel());
    }

    /**
     * 获得模型更新结果
     * @return
     */
    @RequestMapping("/result")
    public Result<Map<Object, Object>> getModelResult() {
        return Result.success(redisTemplate.opsForHash().entries("result"));
    }
}
