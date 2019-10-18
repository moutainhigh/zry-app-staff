package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.ActionType;

import java.util.List;


public class TradeBatchOpsReq {

    private Long reasonId;
    private String reasonContent;
    private Long clientUpdateTime;
    private Long updatorId;
    private String updatorName;
    private Integer actionType;
    private List<TradeOpsReq> list;

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonContent() {
        return reasonContent;
    }

    public void setReasonContent(String reasonContent) {
        this.reasonContent = reasonContent;
    }

    public Long getClientUpdateTime() {
        return clientUpdateTime;
    }

    public void setClientUpdateTime(Long clientUpdateTime) {
        this.clientUpdateTime = clientUpdateTime;
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

    public ActionType getActionType() {
        return ValueEnums.toEnum(ActionType.class, actionType);
    }

    public void setActionType(ActionType actionType) {
        this.actionType = ValueEnums.toValue(actionType);
    }

    public List<TradeOpsReq> getList() {
        return list;
    }

    public void setList(List<TradeOpsReq> list) {
        this.list = list;
    }
}
