package com.zhongmei.bty.entity.bean.print.handover;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;



public class HandoverPrintData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uuid;

    private int padNo;

    private long handoverDate;

    private long startTime;

    private long endTime;

    private String handoverUserName;

    private int saleOrderCount;

    private int refundOrderCount;

    private BigDecimal totalAmount;

    private BigDecimal totalActualAmount;

    private BigDecimal totalDiffAmount;

    private BigDecimal totalCashBoxAmount;

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

}
