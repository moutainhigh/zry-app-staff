package com.zhongmei.bty.basemodule.customer.message;

import java.util.List;

/**
 * 顾客列表数据
 *
 * @date 2017/3/13 14:35
 */
public class CustomerListResp {

    public int currentPage;

    public int pageSize;

    public int rowCount;

    public int startRow;

    public int pageCount;

    /**
     * 服务器返回的查询时间
     */
    public Long queryTime;

    public List<com.zhongmei.yunfu.bean.req.CustomerListResp> items;

    public CustomerListResp() {
    }

    public CustomerListResp(int currentPage, int pageSize, int rowCount, int startRow, int pageCount, Long queryTime, List<com.zhongmei.yunfu.bean.req.CustomerListResp> items) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.rowCount = rowCount;
        this.startRow = startRow;
        this.pageCount = pageCount;
        this.queryTime = queryTime;
        this.items = items;
    }
}
