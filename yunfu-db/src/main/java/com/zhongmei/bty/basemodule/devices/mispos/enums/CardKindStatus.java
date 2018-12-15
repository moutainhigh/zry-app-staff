package com.zhongmei.bty.basemodule.devices.mispos.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * @version: 1.0
 * @date 2016年3月11日
 */
public enum CardKindStatus implements ValueEnum<Integer> {

    /**
     * 未发行
     */
    UNISSUED(1),

    /**
     * 发行中
     */
    ISSUED(2),

    /**
     * 已停用
     */
    DISABLED(3),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private CardKindStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private CardKindStatus() {
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
