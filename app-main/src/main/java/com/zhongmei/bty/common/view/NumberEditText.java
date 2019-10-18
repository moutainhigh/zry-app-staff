package com.zhongmei.bty.common.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.zhongmei.yunfu.R;

import java.math.BigDecimal;


public class NumberEditText extends LinearLayout implements TextWatcher, View.OnClickListener {

        private Context mContext;
    private BigDecimal mValue = BigDecimal.ZERO;
    private BigDecimal mInCreaseUnit = BigDecimal.ONE;    private BigDecimal mOffset = BigDecimal.ONE;    private ChangeListener mChangeListener;

    private ImageButton mIbDecrease;
    private EditText mEtCount;
    private ImageButton mIbIncrease;

    public void setChangeListener(ChangeListener changeListener) {
        this.mChangeListener = changeListener;
    }

    public void setValue(BigDecimal defaultValue) {
        mValue = defaultValue;
        if (mEtCount != null) mEtCount.setText(mValue + "");
    }

    public void setInCreaseUnitOffset(BigDecimal inCreaseUnit, BigDecimal offset) {
        this.mInCreaseUnit = inCreaseUnit;
        this.mOffset = offset;
        this.setBtnStatus();
    }

    public BigDecimal getValue() {
        return mValue;
    }

    private void assignViews() {
        mIbDecrease = (ImageButton) findViewById(R.id.ib_decrease);
        mEtCount = (EditText) findViewById(R.id.et_count);
        mIbIncrease = (ImageButton) findViewById(R.id.ib_increase);
    }


    public NumberEditText(Context context) {
        super(context);
        init(context);
    }

    public NumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_number_edit, this, true);
        assignViews();

        mIbDecrease.setOnClickListener(this);
        mIbIncrease.setOnClickListener(this);
                setBtnStatus();

    }

    private void setBtnStatus() {
                if (mInCreaseUnit.compareTo(mValue) >= 0) {
            mIbIncrease.setEnabled(true);
            mIbDecrease.setEnabled(false);
        } else {
            mIbIncrease.setEnabled(true);
            mIbDecrease.setEnabled(true);
        }
    }


    public void setBtnBg(int decreseBgId, int increaseBgId) {
        mIbDecrease.setBackgroundResource(decreseBgId);
        mIbIncrease.setBackgroundResource(increaseBgId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_decrease:
                                if (mInCreaseUnit.compareTo(mValue) < 0) {
                    mValue = mValue.subtract(mOffset);
                                        if (mChangeListener != null) {
                        mChangeListener.onNumberChanged(mValue);
                    }
                }
                setBtnStatus();
                break;
            case R.id.ib_increase:
                mValue = mValue.add(mOffset);
                                if (mChangeListener != null) {
                    mChangeListener.onNumberChanged(mValue);
                }
                setBtnStatus();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s)) {
            s = "0";
        }



        BigDecimal curCount = new BigDecimal(s.toString());
        if (curCount.compareTo(mValue) == 0) {            return;
        }
        mValue = curCount;
        setBtnStatus();
                if (mChangeListener != null) {
            mChangeListener.onNumberChanged(mValue);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public interface ChangeListener {
        void onNumberChanged(BigDecimal number);
    }
}
