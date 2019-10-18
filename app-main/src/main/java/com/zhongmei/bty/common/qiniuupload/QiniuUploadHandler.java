package com.zhongmei.bty.common.qiniuupload;

import android.os.Looper;
import android.text.TextUtils;

import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.data.operates.UploadOperates;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.data.operates.message.content.TokenReq;
import com.zhongmei.bty.data.operates.message.content.TokenResp;
import com.zhongmei.yunfu.net.volley.VolleyError;

import java.io.File;


public class QiniuUploadHandler {


    public static void getUploadToken(ResponseListener<TokenResp> responseListener) {
        UploadOperates operates = OperatesFactory.create(UploadOperates.class);
        operates.requestToken(new TokenReq(1), responseListener);
    }


    public static void doUpload(File file, String key, String token, UpCompletionHandler completionHandler, UploadOptions options) {
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(file, key, token, completionHandler, options);
    }


    public static void doOneKeyUpload(final File file, final String key, final UpCompletionHandler completionHandler, final UploadOptions options) {
        getUploadToken(new ResponseListener<TokenResp>() {
            @Override
            public void onResponse(ResponseObject<TokenResp> response) {
                if (ResponseObject.isOk(response)
                        && response.getContent() != null
                        && (!TextUtils.isEmpty(response.getContent().getData()))) {
                    final String token = response.getContent().getData();
                    if (Looper.myLooper() != Looper.getMainLooper()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                doUpload(file, key, token, completionHandler, options);
                            }
                        }).start();
                    } else {
                        doUpload(file, key, token, completionHandler, options);
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
}
