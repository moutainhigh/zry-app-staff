package com.zhongmei.yunfu.net.request;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zhongmei.yunfu.net.RequestManagerCompat;
import com.zhongmei.yunfu.net.builder.NetError;
import com.zhongmei.yunfu.net.builder.NetworkRequest;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.VolleyError;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */
public class VolleyNetworkManager {
    private static final int TIMEOUT_MS = 10000;

    private static VolleyNetworkManager sInstance;

    private VolleyNetworkManager() {
    }

    public synchronized static VolleyNetworkManager getInstance() {
        if (sInstance == null) {
            sInstance = new VolleyNetworkManager();
        }
        return sInstance;
    }

    /**
     * 发送请求
     *
     * @param context
     * @param request
     */
    public void executeRequest(Context context, NetworkRequest request) {
        VolleyRequest volleyRequest = createRequest(request);
        if (volleyRequest == null) {
            return;
        }
        setRequestInterceptors(request);
        setRequestHeader(volleyRequest, request.getHeader());
        setResponseProcessor(volleyRequest, request.getProcessor());
        setTimeout(volleyRequest, request.getTimeout());
        setTag(volleyRequest, request.getTag());
        RequestManagerCompat.addRequest(context, volleyRequest, null);
    }

    private void setRequestInterceptors(NetworkRequest request) {
        List<NetworkRequest.RequestInterceptor> interceptors = request.getRequestInterceptors();
        if (interceptors == null || interceptors.isEmpty()) {
            return;
        }
        for (NetworkRequest.RequestInterceptor interceptor : interceptors) {
            interceptor.intercept(request);
        }
    }

    private void setTag(VolleyRequest request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
    }

    private void setTimeout(VolleyRequest request, int timeout) {
        if (timeout <= 0) {
            request.setTimeout(TIMEOUT_MS);
            return;
        }
        request.setTimeout(timeout);
    }

    // 设置返回处理器
    private void setResponseProcessor(VolleyRequest request, NetworkRequest.ResponseProcessor processor) {
        if (processor == null) {
            return;
        }
        request.setResponseProcessor(processor);
    }

    // 设置请求头
    private void setRequestHeader(VolleyRequest volleyRequest, Map<String, String> header) {
        if (header == null || header.isEmpty()) {
            return;
        }
        volleyRequest.setHttpHeader(header);
    }

    private String getRequestContent(Object content) {
        if (content instanceof String) {
            return (String) content;
        } else {
            return new Gson().toJson(content);
        }
    }

    private VolleyRequest createRequest(NetworkRequest request) {
        String url = request.getUrl();
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String content = getRequestContent(request.getContent());
        Type responseType = request.getResponseContentType();
        VolleyResponseListener responseListener = new VolleyResponseListener(request.getSuccessListener());
        VolleyErrorListener errorListener = new VolleyErrorListener(request.getErrorListener());

        VolleyRequest volleyRequest = new VolleyRequest(request.getMethod(), url, content, responseType, responseListener, errorListener);
        volleyRequest.setInterceptEnable(request.isInterceptEnable());
        return volleyRequest;
    }

    private static class VolleyResponseListener implements Response.Listener {
        private NetworkRequest.OnSuccessListener mListener;

        public VolleyResponseListener(NetworkRequest.OnSuccessListener listener) {
            mListener = listener;
        }

        @Override
        public void onResponse(Object response) {
            if (mListener != null) {
                mListener.onSuccess(response);
            }
        }
    }

    private static class VolleyErrorListener implements Response.ErrorListener {
        private NetworkRequest.OnErrorListener mErrorListener;

        public VolleyErrorListener(NetworkRequest.OnErrorListener errorListener) {
            mErrorListener = errorListener;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            if (mErrorListener != null) {
                mErrorListener.onError(new NetError(error));
            }
        }
    }
}
