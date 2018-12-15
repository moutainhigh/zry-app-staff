package com.zhongmei.bty.basemodule.booking.message;

import java.math.BigDecimal;

/**
 * 会员统计信息
 */
public class BookingStatisticsResp {

    /**
     * 消费总金额
     */

    private BigDecimal realConsumeCount;

    /**
     * 通话次数
     */

    private Integer phoneCount;

    /**
     * 人均消费
     */

    private BigDecimal consumPerCount;

    /**
     * 排队次数
     */

    private Integer queueCount;

    /**
     * 排队入场数
     */

    private Integer queueEnterCount;

    /**
     * 外卖次数
     */

    private Integer takeawayCount;

    /**
     * 预订次数
     */
    private Integer bookingCount;

    /**
     * 取消预订次数
     */
    private Integer cancelCount;

    /**
     * 逾期未到店次数
     */
    private Integer overdueCount;

    /**
     * 到店次数
     */
    private Integer arriveCount;

    /**
     * 会员余额
     */
    private BigDecimal memberBalance;

    /**
     * 可用积分
     */
    private BigDecimal memberIntegral;

    /**
     * 可用优惠券
     */
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
