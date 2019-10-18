package com.zhongmei.yunfu.bean.req;

import android.text.TextUtils;

import com.zhongmei.bty.basemodule.customer.bean.ICustomer;
import com.zhongmei.bty.basemodule.customer.bean.ICustomerListBean;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.Sex;

import java.io.Serializable;
import java.math.BigDecimal;

public class CustomerCreateResp implements Serializable, ICustomerListBean, ICustomer {

    private String uniqueCode;


    private String id;


    private String commercialid;


    private String brandid;


    private String invoice;


    private String birthday;


    private String profile;


    private String name;


    private String upgradeTime;


    private String hobby;


    private Integer gender;


    private String address;


    private String groupId;


    private Integer enabledFlag;


    private String groupLevelId;


    private String groupLevel;


    private String serverCreateTime;


    private String serverUpdateTime;


    private BigDecimal integral;


    private BigDecimal storedBalance;


    private String mobile;


    private String telephone;


    private String ext;


    private String email;


    private String commercialName;


    private String userid;


    private Integer source;


    private String uuid;


    private Integer hasFaceCode;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getExt() {
        return ext;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustomerID() {
        return id;
    }

    public String getBrandId() {
        return brandid;
    }

    public void setBrandId(String brandId) {
        this.brandid = brandId;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProfile() {
        return profile;
    }

    public String getEnvironmentHobby() {
        return hobby;
    }

    public Sex getGender() {
        return ValueEnums.toEnum(Sex.class, gender);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGroupId() {
        return groupId;
    }

    public boolean hasEnabledFlag() {
        return enabledFlag != null && enabledFlag == 1;
    }

    public String getGroupLevelId() {
        return groupLevelId;
    }

    public String getUpgradeTime() {
        return upgradeTime;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public BigDecimal getIntegral() {
        if (integral == null) {
            return BigDecimal.ZERO;
        }
        return integral;
    }

    public BigDecimal getStoredBalance() {
        if (storedBalance == null) {
            return BigDecimal.ZERO;
        }
        return storedBalance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommercialName() {
        return commercialName;
    }


    public String getLevelName() {
        return groupLevel;
    }

    public Integer getHasFaceCode() {
        if (hasFaceCode == null) {
            return 0;
        }
        return hasFaceCode;
    }

    @Override
    public CustomerListResp getCustomerListBean() {
        CustomerListResp bean = new CustomerListResp();
        bean.customerId = Long.parseLong(getCustomerID());
        bean.groupId = TextUtils.isEmpty(getGroupId()) ? null : Long.parseLong(getGroupId());
        bean.isDisable = hasEnabledFlag() ? 2 : 1;
        bean.levelId = TextUtils.isEmpty(getGroupLevelId()) ? null : Long.parseLong(getGroupLevelId());
        bean.name = getName();
        bean.mobile = getMobile();
        return bean;
    }

    @Override
    public CustomerResp getCustomer() {
        CustomerResp customer = new CustomerResp();
        customer.customerId = Long.parseLong(getCustomerID());
        customer.groupId = TextUtils.isEmpty(getGroupId()) ? null : Long.parseLong(getGroupId());
        customer.isDisable = hasEnabledFlag() ? 2 : 1;
        customer.levelId = TextUtils.isEmpty(getGroupLevelId()) ? null : Long.parseLong(getGroupLevelId());
        customer.customerName = getName();
        customer.mobile = getMobile();
        customer.memo = getProfile();
        customer.brandId = TextUtils.isEmpty(getBrandId()) ? null : Long.parseLong(getBrandId());
        customer.integral = getIntegral().compareTo(BigDecimal.ZERO) < 0 ? 0 : getIntegral().longValue();
        customer.synFlag = getUuid();
        customer.address = getAddress();
        customer.birthday = getBirthday();
        customer.sex = getGender().value();
        customer.remainValue = getStoredBalance().doubleValue();
        customer.hasFaceCode = getHasFaceCode();
        return customer;
    }
}
