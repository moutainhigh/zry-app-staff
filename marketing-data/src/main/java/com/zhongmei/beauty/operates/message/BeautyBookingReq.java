package com.zhongmei.beauty.operates.message;


import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;
import com.zhongmei.bty.basemodule.booking.entity.BookingTradeItemProperty;
import com.zhongmei.beauty.entity.BookingTradeItemUser;
import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

import java.util.List;

/**
 * 创建预定数据
 *
 * @date 2018/7/23
 */
public class BeautyBookingReq extends BaseRequest {

    public Long clientUpdateTime;

    public Long clientCreateTime;

    public String uuid;

    //预定类型 1正餐 2团餐 3美业
    public Integer bookingType;

    public List<BookingTradeItem> bookingTradeItems;

    public List<BookingTradeItemProperty> bookingTradeItemProperties;

    public List<BookingTradeItemUser> bookingTradeItemUsers;

    public Integer confirmed;//是否接受 1，未确认，2已确认

    public String deviceIdenty;//设备id

    /**
     * 顾客ID
     */
    public Long commercialId;

    public String commercialName;

    public String commercialGroup;

    public String commercialPhone;

    public Integer commercialGender;

    /**
     * 预定时间，与时段的startTime一致
     */
    public Long orderTime;

    /**
     * 预定人数
     */
    public Integer customerNum;

    /**
     * 订单来源
     *
     * @see com.zhongmei.bty.commonmodule.database.enums.BookingOrderSource 22
     */
    public Integer orderSource;


    public Integer bookingSource; //订单来源 1pos，2微信小程序
    /**
     * 预定单状态
     *
     * @see com.zhongmei.bty.commonmodule.database.enums.BookingOrderStatus -1
     */
    public Integer orderStatus;

    /**
     * 备注
     */
    public String remark;

    public Long creatorId;

    public String creatorName;

    public Long updatorId;

    public String updatorName;

    public Long startTime;

    public Long endTime;

    /**
     * Booking对象转BeautyBookingReq对象
     *
     * @see Booking
     */
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
        this.customerNum = booking.getCustomerNum(); // 默认1人
        this.uuid = booking.getUuid();
        this.clientCreateTime = booking.getServerCreateTime();
        this.bookingType = booking.getBookingType().value();
        this.creatorId = Long.valueOf(booking.getCreatorId());
        this.creatorName = booking.getCreatorName();
        this.updatorId = booking.getUpdatorId();
        this.updatorName = booking.getUpdatorName();
        this.bookingSource = booking.getBookingSource().value();
    }

}
