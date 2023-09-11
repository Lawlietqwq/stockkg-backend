package com.example.kg.service.impl;

import com.example.cache.DistributedCache;
import com.example.kg.common.CommonException;
import com.example.kg.dao.HolderRepository;
import com.example.kg.dao.RelationRepository;
import com.example.kg.dao.StockRepository;
import com.example.kg.entity.Holder;
import com.example.kg.entity.Stock;
import com.example.kg.entity.StockRelationship;
import com.example.kg.service.StockService;
import com.example.kg.util.BeanUtil;
import com.example.kg.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private HolderRepository holderRepository;

    private StockRepository stockRepository;

    private RelationRepository relationRepository;

    private final DistributedCache distributedCache;

    @Override
    @Transactional(rollbackFor = CommonException.class)
    public StockVO getStockVOByTsCode(String tsCode, int endDate) {
        StockVO stockVO = new StockVO();
        Stock stock = stockRepository.findByTsCode(tsCode, endDate);
        List<Holder> holders = stockRepository.findStartNodes(stock);
        stockVO.setStockNodeVO(BeanUtil.getStockNode(stock));
        stockVO.setHolders(BeanUtil.getHolderNodeList(holders));
        List<StockRelationship> list = relationRepository.getStockRelationships(stock.getId());
        stockVO.setRelationList(BeanUtil.getRelationList(list));
        return stockVO;
    }

    @Override
    public StockOneHopVO getOneHopStockVO(String tsCode, int endDate) {
        StockOneHopVO stockVO = new StockOneHopVO();
        Stock stock = stockRepository.findByTsCode(tsCode, endDate);
        List<Holder> holders = stockRepository.findStartNodes(stock);
        List<StockNodeVO> oneHopStocks = new ArrayList<>();
        List<StockRelationship> neiborRelations = relationRepository.getStockRelationships(stock.getId());
        List<RelationVO> oneHop = new ArrayList<>();
        for(Holder holder: holders){
            List<Stock> stocks = holderRepository.findEndNodes(holder.getName(), endDate);
            List<StockRelationship> list = relationRepository.getHolderRelationships(endDate, holder.getName());
            for(StockRelationship relation: list){
                if(relation.getTsCode().equals(stock.getTsCode())) continue;
                RelationVO relationVO = BeanUtil.getRelation(relation);
                oneHop.add(relationVO);
            }

            for(Stock stock1: stocks){
                if(stock1.getTsCode().equals(stock.getTsCode())) continue;
                StockNodeVO stockNodeVO = BeanUtil.getStockNode(stock1);
                oneHopStocks.add(stockNodeVO);
            }
        }
        stockVO.setStockNodeVO(BeanUtil.getStockNode(stock));
        stockVO.setHolders(BeanUtil.getHolderNodeList(holders));
        stockVO.setOneHopStocks(oneHopStocks);
        oneHop.addAll(BeanUtil.getRelationList(neiborRelations));
        stockVO.setRelationList(oneHop);
        return stockVO;
    }

    @Override
    public int getFirstDate(String tsCode) {
        return stockRepository.findFirstAnnDate(tsCode);
    }

    @Override
    public Set<String> getIndustries() {
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        return stringRedisTemplate.opsForSet().members("industry");
    }

    @Override
    public Set<Object> getTsCode(String industry) {
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        return stringRedisTemplate.opsForHash().keys(industry);
    }

    @Override
    public Set<String> getAllCodes() {
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        return stringRedisTemplate.opsForSet().members("codes");
    }

    @Override
    public List<OptionVO> getOptions() {
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        Set<String> industries = stringRedisTemplate.opsForSet().members("industry");
        List<OptionVO> optionVOS = new ArrayList<>();
        Iterator<String> iter = industries.iterator();
        while(iter.hasNext()){
            List<Map<String, String>> children = new ArrayList<>();
            OptionVO optionVO = new OptionVO();
            String industry = iter.next();
            Set<Object> codes = stringRedisTemplate.opsForHash().keys(industry);
            for(Object o: codes){
                Map<String, String> childMap = new HashMap<>();
                if (o instanceof String){
                    String code = (String) o;
                    childMap.put("value", code);
                    childMap.put("label", code);
                }
                children.add(childMap);
            }
            optionVO.setValue(industry);
            optionVO.setLabel(industry);
            optionVO.setChildren(children);
            optionVOS.add(optionVO);
        }
        return optionVOS;
    }
}
