/*
package com.zhongmei.bty.data.operates.impl;

import android.util.Log;

import com.zhongmei.bty.MainApplication;
import com.zhongmei.yunfu.R;
import EventListenerProxy;
import com.zhongmei.bty.basemodule.erp.bean.ErpResponseObject;
import com.zhongmei.bty.basemodule.erp.listener.ErpResponseListener;
import RequestManagerCompat;
import NetworkError;
import NoConnectionError;
import Response;
import ServerError;
import TimeoutError;
import VolleyError;
import com.zhongmei.bty.commonmodule.common.Constant;
import com.zhongmei.bty.data.operates.LoadingErpResponseListener;
import com.zhongmei.bty.baseservice.net.request.GetGsonRequest;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

*/
/**
 * Created by demo on 2018/12/15
 *
 * @param url
 * @param responseType  Response数据要转成的对象类型
 * @param listener
 * @param errorListener
 * <p>
 * ResponseObject处理器。
 * 在从网络获取到数据并转换成ResponseObject对象后将使用此处理器先进行处理(如保存到数据库)
 * @param <E>
 * <p>
 * <p>
 * 对response进行处理
 * @param response
 * @return 设置ErpResponseObject的处理器
 * @param processor
 * <p>
 * 获取指定Content类型的ResponseObject对象的类型
 * @param contentType
 * @return 获取当ResponseObject的content是List<?>时的Type
 * @param classType content为List时列表元素的对象类型
 * @return 负责发送Request
 * @param <E> 回复的数据类型
 * @version: 1.0
 * @date 2016年10月28日
 * <p>
 * 发送Request
 * @param listener
 * @param tag
 * @param <T>
 * @version: 1.0
 * @date 2015年4月19日
 * @version: 1.0
 * @date 2015年4月19日
 * @param url
 * @param responseType  Response数据要转成的对象类型
 * @param listener
 * @param errorListener
 * <p>
 * ResponseObject处理器。
 * 在从网络获取到数据并转换成ResponseObject对象后将使用此处理器先进行处理(如保存到数据库)
 * @param <E>
 * <p>
 * <p>
 * 对response进行处理
 * @param response
 * @return 设置ErpResponseObject的处理器
 * @param processor
 * <p>
 * 获取指定Content类型的ResponseObject对象的类型
 * @param contentType
 * @return 获取当ResponseObject的content是List<?>时的Type
 * @param classType content为List时列表元素的对象类型
 * @return 负责发送Request
 * @param <E> 回复的数据类型
 * @version: 1.0
 * @date 2016年10月28日
 * <p>
 * 发送Request
 * @param listener
 * @param tag
 * @param <T>
 * @version: 1.0
 * @date 2015年4月19日
 * @version: 1.0
 * @date 2015年4月19日
 *//*

public class ErpPostRequest<E> extends GetGsonRequest<ErpResponseObject<E>> {

    private static final String TAG = ErpPostRequest.class.getSimpleName();

    private ErpResponseProcessor<E> mProcessor;

    */
/**
 * @param url
 * @param responseType  Response数据要转成的对象类型
 * @param listener
 * @param errorListener
 *//*

    public ErpPostRequest(String url, Type responseType, Response.Listener<ErpResponseObject<E>> listener, Response.ErrorListener errorListener) {
        super(Method.POST,url, responseType, listener, errorListener);
        this.mListener = EventListenerProxy.newListenerProxy(listener, getMethodString(), url, getBodyString());
        setHttpProperty(Constant._KRY_GLOBAL_MSG_ID, UUID.randomUUID().toString());
    }

    */
/**
 * ResponseObject处理器。
 * 在从网络获取到数据并转换成ResponseObject对象后将使用此处理器先进行处理(如保存到数据库)
 *
 * @param <E>

 *//*

    static interface ErpResponseProcessor<E> {

        */
/**
 * 对response进行处理
 *
 * @param response
 * @return
 *//*

        ErpResponseObject<E> process(ErpResponseObject<E> response);
    }

    @Override
    protected ErpResponseObject<E> toResponseValue(String json, Type responseType) throws Exception {
        ErpResponseObject<E> response = super.toResponseValue(json, responseType);
        if (mProcessor != null) {
            return mProcessor.process(response);
        }
        return response;
    }

    */
/**
 * 设置ErpResponseObject的处理器
 *
 * @param processor
 *//*

    void setErpResponseProcessor(ErpResponseProcessor<E> processor) {
        this.mProcessor = processor;
    }

    */
