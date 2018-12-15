package com.zhongmei.bty.basemodule.orderdish.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * Created by demo on 2018/12/15
 */

public enum ShopcartItemType implements ValueEnum<Integer> {
    //和主单、子单没有关系的item
    COMMON(1),
    //    主单批量菜
    MAINBATCH(2),
    //主单子单菜
    MAINSUB(3),
    //    子单批量菜
    SUBBATCH(4),
    //子单批量菜改
    SUBBATCHMODIFY(5),
    //    子单菜
    SUB(6),
    //次卡服务
    CARD_SERVICE(7),
    //小程序
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
