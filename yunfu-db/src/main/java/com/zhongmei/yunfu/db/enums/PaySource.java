package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum PaySource implements ValueEnum<Integer> {


    CASHIER(1),


    KIOSK(2),


    BAIDU_TAKEOUT(3),


    BAIDU_RICE(4),


    BAIDU_MAP(5),


    LOYAL(6),


    ON_MOBILE(7),


    QUICK_PAY(8),


    FAMILIAR(9),


    PORTAL(10),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PaySource(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PaySource() {
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
