package com.zhongmei.yunfu.context.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;

import com.zhongmei.OSLog;
import com.zhongmei.yunfu.context.R;
import com.zhongmei.yunfu.context.ui.SystemExceptionActivity;

/**
 * Created by demo on 2018/12/15
 */

public class AppExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Context context;

    public AppExceptionHandler(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        OSLog.getLog("crash").logImmediate(ex, ex.getMessage());
        BaseApplication.sInstance.setExceptionUnCacthed(true);

        ex.printStackTrace();
        notice(thread, ex);
    }

    private void notice(final Thread thread, final Throwable ex) {
//        noticeWithActivity(thread, ex);
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
//                noticeWithDialog(thread, ex);
                noticeWithActivity(thread, ex);
                Looper.loop();
            }
        }.start();
    }

    private void noticeWithActivity(Thread thread, Throwable ex) {
        Intent intent = new Intent(context, SystemExceptionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (BaseApplication.getInstance() != null) {
                    BaseApplication.getInstance().finishAllActivity(null);
                }
                System.exit(1);
            }
        }, 1000L);
    }

    private void noticeWithDialog(final Thread thread, final Throwable ex) {
        Dialog dialog = getDialog(thread, ex);
        dialog.show();

        new Handler(Looper.myLooper()).postDelayed(new InnerRunnable(dialog), 5000L);
    }

    private Dialog getDialog(Thread thread, Throwable ex) {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.calm_exception_crash_title));
        builder.setMessage(context.getString(R.string.calm_exception_crash_desc));
        builder.setPositiveButton(context.getString(R.string.calm_exception_crash_submit),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        BaseApplication.getInstance().finishAllActivity(null);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                });

        dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        return dialog;
    }

    private static class InnerRunnable implements Runnable {

        private final Dialog dialog;

        public InnerRunnable(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void run() {
            if (dialog != null) {
                dialog.dismiss();
            }
            if (BaseApplication.getInstance() != null) {
                BaseApplication.getInstance().finishAllActivity(null);
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

}
