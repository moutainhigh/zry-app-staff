package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;



public enum OrderActionEnum implements ValueEnum<String> {

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
        ACTION_HANDOVER("dinnerHandover"),
        ACTION_HANDOVER_LIST("dinnerHandoverList"),
        ACTION_HANDOVER_CALIBRATE("dinnerHandoverCalibrate"),
        ACTION_CLOSING("closing"),
        ACTION_CLOSING_HISTORY("closingHistory"),
        ACTION_CREDIT("credit"),
        ACTION_PAYMENTSEDIT("paymentsedit"),
        ACTION_REPORT_FORM("resportFormCenter"),
        ACTION_CLEAR_BALANCE("clearBalance"),
        ACTION_SETTINGS("settings"),
        ACTION_INVOICE_QRCODE("invoiceQrcode"),
        ACTION_INVOICE_REVOKE("invoiceRevoke"),

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
