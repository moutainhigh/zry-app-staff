package com.zhongmei.bty.basemodule.devices.scaner.innerscanner;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.basemodule.devices.scaner.ScanDataReceivedListener;


public class InnerScannerManager1 implements IScannerManager {
    private static final String TAG = "InnerScannerManager1";
    private static final int SUCCESS = 1;
    private long lastCallTime;
    private ScanDataReceivedListener mCallback;


    public synchronized static InnerScannerManager1 newInstance(Context context, ScanDataReceivedListener listener) {
        InnerScannerManager1 scanner = new InnerScannerManager1(context.getApplicationContext(), listener);
        return scanner;
    }

    private InnerScannerManager1(Context context, ScanDataReceivedListener listener) {

    }

        public void start() {

    }

        public void stop() {

    }

    public void destory() {

    }

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

        private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            if (msg.what == SUCCESS) {
                if (mCallback != null) {
                    mCallback.onDataReceivedOver((String) msg.obj);
                }
            }
        }
    };

   }