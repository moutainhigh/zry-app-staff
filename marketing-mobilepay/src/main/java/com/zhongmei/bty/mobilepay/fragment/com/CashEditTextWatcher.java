package com.zhongmei.bty.mobilepay.fragment.com;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;

import java.math.BigDecimal;



public class CashEditTextWatcher implements TextWatcher {
    private static final String TAG = CashEditTextWatcher.class.getSimpleName();
    private IPaymentInfo mPaymentInfo;    private EditText mCashInputET;    private String mSymbol;    private PayView mPayView;
    private boolean isSuportPayMore;
    public CashEditTextWatcher setSuportPayMore(boolean suportPayMore) {
        isSuportPayMore = suportPayMore;
        return this;
    }

    private CashEditTextWatcher() {

    }

    public CashEditTextWatcher(PayView payView, IPaymentInfo paymentInfo, EditText inputEdit, String symbol) {
        this.mPayView = payView;
        this.mPaymentInfo = paymentInfo;
        this.mCashInputET = inputEdit;
        this.mSymbol = symbol;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String content = s.toString();

        boolean ismodify = false;

        if (!TextUtils.isEmpty(content)) {
            mCashInputET.setSelection(content.length());
            mPayView.showClearTB(true);

            if (content.startsWith(".")) {
                content = "0" + content;
                ismodify = true;
            }
            if (!content.startsWith(mSymbol)) {
                content = ShopInfoCfg.formatCurrencySymbol(content);
                ismodify = true;
            }

            String valueStr = content.replace(mSymbol, "").trim();
            if (TextUtils.isEmpty(valueStr)) {                content = "";
                ismodify = true;
            } else if (!CashInfoManager.isMatchCashFormat(valueStr)) {
                content = content.substring(0, content.length() - 1);
                ismodify = true;
                if (isSuportPayMore) {                    mPayView.inputMoreAlter();                }
            } else {
                if (!isSuportPayMore) {
                    try {
                        double value = Double.valueOf(valueStr);
                        BigDecimal shouldAmount = BigDecimal.valueOf(mPaymentInfo.getActualAmount());
                        if (BigDecimal.valueOf(value).compareTo(shouldAmount) > 0) {                            content = content.substring(0, content.length() - 1);
                            ismodify = true;
                            mPayView.inputMoreAlter();
                        }
                    } catch (NumberFormatException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }
            if (ismodify) {
                mCashInputET.setText(content);
            } else {
                mPayView.updateNotPayMent();
            }

        } else {
            mPayView.showClearTB(true);
            mPayView.updateNotPayMent();
        }
    }
}

