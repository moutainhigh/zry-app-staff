package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 桌台/号牌单据类型设置
 */
public enum SetDeliveryType implements ValueEnum<Integer> {

    /**
     * 无
     */
    NONE(0),

    /**
     * 内用，在店内点餐，可以绑定桌号或者叫餐号
     */
    HERE(1),

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

    private SetDeliveryType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private SetDeliveryType() {
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
