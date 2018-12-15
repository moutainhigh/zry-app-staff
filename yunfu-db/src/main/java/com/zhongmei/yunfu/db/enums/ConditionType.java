package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

public enum ConditionType implements ValueEnum<Integer> {
    /**
     * 首次消费门店
     * 20170306 已经停用
     */
    //FIRST_CONSUME_SHOP(1),

    /**
     * 客户类别
     */
    CUSTOMER_TYPE(2),

    /**
     * 客户分组
     */
    CUSTOMER_GROUP(3),

    /**
     * 自助预订来源
     */
    AUTO_ORDER_SOURCE(4),
    /**
     * 参与门店
     * 20170306 已经停用
     */
    //JOIN_SHOP(5),

    /**
     * 消费类型
     */
    CONSUME_TYPE(6),
    /**
     * 支付方式
     */
    PAY_TYPE(7),
    /**
     * 订单来源
     */
    TRADE_SOURCE(8),
    /**
     * 储值来源
     */
    MEMBER_CONSUME_SOURCE(9),
    /**
     * 单据类型
     */
    TRADE_TYPE(10),
    /**
     * 活动终端
     */
    ACTIVITY_POS(11),
    /**
     * 就餐场景
     */
    MEAL_BACKGROUND(12),
    /**
     * 未知的值
     * 1:首次消费门店, 2:顾客类别, 3:顾客分组, 4:自助预订来源, 5:参与门店，6:消费类型，7:支付方式，8:订单来源，9:储值来源，10:单据类型，11:活动终端，12:就餐场景)',
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private ConditionType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private ConditionType() {
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
