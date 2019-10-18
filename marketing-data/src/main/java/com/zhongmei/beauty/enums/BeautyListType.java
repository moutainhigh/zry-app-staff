package com.zhongmei.beauty.enums;

import com.zhongmei.yunfu.util.ValueEnum;



public enum BeautyListType implements ValueEnum<Integer> {
        UNSERVICE(1),
        OUTLINE(2),
        CANCELD(3),
        UNDEAL(4),;
    private final Helper<Integer> helper;

    private BeautyListType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private BeautyListType() {
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
