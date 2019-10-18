package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.pay.message.PaymentItemReq;

public class EntityCardValueReq extends PaymentItemReq {
    private Long customerId;    private String entityNo;
    public EntityCardValueReq(PaymentItem paymentItem) {
        super(paymentItem);
        customerId = paymentItem.getPaymentItemExtra().getCustomerId();
        entityNo = paymentItem.getPaymentItemExtra().getEntityNo();
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }
}
