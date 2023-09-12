package com.example.kg.dto;

import lombok.Data;

/**
 * name——股东姓名&endDate——查询日期查询请求入参
 */
@Data
public class HolderQueryDTO {
    /**
     * 股东姓名/公司名
     */
    private String name;

    /**
     * 查询日期
     */
    private int endDate;
}