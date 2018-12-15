package com.zhongmei.bty.cashier.ordercenter.presenter;

import android.os.AsyncTask;
import android.support.v4.util.Pair;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerShopBiz;
import com.zhongmei.bty.basemodule.database.utils.DbQueryConstant;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.message.BatchDeliveryFee;
import com.zhongmei.bty.cashier.ordercenter.bean.FilterCondition;
import com.zhongmei.bty.cashier.ordercenter.manager.IOrderCenterListManager;
import com.zhongmei.bty.cashier.ordercenter.view.IOrderCenterListView;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.db.entity.trade.Trade;

import java.math.BigDecimal;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 订单中心订单列表快餐Presenter
 */

public abstract class OrderCenterListPresenter implements IOrderCenterListPresenter {
    protected IOrderCenterListView mView;
    protected IOrderCenterListManager mManager;
    private AsyncTask<Void, Void, List<TradePaymentVo>> asyncTask;

    public OrderCenterListPresenter(IOrderCenterListView view, IOrderCenterListManager manager) {
        mView = view;
        mManager = manager;
    }

    @Override
    public boolean showBackButton() {
        return false;
    }

    @Override
    public boolean showMenuButton() {
        return false;
    }

    @Override
    public void openSideMenu() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean showOrderRefreshButton() {
        return false;
    }

    @Override
    public void openNotifyCenter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean showNotifyCenterTip() {
        return false;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void notifyVoice(int type, String tradeUuid, String mobile, String serialNo, String tradeNo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deliveryPayment(List<TradeVo> tradeVos) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void bindDeliveryUser(List<TradeVo> tradeVos, User authUser) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void loadData(final int tab, final FilterCondition condition, final Trade trade) {
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
        final int type = getType(trade);
        if (type == 0) {//加载更多不显示loading
            mView.hideEmptyAndListView();
            mView.showLoadingView();
        }
        asyncTask = new AsyncTask<Void, Void, List<TradePaymentVo>>() {
            @Override
            protected List<TradePaymentVo> doInBackground(Void... params) {
                return mManager.loadData(tab, condition, trade);
            }

            @Override
            protected void onPostExecute(List<TradePaymentVo> tradePaymentVos) {
                if (mView.getViewActivity() != null) {
                    mView.refreshList(tradePaymentVos, type);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private int getType(Trade trade) {
        //1为加载更多模式,0为正常加载模式
        return trade == null ? 0 : 1;
    }

    public void search(final int tab, final int position, final String keyword, final FilterCondition condition, final Trade trade) {
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
        final int type = getType(trade);
        if (type == 0) {//加载更多不显示loading
            mView.hideEmptyAndListView();
            mView.showLoadingView();
        }
        asyncTask = new AsyncTask<Void, Void, List<TradePaymentVo>>() {
            @Override
            protected List<TradePaymentVo> doInBackground(Void... params) {
                return mManager.search(tab, position, keyword, condition, trade);
            }

            @Override
            protected void onPostExecute(List<TradePaymentVo> tradePaymentVos) {
                if (mView.getViewActivity() != null) {
                    mView.refreshList(tradePaymentVos, type);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void countOrder() {
        countOrder(DbQueryConstant.UNPROCESSED_NEW_ORDER);
        countOrder(DbQueryConstant.UNPROCESSED_CANCEL_REQUEST);
    }

    protected void countOrder(final int tab) {
        new AsyncTask<Integer, Void, Long>() {
            @Override
            protected Long doInBackground(Integer... params) {
                return mManager.countOrder(params[0]);
            }

            @Override
            protected void onPostExecute(Long aLong) {
                if (mView.getViewActivity() != null) {
                    mView.refreshOrderCount(tab, aLong);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, tab);
    }

    protected void addChildTab(List<Pair<String, Integer>> tab, int source, int id) {
        String name = mView.getViewActivity().getResources().getString(source);
        tab.add(new Pair<>(name, id));
    }

    /**
     * 注册EventBus
     */
    protected void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    protected void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public String calculateOrderAmount(List<TradeVo> tradeVoList) {
        if (tradeVoList == null || tradeVoList.size() == 0) {
            return mView.getOriginTip();
        }
        BigDecimal bigDecimal = BigDecimal.ZERO;
        for (TradeVo tradeVo : tradeVoList) {
            if (tradeVo != null && tradeVo.getTrade() != null) {
                bigDecimal = bigDecimal.add(tradeVo.getTrade().getTradeAmount());
            }
        }
        return getString(R.string.order_tips, tradeVoList.size())
                + ShopInfoCfg.formatCurrencySymbol(bigDecimal.toString());
    }

    String getString(int resId) {
        return mView.getViewActivity().getString(resId);
    }

    String getString(int resId, Object... formatArgs) {
        return mView.getViewActivity().getString(resId, formatArgs);
    }

    @Override
    public void batchBindDeliveryUser() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showDeliveryPlatformChoose(List<TradeVo> tradeVos) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void prepareDeliveryOrderDispatch(List<TradeVo> tradeVos, PartnerShopBiz partnerShopBiz) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deliveryOrderDispatch(List<TradeVo> tradeVos, PartnerShopBiz partnerShopBiz, List<BatchDeliveryFee> deliveryFees) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isShowKoubeiVerification() {
        return false;
    }
}
