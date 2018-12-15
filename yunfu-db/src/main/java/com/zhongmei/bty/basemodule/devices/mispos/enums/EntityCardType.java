package com.zhongmei.bty.basemodule.devices.mispos.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 实体卡类型 1:会员实体卡  2:匿名实体卡
 */
public enum EntityCardType implements ValueEnum<Integer> {
    /**
     * 权益会员实体卡
     */
    CUSTOMER_ENTITY_CARD(1),

    /**
     * 匿名实体卡
     */
    ANONYMOUS_ENTITY_CARD(2),

    /**
     * 会员实体卡
     */
    GENERAL_CUSTOMER_CARD(3),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private EntityCardType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private EntityCardType() {
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
