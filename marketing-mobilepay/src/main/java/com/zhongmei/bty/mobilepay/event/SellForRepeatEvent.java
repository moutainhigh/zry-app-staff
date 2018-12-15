package com.zhongmei.bty.mobilepay.event;


/**
 * @Date： 16/9/23
 * @Description:
 * @Version: 1.0
 */
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
