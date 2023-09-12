package com.example.kg.entity.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.database.BaseDO;
import lombok.Data;

import java.util.List;


@Data
@TableName("kg_holder_mapping")
public class HolderMappingDO extends BaseDO {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 股东名称
     */
    private String name;

    /**
     * 股东节点被更新的时间
     */
    private Integer endDate;

}