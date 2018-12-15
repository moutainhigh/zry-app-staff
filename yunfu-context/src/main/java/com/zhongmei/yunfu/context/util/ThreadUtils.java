package com.zhongmei.yunfu.context.util;

import android.os.Handler;
import android.os.Looper;

import com.zhongmei.atask.TaskContext;

/**
 * 统一处理短时间的任务，减少零散的线程或线程池
 * Created by demo on 2018/12/15
 */

public class ThreadUtils {
   /* private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;
    private static ExecutorService sThreadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new DefaultThreadFactory("ThreadUtils"));*/

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
