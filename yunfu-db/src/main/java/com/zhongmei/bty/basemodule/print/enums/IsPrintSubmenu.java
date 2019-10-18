package com.zhongmei.bty.basemodule.print.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum IsPrintSubmenu implements ValueEnum<Integer> {


    NOPRINT(0),

    PRINT(1),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private IsPrintSubmenu(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private IsPrintSubmenu() {
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
