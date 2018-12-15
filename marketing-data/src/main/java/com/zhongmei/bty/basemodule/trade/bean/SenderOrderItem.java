package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;

import java.util.List;

public class SenderOrderItem {
    private TradeVo traderVo;

    /**
     * 送餐状态
     */
    private String deliveryStatus;

    /**
     * 客户名字
     */
    private String customerName;

    /**
     * 客户电话
     */
    private String customerPhone;

    /**
     * 付款方式
     */
    private String paymentMethond;

    /**
     * 付款集合
     */
    private List<PaymentVo> paymentList;

    /**
     * 清账状态
     */
    private String cleanStatus;

    /**
     * 是否能清账
     */
    private boolean isCanClean;

    /**
     * 是否选中
     */
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
