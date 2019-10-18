package com.zhongmei.yunfu.bean.req;

import com.zhongmei.yunfu.bean.YFPageReq;


public class CustomerMemberStoreReq extends YFPageReq {

    private Integer tradeStatus;     private String mobile;
    public CustomerMemberStoreReq(Integer tradeStatus, String mobile, Integer page, Integer pageSize) {
        super(page, pageSize);
        this.tradeStatus = tradeStatus;
        this.mobile = mobile;
    }

    public Integer getTradeStatus() {
        return tradeStatus;
    }

    public String getMobile() {
        return mobile;
    }
}
