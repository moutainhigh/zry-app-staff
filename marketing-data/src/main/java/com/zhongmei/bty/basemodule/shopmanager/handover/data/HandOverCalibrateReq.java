package com.zhongmei.bty.basemodule.shopmanager.handover.data;

import com.zhongmei.bty.basemodule.monitor.shopmanager.handover.entitys.HandoverData;

/**
 * 重新校准请求
 */
public class HandOverCalibrateReq {

    public Long getRelateId() {
        return relateId;
    }

    public void setRelateId(Long relateId) {
        this.relateId = relateId;
    }

    public HandoverData getCashHandover() {
        return cashHandover;
    }

    public void setCashHandover(HandoverData cashHandover) {
        this.cashHandover = cashHandover;
    }

    HandoverData cashHandover;
    Long relateId;

}
