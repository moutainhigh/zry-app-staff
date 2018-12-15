package com.zhongmei.yunfu.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

public class BaseDialog extends Dialog {

    protected BaseDialog(Context context, boolean cancelable,
                         OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

    public BaseDialog(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (deelInputKeyboard()) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean deelInputKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(getWindow().getDecorView()
                .getWindowToken(), 0);
    }

}
