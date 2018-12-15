package com.zhongmei.bty.basemodule.commonbusiness.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 业态id
 * Created by demo on 2018/12/15
 */
public enum BusinessId implements ValueEnum<Integer> {

    /**
     * 中餐
     */
    CHINESE_FOOD(1),

    /**
     * 西餐
     */
    WESTERN_FOOD(2),

    /**
     * 火锅
     */
    HOT_POT(3),

    /**
     * 自助餐
     */
    BUFFET(4),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private BusinessId(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private BusinessId() {
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