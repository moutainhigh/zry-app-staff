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

/**
 * Created by demo on 2018/12/15
 * 子单取消联台请求数据结构
 */

public class BuffetUnionTradeCancelResp {

    private List<Trade> trades;
    private List<TradeTax> tradeTaxs; //税率
    public List<TradeInitConfig> tradeInitConfigs;
    private List<TradeDeposit> tradeDeposits; //押金
    private List<TradeBuffetPeople> tradeBuffetPeoples; //餐标人数
    private List<TradeMainSubRelation> tradeMainSubRelations; //联台单关联表
    private TradeTable tradeTable; //桌台人数
    private List<TradeItem> tradeItems;

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
