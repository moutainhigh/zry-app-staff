package com.zhongmei.bty.basemodule.devices.mispos.enums;

import android.content.res.Resources;

import com.zhongmei.yunfu.data.R;
import com.zhongmei.yunfu.util.ValueEnum;
import com.zhongmei.yunfu.context.base.BaseApplication;

/**
 * 会员实体卡状态
 */
public enum CardStatus implements ValueEnum<Integer> {

    /**
     * 未制卡
     */
    UNMAKECARD(0),

    /**
     * 未出售
     */
    UNSELL(1),

    /**
     * 未激活
     */
    UNACTIVATED(2),

    /**
     * 已激活
     */
    ACTIVATED(3),

    /**
     * 已停用
     */
    ISDISABLE(4),

    /**
     * 已废除
     */
    ISCANCEL(5),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
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
