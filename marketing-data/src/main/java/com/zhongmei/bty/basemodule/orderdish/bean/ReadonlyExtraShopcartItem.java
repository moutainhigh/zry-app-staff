package com.zhongmei.bty.basemodule.orderdish.bean;

import java.math.BigDecimal;

import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;

/**
 * @version: 1.0
 * @date 2015年9月20日
 */
public class ReadonlyExtraShopcartItem extends ReadonlyShopcartItemBase implements IExtraShopcartItem {

    private final ReadonlyShopcartItemBase parent;
    private final BigDecimal singleQty;
    private BigDecimal deskCount;

    public ReadonlyExtraShopcartItem(TradeItem tradeItem, ReadonlyShopcartItemBase parent) {
        super(tradeItem);
        this.parent = parent;
        singleQty = ShopcartItemUtils.computeSingleQty(tradeItem.getQuantity(), parent);
    }

    @Override
    public ReadonlyShopcartItemBase getParent() {
        return parent;
    }

    @Override
    public BigDecimal getSingleQty() {
        return singleQty;
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

    public BigDecimal getDeskCount() {
        return deskCount;
    }

    public void setDeskCount(BigDecimal deskCount) {
        this.deskCount = deskCount;
    }

}
