package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class RefundSubmitResp {

    public List<Payment> payments;
    public List<PaymentItem> paymentItems;
    public List<PaymentItemResultsBean> paymentItemResults;

    /*public static class PaymentsBean {
        public int id;
        public long bizDate;
        public long paymentTime;
        public int paymentType;
        public int relateId;
        public String relateUuid;
        public double receivableAmount;
        public double exemptAmount;
        public double actualAmount;
        public int brandIdenty;
        public int shopIdenty;
        public String deviceIdenty;
        public String uuid;
        public int statusFlag;
        public long clientCreateTime;
        public long serverCreateTime;
        public long serverUpdateTime;
        public long creatorId;
        public String creatorName;
        public long updatorId;
        public String updatorName;
        public int isPaid;
        public double shopActualAmount;
    }

    public static class PaymentItemsBean {
        public int id;
        public int paymentId;
        public String paymentUuid;
        public int payModeId;
        public String payModeName;
        public int payModelGroup;
        public double faceAmount;
        public double usefulAmount;
        public double changeAmount;
        public int payStatus;
        public int brandIdenty;
        public int shopIdenty;
        public String deviceIdenty;
        public String uuid;
        public int statusFlag;
        public long clientCreateTime;
        public long serverCreateTime;
        public long serverUpdateTime;
        public long creatorId;
        public String creatorName;
        public int isRefund;
        public int quantity;
        public String memo;
        public int refundWay;
        public int paySource;
        public String relateId;
    }*/

    public static class PaymentItemResultsBean {
        public Long paymentItemId;
        public String paymentItemUuid;
        public int resultStatus;
        public String resultMsg;
    }
}
