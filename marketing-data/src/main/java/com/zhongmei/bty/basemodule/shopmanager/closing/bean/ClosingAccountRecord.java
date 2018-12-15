package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import java.io.Serializable;

/**
 * 关账记录
 */
public class ClosingAccountRecord implements Serializable {

    /**
     * @date：2015年12月18日 上午10:14:02
     * @Description:TODO
     */
    private static final long serialVersionUID = 1L;
    private Long id;                    // 服务端自增ID
    private Long belongDate;            // 归账日
    private Long startTime;        // 开始时间（上次关账时间）
    private Long endTime;          // 结束时间（本次关账时间）
    private String deviceIdenty;        // 发起操作的设备ID
    private Long creatorId;             // 操作人ID
    private String creatorName;         // 操作员名称
    private Long brandIdenty;           // 品牌标识
    private Long shopIdenty;            // 门店标识
    private Integer statusFlag;         // 1:VALID:有效的\\r\\n2:INVALID:无效的
    private String uuid;                // UUID，本笔记录唯一值
    private Long clientCreateTime; // PAD本地创建时间
    private Long clientUpdateTime; // PAD本地最后修改时间
    private Long serverCreateTime; // 服务端创建时间
    private Long serverUpdateTime; // 服务端最后修改时间
    private Long creatorAccount;        // 操作员账号

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBelongDate() {
        return belongDate;
    }

    public void setBelongDate(Long belongDate) {
        this.belongDate = belongDate;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getDeviceIdenty() {
        return deviceIdenty;
    }

    public void setDeviceIdenty(String deviceIdenty) {
        this.deviceIdenty = deviceIdenty;
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

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getClientCreateTime() {
        return clientCreateTime;
    }

    public void setClientCreateTime(Long clientCreateTime) {
        this.clientCreateTime = clientCreateTime;
    }

    public Long getClientUpdateTime() {
        return clientUpdateTime;
    }

    public void setClientUpdateTime(Long clientUpdateTime) {
        this.clientUpdateTime = clientUpdateTime;
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

    public Long getCreatorAccount() {
        return creatorAccount;
    }

    public void setCreatorAccount(Long creatorAccount) {
        this.creatorAccount = creatorAccount;
    }

}
