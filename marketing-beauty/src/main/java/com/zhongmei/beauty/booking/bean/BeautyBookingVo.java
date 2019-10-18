package com.zhongmei.beauty.booking.bean;

import com.zhongmei.bty.basemodule.booking.bean.BookingTradeItemVo;
import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;
import com.zhongmei.bty.basemodule.booking.entity.BookingTradeItemProperty;
import com.zhongmei.beauty.entity.BookingTradeItemUser;
import com.zhongmei.beauty.enums.BeautyListType;

import java.io.Serializable;
import java.util.List;



public class BeautyBookingVo implements Serializable {

    private static final long serialVersionUID = -3480118100975924976L;

    private Booking booking;

    private List<BookingTradeItemVo> bookingTradeItemVos;
    private List<BookingTradeItem> bookingTradeItems;
    private List<BookingTradeItemProperty> bookingTradeItemProperties;
    private List<BookingTradeItemUser> bookingTradeItemUsers;

        private BeautyListType interfaceType;

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }


    public List<BookingTradeItem> getBookingTradeItems() {
        return bookingTradeItems;
    }

    public void setBookingTradeItems(List<BookingTradeItem> bookingTradeItems) {
        this.bookingTradeItems = bookingTradeItems;
    }

    public List<BookingTradeItemProperty> getBookingTradeItemProperties() {
        return bookingTradeItemProperties;
    }

    public void setBookingTradeItemProperties(List<BookingTradeItemProperty> bookingTradeItemProperties) {
        this.bookingTradeItemProperties = bookingTradeItemProperties;
    }

    public List<BookingTradeItemUser> getBookingTradeItemUsers() {
        return bookingTradeItemUsers;
    }

    public void setBookingTradeItemUsers(List<BookingTradeItemUser> bookingTradeItemUsers) {
        this.bookingTradeItemUsers = bookingTradeItemUsers;
    }

    public BeautyListType getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(BeautyListType interfaceType) {
        this.interfaceType = interfaceType;
    }

    public List<BookingTradeItemVo> getBookingTradeItemVos() {
        return bookingTradeItemVos;
    }

    public void setBookingTradeItemVos(List<BookingTradeItemVo> bookingTradeItemVos) {
        this.bookingTradeItemVos = bookingTradeItemVos;
    }

    public Long getBookingId() {
        return booking.getId();
    }
}
