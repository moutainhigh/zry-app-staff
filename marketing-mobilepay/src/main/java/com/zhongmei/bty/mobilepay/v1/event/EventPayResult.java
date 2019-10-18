package com.zhongmei.bty.mobilepay.v1.event;

import com.zhongmei.bty.basemodule.pay.bean.ElectronicInvoiceVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.enums.BusinessType;

public class EventPayResult {

    private ElectronicInvoiceVo electronicInvoiceVo;
    private TradeVo tradeVo;
    private boolean isPrintInvoice;
    private double paidAmount;
    private BusinessType mType;
    private boolean success;

    public boolean isOnlinePay() {
        return isOnlinePay;
    }

    public void setOnlinePay(boolean onlinePay) {
        isOnlinePay = onlinePay;
    }

    private boolean isOnlinePay;

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    private Object content;
    public EventPayResult(boolean success) {
        this.success = success;
    }

    public EventPayResult(boolean success, BusinessType type) {
        this.success = success;
        this.mType = type;
    }

    public boolean isPrintInvoice() {
        return isPrintInvoice;
    }

    public void setPrintInvoice(boolean printInvoice) {
        isPrintInvoice = printInvoice;
    }

    public boolean getPayResult() {

        return success;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BusinessType getType() {
        return mType;
    }

    public void setType(BusinessType type) {
        this.mType = type;
    }

    public ElectronicInvoiceVo getElectronicInvoiceVo() {
        return electronicInvoiceVo;
    }

    public void setElectronicInvoiceVo(ElectronicInvoiceVo electronicInvoiceVo) {
        this.electronicInvoiceVo = electronicInvoiceVo;
    }

    public TradeVo getTradeVo() {
        return tradeVo;
    }

    public void setTradeVo(TradeVo tradeVo) {
        this.tradeVo = tradeVo;
    }
}