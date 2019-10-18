package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.R;
import com.zhongmei.yunfu.util.ValueEnum;


public enum SourceId implements ValueEnum<Integer> {


    POS(1, R.string.commonmodule_dinner_fullservice_order),


    WECHAT(2, R.string.commonmodule_dinner_wechat),


    BAIDU_TAKEOUT(4, R.string.commonmodule_dinner_baidu_takeout),


    BAIDU_ZHIDA(5, R.string.commonmodule_dinner_baidu_nonstop),


    BAIDU_RICE(6, R.string.commonmodule_dinner_baidu_rich),


    BAIDU_MAP(7, R.string.commonmodule_dinner_baidu_map),


    CALL_CENTER(8, R.string.commonmodule_dinner_calling_center),


    KIOSK(9, R.string.commonmodule_dinner_selef_device),



    MERCHANT_HOME(11, R.string.commonmodule_dinner_businessman_website),


    LOYAL(13, R.string.commonmodule_loyal),


    ON_MOBILE(14, R.string.commonmodule_onmobile),


    FAMILIAR(15, R.string.commonmodule_dinner_frequency_customer),


    ELEME(16, R.string.commonmodule_dinner_elm),


    DIANPING(17, R.string.commonmodule_dinner_public_comment),


    MEITUAN_TAKEOUT(18, R.string.commonmodule_dinner_meituan_takeouit),


    KIOSK_ANDROID(19, R.string.commonmodule_dinner_kiosk_android),


    OPEN_PLATFORM(20, R.string.commonmodule_dinner_open_platform),


    JD_HOME(21, R.string.commonmodule_dinner_jd_home),




    XIN_MEI_DA(23, R.string.commonmodule_dinner_xinmeida),


    KOU_BEI(24, R.string.commonmodule_dinner_koubei),


    MEITUAN_QUEUE(-15, R.string.commonmodule_meituan_queue),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private int descResId;

    private SourceId(Integer value, int desc) {
        helper = Helper.valueHelper(value);
        this.descResId = desc;
    }

    private SourceId() {
        helper = Helper.unknownHelper();
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
