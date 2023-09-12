package com.example.kg.entity.neo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.beans.factory.annotation.Value;

@Data
@Builder
@NodeEntity("Holder")
@AllArgsConstructor
@NoArgsConstructor
public class Holder {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Value("${constants.holderType}")
    private String type = "Holder";
//    @Relationship(type = "持有", direction = Relationship.OUTGOING)
//    private List<Stock> stocks;
}
