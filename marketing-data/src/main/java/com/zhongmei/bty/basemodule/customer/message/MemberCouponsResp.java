package com.zhongmei.bty.basemodule.customer.message;


import com.zhongmei.yunfu.bean.req.CustomerCouponResp;

import java.util.List;



public class MemberCouponsResp {
    private int currentPage;    private int pageSize;    private int totalRows;    private int startRow;    private int[] showPageNums;    private int totalPage;    private List<CustomerCouponResp> items;


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int[] getShowPageNums() {
        return showPageNums;
    }

    public void setShowPageNums(int[] showPageNums) {
        this.showPageNums = showPageNums;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<CustomerCouponResp> getItems() {
        return items;
    }

    public void setItems(List<CustomerCouponResp> items) {
        this.items = items;
    }
}
