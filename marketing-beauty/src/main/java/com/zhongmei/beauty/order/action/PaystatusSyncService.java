package com.zhongmei.beauty.order.action;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.basemodule.trade.message.TradePayStateResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;

import java.util.ArrayList;
import java.util.List;



public class PaystatusSyncService extends Service {

    private final long START_TIME_STAMP = 10 * 1000;
    private final long CYCLE_SYNC_TIME_STEP = 30 * 1000;
    private final int CYCLE_COUNT = 3;
    private HandlerThread handlerThread;
    private WorkHandler mWorkHanlder;

    private List<PayStatusRecord> mRequestQueueList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        Long tradeId = intent.getLongExtra("tradeId", 0L);
        Long paymentItemId = intent.getLongExtra("paymentItemId", 0L);
        initData(url, tradeId, paymentItemId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initData(String url, Long tradeId, Long paymentItemId) {
        if (!TextUtils.isEmpty(url) && tradeId != null && paymentItemId != null) {
            mRequestQueueList.add(new PayStatusRecord(url, tradeId, paymentItemId));

                        if (mRequestQueueList.size() == 1) {
                sendWorkMessage(0, START_TIME_STAMP);
            }
        }
    }


    private void sendWorkMessage(int idx, Long timeStamp) {
        Bundle bundle = new Bundle();
        bundle.putInt("index", idx);
        Message msg = mWorkHanlder.obtainMessage();
        msg.setData(bundle);
        mWorkHanlder.sendMessageDelayed(msg, timeStamp);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        handlerThread = new HandlerThread("workHandler");
        handlerThread.start();
        mWorkHanlder = new WorkHandler(handlerThread.getLooper());
    }


    class WorkHandler extends Handler {

        public WorkHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                        int index = msg.getData().getInt("index", -1);

                        if (mRequestQueueList != null && mRequestQueueList.size() <= 0) {
                return;
            }

                        if (mRequestQueueList != null && mRequestQueueList.size() <= index) {
                sendWorkMessage(0, CYCLE_SYNC_TIME_STEP);
                return;
            }

                        if (index >= 0 && mRequestQueueList.size() > index) {
                PayStatusRecord record = mRequestQueueList.get(index);
                refreshTradePayStatus(index, record.getTradeId(), record.getPaymentItemId());
            }


        }
    }

    private boolean isReturnSuccess(List<PaymentItem> paymentItems){
        if(Utils.isNotEmpty(paymentItems)){
            PaymentItem paymentItem=paymentItems.get(0);
            return !paymentItem.getPayStatus().equalsValue(TradePayStatus.REFUNDING.value());
        }

        return false;
    }

    private void refreshTradePayStatus(final int index, Long tradeId, Long paymentItemId) {
        Log.e("SyncPayStatus","启动查询。。。。tradeId:"+tradeId+",paymentItemid:"+paymentItemId);
        TradeOperates mTradeOperates = OperatesFactory.create(TradeOperates.class);
        mTradeOperates.refreshState(tradeId, paymentItemId, new ResponseListener<TradePayStateResp>() {

            @Override
            public void onResponse(ResponseObject<TradePayStateResp> response) {
                int curIdx = index;
                if (ResponseObject.isOk(response) && isReturnSuccess(response.getContent().getPaymentItems())) {
                                        if (mRequestQueueList != null && mRequestQueueList.size() > index) {
                        mRequestQueueList.remove(index);
                    }
                } else {
                                        if (mRequestQueueList != null && mRequestQueueList.size() > index) {
                        PayStatusRecord record = mRequestQueueList.get(index);
                        record.setTryCount(record.getTryCount() + 1);
                        curIdx++;
                    }
                }

                sendWorkMessage(curIdx, 0L);
            }

            @Override
            public void onError(VolleyError error) {
                if (mRequestQueueList != null && mRequestQueueList.size() > index) {
                    PayStatusRecord record = mRequestQueueList.get(index);
                    record.setTryCount(record.getTryCount() + 1);
                    sendWorkMessage(index + 1, 0L);
                }
            }
        });
    }
}
