package com.zhongmei.yunfu.push;

import android.app.Application;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;


public class JPushApplication extends Application {
    private static final String TAG = "JIGUANG-Example";

    @Override
    public void onCreate() {
        Log.d(TAG, "[ExampleApplication] onCreate");
        super.onCreate();

        JPushInterface.setDebugMode(true);            JPushInterface.init(this);                }
}
