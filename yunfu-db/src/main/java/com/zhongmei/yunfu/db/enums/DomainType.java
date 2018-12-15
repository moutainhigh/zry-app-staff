package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 所属领域
 */
public enum DomainType implements ValueEnum<Integer> {

    /**
     * 美业
     */
    BEAUTY(1),

    //以下是无效的定义

    /**
     * 餐饮业
     */
    RESTAURANT(2),

    /**
     * 零售业
     */
    RETAIL(3),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private DomainType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private DomainType() {
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
