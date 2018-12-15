package com.zhongmei.bty.basemodule.trade.message.uniontable;

import com.zhongmei.bty.basemodule.inventory.message.InventoryChangeReq;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 * 子单取消联台请求数据结构
 */

public class UnionTradeSplitReq {
    private TradeUnionSplitSubReq tradeRequest;
    private InventoryChangeReq inventoryRequest;

    public UnionTradeSplitReq() {
        inventoryRequest = new InventoryChangeReq();
    }

    public void setInventoryRequest(InventoryChangeReq inventoryRequest) {
        this.inventoryRequest = inventoryRequest;
    }

    public void setTradeRequest(TradeUnionSplitSubReq tradeRequest) {
        this.tradeRequest = tradeRequest;
    }

    public static class TradeUnionSplitSubReq {
        private Trade mainTrade;
        private Trade subTrade;
        private List<TradeTax> mainTradeTaxs;
        private List<TradeTax> subTradeTaxs;
        private List<TradeInitConfig> subTradeInitConfigs;

        public void setMainTrade(Trade mainTrade) {
            this.mainTrade = mainTrade;
        }

        public void setSubTrade(Trade subTrade) {
            this.subTrade = subTrade;
        }

        public void setMainTradeTaxs(List<TradeTax> mainTradeTaxs) {
            this.mainTradeTaxs = mainTradeTaxs;
        }

        public void setSubTradeTaxs(List<TradeTax> subTradeTaxs) {
            this.subTradeTaxs = subTradeTaxs;
        }

        public void setSubTradeInitConfigs(List<TradeInitConfig> subTradeInitConfigs) {
            this.subTradeInitConfigs = subTradeInitConfigs;
        }
    }

  /*  public static class TradeMainSubRelationReq {
        public Long id;
        public Long serverUpdateTime;
        public Integer statusFlag;
    }*/
}
