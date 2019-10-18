package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;
import com.zhongmei.yunfu.db.R;
import com.zhongmei.yunfu.context.base.BaseApplication;


public enum AppType implements ValueEnum<Integer> {


    IPAD_QUEUE_BOOKING(0),

    IPAD_DINNER_KIOSK(1),

    IPAD_SNACK_KIOSK(2),

    ANDROID_POS(5),

    IOS_ASSISTANT(7),

    ANDROID_ASSISTANT(8),

    OS_MOBILE(12),

    ANDROID_SHUKE(21),

    IOS_SHUKE(22),

    GATEWAY(30),

    WEHCHAT(1001),

    BAIDU_ZHIDAHAO(1002),

    MERCHANT_HOME(1003),

    FIND_SEAT(1004),

    BAIDU_MAP_QUEUE(1005),

    BAIDU_RICE_BOOKING(1006),

    BAIDU_RICE_QUEUE(1007),

    BAIDU_TAKEOUT(1008),

    CALL_CENTER(1009),

    ALIPAY(1010),

    BAIDU_WALLET(1011),

    BAIDU_RICE(1012),

    B_KRY(2001),

    ERP(3001),

    SCM(4001),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private AppType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private AppType() {
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

    public static AppType toEnum(int sex) {
        for (AppType _sex : AppType.values()) {
            if (_sex.value() == sex) {
                return _sex;
            }
        }
        return __UNKNOWN__;
    }

    public String getName() {
        switch (this) {
            case IPAD_QUEUE_BOOKING:
                return getString(R.string.commonmodule_ipad_queue_booking);
            case IPAD_DINNER_KIOSK:
                return getString(R.string.commonmodule_ipad_dinner_kiosk);
            case IPAD_SNACK_KIOSK:
                return getString(R.string.commonmodule_ipad_snack_kiosk);
            case ANDROID_POS:
                return getString(R.string.commonmodule_android_pos);
            case IOS_ASSISTANT:
                return getString(R.string.commonmodule_ios_assistant);
            case ANDROID_ASSISTANT:
                return getString(R.string.commonmodule_android_assistant);
            case ANDROID_SHUKE:
                return getString(R.string.commonmodule_android_shuke);
            case IOS_SHUKE:
                return getString(R.string.commonmodule_ios_shuke);
            case GATEWAY:
                return getString(R.string.commonmodule_gateway);
            case WEHCHAT:
                return getString(R.string.commonmodule_wechat);
            case BAIDU_ZHIDAHAO:
                return getString(R.string.commonmodule_baidu_zhida);
            case MERCHANT_HOME:
                return getString(R.string.commonmodule_merchant_home);
            case FIND_SEAT:
                return getString(R.string.commonmodule_find_seat);
            case BAIDU_MAP_QUEUE:
                return getString(R.string.commonmodule_baidu_map_queue);
            case BAIDU_RICE_BOOKING:
                return getString(R.string.commonmodule_baidu_rice_booking);
            case BAIDU_RICE_QUEUE:
                return getString(R.string.commonmodule_baidu_rice_queue);
            case BAIDU_TAKEOUT:
                return getString(R.string.commonmodule_dinner_baidu_takeout);
            case CALL_CENTER:
                return getString(R.string.commonmodule_call_center);
            case ALIPAY:
                return getString(R.string.commonmodule_alipay);
            case BAIDU_WALLET:
                return getString(R.string.commonmodule_baidupay);
            case BAIDU_RICE:
                return getString(R.string.commonmodule_order_baidu_rice);
            case B_KRY:
                return getString(R.string.commonmodule_b_zmyf);
            case ERP:
                return getString(R.string.commonmodule_erp);
            case SCM:
                return getString(R.string.commonmodule_scm);
        }

        return null;
    }

    private String getString(int resId) {
        return BaseApplication.sInstance.getString(resId);
    }

}
