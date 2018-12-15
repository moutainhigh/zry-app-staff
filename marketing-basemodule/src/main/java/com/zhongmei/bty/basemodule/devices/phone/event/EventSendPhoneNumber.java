package com.zhongmei.bty.basemodule.devices.phone.event;


/**
 * 点击发送电话号码
 *
 * @date 2014-10-27
 */
public class EventSendPhoneNumber {

    private String phoneNumber;


    public EventSendPhoneNumber(String phoneNumber) {
        super();
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
