package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;


public class MemberModifyMobileReq extends BaseRequest {

    private Integer appType = 1;

    private Long userId;


    private Long customerId;

    private String newMobile;

    private String oldMobile;

    private String newNation;
    private String newCountry;
    private String newNationalTelCode;
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setNewMobile(String newMobile) {
        this.newMobile = newMobile;
    }

    public void setOldMobile(String oldMobile) {
        this.oldMobile = oldMobile;
    }

    public void setNation(String nation) {
        newNation = nation;
    }

    public void setCountry(String country) {
        newCountry = country;
    }

    public void setNationalTelCode(String nationalTelCode) {
        newNationalTelCode = nationalTelCode;
    }
}
