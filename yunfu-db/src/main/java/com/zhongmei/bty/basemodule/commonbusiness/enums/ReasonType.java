package com.zhongmei.bty.basemodule.commonbusiness.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 理由类型
 *
 * @version: 1.0
 * @date 2015年12月16日
 */
public enum ReasonType implements ValueEnum<Integer> {

    /**
     * 订单作废理由
     */
    TRADE_INVALID(4),
    /**
     * 订单拒绝理由
     */
    TRADE_REFUSED(1),
    /**
     * 订单反结账理由
     */
    TRADE_REPEATED(5),
    /**
     * 订单免单理由
     */
    TRADE_FREE(7),
    /**
     * 单菜品赠送理由
     */
    ITEM_GIVE(6),
    /**
     * 订单退菜理由
     */
    ITEM_RETURN_QTY(3),
    /**
     * 订单退货理由
     */
    TRADE_RETURNED(8),

    /**
     * 预订拒绝理由
     */
    BOOKING_REFUSED(21),
    /**
     * 订单取消理由
     */
    BOOKING_CANCEL(20),

    /**
     * 客户端自定义:拒绝退单
     */
    REFUSE_RETURN(-1),

    /**
     * 客户端自定义：同意退单
     */
    AGREE_RETURN(-2),

    /**
     * 挂账理由
     */
    LAG_REASON(9),

    /**
     * 押金部分退还理由
     */
    DEPOSIT_RETURN(40),

    /**
     * 整单折扣理由
     */
    TRADE_DISCOUNT(51),

    /**
     * 单品让价理由
     */
    TRADE_SINGLE_DISCOUNT(52),

    /**
     * 宴请理由
     */
    TRADE_BANQUET(30),

    /**
     * 会员积分修改
     */
    INTEGRAL_MODIFY(60),
    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private ReasonType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private ReasonType() {
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


    public static ReasonType newReason(int type) {
        for (ReasonType reasonType : ReasonType.values()) {
            if (reasonType.value() == type) {
                return reasonType;
            }
        }
        return __UNKNOWN__;
    }
}
