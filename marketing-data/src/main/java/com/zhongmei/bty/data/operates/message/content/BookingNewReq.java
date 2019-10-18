package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.basemodule.booking.bean.BookingGroupInfo;
import com.zhongmei.bty.basemodule.booking.entity.BookingPeriod;
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;
import com.zhongmei.bty.basemodule.booking.entity.BookingTradeItemProperty;
import com.zhongmei.yunfu.db.enums.BookingType;

import java.math.BigDecimal;
import java.util.List;


public class BookingNewReq extends BaseRequest {



    private String carDetailed;
    private String consumeStandard;
    private Long creatorID;
    private String commercialGroup;
    private String commercialName;
    private String commercialMobile;
    private String envFavorite;
    private String innerOrderPerson;
    private Integer isImportant;
    private Long clientCreateTime;
    private Long clientUpdateTime;
    private String memo;
    private Integer orderNumber;
    private Integer orderSource;
    private Integer orderStatus;
    private String orderDesc;
    private Long orderTime;
    private Long periodID;
    private String periodUuid;
    private Long userid;

    public String nation;        public String country;        public String nationalTelCode;
    private List<BookingTableGroupReq> bookingTables;

    private List<BookingTradeItem> bookingTradeItems;

    private List<BookingTradeItemProperty> bookingTradeItemPropertys;

    private BookingGroupInfo bookingGroupInfo;


    private Integer commercialGender;
    private String uuid;
    private Integer notifyCount;

    private Integer boxFirst;

    private BookingPeriod bookingPeriod;

    private Integer bookingType;


    private Long cancelOrderTime;
    private String cancelOrderUser;
    private int deleted;
    private BigDecimal realConsume;
    private Long realConsumeTime;
    private String realConsumeUser;
    private String refusalReason;
    private Long shopArriveTime;
    private String shopArriveUser;
    private Long shopLeaveTime;
    private String shopLeaveUser;
    private Long bookingId;

    private Long modifyDateTime;



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
