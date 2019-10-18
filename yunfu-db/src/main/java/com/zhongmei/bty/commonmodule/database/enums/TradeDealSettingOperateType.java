package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum TradeDealSettingOperateType implements ValueEnum<Integer> {


    ACCEPT(1),


    REFUSE(2),


    DINNER_ACCEPT(5),


    SERVER_ACCEPT(6),


    @Deprecated
    __UNKNOWN__;


    public static interface $ {


        TradeDealSettingOperateType ENABLE_SERVING = TradeDealSettingOperateType.ACCEPT;


        TradeDealSettingOperateType AUTO_CLEAR_TABLE = TradeDealSettingOperateType.REFUSE;
    }

    private final Helper<Integer> helper;

    private TradeDealSettingOperateType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private TradeDealSettingOperateType() {
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
