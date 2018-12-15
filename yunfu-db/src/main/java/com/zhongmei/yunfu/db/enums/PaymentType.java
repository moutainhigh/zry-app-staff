package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 付款类型
 *
 * @Date：2015年3月31日
 * @Description:
 * @Version: 5.0
 */
public enum PaymentType implements ValueEnum<Integer> {

    /**
     * 交易支付
     */
    TRADE_SELL(1),

    /**
     * 交易退款
     */
    TRADE_REFUND(2),

    /**
     * 会员充值(pad支付/微信上支付,没有mac地址)
     */
    MEMBER_RECHARGE(3),

    /**
     * 会员储值退款
     */
    MEMBER_REFUND(4),

    /**
     * 次卡购买
     */
    CARD_TIME_SELL(5),

    /**
     * 次卡退卡，
     */
    CARD_TIME_REFUND(6),

    /**
     * 调账
     */
    ADJUST(7),

    /**以下是无用类型,后期删除*/

    /**
     * 交易预支付
     */
    TRADE_PREPAID(13),

    /**
     * 实体卡储值
     */
    ENTITY_CARD_RECHARGE(16),

    /**
     * 收押金
     */
    DEPOSIT_PAY(17),

    /**
     * 退押金
     */
    DEPOSIT_REFUND(18),
    /**
     * 匿名卡储值
     */
    ANONYMOUS_ENTITY_CARD_RECHARGE(19),
    /**
     * 匿名卡售卡并储值
     */
    ANONYMOUS_ENTITY_CARD_SELL_RECHARGE(10),

    /**
     * 匿名卡售卡
     */
    ANONYMOUS_ENTITY_CARD_SELL(11),

    /**
     * 匿名卡退卡
     */
    ANONYMOUS_ENTITY_CARD_REFUND(12),
    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PaymentType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PaymentType() {
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
