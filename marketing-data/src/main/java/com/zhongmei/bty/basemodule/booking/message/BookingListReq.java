package com.zhongmei.bty.basemodule.booking.message;

/**
 * Desc
 *
 * @created 2017/8/24
 */
public class BookingListReq {

    private final long startTime;
    private final long endTime;

    public BookingListReq(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
