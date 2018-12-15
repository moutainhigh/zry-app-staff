package com.zhongmei.bty.basemodule.devices.handset.http.reponse;

import android.text.TextUtils;

public class HandsetPasswordResp extends HandsetBaseResp {

    public String pwd;

    public String band_sn;

    public String pwd_cancel_info;

    public boolean isOk() {
        if (TextUtils.isEmpty(band_sn) || TextUtils.isEmpty(pwd)) {
            return false;
        }
        return true;
    }
}
