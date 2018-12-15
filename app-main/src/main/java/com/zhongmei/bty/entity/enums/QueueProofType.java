package com.zhongmei.bty.entity.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * Created by demo on 2018/12/15
 */
public enum QueueProofType implements ValueEnum<String> {

    //短信
    MMS("mms"),

    //打印
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
