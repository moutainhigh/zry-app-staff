package com.zhongmei.bty.commonmodule.util;

import android.app.AlarmManager;
import android.content.Context;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


public class Standard {

    public static AlarmManager generateAlarmManager(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        return alarmManager;
    }


    public static class Time {

        private static long diffTime = 0;


        public synchronized static long currentTime() {
            return System.currentTimeMillis() + diffTime;
        }


        public synchronized static Calendar getCalendar() {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
            calendar.setTimeInMillis(currentTime());
            return calendar;
        }

    }
}
