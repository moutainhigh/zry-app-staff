package com.zhongmei.bty.basemodule.customer.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum FullSend implements ValueEnum<Integer> {


    YES(0),

    NO(1),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private FullSend(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private FullSend() {
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
