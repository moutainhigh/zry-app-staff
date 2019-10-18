package com.zhongmei.bty.basemodule.booking.bean;

import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;
import com.zhongmei.bty.basemodule.booking.entity.BookingTradeItemProperty;
import com.zhongmei.beauty.entity.BookingTradeItemUser;

import java.io.Serializable;
import java.util.List;



public class BookingTradeItemVo implements Serializable {

    private BookingTradeItem tradeItem;

    private List<BookingTradeItemProperty> tradeItemPropertyList;

    private List<BookingTradeItemUser> bookingTradeItemUsers;

    public BookingTradeItem getTradeItem() {
        return tradeItem;
    }

    public void setTradeItem(BookingTradeItem tradeItem) {
        this.tradeItem = tradeItem;
    }

    public List<BookingTradeItemProperty> getTradeItemPropertyList() {
        return tradeItemPropertyList;
    }

    public void setTradeItemPropertyList(List<BookingTradeItemProperty> tradeItemPropertyList) {
        this.tradeItemPropertyList = tradeItemPropertyList;
    }

    public List<BookingTradeItemUser> getBookingTradeItemUsers() {
        return bookingTradeItemUsers;
    }

    public void setBookingTradeItemUsers(List<BookingTradeItemUser> bookingTradeItemUsers) {
        this.bookingTradeItemUsers = bookingTradeItemUsers;
    }
}
