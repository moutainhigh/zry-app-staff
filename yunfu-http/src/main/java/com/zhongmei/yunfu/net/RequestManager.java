package com.zhongmei.yunfu.net;

import android.content.Context;

import com.zhongmei.yunfu.net.volley.Request;
import com.zhongmei.yunfu.net.volley.RequestQueue;
import com.zhongmei.yunfu.net.volley.toolbox.Volley;


public class RequestManager {

    public interface RequestFilter {
        boolean filter(Context context, Request<?> request, Object tag);
    }


    private static RequestQueue mRequestQueue;
    private static RequestFilter mRequestFilter;
    private static boolean isInit;


    private RequestManager() {
    }


    public static void init(Context context) {
        init(context, null);
    }

    public static void init(Context context, RequestFilter filter) {
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


    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }


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


    public static void cancelAll(Object tag) {
        if (mRequestQueue == null) {
            return;
        }
        mRequestQueue.cancelAll(tag);
    }

}
