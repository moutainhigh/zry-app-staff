package com.zhongmei.bty.entity.enums;

import com.zhongmei.yunfu.util.ValueEnum;



public enum NotifyType implements ValueEnum<Integer> {


    MOBILE(1),


    PHONE(2),


    CALL(3),

    MESSAGE(4),


    IVR_VOICE(5),


    WECHAT(6),

    IVR(7),

    @Deprecated __UNKNOWN__;

    private final Helper<Integer> helper;

    private NotifyType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private NotifyType() {
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
