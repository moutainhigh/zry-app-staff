package com.zhongmei.bty.common.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zhongmei.OSLog;
import com.zhongmei.bty.common.util.UpdateManager;
import com.zhongmei.bty.common.util.UploadLogUtil;

public class SystemReceiver extends BroadcastReceiver {
    private static final String TAG = SystemReceiver.class.getSimpleName();

    public static String SYSTEM_RECEIVER_NETWORK_ACTION = "com.zhongmei.bty.system.access.network";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (SYSTEM_RECEIVER_NETWORK_ACTION.equals(action)) {
            try {
                // CalmHintDialog dialog = new CalmHintDialog(context);
                // dialog.show();
                Intent intents = new Intent();
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // intents.setClass(context, CalmHintDialog.class);
                context.startActivity(intents);
            } catch (Exception e) {
                OSLog.error(e, "");
            }
        } else if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            OSLog.info("download id :"
                    + intent.getLongExtra(
                    DownloadManager.EXTRA_DOWNLOAD_ID, 0));
            doUpdateApp();
        } else if (UploadLogUtil.ACTION_UPLOAD_LOG.equals(action)) {
            //Caishier日志自动上传广播
            UploadLogUtil.uploadCashierLog();
        }
    }

    /**
     * 执行下载完成监听，更新app
     */
    private void doUpdateApp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UpdateManager.getInstance().queryDownloadStatus();
            }
        }).start();
    }
}
