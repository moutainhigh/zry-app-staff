package com.zhongmei.bty.data.operates.message.content;


public class PayStatusReq {

    private long paymentItemId;
    private String paymentItemUuid;
    private boolean returnPayment;

    public long getPaymentItemId() {
        return paymentItemId;
    }

    public void setPaymentItemId(long paymentItemId) {
        this.paymentItemId = paymentItemId;
    }

    public String getPaymentItemUuid() {
        return paymentItemUuid;
    }

    public void setPaymentItemUuid(String paymentItemUuid) {
        this.paymentItemUuid = paymentItemUuid;
    }

    public boolean isReturnPayment() {
        return returnPayment;
    }

    public void setReturnPayment(boolean returnPayment) {
        this.returnPayment = returnPayment;
    }
}
