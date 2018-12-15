package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 预订限制单位
 */
public enum LimitBookingUnit implements ValueEnum<Integer> {
    /**
     * 天
     */
    DAY(1),

    /**
     * 周
     */
    WEEK(2),

    /**
     * 月
     */
    MONTH(3),


    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private LimitBookingUnit(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private LimitBookingUnit() {
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
