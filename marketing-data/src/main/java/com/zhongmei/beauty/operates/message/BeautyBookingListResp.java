package com.zhongmei.beauty.operates.message;

import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;
import com.zhongmei.bty.basemodule.booking.entity.BookingTradeItemProperty;
import com.zhongmei.beauty.entity.BookingTradeItemUser;

import java.util.List;



public class BeautyBookingListResp {
    public List<Booking> bookings;
    public List<BookingTradeItem> bookingTradeItems;
    public List<BookingTradeItemProperty> bookingTradeItemProperties;
    public List<BookingTradeItemUser> bookingTradeItemUsers;
        public int page;
        public long startTime;
        public long endTime;
}
