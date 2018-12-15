package com.zhongmei.yunfu.bean.req;

import android.text.TextUtils;

import com.zhongmei.bty.basemodule.customer.bean.ICustomer;
import com.zhongmei.bty.basemodule.customer.bean.ICustomerListBean;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.Sex;

import java.io.Serializable;
import java.math.BigDecimal;

public class CustomerCreateResp implements Serializable, ICustomerListBean, ICustomer {
    /**
     * 会员识别号
     */
    private String uniqueCode;

    /**
     * 顾客id
     */
    private String id;

    /**
     * 商户id
     */
    private String commercialid;

    /**
     * 品牌id
     */
    private String brandid;

    /**
     * 发票抬头
     */
    private String invoice;

    /**
     * 洗好
     */
    private String birthday;

    /**
     * 备注
     */
    private String profile;

    /**
     * 会员名称
     */
    private String name;

    /**
     * 成为会员时间
     */
    private String upgradeTime;

    /**
     * 用户喜好
     */
    private String hobby;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 地址
     */
    private String address;

    /**
     * 分组
     */
    private String groupId;

    /**
     * 启用
     */
    private Integer enabledFlag;

    /**
     * 级别
     */
    private String groupLevelId;

    /**
     * 会员等级
     */
    private String groupLevel;

    /**
     * 服务器创建时间
     */
    private String serverCreateTime;

    /**
     * 服务器更新时间
     */
    private String serverUpdateTime;

    /**
     * 积分
     */
    private BigDecimal integral;

    /**
     * 余额
     */
    private BigDecimal storedBalance;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 固话
     */
    private String telephone;

    /**
     * 分机
     */
    private String ext;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 门店名称
     */
    private String commercialName;

    /**
     * 操作人Id
     */
    private String userid;

    /**
     * 来源
     */
    private Integer source;

    /**
     * 同步
     */
    private String uuid;

    /**
     * 同步
     */
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
