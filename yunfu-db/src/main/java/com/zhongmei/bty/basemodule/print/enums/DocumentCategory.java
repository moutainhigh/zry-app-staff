package com.zhongmei.bty.basemodule.print.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum DocumentCategory implements ValueEnum<Integer> {


    KITCHEN_SUMMARY(1),


    KITCHEN_DETAIL(2),


    LABEL(3),


    RECEIPT(4),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    DocumentCategory(Integer value) {
        helper = Helper.valueHelper(value);
    }

    DocumentCategory() {
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
