package com.zhongmei.bty.basemodule.notifycenter.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * Created by demo on 2018/12/15
 */
public enum BizStatus implements ValueEnum<Integer> {

    /**
     * 未处理
     */
    UNPROCESSED(1),

    /**
     * 已认领
     */
    CONFIRMED(2),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private BizStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private BizStatus() {
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
