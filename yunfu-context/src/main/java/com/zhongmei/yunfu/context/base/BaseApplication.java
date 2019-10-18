package com.zhongmei.yunfu.context.base;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;

import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.context.util.AppUtils;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.Map;


public abstract class BaseApplication extends MultiDexApplication implements ActivityLifecycle.AppExitCallback {
    public static BaseApplication sInstance;

    private ActivityLifecycle activityLifecycle;
    final Handler mHandler = new Handler();

    public void finishAllActivity(ActivityLifecycle.AppExitCallback callback) {
        activityLifecycle.finishAllActivity(callback);
    }

    public static void setContext(BaseApplication application) {
        sInstance = application;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sInstance = this;
            }

    public static BaseApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new AppExceptionHandler(this.getApplicationContext()));
        registerActivityLifecycleCallbacks(activityLifecycle = new ActivityLifecycle(this));
        initCommonInfo();
        if (this.getPackageName().equals(AppUtils.getCurProcessName(this))) {
            initLocalInfo();
        }
    }

    protected void initCommonInfo() {
    }

    protected void initLocalInfo() {
    }

    public Long getBrandIdenty() {
        return ShopInfoManager.getInstance().getShopInfo().getBrandId();
    }

    public String getBrandName() {
        return ShopInfoManager.getInstance().getShopInfo().getShopName();
    }

    public Long getShopIdenty() {
        return ShopInfoManager.getInstance().getShopInfo().getShopId();
    }

    public String getDeviceIdenty() {
        return SystemUtils.getMacAddress();
    }

    public ActivityLifecycle getActivityLifecycle() {
        return activityLifecycle;
    }


    public abstract Map<String, String> tokenEncrypt();

    @Override
    public void onAppExit() {

    }

    public final void runOnUiThread(Runnable action) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            mHandler.post(action);
        } else {
            action.run();
        }
    }

    public final void postDelayed(Runnable action, long delayMillis) {
        mHandler.removeCallbacks(action);
        mHandler.postDelayed(action, delayMillis);
    }

    private boolean flag;

    public final void setExceptionUnCacthed(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void sendBroadcast(Intent intent) {
        if (flag) {
            return;
        }
        super.sendBroadcast(intent);
    }
}
