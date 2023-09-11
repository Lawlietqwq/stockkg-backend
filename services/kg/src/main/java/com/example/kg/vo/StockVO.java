package com.example.kg.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockVO implements Serializable {
    private StockNodeVO stockNodeVO;
    private List<HolderNodeVO> holders;
    @JsonProperty("lines")
    private List<RelationVO> relationList;
}
