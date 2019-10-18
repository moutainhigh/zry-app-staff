package com.zhongmei.bty.basemodule.devices.display.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;


public class DisplayServiceManager {
    private static final String TAG = DisplayServiceManager.class.getSimpleName();
        private static String mLanguage = "";

    public static void startService(Context context) {

    }


    public synchronized static void updateDisplay(Context context, final Parcelable object) {
            }



    public static void doCancel(Context context) {
            }

    public static void doDelayPause(Context context) {
            }


    public static void updateDisplayPay(Context context, Double amount) {

    }



















    public static void doUpdateQueue(Context context) {
        doUpdateQueue(context, false);
    }

    public static void doUpdateQueue(Context context, boolean isClear) {

    }

    public static void doShowQueueCallNo(Context context, String no, int command) {

    }



    public static void sendBootBroadCast(Context context) {

    }





    public static void doUpdateUnionPay(Context context, int command, String message, String payInfo) {

    }



    public static void doUpdateUnionPay(Context context, int command, String message, String payInfo, boolean isShowAnim) {

    }


    public static void doUpdateRecharge(Context context, int command, String rechargeAmount, String memberName) {

    }

    public static void doUpdateLoginInfo(Context context, int comm, String phone, Bitmap bitmap, boolean isRegister) {

    }



    public static void startInnerScanner(Context context) {

    }

    public static void stopInnerScanner(Context context) {

    }
    }
