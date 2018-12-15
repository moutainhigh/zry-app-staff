package com.zhongmei.bty.basemodule.devices.scaner.innerscanner;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by demo on 2018/12/15
 * 新集成内置扫码器管理器,该工具是跨进程调用，通过广播发送扫描数据（ 目前仅限在线支付扫描调用）
 * 对sdk做的封装
 */

public class InnerScannerTool implements IScannerManager {
    private static final String TAG = "InnerScannerTool";

    public static final String SCANNER_DATA_ACTION = "com.demo.onpos.innerscanner.data";

    public static final String SCANNER_DATA_Extra = "scannerdata";

    public static final int SCANNER_TYPE_HERD = 1;//硬解码

    public static final int SCANNER_TYPE_SOFT = 2;//软解码

    private Context mContext;
    //private ScannerDecode mScannerDecode;//SDK 管理对象
    private long lastCallTime;
    private int scannerType = SCANNER_TYPE_HERD;//1硬解码，2软解码；
    private InnerScannerListener mSurfaceHolderListener;//获取surfaceHolder接口

    public static InnerScannerTool newInstance(Context context, InnerScannerListener callback) {
        synchronized (InnerScannerTool.class) {
            InnerScannerTool scannerTool = new InnerScannerTool(context.getApplicationContext(), callback);
            return scannerTool;
        }
    }

    private InnerScannerTool() {
        /*if (Product.isSupportSoftwareScanner())
            scannerType = SCANNER_TYPE_SOFT;
        else
            scannerType = SCANNER_TYPE_HERD;*/
    }

    private InnerScannerTool(Context context, InnerScannerListener callback) {
        /*this();
        if (Product.isOnPosMachine()) {
            mContext = context;
            mSurfaceHolderListener = callback;
            mScannerDecode = new ScannerDecode(context);
        }*/
    }

    public int getScannerType() {
        return scannerType;
    }

    public boolean isSoftwareScan() {
        return scannerType == SCANNER_TYPE_SOFT;
    }

    public void start() {
        /*if (mScannerDecode != null) {
            SurfaceHolder surfaceHolder = getSurfaceHolder();
            if (scannerType == SCANNER_TYPE_SOFT && surfaceHolder != null) {
                mScannerDecode.startSoftwareScanCode(mScannerValueListener, surfaceHolder);
            } else {
                mScannerDecode.startScanCode(mScannerValueListener);
            }
        }*/
    }

    //停止监听
    public void stop() {
        /*if (mScannerDecode != null)
            mScannerDecode.stopScanCode();*/
    }

    public void destory() {
        /*this.stop();
        this.mScannerDecode = null;*/
    }

    //处理返回的数据
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
        doRestartSoftwareScan();//modify v8.14 优化逻辑
    }

    private SurfaceHolder getSurfaceHolder() {
        if (mSurfaceHolderListener != null)
            return mSurfaceHolderListener.getSurfaceHolder();
        return null;
    }

    private void doRestartSoftwareScan() {
        /*if (mScannerDecode != null) {
            SurfaceHolder surfaceHolder = getSurfaceHolder();
            if (scannerType == SCANNER_TYPE_SOFT && surfaceHolder != null) {
                mScannerDecode.releaseSoftwareScanCode();
                mScannerDecode.startSoftwareScanCode(mScannerValueListener, surfaceHolder);
            } else {
                mScannerDecode.doScanTrigger();
            }
        }*/
    }

    private void sendDataByBroadcast(String data) {
        if (mContext != null) {
            Intent intent = new Intent(SCANNER_DATA_ACTION);
            intent.putExtra(SCANNER_DATA_Extra, data);
            mContext.sendBroadcast(intent);
        }
    }

    /*ScannerDecode.ScannerValueListener mScannerValueListener = new ScannerDecode.ScannerValueListener() {
        @Override
        public void onScannerValue(String value) {
            onReceived(value);
        }
    };*/

    public interface InnerScannerListener {
        public SurfaceHolder getSurfaceHolder();
    }
}
