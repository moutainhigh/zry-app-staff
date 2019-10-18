package com.zhongmei.bty.customer;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.bean.req.CustomerResp;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


@EFragment(R.layout.customer_charging_success_dialog)
public class CustomerChargingSuccessDialog extends BasicDialogFragment implements OnKeyListener {

    private boolean mCancelWithHomeKey = true;

    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    private OnPositiveListener mOnPositiveListener;

    @ViewById(R.id.tvMoney_CustomerSDialog)
    TextView mTvMoney;
    @ViewById(R.id.tvSendMoney_CustomerSDialog)
    TextView mTvSendMoney;
    @ViewById(R.id.tvName_CustomerSDialog)
    TextView mTvName;
    @ViewById(R.id.tvMobile_CustomerSDialog)
    TextView mTvMobile;
    @ViewById(R.id.tvBalance_CustomerSDialog)
    TextView mTvCurrentBalance;

    private String mCurrentBalance;

    private String mSendMoney;

    private String mMoney;

    private CustomerResp mCustomer;

    public interface OnPositiveListener {
        void onClickSubmit();
    }

    public void show(FragmentManager manager, String tag) {
        if (manager != null) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }

    @AfterViews
    protected void initView() {
        getDialog().getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCancelable(false);
        getDialog().setOnKeyListener(this);
        getArgumentsData();
        refreshInfo();
    }

    private void getArgumentsData() {
        mMoney = getArguments().getString("money");
        mCurrentBalance = getArguments().getString("currentBalance");
        mSendMoney = getArguments().getString("sendMoney");
        mCustomer = (CustomerResp) getArguments().getSerializable("customer");
    }

    private void refreshInfo() {
        mTvMoney.setText(ShopInfoCfg.formatCurrencySymbol(mMoney));
        if (!TextUtils.isEmpty(mSendMoney)) {
            mTvSendMoney.setVisibility(View.VISIBLE);
            mTvSendMoney.setText(getString(R.string.customer_charging_dialog_send_money, ShopInfoCfg.formatCurrencySymbol(mSendMoney)));
        } else {
            mTvSendMoney.setVisibility(View.GONE);
        }
        mTvCurrentBalance.setText(getString(R.string.customer_charging_dialog_balance, mCurrentBalance));
        if (TextUtils.isEmpty(mCustomer.mobile)) {
            mTvMobile.setText(getString(R.string.customer_charging_dialog_mobile, "-"));
        } else {
            mTvMobile.setText(getString(R.string.customer_charging_dialog_mobile, mCustomer.mobile));
        }
        mTvName.setText(getString(R.string.customer_charging_dialog_name, TextUtils.isEmpty(mCustomer.customerName) ? getString(R.string.customer_name_null) : mCustomer.customerName));
    }

    public void setOnPositiveListener(OnPositiveListener onPositiveListener) {
        this.mOnPositiveListener = onPositiveListener;
    }

    public void setCancelWithHomeKey(boolean mCancelWithHomeKey) {
        this.mCancelWithHomeKey = mCancelWithHomeKey;
    }

    @Click(R.id.customer_cash_charging)
    public void onClickSubmit() {
        if (mOnPositiveListener != null) {
            mOnPositiveListener.onClickSubmit();
        }
        this.dismissAllowingStateLoss();
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_HOME) {
            if (mCancelWithHomeKey) {
                dismiss();
            }
            getActivity().onKeyDown(keyCode, event);
            return false;
        }
        return false;
    }

}

