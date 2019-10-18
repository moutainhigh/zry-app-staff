package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.commonmodule.database.enums.SubjectType;

import java.math.BigDecimal;


public class PaymentsInputReq {

    private String name;

    private Integer type;

    private Long brandIdenty;

    private Integer statusFlag;

    private Long serverCreateTime;

    private Long serverUpdateTime;

    private Long creatorId;

    private String creatorName;

    private Long updatorId;

    private String updatorName;

    private Long shopIdenty;

    private Long accountSubjectId;
    private String bizDate;
    private BigDecimal amount;
    private Long payModeId;
    private String payModeName;

    public String getDeviceIdenty() {
        return deviceIdenty;
    }

    public void setDeviceIdenty(String deviceIdenty) {
        this.deviceIdenty = deviceIdenty;
    }

    private String deviceIdenty;

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public Long getAccountSubjectId() {
        return accountSubjectId;
    }

    public void setAccountSubjectId(Long accountSubjectId) {
        this.accountSubjectId = accountSubjectId;
    }

    public String getBizDate() {
        return bizDate;
    }

    public void setBizDate(String bizDate) {
        this.bizDate = bizDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getPayModeId() {
        return payModeId;
    }

    public void setPayModeId(Long payModeId) {
        this.payModeId = payModeId;
    }

    public String getPayModeName() {
        return payModeName;
    }

    public void setPayModeName(String payModeName) {
        this.payModeName = payModeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public SubjectType getType() {
        return ValueEnums.toEnum(SubjectType.class, type);
    }

    public void setType(SubjectType type) {
        this.type = ValueEnums.toValue(type);
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }
}
