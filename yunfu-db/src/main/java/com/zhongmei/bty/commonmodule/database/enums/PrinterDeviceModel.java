package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 打印机类型枚举
 * Created by demo on 2018/12/15
 */

public enum PrinterDeviceModel implements ValueEnum<Integer> {
    /**
     * 莹普通 wpt-990
     */
    WPT_990(0),
    /**
     * 莹普通wpt-810
     */
    WPT_810(1),
    /**
     * Kr-610
     */
    KR_610(2),
    /*
     *其他
     */
    OTHRE(3),

    /**
     * 莹浦通wp-300i
     */
    WPT_300i(5),

    /**
     * 除了普通打印机其他类型打印机设置该值
     */
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
