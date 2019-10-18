package com.zhongmei.bty.commonmodule.util;


public class SpendTimeFormater {

    public static String format(int spendTime) {
        StringBuffer timeStr = new StringBuffer();
        if (spendTime < 60) {
            timeStr.append(String.valueOf(spendTime));
            timeStr.append("min");
        } else {
            int hour = spendTime / 60;
            int min = spendTime % 60;

            timeStr.append(hour + "h");
            timeStr.append(min + "m");
        }
        return timeStr.toString();
    }
}
