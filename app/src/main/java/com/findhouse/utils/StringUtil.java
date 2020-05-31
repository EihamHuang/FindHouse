package com.findhouse.utils;

public class StringUtil {
    public final String[] installType = {"洗衣机", "空调", "衣柜","电视", "冰箱", "热水器","床", "暖气", "宽带", "天然气"};
    public final String[] houseType = {"二手房", "租房", "新房"};
    public final String[] priceType = {"万", "元/月", "万元/套"};

    public String[] spiltSemicolon(String url) {
        String[] splitUrl = url.split("\\;");
        return splitUrl;
    }

    public int spiltApartment(String s) {
        int num = 1;
        for(int i=0; i<s.length(); i++) {
            if(s.charAt(i)==';')
                num++;
        }
        return num;
    }

    public int[] spiltInstall(String url) {
        int length = url.length();
        int[] result = new int[length];
        for(int i=0; i<length; i++) {
            result[i] = url.charAt(i)-'0';
        }
        return result;
    }

    public String clearChinese(String s) {
        return s.replaceAll("[\u4e00-\u9fa5]+", "");
    }
}
