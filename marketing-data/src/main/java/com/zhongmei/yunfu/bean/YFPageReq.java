package com.zhongmei.yunfu.bean;

public abstract class YFPageReq {

    protected int pageNo = 1;
    protected int pageSize = 50;

    public YFPageReq(int pageNo) {
        this(pageNo, 100);
    }

    public YFPageReq(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
