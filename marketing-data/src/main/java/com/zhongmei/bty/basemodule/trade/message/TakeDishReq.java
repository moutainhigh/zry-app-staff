package com.zhongmei.bty.basemodule.trade.message;

/**
 * Created by demo on 2018/12/15
 */

public class TakeDishReq {
    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    Long tradeId;

    public Integer getTakeDishStatus() {
        return takeDishStatus;
    }

    public void setTakeDishStatus(Integer takeDishStatus) {
        this.takeDishStatus = takeDishStatus;
    }

    public Long getClientUpdateTime() {
        return clientUpdateTime;
    }

    public void setClientUpdateTime(Long clientUpdateTime) {
        this.clientUpdateTime = clientUpdateTime;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    Integer takeDishStatus;
    Long clientUpdateTime;
    Long serverUpdateTime;
    Long operatorId;
    String operatorName;
}
