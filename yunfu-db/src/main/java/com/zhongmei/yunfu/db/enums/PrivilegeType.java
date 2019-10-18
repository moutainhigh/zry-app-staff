package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum PrivilegeType implements ValueEnum<Integer> {


    DISCOUNT(1),


    REBATE(2),


    FREE(3),


    COUPON(4),


    INTEGRALCASH(5),


    AUTO_DISCOUNT(6),


    PLATFORM(7),


    BUSINESS(8),


    LOGISTICS(9),


    AGENTS(10),


    MEMBER_PRICE(11),


    ADDITIONAL(12),


    MARKTING(13),


    VIP(14),


    WECHAT_CARD_COUPONS(15),


    BANQUET(16),


    PROBLEM(17),


    GIVE(18),


    SERVICE(21),


    CARD_SERVICE(22),


    COLLAGE(23),


    SECKILL(24),


    BARGAIN(25),


    SPECIAL_PRICE(29),


    MEMBER_REBATE(26),

    CHARGE_DISCOUNT(27),


    CHARGE_REBATE(28),




    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PrivilegeType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PrivilegeType() {
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

    public PrivilegeType createPrivilegeType(int value) {
        for (PrivilegeType privilegeType : PrivilegeType.values()) {
            if (privilegeType.value() == value) {
                return privilegeType;
            }
        }
        return __UNKNOWN__;
    }

}
