package com.zhongmei.atask;

import android.os.AsyncTask;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by demo on 2018/12/15
 */

public class DefaultThreadFactory implements ThreadFactory {

    private final AtomicInteger mCount = new AtomicInteger(1);
    private String poolThreadName;

    public static DefaultThreadFactory newThreadFactory() {
        return new DefaultThreadFactory(getDefaultTag());
    }

    public DefaultThreadFactory(String poolThreadName) {
        this.poolThreadName = poolThreadName;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, poolThreadName + " #" + mCount.getAndIncrement());
    }

    public static String getDefaultTag() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTraceElements) {
            if (element.getClassName().equals(AsyncTask.class.getName()) || element.getClassName().equals(TaskContext.class.getName())) {
                return element.getClassName();
            }
        }
        return "unknown";
    }
}
