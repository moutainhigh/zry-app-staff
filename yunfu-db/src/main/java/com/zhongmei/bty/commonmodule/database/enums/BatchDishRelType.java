package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;



public enum BatchDishRelType implements ValueEnum<Integer> {



    ITEM_EXTRA_TYPE(1),


    ITEM_PROPERTY_TYPE(2),


    ITEM_OPERATION_TYPE(3),


    @Deprecated
    __UNKNOWN__;

    private final ValueEnum.Helper<Integer> helper;

    private BatchDishRelType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private BatchDishRelType() {
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