/**
 * 获取指定Content类型的ResponseObject对象的类型
 *
 * @param contentType
 * @return
 *//*

    static Type getErpResponseType(Type contentType) {
        return com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null,
                Type.class.cast(ErpResponseObject.class),
                contentType);
    }

    */
/**
 * 获取当ResponseObject的content是List<?>时的Type
 *
 * @param classType content为List时列表元素的对象类型
 * @return
 *//*

    static <T> Type getListContentErpResponseType(Class<T> classType) {
        Type type = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, List.class, classType);
        return getErpResponseType(type);
    }

    static <Q, E> Type getContentErpResponseType(Class<Q> rawType, Class<E> classType) {
        Type type = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, rawType, classType);
        return getErpResponseType(type);
    }

    */
/**
 * 负责发送Request
 *
 * @param <E> 回复的数据类型

 * @version: 1.0
 * @date 2016年10月28日
 *//*

    public static class ErpPostExecutor<E> {

        public static <E> ErpPostExecutor<E> create(String url) {
            return new ErpPostExecutor<E>(url);
        }

        private ErpPostRequest<E> request;

        private String mUrl;

        private Type mResponseContentType;

        private ErpResponseProcessor<E> mProcessor;

        private Integer mTimeout;

        private ErpPostExecutor(String url) {
            this.mUrl = url;
            this.mProcessor = null;
        }

        public ErpPostExecutor<E> responseType(Type responseContentType) {
            this.mResponseContentType = responseContentType;
            return this;
        }

        public ErpPostExecutor<E> responseClass(Class<E> responseContentClass) {
            return responseType(getErpResponseType(Type.class.cast(responseContentClass)));
        }

        public ErpPostExecutor<E> responseProcessor(ErpResponseProcessor<E> processor) {
            this.mProcessor = processor;
            return this;
        }

        public ErpPostExecutor<E> timeout(int timeout) {
            this.mTimeout = timeout;
            return this;
        }

        */
/**
 * 发送Request
 *
 * @param listener
 * @param tag
 *//*

        public void execute(ErpResponseListener<E> listener, Object tag) {
            ListenerAdapter<E> listenerAdapter = new ListenerAdapter<E>(listener);
            ErrorListenerAdapter errorListener = new ErrorListenerAdapter(listener);
            try {
                request = new ErpPostRequest<E>(mUrl, mResponseContentType, listenerAdapter, errorListener);
                request.setErpResponseProcessor(mProcessor);
            } catch (Exception e) {
                Log.e(TAG, "Create OpsGetRequest error!", e);
                String message = e.getMessage();
                Throwable throwable = e.getCause();
                if (throwable == null) {
                    throwable = e;
                }
                errorListener.onErrorResponse(new VolleyError(message, throwable));
                return;
            }

            LoadingErpResponseListener<E> loadingListener = null;
            if (listener instanceof LoadingErpResponseListener) {
                loadingListener = (LoadingErpResponseListener<E>) listener;
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
                RequestManagerCompat.addRequest(MainApplication.getInstance(), request, tag);
            }
        }

        */
/**
 * @param <T>

 * @version: 1.0
 * @date 2015年4月19日
 *//*

        private static class ListenerAdapter<T> implements Response.Listener<ErpResponseObject<T>> {

            private final ErpResponseListener<T> mListener;

            ListenerAdapter(ErpResponseListener<T> listener) {
                this.mListener = listener;
            }

            @Override
            public void onResponse(ErpResponseObject<T> response) {
                if (mListener != null) {
                    mListener.onResponse(response);
                }
            }
        }

        */
/**

 * @version: 1.0
 * @date 2015年4月19日
 *//*

        private static class ErrorListenerAdapter implements Response.ErrorListener {

            private final ErpResponseListener<?> mListener;

            ErrorListenerAdapter(ErpResponseListener<?> listener) {
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
                            msg = MainApplication.getInstance().getResources().getString(R.string.connect_network_error);
                        } else if (error instanceof ServerError) {
                            msg = MainApplication.getInstance().getResources().getString(R.string.connect_server_error);
                        } else if (error instanceof TimeoutError) {
                            msg = MainApplication.getInstance().getResources().getString(R.string.connect_timeout_error);
                        } else {
                            msg = MainApplication.getInstance().getResources().getString(R.string.connect_exception);
                        }
                        newError = new VolleyError(msg, error.getCause());
                    } else {
                        newError = error;
                    }
                    mListener.onError(newError);
                }
            }

        }

    }

}
*/
