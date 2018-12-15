package com.zhongmei.bty.basemodule.trade.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * @version: 1.0
 * @date 2015年7月10日
 */
public enum TableTypeZone implements ValueEnum<Integer> {

    /**
     * 大厅
     */
    HALL(0),
    /**
     * 包厢
     */
    ROOM(1),
    /**
     * 卡座
     */
    SEAT(2),
    /**
     * 露台
     */
    BALCONY(3),
    /**
     * 其他
     */
    OTHER(4),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private TableTypeZone(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private TableTypeZone() {
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
