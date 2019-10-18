package com.zhongmei.bty.pay.resplistener;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.zhongmei.bty.mobilepay.ISavedCallback;
import com.zhongmei.bty.mobilepay.enums.PayActionPage;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.SeparateShoppingCart;
import com.zhongmei.bty.basemodule.trade.event.ActionCloseOrderDishActivity;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCart;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.view.CalmLoadingDialogFragment;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.event.SeparateEvent;

import java.util.List;

import de.greenrobot.event.EventBus;



public class SaveTradeListener extends EventResponseListener<TradeResp> implements ResponseListener<TradeResp> {
    private static final String TAG = SaveTradeListener.class.getSimpleName();
    private IPayOverCallback mPayOverCallback;
    private ISavedCallback mSavedCallback;
    private IPaymentInfo mPaymentInfo;
    private FragmentActivity mContext;

    public static SaveTradeListener getNewInstance(FragmentActivity activity, IPaymentInfo paymentInfo, IPayOverCallback callBack, ISavedCallback savedCallback) {
        return new SaveTradeListener(activity, paymentInfo, callBack, savedCallback);
    }

    public SaveTradeListener(FragmentActivity activity, IPaymentInfo paymentInfo, IPayOverCallback callBack, ISavedCallback savedCallback) {
        this.mContext = activity;
        this.mPaymentInfo = paymentInfo;
        this.mPayOverCallback = callBack;
        this.mSavedCallback = savedCallback;
        this.eventName = UserActionEvent.DINNER_PAY_SETTLE_CASH.eventName;
    }

    @Override
    public void onResponse(ResponseObject<TradeResp> response) {
        try {
            if (ResponseObject.isOk(response)) {                Log.e(TAG, "下单或改单成功");
                                if (mPaymentInfo.isDinner()) {
                    updateTradeVoUi(true);                                    } else if (mPaymentInfo.getTradeBusinessType() == BusinessType.SNACK || mPaymentInfo.getTradeBusinessType() == BusinessType.TAKEAWAY || mPaymentInfo.getTradeBusinessType() == BusinessType.POS_PAY) {
                                        updateTradeVoUi(true);                } else {                                        Trade trade = response.getContent().getTrades().get(0);
                    mPaymentInfo.getTradeVo().setTrade(trade);
                    mPaymentInfo.setOrdered(true);                                        if (mSavedCallback != null) {
                        mSavedCallback.onSaved(true);
                    }
                }
            } else if (ResponseObject.isExisted(response)) {                ToastUtil.showLongToast(response.getMessage());
                if (mPayOverCallback != null) {
                    mPayOverCallback.onFinished(false, response.getStatusCode());
                }
                                if (mSavedCallback != null) {
                    mSavedCallback.onSaved(false);
                }
            } else {                ToastUtil.showLongToast(response.getMessage());
                if (mPayOverCallback != null) {
                    mPayOverCallback.onFinished(false, response.getStatusCode());
                }
                                if (mSavedCallback != null) {
                    mSavedCallback.onSaved(false);
                }
            }
        } catch (Exception e) {
            if (mPayOverCallback != null) {
                mPayOverCallback.onFinished(false, response.getStatusCode());
            }
                        if (mSavedCallback != null) {
                mSavedCallback.onSaved(false);
            }
            Log.e(TAG, "", e);
        }
    }

    @Override
    public void onError(VolleyError error) {
        try {
            ToastUtil.showLongToast(error.getMessage());
            if (mPayOverCallback != null) {
                mPayOverCallback.onFinished(false, 0);
            }
        } catch (Exception e) {
            if (mPayOverCallback != null) {
                mPayOverCallback.onFinished(false, 0);
            }
            Log.e(TAG, "", e);
        }
    }

            private void updateDinnerTradeVo(TradeVo tradeVo) {
                CustomerResp customer = CustomerManager.getInstance().getDinnerLoginCustomer();
                if (mPaymentInfo.getTradeBusinessType() == BusinessType.DINNER && mPaymentInfo.getTradeVo().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
            DinnerShoppingCart.getInstance().clearShoppingCart();
        }
        if (mPaymentInfo.isSplit()) {
                        EventBus.getDefault().post(new SeparateEvent(SeparateEvent.EVENT_SEPARATE_SAVE));
                        SeparateShoppingCart.getInstance().updateDataFromTradeVo(tradeVo);        } else {
                        if (mPaymentInfo.getTradeBusinessType() == BusinessType.DINNER && mPaymentInfo.getTradeVo().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
                DinnerShoppingCart.getInstance().updateTradeVoNoTradeInfo(tradeVo, true, true);            } else {
                DinnerShoppingCart.getInstance().updateDataFromTradeVo(tradeVo);            }
        }
        CustomerManager.getInstance().setDinnerLoginCustomer(customer);
                DinnerCashManager cashManager = new DinnerCashManager();
        if (customer != null) {
            if (customer.card == null) {
                cashManager.updateIntegralCash(customer);            } else {
                cashManager.updateIntegralCash(customer.card);            }
        }
    }

