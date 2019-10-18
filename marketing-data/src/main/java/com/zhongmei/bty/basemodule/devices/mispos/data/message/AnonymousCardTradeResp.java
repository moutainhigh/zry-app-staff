package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.bty.basemodule.devices.mispos.data.bean.CustomerSaleCardInfo;

import java.util.List;


public class AnonymousCardTradeResp {

    private Trade trade;

    private List<TradeItem> tradeItems;

    private CustomerSaleCardInfo cardSaleInfo;

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public List<TradeItem> getTradeItems() {
        return tradeItems;
    }

    public void setTradeItems(List<TradeItem> tradeItems) {
        this.tradeItems = tradeItems;
    }

    public CustomerSaleCardInfo getCardSaleInfo() {
        return cardSaleInfo;
    }

    public void setCardSaleInfo(CustomerSaleCardInfo cardSaleInfo) {
        this.cardSaleInfo = cardSaleInfo;
    }
}
