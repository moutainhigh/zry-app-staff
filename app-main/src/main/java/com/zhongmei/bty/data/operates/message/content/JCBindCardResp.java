package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.devices.mispos.data.CardSaleInfo;



public class JCBindCardResp {
    private Trade trade;

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public TradeItem getTradeItem() {
        return tradeItem;
    }

    public void setTradeItem(TradeItem tradeItem) {
        this.tradeItem = tradeItem;
    }

    public CardSaleInfo getCardSaleInfo() {
        return cardSaleInfo;
    }

    public void setCardSaleInfo(CardSaleInfo cardSaleInfo) {
        this.cardSaleInfo = cardSaleInfo;
    }

    public TradeCustomer getTradeCustomer() {
        return tradeCustomer;
    }

    public void setTradeCustomer(TradeCustomer tradeCustomer) {
        this.tradeCustomer = tradeCustomer;
    }

    private TradeItem tradeItem;
    private CardSaleInfo cardSaleInfo;
    private TradeCustomer tradeCustomer;
}
