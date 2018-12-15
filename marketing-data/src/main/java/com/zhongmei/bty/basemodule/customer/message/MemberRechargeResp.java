package com.zhongmei.bty.basemodule.customer.message;

import com.google.gson.JsonObject;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Payment;

import java.util.List;

/**
 * 会员充值接口回复数据封装
 *
 * @version: 1.0
 * @date 2015年5月8日
 */
public class MemberRechargeResp {

    private List<JsonObject> memberValuecard;

    private List<JsonObject> memberValuecardHistory;

    private List<Payment> payments;

    private List<PaymentItem> paymentItems;
    private List<JsonObject> memberIntegrals;

    public List<JsonObject> getMemberValuecard() {
        return memberValuecard;
    }

    public void setMemberValuecard(List<JsonObject> memberValuecard) {
        this.memberValuecard = memberValuecard;
    }

    public List<JsonObject> getMemberValuecardHistory() {
        return memberValuecardHistory;
    }

    public List<JsonObject> getMemberIntegrals() {
        return memberIntegrals;
    }

    public void setMemberIntegrals(List<JsonObject> memberIntegrals) {
        this.memberIntegrals = memberIntegrals;
    }

    public void setMemberValuecardHistory(List<JsonObject> memberValuecardHistory) {
        this.memberValuecardHistory = memberValuecardHistory;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<PaymentItem> getPaymentItems() {
        return paymentItems;
    }

    public void setPaymentItems(List<PaymentItem> paymentItems) {
        this.paymentItems = paymentItems;
    }
}
