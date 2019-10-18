package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;
import com.zhongmei.yunfu.db.R;
import com.zhongmei.yunfu.context.base.BaseApplication;


public enum ArriveStatus implements ValueEnum<Integer> {


    BEYOND(1),

    ARRIVED_UNEAT(2),

    ARRIVED_EATING(3),

    LEFT(4),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private ArriveStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private ArriveStatus() {
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

    public static ArriveStatus toEnum(int sex) {
        for (ArriveStatus _sex : ArriveStatus.values()) {
            if (_sex.value() == sex) {
                return _sex;
            }
        }
        return __UNKNOWN__;
    }

    public String getName() {
        switch (this) {
            case ARRIVED_UNEAT:
            case ARRIVED_EATING:
                return getString(R.string.commonmodule_booking_arrived);
            case LEFT:
                return getString(R.string.commonmodule_booking_leave);
            case BEYOND:
                return getString(R.string.commonmodule_booking_beyond);
        }

        return null;
    }

    private String getString(int resId) {
        return BaseApplication.sInstance.getString(resId);
    }

}
