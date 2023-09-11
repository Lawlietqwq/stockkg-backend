package com.example.kg.dao;

import com.example.kg.entity.StockRelationship;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface RelationRepository extends Neo4jRepository<StockRelationship,Long> {
//    @Query("MATCH p=(:Holder)-[r:持有]->(s:Stock) " +
//            "WHERE s.ann_date<=$end_date AND s.ts_code=$tsCode " +
//            "RETURN p")
//    List<Edge> getStockRelationships(@Param("end_date") int endDate, String tsCode);

    @Query("MATCH (h:Holder)-[r:持有]->(s:Stock) " +
            "WHERE id(s)=$id " +
            "RETURN h,r,s")
    List<StockRelationship> getStockRelationships(Long id);


    @Query("MATCH (h:Holder)-[r:持有]->(s:Stock) " +
            "WHERE r.holder_name=$name AND r.ann_date<=$endDate AND r.expires>$endDate " +
            "RETURN h,r,s")
    List<StockRelationship> getHolderRelationships(int endDate, String name);

//    @Query("MATCH (s:Stock)<-[r1:持有]-(h:Holder)-[r2:持有]->(s:Stock) " +
//            "WHERE id(s)=$id AND r1.ann_date<=$endDate AND r1.expires>$endDate " +
//            "RETURN s,r1,h,r2,s")
//    List<StockRelationship> getOneHopGraphRelation(Long id);
}
