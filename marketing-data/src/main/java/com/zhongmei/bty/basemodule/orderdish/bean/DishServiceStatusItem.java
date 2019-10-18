package com.zhongmei.bty.basemodule.orderdish.bean;


public class DishServiceStatusItem {
    private Long id;
    private byte servingStatus;
    private Long serverUpdateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte getServingStatus() {
        return servingStatus;
    }

    public void setServingStatus(byte servingStatus) {
        this.servingStatus = servingStatus;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

}
