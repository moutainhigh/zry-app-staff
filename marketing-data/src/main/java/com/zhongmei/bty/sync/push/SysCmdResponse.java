package com.zhongmei.bty.sync.push;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by demo on 2018/12/15
 */

public class SysCmdResponse {

    public PushConfig push;
    @SerializedName("sync-module")
    public SyncModuleConfig syncModule;


    /**
     * 客户端发送心跳包间隔最大时间 单位秒 默认60S
     * 客户端超过XX秒没有收到服务器数据包则断开连接重连 单位秒 默认130S 对应SO_TIMEOUT
     */
    public static class PushConfig {
        public int heartBeatTime = 20;
        public int readIdleTime = 30;
    }

    /**
     * 关于同步下行模块配置处理
     * 客户端需做健壮性处理以防配置操作导致程序报错或者异常
     * <p>
     * 判断sync-module是否为空
     * 判断所有interval不能少于100，若小于100则本地采用100
     * 如果有没有配置在sync-module中的模块需要同步下行，建议默认按照300S间隔轮询（正常情况下应该不会出现这种情况）
     */
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
            moduleConfig.interval = 180; //3分钟
            moduleConfig.modules = result;
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
            private int interval = 200; //轮询时间间隔 单位秒 默认200S
            public List<String> modules; //模块key名称列表

            public int getInterval() {
                return interval < 100 ? 100 : interval;
            }
        }
    }
}
