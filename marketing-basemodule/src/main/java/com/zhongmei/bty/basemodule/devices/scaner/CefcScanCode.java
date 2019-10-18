package com.zhongmei.bty.basemodule.devices.scaner;

import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.zhongmei.bty.commonmodule.util.manager.ClickManager;


public class CefcScanCode extends ScanCode {

    private EditText editText;


    public CefcScanCode(EditText editText, boolean isAllowInput) {
        this.editText = editText;
        if (!isAllowInput) {
            editText.setInputType(InputType.TYPE_NULL);
        }
    }

    @Override
    public void start() {
        editText.requestFocus();
        editText.setOnKeyListener(scanListener);
    }

    private View.OnKeyListener scanListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            Log.i(TAG, "keyCode: " + keyCode);
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (ClickManager.getInstance().isClicked()) {
                    setReceivedListenerResult(editText.getText().toString().trim());
                }
                return true;
            }
            return false;
        }
    };

    @Override
    public void stop() {
        editText.setOnKeyListener(null);
        editText.clearFocus();
    }
}
