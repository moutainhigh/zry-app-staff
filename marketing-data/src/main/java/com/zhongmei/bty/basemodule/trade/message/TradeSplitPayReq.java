package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.trade.message.TradePaymentReq;
import com.zhongmei.bty.basemodule.trade.message.TradeReq;

/**
 * @Date：2015-11-3 下午5:19:39
 * @Description: 正餐拆单及支付请求数据
 * @Version: 1.0
 */
public class TradeSplitPayReq {
    private TradeReq source;//原单
    private TradePaymentReq target;//子单及子单支付

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
