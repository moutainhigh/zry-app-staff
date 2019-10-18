package com.zhongmei.yunfu.context.session.core.user;

import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;



public class AuthUser extends User implements IAuthUser {

    private Auth auth;

    public AuthUser() {

    }

    public AuthUser(User user, Auth auth) {
        super(user);
        this.auth = auth;
    }

    public Auth getAuth() {
        return auth;
    }
}
