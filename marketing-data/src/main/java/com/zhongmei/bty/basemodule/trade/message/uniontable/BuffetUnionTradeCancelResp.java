package com.zhongmei.bty.basemodule.trade.message.uniontable;

import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.bty.basemodule.trade.entity.TradeMainSubRelation;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;

import java.util.List;



public class BuffetUnionTradeCancelResp {

    private List<Trade> trades;
    private List<TradeTax> tradeTaxs;     public List<TradeInitConfig> tradeInitConfigs;
    private List<TradeDeposit> tradeDeposits;     private List<TradeBuffetPeople> tradeBuffetPeoples;     private List<TradeMainSubRelation> tradeMainSubRelations;     private TradeTable tradeTable;     private List<TradeItem> tradeItems;

    public List<Trade> getTrades() {
        return trades;
    }

    public List<TradeTax> getTradeTaxs() {
        return tradeTaxs;
    }

    public List<TradeInitConfig> getTradeInitConfigs() {
        return tradeInitConfigs;
    }

    public List<TradeDeposit> getTradeDeposits() {
        return tradeDeposits;
    }

    public List<TradeBuffetPeople> getTradeBuffetPeoples() {
        return tradeBuffetPeoples;
    }

    public List<TradeMainSubRelation> getTradeMainSubRelations() {
        return tradeMainSubRelations;
    }

    public TradeTable getTradeTable() {
        return tradeTable;
    }

    public List<TradeItem> getTradeItems() {
        return tradeItems;
    }
}
