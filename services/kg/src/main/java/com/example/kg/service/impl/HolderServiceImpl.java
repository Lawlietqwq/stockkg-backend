package com.example.kg.service.impl;

import com.example.kg.common.CommonException;
import com.example.kg.dao.HolderRepository;
import com.example.kg.dao.RelationRepository;
import com.example.kg.dao.StockRepository;
import com.example.kg.entity.Holder;
import com.example.kg.entity.Stock;
import com.example.kg.service.HolderService;
import com.example.kg.util.BeanUtil;
import com.example.kg.vo.HolderVO;
import lombok.RequiredArgsConstructor;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HolderServiceImpl implements HolderService {
    private HolderRepository holderRepository;

    private StockRepository stockRepository;

    private RelationRepository relationRepository;

    private SessionFactory sessionFactory;

    @Transactional(rollbackFor = CommonException.class)
    @Override
    public HolderVO getHolderVOByName(String name, int endDate) {
        HolderVO holderVO = new HolderVO();
        Holder holder = holderRepository.findByName(name);
        List<Stock> stocks = holderRepository.findEndNodes(name, endDate);
        holderVO.setHolderNodeVO(BeanUtil.getHolderNode(holder));
        holderVO.setStocks(BeanUtil.getStockNodeList(stocks));
        holderVO.setRelationList(BeanUtil.getRelationList(relationRepository.getHolderRelationships(endDate, name)));
        return holderVO;
    }}
