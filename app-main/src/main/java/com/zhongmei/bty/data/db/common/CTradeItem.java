package com.zhongmei.bty.data.db.common;

import com.zhongmei.yunfu.db.entity.trade.TradeItem;

import java.math.BigDecimal;


public class CTradeItem extends TradeItem {
        private BigDecimal moveQuantity;

    public BigDecimal getMoveQuantity() {
        return moveQuantity;
    }

    public void setMoveQuantity(BigDecimal moveQuantity) {
        this.moveQuantity = moveQuantity;
    }
}
