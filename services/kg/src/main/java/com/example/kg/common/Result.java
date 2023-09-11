package com.example.kg.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

/**
 * 后台返回结果
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {
    //消息编号
    private int code;
    //消息
    @JsonProperty("msg")
    private String message;
    //数据
    private T data;

    public Result(ResultTypeEnum result) {
        this.setCode(result.getCode());
        this.setMessage(result.getMessage());
    }

    public Result(int code) {
        this.setCode(code);
        this.setMessage(ResultTypeEnum.getMessage(code));
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result() {

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error() {
        return new Result<>(ResultTypeEnum.ERROR);
    }

    public static <T> Result<T> success() {
        return new Result<>(ResultTypeEnum.SUCCESS);
    }

    public static void main(String[] args) throws JsonProcessingException {
        String json = new ObjectMapper().writeValueAsString(new Result<>(ResultTypeEnum.ERROR));
    }

}

