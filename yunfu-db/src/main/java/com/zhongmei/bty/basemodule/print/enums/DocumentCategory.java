package com.zhongmei.bty.basemodule.print.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * @version: 1.0
 * @date 2015年6月24日
 */
public enum DocumentCategory implements ValueEnum<Integer> {

    /**
     * 厨房总单
     */
    KITCHEN_SUMMARY(1),

    /**
     * 厨房详单(堂口单)
     */
    KITCHEN_DETAIL(2),

    /**
     * 标签
     */
    LABEL(3),

    /**
     * 消费清单
     */
    RECEIPT(4),

    /**
     * 未知的值，为了避免转为enum出错设置的，不应该直接使用
     *
     * @deprecated
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    DocumentCategory(Integer value) {
        helper = Helper.valueHelper(value);
    }

    DocumentCategory() {
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
