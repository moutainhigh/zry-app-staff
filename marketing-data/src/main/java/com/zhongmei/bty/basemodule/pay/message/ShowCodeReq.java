package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;

import java.math.BigDecimal;

public class ShowCodeReq extends PaymentItemReq {
    private String authCode;//扫码支付授权码
    private BigDecimal noDiscountAmount;//不参与折扣的金额

    public ShowCodeReq(PaymentItem paymentItem) {
        super(paymentItem);
        authCode = paymentItem.getAuthCode();
        noDiscountAmount = paymentItem.getNoDiscountAmount();
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public BigDecimal getNoDiscountAmount() {
        return noDiscountAmount;
    }

    public void setNoDiscountAmount(BigDecimal noDiscountAmount) {
        this.noDiscountAmount = noDiscountAmount;
    }
}
