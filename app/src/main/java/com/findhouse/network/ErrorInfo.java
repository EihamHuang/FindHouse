package com.findhouse.network;

import androidx.annotation.Nullable;

import okhttp3.ResponseBody;

/**
 * - @Description:  错误类
 * - @Author:  huangyihang
 * - @Time:  2019-08-14 15:10
 */
public class ErrorInfo extends Exception {
    private int code;
    private String message;
    private ResponseBody body;

    public ErrorInfo(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Nullable
    public ResponseBody getBody() {
        return body;
    }

    @Override
    public String toString(){
        return "ErrorInfo{" +
                "code=" + code +
                ", message='" + getMessage() + '\'' +
                ", body=" + body +
                '}';
    }

}
