package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum CouponType implements ValueEnum<Integer> {


    REBATE(4),

    DISCOUNT(1),

    GIFT(3),

    CASH(2),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private CouponType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private CouponType() {
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
