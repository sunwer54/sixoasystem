package com.oa.pojo;

import java.io.Serializable;

public class TUser implements Serializable {
    private String uid;
    private String password;
    private String realname;

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "TUser{" +
                "uid='" + uid + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
