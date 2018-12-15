package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.bty.basemodule.pay.message.PaymentReq;

import java.util.List;

/**
 * 封装批量清账的数据
 *
 * @Date：2015-4-14 下午6:30:02
 * @Description: TODO
 * @Version: 1.0
 */
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
