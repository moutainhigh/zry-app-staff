package com.zhongmei.bty.cashier.tradedeal;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.yunfu.db.enums.ActionType;
import com.zhongmei.bty.commonmodule.database.enums.TradeDealSettingOperateType;
import com.zhongmei.bty.basemodule.trade.bean.TradeDealSettingVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.net.volley.VolleyError;

import java.util.List;

/**
 * 外卖订单自动拒绝管理类
 */
public class TradeAutoRefuseManager {
    private static final String TAG = TradeAutoRefuseManager.class.getSimpleName();
    //最大行数上限
    private static final long MAX_ROWS = 50L;
    boolean isRefuseing = false;// 正在拒绝单据
    private TradeDealManager mTradeDealManager;
    TradeAutoRefuseAsyncTask mTask;

    public TradeAutoRefuseManager(TradeDealManager tradeDealManager) {
        mTradeDealManager = tradeDealManager;
        isRefuseing = false;
    }


    public void startRefuseTrade() {
        //正在拒绝订单，则不作任何处理
        if (isRefuseing) {
            return;
        }

        Log.e(TAG, "startRefuseTrade");
        if (mTask != null) {
            mTask.cancel(true);
        }

        mTask = new TradeAutoRefuseAsyncTask();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else {
            mTask.execute();
        }
    }

    public void setRefuseing(boolean refuseing) {
        isRefuseing = refuseing;
    }

    /**
     * 批量拒绝订单
     *
     * @param tradeVos 订单列表
     */
    private void refuseTradeBatch(List<TradeVo> tradeVos) {
        Reason refuseReason = new Reason();
        refuseReason.setContent(mTradeDealManager.getContext().getString(R.string.reject_reason_timeout));

        TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);
        tradeOperates.refuseBatch(ActionType.AUTO, refuseReason, tradeVos, new ResponseListener<TradeResp>() {

            @Override
            public void onResponse(ResponseObject<TradeResp> response) {
                setRefuseing(false);
            }

            @Override
            public void onError(VolleyError error) {
                setRefuseing(false);
            }

        });
    }

    /**
     * 自动拒绝异步任务
     */
    class TradeAutoRefuseAsyncTask extends AsyncTask<Void, Void, List<TradeVo>> {
        @Override
        protected List<TradeVo> doInBackground(Void... params) {
            // 自动拒绝后台设置
            TradeDealSettingVo refuseVo = mTradeDealManager.findTradeDealSetting(TradeDealSettingOperateType.REFUSE);
            //后台开关打开
            if (refuseVo != null && refuseVo.isEnabled()) {
                //自动拒绝设置打开时间
                long refuseSwitch = refuseVo.getTradeDealSetting().getServerUpdateTime();
                long currentDayStart = DateTimeUtils.getCurrentDayStart();
                long queryTimeMillis = refuseSwitch >= currentDayStart ? refuseSwitch : currentDayStart;
                long timeoutTimeMillis = System.currentTimeMillis() - refuseVo.getTradeDealSetting().getWaitTime() * 60 * 1000L;//超时时间戳，订单创建时间必须小于此值，算超时
                Log.e(TAG, "queryTimeMillis = " + queryTimeMillis + ", timeoutTimeMillis = " + timeoutTimeMillis);
                return mTradeDealManager.findAutoRejectRecord(queryTimeMillis, timeoutTimeMillis, MAX_ROWS);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<TradeVo> tradeVos) {
            if (Utils.isNotEmpty(tradeVos) && !isRefuseing) {
                setRefuseing(true);
                refuseTradeBatch(tradeVos);
            }
        }
    }
}
