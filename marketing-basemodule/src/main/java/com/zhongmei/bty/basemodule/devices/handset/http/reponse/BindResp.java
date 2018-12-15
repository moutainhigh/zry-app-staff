package com.zhongmei.bty.basemodule.devices.handset.http.reponse;

import android.text.TextUtils;

public class BindResp {

    public String msg;

    public String ip;

    public String hip;

    public String auth;

    public boolean isOk(String hostIp) {
        if (!TextUtils.isEmpty(hip) && !TextUtils.isEmpty(ip) && hip.equals(hostIp)) {
            return true;
        }
        return false;
    }
}
