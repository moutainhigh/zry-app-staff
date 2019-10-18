package com.zhongmei.yunfu.data;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.zhongmei.yunfu.util.Checks;
import com.zhongmei.yunfu.ui.view.CalmLoadingDialogFragment;
import com.zhongmei.yunfu.context.UILoadingController;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.YFProxyResponseListener;
import com.zhongmei.yunfu.resp.YFResponseListener;


public class LoadingYFResponseListener<T> implements YFResponseListener<T>, YFProxyResponseListener<T>, UILoadingController {

    private final YFResponseListener<T> mListener;

    private final FragmentManager mFragmentManager;

    private CalmLoadingDialogFragment mDialogFragment = null;

    private LoadingYFResponseListener(YFResponseListener<T> listener, FragmentManager fragmentManager) {
        Checks.verifyNotNull(listener, "listener");
        Checks.verifyNotNull(fragmentManager, "fragmentManager");
        this.mListener = listener;
        this.mFragmentManager = fragmentManager;
    }

    @Override
    public YFResponseListener<T> getResponseListener() {
        return mListener;
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void showLoadingDialog() {
        mDialogFragment = CalmLoadingDialogFragment.show(mFragmentManager);
        mFragmentManager.executePendingTransactions();
    }

    public void showLoadingDialogByAllowingStateLoss() {
        mDialogFragment = CalmLoadingDialogFragment.showByAllowingStateLoss(mFragmentManager);
        mFragmentManager.executePendingTransactions();
    }

    @Override
    public void dismissLoadingDialog() {
        if (mDialogFragment != null) {
            CalmLoadingDialogFragment.hide(mDialogFragment);
            mDialogFragment = null;
        }
    }

    @Override
    public void onResponse(T response) {
        dismissLoadingDialog();
        mListener.onResponse(response);
    }

    @Override
    public void onError(VolleyError error) {
        dismissLoadingDialog();
        mListener.onError(error);
    }


    public static <T> YFResponseListener<T> ensure(YFResponseListener<T> listener, FragmentManager fragmentManager) {
        if (listener instanceof LoadingYFResponseListener) {
            return listener;
        }
        return new LoadingYFResponseListener<>(listener, fragmentManager);
    }

}
