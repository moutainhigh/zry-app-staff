package com.zhongmei.yunfu.http;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import com.zhongmei.yunfu.net.RequestManagerCompat;
import com.zhongmei.yunfu.net.volley.Response.ErrorListener;
import com.zhongmei.yunfu.net.volley.Response.Listener;
import com.zhongmei.yunfu.net.volley.toolbox.ImageRequest;
import com.zhongmei.yunfu.context.base.BaseApplication;

public class CalmImageRequest extends ImageRequest {

    public CalmImageRequest(String url, Listener<Bitmap> listener,
                            int maxWidth, int maxHeight, Config decodeConfig,
                            ErrorListener errorListener) {
        super(url, listener, maxWidth, maxHeight, decodeConfig, errorListener);
    }

    public void executeRequest(String tag, boolean displayDialog,
                               String hintInfo) {
        RequestManagerCompat.addRequest(BaseApplication.sInstance, this, tag);
    }

}
