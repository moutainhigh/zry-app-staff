package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * @version: 1.0
 * @date 2015年6月24日
 */
public enum PrinterKind implements ValueEnum<Integer> {

    /**
     * 小票(ESC/POS)
     */
    RECEIPT(1),

    /**
     * 标签
     */
    LABEL(2),

    /**
     * 未知的值，为了避免转为enum出错设置的，不应该直接使用
     *
     * @deprecated
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PrinterKind(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PrinterKind() {
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
