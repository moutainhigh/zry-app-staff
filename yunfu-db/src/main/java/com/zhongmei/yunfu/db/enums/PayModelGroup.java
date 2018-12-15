package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * @version: 1.0
 * @date 2015年5月8日
 */
public enum PayModelGroup implements ValueEnum<Integer> {

    /**
     * 在线支付
     */
    ONLINE(1),

    /**
     * 现金
     */
    CASH(2),

    /**
     * 银行卡
     */
    BANK_CARD(3),

    /**
     * 储值卡
     */
    VALUE_CARD(4),

    /**
     * 券类
     */
    COUPON(5),

    /**
     * 其它收款
     */
    OTHER(6),

    /**
     * 挂账，仅客户端使用
     */
    LAGPAY(119),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PayModelGroup(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PayModelGroup() {
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
