package com.zhongmei.yunfu.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.ui.view.CalmToastView;

public class ToastUtil {

    //private static CalmToastView sToastView;

   /* public static void init(Context context) {
        sToastView = new CalmToastView(MainApplication.getInstance());
    }*///modify yutang 20170207 无用的代码


    //public static void init() {
    //if (sToastView == null) {
    //      sToastView = new CalmToastView(MainApplication.getInstance());
    //}
    //}

    public static void showLongToast(String msg) {
        //init();
        CalmToastView sToastView = new CalmToastView(BaseApplication.sInstance);
        sToastView.setMessage(msg);
        sToastView.setDuration(Toast.LENGTH_LONG);
        sToastView.show();
    }

    public static void showLongToast(String msg, int msgId) {
        //init();
        CalmToastView sToastView = new CalmToastView(BaseApplication.sInstance);
        String message = BaseApplication.sInstance.getResources().getString(msgId);
        sToastView.setMessage(msg + message);
        sToastView.setDuration(Toast.LENGTH_LONG);
        sToastView.show();
    }


    public static void showLongToast(int msgRes) {
        //init();
        CalmToastView sToastView = new CalmToastView(BaseApplication.sInstance);
        sToastView.setMessage(msgRes);
        sToastView.setDuration(Toast.LENGTH_LONG);
        sToastView.show();
    }

    public static void showShortToast(String msg) {
        //init();
        CalmToastView sToastView = new CalmToastView(BaseApplication.sInstance);
        sToastView.setMessage(msg);
        sToastView.setDuration(Toast.LENGTH_SHORT);
        sToastView.show();
    }

    public static void showShortToast(String msg, int msgId) {
        //init();
        CalmToastView sToastView = new CalmToastView(BaseApplication.sInstance);
        String message = BaseApplication.sInstance.getResources().getString(msgId);
        sToastView.setMessage(msg + message);
        sToastView.setDuration(Toast.LENGTH_SHORT);
        sToastView.show();
    }

    public static void showShortToast(int msgRes) {
        //init();
        CalmToastView sToastView = new CalmToastView(BaseApplication.sInstance);
        sToastView.setMessage(ResourceUtils.getString(msgRes));
        sToastView.setDuration(Toast.LENGTH_SHORT);
        sToastView.show();
    }

    public static void showToast(String msg, int duration) {
        //init();
        CalmToastView sToastView = new CalmToastView(BaseApplication.sInstance);
        sToastView.setMessage(msg);
        sToastView.setDuration(duration);
        sToastView.show();
    }

    public static void showToast(int msgRes, int duration) {
        //init();
        CalmToastView sToastView = new CalmToastView(BaseApplication.sInstance);
        sToastView.setMessage(msgRes);
        sToastView.setDuration(duration);
        sToastView.show();
    }

    /**
     * 宽体Toast
     *
     * @param msgRes
     */
    public static void showToastWidth(String msgRes) {
        //init();
        CalmToastView sToastView = new CalmToastView(BaseApplication.sInstance);
        sToastView.setMessage(msgRes);
        sToastView.setDuration(Toast.LENGTH_SHORT);
        sToastView.setTextPadding(DensityUtil.dip2px(BaseApplication.sInstance, 15), DensityUtil.dip2px(BaseApplication.sInstance, 30), DensityUtil.dip2px(BaseApplication.sInstance, 15), DensityUtil.dip2px(BaseApplication.sInstance, 30));
        sToastView.show();
    }

    /*
    public static void clearToast() {
        if (sToastView != null) {
            sToastView.cancel();
        }
    }
    */
    //add  new  toastfunction v8.8 start
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
    //add  new  toastfunction v8.8 start
}
