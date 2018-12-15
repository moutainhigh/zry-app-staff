package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.R;
import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 第3方来源。暂用getway枚举，以后要使用关联表
 */
public enum SourceId implements ValueEnum<Integer> {

//	/**
//	 * ANDROID
//	 */
//	ANDROID(1),
//	
//	/**
//	 * ios
//	 */
//	IOS(2),
    /**
     * 堂食下单
     * n
     */
    POS(1, R.string.commonmodule_dinner_fullservice_order),

    /**
     * 微信
     */
    WECHAT(2, R.string.commonmodule_dinner_wechat),

    /**
     * 百度外卖
     */
    BAIDU_TAKEOUT(4, R.string.commonmodule_dinner_baidu_takeout),

    /**
     * 百度直达号
     * n
     */
    BAIDU_ZHIDA(5, R.string.commonmodule_dinner_baidu_nonstop),

    /**
     * 百度糯米
     */
    BAIDU_RICE(6, R.string.commonmodule_dinner_baidu_rich),

    /**
     * 百度地图
     * n
     */
    BAIDU_MAP(7, R.string.commonmodule_dinner_baidu_map),

    /**
     * 呼叫中心
     * n
     */
    CALL_CENTER(8, R.string.commonmodule_dinner_calling_center),

    /**
     * 自助终端
     */
    KIOSK(9, R.string.commonmodule_dinner_selef_device),


    /**
     * 商户官网
     * n
     */
    MERCHANT_HOME(11, R.string.commonmodule_dinner_businessman_website),

    /**
     * loyal
     * n
     */
    LOYAL(13, R.string.commonmodule_loyal),

    /**
     * OnMobile
     * n
     */
    ON_MOBILE(14, R.string.commonmodule_onmobile),

    /**
     * 熟客
     * n
     */
    FAMILIAR(15, R.string.commonmodule_dinner_frequency_customer),

    /**
     * 饿了么
     * n
     */
    ELEME(16, R.string.commonmodule_dinner_elm),

    /**
     * 大众点评
     */
    DIANPING(17, R.string.commonmodule_dinner_public_comment),

    /**
     * 美团外卖
     */
    MEITUAN_TAKEOUT(18, R.string.commonmodule_dinner_meituan_takeouit),

    /**
     * 安卓自助
     * n
     */
    KIOSK_ANDROID(19, R.string.commonmodule_dinner_kiosk_android),

    /**
     * 开放平台
     */
    OPEN_PLATFORM(20, R.string.commonmodule_dinner_open_platform),

    /**
     * 京东到家
     */
    JD_HOME(21, R.string.commonmodule_dinner_jd_home),

    /**
     * Mind 22
     */
    //Mind(22, "Mind"),

    /**
     * 新美大扫码下单
     */
    XIN_MEI_DA(23, R.string.commonmodule_dinner_xinmeida),

    /**
     * v8.12.0
     * 口碑
     */
    KOU_BEI(24, R.string.commonmodule_dinner_koubei),

    /**
     * v8.15.0
     * 美大排队
     */
    MEITUAN_QUEUE(-15, R.string.commonmodule_meituan_queue),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
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
