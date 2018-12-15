package com.zhongmei.bty.mobilepay.enums;

import com.zhongmei.yunfu.util.ValueEnum;

import java.io.Serializable;

/**
 * 支付场景值枚举
 * Created by demo on 2018/12/15
 */


public enum PayActionPage implements ValueEnum<Integer>, Serializable {

    /**
     * 结算界面
     */
    BALANCE(1),

    /**
     * 通用支付界面(快餐界面)
     */
    COMPAY(2),

    /**
     * 充值界面
     */
    CHARGE(3),

    /**
     * 销账界面
     */
    WRITEOFF(5),

    /**
     * 预定交订金界面
     */
    BOOKINGDEPOSIT(6),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PayActionPage(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PayActionPage() {
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