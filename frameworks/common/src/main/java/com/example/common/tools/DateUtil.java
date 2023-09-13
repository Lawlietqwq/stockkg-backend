package com.example.common.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class DateUtil {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static boolean dateDiff(Date d1, Date d2, long diff){
        long time1 = d1.getTime();
        long time2 = d2.getTime();
        return (time2 - time1)/(60*60*1000*24)>=diff;
    }

    public static Date strToDate(String date) throws ParseException {
        return formatter.parse(date);
    }

    public static String dateToStr(Date date){
        return formatter.format(date);
    }
}
