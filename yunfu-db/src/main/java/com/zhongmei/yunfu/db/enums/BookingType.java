package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 预订单状态
 */
public enum BookingType implements ValueEnum<Integer> {

    /**
     * 默认(正餐预订老数据)
     */
    NORMAL(0),
    /**
     * 正餐
     */
    DINNER(1),

    /**
     * 团餐
     */
    GROUP(2),


    /**
     * 美业
     */
    BEAUTY(3),


    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private BookingType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private BookingType() {
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
