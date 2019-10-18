package com.zhongmei.bty.basemodule.async.listener;

import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.basemodule.pay.message.PaymentResp;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseObject;


public class AsyncPayResponseListener implements AsyncResponseListener<PaymentResp> {

    private final static String TAG = AsyncPayResponseListener.class.getSimpleName();

    private AsyncHttpRecord asyncRec;

    public AsyncHttpRecord getAsyncRec() {
        return asyncRec;
    }

    public void setAsyncRec(AsyncHttpRecord asyncRec) {
        this.asyncRec = asyncRec;
    }

    @Override
    public void onResponse(ResponseObject<PaymentResp> response) {
        switch (response.getStatusCode()) {
            case ResponseObject.OK:
                if (asyncRec != null && !TextUtils.isEmpty(asyncRec.getTradeUuId())) {
                    String tradeUuid = asyncRec.getTradeUuId();

                                    }
                break;
                    }

    }

    @Override
    public void onError(VolleyError error) {
        try {
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }


}
