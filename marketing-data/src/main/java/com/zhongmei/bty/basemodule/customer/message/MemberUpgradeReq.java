package com.zhongmei.bty.basemodule.customer.message;

/**
 * @Date：2015年10月16日 下午6:06:51
 * @Description: TODO
 * @Version: 1.0
 */
public class MemberUpgradeReq {
    private String phone;
    private String customerSyncFlag;
    private String sex;
    private String birthday;
    private String isAcceptSubscription;
    private String name;
    private String userId;
    private String commercialGroupId;
    private String memberCard;
    private String password;

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

    public MemberUpgradeReq(String phone, String customerSyncFlag, String sex, String birthday, String isAcceptSubscription
            , String name, String userId, String commercialGroupId, String memberCard, String password) {
        this.phone = phone;
        this.customerSyncFlag = customerSyncFlag;
        this.sex = sex;
        this.birthday = birthday;
        this.isAcceptSubscription = isAcceptSubscription;
        this.name = name;
        this.userId = userId;
        this.commercialGroupId = commercialGroupId;
        this.memberCard = memberCard;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCustomerSyncFlag() {
        return customerSyncFlag;
    }

    public void setCustomerSyncFlag(String customerSyncFlag) {
        this.customerSyncFlag = customerSyncFlag;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIsAcceptSubscription() {
        return isAcceptSubscription;
    }

    public void setIsAcceptSubscription(String isAcceptSubscription) {
        this.isAcceptSubscription = isAcceptSubscription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommercialGroupId() {
        return commercialGroupId;
    }

    public void setCommercialGroupId(String commercialGroupId) {
        this.commercialGroupId = commercialGroupId;
    }

    public String getMemberCard() {
        return memberCard;
    }

    public void setMemberCard(String memberCard) {
        this.memberCard = memberCard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
