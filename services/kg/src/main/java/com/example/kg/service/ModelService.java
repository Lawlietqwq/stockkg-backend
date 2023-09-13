package com.example.kg.service;

import com.example.kg.dto.ModelRemoteDTO;
import com.example.kg.entity.mysql.ModelDO;

import java.util.List;
import java.util.Map;

public interface ModelService {

    public ModelRemoteDTO updateModel();

    public Map<Object, Object> getModelResult(String date);
}
