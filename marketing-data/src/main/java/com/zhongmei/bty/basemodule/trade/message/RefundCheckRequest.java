package com.zhongmei.bty.basemodule.trade.message;

/**
 * 溢收订单退款检查
 * Created by demo on 2018/12/15
 */
public class RefundCheckRequest {

    public Long tradeId;
    public Long paymentItemId;

    public RefundCheckRequest(Long tradeId, Long paymentItemId) {
        this.tradeId = tradeId;
        this.paymentItemId = paymentItemId;
    }
}
