package com.example.kg.entity.neo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Property;
import org.springframework.data.neo4j.annotation.QueryResult;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@QueryResult
public class Edge {
    private Long identity;
    private Long start;
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
//    private double holdAmount;
//    private double ratio;
//    private int expires;
//    private int annDate;
//    private String holderName;
//    private String tsCode;
}
