package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.trade.message.TradeReq;

/**
 * @Date：2015-11-3 上午10:33:15
 * @Description: 正餐拆单
 * @Version: 1.0
 */
public class TradeSplitReq {
    private TradeReq source;//原单
    private TradeReq target;//新子单

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
