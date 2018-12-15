package com.zhongmei.beauty.operates.message;

/**
 * 取消预定 请求
 *
 * @date 2018/7/24
 */
public class BeautyBookingCancelReq {
    /**
     * bookingID
     */
    public Long cancelOrderUser;

    public Long bookingId;

    public Long brandIdenty;

    public Long shopIdenty;
    /**
     * 理由
     */
    public String reason;

}
