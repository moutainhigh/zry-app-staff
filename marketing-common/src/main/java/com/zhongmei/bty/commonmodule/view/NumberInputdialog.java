package com.zhongmei.bty.commonmodule.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.zhongmei.yunfu.commonmodule.R;
import com.zhongmei.yunfu.util.ToastUtil;

import java.lang.ref.WeakReference;

public class NumberInputdialog extends Dialog implements OnClickListener {
    public static int NUMBER_TYPE_INT = 1;
    public static int NUMBER_TYPE_FLOAT = 2;
    private boolean isDefaultValue = false;    private InputOverListener mListener;
    private OnCancelListener onCancelListener;
    private double mMaxValue;        private EditTextWithDeleteIcon mShowValue;
    private String mContent;
    private int mNumberType = NUMBER_TYPE_FLOAT;
    private String mDefaultValue;    private TextView tvRemark;
    private NumberKeyBoard keyBoard;

    private Button mBtnOk;
    private boolean mAllowDefaultValue = false;

    private DotType mDotType = DotType.DOT;


    public enum DotType {
        ClEAR, DOT
    }

    public Button getBtnOk() {
        return mBtnOk;
    }

    public NumberInputdialog(Context context, int theme) {
        super(context, theme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.common_number_keyboard_layout);
    }

    public NumberInputdialog(Context context, int titleResId, int hintResId, String lastInput, double maxValue,
                             InputOverListener linster) {
        this(context, context.getString(titleResId), context.getString(hintResId), lastInput, maxValue, linster);
    }

    public NumberInputdialog(Context context, String title, String hint, String lastInput, double maxValue,
                             InputOverListener linster) {
        this(context, title, hint, lastInput, null, maxValue, linster);
    }

    public NumberInputdialog(Context context, String title, String hint, String lastInput, String desc, double maxValue,
                             InputOverListener linster) {
        this(context, R.style.custom_alert_dialog);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                this.mMaxValue = maxValue;
        this.mDefaultValue = lastInput;
        this.init(title, lastInput, hint, desc);
        setOnInputOverListener(linster);
    }

    public static NumberInputdialog show(Context context, double maxValue, String input, String inputHint, String title, InputOverListener listener) {
        NumberInputdialog numberDialog = new NumberInputdialog(context, title, inputHint, input, maxValue, listener);
        numberDialog.setNumberType(NumberInputdialog.NUMBER_TYPE_INT);
        numberDialog.show();
        return numberDialog;
    }

    public void setOnInputOverListener(InputOverListener listener) {
        this.mListener = listener;
        if (mListener instanceof SimpleInputOverListener) {
            ((SimpleInputOverListener) mListener).setDialog(this);
        }
    }

    public NumberInputdialog setRemark(String remark) {
        if (TextUtils.isEmpty(remark)) {
            tvRemark.setVisibility(View.GONE);
        } else {
            tvRemark.setVisibility(View.VISIBLE);
            tvRemark.setText(remark);
        }
        return this;
    }

    public NumberInputdialog setDotType(DotType type) {
        mDotType = type;
        if (mDotType == DotType.DOT) {
            keyBoard.setShowPoint();
        } else {
            keyBoard.setShowClean();
        }
        return this;
    }

    public NumberInputdialog setNumberType(int type) {
        mNumberType = type;
        if (keyBoard != null) {
            if (mNumberType == NUMBER_TYPE_INT) {
                keyBoard.setPointClickable(false);
            } else {
                keyBoard.setPointClickable(true);
            }
        }
        return this;
    }

    public void setAllowDefaultValue(boolean allowDefaultValue) {
        this.mAllowDefaultValue = allowDefaultValue;
    }

    private void init(String title, String lastInput, String hint, String desc) {
        initContentView(title, lastInput, hint, desc);

    }

