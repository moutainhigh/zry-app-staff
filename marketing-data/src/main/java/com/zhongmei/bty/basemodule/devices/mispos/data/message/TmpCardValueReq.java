package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.pay.message.PaymentItemReq;

public class TmpCardValueReq extends PaymentItemReq {
    private String entityNo;
    public TmpCardValueReq(PaymentItem paymentItem) {
        super(paymentItem);
        entityNo = paymentItem.getPaymentItemExtra().getEntityNo();
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }
}
