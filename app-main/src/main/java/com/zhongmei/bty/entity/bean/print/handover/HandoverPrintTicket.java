package com.zhongmei.bty.entity.bean.print.handover;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;



public class HandoverPrintTicket implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uuid;

    private int padNo;

    private long handoverDate;

    private long startTime;

    private long endTime;

    private String handoverUserName;
        private int saleOrderCount;
        private int refundOrderCount;
        private int saleValuecardCount;
        private int refundValuecardCount;


        private int billCount;

    private BigDecimal totalAmount;

    private BigDecimal totalActualAmount;

    private BigDecimal totalDiffAmount;

    private BigDecimal totalCashBoxAmount;

    private BigDecimal billAmount;

    private List<CashHandoverItem> cashHandoverItems;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getPadNo() {
        return padNo;
    }

    public void setPadNo(int padNo) {
        this.padNo = padNo;
    }

    public long getHandoverDate() {
        return handoverDate;
    }

    public void setHandoverDate(long handoverDate) {
        this.handoverDate = handoverDate;
    }

    public BigDecimal getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(BigDecimal billAmount) {
        this.billAmount = billAmount;
    }

    public int getBillCount() {
        return billCount;
    }

    public void setBillCount(int billCount) {
        this.billCount = billCount;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getHandoverUserName() {
        return handoverUserName;
    }

    public void setHandoverUserName(String handoverUserName) {
        this.handoverUserName = handoverUserName;
    }

    public int getSaleOrderCount() {
        return saleOrderCount;
    }

    public void setSaleOrderCount(int saleOrderCount) {
        this.saleOrderCount = saleOrderCount;
    }

    public int getRefundOrderCount() {
        return refundOrderCount;
    }

    public void setRefundOrderCount(int refundOrderCount) {
        this.refundOrderCount = refundOrderCount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalActualAmount() {
        return totalActualAmount;
    }

    public void setTotalActualAmount(BigDecimal totalActualAmount) {
        this.totalActualAmount = totalActualAmount;
    }

    public BigDecimal getTotalDiffAmount() {
        return totalDiffAmount;
    }

    public void setTotalDiffAmount(BigDecimal totalDiffAmount) {
        this.totalDiffAmount = totalDiffAmount;
    }

    public List<CashHandoverItem> getCashHandoverItems() {
        return cashHandoverItems;
    }

    public void setCashHandoverItems(List<CashHandoverItem> cashHandoverItems) {
        this.cashHandoverItems = cashHandoverItems;
    }

    public BigDecimal getTotalCashBoxAmount() {
        return totalCashBoxAmount;
    }

    public void setTotalCashBoxAmount(BigDecimal totalCashBoxAmount) {
        this.totalCashBoxAmount = totalCashBoxAmount;
    }

    public int getSaleValuecardCount() {
        return saleValuecardCount;
    }

    public void setSaleValuecardCount(int saleValuecardCount) {
        this.saleValuecardCount = saleValuecardCount;
    }

    public int getRefundValuecardCount() {
        return refundValuecardCount;
    }

    public void setRefundValuecardCount(int refundValuecardCount) {
        this.refundValuecardCount = refundValuecardCount;
    }

}
