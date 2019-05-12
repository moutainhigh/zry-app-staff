package com.zhongmei.beauty.entity;

/**
 * Created by demo on 2018/12/15
 */

public class BeautyNotifyEntity {
    private int customerNumber;//到店人数
    private int reserverNumber;//预约单数
    private int unDealReserverNumber;//未处理的订单数
    private int tradeNumber;//订单数
    private int memberNumber;//新增会员数
    private int todayReserverNumber;//今日预约数
    private int unpaidTradeNumber;//待付款订单

    public int getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }

    public int getReserverNumber() {
        return reserverNumber;
    }

    public void setReserverNumber(int reserverNumber) {
        this.reserverNumber = reserverNumber;
    }

    public int getTradeNumber() {
        return tradeNumber;
    }

    public void setTradeNumber(int tradeNumber) {
        this.tradeNumber = tradeNumber;
    }

    public int getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(int memberNumber) {
        this.memberNumber = memberNumber;
    }

    public int getTodayReserverNumber() {
        return todayReserverNumber;
    }

    public void setTodayReserverNumber(int todayReserverNumber) {
        this.todayReserverNumber = todayReserverNumber;
    }

    public int getUnpaidTradeNumber() {
        return unpaidTradeNumber;
    }

    public void setUnpaidTradeNumber(int unpaidTradeNumber) {
        this.unpaidTradeNumber = unpaidTradeNumber;
    }

    public int getUnDealReserverNumber() {
        return unDealReserverNumber;
    }

    public void setUnDealReserverNumber(int unDealReserverNumber) {
        this.unDealReserverNumber = unDealReserverNumber;
    }
}
