package com.zhongmei.bty.basemodule.auth.application;

import android.text.TextUtils;

/**
 * 受权，显示是否可见
 *
 * @created 2017/7/26
 */
public abstract class AuthorizedApplication extends BaseApplication {

    public static final String APP_CODE_NONE = "__app_code_none";

    public static final String APP_CODE_DINNER = joinCode("kry001");//正餐
    public static final String APP_CODE_FAST_FOOD = joinCode("kry002");//快餐
    public static final String APP_CODE_DINNER_GROUP = joinCode("kry004");//团餐

    public static final String APP_CODE_CUSTOMER = joinCode("kry006");//顾客
    public static final String APP_CODE_CALL = joinCode("kry011");//电话
    public static final String APP_CODE_BEAUTY = joinCode("kry017");//美业
    public static final String APP_CODE_BEAUTY_CUSTOMER = joinCode("kry020");//美业会员
    private String appCode;

    public AuthorizedApplication(String appCode) {
        super();
        this.appCode = appCode;
    }

    private static String joinCode(String... objects) {
        return TextUtils.join("/", objects);
    }

    public String getAppCode() {
        return appCode;
    }

    public String[] getAppCodeArray() {
        return !TextUtils.isEmpty(appCode) ? appCode.split("/") : null;
    }
}
