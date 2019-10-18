package com.zhongmei.yunfu.bean.req;

import com.zhongmei.bty.basemodule.customer.message.CustomerInfoResp;
import com.zhongmei.bty.basemodule.customer.bean.ICustomer;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.Sex;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class CustomerLoginResp implements Serializable, ICustomer {

    private Long customerId;    private String customerName;    private Integer sex;     private String mobile;    private String phoneNumber;    private String address;    private Long levelId;
    private String level;     private BigDecimal valueCardBalance;     private BigDecimal integralBalance;     private String openId;    private double remainValue;    private Long integral;    private Integer cardCount;    private Integer coupCount;    private Integer isDisable;    private double creditableValue;    private double remainCreditValue;    private double usedCreditValue;    private List<CustomerInfoResp.Card> cardList;    private Long brandId;    private Long commercialId;    private Integer hasFaceCode;    private Integer storedPrivilegeType;     private BigDecimal storedPrivilegeValue;     private BigDecimal storedFullAmount=BigDecimal.ZERO;
    private CustomerType customerType;


    public String nation;

    public String country;

    public String nationalTelCode;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(Long commercialId) {
        this.commercialId = commercialId;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getIsDisable() {
        return isDisable;
    }

    public void setIsDisable(Integer isDisable) {
        this.isDisable = isDisable;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public double getRemainValue() {
        return remainValue;
    }

    public void setRemainValue(double remainValue) {
        this.remainValue = remainValue;
    }

    public Long getIntegral() {
        return integral;
    }

    public void setIntegral(Long integral) {
        this.integral = integral;
    }

    public Integer getCardCount() {
        return cardCount;
    }

    public void setCardCount(Integer cardCount) {
        this.cardCount = cardCount;
    }

    public Integer getCoupCount() {
        return coupCount;
    }

    public void setCoupCount(Integer coupCount) {
        this.coupCount = coupCount;
    }

    public double getCreditableValue() {
        return creditableValue;
    }

    public void setCreditableValue(double creditableValue) {
        this.creditableValue = creditableValue;
    }

    public double getRemainCreditValue() {
        return remainCreditValue;
    }

    public void setRemainCreditValue(double remainCreditValue) {
        this.remainCreditValue = remainCreditValue;
    }

    public double getUsedCreditValue() {
        return usedCreditValue;
    }

    public void setUsedCreditValue(double usedCreditValue) {
        this.usedCreditValue = usedCreditValue;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Sex getSex() {
        return ValueEnums.toEnum(Sex.class, sex);
    }

    public void setSex(Sex sex) {
        this.sex = ValueEnums.toValue(sex);
    }

    public BigDecimal getValueCardBalance() {
        return valueCardBalance;
    }

    public void setValueCardBalance(BigDecimal valueCardBalance) {
        this.valueCardBalance = valueCardBalance;
    }

    public BigDecimal getIntegralBalance() {
        return integralBalance;
    }

    public void setIntegralBalance(BigDecimal integralBalance) {
        this.integralBalance = integralBalance;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<CustomerInfoResp.Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<CustomerInfoResp.Card> cardList) {
        this.cardList = cardList;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public Integer getHasFaceCode() {
        return hasFaceCode;
    }

    public void setHasFaceCode(Integer hasFaceCode) {
        this.hasFaceCode = hasFaceCode;
    }

    public Integer getStoredPrivilegeType() {
        return storedPrivilegeType;
    }

    public void setStoredPrivilegeType(Integer storedPrivilegeType) {
        this.storedPrivilegeType = storedPrivilegeType;
    }

    public BigDecimal getStoredPrivilegeValue() {
        return storedPrivilegeValue;
    }

    public void setStoredPrivilegeValue(BigDecimal storedPrivilegeValue) {
        this.storedPrivilegeValue = storedPrivilegeValue;
    }

    public BigDecimal getStoredFullAmount() {
        return storedFullAmount;
    }

    public void setStoredFullAmount(BigDecimal storedFullAmount) {
        this.storedFullAmount = storedFullAmount;
    }


    public boolean customerIsDisable() {
        if (isDisable == 1) {
            return true;
        }
        return false;
    }

    @Override
    public CustomerResp getCustomer() {
        CustomerResp customer = new CustomerResp();
        customer.brandId = this.brandId;
        customer.customerId = this.customerId;
        customer.customerMainId = this.customerId;
        customer.memberId = this.customerId;
        customer.customerName = this.customerName;
        customer.mobile = this.mobile;
        customer.sex = this.sex;
        customer.levelId = this.levelId;
        customer.levelName = this.level;
        customer.isDisable = this.isDisable;
        customer.openId = this.openId;
        customer.remainValue = this.remainValue;
        customer.integral = this.integral;
        customer.cardCount = this.cardCount;
        customer.coupCount = this.coupCount;
        customer.creditableValue = this.creditableValue;
        customer.remainCreditValue = this.remainCreditValue;
        customer.usedCreditValue = this.usedCreditValue;
        customer.otherCardList = cardList;
        customer.commercialId = this.commercialId;
        customer.commercialName = "";         customer.address = this.address;
        customer.hasFaceCode = this.hasFaceCode;
        customer.nation = this.nation;        customer.country = this.country;        customer.nationalTelCode = this.nationalTelCode;        customer.storedPrivilegeType=this.storedPrivilegeType;         customer.storedPrivilegeValue=this.storedPrivilegeValue;         customer.storedFullAmount=this.storedFullAmount;
        customer.setCustomerType(this.customerType);
        if (cardList != null && cardList.size() > 0) {
            customer.cardList = new ArrayList<>();
            for (CustomerInfoResp.Card card : cardList) {
                customer.cardList.add(card.getCustomerCardItem());
            }
        }
        return customer;
    }
}
