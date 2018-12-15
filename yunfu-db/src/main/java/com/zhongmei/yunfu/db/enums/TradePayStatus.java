package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 支付状态
 *
 * @Date：2015年3月31日
 * @Description:
 * @Version: 5.0
 */
public enum TradePayStatus implements ValueEnum<Integer> {

    /**
     * 未支付
     */
    UNPAID(1),
    /**
     * 支付中
     */
    PAYING(2),
    /**
     * 已支付
     */
    PAID(3),

    /**
     * 退款中
     */
    REFUNDING(4),

    /**
     * 已退款
     */
    REFUNDED(5),

    /**
     * 退款失败
     */
    REFUND_FAILED(6),

    /**
     * 预支付
     */
    PREPAID(7),

    /**
     * 等待退款
     */
    WAITING_REFUND(8),

    /**
     * 支付失败
     */
    PAID_FAIL(9),

    /**
     * 重复支付
     */
    REPEAT_PAID(10),

    /**
     * 支付异常
     */
    PAID_ERROR(11),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private TradePayStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private TradePayStatus() {
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
