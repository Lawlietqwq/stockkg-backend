package com.example.kg.entity.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.database.BaseDO;
import lombok.Data;

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

}
