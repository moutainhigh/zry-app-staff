package com.zhongmei.bty.data.operates.message.content;


import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.basemodule.pay.message.PaymentTo;

import java.util.List;

/**
 * @date:2015年9月28日下午6:46:59
 */
public class PaymentDinnerTo {

    private long paymentTime;
    private Integer paymentType;
    private String relateUuid;
    private Long updatorId;
    private String updatorName;
    private List<PaymentTo> payments;

    public long getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(long paymentTime) {
        this.paymentTime = paymentTime;
    }

    public PaymentType getPaymentType() {
        return ValueEnums.toEnum(PaymentType.class, paymentType);
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = ValueEnums.toValue(paymentType);
    }

    public String getRelateUuid() {
        return relateUuid;
    }

    public void setRelateUuid(String relateUuid) {
        this.relateUuid = relateUuid;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public List<PaymentTo> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentTo> payments) {
        this.payments = payments;
    }
}
