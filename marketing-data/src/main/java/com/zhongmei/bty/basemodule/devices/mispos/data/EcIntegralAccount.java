package com.zhongmei.bty.basemodule.devices.mispos.data;

import java.io.Serializable;
import java.math.BigDecimal;

import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.ValueEnums;

public class EcIntegralAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long brandId;

    private Long customerId;

    private String serverCreateTime;

    private String serverUpdateTime;

    private Long creatorId;

    private String creatorName;

    private Long updatorId;

    private String updatorName;

    private Integer statusFlag;

    private Long cardInstanceId;

    private Long totalIntegral;

    private Long integral;

    private BigDecimal waitIntegralCash;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(String serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public String getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(String serverUpdateTime) {
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

    public StatusFlag getStatusFlag() {
        return ValueEnums.toEnum(StatusFlag.class, statusFlag);
    }

    public void setStatusFlag(StatusFlag statusFlag) {
        this.statusFlag = ValueEnums.toValue(statusFlag);
    }

    public Long getCardInstanceId() {
        return cardInstanceId;
    }

    public void setCardInstanceId(Long cardInstanceId) {
        this.cardInstanceId = cardInstanceId;
    }

    public Long getTotalIntegral() {
        return totalIntegral;
    }

    public void setTotalIntegral(Long totalIntegral) {
        this.totalIntegral = totalIntegral;
    }

    public Long getIntegral() {
        if (integral == null) {
            return 0L;
        }
        return integral;
    }

    public void setIntegral(Long integral) {
        this.integral = integral;
    }

    public BigDecimal getWaitIntegralCash() {
        return waitIntegralCash;
    }

    public void setWaitIntegralCash(BigDecimal waitIntegralCash) {
        this.waitIntegralCash = waitIntegralCash;
    }

}
