package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;


public class MemberModifyMobileReq extends BaseRequest {

    private Integer appType = 1;
    /**
     * 操作人ID
     * 必
     */
    private Long userId;

    /**
     * 必填
     */
    private Long customerId;

    private String newMobile;

    private String oldMobile;

    private String newNation;    //国家英文名称(为空默认中国)	否	String

    private String newCountry;    //国家中文名称(为空默认中国)	否	String

    private String newNationalTelCode;    //电话国际区码(为空默认中国)	否	String

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
