package com.scu.hufu.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by tianfei on 2017/4/10.
 */
@Entity
public class AppKeyValue {

    @Id

    private String appKey;

    private String appSecret;

    public AppKeyValue() {}

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
