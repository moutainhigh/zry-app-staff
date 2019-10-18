package com.zhongmei.bty.basemodule.devices.scaner.innerscanner;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;



public class InnerScannerTool implements IScannerManager {
    private static final String TAG = "InnerScannerTool";

    public static final String SCANNER_DATA_ACTION = "com.demo.onpos.innerscanner.data";

    public static final String SCANNER_DATA_Extra = "scannerdata";

    public static final int SCANNER_TYPE_HERD = 1;
    public static final int SCANNER_TYPE_SOFT = 2;
    private Context mContext;
        private long lastCallTime;
    private int scannerType = SCANNER_TYPE_HERD;    private InnerScannerListener mSurfaceHolderListener;
    public static InnerScannerTool newInstance(Context context, InnerScannerListener callback) {
        synchronized (InnerScannerTool.class) {
            InnerScannerTool scannerTool = new InnerScannerTool(context.getApplicationContext(), callback);
            return scannerTool;
        }
    }

    private InnerScannerTool() {

    }

    private InnerScannerTool(Context context, InnerScannerListener callback) {

    }

    public int getScannerType() {
        return scannerType;
    }

    public boolean isSoftwareScan() {
        return scannerType == SCANNER_TYPE_SOFT;
    }

    public void start() {

    }

        public void stop() {

    }

    public void destory() {

    }

        private void onReceived(String data) {
        Log.v(TAG, "onReceived:->data >" + data);
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCallTime > 5000) {
            if (!TextUtils.isEmpty(data)) {
                String contentData = data.replace("\r\n", "");
                sendDataByBroadcast(contentData);
            }
            lastCallTime = currentTime;
        }
        doRestartSoftwareScan();    }

    private SurfaceHolder getSurfaceHolder() {
        if (mSurfaceHolderListener != null)
            return mSurfaceHolderListener.getSurfaceHolder();
        return null;
    }

    private void doRestartSoftwareScan() {

    }

    private void sendDataByBroadcast(String data) {
        if (mContext != null) {
            Intent intent = new Intent(SCANNER_DATA_ACTION);
            intent.putExtra(SCANNER_DATA_Extra, data);
            mContext.sendBroadcast(intent);
        }
    }



    public interface InnerScannerListener {
        public SurfaceHolder getSurfaceHolder();
    }
}
