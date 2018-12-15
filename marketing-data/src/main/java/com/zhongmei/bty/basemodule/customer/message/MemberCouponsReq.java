package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.yunfu.bean.req.CustomerRefReq;

/**
 * Created by demo on 2018/12/15
 */

public class MemberCouponsReq extends CustomerRefReq {
    private String clientType;//客户端请求来源
    private Long brandId;//品牌ID
    private Long commercialId;//门店ID

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
