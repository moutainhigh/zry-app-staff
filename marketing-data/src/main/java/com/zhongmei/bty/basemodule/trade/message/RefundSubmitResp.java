package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;

import java.util.List;



public class RefundSubmitResp {

    public List<Payment> payments;
    public List<PaymentItem> paymentItems;
    public List<PaymentItemResultsBean> paymentItemResults;



    public static class PaymentItemResultsBean {
        public Long paymentItemId;
        public String paymentItemUuid;
        public int resultStatus;
        public String resultMsg;
    }
}
