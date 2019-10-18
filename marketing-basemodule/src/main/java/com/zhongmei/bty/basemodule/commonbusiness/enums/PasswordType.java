package com.zhongmei.bty.basemodule.commonbusiness.enums;

import com.zhongmei.yunfu.util.ValueEnum;




public enum PasswordType implements ValueEnum<Integer> {


    SOURCE_CODE(1),

    MD5_CODE(2),

    TOKEN_CODE(3),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PasswordType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PasswordType() {
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