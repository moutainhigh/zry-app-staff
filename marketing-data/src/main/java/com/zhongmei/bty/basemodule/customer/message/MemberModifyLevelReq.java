package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;


public class MemberModifyLevelReq extends BaseRequest {

    private Integer appType = 1;

    private Long userId;

    private String name;


    private Long customerId;

    private Long levelId;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
