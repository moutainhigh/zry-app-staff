package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;

import java.util.List;

public class SenderOrderItem {
    private TradeVo traderVo;


    private String deliveryStatus;


    private String customerName;


    private String customerPhone;


    private String paymentMethond;


    private List<PaymentVo> paymentList;


    private String cleanStatus;


    private boolean isCanClean;


    private boolean isSelected;

    public TradeVo getTraderVo() {
        return traderVo;
    }

    public void setTraderVo(TradeVo traderVo) {
        this.traderVo = traderVo;
    }

    public String getPaymentMethond() {
        return paymentMethond;
    }

    public void setPaymentMethond(String paymentMethond) {
        this.paymentMethond = paymentMethond;
    }

    public String getCleanStatus() {
        return cleanStatus;
    }

    public void setCleanStatus(String cleanStatus) {
        this.cleanStatus = cleanStatus;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isCanClean() {
        return isCanClean;
    }

    public void setCanClean(boolean isCanClean) {
        this.isCanClean = isCanClean;
    }

    public List<PaymentVo> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<PaymentVo> paymentList) {
        this.paymentList = paymentList;
    }

}
