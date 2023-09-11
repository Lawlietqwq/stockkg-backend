package com.example.kg.controller;

import com.example.kg.common.RepeatSubmit;
import com.example.kg.common.ResultTypeEnum;
import com.example.kg.service.HolderService;
import com.example.kg.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.kg.vo.HolderVO;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/holder")
public class HolderController {

    @Autowired
    private HolderService holderService;

    @RepeatSubmit
    @GetMapping("")
    public Result<HolderVO> getHolderMap(@RequestParam("holder_name") String holderName, @RequestParam("end_date") int endDate){
        HolderVO holderVO = holderService.getHolderVOByName(holderName, endDate);
        Result<HolderVO> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(holderVO);
        return result;
    }
}
