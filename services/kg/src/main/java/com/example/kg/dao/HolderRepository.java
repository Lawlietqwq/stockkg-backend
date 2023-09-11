package com.example.kg.dao;

import com.example.kg.entity.Holder;
import com.example.kg.entity.Stock;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HolderRepository extends Neo4jRepository<Holder,Long> {

    @Query("MATCH (h:Holder) WHERE h.name = $name RETURN h")
//    @Query("MATCH (h:Holder)-[r:持有]->(s:Stock) " +
//            "WHERE h.name = $name " +
//            "RETURN {id:h.id, name:h.name, stocks:collect(s)}")
    Holder findByName(String name);


    @Query("MATCH (:Holder)-[r:持有]->(s:Stock) " +
            "WHERE r.holder_name=$name AND r.ann_date<=$endDate AND r.expires>$endDate " +
            "RETURN s")
    List<Stock> findEndNodes(String name, int endDate);


    List<Holder> findByNameOrderByNameAsc(String name);
}
