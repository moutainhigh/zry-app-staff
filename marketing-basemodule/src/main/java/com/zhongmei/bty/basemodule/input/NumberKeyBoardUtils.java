package com.zhongmei.bty.basemodule.input;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by demo on 2018/12/15
 */

public class NumberKeyBoardUtils {

    public static void setTouchListener(final EditText input) {
        input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocus();
                if (input.getText() != null) {
                    input.setSelection(input.getText().length());
                }
                return true;
            }
        });
    }

    /**
     * 显示软键盘
     *
     * @param text
     */
    public static void showSoftKeyboard(TextView text) {
        InputMethodManager imm = (InputMethodManager) text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏软键盘
     *
     * @param text
     */
    public static void hiddenSoftKeyboard(TextView text) {
        InputMethodManager imm = (InputMethodManager) text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
    }

}
