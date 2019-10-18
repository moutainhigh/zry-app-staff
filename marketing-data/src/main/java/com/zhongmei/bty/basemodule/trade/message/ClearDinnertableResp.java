package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.bty.basemodule.trade.entity.TradeStatusLog;

import java.util.List;


public class ClearDinnertableResp {

    private List<Tables> tables;

    private List<TradeStatusLog> tradeStatusLogs;

    public List<Tables> getTables() {
        return tables;
    }

    public void setTables(List<Tables> tables) {
        this.tables = tables;
    }

    public List<TradeStatusLog> getTradeStatusLogs() {
        return tradeStatusLogs;
    }

    public void setTradeStatusLogs(List<TradeStatusLog> tradeStatusLogs) {
        this.tradeStatusLogs = tradeStatusLogs;
    }
}
