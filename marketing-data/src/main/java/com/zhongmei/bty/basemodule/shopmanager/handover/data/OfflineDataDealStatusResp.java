package com.zhongmei.bty.basemodule.shopmanager.handover.data;



public class OfflineDataDealStatusResp {
    Long shopId;
    Long brandId;
    Long dateStart;
    Long dateEnd;
    Integer failedQuantity;
    Integer untreatedQuantity;
    Long serverCreateTime;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Integer getFailedQuantity() {
        return failedQuantity;
    }

    public void setFailedQuantity(Integer failedQuantity) {
        this.failedQuantity = failedQuantity;
    }

    public Integer getUntreatedQuantity() {
        return untreatedQuantity;
    }

    public void setUntreatedQuantity(Integer untreatedQuantity) {
        this.untreatedQuantity = untreatedQuantity;
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public Long getDateStart() {
        return dateStart;
    }

    public void setDateStart(Long dateStart) {
        this.dateStart = dateStart;
    }

    public Long getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Long dateEnd) {
        this.dateEnd = dateEnd;
    }
}
