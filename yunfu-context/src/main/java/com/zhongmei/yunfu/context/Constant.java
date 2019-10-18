package com.zhongmei.yunfu.context;

import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;


public class Constant {

    public static final String SP_PRINT_IP_ADDRESS = "current_print_ip_address";

        public static final String DEFAULTLANGUAGE = "defaultLanguage";

        public static final String PRINT_SERVICE_PACKAGE_NAME = "com.demo.print";

    public static final String SP_NEED_INIT = "sync_need_init";

    public static boolean isNeedInit() {
        return SharedPreferenceUtil.getSpUtil().getBoolean(SP_NEED_INIT, true);
    }

    public static void setIsNeedInit(boolean isNeedInit) {
        SharedPreferenceUtil.getSpUtil().putBoolean(SP_NEED_INIT, isNeedInit);
    }

}
