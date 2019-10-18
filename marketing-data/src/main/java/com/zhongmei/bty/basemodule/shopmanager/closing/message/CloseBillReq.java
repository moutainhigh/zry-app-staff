package com.zhongmei.bty.basemodule.shopmanager.closing.message;


public class CloseBillReq {
        private Long creatorId;

        private String creatorName;

        private String uuid;

        private String clientCreateTime;

        private String clientUpdateTime;

        private Long creatorAccount;

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getClientCreateTime() {
        return clientCreateTime;
    }

    public void setClientCreateTime(String clientCreateTime) {
        this.clientCreateTime = clientCreateTime;
    }

    public String getClientUpdateTime() {
        return clientUpdateTime;
    }

    public void setClientUpdateTime(String clientUpdateTime) {
        this.clientUpdateTime = clientUpdateTime;
    }

    public Long getCreatorAccount() {
        return creatorAccount;
    }

    public void setCreatorAccount(Long creatorAccount) {
        this.creatorAccount = creatorAccount;
    }

}
