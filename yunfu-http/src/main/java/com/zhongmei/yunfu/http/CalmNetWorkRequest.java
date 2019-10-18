package com.zhongmei.yunfu.http;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.zhongmei.yunfu.context.UILoadingController;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.http.R;
import com.zhongmei.yunfu.http.utils.RequestRetryUtil;
import com.zhongmei.yunfu.monitor.CalmEventProcessorProxy;
import com.zhongmei.yunfu.monitor.CalmEventSuccessListenerProxy;
import com.zhongmei.yunfu.net.builder.NetError;
import com.zhongmei.yunfu.net.builder.NetworkProvider;
import com.zhongmei.yunfu.net.builder.NetworkRequest;
import com.zhongmei.yunfu.net.volley.NetworkError;
import com.zhongmei.yunfu.net.volley.NoConnectionError;
import com.zhongmei.yunfu.net.volley.ServerError;
import com.zhongmei.yunfu.net.volley.TimeoutError;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CalmNetWorkRequest {

    @NonNull
    public static Type getResponseType(Type contentType) {
        return com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null,
                Type.class.cast(ResponseObject.class),
                contentType);
    }

    public static Builder with(Context context) {
        return new Builder<>().with(context);
    }

    public static Builder with(Fragment fragment) {
        return new Builder<>().with(fragment);
    }

    public static Builder with(FragmentActivity activity) {
        return new Builder<>().with(activity);
    }


    public static class Builder<T, R> {
        private Context context;         private Fragment fragment;
                private int method = NetworkRequest.HttpMethod.POST;
        private T requestContent;         private Type responseType;         private String url;         private NetworkRequest.ResponseProcessor responseProcessor;
        private NetworkRequest.OnSuccessListener<R> successListener;
        private NetworkRequest.OnErrorListener errorListener;
        private Map<String, String> headers;
        private Object tag;
        private int timeout = -1;
        private boolean isShowLoading = false;
        private List<NetworkRequest.RequestInterceptor> requestInterceptors = new ArrayList<>();
                private UILoadingController mDialogFragment = null;
        private String reqMarker = null;        private int serviceRetryCount = 0;
        private boolean interceptEnable;

        public void create() {
                        if (TextUtils.isEmpty(url) || requestContent == null || responseType == null) {
                return;
            }

            RequestObject<T> requestObject = RequestObject.create(requestContent);
            requestObject.reqMarker = this.reqMarker;
            NetworkRequest.Builder builder = new NetworkRequest.Builder()
                    .url(url)
                    .content(requestObject)
                    .responseType(responseType)
                    .interceptEnable(interceptEnable)
                    .httpMethod(method);
                        if (responseProcessor != null) {
                String eventName = CalmEventSuccessListenerProxy.getEventName(successListener);
                NetworkRequest.ResponseProcessor proxy = CalmEventProcessorProxy.newProxy(responseProcessor, eventName, method, url, requestObject);
                builder.responseProcessor(proxy);
            }

            builder.tag(tag);             builder.timeout(timeout);
                        NetworkRequest.OnSuccessListener proxy = CalmEventSuccessListenerProxy.newProxy(successListener, method, url, requestObject);
            ApiResponse apiResponse = new ApiResponse(url, requestObject, proxy, errorListener) {
                @Override
                public void onError(NetError error) {
                    if (RequestRetryUtil.isUrlCanServiceRetry(url) && !TextUtils.isEmpty(reqMarker)
                            && serviceRetryCount < RequestRetryUtil.MAX_SERVICE_RETRY_COUNT) {
                                                create();
                        serviceRetryCount++;
                        return;
                    }

                    super.onError(error);
                }
            };
            builder.successListener(apiResponse);
            builder.errorListener(apiResponse);

            if (isShowLoading && mDialogFragment == null) {
                showLoadingDialog();
            }
                        NetworkRequest request = builder.build();
            if (headers != null && !headers.isEmpty()) {
                request.setHeader(headers);
            }
            addRequestInterceptors(request);
            createRequest(request);
        }

        private void createRequest(NetworkRequest request) {
            NetworkProvider.Builder builder = new NetworkProvider.Builder();
            if (context != null) {
                builder.with(context);
            } else if (fragment != null) {
                builder.with(fragment);
            }  else {
                                throw new RuntimeException("sholud contain context or fragment or activty");
            }
            builder.request(request).create();
        }

        private void showLoadingDialog() {


            if (context instanceof UILoadingController) {
                mDialogFragment = (UILoadingController) context;
            } else if (fragment instanceof UILoadingController) {
                mDialogFragment = (UILoadingController) fragment;
            } else if (fragment != null && fragment.getActivity() instanceof UILoadingController) {
                mDialogFragment = (UILoadingController) fragment.getActivity();
            }

            if (mDialogFragment != null) {
                mDialogFragment.showLoadingDialog();
            }
        }



        private void addRequestInterceptors(NetworkRequest request) {
            if (requestInterceptors != null && !requestInterceptors.isEmpty()) {
                for (NetworkRequest.RequestInterceptor interceptor : requestInterceptors) {
                    request.addRequestInterceptor(interceptor);
                }
            }

            request.addRequestInterceptor(new DefaultHeaderInterceptor(request.getUrl()));
        }

        public Builder with(Context context) {
            this.context = context;
            return this;
        }

        public Builder with(Fragment fragment) {
            this.fragment = fragment;
            return this;
        }



        public Builder httpMethod(int method) {
            this.method = method;
            return this;
        }

        public Builder requestContent(T requestContent) {
            this.requestContent = requestContent;
            return this;
        }

        public Builder responseClass(Class responseClass) {
            this.responseType = getResponseType(Type.class.cast(responseClass));
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            if (RequestRetryUtil.isUrlCanServiceRetry(url)) {
                reqMarker = SystemUtils.genOnlyIdentifier();
            }
            return this;
        }

        public Builder successListener(NetworkRequest.OnSuccessListener successListener) {
            this.successListener = successListener;
            return this;
        }

        public Builder errorListener(NetworkRequest.OnErrorListener errorListener) {
            this.errorListener = errorListener;
            return this;
        }

        public Builder responseProcessor(NetworkRequest.ResponseProcessor responseProcessor) {
            this.responseProcessor = responseProcessor;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder tag(Object tag) {
            this.tag = tag;
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder showLoading() {
            this.isShowLoading = true;
            return this;
        }

        public Builder interceptEnable(boolean enable) {
            this.interceptEnable = enable;
            return this;
        }

        public Builder addRequestInterceptor(NetworkRequest.RequestInterceptor requestInterceptor) {
            this.requestInterceptors.add(requestInterceptor);
            return this;
        }

        private boolean isDialogShowing() {
            return isShowLoading && mDialogFragment != null;
        }

        private class ApiResponse<R> implements NetworkRequest.OnSuccessListener<R>, NetworkRequest.OnErrorListener {
            private NetworkRequest.OnSuccessListener mSuccessListener;
            private NetworkRequest.OnErrorListener mErrorListener;

            public ApiResponse(String url, RequestObject<?> requestObject, NetworkRequest.OnSuccessListener successListener, NetworkRequest.OnErrorListener errorListener) {
                mSuccessListener = successListener;
                mErrorListener = errorListener;
            }

            @Override
            public void onSuccess(R data) {
                if (isDialogShowing()) {
                                        mDialogFragment.dismissLoadingDialog();
                    mDialogFragment = null;
                }
                if (!(data instanceof ResponseObject)) {
                    onError(new NetError("ResponseObject ParseException", null));
                    return;
                }
                ResponseObject responseObject = (ResponseObject) data;
                handleException(responseObject);
                if (mSuccessListener != null) {
                    mSuccessListener.onSuccess(responseObject);
                }
            }

            private void handleException(ResponseObject responseObject) {

            }

            @Override
            public void onError(NetError error) {
                if (isDialogShowing()) {
                                        mDialogFragment.dismissLoadingDialog();
                    mDialogFragment = null;
                }
                if (mErrorListener == null) {
                    return;
                }
                VolleyError volleyError = error.getVolleyError();
                NetError newError;
                if (volleyError != null) {
                    String msg = getErrorMsg(volleyError);
                    newError = new NetError(msg, volleyError.getCause());
                } else {
                    newError = error;
                }
                mErrorListener.onError(newError);
            }
        }
    }

    private static String getErrorMsg(VolleyError volleyError) {
        String msg;
        if (volleyError instanceof NoConnectionError || volleyError instanceof NetworkError) {
            msg = BaseApplication.sInstance.getString(R.string.connect_network_error);
        } else if (volleyError instanceof ServerError) {
            msg = BaseApplication.sInstance.getString(R.string.connect_server_error);
        } else if (volleyError instanceof TimeoutError) {
            msg = BaseApplication.sInstance.getString(R.string.connect_timeout_error);
        } else {
            msg = BaseApplication.sInstance.getString(R.string.connect_exception);
        }

        return msg;
    }
}

