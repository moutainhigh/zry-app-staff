package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum PrintOperationOpType implements ValueEnum<Integer> {


    DISH_SEQUENCE,

    BATCH_OPERATION,

    DINNER_TRANSFER(1),


    DINNER_MERGE(2),


    REMIND_DISH(3),


    DINNER_PAY(4),


    WAKE_UP(5),


    RISE_DISH(6),


    DINNER_PRE_CASH(7),


    KITCHEN_PRINT(8),


    RECEIPT(9),


    DINNER_MOVE_DISH(10),


    DINNER_MODIFY_TRADE(11),


    DINNER_INVALID_TRADE(12),

        CUSTOMER_CHARGING(13),
        CUSTOMER_CONSUME(14),


    LABEL(15),


    MARKET_TABLE(16),

    DINNER_AUTO_ACCEPT_KITCHEN(17),


    CASHIER_REFUND_TICKET(18),


    CASHIER_CANCEL_TICKET(19),


    CASHIER_CANCEL_KITCHEN_PRINT(20),


    WAKE_UP_CANCEL(21),


    RISE_DISH_CANCEL(22),


    DINNER_REFUND(23),


    DINNER_REPRINT(24),


    TEST_TICKET(25),


    TANG_JIU_OPEN_PLATFORM(26),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PrintOperationOpType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PrintOperationOpType() {
        helper = Helper.unknownHelper();
    }

    @Override
    public Integer value() {
        return helper.value();
    }

    @Override
    public boolean equalsValue(Integer value) {
        return helper.equalsValue(this, value);
    }

    @Override
    public boolean isUnknownEnum() {
        return helper.isUnknownEnum();
    }

    @Override
    public void setUnknownValue(Integer value) {
        helper.setUnknownValue(value);
    }

    @Override
    public String toString() {
        return "" + value();
    }

}
