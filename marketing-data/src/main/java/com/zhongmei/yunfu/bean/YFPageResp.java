package com.zhongmei.yunfu.bean;

public class YFPageResp {
    /**
     * 总数
     */
    private long rowCount;

    /**
     * 每页显示条数，默认 10
     */
    private int pageSize;

    /**
     * 总页数
     */
    private long pageCount;

    /**
     * 当前页
     */
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
