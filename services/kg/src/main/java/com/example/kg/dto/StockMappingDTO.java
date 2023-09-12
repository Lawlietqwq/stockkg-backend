package com.example.kg.dto;

import lombok.Data;

import java.util.List;


/**
 * Key ->{股票代码:List（更新时间）}
 */
@Data
public class StockMappingDTO {
    /**
     * 股票代码
     */
    private String tsCode;

    /**
     * 更新时间
     */
    private List<Integer> endDateList;
}
