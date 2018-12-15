package com.zhongmei.bty.basemodule.print.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 是否打印枚举
 * <p>
 * Created by demo on 2018/12/15
 */

public enum IsPrint implements ValueEnum<Integer> {

    /**
     * 不打印
     */
    NOPRINT(2),

    /**
     * 打印
     */
    PRINT(1),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private IsPrint(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private IsPrint() {
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
