package com.zhongmei.bty.basemodule.commonbusiness.constants;

import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;



public class SnackConstant {

    public final static String PAGE_TAG = "pageTag";
    public final static String NAME_SPACE = SnackConstant.class.getSimpleName() + "_";

    public final static int PAGE_ORDER_CENTER = 2;

    public final static class OfflineEnableSwitch {
        public static final String KEY_OFFLINE_ENABLE = "offline_enable";        private static final String VALUE_OFFLINE_ENABLE = "1";         private static final String VALUE_OFFLINE_DISABLE = "2";
        public static final String KEY_SP_SWITCH = NAME_SPACE + "key_sp_switch";
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
