package com.zhongmei.bty.basemodule.trade.message;

import java.util.List;

/**
 * 批量解绑优惠券接口请求体
 */
public class TradeBatchUnbindCouponReq {
    private Long tradeId;
    private String tradeUuid;//可选参数，用于快餐异步机制
    private List<Long> tradePrivilegeIds;
    private List<String> tradePrivilegeUuids;//可选参数，用于快餐异步机制

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
