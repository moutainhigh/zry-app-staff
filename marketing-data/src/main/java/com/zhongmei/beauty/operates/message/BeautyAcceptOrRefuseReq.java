package com.zhongmei.beauty.operates.message;


import com.zhongmei.bty.basemodule.booking.entity.BookingTradeItemProperty;
import com.zhongmei.beauty.entity.BookingTradeItemUser;
import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;

import java.util.List;

/**
 * 编辑预定数据
 *
 * @date 2018/7/23
 */
public class BeautyAcceptOrRefuseReq extends BaseRequest {


    private Long bookingId;
    private Long bookingServerUpdateTime;
    private Integer toOrderStatus;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getBookingServerUpdateTime() {
        return bookingServerUpdateTime;
    }

    public void setBookingServerUpdateTime(Long bookingServerUpdateTime) {
        this.bookingServerUpdateTime = bookingServerUpdateTime;
    }

    public Integer getToOrderStatus() {
        return toOrderStatus;
    }

    public void setToOrderStatus(Integer toOrderStatus) {
        this.toOrderStatus = toOrderStatus;
    }
}
