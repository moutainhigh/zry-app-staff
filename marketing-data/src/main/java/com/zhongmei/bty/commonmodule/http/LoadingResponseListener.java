package com.zhongmei.bty.commonmodule.http;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.util.Checks;
import com.zhongmei.yunfu.ui.view.CalmLoadingDialogFragment;
import com.zhongmei.yunfu.context.UILoadingController;

/**
 * @param <T>
 * @version: 1.0
 * @date 2015年4月19日
 */
public class LoadingResponseListener<T> extends EventResponseListener<T> implements ResponseListener<T>, UILoadingController {

    private final ResponseListener<T> mListener;

    private final FragmentManager mFragmentManager;

    private CalmLoadingDialogFragment mDialogFragment = null;

    private LoadingResponseListener(ResponseListener<T> listener, FragmentManager fragmentManager) {
        Checks.verifyNotNull(listener, "listener");
        Checks.verifyNotNull(fragmentManager, "fragmentManager");
        this.mListener = listener;
        this.mFragmentManager = fragmentManager;
        this.eventName = EventResponseListener.getEventName(listener);
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
    public void onResponse(ResponseObject<T> response) {
        dismissLoadingDialog();
        mListener.onResponse(response);
    }

    @Override
    public void onError(VolleyError error) {
        dismissLoadingDialog();
        mListener.onError(error);
    }

    /**
     * 确保获取到一个LoadingResponseListener对象
     * 如果指定的listener不是LoadingResponseListener则创建一个LoadingResponseListener并返回
     *
     * @param listener
     * @param fragmentManager
     * @return
     */
    public static <T> ResponseListener<T> ensure(ResponseListener<T> listener, FragmentManager fragmentManager) {
        if (listener instanceof LoadingResponseListener) {
            return listener;
        }
        return new LoadingResponseListener<T>(listener, fragmentManager);
    }

}
