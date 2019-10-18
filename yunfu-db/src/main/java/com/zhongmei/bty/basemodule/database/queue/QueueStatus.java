package com.zhongmei.bty.basemodule.database.queue;

import com.zhongmei.yunfu.util.ValueEnum;


public enum QueueStatus implements ValueEnum<Integer> {

    QUEUEING(0),


    ADMISSION(1),


    INVALID(-1),


    CANCEL(-2),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private QueueStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private QueueStatus() {
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
