package com.zhongmei.bty.splash.login;

public class ChangePasswordSuccessEvent {
    public boolean success;
    public String saltPassword = "";

    public ChangePasswordSuccessEvent(boolean success) {
        this.success = success;
    }

    public ChangePasswordSuccessEvent(boolean success, String saltPassword) {
        this.success = success;
        this.saltPassword = saltPassword;
    }
}
