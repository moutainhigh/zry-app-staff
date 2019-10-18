package com.zhongmei.bty.basemodule.notifycenter.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum BizStatus implements ValueEnum<Integer> {


    UNPROCESSED(1),


    CONFIRMED(2),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private BizStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private BizStatus() {
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
