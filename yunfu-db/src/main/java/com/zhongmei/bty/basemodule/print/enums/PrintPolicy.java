package com.zhongmei.bty.basemodule.print.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * @version: 1.0
 * @date 2015年6月24日
 */
public enum PrintPolicy implements ValueEnum<Integer> {

    /**
     * 单品项出单（即一个品项一张单据）
     */
    SINGLE(2),

    /**
     * 全品项出单（即所有品项一张单据）
     */
    GATHER(1),

    /**
     * 复合出单（既单品项出单，又全品项出单）
     */
    ALL(3),

    /**
     * 未知的值，为了避免转为enum出错设置的，不应该直接使用
     *
     * @deprecated
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PrintPolicy(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PrintPolicy() {
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
