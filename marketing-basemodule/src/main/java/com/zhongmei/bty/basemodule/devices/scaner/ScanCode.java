package com.zhongmei.bty.basemodule.devices.scaner;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

/**
 * Desc
 *
 * @created 2017/9/13
 */
public abstract class ScanCode {

    protected static final String TAG = ScanCode.class.getSimpleName();
    protected Handler handler = new Handler(Looper.getMainLooper());
    protected ScanCodeReceivedListener scanCodeReceivedListener;

    public void start(ScanCodeReceivedListener listener) {
        this.scanCodeReceivedListener = listener;
        start();
    }

    public abstract void start();

    public abstract void stop();

    protected void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    public interface ScanCodeReceivedListener {
        void onScanCodeReceived(String data);
    }

    public void setOnScanCodeReceivedListener(ScanCodeReceivedListener listener) {
        this.scanCodeReceivedListener = listener;
    }

    protected void setReceivedListenerResult(final String data) {
        Log.i(TAG, "data: " + data);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(data) && scanCodeReceivedListener != null) {
                    scanCodeReceivedListener.onScanCodeReceived(data);
                }
            }
        });
    }
}
