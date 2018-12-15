package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 业务形态
 */
public enum BusinessType implements ValueEnum<Integer> {
    /**
     * 美业
     */
    BEAUTY(1),

    /**
     * 余额充值
     */
    ONLINE_RECHARGE(2),

    /**
     * 次卡充值
     */
    CARD_TIME(3)

    /**待删除**/
    ,
    /**
     * 快餐
     */
    SNACK(4),

    /**
     * 正餐
     */
    DINNER(5),

    /**
     * 外卖
     */
    TAKEAWAY(6),


    /**
     * POS支付
     */
    POS_PAY(5),

    /**
     * 微信商品
     */
    WEIXIN_PRODUCT(6),

    /**
     * 快捷支付
     */
    QUICK_PAY(7),

    /**
     * 实体卡出售
     */
    CARD(8),

    /**
     * 实体卡储值
     */
    CARD_RECHARGE(9),

    /**
     * 匿名实体卡售卡及储值
     */
    ANONYMOUS_ENTITY_CARD_SELL_AND_RECHARGE(10),

    /**
     * 匿名实体卡售卡
     */
    ANONYMOUS_ENTITY_CARD_SELL(11),

    /**
     * 匿名实体卡储值
     */
    ANONYMOUS_ENTITY_CARD_RECHARGE(12),

    /**
     * 匿名实体卡退卡
     */
    ANONYMOUS_ENTITY_CARD_RETURN(13),
    /**
     * 销账
     */
    WRITEOFF(14),
    /**
     * 团餐
     */
    GROUP(15),

    /**
     * 自助餐
     */
    BUFFET(16),

    /**
     * 换卡
     */
    ENTITY_CARD_CHANGE(17),

    /**
     * 预订单
     */
    BOOKING_LIST(18),

    /**
     * 尾款单
     */
    TAILING_LIST(19),
    /**
     * 烘焙押金
     */
    BEKEAY_DEPOSIT(21),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private BusinessType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private BusinessType() {
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
