package com.findhouse.data;

import java.util.List;

/**
 * - @Description:
 * - @Author:  huangyihang
 * - @Time:  2019-08-15 18:23
 */
public class JsonData<T> {

    private List<T> data;
    private String stat;
    private String msg;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
