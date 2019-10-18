package com.zhongmei.yunfu.init.sync.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SyncModuleBean {

    private List<String> syncModules;
    private boolean isNotify;
    private static final Set<String> hyalineModule = new HashSet<>();


    public SyncModuleBean(List<String> syncModules, boolean isNotify) {
        this.syncModules = syncModules;
        this.isNotify = isNotify;
    }

    public List<String> getModuleAll() {
        return syncModules;
    }

    public boolean isNotify() {
        return isNotify;
    }

    public List<String> getSyncModule() {
        return getModuleInner(true);
    }

    public List<String> getOtherModule() {
        return getModuleInner(false);
    }

    protected List<String> getModuleInner(boolean syncModule) {
        List<String> result = new ArrayList<>();
        for (String module : syncModules) {

            if (isAddModule(module, syncModule)) {
                result.add(module);
            }
        }
        return result;
    }

    private boolean isAddModule(String module, boolean syncModule) {
        return syncModule ? !hyalineModule.contains(module) : hyalineModule.contains(module);
    }
}
