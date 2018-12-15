package com.zhongmei.bty.data.db.common;

import com.zhongmei.yunfu.db.entity.trade.TradeItem;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 * 在移菜种请求服务器
 */
public class CTradeItem extends TradeItem {
    //移菜操作，需要一定的分数
    private BigDecimal moveQuantity;

    public BigDecimal getMoveQuantity() {
        return moveQuantity;
    }

    public void setMoveQuantity(BigDecimal moveQuantity) {
        this.moveQuantity = moveQuantity;
    }
}
