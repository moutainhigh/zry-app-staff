package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;

import java.util.List;

/**
 * @version: 1.0
 * @date 2015年7月22日
 */
public class TradePendingVo {

    private final TradeVo tradeVo;

    /**
     * 为null表示此挂单已经失效
     */
    private final List<ShopcartItem> shopcartItemList;
    /**
     * 为true时表示有些商品的价格已经变更了
     */
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
