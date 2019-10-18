package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.yunfu.db.enums.TradeReturnInfoReturnStatus;
import com.zhongmei.yunfu.util.ValueEnums;


public class ReturnConfirmReq {
    Long tradeId;

    Integer returnStatus;

    String requestUuid;

    String reason;

    String cancelCode;

    Long operatorId;

    String operatorName;

    boolean reviseStock;

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

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public TradeReturnInfoReturnStatus getReturnStatus() {
        return ValueEnums.toEnum(TradeReturnInfoReturnStatus.class, returnStatus);
    }

    public void setReturnStatus(TradeReturnInfoReturnStatus returnStatus) {
        this.returnStatus = ValueEnums.toValue(returnStatus);
    }

    public String getRequestUuid() {
        return requestUuid;
    }

    public void setRequestUuid(String requestUuid) {
        this.requestUuid = requestUuid;
    }

    public String getReason() {
        return reason;
    }

    public boolean isReviseStock() {
        return reviseStock;
    }

    public void setReviseStock(boolean reviseStock) {
        this.reviseStock = reviseStock;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setCancelCode(String cancelCode) {
        this.cancelCode = cancelCode;
    }
}
