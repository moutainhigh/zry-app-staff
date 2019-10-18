package com.zhongmei.yunfu.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;



public class ViewUtil {


    public static void setButtonSelected(ViewGroup viewGroup, Button button) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            if(view instanceof ViewGroup){
                setButtonSelected((ViewGroup) view,button);
            }else{
                if (button != null && button.getId() == view.getId()) {
                    view.setSelected(true);
                } else {
                    view.setSelected(false);
                }
            }
        }
    }


    public static void setButtonEnabled(Button button, boolean enabled) {
        if (button != null) {
            button.setEnabled(enabled);
        }
    }


    public static void setViewVisibility(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }


    public static void hiddenSoftKeyboard(TextView text) {
        InputMethodManager imm = (InputMethodManager) text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
    }


    public static void showSoftKeyboard(TextView text) {
        InputMethodManager imm = (InputMethodManager) text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
