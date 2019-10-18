package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum YesOrNo implements ValueEnum<Integer> {


    YES(1),

    NO(0),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private YesOrNo(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private YesOrNo() {
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
