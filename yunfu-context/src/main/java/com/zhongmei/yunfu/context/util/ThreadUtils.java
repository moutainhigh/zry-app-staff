package com.zhongmei.yunfu.context.util;

import android.os.Handler;
import android.os.Looper;

import com.zhongmei.atask.TaskContext;



public class ThreadUtils {


    public static void runOnWorkThread(Runnable runnable) {
        if (runnable != null)
            TaskContext.execute(runnable);
    }

    private static Handler sHandler;

    private static void initHandler() {
        if (sHandler == null) {
            sHandler = new Handler(Looper.getMainLooper());
        }
    }

    public static void runOnUiThread(Runnable action) {
        if (action != null) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                initHandler();
                sHandler.post(action);
            } else {
                action.run();
            }
        }
    }
}
