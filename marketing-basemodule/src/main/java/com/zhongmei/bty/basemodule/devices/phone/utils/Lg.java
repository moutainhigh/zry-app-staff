package com.zhongmei.bty.basemodule.devices.phone.utils;

import android.util.Log;

/**
 * 简单的Log工具, 仅用于Debug
 *
 * @date 2014-8-5
 */
public class Lg {

    public static boolean DEBUG = true;
    public static String DEFAULT_TAG = "phone_test";


    public static void d() {
        if (DEBUG) {
            Log.d(DEFAULT_TAG, "just test");
        }
    }

    public static void d(String log) {
        if (DEBUG) {
            Log.d(DEFAULT_TAG, log);
        }
    }

    public static void d(Object log) {
        d(log.toString());
    }

    public static void d(String tag, String log) {
        if (DEBUG) {
            Log.d(tag, log);
        }
    }


}
