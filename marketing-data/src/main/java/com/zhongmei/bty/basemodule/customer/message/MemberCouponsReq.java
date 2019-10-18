package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.yunfu.bean.req.CustomerRefReq;



public class MemberCouponsReq extends CustomerRefReq {
    private String clientType;    private Long brandId;    private Long commercialId;
    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

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
