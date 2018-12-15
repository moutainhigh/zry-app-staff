package com.zhongmei.bty.data.operates.message.content;

/**
 * Created by demo on 2018/12/15
 */

public class CheckAuthReq {

    private Long brandId;
    private Long commercialId;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(Long commercialId) {
        this.commercialId = commercialId;
    }
}
