package com.zhongmei.beauty.operates.message;

/**
 * 美业预定列表请求
 * Created by demo on 2018/12/15
 */

public class BeautyBookingListReq {
    //    开始时间
    private Long startTime;
    //    结束时间
    private Long endTime;
    //    请求页数
    private Integer page;
    //    查询列表类型，1待服务，2已超时(逾期)，3已取消
    private Integer type;
    //    每页下行数据数量
    private Integer pageCount = 30;

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }
}
