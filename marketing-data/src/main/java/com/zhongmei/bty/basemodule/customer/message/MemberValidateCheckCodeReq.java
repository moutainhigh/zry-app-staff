package com.zhongmei.bty.basemodule.customer.message;

public class MemberValidateCheckCodeReq {

    private String mobile;

    private Long checkCodeId;

    private String checkCode;

    public MemberValidateCheckCodeReq(String mobile, Long checkCodeId, String checkCode) {
        this.mobile = mobile;
        this.checkCodeId = checkCodeId;
        this.checkCode = checkCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getCheckCodeId() {
        return checkCodeId;
    }

    public void setCheckCodeId(Long checkCodeId) {
        this.checkCodeId = checkCodeId;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

}
