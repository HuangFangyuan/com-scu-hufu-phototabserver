package com.scu.hufu.enums;

/**
 * Created by tianfei on 2017/4/7.
 */

public enum ResponseEnum{
    UNKONW_ERROR(-1, "未知错误,由抛出者自定义的错误或者系统错误：如空指针"),
    SUCCESS(200, "成功"),

    //登录相关
    USER_NOT_FOUND(401, "用户不存在"),
    BAD_REQUEST(402, "请求头错误"),
    PASSWORD_ERROR(403,"密码错误"),
    TOKEN_INVALID(405,"Token不合法"),
    OVER_TIME_REQUEST(406,"过时的请求"),
    SAME_NONCE(407,"禁止重放"),

    FILE_NOT_FOUND(404,"请求的文件不存在"),
    ;

    private Integer code;

    private String msg;

    ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}