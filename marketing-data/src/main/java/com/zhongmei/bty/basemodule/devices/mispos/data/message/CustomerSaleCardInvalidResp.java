package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import java.util.List;

import com.zhongmei.bty.basemodule.devices.mispos.data.bean.CustomerSaleCardInfo;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;


public class CustomerSaleCardInvalidResp {
    List<Trade> trades;
    List<TradeItem> tradeItems;
    List<CustomerSaleCardInfo> cardSaleInfos;

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    public List<TradeItem> getTradeItems() {
        return tradeItems;
    }

    public void setTradeItems(List<TradeItem> tradeItems) {
        this.tradeItems = tradeItems;
    }

    public List<CustomerSaleCardInfo> getCardSaleInfos() {
        return cardSaleInfos;
    }

    public void setCardSaleInfos(List<CustomerSaleCardInfo> cardSaleInfos) {
        this.cardSaleInfos = cardSaleInfos;
    }
}
