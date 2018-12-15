package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 性别
 */
public enum Sex implements ValueEnum<Integer> {

    /**
     * 男
     */
    MALE(1),
    /**
     * 女
     */
    FEMALE(0),
    /**
     * 未知
     */
    UNKNOW(-1),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
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
