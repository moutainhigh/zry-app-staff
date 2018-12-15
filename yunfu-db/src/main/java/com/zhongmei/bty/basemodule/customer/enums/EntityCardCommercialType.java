package com.zhongmei.bty.basemodule.customer.enums;

import com.zhongmei.yunfu.util.ValueEnum;

public enum EntityCardCommercialType implements ValueEnum<Integer> {

    /**
     * 可售门店
     */
    SELL_SHOP(1),

    /**
     * 使用门店
     */
    USE_SHOP(2),

    /**
     * 储值门店
     */
    STORED_VALUE_SHOP(3),

    /**
     * 消费门店
     */
    CONSUME_SHOP(4),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private EntityCardCommercialType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private EntityCardCommercialType() {
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
