package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum InvalidType implements ValueEnum<Integer> {


    RETURN_QTY(1),


    SPLIT(2),


    DELETE(3),


    MODIFY_DISH(4),


    DELETE_RETURN_QTY(5),


    DELETE_MODIY_DISH(6),

    SUB_BATCH_VITURAL(8),



    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private InvalidType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private InvalidType() {
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
