package com.zhongmei.yunfu.http;

import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.context.UILoadingController;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.monitor.EventListener;
import com.zhongmei.yunfu.monitor.ResponseStringListener;
import com.zhongmei.yunfu.net.RequestManagerCompat;
import com.zhongmei.yunfu.net.volley.NetworkError;
import com.zhongmei.yunfu.net.volley.NoConnectionError;
import com.zhongmei.yunfu.net.volley.Response.ErrorListener;
import com.zhongmei.yunfu.net.volley.ServerError;
import com.zhongmei.yunfu.net.volley.TimeoutError;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.YFProxyResponseListener;
import com.zhongmei.yunfu.resp.YFResponseListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


public class JFRequest<E> extends CalmGsonRequest<Object, E> {

    private static final String TAG = JFRequest.class.getSimpleName();

    private static final int TIMEOUT_MS = 10000;
    private int mHeaderType;
    private ResponseProcessor<E> mProcessor;
    private YFResponseListener<E> responseListener;


    private JFRequest(int method, String url, Object requestContent, Type responseContentType, ListenerAdapter<E> listener,
                      ErrorListenerAdapter errorListner) {
        super(method, url, requestContent, responseContentType, listener, errorListner);
            }

    private JFRequest(String url, String body, Type responseContentType, ListenerAdapter<E> listener,
                      ErrorListenerAdapter errorListner) {
        super(url, body, responseContentType, listener, errorListner);
            }

    @Override
    protected E toResponseValue(String json, Type responseType) throws Exception {
        E response = super.toResponseValue(json, responseType);
        if (mProcessor != null) {
            return mProcessor.process(response);
        }
        return response;
    }

    public void setHeaderType(int headerType) {
        this.mHeaderType = headerType;
    }


    void setResponseProcessor(ResponseProcessor<E> processor) {
        this.mProcessor = processor;
        if (processor != null) {
            this.mProcessor = processor;
        }
    }


