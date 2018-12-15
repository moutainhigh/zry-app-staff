package com.zhongmei.bty.commonmodule.util;

import android.app.AlarmManager;
import android.content.Context;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 【标准】 主要是统一时间、闹钟、地理、等等
 * Created by demo on 2018/12/15
 */
public class Standard {

    public static AlarmManager generateAlarmManager(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        return alarmManager;
    }

    /**
     * 负责时间的统一标准
     */
    public static class Time {

        private static long diffTime = 0;

        /**
         * 当前时间
         *
         * @return 返回当前时间(毫秒)
         */
        public synchronized static long currentTime() {
            return System.currentTimeMillis() + diffTime;
        }

        /**
         * 获取当前的日历
         *
         * @return
         */
        public synchronized static Calendar getCalendar() {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
            calendar.setTimeInMillis(currentTime());
            return calendar;
        }

    }
}
