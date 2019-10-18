package com.zhongmei.bty.basemodule.orderdish.bean;

import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;

import java.math.BigDecimal;


public class ExtraShopcartItem extends ShopcartItemBase<OrderExtra> implements IExtraShopcartItem {

    public ExtraShopcartItem(String uuid, OrderExtra orderExtra, ShopcartItemBase<?> parent) {
        super(uuid, orderExtra, parent);
    }

    @Override
    public void changeQty(BigDecimal singleQty) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDiscountReasonRel(TradeReasonRel reasonRel) {

    }

    @Override
    public TradeReasonRel getDiscountReasonRel() {
        return null;
    }
}
