package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

import java.util.HashMap;
import java.util.Map;

public class CustomTimesReq extends BaseRequest {

    public String scriptId = "crow_001";

    public Map<String, Long> params = new HashMap<>();
    public void setBrandId(Long brandId) {
        this.shopIdenty = brandId;
        params.put("brandId", brandId);
    }

    public void setCustomerId(Long customerId) {
        params.put("customerId", customerId);
    }
}
