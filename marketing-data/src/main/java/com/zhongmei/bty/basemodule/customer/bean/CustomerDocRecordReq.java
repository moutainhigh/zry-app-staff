package com.zhongmei.bty.basemodule.customer.bean;

/**
 * Created by dingzb on 2019/6/25.
 */

public class CustomerDocRecordReq {

    private int pageNo = 0;
    private int  pageSize = 100;
    private Long customerId;
    private int type;//类型，1:事务性   2：疗效性   可不传
    private String title; //主题，可用来做模糊查询， 可不传

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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
