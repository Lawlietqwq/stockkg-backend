package com.example.kg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.example.common.result.Result;
import com.example.common.result.ResultTypeEnum;
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
    public Result<String> getModelService() {
        String date = redisTemplate.opsForValue().get("last_modified");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String nowDay = sdf.format(new Date());
        if(date!=null&&date.equals(nowDay)) return new Result<>(ResultTypeEnum.UPDATE_ALREADY);
        String res = modelService.updateModel();
        if(res.equals("更新失败")) return new Result<>(ResultTypeEnum.UPDATE_ERROR);
        return new Result<>(ResultTypeEnum.UPDATE_SUCCESS);
    }

    /**
     * 获得模型更新结果
     * @return
     */
    @RequestMapping("/result")
    public Result<Map<Object, Object>> getModelResult() {
        Map<Object,Object> data =  redisTemplate.opsForHash().entries("result");
        Result<Map<Object, Object>> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(data);
        return result;
    }
}
