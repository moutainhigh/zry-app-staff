package com.zhongmei.bty.basemodule.customer.enums;

import com.zhongmei.yunfu.util.ValueEnum;

public enum CustomerLoginType implements ValueEnum<Integer> {


    //"手机号码"
    MOBILE(1),

    //"顾客customerId"
    MEMBER_ID(2),

    //"微信openid"
    WECHAT_OPENID(3),

    //"微信会员卡卡号"
    WECHAT_MEMBERCARD_ID(101),

    //"人脸faceCode"
    FACE_CODE(104),


    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
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
