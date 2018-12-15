package com.zhongmei.bty.basemodule.devices.phone.event;

import com.zhongmei.bty.basemodule.devices.phone.manager.CalmPhoneManager.PhoneType;
import com.zhongmei.bty.basemodule.devices.phone.bean.ICalmPhone;
import com.zhongmei.bty.basemodule.devices.phone.bean.CalmPhoneInfo;


/**
 * {@link ICalmPhone#connect(com.zhongmei.bty.platform.phone.type.CalmPhoneInfo)} 执行后的回调结果<br />
 * 反馈此次绑定的结果<br />
 * 但是在其他时候, 如 断线自动重连, 连接丢失, 也会触发此事件, 故请根据实际情况处理
 *
 * @date 2014-8-7
 */
public class EventConnectResult {

    private boolean success;
    private CalmPhoneInfo phone;
    private PhoneType type;

    public EventConnectResult(PhoneType type, boolean success) {
        super();
        this.success = success;
        this.type = type;
    }

    public EventConnectResult(PhoneType type, boolean success, CalmPhoneInfo phone) {
        super();
        this.type = type;
        this.success = success;
        this.phone = phone;
    }

    public boolean isSuccess() {
        return success;
    }


    public PhoneType getType() {
        return type;
    }

    public CalmPhoneInfo getPhone() {
        return phone;
    }

}
