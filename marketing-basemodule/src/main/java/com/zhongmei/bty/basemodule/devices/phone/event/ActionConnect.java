package com.zhongmei.bty.basemodule.devices.phone.event;

import com.zhongmei.bty.basemodule.devices.phone.bean.CalmPhoneInfo;

/**
 * 连接到电话设备
 *
 * @date 2014-8-7
 */
public class ActionConnect {
    private CalmPhoneInfo calmPhone;

    public ActionConnect(CalmPhoneInfo calmPhone) {
        super();
        this.calmPhone = calmPhone;
    }

    public CalmPhoneInfo getCalmPhone() {
        return calmPhone;
    }

    public void setCalmPhone(CalmPhoneInfo calmPhone) {
        this.calmPhone = calmPhone;
    }

}
