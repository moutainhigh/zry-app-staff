package com.zhongmei.beauty.operates.message;

import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.bty.basemodule.booking.entity.BookingPeriod;
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;
import com.zhongmei.bty.basemodule.booking.entity.BookingTradeItemProperty;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.beauty.entity.BookingTradeItemUser;

import java.util.List;

/**
 * 预定响应
 *
 * @date 2018/7/23
 */
public class BeautyBookingResp {

    public Booking booking;

    public List<BookingTradeItem> bookingTradeItems;

    public List<BookingTradeItemProperty> bookingTradeItemProperties;

    public List<BookingTradeItemUser> bookingTradeItemUsers;

    public CustomerResp customer;

    public BookingPeriod bookingPeriod;
}
