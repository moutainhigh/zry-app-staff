package com.zhongmei.bty.common.util;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EBean;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.input.NumberKeyBoardUtils;

/**
 * @date 2015年7月23日下午5:36:44
 */

@EBean
public class CommonKeyBorad {

    private EditText mShowValue;

    private boolean isEnabled = true;// 键盘是否可用

    private String defaultSuffix = "";

    private int maxLength;

    @Click({R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine,
            R.id.zero, R.id.delete, R.id.clean, R.id.dot})
    public void onClick(View v) {
        if (mShowValue != null && isEnabled) {
            switch (v.getId()) {
                case R.id.one:
                    insert("1");
                    break;
                case R.id.two:
                    insert("2");
                    break;
                case R.id.three:
                    insert("3");
                    break;
                case R.id.four:
                    insert("4");
                    break;
                case R.id.five:
                    insert("5");
                    break;
                case R.id.six:
                    insert("6");
                    break;
                case R.id.seven:
                    insert("7");
                    break;
                case R.id.eight:
                    insert("8");
                    break;
                case R.id.nine:
                    insert("9");
                    break;
                case R.id.zero:
                    insert("0");
                    break;
                case R.id.dot:
                    insert(".");
                    break;
                case R.id.delete:
                    delte();
                    break;
                case R.id.clean:
                    mShowValue.setText("");
                    break;
                default:
                    break;
            }
        }
    }

    private void insert1(String num) {
        String src = mShowValue.getText().toString().trim();
        if (TextUtils.isEmpty(src)) {
            mShowValue.append(num);
            mShowValue.append(defaultSuffix);
        } else {
            mShowValue.append(num);
        }
        mShowValue.setSelection(mShowValue.getText().length() - defaultSuffix.length());
    }

    public void insert(String num) {
        if (mShowValue == null) {
            return;
        }
        int index = mShowValue.getSelectionStart();
        String src = mShowValue.getText().toString().trim();
        if (defaultSuffix.length() >= 1 && ((src.length() + 1) > maxLength)) {
            return;
        }
        if (src.length() == 0 && defaultSuffix.length() >= 1) {
            mShowValue.setText(num + defaultSuffix);
        } else {
            StringBuffer s = new StringBuffer(src);
            if (index - defaultSuffix.length() < 0) {
                index = mShowValue.length() - defaultSuffix.length();
            } else if (index == mShowValue.length() && s.toString().contains(defaultSuffix)) {
                index = index - defaultSuffix.length();
            }
            if (index > s.length()) {
                return;
            }
            s.insert(index, num);
            if (!s.toString().contains(defaultSuffix)) {
                s.append(defaultSuffix);
            }
            mShowValue.setText(s.toString());
        }

        mShowValue.setSelection(mShowValue.length() - defaultSuffix.length());
        // modify by dzb indexOutOfBound Exception.
//		if(index == mShowValue.length() - defaultSuffix.length()){
//			mShowValue.setSelection(mShowValue.length() - defaultSuffix.length());
//		}else{
//			mShowValue.setSelection(index+1);
//		}

    }

    public void delte() {
        if (mShowValue == null) {
            return;
        }
        int selectPosition = getSelctedPosition();
        if (selectPosition < 0) {
            return;
        }
        int keyCode = KeyEvent.KEYCODE_DEL;
        KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, keyCode);

        String src = mShowValue.getText().toString().trim();
        if (src.length() == defaultSuffix.length() + 1) {
            //如果是最后一个数字 删两次
            mShowValue.setText("");
//			mShowValue.setSelection(defaultSuffix.length()+1);
//			mShowValue.onKeyDown(keyCode, keyEventDown); // editNumber
//			mShowValue.onKeyUp(keyCode, keyEventUp);
        }
        mShowValue.onKeyDown(keyCode, keyEventDown); // editNumber
        mShowValue.onKeyUp(keyCode, keyEventUp);
    }

    private int getSelctedPosition() {
        int selectPosition = mShowValue.getText().length();
        if (selectPosition < 0) {
            selectPosition = 0;
        }
        selectPosition -= defaultSuffix.length();
        return selectPosition;
    }

    public String getValue() {
        if (mShowValue != null) {
            return mShowValue.getText().toString();
        }
        return "";
    }

    /**
     * 设置当前编辑栏
     *
     * @param input
     */
    public void setCurrentInput(EditText input) {
        if (input != null) {
            input.requestFocus();
            NumberKeyBoardUtils.hiddenSoftKeyboard(input);
        }
        mShowValue = input;
    }

    public void setMax(int maxLength) {
        this.maxLength = maxLength;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * 数字后面的默认值显示
     *
     * @param defaultSuffix
     */
    public void setDefaultSuffix(String defaultSuffix) {
        this.defaultSuffix = defaultSuffix;
    }

}
