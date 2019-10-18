package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;

import java.util.List;



public class BuffetNoTableTradeResp {
    private Trade trade;
    private TradeExtra tradeExtra;
    private List<TradeItem> tradeItems;
    private List<TradeCustomer> tradeCustomers;
    private List<TradeBuffetPeople> tradeBuffetPeoples;
    private TradeDeposit tradeDeposit;

    private List<TradeTax> tradeTaxs;     private List<TradeInitConfig> tradeInitConfigs;

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public TradeExtra getTradeExtra() {
        return tradeExtra;
    }

    public void setTradeExtra(TradeExtra tradeExtra) {
        this.tradeExtra = tradeExtra;
    }

    public List<TradeItem> getTradeItems() {
        return tradeItems;
    }

    public void setTradeItems(List<TradeItem> tradeItems) {
        this.tradeItems = tradeItems;
    }

    public List<TradeCustomer> getTradeCustomers() {
        return tradeCustomers;
    }

    public void setTradeCustomers(List<TradeCustomer> tradeCustomers) {
        this.tradeCustomers = tradeCustomers;
    }

    public List<TradeBuffetPeople> getTradeBuffetPeoples() {
        return tradeBuffetPeoples;
    }

    public void setTradeBuffetPeoples(List<TradeBuffetPeople> tradeBuffetPeoples) {
        this.tradeBuffetPeoples = tradeBuffetPeoples;
    }

    public TradeDeposit getTradeDeposit() {
        return tradeDeposit;
    }

    public void setTradeDeposit(TradeDeposit tradeDeposit) {
        this.tradeDeposit = tradeDeposit;
    }

    public List<TradeTax> getTradeTaxs() {
        return tradeTaxs;
    }

    public List<TradeInitConfig> getTradeInitConfigs() {
        return tradeInitConfigs;
    }
}
