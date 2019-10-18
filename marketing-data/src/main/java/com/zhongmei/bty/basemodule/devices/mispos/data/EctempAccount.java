package com.zhongmei.bty.basemodule.devices.mispos.data;

import java.io.Serializable;


public class EctempAccount implements Serializable {


    private static final long serialVersionUID = 1L;
    private String serverCreateTime;

    private String serverUpdateTime;

    private Long creatorId;

    private Long updatorId;

    private Integer statusFlag;

    private Long id;

    private Long brandId;

    private Long cardInstanceId;

    private Double remainValue;
    private Double totalValue;
    private int version;

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

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public int getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(int statusFlag) {
        this.statusFlag = statusFlag;
    }

    public Double getRemainValue() {
        return remainValue;
    }

    public void setRemainValue(Double remainValue) {
        this.remainValue = remainValue;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }

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

    public Long getCardInstanceId() {
        return cardInstanceId;
    }

    public void setCardInstanceId(Long cardInstanceId) {
        this.cardInstanceId = cardInstanceId;
    }
}
