package com.zhongmei.beauty.operates.message;


import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;
import com.zhongmei.bty.basemodule.booking.entity.BookingTradeItemProperty;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.beauty.entity.BookingTradeItemUser;
import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

import java.util.List;

/**
 * 编辑预定数据
 *
 * @date 2018/7/23
 */
public class BeautyBookingUpdateReq extends BaseRequest {


    public Long id;
    public String uuid;
    //
//    //预定类型 1正餐 2团餐 3美业
    public Integer bookingType;

    public List<BookingTradeItem> bookingTradeItems;

    public List<BookingTradeItemProperty> bookingTradeItemProperties;

    public List<BookingTradeItemUser> bookingTradeItemUsers;

    public String deviceIdenty;

    /**
     * 预定人数
     */
    public Integer customerNum;

    public Long clientUpdateTime;

    public Long clientCreateTime;

    public Integer confirmed;


    public Integer bookingSource; //订单来源 1pos，2微信小程序

    /**
     * 国家英文名称(为空默认中国) = countryEN
     */
    public String nation;
    /**
     * 国家中文名称(为空默认中国) = countryZH
     */
    public String country;
    /**
     * 电话国际区码(为空默认中国) = AreaCode
     */
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

    /**
     * 预定时间，与时段的startTime一致
     */
    public Long orderTime;


//    /**
//     * 预定人数
//     */
//    public Integer orderNumber;

//    /**
//     * 订单来源
//     * @see com.zhongmei.bty.commonmodule.database.enums.BookingOrderSource 22
//     */
//    public Integer orderSource;
    /**
     * 预定单状态
     *
     * @see com.zhongmei.bty.commonmodule.database.enums.BookingOrderStatus -1
     */
    public Integer orderStatus;
    public Integer sort;

    /**
     * 备注
     */
    public String orderDesc;

    public Long creatorId;
    public String creatorName;

    public Long updatorId;
    public String updatorName;

    public Integer commercialGender;

    public Integer orderSource;

    /**
     * Booking对象转BeautyBookingUpdateReq对象
     *
     * @see Booking
     */
    public void convertBookingToBookingUpdateReq(Booking booking, AuthUser user, String remark) {

        this.endTime = booking.getEndTime();
        this.startTime = booking.getStartTime();
        this.deviceIdenty = BaseApplication.getInstance().getDeviceIdenty();
        this.orderStatus = booking.getOrderStatus().value();
        this.customerNum = booking.getCustomerNum(); // 默认1人
        this.clientUpdateTime = System.currentTimeMillis();
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
