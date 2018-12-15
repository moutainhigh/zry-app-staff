package com.zhongmei.bty.basemodule.devices.mispos.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 卡状态
 */
public enum CardIsBind implements ValueEnum<Integer> {

    /**
     * 已绑定
     */
    YES(1),

    /**
     * 未绑定
     */
    NO(2),


    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    CardIsBind(Integer value) {
        helper = Helper.valueHelper(value);
    }

    CardIsBind() {
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

