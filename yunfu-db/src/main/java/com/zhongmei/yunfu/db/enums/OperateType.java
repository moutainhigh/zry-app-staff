package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * @Date：2015年12月18日 下午3:52:16
 * @Description: 理由产生的操作类型
 * @Version: 1.0
 */
public enum OperateType implements ValueEnum<Integer> {

    /**
     * 无单退货操作
     */
    TRADE_HAVENOORDER_RETURN(1),
    /**
     * 整单退货操作
     */
    TRADE_RETURNED(2),
    /**
     * 退菜操作
     */
    ITEM_RETURN_QTY(3),
    /**
     * 订单反结账理由
     */
    TRADE_REPEATED(4),
    /**
     * 正餐作废操作
     */
    TRADE_DINNER_INVALID(5),
    /**
     * 快餐作废操作
     */
    TRADE_FASTFOOD_INVALID(6),

    /**
     * 正餐拒绝操作
     */
    TRADE_DINNER_REFUSE(7),

    /**
     * 快餐拒绝操作
     */
    TRADE_FASTFOOD_REFUSE(8),

    /**
     * 正餐免单操作
     */
    TRADE_DINNER_FREE(9),

    /**
     * 快餐免单操作
     */
    TRADE_FASTFOOD_FREE(10),

    /**
     * 取消订单
     */
    TRADE_CANCELLED(11),


    /**
     * 挂账订单
     */
    TRADE_CREDIT(12),

    /**
     * 退押金
     */
    TRADE_DEPOSIT(13),

    /**
     * 整单折扣
     */
    TRADE_DISCOUNT(14),

    /**
     * 菜品赠送
     */
    ITEM_GIVE(15),

    /**
     * 宴请
     */
    TRADE_BANQUET(16),

    /**
     * 整单让价
     */
    TRADE_REBATE(17),

    /**
     * 单品折扣
     */
    TRADE_SINGLE_DISCOUNT(18),

    /**
     * 单品让价
     */
    TRADE_SINGLE_REBATE(19),
    /**
     * 问题菜品
     */
    TRADE_SINGLE_PROBLEM(20),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private OperateType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private OperateType() {
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
