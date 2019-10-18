package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

public enum CommercialType implements ValueEnum<Integer> {


    COUPON_SHOP(1),


    CONSUME_SHOP(2),


    MEMBER_UPGRADE_SHOP(3),


    MEMBER_JOIN_SHOP(4),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private CommercialType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private CommercialType() {
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
