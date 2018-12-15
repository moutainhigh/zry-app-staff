package com.zhongmei.bty.basemodule.database.utils;

public final class DbQueryConstant {

    public static final int USER_QUERY = 1;

    public static final int ALL_UNPROCESS_ORDER = 10002;

    public static final int ALL_UNPAY_ORDER = 10003;

    public static final int ALL_PAY_ORDER = 10004;

    public static final int ORDER_DISH_DETAIL = 5;

    public static final int ALL_REFUND_ORDER = 10008;

    public static final int LOAD_TRADE_EX = 9;

    public static final int LOAD_TRADE_CUSTOMER = 10;

    public static final int LOAD_TRADE_PRIVILEGE = 11;

    public static final int LOAD_PAYMENT = 12;

    public static final int LOAD_PAYMENT_ITEM = 13;

    public static final int LOAD_PAYMENT_ITEM_EXTRA = 14;

    public static final int LOAD_TRADE_ITEM_PROPERTY = 15;

    public static final int LOAD_TRADE_FOUR_TAB_COUNT = 16;

    public static final int LOAD_ALL_REASON = 17;

    public static final int LOAD_TRADE_DEPOSIT = 18;

    public static final int LOAD_TRADE_TABLE = 19;

    public static final int LOAD_TRADE_PLANACTIVITY = 20;

    public static final int LOAD_TRADE_ITEMPLANACTIVITY = 21;

    public static final int DISH_TYPE_QUERY = 51;

    public static final int DISH_QUERY = 61;

    public static final int DISH_MEMO_QUERY = 62;

    public static final int TABLE_QUERY = 71;

    public static final int COMBO_QUERY = 81;

    public static final int ORDER_MEMO_QUERY = 91;

    public static final int ORDER_QUERY = 101;

    public static final int ORDER_DETAIL_QUERY = 102;

    public static final int DISH_RECIPE_QUERY = 103;

    public static final int DISH_TASTE_QUERY = 104;

    public static final int DELIVERY_ALL = 111;
    /**
     * 内用
     */
    public static final int DELIVERY_HERE = 112;
    /**
     * 外送
     */
    public static final int DELIVERY_SEND = 113;
    /**
     * 自提
     */
    public static final int DELIVERY_TAKE = 114;
    /**
     * 外带
     */
    public static final int DELIVERY_CARRY = 115;

    public static final int REFUND_ALL = 121;
    /**
     * 已退货
     */
    public static final int REFUND_RETURNED = 122;
    /**
     * 已作废
     */
    public static final int REFUND_INVALID = 123;
    /**
     * 已拒绝
     */
    public static final int REFUND_REFUSED = 124;
    /**
     * 已取消
     */
    public static final int REFUND_CANCELLED = 125;

    public static final int UNPROCESS_ALL = 131;

    /**
     * 新订单
     */
    public static final int UNPROCESS_NEW_ORDER = 132;

    /**
     * 取消请求
     */
    public static final int UNPROCESS_CANCEL_REQUEST = 133;

    // 未处理
    public static final int ALL_UNDEAL__TAKEOUT_ORDER = 110001;
    // 待配送
    public static final int ALL_WAIT_DISTRIBUTE__TAKEOUT_ORDER = 110002;
    // 配送中
    public static final int ALL_DISTRIBUTING__TAKEOUT_ORDER = 110003;
    // 配送完成
    public static final int ALL_DISTRIBUTE_FINISH__TAKEOUT_ORDER = 110004;

    /**
     * 网络订单
     */
    public static final int ALL_BILLCENTER_NETWORK_ORDER = 12001;
    /**
     * 销货单
     */
    public static final int ALL_BILLCENTER_SALES_ORDER = 12002;
    /**
     * 退货单
     */
    public static final int ALL_BILLCENTER_RETURNS_ORDER = 12003;

    /**
     * 调账单
     */
    public static final int ALL_BILLCENTER_ADJUST = 12004;

    /**
     * 在线支付订单
     */
    public static final int ALL_BILLCENTER_ONLINE = 12005;

    /**
     * 待处理
     */
    public static final int UNPROCESSED = 200;

    /**
     * 待处理-全部
     */
    public static final int UNPROCESSED_ALL = 201;

    /**
     * 待处理-新订单
     */
    public static final int UNPROCESSED_NEW_ORDER = 202;

    /**
     * 待处理-取消请求
     */
    public static final int UNPROCESSED_CANCEL_REQUEST = 203;

    /**
     * 待处理-已拒绝/取消
     */
    public static final int UNPROCESSED_INVALID = 204;

    /**
     * 销货单
     */
    public static final int SALES = 210;

    /**
     * 销货单-全部
     */
    public static final int SALES_ALL = 211;

    /**
     * 销货单-未结账
     */
    public static final int SALES_UNPAID = 212;

    /**
     * 销货单-已结账
     */
    public static final int SALES_PAID = 213;

    /**
     * 销货单-已退货
     */
    public static final int SALES_REFUNDED = 214;

    /**
     * 销货单-已作废
     */
    public static final int SALES_INVALID = 215;

    /**
     * 销货单-支付中
     */
    public static final int SALES_PAYING = 216;
}
