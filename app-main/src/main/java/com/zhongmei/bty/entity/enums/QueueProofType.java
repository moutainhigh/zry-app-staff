package com.zhongmei.bty.entity.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum QueueProofType implements ValueEnum<String> {

        MMS("mms"),

        PRINT("print"),


    @Deprecated
    __UNKNOWN__;

    private final Helper<String> helper;

    private QueueProofType(String value) {
        helper = Helper.valueHelper(value);
    }

    private QueueProofType() {
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
        return value();
    }
}
