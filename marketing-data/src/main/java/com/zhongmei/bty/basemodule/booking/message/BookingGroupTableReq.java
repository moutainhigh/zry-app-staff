package com.zhongmei.bty.basemodule.booking.message;

/**
 * 预定桌台查询请求
 * <p>
 * Created by demo on 2018/12/15
 */
public class BookingGroupTableReq {
    /**
     * 时段开始时间
     */
    public Long startTime;
    /**
     * 时段结束时间
     */
    public Long endTime;
    /**
     * 时段的id
     */
    public Long periodId;
}
