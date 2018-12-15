package com.zhongmei.bty.basemodule.booking.message;

import java.util.List;

/**
 * @Date： 2018/6/29
 * @Description:
 * @Version: 1.0
 */
public class BookingQueryNumResp {

    private List<String> bookingDates;

    public List<String> getBookingDates() {
        return bookingDates;
    }

    public void setBookingDates(List<String> bookingDates) {
        this.bookingDates = bookingDates;
    }
}
