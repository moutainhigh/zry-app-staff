package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.R;
import com.zhongmei.yunfu.util.ValueEnum;

/**
 *

 *
 */
public enum SourceChild implements ValueEnum<Integer> {

    /**
     * ANDROID
     */
    ANDROID(1, R.string.commonmodule_android),

    /**
     * IPAD自助设备
     */
    IPAD_SELF(2, R.string.commonmodule_dinner_ipad_self_device),

    /**
     * 商户收银终端
     */
    IPAD(3, R.string.commonmodule_dinner_businessman_cashier_device),

    /**
     * 官微
     */
    SELF_WEIXIN(31, R.string.commonmodule_dinner_offical_wechat),

    /**
     * 商微
     */
    MERCHANT_WEIXIN(32, R.string.commonmodule_dinner_business_wechat),

    /**
     * 快捷支付
     */
    QUICK_PAY(33, R.string.commonmodule_dinner_quick_pay),

    /**
     * 百度外卖
     */
    BAIDU_TAKEOUT(41, R.string.commonmodule_dinner_baidu_takeout),

    /**
     * 百度直达号
     */
    BAIDU_DIRECT_NUMBER(51, R.string.commonmodule_dinner_baidu_nonstop),

    /**
     * 百度糯米
     */
    BAIDU_RICE(61, R.string.commonmodule_dinner_baidu_rich),

    /**
     * 百度地图
     */
    BAIDU_MAP(71, R.string.commonmodule_dinner_baidu_map),

    /**
     * 呼叫中心
     */
    CALL_CENTER(81, R.string.commonmodule_dinner_calling_center),

    /**
     * 必胜客
     */
    PIZZAHUT(91, R.string.commonmodule_dinner_pizza_hut),

    /**
     * 商户官网
     */
    MERCHANT_HOME(111, R.string.commonmodule_dinner_businessman_website),

    /**
     * Loyal
     */
    LOYAL(131, R.string.commonmodule_loyal),

    /**
     * OnMobile
     */
    ON_MOBILE(141, R.string.commonmodule_onmobile),

    /**
     * 熟客
     */
    FAMILIAR(151, R.string.commonmodule_dinner_frequency_customer),

    /**
     * 饿了么
     */
    ELEME(161, R.string.commonmodule_dinner_elm),

    /**
     * 美团外卖
     */
    MEITUAN_TAKEOUT(181, R.string.commonmodule_dinner_meituan_takeouit),

    /**
     * 安卓自助
     */
    KIOSK_ANDROID(191, R.string.commonmodule_dinner_kiosk_android),

    /**
     * 新美大扫码下单
     */
    XIN_MEI_DA(231, R.string.commonmodule_dinner_xinmeida),

    /**
     * 口碑
     */
    KOU_BEI(815, R.string.commonmodule_dinner_koubei),

    /**
     * 快餐离线下单
     */
    OFFLINE(819, R.string.commonmodule_dinner_offline),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
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
