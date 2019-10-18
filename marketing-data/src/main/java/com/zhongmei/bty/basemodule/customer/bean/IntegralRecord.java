package com.zhongmei.bty.basemodule.customer.bean;

public class IntegralRecord {

    private Long id;
    private Integer recordType;
    private String beforeIntegral;
    private String addIntegral;
    private String endIntegral;
    private String userId;
    private String commercialId;
    private String commercialGroupId;
    private String commercialMemberId;
    private long createDateTime;
    private long modifyDateTime;
    private String status;
    private String reason;

    private String aggregateCount;

    public String getAggregateCount() {
        return aggregateCount;
    }

    public void setAggregateCount(String aggregateCount) {
        this.aggregateCount = aggregateCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getBeforeIntegral() {
        return beforeIntegral;
    }

    public void setBeforeIntegral(String beforeIntegral) {
        this.beforeIntegral = beforeIntegral;
    }

    public String getAddIntegral() {
        return addIntegral;
    }

    public void setAddIntegral(String addIntegral) {
        this.addIntegral = addIntegral;
    }

    public String getEndIntegral() {
        return endIntegral;
    }

    public void setEndIntegral(String endIntegral) {
        this.endIntegral = endIntegral;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(String commercialId) {
        this.commercialId = commercialId;
    }

    public String getCommercialGroupId() {
        return commercialGroupId;
    }

    public void setCommercialGroupId(String commercialGroupId) {
        this.commercialGroupId = commercialGroupId;
    }

    public String getCommercialMemberId() {
        return commercialMemberId;
    }

    public void setCommercialMemberId(String commercialMemberId) {
        this.commercialMemberId = commercialMemberId;
    }

    public long getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(long createDateTime) {
        this.createDateTime = createDateTime;
    }

    public long getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(long modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }
}
