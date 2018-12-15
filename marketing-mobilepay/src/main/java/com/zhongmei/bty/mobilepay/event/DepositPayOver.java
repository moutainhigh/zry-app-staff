package com.zhongmei.bty.mobilepay.event;

import com.zhongmei.bty.basemodule.pay.message.PayResp;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

/**
 * Created by demo on 2018/12/15
 */

public class DepositPayOver {
    private PayResp payResp;
    private TradeVo tradeVo;

    public DepositPayOver(PayResp payResp) {
        this.payResp = payResp;
    }

    public DepositPayOver(TradeVo tradeVo) {
        this.tradeVo = tradeVo;
    }

    public PayResp getPayResp() {
        return payResp;
    }

    public void setPayResp(PayResp payResp) {
        this.payResp = payResp;
    }

    public TradeVo getTradeVo() {
        return tradeVo;
    }
}
