package com.zhongmei.bty.basemodule.discount.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 微信卡券类型
 * Created by demo on 2018/12/15
 */
public enum WeiXinCardType implements ValueEnum<String> {

    /**
     * 代金券
     */
    CASH("CASH"),

    /**
     * 未知的值，为了避免转为enum出错设置的，不应该直接使用
     *
     * @deprecated
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<String> helper;

    private WeiXinCardType(String value) {
        helper = Helper.valueHelper(value);
    }

    private WeiXinCardType() {
        helper = Helper.unknownHelper();
    }

    @Override
    public String value() {
        return helper.value();
    }

    @Override
    public boolean equalsValue(String value) {
        return helper.equalsValue(this, value);
    }

    @Override
    public boolean isUnknownEnum() {
        return helper.isUnknownEnum();
    }

    @Override
    public void setUnknownValue(String value) {
        helper.setUnknownValue(value);
    }

    @Override
    public String toString() {
        return "" + value();
    }
}
