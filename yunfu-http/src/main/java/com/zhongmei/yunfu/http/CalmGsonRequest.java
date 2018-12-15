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

/**
 * @param <Q> Request数据类型
 * @param <R> Response数据类型
 */
public class CalmGsonRequest<Q, R> extends GsonRequest<Q, R> implements Cloneable {

    private static final String TAG = CalmGsonRequest.class.getSimpleName();

    private static final boolean isDebug = true;

    //幂等重试次数
    protected int serviceRetryCount = 0;

    /**
     * @param url
     * @param requestObject 要POST的数据
     * @param responseType  Response数据要转成的对象类型
     * @param listener
     * @param errorListener
     */
    public CalmGsonRequest(String url, Q requestObject, Type responseType, Listener<R> listener,
                           ErrorListener errorListener) {
        /*super(url, requestObject, responseType, listener, errorListener);
        this.mListener = EventListenerProxy.newListenerProxy(listener, getMethodString(), url, body);*/
        this(Method.POST, url, requestObject, responseType, listener, errorListener);
    }

    /**
     * @param url
     * @param body
     * @param responseType
     * @param listener
     * @param errorListener
     */
    public CalmGsonRequest(String url, String body, Type responseType, Listener<R> listener,
                           ErrorListener errorListener) {
        /*super(url, body, responseType, listener, errorListener);
        this.mListener = EventListenerProxy.newListenerProxy(listener, getMethodString(), url, body);*/
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
        //同步服务器请求成功，失败次数置为0
        SwitchServerManager.getInstance().reset();
        super.deliverResponse1(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        //先确定网络是可用的
        if (NetworkUtil.isNetworkConnected()) {
            //同步服务器请求失败，失败次数＋1
            if (SwitchServerManager.getInstance().isServerError(error)) {
                SwitchServerManager.getInstance().retryFailCount();
            }

            //同步组幂等重试
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
