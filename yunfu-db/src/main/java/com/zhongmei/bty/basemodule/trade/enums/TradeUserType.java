package com.zhongmei.bty.basemodule.trade.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 关联tradeUser表的 userType
 * 5 厨师   6 骑手   9 销售员 10 推销员 11 店长 12 顾问 13 技师
 * Created by demo on 2018/12/15
 */
public enum TradeUserType implements ValueEnum<Integer> {
    WAITER(2),//服务员
    CASHIER(3),//收银员
    COOKER(5),//厨师
    RIDER(6),//骑手
    SALESMAN(9),//销售员
    MARKETMAN(10),//推销员
    SHOPOWER(11),//店长
    ADVISER(12),//顾问
    TECHNICIAN(13)//技师
    ;

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
