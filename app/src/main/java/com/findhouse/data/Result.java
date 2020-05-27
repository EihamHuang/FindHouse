package com.findhouse.data;

import java.util.List;

/**
 * - @Description:
 * - @Author:  huangyihang
 * - @Time:  2019/9/7 23:36
 */
public class Result<T> {
    private String stat;
    private List<T> data;

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
