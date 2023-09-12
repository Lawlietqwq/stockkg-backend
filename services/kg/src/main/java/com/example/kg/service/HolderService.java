package com.example.kg.service;

import com.example.kg.dto.HolderQueryDTO;
import com.example.kg.vo.HolderVO;

public interface HolderService {
    HolderVO getHolderVOByName(HolderQueryDTO requestParam);
}
