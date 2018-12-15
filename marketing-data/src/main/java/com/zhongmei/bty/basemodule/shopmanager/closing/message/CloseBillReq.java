package com.zhongmei.bty.basemodule.shopmanager.closing.message;

/**
 * 关账请求数据
 *
 * @date:2015年12月14日下午1:53:54
 */
public class CloseBillReq {
    // 创建者id
    private Long creatorId;

    // 创建者姓名
    private String creatorName;

    // 唯一标识
    private String uuid;

    // 客户端创建时间
    private String clientCreateTime;

    // 客户端修改时间
    private String clientUpdateTime;

    // 操作人id
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
