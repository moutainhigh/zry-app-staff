package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 交易类型
 *
 * @Date：2015年3月31日
 * @Description:
 * @Version: 5.0
 */
public enum TradeType implements ValueEnum<Integer> {

    /**
     * 售货
     */
    SELL(1),

    /**
     * 退货
     */
    REFUND(2),

    /**
     * 被拆单支付拆出来的单
     */
    SPLIT(3),

    /**
     * 反结账产生的新单
     */
    SELL_FOR_REPEAT(4),

    /**
     * 反结账产生的退货单
     */
    REFUND_FOR_REPEAT(5),

    /**
     * 冲账售货单。删除SELL_FOR_REPEAT单中的商品后产生
     */
    SELL_FOR_REVERSAL(6),

    /**
     * 冲账退货单。删除SELL_FOR_REPEAT单中的商品后产生
     */
    REFUND_FOR_REVERSAL(7),

    /**
     * 联台主单
     */
    UNOIN_TABLE_MAIN(9),

    /**
     * 联台子单
     */
    UNOIN_TABLE_SUB(10),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private TradeType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private TradeType() {
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

    public static boolean hasUnionTable(TradeType type) {
        return type != null && (type == UNOIN_TABLE_MAIN || type == UNOIN_TABLE_SUB);
    }

    @Override
    public String toString() {
        return "" + value();
    }

}
