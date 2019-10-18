package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;



public enum PrinterDeviceModel implements ValueEnum<Integer> {

    WPT_990(0),

    WPT_810(1),

    KR_610(2),

    OTHRE(3),


    WPT_300i(5),


    UNKNOWN(4);

    private final Helper<Integer> helper;

    private PrinterDeviceModel(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PrinterDeviceModel() {
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
}
