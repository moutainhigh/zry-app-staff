package com.zhongmei.bty.dinner.ordercenter.bean;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGroupon;
import com.zhongmei.bty.basemodule.pay.entity.RefundExceptionReason;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class PayModeDetailsBean {

    private PaymentType paymentType;
    //用券名称
    private String payModeName;
    //用券总金额
    private BigDecimal payModeTotalDenomination;
    //使用券的详情
    private List<PaymentItemGroupon> payModeItems;

    private PaymentItem paymentItem;

    private List<RefundExceptionReason> refundExceptionReasonList;

    public PayModeDetailsBean() {
    }

    public PayModeDetailsBean(PaymentType paymentType, String payModeName, BigDecimal payModeTotalDenomination, List<PaymentItemGroupon> payModeItems, PaymentItem paymentItem) {
        this.paymentType = paymentType;
        this.payModeName = payModeName;
        this.payModeTotalDenomination = payModeTotalDenomination;
        this.payModeItems = payModeItems;
        this.paymentItem = paymentItem;
    }

    public String getPayModeName() {
        return payModeName;
    }

    public BigDecimal getPayModeTotalDenomination() {
        return payModeTotalDenomination;
    }

    public List<PaymentItemGroupon> getPayModeItems() {
        return payModeItems;
    }

    public void setPayModeName(String payModeName) {
        this.payModeName = payModeName;
    }

    public void setPayModeTotalDenomination(BigDecimal payModeTotalDenomination) {
        this.payModeTotalDenomination = payModeTotalDenomination;
    }

    public void setPayModeItems(List<PaymentItemGroupon> payModeItems) {
        this.payModeItems = payModeItems;
    }

    public PaymentItem getPaymentItem() {
        return paymentItem;
    }

    public void setPaymentItem(PaymentItem paymentItem) {
        this.paymentItem = paymentItem;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public List<RefundExceptionReason> getRefundExceptionReasonList() {
        return refundExceptionReasonList;
    }

    public void setRefundExceptionReasonList(List<RefundExceptionReason> refundExceptionReasonList) {
        this.refundExceptionReasonList = refundExceptionReasonList;
    }

    public RefundExceptionReason getNewestRefundExceptionReason() {
        RefundExceptionReason refundExceptionReason = null;
        if (Utils.isNotEmpty(refundExceptionReasonList)) {
            for (RefundExceptionReason rer : refundExceptionReasonList) {
                if (refundExceptionReason == null || refundExceptionReason.getServerUpdateTime() == null) {
                    refundExceptionReason = rer;
                    continue;
                }

                if (rer.getServerUpdateTime() != null && rer.getServerUpdateTime() > refundExceptionReason.getServerUpdateTime()) {
                    refundExceptionReason = rer;
                    continue;
                }
            }
        }

        return refundExceptionReason;
    }
}
