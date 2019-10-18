package com.zhongmei.yunfu.util;

import android.text.TextUtils;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;


public class ResourceUtils {

    private static final String CURRENCY_SYMBOL = "\\{HBF\\}";

    public static String getString(int resId) {
        return formatString(BaseApplication.sInstance.getString(resId));
    }

    public static String getString(int resId, Object... formatArgs) {
        return formatString(BaseApplication.sInstance.getString(resId, formatArgs));
    }

    public static String formatString(CharSequence string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        return string.toString().replaceAll(CURRENCY_SYMBOL, String.format("\\%s", ShopInfoCfg.getInstance().getCurrencySymbol()));
    }
}
