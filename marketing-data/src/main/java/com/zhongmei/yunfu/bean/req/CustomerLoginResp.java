package com.zhongmei.yunfu.bean.req;

import com.zhongmei.bty.basemodule.customer.message.CustomerInfoResp;
import com.zhongmei.bty.basemodule.customer.bean.ICustomer;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.Sex;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @version: 1.0
 * @date 2015年5月13日
 */
public class CustomerLoginResp implements Serializable, ICustomer {

    private Long customerId;//顾客id
    private String customerName;// 顾客名字 （新的登录接口使用）
    private Integer sex; //性别
    private String mobile;//联系电话 （新接口使用）
    private String phoneNumber;//手机号码(老接口使用)
    private String address;//顾客地址
    private Long levelId;
    private String level; //会员等级
    private BigDecimal valueCardBalance; //储值余额
    private BigDecimal integralBalance; //积分余额
    private String openId;//微信openID
    private double remainValue;//当前虚拟会员储值余额
    private Long integral;//当前积分
    private Integer cardCount;//实体卡（有/无）
    private Integer coupCount;//优惠券（有/无）
    private Integer isDisable;//是否停用 1.是停用; 2.否
    private double creditableValue;//可挂账总额度
    private double remainCreditValue;//可挂账余额
    private double usedCreditValue;//已挂账金额
    private List<CustomerInfoResp.Card> cardList;//实体卡列表
    private Long brandId;//品牌id
    private Long commercialId;//顾客所属门店id
    private Integer hasFaceCode;//是否有人脸识别码
    private Integer storedPrivilegeType; //储值支付优惠类型  折扣，折让
    private BigDecimal storedPrivilegeValue; //储值支付优惠值
    private BigDecimal storedFullAmount;

    /**
     * 国家英文名称(为空默认中国) = countryEN
     */
    public String nation;
    /**
     * 国家中文名称(为空默认中国) = countryZH
     */
    public String country;
    /**
     * 电话国际区码(为空默认中国) = AreaCode
     */
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

    /***
     *
     * 判断是否停用
     * */
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
        customer.commercialName = ""; //this.commercialName;
        customer.address = this.address;
        customer.hasFaceCode = this.hasFaceCode;
        customer.nation = this.nation;//add v8.16 修复国际化bug
        customer.country = this.country;//add v8.16 修复国际化bug
        customer.nationalTelCode = this.nationalTelCode;//add v8.16 修复国际化bug
        customer.storedPrivilegeType=this.storedPrivilegeType; //储值支付优惠类型  折扣，折让
        customer.storedPrivilegeValue=this.storedPrivilegeValue; //储值支付优惠值
        customer.storedFullAmount=this.storedFullAmount;
        if (cardList != null && cardList.size() > 0) {
            customer.cardList = new ArrayList<>();
            for (CustomerInfoResp.Card card : cardList) {
                customer.cardList.add(card.getCustomerCardItem());
            }
        }
        return customer;
    }
}
