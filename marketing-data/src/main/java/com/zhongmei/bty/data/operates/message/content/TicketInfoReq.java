package com.zhongmei.bty.data.operates.message.content;


public class TicketInfoReq {

    private String serialNumber;

    public TicketInfoReq(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
