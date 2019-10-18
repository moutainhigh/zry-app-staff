package com.zhongmei.bty.basemodule.trade.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum TradeUserType implements ValueEnum<Integer> {
    WAITER(2),    CASHIER(3),    COOKER(5),    RIDER(6),    SALESMAN(9),    MARKETMAN(10),    SHOPOWER(11),    ADVISER(12),    TECHNICIAN(13)    ;

    private final Helper<Integer> helper;

    TradeUserType(Integer value) {
        helper = Helper.valueHelper(value);
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
