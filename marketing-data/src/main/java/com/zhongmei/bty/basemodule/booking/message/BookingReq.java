package com.zhongmei.bty.basemodule.booking.message;

import java.math.BigDecimal;
import java.util.List;

import com.zhongmei.yunfu.db.enums.BookingOrderSource;
import com.zhongmei.yunfu.db.enums.BookingOrderStatus;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.util.ValueEnums;


public class BookingReq {

    private int deleted;

    private String serverId;

    private Long localCreateDateTime;

    private Long localModifyDateTime;


    private Long orderID;

    private Long commercialId;


    private Long customerLocalID;


    private String commercialName;


    private String commercialGroup;


    private String commercialPhone;


    private Long orderTime;


    private int orderNumber;


    private Integer bookingSource;


    private Integer orderStatus;


    private String shopArriveTime;


    private String shopLeaveTime;


    private String consumeStandard;


    private String innerOrderPerson;


    private String creatorID;


    private Integer commercialGender;


    private String memo;


    private BigDecimal realConsume;


    private int boxFirst;


    private String realConsumeUser;


    private String shopArriveUser;


    private String shopLeaveUser;


    private String cancelOrderUser;


    private String realConsumeTime;


    private String cancelOrderTime;


    private String customerID;


    private String periodServerId;


    private String customerSynflag;


    private String rr;


    private Integer isImportant;


    private String envFavorite;

    private List<BookingTableReq> bookingTableList;

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Long getLocalCreateDateTime() {
        return localCreateDateTime;
    }

    public void setLocalCreateDateTime(Long localCreateDateTime) {
        this.localCreateDateTime = localCreateDateTime;
    }

    public Long getLocalModifyDateTime() {
        return localModifyDateTime;
    }

    public void setLocalModifyDateTime(Long localModifyDateTime) {
        this.localModifyDateTime = localModifyDateTime;
    }

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public Long getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(Long commercialId) {
        this.commercialId = commercialId;
    }

    public Long getCustomerLocalID() {
        return customerLocalID;
    }

    public void setCustomerLocalID(Long customerLocalID) {
        this.customerLocalID = customerLocalID;
    }

    public String getCommercialName() {
        return commercialName;
    }

    public void setCommercialName(String customerName) {
        this.commercialName = customerName;
    }

    public String getCommercialGroup() {
        return commercialGroup;
    }

    public void setCommercialGroup(String customerGroup) {
        this.commercialGroup = customerGroup;
    }

    public String getCommercialPhone() {
        return commercialPhone;
    }

    public void setCommercialPhone(String customerPhone) {
        this.commercialPhone = customerPhone;
    }

    public Long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Long orderTime) {
        this.orderTime = orderTime;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BookingOrderSource getBookingSource() {
        return ValueEnums.toEnum(BookingOrderSource.class, bookingSource);
    }

    public void setBookingSource(BookingOrderSource orderSource) {
        this.bookingSource = ValueEnums.toValue(orderSource);
    }

    public BookingOrderStatus getOrderStatus() {
        return ValueEnums.toEnum(BookingOrderStatus.class, orderStatus);
    }

    public void setOrderStatus(BookingOrderStatus orderStatus) {
        this.orderStatus = ValueEnums.toValue(orderStatus);
    }

    public String getShopArriveTime() {
        return shopArriveTime;
    }

    public void setShopArriveTime(String shopArriveTime) {
        this.shopArriveTime = shopArriveTime;
    }

    public String getShopLeaveTime() {
        return shopLeaveTime;
    }

    public void setShopLeaveTime(String shopLeaveTime) {
        this.shopLeaveTime = shopLeaveTime;
    }

    public String getConsumeStandard() {
        return consumeStandard;
    }

    public void setConsumeStandard(String consumeStandard) {
        this.consumeStandard = consumeStandard;
    }

    public String getInnerOrderPerson() {
        return innerOrderPerson;
    }

    public void setInnerOrderPerson(String innerOrderPerson) {
        this.innerOrderPerson = innerOrderPerson;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public Sex getCommercialGender() {
        return ValueEnums.toEnum(Sex.class, commercialGender);
    }

    public void setCommercialGender(Sex sex) {
        this.commercialGender = ValueEnums.toValue(sex);
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public BigDecimal getRealConsume() {
        return realConsume;
    }

    public void setRealConsume(BigDecimal realConsume) {
        this.realConsume = realConsume;
    }

    public int getBoxFirst() {
        return boxFirst;
    }

    public void setBoxFirst(int boxFirst) {
        this.boxFirst = boxFirst;
    }

    public String getRealConsumeUser() {
        return realConsumeUser;
    }

    public void setRealConsumeUser(String realConsumeUser) {
        this.realConsumeUser = realConsumeUser;
    }

    public String getShopArriveUser() {
        return shopArriveUser;
    }

    public void setShopArriveUser(String shopArriveUser) {
        this.shopArriveUser = shopArriveUser;
    }

    public String getShopLeaveUser() {
        return shopLeaveUser;
    }

    public void setShopLeaveUser(String shopLeaveUser) {
        this.shopLeaveUser = shopLeaveUser;
    }

    public String getCancelOrderUser() {
        return cancelOrderUser;
    }

    public void setCancelOrderUser(String cancelOrderUser) {
        this.cancelOrderUser = cancelOrderUser;
    }

    public String getRealConsumeTime() {
        return realConsumeTime;
    }

    public void setRealConsumeTime(String realConsumeTime) {
        this.realConsumeTime = realConsumeTime;
    }

    public String getCancelOrderTime() {
        return cancelOrderTime;
    }

    public void setCancelOrderTime(String cancelOrderTime) {
        this.cancelOrderTime = cancelOrderTime;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getPeriodServerId() {
        return periodServerId;
    }

    public void setPeriodServerId(String periodServerId) {
        this.periodServerId = periodServerId;
    }

    public String getCustomerSynflag() {
        return customerSynflag;
    }

    public void setCustomerSynflag(String customerSynflag) {
        this.customerSynflag = customerSynflag;
    }

    public String getRr() {
        return rr;
    }

    public void setRr(String rr) {
        this.rr = rr;
    }

    public Integer getIsImportant() {
        return isImportant;
    }

    public void setIsImportant(Integer isImportant) {
        this.isImportant = isImportant;
    }

    public String getEnvFavorite() {
        return envFavorite;
    }

    public void setEnvFavorite(String envFavorite) {
        this.envFavorite = envFavorite;
    }


    public List<BookingTableReq> getBookingTableList() {
        return bookingTableList;
    }

    public void setBookingTableList(List<BookingTableReq> bookingTableList) {
        this.bookingTableList = bookingTableList;
    }

}
