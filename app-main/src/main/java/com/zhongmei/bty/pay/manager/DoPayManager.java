package com.zhongmei.bty.pay.manager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.beauty.order.BeautyOrderManager;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.manager.QueuePlayServiceManager;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.basemodule.customer.bean.ChargingPrint;
import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.message.MemberLoginVoResp;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.ChangeCardReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.JCRechargeCardReq;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.bty.basemodule.pay.message.PayResp;
import com.zhongmei.bty.basemodule.pay.message.PayStateResp;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.SeparateShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.basemodule.trade.message.TradePayStateReq;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.data.operates.GroupOperates;
import com.zhongmei.bty.data.operates.impl.NewTradeOperatesImpl;
import com.zhongmei.bty.data.operates.message.content.ChangeCardResp;
import com.zhongmei.bty.data.operates.message.content.JCBindCardReq;
import com.zhongmei.bty.data.operates.message.content.JCBindCardResp;
import com.zhongmei.bty.data.operates.message.content.JCRechargeCardResp;
import com.zhongmei.bty.dinner.table.manager.DinnerMergeUnionManager;
import com.zhongmei.bty.mobilepay.IOnlinePayBreakCallback;
import com.zhongmei.bty.mobilepay.IOnlinePayOverCallback;
import com.zhongmei.bty.mobilepay.IPayConstParame;
import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.bty.mobilepay.ISavedCallback;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.bean.PayModelItem;
import com.zhongmei.bty.mobilepay.bean.PaymentReqTool;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.core.OnlinePayCallback;
import com.zhongmei.bty.mobilepay.enums.PayActionPage;
import com.zhongmei.bty.mobilepay.event.DepositPayOver;
import com.zhongmei.bty.mobilepay.event.MemberPayChargeEvent;
import com.zhongmei.bty.mobilepay.event.SellForRepeatEvent;
import com.zhongmei.bty.mobilepay.event.SeparateEvent;
import com.zhongmei.bty.mobilepay.event.StopPayStatusTimer;
import com.zhongmei.bty.mobilepay.fragment.mobilepay.MobilePayDialog;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.mobilepay.manager.PaymentInfoManager;
import com.zhongmei.bty.mobilepay.utils.DoPayUtils;
import com.zhongmei.bty.mobilepay.v1.event.EventPayResult;
import com.zhongmei.bty.pay.resplistener.PayListener;
import com.zhongmei.bty.pay.resplistener.SaveTradeListener;
import com.zhongmei.bty.pay.utils.PayUtils;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.PaymentItemExtra;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.DomainType;
import com.zhongmei.yunfu.db.enums.IssueStatus;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.yunfu.db.enums.SourceChild;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayForm;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.http.CalmNetWorkRequest;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.yunfu.net.builder.NetError;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.ui.view.CalmLoadingDialogFragment;
import com.zhongmei.yunfu.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;


public class DoPayManager extends DoPayApi<PayResp> {
    private static final long serialVersionUID = 1L;
    private static final String TAG = DoPayManager.class.getSimpleName();


    private static class LazySingletonHolder {
        private static final DoPayManager INSTANCE = new DoPayManager();
    }

    public static DoPayManager getInstance() {
        return LazySingletonHolder.INSTANCE;
    }

    public DoPayManager() {
    }


    public void doPay(final FragmentActivity context, final IPaymentInfo paymentInfo, final IPayOverCallback callback) {
        if (paymentInfo.isDinner()) {
            VerifyHelper.verifyAlert(context, DinnerApplication.PERMISSION_DINNER_CASH,
                    new VerifyHelper.Callback() {
                        @Override
                        public void onPositive(User user, String code, Auth.Filter filter) {
                            super.onPositive(user, code, filter);
                            pay(context, paymentInfo, callback);
                        }

                        @Override
                        public void onNegative(String code, Auth.Filter filter) {
                            super.onNegative(code, filter);
                            if (callback != null) {
                                callback.onFinished(false, 0);
                            }
                        }
                    });
        } else {
            pay(context, paymentInfo, callback);
        }
    }

