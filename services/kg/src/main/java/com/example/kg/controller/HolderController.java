package com.example.kg.controller;

import com.example.common.result.Result;
import com.example.common.result.ResultTypeEnum;
import com.example.kg.dto.HolderQueryDTO;
import com.example.kg.service.HolderService;
import com.example.web.annotations.RepeatSubmit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.kg.vo.HolderVO;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/holder")
public class HolderController {

    @Autowired
    private HolderService holderService;

    /**
     * 获得股东以及股东相邻股票节点
     * @param requestParam
     * @return
     */
    @RepeatSubmit
    @GetMapping("")
    public Result<HolderVO> getHolderMap(HolderQueryDTO requestParam){
        HolderVO holderVO = holderService.getHolderVOByName(requestParam);
        Result<HolderVO> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(holderVO);
        return result;
    }
}
