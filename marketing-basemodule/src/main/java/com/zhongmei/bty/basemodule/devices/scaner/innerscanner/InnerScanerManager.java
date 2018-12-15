package com.zhongmei.bty.basemodule.devices.scaner.innerscanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.devices.scaner.ScanDataReceivedListener;

/**
 * Created by demo on 2018/12/15
 * 新集成内置扫码器管理器,该工具是跨进程调用，通过广播接收扫描数据（ 目前仅限在线支付扫描调用）
 */

public class InnerScanerManager implements IScannerManager {
    private static final String TAG = InnerScanerManager.class.getSimpleName();
    private Context mContext;
    private ScannerDataReceiver mScannerDataReceiver;
    private boolean isStart = false;

    public synchronized static InnerScanerManager newInstance(Context context, ScanDataReceivedListener listener) {
        InnerScanerManager scanner = new InnerScanerManager(context.getApplicationContext(), listener);
        return scanner;
    }

    private InnerScanerManager(Context context, ScanDataReceivedListener listener) {
        this.mContext = context;
        this.mScannerDataReceiver = new ScannerDataReceiver(listener);
    }

    public void start() {
        if (!isStart) {
            DisplayServiceManager.startInnerScanner(this.mContext);
            registerReceiver();
            isStart = true;
        }
    }

    public void stop() {
        if (isStart) {
            mContext.unregisterReceiver(this.mScannerDataReceiver);
            DisplayServiceManager.stopInnerScanner(this.mContext);
            isStart = false;
        }
    }

    public void destory() {
        this.stop();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(InnerScannerTool.SCANNER_DATA_ACTION);
        mContext.registerReceiver(this.mScannerDataReceiver, intentFilter);
    }

    private final class ScannerDataReceiver extends BroadcastReceiver {
        private ScanDataReceivedListener mDataListener;

        public ScannerDataReceiver(ScanDataReceivedListener listener) {
            mDataListener = listener;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras_ = intent.getExtras();
            if (extras_ != null) {
                String data = extras_.getString(InnerScannerTool.SCANNER_DATA_Extra);
                if (!TextUtils.isEmpty(data) && mDataListener != null) {
                    Log.v(TAG, "onReceive:->data >" + data);
                    mDataListener.onDataReceivedOver(data);
                }
            }
        }
    }
}
