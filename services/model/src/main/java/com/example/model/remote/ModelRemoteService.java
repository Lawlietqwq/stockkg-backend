package com.example.model.remote;

import com.example.model.remote.dto.ModelRemoteDTO;
import org.springframework.web.bind.annotation.RequestParam;

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

