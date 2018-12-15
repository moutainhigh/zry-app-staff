package com.zhongmei.bty.basemodule.booking.event;

import com.zhongmei.yunfu.db.entity.booking.BookingTable;

import java.util.HashMap;

/**
 * @Date 2016/8/12
 * @Description:刷新预定信息
 */
public class EventRefreshReserveNotice {
    public HashMap<String, BookingTable> bookingMap;

    public EventRefreshReserveNotice(HashMap<String, BookingTable> bookingMap) {
        this.bookingMap = bookingMap;
    }
}
