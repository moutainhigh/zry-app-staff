package com.zhongmei.bty.basemodule.orderdish.enums;

import com.zhongmei.yunfu.util.ValueEnum;



public enum ShopcartItemType implements ValueEnum<Integer> {
        COMMON(1),
        MAINBATCH(2),
        MAINSUB(3),
        SUBBATCH(4),
        SUBBATCHMODIFY(5),
        SUB(6),
        CARD_SERVICE(7),
        SMALL_PROGRAM(8);

    private final Helper<Integer> helper;

    private ShopcartItemType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private ShopcartItemType() {
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