        private void updateTradeVoUi(final boolean savedCallbackOk) {
                if (mPaymentInfo.getTradeBusinessType() == BusinessType.SNACK || mPaymentInfo.getTradeBusinessType() == BusinessType.TAKEAWAY) {
            ShoppingCart.getInstance().clearShoppingCart();
        }
                if (mPaymentInfo.getTradeVo().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
            EventBus.getDefault().post(new ActionCloseOrderDishActivity());
        }
        if (mPaymentInfo.getTradeVo().isBuffetUnionMainTrade()) {
            updateBuffetUnionUi(savedCallbackOk);
        } else {
            updateCommon(savedCallbackOk);
        }
    }

    private void updateCommon(final boolean savedCallbackOk) {
        new AsyncTask<Void, Void, TradeVo>() {
            private CalmLoadingDialogFragment mDialogFragment = null;
            private TradeDal tradeDal = OperatesFactory.create(TradeDal.class);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                                mDialogFragment = CalmLoadingDialogFragment.show(mContext.getSupportFragmentManager());
            }

            @Override
            protected TradeVo doInBackground(Void... params) {
                Log.e(TAG, "下单成功后开始查询定单");
                TradeVo tradeVo = null;
                try {
                    tradeVo = tradeDal.findTrade(mPaymentInfo.getTradeVo().getTrade().getUuid());
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
                return tradeVo;
            }

            protected void onPostExecute(TradeVo tradeVo) {
                                if (mPaymentInfo.isDinner() || mPaymentInfo.getPayActionPage() == PayActionPage.BALANCE) {
                    updateDinnerTradeVo(tradeVo);
                }
                mPaymentInfo.setTradeVo(tradeVo);
                mPaymentInfo.setOrdered(true);                                CalmLoadingDialogFragment.hide(mDialogFragment);
                                if (mSavedCallback != null) {
                    mSavedCallback.onSaved(savedCallbackOk);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void updateBuffetUnionUi(final boolean savedCallbackOk) {

        new AsyncTask<Void, Void, List<TradeVo>>() {
            private CalmLoadingDialogFragment mDialogFragment = null;
            private TradeDal tradeDal = OperatesFactory.create(TradeDal.class);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                                mDialogFragment = CalmLoadingDialogFragment.show(mContext.getSupportFragmentManager());
            }

            @Override
            protected List<TradeVo> doInBackground(Void... params) {
                Log.e(TAG, "下单成功后开始查询定单");
                List<TradeVo> tradeVos = null;
                try {
                    List<Trade> trades = tradeDal.getUnionTradesByTrade(mPaymentInfo.getTradeVo().getTrade());
                    tradeVos = tradeDal.getTradeVosByTrades(trades);
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
                return tradeVos;
            }

            protected void onPostExecute(List<TradeVo> tradeVos) {
                                if (mPaymentInfo.isDinner() || mPaymentInfo.getPayActionPage() == PayActionPage.BALANCE) {
                    CustomerResp customer = CustomerManager.getInstance().getDinnerLoginCustomer();
                    DinnerShoppingCart.getInstance().clearShoppingCart();
                    DinnerShoppingCart.getInstance().updateUnionMainTradeInfo(tradeVos);
                    CustomerManager.getInstance().setDinnerLoginCustomer(customer);
                                        DinnerCashManager cashManager = new DinnerCashManager();
                    if (customer != null) {
                        if (customer.card == null) {
                            cashManager.updateIntegralCash(customer);                        } else {
                            cashManager.updateIntegralCash(customer.card);                        }
                    }
                    if (Utils.isNotEmpty(tradeVos)) {
                        for (TradeVo tradeVo : tradeVos) {
                            if (tradeVo.isBuffetUnionMainTrade()) {
                                mPaymentInfo.setTradeVo(DinnerShoppingCart.getInstance().getMainTradeInfo().getTradeVo());
                            }
                        }
                    }
                }

                mPaymentInfo.setOrdered(true);                                CalmLoadingDialogFragment.hide(mDialogFragment);
                                if (mSavedCallback != null) {
                    mSavedCallback.onSaved(savedCallbackOk);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

}
