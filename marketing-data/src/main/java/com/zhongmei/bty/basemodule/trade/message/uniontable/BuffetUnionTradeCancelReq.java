package com.zhongmei.bty.basemodule.trade.message.uniontable;

import com.zhongmei.bty.basemodule.inventory.message.InventoryChangeReq;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;

import java.util.List;



public class BuffetUnionTradeCancelReq {
    private TradeUnionReq tradeRequest;
    private InventoryChangeReq inventoryRequest;

    public void setInventoryRequest(InventoryChangeReq inventoryRequest) {
        this.inventoryRequest = inventoryRequest;
    }

    public void setTradeRequest(TradeUnionReq tradeRequest) {
        this.tradeRequest = tradeRequest;
    }

    public TradeUnionReq getTradeRequest() {
        return tradeRequest;
    }

    public static class TradeUnionReq {
        private Trade mainTrade;
        private Trade subTrade;
        private List<TradeBuffetPeople> mainTradeBuffetPeoples;         private List<TradeBuffetPeople> subTradeBuffetPeoples;
        private TradeDeposit mainTradeDeposit;         private TradeDeposit subTradeDeposit;
        private List<TradeTax> mainTradeTaxs;
        private List<TradeTax> subTradeTaxs;
        private TradeTable subTradeTable;
        private TradeItem mainMenuTradeItem;
        private TradeItem subMenuTradeItem;
                private List<TradeInitConfig> subTradeInitConfigs;

        public void setMainTrade(Trade mainTrade) {
            this.mainTrade = mainTrade;
        }

        public void setSubTrade(Trade subTrade) {
            this.subTrade = subTrade;
        }

        public void setMainTradeBuffetPeoples(List<TradeBuffetPeople> mainTradeBuffetPeoples) {
            this.mainTradeBuffetPeoples = mainTradeBuffetPeoples;
        }

        public List<TradeBuffetPeople> getMainTradeBuffetPeoples() {
            return mainTradeBuffetPeoples;
        }

        public void setSubTradeBuffetPeoples(List<TradeBuffetPeople> subTradeBuffetPeoples) {
            this.subTradeBuffetPeoples = subTradeBuffetPeoples;
        }

        public List<TradeBuffetPeople> getSubTradeBuffetPeoples() {
            return subTradeBuffetPeoples;
        }

        public void setMainTradeDeposit(TradeDeposit mainTradeDeposit) {
            this.mainTradeDeposit = mainTradeDeposit;
        }

        public TradeDeposit getMainTradeDeposit() {
            return mainTradeDeposit;
        }

        public void setSubTradeDeposit(TradeDeposit subTradeDeposit) {
            this.subTradeDeposit = subTradeDeposit;
        }

        public void setMainTradeTaxs(List<TradeTax> mainTradeTaxs) {
            this.mainTradeTaxs = mainTradeTaxs;
        }

        public void setSubTradeTaxs(List<TradeTax> subTradeTaxs) {
            this.subTradeTaxs = subTradeTaxs;
        }

        public void setSubTradeTable(TradeTable subTradeTable) {
            this.subTradeTable = subTradeTable;
        }

        public void setMainMenuTradeItem(TradeItem mainMenuTradeItem) {
            this.mainMenuTradeItem = mainMenuTradeItem;
        }

        public void setSubMenuTradeItem(TradeItem subMenuTradeItem) {
            this.subMenuTradeItem = subMenuTradeItem;
        }

        public void setSubTradeInitConfigs(List<TradeInitConfig> subTradeInitConfigs) {
            this.subTradeInitConfigs = subTradeInitConfigs;
        }
    }


}
