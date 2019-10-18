package com.zhongmei.bty.basemodule.input;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;



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


    public static void showSoftKeyboard(TextView text) {
        InputMethodManager imm = (InputMethodManager) text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }


    public static void hiddenSoftKeyboard(TextView text) {
        InputMethodManager imm = (InputMethodManager) text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
    }

}
