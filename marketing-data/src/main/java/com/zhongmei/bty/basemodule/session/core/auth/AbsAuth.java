package com.zhongmei.bty.basemodule.session.core.auth;

import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.base.BaseApplication;

/**
 * Created by demo on 2018/12/15
 */

public abstract class AbsAuth extends Auth {

    protected Long getShopId() {
        return BaseApplication.sInstance.getShopIdenty();
    }

    protected static void mustIsAuthUser(User user) {
        if (!(user instanceof AuthUser)) {
            throw new IllegalArgumentException("It is not " + AuthUser.class.getName());
        }
    }
}
