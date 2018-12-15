package com.zhongmei.bty.customer.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.bty.settings.fragment.QueueSettingSwitchFragment;

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


    /**
     * 电话号码加星保护
     **/
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

    /**
     * 如果设置了电话号码隐私保护，显示加星的号码，否则显示正常号码
     *
     * @param normalTel
     * @return
     */
    public static String getTel(String normalTel) {

        if (SpHelper.getDefault().getBoolean(QueueSettingSwitchFragment.MOBILE_PRIVACY, false)) {
            return getHideTel(normalTel);
        }
        return normalTel;
    }

}
