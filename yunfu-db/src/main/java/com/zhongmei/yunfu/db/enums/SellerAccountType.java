package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 收款账号类型
 *
 * @version: 1.0
 * @date 2015年5月18日
 */
public enum SellerAccountType implements ValueEnum<Integer> {

    /**
     * 账号
     */
    SELF(1),

    /**
     * 商家账号
     */
    MERCHANT(2),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private SellerAccountType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private SellerAccountType() {
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
