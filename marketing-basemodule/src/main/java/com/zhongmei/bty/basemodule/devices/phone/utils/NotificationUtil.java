package com.zhongmei.bty.basemodule.devices.phone.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import com.zhongmei.yunfu.basemodule.R;


public class NotificationUtil {

    public static void showPhoneStatusNotification(Context context,
                                                   boolean enable) {
        String title = enable ? context.getString(R.string.string_phone_phone_ok) : context.getString(R.string.string_phone_phone_dis);
        String msg = context.getString(R.string.string_phone_settings_to);
        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder build = new Notification.Builder(context);
        Notification n = build.setTicker(title)
                .setSmallIcon(R.drawable.phone_ic_launcher1)
                .setWhen(System.currentTimeMillis()).setContentTitle(title)
                .setContentText(msg).getNotification();
        n.flags = Notification.FLAG_NO_CLEAR;

        // Intent i = new Intent(arg0.getContext(), NotificationShow.class);
        // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        // //PendingIntent
        // PendingIntent contentIntent = PendingIntent.getActivity(
        // arg0.getContext(),
        // R.string.app_name,
        // i,
        // PendingIntent.FLAG_UPDATE_CURRENT);
        //

        nm.notify(0, n);
    }

}
