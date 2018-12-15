package com.zhongmei.bty.basemodule.devices.mispos.data.message;


/**
 * @Date：2016年3月16日
 * @Description:售卡记录作废
 * @Version: 1.0
 */
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
