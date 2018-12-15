package com.zhongmei.yunfu.context.session;

import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.session.core.user.User;

/**
 * Created by demo on 2018/12/15
 */

public abstract class AbsSession implements ISession {

    public abstract int getAuthVersion();

    public abstract void bind(final User user, final Callback callback);

    public abstract void unbind();

    public abstract AuthUser getAuthUser();

    /*public abstract void registerCallback(BindListener bindListener);

    public abstract void unRegisterCallback(BindListener bindListener);*/

    public abstract <T extends Session.SessionFunc> T getFunc(Class<T> tClass);

}