package com.example.kg.controller;


import com.example.kg.common.RepeatSubmit;
import com.example.kg.common.Result;
import com.example.kg.common.ResultTypeEnum;
import com.example.kg.service.StockService;
import com.example.kg.vo.OptionVO;
import com.example.kg.vo.StockOneHopVO;
import com.example.kg.vo.StockVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @RepeatSubmit
    @GetMapping("")
    public Result<StockVO> getStockMap(@RequestParam("ts_code") String tsCode, @RequestParam("end_date") int endDate){
        StockVO stockVO = stockService.getStockVOByTsCode(tsCode, endDate);
        Result<StockVO> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(stockVO);
        return result;
    }

    @RepeatSubmit
    @GetMapping("oneHop")
    public Result<StockOneHopVO> getOneHopStockMap(@RequestParam("ts_code") String tsCode, @RequestParam("end_date") int endDate){
        StockOneHopVO stockVO = stockService.getOneHopStockVO(tsCode, endDate);
        Result<StockOneHopVO> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(stockVO);
        return result;
    }

    @RepeatSubmit
    @GetMapping("annDate")
    public Result<Integer> getFisrtAnnDate(@RequestParam("ts_code") String tsCode){
        Integer date = stockService.getFirstDate(tsCode);
        Result<Integer> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(date);
        return result;
    }

    @RepeatSubmit
    @GetMapping("industry")
    public Result<Set<String>> getIndustry(){
        Set<String> data = stockService.getIndustries();
        Result<Set<String>> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(data);
        return result;
    }

    @RepeatSubmit
    @GetMapping("industry/codes")
    public Result<Set<Object>> getCodeByIndustry(String industry){
        Set<Object> data = stockService.getTsCode(industry);
        Result<Set<Object>> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(data);
        return result;
    }

    @RepeatSubmit
    @GetMapping("codes")
    public Result<Set<String>> getCodes(){
        Set<String> data = stockService.getAllCodes();
        Result<Set<String>> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(data);
        return result;
    }

    @RepeatSubmit
    @GetMapping("options")
    public Result<List<OptionVO>> getOptions(String industry){
        List<OptionVO> data = stockService.getOptions();
        Result<List<OptionVO>> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(data);
        return result;
    }
}
