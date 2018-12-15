package com.zhongmei.yunfu.bean.req;

import com.zhongmei.yunfu.bean.YFPageReq;

public class CustomerRefReq extends YFPageReq {

    protected Long customerId;
    protected Long lastId;

    public CustomerRefReq() {
        super(1);
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getLastId() {
        return lastId;
    }

    public void setLastId(Long lastId) {
        this.lastId = lastId;
    }
}
