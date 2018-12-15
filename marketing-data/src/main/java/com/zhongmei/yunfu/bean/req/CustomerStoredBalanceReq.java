package com.zhongmei.yunfu.bean.req;

public class CustomerStoredBalanceReq extends CustomerRefReq {

    private int type;// 0：充值，1：消费退款，2：充值撤销, 3：消费，5，批量储值,6，批量消储，7:补录， 8:补扣， 12:转入，90:导入, 98:支付宝合并，99:微信合并

    private int allHistory;//value = 1 --所有的储值记录， 不受type限制

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
