package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.R;
import com.zhongmei.yunfu.util.ValueEnum;


public enum SourceChild implements ValueEnum<Integer> {


    ANDROID(1, R.string.commonmodule_android),


    IPAD_SELF(2, R.string.commonmodule_dinner_ipad_self_device),


    IPAD(3, R.string.commonmodule_dinner_businessman_cashier_device),


    SELF_WEIXIN(31, R.string.commonmodule_dinner_offical_wechat),


    MERCHANT_WEIXIN(32, R.string.commonmodule_dinner_business_wechat),


    QUICK_PAY(33, R.string.commonmodule_dinner_quick_pay),


    BAIDU_TAKEOUT(41, R.string.commonmodule_dinner_baidu_takeout),


    BAIDU_DIRECT_NUMBER(51, R.string.commonmodule_dinner_baidu_nonstop),


    BAIDU_RICE(61, R.string.commonmodule_dinner_baidu_rich),


    BAIDU_MAP(71, R.string.commonmodule_dinner_baidu_map),


    CALL_CENTER(81, R.string.commonmodule_dinner_calling_center),


    PIZZAHUT(91, R.string.commonmodule_dinner_pizza_hut),


    MERCHANT_HOME(111, R.string.commonmodule_dinner_businessman_website),


    LOYAL(131, R.string.commonmodule_loyal),


    ON_MOBILE(141, R.string.commonmodule_onmobile),


    FAMILIAR(151, R.string.commonmodule_dinner_frequency_customer),


    ELEME(161, R.string.commonmodule_dinner_elm),


    MEITUAN_TAKEOUT(181, R.string.commonmodule_dinner_meituan_takeouit),


    KIOSK_ANDROID(191, R.string.commonmodule_dinner_kiosk_android),


    XIN_MEI_DA(231, R.string.commonmodule_dinner_xinmeida),


    KOU_BEI(815, R.string.commonmodule_dinner_koubei),


    OFFLINE(819, R.string.commonmodule_dinner_offline),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private int descResId;

    private SourceChild(Integer value, int descResId) {
        helper = Helper.valueHelper(value);
        this.descResId = descResId;
    }

    private SourceChild() {
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
