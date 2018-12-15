package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.db.R;
import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 预订单状态
 */
public enum BookingOrderStatus implements ValueEnum<Integer> {
    /**
     * 用户未到店
     */
    UNARRIVED(-1, R.string.booking_unarrived),

    /**
     * 用户到店
     */
    ARRIVED(1, R.string.booking_arrived),

    /**
     * 用户离店
     */
    LEAVE(2, R.string.booking_leave),

    /**
     * 已取消
     */
    CANCEL(9, R.string.booking_cancel),

    /**
     * 未处理
     */
    UNPROCESS(-2, R.string.booking_unprocess),

    /**
     * 已拒绝
     */
    REFUSED(-3, R.string.booking_refused),

    /**
     * 逾期未到店
     */
    TIMEOUT(-4, R.string.booking_timeout),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;
    private int descResId;

    private BookingOrderStatus(Integer value, int resId) {
        helper = Helper.valueHelper(value);
        this.descResId = resId;
    }

    private BookingOrderStatus() {
        helper = Helper.unknownHelper();
    }

    @Override
    public Integer value() {
        return helper.value();
    }

    public int getDescResId() {
        return descResId;
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
