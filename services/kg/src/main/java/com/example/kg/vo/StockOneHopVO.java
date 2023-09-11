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
public class StockOneHopVO implements Serializable {
    private StockNodeVO stockNodeVO;
    private List<HolderNodeVO> holders;
    private List<StockNodeVO> oneHopStocks;
    @JsonProperty("lines")
    private List<RelationVO> relationList;
}
