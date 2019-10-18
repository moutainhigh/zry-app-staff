package com.zhongmei.bty.basemodule.devices.scaner;

import android.content.Context;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;


public class ScanCodeManager extends ScanCode {

    List<ScanCode> scanCodeList = new ArrayList<>();

    public ScanCodeManager(Context context, EditText editText, boolean isAllowInput) {
        scanCodeList.add(new InternalScanCode(context));
        scanCodeList.add(new CefcScanCode(editText, isAllowInput));
            }

    @Override
    public void start() {
        for (ScanCode scanCode : scanCodeList) {
            scanCode.setOnScanCodeReceivedListener(scanCodeReceivedListener);
            scanCode.start();
        }
    }

    @Override
    public void stop() {
        for (ScanCode scanCode : scanCodeList) {
            scanCode.stop();
        }
    }
}
