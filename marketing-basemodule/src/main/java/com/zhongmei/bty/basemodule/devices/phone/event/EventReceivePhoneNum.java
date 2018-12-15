package com.zhongmei.bty.basemodule.devices.phone.event;

/**
 * 收到来电号码
 *
 * @date 2014-10-20
 */
public class EventReceivePhoneNum {
    private String phoneNum;

    public EventReceivePhoneNum(String phoneNum) {
        super();
        this.phoneNum = phoneNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }


}
