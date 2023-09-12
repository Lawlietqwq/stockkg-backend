package com.example.common.exception;

import com.example.common.result.ResultTypeEnum;

public class CommonException extends RuntimeException{

    private ResultTypeEnum resultTypeEnum;

    public CommonException() {
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(ResultTypeEnum resultTypeEnum) {
        super(resultTypeEnum.getMessage());
        this.resultTypeEnum = resultTypeEnum;
    }

    public static void fail(String message){
        throw new CommonException(message);
    }

    public static void fail(ResultTypeEnum resultTypeEnum){
        throw new CommonException(resultTypeEnum);
    }


    public ResultTypeEnum getResultTypeEnum() {
        return resultTypeEnum;
    }

    public void setResultTypeEnum(ResultTypeEnum resultTypeEnum) {
        this.resultTypeEnum = resultTypeEnum;
    }
}
