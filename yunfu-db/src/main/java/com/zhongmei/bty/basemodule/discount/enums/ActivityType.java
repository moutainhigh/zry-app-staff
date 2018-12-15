package com.zhongmei.bty.basemodule.discount.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 营销方式
 *
 * @date:2016年4月27日下午3:14:14
 */
public enum ActivityType implements ValueEnum<Integer> {
    //	单商品活动
    SINGLE(1),
    //	多商品活动
    MULTI(2),
    ALL(-100),//所有活动类型，只用于本地查询使用
    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private ActivityType(Integer value) {
        // TODO Auto-generated constructor stub
        helper = Helper.valueHelper(value);
    }

    private ActivityType() {
        // TODO Auto-generated constructor stub
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
