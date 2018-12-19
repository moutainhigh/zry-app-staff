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

/**
 * 收银接口(v3)回调处理
 * Created by demo on 2018/12/15
 */

public class PayListener extends EventResponseListener<PayResp> implements ResponseListener<PayResp> {
    private static final String TAG = PayListener.class.getSimpleName();
    private FragmentActivity context;
    private IPayOverCallback callback;
    private boolean isAsync;//add 20161010 是否异步
    private IPaymentInfo paymentInfo;
    private PaymentVo paymentVo;//add 20161228
    private GroupPay inputGroupPay;//add v8.16 记录本次支付信息

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
            //显示收银结果
            if (response != null) {
                switch (response.getStatusCode()) {
                    case ResponseObject.OK://成功
                    case ResponseObject.OK_TIMEOUT:
                        //获取最新订单
                        if (response.getContent() != null && !Utils.isEmpty(response.getContent().getTrades())) {
                            this.paymentInfo.getTradeVo().setTrade(response.getContent().getTrades().get(0));
                        }
                        final Trade trade = this.paymentInfo.getTradeVo().getTrade();
                        //如果订单状态是已完成，表示已经完成支付
                        if (DoPayUtils.isTradePaidOver(trade)) {
                            //add begin v9.0
                            boolean isPintPayTick = true;// 是否打印结账单
                            //如果有消费税，先不打印结账单或消费单
                            if (DoPayUtils.isHaveTradeTax(this.paymentInfo.getTradeVo())) {
                                isPintPayTick = false;
                            }
                            //add end v9.0
                            //准备储值支付打印信息
                            if (paymentInfo.getCustomer() == null && paymentInfo.getEcCard() != null) {
                                paymentInfo.setPrintMemeberInfoByCard();
                            }
                            //快餐反结账通知
                            if (!paymentInfo.isDinner() && trade.getTradeType() == TradeType.SELL_FOR_REPEAT) {
                                EventBus.getDefault().post(new SellForRepeatEvent(trade.getUuid()));
                            }

                            //发送预定金收银成功消息
                            //if(paymentInfo.getPayScene() == PayScene.SCENE_CODE_BOOKING_DEPOSIT){
                            //    EventBus.getDefault().post(new BookingDepositPayEvent());
                            //}
                            //发送会员余额通知刷新UI
                            sendMemberPayChargeEvent(paymentInfo);
                            // 打印
                            if (!paymentInfo.isPrintedOk()) {
                                EventBus.getDefault().post(new StopPayStatusTimer(true));
                                //准备储值支付打印信息
                                if (response.getContent().getPrintOperations() != null && !response.getContent().getPrintOperations().isEmpty()
                                        && paymentInfo.getMemberResp() != null) {
                                    String tmp = response.getContent().getPrintOperations().get(0).getExtendsStr();
                                    JSONObject extendsStr = new JSONObject(tmp);
                                    double beforeActualvalue = extendsStr.optDouble("beforeActualvalue", 0);
                                    double beforeSendValue = extendsStr.optDouble("beforeSendValue", 0);
                                    paymentInfo.getMemberResp().setValueCardBalance(BigDecimal.valueOf(beforeActualvalue + beforeSendValue));
                                }

                                // 如果是订单中心的内用且是Android端下的订单，要打印厨房单
                                if (paymentInfo.isOrderCenter() && trade.getDeliveryType() == DeliveryType.HERE
                                        && trade.getSource() == SourceId.POS && trade.getSourceChild() == SourceChild.ANDROID) {
                                    DoPayManager.getInstance().doPrint(paymentInfo, paymentInfo.getTradeVo().getTrade().getUuid(), true, true, true, isPintPayTick);
                                    paymentInfo.setPrintedOk(true);
                                } else {
                                    //金诚绑卡,换卡,金诚充值不打印
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
                                        //换卡收银不打印,交预付金不打印(add v8.14)
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

                        } else {// 继续收银
                            //如果有现金支付开弹钱箱
                            if (paymentInfo.getOtherPay().isContainsPayModel(PayModeId.CASH)) {
                                //PRTPrintContentQueue.getCommonPrintQueue().openMoneyBox(null);
                                //IPrintHelper.Holder.getInstance().openMoneyBox();
                            }
                            //如果正餐结算界面，支付中不能改单
                            if (paymentInfo.getPayActionPage() == PayActionPage.BALANCE) {
                                if (paymentInfo.isSplit()) {
                                    EventBus.getDefault().post(new SeparateEvent(SeparateEvent.EVENT_SEPARATE_PAYING));
                                } else {
                                    EventBus.getDefault().post(new SeparateEvent(SeparateEvent.EVENT_RESOURCE_PAYING));
                                }
                            }
                            //发送会员余额通知刷新UI
                            sendMemberPayChargeEvent(paymentInfo);
                        }

                        /* 每笔支付成功都要执行start */
                        //打印储值消费单
                        if (paymentInfo.getOtherPay().isContainsPayModel(PayModeId.MEMBER_CARD) || paymentInfo.getOtherPay()
                                .isContainsPayModel(PayModeId.ENTITY_CARD)) {
                            try {
                                DoPayManager.getInstance().memberPayPrint(paymentInfo.getCustomer(), paymentInfo.getEcCard(), response.getContent());
                                /*PLog.d(PLog.TAG_CALLPRINT_KEY,
                                        "info:收银调用储值消费单打印接口printCardOrMemberCharge（）;tradeUuid:" + trade.getUuid() + ",position:" + TAG
                                                + "->ChargingPayPrint()");*/
                            } catch (Exception e) {
                                Log.e(TAG, "", e);
                            }
                        }
                        // v8.5.0 打印匿名卡储值消费单
                        if (paymentInfo.getOtherPay().isContainsPayModel(PayModeId.ANONYMOUS_ENTITY_CARD)) {
                            try {
                                EcCard ecCard = paymentInfo.getEcCard();
                                //PrintTool.printAnonyCardTicket(ecCard.getCardNum(), null, response.getContent());
                            } catch (Exception e) {
                                Log.e(TAG, "", e);
                            }
                        }
                        //金诚充值打印事件发送
                        if (ServerSettingCache.getInstance().isJinChBusiness()
                                && (paymentInfo.getTradeBusinessType() == BusinessType.ONLINE_RECHARGE
                                || paymentInfo.getTradeBusinessType() == BusinessType.ANONYMOUS_ENTITY_CARD_RECHARGE)) {
                            EventPayResult payResult = new EventPayResult(true, paymentInfo.getTradeBusinessType());
                            //payResult.setOnlinePay(true);//在线支付充值跳转到充值界面去打印，以便计算余额
                            payResult.setContent(response.getContent());
                            EventBus.getDefault().post(payResult);
                        }

                        // v8.11.0 雅座
                        //KeyAtDispatcher.getInstance().keyAtPrint(paymentInfo.getTradeVo().getTrade(), paymentInfo.getTradeBusinessType(), response.getContent());

                        //打印押金单 add 20170707
                        if (paymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT && paymentInfo.getTradeBusinessType() == BusinessType.BUFFET) {
                            DepositPayOver depositPayOver = new DepositPayOver(response.getContent());
                            EventBus.getDefault().post(depositPayOver);
                            printDepositTicket();
                            paymentInfo.setPrintedOk(true);
                        }
                        //未完成的订单或者要开电子发票刷新已收金额
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
                                    // 通知ui更新
                                    paymentInfo.setPaidPaymentRecords(data);
                                    //显示收银结果
                                    PayUtils.showPayOkDialog(context, paymentInfo, isAsync);
                                    //ui线程回调处理,订单未完成时回调
                                    if (callback != null && (trade.getTradeStatus() != TradeStatus.FINISH || paymentInfo.isSplit() || isMemberPay())) {
                                        callback.onFinished(true, response.getStatusCode());
                                    }
                                    UserActionEvent.end(UserActionEvent.DINNER_PAY_SETTLE_CASH);
                                }
                            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                        } else {
                            //显示收银结果
                            PayUtils.showPayOkDialog(context, paymentInfo, isAsync);
                            //ui线程回调处理,订单未完成时回调
                            if (callback != null && (!DoPayUtils.isTradePaidOver(trade) || paymentInfo.isSplit() || isMemberPay())) {
                                callback.onFinished(true, response.getStatusCode());
                            }
                            UserActionEvent.end(UserActionEvent.DINNER_PAY_SETTLE_CASH);
                        }
                        /* 每笔支付成功都要执行 end*/
                        break;

                    case ResponseObject.COUPON_CHECK_FAILED:
                    case ResponseObject.COUPON_FAILED:// 优惠劵验证失败
                        AuthLogManager.getInstance().clear();

                        if (callback != null) {
                            callback.onFinished(false, response.getStatusCode());
                        }
                        //如果正餐， 本地移除
                        if (paymentInfo.getPayActionPage() == PayActionPage.BALANCE) {
                            if (response.getContent() != null && response.getContent().getPromoIds() != null) {
                                PayUtils.showDinnerRemoveCouponDilog(context, paymentInfo, response.getMessage(), response.getContent().getPromoIds());
                            } else {
                                ToastUtil.showLongToast(response.getMessage());
                            }
                        } else if (paymentInfo.getPayActionPage() == PayActionPage.COMPAY) {//如果已经保存，服务端移除
                            if (response.getContent() != null && response.getContent().getPromoIds() != null) {
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
                    case ResponseObject.WEIXIN_COUPON_FAILED:// 微信优惠券验证失败
                        AuthLogManager.getInstance().clear();

                        if (callback != null) {
                            callback.onFinished(false, response.getStatusCode());
                        }
                        if (paymentInfo.getPayActionPage() == PayActionPage.BALANCE) { //如果正餐，本地移除微信优惠券
                            PayUtils.showDinnerRemoveWeixinCouponDilog(context, response.getMessage(), response.getContent().getPromoIds());

                        } else if (paymentInfo.getPayActionPage() == PayActionPage.COMPAY) { //如果快餐界面，调接口移除微信优惠券
                            PayUtils.showRemoveWeiXinCouponsOrDelTradeDialog(context, paymentInfo,
                                    response.getMessage(),
                                    response.getContent().getPromoIds());
                        } else {
                            ToastUtil.showLongToast(response.getMessage());

                        }
                        break;
                    case ResponseObject.INTEGRAL_FAILED:// 积分验证失败 add 20180123
                        AuthLogManager.getInstance().clear();
                        if (callback != null) {
                            callback.onFinished(false, response.getStatusCode());
                        }
                        //如果正餐结算界面， 本地移除积分
                        if (paymentInfo.getPayActionPage() == PayActionPage.BALANCE) {
                            PayUtils.showRemoveDinnerIntegralDialog(context, response.getMessage());

                        } else if (paymentInfo.getPayActionPage() == PayActionPage.COMPAY) { //如果快餐收银界面，服务端移除
                            PayUtils.showRemoveIntegralOrDelTradeDialog(context, paymentInfo,
                                    response.getMessage());
                        } else {
                            ToastUtil.showLongToast(response.getMessage());
                        }
                        break;
                    case ResponseObject.MEMBER_REJECT://会员禁用
                        AuthLogManager.getInstance().clear();
                        if (callback != null) {
                            callback.onFinished(false, response.getStatusCode());
                        }
                        String errorText = context.getString(R.string.order_dish_member_disabled);
                        ToastUtil.showLongToast(errorText);
                        break;
                    //add 20170621 start 支付过程失败（payment paymentItem 都已经写入,trade 时间戳已经改动）
                    case ResponseObject.CORE_PAY_PROCESS_FAIL:
                    case ResponseObject.PAY_PROCESS_FAIL:
                        AuthLogManager.getInstance().clear();
                        if (response.getContent() != null) {
                            if (!Utils.isEmpty(response.getContent().getTrades())) {
                                //获取最新订单
                                this.paymentInfo.getTradeVo().setTrade(response.getContent().getTrades().get(0));
                                //如果正餐
                                if (this.paymentInfo.isDinner()) {
                                    if (this.paymentInfo.isSplit()) {
                                        //刷新拆单购物车数据
                                        SeparateShoppingCart.getInstance().updateDataFromTradeVo(this.paymentInfo.getTradeVo());
                                    } else {
                                        DinnerShoppingCart.getInstance().updateDataFromTradeVo(this.paymentInfo.getTradeVo());
                                    }
                                    this.paymentInfo.setOrdered(true);//add 20180130
                                }
                            }
                            //刷新支付数据
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
                                        // 通知ui更新
                                        paymentInfo.setPaidPaymentRecords(data);
                                    }
                                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }
                        }
                        if (response.getContent() != null) {
                            String resultMsg = TextUtils.isEmpty(response.getContent().getTopPaymentItemResultMsg()) ? response.getMessage()
                                    : response.getContent().getTopPaymentItemResultMsg();
                            //modiyf v8.7 如果是烽火手环，自己处理异常
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
                    //add 20170621 end
                    case ResponseObject.BAINUOCOUPONSPARTOK://百糯券部分成功\部分失败
                        //获取最新订单
                        if (response.getContent() != null && !Utils.isEmpty(response.getContent().getTrades())) {
                            this.paymentInfo.getTradeVo().setTrade(response.getContent().getTrades().get(0));
                        }
                        AuthLogManager.getInstance().clear();
                        //如果正餐支付中不能改单
                        if (paymentInfo.isDinner()) {
                            if (paymentInfo.isSplit()) {
                                EventBus.getDefault().post(new SeparateEvent(SeparateEvent.EVENT_SEPARATE_PAYING));
                            } else {
                                EventBus.getDefault().post(new SeparateEvent(SeparateEvent.EVENT_RESOURCE_PAYING));
                            }
                        }
                        //刷新已收金额
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
                                // 通知ui更新
                                paymentInfo.setPaidPaymentRecords(data);
                                //显示百糯券部分成功\部分失败界面
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
                        //如果返回1100且有用百糯券，跳转到百糯全部失败界面
                        if (response.getStatusCode() == ResponseObject.BusinessOperationFailed && paymentInfo.getOtherPay()
                                .isContainsPayModel(PayModeId.BAINUO_TUANGOU)) {
                            BaiNuoAllErrorDialog dig = new BaiNuoAllErrorDialog(context, paymentInfo, paymentVo, response.getContent(), callback);
                            dig.show();
                        } else {
                            // add 20170614 失败原因
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

    /**
     * 打印押金单
     */
    private void printDepositTicket() {
        //DinnerPrintHelper.getInstance().printDepositTicket(paymentInfo.getTradeVo().getTrade().getUuid());
    }

    @Override
    public void onError(VolleyError error) {
        try {
            if (isAsync)//add 20161010 如果是异步收银
            {
                return;
            }

            PayUtils.showPayErrorDialog(context, paymentInfo, error.getMessage(), callback, 0);

        } catch (Exception e) {
            Log.e(TAG, "", e);
        }

        AuthLogManager.getInstance().clear();
    }

    //发送会员余额通知刷新UI
    private void sendMemberPayChargeEvent(IPaymentInfo paymentInfo) {
        if (paymentInfo.getOtherPay().isContainsPayModel(PayModeId.MEMBER_CARD)
                || paymentInfo.getOtherPay().isContainsPayModel(PayModeId.ENTITY_CARD)
                || paymentInfo.getOtherPay().isContainsPayModel(PayModeId.ANONYMOUS_ENTITY_CARD)
                ) {
            MemberPayChargeEvent memberPayChargeEvent = new MemberPayChargeEvent();
            memberPayChargeEvent.setmValueCardBalance(BigDecimal
                    .valueOf(CashInfoManager.floatSubtract(paymentInfo.getMemberCardBalance(), paymentInfo.getOtherPay().getGroupActualAmount())));
            EventBus.getDefault().post(memberPayChargeEvent);//发送EVENTBUS到会员支付界面MemberPayFragment
        }
    }

    //判断是否虚拟会员支付 add v8.16
    private boolean isMemberPay() {
        if (this.inputGroupPay != null) {
            return this.inputGroupPay.isContainsPayModel(PayModeId.MEMBER_CARD);
        }
        return false;
    }
}