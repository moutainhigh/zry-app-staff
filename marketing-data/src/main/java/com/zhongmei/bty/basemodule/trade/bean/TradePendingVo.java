package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;

import java.util.List;


public class TradePendingVo {

    private final TradeVo tradeVo;


    private final List<ShopcartItem> shopcartItemList;

    private final boolean priceChanged;

    public TradePendingVo(TradeVo tradeVo) {
        this(tradeVo, null, false);
    }

    public TradePendingVo(TradeVo tradeVo, List<ShopcartItem> shopcartItemList, boolean priceChanged) {
        this.tradeVo = tradeVo;
        this.shopcartItemList = shopcartItemList;
        this.priceChanged = priceChanged;
    }

    public TradeVo getTradeVo() {
        return tradeVo;
    }

    public List<ShopcartItem> getShopcartItemList() {
        return shopcartItemList;
    }

    public boolean isPriceChanged() {
        return priceChanged;
    }

}
