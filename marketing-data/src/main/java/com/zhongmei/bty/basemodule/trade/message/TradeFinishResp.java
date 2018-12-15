package com.zhongmei.bty.basemodule.trade.message;

import java.util.List;

import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.basemodule.trade.entity.TradeStatusLog;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;

public class TradeFinishResp {

    private List<Trade> trades;

    private List<TradeTable> tradeTables;

    private List<TradeStatusLog> tradeStatusLogs;

    private List<Tables> tables;

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    public List<TradeTable> getTradeTables() {
        return tradeTables;
    }

    public void setTradeTables(List<TradeTable> tradeTables) {
        this.tradeTables = tradeTables;
    }

    public List<TradeStatusLog> getTradeStatusLogs() {
        return tradeStatusLogs;
    }

    public void setTradeStatusLogs(List<TradeStatusLog> tradeStatusLogs) {
        this.tradeStatusLogs = tradeStatusLogs;
    }

    public List<Tables> getTables() {
        return tables;
    }

    public void setTables(List<Tables> tables) {
        this.tables = tables;
    }


}
