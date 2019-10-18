package com.zhongmei.yunfu;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;

public class BootReceiver extends BroadcastReceiver {

    public static final String ACTION_APP_RESTART = "com.zhongmei.yunfu.app_restart";

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
        }  else if (ACTION_APP_RESTART.equals(action)) {
                        PackageManager packageManager = context.getPackageManager();
            Intent sender = packageManager.getLaunchIntentForPackage(context.getPackageName());
            sender.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(sender);
        }
    }

    private void startLoginInitActivity(Context context) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime() + 100;
        PackageManager packageManager = context.getPackageManager();
        Intent sender = packageManager.getLaunchIntentForPackage(context.getPackageName());
        sender.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi = PendingIntent.getActivity(context, 0, sender, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
    }

}