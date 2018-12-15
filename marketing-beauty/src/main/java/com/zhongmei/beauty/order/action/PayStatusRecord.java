package com.zhongmei.beauty.order.action;

/**
 * Created by demo on 2018/12/15
 */

public class PayStatusRecord {
    private String url;
    private Long tradeId;
    private Long paymentItemId;
    private Integer tryCount;

    public PayStatusRecord(String url, Long tradeId, Long paymentItemId) {
        this.url = url;
        this.tradeId = tradeId;
        this.paymentItemId = paymentItemId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getPaymentItemId() {
        return paymentItemId;
    }

    public void setPaymentItemId(Long paymentItemId) {
        this.paymentItemId = paymentItemId;
    }

    public Integer getTryCount() {
        return tryCount;
    }

    public void setTryCount(Integer tryCount) {
        this.tryCount = tryCount;
    }
}
