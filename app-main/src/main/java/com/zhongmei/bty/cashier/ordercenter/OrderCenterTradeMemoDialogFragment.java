package com.zhongmei.bty.cashier.ordercenter;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.cashier.ordercenter.view.MyEditText;
import com.zhongmei.bty.cashier.ordercenter.view.TradeMemoModelView;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.bty.snack.orderdish.adapter.ExtraOrderAdapter;
import com.zhongmei.bty.snack.orderdish.adapter.RecyclerViewBaseAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by demo on 2018/12/15
 */
@EFragment(R.layout.order_center_trade_memo_dialog_fragment)
public class OrderCenterTradeMemoDialogFragment extends BasicDialogFragment {

    @ViewById(R.id.met_trade_memo)
    MyEditText metTradeMemo;

    @ViewById(R.id.ttmv_trade_memo)
    TradeMemoModelView ttmvTradeMemo;

    public TradeMemoConfirmListener listener;

    public String tradeMemo;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_LAYOUT_FLAGS | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        Window window = dialog.getWindow();
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(uiOptions);

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        return dialog;
    }

    @AfterViews
    void initViews() {
        if (!TextUtils.isEmpty(tradeMemo)) {
            metTradeMemo.setText(tradeMemo);
            metTradeMemo.setSelection(tradeMemo.length());
        }

        ttmvTradeMemo.setOnItemClickListener(new RecyclerViewBaseAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                RecyclerView.Adapter adapter = ttmvTradeMemo.getAdapter();
                if (adapter instanceof ExtraOrderAdapter) {
                    ExtraOrderAdapter extraOrderAdapter = (ExtraOrderAdapter) adapter;
                    StringBuilder sb = new StringBuilder();
                    if (!TextUtils.isEmpty(metTradeMemo.getText())) {
                        sb.append(metTradeMemo.getText() + ",");
                    }
                    String memo = extraOrderAdapter.getItem(position).toString().trim();
                    sb.append(memo);

                    metTradeMemo.setText(sb.toString());
                    metTradeMemo.setSelection(metTradeMemo.getText().length());
                }
            }
        });
    }

    @Click({R.id.btn_close, R.id.btn_ok})
    void click(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                dismiss();
                break;
            case R.id.btn_ok:
                String memo = metTradeMemo.getText().toString();
                if (listener != null) {
                    listener.onConfirm(memo);
                }

                dismiss();
                break;
        }
    }

    public interface TradeMemoConfirmListener {
        void onConfirm(String memo);
    }

}
