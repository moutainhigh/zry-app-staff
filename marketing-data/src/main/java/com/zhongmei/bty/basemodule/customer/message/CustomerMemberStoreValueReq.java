package com.zhongmei.bty.basemodule.customer.message;


public class CustomerMemberStoreValueReq {
    private String fuzzyMobile;
    private int page;
    private int pageSize;
    private int tradeStatus;

    public String getFuzzyMobile() {
        return fuzzyMobile;
    }

    public void setFuzzyMobile(String fuzzyMobile) {
        this.fuzzyMobile = fuzzyMobile;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(int tradeStatus) {
        this.tradeStatus = tradeStatus;
    }


}
