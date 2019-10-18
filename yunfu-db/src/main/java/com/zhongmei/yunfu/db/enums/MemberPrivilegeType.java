package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum MemberPrivilegeType implements ValueEnum<Integer> {


    DISCOUNT(1),


    REBATE(2),


    PRICE(3),



    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private MemberPrivilegeType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private MemberPrivilegeType() {
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

    public MemberPrivilegeType createPrivilegeType(int value) {
        for (MemberPrivilegeType privilegeType : MemberPrivilegeType.values()) {
            if (privilegeType.value() == value) {
                return privilegeType;
            }
        }
        return __UNKNOWN__;
    }

}
