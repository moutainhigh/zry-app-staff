package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

public enum RefundWay implements ValueEnum<Integer> {

    /**
     * 无需退款
     */
    NONEED_REFUND(1),
    /**
     * 自动退款
     */
    AUTO_REFUND(2),
    /**
     * 手动退款
     */
    HAND_REFUND(3),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private RefundWay(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private RefundWay() {
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
