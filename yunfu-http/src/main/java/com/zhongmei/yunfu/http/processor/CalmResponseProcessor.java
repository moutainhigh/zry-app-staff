package com.zhongmei.yunfu.http.processor;

import android.util.Log;

import com.zhongmei.yunfu.net.builder.NetworkRequest;
import com.zhongmei.yunfu.resp.ResponseObject;

/**
 * Created by demo on 2018/12/15
 */
public abstract class CalmResponseProcessor<R> implements NetworkRequest.ResponseProcessor {
    private static final String TAG = CalmResponseProcessor.class.getName();

    protected boolean isSuccessful(ResponseObject response) {
        return ResponseObject.isOk(response) || ResponseObject.isExisted(response);
    }

    @Override
    public Object process(Object response) {
        if (!(response instanceof ResponseObject)) {
            throw new RuntimeException("response type is not compatiable to the define type");
        }
        ResponseObject responseObj = (ResponseObject) response;
        if (isSuccessful(responseObj) && responseObj.getContent() != null) {
            try {
                saveToDatabase((R) responseObj.getContent());
            } catch (Exception ex) {
                // block
                Log.e(TAG, "Save to database error!", ex);
            }
        }
        return response;
    }


    protected abstract void saveToDatabase(R resp) throws Exception;

}
