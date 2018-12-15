package com.zhongmei.bty.basemodule.trade.message;

/**
 * @Date： 2018/6/4
 * @Description:
 * @Version: 1.0
 */
public class DeletePrePayTradeReq {

    private Long tradeId;
    private Long serverUpdateTime;
    private Long updatorId;
    private String updatorName;

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
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
