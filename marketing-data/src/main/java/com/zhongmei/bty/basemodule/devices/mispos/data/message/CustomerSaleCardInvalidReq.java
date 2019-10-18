package com.zhongmei.bty.basemodule.devices.mispos.data.message;



public class CustomerSaleCardInvalidReq {
    private long tradeId;
    private long serverUpdateTime;

    public long getTradeId() {
        return tradeId;
    }

    public void setTradeId(long tradeId) {
        this.tradeId = tradeId;
    }

    public long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }


}
