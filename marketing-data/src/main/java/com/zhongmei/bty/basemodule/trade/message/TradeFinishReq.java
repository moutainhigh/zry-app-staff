package com.zhongmei.bty.basemodule.trade.message;

public class TradeFinishReq {

    private Long tradeId;

    private Long updatorId;

    private String updatorName;

    public TradeFinishReq(Long tradeId, Long updatorId, String updatorName) {
        this.tradeId = tradeId;
        this.updatorId = updatorId;
        this.updatorName = updatorName;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
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

}
