package com.findhouse.data;

import java.io.Serializable;

public class User implements Serializable {
    private String uid;  // 用户id
    private String name;  // 昵称
    private String pass;  // 密码
    private String tel;  // 电话
    private String head;  // 头像

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }
}
