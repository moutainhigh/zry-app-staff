package com.zhongmei.bty.basemodule.shopmanager.paymentmanager.entitys;

import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.bty.commonmodule.database.enums.SubjectType;

import java.io.Serializable;
import java.math.BigDecimal;


public class PaymentsDetailInfo implements Serializable {

    private Long id;

    private Long brandIdenty;

    private Long shopIdenty;

    private Long accountSubjectId;

    private BigDecimal amount;

    private String bizDate;

    private Integer type;

    private String name;

    private String payModeName;

    private Long payModeId;

    private Long serverCreateTime;

    private Long serverUpdateTime;

    private Long creatorId;

    private String creatorName;

    private Long updatorId;

    private String updatorName;

    private Long secondSubjectId;

    private String secondSubjectName;

    public Long getSecondSubjectId() {
        return secondSubjectId;
    }

    public void setSecondSubjectId(Long secondSubjectId) {
        this.secondSubjectId = secondSubjectId;
    }

    public String getSecondSubjectName() {
        return secondSubjectName;
    }

    public void setSecondSubjectName(String secondSubjectName) {
        this.secondSubjectName = secondSubjectName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getBizDate() {
        return bizDate;
    }

    public void setBizDate(String bizDate) {
        this.bizDate = bizDate;
    }

    public SubjectType getType() {
        return ValueEnums.toEnum(SubjectType.class, type);
    }

    public void setType(SubjectType type) {
        this.type = ValueEnums.toValue(type);
    }

    public String getPayModeName() {
        return payModeName;
    }

    public void setPayModeName(String payModeName) {
        this.payModeName = payModeName;
    }

    public PayModeId getPayModeId() {
        return ValueEnums.toEnum(PayModeId.class, payModeId);
    }

    public void setPayModeId(PayModeId payModeId) {
        this.payModeId = ValueEnums.toValue(payModeId);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
