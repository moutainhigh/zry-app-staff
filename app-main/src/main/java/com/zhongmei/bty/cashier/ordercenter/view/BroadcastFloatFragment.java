package com.zhongmei.bty.cashier.ordercenter.view;

import android.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.cashier.ordercenter.presenter.IOrderCenterListPresenter;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.bty.data.operates.impl.CallDishNotifyOperatesImpl;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
@EFragment(R.layout.order_center_broadcast_layout)
public class BroadcastFloatFragment extends BasicDialogFragment {

    @ViewById(R.id.order_center_list_item_notice_wechat)
    ImageView mWechat;

    @ViewById(R.id.order_center_list_item_notice_ivr)
    ImageView mIVR;
    private View mRelativeView;
    private Pair<List<String>, CallDishNotifyOperatesImpl.NotifyReq> notify;
    private IOrderCenterListPresenter mPresenter;
    private CallDishNotifyOperatesImpl.NotifyReq mNotifyReq;
    private boolean mIvrSelect;
    private boolean mWechatSelect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.common_no_dark_dialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        int height = DensityUtil.dip2px(MainApplication.getInstance(), 80);
        window.setLayout(-2, height);
        WindowManager.LayoutParams wl = window.getAttributes();
        if (mRelativeView != null) {
            int[] position = new int[2];
            mRelativeView.getLocationOnScreen(position);
            int x = position[0] + mRelativeView.getWidth() + DensityUtil.dip2px(MainApplication.getInstance(), 12);
            int y = position[1] + (mRelativeView.getHeight() - height) / 2;
            wl.x = x;
            wl.y = y;
            wl.gravity = Gravity.START | Gravity.TOP;
            window.setAttributes(wl);
        }
    }

    @AfterViews
    void initView() {
        if (mIvrSelect) {
            mIVR.setSelected(true);
        }
        if (mWechatSelect) {
            mWechat.setSelected(true);
        }
    }

    @Click({R.id.order_center_list_item_notice_wechat, R.id.order_center_list_item_notice_ivr})
    void buttonClick(View v) {
        if (mPresenter == null || mNotifyReq == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.order_center_list_item_notice_wechat:
                mWechat.setSelected(true);
                mPresenter.notifyVoice(1, mNotifyReq.getTradeUuid(), mNotifyReq.getMobile(), mNotifyReq.getSerialNo(), mNotifyReq.getTradeNo());
                break;
            case R.id.order_center_list_item_notice_ivr:
                mIVR.setSelected(true);
                mPresenter.notifyVoice(2, mNotifyReq.getTradeUuid(), mNotifyReq.getMobile(), mNotifyReq.getSerialNo(), mNotifyReq.getTradeNo());
                break;
        }
    }

    public void showAsRight(FragmentTransaction transaction, String tag, View view) {
        mRelativeView = view;
        show(transaction, tag);
    }

    public void setPresenterAndData(IOrderCenterListPresenter presenter, CallDishNotifyOperatesImpl.NotifyReq notifyReq
            , boolean wechatSelect, boolean ivrSelect) {
        mPresenter = presenter;
        mNotifyReq = notifyReq;
        mWechatSelect = wechatSelect;
        mIvrSelect = ivrSelect;
    }
}
