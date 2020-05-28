package com.findhouse.utils;

public class SpiltUtil {
    public String[] spiltSemicolon(String url) {
        String[] splitUrl = url.split("\\;");
        return splitUrl;
    }
}
