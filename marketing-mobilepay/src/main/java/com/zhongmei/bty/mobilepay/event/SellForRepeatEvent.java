package com.zhongmei.bty.mobilepay.event;



public class SellForRepeatEvent {

    private String tradeUuid;

    public SellForRepeatEvent(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }
}
