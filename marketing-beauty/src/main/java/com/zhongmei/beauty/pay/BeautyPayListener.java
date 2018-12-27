package com.zhongmei.beauty.pay;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.mobilepay.IPayConstParame;
import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.dialog.BaiNuoAllErrorDialog;
import com.zhongmei.bty.mobilepay.event.DepositPayOver;
import com.zhongmei.bty.mobilepay.event.MemberPayChargeEvent;
import com.zhongmei.bty.mobilepay.event.SeparateEvent;
import com.zhongmei.bty.mobilepay.event.StopPayStatusTimer;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.mobilepay.utils.DoPayUtils;
import com.zhongmei.bty.mobilepay.v1.event.EventPayResult;
import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.bty.basemodule.pay.message.PayResp;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.event.ActionCloseOrderDishActivity;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.yunfu.db.enums.SourceChild;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 收银接口(v3)回调处理
 * Created by demo on 2018/12/15
 */

public class BeautyPayListener implements ResponseListener<PayResp> {
    private static final String TAG = BeautyPayListener.class.getSimpleName();
    private FragmentActivity context;
    private IPayOverCallback callback;
    private boolean isAsync;//add 20161010 是否异步
    private IPaymentInfo paymentInfo;
    private PaymentVo paymentVo;//add 20161228

    private BeautyDoPayApi doPayApi;

    public void setAsync(boolean async) {
        isAsync = async;
    }

    public void setPaymentVo(PaymentVo paymentVo) {
        this.paymentVo = paymentVo;
    }


    public BeautyPayListener(BeautyDoPayApi doPayApi, FragmentActivity activity, IPaymentInfo paymentInfo, IPayOverCallback callBack, boolean isAsync) {
        this.context = activity;
        this.paymentInfo = paymentInfo;
        this.callback = callBack;
        this.isAsync = isAsync;
        this.doPayApi = doPayApi;
    }

