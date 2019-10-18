package com.zhongmei.bty.basemodule.orderdish.enums;



import com.zhongmei.yunfu.util.ValueEnum;

public enum ExtraItemType implements ValueEnum<Integer> {

    DEPOSIT(1),
    OUTTIME_FEE(2),
    MIN_CONSUM(3),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private ExtraItemType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private ExtraItemType() {
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
