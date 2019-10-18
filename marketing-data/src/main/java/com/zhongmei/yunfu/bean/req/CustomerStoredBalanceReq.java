package com.zhongmei.yunfu.bean.req;

public class CustomerStoredBalanceReq extends CustomerRefReq {

    private int type;
    private int allHistory;
    public CustomerStoredBalanceReq(int allHistory, Long customerId) {
        this.allHistory = allHistory;
        this.customerId = customerId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAllHistory() {
        return allHistory;
    }

    public void setAllHistory(int allHistory) {
        this.allHistory = allHistory;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
