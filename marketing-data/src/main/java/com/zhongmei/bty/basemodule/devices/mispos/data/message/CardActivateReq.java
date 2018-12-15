package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

import java.util.List;

public class CardActivateReq extends BaseRequest {

    private Integer source;

    private Long userId;

    private Long customerId;

    private List<String> entityCardNos;

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<String> getEntityCardNos() {
        return entityCardNos;
    }

    public void setEntityCardNos(List<String> entityCardNos) {
        this.entityCardNos = entityCardNos;
    }

}
