package com.example.kg.util;

import com.example.kg.entity.neo.Holder;
import com.example.kg.entity.neo.Stock;
import com.example.kg.entity.neo.StockRelationship;
import com.example.kg.vo.HolderNodeVO;
import com.example.kg.vo.RelationVO;
import com.example.kg.vo.StockNodeVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class POJOUtil {
    public static StockNodeVO getStockNode(Stock stock) {
        StockNodeVO stockNodeVO = new StockNodeVO();
        stockNodeVO.setId(String.valueOf(stock.getId()));
        stockNodeVO.setText(stock.getName());
        HashMap<String, Object> data = new HashMap<>();
        data.put("ts_code", stock.getTsCode());
        data.put("type", stock.getType());
        data.put("industry", stock.getIndustry());
        data.put("ann_date", stock.getAnnDate());
        stockNodeVO.setData(data);
        return stockNodeVO;
    }

    public static HolderNodeVO getHolderNode(Holder holder) {
        HolderNodeVO holderNodeVO = new HolderNodeVO();
        holderNodeVO.setId(String.valueOf(holder.getId()));
        holderNodeVO.setText(holder.getName());
        HashMap<String, Object> data = new HashMap<>();
        data.put("type", holder.getType());
        holderNodeVO.setData(data);
        return holderNodeVO;
    }

    public static RelationVO getRelation(StockRelationship stockRelationship){
        RelationVO relationVO = new RelationVO();
        relationVO.setId(String.valueOf(stockRelationship.getId()));
        relationVO.setFrom(String.valueOf(stockRelationship.getHolder().getId()));
        relationVO.setTo(String.valueOf(stockRelationship.getStock().getId()));
        relationVO.setText(stockRelationship.getRatio());
        HashMap<String, Object> data = new HashMap<>();
        data.put("ts_code", stockRelationship.getTsCode());
        data.put("holder_name", stockRelationship.getHolderName());
        data.put("hold_amount", stockRelationship.getHoldAmount());
        data.put("hold_ratio",stockRelationship.getRatio());
        data.put("ann_date", stockRelationship.getAnnDate());
        data.put("expires", stockRelationship.getExpires());
        relationVO.setData(data);
        return relationVO;
    }

    public static List<HolderNodeVO> getHolderNodeList(List<Holder> holders){
        List<HolderNodeVO> list = new ArrayList<>();
        for(Holder holder: holders){
            list.add(getHolderNode(holder));
        }
        return list;
    }

    public static List<StockNodeVO> getStockNodeList(List<Stock> stocks){
        List<StockNodeVO> list = new ArrayList<>();
        for(Stock stock: stocks){
            list.add(getStockNode(stock));
        }
        return list;
    }

    public static List<RelationVO> getRelationList(List<StockRelationship> stockRelationshipList){
        List<RelationVO> list = new ArrayList<>();
        for(StockRelationship stockRelationship: stockRelationshipList){
            list.add(POJOUtil.getRelation(stockRelationship));
        }
        return list;
    }
}
