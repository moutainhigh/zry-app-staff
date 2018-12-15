package com.zhongmei.yunfu.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by demo on 2018/12/15
 */

public class ViewUtil {

    /**
     * 设置某个按钮选中
     *
     * @param viewGroup
     * @param button
     */
    public static void setButtonSelected(ViewGroup viewGroup, Button button) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            if (button != null && button.getId() == view.getId()) {
                view.setSelected(true);
            } else {
                view.setSelected(false);
            }
        }
    }

    /**
     * 设置按钮是否可用
     *
     * @param button
     * @param enabled
     */
    public static void setButtonEnabled(Button button, boolean enabled) {
        if (button != null) {
            button.setEnabled(enabled);
        }
    }

    /**
     * 设置按钮可见性
     *
     * @param view
     * @param visibility
     */
    public static void setViewVisibility(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
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

    /**
     * 隐藏软键盘
     *
     * @param text
     */
    public static void showSoftKeyboard(TextView text) {
        InputMethodManager imm = (InputMethodManager) text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
