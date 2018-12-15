package com.zhongmei.bty.data.operates.message.content;

/**
 * 预定开台信息
 * <p>
 * Created by demo on 2018/12/15
 */
public class BookingGroupOpenInfoReq {
    /**
     * 预定 ID
     */
    public Long bookingId;
    /**
     * 预定 UUID
     */
    public String bookingUuid;
    /**
     * 预定修改时间
     */
    public Long bookingServerUpdateTime;
    /**
     * 当前操作时间
     */
    public long shopArriveTime;
    /**
     * 当前操作人员
     */
    public String shopArriveUser;
}
