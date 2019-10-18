package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.PaymentType;

import java.util.List;


public class PaymentReq {

    private Long relateId;
    private String relateUuid;
    private Integer paymentType;
    private Long serverUpdateTime;
    private Long updatorId;
    private String updatorName;
    private List<PaymentTo> payments;
    private Long tradePayForm = 1L;
    public Long getRelateId() {
        return relateId;
    }

    public void setRelateId(Long relateId) {
        this.relateId = relateId;
    }

    public String getRelateUuid() {
        return relateUuid;
    }

    public void setRelateUuid(String relateUuid) {
        this.relateUuid = relateUuid;
    }

    public PaymentType getPaymentType() {
        return ValueEnums.toEnum(PaymentType.class, paymentType);
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = ValueEnums.toValue(paymentType);
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
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

    public Long getTradePayForm() {
        return tradePayForm;
    }

    public void setTradePayForm(Long tradePayForm) {
        this.tradePayForm = tradePayForm;
    }





}
