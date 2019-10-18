package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;
import com.zhongmei.yunfu.db.R;
import com.zhongmei.yunfu.context.base.BaseApplication;


public enum ArriveWay implements ValueEnum<Integer> {


    WECHAT(1),

    SHUKE(2),

    FACE_RECOGNITION(3),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private ArriveWay(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private ArriveWay() {
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

    public static ArriveWay toEnum(int sex) {
        for (ArriveWay _sex : ArriveWay.values()) {
            if (_sex.value() == sex) {
                return _sex;
            }
        }
        return __UNKNOWN__;
    }

    public String getName() {
        switch (this) {
            case WECHAT:
                return getString(R.string.commonmodule_wechat);
            case SHUKE:
                return getString(R.string.commonmodule_shuke);
            case FACE_RECOGNITION:
                return getString(R.string.commonmodule_face_recognition);
        }

        return null;
    }

    private String getString(int resId) {
        return BaseApplication.sInstance.getString(resId);
    }

}
