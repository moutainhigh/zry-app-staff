package com.zhongmei.bty.basemodule.customer.enums;

import com.zhongmei.yunfu.util.ValueEnum;

public enum CustomerLoginType implements ValueEnum<Integer> {


        MOBILE(1),

        MEMBER_ID(2),

        WECHAT_OPENID(3),

        CARD_NO_ENTITY(4),

        WECHAT_MEMBERCARD_ID(101),

        FACE_CODE(104),



    @Deprecated __UNKNOWN__;

    private final Helper<Integer> helper;

    CustomerLoginType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    CustomerLoginType() {
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
