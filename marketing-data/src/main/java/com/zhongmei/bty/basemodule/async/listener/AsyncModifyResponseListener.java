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
 * 异步改单回调
 * Created by demo on 2018/12/15
 */
public class AsyncModifyResponseListener extends EventResponseListener<TradeResp> implements AsyncResponseListener<TradeResp> {

    private final static String TAG = AsyncModifyResponseListener.class.getSimpleName();

    protected AsyncHttpRecord asyncRec;

    //public DinnerModifyPrintBean printBean;

    public boolean isDinner = true;//正餐使用解藕后的打印，自助暂时使用以前的那套

    public AsyncModifyResponseListener() {
    }

    public AsyncModifyResponseListener(UserActionEvent eventName) {
        super(eventName);
    }

    public AsyncHttpRecord getAsyncRec() {
        return asyncRec;
    }

    public void setAsyncRec(AsyncHttpRecord asyncRec) {
        this.asyncRec = asyncRec;
    }

    @Override
    public void onResponse(ResponseObject<TradeResp> response) {
        ToastUtil.showShortToast(response.getMessage());
        try {
            switch (response.getStatusCode()) {
                case ResponseObject.OK:
                    if (asyncRec != null) {
                        //PRTPrintOperator operator = new PRTPrintOperator();
                        //operator.printTradeModifyTicket(printBean);
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
