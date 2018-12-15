package com.zhongmei.bty.data.operates.message.content;

/**
 * Created by demo on 2018/12/15
 */

public class BookingPeriodReq {
    private Long startTime;

    private Long endTime;

    public BookingPeriodReq() {

    }

    public BookingPeriodReq(Long startTime, Long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

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
}
