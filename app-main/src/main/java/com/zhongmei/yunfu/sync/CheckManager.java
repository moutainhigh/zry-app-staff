package com.zhongmei.yunfu.sync;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
public class CheckManager {

    private volatile Looper mServiceLooper;
    private volatile ServiceHandler mServiceHandler;

    private List<InitCheck> initCheckGroup = new ArrayList<>();
    private boolean cancelled;
    private InitCheckCallback callback;
    static CheckManager checkManager;
    private static int ignore = 0;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            onHandleCheck(msg.arg1);
        }
    }

    private CheckManager(InitCheckCallback callback) {
        this.callback = callback;
        cancelled = false;
        initCheckGroup.add(new ShopBaseInitCheck());
        initCheckGroup.add(new ShopConfigInitCheck());
        initCheckGroup.add(new ShopDataInitCheck());
        initCheckGroup.add(new ShopCacheInitCheck());

        HandlerThread thread = new HandlerThread("CheckManager");
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    public static void initCheck(final InitCheckCallback callback) {
        cancelCheck(false);
        checkManager = new CheckManager(callback);
        checkManager.start();
    }

    public static void setIgnore(int flag) {
        CheckManager.ignore |= flag;
    }

    public static boolean isIgnore(int flag) {
        return (CheckManager.ignore & flag) == flag;
    }

    public void start() {
        initCheckGroup.iterator();
        sendCheckCmd(0);
    }

    private void sendCheckCmd(int index) {
        if (mServiceHandler != null) {
            Message msg = mServiceHandler.obtainMessage();
            msg.arg1 = index;
            mServiceHandler.sendMessage(msg);
        }
    }

    protected void onHandleCheck(final int i) {
        if (cancelled || i >= initCheckGroup.size()) {
            return;
        }

        InitCheck initCheck = initCheckGroup.get(i);
        initCheck.check(new InitCheckCallback() {
            @Override
            public void onCheckProgress(InitCheck initCheck, int progress, String message) {
                checkProgress(initCheck, progress, message);
                if (progress == 100) {
                    sendCheckCmd(i + 1);
                }
            }

            @Override
            public void onCheckComplete(InitCheck initCheck, boolean success, String error, Throwable err) {
                checkComplete(initCheck, success, error, err);
            }
        });
    }

    private void checkProgress(final InitCheck initCheck, final int progress, final String message) {
        if (!cancelled) {
            handlerPost(new Runnable() {
                @Override
                public void run() {
                    callback.onCheckProgress(initCheck, progress, message);
                }
            });
        }
    }

    private void checkComplete(final InitCheck initCheck, final boolean success, final String error, final Throwable err) {
        if (!cancelled) {
            handlerPost(new Runnable() {
                @Override
                public void run() {
                    callback.onCheckComplete(initCheck, success, error, err);
                }
            });
        }
    }

    public static void cancelCheck() {
        cancelCheck(true);
    }

    private static void cancelCheck(boolean clearIgnore) {
        if (clearIgnore) {
            ignore = 0;
        }
        if (checkManager != null) {
            checkManager.cancel();
            checkManager = null;
        }
    }

    public void cancel() {
        cancelled = true;
        mServiceLooper.quit();
        mServiceHandler = null;
    }

    private static void handlerPost(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

}
