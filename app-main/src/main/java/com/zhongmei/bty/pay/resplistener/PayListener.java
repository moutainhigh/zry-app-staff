package com.zhongmei.bty.pay.resplistener;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.bty.basemodule.pay.message.PayResp;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.SeparateShoppingCart;
import com.zhongmei.bty.basemodule.trade.event.ActionCloseOrderDishActivity;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.bty.mobilepay.IPayConstParame;
import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.bty.mobilepay.bean.GroupPay;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.dialog.BaiNuoAllErrorDialog;
import com.zhongmei.bty.mobilepay.dialog.BaiNuoPartErrorDialog;
import com.zhongmei.bty.mobilepay.enums.PayActionPage;
import com.zhongmei.bty.mobilepay.event.DepositPayOver;
import com.zhongmei.bty.mobilepay.event.MemberPayChargeEvent;
import com.zhongmei.bty.mobilepay.event.SellForRepeatEvent;
import com.zhongmei.bty.mobilepay.event.SeparateEvent;
import com.zhongmei.bty.mobilepay.event.StopPayStatusTimer;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.mobilepay.utils.DoPayUtils;
import com.zhongmei.bty.mobilepay.v1.event.EventPayResult;
import com.zhongmei.bty.pay.manager.DoPayManager;
import com.zhongmei.bty.pay.utils.PayUtils;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.yunfu.db.enums.SourceChild;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.util.ToastUtil;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;

import de.greenrobot.event.EventBus;



public class PayListener extends EventResponseListener<PayResp> implements ResponseListener<PayResp> {
    private static final String TAG = PayListener.class.getSimpleName();
    private FragmentActivity context;
    private IPayOverCallback callback;
    private boolean isAsync;    private IPaymentInfo paymentInfo;
    private PaymentVo paymentVo;    private GroupPay inputGroupPay;
    public void setAsync(boolean async) {
        isAsync = async;
    }

    public void setPaymentVo(PaymentVo paymentVo) {
        this.paymentVo = paymentVo;
    }

    public static PayListener getNewInstance(FragmentActivity activity, IPaymentInfo paymentInfo, IPayOverCallback callBack, boolean isAsync) {
        return new PayListener(activity, paymentInfo, callBack, isAsync);
    }

    public PayListener(FragmentActivity activity, IPaymentInfo paymentInfo, IPayOverCallback callBack, boolean isAsync) {
        this.context = activity;
        this.paymentInfo = paymentInfo;
        this.callback = callBack;
        this.isAsync = isAsync;
        this.eventName = UserActionEvent.DINNER_PAY_SETTLE_CASH.eventName;
        if (paymentInfo != null && paymentInfo.getOtherPay() != null) {
            this.inputGroupPay = paymentInfo.getOtherPay().clone();
        }
    }

