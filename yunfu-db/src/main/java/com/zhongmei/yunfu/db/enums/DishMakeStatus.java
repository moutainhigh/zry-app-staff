package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.R;
import com.zhongmei.yunfu.util.ValueEnum;


public enum DishMakeStatus implements ValueEnum<Integer> {


    WAITING(0, R.string.commonmodule_dinner_waiting),

    MATCHING(1, R.string.commonmodule_dinner_jardiniering),

    MAKING(2, R.string.commonmodule_dinner_cooking),

    FINISHED(3, R.string.commonmodule_dinner_finished),

    CANCELED(4, R.string.commonmodule_dinner_canceded),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private int descResId;

    private DishMakeStatus(Integer value, int descResId) {
        helper = Helper.valueHelper(value);
        this.descResId = descResId;
    }

    private DishMakeStatus() {
        helper = Helper.unknownHelper();
        descResId = -1;
    }

    @Override
    public Integer value() {
        return helper.value();
    }

    public String desc() {
        if (descResId > 0) {
            return BaseApplication.sInstance.getString(descResId);
        } else {
            return BaseApplication.sInstance.getString(R.string.commonmodule_dialog_other);
        }
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
