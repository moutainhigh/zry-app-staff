package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * Created by demo on 2018/12/15
 * <p>
 * 权限关联的单据类型
 */

public enum OrderActionEnum implements ValueEnum<String> {
//    ACTION_START_DESK(1, "createOrder", "开台"),
//    ACTION_FOOD_BACK(2, "fffee", "作废商品(退菜)"),
//    TRANSFER_TABLES(3, "transferTables", "转台"),
//    JOIN_TABLES(4, "joinTables", "合台"),
//    /** 改单 */
//    ACTION_CHANGE_ORDER(5, "changeOrder", "改单"),
//    /** 退货退款 */
//    ACTION_RETURN_REFUND(6, "returnAndRefund", "退货退款"),
//    /** 拒绝 */
//    ACTION_REJECT_ORDER(7, "rejectOrder", "拒绝"),
//    /** 接受 */
//    ACTION_ACCPET_ORDER(8, "accpetOrder", "接受"),
//    /** 拆单 */
//    ACTION_SPLIT_ORDER(9, "splitOrder", "拆单"),
//    /** 反结账 */
//    ACTION_ANTI_SETTLEMENT(10, "antiSettlement", "反结账"),
//    ACTION_MEALS_AT(11, "fff",
//            "上餐/取消上餐"),
//    /** 结账 */
//    ACTION_CHECK_OUT(12, "checkoutOrder", "结账"),
//    /** 作废 */
//    ACTION_CANCEL_ORDER(13, "cancelOrder", "作废"),

    ACTION_START_DESK("createOrder"),
    ACTION_FOOD_BACK("fffee"),
    TRANSFER_TABLES("transferTables"),
    JOIN_TABLES("joinTables"),
    ACTION_CHANGE_ORDER("changeOrder"),
    ACTION_RETURN_REFUND("returnAndRefund"),
    ACTION_REJECT_ORDER("rejectOrder"),
    ACTION_ACCPET_ORDER("accpetOrder"),
    ACTION_SPLIT_ORDER("splitOrder"),
    ACTION_ANTI_SETTLEMENT("antiSettlement"),
    ACTION_MEALS_AT("fff"),
    ACTION_CHECK_OUT("checkoutOrder"),
    ACTION_CANCEL_ORDER("cancelOrder"),
    //交接
    ACTION_HANDOVER("dinnerHandover"),
    //    交接历史
    ACTION_HANDOVER_LIST("dinnerHandoverList"),
    //校准
    ACTION_HANDOVER_CALIBRATE("dinnerHandoverCalibrate"),
    //关账
    ACTION_CLOSING("closing"),
    //关账历史
    ACTION_CLOSING_HISTORY("closingHistory"),
    //    挂账
    ACTION_CREDIT("credit"),
    //收支管理
    ACTION_PAYMENTSEDIT("paymentsedit"),
    //报表中心
    ACTION_REPORT_FORM("resportFormCenter"),
    //清账
    ACTION_CLEAR_BALANCE("clearBalance"),
    //设置
    ACTION_SETTINGS("settings"),
    //开票
    ACTION_INVOICE_QRCODE("invoiceQrcode"),
    //冲红
    ACTION_INVOICE_REVOKE("invoiceRevoke"),
    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<String> helper;

    private OrderActionEnum(String value) {
        helper = Helper.valueHelper(value);
    }

    private OrderActionEnum() {
        helper = Helper.unknownHelper();
    }

    @Override
    public String value() {
        return helper.value();
    }

    @Override
    public boolean isUnknownEnum() {
        return helper.isUnknownEnum();
    }

    @Override
    public void setUnknownValue(String value) {
        helper.setUnknownValue(value);
    }

    @Override
    public boolean equalsValue(String value) {
        return helper.equalsValue(this, value);
    }

    @Override
    public String toString() {
        return "" + value();
    }
}
