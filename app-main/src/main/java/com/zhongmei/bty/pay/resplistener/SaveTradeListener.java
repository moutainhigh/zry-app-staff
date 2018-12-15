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

/**
 * 保存订单，用于先下单在收银
 * Created by demo on 2018/12/15
 */

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
            if (ResponseObject.isOk(response)) {// 下单成功
                Log.e(TAG, "下单或改单成功");
                //如果正餐
                if (mPaymentInfo.isDinner()) {
                    updateTradeVoUi(true);//modify v8.2
                    //如果快餐,外卖，开放平台
                } else if (mPaymentInfo.getTradeBusinessType() == BusinessType.SNACK || mPaymentInfo.getTradeBusinessType() == BusinessType.TAKEAWAY || mPaymentInfo.getTradeBusinessType() == BusinessType.POS_PAY) {
                    //快餐刷新数据
                    updateTradeVoUi(true);//modify v8.2
                } else {//其它单据类型
                    //查找最新trade
                    Trade trade = response.getContent().getTrades().get(0);
                    mPaymentInfo.getTradeVo().setTrade(trade);
                    mPaymentInfo.setOrdered(true);// 标识已经下单
                    //保存成功后回调处理
                    if (mSavedCallback != null) {
                        mSavedCallback.onSaved(true);
                    }
                }
            } else if (ResponseObject.isExisted(response)) {//数据已经存在或者其它端已经更新 aad v8.2
                ToastUtil.showLongToast(response.getMessage());
                if (mPayOverCallback != null) {
                    mPayOverCallback.onFinished(false, response.getStatusCode());
                }
                //保存后回调处理
                if (mSavedCallback != null) {
                    mSavedCallback.onSaved(false);
                }
            } else {//其它改单失败情况
                ToastUtil.showLongToast(response.getMessage());
                if (mPayOverCallback != null) {
                    mPayOverCallback.onFinished(false, response.getStatusCode());
                }
                //保存后回调处理
                if (mSavedCallback != null) {
                    mSavedCallback.onSaved(false);
                }
            }
        } catch (Exception e) {
            if (mPayOverCallback != null) {
                mPayOverCallback.onFinished(false, response.getStatusCode());
            }
            //保存后回调处理
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

    //add 20171101 begin
    //正餐刷新购物车数据及Ui
    private void updateDinnerTradeVo(TradeVo tradeVo) {
        //先把原单会员拿出来，等reset方法玩了以后再放回去（临时方案，后续需要优化）
        CustomerResp customer = CustomerManager.getInstance().getDinnerLoginCustomer();
        // add 8.4 正餐合单成功清空购物车
        if (mPaymentInfo.getTradeBusinessType() == BusinessType.DINNER && mPaymentInfo.getTradeVo().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
            DinnerShoppingCart.getInstance().clearShoppingCart();
        }
        if (mPaymentInfo.isSplit()) {
            //拆单成功
            EventBus.getDefault().post(new SeparateEvent(SeparateEvent.EVENT_SEPARATE_SAVE));
            //刷新拆单购物车数据
            SeparateShoppingCart.getInstance().updateDataFromTradeVo(tradeVo);//set callback false v8.10
        } else {
            // add 8.4 合单成功新建桌台
            if (mPaymentInfo.getTradeBusinessType() == BusinessType.DINNER && mPaymentInfo.getTradeVo().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
                DinnerShoppingCart.getInstance().updateTradeVoNoTradeInfo(tradeVo, true, true);//set callback false v8.10
            } else {
                DinnerShoppingCart.getInstance().updateDataFromTradeVo(tradeVo);//set callback false v8.10
            }
        }
        CustomerManager.getInstance().setDinnerLoginCustomer(customer);
        //刷新积分抵现
        DinnerCashManager cashManager = new DinnerCashManager();
        if (customer != null) {
            if (customer.card == null) {
                cashManager.updateIntegralCash(customer);//set callback false v8.10
            } else {
                cashManager.updateIntegralCash(customer.card);//set callback false v8.10
            }
        }
    }

    //刷新购物车数据及Ui
    private void updateTradeVoUi(final boolean savedCallbackOk) {
        // 清空快餐购物车
        if (mPaymentInfo.getTradeBusinessType() == BusinessType.SNACK || mPaymentInfo.getTradeBusinessType() == BusinessType.TAKEAWAY) {
            ShoppingCart.getInstance().clearShoppingCart();
        }
        // add 8.4 合单成功关闭点菜界面
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
                //显示咖啡杯
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
                //如果是正餐结算界面，刷新购物车数据 ,对接开放平台modify 20180129
                if (mPaymentInfo.isDinner() || mPaymentInfo.getPayActionPage() == PayActionPage.BALANCE) {
                    updateDinnerTradeVo(tradeVo);
                }
                mPaymentInfo.setTradeVo(tradeVo);
                mPaymentInfo.setOrdered(true);// 标识已经下单
                //关闭咖啡杯
                CalmLoadingDialogFragment.hide(mDialogFragment);
                //保存成功后回调处理
                if (mSavedCallback != null) {
                    mSavedCallback.onSaved(savedCallbackOk);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * 更新自助联台主单
     *
     * @param savedCallbackOk
     */
    private void updateBuffetUnionUi(final boolean savedCallbackOk) {

        new AsyncTask<Void, Void, List<TradeVo>>() {
            private CalmLoadingDialogFragment mDialogFragment = null;
            private TradeDal tradeDal = OperatesFactory.create(TradeDal.class);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //显示咖啡杯
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
                //如果是正餐结算界面，刷新购物车数据 ,对接开放平台modify 20180129
                if (mPaymentInfo.isDinner() || mPaymentInfo.getPayActionPage() == PayActionPage.BALANCE) {
                    CustomerResp customer = CustomerManager.getInstance().getDinnerLoginCustomer();
                    DinnerShoppingCart.getInstance().clearShoppingCart();
                    DinnerShoppingCart.getInstance().updateUnionMainTradeInfo(tradeVos);
                    CustomerManager.getInstance().setDinnerLoginCustomer(customer);
                    //刷新积分抵现
                    DinnerCashManager cashManager = new DinnerCashManager();
                    if (customer != null) {
                        if (customer.card == null) {
                            cashManager.updateIntegralCash(customer);//set callback false v8.10
                        } else {
                            cashManager.updateIntegralCash(customer.card);//set callback false v8.10
                        }
                    }
                    if (Utils.isNotEmpty(tradeVos)) {
                        for (TradeVo tradeVo : tradeVos) {
                            if (tradeVo.isBuffetUnionMainTrade()) {
                                mPaymentInfo.setTradeVo(DinnerShoppingCart.getInstance().getMainTradeInfo().getTradeVo());
                            }
                        }
                    }
                }

                mPaymentInfo.setOrdered(true);// 标识已经下单
                //关闭咖啡杯
                CalmLoadingDialogFragment.hide(mDialogFragment);
                //保存成功后回调处理
                if (mSavedCallback != null) {
                    mSavedCallback.onSaved(savedCallbackOk);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

}
