package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 *

 *
 */
public enum TradeDealSettingBusinessType implements ValueEnum<Integer> {

    /**
     * 外卖订单
     */
    TAKEAWAY(1),

    /**
     * 预订订单
     */
    BOOKING(2),

    /**
     * 正餐
     */
    DINNER(3),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private TradeDealSettingBusinessType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private TradeDealSettingBusinessType() {
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
