package com.zhongmei.bty.basemodule.orderdish.enums;

/**
 * Created by demo on 2018/12/15
 */

import com.zhongmei.yunfu.util.ValueEnum;

public enum ExtraItemType implements ValueEnum<Integer> {

    DEPOSIT(1), // 押金

    OUTTIME_FEE(2), // 超时费

    MIN_CONSUM(3), // 最低消费附加


    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private ExtraItemType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private ExtraItemType() {
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
