package com.zhongmei.yunfu.context.session;

import android.content.Context;
import android.net.Uri;

import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;

import java.util.List;
import java.util.Map;

public interface ISession {

    public interface BindListener {
        void onBind(AuthUser user);

        void onUnbind(AuthUser user);
    }

    /*public interface Callback {
        void onBindSuccess();

        void onBindError(String message);
    }*/

    public interface SessionFunc {

    }

    public interface Adapter {
        Context getContext();

        List<Uri> getDataObserverUris();

        Map<Class<? extends SessionFunc>, Class> getDictionary();

        Auth getAuth();

        void release();
    }
}
