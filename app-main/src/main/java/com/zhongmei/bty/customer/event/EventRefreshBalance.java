package com.zhongmei.bty.customer.event;


/**
 * 刷新账户余额
 */
public class EventRefreshBalance {
    public final String cardNo;

    public EventRefreshBalance(String cardNo) {
        this.cardNo = cardNo;
    }

}
