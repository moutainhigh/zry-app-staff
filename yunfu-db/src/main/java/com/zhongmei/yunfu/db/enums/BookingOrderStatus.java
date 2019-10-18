package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.db.R;
import com.zhongmei.yunfu.util.ValueEnum;


public enum BookingOrderStatus implements ValueEnum<Integer> {

    UNARRIVED(-1, R.string.booking_unarrived),


    ARRIVED(1, R.string.booking_arrived),


    LEAVE(2, R.string.booking_leave),


    CANCEL(9, R.string.booking_cancel),


    UNPROCESS(-2, R.string.booking_unprocess),


    REFUSED(-3, R.string.booking_refused),


    TIMEOUT(-4, R.string.booking_timeout),


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
