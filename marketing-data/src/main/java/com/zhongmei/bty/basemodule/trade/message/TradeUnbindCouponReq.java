package com.zhongmei.bty.basemodule.trade.message;

public class TradeUnbindCouponReq {
    private Long tradeId;

    private Long tradePrivilegeId;

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getTradePrivilegeId() {
        return tradePrivilegeId;
    }

    public void setTradePrivilegeId(Long tradePrivilegeId) {
        this.tradePrivilegeId = tradePrivilegeId;
    }
}
