package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;



public enum DishType implements ValueEnum<Integer> {


    SINGLE(0),


    COMBO(1),


    EXTRA(2),


    CARD(5),


    SERVER_COMBO_PART(3),


    SERVER_COMBO_ALL(4),


    ANONYMOUS_ENTITY_CARD_SELL(10),


    ANONYMOUS_ENTITY_CARD_RECHARGE(11),


    MEAL_SHELL(12),


    BUFFET_COMBO_SHELL(13),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private DishType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private DishType() {
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
