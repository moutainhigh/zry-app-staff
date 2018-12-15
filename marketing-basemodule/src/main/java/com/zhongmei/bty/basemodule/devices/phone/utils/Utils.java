package com.zhongmei.bty.basemodule.devices.phone.utils;

import android.text.TextUtils;

public class Utils {

    /**
     * 是否是带0的外地手机号码  true 是， false 否
     *
     * @param no
     * @return
     */
    public static Boolean isNonNativePhone(String no) {
        if (TextUtils.isEmpty(no)) {
            return false;
        }
        return no.length() == 12 && no.startsWith("01");
    }


    /**
     * 如果是外地手机号码并且前面加了0，去掉前面的0；
     *
     * @param phone
     * @return
     */
    public static String dealNativePhone(String phone) {
        String newPhone = null;
        if (Utils.isNonNativePhone(phone)) {
            newPhone = phone.substring(1);
        } else {
            newPhone = phone;
        }
        return newPhone;
    }


}
