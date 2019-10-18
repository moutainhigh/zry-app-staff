package com.zhongmei.bty.basemodule.devices.mispos.enums;

import android.content.res.Resources;

import com.zhongmei.yunfu.data.R;
import com.zhongmei.yunfu.util.ValueEnum;
import com.zhongmei.yunfu.context.base.BaseApplication;


public enum CardStatus implements ValueEnum<Integer> {


    UNMAKECARD(0),


    UNSELL(1),


    UNACTIVATED(2),


    ACTIVATED(3),


    ISDISABLE(4),


    ISCANCEL(5),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private CardStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private CardStatus() {
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

    public static String getStatuS(int code) {
        Resources res = BaseApplication.sInstance.getResources();
        switch (code) {
            case 0:
                return res.getString(R.string.eccard_not_make_card);
            case 1:
                return res.getString(R.string.eccard_unsale);
            case 2:
                return res.getString(R.string.eccard_unactive);
            case 3:
                return res.getString(R.string.eccard_actived);
            case 4:
                return res.getString(R.string.eccard_disabled);
            case 5:
                return res.getString(R.string.eccard_invalided);
        }
        return res.getString(R.string.eccard_not_make_card);
    }

    public static String getStatuS(CardStatus status) {
        return getStatuS(status.value());
    }


}
