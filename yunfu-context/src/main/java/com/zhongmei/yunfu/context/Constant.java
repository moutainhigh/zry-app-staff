package com.zhongmei.yunfu.context;

import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;

/**
 * @Date：2014年11月4日 上午11:43:44
 * @Description: 公用的静态变量声明
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class Constant {

    public static final String SP_PRINT_IP_ADDRESS = "current_print_ip_address";

    //系统默认语言
    public static final String DEFAULTLANGUAGE = "defaultLanguage";

    //打印的包名
    public static final String PRINT_SERVICE_PACKAGE_NAME = "com.demo.print";

    public static final String SP_NEED_INIT = "sync_need_init";

    public static boolean isNeedInit() {
        return SharedPreferenceUtil.getSpUtil().getBoolean(SP_NEED_INIT, true);
    }

    public static void setIsNeedInit(boolean isNeedInit) {
        SharedPreferenceUtil.getSpUtil().putBoolean(SP_NEED_INIT, isNeedInit);
    }

}
