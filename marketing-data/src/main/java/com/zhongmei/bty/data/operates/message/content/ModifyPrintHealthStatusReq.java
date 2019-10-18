package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.bty.commonmodule.database.enums.HealthStatus;
import com.zhongmei.yunfu.util.ValueEnums;


public class ModifyPrintHealthStatusReq {

    private Long id;
    private Long updatorId;

    private String uuid;

    private Long clientUpdateTime;

    private Long serverUpdateTime;

    private Integer healthStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getClientUpdateTime() {
        return clientUpdateTime;
    }

    public void setClientUpdateTime(Long clientUpdateTime) {
        this.clientUpdateTime = clientUpdateTime;
    }

    public HealthStatus getHealthStatus() {
        return ValueEnums.toEnum(HealthStatus.class, healthStatus);
    }

    public void setHealthStatus(HealthStatus healthStatus) {
        this.healthStatus = ValueEnums.toValue(healthStatus);
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }
}
