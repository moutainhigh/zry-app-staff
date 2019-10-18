package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;




public enum AsyncHttpState implements ValueEnum<Integer> {

        EXCUTING(2),

        SUCCESS(1),

        RETRING(0),


        FAILED(-1),



    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private AsyncHttpState(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private AsyncHttpState() {
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
