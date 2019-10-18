package com.zhongmei.bty.mobilepay.message;

import java.util.List;



public class TuanGouCouponReq {
    Long payModeId;
    String serialNumber;
    List<String> dishUuids;

    public void setDishUuids(List<String> dishUuids) {
        this.dishUuids = dishUuids;
    }

    public TuanGouCouponReq(Long payModeId, String serialNumber) {
        this.payModeId = payModeId;
        this.serialNumber = serialNumber;
    }

    public Long getPayModeId() {
        return payModeId;
    }

    public void setPayModeId(Long payModeId) {
        this.payModeId = payModeId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }


}
