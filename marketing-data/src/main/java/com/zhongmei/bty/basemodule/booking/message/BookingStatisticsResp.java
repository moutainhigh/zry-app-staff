package com.zhongmei.bty.basemodule.booking.message;

import java.math.BigDecimal;


public class BookingStatisticsResp {



    private BigDecimal realConsumeCount;



    private Integer phoneCount;



    private BigDecimal consumPerCount;



    private Integer queueCount;



    private Integer queueEnterCount;



    private Integer takeawayCount;


    private Integer bookingCount;


    private Integer cancelCount;


    private Integer overdueCount;


    private Integer arriveCount;


    private BigDecimal memberBalance;


    private BigDecimal memberIntegral;


    private BigDecimal memberCoupons;

    public BigDecimal getRealConsumeCount() {
        return realConsumeCount;
    }

    public void setRealConsumeCount(BigDecimal realConsumeCount) {
        this.realConsumeCount = realConsumeCount;
    }

    public Integer getPhoneCount() {
        return phoneCount;
    }

    public void setPhoneCount(Integer phoneCount) {
        this.phoneCount = phoneCount;
    }

    public BigDecimal getConsumPerCount() {
        return consumPerCount;
    }

    public void setConsumPerCount(BigDecimal consumPerCount) {
        this.consumPerCount = consumPerCount;
    }

    public Integer getQueueCount() {
        return queueCount;
    }

    public void setQueueCount(Integer queueCount) {
        this.queueCount = queueCount;
    }

    public Integer getQueueEnterCount() {
        return queueEnterCount;
    }

    public void setQueueEnterCount(Integer queueEnterCount) {
        this.queueEnterCount = queueEnterCount;
    }

    public Integer getTakeawayCount() {
        return takeawayCount;
    }

    public void setTakeawayCount(Integer takeawayCount) {
        this.takeawayCount = takeawayCount;
    }

    public Integer getBookingCount() {
        return bookingCount;
    }

    public void setBookingCount(Integer bookingCount) {
        this.bookingCount = bookingCount;
    }

    public Integer getCancelCount() {
        return cancelCount;
    }

    public void setCancelCount(Integer cancelCount) {
        this.cancelCount = cancelCount;
    }

    public Integer getOverdueCount() {
        return overdueCount;
    }

    public void setOverdueCount(Integer overdueCount) {
        this.overdueCount = overdueCount;
    }

    public Integer getArriveCount() {
        return arriveCount;
    }

    public void setArriveCount(Integer arriveCount) {
        this.arriveCount = arriveCount;
    }

    public BigDecimal getMemberBalance() {
        return memberBalance;
    }

    public void setMemberBalance(BigDecimal memberBalance) {
        this.memberBalance = memberBalance;
    }

    public BigDecimal getMemberIntegral() {
        return memberIntegral;
    }

    public void setMemberIntegral(BigDecimal memberIntegral) {
        this.memberIntegral = memberIntegral;
    }

    public BigDecimal getMemberCoupons() {
        return memberCoupons;
    }

    public void setMemberCoupons(BigDecimal memberCoupons) {
        this.memberCoupons = memberCoupons;
    }


}
