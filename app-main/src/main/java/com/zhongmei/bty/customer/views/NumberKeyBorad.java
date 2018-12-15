package com.zhongmei.bty.customer.views;

import java.math.BigDecimal;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.customer.CustomerChargingFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@EBean
public class NumberKeyBorad {

    private static final String TAG = NumberKeyBorad.class.getSimpleName();

    @ViewById(R.id.password_dot_group)
    protected LinearLayout dotGroup;

    @ViewById(R.id.show_value)
    protected TextView mShowValue;

    @ViewById(R.id.customer_verification)
    protected Button customer_verification;

    @ViewById(R.id.delete_all_text)
    protected ImageView mCloseImage;

    private String CUSTOMER_LOGIN = "customerLogin";

    public String fromType = null;

    public void setFromType(String mFromType) {
        this.fromType = mFromType;
    }

    // @ViewById(R.id.error_text)
    // protected TextView error_text;

    @Click({R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine,
            R.id.zero, R.id.dot, R.id.delete, R.id.delete_all_text})
    public void onClick(View v) {
        if (mShowValue != null) {
            switch (v.getId()) {
                case R.id.one:
                    // mShowValue.append("1");
                    setMShowValue("1");
                    break;
                case R.id.two:
                    // mShowValue.append("2");
                    setMShowValue("2");
                    break;
                case R.id.three:
                    // mShowValue.append("3");
                    setMShowValue("3");
                    break;
                case R.id.four:
                    // mShowValue.append("4");
                    setMShowValue("4");
                    break;
                case R.id.five:
                    // mShowValue.append("5");
                    setMShowValue("5");
                    break;
                case R.id.six:
                    // mShowValue.append("6");
                    setMShowValue("6");
                    break;
                case R.id.seven:
                    // mShowValue.append("7");
                    setMShowValue("7");
                    break;
                case R.id.eight:
                    // mShowValue.append("8");
                    setMShowValue("8");
                    break;
                case R.id.nine:
                    // mShowValue.append("9");
                    setMShowValue("9");
                    break;
                case R.id.zero:
                    // mShowValue.append("0");
                    setMShowValue("0");
                    break;
                case R.id.dot:
                    // mShowValue.append(".");
                    setMShowValue(".");
                    break;
                case R.id.delete:
                    if (!isNumber(mShowValue.getText())) {
                        return;
                    }

                    if (mShowValue != null && !TextUtils.isEmpty(mShowValue.getText())) {
                        if (dotGroup != null && dotGroup.getVisibility() == View.VISIBLE
                                && mShowValue.getText().length() == 6) {
                            return;
                        }
                        mShowValue.setText(mShowValue.getText().toString().subSequence(0, mShowValue.length() - 1));
                        initDotGroup();
                    }

                    if (TextUtils.isEmpty(mShowValue.getText())
                            && CustomerChargingFragment.FLAG.equals(mShowValue.getTag())) {
                        mShowValue.setText(R.string.zero);
                        mShowValue.setTextColor(mShowValue.getContext().getResources().getColor(R.color.gray));
                    }

                    break;
                case R.id.delete_all_text:
                    if (mShowValue != null && !TextUtils.isEmpty(mShowValue.getText())) {
                        mShowValue.setText("");
                        initDotGroup();
                    }
                    break;

                default:
                    break;
            }
        }

        initDotGroup();

        setButton();
    }

