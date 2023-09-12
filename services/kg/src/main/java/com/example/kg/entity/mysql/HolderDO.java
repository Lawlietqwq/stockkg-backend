package com.example.kg.entity.mysql;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.database.BaseDO;
import lombok.Data;

@Data
@TableName("kg_holder")
public class HolderDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 股东姓名
     */
    private String name;

}
