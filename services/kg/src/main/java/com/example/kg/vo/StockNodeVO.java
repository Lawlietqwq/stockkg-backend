package com.example.kg.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockNodeVO {
    private String id;
    private String text;
    private HashMap<String, Object> data;
}