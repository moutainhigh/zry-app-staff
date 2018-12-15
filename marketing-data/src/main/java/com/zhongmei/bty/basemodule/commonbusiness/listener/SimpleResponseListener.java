package com.zhongmei.bty.basemodule.commonbusiness.listener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.data.TransferResp;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;

/**
 * @date 2017/2/14 10:46
 */

public abstract class SimpleResponseListener<T> implements ResponseListener<T> {

    private boolean isCancelled = false;

    public static <T> ResponseListener newLoading(FragmentActivity activity, SimpleResponseListener<T> listener) {
        return LoadingResponseListener.ensure(listener, activity.getSupportFragmentManager());
    }

    public static <T> ResponseListener newLoading(Fragment fragment, SimpleResponseListener<T> listener) {
        return LoadingResponseListener.ensure(listener, fragment.getFragmentManager());
    }

    @Override
    public final void onResponse(ResponseObject<T> response) {
        if (isCancelled) {
            return;
        }
        try {
            String errorMsg = isOk(response);
            if (errorMsg == null) {
                onSuccess(response);
            } else {
                onError(new VolleyError(errorMsg));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static <T> String isOk(ResponseObject<T> response) {
        if (!ResponseObject.isOk(response)) {
            return response.getMessage();
        }

        if (response.getContent() instanceof TransferResp) {
            TransferResp baseResp = (TransferResp) response.getContent();
            if (!TransferResp.isOk(baseResp)) {
                return baseResp.getMessage();
            }
        }

        return null;
    }

    public abstract void onSuccess(ResponseObject<T> response);

    @Override
    @Deprecated
    public void onError(VolleyError error) {
        if (!isCancelled) {
            onFailure(error);
        }
    }

    public void onFailure(VolleyError error) {

    }

    public void cancel() {
        isCancelled = true;
    }
}
