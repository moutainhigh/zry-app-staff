package com.zhongmei.bty.basemodule.database.queue;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 *

 *
 */
public enum SuccessOrFaild implements ValueEnum<Integer> {

    /**
     * 成功
     */
    SUCCESS(0),
    /**
     * 失败
     */
    FAILD(-1),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private SuccessOrFaild(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private SuccessOrFaild() {
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
