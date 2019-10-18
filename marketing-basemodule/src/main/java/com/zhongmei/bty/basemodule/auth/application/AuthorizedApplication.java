package com.zhongmei.bty.basemodule.auth.application;

import android.text.TextUtils;


public abstract class AuthorizedApplication extends BaseApplication {

    public static final String APP_CODE_NONE = "__app_code_none";

    public static final String APP_CODE_DINNER = joinCode("ZMYF001");
    public static final String APP_CODE_FAST_FOOD = joinCode("ZMYF002");
    public static final String APP_CODE_DINNER_GROUP = joinCode("ZMYF004");
    public static final String APP_CODE_CUSTOMER = joinCode("ZMYF006");
    public static final String APP_CODE_CALL = joinCode("ZMYF011");
    public static final String APP_CODE_BEAUTY = joinCode("ZMYF017");
    public static final String APP_CODE_BEAUTY_CUSTOMER = joinCode("ZMYF020");
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
