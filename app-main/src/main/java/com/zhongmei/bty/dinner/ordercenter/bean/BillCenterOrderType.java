package com.zhongmei.bty.dinner.ordercenter.bean;

public class BillCenterOrderType {

    /**
     * 默认类型
     */
    public final static int DEFAULT = -1;

    /**
     * 网络订单 未处理
     */
    public final static int NETWORKORDER_UNHANDLE = 100;

    /**
     * 网络订单 已拒绝
     */
    public static final int NETWORKORDER_REFUSED = 101;

    /**
     * 销货单 未结账
     */
    public static final int SALESORDER_NOTPAY = 200;

    /**
     * 销货单 已结账
     */
    public static final int SALESORDER_PAYED = 201;

    /**
     * 销货单 已退款
     */
    public static final int SALESORDER_REFUNDED = 202;

    /**
     * 销货单 已作废
     */
    public static final int SALESORDER_INVALID = 203;

    /**
     * 退货单 有票退货
     */
    public static final int RETURNGOODS_TICKET = 300;

    /**
     * 退货单 无票退货
     */
    public static final int RETURNGOODS_NOTTICKET = 301;

    /**
     * 在线支付 支付状态
     */
    public static final int ONLINEPAY_PAY = 302;

    /**
     * 在线支付 退款状态
     */
    public static final int ONLINEPAY_REFUND = 303;

    /**
     * 调账
     */
    public static final int ADJUST = 400;

}
