package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.trade.message.TradeReq;


public class TradeSplitReq {
    private TradeReq source;    private TradeReq target;
    public TradeReq getSource() {
        return source;
    }

    public void setSource(TradeReq source) {
        this.source = source;
    }

    public TradeReq getTarget() {
        return target;
    }

    public void setTarget(TradeReq target) {
        this.target = target;
    }
}
