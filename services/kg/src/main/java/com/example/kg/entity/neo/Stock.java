package com.example.kg.entity.neo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.springframework.beans.factory.annotation.Value;

@Data
@Builder
@NodeEntity("Stock")
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    @Id
    @GeneratedValue
    private Long id;
    @JsonProperty("text")
    private String name;
    @Property("ts_code")
    private String tsCode;
    private String industry;
    @Property("ann_date")
    private int annDate;
    @Value("${constants.stockType}")
    private String type = "Stock";
//    @Relationship(type = "持有", direction = Relationship.INCOMING)
//    private List<Holder> holders;
}
