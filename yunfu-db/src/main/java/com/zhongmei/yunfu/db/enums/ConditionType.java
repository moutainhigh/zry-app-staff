package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

public enum ConditionType implements ValueEnum<Integer> {



    CUSTOMER_TYPE(2),


    CUSTOMER_GROUP(3),


    AUTO_ORDER_SOURCE(4),



    CONSUME_TYPE(6),

    PAY_TYPE(7),

    TRADE_SOURCE(8),

    MEMBER_CONSUME_SOURCE(9),

    TRADE_TYPE(10),

    ACTIVITY_POS(11),

    MEAL_BACKGROUND(12),

    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private ConditionType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private ConditionType() {
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
