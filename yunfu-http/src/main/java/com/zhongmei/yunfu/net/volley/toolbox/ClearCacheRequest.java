

package com.zhongmei.yunfu.net.volley.toolbox;

import android.os.Handler;
import android.os.Looper;

import com.zhongmei.yunfu.net.volley.Cache;
import com.zhongmei.yunfu.net.volley.NetworkResponse;
import com.zhongmei.yunfu.net.volley.Request;
import com.zhongmei.yunfu.net.volley.Response;


public class ClearCacheRequest extends Request<Object> {
    private final Cache mCache;
    private final Runnable mCallback;


    public ClearCacheRequest(Cache cache, Runnable callback) {
        super(Method.GET, null, null);
        mCache = cache;
        mCallback = callback;
    }

    @Override
    public boolean isCanceled() {
                mCache.clear();
        if (mCallback != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postAtFrontOfQueue(mCallback);
        }
        return true;
    }

    @Override
    public Priority getPriority() {
        return Priority.IMMEDIATE;
    }

    @Override
    protected Response<Object> parseNetworkResponse(NetworkResponse response) {
        return null;
    }

    @Override
    protected void deliverResponse(Object response) {
    }
}
