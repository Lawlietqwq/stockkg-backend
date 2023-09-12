package com.example.kg.entity.neo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.*;

@Data
@Builder
@RelationshipEntity("持有")
@AllArgsConstructor
@NoArgsConstructor
public class StockRelationship {
    @Id
    @GeneratedValue
    private Long id;
    @JsonProperty("from")
    private Long start;
    @JsonProperty("to")
    private Long end;
    @Property("hold_amount")
    private String holdAmount;
    @Property("hold_ratio")
    private String ratio;
    private int expires;
    @Property("ann_date")
    private int annDate;
    @Property("holder_name")
    private String holderName;
    @Property("ts_code")
    private String tsCode;
//    @Property("start")
//    private int from;
//    @Property("end")
//    private int to;
    @StartNode
    @JsonIgnore
    private Holder holder;
    @EndNode
    @JsonIgnore
    private Stock stock;
}
