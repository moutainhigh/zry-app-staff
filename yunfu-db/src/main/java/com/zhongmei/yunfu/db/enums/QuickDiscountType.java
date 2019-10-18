package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum QuickDiscountType implements ValueEnum<Integer> {


    MEI_TUAN_TUAN_GOU(1),


    MEI_TUAN_QUICK_DISCOUNT(2),


    BAI_DU_NUO_MI_TUAN_GOU(3),


    CUSTOM_PAY(4),


    WEIXIN_PAY(5),


    ALI_PAY(6),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private QuickDiscountType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private QuickDiscountType() {
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
