package com.zhongmei.bty.basemodule.devices.phone.event;


public class EventOpenCallDailog {
    /*需要拨打的电话号码*/
    private String phone;

    public EventOpenCallDailog(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
