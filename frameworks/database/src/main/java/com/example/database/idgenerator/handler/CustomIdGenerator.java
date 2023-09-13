package com.example.database.idgenerator.handler;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.example.database.idgenerator.tools.SnowflakeIdUtil;

/**
 * 自定义雪花算法生成器
 *

 */
public class CustomIdGenerator implements IdentifierGenerator {

    @Override
    public Number nextId(Object entity) {
        return SnowflakeIdUtil.nextId();
    }
}

