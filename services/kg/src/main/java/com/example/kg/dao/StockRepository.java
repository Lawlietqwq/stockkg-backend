package com.example.kg.dao;

import com.example.kg.entity.Holder;
import com.example.kg.entity.Stock;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface StockRepository extends Neo4jRepository<Stock,Long> {

//    @Query(value = "create (d:Dept {deptno:{0},dname:{1},location:{2}})")
//    void create(String tsCode, String dname, String location);

    @Query("MATCH (s:Stock) WHERE s.ts_code = $tsCode AND s.ann_date<=$endDate RETURN s ORDER BY s.ann_date DESC LIMIT 1")
    Stock findByTsCode(String tsCode, int endDate);

    @Query("MATCH (h:Holder)-[:持有]->(s:Stock) " +
            "WHERE id(s)=$stock " +
            "RETURN h")
    List<Holder> findStartNodes(Stock stock);
    Stock findByTsCodeAndAnnDate(String tsCode, Date annDate);

    @Query("MATCH (:Holder)-[:持有]->(s:Stock) " +
            "WHERE s.ts_code=$tsCode " +
            "RETURN s.ann_date ORDER BY s.ann_date LIMIT 1")
    int findFirstAnnDate(String tsCode);

}
