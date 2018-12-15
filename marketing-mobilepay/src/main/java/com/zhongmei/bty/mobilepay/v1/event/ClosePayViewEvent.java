package com.zhongmei.bty.mobilepay.v1.event;

public class ClosePayViewEvent {

    private Long tradeId;

    private String tradeUUid;

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeUUid() {
        return tradeUUid;
    }

    public void setTradeUUid(String tradeUUid) {
        this.tradeUUid = tradeUUid;
    }
}
