package com.zhongmei.bty.basemodule.async.listener;

import android.util.Log;

import com.zhongmei.bty.basemodule.async.util.AsyncNetworkUtil;
import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpType;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.basemodule.async.operates.AsyncDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
public class AsyncOpenTableResponseListener extends EventResponseListener<TradeResp> implements AsyncResponseListener<TradeResp> {

    private final static String TAG = AsyncOpenTableResponseListener.class.getSimpleName();

    protected AsyncHttpRecord asyncRec;
    private AsyncHttpRecord asyncSourceRec;//引发开台的异步操作记录，如果没有则为null

    public AsyncOpenTableResponseListener() {
    }

    public AsyncOpenTableResponseListener(UserActionEvent eventName) {
        super(eventName);
    }

    public AsyncOpenTableResponseListener(String eventName) {
        super(eventName);
    }

    @Override
    public AsyncHttpRecord getAsyncRec() {
        return asyncRec;
    }

    @Override
    public void setAsyncRec(AsyncHttpRecord asyncRec) {
        this.asyncRec = asyncRec;
    }

    public AsyncHttpRecord getAsyncSourceRec() {
        return asyncSourceRec;
    }

    public void setAsyncSourceRec(AsyncHttpRecord asyncSourceRec) {
        this.asyncSourceRec = asyncSourceRec;
    }

    @Override
    public void onResponse(ResponseObject<TradeResp> response) {
        try {
            if (ResponseObject.isOk(response)) {

                if (response.getContent() != null && Utils.isNotEmpty(response.getContent().getTrades())) {
                    Trade trade = response.getContent().getTrades().get(0);
                    AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_START_DESK, trade.getId(), trade.getUuid(), trade.getClientUpdateTime());
                }
                //成功后，重试引发开台的异步操作
                if (asyncSourceRec != null && asyncSourceRec.getType() != null) {
                    AsyncNetworkUtil.retryModifyOrCasher(asyncSourceRec);
                    //查询这个订单对应的其他异步操作
                } else {
                    AsyncDal asyncDal = OperatesFactory.create(AsyncDal.class);
                    List<AsyncHttpType> types = new ArrayList<>();
                    types.add(AsyncHttpType.CASHER);
                    types.add(AsyncHttpType.MODIFYTRADE);
                    types.add(AsyncHttpType.UNION_MAIN_MODIFYTRADE);
                    types.add(AsyncHttpType.UNION_SUB_MODIFYTRADE);

                    if (asyncRec != null) {
                        List<AsyncHttpRecord> asyncRecs = asyncDal.query(asyncRec.getTradeUuId(), types);
                        if (Utils.isNotEmpty(asyncRecs) && asyncRecs.get(0).getType() != null) {
                            AsyncNetworkUtil.retryModifyOrCasher(asyncRecs.get(0));
                        }
                    }
                }
//            } else {//删除开台异步操作记录本身，和引发开台的异步操作记录
//                DBHelper.deleteById(AsyncHttpRecord.class, asyncRec.getUuid());
//                if(asyncSourceRec != null) {
//                    DBHelper.deleteById(AsyncHttpRecord.class, asyncSourceRec.getUuid());
//                }
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        UserActionEvent.end(getEventName());
    }

    @Override
    public void onError(VolleyError error) {

    }

}
