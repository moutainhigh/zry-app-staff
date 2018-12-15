package com.zhongmei.bty.basemodule.customer.message;

public class MemberCheckCodeReq {

    private int type;

    private String customerUuid;

    private String mobile;

    /**
     * 国家英文名称(为空默认中国) = countryEN
     */
    private String nation;
    /**
     * 国家中文名称(为空默认中国) = countryZH
     */
    private String country;
    /**
     * 电话国际区码(为空默认中国) = AreaCode
     */
    private String nationalTelCode;

    public MemberCheckCodeReq(int type, String customerUuid, String mobile) {
        this.type = type;
        this.customerUuid = customerUuid;
        this.mobile = mobile;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
