package com.zhongmei.bty.entity.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 自助餐类型
 */
public enum BuffetType implements ValueEnum<Integer> {

    /**
     * 传统自助
     */
    TRADITION(1),

    /**
     * 点餐模式
     */
    DISHMENU(2),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private BuffetType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private BuffetType() {
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