    @Override
    public void onResponse(final ResponseObject<PayResp> response) {
        try {
            //显示收银结果
            if (response != null) {
                switch (response.getStatusCode()) {

                    case ResponseObject.OK://成功
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
                                    doPayApi.doPrint(paymentInfo, paymentInfo.getTradeVo().getTrade().getUuid(), true, true, true, isPintPayTick);
                                    paymentInfo.setPrintedOk(true);
                                } else {
                                    //换卡收银不打印,交预付金不打印(add v8.14)
                                    if (paymentInfo.getTradeBusinessType() != BusinessType.ENTITY_CARD_CHANGE && paymentInfo.getPayScene() != PayScene.SCENE_CODE_BOOKING_DEPOSIT) {
                                        doPayApi
                                                .doPrint(paymentInfo, paymentInfo.getTradeVo().getTrade().getUuid(), !paymentInfo.isOrderCenter(),
                                                        !paymentInfo.isOrderCenter(), true, isPintPayTick);
                                        paymentInfo.setPrintedOk(true);
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
                            //发送正在支付中事件
                            EventBus.getDefault().post(new SeparateEvent(SeparateEvent.EVENT_RESOURCE_PAYING));
                            //发送会员余额通知刷新UI
                            sendMemberPayChargeEvent(paymentInfo);
                        }

                        /* 每笔支付成功都要执行start */
                        //打印储值消费单
                        if (paymentInfo.getOtherPay().isContainsPayModel(PayModeId.MEMBER_CARD) || paymentInfo.getOtherPay()
                                .isContainsPayModel(PayModeId.ENTITY_CARD)) {
                            try {
                                BeautyPayPrintUtil.memberPayPrint(paymentInfo.getCustomer(), paymentInfo.getEcCard(), response.getContent());
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
                                /*PRTPrintContentQueue.getCommonPrintQueue()
                                        .printAnonymousCardTicket(PRTStoreBeanConvertHelper.createStoreBean(ecCard.getCardNum(), null, response.getContent()),
                                                new PRTOnSimplePrintListener(PrintTicketTypeEnum.STORE));*/
                            } catch (Exception e) {
                                Log.e(TAG, "", e);
                            }
                        }

                        //打印押金单 add 20170707
                        if (paymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT && paymentInfo.getTradeBusinessType() == BusinessType.BUFFET) {
                            DepositPayOver depositPayOver = new DepositPayOver(response.getContent());
                            EventBus.getDefault().post(depositPayOver);
                            BeautyPayPrintUtil.printDepositTicket(paymentInfo.getTradeVo().getTrade().getUuid());
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
                                    DoPayUtils.showPayOkDialog(context, doPayApi, paymentInfo, isAsync, IPayConstParame.OP_TYPE_DOPAY);
                                    //ui线程回调处理,订单未完成时回调
                                    if (callback != null && (trade.getTradeStatus() != TradeStatus.FINISH || paymentInfo.isSplit())) {
                                        callback.onFinished(true, response.getStatusCode());
                                    }
                                    UserActionEvent.end(UserActionEvent.DINNER_PAY_SETTLE_CASH);
                                }
                            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                        } else {
                            //显示收银结果
                            DoPayUtils.showPayOkDialog(context, doPayApi, paymentInfo, isAsync, IPayConstParame.OP_TYPE_DOPAY);
                            //ui线程回调处理,订单未完成时回调
                            if (callback != null && (!DoPayUtils.isTradePaidOver(trade) || paymentInfo.isSplit())) {
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
                        // 本地移除
                        if (response.getContent() != null && response.getContent().getPromoIds() != null) {
                            BeautyCheckDialog.showDinnerRemoveCouponDilog(context, paymentInfo, response.getMessage(), response.getContent().getPromoIds());
                        } else {
                            //core api未返回要移除的promoid
                            BeautyCheckDialog.updateServerData(paymentInfo);
                            ToastUtil.showLongToast(response.getMessage());
                        }
                        break;
                    case ResponseObject.WEIXINCODE_CHECK_FAILED:
                    case ResponseObject.WEIXIN_COUPON_FAILED:// 微信优惠券验证失败
                        AuthLogManager.getInstance().clear();

                        if (callback != null) {
                            callback.onFinished(false, response.getStatusCode());
                        }
                        BeautyCheckDialog.showDinnerRemoveWeixinCouponDilog(context, response.getMessage(), response.getContent().getPromoIds(), paymentInfo);
                        break;
                    case ResponseObject.INTEGRAL_FAILED:// 积分验证失败 add 20180123
                        AuthLogManager.getInstance().clear();
                        if (callback != null) {
                            callback.onFinished(false, response.getStatusCode());
                        }
                        //本地移除积分
                        BeautyCheckDialog.showRemoveDinnerIntegralDialog(context, response.getMessage(), paymentInfo);
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
                                //如果正餐或者美业
                                DinnerShoppingCart.getInstance().updateDataFromTradeVo(this.paymentInfo.getTradeVo());
                                this.paymentInfo.setOrdered(true);//add 20180130
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
                                        DoPayUtils.showPayErrorDialog(context, doPayApi, paymentInfo, resultMsg, callback, response.getStatusCode());
                                        break;
                                }

                            } else {
                                DoPayUtils.showPayErrorDialog(context, doPayApi, paymentInfo, resultMsg, callback, response.getStatusCode());
                            }
                        } else {
                            DoPayUtils.showPayErrorDialog(context, doPayApi, paymentInfo, response.getMessage(), callback, response.getStatusCode());
                        }
                        break;
                    case ResponseObject.CARD_SERVICE_CHECK_FAILED:
                        ToastUtil.showLongToast(response.getMessage());
                        BeautyCheckDialog.updateServerData(paymentInfo);
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
                                DoPayUtils.showPayErrorDialog(context, doPayApi, paymentInfo, resultMsg, callback, response.getStatusCode());
                            } else {
                                DoPayUtils.showPayErrorDialog(context, doPayApi, paymentInfo, response.getMessage(), callback, response.getStatusCode());
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


    @Override
    public void onError(VolleyError error) {
        try {
            if (isAsync)//add 20161010 如果是异步收银
            {
                return;
            }

            DoPayUtils.showPayErrorDialog(context, doPayApi, paymentInfo, error.getMessage(), callback, 0);

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


}