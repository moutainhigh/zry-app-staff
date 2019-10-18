package com.zhongmei.bty.basemodule.notifycenter.enums;

import com.zhongmei.yunfu.data.R;
import com.zhongmei.yunfu.context.base.BaseApplication;


public enum NotificationType {

    NEWBOOKING("newbooking", R.string.dinner_new_booking),

    NEWQUEUE("newqueue", R.string.dinner_new_queue),


    BAIDU_RICE_NEWTRADE("baidu_rice_newtrade", R.string.dinner_newtrade_baidurich),

    BAIDU_TAKEOUT_NEWTRADE("baidu_takeout_newtrade", R.string.dinner_newtrade_baidutakeout),

    PIZZAHUT_NEWTRADE("pizzahut_newtrade", R.string.dinner_newtrade_pizza_hut),

    ELEME_NEWTRADE("eleme_newtrade", R.string.dinner_newtrade_elm),

    MEITUAN_TAKEOUT_NEWTRADE("meituan_takeout_newtrade", R.string.dinner_newtrade_meituantakeout),


    JD_HOME_NEWTRADE("jd_hone_newtrade", R.string.dinner_newtrade_jd_home),

    OPEN_PLATFORM_NEWTRADE("open_platform_newtrade", R.string.dinner_newtrade_open_platform),

    DIANPING_NEWTRADE("dianping_newtrade", R.string.dinner_newtrade_publiccomment),


    SHUKE_NEWTRADE("shuke_newtrade", R.string.dinner_newtrade_freqencycustomer),


    WECHAT_NEWTRADE("wechat_newtrade", R.string.dinner_newtrade_wechat),

    XIN_MEI_DA("xinmeida", R.string.dinner_newtrade_xinmeida),

    PRINT_FAILED("print_failed", R.string.print_failed),

    WECHAT_REFUND_FAILED("wechat_refund_failed", R.string.dinner_wechat_repay_fail),

    POS_RECORD("pos_record", R.string.dinner_pos_trade_unfinish),


    TAKE_OUT_CANCEL("take_out_cancel", R.string.dinner_take_out_retrade_apply),

    REMIND_DISH("remind_dish", R.string.dinner_orderdish_dish_urge),

    WAKE_UP("wake_up", R.string.dinner_dish_call_up),

    RISE_DISH("rise_dish", R.string.dinner_orderdish_dish_make),

    HETONG_EXPIRE("hetong_expire", R.string.dinner_compact_over),

    FREE_TABLES("free_tables", R.string.dinner_free_table),

    ERROR_ORDER_PRINT("error_order_print", R.string.dinner_print_fail_trade),

    CALL_WAITER("call_waiter", R.string.dinner_server_calling),

    WECHAT_ADDTRADE("wechat_addtrade", R.string.dinner_WeChat_additem),


    POS_ADDTRADE("wechat_addtrade", R.string.dinner_additem),


    FAMILIAR_ADDTRADE("wechat_addtrade", R.string.dinner_returnguest_additem),

    SYSTEM_INFORMATION("system_information", R.string.dinner_system_information),


    OTHER_CLIENT_PAYED("other_client_payed", R.string.dinner_other_client_payed),


    PRINT_ERROR("print_error", R.string.dinner_print_error),


    NEW_TRADE("new_trade", R.string.new_trade),


    DELIVERY_CANCEL("delivery_cancel", R.string.delivery_cancel_trade),


    DELIVERY_WAITING("delivery_waiting", R.string.delivery_waiting),


    KOUBEI_NEWTRADE("koubei_newtrade", R.string.dinner_newtrade_koubei);

    private String value;

    private int descResId;

    private NotificationType(String value, int descResId) {
        this.value = value;
        this.descResId = descResId;
    }

    public String value() {
        return value;
    }

    public String desc() {
        if (descResId > 0) {
            return BaseApplication.sInstance.getString(descResId);
        } else {
            return BaseApplication.sInstance.getString(R.string.commonmodule_dialog_other);
        }
    }
}
