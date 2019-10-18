package com.zhongmei.yunfu.http;

import android.util.Log;

import com.zhongmei.yunfu.context.util.NetworkUtil;
import com.zhongmei.yunfu.context.util.manager.SwitchServerManager;
import com.zhongmei.yunfu.http.utils.RequestRetryUtil;
import com.zhongmei.yunfu.net.request.GsonRequest;
import com.zhongmei.yunfu.net.volley.Response.ErrorListener;
import com.zhongmei.yunfu.net.volley.Response.Listener;
import com.zhongmei.yunfu.net.volley.VolleyError;

import java.lang.reflect.Type;


public class CalmGsonRequest<Q, R> extends GsonRequest<Q, R> implements Cloneable {

    private static final String TAG = CalmGsonRequest.class.getSimpleName();

    private static final boolean isDebug = true;

        protected int serviceRetryCount = 0;


    public CalmGsonRequest(String url, Q requestObject, Type responseType, Listener<R> listener,
                           ErrorListener errorListener) {

        this(Method.POST, url, requestObject, responseType, listener, errorListener);
    }


    public CalmGsonRequest(String url, String body, Type responseType, Listener<R> listener,
                           ErrorListener errorListener) {

        this(Method.POST, url, body, responseType, listener, errorListener);
    }

    public CalmGsonRequest(int method, String url, String body, Type responseType, Listener<R> listener,
                           ErrorListener errorListener) {
        super(method, url, body, responseType, listener, errorListener);
        this.mListener = EventListenerProxy.newListenerProxy(listener, getMethodString(), url, body);
    }

    public CalmGsonRequest(int method, String url, Q requestObject, Type responseType, Listener<R> listener,
                           ErrorListener errorListener) {
        super(method, url, requestObject, responseType, listener, errorListener);
        this.mListener = EventListenerProxy.newListenerProxy(listener, getMethodString(), url, body);
    }

    @Override
    protected void deliverResponse1(R response) {
                SwitchServerManager.getInstance().reset();
        super.deliverResponse1(response);
    }

    @Override
    public void deliverError(VolleyError error) {
                if (NetworkUtil.isNetworkConnected()) {
                        if (SwitchServerManager.getInstance().isServerError(error)) {
                SwitchServerManager.getInstance().retryFailCount();
            }

                        if (serviceRetryCount < RequestRetryUtil.MAX_SERVICE_RETRY_COUNT) {
                if (RequestRetryUtil.isUrlCanServiceRetry(getUrl())) {
                    Log.e(TAG, "幂等重试..." + serviceRetryCount);
                    if (serviceRetry()) {
                        return;
                    }
                }
            }
        }

        super.deliverError(error);
    }

}
