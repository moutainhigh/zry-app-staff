package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 配送单配送状态
 */
public enum DeliveryOrderStatus implements ValueEnum<Integer> {

    /**
     * 待下发
     */
    WAITING_CREATE(0),
    /**
     * 待接单
     */
    WAITING_ACCEPT(1),
    /**
     * 待取货
     */
    WAITING_PICK_UP(2),
    /**
     * 配送中
     */
    DELIVERYING(3),

    /**
     * 配送完成
     */
    REAL_DELIVERY(4),

    /**
     * 配送取消
     */
    DELIVERY_CANCEL(5),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private DeliveryOrderStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private DeliveryOrderStatus() {
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
