package com.zhongmei.atask;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 统一管理AsyncTask绑定Fragment,Activity生命周期
 *
 * @Created by demo on 2018/12/15
 */
public class TaskContext {

    private static final String TAG = TaskContext.class.getSimpleName();

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;
    public static final ExecutorService THREAD_POOL_EXECUTOR
            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new DefaultThreadFactory("TaskContext"));

    static final String FRAGMENT_TAG = "com.zhongmei.bty.common.atask";
    private volatile TaskManager applicationManager;
    private final HashMap<Object, ITaskManagerFragment> taskManagerFragments = new HashMap<>();
    private static TaskContext defaultInstance;

    private TaskContext() {
        setDefaultExecutor();
        applicationManager = new TaskManager(this, null);
    }

    public static void setDefaultExecutor() {
        try {
            setValue(AsyncTask.class, "sDefaultExecutor", THREAD_POOL_EXECUTOR);
            setValue(AsyncTask.class, "THREAD_POOL_EXECUTOR", THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            try {
                invokeMethod(AsyncTask.class, "setDefaultExecutor", new Class[]{Executor.class}, THREAD_POOL_EXECUTOR);
            } catch (Exception em) {
                Log.e(TAG, em.getMessage(), em);
            }
        }
    }

    public static void setValue(Class<?> clazz, String fieldName, Object newValue) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, newValue);
    }

    public static void invokeMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes, Object newValue) throws Exception {
        Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
        invokeMethod(method, newValue);
    }

    public static void invokeMethod(Method method, Object newValue) throws Exception {
        method.setAccessible(true);
        method.invoke(null, newValue);
    }

    private static TaskContext getDefault() {
        synchronized (TaskContext.class) {
            if (defaultInstance == null) {
                defaultInstance = new TaskContext();
            }
        }

        return defaultInstance;
    }

    @Deprecated
    public static TaskManager bindDefault() {
        TaskContext taskContext = getDefault();
        return taskContext.applicationManager;
    }

    public static TaskManager bind(FragmentActivity activity) {
        FragmentManager fm = activity.getSupportFragmentManager();
        TaskContext taskContext = getDefault();
        return taskContext.supportFragmentGet(activity, fm);
    }

    public static TaskManager bind(Fragment fragment) {
        FragmentManager fm = fragment.getChildFragmentManager();
        TaskContext taskContext = getDefault();
        return taskContext.supportFragmentGet(fragment, fm);
    }

    public static TaskManager bind(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return bindDefault();
        } else {
            android.app.FragmentManager fm = activity.getFragmentManager();
            TaskContext taskContext = getDefault();
            return taskContext.fragmentGet(activity, fm);
        }
    }

    public static TaskManager bind(android.app.Fragment fragment) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return bindDefault();
        } else {
            android.app.FragmentManager fm = fragment.getChildFragmentManager();
            TaskContext taskContext = getDefault();
            return taskContext.fragmentGet(fragment, fm);
        }
    }

    private TaskManager fragmentGet(Object parent, android.app.FragmentManager fm) {
        if (fm.isDestroyed()) {
            Log.e("TaskContext", "FragmentManager is Destroyed: " + parent.getClass().getName());
            return applicationManager;
        }

        TaskManagerFragment current = (TaskManagerFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current == null) {
            current = (TaskManagerFragment) taskManagerFragments.get(parent);
            if (current == null) {
                current = new TaskManagerFragment();
                taskManagerFragments.put(parent, current);
                fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();
            }
        }
        TaskManager taskManager = getTaskManager(parent, current);
        return taskManager;
    }

    private TaskManager supportFragmentGet(Object parent, FragmentManager fm) {
        if (fm.isDestroyed()) {
            Log.e("TaskContext", "FragmentManager is Destroyed: " + parent.getClass().getName());
            return applicationManager;
        }

        TaskManagerSupportFragment current = (TaskManagerSupportFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current == null) {
            current = (TaskManagerSupportFragment) taskManagerFragments.get(parent);
            if (current == null) {
                current = new TaskManagerSupportFragment();
                taskManagerFragments.put(parent, current);
                fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();
            }
        }
        TaskManager taskManager = getTaskManager(parent, current);
        return taskManager;
    }

    private TaskManager getTaskManager(Object parent, ITaskManagerFragment current) {
        TaskManager taskManager = current.getTaskManager();
        if (taskManager == null) {
            taskManager = new TaskManager(this, parent);
            current.setTaskManager(taskManager);
        }
        return taskManager;
    }

    void removeTaskFragment(Object parent) {
        taskManagerFragments.remove(parent);
    }

    public static void execute(Runnable asyncTask) {
        THREAD_POOL_EXECUTOR.execute(asyncTask);
    }

    public static <Progress, Result> AbsAsyncTask<Progress, Result> execute(AbsAsyncTask<Progress, Result> asyncTask) {
        return (AbsAsyncTask<Progress, Result>) AsyncTaskCompat.executeParallel(asyncTask);
    }

    public static void bindExecute(Activity activity, final Runnable runnable) {
        bindExecute(activity, new SimpleAsyncTask<Void>() {
            @Override
            public Void doInBackground(Void... params) {
                if (runnable != null) {
                    runnable.run();
                }
                return null;
            }
        });
    }

    public static void bindExecute(Fragment fragment, final Runnable runnable) {
        bindExecute(fragment, new SimpleAsyncTask<Void>() {
            @Override
            public Void doInBackground(Void... params) {
                if (runnable != null) {
                    runnable.run();
                }
                return null;
            }
        });
    }

    public static <Progress, Result> void bindExecute(Activity activity, AbsAsyncTask<Progress, Result> asyncTask) {
        bindExecute(activity, asyncTask, null);
    }

    public static <Progress, Result> void bindExecute(Activity activity, final AbsAsyncTask<Progress, Result> asyncTask, String tag) {
        TaskContext.bind(activity).execute(asyncTask, tag);
    }

    public static <Progress, Result> void bindExecute(Fragment fragment, AbsAsyncTask<Progress, Result> asyncTask) {
        bindExecute(fragment, asyncTask, null);
    }

    public static <Progress, Result> void bindExecute(Fragment fragment, final AbsAsyncTask<Progress, Result> asyncTask, String tag) {
        TaskContext.bind(fragment).execute(asyncTask, tag);
    }
}
