package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.bty.basemodule.pay.message.PaymentReq;

import java.util.List;


public class DeliveredPaymentReq {

    private List<PaymentReq> deliveredPayments;

    public List<PaymentReq> getDeliveredPayments() {
        return deliveredPayments;
    }

    public void setDeliveredPayments(List<PaymentReq> deliveredPayments) {
        this.deliveredPayments = deliveredPayments;
    }

    public static class DeliveredPayment extends PaymentReq {
        private int deliveredStatus;

        public int getDeliveredStatus() {
            return deliveredStatus;
        }

        public void setDeliveredStatus(int deliveredStatus) {
            this.deliveredStatus = deliveredStatus;
        }
    }

}
