package com.zhongmei.bty.basemodule.orderdish.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 库存类型
 */
public enum WmType implements ValueEnum<Integer> {

    /**
     * 预制商品
     */
    PREFABRICATE(1),

    /**
     * 现制商品
     */
    CURRENT(2),

    /**
     * 外购商品
     */
    OUTSOURSING(3),

    /**
     * 原材料
     */
    COMMODITIES(4),

    /**
     * 半成品
     */
    SEMI(5),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private WmType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private WmType() {
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
