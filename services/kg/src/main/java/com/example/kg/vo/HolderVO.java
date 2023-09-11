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
public class HolderVO implements Serializable {
    private HolderNodeVO holderNodeVO;
    private List<StockNodeVO> stocks;
    @JsonProperty("lines")
    private List<RelationVO> relationList;
}
