package com.zhongmei.yunfu.init.sync;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.zhongmei.yunfu.init.sync.bean.ModulesBody;
import com.zhongmei.yunfu.init.sync.bean.SyncContent;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;


public class SyncService extends Service {

    public static final String SYNC_KEEP_ALIVE = ":SYNC_KEEP_ALIVE";
    public static final String SYNC_MODULE_PUSH = ":SYNC_MODULE_PUSH";
    private static final String EXTRA_MODULE_KEY = "module";

    private SyncServiceManager syncServiceManager;
    private Set<String> mSyncAllModules = SyncContent.getSyncModuleAll();     private long keepAliveCount = 0;
    public static void startService(Context context) {
        Intent intent = new Intent(context, SyncService.class);
        context.startService(intent);
    }

    public static void startService(Context context, String command, ModulesBody modulesBody) {
        Intent intent = new Intent(context, SyncService.class);
        intent.setAction(command);
        intent.putExtra(EXTRA_MODULE_KEY, modulesBody);
        context.startService(intent);
    }

    public static void stopService(Context context) {
        Intent intent = new Intent(context, SyncService.class);
        context.stopService(intent);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SyncServiceUtil.log("onCreate");
        syncServiceManager = new SyncServiceManager(this);
        syncServiceManager.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SyncServiceUtil.log("onStartCommand");
        ModuleBean moduleBean = getSyncModules(intent);
        if (moduleBean != null) {
            syncServiceManager.putSyncModules(moduleBean.modules, moduleBean.isNotify);
        }

        return START_REDELIVER_INTENT;
    }

    private ModuleBean getSyncModules(Intent intent) {
        if (intent != null) {
            String command = intent.getAction();
            if (SYNC_MODULE_PUSH.equals(command)) {
                Serializable content = intent.getSerializableExtra(EXTRA_MODULE_KEY);
                if (content instanceof ModulesBody) {
                    ModulesBody modulesBody = (ModulesBody) content;
                    SyncServiceUtil.log("module: " + modulesBody);
                    return new ModuleBean(modulesBody.getModules(), true);
                }
            } else if (SYNC_KEEP_ALIVE.equals(command)) {
                keepAliveCount++;
                SyncServiceUtil.log("keepAliveCount: " + keepAliveCount);
                return null;
            }
        }
        SyncServiceUtil.log("module: SyncContent.getSyncModuleAll()");
        return new ModuleBean(mSyncAllModules, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SyncServiceUtil.log("onDestroy: onStartCommandCount" + keepAliveCount);
        syncServiceManager.shutdown();
    }

    class ModuleBean {
        Collection<String> modules;
        boolean isNotify;

        public ModuleBean(Collection<String> modules, boolean isNotify) {
            this.modules = modules;
            this.isNotify = isNotify;
        }
    }
}
