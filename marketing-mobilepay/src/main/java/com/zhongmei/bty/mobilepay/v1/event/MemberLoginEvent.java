package com.zhongmei.bty.mobilepay.v1.event;

/**
 * Created by demo on 2018/12/15
 */

public class MemberLoginEvent {
    private boolean isshowLogin;

    public MemberLoginEvent(boolean misshowLogin) {
        isshowLogin = misshowLogin;
    }

    public boolean isshowLogin() {
        return isshowLogin;
    }

    public void setIsshowLogin(boolean isshowLogin) {
        this.isshowLogin = isshowLogin;
    }
}
