package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.yunfu.db.entity.trade.TradeExtra;

/**
 * Created by demo on 2018/12/15
 */

public class TakeDishResp {
    public TradeExtra getTradeExtra() {
        return tradeExtra;
    }

    public void setTradeExtra(TradeExtra tradeExtra) {
        this.tradeExtra = tradeExtra;
    }

    TradeExtra tradeExtra;
}
