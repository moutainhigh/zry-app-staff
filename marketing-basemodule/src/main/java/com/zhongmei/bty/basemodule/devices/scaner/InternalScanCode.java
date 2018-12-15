package com.zhongmei.bty.basemodule.devices.scaner;

import android.content.Context;

import com.zhongmei.bty.basemodule.devices.scaner.innerscanner.InnerScannerManager1;

/**
 * 内置扫码枪
 *
 * @created 2017/9/13
 */
public class InternalScanCode extends ScanCode implements ScanDataReceivedListener {

    private InnerScannerManager1 mScannerManager;

    public InternalScanCode(Context context) {
        mScannerManager = InnerScannerManager1.newInstance(context, this);
    }

    @Override
    public void start() {
        mScannerManager.start();
    }

    @Override
    public void stop() {
        mScannerManager.destory();
    }

    @Override
    public void onDataReceivedOver(String data) {
        setReceivedListenerResult(data);
    }
}
