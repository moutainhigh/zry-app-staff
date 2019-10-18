package com.zhongmei.bty.basemodule.customer.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum CouponRuleType implements ValueEnum<Integer> {


    FIXED_AMOUNT(1),

    INVERSE_RANGE(2),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private CouponRuleType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private CouponRuleType() {
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
