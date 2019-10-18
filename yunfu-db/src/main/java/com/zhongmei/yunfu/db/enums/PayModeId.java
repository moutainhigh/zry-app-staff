package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum PayModeId implements ValueEnum<Long> {


    MEMBER_CARD(1L),


    CASH(2L),


    BANK_CARD(3L),


    WEIXIN_PAY(4L),


    ALIPAY(5L),


    INTERGRAL(6L),


    COUPON(7L),


    OTHER_WX_PAY(101L),


    OTHER_ALI_PAY(102L),





    BAIFUBAO(-7L),


    BAIDU_ZHIDA(-8L),



    BAIDU_MAP(-10L),


    POS_CARD(-11L),


    BAIDU_RICE(-12L),


    BAIDU_TAKEOUT(-13L),


    ELM(-14L),


    ENTITY_CARD(-15L),

    DAZHONG_DIANPING(-16L),

    MEITUAN_TAKEOUT(-17L),

    DIANPING_COUPON(-18L),

    DIANPING_FASTPAY(-19L),


    ANONYMOUS_ENTITY_CARD(-20L),

    NUOMI_DIANCAI(-21L),

    THIRD_CLIENT(-22L),

    MEITUAN_FASTPAY(-23L),

    MEITUAN_TUANGOU(-24L),

    PURSE_LIFE(-25L),


    BAINUO_TUANGOU(-26L),

    JIN_CHENG(-29L),

    JIN_CHENG_VALUE_CARD(-30L),

    FENGHUO_WRISTBAND(-34L),

    KOUBEI_TUANGOU(-36L),

    UNIONPAY_CLOUD_PAY(-37L),


    ICBC_E_PAY(-38L),


    MOBILE_PAY(-40L),


    EARNEST_DEDUCT(-42L),


    DIANXIN_YIPAY(-101L),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Long> helper;

    private PayModeId(Long value) {
        helper = Helper.valueHelper(value);
    }

    private PayModeId() {
        helper = Helper.unknownHelper();
    }

    @Override
    public Long value() {
        return helper.value();
    }

    @Override
    public boolean equalsValue(Long value) {
        return helper.equalsValue(this, value);
    }

    @Override
    public boolean isUnknownEnum() {
        return helper.isUnknownEnum();
    }

    @Override
    public void setUnknownValue(Long value) {
        helper.setUnknownValue(value);
    }

    @Override
    public String toString() {
        return "" + value();
    }

    public static PayModeId toEnum(Long value) {
        if (value != null) {
            for (PayModeId _enum : PayModeId.values()) {
                if (_enum.value() == value) {
                    return _enum;
                }
            }
        }
        return __UNKNOWN__;
    }

}
