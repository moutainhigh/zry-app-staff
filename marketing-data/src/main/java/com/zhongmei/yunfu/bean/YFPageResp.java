package com.zhongmei.yunfu.bean;

public class YFPageResp {

    private long rowCount;


    private int pageSize;


    private long pageCount;


    private int currentPage;

    private Long queryTime;

    public long getRowCount() {
        return rowCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getPageCount() {
        return pageCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public Long getQueryTime() {
        return queryTime;
    }
}
