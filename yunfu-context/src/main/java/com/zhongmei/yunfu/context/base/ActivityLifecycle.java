package com.zhongmei.yunfu.context.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Desc
 *
 * @created 2017/6/19
 */
public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private List<SoftReference<Activity>> activityList = new ArrayList<>();//管理程序中所有activity
    private AppExitCallback appExitCallback;
    private AppExitCallback finishExitCallback;

    public interface AppExitCallback {
        void onAppExit();
    }

    public ActivityLifecycle(AppExitCallback appExitCallback) {
        this.appExitCallback = appExitCallback;
    }

    /**
     * 添加activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        synchronized (activityList) {
            activityList.add(new SoftReference<>(activity));
        }
    }

    /**
     * 移出activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        synchronized (activityList) {
            for (int i = activityList.size() - 1; i >= 0; i--) {
                SoftReference<Activity> reference = activityList.get(i);
                if (reference != null && reference.get() == activity) {
                    activityList.remove(i);
                }
            }
        }
    }

    /**
     * 结束所有activity
     */
    public void finishAllActivity(AppExitCallback callback) {
        this.finishExitCallback = callback;
        synchronized (activityList) {
            for (int i = activityList.size() - 1; i >= 0; i--) {
                SoftReference<Activity> reference = activityList.get(i);
                if (reference != null) {
                    Activity activity = reference.get();
                    if (activity != null && !activity.isFinishing()) {
                        activity.finish();
                    }
                }
            }

            activityList.clear();
        }
    }

    /**
     * 获取当前
     *
     * @return
     */
    public Activity getCurrentActivity() {
        for (int i = activityList.size() - 1; i >= 0; i--) {
            SoftReference<Activity> softReference = activityList.get(i);
            Activity activity = softReference.get();
            if (activity != null && !activity.isFinishing()) {
                return activity;
            }
        }

        return null;
    }

    public FragmentActivity getCurrentFragmentActivity() {
        Activity activity = getCurrentActivity();
        return activity instanceof FragmentActivity ? (FragmentActivity) activity : null;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        removeActivity(activity);
        if (activityList.isEmpty()) {
            callAppExit();
        }
    }

    private void callAppExit() {
        Log.i("ActivityLifecycle", "callAppExit");
        if (appExitCallback != null) {
            appExitCallback.onAppExit();
        }

        if (finishExitCallback != null) {
            finishExitCallback.onAppExit();
        }

        System.exit(0);
    }
}
