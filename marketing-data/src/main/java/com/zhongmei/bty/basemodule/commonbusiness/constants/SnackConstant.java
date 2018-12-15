package com.zhongmei.bty.basemodule.commonbusiness.constants;

import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;

/**
 * Created by demo on 2018/12/15
 */

public class SnackConstant {

    public final static String PAGE_TAG = "pageTag";
    public final static String NAME_SPACE = SnackConstant.class.getSimpleName() + "_";

    public final static int PAGE_ORDER_CENTER = 2;

    public final static class OfflineEnableSwitch {
        public static final String KEY_OFFLINE_ENABLE = "offline_enable";//离线是否可用的key
        private static final String VALUE_OFFLINE_ENABLE = "1"; //离线可用
        private static final String VALUE_OFFLINE_DISABLE = "2";//离线不可用

        public static final String KEY_SP_SWITCH = NAME_SPACE + "key_sp_switch";//记录到sp里面的key

        private OfflineEnableSwitch() {

        }

        public static boolean isOfflineEnable() {
            String value = SharedPreferenceUtil.getSpUtil().getString(KEY_SP_SWITCH, VALUE_OFFLINE_DISABLE);
            return VALUE_OFFLINE_ENABLE.equals(value);
        }

        public static void recordSwitchValue(String value) {
            SharedPreferenceUtil.getSpUtil().putString(KEY_SP_SWITCH, value);
        }
    }
}
