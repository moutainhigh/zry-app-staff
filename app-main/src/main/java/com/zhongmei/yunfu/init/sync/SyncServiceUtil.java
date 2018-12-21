package com.zhongmei.yunfu.init.sync;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.zhongmei.OSLog;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;

public class SyncServiceUtil {

    private static boolean startSync; //是否已经启动过服务
    private static PendingIntent pendingIntent;

    /**
     * 是否需要初始化
     *
     * @return
     */
    public static boolean isNeedInit() {
        return SharedPreferenceUtil.getSpUtil().getBoolean(SyncConstant.SP_NEED_INIT, true);
    }

    public static void setIsNeedInit(boolean isNeedInit) {
        SharedPreferenceUtil.getSpUtil().putBoolean(SyncConstant.SP_NEED_INIT, isNeedInit);
    }

    /**
     * 启动同步服务
     *
     * @param context
     */
    public static void startService(Context context) {
        SyncService.startService(context);
        if (!startSync) {
            startSync = true;
            // 包装需要执行Service的Intent
            Intent intent = new Intent(context, SyncService.class);
            intent.setAction(SyncService.SYNC_KEEP_ALIVE);
            pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            // 触发服务的起始时间
            long triggerAtTime = SystemClock.elapsedRealtime() + 60 * 1000;
            // 获取AlarmManager系统服务
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            // 使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service
            manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime,
                    60 * 1000, pendingIntent);
        }
    }

    /**
     * 停止服务
     *
     * @param context
     */
    public static void stopService(Context context) {
        if (startSync) {
            startSync = false;
            Intent intent = new Intent(context, SyncService.class);
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            manager.cancel(pendingIntent);
            context.stopService(intent);
        }
    }

    static final OSLog log = OSLog.getLog("sync");

    public static void info(String msg, Object... args) {
        log.info(msg, args);
    }

    public static void error(Throwable t, String msg, Object... args) {
        log.error(t, msg, args);
    }

    public static void log(String msg, Object... args) {
        log.log(msg, args);
    }

    public static void log(Throwable t, String msg, Object... args) {
        log.log(t, msg, args);
    }
}
