package com.zhongmei.bty.basemodule.devices.phone.bean;

import com.zhongmei.bty.basemodule.devices.phone.event.ActionAutoConnect;
import com.zhongmei.bty.basemodule.devices.phone.event.ActionConnect;
import com.zhongmei.bty.basemodule.devices.phone.event.ActionDial;
import com.zhongmei.bty.basemodule.devices.phone.event.ActionDisconnect;
import com.zhongmei.bty.basemodule.devices.phone.event.ActionHangup;
import com.zhongmei.bty.basemodule.devices.phone.event.ActionSearchDevice;
import com.zhongmei.bty.basemodule.devices.phone.exception.CalmPhoneException;
import com.zhongmei.bty.basemodule.devices.phone.exception.CalmPhoneStateException;
import com.zhongmei.bty.basemodule.devices.phone.manager.CalmPhoneManager.PhoneType;

import de.greenrobot.event.EventBus;

public class CalmPhoneNmBlueTooth implements ICalmPhone {
    public static final String BLUETOOTH_PREFIX_FILTER = "NM_BTHP";

    @Override
    public void autoConnect() {
        EventBus.getDefault().post(new ActionAutoConnect());
    }

    @Override
    public void connect(CalmPhoneInfo calmPhone) {
        EventBus.getDefault().post(new ActionConnect(calmPhone));
    }

    @Override
    public void dial(String phoneNumber) throws CalmPhoneStateException,
            CalmPhoneException, IllegalArgumentException {
        EventBus.getDefault().post(new ActionDial(phoneNumber));
    }

    @Override
    public void hangup() {
        EventBus.getDefault().post(new ActionHangup());
    }

    @Override
    public void disconnect() {
        EventBus.getDefault().post(new ActionDisconnect());
    }

    @Override
    public void search(PhoneType type) {
        EventBus.getDefault().post(new ActionSearchDevice(true));
    }

}
