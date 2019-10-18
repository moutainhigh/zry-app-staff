package com.zhongmei.bty.customer.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtil {

    public static int getPixels(int dpValue, Context context) {
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, r.getDisplayMetrics());
        return px;
    }

    public static boolean isMobileNo(String mobile) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }



    private static String star = "****";
    private static StringBuilder newNo;

    public static String getHideTel(String normalTel) {
        int size = normalTel.length();
        if (size <= 2) {
            return "";
        }

        newNo = new StringBuilder();
        if (normalTel.length() <= 5) {
            newNo.append(normalTel.charAt(0));
            newNo.append(star);
            newNo.append(normalTel.charAt(size - 1));
        } else {
            newNo.append(normalTel);
            newNo.replace(size / 2 - 2, size / 2 + 2, star);
        }

        return newNo.toString();
    }


    public static String getTel(String normalTel) {

        return normalTel;
    }

}
