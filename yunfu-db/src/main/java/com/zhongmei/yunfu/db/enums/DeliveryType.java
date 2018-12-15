package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 交付方式
 */
public enum DeliveryType implements ValueEnum<Integer> {

    /**
     * 内用，在店内点餐，可以绑定桌号或者叫餐号
     */
    HERE(1),

    /**
     * 外送，需要填写收货人信息和期望送达时间
     */
    SEND(2),

    /**
     * 自提，需要填写取货人和约定取货时间
     */
    TAKE(3),

    /**
     * 打包（外带），顾客立即拿走的
     */
    CARRY(4),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private DeliveryType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private DeliveryType() {
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
