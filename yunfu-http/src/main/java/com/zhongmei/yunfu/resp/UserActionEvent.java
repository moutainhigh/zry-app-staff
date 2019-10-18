package com.zhongmei.yunfu.resp;

import android.content.Context;
import android.util.Log;

import com.zhongmei.analysis.DataMobAgent;
import com.zhongmei.analysis.PerformanceActionData;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public enum UserActionEvent {


        SERVER_API("000000", "基础-接口访问"),
    SERVER_REQUEST_DB("000001", "基础-接口访问回调保存Db"),

        INIT_PROCESS("100000", "初始化过程"),
    INIT_LOGIN("100001", "用户登录"),

        DINNER_TABLE_OPEN("110000", "正餐-开台"),
    DINNER_TABLE_CLEAR("110001", "正餐-清台"),
    DINNER_DISHES_MOVE("110002", "正餐-移菜"),
    DINNER_DISHES_COPY("110003", "正餐-复制菜"),
    DINNER_DEL_ORDER("110004", "正餐-删除订单"),

        DINNER_DISH_SHOPCART_DISPLAY("110005", "正餐-点菜界面购物车显示"),
    DINNER_DISH_DISPLAY("110006", "正餐-菜品列表显示"),
    DINNER_TABLE_ZONE_REFRESH("110007", "桌台区域刷新"),
    DINNER_TABLE_REFRESH("110008", "桌台刷新"),
    DINNER_TRADE_REFRESH("110009", "订单刷新"),
    DINNER_TABLE_DATA_QUERY("110010", "桌台数据查询"),
    DINNER_TRADEINFO_REFRESH("110011", "桌台详情刷新"),
    DINNER_SCRATCH_DISH("110012", "正餐-划菜"),

        DINNER_TRADE_ACCEPT("11000901", "第三方订单接受"),
    DINNER_TRADE_ITEM_ADD_ACCEPT("11000902", "订单加菜单接受"),
    DINNER_TRADE_REFUSE("11000903", "第三方订单拒绝"),
    DINNER_TRADE_ITEM_ADD_REFUSE("11000904", "订单加菜单拒绝"),

        DINNER_DISHES_TRANSFER_KITCHEN("110100", "正餐-传后厨"),
    DINNER_PAY_ORDER_SAVE("110101", "正餐-保存结算"),
    DINNER_PAY_ORDER_PRE("110102", "正餐-预结单"),
    DINNER_PAY_WECHAT_CARD_COUPON("110103", "正餐-微信卡券"),
    DINNER_PAY_LOGIN_SWIPE_CARD("110104", "正餐-刷卡登录"),
    DINNER_PAY_LOGIN_SCAN_CODE("110105", "正餐-扫码登录"),
    DINNER_PAY_LOGIN_SCAN_WECHAT_CODE("110106", "正餐-扫码微信登录"),
    DINNER_PAY_LOGIN_INPUT_MOBILE("110107", "正餐-输入手机号登录"),
    DINNER_PAY_LOGIN_INPUT_FACE("110109", "正餐-人脸登录"),

        DINNER_PAY_SHOPCART_DISPLAY("110108", "正餐-结账购物车显示"),

        DINNER_PAY_SETTLE_ALIPAY("110201", "正餐-支付-结账支付宝"),
    DINNER_PAY_SETTLE_WECHAT_PAY("110202", "正餐-支付-结账微信"),
    DINNER_PAY_SETTLE_MEITUAN_CASH_PAY("110203", "正餐-支付-结账美团闪惠"),
    DINNER_PAY_SETTLE_BAIDU_PAY("110204", "正餐-支付-结账百度钱包"),
    DINNER_PAY_SETTLE_JIN_CHENG_PAY("110205", "正餐-支付-结账金诚"),
    DINNER_PAY_SETTLE_JIN_CHENG_VALUE_CARD_PAY("110206", "正餐-支付-结账金诚充值卡"),


        DINNER_PAY_SETTLE_CASH("110200", "正餐-支付-结账现金"),
    DINNER_PAY_SETTLE_UNION("110200", "正餐-支付-结账银联"),
    DINNER_PAY_SETTLE_STORE("110200", "正餐-支付-储值"),
    DINNER_PAY_SETTLE_BAIDU_COUPON("110200", "正餐-支付-糯米验券"),
    DINNER_PAY_SETTLE_MEITUAN("110200", "正餐-支付-美团券"),
    DINNER_PAY_SETTLE_ORDER("110200", "正餐-支付-其它"),
    DINNER_PAY_SETTLE_LAG_PAY("110299", "正餐-支付-挂账"),

        CUSTOMER_QUERY_DATA_BY_LOGIN("130000", " 通过二维码查询顾客数据"),
    CUSTOMER_QUERY_LIST("130001", " 查询顾客列表"),
    CUSTOMER_CREATE("130002", " 创建会员"),
    CUSTOMER_EDIT("130003", " 编辑会员信息"),
    CUSTOMER_QUERY_CARDINFO_BY_RANG("130004", " 范围查询会员卡"),
    CUSTOMER_QUERY_CARDINFO_BY_SINGLE("130005", " 单卡查询会员卡"),
    CUSTOMER_SALE_CARD("130006", " 售卡"),
    CUSTOMER_QUERY_DATA_BY_CUSTOMER_ID("130007", " 通过顾客ID查询并展示顾客基本信息"),
    CUSTOMER_QUERY_EC_CARD("130008", " 查询顾客的权益卡信息并展示"),

    FAST_FOOD_HOME("120001", "快餐首页"),
    FAST_FOOD_PAY("120002", "快餐收银");






    public String eventName;
    public String eventDesc;

    UserActionEvent(String eventName, String eventDesc) {
        this.eventName = eventName;
        this.eventDesc = eventDesc;
    }

    public static UserActionEvent enumOf(String eventName) {
        for (UserActionEvent event : UserActionEvent.values()) {
            if (event.eventName.equals(eventName)) {
                return event;
            }
        }
        return null;
    }

    public static String getEventDesc(String eventName) {
        UserActionEvent event = enumOf(eventName);
        return event != null ? event.eventName : "";
    }


    private static Context context;
    private static Long shopId;
    private static String macAddress;

    public static void init(Context context, Long shopId, String macAddress) {
        UserActionEvent.context = context;
        UserActionEvent.shopId = shopId;
        UserActionEvent.macAddress = macAddress;
    }

    public static final Map<String, ActionTransaction> timeConsumingMap = new HashMap<>();
        public static final String SP_USER_ACTION_SWITCH = "user_action_switch";

    public static boolean isSwitch() {
        return SharedPreferenceUtil.getSpUtil().getBoolean(SP_USER_ACTION_SWITCH, false);
    }

    public static void setSwitch(boolean enabled) {
        SharedPreferenceUtil.getSpUtil().putBoolean(SP_USER_ACTION_SWITCH, enabled);
    }

    private static String getNetKey(String eventName, String tag) {
        return String.format("[%s]%s", eventName, tag);
    }

    public static void start(UserActionEvent event) {
        timeConsumingMap.put(event.eventName, new ActionTransaction());
    }

    public static void end(UserActionEvent event) {
        end(event.eventName);
    }

    public static void end(String eventName) {
        ActionTransaction actionTransaction = timeConsumingMap.get(eventName);
        if (actionTransaction != null) {
            onCustomerEvent(eventName, actionTransaction.transactionNo, actionTransaction.getTimeConsuming(), null);
        }
    }

    public static void netStart(String eventName, String tag) {
        ActionTransaction actionTransaction = timeConsumingMap.get(eventName);
        if (actionTransaction != null) {
            timeConsumingMap.put(getNetKey(eventName, tag), new ActionTransaction(actionTransaction.transactionNo));
        }
    }

    public static void netEnd(String eventName, String tag, String description) {
        ActionTransaction at = timeConsumingMap.remove(getNetKey(eventName, tag));
        if (at != null) {
            onCustomerEvent(SERVER_API, at.transactionNo, at.getTimeConsuming(), description);
        }
    }

    public static void netDbEnd(String eventName, String tag, String description) {
        ActionTransaction at = timeConsumingMap.remove(getNetKey(eventName, tag));
        if (at != null) {
            onCustomerEvent(SERVER_REQUEST_DB, at.transactionNo, at.getTimeConsuming(), description);
        }
    }

    @Deprecated
    public static void onCustomerEvent(String eventCode, String transactionNo, long timeConsuming, String description) {
        onCustomerEvent(enumOf(eventCode), transactionNo, timeConsuming, description);
    }

    public static void onCustomerEvent(UserActionEvent event, String transactionNo, long timeConsuming, String description) {
        Log.i("UserActionEvent", String.format("[%s-%s-%s]%s -> %s(ms) -> %s", isSwitch(), event.eventName, transactionNo, event.eventDesc, timeConsuming, description));
        if (isSwitch()) {
                        PerformanceActionData data = new PerformanceActionData();
            data.setShopId(String.valueOf(shopId));
            data.setMacAddress(macAddress);
            data.setCode(event.eventName);
            HashMap<String, String> userActionMap = new HashMap<>();
            userActionMap.put("transactionNo", transactionNo);
            userActionMap.put("eventDesc", event.eventDesc);
            userActionMap.put("duration", String.valueOf(timeConsuming));
            userActionMap.put("description", description);
            userActionMap.put("createTime", String.valueOf(System.currentTimeMillis()));
            data.setUserActionMap(userActionMap);
            DataMobAgent.onEvent(context, data);
        }
    }



    static class ActionTransaction {
        public long startTime;
        public String transactionNo;

        public ActionTransaction() {
            this(UUID.randomUUID().toString().replaceAll("-", ""));
        }

        public ActionTransaction(String transactionNo) {
            this.startTime = System.currentTimeMillis();
            this.transactionNo = transactionNo;
        }

        public long getTimeConsuming() {
            return System.currentTimeMillis() - startTime;
        }
    }
}