package com.zhongmei.bty.splash.login.manager;

//登录用户的管理者，纪录一个登录用户，以及所有权限,此实例只有一个
public class LoginUserManager {

    public static LoginUserManager instance;

    public LoginUserManager getInstance() {
        if (null == instance) {
            instance = new LoginUserManager();
        }
        return instance;
    }

}
