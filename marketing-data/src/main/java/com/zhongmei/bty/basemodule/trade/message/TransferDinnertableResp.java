package com.zhongmei.bty.basemodule.trade.message;

import java.util.List;

import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;


public class TransferDinnertableResp {

    private List<Tables> tables;
    private List<TradeTable> tradeTables;
    private List<TradeItemExtraDinner> tradeItemExtraDinners;

    public List<Tables> getTables() {
        return tables;
    }

    public void setTables(List<Tables> tables) {
        this.tables = tables;
    }

    public List<TradeTable> getTradeTables() {
        return tradeTables;
    }

    public void setTradeTables(List<TradeTable> tradeTables) {
        this.tradeTables = tradeTables;
    }

    public List<TradeItemExtraDinner> getTradeItemExtraDinners() {
        return tradeItemExtraDinners;
    }

    public void setTradeItemExtraDinners(List<TradeItemExtraDinner> tradeItemExtraDinners) {
        this.tradeItemExtraDinners = tradeItemExtraDinners;
    }
}
