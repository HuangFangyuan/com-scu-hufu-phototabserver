package com.scu.hufu.bean;

import javax.persistence.*;
import javax.validation.constraints.Null;

/**
 * Created by tianfei on 2017/4/6.
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    @Column(unique = true,nullable = false)
    private String email;

    private String signature;
    private String headerUrl;

    @Column(nullable = false)
    private String passwordMD5;




    public User(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPasswordMD5() {
        return passwordMD5;
    }

    public void setPasswordMD5(String passwordMD5) {
        this.passwordMD5 = passwordMD5;
    }
}