    private void initContentView(String title, String lastInput, String hint, String desc) {
        findViewById(R.id.btn_close).setOnClickListener(this);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mBtnOk.setOnClickListener(this);
        tvRemark = (TextView) findViewById(R.id.tv_remark);
        mShowValue = (EditTextWithDeleteIcon) findViewById(R.id.et_input);
        keyBoard = (NumberKeyBoard) findViewById(R.id.keyboard);
        keyBoard.setShowPoint();
        keyBoard.setListener(listener);
                if (mNumberType == NUMBER_TYPE_INT) {
            keyBoard.setPointClickable(false);
        }
        mShowValue.setHint(hint);
        if (!TextUtils.isEmpty(lastInput)) {
            mContent = lastInput;
            mShowValue.setText(lastInput);
            mShowValue.setSelection(mContent.length());
            isDefaultValue = true;
        }
        if (!TextUtils.isEmpty(desc)) {
            tvRemark.setText(desc);
            tvRemark.setVisibility(View.VISIBLE);
        }

        TextView titleTV = (TextView) findViewById(R.id.tv_title);
        titleTV.setText(title);
        mShowValue.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                arg0.requestFocus();
                mShowValue.onTouchEvent(arg1);
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
                    if (!isMatchNumberFormat(mContent)) {
                        ToastUtil.showShortToast(R.string.input_format_wrong);
                        mContent = mContent.substring(0, mContent.length() - 1);
                        ismodify = true;
                    } else {
                        if (Double.valueOf(mContent) > mMaxValue) {
                            ToastUtil.showShortToast(R.string.input_amount_too_much);
                            mContent = mContent.substring(0, mContent.length() - 1);
                            ismodify = true;
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

    public void setOnCancelListener(OnCancelListener listener) {
        this.onCancelListener = listener;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btn_close) {
            this.dismiss();
            if (onCancelListener != null) {
                onCancelListener.onCancel(this);
            }
        } else if (viewId == R.id.btn_ok) {
            if (!TextUtils.isEmpty(mContent)) {
                if (mListener != null && (!isEqualsDefault(mContent) || mAllowDefaultValue)) {
                    if (mNumberType == NUMBER_TYPE_INT && mContent.startsWith("0")) {
                        if (mContent.contains(".")) {
                            mContent = mContent.replace(".", "");
                        }
                        mContent = Long.valueOf(mContent).toString();
                    }
                    mListener.afterInputOver(mContent);
                }
            } else {
                if (mListener != null)
                    mListener.afterInputOver("0");
            }

            if (mListener instanceof SimpleInputOverListener) {
                if (((SimpleInputOverListener) mListener).isDismiss()) {
                    this.dismiss();
                }
            } else {
                this.dismiss();
            }
        }
    }

    NumberKeyBoard.NumberClickListener listener = new NumberKeyBoard.NumberClickListener() {
        @Override
        public void numberClicked(String number) {
            clearDefaultValue();
            mShowValue.append(number);
        }

        @Override
        public void clearClicked() {
            if (mShowValue != null && !TextUtils.isEmpty(mShowValue.getText())) {
                mShowValue.setText("");
                isDefaultValue = false;
            }
        }

        @Override
        public void deleteClicked() {
            if (mShowValue != null && !TextUtils.isEmpty(mShowValue.getText())) {
                mShowValue.setText(mShowValue.getText().toString().subSequence(0, mShowValue.length() - 1));
                mShowValue.setSelection(mShowValue.length());
                isDefaultValue = false;
            }
        }
    };
    public static final String CASH_FORMAT_REG = "([0-9]{1,8}[.][0-9]{0,2})|([0-9]{1,8})";
    public static boolean isMatchNumberFormat(String value) {
        if (value == null) {
            return false;
        }
        return value.matches(CASH_FORMAT_REG);
    }

    public interface InputOverListener {
        void afterInputOver(String inputContent);
    }

    public abstract static class SimpleInputOverListener implements InputOverListener {

        WeakReference<Dialog> dialogWeakReference;

        public void setDialog(Dialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        public boolean isDismiss() {
            return true;
        }

        @Override
        public void afterInputOver(String inputContent) {
            Dialog dialog = dialogWeakReference.get();
            if (dialog != null) {
                afterInputOver(dialog, inputContent);
            }
        }

        public abstract void afterInputOver(Dialog dialog, String inputContent);

    }
}
