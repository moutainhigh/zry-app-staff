package com.zhongmei.beauty.entity;



public class BeautyNotifyEntity {
    private int customerNumber;    private int reserverNumber;    private int unDealReserverNumber;    private int tradeNumber;    private int memberNumber;    private int todayReserverNumber;    private int unpaidTradeNumber;    private int taskNumber;

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

    public int getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }
}
