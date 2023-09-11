package com.example.kg.service;

import com.example.kg.vo.HolderVO;

public interface HolderService {
    HolderVO getHolderVOByName(String name, int endDate);
}
