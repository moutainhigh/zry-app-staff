package com.zhongmei.beauty.customer;

import android.content.Context;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.commonmodule.view.NumberInputdialog;

/**
 * 美业输入
 *
 * @date 2018/6/25
 */
public class BeautyNumberInputdialog extends NumberInputdialog {
    public BeautyNumberInputdialog(Context context, int theme) {
        super(context, theme);
    }

    public BeautyNumberInputdialog(Context context, int titleResId, int hintResId, String lastInput, double maxValue, InputOverListener linster) {
        super(context, titleResId, hintResId, lastInput, maxValue, linster);
    }

    public BeautyNumberInputdialog(Context context, String title, String hint, String lastInput, double maxValue, InputOverListener linster) {
        super(context, title, hint, lastInput, maxValue, linster);
        getBtnOk().setBackgroundResource(R.drawable.beauty_customer_charging_pay_cash_selector);
    }
}
