package com.example.kg.service;

import com.example.kg.dto.StockQueryDTO;
import com.example.kg.vo.OptionVO;
import com.example.kg.vo.StockOneHopVO;
import com.example.kg.vo.StockVO;

import java.util.List;
import java.util.Set;

public interface StockService {
    StockVO getStockVOByTsCode(StockQueryDTO requestParam);

    StockOneHopVO getOneHopStockVO(StockQueryDTO requestParam);

    int getFirstDate(String tsCode);

    Set<String> getIndustries();

    Set<Object>  getTsCode(String industry);

    List<OptionVO> getOptions();

    Set<String> getAllCodes();
}
