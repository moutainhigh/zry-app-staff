package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.enums.PayModelGroup;
import com.zhongmei.yunfu.db.enums.PaySource;
import com.zhongmei.yunfu.db.enums.RefundWay;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.Bool;

import java.math.BigDecimal;

public class PaymentItemReq {
    protected BigDecimal changeAmount;    protected BigDecimal faceAmount;    protected Integer isRefund;    protected Long payModeId;    protected String payModeName;    protected Integer payModelGroup;    protected Integer paySource;    protected Integer refundWay;    protected BigDecimal usefulAmount;    protected String uuid;
    public PaymentItemReq(PaymentItem paymentItem) {
        changeAmount = paymentItem.getChangeAmount();
        faceAmount = paymentItem.getFaceAmount();
        isRefund = ValueEnums.toValue(Bool.YES);
        payModeId = paymentItem.getPayModeId();
        payModeName = paymentItem.getPayModeName();
        payModelGroup = ValueEnums.toValue(paymentItem.getPayModelGroup());
        paySource = ValueEnums.toValue(paymentItem.getPaySource());
        refundWay = ValueEnums.toValue(paymentItem.getRefundWay());
        usefulAmount = paymentItem.getUsefulAmount();
        uuid = paymentItem.getUuid();
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public BigDecimal getFaceAmount() {
        return faceAmount;
    }

    public void setFaceAmount(BigDecimal faceAmount) {
        this.faceAmount = faceAmount;
    }

    public Bool getIsRefund() {
        return ValueEnums.toEnum(Bool.class, isRefund);
    }

    public void setIsRefund(Bool isRefund) {
        this.isRefund = ValueEnums.toValue(isRefund);
    }

    public Long getPayModeId() {
        return payModeId;
    }

    public void setPayModeId(Long payModeId) {
        this.payModeId = payModeId;
    }

    public String getPayModeName() {
        return payModeName;
    }

    public void setPayModeName(String payModeName) {
        this.payModeName = payModeName;
    }

    public PayModelGroup getPayModelGroup() {
        return ValueEnums.toEnum(PayModelGroup.class, payModelGroup);
    }

    public void setPayModelGroup(PayModelGroup payModelGroup) {
        this.payModelGroup = ValueEnums.toValue(payModelGroup);
    }

    public PaySource getPaySource() {
        return ValueEnums.toEnum(PaySource.class, paySource);
    }

    public void setPaySource(PaySource paySource) {
        this.paySource = ValueEnums.toValue(paySource);
    }

    public RefundWay getRefundWay() {
        return ValueEnums.toEnum(RefundWay.class, refundWay);
    }

    public void setRefundWay(RefundWay refundWay) {
        this.refundWay = ValueEnums.toValue(refundWay);
    }

    public BigDecimal getUsefulAmount() {
        return usefulAmount;
    }

    public void setUsefulAmount(BigDecimal usefulAmount) {
        this.usefulAmount = usefulAmount;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
