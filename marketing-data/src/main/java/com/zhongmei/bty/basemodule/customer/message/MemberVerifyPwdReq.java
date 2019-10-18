package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;


public class MemberVerifyPwdReq extends BaseRequest {

    private Integer appType = 1;

    private Long userId;


    private String mobile;


    private Long customerId;


    private String password;


    private String entityCardNo;


    private Integer type;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEntityCardNo(String entityCardNo) {
        this.entityCardNo = entityCardNo;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "?appType=" + appType
                + "&brandId=" + brandIdenty
                + "&commercialId=" + shopIdenty
                + "&userId=" + userId
                + "&mobile=" + mobile
                + "&customerId=" + customerId
                + "&password=" + password
                + "&entityCardNo=" + entityCardNo
                + "&type=" + type;
    }
}
