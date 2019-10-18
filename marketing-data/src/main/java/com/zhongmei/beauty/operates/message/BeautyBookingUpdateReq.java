package com.zhongmei.beauty.operates.message;


import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;
import com.zhongmei.bty.basemodule.booking.entity.BookingTradeItemProperty;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.beauty.entity.BookingTradeItemUser;
import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

import java.util.List;


public class BeautyBookingUpdateReq extends BaseRequest {


    public Long id;
    public String uuid;
        public Integer bookingType;

    public List<BookingTradeItem> bookingTradeItems;

    public List<BookingTradeItemProperty> bookingTradeItemProperties;

    public List<BookingTradeItemUser> bookingTradeItemUsers;

    public String deviceIdenty;


    public Integer customerNum;

    public Long clientUpdateTime;

    public Long clientCreateTime;

    public Integer confirmed;


    public Integer bookingSource;

    public String nation;

    public String country;

    public String nationalTelCode;

    public Long startTime;
    public Long endTime;

    private Long serverCreateTime;

    private Long serverUpdateTime;

    private Integer statusFlag;

    public String commercialName;

    private String commercialGroup;

    public String commercialPhone;

    private Long commercialId;


    public Long orderTime;




    public Integer orderStatus;
    public Integer sort;


    public String orderDesc;

    public String remark;

    public Long creatorId;
    public String creatorName;

    public Long updatorId;
    public String updatorName;

    public Integer commercialGender;

    public Integer orderSource;


    public void convertBookingToBookingUpdateReq(Booking booking, AuthUser user, String remark) {

        this.endTime = booking.getEndTime();
        this.startTime = booking.getStartTime();
        this.deviceIdenty = BaseApplication.getInstance().getDeviceIdenty();
        this.orderStatus = booking.getOrderStatus().value();
        this.customerNum = booking.getCustomerNum();         this.clientUpdateTime = System.currentTimeMillis();
        this.orderTime = booking.getOrderTime();
        this.confirmed = 2;
        this.commercialName = booking.getCommercialName();
        this.bookingSource = booking.getBookingSource().value();
        this.commercialGender = booking.getCommercialGender().value();
        this.commercialGroup = booking.getCommercialGroup();
        this.commercialPhone = booking.getCommercialPhone();
        this.creatorId = Long.valueOf(booking.getCreatorId());
        this.creatorName = booking.getCreatorName();
        this.updatorId = user.getId();
        this.updatorName = user.getName();
        this.clientCreateTime = booking.getClientCreateTime();
        this.bookingType = booking.getBookingType().value();
        this.orderDesc = remark;
        this.remark=remark;
        this.commercialId = booking.getCommercialId();
        this.uuid = booking.getUuid();
        this.orderSource = 1;


        this.id = booking.getId();
        this.customerNum = booking.getCustomerNum();
        this.serverCreateTime = booking.getServerCreateTime();
        this.serverUpdateTime = booking.getServerUpdateTime();
        this.statusFlag = booking.getStatusFlag().value();
        this.sort = 1;

    }

}
