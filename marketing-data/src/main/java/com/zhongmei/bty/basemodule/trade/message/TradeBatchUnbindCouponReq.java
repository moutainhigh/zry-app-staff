package com.zhongmei.bty.basemodule.trade.message;

import java.util.List;


public class TradeBatchUnbindCouponReq {
    private Long tradeId;
    private String tradeUuid;    private List<Long> tradePrivilegeIds;
    private List<String> tradePrivilegeUuids;
    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public List<Long> getTradePrivilegeIds() {
        return tradePrivilegeIds;
    }

    public void setTradePrivilegeIds(List<Long> tradePrivilegeIds) {
        this.tradePrivilegeIds = tradePrivilegeIds;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    public List<String> getTradePrivilegeUuids() {
        return tradePrivilegeUuids;
    }

    public void setTradePrivilegeUuids(List<String> tradePrivilegeUuids) {
        this.tradePrivilegeUuids = tradePrivilegeUuids;
    }
}
