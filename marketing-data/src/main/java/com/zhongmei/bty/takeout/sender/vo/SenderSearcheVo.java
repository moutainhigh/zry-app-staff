package com.zhongmei.bty.takeout.sender.vo;

public class SenderSearcheVo {
    private String tradeUuid;

    /**
     * 外卖员id
     */
    private String senderId;

    /**
     * 外卖员名称
     */
    private String senderName;

    /**
     * 下单时间
     */
    private String orderCreateTime;

    /**
     * 单号
     */
    private String trandeNo;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(String orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    public String getTrandeNo() {
        return trandeNo;
    }

    public void setTrandeNo(String trandeNo) {
        this.trandeNo = trandeNo;
    }

}
