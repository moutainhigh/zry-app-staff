package com.zhongmei.bty.basemodule.session;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.zhongmei.atask.AbsAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.yunfu.context.session.AbsSession;
import com.zhongmei.yunfu.context.session.Callback;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.yunfu.context.util.peony.ArgsUtils;
import com.zhongmei.yunfu.orm.DatabaseHelper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by demo on 2018/12/15
 */

public class SessionImpl extends AbsSession {

    private static final String TAG = Session.class.getSimpleName();

    public static final int AV1 = 1;
    public static final int AV2 = 2;

    //private static List<BindListener> bindListeners = new ArrayList<>();
    private static Map<Class<? extends SessionFunc>, SessionFunc> pool = new WeakHashMap<>();

    private static AuthUser authUser;

    private static Adapter adapter;

    private static List<Uri> dataObserverUris;
    private static Map<Class<? extends SessionFunc>, Class> dictionary;
    private static Auth auth;

    private static int version;

    private static DatabaseHelper.DataChangeObserver observer = new DatabaseHelper.DataChangeObserver() {
        @Override
        public void onChange(Collection<Uri> uris) {
            if (false && needRefresh(uris)) {
                //目前屏蔽数据库变化的时候加载权限
                if (auth != null) {
                    auth.load(authUser);
                }
            }
        }

        private boolean needRefresh(Collection<Uri> uris) {
            if (!ArgsUtils.isEmpty(uris) && !ArgsUtils.isEmpty(dataObserverUris)) {
                for (Uri changed : uris) {
                    for (Uri local : dataObserverUris) {
                        if (changed.equals(local)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    };

    public SessionImpl(Context context, int authVersion) {
        if (authVersion == SessionImpl.AV1) {
            adapter = new com.zhongmei.bty.basemodule.session.ver.v1.AdapterImpl(context);
        } else if (authVersion == SessionImpl.AV2) {
            adapter = new com.zhongmei.bty.basemodule.session.ver.v2.AdapterImpl(context);
        } else {
            throw new IllegalArgumentException("Not support auth version:" + authVersion);
        }

        log("初始化回话，权限版本为：" + authVersion);

        dataObserverUris = adapter.getDataObserverUris();
        dictionary = adapter.getDictionary();
        auth = adapter.getAuth();
        version = authVersion;
        SpHelper.getDefault().putInt("Session.authVersion", authVersion);

        ArgsUtils.notNull(dictionary, "Can`t return a null dictionary");
        ArgsUtils.notNull(auth, "Can`t return a null Auth");
    }

    public int getAuthVersion() {
        return version;
    }

    public synchronized void bind(final User user, final Callback callback) {
        ArgsUtils.notNull(adapter, "Please init at first");
        ArgsUtils.notNull(user, "Can`t bind a null-User");
        ArgsUtils.notNull(callback, "Callback may not be null");

        log("绑定登录用户");

        TaskContext.execute(new AbsAsyncTask<Void, Boolean>() {
            @Override
            public Boolean doInBackground(Void... params) {
                auth.load(user);
                authUser = new AuthUser(user, auth);
                return true;
            }

            @Override
            public void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (result) {
                    DatabaseHelper.Registry.register(observer);
                    notifyBind();

                    callback.onBindSuccess();
                } else {
                    callback.onBindError("Bind user flow error");
                }
            }
        });
    }

    public synchronized void unbind() {
        if (auth != null) {
            log("解绑登录用户");
            auth.release();
            pool.clear();
            authUser = null;

            DatabaseHelper.Registry.unregister(observer);
            notifyUnbind();
        }
    }

    /**
     * 请注意：
     * 1.本来原来为空的时候，会发送广播，跳转到登录页面，但是由于项目中，很多地方，调用后如果为空，在外面又跳转到
     * 登录页面，所以，后来去掉了发送广播。
     * <p>
     * 2.原来调用unbind的时候，会回调出去，在MainApplication中发送一个广播，跳转到登录页面，但是由于后来需求修改了
     * 解绑登录用户后，不自动跳转到登录页面。
     * <p>
     * 故：基于以上2个原因，请调用这个方法的地方，自行判断是否为null。不要在业务代码很深的地方调用该方法，最好离UI代码
     * 很近的地方调用，然后传入到你所需要的地方。
     *
     * @return
     */
    public synchronized AuthUser getAuthUser() {
        if (authUser == null) {
            log("当前登录用户不存在");
        }
        return authUser;
    }

	/*public synchronized void registerCallback(BindListener bindListener) {
		if (bindListener != null) {
			bindListeners.add(bindListener);
		}
	}

	public synchronized void unRegisterCallback(BindListener bindListener) {
		if (bindListener != null) {
			bindListeners.remove(bindListener);
		}
	}*/

    public synchronized <T extends SessionFunc> T getFunc(Class<T> tClass) {
        ArgsUtils.notNull(tClass, "Class of SessionFunc is null");
        try {
            log("获取 " + tClass.getName() + " 的实例");
            SessionFunc impl = pool.get(tClass);
            if (impl == null) {
                log("没有 " + tClass.getName() + " 的实例，准备新建一个");
                Class implCls = dictionary.get(tClass);
                log(tClass.getName() + " 的实现类为：" + implCls.getName());
                Object object = implCls.newInstance();
                impl = (SessionFunc) object;
                pool.put(tClass, impl);
                log(implCls.getName() + "创建成功");
            }
            return (T) impl;
        } catch (Exception e) {
            log("获取 " + tClass.getName() + " 的实例出现异常：" + e.getMessage());
            throw new RuntimeException("Can`t find an instance of " + tClass.getName());
        }
    }

    private void notifyBind() {
        for (BindListener bindListener : Session.getBindListeners()) {
            bindListener.onBind(authUser);
        }
    }

    private void notifyUnbind() {
        for (BindListener bindListener : Session.getBindListeners()) {
            bindListener.onUnbind(authUser);
        }
    }

    private static void log(String message) {
        Log.d("--->", message);
//		OSLog.getLog("session").log(message);

        //PLog.d("Session--->", message);
    }
}