package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 关联tradeUser表的type
 * Created by demo on 2018/12/15
 */

public enum TradeScenceType implements ValueEnum<Integer> {

    SALEDISH(1),//售菜
    SALECARD(2), // 售卡
    STORAGE_RECHARGE(3)// 储值充值
    ;

    private final Helper<Integer> helper;

    TradeScenceType(Integer value) {
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
