package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum IssueStatus implements ValueEnum<Integer> {


    PAUSE(1),

    DIRECTLY(2),

    ISSUING(3),

    FINISHED(4),

    FAILED(5),


    DIRECTLY_FROM_CLOUD(6),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private IssueStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private IssueStatus() {
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
