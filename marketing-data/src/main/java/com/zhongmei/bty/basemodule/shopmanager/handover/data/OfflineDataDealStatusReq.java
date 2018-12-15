package com.zhongmei.bty.basemodule.shopmanager.handover.data;

/**
 * Created by demo on 2018/12/15
 */

public class OfflineDataDealStatusReq {
    Long shopId;
    Long brandId;
    Long dateStart;
    Long dateEnd;

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
