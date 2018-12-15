package com.zhongmei.bty.data.operates.message.content;

/**
 * 点评查询单张团购券请求体
 * Created by demo on 2018/12/15
 */
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
