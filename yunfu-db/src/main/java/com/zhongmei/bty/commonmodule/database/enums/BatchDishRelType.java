package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * tradeItemMainBatchRel 表 对应的tadeItem 或者 tradeItemPropertites 表的type
 * Created by demo on 2018/12/15
 */

public enum BatchDishRelType implements ValueEnum<Integer> {


    /**
     * trade_item_extra关联关系
     */
    ITEM_EXTRA_TYPE(1),

    /**
     * trade_item_property关联关系
     */
    ITEM_PROPERTY_TYPE(2),

    /**
     * trade_item_operation关联关系
     */
    ITEM_OPERATION_TYPE(3),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final ValueEnum.Helper<Integer> helper;

    private BatchDishRelType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private BatchDishRelType() {
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
