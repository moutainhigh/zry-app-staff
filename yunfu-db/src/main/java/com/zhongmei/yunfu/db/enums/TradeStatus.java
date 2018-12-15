package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 交易状态
 *
 * @Date：2015年3月31日
 * @Description:
 * @Version: 5.0
 */
public enum TradeStatus implements ValueEnum<Integer> {

    /**
     * 未处理
     */
    UNPROCESSED(1),

    /**
     * 挂单，不需要厨房打印
     */
    TEMPORARY(2),

    /**
     * 已确认
     */
    CONFIRMED(3),

    /**
     * 已完成(全部支付)
     */
    FINISH(4),

    /**
     * 已退货
     */
    RETURNED(5),

    /**
     * 已作废
     */
    INVALID(6),

    /**
     * 已拒绝
     */
    REFUSED(7),

    /**
     * 已取消
     */
    CANCELLED(8),

    /**
     * 重新退款
     */
    TOREFUND(9),

    /**
     * 被反结账
     */
    REPEATED(10),
    /**
     * 挂账
     */
    CREDIT(11),
    /**
     * 已销账
     */
    WRITEOFF(12),
    /**
     * 待清账
     */
    SQUAREUP(13),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private TradeStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private TradeStatus() {
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
