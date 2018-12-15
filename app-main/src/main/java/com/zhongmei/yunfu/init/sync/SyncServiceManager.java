package com.zhongmei.yunfu.init.sync;

import android.content.Context;

import com.zhongmei.yunfu.init.sync.bean.SyncModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 同步服务管理器
 */
public class SyncServiceManager {

    private SyncModule syncModule;
    private SyncRequestManager syncRequestTask;
    private SyncTaskTimer syncTaskTimer;
    private static final List<SyncCheckListener> checkListener = new ArrayList<>();

    public SyncServiceManager(Context context) {
        syncModule = new SyncModule();
        syncRequestTask = new SyncRequestManager(context, syncModule);
        syncRequestTask.setCheckListener(new SyncCheckListener() {
            @Override
            public void onSyncCheck(boolean success, String errorMsg, Throwable err) {
                notifyCheckListener(success, errorMsg, err);
                if (syncTaskTimer == null) {
                    syncTaskTimer = new SyncTaskTimer(syncModule);
                    syncTaskTimer.start();
                }
            }
        });
    }

    public void start() {
        SyncServiceUtil.log("start");
        syncRequestTask.start();
    }

    public void putSyncModules(Collection<String> modules) {
        syncModule.putSyncModules(modules, false);
    }

    public void putSyncModules(Collection<String> modules, boolean isNotify) {
        syncModule.putSyncModules(modules, isNotify);
    }

    public static void addCheckListener(SyncCheckListener listener) {
        synchronized (checkListener) {
            checkListener.add(listener);
        }
    }

    public static void removeCheckListener(SyncCheckListener listener) {
        synchronized (checkListener) {
            checkListener.remove(listener);
        }
    }

    public static void clearCheckListener() {
        synchronized (checkListener) {
            checkListener.clear();
        }
    }

    private void notifyCheckListener(boolean success, String errorMsg, Throwable err) {
        synchronized (checkListener) {
            for (int i = checkListener.size() - 1; i >= 0; i--) {
                checkListener.get(i).onSyncCheck(success, errorMsg, err);
            }
        }
    }

    public void shutdown() {
        SyncServiceUtil.log("shutdown");
        clearCheckListener();
        if (syncTaskTimer != null) {
            syncTaskTimer.stop();
            syncTaskTimer = null;
        }
        if (syncRequestTask != null) {
            syncRequestTask.stop();
            syncRequestTask = null;
        }
    }
}
