package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;



public class BindCustomerFaceCodeReq extends BaseRequest {

    private Long userId;

    private Long customerId;

    private String faceCode;

    private String peopleId;

    public void setPeopleId(String peopleId) {
        this.peopleId = peopleId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setFaceCode(String faceCode) {
        this.faceCode = faceCode;
    }
}
