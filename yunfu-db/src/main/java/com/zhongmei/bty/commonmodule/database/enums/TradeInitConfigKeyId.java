package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;

public enum TradeInitConfigKeyId implements ValueEnum<Integer> {

    CURRENCY_TYPE(1), //货币类型
    SERVICE_CHARGE_RATE(2),//服务费费率
    /**
     * {@link KouBeiNumberType}
     */
    KOU_BEI_NUMBER_TYPE(3),//口碑 桌台号:1,号牌:2

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final ValueEnum.Helper<Integer> helper;

    private TradeInitConfigKeyId(Integer value) {

        helper = ValueEnum.Helper.valueHelper(value);
    }

    private TradeInitConfigKeyId() {
        helper = ValueEnum.Helper.unknownHelper();
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
