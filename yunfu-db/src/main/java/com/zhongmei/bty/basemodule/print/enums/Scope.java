package com.zhongmei.bty.basemodule.print.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 适用范围
 *
 * @version: 1.0
 * @date 2015年6月25日
 */
public enum Scope implements ValueEnum<Integer> {

    /**
     * 门店有效
     */
    SHOP(1),

    /**
     * 本PAD有效
     */
    PAD(2),

    /**
     * 未知的值，为了避免转为enum出错设置的，不应该直接使用
     *
     * @deprecated
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private Scope(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private Scope() {
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
