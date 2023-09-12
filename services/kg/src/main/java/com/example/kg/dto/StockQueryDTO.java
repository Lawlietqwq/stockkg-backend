package com.example.kg.dto;

import lombok.Data;

/**
 * 根据tsCode——股票代码&endDate——查询日期查询请求入参
 */
@Data
public class StockQueryDTO {
    /**
     * 股票代码
     */
    private String tsCode;

    /**
     * 查询日期
     */
    private int endDate;
}
