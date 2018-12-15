package com.zhongmei.bty.basemodule.customer.message;

public class MemberResetPswdReq {

    private String customerUuid;

    private String mobile;

    private Long checkCodeId;

    private String checkCode;

    private String password;

    public String nation;
    public String country;
    public String nationalTelCode;

    public MemberResetPswdReq(String customerUuid, String mobile, Long checkCodeId, String checkCode, String password) {
        this.customerUuid = customerUuid;
        this.mobile = mobile;
        this.checkCodeId = checkCodeId;
        this.checkCode = checkCode;
        this.password = password;
    }

    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNationalTelCode() {
        return nationalTelCode;
    }

    public void setNationalTelCode(String nationalTelCode) {
        this.nationalTelCode = nationalTelCode;
    }
}
