package com.zhongmei.bty.mobilepay.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 团购券状态
 * Created by demo on 2018/12/15
 */
public enum TicketInfoStatus implements ValueEnum<Integer> {

    /**
     * 券码不存在
     */
    NON_EXISTENT(-1),

    /**
     * 未使用
     */
    NOT_USED(1),

    /**
     * 已使用
     */
    USED(2),

    /**
     * 已过期
     */
    EXPIRED(3),

    /**
     * 已退款
     */
    REFUNDED(4),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private TicketInfoStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private TicketInfoStatus() {
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
