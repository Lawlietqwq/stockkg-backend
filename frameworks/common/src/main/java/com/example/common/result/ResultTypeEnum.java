package com.example.common.result;

import com.example.common.constant.Constants;

public enum ResultTypeEnum {
    SUCCESS(Constants.SUCCESS,"查询成功"),
    ERROR(Constants.ERROR,"查询失败"),
    UPDATE_SUCCESS(Constants.SUCCESS,"更新模型参数中"),
    UPDATE_ALREADY(Constants.SUCCESS,"模型参数已经最新"),
    UPDATE_ERROR(Constants.ERROR,"更新失败"),
    REPEAT_ERROR(Constants.ERROR,"请勿重复提交");

    private int code;
    private String message;

    ResultTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(int code) {
        for (ResultTypeEnum result : ResultTypeEnum.values()) {
            if (result.getCode() == code) {
                return result.message;
            }
        }
        return null;
    }

    /**
     * 获取结果类型代号
     *
     * @return code
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取结果类型描述
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return this.code + ": " + this.message;
    }

}
