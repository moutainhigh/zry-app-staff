package com.zhongmei.bty.basemodule.devices.scaner.innerscanner;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.basemodule.devices.scaner.ScanDataReceivedListener;

/**
 * @Date：2015-10-17 上午9:04:29
 * @Description:集成内置扫码器管理器,该工具直接调用sdk 接口并返回，不涉及跨进程调用，与InnerScannerTool功能类似
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class InnerScannerManager1 implements IScannerManager {
    private static final String TAG = "InnerScannerManager1";
    private static final int SUCCESS = 1;
    private long lastCallTime;
    private ScanDataReceivedListener mCallback;
    //private ScannerDecode mScanerModulesManager;//SDK 管理对象


    public synchronized static InnerScannerManager1 newInstance(Context context, ScanDataReceivedListener listener) {
        InnerScannerManager1 scanner = new InnerScannerManager1(context.getApplicationContext(), listener);
        return scanner;
    }

    private InnerScannerManager1(Context context, ScanDataReceivedListener listener) {
        /*if (Product.isOnPosMachine()) {
            mCallback = listener;
            mScanerModulesManager = new ScannerDecode(context);
            *//*
            mScanerModulesManager.startScanCode(new ScanerManager.ScanerValueListener() {
                @Override
                public void onScanerValue(String value) {
                    long currentTime = System.currentTimeMillis();
                    if (mCallback != null && (currentTime - lastCallTime > 5000)) {
                        Log.v(TAG, "onScanerValue:->onDataReceived:" + value);
                        onReceived(value);
                        lastCallTime = currentTime;
                    }
                }
            });
            *//*
        }*/
    }

    // 开启监听
    public void start() {
        /*if (mScanerModulesManager != null) {
            //mScanerModulesManager.doScanTrigger();
            try {
                mScanerModulesManager.startScanCode(new ScannerDecode.ScannerValueListener() {
                    @Override
                    public void onScannerValue(String value) {
                        long currentTime = System.currentTimeMillis();
                        if (mCallback != null && (currentTime - lastCallTime > 5000)) {
                            Log.v(TAG, "onScanerValue:->onDataReceived:" + value);
                            onReceived(value);
                            lastCallTime = currentTime;
                        }
                        mScanerModulesManager.doScanTrigger();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    //停止监听
    public void stop() {
        /*if (mScanerModulesManager != null)
            mScanerModulesManager.stopScanCode();*/
    }

    public void destory() {
        /*this.stop();
        this.mScanerModulesManager = null;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }*/
    }

    //处理工作线程返回的数据
    private void onReceived(String data) {
        Log.v(TAG, "onReceived:->data >" + data);
        String contentData = data;
        if (!TextUtils.isEmpty(data)) {
            contentData = data.replace("\r\n", "");
        }
        Message ms = mHandler.obtainMessage();
        ms.what = SUCCESS;
        ms.obj = contentData;
        mHandler.sendMessage(ms);
    }

    //用于主线程回调UI注册接口
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            if (msg.what == SUCCESS) {
                if (mCallback != null) {
                    mCallback.onDataReceivedOver((String) msg.obj);
                }
            }
        }
    };

   /* public interface ScanDataReceivedListener {
        void onDataReceivedOver(String data);
    }*///modify 20180201
}