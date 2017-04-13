package com.scu.hufu.enums;

/**
 * Created by tianfei on 2017/4/7.
 */

public enum ResponseEnum{

    //由抛出者自定义的错误或者系统错误：如空指针,包括创建Token失败时抛出的UnsupportedEncodingException
    UNKONW_ERROR(-1, "未知错误"),


    SUCCESS(200, "成功"),

    //注册
    REGISTER_ERR(301,"注册数据不完整"),
    USER_EXISTED(302,"用户已存在"),

    //登录相关
    USER_NOT_FOUND(401, "用户不存在"),
    PASSWORD_ERROR(402,"密码错误"),
    TOKEN_INVALID(403,"Token不合法"),
    MISS_TOKEN(404,"Token缺失"),
    OVER_TIME_TOKEN(405,"Token过时"),

    //API安全相关
    BAD_REQUEST(501, "请求头错误"),
    OVER_TIME_REQUEST(502,"过时的请求"),
    SAME_NONCE(503,"禁止重放"),
    UNKNOWN_CONTENT_TYPE(504,"格式错误"),
    WRONG_CHECKSUM(505,"CheckSum错误"),
    ONLY_HTTP_ALLOWED(506,"请使用HTTP"),
    BAD_URL_FORMAT(507,"不合法的URL"),
    MISS_REQUIRED_PARAM(508,"参数缺失"),
    ONLY_POST_SUPPORTED(509,"只支持POST请求"),


    //FILE_NOT_FOUND(404,"请求的文件不存在"),
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