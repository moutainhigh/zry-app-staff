package com.zhongmei.bty.dinner.orderdish.view;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.R;

@EFragment(R.layout.repay_confirm_dialog)
public class RepayConfirmDialog extends BasicDialogFragment {

    private OnClickListener mNegativeListener;

    private OnClickListener mPositiveLinstner;

    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @AfterViews
    protected void initView() {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCancelable(false);
    }

    public void setNegativeListener(OnClickListener listener) {
        this.mNegativeListener = listener;
    }

    public void setPositiveListener(OnClickListener listener) {
        this.mPositiveLinstner = listener;
    }

    @Click({R.id.negative_button, R.id.positive_button})
    void click(View v) {
        switch (v.getId()) {
            case R.id.positive_button:
                if (mPositiveLinstner != null) {
                    mPositiveLinstner.onClick(v);
                }
                break;
            case R.id.negative_button:
                if (mNegativeListener != null) {
                    mNegativeListener.onClick(v);
                }
                break;
            default:
                break;
        }
        this.dismissAllowingStateLoss();
    }

    public static class RepayConfirmDialogBuilder {
        private Bundle mBundle;

        private OnClickListener mNegativeListener;

        private OnClickListener mPositiveLinstner;

        public RepayConfirmDialogBuilder() {
            mBundle = new Bundle();
        }

        public RepayConfirmDialog build() {
            RepayConfirmDialog fragment = new RepayConfirmDialog_();
            fragment.setArguments(mBundle);
            if (mNegativeListener != null) {
                fragment.setNegativeListener(mNegativeListener);
            }
            if (mPositiveLinstner != null) {
                fragment.setPositiveListener(mPositiveLinstner);
            }
            return fragment;
        }

        public RepayConfirmDialogBuilder negativeLisnter(OnClickListener listener) {
            mNegativeListener = listener;
            return this;
        }

        public RepayConfirmDialogBuilder positiveLinstner(OnClickListener listener) {
            mPositiveLinstner = listener;
            return this;
        }
    }

}
