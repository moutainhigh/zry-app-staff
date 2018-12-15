package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * @version: 1.0
 * @date 2015年7月10日
 */
public enum CouponType implements ValueEnum<Integer> {

    /**
     * 满减券
     */
    REBATE(4),
    /**
     * 折扣券
     */
    DISCOUNT(1),
    /**
     * 礼品券
     */
    GIFT(3),
    /**
     * 代金券
     */
    CASH(2),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private CouponType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private CouponType() {
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
