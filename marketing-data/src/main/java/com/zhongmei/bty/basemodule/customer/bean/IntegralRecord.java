package com.zhongmei.bty.basemodule.customer.bean;

public class IntegralRecord {

    private Long id;//序号

    private Integer recordType; //记录类型1储值、2消费

    private String beforeIntegral;// 储值之前的积分余额

    private String addIntegral;// 增加的积分

    private String endIntegral;// 增加积分之后的余额

    private String userId;// 操作员

    private String commercialId;// 商户Id

    private String commercialGroupId;// 品牌编号

    private String commercialMemberId;// 会员编号

    private long createDateTime;// 创建时间

    private long modifyDateTime;// 修改时间

    private String status;// 有效状态

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
