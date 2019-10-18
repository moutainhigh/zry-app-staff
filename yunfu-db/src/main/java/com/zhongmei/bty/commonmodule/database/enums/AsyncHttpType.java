package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum AsyncHttpType implements ValueEnum<Integer> {
        MODIFYTRADE(1),
        CASHER(2),
        OPENTABLE(3),
        UNION_MAIN_MODIFYTRADE(4),
        UNION_SUB_MODIFYTRADE(5),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private AsyncHttpType(Integer value) {
                helper = Helper.valueHelper(value);
    }

    private AsyncHttpType() {
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
