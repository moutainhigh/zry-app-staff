package com.zhongmei.bty.basemodule.booking.event;

import com.zhongmei.yunfu.db.entity.booking.BookingTable;

import java.util.HashMap;


public class EventRefreshReserveNotice {
    public HashMap<String, BookingTable> bookingMap;

    public EventRefreshReserveNotice(HashMap<String, BookingTable> bookingMap) {
        this.bookingMap = bookingMap;
    }
}
