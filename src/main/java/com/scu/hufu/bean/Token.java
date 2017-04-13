package com.scu.hufu.bean;

import javax.persistence.Entity;

/**
 * Created by tianfei on 2017/4/6.
 */
public class Token {

    private String token;

    public Token(){}

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
