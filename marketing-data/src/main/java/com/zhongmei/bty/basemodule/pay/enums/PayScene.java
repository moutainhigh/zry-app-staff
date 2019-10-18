package com.zhongmei.bty.basemodule.pay.enums;

import com.zhongmei.yunfu.util.ValueEnum;

import java.io.Serializable;




public enum PayScene implements ValueEnum<Integer>, Serializable {


    SCENE_CODE_CHARGE(1),


    SCENE_CODE_SHOP(2),

    SCENE_CODE_WRITEOFF(3),


    SCENE_CODE_BAKERY_BOOKING(4),


    SCENE_CODE_BUFFET_DEPOSIT(5),

    SCENE_CODE_BOOKING_DEPOSIT(6),

    SCENE_CODE_BAKERY_BOOKING_DEPOSIT(7),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PayScene(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PayScene() {
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