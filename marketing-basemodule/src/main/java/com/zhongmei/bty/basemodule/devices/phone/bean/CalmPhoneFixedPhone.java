package com.zhongmei.bty.basemodule.devices.phone.bean;

import com.zhongmei.bty.basemodule.devices.phone.event.ActionDial;
import com.zhongmei.bty.basemodule.devices.phone.event.ActionHangup;
import com.zhongmei.bty.basemodule.devices.phone.exception.CalmPhoneException;
import com.zhongmei.bty.basemodule.devices.phone.exception.CalmPhoneStateException;
import com.zhongmei.bty.basemodule.devices.phone.manager.CalmPhoneManager.PhoneType;

import de.greenrobot.event.EventBus;

public class CalmPhoneFixedPhone implements ICalmPhone {

    @Override
    public void autoConnect() {
        // TODO Auto-generated method stub

    }

    @Override
    public void connect(CalmPhoneInfo calmPhone) {
        // TODO Auto-generated method stub

    }

    @Override
    public void dial(String phoneNumber) throws CalmPhoneStateException,
            CalmPhoneException, IllegalArgumentException {
        // TODO Auto-generated method stub
        EventBus.getDefault().post(new ActionDial(phoneNumber));
    }

    @Override
    public void hangup() {
        // TODO Auto-generated method stub
        EventBus.getDefault().post(new ActionHangup());
    }

    @Override
    public void disconnect() {
        // TODO Auto-generated method stub

    }

    @Override
    public void search(PhoneType type) {
        // TODO Auto-generated method stub

    }

}
