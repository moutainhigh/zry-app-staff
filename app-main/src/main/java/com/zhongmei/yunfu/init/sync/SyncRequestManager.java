package com.zhongmei.yunfu.init.sync;

import android.content.Context;
import android.support.annotation.NonNull;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.manager.SwitchServerManager;
import com.zhongmei.yunfu.init.sync.bean.SyncModule;
import com.zhongmei.yunfu.init.sync.bean.SyncModuleBean;
import com.zhongmei.yunfu.init.sync.event.SyncEndEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import de.greenrobot.event.EventBus;


public class SyncRequestManager implements Runnable {

    private final Object lock = new Object();    private Context context;
    private SyncModule syncModule;
    private SyncCheckListener checkListener;
    private Thread thread;

    private ExecutorService executorChildService;
    private List<SyncRequestTask> requestChildList;
    private volatile boolean cancelled;
    private volatile Throwable childException;

    public SyncRequestManager(Context context, SyncModule syncModule) {
        this.context = context;
        this.syncModule = syncModule;
        requestChildList = new ArrayList<>();
        executorChildService = Executors.newFixedThreadPool(2, new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            @Override
            public Thread newThread(@NonNull Runnable r) {
                final Thread result = new Thread(r, "sync task #" + mCount.getAndIncrement()) {
                    @Override
                    public void run() {
                        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                        super.run();
                    }
                };
                return result;
            }
        });
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this, "SyncRequestManager");
            thread.start();
        }
    }

    public void stop() {
        cancelled = true;
        checkListener = null;
        executorChildService.shutdownNow();
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    public void setCheckListener(SyncCheckListener listener) {
        checkListener = listener;
    }

    public SyncModule getSyncModule() {
        return syncModule;
    }

    @Override
    public void run() {
        SyncServiceUtil.log("start :同步开始");
        while (true) {
            Throwable exception = null;
                        try {
                checkCancelled();

                childException = null;
                SyncModuleBean syncModuleBean = syncModule.getSyncModules();
                                addChildRequestTask(context, syncModuleBean.getModuleAll(), syncModuleBean.isNotify(), false);
                waitChildThread();

                SyncServiceUtil.log("同步执行完成");


                                SwitchServerManager.getInstance().reset();
                if (!syncModule.hasSyncModules()) {
                    boolean isInit = SyncServiceUtil.isNeedInit();
                    if (isInit) {
                        SyncServiceUtil.setIsNeedInit(false);
                    }
                    syncCheck(childException);
                }
            } catch (Throwable e) {
                exception = e;
                SyncServiceUtil.log("同步出错:" + e.getMessage());
                syncCheck(e);
                if (cancelled) {
                    break;
                }
            } finally {
                EventBus.getDefault().post(new SyncEndEvent(exception));
            }
        }
    }

    private void waitChildThread() throws InterruptedException {
        synchronized (lock) {
            while (!isChildThreadDone()) {
                lock.wait();
            }
        }
    }

    protected boolean isChildThreadDone() {
        for (SyncRequestTask future : requestChildList) {
            if (!future.isDone()) {
                return false;
            }
        }
        return true;
    }

    void notifyChildThread(SyncRequestTask syncRequestTask, Throwable error) {
        syncRequestTask.setDone(error);
        synchronized (lock) {
            if (error != null) {
                if (childException == null) {
                    childException = error;
                    for (SyncRequestTask requestTask : requestChildList) {
                        if (!syncRequestTask.equals(requestTask)) {
                            requestTask.setCancelled(true);
                        }
                    }
                }
            }
            lock.notifyAll();
        }
    }


    protected void addChildRequestTask(Context context, List<String> syncModule, boolean isNotify, boolean isSyncModule) {
        if (syncModule.size() > 0) {
            SyncRequestTask syncRequestTask = new SyncRequestTask(context, this, syncModule, isNotify, isSyncModule);
            requestChildList.add(syncRequestTask);
            executorChildService.submit(syncRequestTask);
        }
    }

    private void setCityCode() {

    }

    private void syncCheck(final Throwable err) {
        if (!cancelled) {
            final boolean success = err == null;
            MainApplication.getInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (checkListener != null) {
                        checkListener.onSyncCheck(success, getErrorMessage(err), err);
                    }
                }
            });
        }
    }

    private String getErrorMessage(Throwable e) {
        if (e != null) {
            if (e instanceof InterruptedException) {
                return context.getString(R.string.login_sync_cancel);
            }
            if (e instanceof SQLException) {
                return "[SQL]" + context.getString(R.string.login_sync_error);
            }
            return context.getString(R.string.login_sync_error);
        }
        return "SYNC OK";
    }

    private void checkCancelled() throws SyncCancelException {
        if (cancelled) {
            throw new SyncCancelException("sync manager cancel");
        }
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
