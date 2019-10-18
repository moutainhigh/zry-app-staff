package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.trade.message.TradePaymentReq;
import com.zhongmei.bty.basemodule.trade.message.TradeReq;


public class TradeSplitPayReq {
    private TradeReq source;    private TradePaymentReq target;
    public TradeReq getSource() {
        return source;
    }

    public void setSource(TradeReq source) {
        this.source = source;
    }

    public TradePaymentReq getTarget() {
        return target;
    }

    public void setTarget(TradePaymentReq target) {
        this.target = target;
    }

}
