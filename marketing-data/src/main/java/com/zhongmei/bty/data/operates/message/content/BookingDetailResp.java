package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.bty.basemodule.booking.bean.BookingGroupInfo;
import com.zhongmei.bty.basemodule.booking.entity.BookingPeriod;
import com.zhongmei.yunfu.db.entity.booking.BookingTable;
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;
import com.zhongmei.bty.basemodule.booking.entity.BookingTradeItemProperty;
import com.zhongmei.bty.basemodule.booking.bean.BookingDepositInfo;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class BookingDetailResp {

    private Booking booking;

    private BookingPeriod bookingPeriod;

    private List<BookingTable> bookingTable;

    private List<BookingTradeItem> bookingTradeItem;

    private List<BookingTradeItemProperty> bookingTradeItemPropertie;

    private BookingGroupInfo bookingGroupInfo;

    public Booking getBooking() {
        return booking;
    }

    public BookingDepositInfo bookingDepositInfo;

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public BookingPeriod getBookingPeriod() {
        return bookingPeriod;
    }

    public void setBookingPeriod(BookingPeriod bookingPeriod) {
        this.bookingPeriod = bookingPeriod;
    }

    public List<BookingTable> getBookingTable() {
        return bookingTable;
    }

    public void setBookingTable(List<BookingTable> bookingTable) {
        this.bookingTable = bookingTable;
    }

    public List<BookingTradeItem> getBookingTradeItem() {
        return bookingTradeItem;
    }

    public void setBookingTradeItem(List<BookingTradeItem> bookingTradeItem) {
        this.bookingTradeItem = bookingTradeItem;
    }

    public List<BookingTradeItemProperty> getBookingTradeItemPropertie() {
        return bookingTradeItemPropertie;
    }

    public void setBookingTradeItemPropertie(List<BookingTradeItemProperty> bookingTradeItemPropertie) {
        this.bookingTradeItemPropertie = bookingTradeItemPropertie;
    }

    public BookingGroupInfo getBookingGroupInfo() {
        return bookingGroupInfo;
    }

    public void setBookingGroupInfo(BookingGroupInfo bookingGroupInfo) {
        this.bookingGroupInfo = bookingGroupInfo;
    }


}
