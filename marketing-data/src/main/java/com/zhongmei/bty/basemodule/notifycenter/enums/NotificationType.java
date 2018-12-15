package com.zhongmei.bty.basemodule.notifycenter.enums;

import com.zhongmei.yunfu.data.R;
import com.zhongmei.yunfu.context.base.BaseApplication;

/**
 * 通知中心的通知类型
 *
 * @Date：2016-2-1 上午9:30:27
 * @Description: TODO
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public enum NotificationType {
    /**
     * 未处理预订
     */
    NEWBOOKING("newbooking", R.string.dinner_new_booking),
    /**
     * 未处理排队
     */
    NEWQUEUE("newqueue", R.string.dinner_new_queue),

    /**
     * 百度糯米新订单
     */
    BAIDU_RICE_NEWTRADE("baidu_rice_newtrade", R.string.dinner_newtrade_baidurich),
    /**
     * 新订单－百度外卖
     */
    BAIDU_TAKEOUT_NEWTRADE("baidu_takeout_newtrade", R.string.dinner_newtrade_baidutakeout),
    /**
     * 必胜客新订单
     */
    PIZZAHUT_NEWTRADE("pizzahut_newtrade", R.string.dinner_newtrade_pizza_hut),
    /**
     * 新订单－饿了么
     */
    ELEME_NEWTRADE("eleme_newtrade", R.string.dinner_newtrade_elm),
    /**
     * 美团外卖新订单
     */
    MEITUAN_TAKEOUT_NEWTRADE("meituan_takeout_newtrade", R.string.dinner_newtrade_meituantakeout),

    /**
     * 京东到家新订单
     */
    JD_HOME_NEWTRADE("jd_hone_newtrade", R.string.dinner_newtrade_jd_home),
    /**
     * 开放平台新订单
     */
    OPEN_PLATFORM_NEWTRADE("open_platform_newtrade", R.string.dinner_newtrade_open_platform),
    /**
     * 大众点评新订单
     */
    DIANPING_NEWTRADE("dianping_newtrade", R.string.dinner_newtrade_publiccomment),

    /**
     * 熟客新订单
     */
    SHUKE_NEWTRADE("shuke_newtrade", R.string.dinner_newtrade_freqencycustomer),

    /**
     * 微信新订单
     */
    WECHAT_NEWTRADE("wechat_newtrade", R.string.dinner_newtrade_wechat),
    /**
     * 新美大扫码下单
     */
    XIN_MEI_DA("xinmeida", R.string.dinner_newtrade_xinmeida),
    /**
     * 票据打印失败
     */
    PRINT_FAILED("print_failed", R.string.print_failed),
    /**
     * 退款失败
     */
    WECHAT_REFUND_FAILED("wechat_refund_failed", R.string.dinner_wechat_repay_fail),
    /**
     * 刷卡交易记录
     */
    POS_RECORD("pos_record", R.string.dinner_pos_trade_unfinish),

    /**
     * 外卖退单申请(不区分饿了么和美团)
     */
    TAKE_OUT_CANCEL("take_out_cancel", R.string.dinner_take_out_retrade_apply),
    /**
     * 催菜
     */
    REMIND_DISH("remind_dish", R.string.dinner_orderdish_dish_urge),
    /**
     * 等叫
     */
    WAKE_UP("wake_up", R.string.dinner_dish_call_up),
    /**
     * 起菜
     */
    RISE_DISH("rise_dish", R.string.dinner_orderdish_dish_make),
    /**
     * 合同到期
     */
    HETONG_EXPIRE("hetong_expire", R.string.dinner_compact_over),
    /**
     * 空闲桌台
     */
    FREE_TABLES("free_tables", R.string.dinner_free_table),
    /**
     * 故障订单
     */
    ERROR_ORDER_PRINT("error_order_print", R.string.dinner_print_fail_trade),
    /**
     * 服务铃
     */
    CALL_WAITER("call_waiter", R.string.dinner_server_calling),
    /**
     * 微信加菜单
     */
    WECHAT_ADDTRADE("wechat_addtrade", R.string.dinner_WeChat_additem),

    /**
     * 加菜单
     */
    POS_ADDTRADE("wechat_addtrade", R.string.dinner_additem),

    /**
     * 熟客小程序加菜单
     */
    FAMILIAR_ADDTRADE("wechat_addtrade", R.string.dinner_returnguest_additem),
    /**
     * 系统公告
     */
    SYSTEM_INFORMATION("system_information", R.string.dinner_system_information),

    /**
     * 其他端支付
     */
    OTHER_CLIENT_PAYED("other_client_payed", R.string.dinner_other_client_payed),

    /**
     * 打印出错
     */
    PRINT_ERROR("print_error", R.string.dinner_print_error),

    /**
     * 新订单的总类，用来做多中来源订单的统一处理，没有实际的业务意义
     */
    NEW_TRADE("new_trade", R.string.new_trade),

    /**
     * 配送取消订单
     */
    DELIVERY_CANCEL("delivery_cancel", R.string.delivery_cancel_trade),

    /**
     * 待下发派送订单
     */
    DELIVERY_WAITING("delivery_waiting", R.string.delivery_waiting),

    /**
     * v8.12.0
     * 口碑
     */
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
