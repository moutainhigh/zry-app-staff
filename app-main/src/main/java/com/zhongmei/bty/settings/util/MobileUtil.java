package com.zhongmei.bty.settings.util;

import android.text.TextUtils;

import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.bty.settings.fragment.QueueSettingSwitchFragment;

public abstract class MobileUtil {

    public static CharSequence getMobileAndAreaCode(String nationalTelCode, String mobile) {
        if (TextUtils.isEmpty(nationalTelCode)) {
            return getMobile(mobile);
        }

        nationalTelCode = getAreaCode(nationalTelCode);
        return String.format("%-4s %s", nationalTelCode, getMobile(mobile));
    }

    public static String getAreaCode(String nationalTelCode) {
        return nationalTelCode.trim().startsWith("+") ? nationalTelCode : "+" + nationalTelCode;
    }

    public static CharSequence getMobile(String mobile) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(mobile)) {
            sb.append(mobile);
            if (SpHelper.getDefault().getBoolean(QueueSettingSwitchFragment.MOBILE_PRIVACY, false)) {
                if (mobile.length() > 10) {
                    sb.replace(3, 7, "****");
                } else if (mobile.length() > 6) {
                    sb.replace(3, 6, "****");
                }
            }
        }
        return sb.toString();
    }
}
