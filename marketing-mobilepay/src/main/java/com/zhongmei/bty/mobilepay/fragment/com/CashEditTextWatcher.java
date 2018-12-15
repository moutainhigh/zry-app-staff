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

/**
 * Created by demo on 2018/12/15
 * 支付金额输入框监听器，统一处理支付金额输入逻辑,暂时不支持溢收
 */

public class CashEditTextWatcher implements TextWatcher {
    private static final String TAG = CashEditTextWatcher.class.getSimpleName();
    private IPaymentInfo mPaymentInfo;//支付信息
    private EditText mCashInputET;//金额输入框
    private String mSymbol;//货币符号
    private PayView mPayView;
    private boolean isSuportPayMore;//是否支持溢收

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
            if (TextUtils.isEmpty(valueStr)) {//modify v8.11
                content = "";
                ismodify = true;
            } else if (!CashInfoManager.isMatchCashFormat(valueStr)) {
                content = content.substring(0, content.length() - 1);
                ismodify = true;
                if (isSuportPayMore) {//提示金额太大
                    mPayView.inputMoreAlter();//只有现金输入会提醒
                }
            } else {
                if (!isSuportPayMore) {
                    try {
                        double value = Double.valueOf(valueStr);
                        BigDecimal shouldAmount = BigDecimal.valueOf(mPaymentInfo.getActualAmount());
                        if (BigDecimal.valueOf(value).compareTo(shouldAmount) > 0) {// 产生溢收后不允许输入现金
                            content = content.substring(0, content.length() - 1);
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

