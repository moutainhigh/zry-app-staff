package com.zhongmei.bty.basemodule.orderdish.bean;

import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;


public class SetmealShopcartItem extends ShopcartItemBase<OrderSetmeal> implements ISetmealShopcartItem {

    public SetmealShopcartItem(String uuid, OrderSetmeal orderSetmeal, ShopcartItem parent) {
        this(uuid, orderSetmeal, parent, null);
    }

    protected SetmealShopcartItem(String uuid, OrderSetmeal orderSetmeal, ShopcartItem parent, ItemMetadata metadata) {
        super(uuid, orderSetmeal, parent, metadata);
    }

    public Long getSetmealId() {
        return getOrderDish().getSetmealId();
    }

    @Override
    public Long getSetmealGroupId() {
        return getOrderDish().getSetmealGroupId();
    }

    @Override
    public void setDiscountReasonRel(TradeReasonRel reasonRel) {

    }

    @Override
    public TradeReasonRel getDiscountReasonRel() {
        return null;
    }
}
