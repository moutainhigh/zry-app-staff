package com.zhongmei.bty.basemodule.booking.bean;

import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.bty.basemodule.booking.message.BookingStatisticsResp;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;

import java.util.List;

public class BookingPrintVo {

    /**
     * uuid预订单
     */
    private String uuid;
    /**
     * 预订单
     */
    private BookingVo bookingVo;

    /**
     * 会员信息
     */
    private BookingStatisticsResp bookingRecorde;

    /**
     * 客户信息
     */
    private BookingCustomer customer;

    private String periodName;

    /**
     * 内部预订代订人
     */
    public String getInnerOrderPersonName() {
        List<User> authUserList = Session.getFunc(UserFunc.class).getUsers();
        if (authUserList != null && bookingVo != null && bookingVo.getBooking() != null) {
            for (User user : authUserList) {
                if (user.getId() != null && user.getId().equals(bookingVo.getBooking().getInnerOrderPerson())) {
                    return user.getName();
                }
            }
        }
        return null;
    }

    public String getLoginName() {
        AuthUser user = Session.getAuthUser();
        return user != null ? user.getName() : null;
    }

    public BookingVo getBookingVo() {
        return bookingVo;
    }

    public void setBookingVo(BookingVo bookingVo) {
        this.bookingVo = bookingVo;
    }

    public BookingCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(BookingCustomer customer) {
        this.customer = customer;
    }

    public BookingStatisticsResp getBookingRecorde() {
        return bookingRecorde;
    }

    public void setBookingRecorde(BookingStatisticsResp bookingRecorde) {
        this.bookingRecorde = bookingRecorde;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
