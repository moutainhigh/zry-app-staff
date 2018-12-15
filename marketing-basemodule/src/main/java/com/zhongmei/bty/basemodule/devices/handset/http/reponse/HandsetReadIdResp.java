package com.zhongmei.bty.basemodule.devices.handset.http.reponse;

import android.text.TextUtils;

public class HandsetReadIdResp extends HandsetBaseResp {

    public String band_sn;

    public String band_read_info;

    public boolean isOk() {
        if (TextUtils.isEmpty(band_sn)) {
            return false;
        }
        return true;
    }
}
