package com.zhongmei.bty.basemodule.discount.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum WeiXinCardType implements ValueEnum<String> {


    CASH("CASH"),


    @Deprecated
    __UNKNOWN__;

    private final Helper<String> helper;

    private WeiXinCardType(String value) {
        helper = Helper.valueHelper(value);
    }

    private WeiXinCardType() {
        helper = Helper.unknownHelper();
    }

    @Override
    public String value() {
        return helper.value();
    }

    @Override
    public boolean equalsValue(String value) {
        return helper.equalsValue(this, value);
    }

    @Override
    public boolean isUnknownEnum() {
        return helper.isUnknownEnum();
    }

    @Override
    public void setUnknownValue(String value) {
        helper.setUnknownValue(value);
    }

    @Override
    public String toString() {
        return "" + value();
    }
}
