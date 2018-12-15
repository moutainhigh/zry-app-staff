package com.zhongmei.bty.commonmodule.util;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

/**
 * Created by demo on 2018/12/15
 */

public class RingtoneUtils {

    private final static String TAG = RingtoneUtils.class.getSimpleName();

    public static void playSystemRingtone(Context context) {
        // 加载铃声
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ring = RingtoneManager.getRingtone(context, notification);
            ring.play();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

}