    public void setMShowValue(String value) {
        if (TextUtils.isEmpty(mShowValue.getText()) || !isNumber(mShowValue.getText())) {
            mShowValue.setText(value);
            mShowValue.setTextColor(mShowValue.getContext().getResources().getColor(R.color.text_blue));
            return;
        }

        if (fromType != null && fromType.equals(CUSTOMER_LOGIN)) {
            if (".".equals(value)) {
                return;
            } else if (!TextUtils.isEmpty(mShowValue.getText().toString())
                    && mShowValue.getText().toString().length() > 14) {
                return;
            }
            mShowValue.setText(mShowValue.getText().toString() + value);
        } else if (CustomerChargingFragment.FLAG.equals(mShowValue.getTag())) {
            if (value.equals(".") && mShowValue.getText().toString().indexOf(".") != -1) {
                ToastUtil.showShortToast(R.string.customer_input_cash_incorrect);
                return;
            } else if (!value.equals(".") && mShowValue.getText().toString().indexOf(".") == -1
                    && Double.parseDouble(mShowValue.getText().toString()) >= 10000000) {
                ToastUtil.showShortToast(R.string.customer_charging_overrun);
                return;
            } else if (!value.equals(".") && mShowValue.getText().toString().indexOf(".") != -1) {
                if (mShowValue.getText().toString().length() > mShowValue.getText().toString().indexOf(".") + 2) {
                    ToastUtil.showShortToast(R.string.customer_charging_extract_to_cent);
                    return;
                }
            } else if (mShowValue.getText().toString().length() == 1 && mShowValue.getText().toString().startsWith("0")
                    && "0".equals(value)) {
                ToastUtil.showShortToast(R.string.customer_input_cash_incorrect);
                return;
            }
            mShowValue.setText(mShowValue.getText().toString() + value);
        } else {
            mShowValue.setText(mShowValue.getText().toString() + value);
        }

    }

    private boolean isNumber(CharSequence cs) {
        for (int i = 0; i < cs.length(); i++) {
            char c = cs.charAt(i);
            if ((c >= '0' && c <= '9') || c == '.') {
                continue;
            } else {
                return false;
            }
        }

        return true;
    }

    protected void initDotGroup() {
        if (dotGroup != null && dotGroup.getVisibility() == View.VISIBLE) {
            int count = mShowValue.getText().toString().length();
            if (count > 6) {
                count = 6;
            }
            for (int i = 0; i < count; i++) {
                dotGroup.getChildAt(i).setBackgroundResource(R.drawable.input_password_dot_input);
            }
            for (int i = 5; i >= count; i--) {
                dotGroup.getChildAt(i).setBackgroundResource(R.drawable.input_password_dot_normal);
            }
        }
    }

    @TextChange(R.id.show_value)
    public void onTextChange(CharSequence s, int start, int before, int count) {
        if (mCloseImage != null) {
            if (TextUtils.isEmpty(s)) {
                mCloseImage.setVisibility(View.GONE);
            } else {
                mCloseImage.setVisibility(View.VISIBLE);
            }
        }
    }

    public String getValue() {
        if (mShowValue != null) {
            return mShowValue.getText().toString();
        }
        return "";
    }

    public Float getNumber() {
        try {
            return Float.parseFloat(getValue());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return 0f;
    }

    public boolean cashCheckNotPass() {
        if (TextUtils.isEmpty(getValue())) {
            ToastUtil.showShortToast(R.string.customer_no_input_cash);
            return true;
        }

        try {
            BigDecimal min = BigDecimal.valueOf(0.01);
            BigDecimal max = BigDecimal.valueOf(100000000);
            BigDecimal value = new BigDecimal(getValue());
            if (value.compareTo(min) <= 0 || value.compareTo(max) >= 0) {
                ToastUtil.showShortToast(R.string.customer_input_cash_incorrect);
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            ToastUtil.showShortToast(R.string.customer_input_cash_incorrect);
            return true;
        }

        return false;
    }

    public void setButton() {
        if (customer_verification != null) {
            if (!TextUtils.isEmpty(mShowValue.getText())) {
                customer_verification.setEnabled(true);
                customer_verification.setBackgroundResource(R.drawable.handover_button);
            }
            if (TextUtils.isEmpty(mShowValue.getText())) {
                customer_verification.setEnabled(false);
                customer_verification.setBackgroundResource(R.drawable.pay_dopay_btn_selector);
            }
        }

    }
}
