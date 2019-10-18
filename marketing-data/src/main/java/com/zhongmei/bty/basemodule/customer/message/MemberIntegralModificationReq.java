package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

import java.util.Date;



public class MemberIntegralModificationReq extends BaseRequest {
        private Long customerId;
        private Integer operateType;
        private Integer integral;
        private long userId;
        private String reason;

    private Integer source;
        private Date bizDate;

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public void setBizDate(Date bizDate) {
        this.bizDate = bizDate;
    }
}
