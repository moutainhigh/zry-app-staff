package com.zhongmei.bty.commonmodule.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    public static final String TIME_FORMAT = "HH:mm";

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public static final String TIME_FORMAT_HHMMdd = "HH:mm:ss";


    public static String format(long date, String format) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(new Date(date));
    }

    public static String format(long date) {
        return format(date, "yyyy-MM-dd HH:mm");
    }

    public static String fomatDayTime(long date) {
        return format(date, "MM-dd HH:mm");
    }

    /**
     * 将毫秒转换成时间字符串.  注意...不是格式化时间.
     *
     * @param millis
     * @return
     */
    public static String exchangeMillisToTime(long millis) {
        if (millis < 60 * 60 * 1000) {
            //小于一个小时的只返回分秒
            return format(millis, "mm:ss");
        } else {
            //大于一个小时的话加上16小时的时间,  因为时间戳是从 1970-1-1 8:00 算起的.... 直接format会不正确
            millis += 16 * 60 * 60 * 1000;
            return format(millis, "HH:mm:ss");
        }
    }

    /**
     * @param date
     * @return yyyy-MM-dd
     */
    public static String formatDate(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT,
                Locale.getDefault());
        return sdf.format(new Date(date));
    }

    /*
     * @return HH:mm
     */
    public static String getHHmm(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT,
                Locale.getDefault());
        return sdf.format(time);
    }


    public static long getCurrentTimeMillis() {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        return cal.getTime().getTime();
    }


    public static String getCalllogTime(long time) {

        long currentTime = getCurrentTimeMillis();

        String calllogDate = formatDate(time);

        String currentDate = formatDate(currentTime);

        if (calllogDate.equals(currentDate)) {

            return getHHmm(time);

        } else {

            return formatDate(time);
        }

    }


    /*
     *获取天数转换的毫秒总时间
     *@param 天数
     * */
    public static long getDayConvertMillisTotal(int daytime) {
        return daytime * 24 * 60 * 60 * 1000;
    }


    public static String getSecond(long time) {

        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT_HHMMdd,
                Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        return sdf.format(time);

    }
}
