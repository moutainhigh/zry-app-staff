package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 配送子状态
 */
public enum DeliveryOrderSubStatus implements ValueEnum<Integer> {

    /**
     * 商户取消
     */
    MERCHANT_CANCEL(501),
    /**
     * 配送员取消
     */
    DELIVERY_MAN_CANCEL(502),

    /**
     * 配送异常取消可再下发
     */
    DELIVERY_ERROR_RECREATE_ALLOW(503),

    /**
     * 配送异常取消不可再下发
     */
    DELIVERY_ERROR_RECREATE_FORBID(504),

    /**
     * 用户取消
     */
    USER_CANCEL(505),

    /**
     * 系统取消
     */
    SYSTEM_CANCEL(506),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private DeliveryOrderSubStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private DeliveryOrderSubStatus() {
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
