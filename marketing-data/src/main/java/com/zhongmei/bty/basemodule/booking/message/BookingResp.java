package com.zhongmei.bty.basemodule.booking.message;

import com.google.gson.JsonObject;
import com.zhongmei.bty.basemodule.booking.bean.BookingGroupInfo;
import com.zhongmei.bty.basemodule.booking.bean.BookingMealShellVo;
import com.zhongmei.bty.basemodule.booking.bean.BookingTradeItemVo;
import com.zhongmei.bty.basemodule.booking.bean.BookingVo;
import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.bty.basemodule.booking.entity.BookingPeriod;
import com.zhongmei.yunfu.db.entity.booking.BookingTable;
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;
import com.zhongmei.bty.basemodule.booking.entity.BookingTradeItemProperty;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BookingResp {
    private Booking booking;

    private List<Booking> bookings;

    private List<BookingTable> bookingTable;

    private List<BookingTradeItem> bookingTradeItem;

    private List<BookingTradeItemProperty> bookingTradeItemProperty;

    private BookingGroupInfo bookingGroupInfo;

    private BookingPeriod bookingPeriod;

    private List<JsonObject> customers;

    private BookingVo mBookingVo;

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public List<BookingTable> getBookingTables() {
        return bookingTable;
    }

    public void setBookingTable(List<BookingTable> bookingTables) {
        this.bookingTable = bookingTables;
    }

    public List<JsonObject> getCustomers() {
        return customers;
    }

    public void setCustomers(List<JsonObject> customers) {
        this.customers = customers;
    }

    public List<BookingTradeItem> getBookingTradeItem() {
        return bookingTradeItem;
    }

    public void setBookingTradeItem(List<BookingTradeItem> bookingTradeItem) {
        this.bookingTradeItem = bookingTradeItem;
    }

    public List<BookingTradeItemProperty> getBookingTradeItemProperty() {
        return bookingTradeItemProperty;
    }

    public void setBookingTradeItemProperty(List<BookingTradeItemProperty> bookingTradeItemProperty) {
        this.bookingTradeItemProperty = bookingTradeItemProperty;
    }

    public BookingGroupInfo getBookingGroupInfo() {
        return bookingGroupInfo;
    }

    public void setBookingGroupInfo(BookingGroupInfo bookingGroupInfo) {
        this.bookingGroupInfo = bookingGroupInfo;
    }

    public BookingPeriod getBookingPeriod() {
        return bookingPeriod;
    }

    public void setBookingPeriod(BookingPeriod bookingPeriod) {
        this.bookingPeriod = bookingPeriod;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }


    public BookingVo getBookingVo() {
        if (mBookingVo == null) {
            mBookingVo = new BookingVo();
            mBookingVo.setBooking(booking);
            mBookingVo.setBookingTableList(bookingTable);
            mBookingVo.setBookingPeriod(bookingPeriod);
            mBookingVo.setBookingGroupInfo(bookingGroupInfo);
            buildTradeItemVos();
        }
        return mBookingVo;
    }

    private void buildTradeItemVos() {
        if (Utils.isEmpty(bookingTradeItem)) {
            return;
        }
        Map<String, BookingTradeItemVo> bookingTradeItemVoMap = new HashMap<>();
        for (BookingTradeItem bTradeItem : bookingTradeItem) {
                        if (bTradeItem.getType() == DishType.MEAL_SHELL) {
                BookingMealShellVo bookingMealShellVo = new BookingMealShellVo();
                bookingMealShellVo.setBookingTradeItem(bTradeItem);
                mBookingVo.setMealShellVo(bookingMealShellVo);
                continue;
            }
            BookingTradeItemVo bookingTradeItemVo = new BookingTradeItemVo();
            bookingTradeItemVo.setTradeItem(bTradeItem);
            bookingTradeItemVoMap.put(bTradeItem.getUuid(), bookingTradeItemVo);
        }
        List<BookingTradeItemVo> bookingTradeItemVoList = new ArrayList<BookingTradeItemVo>();
        bookingTradeItemVoList.addAll(bookingTradeItemVoMap.values());
        mBookingVo.setTradeItemVoList(bookingTradeItemVoList);
        if (Utils.isEmpty(bookingTradeItemProperty)) {
            return;
        }
        for (BookingTradeItemProperty bTradeItemProperty : bookingTradeItemProperty) {
            BookingTradeItemVo bookingTradeItemVo = bookingTradeItemVoMap.get(bTradeItemProperty.getBookingTradeItemUuid());
            if (bookingTradeItemVo != null) {
                if (bookingTradeItemVo.getTradeItemPropertyList() == null) {
                    bookingTradeItemVo.setTradeItemPropertyList(new ArrayList<BookingTradeItemProperty>());
                }
                bookingTradeItemVo.getTradeItemPropertyList().add(bTradeItemProperty);
            }
        }

    }


}
