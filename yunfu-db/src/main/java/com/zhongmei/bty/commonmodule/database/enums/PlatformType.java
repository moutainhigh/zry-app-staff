package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * @version: 1.0
 * @date 2015年11月17日
 */
public enum PlatformType implements ValueEnum<Integer> {

    /**
     * 1：云后台；2：云营销；3：云收银POS；
     */
    ON_MIND(1),

    /**
     * 云营销
     */
    ON_LOYAL(3),

    /**
     * 云收银POS
     */
    ON_POS(2),

    /**
     * 未知的值，为了避免转为enum出错设置的，不应该直接使用
     *
     * @deprecated
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PlatformType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PlatformType() {
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
