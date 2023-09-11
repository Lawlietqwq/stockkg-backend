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
public class HolderNodeVO {
    private String id;
    private String text;
    private int nodeShape = 1;
    private String color = "#43a2f1";
    private HashMap<String, Object> data;
}
