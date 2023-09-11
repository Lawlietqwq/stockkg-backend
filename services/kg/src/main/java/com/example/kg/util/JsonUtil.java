package com.example.kg.util;

import com.example.kg.config.JacksonConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
@ConditionalOnClass(JacksonConfig.class)
public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static ObjectMapper objectMapper;

    public static ObjectMapper getObjectMapper(){
        return objectMapper;
    }


    //静态变量注入时，@Autowired注解只能在方法，不能在参数
    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper){
        JsonUtil.objectMapper = objectMapper;
    }

    /**
     * Object转json字符串
     */
    public static String objToJson(Object obj){
        try{
            if (obj == null){
                return null;
            }else if (obj instanceof String){
                return (String) obj;
            }else {
                return objectMapper.writeValueAsString(obj);
            }
        }catch (Exception e){
            logger.error("Parse object to String error", e);
            return null;
        }
    }

    /**
     * Object转json字符串并格式化美化
     */
    public static <T> String objToJsonPretty(T obj) {
        try {
            if (obj == null)
                return null;
            else if (obj instanceof String)
                return (String) obj;
            else
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            logger.error("Parse object to String Pretty error", e);
            return null;
        }
    }

    /**
     * json字符串转object
     */
    public static <T> T jsonToObj(String json, Class<T> clazz){
        try {
            if (StringUtils.isEmpty(json) || clazz == null){
                return null;
            } else if (clazz.equals(String.class)){
                return (T) json;
            } else {
                return objectMapper.readValue(json, clazz);
            }
        } catch (IOException e){
            logger.error("Parse String to bean error", e);
            return null;
        }
    }

    /**
     * json转集合
     */
    public static <T> T toBean(String json, TypeReference<T> typeReference){
        try {
            if (StringUtils.isEmpty(json) || typeReference == null){
                return null;
            } else if (typeReference.getType().equals(String.class)){
                return (T) json;
            } else {
                return objectMapper.readValue(json, typeReference);
            }
        } catch (IOException e) {
            logger.error("Parse String to Bean error", e);
            return null;
        }
    }

    /**
     * string转object 用于转为集合对象
     */
    public static <T> T toBean(String json, Class<?> collectionClass, Class<?>... elementClasses) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            logger.error("Parse String to Bean error", e);
            return null;
        }
    }

}

