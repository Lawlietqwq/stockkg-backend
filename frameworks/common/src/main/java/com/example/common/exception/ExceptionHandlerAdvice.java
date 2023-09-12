package com.example.common.exception;

import com.example.common.result.Result;
import com.example.common.result.ResultTypeEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class ExceptionHandlerAdvice {
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        if(e instanceof CommonException){
            CommonException commonException = (CommonException)e;
            commonException.printStackTrace();
            return new Result<>(commonException.getResultTypeEnum());
        }else {
            e.printStackTrace();
            return new Result<>(ResultTypeEnum.ERROR);
        }

    }
    @ExceptionHandler(CommonException.class)
    public Result<?> handleTipException(CommonException e) {

        return new Result<>(e.getResultTypeEnum());
    }
}

