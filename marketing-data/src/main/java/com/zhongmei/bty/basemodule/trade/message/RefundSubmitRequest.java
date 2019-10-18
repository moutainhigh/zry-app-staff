package com.zhongmei.bty.basemodule.trade.message;

import java.math.BigDecimal;


public class RefundSubmitRequest {

    public Long tradeId;
    public Long paymentItemId;
    public BigDecimal refundFee;

    public RefundSubmitRequest(Long tradeId, Long paymentItemId, BigDecimal refundFee) {
        this.tradeId = tradeId;
        this.paymentItemId = paymentItemId;
        this.refundFee = refundFee;
    }
}
