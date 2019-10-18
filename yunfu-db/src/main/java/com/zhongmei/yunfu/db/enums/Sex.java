package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum Sex implements ValueEnum<Integer> {


    MALE(1),

    FEMALE(0),

    UNKNOW(-1),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private Sex(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private Sex() {
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

    public static Sex toEnum(int sex) {
        for (Sex _sex : Sex.values()) {
            if (_sex.value() == sex) {
                return _sex;
            }
        }
        return UNKNOW;
    }

}
