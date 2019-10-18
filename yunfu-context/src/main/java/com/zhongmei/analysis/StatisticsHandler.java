package com.zhongmei.analysis;

import android.os.Handler;
import android.os.HandlerThread;


public class StatisticsHandler {
    private static final StatisticsHandler INSTANCE = new StatisticsHandler();
    private Handler mHandler;
    private final HandlerThread mThread = new HandlerThread("statistic.bus", 10);

    public static StatisticsHandler getInstance() {
        return INSTANCE;
    }

    private StatisticsHandler() {
        mThread.start();
    }

    private synchronized Handler getHandler() {
        if (this.mHandler == null) {
            this.mHandler = new Handler(this.mThread.getLooper());
        }
        return this.mHandler;
    }

    public synchronized void commit(Runnable r) {
        getHandler().post(r);
    }

    public synchronized void commitAtFrontOfQueue(Runnable r) {
        getHandler().postAtFrontOfQueue(r);
    }

    public synchronized void commitDelayed(Runnable r, long delayMillis) {
        getHandler().postDelayed(r, delayMillis);
    }

    public synchronized void remove(Runnable r) {
        getHandler().removeCallbacks(r);
    }
}
