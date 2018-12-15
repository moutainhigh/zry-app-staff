package com.zhongmei.bty.basemodule.trade.bean;

import java.io.Serializable;

import com.zhongmei.yunfu.db.enums.Sex;


public class TakeOutInfo implements Serializable {
    private String receiverName;
    private String receiverTel;
    private String invoiceTitle;
    private String deliveryAddress;
    private long deliveryAddressID;
    private Long expectTime;
    private Sex receiverSex;
    private Long customerID;
    //国家英文名称
    private String nation;
    //国家中文名称
    private String country;
    //电话国际区码
    private String nationalTelCode;

    public Long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverTel() {
        return receiverTel;
    }

    public void setReceiverTel(String receiverTel) {
        this.receiverTel = receiverTel;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddressID(int deliveryAddressID) {
        this.deliveryAddressID = deliveryAddressID;
    }

    public Long getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(Long expectTime) {
        this.expectTime = expectTime;
    }

    public long getDeliveryAddressID() {
        return deliveryAddressID;
    }

    public void setDeliveryAddressID(long deliveryAddressID) {
        this.deliveryAddressID = deliveryAddressID;
    }

    public Sex getReceiverSex() {
        return receiverSex;
    }

    public void setReceiverSex(Sex receiverSex) {
        this.receiverSex = receiverSex;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNationalTelCode() {
        return nationalTelCode;
    }

    public void setNationalTelCode(String nationalTelCode) {
        this.nationalTelCode = nationalTelCode;
    }
}
