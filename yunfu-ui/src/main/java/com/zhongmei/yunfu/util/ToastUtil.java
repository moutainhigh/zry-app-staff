package com.zhongmei.yunfu.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.ui.view.CalmToastView;

public class ToastUtil {





    public static void showLongToast(String msg) {
                CalmToastView sToastView = new CalmToastView(BaseApplication.sInstance);
        sToastView.setMessage(msg);
        sToastView.setDuration(Toast.LENGTH_LONG);
        sToastView.show();
    }

    public static void showLongToast(String msg, int msgId) {
                CalmToastView sToastView = new CalmToastView(BaseApplication.sInstance);
        String message = BaseApplication.sInstance.getResources().getString(msgId);
        sToastView.setMessage(msg + message);
        sToastView.setDuration(Toast.LENGTH_LONG);
        sToastView.show();
    }


    public static void showLongToast(int msgRes) {
                CalmToastView sToastView = new CalmToastView(BaseApplication.sInstance);
        sToastView.setMessage(msgRes);
        sToastView.setDuration(Toast.LENGTH_LONG);
        sToastView.show();
    }

    public static void showShortToast(String msg) {
                CalmToastView sToastView = new CalmToastView(BaseApplication.sInstance);
        sToastView.setMessage(msg);
        sToastView.setDuration(Toast.LENGTH_SHORT);
        sToastView.show();
    }

    public static void showShortToast(String msg, int msgId) {
                CalmToastView sToastView = new CalmToastView(BaseApplication.sInstance);
        String message = BaseApplication.sInstance.getResources().getString(msgId);
        sToastView.setMessage(msg + message);
        sToastView.setDuration(Toast.LENGTH_SHORT);
        sToastView.show();
    }

    public static void showShortToast(int msgRes) {
                CalmToastView sToastView = new CalmToastView(BaseApplication.sInstance);
        sToastView.setMessage(ResourceUtils.getString(msgRes));
        sToastView.setDuration(Toast.LENGTH_SHORT);
        sToastView.show();
    }

    public static void showToast(String msg, int duration) {
                CalmToastView sToastView = new CalmToastView(BaseApplication.sInstance);
        sToastView.setMessage(msg);
        sToastView.setDuration(duration);
        sToastView.show();
    }

    public static void showToast(int msgRes, int duration) {
                CalmToastView sToastView = new CalmToastView(BaseApplication.sInstance);
        sToastView.setMessage(msgRes);
        sToastView.setDuration(duration);
        sToastView.show();
    }


    public static void showToastWidth(String msgRes) {
                CalmToastView sToastView = new CalmToastView(BaseApplication.sInstance);
        sToastView.setMessage(msgRes);
        sToastView.setDuration(Toast.LENGTH_SHORT);
        sToastView.setTextPadding(DensityUtil.dip2px(BaseApplication.sInstance, 15), DensityUtil.dip2px(BaseApplication.sInstance, 30), DensityUtil.dip2px(BaseApplication.sInstance, 15), DensityUtil.dip2px(BaseApplication.sInstance, 30));
        sToastView.show();
    }


        private static CalmToastView sToastView;

    public static synchronized void showLongToastCenter(Context context, String msg) {
        if (context != null) {
            if (sToastView == null) {
                sToastView = new CalmToastView(context.getApplicationContext());
            }
            sToastView.setGravity(Gravity.CENTER, 0, -45);
            sToastView.setMessage(msg);
            sToastView.setDuration(Toast.LENGTH_LONG);
            sToastView.show();
        }
    }
    }
