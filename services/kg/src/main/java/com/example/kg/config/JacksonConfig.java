package com.example.kg.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Configuration
public class JacksonConfig {

    //线程安全，单例节省new对象时间加速
    @Bean("objectMapper")
    // 避免Bean同时注入
    // @Primary 赋予ObjectMapper单例Bean最高优先级
    @Primary
    // 如果注册相同类型的bean，出现异常
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper getObjectMapper(Jackson2ObjectMapperBuilder builder){
        ObjectMapper mapper = builder.build();

        // 日期格式
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        //GMT+8:东八区
        mapper.setTimeZone(TimeZone.getTimeZone("GMT-8"));

        //VO对象有属性为null时不序列化，不包括""
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        //允许字段名没有引号（可以进一步减小json体积）：
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        //允许单引号：
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        // 允许出现特殊字符和转义符
        //mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);这个已经过时。
        mapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);

        //允许C和C++样式注释：
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);


        //序列化结果格式化
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        //枚举输出成字符串
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        //空对象不要抛出异常
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        //Date、Calendar等序列化为时间格式的字符串(如果不执行以下设置，就会序列化成时间戳格式)
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        //反序列化时，遇到未知属性不要抛出异常
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        //反序列化时，遇到忽略属性不要抛出异常
        mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);

        //反序列化时，空字符串对于的实例属性为null
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        return mapper;
    }
}
