package com.zhongmei.bty.basemodule.async.listener;

import android.util.Log;

import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.util.ToastUtil;

/**
 * Created by demo on 2018/12/15
 */

public class AsyncBatchModifyResponseListener extends EventResponseListener<TradeResp> implements AsyncResponseListener<TradeResp> {

    private final static String TAG = AsyncBatchModifyResponseListener.class.getSimpleName();

    protected AsyncHttpRecord asyncRec;

    //public DinnerModifyPrintBean printBean;

    @Override
    public AsyncHttpRecord getAsyncRec() {
        return asyncRec;
    }

    @Override
    public void setAsyncRec(AsyncHttpRecord asyncRec) {
        this.asyncRec = asyncRec;
    }

    public AsyncBatchModifyResponseListener() {
    }

    public AsyncBatchModifyResponseListener(UserActionEvent event) {
        super(event);
    }

    @Override
    public void onResponse(ResponseObject<TradeResp> response) {
        ToastUtil.showShortToast(response.getMessage());
        try {
            switch (response.getStatusCode()) {
                case ResponseObject.OK:
                    if (asyncRec != null) {
                        //PRTPrintOperator operator = new PRTPrintOperator();
                        //operator.printUnionMainTradeModifyTicket(asyncRec.getPrintBeanJson());
                        AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_CHANGE_ORDER, asyncRec.getTradeId(), asyncRec.getTradeUuId(), asyncRec.getTradeUpdateTime());
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onError(VolleyError error) {
        Log.e(TAG, error.getMessage(), error);
        ToastUtil.showShortToast(error.getMessage());
    }
}
