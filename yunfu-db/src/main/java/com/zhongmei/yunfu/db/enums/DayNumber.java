package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 日期序号，1代表周一 .....7代表周日'
 */
public enum DayNumber implements ValueEnum<Integer> {

    /**
     * 星期一
     */
    MONDAY(1),

    /**
     * 星期二
     */
    TUESDAY(2),

    /**
     * 星期三
     */
    WEDNESDAY(3),

    /**
     * 星期四
     */
    THURSDAY(4),

    /**
     * 星期五
     */
    FRIDAY(5),

    /**
     * 星期六
     */
    SATURDAY(6),

    /**
     * 星期日
     */
    SUNDAY(7),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private DayNumber(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private DayNumber() {
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
