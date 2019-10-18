package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;

import java.math.BigDecimal;

public class ShowCodeReq extends PaymentItemReq {
    private String authCode;    private BigDecimal noDiscountAmount;
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
