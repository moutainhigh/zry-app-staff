package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

public enum CommercialType implements ValueEnum<Integer> {

    /**
     * 用券门店
     */
    COUPON_SHOP(1),

    /**
     * 消费门店
     */
    CONSUME_SHOP(2),

    /**
     * 升级会员门店
     */
    MEMBER_UPGRADE_SHOP(3),

    /**
     * 会员加入门店
     */
    MEMBER_JOIN_SHOP(4),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private CommercialType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private CommercialType() {
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
