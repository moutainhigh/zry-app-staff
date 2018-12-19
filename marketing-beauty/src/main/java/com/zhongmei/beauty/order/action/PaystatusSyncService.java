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

/**
 * Created by demo on 2018/12/15
 * 退货退款支付状态同步
 * 主要流程：pos发起退货成功后，如果paymentItem中有第三方支付，退款结果无法返回，由客户端发起轮训。
 * 一个成功的退货后10s开始轮训，轮训失败后30s后开始轮训，轮训3次失败后，移除轮训队列
 */

public class PaystatusSyncService extends Service {

    private final long START_TIME_STAMP = 10 * 1000;//10后开始轮训

    private final long CYCLE_SYNC_TIME_STEP = 30 * 1000;//一次轮训之后的间隔时间

    private final int CYCLE_COUNT = 3;//轮训次数

    private HandlerThread handlerThread;
    private WorkHandler mWorkHanlder;

    private List<PayStatusRecord> mRequestQueueList = new ArrayList<>();//轮训队列


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

            //如果只有新加的一条记录，需要10s之后开始发起轮训
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
            //网络请求
            int index = msg.getData().getInt("index", -1);

            //如果没有了，就返回
            if (mRequestQueueList != null && mRequestQueueList.size() <= 0) {
                return;
            }

            //轮训以前之后，休息30s
            if (mRequestQueueList != null && mRequestQueueList.size() <= index) {
                sendWorkMessage(0, CYCLE_SYNC_TIME_STEP);
                return;
            }

            //发起请求
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
                    //请求下一个，移除当前这个
                    if (mRequestQueueList != null && mRequestQueueList.size() > index) {
                        mRequestQueueList.remove(index);
                    }
                } else {
                    //当前的+1
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
