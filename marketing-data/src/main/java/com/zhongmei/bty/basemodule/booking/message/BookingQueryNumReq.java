package com.zhongmei.bty.basemodule.booking.message;


public class BookingQueryNumReq {


    private long beginTime;
    private long endTime;

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
