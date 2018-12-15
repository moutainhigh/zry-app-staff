package com.zhongmei.yunfu.init.sync;

import com.zhongmei.yunfu.init.sync.bean.SyncContent;
import com.zhongmei.bty.sync.push.SysCmdResponse;
import com.zhongmei.yunfu.init.sync.SyncServiceUtil;
import com.zhongmei.yunfu.init.sync.bean.SyncModule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 任务定时器，定时把同步表添加到同步池
 * Created by demo on 2018/12/15
 */
public class SyncTaskTimer {

    private final ScheduledExecutorService scheduledExecService;
    private List<ScheduledFuture<?>> heartbeatFuture;
    private final SyncModule syncModule;

    public SyncTaskTimer(SyncModule syncModule) {
        this.syncModule = syncModule;
        this.scheduledExecService = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        SyncServiceUtil.log("start");
        cancelScheduledThread();
        final List<SysCmdResponse.SyncModuleConfig.ModuleBean> moduleConfig = SyncContent.getModuleConfig();
        heartbeatFuture = new ArrayList<>();
        for (final SysCmdResponse.SyncModuleConfig.ModuleBean config : moduleConfig) {
            final int delay = config.getInterval();
            SyncServiceUtil.info("delay: " + delay + ", modules=" + config.modules);
            heartbeatFuture.add(scheduledExecService.scheduleWithFixedDelay(
                    new Runnable() {
                        @Override
                        public void run() {
                            SyncServiceUtil.info("delay: " + delay + ", modules=" + config.modules);
                            syncModule.putSyncModules(config.modules);
                        }
                    }, delay, delay, TimeUnit.SECONDS));
        }
    }

    public static long gys(int... b) {
        int gbs = 0;
        if (b != null && b.length > 0) {
            gbs = b[0];
            for (int i = 1; i < b.length; i++) {
                gbs = LCM(gbs, b[i]);
            }
        }

        return gbs;
    }

    public static int LCM(int a, int b) {
        return a * b / GCD(a, b);
    }

    public static int GCD(int a, int b) {
        return a % b == 0 ? b : GCD(b, a % b);
    }

    private void cancelScheduledThread() {
        if (heartbeatFuture != null) {
            for (ScheduledFuture<?> future : heartbeatFuture) {
                future.cancel(true);
            }
            heartbeatFuture = null;
        }
    }

    public void stop() {
        SyncServiceUtil.log("stop");
        cancelScheduledThread();
        scheduledExecService.shutdownNow();
    }
}
