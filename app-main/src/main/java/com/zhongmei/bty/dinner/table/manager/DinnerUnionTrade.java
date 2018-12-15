package com.zhongmei.bty.dinner.table.manager;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.message.uniontable.UnionTradeSplitReq;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.buffet.table.view.BuffetUnionCancelVo;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.dinner.vo.DinnerConnectTablesVo;

import java.util.List;

public class DinnerUnionTrade extends IUnionTrade {

    @Override
    public void create(List<DinnerConnectTablesVo> tablesVoList, UnionListener listener) {

    }

    @Override
    public void cancel(final List<DinnerConnectTablesVo> tablesVoList, BuffetUnionCancelVo unionCancelVo, final UnionListener listener) {
        final DinnerSplitUnionManager splitTool = new DinnerSplitUnionManager();
        new AsyncTask<Void, Void, UnionTradeSplitReq>() {
            @Override
            protected UnionTradeSplitReq doInBackground(Void... params) {
                return splitTool.createUnionTradeSplitReq(tablesVoList);
            }

            protected void onPostExecute(UnionTradeSplitReq req) {
                if (req == null) {
                    String errorMsg = tablesVoList.get(0).areaName
                            + BaseApplication.getInstance().getString(R.string.dinner_cancel_union_trade_tables)
                            + tablesVoList.get(0).tables.getTableName()
                            + BaseApplication.getInstance().getString(R.string.dinner_cancel_union_trade_limit_three);
                    setResponse(listener, new Exception(errorMsg), null);
                    return;
                }

                setResult(req, listener);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setResult(UnionTradeSplitReq req, final UnionListener listener) {
        Fragment fragment = mWeakReference.get();
        if (fragment != null) {
            TradeOperates operates = OperatesFactory.create(TradeOperates.class);
            operates.splitUnionTrade(req, LoadingResponseListener.ensure(newResponseListener(listener), fragment.getChildFragmentManager()));
        }
    }

    private ResponseListener<TradeResp> newResponseListener(final UnionListener listener) {
        return new ResponseListener<TradeResp>() {
            @Override
            public void onResponse(ResponseObject<TradeResp> response) {
                setResponse(listener, null, response);
            }

            @Override
            public void onError(VolleyError error) {
                setResponse(listener, error, null);
            }
        };
    }
}
