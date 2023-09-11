package com.example.kg.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionVO {
    private String value;
    private String label;
    private List<Map<String, String>> children;
}
