package com.zhongmei.yunfu.db.enums;


import com.zhongmei.yunfu.util.ValueEnum;

public enum TakeDishStatus implements ValueEnum<Byte> {


    HAVE_TAKE_DISH((byte) 1),

    NOT_TAKE_DISH((byte) 0),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Byte> helper;

    TakeDishStatus(Byte value) {
        helper = Helper.valueHelper(value);
    }

    TakeDishStatus() {
        helper = Helper.unknownHelper();
    }

    @Override
    public Byte value() {
        return helper.value();
    }

    @Override
    public boolean equalsValue(Byte value) {
        return helper.equalsValue(this, value);
    }

    @Override
    public boolean isUnknownEnum() {
        return helper.isUnknownEnum();
    }

    @Override
    public void setUnknownValue(Byte value) {
        helper.setUnknownValue(value);
    }

    @Override
    public String toString() {
        return "" + value();
    }

}
