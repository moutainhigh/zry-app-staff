package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum ChargePrivilegeType implements ValueEnum<Integer> {


    DISCOUNT(3),


    REBATE(4),



    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private ChargePrivilegeType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private ChargePrivilegeType() {
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

    public ChargePrivilegeType createPrivilegeType(int value) {
        for (ChargePrivilegeType privilegeType : ChargePrivilegeType.values()) {
            if (privilegeType.value() == value) {
                return privilegeType;
            }
        }
        return __UNKNOWN__;
    }

}
