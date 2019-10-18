package com.zhongmei.yunfu.http;

import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.yunfu.context.UILoadingController;
import com.zhongmei.yunfu.net.RequestManagerCompat;
import com.zhongmei.yunfu.net.volley.NetworkError;
import com.zhongmei.yunfu.net.volley.NoConnectionError;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.ServerError;
import com.zhongmei.yunfu.net.volley.TimeoutError;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Callable;


public class QSOpsRequest<Q, E> extends CalmGsonRequest<QSRequestObject<Q>, QSResponseObject<E>> {

    private static final String TAG = QSOpsRequest.class.getSimpleName();

    private static final int TIMEOUT_MS = 10000;

    private QSResponseProcessor<E> mProcessor;

    private QSOpsRequest(String url, Q requestContent, Type responseContentType, ListenerAdapter<E> listener,
                         ErrorListenerAdapter errorListner) {
        super(url, QSRequestObject.create(requestContent), responseContentType, listener, errorListner);
        setDataIntoHeader();
    }

    private QSOpsRequest(String url, String body, Type responseContentType, ListenerAdapter<E> listener,
                         ErrorListenerAdapter errorListner) {
        super(url, body, responseContentType, listener, errorListner);
        setDataIntoHeader();
    }

    @Override
    protected QSResponseObject<E> toResponseValue(String json, Type responseType) throws Exception {
        QSResponseObject<E> response = super.toResponseValue(json, responseType);
        if (mProcessor != null) {
            return mProcessor.process(response);
        }
        return response;
    }

    public void setDataIntoHeader() {


        DefaultHeaderInterceptor headerInterceptor = new DefaultHeaderInterceptor(getUrl());
        this.setHttpProperty(headerInterceptor.getDefaultHeader());
    }


    void setResponseProcessor(QSResponseProcessor<E> processor) {
        this.mProcessor = processor;
    }


    static Type getResponseType(Type contentType) {
        return com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null,
                Type.class.cast(QSResponseObject.class),
                contentType);
    }

    public static <T> Type getRequestType(Class<T> classType) {
        return com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null,
                Type.class.cast(RequestObject.class),
                classType);
    }


    static <T> Type getListContentResponseType(Class<T> classType) {
        Type type = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, List.class, classType);
        return getResponseType(type);
    }

    public static <Q, E> Type getContentResponseType(Class<Q> rawType, Class<E> classType) {
        Type type = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, rawType, classType);
        return getResponseType(type);
    }


    static interface QSResponseProcessor<E> {


        QSResponseObject<E> process(QSResponseObject<E> response);
    }


    public static abstract class SaveDatabaseResponseProcessor<E> implements QSResponseProcessor<E> {

        public SaveDatabaseResponseProcessor() {
        }

        protected boolean isSuccessful(QSResponseObject<E> response) {
            return response.getCode() == 1000;
        }

        @Override
        public QSResponseObject<E> process(final QSResponseObject<E> response) {
            if (isSuccessful(response) && response.getContent() != null) {

                try {
                    saveToDatabase(response.getContent());
                } catch (Exception ex) {
                    Log.e(TAG, "Save to database error!", ex);
                }

            }
            return response;
        }


        protected void saveToDatabase(E resp) throws Exception {
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


    private static class ListenerAdapter<T> implements Response.Listener<QSResponseObject<T>> {

        private final QSResponseListener<T> mListener;

        ListenerAdapter(QSResponseListener<T> listener) {
            this.mListener = listener;
        }

        @Override
        public void onResponse(QSResponseObject<T> response) {
            if (mListener != null) {
                mListener.onResponse(response);
            }
        }
    }


    private static class ErrorListenerAdapter implements Response.ErrorListener {

        private final QSResponseListener<?> mListener;

        ErrorListenerAdapter(QSResponseListener<?> listener) {
            this.mListener = listener;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            if (error != null) {
                error.printStackTrace();
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


    public static class Executor<Q, E> {

        public static <Q, E> Executor<Q, E> create(String url) {
            return new Executor<Q, E>(url);
        }

        private QSOpsRequest<Q, E> request;

        private String mUrl;

        private Q mRequestContent;

        private String body;
        private Type mResponseContentType;

        private QSResponseProcessor<E> mProcessor;

        private Integer mTimeout;

        private Executor(String url) {
            this.mUrl = url;
            this.mRequestContent = null;
        }

        public Executor<Q, E> requestValue(Q requestContent) {
            this.mRequestContent = requestContent;
            return this;
        }

        public Executor<Q, E> responseType(Type responseContentType) {
            this.mResponseContentType = responseContentType;
            return this;
        }

        public Executor<Q, E> responseClass(Class<E> responseContentClass) {
            return responseType(getResponseType(Type.class.cast(responseContentClass)));
        }

        public Executor<Q, E> responseProcessor(QSResponseProcessor<E> processor) {
            this.mProcessor = processor;
            return this;
        }

        public Executor<Q, E> timeout(int timeout) {
            this.mTimeout = timeout;
            return this;
        }


        public void execute(QSResponseListener<E> listener, Object tag) {
            execute(listener, tag, false);
        }


        public void execute(QSResponseListener<E> listener, Object tag, boolean isBody) {
            ListenerAdapter<E> listenerAdapter = new ListenerAdapter<E>(listener);
            ErrorListenerAdapter errorListener = new ErrorListenerAdapter(listener);
            try {
                if (isBody) {
                                        request =
                            new QSOpsRequest<Q, E>(mUrl, mRequestContent.toString(), mResponseContentType, listenerAdapter, errorListener);
                } else {
                                        if (!TextUtils.isEmpty(body)) {
                        request = new QSOpsRequest<Q, E>(mUrl, body, mResponseContentType, listenerAdapter, errorListener);
                    } else {
                        request = new QSOpsRequest<Q, E>(mUrl, mRequestContent, mResponseContentType, listenerAdapter, errorListener);
                    }
                }
                request.setResponseProcessor(mProcessor);
            } catch (Exception e) {
                Log.e(TAG, "Create request error!", e);
                String message = e.getMessage();
                Throwable throwable = e.getCause();
                if (throwable == null) {
                    throwable = e;
                }
                errorListener.onErrorResponse(new VolleyError(message, throwable));
                return;
            }

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

        public String getUrl() {
            return mUrl;
        }

    }

}
