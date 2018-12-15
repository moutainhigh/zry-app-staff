package com.zhongmei.yunfu.http;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.context.UILoadingController;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.util.Gsons;
import com.zhongmei.yunfu.http.utils.RequestRetryUtil;
import com.zhongmei.yunfu.monitor.EventListener;
import com.zhongmei.yunfu.monitor.EventResponseProcessorProxy;
import com.zhongmei.yunfu.net.RequestManagerCompat;
import com.zhongmei.yunfu.net.volley.NetworkError;
import com.zhongmei.yunfu.net.volley.NoConnectionError;
import com.zhongmei.yunfu.net.volley.Response.ErrorListener;
import com.zhongmei.yunfu.net.volley.ServerError;
import com.zhongmei.yunfu.net.volley.TimeoutError;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @param <Q>Request的Content类型
 * @param <E>                  Response的Content类型
 * @version: 1.0
 * @date 2015年4月15日
 */
public class OpsRequest<Q, E> extends CalmGsonRequest<RequestObject<Q>, ResponseObject<E>> {

    private static final String TAG = OpsRequest.class.getSimpleName();

    private static final int TIMEOUT_MS = 10000;
    private int mHeaderType;
    private ResponseProcessor<E> mProcessor;

    private ResponseListener<E> responseListener;

    private OpsRequest(String url, Q requestContent, Type responseContentType, ListenerAdapter<E> listener,
                       ErrorListenerAdapter errorListner) {
        super(url, RequestObject.create(requestContent, RequestRetryUtil.isUrlCanServiceRetry(url)), responseContentType, listener, errorListner);
        //setDataIntoHeader();
    }

    private OpsRequest(int method, String url, Q requestContent, Type responseContentType, ListenerAdapter<E> listener,
                       ErrorListenerAdapter errorListner) {
        super(method, url, RequestObject.create(requestContent, RequestRetryUtil.isUrlCanServiceRetry(url)), responseContentType, listener, errorListner);
        //setDataIntoHeader();
    }

    private OpsRequest(String url, String body, Type responseContentType, ListenerAdapter<E> listener,
                       ErrorListenerAdapter errorListner) {
        super(url, body, responseContentType, listener, errorListner);
        //setDataIntoHeader();
    }

    private OpsRequest(int method, String url, String body, Type responseContentType, ListenerAdapter<E> listener,
                       ErrorListenerAdapter errorListner) {
        super(method, url, body, responseContentType, listener, errorListner);
        //setDataIntoHeader();
    }

    @Override
    protected ResponseObject<E> toResponseValue(String json, Type responseType) throws Exception {
        ResponseObject<E> response = super.toResponseValue(json, responseType);
        if (mProcessor != null) {
            return mProcessor.process(response);
        }
        return response;
    }

    public void setHeaderType(int headerType) {
        this.mHeaderType = headerType;
    }

    /**
     * 设置ResponseObject的处理器
     *
     * @param processor
     */
    void setResponseProcessor(ResponseProcessor<E> processor) {
        this.mProcessor = processor;
        if (processor != null) {
            this.mProcessor = new EventResponseProcessorProxy(processor, EventListenerProxy.getEventName(mListener), getMethodString(), getUrl(), getBodyString());
        }
    }

    /**
     * 获取指定Content类型的ResponseObject对象的类型
     *
     * @param contentType
     * @return
     */
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

