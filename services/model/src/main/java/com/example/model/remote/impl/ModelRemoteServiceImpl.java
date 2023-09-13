package com.example.model.remote.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.example.model.remote.ModelRemoteService;
import com.example.model.remote.dto.ModelRemoteDTO;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Service
@Component
public class ModelRemoteServiceImpl implements ModelRemoteService {
    @Override
    public ModelRemoteDTO updateModel(Long id) {
        ModelRemoteDTO modelRemoteDTO = new ModelRemoteDTO();
        modelRemoteDTO.setId(id);
        try {
            Process proc = Runtime.getRuntime().exec("/home/wq/update_model.py");
            proc.waitFor();
        } catch (Exception e) {
            return modelRemoteDTO;
        }
        modelRemoteDTO.setUpdateTime(new Date());
        return modelRemoteDTO;
    }
}
