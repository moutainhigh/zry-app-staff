package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 菜品类型
 */

public enum DishType implements ValueEnum<Integer> {

    /**
     * 单菜／套餐子菜
     */
    SINGLE(0),

    /**
     * 套餐外壳
     */
    COMBO(1),

    /**
     * 加料
     */
    EXTRA(2),

    /**
     * 实体卡
     */
    CARD(5),

    /**
     * 服务次卡套餐(部分)
     */
    SERVER_COMBO_PART(3),

    /**
     * 服务次卡套餐(全部)
     */
    SERVER_COMBO_ALL(4),

    /**
     * 匿名实体卡售卡
     */
    ANONYMOUS_ENTITY_CARD_SELL(10),

    /**
     * 匿名实体卡储值
     */
    ANONYMOUS_ENTITY_CARD_RECHARGE(11),

    /**
     * 餐标外壳
     */
    MEAL_SHELL(12),

    /**
     * 自助餐外壳
     */
    BUFFET_COMBO_SHELL(13),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private DishType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private DishType() {
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
