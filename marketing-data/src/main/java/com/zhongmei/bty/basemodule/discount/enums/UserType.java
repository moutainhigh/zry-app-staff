package com.zhongmei.bty.basemodule.discount.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * @date:2016年5月18日下午3:55:45
 */
public enum UserType implements ValueEnum<Integer> {

    MEMBER(1), // 会员
    MEMBERNON(2),//非会员
    ALL(-100), //全部人员，本地创建的不和服务器相关联

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated __UNKNOWN__;

    private final Helper<Integer> helper;

    private UserType(Integer value) {
        // TODO Auto-generated constructor stub
        helper = Helper.valueHelper(value);
    }

    private UserType() {
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