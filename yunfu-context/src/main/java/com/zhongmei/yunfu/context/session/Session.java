package com.zhongmei.yunfu.context.session;

import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.session.core.user.User;

import java.util.ArrayList;
import java.util.List;


public class Session implements ISession {

    private static AbsSession session;
    private static List<BindListener> bindListeners = new ArrayList<>();

    public static void init(AbsSession session) {
        Session.session = session;
    }

    public static int getAuthVersion() {
        return session.getAuthVersion();
    }

    public static synchronized void bind(final User user, final Callback callback) {
        session.bind(user, callback);
    }

    public static synchronized void unbind() {
        if (session != null) {
            session.unbind();
        }
    }

    public static synchronized AuthUser getAuthUser() {
        if (session == null) {
            return null;
        }
        return session.getAuthUser();
    }

    public static List<BindListener> getBindListeners() {
        return bindListeners;
    }

    public static synchronized void registerCallback(BindListener bindListener) {
        if (bindListener != null) {
            bindListeners.add(bindListener);
        }
    }

    public static synchronized void unRegisterCallback(BindListener bindListener) {
        if (bindListener != null) {
            bindListeners.remove(bindListener);
        }
    }

    public static synchronized <T extends SessionFunc> T getFunc(Class<T> tClass) {
        return session.getFunc(tClass);
    }

}