package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 加菜记录处理状态
 */
public enum AddItemRecordStatus implements ValueEnum<Integer> {

    /**
     * 未处理
     */
    UNDEAL(1),

    /**
     * 已接受
     */
    ACCEPTED(2),

    /**
     * 已拒绝
     */
    REFUSED(3),


    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private AddItemRecordStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private AddItemRecordStatus() {
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
