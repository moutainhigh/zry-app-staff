package com.zhongmei.bty.cashier.ordercenter.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.cashier.ordercenter.presenter.AddFeePresenter;
import com.zhongmei.bty.cashier.ordercenter.presenter.IAddFeeContract;
import com.zhongmei.bty.cashier.ordercenter.presenter.IAddFeeContract.IAddFeePresenter;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;


public class AddFeeDialog extends BasicDialogFragment implements View.OnClickListener, IAddFeeContract.IAddFeeView {

    public static final String EXTRA_DELIVERY_ORDER_ID = "EXTRA_DELIVERY_ORDER_ID";

    public static final String EXTRA_DELIVERY_PLATFORM = "EXTRA_DELIVERY_PLATFORM";

    private EditText editText;

    private ImageView mPlus;

    private ImageView mMinus;

    private IAddFeePresenter mPresenter;

    private Long deliveryOrderId;

    private Integer deliveryPlatform;

    public static AddFeeDialog newInstance(Long deliveryOrderId, Integer deliveryPlatform) {
        AddFeeDialog addFeeDialog = new AddFeeDialog();
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_DELIVERY_ORDER_ID, deliveryOrderId);
        bundle.putInt(EXTRA_DELIVERY_PLATFORM, deliveryPlatform);
        addFeeDialog.setArguments(bundle);
        return addFeeDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_add_fee, container);
        dialog.setCanceledOnTouchOutside(false);        assignViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setBackgroundDrawableResource(R.drawable.bg_round_white);
        }
    }

    private void assignViews(View view) {
        mPresenter = new AddFeePresenter();
        mPresenter.attachView(this);

                Bundle bundle = getArguments();
        deliveryOrderId = bundle.getLong(EXTRA_DELIVERY_ORDER_ID);
        deliveryPlatform = bundle.getInt(EXTRA_DELIVERY_PLATFORM);

        TextView v1 = (TextView) view.findViewById(R.id.one);
        TextView v2 = (TextView) view.findViewById(R.id.two);
        TextView v3 = (TextView) view.findViewById(R.id.three);
        TextView v4 = (TextView) view.findViewById(R.id.four);
        TextView v5 = (TextView) view.findViewById(R.id.five);
        TextView v6 = (TextView) view.findViewById(R.id.six);
        TextView v7 = (TextView) view.findViewById(R.id.seven);
        TextView v8 = (TextView) view.findViewById(R.id.eight);
        TextView v9 = (TextView) view.findViewById(R.id.nine);
        ImageView vPoint = (ImageView) view.findViewById(R.id.point);
        TextView v0 = (TextView) view.findViewById(R.id.zero);
        ImageView vDelete = (ImageView) view.findViewById(R.id.delete);
        editText = (EditText) view.findViewById(R.id.edit_text);
        TextView tvOk = (TextView) view.findViewById(R.id.tv_ok);
        ImageButton ibClose = (ImageButton) view.findViewById(R.id.ib_close);

        mMinus = (ImageView) view.findViewById(R.id.ib_minus);
        mPlus = (ImageView) view.findViewById(R.id.ib_plus);

        v1.setOnClickListener(this);
        v2.setOnClickListener(this);
        v3.setOnClickListener(this);
        v4.setOnClickListener(this);
        v5.setOnClickListener(this);
        v6.setOnClickListener(this);
        v7.setOnClickListener(this);
        v8.setOnClickListener(this);
        v9.setOnClickListener(this);
        v0.setOnClickListener(this);
        vPoint.setOnClickListener(this);
        vDelete.setOnClickListener(this);
        tvOk.setOnClickListener(this);
        ibClose.setOnClickListener(this);
        mMinus.setOnClickListener(this);
        mPlus.setOnClickListener(this);

        hideSoftInputFromWindow(editText);
        editText.setSelection(editText.getText().toString().length());

        mMinus.setEnabled(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.one:
                mPresenter.numClick(1);
                break;
            case R.id.two:
                mPresenter.numClick(2);
                break;
            case R.id.three:
                mPresenter.numClick(3);
                break;
            case R.id.four:
                mPresenter.numClick(4);
                break;
            case R.id.five:
                mPresenter.numClick(5);
                break;
            case R.id.six:
                mPresenter.numClick(6);
                break;
            case R.id.seven:
                mPresenter.numClick(7);
                break;
            case R.id.eight:
                mPresenter.numClick(8);
                break;
            case R.id.nine:
                mPresenter.numClick(9);
                break;
            case R.id.zero:
                mPresenter.numClick(0);
                break;
            case R.id.point:
                mPresenter.numClick(-1);
                break;
            case R.id.delete:
                mPresenter.numClick(-2);
                break;
            case R.id.tv_ok:
                mPresenter.sureClick(deliveryOrderId, deliveryPlatform);
                break;
            case R.id.ib_close:
                dismiss();
                break;
            case R.id.ib_minus:
                mPresenter.minusClick();
                break;
            case R.id.ib_plus:
                mPresenter.plusClick();
                break;
        }
    }

    @Override
    public void dismiss() {
        if (isVisible()) {
            super.dismiss();
        }
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void setFeeValue(String value) {
        editText.setText(value);
        editText.setSelection(value.length());
    }

    @Override
    public void showDialog(FragmentManager fragmentManager) {
        show(fragmentManager, "AddFeeDialog");
    }

    @Override
    public void dismissDialog() {
        dismiss();
    }

    @Override
    public void setPlusEnable(boolean enable) {
        mPlus.setEnabled(enable);
    }

    @Override
    public void setMinusEnable(boolean enable) {
        mMinus.setEnabled(enable);
    }

    @Override
    public void onDestroyView() {
        mPresenter.detachView();
        mPresenter = null;
        super.onDestroyView();
    }

    @Override
    public FragmentManager getViewFragmentManager() {
        return getFragmentManager();
    }
}
