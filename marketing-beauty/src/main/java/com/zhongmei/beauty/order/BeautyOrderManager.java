package com.zhongmei.beauty.order;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.bty.mobilepay.ISavedCallback;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.basemodule.beauty.BeautyCardManager;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.bean.ShoppingCartVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.yunfu.net.builder.NetError;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.beauty.operates.BeautyOperates;
import com.zhongmei.beauty.operates.message.BeautyTradeResp;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.DomainType;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.view.CalmLoadingDialogFragment;
import com.zhongmei.yunfu.util.ValueEnums;

import java.util.ArrayList;
import java.util.List;

import com.zhongmei.ZMIntent;




public class BeautyOrderManager {

    private static final String TAG = "BeautyOrderManager";


    public static void initOrder(boolean isEdit, Long tradeId, Tables tables, BusinessType busType) {
        if (!isEdit) {
            doInit(tables, busType);
        } else {
            doEdit(tradeId);
        }
    }

    private static void doInit(Tables tables, BusinessType busType) {
        ShoppingCartVo shoppingCartVo = new ShoppingCartVo();
        DinnerShoppingCart.getInstance().createOrder(shoppingCartVo, false);
        DinnerShoppingCart.getInstance().setOrderBusinessType(DinnerShoppingCart.getInstance().getShoppingCartVo(), busType);
        List<Tables> tablesList = new ArrayList<Tables>();
        if (tables != null) {
            tablesList.add(tables);
            DinnerShoppingCart.getInstance().updateOrCreateTables(tablesList, false);
        }
        DinnerShoppingCart.getInstance().setOrderType(DinnerShoppingCart.getInstance().getShoppingCartVo(), DeliveryType.HERE);
        DinnerShoppingCart.getInstance().setDinnerOrderType(DeliveryType.HERE);
        DinnerShoppingCart.getInstance().setDominType(DinnerShoppingCart.getInstance().getShoppingCartVo(), DomainType.BEAUTY);
                DinnerShoppingCart.getInstance().getOrder().getTrade().setTradePeopleCount(1);
    }

    private static void doEdit(Long tradeId) {
        if (tradeId == -1) {
            return;
        }
        TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
        try {
            TradeVo tradeVo = tradeDal.findTrade(tradeId);
            DinnerShoppingCart.getInstance().updateTradeVoNoTradeInfo(tradeVo, false, false);
            DinnerShopManager.getInstance().setLoginCustomerFromShoppingCart();
            BeautyCardManager.getInstance().init(DinnerShoppingCart.getInstance().getShoppingCartDish());
        } catch (Exception e) {
            Log.e(TAG, "doEdit:" + e.getMessage());
        }
    }



    public static void doBeautyTradePayRequest(DoPayApi doPayApi, IPaymentInfo paymentInfo, FragmentActivity activity, final IPayOverCallback callback, final ISavedCallback savedCallback) {
        BeautyOperates beautyOperates = OperatesFactory.create(BeautyOperates.class);
        if (paymentInfo.getTradeVo().getTrade().getId() != null) {
            beautyOperates.modifyBeauty(paymentInfo.getTradeVo(), new CalmPayResponseListener(doPayApi, paymentInfo, callback, savedCallback, activity), activity);
        } else {
            beautyOperates.submitBeauty(paymentInfo.getTradeVo(), new CalmPayResponseListener(doPayApi, paymentInfo, callback, savedCallback, activity), activity);
        }
    }


    public static boolean isBuyServerBus() {
        TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
        if (tradeVo != null && tradeVo.getTrade() != null) {
            return ValueEnums.equalsValue(BusinessType.CARD_TIME, ValueEnums.toValue(tradeVo.getTrade().getBusinessType()));
        }
        return false;
    }

    private static class CalmPayResponseListener extends CalmResponseListener<ResponseObject<BeautyTradeResp>> {
        private DoPayApi doPayApi;
        private IPayOverCallback callback;
        private ISavedCallback savedCallback;
        private IPaymentInfo paymentInfo;
        private FragmentActivity mActivity;

        public CalmPayResponseListener(DoPayApi doPayApi, IPaymentInfo paymentInfo, IPayOverCallback callback, ISavedCallback savedCallback, FragmentActivity activity) {
            this.doPayApi = doPayApi;
            this.paymentInfo = paymentInfo;
            this.callback = callback;
            this.savedCallback = savedCallback;
            mActivity = activity;
        }

        @Override
        public void onSuccess(ResponseObject<BeautyTradeResp> response) {
            if (ResponseObject.isOk(response)) {
                updateCommon(mActivity, paymentInfo, savedCallback, true);
            } else {
                if (ResponseObject.isExisted(response)) {
                    updateCommon(mActivity, paymentInfo, savedCallback, true);

                }

                                ToastUtil.showLongToast(response.getMessage());
                if (callback != null) {
                    callback.onFinished(false, response.getStatusCode());
                }
                                if (savedCallback != null) {
                    savedCallback.onSaved(false);
                }
            }
        }

