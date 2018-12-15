package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.basemodule.trade.entity.TradeStatusLog;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;

import java.util.List;

public class TableTradeVo {

    private Tables table;

    private Trade trade;

    private TradeTable tradeTable;

    private TradeExtra tradeExtra;

    private CommercialArea commercialArea;

    private List<TradeStatusLog> tradeStatusLogs;

    public Tables getTable() {
        return table;
    }

    public void setTable(Tables table) {
        this.table = table;
    }

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

    public CommercialArea getCommercialArea() {
        return commercialArea;
    }

    public void setCommercialArea(CommercialArea commercialArea) {
        this.commercialArea = commercialArea;
    }

    public TradeTable getTradeTable() {
        return tradeTable;
    }

    public void setTradeTable(TradeTable tradeTable) {
        this.tradeTable = tradeTable;
    }

    public List<TradeStatusLog> getTradeStatusLogs() {
        return tradeStatusLogs;
    }

    public void setTradeStatusLogs(List<TradeStatusLog> tradeStatusLogs) {
        this.tradeStatusLogs = tradeStatusLogs;
    }
}
