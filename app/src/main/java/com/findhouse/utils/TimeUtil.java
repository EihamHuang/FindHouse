package com.findhouse.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    public String dateToStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public Date strToDate(String str) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date date = formatter.parse(str, pos);
        return date;
    }

    /**
     * 计算时间差
     * @param starTime 开始时间
     * @param endTime  结束时间
     * @param
     * @return 返回时间差
     */
    // 返回天数
    public int getTimeDifference(String starTime, String endTime) {
        int time = 0;
        if(starTime == null || endTime == null) {
            return time;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endTime);
            long diff = parse1.getTime() - parse.getTime();
            long day = diff / (24 * 60 * 60 * 1000);
            time = (int)day;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
    // 返回月份
    public int getMonth(int day) {
        int month = 0;
        double a = day;
        if (day > 0) {
            month = (int)Math.ceil(a/30);
        }
        return month;
    }

}
