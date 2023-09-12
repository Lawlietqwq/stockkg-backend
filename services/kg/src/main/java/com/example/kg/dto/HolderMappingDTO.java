package com.example.kg.dto;

import lombok.Data;

import java.util.List;

/**
 * Key ->{股东名称:List（更新时间）}
 */
@Data
public class HolderMappingDTO {
    /**
     * 股东姓名/公司名
     */
    private String name;

    /**
     * 更新时间
     */
    private List<Integer> endDateList;
}