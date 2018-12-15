package com.zhongmei.bty.basemodule.devices.phone.event;

import com.zhongmei.bty.basemodule.devices.phone.manager.CalmPhoneManager.PhoneType;

/**
 * 拨号
 *
 * @date 2014-10-20
 */
public class EventDial {

    private String phoneNumber;

    private PhoneType mPhoneType;

    public EventDial(String phoneNumber, PhoneType type) {
        super();
        this.phoneNumber = phoneNumber;
        this.mPhoneType = type;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public PhoneType getPhoneType() {

        return mPhoneType;

    }


}
