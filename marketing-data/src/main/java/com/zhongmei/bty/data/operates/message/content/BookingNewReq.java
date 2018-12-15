package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.basemodule.booking.bean.BookingGroupInfo;
import com.zhongmei.bty.basemodule.booking.entity.BookingPeriod;
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;
import com.zhongmei.bty.basemodule.booking.entity.BookingTradeItemProperty;
import com.zhongmei.yunfu.db.enums.BookingType;

import java.math.BigDecimal;
import java.util.List;

/**
 * 封装预订创建或修改请求
 */
public class BookingNewReq extends BaseRequest {

    /**
     * 创建和更新的共有部分
     */

    private String carDetailed;//	客户车牌号

    private String consumeStandard;//消费标准

    private Long creatorID;//经手人

    private String commercialGroup;//客户分组

    private String commercialName;//客户名称

    private String commercialMobile;//客户电话

    private String envFavorite;//环境喜好

    private String innerOrderPerson;//代订人

    private Integer isImportant;//是否重点预订 0——普通 1——重点

    private Long clientCreateTime;//本地创建时间毫秒

    private Long clientUpdateTime;//本地修改时间

    private String memo;//备注

    private Integer orderNumber;//预订人数

    private Integer orderSource;//订单来源

    private Integer orderStatus;//订单状态

    private String orderDesc;//预定描述

    private Long orderTime;//预订时间

    private Long periodID;//时段id

    private String periodUuid;//时段uuid

    private Long userid;

    public String nation;    //国家英文名称(为空默认中国)	否	String
    public String country;    //国家中文名称(为空默认中国)	否	String
    public String nationalTelCode;    //电话国际区码(为空默认中国)	否	String

    private List<BookingTableGroupReq> bookingTables;

    private List<BookingTradeItem> bookingTradeItems;

    private List<BookingTradeItemProperty> bookingTradeItemPropertys;

    private BookingGroupInfo bookingGroupInfo;

    /**
     * 必填
     */
    private Integer commercialGender;//性别

    private String uuid;//booking——uuid

    private Integer notifyCount;

    private Integer boxFirst;

    private BookingPeriod bookingPeriod;

    private Integer bookingType;//0 默认 1 正餐 2 团餐

    /**--------------------------------------------------------------------------------------------*/
    /**
     * updateBooking
     */
    private Long cancelOrderTime;//取消预订操作时间

    private String cancelOrderUser;//取消操作员

    private int deleted;//是否删除

    private BigDecimal realConsume;//实际消费金额

    private Long realConsumeTime;//实际消费金额修改时间

    private String realConsumeUser;//实际消费操作员

    private String refusalReason;//拒绝理由

    private Long shopArriveTime;//到店时间

    private String shopArriveUser;//到店操作员

    private Long shopLeaveTime;//离店时间

    private String shopLeaveUser;//离店操作员

    private Long bookingId;

    private Long modifyDateTime;

    /*-------------------------------------------------------------------------------------------------*/

    public BookingType getBookingType() {
        return ValueEnums.toEnum(BookingType.class, bookingType);
    }

    public void setBookingType(BookingType bookingType) {
        this.bookingType = ValueEnums.toValue(bookingType);
    }

    public void setCarDetailed(String carDetailed) {
        this.carDetailed = carDetailed;
    }

    public void setConsumeStandard(String consumeStandard) {
        this.consumeStandard = consumeStandard;
    }

    public void setCreatorID(Long creatorID) {
        this.creatorID = creatorID;
    }

    public void setCommercialGroup(String customerGroup) {
        this.commercialGroup = customerGroup;
    }

    public void setCommercialName(String customerName) {
        this.commercialName = customerName;
    }

    public void setCommercialMobile(String customerMobile) {
        this.commercialMobile = customerMobile;
    }

    public void setEnvFavorite(String envFavorite) {
        this.envFavorite = envFavorite;
    }

    public void setInnerOrderPerson(String innerOrderPerson) {
        this.innerOrderPerson = innerOrderPerson;
    }

    public void setIsImportant(Integer isImportant) {
        this.isImportant = isImportant;
    }

    public void setClientCreateTime(Long clientCreateTime) {
        this.clientCreateTime = clientCreateTime;
    }

    public void setClientUpdateTime(Long clientUpdateTime) {
        this.clientUpdateTime = clientUpdateTime;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setOrderSource(Integer orderSource) {
        this.orderSource = orderSource;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public void setOrderTime(Long orderTime) {
        this.orderTime = orderTime;
    }

    public void setPeriodUuid(String periodUuid) {
        this.periodUuid = periodUuid;
    }

    public List<BookingTableGroupReq> getBookingTables() {
        return bookingTables;
    }

    public void setBookingTables(List<BookingTableGroupReq> bookingTables) {
        this.bookingTables = bookingTables;
    }

    public List<BookingTradeItem> getBookingTradeItems() {
        return bookingTradeItems;
    }

    public void setBookingTradeItems(List<BookingTradeItem> bookingTradeItems) {
        this.bookingTradeItems = bookingTradeItems;
    }

    public List<BookingTradeItemProperty> getBookingTradeItemPropertys() {
        return bookingTradeItemPropertys;
    }

    public void setBookingTradeItemPropertys(List<BookingTradeItemProperty> bookingTradeItemPropertys) {
        this.bookingTradeItemPropertys = bookingTradeItemPropertys;
    }

    public BookingGroupInfo getBookingGroupInfo() {
        return bookingGroupInfo;
    }

    public void setBookingGroupInfo(BookingGroupInfo bookingGroupInfo) {
        this.bookingGroupInfo = bookingGroupInfo;
    }

    public void setCommercialGender(Integer sex) {
        this.commercialGender = sex;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setNotifyCount(Integer notifyCount) {
        this.notifyCount = notifyCount;
    }

    public void setBoxFirst(Integer boxFirst) {
        this.boxFirst = boxFirst;
    }

    public void setCancelOrderTime(Long cancelOrderTime) {
        this.cancelOrderTime = cancelOrderTime;
    }

    public void setCancelOrderUser(String cancelOrderUser) {
        this.cancelOrderUser = cancelOrderUser;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public void setRealConsume(BigDecimal realConsume) {
        this.realConsume = realConsume;
    }

    public void setRealConsumeTime(Long realConsumeTime) {
        this.realConsumeTime = realConsumeTime;
    }

    public void setRealConsumeUser(String realConsumeUser) {
        this.realConsumeUser = realConsumeUser;
    }

    public void setRr(String refusalReason) {
        this.refusalReason = refusalReason;
    }

    public void setShopArriveTime(Long shopArriveTime) {
        this.shopArriveTime = shopArriveTime;
    }

    public void setShopArriveUser(String shopArriveUser) {
        this.shopArriveUser = shopArriveUser;
    }

    public void setShopLeaveTime(Long shopLeaveTime) {
        this.shopLeaveTime = shopLeaveTime;
    }

    public void setShopLeaveUser(String shopLeaveUser) {
        this.shopLeaveUser = shopLeaveUser;
    }

    public void setBookingPeriod(BookingPeriod bookingPeriod) {
        this.bookingPeriod = bookingPeriod;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public void setModifyDateTime(Long modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    public void setPeriodID(Long periodID) {
        this.periodID = periodID;
    }

    public void setBookingType(Integer bookingType) {
        this.bookingType = bookingType;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNationalTelCode() {
        return nationalTelCode;
    }

    public void setNationalTelCode(String nationalTelCode) {
        this.nationalTelCode = nationalTelCode;
    }

    public void setUserId(Long userid) {
        this.userid = userid;
    }
}
