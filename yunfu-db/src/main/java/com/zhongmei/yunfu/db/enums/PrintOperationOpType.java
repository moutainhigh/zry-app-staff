package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * @version: 1.0
 * @date 2016年3月9日
 */
public enum PrintOperationOpType implements ValueEnum<Integer> {

    /**
     * 上菜批次
     */
    DISH_SEQUENCE,
    /**
     * 批量操作，本地使用的枚举值
     */
    BATCH_OPERATION,
    /**
     * 转台
     */
    DINNER_TRANSFER(1),

    /**
     * 合单
     */
    DINNER_MERGE(2),

    /**
     * 催菜
     */
    REMIND_DISH(3),

    /**
     * 正餐结账
     */
    DINNER_PAY(4),

    /**
     * 等叫
     */
    WAKE_UP(5),

    /**
     * 起菜
     */
    RISE_DISH(6),

    /**
     * 预结单
     */
    DINNER_PRE_CASH(7),

    /**
     * 打印后厨
     */
    KITCHEN_PRINT(8),

    /**
     * 消费清单
     */
    RECEIPT(9),

    /**
     * 正餐移菜
     */
    DINNER_MOVE_DISH(10),

    /**
     * 正餐改单（包含客看单和后厨单据的打印）
     */
    DINNER_MODIFY_TRADE(11),

    /**
     * 正餐作废单
     */
    DINNER_INVALID_TRADE(12),

    //会员卡储值
    CUSTOMER_CHARGING(13),
    //会员卡消费
    CUSTOMER_CONSUME(14),

    /**
     * 标签
     */
    LABEL(15),

    /**
     * 销售报表
     */
    MARKET_TABLE(16),
    /**
     * 正餐自动接单后厨单打印
     */
    DINNER_AUTO_ACCEPT_KITCHEN(17),

    /**
     * 快餐退货单
     */
    CASHIER_REFUND_TICKET(18),

    /**
     * 快餐作废单
     */
    CASHIER_CANCEL_TICKET(19),

    /**
     * 快餐取消(作废/退货)后厨单据打印
     */
    CASHIER_CANCEL_KITCHEN_PRINT(20),

    /**
     * 取消等叫
     */
    WAKE_UP_CANCEL(21),

    /**
     * 取消起菜
     */
    RISE_DISH_CANCEL(22),

    /**
     * 正餐退货
     */
    DINNER_REFUND(23),

    /**
     * 正餐补打
     */
    DINNER_REPRINT(24),

    /**
     * 测试小票
     */
    TEST_TICKET(25),

    /**
     * 唐久开放平台
     */
    TANG_JIU_OPEN_PLATFORM(26),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PrintOperationOpType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PrintOperationOpType() {
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
