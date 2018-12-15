package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.R;
import com.zhongmei.yunfu.util.ValueEnum;

/**
 *

 *
 */
public enum DishMakeStatus implements ValueEnum<Integer> {

    /**
     * 等待中
     */
    WAITING(0, R.string.commonmodule_dinner_waiting),
    /**
     * 配菜中
     */
    MATCHING(1, R.string.commonmodule_dinner_jardiniering),
    /**
     * 制作中
     */
    MAKING(2, R.string.commonmodule_dinner_cooking),
    /**
     * 已完成
     */
    FINISHED(3, R.string.commonmodule_dinner_finished),
    /**
     * 已作废
     */
    CANCELED(4, R.string.commonmodule_dinner_canceded),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
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
