package com.zhongmei.bty.basemodule.booking.message;

import java.math.BigDecimal;
import java.util.List;

import com.zhongmei.yunfu.db.enums.BookingOrderSource;
import com.zhongmei.yunfu.db.enums.BookingOrderStatus;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.util.ValueEnums;

/**
 * 封装预订创建或修改请求
 */
public class BookingReq {
    /**
     * 是否删除
     */
    private int deleted;

    private String serverId;

    private Long localCreateDateTime;

    private Long localModifyDateTime;

    /**
     * 订单id 可以为空
     */
    private Long orderID;

    private Long commercialId;

    /**
     * 本地客户表示
     */
    private Long customerLocalID;

    /**
     * 客户名称
     */
    private String commercialName;

    /**
     * 客户分组
     */
    private String commercialGroup;

    /**
     * 客户电话
     */
    private String commercialPhone;

    /**
     * 预订时间
     */
    private Long orderTime;

    /**
     * 预订人数
     */
    private int orderNumber;

    /**
     * 订单来源
     */
    private Integer bookingSource;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 到店时间
     */
    private String shopArriveTime;

    /**
     * 离店时间
     */
    private String shopLeaveTime;

    /**
     * 消费标准
     */
    private String consumeStandard;

    /**
     * 代订人
     */
    private String innerOrderPerson;

    /**
     * 经手人
     */
    private String creatorID;

    /**
     * 性别
     */
    private Integer commercialGender;

    /**
     * 备注
     */
    private String memo;

    /**
     * 实际消费金额
     */
    private BigDecimal realConsume;

    /**
     * 包厢优先
     */
    private int boxFirst;

    /**
     * 实际消费操作员
     */
    private String realConsumeUser;

    /**
     * 到店操作员
     */
    private String shopArriveUser;

    /**
     * 离店操作员
     */
    private String shopLeaveUser;

    /**
     * 取消操作员
     */
    private String cancelOrderUser;

    /**
     * 实际消费金额修改时间
     */
    private String realConsumeTime;

    /**
     * 取消预订操作时间
     */
    private String cancelOrderTime;

    /**
     * 客户唯一标识 uuid
     */
    private String customerID;

    /**
     * 时段uuid
     */
    private String periodServerId;

    /**
     * 客户uuid
     */
    private String customerSynflag;

    /**
     * 拒绝理由
     */
    private String rr;

    /**
     * 是否重点预订
     */
    private Integer isImportant;

    /**
     * 环境喜好
     */
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
