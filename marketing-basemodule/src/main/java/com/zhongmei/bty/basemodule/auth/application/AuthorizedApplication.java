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
    public static final String APP_CODE_RETAIL = joinCode("kry003");//零售
    public static final String APP_CODE_DINNER_GROUP = joinCode("kry004");//团餐
    public static final String APP_CODE_BUFFET = joinCode("kry005");//自助

    public static final String APP_CODE_CUSTOMER = joinCode("kry006");//顾客
    public static final String APP_CODE_QUEUE = joinCode("kry007");//排队
    public static final String APP_CODE_BOOKING = joinCode("kry008");//预定
    public static final String APP_CODE_KRY_SET = joinCode("kry009");//设置
    public static final String APP_CODE_RETAIL_KRY_SET = joinCode("kry010");//零售设置
    public static final String APP_CODE_CALL = joinCode("kry011");//电话
    public static final String APP_CODE_PRINT = joinCode("kry012");//打印
    public static final String APP_CODE_STORE = joinCode("kry013");//我的服务
    public static final String APP_CODE_HELPER = joinCode("kry014");//帮助中心
    public static final String APP_CODE_SYS_SETTING = joinCode("kry015");//系统设置
    public static final String APP_CODE_BROWSER = joinCode("kry016");//浏览器
    public static final String APP_CODE_BEAUTY = joinCode("kry017");//美业
    public static final String APP_CODE_BEAUTY_setting = joinCode("kry018");//美业设置
    public static final String APP_CODE_BAKERY = joinCode("kry019");//烘焙
    public static final String APP_CODE_BEAUTY_CUSTOMER = joinCode("kry020");//美业会员
    public static final String APP_CODE_TALENT = joinCode("kry021");//员工
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
