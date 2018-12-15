package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 由goujiabo创建
 * 营销活动是否生效
 * 创建时间 2016/5/27.
 */
public enum ActivityRuleEffective implements ValueEnum<Integer> {
    VALID(1), //已生效
    INVALID(2), //未生效

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated __UNKNOWN__;

    private final Helper<Integer> helper;

    private ActivityRuleEffective(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private ActivityRuleEffective() {
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
