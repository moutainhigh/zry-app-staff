package com.zhongmei.bty.basemodule.orderdish.bean;

import java.math.BigDecimal;
import java.util.List;

import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;

/**
 * @version: 1.0
 * @date 2015年9月20日
 */
public class ReadonlySetmealShopcartItem extends ReadonlyShopcartItemBase implements ISetmealShopcartItem {

    private final ReadonlyShopcartItem parent;
    private final BigDecimal singleQty;

    public ReadonlySetmealShopcartItem(TradeItem tradeItem, ReadonlyShopcartItem parent) {
        super(tradeItem);
        this.parent = parent;
        singleQty = ShopcartItemUtils.computeSingleQty(tradeItem.getQuantity(), parent);
    }

    @Override
    public ReadonlyShopcartItemBase getParent() {
        return parent;
    }

    @Override
    public void setProperties(List<ReadonlyOrderProperty> properties) {
        super.setProperties(properties);
    }

    @Override
    public void setExtraItems(List<ReadonlyExtraShopcartItem> extraItems) {
        super.setExtraItems(extraItems);
    }

    @Override
    public BigDecimal getSingleQty() {
        return singleQty;
    }

    @Override
    public Long getSetmealGroupId() {
        return tradeItem.getDishSetmealGroupId();
    }

    @Override
    public TradeReasonRel getReturnQtyReason() {
        return null;
    }

    @Override
    public void setDiscountReasonRel(TradeReasonRel reasonRel) {

    }

    @Override
    public TradeReasonRel getDiscountReasonRel() {
        return null;
    }
}