    /**
     * 获取当ResponseObject的content是List<?>时的Type
     *
     * @param classType content为List时列表元素的对象类型
     * @return
     */
    public static <T> Type getListContentResponseType(Class<T> classType) {
        Type type = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, List.class, classType);
        return getResponseType(type);
    }

    public static <Q, E> Type getContentResponseType(Class<Q> rawType, Class<E> classType) {
        Type type = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, rawType, classType);
        return getResponseType(type);
    }

    /**
     * ResponseObject处理器。
     * 在从网络获取到数据并转换成ResponseObject对象后将使用此处理器先进行处理(如保存到数据库)
     *
     * @param <E>
     */
    static public interface ResponseProcessor<E> {

        /**
         * 对response进行处理
         *
         * @param response
         * @return
         */
        ResponseObject<E> process(ResponseObject<E> response);
    }

    /**
     * 将数据保存到数据库的处理器
     *
     * @param <E>
     * @version: 1.0
     * @date 2015年4月15日
     */
    public static abstract class SaveDatabaseResponseProcessor<E> implements ResponseProcessor<E> {

        public SaveDatabaseResponseProcessor() {
        }

        protected boolean isSuccessful(ResponseObject<E> response) {
            return ResponseObject.isOk(response) || ResponseObject.isExisted(response);
        }

        @Override
        public ResponseObject<E> process(final ResponseObject<E> response) {
            if (isSuccessful(response) && response.getContent() != null) {

                try {
                    saveToDatabase(response.getContent());
                } catch (Exception ex) {
                    // TODO Auto-generated catch
                    // block
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

        /**
         * 保存旧表数据到数据库中
         *
         * @param classType
         * @param datas
         */
		/*protected void saveBaseInfo(Class<? extends DataBaseInfo> classType, List<JsonObject> datas) {
			if (datas != null && !datas.isEmpty()) {
				Gson gson = new Gson();
				String json = gson.toJson(datas);
				JSONArray jsonArray;
				try {
					jsonArray = new JSONArray(json);
					BatchOperation batchOp = new BatchOperation(BaseApplication.sInstance.getContentResolver());
					BaseSyncManager syncMgr = new BaseSyncManager(classType);
					syncMgr.syncArrayToLocal(jsonArray, false, batchOp, true);
					batchOp.execute();
				} catch (JSONException e) {
					Log.e(TAG, "Save BaseInfo error! " + classType, e);
				}
			}
		}*/

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
     * @param <E>
     * @version: 1.0
     * @date 2016/12/21
     */
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

        /**
         * 只需关心业务数据处事务已经在上层处理了
         *
         * @param helper
         * @param resp
         */
        protected abstract void transactionCallable(DatabaseHelper helper, E resp) throws Exception;
    }

    /**
     * @param <T>
     * @version: 1.0
     * @date 2015年4月19日
     */
    private static class ListenerAdapter<T> extends EventListener<ResponseObject<T>> {

        private final ResponseListener<T> mListener;

        ListenerAdapter(ResponseListener<T> listener) {
            super(EventResponseListener.getEventName(listener));
            this.mListener = listener;
        }

        @Override
        public void onResponse(ResponseObject<T> response) {
            if (mListener != null) {
                mListener.onResponse(response);
            }
        }
    }

    /**
     * @version: 1.0
     * @date 2015年4月19日
     */
    private static class ErrorListenerAdapter implements ErrorListener {

        private final ResponseListener<?> mListener;

        ErrorListenerAdapter(ResponseListener<?> listener) {
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

        private OpsRequest<Q, E> request;

        private String mUrl;

        private Q mRequestContent;

        private String body;//转换好的json请求体，提前调用getRequestJson会生成，否则走之前mRequestContent的模式

        private Type mResponseContentType;

        private ResponseProcessor<E> mProcessor;

        private Integer mTimeout;

        private boolean interceptEnable;//异步拦截开关
        private List<String> router = new ArrayList<>();//异步Task路由匹配

        private boolean isDefineMethod;//是否定义的http 方式

        private int mMethod = Method.POST;//http方式

        private int mHeaderType = HeaderType.HEADER_TYPE_SYNC;//header类型

        private Executor(String url) {
            this.mUrl = url;
            this.mRequestContent = null;
            this.mProcessor = null;
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

        public Executor<Q, E> responseProcessor(ResponseProcessor<E> processor) {
            this.mProcessor = processor;
            return this;
        }

        public Executor<Q, E> timeout(int timeout) {
            this.mTimeout = timeout;
            return this;
        }

        public Executor<Q, E> setMethod(int method) {
            this.isDefineMethod = true;
            this.mMethod = method;
            return this;
        }

        public Executor<Q, E> setHeaderType(int headerType) {
            this.mHeaderType = headerType;
            return this;
        }

        public Executor<Q, E> interceptEnable(boolean interceptEnable) {
            this.interceptEnable = interceptEnable;
            return this;
        }

        public Executor<Q, E> addRouter(String name) {
            this.router.add(name);
            return this;
        }

        /**
         * 发送Request
         *
         * @param listener
         * @param tag
         */
        public void execute(ResponseListener<E> listener, Object tag) {
            execute(listener, tag, false);
        }

        /**
         * 发送Request
         *
         * @param listener
         * @param tag
         */
        public void execute(ResponseListener<E> listener, Object tag, boolean isBody) {
            ListenerAdapter<E> listenerAdapter = new ListenerAdapter<>(listener);
            ErrorListenerAdapter errorListener = new ErrorListenerAdapter(listener);
            try {
                if (isBody) {
                    if (this.isDefineMethod) {
                        //外层传入进行的就是一个body对象
                        request = new OpsRequest<Q, E>(mMethod, mUrl, mRequestContent == null ? "" : mRequestContent.toString(), mResponseContentType, listenerAdapter, errorListener);
                    } else {
                        //外层传入进行的就是一个body对象
                        request = new OpsRequest<Q, E>(mUrl, mRequestContent == null ? "" : mRequestContent.toString(), mResponseContentType, listenerAdapter, errorListener);
                    }
                } else {
                    if (this.isDefineMethod) {
                        // body是转换好的json请求体，提前调用getRequestJson会生成，否则走之前mRequestContent的模式
                        if (!TextUtils.isEmpty(body)) {
                            request = new OpsRequest<Q, E>(mMethod, mUrl, body, mResponseContentType, listenerAdapter, errorListener);
                        } else {
                            request = new OpsRequest<Q, E>(mMethod, mUrl, mRequestContent, mResponseContentType, listenerAdapter, errorListener);
                        }
                    } else {
                        // body是转换好的json请求体，提前调用getRequestJson会生成，否则走之前mRequestContent的模式
                        if (!TextUtils.isEmpty(body)) {
                            request = new OpsRequest<Q, E>(mUrl, body, mResponseContentType, listenerAdapter, errorListener);
                        } else {
                            request = new OpsRequest<Q, E>(mUrl, mRequestContent, mResponseContentType, listenerAdapter, errorListener);
                        }
                    }
                }
                request.setHttpProperty(new DefaultHeaderInterceptor(request.getUrl()).getHeaders(this.mHeaderType));
                request.setHeaderType(this.mHeaderType);
                request.setResponseProcessor(mProcessor);
                request.setInterceptEnable(interceptEnable);
                request.addRouter(router);
                request.responseListener = listener;
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

        public String getRequestJson() {
            if (request != null) {
                return request.getBodyStr();
            } else {
                if (mRequestContent != null) {
                    Gson gson = Gsons.gsonBuilder().create();
                    body = gson.toJson(RequestObject.create(mRequestContent, RequestRetryUtil.isUrlCanServiceRetry(getUrl())));
                    return body;
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    protected boolean serviceRetry() {
        try {
            OpsRequest request = clone();
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
    protected OpsRequest clone() throws CloneNotSupportedException {
        ResponseListener<E> listener = this.responseListener;
        ListenerAdapter<E> listenerAdapter = new ListenerAdapter(listener);
        ErrorListenerAdapter errorListener = new ErrorListenerAdapter(listener);

        OpsRequest request = null;
        if (!TextUtils.isEmpty(body)) {//使用之前生成好的body进行clone
            request = new OpsRequest(this.getUrl(), this.body, this.mResponseType, listenerAdapter, errorListener);
            request.setHttpProperty(new DefaultHeaderInterceptor(request.getUrl()).getHeaders(this.mHeaderType));
            request.setTag(this.getTag());
            request.setResponseProcessor(this.mProcessor);
            request.setInterceptEnable(this.getInterceptEnable());
            request.responseListener = listener;
            request.setTimeout(TIMEOUT_MS);
//		} else {
//			request = new OpsRequest(this.getUrl(), this.mRequestObject.getContent(), this.mResponseType, listenerAdapter, errorListener);
        }
        return request;
    }
}
