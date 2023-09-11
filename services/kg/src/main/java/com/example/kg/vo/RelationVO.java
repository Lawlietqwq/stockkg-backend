package com.example.kg.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationVO {
    private String from;
    private String to;
    private String id;
    private String text;
//    private int lineShape = 5;
    private HashMap<String, Object> data;
}
