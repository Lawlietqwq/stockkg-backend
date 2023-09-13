package com.example.kg.remote;

import com.example.kg.dto.ModelRemoteDTO;

/**
 * 模型远程调用服务
 *
 */
public interface ModelRemoteService {

    /**
     * 更新参数
     */
    ModelRemoteDTO updateModel(Long id);
}
