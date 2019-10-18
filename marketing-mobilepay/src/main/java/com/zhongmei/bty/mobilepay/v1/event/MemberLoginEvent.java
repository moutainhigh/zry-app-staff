package com.zhongmei.bty.mobilepay.v1.event;



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
