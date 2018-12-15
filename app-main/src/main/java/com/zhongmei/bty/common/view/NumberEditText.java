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

/**
 * @Date： 17/8/22
 * @Description:
 * @Version: 1.0
 */
public class NumberEditText extends LinearLayout implements TextWatcher, View.OnClickListener {

    //private final String matchstr="^[0-9]{1,3}?$"; //正则表达式验证，只允许输入三位数字
    private Context mContext;
    private BigDecimal mValue = BigDecimal.ZERO;
    private BigDecimal mInCreaseUnit = BigDecimal.ONE;//默认启卖分数
    private BigDecimal mOffset = BigDecimal.ONE;//默认曾量
    private ChangeListener mChangeListener;

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
        // mEtCount.addTextChangedListener(this);
        setBtnStatus();

    }

    private void setBtnStatus() {
        //小于等于启卖分数不可以减
        if (mInCreaseUnit.compareTo(mValue) >= 0) {
            mIbIncrease.setEnabled(true);
            mIbDecrease.setEnabled(false);
        } else {
            mIbIncrease.setEnabled(true);
            mIbDecrease.setEnabled(true);
        }
    }

    /**
     * 设置加减背景
     *
     * @param decreseBgId
     * @param increaseBgId
     */
    public void setBtnBg(int decreseBgId, int increaseBgId) {
        mIbDecrease.setBackgroundResource(decreseBgId);
        mIbIncrease.setBackgroundResource(increaseBgId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_decrease:
                //大于启卖分数可以减
                if (mInCreaseUnit.compareTo(mValue) < 0) {
                    mValue = mValue.subtract(mOffset);
                    // mEtCount.setText(mValue + "");
                    if (mChangeListener != null) {
                        mChangeListener.onNumberChanged(mValue);
                    }
                }
                setBtnStatus();
                break;
            case R.id.ib_increase:
                mValue = mValue.add(mOffset);
                // mEtCount.setText(mValue + "");
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

        /*if(s.toString().matches(matchstr)){*/

        BigDecimal curCount = new BigDecimal(s.toString());
        if (curCount.compareTo(mValue) == 0) {//如果值没有改变
            return;
        }
        mValue = curCount;
        setBtnStatus();
        //同时数据改变
        if (mChangeListener != null) {
            mChangeListener.onNumberChanged(mValue);
        }
       /* }else{
            s= mValue + "";
            mEtCount.setText(s);
            mEtCount.setSelection(s.length());
        }*/
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public interface ChangeListener {
        void onNumberChanged(BigDecimal number);
    }
}
