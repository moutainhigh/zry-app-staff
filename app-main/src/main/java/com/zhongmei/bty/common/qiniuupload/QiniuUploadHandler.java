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

/**
 * Created by demo on 2018/12/15
 */
public class QiniuUploadHandler {

    /**
     * 获取七牛上传的token
     *
     * @param responseListener 回调方法
     */
    public static void getUploadToken(ResponseListener<TokenResp> responseListener) {
        UploadOperates operates = OperatesFactory.create(UploadOperates.class);
        operates.requestToken(new TokenReq(1), responseListener);
    }

    /**
     * 上传文件到七牛
     *
     * @param file              需要上传的文件
     * @param key               自定义的key
     * @param token             上传的token
     * @param completionHandler 上传成功或者失败的回调
     * @param options           上传的参数设置
     */
    public static void doUpload(File file, String key, String token, UpCompletionHandler completionHandler, UploadOptions options) {
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(file, key, token, completionHandler, options);
    }

    /**
     * 一键上传，同时获取token和上传文件
     *
     * @param file              需要上传的文件
     * @param key               文件key
     * @param completionHandler 上传成功或者失败的回调
     * @param options           上传的参数设置
     */
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
