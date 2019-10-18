package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.db.entity.trade.TradeUser;

import java.util.List;


public class VirtualCardRechargeReq {
    private long customerId;
    private int onlinePay;
    private TradeInfo tradeInfo;    private PaymentInfo paymentRequest;    private TradeUser tradeUser;    private List<TradeUser> tradeUsers;
    public int getOnlinePay() {
        return onlinePay;
    }

    public void setOnlinePay(int onlinePay) {
        this.onlinePay = onlinePay;
    }

    public TradeInfo getTradeInfo() {
        return tradeInfo;
    }

    public void setTradeInfo(TradeInfo tradeInfo) {
        this.tradeInfo = tradeInfo;
    }

    public PaymentInfo getPaymentRequest() {
        return paymentRequest;
    }

    public void setPaymentRequest(PaymentInfo paymentRequest) {
        this.paymentRequest = paymentRequest;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public TradeUser getTradeUser() {
        return tradeUser;
    }

    public void setTradeUser(TradeUser tradeUser) {
        this.tradeUser = tradeUser;
    }

    public List<TradeUser> getTradeUsers() {
        return tradeUsers;
    }

    public void setTradeUsers(List<TradeUser> tradeUsers) {
        this.tradeUsers = tradeUsers;
    }
}
