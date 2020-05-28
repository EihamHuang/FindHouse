package com.findhouse.utils;

public class SpiltUtil {
    public String[] spiltSemicolon(String url) {
        String[] splitUrl = url.split("\\;");
        return splitUrl;
    }
    public int[] spiltInstallation(String url) {
        int length = url.length();
        int[] result = new int[length];
        for(int i=0; i<length; i++) {
            result[i] = url.charAt(i)-'0';
        }
        return result;
    }
}
