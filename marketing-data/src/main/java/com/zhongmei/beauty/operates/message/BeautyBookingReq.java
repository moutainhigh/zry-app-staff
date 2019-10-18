package com.zhongmei.beauty.operates.message;


import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;
import com.zhongmei.bty.basemodule.booking.entity.BookingTradeItemProperty;
import com.zhongmei.beauty.entity.BookingTradeItemUser;
import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

import java.util.List;


public class BeautyBookingReq extends BaseRequest {

    public Long clientUpdateTime;

    public Long clientCreateTime;

    public String uuid;

        public Integer bookingType;

    public List<BookingTradeItem> bookingTradeItems;

    public List<BookingTradeItemProperty> bookingTradeItemProperties;

    public List<BookingTradeItemUser> bookingTradeItemUsers;

    public Integer confirmed;
    public String deviceIdenty;

    public Long commercialId;

    public String commercialName;

    public String commercialGroup;

    public String commercialPhone;

    public Integer commercialGender;


    public Long orderTime;


    public Integer customerNum;


    public Integer orderSource;


    public Integer bookingSource;
    public Integer orderStatus;


    public String remark;

    public Long creatorId;

    public String creatorName;

    public Long updatorId;

    public String updatorName;

    public Long startTime;

    public Long endTime;


    public void bookingToBookingReq(Booking booking) {
        this.commercialId = booking.getCommercialId();
        this.commercialGender = booking.getCommercialGender().value();
        this.commercialName = booking.getCommercialName();
        this.commercialGroup = booking.getCommercialGroup();
        this.commercialPhone = booking.getCommercialPhone();
        this.commercialId = booking.getCommercialId();
        this.orderTime = booking.getOrderTime();
        this.orderStatus = booking.getOrderStatus().value();
        this.orderSource = booking.getBookingSource().value();
        this.remark = booking.orderDesc;
        this.customerNum = booking.getCustomerNum();         this.uuid = booking.getUuid();
        this.clientCreateTime = booking.getServerCreateTime();
        this.bookingType = booking.getBookingType().value();
        this.creatorId = Long.valueOf(booking.getCreatorId());
        this.creatorName = booking.getCreatorName();
        this.updatorId = booking.getUpdatorId();
        this.updatorName = booking.getUpdatorName();
        this.bookingSource = booking.getBookingSource().value();
    }

}
