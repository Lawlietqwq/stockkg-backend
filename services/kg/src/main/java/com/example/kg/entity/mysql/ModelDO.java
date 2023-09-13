package com.example.kg.entity.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.database.BaseDO;
import lombok.Data;

import java.util.Date;

@Data
@TableName("kg_model")
public class ModelDO extends BaseDO {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 更新模型的服务器id
     */
    private int serverId;

    /**
     * 模型id
     */
    private int modelId;

    /**
     * 行业
     */
    private String industry;

    /**
     * 排名
     */
    private int rank;

    /**
     * 交易日期
     */
    private Date tradingDate;
}
