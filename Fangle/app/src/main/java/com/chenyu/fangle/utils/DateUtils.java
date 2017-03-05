package com.chenyu.fangle.utils;


import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类
 */
public class DateUtils {

    private static final DateFormat format = new SimpleDateFormat("yyyyMMdd");
    private static final Calendar calender = new GregorianCalendar();

    public static String getDate(){
        return format.format(new Date());
    }

    public static String getPreviousDate(int diff){
        calender.setTime(new Date());
        calender.add(calender.DATE, -diff);
        return format.format(calender.getTime());
    }

    public static String getDateHeader(String dateStr){
        try {
            if (dateStr.equals(getDate())){
                return "今日要闻";
            }
            Date date = format.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            //Log.d("cylog","year:"+calendar.get(Calendar.YEAR)+",month:"+calendar.get(Calendar.MONTH)+",day:"+calendar.get(Calendar.DAY_OF_MONTH)+"week:"+calendar.get(Calendar.DAY_OF_WEEK));
            StringBuffer str = new StringBuffer();
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int weekday = calendar.get(Calendar.DAY_OF_WEEK);
            if (month >= 10){
                str.append(month+1);
            }else {
                str.append("0"+(month+1));
            }
            str.append("月");
            if (day >= 10){
                str.append(day);
            }else {
                str.append("0"+day);
            }
            str.append("日 星期");
            str.append(toCH(weekday-1));
            Log.d("cylog", String.valueOf(str));
            return String.valueOf(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String toCH(int day){
        String str = "";
        switch (day){
            case 1:
                str = "一";
                break;

            case 2:
                str = "二";
                break;

            case 3:
                str = "三";
                break;

            case 4:
                str = "四";
                break;

            case 5:
                str = "五";
                break;

            case 6:
                str = "六";
                break;

            case 0:
                str = "天";
                break;
        }
        return str;
    }

}
