package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum DeliveryPlatform implements ValueEnum<Integer> {

    MERCHANT(1),


    BAIDU_TAKEOUT(2),


    ELEME(3),


    MEITUAN_TAKEOUT(4),


    DA_DA(5),


    SHUN_FENG(6),


    MEITUAN_ZHONGBAO(7),


    ELEME_ZHONGBAO(8),


    JD_HOME(11),


    OPEN_PLATFORM(20),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private DeliveryPlatform(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private DeliveryPlatform() {
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
}
