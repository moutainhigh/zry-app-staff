package com.zhongmei.bty.common.view;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;


public class RefundNumberInputdialog extends Dialog implements OnClickListener {
    public static int NUMBER_TYPE_INT = 1;
    public static int NUMBER_TYPE_FLOAT = 2;
    private boolean isDefaultValue = false;    private InputOverListener mListener;
    private double mMaxValue;        private EditText mShowValue;
    private ImageView mDotIV;
    private Button btnOK;
    private String mContent;
    private int mNumberType = NUMBER_TYPE_FLOAT;
    private String mDefaultValue;
    public RefundNumberInputdialog(Context context, int theme) {
        super(context, theme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.common_dialog_refund);
    }

    public RefundNumberInputdialog(Context context, String title, String hint, String lastInput, double maxValue,
                                   InputOverListener linster) {
        this(context, R.style.custom_alert_dialog);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        this.mListener = linster;
        this.mMaxValue = maxValue;
        this.mDefaultValue = lastInput;
        this.init(title, lastInput, hint);
    }

    public static RefundNumberInputdialog show(Context context, String title, String input, String inputHint, double maxValue, InputOverListener listener) {
        RefundNumberInputdialog numberDialog = new RefundNumberInputdialog(context, title, inputHint, input, maxValue, listener);
        numberDialog.setNumberType(RefundNumberInputdialog.NUMBER_TYPE_FLOAT);
        numberDialog.show();
        return numberDialog;
    }

    public RefundNumberInputdialog setNumberType(int type) {
        mNumberType = type;
        if (mNumberType == NUMBER_TYPE_INT) {
            mDotIV.setEnabled(false);
        } else {
            mDotIV.setEnabled(true);
        }
        return this;
    }

    private void init(String title, String lastInput, String hint) {
        initContentView(title, lastInput, hint);

    }

    private void initContentView(String title, String lastInput, String hint) {
        mDotIV = (ImageView) findViewById(R.id.dot);
        findViewById(R.id.one).setOnClickListener(this);
        findViewById(R.id.two).setOnClickListener(this);
        findViewById(R.id.three).setOnClickListener(this);
        findViewById(R.id.four).setOnClickListener(this);
        findViewById(R.id.five).setOnClickListener(this);
        findViewById(R.id.six).setOnClickListener(this);
        findViewById(R.id.seven).setOnClickListener(this);
        findViewById(R.id.eight).setOnClickListener(this);
        findViewById(R.id.nine).setOnClickListener(this);
        findViewById(R.id.dot).setOnClickListener(this);
        findViewById(R.id.zero).setOnClickListener(this);
        findViewById(R.id.delete).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
        btnOK = (Button) findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(this);
        mShowValue = (EditText) findViewById(R.id.et_input);
                if (mNumberType == NUMBER_TYPE_INT) {
            mDotIV.setEnabled(false);
        }
        mShowValue.setHint(hint);
        if (!TextUtils.isEmpty(lastInput)) {
            mContent = lastInput;
            mShowValue.setText(lastInput);
            mShowValue.setSelection(mContent.length());
            isDefaultValue = true;
        }

        TextView titleTV = (TextView) findViewById(R.id.tv_title);
        titleTV.setText(title);
        mShowValue.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                arg0.requestFocus();
                return true;
            }
        });
        mShowValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mContent = s.toString();
                boolean ismodify = false;
                if (!TextUtils.isEmpty(mContent)) {
                    mShowValue.setSelection(mContent.length());
                    if (mContent.startsWith(".")) {
                        mContent = "0" + mContent;
                        ismodify = true;
                    }
                    if (!CashInfoManager.isMatchCashFormat(mContent)) {
                                                btnOK.setEnabled(false);
                        mContent = mContent.substring(0, mContent.length() - 1);
                        ismodify = true;
                    } else {
                        if (Double.valueOf(mContent) > mMaxValue) {
                                                        btnOK.setEnabled(false);
                            mContent = mContent.substring(0, mContent.length() - 1);
                            ismodify = true;
                        } else {
                            if (Double.valueOf(mContent) == 0) {
                                btnOK.setEnabled(false);
                            } else {
                                btnOK.setEnabled(true);
                            }
                        }
                    }
                    if (ismodify == true) {
                        mShowValue.setText(mContent);
                    }
                }
            }
        });
    }


    private void clearDefaultValue() {
        if (isDefaultValue) {
            mShowValue.setText("");
            isDefaultValue = false;
        }
    }

    private boolean isEqualsDefault(String content) {
        if (content == mDefaultValue || (content != null && content.equals(mDefaultValue))) {
            return true;
        }
        if (!TextUtils.isEmpty(mDefaultValue) && !TextUtils.isEmpty(content)) {
            Double v1 = Double.valueOf(mDefaultValue);
            Double v2 = Double.valueOf(content);
            return v1.equals(v2);
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.btn_close:                this.dismiss();
                break;
            case R.id.btn_ok:                if (!TextUtils.isEmpty(mContent)) {
                    if (mListener != null ) {
                        if (mNumberType == NUMBER_TYPE_INT && mContent.startsWith("0")) {
                            mContent = Long.valueOf(mContent).toString();
                        }
                        mListener.afterInputOver(mContent);
                    }
                } else {
                    if (mListener != null)
                        mListener.afterInputOver("0");
                }
                this.dismiss();
                break;
            case R.id.one:
                clearDefaultValue();
                mShowValue.append("1");
                break;
            case R.id.two:
                clearDefaultValue();
                mShowValue.append("2");
                break;
            case R.id.three:
                clearDefaultValue();
                mShowValue.append("3");
                break;
            case R.id.four:
                clearDefaultValue();
                mShowValue.append("4");
                break;
            case R.id.five:
                clearDefaultValue();
                mShowValue.append("5");
                break;
            case R.id.six:
                clearDefaultValue();
                mShowValue.append("6");
                break;
            case R.id.seven:
                clearDefaultValue();
                mShowValue.append("7");
                break;
            case R.id.eight:
                clearDefaultValue();
                mShowValue.append("8");
                break;
            case R.id.nine:
                clearDefaultValue();
                mShowValue.append("9");
                break;
            case R.id.zero:
                clearDefaultValue();
                mShowValue.append("0");
                break;
            case R.id.dot:
                clearDefaultValue();
                mShowValue.append(".");
                break;
            case R.id.delete:
                if (mShowValue != null && !TextUtils.isEmpty(mShowValue.getText())) {
                    mShowValue.setText(mShowValue.getText().toString().subSequence(0, mShowValue.length() - 1));
                    mShowValue.setSelection(mShowValue.length());
                    isDefaultValue = false;
                }
                break;
        }
    }

    public interface InputOverListener {
        public void afterInputOver(String inputContent);
    }
}
