package com.zhongmei.beauty.booking.util;

import android.content.Context;

import com.zhongmei.yunfu.beauty.R;

import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class BeautyDateTool {

    private static final int YESTERDY = -1;
    private static final int TODAY = 0;
    private static final int TOMORROWDAT = 1;
    private static final int AFTER = 2;
    private static final int OTHER_DAY = 10000;

    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();


    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }


    public static String getTitleDay(Context context, String day) {
        try {
            switch (JudgmentDay(day)) {
                case YESTERDY: {
                    return context.getResources().getString(R.string.beauty_yesterdy);
                }
                case TODAY: {
                    return context.getResources().getString(R.string.beauty_today);
                }
                case TOMORROWDAT: {
                    return context.getResources().getString(R.string.beauty_tomorrow);
                }
                default:
                    return day;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static int JudgmentDay(String day) throws ParseException {
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            switch (diffDay) {
                case YESTERDY: {
                    return YESTERDY;
                }
                case TODAY: {
                    return TODAY;
                }
                case TOMORROWDAT: {
                    return TOMORROWDAT;
                }
                case AFTER:
                    return AFTER;
            }
        }
        return OTHER_DAY;
    }


    public static boolean IsYesterday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == YESTERDY) {
                return true;
            }
        }
        return false;
    }


    public static boolean IsToday(String day) throws ParseException {
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == TODAY) {
                return true;
            }
        }
        return false;
    }


    public static boolean IsTomorrowday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == TOMORROWDAT) {
                return true;
            }
        }
        return false;
    }

}