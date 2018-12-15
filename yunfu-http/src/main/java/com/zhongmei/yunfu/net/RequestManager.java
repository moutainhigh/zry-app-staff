package com.zhongmei.yunfu.net;

import android.content.Context;

import com.zhongmei.yunfu.net.volley.Request;
import com.zhongmei.yunfu.net.volley.RequestQueue;
import com.zhongmei.yunfu.net.volley.toolbox.Volley;

/**
 * @Date：2014年11月4日 上午11:22:06
 * @Description: 封装网络请求队列
 * @Version: 1.0
 */
public class RequestManager {

    public interface RequestFilter {
        boolean filter(Context context, Request<?> request, Object tag);
    }

    /**
     * @date：2014年11月4日 上午11:22:22
     * @Description:网络请求队列的实体
     */
    private static RequestQueue mRequestQueue;
    private static RequestFilter mRequestFilter;
    private static boolean isInit;

    /**
     * @Constructor
     * @Description 生成一个请求队列的管理实体对象
     */
    private RequestManager() {
    }

    /**
     * @Title: init
     * @Description: 初始化网络请求队列
     * @Param @param context 请求队列需要的context
     * @Return void 返回类型
     */
    public static void init(Context context) {
        init(context, null);
    }

    public static void init(Context context, RequestFilter filter) {
        //ArgsUtils.mustInMainThread();
        if (!isInit) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
            mRequestFilter = filter != null ? filter : new RequestFilter() {
                @Override
                public boolean filter(Context context, Request<?> request, Object tag) {
                    return false;
                }
            };
            isInit = true;
        }
    }

    /**
     * @Title: getRequestQueue
     * @Description: 获取当前的请求队列
     * @Param @return 返回当前队列，没有返回异常
     * @Return RequestQueue 返回类型
     */
    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    /**
     * @Title: addRequest
     * @Description: 添加一个网络请求到队列中
     * @Param @param request 需要添加的网络请求
     * @Param @param tag 此队列的标签，便于分组请求
     * @Return void 返回类型
     */
    public static void addRequest(Context context, Request<?> request, Object tag) {
        if (mRequestQueue == null) {
            throw new RuntimeException("Please call init-method at first");
        }
        if (request == null) {
            throw new NullPointerException("Request is null");
        }
        if (tag != null) {
            request.setTag(tag);
        }
        if (!mRequestFilter.filter(context, request, tag)) {
            mRequestQueue.add(request);
        }
    }

    /**
     * @Title: cancelAll
     * @Description: 取消某一个标签下的所有网络请求
     * @Param @param tag 需要过滤的网络请求标签
     * @Return void 返回类型
     */
    public static void cancelAll(Object tag) {
        if (mRequestQueue == null) {
            return;
        }
        mRequestQueue.cancelAll(tag);
    }

}
