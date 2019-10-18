package com.zhongmei.bty.basemodule.discount.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum PromotionType implements ValueEnum<Integer> {
        MINUS(1),     DISCOUNT(2),     GIFT(3),    SPECAILPRICE(4),

    @Deprecated __UNKNOWN__;

    private final Helper<Integer> helper;

    private PromotionType(Integer value) {
                helper = Helper.valueHelper(value);
    }

    private PromotionType() {
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
