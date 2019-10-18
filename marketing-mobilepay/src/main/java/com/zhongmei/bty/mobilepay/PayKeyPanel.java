package com.zhongmei.bty.mobilepay;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.zhongmei.yunfu.util.ViewUtil;
import com.zhongmei.yunfu.mobilepay.R;

public class PayKeyPanel implements View.OnClickListener {

    private EditText mShowValue;

    private boolean isDefaultValue = false;
    private boolean isEnabled = true;
    private TextView mOne;
    private TextView mTwo;
    private TextView mThree;
    private TextView mFour;
    private TextView mFive;
    private TextView mSix;
    private TextView mSeven;
    private TextView mEight;
    private TextView mNine;
    private View mDot;
    private TextView mZero;
    private View mDelete;
    private View mClear;

    private void assignViews(View view) {
        mOne = (TextView) view.findViewById(R.id.one);
        mTwo = (TextView) view.findViewById(R.id.two);
        mThree = (TextView) view.findViewById(R.id.three);
        mFour = (TextView) view.findViewById(R.id.four);
        mFive = (TextView) view.findViewById(R.id.five);
        mSix = (TextView) view.findViewById(R.id.six);
        mSeven = (TextView) view.findViewById(R.id.seven);
        mEight = (TextView) view.findViewById(R.id.eight);
        mNine = (TextView) view.findViewById(R.id.nine);
        mDot = view.findViewById(R.id.dot);
        mZero = (TextView) view.findViewById(R.id.zero);
        mDelete = view.findViewById(R.id.delete);
        mClear = view.findViewById(R.id.clean);
    }

    public PayKeyPanel(View view) {
        assignViews(view);
        mOne.setOnClickListener(this);
        mTwo.setOnClickListener(this);
        mThree.setOnClickListener(this);
        mFour.setOnClickListener(this);
        mFive.setOnClickListener(this);
        mSix.setOnClickListener(this);
        mSeven.setOnClickListener(this);
        mEight.setOnClickListener(this);
        mNine.setOnClickListener(this);
        if (mZero != null)
            mZero.setOnClickListener(this);

        if (mDot != null)
            mDot.setOnClickListener(this);

        if (mDelete != null)
            mDelete.setOnClickListener(this);

        if (mClear != null)
            mClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mShowValue != null && isEnabled) {
            int vId = v.getId();
            if (vId == R.id.one) {
                clearDefaultValue();
                mShowValue.append("1");

            } else if (vId == R.id.two) {
                clearDefaultValue();
                mShowValue.append("2");

            } else if (vId == R.id.three) {
                clearDefaultValue();
                mShowValue.append("3");

            } else if (vId == R.id.four) {
                clearDefaultValue();
                mShowValue.append("4");

            } else if (vId == R.id.five) {
                clearDefaultValue();
                mShowValue.append("5");

            } else if (vId == R.id.six) {
                clearDefaultValue();
                mShowValue.append("6");

            } else if (vId == R.id.seven) {
                clearDefaultValue();
                mShowValue.append("7");

            } else if (vId == R.id.eight) {
                clearDefaultValue();
                mShowValue.append("8");

            } else if (vId == R.id.nine) {
                clearDefaultValue();
                mShowValue.append("9");

            } else if (vId == R.id.zero) {
                clearDefaultValue();
                mShowValue.append("0");

            } else if (vId == R.id.dot) {
                clearDefaultValue();
                mShowValue.append(".");

            } else if (vId == R.id.delete) {
                if (!TextUtils.isEmpty(mShowValue.getText())) {
                    mShowValue.setText(mShowValue.getText().toString().subSequence(0, mShowValue.length() - 1));
                    mShowValue.setSelection(mShowValue.length());
                    isDefaultValue = false;
                }
            } else if (vId == R.id.clean) {
                mShowValue.setText("");
            }
        }
    }


    private void clearDefaultValue() {
        if (isDefaultValue) {
            mShowValue.setText("");
            isDefaultValue = false;
        }
    }

    public String getValue() {
        if (mShowValue != null) {
            return mShowValue.getText().toString();
        }
        return "";
    }


    public void setCurrentInput(EditText input) {
        if (input != null) {
            input.requestFocus();
            ViewUtil.hiddenSoftKeyboard(input);
            mShowValue = input;
        }
    }

    public boolean isDefaultValue() {
        return isDefaultValue;
    }

    public void setDefaultValue(boolean isDefaultValue) {
        this.isDefaultValue = isDefaultValue;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
}