    @Override
    public void getOnlinePayBarcode(final FragmentActivity context, final IPaymentInfo paymentInfo, final PayModelItem payModelItem, final OnlinePayCallback onlinePayCallback) {
        IPayOverCallback payOverCallback = new IPayOverCallback() {

            @Override
            public void onFinished(boolean isOK, int statusCode) {
                if (!isOK) {
                    onlinePayCallback.onBarcodeError();
                }
            }
        };
        ResponseListener<PayResp> onLinePayListener = new ResponseListener<PayResp>() {

            @Override
            public void onResponse(ResponseObject<PayResp> response) {
                try {
                    PayResp resp = response.getContent();
                    if (ResponseObject.isOk(response) && !Utils.isEmpty(resp.getPaymentItemResults())) {
                        Bitmap bitmap = null;
                        Bitmap logo = null;
                        final PayResp.PItemResults paymentItem = resp.getPaymentItemResults().get(0);
                        setOnlinePaymentItemUuid(paymentItem.getPaymentItemUuid());
                        Map addition = resp.getPaymentItemResults().get(0).getAddition();
                        final String codeUrl = (String) addition.get("codeUrl");
                        if (paymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT && paymentInfo.getTradeBusinessType() == BusinessType.BUFFET) {
                            DepositPayOver depositPayOver = new DepositPayOver(resp);
                            EventBus.getDefault().post(depositPayOver);
                        }
                        if (!Utils.isEmpty(resp.getTrades())) {
                            Trade trade = resp.getTrades().get(0);
                            if (trade.getServerUpdateTime() > paymentInfo.getTradeVo().getTrade().getServerUpdateTime()) {
                                paymentInfo.getTradeVo().setTrade(trade);
                                updateShoppingCartTrade(paymentInfo, trade);
                            }
                        }
                        barcodeScuessCallback(context, onlinePayCallback, payModelItem, paymentItem.getPaymentItemId(), codeUrl);


                    } else {
                        onlinePayCallback.onBarcodeError();
                        payErrorHandler(context, response, paymentInfo, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                try {
                    ToastUtil.showLongToast(error.getMessage());
                    onlinePayCallback.onBarcodeError();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        this.pay(context, paymentInfo, payOverCallback, onLinePayListener);
    }


    @Override
    public void onlinePayByAuthCode(final FragmentActivity context, final IPaymentInfo paymentInfo, final PayModelItem payModelItem, final OnlinePayCallback onlinePayCallback) {
        if (payModelItem.getPayMode() == PayModeId.JIN_CHENG_VALUE_CARD) {
            this.doJCValueCardScanPay(context, paymentInfo, payModelItem.getAuthCode());
            return;
        }
        IPayOverCallback payOverCallback = new IPayOverCallback() {

            @Override
            public void onFinished(boolean isOK, int statusCode) {
                if (!isOK) {
                    onlinePayCallback.onAuthCodeError();
                }
            }
        };
        ResponseListener<PayResp> onLinePayListener = new EventResponseListener<PayResp>(getUserActionEventName(payModelItem.getPayMode())) {
            @Override
            public void onResponse(ResponseObject<PayResp> response) {
                try {
                    PayResp resp = response.getContent();
                    if (ResponseObject.isOk(response) && !Utils.isEmpty(resp.getPaymentItemResults())) {
                        final PayResp.PItemResults paymentItem = resp.getPaymentItemResults().get(0);
                        setOnlinePaymentItemUuid(paymentItem.getPaymentItemUuid());
                        onlinePayCallback.onAuthCodeScuess(paymentItem.getPaymentItemId());
                        if (paymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT && paymentInfo.getTradeBusinessType() == BusinessType.BUFFET) {
                            DepositPayOver depositPayOver = new DepositPayOver(resp);
                            EventBus.getDefault().post(depositPayOver);
                        }
                        UserActionEvent.end(getUserActionEventName(payModelItem.getPayMode()));
                        doVerifyPayResp(context, paymentInfo, resp, onlinePayCallback);
                    } else {
                        onlinePayCallback.onAuthCodeError();
                        payErrorHandler(context, response, paymentInfo, onlinePayCallback);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onlinePayCallback.onAuthCodeError();
                }
            }

            @Override
            public void onError(VolleyError error) {
                try {
                    ToastUtil.showLongToast(error.getMessage());
                    onlinePayCallback.onAuthCodeError();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        this.pay(context, paymentInfo, payOverCallback, onLinePayListener);
    }

    @Override
    public void getOnlinePayState(final FragmentActivity context, final IPaymentInfo paymentInfo, final long paymentItemId, final String paymentItemUuid, final IOnlinePayOverCallback onlinePayOverCallback) {
        ResponseListener<PayResp> listener = new ResponseListener<PayResp>() {

            @Override
            public void onResponse(ResponseObject<PayResp> response) {
                try {
                    if (ResponseObject.isOk(response)) {
                        PayResp result = response.getContent();
                        Trade trade = paymentInfo.getTradeVo().getTrade();
                        if (result.getTrades() != null && !result.getTrades().isEmpty())
                            trade = result.getTrades().get(0);
                        if (paymentInfo.getPayScene() != PayScene.SCENE_CODE_BUFFET_DEPOSIT && isPaidScuess(result, paymentItemUuid) && paymentInfo.getActualAmount() <= paymentInfo.getOtherPay().getGroupAmount() && !DoPayUtils.isTradePaidOver(trade)) {
                            onlinePayOverCallback.onPayResult(null, TradePayStatus.PAYING.value());
                        } else {
                            doVerifyPayResp(context, paymentInfo, result, onlinePayOverCallback);
                        }
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                onlinePayOverCallback.onPayResult(null, TradePayStatus.PAYING.value());
            }

            @Override
            public void onError(VolleyError error) {
                onlinePayOverCallback.onPayResult(null, TradePayStatus.PAYING.value());
            }
        };
        NewTradeOperatesImpl tradeOperates = OperatesFactory.create(NewTradeOperatesImpl.class);
        tradeOperates.getOnlinePayStatus(paymentInfo.getTradeVo(), paymentItemId, paymentItemUuid, listener);
    }

    @Override
    public void getOnlinePayStateOfThird(final FragmentActivity context, final IPaymentInfo paymentInfo, final long paymentItemId, final String paymentItemUuid, final IOnlinePayOverCallback onlinePayOverCallback) {
        TradePayStateReq payStateReq = new TradePayStateReq();
        payStateReq.setTradeId(paymentInfo.getTradeVo().getTrade().getId());
        payStateReq.setPaymentItemId(paymentItemId);
        String url = ServerAddressUtil.getInstance().getPayStateUrl();
        new CalmNetWorkRequest.Builder<TradePayStateReq, PayStateResp>()
                .requestContent(payStateReq)
                .with(context)
                .responseClass(PayStateResp.class)
                .url(url)
                .successListener(new CalmResponseListener<ResponseObject<PayStateResp>>() {
                    @Override
                    public void onSuccess(ResponseObject<PayStateResp> payStateResp) {
                        if (TradePayStatus.PAID.value().equals(payStateResp.getContent().getPayState())) {
                            getOnlinePayState(context, paymentInfo, paymentItemId, paymentItemUuid, onlinePayOverCallback);
                        }
                    }

                    @Override
                    public void onError(NetError error) {
                        ToastUtil.showShortToast(error.getVolleyError().getMessage());
                    }
                })
                .showLoading()
                .tag(url)
                .create();
    }

    private void pay(final FragmentActivity context, final IPaymentInfo paymentInfo, IPayOverCallback callback) {
        final PayListener payListener = PayListener.getNewInstance(context, paymentInfo, callback, false);
        final PaymentVo paymentVo = paymentInfo.getTradePaymentVo();
        payListener.setPaymentVo(paymentVo);
        this.pay(context, paymentInfo, callback, payListener);
    }

    private void pay(final FragmentActivity context, final IPaymentInfo paymentInfo, IPayOverCallback callback, final ResponseListener<PayResp> payListener) {
        final NewTradeOperatesImpl tradeOperates = OperatesFactory.create(NewTradeOperatesImpl.class);
        final PaymentVo paymentVo = paymentInfo.getTradePaymentVo();
        if (!this.checkInputPaymentInfo(context, paymentVo)) {
            if (callback != null) {
                callback.onFinished(false, 0);
            }
            return;
        }
        if (paymentInfo.getActualAmount() < 0) {
            ToastUtil.showShortToast(R.string.pay_negative_not_pay);
            if (callback != null) {
                callback.onFinished(false, 0);
            }
            return;
        }


        if (paymentInfo.isOrdered() || (paymentInfo.getTradeVo().getTrade().getTradePayStatus() == TradePayStatus.PAYING
                && paymentInfo.getTradeVo().getTrade().getBusinessType() != BusinessType.BUFFET)) {
            if (paymentInfo.getTradeVo().isBeauty()) {
                tradeOperates.beautyPay(paymentInfo.getTradeVo().getTrade(), new PaymentReqTool(paymentInfo).creatPaymentReq(),
                        LoadingResponseListener.ensure(payListener, context.getSupportFragmentManager()));
            } else {
                if (!paymentInfo.getOtherPay().isEmpty()) {
                    PayModelItem payModelItem = paymentInfo.getOtherPay().getAllPayModelItems().get(0);
                    if (payModelItem.getPayMode() == PayModeId.EARNEST_DEDUCT) {
                        if (paymentInfo.getTradeVo().getTradeEarnestMoney() > paymentInfo.getActualAmount()) {
                            tradeOperates.bookingRrePayRefund(new PaymentReqTool(paymentInfo).createPrePayRefundReq(), LoadingResponseListener.ensure(payListener, context.getSupportFragmentManager()));
                        } else {
                            tradeOperates.useEarnestDeduct(new PaymentReqTool(paymentInfo).createEarnestDeductReq(), LoadingResponseListener.ensure(payListener, context.getSupportFragmentManager()));
                        }
                    } else {
                        tradeOperates.newpay(paymentInfo.getTradeVo().getTrade(), new PaymentReqTool(paymentInfo).creatPaymentReq(),
                                LoadingResponseListener.ensure(payListener, context.getSupportFragmentManager()));
                    }
                } else {
                    ToastUtil.showShortToast(context.getString(R.string.pay_cash_paymentinfo_empty_alter));
                }
            }
        } else {
            ISavedCallback savedCallback = new ISavedCallback() {

                @Override
                public void onSaved(boolean isOk) {
                    if (isOk) {
                        if (paymentInfo.getTradeVo().isBeauty()) {
                            tradeOperates.beautyPay(paymentInfo.getTradeVo().getTrade(), new PaymentReqTool(paymentInfo).creatPaymentReq(),
                                    LoadingResponseListener.ensure(payListener, context.getSupportFragmentManager()));
                        } else {
                            if (!paymentInfo.getOtherPay().isEmpty()) {
                                PayModelItem payModelItem = paymentInfo.getOtherPay().getAllPayModelItems().get(0);
                                if (payModelItem.getPayMode() == PayModeId.EARNEST_DEDUCT) {
                                    if (paymentInfo.getTradeVo().getTradeEarnestMoney() > paymentInfo.getActualAmount()) {
                                        tradeOperates.bookingRrePayRefund(new PaymentReqTool(paymentInfo).createPrePayRefundReq(), LoadingResponseListener.ensure(payListener, context.getSupportFragmentManager()));
                                    } else {
                                        tradeOperates.useEarnestDeduct(new PaymentReqTool(paymentInfo).createEarnestDeductReq(), LoadingResponseListener.ensure(payListener, context.getSupportFragmentManager()));
                                    }
                                } else {
                                    tradeOperates.newpay(paymentInfo.getTradeVo().getTrade(), new PaymentReqTool(paymentInfo).creatPaymentReq(),
                                            LoadingResponseListener.ensure(payListener, context.getSupportFragmentManager()));
                                }
                            } else {
                                ToastUtil.showShortToast(context.getString(R.string.pay_cash_paymentinfo_empty_alter));
                            }
                        }
                    }
                }
            };
            saveTrade(context, paymentInfo, callback, savedCallback);
        }
    }

    public void saveTrade(final FragmentActivity context, final IPaymentInfo paymentInfo, final IPayOverCallback callback, final ISavedCallback savedCallback) {
        {
            final NewTradeOperatesImpl tradeOperates = OperatesFactory.create(NewTradeOperatesImpl.class);
            ResponseListener<TradeResp> listener = SaveTradeListener.getNewInstance(context, paymentInfo, callback, savedCallback);
            BusinessType tradeBusinessType = paymentInfo.getTradeBusinessType();
            switch (tradeBusinessType) {
                case DINNER:
                    if (paymentInfo.isSplit()) {
                        if (paymentInfo.getSourceTradeVo().getTrade() != null && paymentInfo.getSourceTradeVo().getTrade().getId() == null) {
                            ToastUtil.showShortToast(R.string.open_table_please);
                            if (callback != null) {
                                callback.onFinished(false, 0);
                            }
                            return;
                        }
                    } else {
                        if (paymentInfo.getTradeVo().getTrade() != null && paymentInfo.getTradeVo().getTrade().getId() == null) {
                            ToastUtil.showShortToast(R.string.open_table_please);
                            if (callback != null) {
                                callback.onFinished(false, 0);
                            }
                            return;
                        }
                    }
                    if (PayUtils.isNegativeTradeAmount(paymentInfo.getTradeVo())) {
                        ToastUtil.showShortToast(R.string.negative_not_pay);
                        if (callback != null) {
                            callback.onFinished(false, 0);
                        }
                        return;
                    }
                    if (!DinnerShopManager.getInstance().hasValidItems(paymentInfo.getTradeVo())) {
                        ToastUtil.showShortToast(R.string.dinner_no_item_hint);
                        if (callback != null) {
                            callback.onFinished(false, 0);
                        }
                        return;
                    }
                    if (DinnerShopManager.getInstance().hasUnactivedPrivilege(paymentInfo.getTradeVo())) {
                        ToastUtil.showShortToast(R.string.privilege_ineffective);
                        if (callback != null) {
                            callback.onFinished(false, 0);
                        }
                        return;
                    }
                    if (paymentInfo.getTradeVo().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
                        tradeOperates.unionAndModifyUnionTrade(DinnerMergeUnionManager.createUnionTradeMergeReq(paymentInfo.getTradeVo()), LoadingResponseListener.ensure(listener, context.getSupportFragmentManager()));
                    } else {
                        if (paymentInfo.isSplit() && paymentInfo.getTradeVo().getTrade().getId() == null) {
                            tradeOperates.tradeSplitDinner(paymentInfo.getSourceTradeVo(),
                                    paymentInfo.getTradeVo(),
                                    LoadingResponseListener.ensure(listener, context.getSupportFragmentManager()));
                        } else {
                            if (paymentInfo.getTradeVo().getTrade().getId() == null) {
                                tradeOperates.insert(paymentInfo.getTradeVo(), LoadingResponseListener.ensure(listener, context.getSupportFragmentManager()));
                            } else {
                                tradeOperates.modifyDinner(paymentInfo.getTradeVo(),
                                        LoadingResponseListener.ensure(listener, context.getSupportFragmentManager()));
                            }
                        }
                    }
                    break;
                case BUFFET:
                    buffetSave(context, paymentInfo, callback, savedCallback);
                    break;
                case GROUP:
                    groupSave(context, paymentInfo, callback, savedCallback);
                    break;
                case SNACK:
                case TAKEAWAY:
                    if (paymentInfo.getTradeVo().getTrade().getId() != null || paymentInfo.getTradeVo().getTrade().getTradeType() == TradeType.SELL_FOR_REPEAT) {
                        tradeOperates.modifyDinner(paymentInfo.getTradeVo(), LoadingResponseListener.ensure(listener, context.getSupportFragmentManager()));
                    } else {

                        {
                            tradeOperates.insert(paymentInfo.getTradeVo(), LoadingResponseListener.ensure(listener, context.getSupportFragmentManager()));
                        }
                    }
                    break;
                case ENTITY_CARD_CHANGE:

                    break;
                case ONLINE_RECHARGE:
                case ANONYMOUS_ENTITY_CARD_RECHARGE:


                    break;
                case CARD:
                case ANONYMOUS_ENTITY_CARD_SELL:


                    break;
                case BEAUTY:
                    if (DinnerShopManager.getInstance().hasUnactivedPrivilege(paymentInfo.getTradeVo())) {
                        ToastUtil.showShortToast(R.string.privilege_ineffective);
                        if (callback != null) {
                            callback.onFinished(false, 0);
                        }
                        return;
                    }
                    BeautyOrderManager.doBeautyTradePayRequest(this, paymentInfo, context, callback, savedCallback);
                    break;
                case POS_PAY:
                default:
                    if (paymentInfo.getTradeVo().getTrade().getId() != null) {
                        tradeOperates.modifyDinner(paymentInfo.getTradeVo(), LoadingResponseListener.ensure(listener, context.getSupportFragmentManager()));
                    } else {
                        tradeOperates.insert(paymentInfo.getTradeVo(), LoadingResponseListener.ensure(listener, context.getSupportFragmentManager()));
                    }
                    break;
            }
        }
    }

    @Override
    public void showOnlinePayDialog(final FragmentActivity context, PayModelItem payModelItem, IPaymentInfo paymentInfo, int scanType, IOnlinePayBreakCallback callback) {
        com.zhongmei.bty.mobilepay.fragment.onlinepay.OnlinePayDialog payDialog = com.zhongmei.bty.mobilepay.fragment.onlinepay.OnlinePayDialog.newInstance(DoPayManager.getInstance(), paymentInfo, payModelItem, IPayConstParame.ONLIEN_SCAN_TYPE_ACTIVE);
        payDialog.setOnPayStopListener(callback);
        payDialog.show(context.getSupportFragmentManager(), "OnlinePayDialog");
    }


    private void groupSave(final FragmentActivity context, final IPaymentInfo paymentInfo, final IPayOverCallback callback, final ISavedCallback savedCallback) {
        if (PayUtils.isNegativeTradeAmount(paymentInfo.getTradeVo())) {
            ToastUtil.showShortToast(R.string.negative_not_pay);
            if (callback != null) {
                callback.onFinished(false, 0);
            }
            return;
        }
        if (DinnerShopManager.getInstance().hasUnactivedPrivilege(paymentInfo.getTradeVo())) {
            ToastUtil.showShortToast(R.string.privilege_ineffective);
            if (callback != null) {
                callback.onFinished(false, 0);
            }
            return;
        }
        final TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
        GroupOperates groupOperates = OperatesFactory.create(GroupOperates.class);
        groupOperates.modifyGroup(paymentInfo.getTradeVo(), new CalmResponseListener<ResponseObject<TradeResp>>() {
            @Override
            public void onError(NetError error) {
                ToastUtil.showLongToast(error.getVolleyError().getMessage());
                if (callback != null) {
                    callback.onFinished(false, 0);
                }
                if (savedCallback != null) {
                    savedCallback.onSaved(false);
                }
            }

            @Override
            public void onSuccess(ResponseObject<TradeResp> data) {
                if (!ResponseObject.isOk(data)) {
                    ToastUtil.showLongToast(data.getMessage());
                    if (callback != null) {
                        callback.onFinished(false, 0);
                    }
                    if (savedCallback != null) {
                        savedCallback.onSaved(false);
                    }
                    return;
                }
                new AsyncTask<Void, Void, TradeVo>() {
                    private CalmLoadingDialogFragment mDialogFragment = null;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        mDialogFragment = CalmLoadingDialogFragment.show(context.getSupportFragmentManager());
                    }

                    @Override
                    protected TradeVo doInBackground(Void... params) {
                        Log.e(TAG, "改单成功后开始查询定单");
                        TradeVo tradeVo = null;
                        try {
                            tradeVo = tradeDal.findTrade(paymentInfo.getTradeVo().getTrade().getUuid(), false);
                        } catch (Exception e) {
                            Log.e(TAG, "", e);
                        }
                        return tradeVo;
                    }

                    protected void onPostExecute(TradeVo tradeVo) {
                        CustomerResp customer = CustomerManager.getInstance().getDinnerLoginCustomer();
                        DinnerShoppingCart.getInstance().updateDataFromTradeVo(tradeVo);
                        CustomerManager.getInstance().setDinnerLoginCustomer(customer);
                        DinnerCashManager cashManager = new DinnerCashManager();
                        if (customer != null) {
                            if (customer.card == null) {
                                cashManager.updateIntegralCash(customer);
                            } else {
                                cashManager.updateIntegralCash(customer.card);
                            }
                        }
                        paymentInfo.setTradeVo(tradeVo);
                        paymentInfo.setOrdered(true);
                        CalmLoadingDialogFragment.hide(mDialogFragment);
                        if (savedCallback != null) {
                            savedCallback.onSaved(true);
                        }
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }, true);
    }


    private void buffetSave(final FragmentActivity context, final IPaymentInfo paymentInfo, final IPayOverCallback callback, final ISavedCallback savedCallback) {

        if (paymentInfo.getTradeVo().getTrade() != null && paymentInfo.getTradeVo().getTrade().getId() == null) {
            ToastUtil.showShortToast(R.string.open_table_please);
            if (callback != null) {
                callback.onFinished(false, 0);
            }
            return;
        }

        if (PayUtils.isNegativeTradeAmount(paymentInfo.getTradeVo())) {
            ToastUtil.showShortToast(R.string.negative_not_pay);
            if (callback != null) {
                callback.onFinished(false, 0);
            }
            return;
        }
        if (DinnerShopManager.getInstance().hasUnactivedPrivilege(paymentInfo.getTradeVo())) {
            ToastUtil.showShortToast(R.string.privilege_ineffective);
            if (callback != null) {
                callback.onFinished(false, 0);
            }
            return;
        }

        TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);
        ResponseListener<TradeResp> listener = SaveTradeListener.getNewInstance(context, paymentInfo, callback, savedCallback);
        if (paymentInfo.getTradeVo().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN) {

        } else {
            tradeOperates.modifyBuffet(paymentInfo.getTradeVo(), LoadingResponseListener.ensure(listener, context.getSupportFragmentManager()));
        }
    }

    private JCBindCardReq createJCBindCardReq(IPaymentInfo paymentInfo) {
        JCBindCardReq jcBindCardReq = new JCBindCardReq();
        Trade trade = paymentInfo.getTradeVo().getTrade();
        jcBindCardReq.validateCreate();
        jcBindCardReq.validateUpdate();
        jcBindCardReq.setDomainType(DomainType.RESTAURANT);
        jcBindCardReq.setTradeType(TradeType.SELL);
        jcBindCardReq.setUuid(trade.getUuid());
        jcBindCardReq.setBusinessType(trade.getBusinessType());
        jcBindCardReq.setSource(trade.getSource());
        jcBindCardReq.setSourceChild(trade.getSourceChild());
        jcBindCardReq.setTradeNo(trade.getTradeNo());
        jcBindCardReq.setClientUpdateTime(trade.getClientUpdateTime());
        jcBindCardReq.setClientCreateTime(trade.getClientCreateTime());
        jcBindCardReq.setBizDate(trade.getBizDate());
        jcBindCardReq.setTradeTime(trade.getTradeTime());
        jcBindCardReq.setServerUpdateTime(trade.getClientCreateTime());
        jcBindCardReq.setServerUpdateTime(trade.getServerUpdateTime());
        jcBindCardReq.setCreatorId(trade.getId());
        jcBindCardReq.setCreatorName(trade.getCreatorName());
        jcBindCardReq.setUpdatorId(trade.getId());
        jcBindCardReq.setUpdatorName(trade.getUpdatorName());
        jcBindCardReq.setTradePayStatus(TradePayStatus.UNPAID);
        jcBindCardReq.setTradePayForm(TradePayForm.OFFLINE);
        jcBindCardReq.setBusinessType(trade.getBusinessType());
        jcBindCardReq.setDishKindCount(trade.getDishKindCount());
        jcBindCardReq.setSaleAmount(trade.getSaleAmount());
        jcBindCardReq.setTradeAmount(trade.getTradeAmount());
        jcBindCardReq.setPrivilegeAmount(trade.getPrivilegeAmount());
        jcBindCardReq.setTradePeopleCount(trade.getTradePeopleCount());
        jcBindCardReq.setTradeStatus(trade.getTradeStatus());
        jcBindCardReq.setTradeCustomer(paymentInfo.getTradeVo().getTradeCustomerList().get(0));
        JCBindCardReq.JCBindCardTradeItem tradeItem = new JCBindCardReq.JCBindCardTradeItem();
        TradeItem tradeItemTmp = paymentInfo.getTradeVo().getTradeItemList().get(0).getTradeItem();
        tradeItem.setActualAmount(tradeItemTmp.getActualAmount());
        tradeItem.validateCreate();
        tradeItem.setTradeUuid(tradeItemTmp.getTradeUuid());
        tradeItem.setUuid(SystemUtils.genOnlyIdentifier());
        tradeItem.setSort(tradeItemTmp.getSort());
        tradeItem.setPrice(tradeItemTmp.getPrice());
        tradeItem.setQuantity(tradeItemTmp.getQuantity());
        tradeItem.setAmount(tradeItemTmp.getAmount());
        tradeItem.setPropertyAmount(BigDecimal.ZERO);
        tradeItem.setFeedsAmount(BigDecimal.ZERO);
        tradeItem.setType(DishType.CARD);
        tradeItem.setIssueStatus(IssueStatus.DIRECTLY);
        tradeItem.setEnableWholePrivilege(Bool.YES);
        tradeItem.setStatusFlag(StatusFlag.VALID);
        tradeItem.setDishName(tradeItemTmp.getDishName());
        tradeItem.setSkuUuid(tradeItemTmp.getSkuUuid());
        tradeItem.setCardSaleInfo(paymentInfo.getTradeVo().getTradeItemList().get(0).getCardSaleInfos().get(0));
        jcBindCardReq.setTradeItem(tradeItem);
        return jcBindCardReq;
    }

    private void doJCValueCardScanPay(final FragmentActivity context, final IPaymentInfo paymentInfo, String authCode) {

    }

    private boolean checkInputPaymentInfo(Context context, PaymentVo paymentVo) {
        if (paymentVo != null) {
            Payment payment = paymentVo.getPayment();
            if (payment != null) {
                BigDecimal emptyAmount = payment.getReceivableAmount().subtract(payment.getActualAmount());
                if (emptyAmount.compareTo(payment.getExemptAmount()) != 0) {
                    ToastUtil.showShortToast(context.getString(R.string.pay_cash_emptyamount_is_error));
                    return false;
                }
            } else {
                ToastUtil.showShortToast(context.getString(R.string.pay_cash_paymentinfo_empty_alter));
                return false;
            }
            if (!Utils.isEmpty(paymentVo.getPaymentItemList())) {
                for (PaymentItem item : paymentVo.getPaymentItemList()) {
                    if (item.getUsefulAmount().compareTo(BigDecimal.ZERO) == -1) {
                        ToastUtil.showShortToast(context.getString(R.string.pay_negative_not_pay));
                        return false;
                    }
                    BigDecimal changeAmount = item.getFaceAmount().subtract(item.getUsefulAmount());
                    if ((item.getChangeAmount().compareTo(changeAmount) == 1)) {
                        ToastUtil.showShortToast(context.getString(R.string.pay_cash_changeamount_is_error));
                        return false;
                    }
                }
            } else {
                ToastUtil.showShortToast(context.getString(R.string.pay_cash_paymentinfo_empty_alter));
                return false;
            }
        }
        return true;
    }


    public void doPrint(IPaymentInfo paymentInfo, String tradeUuid, boolean isPrintLabel, boolean isPrintKitchen, boolean isOpenMoneyBox, boolean isPintPayTick) {
        if (isOpenMoneyBox && paymentInfo.getOtherPay().isContainsPayModel(PayModeId.CASH)) {
        }
        if (paymentInfo.isDinner() || paymentInfo.getTradeVo().isBeauty()) {
            if (isPintPayTick) {
                if (paymentInfo.getTradeBusinessType() == BusinessType.BUFFET && paymentInfo.getTradeVo().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
                    TradeVo printTradeVo = DinnerCashManager.cloneValidTradeVo(DinnerShopManager.getInstance().getShoppingCart().createOrder());
                } else {
                }
            }
        } else {
            if (paymentInfo.getTradeVo() != null && paymentInfo.getTradeVo().getTradeDeposit() != null
                    && paymentInfo.getTradeVo().getTradeDeposit().getStatusFlag() == StatusFlag.VALID) {
            } else {
            }
        }
    }


    public void memberPayPrint(CustomerResp customer, EcCard ecCard, PayResp resp) throws Exception {
        if (resp.getPrintOperations() == null || resp.getPrintOperations().isEmpty()) {
            return;
        }
        String tmp = resp.getPrintOperations().get(0).getExtendsStr();
        JSONObject extendsStr = new JSONObject(tmp);
        double subValue = extendsStr.optDouble("subValue", 0);
        double beforeActualvalue = extendsStr.optDouble("beforeActualvalue", 0);
        double beforeSendValue = extendsStr.optDouble("beforeSendValue", 0);
        double currentReduceActualValue = extendsStr.optDouble("currentReduceActualValue", 0);
        double currentReduceSendValue = extendsStr.optDouble("currentReduceSendValue", 0);
        ChargingPrint print = new ChargingPrint();
        if (ecCard != null) {
            print.setCardNum(ecCard.getCardNum());
            print.setCustomerName(ecCard.getCustomer().getName());
            print.setCustomerSex(String.valueOf(ecCard.getCustomer().getSex()));
            print.setPhoneNo(ecCard.getCustomer().getMobile());
        } else if (customer != null) {
            print.setCustomerName(customer.customerName);
            print.setCustomerSex(customer.sex + "");
            print.setPhoneNo(customer.mobile);
        }
        print.setChargingType(2);
        print.setTrueIncomeValuecard(BigDecimal.valueOf(currentReduceActualValue));
        print.setBeforeValuecard(BigDecimal.valueOf(beforeActualvalue + beforeSendValue));
        print.setCommercialName(ShopInfoCfg.getInstance().commercialName);
        print.setUserId(Session.getAuthUser().getName());
        print.setChargeValuecard(BigDecimal.valueOf(subValue));
        print.setCustomerIntegral(null);
        print.setEndValuecard(print.getBeforeValuecard().subtract(print.getChargeValuecard()));

        print.setPresentStart(BigDecimal.valueOf(beforeSendValue));
        print.setPresentEnd(BigDecimal.valueOf(beforeSendValue - currentReduceSendValue));
        print.setCapitalStart(BigDecimal.valueOf(beforeActualvalue));
        print.setCapitalEnd(BigDecimal.valueOf(beforeActualvalue - currentReduceActualValue));

        print.setPayMethods(null);
        print.setChargingTime(resp.getTrades().get(0).getServerUpdateTime());


    }

    public void updateShoppingCartTrade(final IPaymentInfo paymentInfo, Trade trade) {
        if (paymentInfo.isSplit()) {
            SeparateShoppingCart.getInstance().updateDataWithTrade(trade);
        } else if (paymentInfo.isDinner()) {
            DinnerShoppingCart.getInstance().updateDataWithTrade(trade);
        }
    }

    public final boolean isPaidScuess(PayResp result, String paymentItemUuid) {
        List<PaymentItem> paymentItems = result.getPaymentItems();
        if (TextUtils.isEmpty(paymentItemUuid)
                || paymentItems == null || paymentItems.isEmpty()) {
            return false;
        }

        for (final PaymentItem item : paymentItems) {
            if (item.getUuid().equals(paymentItemUuid)) {
                if (item.isValid() && item.getPayStatus() == TradePayStatus.PAID) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkPayModeSettings(final Context context) {
        if (!PaySettingCache.isContainsPayModes()) {

            ToastUtil.showShortToast(context.getString(R.string.pay_no_paytype_set));
            return false;
        }
        return true;
    }

    public static void gotoPayActivity(final Activity context, final TradeVo tradeVo, boolean isOrderCenter) {
        gotoPayActivity(context, DoPayManager.getInstance(), tradeVo, isOrderCenter, PayScene.SCENE_CODE_SHOP, -1, -1);
    }

    public static void gotoPayActivity(final Activity context, final TradeVo tradeVo, boolean isOrderCenter, int defaultPayMenuType) {
        gotoPayActivity(context, DoPayManager.getInstance(), tradeVo, isOrderCenter, PayScene.SCENE_CODE_SHOP, defaultPayMenuType, -1);
    }

    public static void gotoPayActivity(final Activity context, final TradeVo tradeVo, boolean isOrderCenter, int defaultPayMenuType, boolean isGroup) {
        gotoPayActivity(context, DoPayManager.getInstance(), tradeVo, isOrderCenter, PayScene.SCENE_CODE_SHOP, defaultPayMenuType, -1, isGroup);
    }

    public static void gotoPayActivity(final Activity context, final TradeVo tradeVo, boolean isOrderCenter, PayScene payScene) {
        gotoPayActivity(context, DoPayManager.getInstance(), tradeVo, isOrderCenter, payScene, -1, -1);
    }

    public static void gotoPayActivity(final Activity context, final TradeVo tradeVo, int defaultPayMenuType, int quickPayType, boolean isGroup) {
        gotoPayActivity(context, DoPayManager.getInstance(), tradeVo, false, PayScene.SCENE_CODE_SHOP, defaultPayMenuType, quickPayType, isGroup);
    }

    public static void gotoPayActivity(final Activity context, DoPayApi doPayApi, final TradeVo tradeVo, boolean isOrderCenter, PayScene payScene, int defaultPayMenuType, int quickPayType) {
        DoPayUtils.gotoPayActivity(context, doPayApi, tradeVo, isOrderCenter, payScene, defaultPayMenuType, quickPayType);
    }

    public static void gotoPayActivity(final Activity context, DoPayApi doPayApi, final TradeVo tradeVo, boolean isOrderCenter, PayScene payScene, int defaultPayMenuType, int quickPayType, boolean isGroupPay) {
        DoPayUtils.gotoPayActivity(context, doPayApi, tradeVo, isOrderCenter, payScene, defaultPayMenuType, quickPayType, isGroupPay);
    }

    public static void fastOnlinePay(FragmentManager fragmentManager, TradeVo tradeVo, PayModeId payModeId, int scanType) {
        fastOnlinePay(fragmentManager, DoPayManager.getInstance(), tradeVo, payModeId, scanType);
    }

    public static void fastOnlinePay(FragmentManager fragmentManager, DoPayApi doPayApi, TradeVo tradeVo, PayModeId payModeId, int scanType) {
        if (fragmentManager == null || tradeVo == null || payModeId == null) return;
        final IPaymentInfo paymentInfo = PaymentInfoManager.getNewInstance(null);
        paymentInfo.setTradeVo(tradeVo);
        PayModelItem payModelItem = new PayModelItem(payModeId);
        payModelItem.setUsedValue(BigDecimal.valueOf(paymentInfo.getActualAmount()));
        final com.zhongmei.bty.mobilepay.fragment.onlinepay.OnlinePayDialog payDialog = com.zhongmei.bty.mobilepay.fragment.onlinepay.OnlinePayDialog.newInstance(doPayApi, paymentInfo, payModelItem, IPayConstParame.ONLIEN_SCAN_TYPE_ACTIVE);
        payDialog.setOnPayStopListener(new IOnlinePayBreakCallback() {
            @Override
            public void onPayStop() {
                if (!DoPayUtils.isTradePaidOver(paymentInfo.getTradeVo().getTrade()))
                    DisplayServiceManager.doCancel(payDialog.getContext().getApplicationContext());
            }
        });
        payDialog.show(fragmentManager, "OnlinePayDialog");
    }

    public static void fastMobilePay(FragmentManager fragmentManager, TradeVo tradeVo, PayModeId payModeId, int scanType) {
        fastMobilePay(fragmentManager, DoPayManager.getInstance(), tradeVo, payModeId, scanType);
    }

    public static void fastMobilePay(FragmentManager fragmentManager, DoPayApi doPayApi, TradeVo tradeVo, PayModeId payModeId, int scanType) {
        if (fragmentManager == null || tradeVo == null || payModeId == null) {
            return;
        }
        final IPaymentInfo paymentInfo = PaymentInfoManager.getNewInstance(null);
        paymentInfo.setTradeVo(tradeVo);
        PayModelItem payModelItem = new PayModelItem(payModeId);
        payModelItem.setUsedValue(BigDecimal.valueOf(paymentInfo.getActualAmount()));
        final MobilePayDialog payDialog = MobilePayDialog.newInstance(doPayApi, paymentInfo, payModelItem, scanType);
        payDialog.setOnPayStopListener(new IOnlinePayBreakCallback() {
            @Override
            public void onPayStop() {
                if (!DoPayUtils.isTradePaidOver(paymentInfo.getTradeVo().getTrade())) {
                    DisplayServiceManager.doCancel(payDialog.getContext().getApplicationContext());
                }
            }
        });
        payDialog.show(fragmentManager, "MobilePayDialog");
    }

    public void fastCashPay(FragmentActivity context, TradeVo tradeVo, boolean isOrderCenter,
                            IPayOverCallback callback) {
        if (context == null || tradeVo == null) return;
        IPaymentInfo paymentInfo = PaymentInfoManager.getNewInstance(null);
        paymentInfo.setTradeVo(tradeVo);
        paymentInfo.setOrdered(isOrderCenter);
        PayModelItem payModelItem = new PayModelItem(PayModeId.CASH);
        payModelItem.setUsedValue(BigDecimal.valueOf(paymentInfo.getActualAmount()));
        payModelItem.setChangeAmount(BigDecimal.ZERO);
        paymentInfo.getOtherPay().addPayModelItem(payModelItem);
        pay(context, paymentInfo, callback);
    }

    public void payErrorHandler(final FragmentActivity context, ResponseObject<PayResp> response, final IPaymentInfo paymentInfo, IOnlinePayOverCallback callback) {
        AuthLogManager.getInstance().clear();
        if (callback != null)
            callback.onPayResult(null, TradePayStatus.PAID_FAIL.value());
        switch (response.getStatusCode()) {
            case ResponseObject.COUPON_CHECK_FAILED:
            case ResponseObject.COUPON_FAILED:
                if (paymentInfo.getPayActionPage() == PayActionPage.BALANCE) {
                    PayUtils.showDinnerRemoveCouponDilog(context, paymentInfo, response.getMessage(), response.getContent().getPromoIds());

                } else if (paymentInfo.getPayActionPage() == PayActionPage.COMPAY) {
                    PayUtils.showRemoveCouponOrDelTradeDialog(context, paymentInfo, response.getMessage(), response.getContent().getPromoIds());

                } else {
                    ToastUtil.showLongToast(response.getMessage());
                }
                break;
            case ResponseObject.WEIXINCODE_CHECK_FAILED:
            case ResponseObject.WEIXIN_COUPON_FAILED:
                if (paymentInfo.getPayActionPage() == PayActionPage.BALANCE) {
                    PayUtils.showDinnerRemoveWeixinCouponDilog(context, response.getMessage(), response.getContent().getPromoIds());

                } else if (paymentInfo.getPayActionPage() == PayActionPage.COMPAY) {
                    PayUtils.showRemoveWeiXinCouponsOrDelTradeDialog(context, paymentInfo,
                            response.getMessage(),
                            response.getContent().getPromoIds());
                } else {
                    ToastUtil.showLongToast(response.getMessage());
                }
                break;
            case ResponseObject.MEMBER_REJECT:
                String errorText = context.getString(R.string.order_dish_member_disabled);
                ToastUtil.showLongToast(errorText);
                break;
            case ResponseObject.CORE_PAY_PROCESS_FAIL:
            case ResponseObject.PAY_PROCESS_FAIL:
                AuthLogManager.getInstance().clear();
                if (response.getContent() != null) {
                    if (!Utils.isEmpty(response.getContent().getTrades())) {
                        paymentInfo.getTradeVo().setTrade(response.getContent().getTrades().get(0));
                        if (paymentInfo.isDinner() || paymentInfo.getTradeVo().isBeauty()) {
                            if (paymentInfo.isSplit()) {
                                SeparateShoppingCart.getInstance().updateDataFromTradeVo(paymentInfo.getTradeVo());
                            } else {
                                DinnerShoppingCart.getInstance().updateDataFromTradeVo(paymentInfo.getTradeVo());
                            }
                            paymentInfo.setOrdered(true);
                        }
                    }
                    if (!Utils.isEmpty(response.getContent().getPayments())) {
                        new AsyncTask<Void, Void, List<PaymentVo>>() {
                            @Override
                            protected List<PaymentVo> doInBackground(Void... params) {
                                List<PaymentVo> list = null;
                                try {
                                    TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
                                    list = tradeDal.listPayment(paymentInfo.getTradeVo().getTrade().getUuid(), PaymentType.TRADE_SELL);
                                } catch (Exception e) {
                                    Log.e(TAG, "", e);
                                }
                                return list;
                            }

                            protected void onPostExecute(List<PaymentVo> data) {
                                paymentInfo.setPaidPaymentRecords(data);
                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
                showErrorToast(response);
                break;
            case ResponseObject.CARD_SERVICE_CHECK_FAILED:
                ToastUtil.showLongToast(response.getMessage());
                break;
            default:
                showErrorToast(response);
                break;

        }


    }

    public void doVerifyPayResp(final FragmentActivity context, final IPaymentInfo paymentInfo, final PayResp result, final IOnlinePayOverCallback payOverCallback) {
        List<PaymentItem> paymentItems = result.getPaymentItems();
        if (TextUtils.isEmpty(getOnlinePaymentItemUuid())
                || paymentItems == null || paymentItems.isEmpty() || context == null) {
            return;
        }
        if (!paymentInfo.getId().equals(getCurrentPaymentInfoId())) {
            return;
        }
        for (final PaymentItem item : paymentItems) {
            if (item.getUuid().equals(getOnlinePaymentItemUuid())) {
                if (item.getPayStatus() == TradePayStatus.PAID) {
                    snackPaidRemind(context, item);
                    setOnlinePaymentItemUuid(null);
                    paymentInfo.getTradeVo().setTrade(result.getTrades().get(0));
                    if (DoPayUtils.isTradePaidOver(paymentInfo.getTradeVo().getTrade())) {
                        boolean isPintPayTick = true;
                        if (DoPayUtils.isHaveTradeTax(paymentInfo.getTradeVo())) {
                            isPintPayTick = false;
                        }
                        if (paymentInfo.getCustomer() == null && paymentInfo.getEcCard() != null) {
                            paymentInfo.setPrintMemeberInfoByCard();
                        }
                        if (!paymentInfo.isDinner() && paymentInfo.getTradeVo().getTrade().getTradeType() == TradeType.SELL_FOR_REPEAT) {
                            EventBus.getDefault().post(new SellForRepeatEvent(paymentInfo.getTradeVo().getTrade().getUuid()));
                        }

                        if (paymentInfo.isSplit() && (paymentInfo.getOtherPay().isContainsPayModel(PayModeId.MEMBER_CARD)
                                || paymentInfo.getOtherPay().isContainsPayModel(PayModeId.ENTITY_CARD)
                                || paymentInfo.getOtherPay().isContainsPayModel(PayModeId.ANONYMOUS_ENTITY_CARD)
                        )) {
                            MemberPayChargeEvent memberPayChargeEvent = new MemberPayChargeEvent();
                            memberPayChargeEvent.setmValueCardBalance(BigDecimal.valueOf(CashInfoManager.floatSubtract(paymentInfo.getMemberCardBalance(), paymentInfo.getOtherPay().getGroupActualAmount())));
                            EventBus.getDefault().post(memberPayChargeEvent);
                        }

                        if (!paymentInfo.isPrintedOk()) {
                            EventBus.getDefault().post(new StopPayStatusTimer(true));
                            Trade trade = paymentInfo.getTradeVo().getTrade();
                            if (result.getPrintOperations() != null && !result.getPrintOperations().isEmpty()
                                    && paymentInfo.getMemberResp() != null) {
                                try {
                                    String tmp = result.getPrintOperations().get(0).getExtendsStr();
                                    JSONObject extendsStr = new JSONObject(tmp);
                                    double beforeActualvalue = extendsStr.optDouble("beforeActualvalue", 0);
                                    double beforeSendValue = extendsStr.optDouble("beforeSendValue", 0);
                                    paymentInfo.getMemberResp().setValueCardBalance(BigDecimal.valueOf(beforeActualvalue + beforeSendValue));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (paymentInfo.isOrderCenter() && trade.getDeliveryType() == DeliveryType.HERE
                                    && trade.getSource() == SourceId.POS && trade.getSourceChild() == SourceChild.ANDROID) {
                                doPrint(paymentInfo, paymentInfo.getTradeVo().getTrade().getUuid(), true, true, true, isPintPayTick);
                                paymentInfo.setPrintedOk(true);
                            } else {
                                if (ServerSettingCache.getInstance().isJinChBusiness()) {
                                    if (paymentInfo.getTradeBusinessType() != BusinessType.CARD
                                            && paymentInfo.getTradeBusinessType() != BusinessType.ONLINE_RECHARGE
                                            && paymentInfo.getTradeBusinessType() != BusinessType.ENTITY_CARD_CHANGE
                                            && paymentInfo.getTradeBusinessType() != BusinessType.ANONYMOUS_ENTITY_CARD_RECHARGE) {
                                        doPrint(paymentInfo, paymentInfo.getTradeVo().getTrade().getUuid(), !paymentInfo.isOrderCenter(), !paymentInfo.isOrderCenter(), true, isPintPayTick);
                                        paymentInfo.setPrintedOk(true);
                                    }
                                } else {
                                    if (paymentInfo.getTradeBusinessType() != BusinessType.ENTITY_CARD_CHANGE && paymentInfo.getPayScene() != PayScene.SCENE_CODE_BOOKING_DEPOSIT) {
                                        doPrint(paymentInfo, paymentInfo.getTradeVo().getTrade().getUuid(), !paymentInfo.isOrderCenter(), !paymentInfo.isOrderCenter(), true, isPintPayTick);
                                        paymentInfo.setPrintedOk(true);
                                    }
                                }
                            }
                            AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_CHECK_OUT, trade.getId(), trade.getUuid(), trade.getClientUpdateTime());
                        }

                    } else {
                        if (paymentInfo.isDinner() && paymentInfo.getPayScene() != PayScene.SCENE_CODE_BUFFET_DEPOSIT) {
                            if (paymentInfo.isSplit()) {
                                EventBus.getDefault().post(new SeparateEvent(SeparateEvent.EVENT_SEPARATE_PAYING));
                            } else {
                                EventBus.getDefault().post(new SeparateEvent(SeparateEvent.EVENT_RESOURCE_PAYING));
                            }
                        }
                        if (paymentInfo.getOtherPay().isContainsPayModel(PayModeId.MEMBER_CARD)
                                || paymentInfo.getOtherPay().isContainsPayModel(PayModeId.ENTITY_CARD)
                                || paymentInfo.getOtherPay().isContainsPayModel(PayModeId.ANONYMOUS_ENTITY_CARD)
                        ) {
                            MemberPayChargeEvent memberPayChargeEvent = new MemberPayChargeEvent();
                            memberPayChargeEvent.setmValueCardBalance(BigDecimal.valueOf(CashInfoManager.floatSubtract(paymentInfo.getMemberCardBalance(), paymentInfo.getOtherPay().getGroupActualAmount())));
                            EventBus.getDefault().post(memberPayChargeEvent);
                        }
                    }
                    if (ServerSettingCache.getInstance().isJinChBusiness()
                            && (paymentInfo.getTradeBusinessType() == BusinessType.ONLINE_RECHARGE
                            || paymentInfo.getTradeBusinessType() == BusinessType.ANONYMOUS_ENTITY_CARD_RECHARGE)) {
                        EventPayResult payResult = new EventPayResult(true, paymentInfo.getTradeBusinessType());
                        payResult.setContent(result);
                        EventBus.getDefault().post(payResult);
                    }
                    if (paymentInfo.getOtherPay().isContainsPayModel(PayModeId.MEMBER_CARD)
                            && Utils.isNotEmpty(result.getPaymentItemExtras())) {
                        PaymentItemExtra paymentItemExtra = result.getPaymentItemExtras().get(0);
                        Long customerId = paymentItemExtra.getCustomerId();
                        if (customerId != null) {
                            ResponseListener<MemberLoginVoResp> listener = new ResponseListener<MemberLoginVoResp>() {
                                @Override
                                public void onResponse(ResponseObject<MemberLoginVoResp> response) {
                                    if (ResponseObject.isOk(response)
                                            && MemberLoginVoResp.isOk(response.getContent())) {

                                        CustomerLoginResp resp = response.getContent().getResult();
                                        if (resp.customerIsDisable()) {
                                            return;
                                        }
                                        CustomerResp customerNew = resp.getCustomer();
                                        try {
                                            memberPayPrint(customerNew, null, result);

                                        } catch (Exception e) {
                                            Log.e(TAG, "", e);
                                        }
                                    }
                                }

                                @Override
                                public void onError(VolleyError error) {
                                }
                            };
                            CustomerManager.getInstance()
                                    .customerLogin(CustomerLoginType.MEMBER_ID, String.valueOf(customerId), "", false, false, false, listener);
                        }
                    }

                    if (paymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT && paymentInfo.getTradeBusinessType() == BusinessType.BUFFET) {
                        DepositPayOver depositPayOver = new DepositPayOver(result);
                        EventBus.getDefault().post(depositPayOver);
                        paymentInfo.setPrintedOk(true);
                    }
                    if (paymentInfo.getTradeVo().getTrade().getTradeStatus() != TradeStatus.FINISH || paymentInfo.isOpenElectronicInvoice()) {
                        new AsyncTask<Void, Void, List<PaymentVo>>() {
                            @Override
                            protected List<PaymentVo> doInBackground(Void... params) {
                                List<PaymentVo> list = null;
                                try {
                                    TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
                                    list = tradeDal.listPayment(paymentInfo.getTradeVo().getTrade().getUuid(), PaymentType.TRADE_SELL);
                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage(), e);
                                }
                                return list;
                            }

                            protected void onPostExecute(List<PaymentVo> data) {
                                paymentInfo.setPaidPaymentRecords(data);
                                if (payOverCallback != null) {
                                    payOverCallback.onPayResult(item.getId(), item.getPayStatus().value());
                                }
                                PayUtils.showPayOkDialog(context, paymentInfo);
                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        if (payOverCallback != null) {
                            payOverCallback.onPayResult(item.getId(), item.getPayStatus().value());
                        }
                        PayUtils.showPayOkDialog(context, paymentInfo);
                    }

                } else if (item.getPayStatus() == TradePayStatus.PAID_FAIL) {
                    String resultMsg = TextUtils.isEmpty(result.getTopPaymentItemResultMsg()) ? result.getMessage() : result.getTopPaymentItemResultMsg();
                    if (payOverCallback != null) {
                        payOverCallback.onPayResult(item.getId(), item.getPayStatus().value());
                    }
                    PayUtils.showPayErrorDialog(context, paymentInfo, resultMsg, null);

                } else {
                    if (payOverCallback != null) {
                        payOverCallback.onPayResult(item.getId(), item.getPayStatus().value());
                    }
                }
                break;
            }
        }
    }

    private static void showErrorToast(ResponseObject<PayResp> response) {
        String resultMsg = response.getContent() == null ? null : response.getContent().getTopPaymentItemResultMsg();
        if (!TextUtils.isEmpty(resultMsg)) {
            ToastUtil.showLongToast(resultMsg);
        } else {
            ToastUtil.showLongToast(response.getMessage());
        }
    }

    private static void snackPaidRemind(Context context, PaymentItem paymentItem) {
        int strId;
        if (paymentItem.getPayModeId().equals(PayModeId.ALIPAY.value())) {
            strId = R.string.alipay_get_amount;
        } else if (paymentItem.getPayModeId().equals(PayModeId.WEIXIN_PAY.value())) {
            strId = R.string.wechatpay_get_amount;
        } else {
            return;
        }
        String text = context.getResources().getString(strId);
        String amountText = CashInfoManager.formatCashAmount(paymentItem.getUsefulAmount().doubleValue());
        QueuePlayServiceManager.playAudition(String.format(text, amountText));
    }
}
