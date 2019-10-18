package com.zhongmei.yunfu.context.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zhongmei.yunfu.context.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String QUERY_DATE_FORMAT = "yyyy-MM-dd";

    public static final String DEFAULT_WEEK_FORMAT = "E";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String TIME_FORMAT = "HH:mm";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    public static final String DATE_TIME_FORMAT3 = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_TIME_FORMAT4 = "yyyy.MM.dd HH:mm:ss";

    public static final String DATE_TIME = "MM-dd HH:mm";

    public static final String DATE_TIME_FORMAT2 = "yyyy/MM/dd HH:mm";
    public static final String DATE_TIME_YYYY_MM_DD_HH_MM = "yyyy/MM/dd HH:mm";

    private static ThreadLocal<SimpleDateFormat> mYMDHMDateTime = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
        }

    };

    private static ThreadLocal<SimpleDateFormat> mYMDDateTime = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
        }

    };

    private static ThreadLocal<SimpleDateFormat> mMDHMDateTime = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_TIME, Locale.getDefault());
        }

    };


    public static String getCurrentDateTime() {
        return mYMDHMDateTime.get().format(System.currentTimeMillis());
    }

    public static String getCurrentDateTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(System.currentTimeMillis());
    }

    public static String formatDateTime(Long timeInMills, String format) {
        if (timeInMills == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(timeInMills);
    }


    public static String formatDateTime(long timemillis) {
        return mYMDHMDateTime.get().format(timemillis);
    }

    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
        String dateString = sdf.format(date);
        return dateString;
    }

    public static Date timeStr2Date(String time) {
        SimpleDateFormat dfs = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
        Date date;
        try {
            date = dfs.parse(time);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }


    public static String formatDateSlash(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_YYYY_MM_DD_HH_MM, Locale.getDefault());
        String dateString = sdf.format(date);
        return dateString;
    }


    public static long formatDate(String selectedDate) {
        SimpleDateFormat sdf = mYMDDateTime.get();
        try {
            Date date = sdf.parse(selectedDate);
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.setTime(date);
            return cal.getTimeInMillis();
        } catch (ParseException e) {
        }
        return 0;
    }


    public static long formatDate(String selectedDate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(selectedDate);
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.setTime(date);
            return cal.getTimeInMillis();
        } catch (ParseException e) {
        }
        return 0;
    }


    public static String formatDate(Long date) {
        return formatDate(date, DEFAULT_DATE_FORMAT);
    }

    public static String formatDate(Long date, String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date(date));
    }


    public static int formatWeek(long date) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(new Date(date));
        return cal.get(Calendar.DAY_OF_WEEK);
    }


    public static String getDisplayDate(long initTime, int step, int base) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(new Date(initTime));
        cal.add(Calendar.DATE, step + base);
        Date time = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
        return sdf.format(time);
    }

    public static Date getDefaultDate(long initTime, int step, int base) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(new Date(initTime));
        cal.add(Calendar.DATE, step + base);
        return cal.getTime();
    }

        public static int getCurrentDayOfWeek() {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        return cal.get(Calendar.DAY_OF_WEEK);
    }

        public static int getCurrentDayOfWeekName() {
        int weekDayName = -1;
        int weekDay = getCurrentDayOfWeek();

        switch (weekDay) {
            case 1:
                weekDayName = 7;
                break;

            default:
                weekDayName = weekDay - 1;
                break;
        }
        return weekDayName;
    }


    public static String getDisplayWeek(long initTime, int step, int base) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(new Date(initTime));
        cal.add(Calendar.DATE, step + base);
        Date timeTmp = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_WEEK_FORMAT, Locale.getDefault());
        return sdf.format(timeTmp);
    }

    public static String getDisplayWeek(Date initTime, int step, int base) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(initTime);
        cal.add(Calendar.DATE, step + base);
        Date timeTmp = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_WEEK_FORMAT, Locale.getDefault());
        return sdf.format(timeTmp);
    }


    public static String getDisplayWeek(String date) {
        try {
            String[] dates = date.split("-");
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.set(Integer.valueOf(dates[0]), Integer.valueOf(dates[1]) - 1, Integer.valueOf(dates[2]));
            SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_WEEK_FORMAT, Locale.getDefault());
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDisplayTime(long time) {

        long currentTime = getCurrentTimeMillis();

        String calllogDate = formatDate(time);

        String currentDate = formatDate(currentTime);

        if (calllogDate.equals(currentDate)) {

            return getHHmm(time);

        } else {

            return formatDateTime(time);
        }

    }


    public static Date formatStringToDate(String date) {
        try {
            String[] dates = date.split("-");
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.set(Integer.valueOf(dates[0]), Integer.valueOf(dates[1]) - 1, Integer.valueOf(dates[2]));
            return cal.getTime();
        } catch (Exception e) {
            return null;
        }
    }


    public static String getDisplayDate(String date, int step) {
        try {
            String[] dates = date.split("-");
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.set(Integer.valueOf(dates[0]), Integer.valueOf(dates[1]) - 1, Integer.valueOf(dates[2]));
            cal.add(Calendar.DATE, step);
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            return "";
        }
    }


    public static String getDisplayHour(String hour, int step) {
        try {
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hour));
            cal.add(Calendar.HOUR_OF_DAY, step);
            SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.getDefault());
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            return "";
        }
    }


    public static String getDisplayMinute(String minute, int step) {
        try {
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.set(Calendar.MINUTE, Integer.valueOf(minute));
            cal.add(Calendar.MINUTE, step);
            SimpleDateFormat sdf = new SimpleDateFormat("mm", Locale.getDefault());
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            return "";
        }
    }


    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(QUERY_DATE_FORMAT, Locale.getDefault());
        return sdf.format(date);
    }


    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(date);
    }



    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return sdf.format(System.currentTimeMillis());
    }

    public static long getCurrentTimeMillis() {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        return cal.getTime().getTime();
    }


    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
        return sdf.format(System.currentTimeMillis());
    }


    public static String getSubMinute(Context context, String beginDateTime, String endDateTime) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            Date beginDate = myFormatter.parse(beginDateTime);
            Date endDate = myFormatter.parse(endDateTime);
            long time = (endDate.getTime() - beginDate.getTime()) / (60 * 1000);
            if (time < 0) {
                return context.getString(R.string.baseservice_time_input_wrong);
            }
            int hour = (int) time / 60;
            int minute = (int) time % 60;
            if (hour == 0) {
                return context.getString(R.string.baseservice_waiting_time, minute);
            } else {
                return context.getString(R.string.baseservice_hour_minute, hour, minute);
            }
        } catch (ParseException e) {
            return context.getString(R.string.baseservice_time_input_wrong);
        }

    }


    public static String getAge(String oldDate) {
        if (oldDate == null) {
            return null;
        }
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date mydate = myFormatter.parse(oldDate);
            long day = (System.currentTimeMillis() - mydate.getTime()) / (24 * 60 * 60 * 1000) + 1;
            String year = new DecimalFormat().format(day / 365);
            return year;
        } catch (ParseException e) {
        }
        return "";
    }


    public static String formatBookingTime(String orderTime) {
        try {
            String[] dateTime = orderTime.split(" ");
            String[] ymd = dateTime[0].split("-");
            String[] hm = dateTime[1].split(":");
            int h = Integer.valueOf(hm[0]);
            int minute = Integer.valueOf(hm[1]);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.set(Integer.valueOf(ymd[0]), Integer.valueOf(ymd[1]) - 1, Integer.valueOf(ymd[2]));
            cal.set(Calendar.HOUR_OF_DAY, h);
            cal.set(Calendar.MINUTE, minute);
            cal.set(Calendar.SECOND, 0);
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            return orderTime;
        }
    }


    public static String formatBookingTime2(String orderTime) {
        try {
            String[] dateTime = orderTime.split(" ");
            String[] ymd = dateTime[0].split("-");
            String[] hms = dateTime[1].split(":");
            int h = Integer.valueOf(hms[0]);
            int minute = Integer.valueOf(hms[1]);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.set(Integer.valueOf(ymd[0]), Integer.valueOf(ymd[1]) - 1, Integer.valueOf(ymd[2]));
            cal.set(Calendar.HOUR_OF_DAY, h);
            cal.set(Calendar.MINUTE, minute);
            cal.set(Calendar.SECOND, Integer.valueOf(hms[2]));
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            return orderTime;
        }
    }


    public static String formatAppClientDateTime(String dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date = sdf.parse(dateTime);
            SimpleDateFormat sdf2 = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
            return sdf2.format(date);
        } catch (ParseException e) {
        }
        return null;
    }


    public static String formatAppClientTime(String dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date = sdf.parse(dateTime);
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return sdf2.format(date);
        } catch (ParseException e) {
        }
        return null;
    }


    public static String formatYudingTime(String dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date = sdf.parse(dateTime);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd\nHH:mm", Locale.getDefault());
            return sdf2.format(date);
        } catch (ParseException e) {
        }
        return null;
    }


    public static String formatCalledTime(long ms) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss", Locale.getDefault());
        return sdf.format(ms);
    }


    public static boolean beforeCurrentDateTime(int year, int month, int date, int hourOfDay, int minute) {
        Calendar calParam = Calendar.getInstance(Locale.getDefault());
        calParam.set(year, month - 1, date, hourOfDay, minute);
        int r = calParam.compareTo(Calendar.getInstance(Locale.getDefault()));
        return r < 0;
    }


    public static boolean afterCurrentDate(int year, int month, int date) {
        Calendar calParam = Calendar.getInstance(Locale.getDefault());
        calParam.set(year, month - 1, date);
        int r = calParam.compareTo(Calendar.getInstance(Locale.getDefault()));
        return r > 0;
    }


    public static boolean setSystemAutoDateTime(Context context) {
        boolean autoTimeEnabled = getAutoState(context, Settings.System.AUTO_TIME);
        boolean autoTimeZoneEnabled = getAutoState(context, Settings.System.AUTO_TIME_ZONE);
        if (!autoTimeEnabled) {
            Settings.System.putInt(context.getContentResolver(), Settings.System.AUTO_TIME, 1);                    }
        if (!autoTimeZoneEnabled) {
            Settings.System.putInt(context.getContentResolver(), Settings.System.AUTO_TIME_ZONE, 1);                                }
        autoTimeEnabled = getAutoState(context, Settings.System.AUTO_TIME);
        autoTimeZoneEnabled = getAutoState(context, Settings.System.AUTO_TIME_ZONE);
        return (autoTimeEnabled && autoTimeZoneEnabled);
    }

    private static boolean getAutoState(Context context, String name) {
        try {
            return Settings.System.getInt(context.getContentResolver(), name) > 0;
        } catch (SettingNotFoundException snfe) {
            return false;
        }
    }

    public final static String getCurrentTimeString() {
        return System.currentTimeMillis() + "";
    }


    public static boolean beforeCurrentDateTime(String dateTime, int amountSeconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date = sdf.parse(dateTime);
            Calendar calParam = Calendar.getInstance(Locale.getDefault());
            calParam.setTime(date);
            calParam.add(Calendar.SECOND, amountSeconds);
            return calParam.getTime().before(new Date());
        } catch (ParseException e) {
        }
        return false;
    }


    public static boolean afterYesterday(String dateArgs) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = sdf.parse(dateArgs);
            Calendar calParam = Calendar.getInstance(Locale.getDefault());
            calParam.setTime(date);
            Calendar calParamYesterday = Calendar.getInstance(Locale.getDefault());
            calParamYesterday.add(Calendar.DAY_OF_YEAR, -1);
            return calParam.getTime().after(calParamYesterday.getTime());
        } catch (ParseException e) {
        }
        return false;
    }


    public static int getSecondBetweenTime(String afterTime, String beforeTime) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date begin = null;
        Date end = null;
        try {
            begin = dfs.parse(beforeTime);
            end = dfs.parse(afterTime);
        } catch (ParseException e) {
            return 0;
        }
        long between = (end.getTime() - begin.getTime()) / 1000;
        int second = (int) between / 60;
        return second;
    }


    public static String getHHmm(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
        return sdf.format(time);
    }

    public static long getCurrentDayStart() {
        SimpleDateFormat dfs = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        String time = dfs.format(System.currentTimeMillis());
        time += " 00:00:00";
        SimpleDateFormat dfs2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date begin = null;
        try {
            begin = dfs2.parse(time);
        } catch (ParseException e) {
            return 0;
        }
        return begin.getTime();
    }

    public static long getCurrentDayEnd() {
        SimpleDateFormat dfs = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        String time = dfs.format(System.currentTimeMillis());
        time += " 23:59:59";
        SimpleDateFormat dfs2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date begin = null;
        try {
            begin = dfs2.parse(time);
        } catch (ParseException e) {
            return 0;
        }
        return begin.getTime();
    }


    public static long afterDays(int days) {
        long currentEndTime = getCurrentDayEnd();
        return currentEndTime + days * 24 * 60 * 60 * 1000;
    }


    public static long beforeDays(int days) {
        return getCurrentDayStart() - days * 24 * 60 * 60 * 1000;
    }


    public static long getTime(String time) {
        SimpleDateFormat dfs = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        String s = dfs.format(System.currentTimeMillis());
        s += " " + time;
        SimpleDateFormat dfs2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = null;
        try {
            date = dfs2.parse(s);
        } catch (ParseException e) {
            return 0;
        }
        return date.getTime();
    }



    public static Date onlyDate(Date date) {
        if (date == null) {
            return date;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            String s = df.format(date);
            return df.parse(s);
        } catch (ParseException e) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        }
    }


    public static long string2Timestamp(String dateString) {
        long temp = 0;
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateString);
            temp = date1.getTime();
        } catch (ParseException e) {
        }
        return temp;
    }


    public static long getDayStart(Date date) {
        SimpleDateFormat dfs = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        String time = dfs.format(date);
        time += " 00:00:00";
        SimpleDateFormat dfs2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date begin = null;
        try {
            begin = dfs2.parse(time);
        } catch (ParseException e) {
            return 0;
        }
        return begin.getTime();
    }


    public static long getDayEnd(Date date) {
        SimpleDateFormat dfs = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        String time = dfs.format(date);
        time += " 23:59:59";
        SimpleDateFormat dfs2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date begin = null;
        try {
            begin = dfs2.parse(time);
        } catch (ParseException e) {
            return 0;
        }
        return begin.getTime();
    }


    public static Date resetDate(Date date, String period) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(date);
            String[] periods = period.split(":");
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(periods[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(periods[1]));
        } catch (Exception e) {
        }
        return calendar.getTime();
    }


    public static String getFormatHHMM(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
        return dateFormat.format(date.getTime());
    }


    public static Date formatDateMinutes(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
        String dateString = sdf.format(date);
        Date rdate = null;
        try {
            rdate = sdf.parse(dateString);
        } catch (ParseException e) {
        }
        return rdate;
    }


    public static String getMMdd() {
        return mMDHMDateTime.get().format(System.currentTimeMillis());
    }


    public static String getMMddss() {
        return getMMddss(System.currentTimeMillis());
    }


    public static String getMMddss(Object time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");
        mMDHMDateTime.set(format);
        return mMDHMDateTime.get().format(time);
    }


    public static String formatMMdd(long timemillis) {
        return mMDHMDateTime.get().format(timemillis);
    }


    public static String calMinute(long saveTime) {
        if (saveTime < 1) {
            return "0min";
        }
        StringBuilder returnBuilder = new StringBuilder();
        Date currentDate = new Date();
        long currentTime = currentDate.getTime();
        long minute = (currentTime - saveTime) / (60 * 1000);
        returnBuilder.append(minute);
        return returnBuilder.toString();
    }

    public static int getTimeInterval(Long time) {
        if (time < 1) {
            return 0;
        }
        Date currentDate = new Date();
        long currentTime = currentDate.getTime();
        long day = (currentTime - time) / (60 * 1000 * 60 * 24);
        return (int) day;
    }


    public static Long parseStringDateTime(String datetime) {
        Long timestap = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
            Date d = df.parse(datetime);
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.setTime(d);
            timestap = cal.getTimeInMillis();
        } catch (ParseException e) {
            Log.e("DateTimeUtils", "parseStringDateTime", e);
        }
        return timestap;
    }


    public static int getDayNumber(long date) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(new Date(date));
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        if (cal.getFirstDayOfWeek() == Calendar.SUNDAY) {
            dayOfWeek = dayOfWeek - 1;
            if (dayOfWeek == 0) {
                dayOfWeek = 7;
            }
        }

        return dayOfWeek;
    }


    @SuppressLint("SimpleDateFormat")
    public static Date getDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -7);
        Date date = c.getTime();
        return date;
    }


    public static Date stringToDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date tempDate = null;
        try {
            tempDate = sdf.parse(date);
        } catch (ParseException e) {
            Log.e(DateTimeUtils.class.getSimpleName(), "", e);
        }
        return tempDate;

    }


    public static Date string2DateByFormat(String format, String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date tempDate = null;
        try {
            tempDate = sdf.parse(date);
        } catch (ParseException e) {
            Log.e(DateTimeUtils.class.getSimpleName(), "", e);
        }
        return tempDate;

    }


    public static boolean isEarly(Date date, String time) {
        if (date == null) {
            return false;
        }
        try {
            Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();
            String[] times = time.split(":");
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
            if (currentDate.getTime() > calendar.getTimeInMillis()) {
                return true;
            }
        } catch (Exception e) {
            Log.e(DateTimeUtils.class.getSimpleName(), "", e);
        }
        return false;
    }


    public static boolean isInPeriod(Date date, String periodStart, String periodEnd) {
        if (date == null || periodStart == null || periodEnd == null) {
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
        String sDate = dateFormat.format(date);
        try {
            Date srcDate = dateFormat.parse(sDate);
            Date startDate = dateFormat.parse(periodStart);
            Date endDate = dateFormat.parse(periodEnd);
            if (srcDate.compareTo(startDate) >= 0 && srcDate.compareTo(endDate) <= 0) {
                return true;
            }
        } catch (ParseException e) {
            Log.e(DateTimeUtils.class.getSimpleName(), "", e);
        }
        return false;
    }


    public static boolean isSameDay(Date day1, Date day2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ds1 = sdf.format(day1);
        String ds2 = sdf.format(day2);
        if (ds1.equals(ds2)) {
            return true;
        } else {
            return false;
        }
    }



    public static int trimZero(@NonNull String text) {
        try {
            if (text.startsWith("0")) {
                text = text.substring(1);
            }
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            Log.e("DataUtils", e.getMessage());
            return 0;
        }
    }


    @NonNull
    public static String fillZero(int number) {
        return number < 10 ? "0" + number : "" + number;
    }

    @NonNull
    public static String fillZero(String number) {
        return number.length() < 2 ? "0" + number : number;
    }


    public static long getMMDDtoLong(String time) {
        SimpleDateFormat dfs = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        String s = dfs.format(System.currentTimeMillis());
        s += " " + time;
        SimpleDateFormat dfs2 = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date date = null;
        try {
            date = dfs2.parse(s);
        } catch (ParseException e) {
            return 0;
        }
        return date.getTime();
    }
}