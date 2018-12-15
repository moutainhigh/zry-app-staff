package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 *

 *
 */
public enum PaySource implements ValueEnum<Integer> {

    /**
     * Cashier
     */
    CASHIER(1),

    /**
     * Kiosk
     */
    KIOSK(2),

    /**
     * 百度外卖
     */
    BAIDU_TAKEOUT(3),

    /**
     * 百度糯米
     */
    BAIDU_RICE(4),

    /**
     * 百度地图
     */
    BAIDU_MAP(5),

    /**
     * Loyal
     */
    LOYAL(6),

    /**
     * OnMobile
     */
    ON_MOBILE(7),

    /**
     * 快捷支付
     */
    QUICK_PAY(8),

    /**
     * 熟客
     */
    FAMILIAR(9),

    /**
     * portal
     */
    PORTAL(10),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PaySource(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PaySource() {
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
