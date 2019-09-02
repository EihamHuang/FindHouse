package com.huangyihang.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * - @Description:
 * - @Author:  huangyihang
 * - @Time:  2019-08-15 18:23
 */
public class JsonData<T> {

    private List<T> data;
    private int code;
    private String msg;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
