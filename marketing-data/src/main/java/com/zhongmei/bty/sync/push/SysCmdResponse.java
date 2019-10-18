package com.zhongmei.bty.sync.push;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;



public class SysCmdResponse {

    public PushConfig push;
    @SerializedName("sync-module")
    public SyncModuleConfig syncModule;



    public static class PushConfig {
        public int heartBeatTime = 20;
        public int readIdleTime = 30;
    }


    public static class SyncModuleConfig implements Serializable {
        @SerializedName("short")
        public ModuleBean small;
        public ModuleBean middle;
        @SerializedName("long")
        public ModuleBean large;

        public List<ModuleBean> getModuleConfig(Set<String> modules) {
            List<ModuleBean> configModuleList = new ArrayList<>();
            addModuleBean(configModuleList, small);
            addModuleBean(configModuleList, middle);
            addModuleBean(configModuleList, large);
            ModuleBean syncModuleOther = getSyncModuleOther(configModuleList, modules);
            addModuleBean(configModuleList, syncModuleOther);
            return configModuleList;
        }

        private void addModuleBean(List<ModuleBean> result, ModuleBean bean) {
            if (bean != null && bean.modules != null && bean.modules.size() > 0) {
                result.add(bean);
            }
        }

        private ModuleBean getSyncModuleOther(List<ModuleBean> moduleConfig, Set<String> modules) {
            List<String> result = new ArrayList<>();
            for (String module : modules) {
                if (!containsModule(moduleConfig, module)) {
                    result.add(module);
                }
            }

            return result.size() > 0 ? createModuleBean(result) : null;
        }

        public static ModuleBean createModuleBean(List<String> result) {
            ModuleBean moduleConfig = new ModuleBean();
            moduleConfig.interval = 180;             moduleConfig.modules = result;
            return moduleConfig;
        }

        public boolean containsModule(List<ModuleBean> moduleConfig, String moduleName) {
            for (ModuleBean bean : moduleConfig) {
                if (bean.modules != null) {
                    if (bean.modules.contains(moduleName)) {
                        return true;
                    }
                }
            }

            return false;
        }

        public static class ModuleBean implements Serializable {
            private int interval = 200;             public List<String> modules;
            public int getInterval() {
                return interval < 100 ? 100 : interval;
            }
        }
    }
}
