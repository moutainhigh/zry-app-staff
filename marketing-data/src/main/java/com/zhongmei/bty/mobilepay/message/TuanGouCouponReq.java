package com.zhongmei.bty.mobilepay.message;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 * 美团点评团购券查询参数
 */

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
