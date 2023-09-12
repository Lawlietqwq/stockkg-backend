package com.example.kg.controller;


import com.example.common.result.Result;
import com.example.common.result.ResultTypeEnum;
import com.example.kg.dto.StockQueryDTO;
import com.example.kg.service.StockService;
import com.example.kg.vo.OptionVO;
import com.example.kg.vo.StockOneHopVO;
import com.example.kg.vo.StockVO;
import com.example.web.annotations.RepeatSubmit;
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

    /**
     * 根据股票代码和更新日期进行查询股票节点和相连股东节点
     * @param stockQueryDTO
     * @return
     */

    @RepeatSubmit
    @GetMapping("")
    public Result<StockVO> getStockMap(StockQueryDTO stockQueryDTO){
        StockVO stockVO = stockService.getStockVOByTsCode(stockQueryDTO);
        Result<StockVO> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(stockVO);
        return result;
    }

    /**
     * 根据股票代码和更新日期进行查询股票节点和相连股东节点以及股东节点的一跳邻居
     * @param stockQueryDTO
     * @return
     */
    @RepeatSubmit
    @GetMapping("oneHop")
    public Result<StockOneHopVO> getOneHopStockMap(StockQueryDTO stockQueryDTO){
        StockOneHopVO stockVO = stockService.getOneHopStockVO(stockQueryDTO);
        Result<StockOneHopVO> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(stockVO);
        return result;
    }

    /**
     * 查询股票节点的第一次更新时间
     * @param tsCode
     * @return
     */
    @RepeatSubmit
    @GetMapping("annDate")
    public Result<Integer> getFisrtAnnDate(@RequestParam("ts_code") String tsCode){
        Integer date = stockService.getFirstDate(tsCode);
        Result<Integer> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(date);
        return result;
    }

    /**
     * 查询行业信息
     * @return
     */

    @RepeatSubmit
    @GetMapping("industry")
    public Result<Set<String>> getIndustry(){
        Set<String> data = stockService.getIndustries();
        Result<Set<String>> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(data);
        return result;
    }

    /**
     * 查询股票所属行业
     * @param industry
     * @return
     */
    @RepeatSubmit
    @GetMapping("industry/codes")
    public Result<Set<Object>> getCodeByIndustry(String industry){
        Set<Object> data = stockService.getTsCode(industry);
        Result<Set<Object>> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(data);
        return result;
    }

    /**
     * 查询所有股票代码
     * @return
     */
    @RepeatSubmit
    @GetMapping("codes")
    public Result<Set<String>> getCodes(){
        Set<String> data = stockService.getAllCodes();
        Result<Set<String>> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(data);
        return result;
    }

    /**
     * 返回前端多级页表结构支持多行业查询
     * @return
     */
    @RepeatSubmit
    @GetMapping("options")
    public Result<List<OptionVO>> getOptions(){
        List<OptionVO> data = stockService.getOptions();
        Result<List<OptionVO>> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(data);
        return result;
    }
}
