package com.zhongmei.bty.data.operates;

import android.support.v4.app.FragmentManager;

import com.zhongmei.yunfu.resp.ErpResponseListener;
import com.zhongmei.yunfu.ui.view.CalmLoadingDialogFragment;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ErpResponseObject;
import com.zhongmei.yunfu.util.Checks;

/**
 * Created by demo on 2018/12/15
 */
public class LoadingErpResponseListener<T> implements ErpResponseListener<T> {

    private final ErpResponseListener<T> mListener;

    private final FragmentManager mFragmentManager;

    private CalmLoadingDialogFragment mDialogFragment = null;

    private LoadingErpResponseListener(ErpResponseListener<T> listener, FragmentManager fragmentManager) {
        Checks.verifyNotNull(listener, "listener");
        Checks.verifyNotNull(fragmentManager, "fragmentManager");
        this.mListener = listener;
        this.mFragmentManager = fragmentManager;
    }

    public void showLoadingDialog() {
        mDialogFragment = CalmLoadingDialogFragment.show(mFragmentManager);
        mFragmentManager.executePendingTransactions();
    }

    private void dismissLoadingDialog() {
        if (mDialogFragment != null) {
            CalmLoadingDialogFragment.hide(mDialogFragment);
            mDialogFragment = null;
        }
    }

    @Override
    public void onResponse(ErpResponseObject<T> response) {
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
    public static <T> ErpResponseListener<T> ensure(ErpResponseListener<T> listener, FragmentManager fragmentManager) {
        if (listener instanceof LoadingErpResponseListener) {
            return listener;
        }
        return new LoadingErpResponseListener<T>(listener, fragmentManager);
    }

}