    static Type getResponseType(Type contentType) {
        return com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null,
                Type.class.cast(ResponseObject.class),
                contentType);
    }

    public static <T> Type getRequestType(Class<T> classType) {
        return com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null,
                Type.class.cast(RequestObject.class),
                classType);
    }


    public static <T> Type getListContentResponseType(Class<T> classType) {
        Type type = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, List.class, classType);
        return getResponseType(type);
    }

    public static <Q, E> Type getContentResponseType(Class<Q> rawType, Class<E> classType) {
        Type type = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, rawType, classType);
        return getResponseType(type);
    }


    public interface ResponseProcessor<E> {


        E process(E response);
    }


    public static abstract class SaveDatabaseResponseProcessor<E> implements ResponseProcessor<E> {

        public SaveDatabaseResponseProcessor() {
        }

        @Override
        public E process(final E response) {
            try {
                saveToDatabase(response);
            } catch (Exception ex) {
                Log.e(TAG, "Save to database error!", ex);
            }

            return response;
        }


        public void saveToDatabase(E resp) throws Exception {
            DatabaseHelper helper = DBHelperManager.getHelper();
            try {
                Callable<Void> callable = getCallable(helper, resp);
                if (callable != null) {
                    helper.callInTransaction(callable);
                }
            } finally {
                DBHelperManager.releaseHelper(helper);
            }
        }


        protected abstract Callable<Void> getCallable(final DatabaseHelper helper, final E resp) throws Exception;

    }


    static public abstract class DatabaseResponseProcessor<E> extends SaveDatabaseResponseProcessor<E> {
        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final E resp) throws Exception {
            return new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    transactionCallable(helper, resp);
                    return null;
                }
            };
        }


        protected abstract void transactionCallable(DatabaseHelper helper, E resp) throws Exception;
    }


    private static class ListenerAdapter<T> extends EventListener<T> {

        private final YFResponseListener<T> mListener;

        ListenerAdapter(YFResponseListener<T> listener) {
            super(EventResponseListener.getEventName(listener));
            this.mListener = listener;
        }

        @Override
        public void onResponse(T response) {
            if (mListener != null) {
                mListener.onResponse(response);
            }
        }
    }


    private static class ErrorListenerAdapter implements ErrorListener {

        private final YFResponseListener<?> mListener;

        ErrorListenerAdapter(YFResponseListener<?> listener) {
            this.mListener = listener;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            if (error != null) {
                Log.w(TAG, error.getMessage() + ":" + error.getCause());
            }
            if (mListener != null) {
                VolleyError newError;
                if (error != null) {
                    String msg;
                    if (error instanceof NoConnectionError || error instanceof NetworkError) {
                        msg = BaseApplication.sInstance.getResources().getString(R.string.connect_network_error);
                    } else if (error instanceof ServerError) {
                        msg = BaseApplication.sInstance.getResources().getString(R.string.connect_server_error);
                    } else if (error instanceof TimeoutError) {
                        msg = BaseApplication.sInstance.getResources().getString(R.string.connect_timeout_error);
                    } else {
                        msg = BaseApplication.sInstance.getResources().getString(R.string.connect_exception);
                    }
                    newError = new VolleyError(msg, error.getCause());
                } else {
                    newError = error;
                }
                mListener.onError(newError);
            }
        }

    }

    public static Executor create(String url) {
        return new Executor(url);
    }


    public static class Executor {

        private JFRequest request;

        private String mUrl;

        private Object mRequestContent;

        private Type mResponseContentType;

        private ResponseProcessor<?> mProcessor;

        private Integer mTimeout;

        private boolean interceptEnable;        private List<String> router = new ArrayList<>();
        private int mMethod = Method.POST;
        private int mHeaderType = HeaderType.HEADER_TYPE_SYNC;
        private Executor(String url) {
            this.mUrl = url;
            this.mRequestContent = null;
            this.mProcessor = null;
        }

        public Executor requestValue(Object requestContent) {
            this.mRequestContent = requestContent;
            return this;
        }

        public Executor responseProcessor(ResponseProcessor<?> processor) {
            this.mProcessor = processor;
            return this;
        }

        public Executor timeout(int timeout) {
            this.mTimeout = timeout;
            return this;
        }

        public Executor setMethod(int method) {
            this.mMethod = method;
            return this;
        }

        public Executor setHeaderType(int headerType) {
            this.mHeaderType = headerType;
            return this;
        }

        public Executor interceptEnable(boolean interceptEnable) {
            this.interceptEnable = interceptEnable;
            return this;
        }

        public Executor addRouter(String name) {
            this.router.add(name);
            return this;
        }


        public <T> void execute(YFResponseListener<T> listener, Object tag) {
            ListenerAdapter listenerAdapter = new ListenerAdapter<>(listener);
            ErrorListenerAdapter errorListener = new ErrorListenerAdapter(listener);
            YFResponseListener<T> yfResponseListener = getYFResponseListener(listener);
            this.mResponseContentType = ResponseStringListener.getSuperclassTypeParameter(yfResponseListener);
            request = new JFRequest<>(mMethod, mUrl, mRequestContent, mResponseContentType, listenerAdapter, errorListener);
            request.setHttpProperty(new DefaultHeaderInterceptor(request.getUrl()).getHeaders(this.mHeaderType));
            request.setHeaderType(this.mHeaderType);
            request.setResponseProcessor(mProcessor);
            request.setInterceptEnable(interceptEnable);
            request.addRouter(router);
            request.responseListener = listener;

            UILoadingController loadingListener = null;
            if (listener instanceof UILoadingController) {
                loadingListener = (UILoadingController) listener;
            }
            if (loadingListener != null) {
                try {
                    loadingListener.showLoadingDialog();
                } catch (Exception e) {
                    Log.w(TAG, "Show loading dialog faild!", e);
                }
            }
            if (mTimeout == null) {
                mTimeout = TIMEOUT_MS;
            }
            if (request != null) {
                request.setTimeout(mTimeout);
                RequestManagerCompat.addRequest(BaseApplication.sInstance, request, tag);
            }
        }

        private <T> YFResponseListener<T> getYFResponseListener(YFResponseListener<T> listener) {
            if (listener instanceof YFProxyResponseListener) {
                YFProxyResponseListener proxyResponseListener = (YFProxyResponseListener) listener;
                return getYFResponseListener(proxyResponseListener.getResponseListener());
            }
            return listener;
        }

        public String getUrl() {
            return mUrl;
        }
    }

    @Override
    protected boolean serviceRetry() {
        try {
            JFRequest request = clone();
            if (request != null) {
                request.serviceRetryCount = this.serviceRetryCount + 1;
                RequestManagerCompat.addRequest(BaseApplication.sInstance, request, request.getTag());

                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return false;
    }

    @Override
    protected JFRequest clone() {
        YFResponseListener<E> listener = this.responseListener;
        ListenerAdapter<E> listenerAdapter = new ListenerAdapter(listener);
        ErrorListenerAdapter errorListener = new ErrorListenerAdapter(listener);

        JFRequest request = null;
        if (!TextUtils.isEmpty(body)) {            request = new JFRequest(this.getUrl(), this.body, this.mResponseType, listenerAdapter, errorListener);
            request.setHttpProperty(new DefaultHeaderInterceptor(request.getUrl()).getHeaders(this.mHeaderType));
            request.setTag(this.getTag());
            request.setResponseProcessor(this.mProcessor);
            request.setInterceptEnable(this.getInterceptEnable());
            request.responseListener = listener;
            request.setTimeout(TIMEOUT_MS);
        }
        return request;
    }
}