        @Override
        public void onError(NetError error) {
            ToastUtil.showLongToast(error.getVolleyError().getMessage());
                        if (savedCallback != null) {
                savedCallback.onSaved(false);
            }
        }
    }

    private static void updateCommon(final FragmentActivity mContext, final IPaymentInfo mPaymentInfo, final ISavedCallback mSavedCallback, final boolean savedCallbackOk) {
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
                                updateDinnerTradeVo(tradeVo, mPaymentInfo);
                mPaymentInfo.setTradeVo(tradeVo);
                mPaymentInfo.setOrdered(true);                                CalmLoadingDialogFragment.hide(mDialogFragment);
                                if (mSavedCallback != null) {
                    mSavedCallback.onSaved(savedCallbackOk);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

        private static void updateDinnerTradeVo(TradeVo tradeVo, IPaymentInfo mPaymentInfo) {
                CustomerResp customer = CustomerManager.getInstance().getDinnerLoginCustomer();
        DinnerShoppingCart.getInstance().updateBeautyDataFromTradeVo(tradeVo);        CustomerManager.getInstance().setDinnerLoginCustomer(customer);
                DinnerCashManager cashManager = new DinnerCashManager();
        if (customer != null) {
            if (customer.card == null) {
                cashManager.updateIntegralCash(customer);            } else {
                cashManager.updateIntegralCash(customer.card);            }
        }
    }


    public static void saveTrade(FragmentActivity activity, DinnerShoppingCart dinnerShoppingCart) {
        TradeVo tradeVo = dinnerShoppingCart.createOrder();
        if (tradeVo.getTrade().getId() == null) {
            submitTrade(activity, tradeVo);
        } else {
            modifyTrade(activity, tradeVo);
        }
    }


    public static void submitTrade(final FragmentActivity activity, TradeVo tradeVo) {
        BeautyOperates beautyOperates = OperatesFactory.create(BeautyOperates.class);
        beautyOperates.submitBeauty(tradeVo, new CalmResponseListener<ResponseObject<BeautyTradeResp>>() {
            @Override
            public void onError(NetError error) {
                ToastUtil.showLongToast(error.getVolleyError().getMessage());
            }

            @Override
            public void onSuccess(ResponseObject<BeautyTradeResp> resp) {
                if (ResponseObject.isOk(resp)) {
                    activity.finish();
                } else {
                    if (resp.getMessage() != null) {
                        ToastUtil.showLongToast(resp.getMessage());
                    }
                }
            }
        }, activity);

    }


    public static void modifyTrade(final FragmentActivity activity, TradeVo tradeVo) {
        BeautyOperates beautyOperates = OperatesFactory.create(BeautyOperates.class);
        beautyOperates.modifyBeauty(tradeVo, new CalmResponseListener<ResponseObject<BeautyTradeResp>>() {
            @Override
            public void onError(NetError error) {
                ToastUtil.showLongToast(error.getVolleyError().getMessage());
            }

            @Override
            public void onSuccess(ResponseObject<BeautyTradeResp> resp) {
                if (ResponseObject.isOk(resp)) {
                    activity.finish();
                } else {
                    if (resp.getMessage() != null) {
                        ToastUtil.showLongToast(resp.getMessage());
                    }
                }
            }
        }, activity);

    }


    public static void gotoPay(Activity activity, TradeVo tradeVo) {

        ZMIntent.pay(activity, tradeVo);
    }

    public static void clearShopcart() {
        DinnerShoppingCart.getInstance().clearShoppingCart();
        CustomerManager.getInstance().setDinnerLoginCustomer(null);
        BeautyCardManager.getInstance().exitCardManager();
    }


    public static void clearShopcartDish() {
        DinnerShoppingCart.getInstance().removeAllDishs();
        DinnerShoppingCart.getInstance().setDinnerRemarks("");
        DinnerShoppingCart.getInstance().removeAllPrivilegeForCustomer(false, false);
        DinnerShoppingCart.getInstance().removeAllTradeUser();
        DinnerShoppingCart.getInstance().removeOrderPrivilege();
    }


    public static boolean isShopcartCanDisplay(TradeVo tradeVo, List<IShopcartItem> list) {

        if (Utils.isNotEmpty(list)) {
            return true;
        }
        if (MathDecimal.isCompareZeroGe(tradeVo.getTrade().getPrivilegeAmount())) {
            return true;
        }

        if (!TextUtils.isEmpty(tradeVo.getTrade().getTradeMemo())) {
            return true;
        }
        boolean isHasValidData = false;
        if (!Utils.isEmpty(tradeVo.getTradeUsers())) {
            for (TradeUser tradeUser : tradeVo.getTradeUsers()) {
                if (!tradeUser.isValid()) {
                    continue;
                }
                isHasValidData = true;
            }
        }
        if (CustomerManager.getInstance().getDinnerLoginCustomer() != null) {
            isHasValidData = true;
        }
        if (isHasValidData) {
            return true;
        }
        return false;

    }

}