    @Override
    public void onResponse(final ResponseObject<PayResp> response) {
        try {
                        if (response != null) {
                switch (response.getStatusCode()) {
                    case ResponseObject.OK:                    case ResponseObject.OK_TIMEOUT:
                                                if (response.getContent() != null && !Utils.isEmpty(response.getContent().getTrades())) {
                            this.paymentInfo.getTradeVo().setTrade(response.getContent().getTrades().get(0));
                        }
                        final Trade trade = this.paymentInfo.getTradeVo().getTrade();
                                                if (DoPayUtils.isTradePaidOver(trade)) {
                                                        boolean isPintPayTick = true;                                                        if (DoPayUtils.isHaveTradeTax(this.paymentInfo.getTradeVo())) {
                                isPintPayTick = false;
                            }
                                                                                    if (paymentInfo.getCustomer() == null && paymentInfo.getEcCard() != null) {
                                paymentInfo.setPrintMemeberInfoByCard();
                            }
                                                        if (!paymentInfo.isDinner() && trade.getTradeType() == TradeType.SELL_FOR_REPEAT) {
                                EventBus.getDefault().post(new SellForRepeatEvent(trade.getUuid()));
                            }

                                                                                                                                                                        sendMemberPayChargeEvent(paymentInfo);
                                                        if (!paymentInfo.isPrintedOk()) {
                                EventBus.getDefault().post(new StopPayStatusTimer(true));
                                                                if (response.getContent().getPrintOperations() != null && !response.getContent().getPrintOperations().isEmpty()
                                        && paymentInfo.getMemberResp() != null) {
                                    String tmp = response.getContent().getPrintOperations().get(0).getExtendsStr();
                                    JSONObject extendsStr = new JSONObject(tmp);
                                    double beforeActualvalue = extendsStr.optDouble("beforeActualvalue", 0);
                                    double beforeSendValue = extendsStr.optDouble("beforeSendValue", 0);
                                    paymentInfo.getMemberResp().setValueCardBalance(BigDecimal.valueOf(beforeActualvalue + beforeSendValue));
                                }

                                                                if (paymentInfo.isOrderCenter() && trade.getDeliveryType() == DeliveryType.HERE
                                        && trade.getSource() == SourceId.POS && trade.getSourceChild() == SourceChild.ANDROID) {
                                    DoPayManager.getInstance().doPrint(paymentInfo, paymentInfo.getTradeVo().getTrade().getUuid(), true, true, true, isPintPayTick);
                                    paymentInfo.setPrintedOk(true);
                                } else {
                                                                        if (ServerSettingCache.getInstance().isJinChBusiness()) {
                                        if (paymentInfo.getTradeBusinessType() != BusinessType.CARD
                                                && paymentInfo.getTradeBusinessType() != BusinessType.ONLINE_RECHARGE
                                                && paymentInfo.getTradeBusinessType() != BusinessType.ANONYMOUS_ENTITY_CARD_SELL
                                                && paymentInfo.getTradeBusinessType() != BusinessType.ANONYMOUS_ENTITY_CARD_RECHARGE
                                                && paymentInfo.getTradeBusinessType() != BusinessType.ENTITY_CARD_CHANGE) {
                                            DoPayManager.getInstance()
                                                    .doPrint(paymentInfo, paymentInfo.getTradeVo().getTrade().getUuid(), !paymentInfo.isOrderCenter(),
                                                            !paymentInfo.isOrderCenter(), true, isPintPayTick);
                                            paymentInfo.setPrintedOk(true);
                                        }
                                    } else {
                                                                                if (paymentInfo.getTradeBusinessType() != BusinessType.ENTITY_CARD_CHANGE && paymentInfo.getPayScene() != PayScene.SCENE_CODE_BOOKING_DEPOSIT) {
                                            DoPayManager.getInstance()
                                                    .doPrint(paymentInfo, paymentInfo.getTradeVo().getTrade().getUuid(), !paymentInfo.isOrderCenter(),
                                                            !paymentInfo.isOrderCenter(), true, isPintPayTick);
                                            paymentInfo.setPrintedOk(true);
                                        }
                                    }
                                }

                                AuthLogManager.getInstance()
                                        .flush(OrderActionEnum.ACTION_CHECK_OUT, trade.getId(), trade.getUuid(), trade.getClientUpdateTime());
                                if (paymentInfo.getTradeBusinessType() != BusinessType.BOOKING_LIST)
                                    EventBus.getDefault().post(new ActionCloseOrderDishActivity());
                            }

                        } else {                                                        if (paymentInfo.getOtherPay().isContainsPayModel(PayModeId.CASH)) {
                                                                                            }
                                                        if (paymentInfo.getPayActionPage() == PayActionPage.BALANCE) {
                                if (paymentInfo.isSplit()) {
                                    EventBus.getDefault().post(new SeparateEvent(SeparateEvent.EVENT_SEPARATE_PAYING));
                                } else {
                                    EventBus.getDefault().post(new SeparateEvent(SeparateEvent.EVENT_RESOURCE_PAYING));
                                }
                            }
                                                        sendMemberPayChargeEvent(paymentInfo);
                        }


                                                if (paymentInfo.getOtherPay().isContainsPayModel(PayModeId.MEMBER_CARD) || paymentInfo.getOtherPay()
                                .isContainsPayModel(PayModeId.ENTITY_CARD)) {
                            try {
                                DoPayManager.getInstance().memberPayPrint(paymentInfo.getCustomer(), paymentInfo.getEcCard(), response.getContent());

                            } catch (Exception e) {
                                Log.e(TAG, "", e);
                            }
                        }
                                                if (paymentInfo.getOtherPay().isContainsPayModel(PayModeId.ANONYMOUS_ENTITY_CARD)) {
                            try {
                                EcCard ecCard = paymentInfo.getEcCard();
                                                            } catch (Exception e) {
                                Log.e(TAG, "", e);
                            }
                        }
                                                if (ServerSettingCache.getInstance().isJinChBusiness()
                                && (paymentInfo.getTradeBusinessType() == BusinessType.ONLINE_RECHARGE
                                || paymentInfo.getTradeBusinessType() == BusinessType.ANONYMOUS_ENTITY_CARD_RECHARGE)) {
                            EventPayResult payResult = new EventPayResult(true, paymentInfo.getTradeBusinessType());
                                                        payResult.setContent(response.getContent());
                            EventBus.getDefault().post(payResult);
                        }


                                                if (paymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT && paymentInfo.getTradeBusinessType() == BusinessType.BUFFET) {
                            DepositPayOver depositPayOver = new DepositPayOver(response.getContent());
                            EventBus.getDefault().post(depositPayOver);
                            printDepositTicket();
                            paymentInfo.setPrintedOk(true);
                        }
                                                if (!DoPayUtils.isTradePaidOver(trade) || paymentInfo.isOpenElectronicInvoice()) {
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
                                                                        PayUtils.showPayOkDialog(context, paymentInfo, isAsync);
                                                                        if (callback != null && (trade.getTradeStatus() != TradeStatus.FINISH || paymentInfo.isSplit() || isMemberPay())) {
                                        callback.onFinished(true, response.getStatusCode());
                                    }
                                    UserActionEvent.end(UserActionEvent.DINNER_PAY_SETTLE_CASH);
                                }
                            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                        } else {
                                                        PayUtils.showPayOkDialog(context, paymentInfo, isAsync);
                                                        if (callback != null && (!DoPayUtils.isTradePaidOver(trade) || paymentInfo.isSplit() || isMemberPay())) {
                                callback.onFinished(true, response.getStatusCode());
                            }
                            UserActionEvent.end(UserActionEvent.DINNER_PAY_SETTLE_CASH);
                        }

                        break;

                    case ResponseObject.COUPON_CHECK_FAILED:
                    case ResponseObject.COUPON_FAILED:                        AuthLogManager.getInstance().clear();

                        if (callback != null) {
                            callback.onFinished(false, response.getStatusCode());
                        }
                                                if (paymentInfo.getPayActionPage() == PayActionPage.BALANCE) {
                            if (response.getContent() != null && response.getContent().getPromoIds() != null) {
                                PayUtils.showDinnerRemoveCouponDilog(context, paymentInfo, response.getMessage(), response.getContent().getPromoIds());
                            } else {
                                ToastUtil.showLongToast(response.getMessage());
                            }
                        } else if (paymentInfo.getPayActionPage() == PayActionPage.COMPAY) {                            if (response.getContent() != null && response.getContent().getPromoIds() != null) {
                                PayUtils.showRemoveCouponOrDelTradeDialog(context, paymentInfo, response.getMessage(),
                                        response.getContent().getPromoIds());
                            } else {
                                ToastUtil.showLongToast(response.getMessage());
                            }
                        } else {
                            ToastUtil.showLongToast(response.getMessage());
                        }
                        break;
                    case ResponseObject.WEIXINCODE_CHECK_FAILED:
                    case ResponseObject.WEIXIN_COUPON_FAILED:                        AuthLogManager.getInstance().clear();

                        if (callback != null) {
                            callback.onFinished(false, response.getStatusCode());
                        }
                        if (paymentInfo.getPayActionPage() == PayActionPage.BALANCE) {                             PayUtils.showDinnerRemoveWeixinCouponDilog(context, response.getMessage(), response.getContent().getPromoIds());

                        } else if (paymentInfo.getPayActionPage() == PayActionPage.COMPAY) {                             PayUtils.showRemoveWeiXinCouponsOrDelTradeDialog(context, paymentInfo,
                                    response.getMessage(),
                                    response.getContent().getPromoIds());
                        } else {
                            ToastUtil.showLongToast(response.getMessage());

                        }
                        break;
                    case ResponseObject.INTEGRAL_FAILED:                        AuthLogManager.getInstance().clear();
                        if (callback != null) {
                            callback.onFinished(false, response.getStatusCode());
                        }
                                                if (paymentInfo.getPayActionPage() == PayActionPage.BALANCE) {
                            PayUtils.showRemoveDinnerIntegralDialog(context, response.getMessage());

                        } else if (paymentInfo.getPayActionPage() == PayActionPage.COMPAY) {                             PayUtils.showRemoveIntegralOrDelTradeDialog(context, paymentInfo,
                                    response.getMessage());
                        } else {
                            ToastUtil.showLongToast(response.getMessage());
                        }
                        break;
                    case ResponseObject.MEMBER_REJECT:                        AuthLogManager.getInstance().clear();
                        if (callback != null) {
                            callback.onFinished(false, response.getStatusCode());
                        }
                        String errorText = context.getString(R.string.order_dish_member_disabled);
                        ToastUtil.showLongToast(errorText);
                        break;
                                        case ResponseObject.CORE_PAY_PROCESS_FAIL:
                    case ResponseObject.PAY_PROCESS_FAIL:
                        AuthLogManager.getInstance().clear();
                        if (response.getContent() != null) {
                            if (!Utils.isEmpty(response.getContent().getTrades())) {
                                                                this.paymentInfo.getTradeVo().setTrade(response.getContent().getTrades().get(0));
                                                                if (this.paymentInfo.isDinner()) {
                                    if (this.paymentInfo.isSplit()) {
                                                                                SeparateShoppingCart.getInstance().updateDataFromTradeVo(this.paymentInfo.getTradeVo());
                                    } else {
                                        DinnerShoppingCart.getInstance().updateDataFromTradeVo(this.paymentInfo.getTradeVo());
                                    }
                                    this.paymentInfo.setOrdered(true);                                }
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
                        if (response.getContent() != null) {
                            String resultMsg = TextUtils.isEmpty(response.getContent().getTopPaymentItemResultMsg()) ? response.getMessage()
                                    : response.getContent().getTopPaymentItemResultMsg();
                                                        if (paymentInfo.getOtherPay().isContainsPayModel(PayModeId.FENGHUO_WRISTBAND)) {
                                int resultStatus = response.getContent().getTopPaymentItemResultStatus();
                                switch (resultStatus) {
                                    case IPayConstParame.GATWAY_CODE_BALANCE_NOT_ENOUGH:
                                    case IPayConstParame.GATWAY_CODE_PASSWORD_EMPTY:
                                    case IPayConstParame.GATWAY_CODE_PASSWORD_ERROR:
                                        callback.onFinished(false, resultStatus);
                                        break;

                                    default:
                                        PayUtils.showPayErrorDialog(context, paymentInfo, resultMsg, callback, response.getStatusCode());
                                        break;
                                }

                            } else {
                                PayUtils.showPayErrorDialog(context, paymentInfo, resultMsg, callback, response.getStatusCode());
                            }
                        } else {
                            PayUtils.showPayErrorDialog(context, paymentInfo, response.getMessage(), callback, response.getStatusCode());
                        }
                        break;
                                        case ResponseObject.BAINUOCOUPONSPARTOK:                                                if (response.getContent() != null && !Utils.isEmpty(response.getContent().getTrades())) {
                            this.paymentInfo.getTradeVo().setTrade(response.getContent().getTrades().get(0));
                        }
                        AuthLogManager.getInstance().clear();
                                                if (paymentInfo.isDinner()) {
                            if (paymentInfo.isSplit()) {
                                EventBus.getDefault().post(new SeparateEvent(SeparateEvent.EVENT_SEPARATE_PAYING));
                            } else {
                                EventBus.getDefault().post(new SeparateEvent(SeparateEvent.EVENT_RESOURCE_PAYING));
                            }
                        }
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
                                                                BaiNuoPartErrorDialog dialog = new BaiNuoPartErrorDialog(context, paymentInfo, paymentVo, response.getContent(),
                                        callback);
                                dialog.show();
                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        break;
                    case ResponseObject.CARD_SERVICE_CHECK_FAILED:
                        ToastUtil.showLongToast(response.getMessage());
                        break;
                    default:
                        AuthLogManager.getInstance().clear();
                                                if (response.getStatusCode() == ResponseObject.BusinessOperationFailed && paymentInfo.getOtherPay()
                                .isContainsPayModel(PayModeId.BAINUO_TUANGOU)) {
                            BaiNuoAllErrorDialog dig = new BaiNuoAllErrorDialog(context, paymentInfo, paymentVo, response.getContent(), callback);
                            dig.show();
                        } else {
                                                        if (response.getContent() != null) {
                                String resultMsg = TextUtils.isEmpty(response.getContent().getTopPaymentItemResultMsg()) ? response.getMessage()
                                        : response.getContent().getTopPaymentItemResultMsg();
                                PayUtils.showPayErrorDialog(context, paymentInfo, resultMsg, callback, response.getStatusCode());
                            } else {
                                PayUtils.showPayErrorDialog(context, paymentInfo, response.getMessage(), callback, response.getStatusCode());
                            }
                        }
                        break;
                }
            }

        } catch (Exception e) {
            if (callback != null) {
                callback.onFinished(false, response.getStatusCode());
            }
            Log.e(TAG, "", e);
        }
    }


    private void printDepositTicket() {
            }

    @Override
    public void onError(VolleyError error) {
        try {
            if (isAsync)            {
                return;
            }

            PayUtils.showPayErrorDialog(context, paymentInfo, error.getMessage(), callback, 0);

        } catch (Exception e) {
            Log.e(TAG, "", e);
        }

        AuthLogManager.getInstance().clear();
    }

        private void sendMemberPayChargeEvent(IPaymentInfo paymentInfo) {
        if (paymentInfo.getOtherPay().isContainsPayModel(PayModeId.MEMBER_CARD)
                || paymentInfo.getOtherPay().isContainsPayModel(PayModeId.ENTITY_CARD)
                || paymentInfo.getOtherPay().isContainsPayModel(PayModeId.ANONYMOUS_ENTITY_CARD)
                ) {
            MemberPayChargeEvent memberPayChargeEvent = new MemberPayChargeEvent();
            memberPayChargeEvent.setmValueCardBalance(BigDecimal
                    .valueOf(CashInfoManager.floatSubtract(paymentInfo.getMemberCardBalance(), paymentInfo.getOtherPay().getGroupActualAmount())));
            EventBus.getDefault().post(memberPayChargeEvent);        }
    }

        private boolean isMemberPay() {
        if (this.inputGroupPay != null) {
            return this.inputGroupPay.isContainsPayModel(PayModeId.MEMBER_CARD);
        }
        return false;
    }
}