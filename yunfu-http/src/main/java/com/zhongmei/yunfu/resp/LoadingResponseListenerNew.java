package com.zhongmei.yunfu.resp;

import android.content.Context;

import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.util.Checks;
import com.zhongmei.yunfu.context.UILoadingController;

import java.lang.ref.WeakReference;


public class LoadingResponseListenerNew<T> extends EventResponseListener<T> implements ResponseListener<T> {

    private final ResponseListener<T> mListener;

    private final WeakReference<Context> mFragmentManager;

    private LoadingResponseListenerNew(ResponseListener<T> listener, Context fragmentManager) {
        Checks.verifyNotNull(listener, "listener");
        Checks.verifyNotNull(fragmentManager, "fragmentManager");
        this.mListener = listener;
        this.mFragmentManager = new WeakReference<>(fragmentManager);
        this.eventName = getEventName(listener);
    }

    public void showLoadingDialog() {

        Context context = mFragmentManager.get();
        if (context instanceof UILoadingController) {
            ((UILoadingController) context).showLoadingDialog();
        }
    }

    @Deprecated
    public void showLoadingDialogByAllowingStateLoss() {

        showLoadingDialog();
    }

    private void dismissLoadingDialog() {
        Context context = mFragmentManager.get();
        if (context instanceof UILoadingController) {
                        ((UILoadingController) context).dismissLoadingDialog();
            mFragmentManager.clear();
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


    public static <T> ResponseListener<T> ensure(ResponseListener<T> listener, Context fragmentManager) {
        if (listener instanceof LoadingResponseListenerNew) {
            return listener;
        }
        return new LoadingResponseListenerNew<T>(listener, fragmentManager);
    }

}
