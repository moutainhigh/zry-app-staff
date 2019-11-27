package com.zhongmei.yunfu.bean.req;


import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType;
import com.zhongmei.yunfu.util.ValueEnums;


public class CustomerLoginReq {

    @Deprecated
    private String mobile;

    private Long brandId;
    private Long shopId;
    private Integer loginType;
    private String loginId;
    private String password;
    private boolean isNeedPwd;
    private boolean isNeedCredit;
    private boolean isNeedCard;
    public String nation;

    public String country;

    public String nationalTelCode;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public CustomerLoginType getLoginType() {
        return ValueEnums.toEnum(CustomerLoginType.class, loginType);
    }

    public void setLoginType(CustomerLoginType loginType) {
        this.loginType = ValueEnums.toValue(loginType);
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public boolean getIsNeedPwd() {
        return isNeedPwd;
    }

    public void setIsNeedPwd(boolean isNeedPwd) {
        this.isNeedPwd = isNeedPwd;
    }

    public boolean getIsNeedCredit() {
        return isNeedCredit;
    }

    public void setIsNeedCredit(boolean isNeedCredit) {
        this.isNeedCredit = isNeedCredit;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    public boolean getIsNeedCard() {
        return isNeedCard;
    }

    public void setIsNeedCard(boolean isNeedCard) {
        this.isNeedCard = isNeedCard;
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
