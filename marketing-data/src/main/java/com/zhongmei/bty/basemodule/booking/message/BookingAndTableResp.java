package com.zhongmei.bty.basemodule.booking.message;

import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.db.entity.booking.BookingTable;

import java.util.List;

/**
 * 接收数据
 */
public class BookingAndTableResp {

    public Booking booking;
    public List<BookingTable> bookingTables;

}
