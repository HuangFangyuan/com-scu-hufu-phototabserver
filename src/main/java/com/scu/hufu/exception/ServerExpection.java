package com.scu.hufu.exception;

import com.scu.hufu.enums.ResponseEnum;

/**
 * Created by tianfei on 2017/4/7.
 */
public class ServerExpection extends RuntimeException {

    private Integer code;

    //只提供message的构造方法，返回-1状态码
    public ServerExpection(String message){
        super(message);
        this.code=-1;
    }


    public ServerExpection(ResponseEnum mEnum) {
        super(mEnum.getMsg());
        this.code = mEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
