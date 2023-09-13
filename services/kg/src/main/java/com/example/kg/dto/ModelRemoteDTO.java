package com.example.kg.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ModelRemoteDTO implements Serializable {
    /**
     * 业务消息ID
     */
    private Long id;

    /**
     * 更新时间
     */
    private Date updateTime;
}