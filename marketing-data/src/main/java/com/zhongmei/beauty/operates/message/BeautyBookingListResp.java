package com.zhongmei.beauty.operates.message;

import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;
import com.zhongmei.bty.basemodule.booking.entity.BookingTradeItemProperty;
import com.zhongmei.beauty.entity.BookingTradeItemUser;

import java.util.List;

/**
 * 预定列表接口返回
 * Created by demo on 2018/12/15
 */

public class BeautyBookingListResp {
    public List<Booking> bookings;
    public List<BookingTradeItem> bookingTradeItems;
    public List<BookingTradeItemProperty> bookingTradeItemProperties;
    public List<BookingTradeItemUser> bookingTradeItemUsers;
    //    当前页数
    public int page;
    //    开始时间
    public long startTime;
    //    结束时间
    public long endTime;
}
