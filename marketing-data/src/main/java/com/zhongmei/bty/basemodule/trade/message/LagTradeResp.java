package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.yunfu.db.entity.trade.TradeCreditLog;

import java.util.List;



public class LagTradeResp extends TradeResp {
    List<TradeCreditLog> tradeCreditLogs;

    public List<TradeCreditLog> getTradeCreditLogs() {
        return tradeCreditLogs;
    }

    public void setTradeCreditLogs(List<TradeCreditLog> tradeCreditLogs) {
        this.tradeCreditLogs = tradeCreditLogs;
    }
}
