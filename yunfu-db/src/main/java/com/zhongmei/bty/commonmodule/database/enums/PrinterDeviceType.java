package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * @version: 1.0
 * @date 2015年6月24日
 */
public enum PrinterDeviceType implements ValueEnum<Integer> {

    /**
     * 打印服务器
     */
    SERVER(1),

    /**
     * 打印机
     */
    PRINTER(2),

    /**
     * 蓝牙打印机
     */
    BLUETOOTH_PRINTER(3),

    /**
     * 未知的值，为了避免转为enum出错设置的，不应该直接使用
     *
     * @deprecated
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PrinterDeviceType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PrinterDeviceType() {
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
