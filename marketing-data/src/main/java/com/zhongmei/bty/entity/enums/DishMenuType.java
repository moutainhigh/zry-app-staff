package com.zhongmei.bty.entity.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 菜单模板类型
 * Created by demo on 2018/12/15
 */

public enum DishMenuType implements ValueEnum<Integer> {

    /**
     * 非通用模板
     */
    NON_CURRENCY(0),
    /**
     * 通用模板
     */
    CURRENCY(1),

    /**
     * 自定义模板
     */
    CUSTOM(2),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private DishMenuType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private DishMenuType() {
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
