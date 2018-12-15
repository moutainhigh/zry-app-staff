package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 *

 *
 */
public enum PayType implements ValueEnum<Integer> {

    /**
     * 网页
     */
    WEB(1),
    /**
     * 主扫。生成二维码给顾客扫
     */
    QCODE(2),
    /**
     * 被扫。扫顾客提供的条码
     */
    SCAN(3),

    /**
     * APP
     */
    APP(4),

    /**
     * 快捷支付
     */
    QUICK_PAY(99),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PayType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PayType() {
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
