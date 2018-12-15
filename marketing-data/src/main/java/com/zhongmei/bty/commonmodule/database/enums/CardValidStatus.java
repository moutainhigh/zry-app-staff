package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 性别
 */
public enum CardValidStatus implements ValueEnum<Integer> {

    /**
     * 1：可用
     */
    EFFECTIVE(1),
    /**
     * 2：不可用
     */
    INVALID(2),
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

    private CardValidStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private CardValidStatus() {
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

    public static CardValidStatus toEnum(int sex) {
        for (CardValidStatus _sex : CardValidStatus.values()) {
            if (_sex.value() == sex) {
                return _sex;
            }
        }
        return UNKNOW;
    }

}
