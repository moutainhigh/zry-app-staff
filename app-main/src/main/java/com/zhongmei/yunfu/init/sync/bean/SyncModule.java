package com.zhongmei.yunfu.init.sync.bean;

import com.zhongmei.yunfu.init.sync.SyncServiceUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class SyncModule {

    private static final Set<String> waitSyncModules = new LinkedHashSet<>();    private volatile boolean isNotify = false;

    public boolean hasSyncModules() {
        synchronized (waitSyncModules) {
            return waitSyncModules.size() > 0;
        }
    }

    public SyncModuleBean getSyncModules() throws InterruptedException {
        return getSyncModules(null);
    }

    public SyncModuleBean getSyncModules(WaitCallback callback) throws InterruptedException {
        synchronized (waitSyncModules) {
            SyncServiceUtil.info(String.format("getSyncModules[%d]: %s", waitSyncModules.size(), waitSyncModules));
            if (hasSyncModules()) {
                List<String> modules = new ArrayList<>(waitSyncModules);
                waitSyncModules.clear();
                isNotify = false;
                excludeModule(modules);
                Collections.sort(modules);
                return new SyncModuleBean(modules, isNotify);
            }

            try {
                SyncServiceUtil.info("getSyncModules: wait()");
                if (callback != null) {
                    callback.onWait();
                }
                waitSyncModules.wait();
            } catch (InterruptedException e) {
                SyncServiceUtil.info("getSyncModules: " + e.getMessage());
                throw new InterruptedException(e.getMessage());
            }
            return getSyncModules();
        }
    }

    public void putSyncModules(Collection<String> modules) {
        putSyncModules(modules, false);
    }


    public void putSyncModules(Collection<String> modules, boolean isNotify) {
        synchronized (waitSyncModules) {
            SyncServiceUtil.info(String.format("putSyncModules[%d]: %s", modules.size(), modules));
            if (modules != null) {
                waitSyncModules.addAll(modules);
                                if (isNotify) {
                    this.isNotify = isNotify;
                }

                waitSyncModules.notifyAll();
            }
        }
    }

    interface WaitCallback {
        void onWait();
    }


    public static void excludeModule(Collection<String> modules) {
        Set<String> syncModuleAll = SyncContent.getSyncModuleAll();
        List<String> removeList = new ArrayList<>();
        Iterator<String> it = modules.iterator();
        while (it.hasNext()) {
            String m = it.next();
            if (!syncModuleAll.contains(m)) {
                it.remove();
                removeList.add(m);
            }
        }

        SyncServiceUtil.info("excludeModule removeList: " + removeList);
    }
}
