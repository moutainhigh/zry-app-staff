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

/**
 * @param <Q>Request的Content类型
 * @param <E>                  Response的Content类型
 * @version: 1.0
 * @date 2015年4月15日
 */
public class QSOpsRequest<Q, E> extends CalmGsonRequest<QSRequestObject<Q>, QSResponseObject<E>> {

    private static final String TAG = QSOpsRequest.class.getSimpleName();

    private static final int TIMEOUT_MS = 10000;

    private QSResponseProcessor<E> mProcessor;

    private QSOpsRequest(String url, Q requestContent, Type responseContentType, ListenerAdapter<E> listener,
                         ErrorListenerAdapter errorListner) {
        super(url, QSRequestObject.create(requestContent), responseContentType, listener, errorListner);
//		setHttpProperty(Constant._KRY_GLOBAL_MSG_ID, UUID.randomUUID().toString());
        setDataIntoHeader();
    }

    private QSOpsRequest(String url, String body, Type responseContentType, ListenerAdapter<E> listener,
                         ErrorListenerAdapter errorListner) {
        super(url, body, responseContentType, listener, errorListner);
//		setHttpProperty(Constant._KRY_GLOBAL_MSG_ID, UUID.randomUUID().toString());
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

		/*setHttpProperty(Constant._KRY_GLOBAL_MSG_ID, UUID.randomUUID().toString());

		//添加语言
		setHttpProperty(Constant.KRYSYNCLOCALE, SharedPreferenceUtil.getSpUtil().getString(Constant.DEFAULTLANGUAGE,""));
		//添加api安全认证信息

		Map<String,String> apiSignMap = BaseApplication.sInstance.tokenEncrypt();
		if(apiSignMap != null && apiSignMap.size() > 0){
			setHttpProperty(Constant.KRY_API_TOKEN, SharedPreferenceUtil.getSpUtil().getString(Constant.TOKENENCRYPT, ""));
			setHttpProperty(Constant.APISIGN, apiSignMap.get("token"));
			setHttpProperty(Constant.APITIMESTAMP, apiSignMap.get("time"));
		}
		setHttpProperty(Constant.APISHOPID, ShopInfoCfg.getInstance().shopId);
		setHttpProperty(Constant.APIDEVICEID, SystemUtils.getMacAddress());
		setHttpProperty(Constant.BRANDID, ShopInfoCfg.getInstance().commercialGroupId);*/
        DefaultHeaderInterceptor headerInterceptor = new DefaultHeaderInterceptor(getUrl());
        this.setHttpProperty(headerInterceptor.getDefaultHeader());
    }

    /**
     * 设置ResponseObject的处理器
     *
     * @param processor
     */
    void setResponseProcessor(QSResponseProcessor<E> processor) {
        this.mProcessor = processor;
    }

    /**
     * 获取指定Content类型的ResponseObject对象的类型
     *
     * @param contentType
     * @return
     */
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

    /**
     * 获取当ResponseObject的content是List<?>时的Type
     *
     * @param classType content为List时列表元素的对象类型
     * @return
     */
    static <T> Type getListContentResponseType(Class<T> classType) {
        Type type = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, List.class, classType);
        return getResponseType(type);
    }

    public static <Q, E> Type getContentResponseType(Class<Q> rawType, Class<E> classType) {
        Type type = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, rawType, classType);
        return getResponseType(type);
    }

    /**
     * QSResponseObject处理器。
     * 在从网络获取到数据并转换成QSResponseObject对象后将使用此处理器先进行处理(如保存到数据库)
     *
     * @param <E>
     */
    static interface QSResponseProcessor<E> {

        /**
         * 对response进行处理
         *
         * @param response
         * @return
         */
        QSResponseObject<E> process(QSResponseObject<E> response);
    }

    /**
     * 将数据保存到数据库的处理器
     *
     * @param <E>
     * @version: 1.0
     * @date 2015年4月15日
     */
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

        /**
         * 回复操作成功后将调用此方法将回复数据保存到数据库中
         *
         * @param resp
         */
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

        /**
         * 返回要在同一个数据库事务中执行的操作
         *
         * @param helper
         * @param resp
         * @return
         */
        protected abstract Callable<Void> getCallable(final DatabaseHelper helper, final E resp) throws Exception;

    }

    /**
     * @param <T>
     * @version: 1.0
     * @date 2015年4月19日
     */
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

    /**
     * @version: 1.0
     * @date 2015年4月19日
     */
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

    /**
     * 负责发送Request
     *
     * @param <Q>请求的数据类型
     * @param <E>        回复的数据类型
     * @version: 1.0
     * @date 2015年4月19日
     */
    public static class Executor<Q, E> {

        public static <Q, E> Executor<Q, E> create(String url) {
            return new Executor<Q, E>(url);
        }

        private QSOpsRequest<Q, E> request;

        private String mUrl;

        private Q mRequestContent;

        private String body;//转换好的json请求体，提前调用getRequestJson会生成，否则走之前mRequestContent的模式

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

        /**
         * 发送Request
         *
         * @param listener
         * @param tag
         */
        public void execute(QSResponseListener<E> listener, Object tag) {
            execute(listener, tag, false);
        }

        /**
         * 发送Request
         *
         * @param listener
         * @param tag
         */
        public void execute(QSResponseListener<E> listener, Object tag, boolean isBody) {
            ListenerAdapter<E> listenerAdapter = new ListenerAdapter<E>(listener);
            ErrorListenerAdapter errorListener = new ErrorListenerAdapter(listener);
            try {
                if (isBody) {
                    //外层传入进行的就是一个body对象
                    request =
                            new QSOpsRequest<Q, E>(mUrl, mRequestContent.toString(), mResponseContentType, listenerAdapter, errorListener);
                } else {
                    // body是转换好的json请求体，提前调用getRequestJson会生成，否则走之前mRequestContent的模式
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
