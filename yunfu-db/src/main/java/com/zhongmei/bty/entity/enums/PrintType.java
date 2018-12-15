package com.zhongmei.bty.entity.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * Created by demo on 2018/12/15
 */
public enum PrintType implements ValueEnum<Integer> {

    CUSTOM(1),

    KITCHEN(2),

    /**
     * 未知的值，为了避免转为enum出错设置的，不应该直接使用
     *
     * @deprecated
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PrintType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PrintType() {
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
