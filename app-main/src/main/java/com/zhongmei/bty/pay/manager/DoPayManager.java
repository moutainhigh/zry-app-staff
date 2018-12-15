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

/**
 * 新收银接口(v3)封装管理,主要是为正餐提供接口
 * Created by demo on 2018/12/15
 */

public class DoPayManager extends DoPayApi<PayResp> {
    private static final long serialVersionUID = 1L;
    private static final String TAG = DoPayManager.class.getSimpleName();
    //private CashierPrintManager cashierPrintManager;


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
            //modify begin 20170323 start 解决关闭权限按钮收银键盘无法编辑问题
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
            //modify begin 20170323 end
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
                    onlinePayCallback.onBarcodeError();//生成二维码失败
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
                        //获取支付明细uuid,用于推送
                        setOnlinePaymentItemUuid(paymentItem.getPaymentItemUuid());
                        Map addition = resp.getPaymentItemResults().get(0).getAddition();
                        final String codeUrl = (String) addition.get("codeUrl");
                        //打印押金单 add 20170707 start
                        if (paymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT && paymentInfo.getTradeBusinessType() == BusinessType.BUFFET) {
                            DepositPayOver depositPayOver = new DepositPayOver(resp);
                            EventBus.getDefault().post(depositPayOver);
                        }
                        //判断订单时间戳有没有更新 add v8.2
                        if (!Utils.isEmpty(resp.getTrades())) {
                            Trade trade = resp.getTrades().get(0);
                            if (trade.getServerUpdateTime() > paymentInfo.getTradeVo().getTrade().getServerUpdateTime()) {
                                paymentInfo.getTradeVo().setTrade(trade);
                                updateShoppingCartTrade(paymentInfo, trade);
                            }
                        }
                        //add v8.16 抽取公共方法
                        barcodeScuessCallback(context, onlinePayCallback, payModelItem, paymentItem.getPaymentItemId(), codeUrl);

                        //打印押金单 add 20170707 end
                       /* switch (payModelItem.getPayMode()) {

                            case ALIPAY:  // 支付宝方式
                                bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                                logo = BitmapFactory.decodeResource(context.getResources(), com.zhongmei.yunfu.mobilepay.R.drawable.pay_online_alipay);
                                bitmap = EncodingHandler.addQRCodeLogo(bitmap, logo);
                                onlinePayCallback.onBarcodeScuess(paymentItem.getPaymentItemId(), bitmap, codeUrl, true);
                                break;

                            case BAIFUBAO:// 百度钱包
                                if (!TextUtils.isEmpty(codeUrl) && URLUtil.isValidUrl(codeUrl)) {
                                    CalmImageRequest imageRequest =
                                            new CalmImageRequest(codeUrl, new Response.Listener<Bitmap>() {

                                                @Override
                                                public void onResponse(Bitmap bitmap) {
                                                    onlinePayCallback.onBarcodeScuess(paymentItem.getPaymentItemId(), bitmap, codeUrl, false);
                                                }
                                            }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    ToastUtil.showLongToast(error.getMessage());
                                                    onlinePayCallback.onBarcodeError();

                                                }
                                            });
                                    imageRequest.executeRequest("3", false, "");

                                } else {
                                    ToastUtil.showLongToast(response.getMessage());
                                    onlinePayCallback.onBarcodeError();
                                }
                                break;
                            case WEIXIN_PAY:// 微信
                                // 生成二维码
                                bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                                // 客显显示二维码
                                logo = BitmapFactory.decodeResource(context.getResources(), com.zhongmei.yunfu.mobilepay.R.drawable.pay_online_weixin);
                                bitmap = EncodingHandler.addQRCodeLogo(bitmap, logo);
                                onlinePayCallback.onBarcodeScuess(paymentItem.getPaymentItemId(), bitmap, codeUrl, true);
                                break;
                            case MEITUAN_FASTPAY://点评闪付

                                bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                                onlinePayCallback.onBarcodeScuess(paymentItem.getPaymentItemId(), bitmap, codeUrl, true);
                                break;

                            case JIN_CHENG:// 金诚
                                // 生成二维码
                                bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                                logo = BitmapFactory.decodeResource(context.getResources(), com.zhongmei.yunfu.mobilepay.R.drawable.pay_other_jin_cheng);
                                bitmap = EncodingHandler.addQRCodeLogo(bitmap, logo);
                                onlinePayCallback.onBarcodeScuess(paymentItem.getPaymentItemId(), bitmap, codeUrl, true);
                                break;

                            case MEMBER_CARD:
                                // 生成二维码
                                bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                                logo = BitmapFactory.decodeResource(context.getResources(), com.zhongmei.yunfu.mobilepay.R.drawable.pay_online_member_card);
                                bitmap = EncodingHandler.addQRCodeLogo(bitmap, logo);
                                onlinePayCallback.onBarcodeScuess(paymentItem.getPaymentItemId(), bitmap, codeUrl, true);
                                break;

                            case DIANXIN_YIPAY://add v8.16
                                bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                                logo = BitmapFactory.decodeResource(context.getResources(), com.zhongmei.yunfu.mobilepay.R.drawable.pay_online_yipay);
                                bitmap = EncodingHandler.addQRCodeLogo(bitmap, logo);
                                onlinePayCallback.onBarcodeScuess(paymentItem.getPaymentItemId(), bitmap, codeUrl, true);
                                break;

                            default:
                                // 生成二维码
                                bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                                bitmap = EncodingHandler.addQRCodeLogo(bitmap, logo);
                                onlinePayCallback.onBarcodeScuess(paymentItem.getPaymentItemId(), bitmap, codeUrl, true);
                                break;
                        }*/
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
                    // 获取二维码失败
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
        if (payModelItem.getPayMode() == PayModeId.JIN_CHENG_VALUE_CARD) {//如果是金城充值，走金城支付 add v8.14 for 在线支付解耦
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
                    // 支付结束
                    PayResp resp = response.getContent();
                    if (ResponseObject.isOk(response) && !Utils.isEmpty(resp.getPaymentItemResults())) {
                        //获取支付明细uuid,用于推送
                        final PayResp.PItemResults paymentItem = resp.getPaymentItemResults().get(0);
                        setOnlinePaymentItemUuid(paymentItem.getPaymentItemUuid());
                        onlinePayCallback.onAuthCodeScuess(paymentItem.getPaymentItemId());
                        //打印押金单 add 20170707 start
                        if (paymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT && paymentInfo.getTradeBusinessType() == BusinessType.BUFFET) {
                            DepositPayOver depositPayOver = new DepositPayOver(resp);
                            EventBus.getDefault().post(depositPayOver);
                        }
                        //打印押金单 add 20170707 end
                        UserActionEvent.end(getUserActionEventName(payModelItem.getPayMode()));
                        //add 20170724 如果支付成功直接处理结果
                        doVerifyPayResp(context, paymentInfo, resp, onlinePayCallback);// 处理收银结果
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
                try {// 支付结束
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
                        //如果是购物消费场景（解决押金在线一直轮训问题），且已经支付成功，且支付完成，但是订单状态没改成完成或预支付，继续轮训 modify v8.7
                        if (paymentInfo.getPayScene() != PayScene.SCENE_CODE_BUFFET_DEPOSIT && isPaidScuess(result, paymentItemUuid) && paymentInfo.getActualAmount() <= paymentInfo.getOtherPay().getGroupAmount() && !DoPayUtils.isTradePaidOver(trade)) {
                            onlinePayOverCallback.onPayResult(null, TradePayStatus.PAYING.value());
                        } else {
                            doVerifyPayResp(context, paymentInfo, result, onlinePayOverCallback);// 处理收银结果
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
        payListener.setPaymentVo(paymentVo);//add 20161228
        this.pay(context, paymentInfo, callback, payListener);
    }

    private void pay(final FragmentActivity context, final IPaymentInfo paymentInfo, IPayOverCallback callback, final ResponseListener<PayResp> payListener) {
        final NewTradeOperatesImpl tradeOperates = OperatesFactory.create(NewTradeOperatesImpl.class);
        final PaymentVo paymentVo = paymentInfo.getTradePaymentVo();
        //检测支付数据是否正确
        if (!this.checkInputPaymentInfo(context, paymentVo)) {
            if (callback != null) {
                callback.onFinished(false, 0);
            }
            return;
        }
        //应付金额不能为负
        if (paymentInfo.getActualAmount() < 0) {
            ToastUtil.showShortToast(R.string.pay_negative_not_pay);
            if (callback != null) {
                callback.onFinished(false, 0);
            }
            return;
        }

        // 烘焙支付
        /*if (!BakeryPayData.getInstance().isUnKnown()) {
            // 烘焙收银及下单
            BakeryPayFactory.newInstance().toPayForBakery(context, paymentInfo, callback);
            return;
        }*/

        if (paymentInfo.isOrdered() || (paymentInfo.getTradeVo().getTrade().getTradePayStatus() == TradePayStatus.PAYING
                && paymentInfo.getTradeVo().getTrade().getBusinessType() != BusinessType.BUFFET))//如果已经保存订单直接收银
        {
            if (paymentInfo.getTradeVo().isBeauty()) {
                //美业使用新的接口
                tradeOperates.beautyPay(paymentInfo.getTradeVo().getTrade(), new PaymentReqTool(paymentInfo).creatPaymentReq(),
                        LoadingResponseListener.ensure(payListener, context.getSupportFragmentManager()));
            } else {
                //v3 收银接口
                //add v8.13 begin 订金抵扣
                if (!paymentInfo.getOtherPay().isEmpty()) {//add 2018.027  添加空数据判断
                    PayModelItem payModelItem = paymentInfo.getOtherPay().getAllPayModelItems().get(0);
                    if (payModelItem.getPayMode() == PayModeId.EARNEST_DEDUCT) {//订金抵扣
                        if (paymentInfo.getTradeVo().getTradeEarnestMoney() > paymentInfo.getActualAmount()) { //抵扣并退款
                            tradeOperates.bookingRrePayRefund(new PaymentReqTool(paymentInfo).createPrePayRefundReq(), LoadingResponseListener.ensure(payListener, context.getSupportFragmentManager()));
                        } else { // 抵扣并结账
                            tradeOperates.useEarnestDeduct(new PaymentReqTool(paymentInfo).createEarnestDeductReq(), LoadingResponseListener.ensure(payListener, context.getSupportFragmentManager()));
                        }
                        //add v8.13 end 订金抵扣
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
                    if (isOk) {//v3 收银接口
                        if (paymentInfo.getTradeVo().isBeauty()) {
                            //美业使用新的接口
                            tradeOperates.beautyPay(paymentInfo.getTradeVo().getTrade(), new PaymentReqTool(paymentInfo).creatPaymentReq(),
                                    LoadingResponseListener.ensure(payListener, context.getSupportFragmentManager()));
                        } else {
                            //add v8.13 begin 订金抵扣
                            if (!paymentInfo.getOtherPay().isEmpty()) {//add 2018.027  添加空数据判断
                                PayModelItem payModelItem = paymentInfo.getOtherPay().getAllPayModelItems().get(0);
                                if (payModelItem.getPayMode() == PayModeId.EARNEST_DEDUCT) {//订金抵扣
                                    if (paymentInfo.getTradeVo().getTradeEarnestMoney() > paymentInfo.getActualAmount()) {//抵扣并退款
                                        tradeOperates.bookingRrePayRefund(new PaymentReqTool(paymentInfo).createPrePayRefundReq(), LoadingResponseListener.ensure(payListener, context.getSupportFragmentManager()));
                                    } else {// 抵扣并结账
                                        tradeOperates.useEarnestDeduct(new PaymentReqTool(paymentInfo).createEarnestDeductReq(), LoadingResponseListener.ensure(payListener, context.getSupportFragmentManager()));
                                    }
                                    //add v8.13 end 订金抵扣
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
            //保存订单数据
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
                    //订单为负，不能支付
                    if (PayUtils.isNegativeTradeAmount(paymentInfo.getTradeVo())) {
                        ToastUtil.showShortToast(R.string.negative_not_pay);
                        if (callback != null) {
                            callback.onFinished(false, 0);
                        }
                        return;
                    }
                    //正餐需要判断支付条件
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
                    //如果是联台主单，走合单接口
                    if (paymentInfo.getTradeVo().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
                        tradeOperates.unionAndModifyUnionTrade(DinnerMergeUnionManager.createUnionTradeMergeReq(paymentInfo.getTradeVo()), LoadingResponseListener.ensure(listener, context.getSupportFragmentManager()));
                    } else {
                        if (paymentInfo.isSplit() && paymentInfo.getTradeVo().getTrade().getId() == null) {// 正餐拆单
                            tradeOperates.tradeSplitDinner(paymentInfo.getSourceTradeVo(),
                                    paymentInfo.getTradeVo(),
                                    LoadingResponseListener.ensure(listener, context.getSupportFragmentManager()));
                        } else {
                            if (paymentInfo.getTradeVo().getTrade().getId() == null)// 正餐下单
                            {
                                tradeOperates.insert(paymentInfo.getTradeVo(), LoadingResponseListener.ensure(listener, context.getSupportFragmentManager()));
                            } else // 正餐改单
                            {
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
                    //快餐反结账走改单
                    if (paymentInfo.getTradeVo().getTrade().getId() != null || paymentInfo.getTradeVo().getTrade().getTradeType() == TradeType.SELL_FOR_REPEAT) {
                        tradeOperates.modifyDinner(paymentInfo.getTradeVo(), LoadingResponseListener.ensure(listener, context.getSupportFragmentManager()));
                    } else {
                        //modify 20180126 begin 快餐走下单及收银老接口弃用
                        // PaymentVo paymentVo = paymentInfo.getTradePaymentVo();
                        //新订单如果一次付完且不是储值支付,直接调用下单及收银接口
                       /* if (paymentInfo.getNotPayMent() <= 0 && !isConditionInsertAndPay(paymentVo.getPaymentItemList())) {
                            InsertAndPayListener insertAndPayListener = new InsertAndPayListener(context, paymentInfo, callback);
                            List<PaymentVo> paymentVoList = new ArrayList<PaymentVo>();
                            paymentVoList.add(paymentVo);
                            tradeOperates.insertAndPay(paymentInfo.getTradeVo(),
                                    paymentVoList,
                                    null,
                                    LoadingResponseListener.ensure(insertAndPayListener, context.getSupportFragmentManager()));
                        } else*/
                        //modify 20180126 end
                        {
                            // 快餐下单
                            tradeOperates.insert(paymentInfo.getTradeVo(), LoadingResponseListener.ensure(listener, context.getSupportFragmentManager()));
                        }
                    }
                    break;
                case ENTITY_CARD_CHANGE:
                    ChangeCardReq changeCardReq = paymentInfo.getTradeVo().getChangeCardReq();
                    String url;
                    if (ServerSettingCache.getInstance().isJinChBusiness()) {
                        url = ServerAddressUtil.getInstance().getJCChangeCardUrl();
                    } else {
                        url = ServerAddressUtil.getInstance().getChangeCardUrl();
                    }
                    new CalmNetWorkRequest.Builder<ChangeCardReq, ChangeCardResp>()
                            .requestContent(changeCardReq)
                            .url(url)
                            .with(context)
                            .responseClass(ChangeCardResp.class)
                            .showLoading()
                            .successListener(new CalmResponseListener<ResponseObject<ChangeCardResp>>() {
                                @Override
                                public void onSuccess(ResponseObject<ChangeCardResp> data) {
                                    if (ResponseObject.isOk(data)) {
                                        Trade trade = data.getContent().getTrade();
                                        paymentInfo.getTradeVo().setTrade(trade);
                                        if (savedCallback != null) {
                                            savedCallback.onSaved(true);
                                        }
                                    } else {
                                        ToastUtil.showLongToast(data.getMessage());
                                        if (callback != null) {
                                            callback.onFinished(false, 0);
                                        }
                                        //保存后回调处理
                                        if (savedCallback != null) {
                                            savedCallback.onSaved(false);
                                        }
                                    }
                                }

                                @Override
                                public void onError(NetError error) {
                                    ToastUtil.showLongToast(error.getVolleyError().getMessage());
                                    if (callback != null) {
                                        callback.onFinished(false, 0);
                                    }
                                    //保存后回调处理
                                    if (savedCallback != null) {
                                        savedCallback.onSaved(false);
                                    }
                                }
                            })
                            .tag(url)
                            .create();
                    break;
                case ONLINE_RECHARGE:
                case ANONYMOUS_ENTITY_CARD_RECHARGE:
                    // v8.11.0 大客户充值
                    /*if (KeyAt.getKeyAtType() == KeyAtType.YAZUO) {
                        // 临时判断 等待金城合并到分发器中，取消判断条件
                        KeyAtDispatcher.getInstance().keyAtChargeCard(paymentInfo, savedCallback, callback);
                    }*/
                    // v8.3.1 金诚临时卡充值
                    if (ServerSettingCache.getInstance().isJinChBusiness()) {
                        JCRechargeCardReq jcRechargeCardReq = paymentInfo.getTradeVo().getRechargeCardReq();
                        String jcRechargeUrl = ServerAddressUtil.getInstance().getJCRechargeUrl();
                        new CalmNetWorkRequest.Builder<JCRechargeCardReq, JCRechargeCardResp>()
                                .requestContent(jcRechargeCardReq)
                                .url(jcRechargeUrl)
                                .with(context)
                                .responseClass(JCRechargeCardResp.class)
                                .showLoading()
                                .successListener(new CalmResponseListener<ResponseObject<JCRechargeCardResp>>() {
                                    @Override
                                    public void onSuccess(ResponseObject<JCRechargeCardResp> data) {
                                        if (ResponseObject.isOk(data)) {
                                            Trade trade = data.getContent().getTrade();
                                            paymentInfo.getTradeVo().setTrade(trade);
                                            if (savedCallback != null) {
                                                savedCallback.onSaved(true);
                                            }
                                        } else {
                                            ToastUtil.showLongToast(data.getMessage());
                                            if (callback != null) {
                                                callback.onFinished(false, 0);
                                            }
                                            //保存后回调处理
                                            if (savedCallback != null) {
                                                savedCallback.onSaved(false);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(NetError error) {
                                        ToastUtil.showLongToast(error.getVolleyError().getMessage());
                                        if (callback != null) {
                                            callback.onFinished(false, 0);
                                        }
                                        //保存后回调处理
                                        if (savedCallback != null) {
                                            savedCallback.onSaved(false);
                                        }
                                    }
                                })
                                .tag(jcRechargeUrl)
                                .create();
                    }
                    break;
                case CARD:
                case ANONYMOUS_ENTITY_CARD_SELL:
                    // v8.11.0 大客户绑卡
                    /*if (KeyAt.getKeyAtType() == KeyAtType.YAZUO) {
                        // 临时判断 等待金城合并到分发器中，取消判断条件
                        KeyAtDispatcher.getInstance().keyAtBindCard(paymentInfo, savedCallback, callback);
                    }*/
                    // v8.3.1 金诚临时卡绑卡
                    if (ServerSettingCache.getInstance().isJinChBusiness()) {
                        String jcBindUrl = ServerAddressUtil.getInstance().getJCBindCardUrl();
                        new CalmNetWorkRequest.Builder<JCBindCardReq, JCBindCardResp>()
                                .requestContent(createJCBindCardReq(paymentInfo))
                                .url(jcBindUrl)
                                .with(context)
                                .responseClass(JCBindCardResp.class)
                                .showLoading()
                                .successListener(new CalmResponseListener<ResponseObject<JCBindCardResp>>() {
                                    @Override
                                    public void onSuccess(ResponseObject<JCBindCardResp> data) {
                                        if (ResponseObject.isOk(data)) {
                                            Trade trade = data.getContent().getTrade();
                                            paymentInfo.getTradeVo().setTrade(trade);
                                            if (savedCallback != null) {
                                                savedCallback.onSaved(true);
                                            }
                                        } else {
                                            ToastUtil.showLongToast(data.getMessage());
                                            if (callback != null) {
                                                callback.onFinished(false, 0);
                                            }
                                            //保存后回调处理
                                            if (savedCallback != null) {
                                                savedCallback.onSaved(false);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(NetError error) {
                                        ToastUtil.showLongToast(error.getVolleyError().getMessage());
                                        if (callback != null) {
                                            callback.onFinished(false, 0);
                                        }
                                        //保存后回调处理
                                        if (savedCallback != null) {
                                            savedCallback.onSaved(false);
                                        }
                                    }
                                })
                                .tag(jcBindUrl)
                                .create();
                    }
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
                case POS_PAY://开放平台来的
                default:
                    if (paymentInfo.getTradeVo().getTrade().getId() != null) {
                        //正餐改单
                        tradeOperates.modifyDinner(paymentInfo.getTradeVo(), LoadingResponseListener.ensure(listener, context.getSupportFragmentManager()));
                    } else {
                        // 下单
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

    /**
     * 团餐收银
     *
     * @param context
     * @param paymentInfo
     * @param callback
     * @param savedCallback
     */
    private void groupSave(final FragmentActivity context, final IPaymentInfo paymentInfo, final IPayOverCallback callback, final ISavedCallback savedCallback) {
        //订单为负，不能支付
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
                //保存后回调处理
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
                    //保存后回调处理
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
                        //显示咖啡杯
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
                        //先把原单会员拿出来，等reset方法玩了以后再放回去（临时方案，后续需要优化）
                        CustomerResp customer = CustomerManager.getInstance().getDinnerLoginCustomer();
                        DinnerShoppingCart.getInstance().updateDataFromTradeVo(tradeVo);
                        CustomerManager.getInstance().setDinnerLoginCustomer(customer);
                        //刷新积分抵现
                        DinnerCashManager cashManager = new DinnerCashManager();
                        if (customer != null) {
                            if (customer.card == null) {
                                cashManager.updateIntegralCash(customer);
                            } else {
                                cashManager.updateIntegralCash(customer.card);
                            }
                        }
                        paymentInfo.setTradeVo(tradeVo);
                        paymentInfo.setOrdered(true);// 标识已经下单
                        //关闭咖啡杯
                        CalmLoadingDialogFragment.hide(mDialogFragment);
                        //保存成功后回调处理
                        if (savedCallback != null) {
                            savedCallback.onSaved(true);
                        }
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }, true);
    }

    /**
     * 自助收银
     *
     * @param context
     * @param paymentInfo
     * @param callback
     * @param savedCallback
     */
    private void buffetSave(final FragmentActivity context, final IPaymentInfo paymentInfo, final IPayOverCallback callback, final ISavedCallback savedCallback) {

        if (paymentInfo.getTradeVo().getTrade() != null && paymentInfo.getTradeVo().getTrade().getId() == null) {
            ToastUtil.showShortToast(R.string.open_table_please);
            if (callback != null) {
                callback.onFinished(false, 0);
            }
            return;
        }

        //订单为负，不能支付
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
        //如果是自助联台主单，走主单改单接口 modify v9.0
        if (paymentInfo.getTradeVo().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
            //tradeOperates.buffetUnionMainTradeModify(BuffetMainTradeModifyManager.creatBuffetMainTradeModifyReq(paymentInfo.getTradeVo()), LoadingResponseListener.ensure(listener, context.getSupportFragmentManager()));

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

    //金城充值卡充值 add v8.14
    private void doJCValueCardScanPay(final FragmentActivity context, final IPaymentInfo paymentInfo, String authCode) {
        /*JCGatewayModel.newInstance(context)
                .requestRechargeCardInfo(authCode, new JCModelResponseListener<JCRechargeCardInfoResp>() {
                    @Override
                    public void onJCSuccess(JCResponse<JCRechargeCardInfoResp> jcResponse) {
                        if (jcResponse == null) {
                            return;
                        }
                        if (jcResponse.isOk()) {
                            JCRechargeCardInfoResp data = jcResponse.getResult(JCRechargeCardInfoResp.class);
                            paymentInfo.setMemberPassword(data.getCardKey());
                            EcCard ecCard = new EcCard();
                            ecCard.setCardNum(data.getCardNum());
                            EcCardLevel ecCardLevel = new EcCardLevel();
                            ecCardLevel.setCardLevelName(context.getString(R.string.jc_customer_card));
                            ecCard.setCardLevel(ecCardLevel);
                            ecCard.setCardType(ANONYMOUS_ENTITY_CARD);
                            //end
                            paymentInfo.setEcCard(ecCard);
                            PayModelItem item = new PayModelItem(PayModeId.JIN_CHENG_VALUE_CARD); //金诚充值卡只有主扫
                            item.setUsedValue(data.getAmount());
                            paymentInfo.getOtherPay().clear();
                            paymentInfo.getOtherPay().addPayModelItem(item);
                            if (data.getAmount().compareTo(BigDecimal.valueOf(paymentInfo.getActualAmount())) != 0) {
                                ToastUtil.showLongToast(context.getString(R.string.value_card_amount_not_equal_charge_amount));
                            } else {
                                doPay(context, paymentInfo, null);
                            }
                        } else {
                            ToastUtil.showLongToast(jcResponse.getMessage());
                        }
                    }
                });*/
    }

    //检测用户输入支付金额是否正确
    private boolean checkInputPaymentInfo(Context context, PaymentVo paymentVo) {
        if (paymentVo != null) {
            Payment payment = paymentVo.getPayment();
            //检测抹零金额正确否（实际该收金额(应收-抹零)）
            if (payment != null) {
                //抹零 = 应收-实际该收金额
                BigDecimal emptyAmount = payment.getReceivableAmount().subtract(payment.getActualAmount());
                if (emptyAmount.compareTo(payment.getExemptAmount()) != 0) {
                    ToastUtil.showShortToast(context.getString(R.string.pay_cash_emptyamount_is_error));
                    return false;
                }
            } else {
                ToastUtil.showShortToast(context.getString(R.string.pay_cash_paymentinfo_empty_alter));
                return false;
            }
            //检测找零金额正确否（找零<= 面值-应付）
            if (!Utils.isEmpty(paymentVo.getPaymentItemList())) {
                for (PaymentItem item : paymentVo.getPaymentItemList()) {
                    //如果应付小于0 ，不容许支付
                    if (item.getUsefulAmount().compareTo(BigDecimal.ZERO) == -1) {
                        ToastUtil.showShortToast(context.getString(R.string.pay_negative_not_pay));
                        return false;
                    }
                    BigDecimal changeAmount = item.getFaceAmount().subtract(item.getUsefulAmount());
                    //如果找零大于面值-应付不容许支付
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

    //modify 20180126 begin 快餐走下单及收银老接口弃用
    //add 20170223 判断是否走V1下单及收银接口(储值、美团、百糯不支持)
    /*private boolean isConditionInsertAndPay(List<PaymentItem> paymentItems) {
        //同一订单不允许相同账号重复支付
        if (!Utils.isEmpty(paymentItems)) {
            for (PaymentItem item : paymentItems) {
                if (PayModeId.MEMBER_CARD.value().equals(item.getPayModeId()) ||
                        PayModeId.ENTITY_CARD.value().equals(item.getPayModeId())
                        || PayModeId.ANONYMOUS_ENTITY_CARD.value().equals(item.getPayModeId())
                        || PayModeId.MEITUAN_TUANGOU.value().equals(item.getPayModeId())
                        || PayModeId.BAINUO_TUANGOU.value().equals(item.getPayModeId())
                        ) {
                    return true;
                }
            }
        }
        return false;
    }*/
    //modify 20180126 end 快餐走下单及收银老接口弃用
    //打印方法
    public void doPrint(IPaymentInfo paymentInfo, String tradeUuid, boolean isPrintLabel, boolean isPrintKitchen, boolean isOpenMoneyBox, boolean isPintPayTick) {
        //如果有现金支付开弹钱箱
        if (isOpenMoneyBox && paymentInfo.getOtherPay().isContainsPayModel(PayModeId.CASH)) {
            //PRTPrintContentQueue.getCommonPrintQueue().openMoneyBox(null);
            //IPrintHelper.Holder.getInstance().openMoneyBox();
        }
        if (paymentInfo.isDinner() || paymentInfo.getTradeVo().isBeauty()) {// 正餐
            if (isPintPayTick) {
                //add v9.0 自助联台主单调用新打印接口，直接传购物车tradeVo,和打印预结单的数据一致
                if (paymentInfo.getTradeBusinessType() == BusinessType.BUFFET && paymentInfo.getTradeVo().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
                    TradeVo printTradeVo = DinnerCashManager.cloneValidTradeVo(DinnerShopManager.getInstance().getShoppingCart().createOrder());
                    //printTradeVo = BuffetMergeUnionManager.createBuffeUnionGroupDishParent(printTradeVo);
                    //IPrintHelper.Holder.getInstance().printDinnerPayTicket(printTradeVo, paymentInfo.getMemberResp(), false, false, new PRTBatchOnSimplePrintListener(PrintTicketTypeEnum.CASH));
                } else {
                    //modify v8.8 替换新接口
                    //IPrintHelper.Holder.getInstance().printDinnerPayTicket(tradeUuid, paymentInfo.getMemberResp(), false, false, new PRTBatchOnSimplePrintListener(PrintTicketTypeEnum.CASH));
                }
                //PLog.d(PLog.TAG_CALLPRINT_KEY, "info:正餐收银调用结账单打印接口printDinnerPayTicket（）;tradeUuid:" + tradeUuid + ",position:" + TAG + "->doPrint()");
            }
        } else {//快餐及外卖
            /*cashierPrintManager = new CashierPrintManager();
            cashierPrintManager.setListener(retryListener);
            if (paymentInfo.getTradeVo().getTrade().getTradeType() == TradeType.SELL_FOR_REPEAT) {
                isPrintLabel = false;
                isPrintKitchen = false;
                cashierPrintManager.printDishChangeKitchenTrade(tradeUuid);
                //PLog.d(PLog.TAG_CALLPRINT_KEY, "info:快餐反结账收银调用厨打接口cashierPrintManager.printDishChangeKitchenTrade（）;tradeUuid:" + tradeUuid + ",position:" + TAG + "->doPrint()");
                //InventoryManager.getInstance().changeInvetory(paymentInfo.getTradeVo());
            }*/
            // 表示有押金
            if (paymentInfo.getTradeVo() != null && paymentInfo.getTradeVo().getTradeDeposit() != null
                    && paymentInfo.getTradeVo().getTradeDeposit().getStatusFlag() == StatusFlag.VALID) {
//                cashierPrintManager.printTrade(tradeUuid, Calm.PRINT_TYPE_GEN, paymentInfo.getMemberResp(), isPintPayTick, isPrintLabel, isPrintKitchen, true);
                //cashierPrintManager.printTrade(paymentInfo.getTradeVo(), paymentInfo.getPaymentRecords(), Calm.PRINT_TYPE_GEN, paymentInfo.getMemberResp(), isPintPayTick, isPrintLabel, isPrintKitchen, true);
            } else {
//                cashierPrintManager.printTrade(tradeUuid, Calm.PRINT_TYPE_GEN, paymentInfo.getMemberResp(), isPintPayTick, isPrintLabel, isPrintKitchen);
                //cashierPrintManager.printTrade(paymentInfo.getTradeVo(), paymentInfo.getPaymentRecords(), Calm.PRINT_TYPE_GEN, paymentInfo.getMemberResp(), isPintPayTick, isPrintLabel, isPrintKitchen);
            }
        }
    }

    /*private CashierPrintManager.retryListener retryListener = new CashierPrintManager.retryListener() {
        @Override
        public void retryData(final List<CashierPrintManager.SendDataEntity> data) {
            if (cashierPrintManager != null) {
                EventBus.getDefault().post(new CashierPrintErrorEvent(data, cashierPrintManager.retryText(data)));
            }
        }
    };*/

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
        //  PrintContentQueue.getInstance().printCardOrMemberCharge(print,false,new OnSimplePrintListener(PrintTicketTypeEnum.STORE_OUT));

        /*PrintTool.printCardOrMemberCharge(print, false,
                new PRTOnSimplePrintListener(PrintTicketTypeEnum.STORE_OUT));*/
    }

    public void updateShoppingCartTrade(final IPaymentInfo paymentInfo, Trade trade) {
        //刷新拆单购物车数据
        if (paymentInfo.isSplit()) {
            SeparateShoppingCart.getInstance().updateDataWithTrade(trade);
        } else if (paymentInfo.isDinner()) {
            DinnerShoppingCart.getInstance().updateDataWithTrade(trade);
        }
    }

    //判断是否支付成功 addv8.7
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

    //跳转到快餐支付界面
    public static void gotoPayActivity(final Activity context, final TradeVo tradeVo, boolean isOrderCenter) {
        gotoPayActivity(context, DoPayManager.getInstance(), tradeVo, isOrderCenter, PayScene.SCENE_CODE_SHOP, -1, -1);//默认是消费付款场景
    }

    //跳转到快餐支付界面
    public static void gotoPayActivity(final Activity context, final TradeVo tradeVo, boolean isOrderCenter, int defaultPayMenuType) {
        gotoPayActivity(context, DoPayManager.getInstance(), tradeVo, isOrderCenter, PayScene.SCENE_CODE_SHOP, defaultPayMenuType, -1);//默认是消费付款场景
    }

    // 跳转支付界面
    public static void gotoPayActivity(final Activity context, final TradeVo tradeVo, boolean isOrderCenter, int defaultPayMenuType, boolean isGroup) {
        gotoPayActivity(context, DoPayManager.getInstance(), tradeVo, isOrderCenter, PayScene.SCENE_CODE_SHOP, defaultPayMenuType, -1, isGroup);//默认是消费付款场景
    }

    //跳转到快餐支付界面
    public static void gotoPayActivity(final Activity context, final TradeVo tradeVo, boolean isOrderCenter, PayScene payScene) {
        gotoPayActivity(context, DoPayManager.getInstance(), tradeVo, isOrderCenter, payScene, -1, -1);//默认是消费付款场景
    }

    // 跳转到快捷支付界面
    public static void gotoPayActivity(final Activity context, final TradeVo tradeVo, int defaultPayMenuType, int quickPayType, boolean isGroup) {
        gotoPayActivity(context, DoPayManager.getInstance(), tradeVo, false, PayScene.SCENE_CODE_SHOP, defaultPayMenuType, quickPayType, isGroup);
    }

    //跳转到快餐支付界面
    public static void gotoPayActivity(final Activity context, DoPayApi doPayApi, final TradeVo tradeVo, boolean isOrderCenter, PayScene payScene, int defaultPayMenuType, int quickPayType) {
        DoPayUtils.gotoPayActivity(context, doPayApi, tradeVo, isOrderCenter, payScene, defaultPayMenuType, quickPayType);
    }

    //跳转到快餐支付界面
    public static void gotoPayActivity(final Activity context, DoPayApi doPayApi, final TradeVo tradeVo, boolean isOrderCenter, PayScene payScene, int defaultPayMenuType, int quickPayType, boolean isGroupPay) {
        DoPayUtils.gotoPayActivity(context, doPayApi, tradeVo, isOrderCenter, payScene, defaultPayMenuType, quickPayType, isGroupPay);
    }

    //快捷在线支付 add v8.6
    public static void fastOnlinePay(FragmentManager fragmentManager, TradeVo tradeVo, PayModeId payModeId, int scanType) {
        fastOnlinePay(fragmentManager, DoPayManager.getInstance(), tradeVo, payModeId, scanType);
    }

    // add v8.14 快捷在线支付
    public static void fastOnlinePay(FragmentManager fragmentManager, DoPayApi doPayApi, TradeVo tradeVo, PayModeId payModeId, int scanType) {
        if (fragmentManager == null || tradeVo == null || payModeId == null) return;
        final IPaymentInfo paymentInfo = PaymentInfoManager.getNewInstance(null);//初始化对象
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

    // v8.12.0 快捷支付一码付
    public static void fastMobilePay(FragmentManager fragmentManager, TradeVo tradeVo, PayModeId payModeId, int scanType) {
        fastMobilePay(fragmentManager, DoPayManager.getInstance(), tradeVo, payModeId, scanType);
    }

    // add v8.14 快捷支付一码付
    public static void fastMobilePay(FragmentManager fragmentManager, DoPayApi doPayApi, TradeVo tradeVo, PayModeId payModeId, int scanType) {
        if (fragmentManager == null || tradeVo == null || payModeId == null) {
            return;
        }
        final IPaymentInfo paymentInfo = PaymentInfoManager.getNewInstance(null);//初始化对象
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

    //快捷现金支付(0元闪结接口，替换PayManager.quickPay接口) add v8.6
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
                //如果正餐， 本地移除
                if (paymentInfo.getPayActionPage() == PayActionPage.BALANCE) {//modify 20180130
                    PayUtils.showDinnerRemoveCouponDilog(context, paymentInfo, response.getMessage(), response.getContent().getPromoIds());

                } else if (paymentInfo.getPayActionPage() == PayActionPage.COMPAY) {
                    //如果已经保存，服务端移除
                    PayUtils.showRemoveCouponOrDelTradeDialog(context, paymentInfo, response.getMessage(), response.getContent().getPromoIds());

                } else {
                    ToastUtil.showLongToast(response.getMessage());
                }
                break;
            case ResponseObject.WEIXINCODE_CHECK_FAILED:
            case ResponseObject.WEIXIN_COUPON_FAILED:
                //如果正餐，关闭收银界面, 本地移除微信优惠券
                if (paymentInfo.getPayActionPage() == PayActionPage.BALANCE) {//modify 20180130
                    PayUtils.showDinnerRemoveWeixinCouponDilog(context, response.getMessage(), response.getContent().getPromoIds());

                }  //如果已经保存，服务端移除
                else if (paymentInfo.getPayActionPage() == PayActionPage.COMPAY) {
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
                        //获取最新订单
                        paymentInfo.getTradeVo().setTrade(response.getContent().getTrades().get(0));
                        //如果正餐
                        if (paymentInfo.isDinner() || paymentInfo.getTradeVo().isBeauty()) {
                            if (paymentInfo.isSplit()) {
                                //刷新拆单购物车数据
                                SeparateShoppingCart.getInstance().updateDataFromTradeVo(paymentInfo.getTradeVo());
                            } else {
                                DinnerShoppingCart.getInstance().updateDataFromTradeVo(paymentInfo.getTradeVo());
                            }
                            paymentInfo.setOrdered(true);//add 20180130
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

    //在线支付处理结果
    public void doVerifyPayResp(final FragmentActivity context, final IPaymentInfo paymentInfo, final PayResp result, final IOnlinePayOverCallback payOverCallback) {
        List<PaymentItem> paymentItems = result.getPaymentItems();
        if (TextUtils.isEmpty(getOnlinePaymentItemUuid())
                || paymentItems == null || paymentItems.isEmpty() || context == null) {
            return;
        }
        //如果不是当前支付界面发起的支付，不处理  add 20170708
        if (!paymentInfo.getId().equals(getCurrentPaymentInfoId())) {
            return;
        }
        for (final PaymentItem item : paymentItems) {
            if (item.getUuid().equals(getOnlinePaymentItemUuid())) {
                if (item.getPayStatus() == TradePayStatus.PAID) {
                    //播放收款成功语音提示
                    snackPaidRemind(context, item);
                    setOnlinePaymentItemUuid(null);//支付成功后清空标识
                    paymentInfo.getTradeVo().setTrade(result.getTrades().get(0));
                    //如果剩余金额小于等于零，表示已经已经完成支付
                    if (DoPayUtils.isTradePaidOver(paymentInfo.getTradeVo().getTrade())) {
                        //add begin v9.0
                        boolean isPintPayTick = true;// 是否打印结账单
                        //如果有消费税，先不打印结账单或消费单
                        if (DoPayUtils.isHaveTradeTax(paymentInfo.getTradeVo())) {
                            isPintPayTick = false;
                        }
                        //add end v9.0
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
                            EventBus.getDefault().post(memberPayChargeEvent);//发送EVENTBUS到会员支付界面MemberPayFragment
                        }
                        //new GetCouponUrlThread().start();

                        // 打印
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
                            // 如果是订单中心的内用且是Android端下的订单，要打印厨房单
                            if (paymentInfo.isOrderCenter() && trade.getDeliveryType() == DeliveryType.HERE
                                    && trade.getSource() == SourceId.POS && trade.getSourceChild() == SourceChild.ANDROID) {
                                doPrint(paymentInfo, paymentInfo.getTradeVo().getTrade().getUuid(), true, true, true, isPintPayTick);
                                paymentInfo.setPrintedOk(true);
                            } else {
                                //金诚绑卡,换卡,金诚充值不打印
                                if (ServerSettingCache.getInstance().isJinChBusiness()) {
                                    if (paymentInfo.getTradeBusinessType() != BusinessType.CARD
                                            && paymentInfo.getTradeBusinessType() != BusinessType.ONLINE_RECHARGE
                                            && paymentInfo.getTradeBusinessType() != BusinessType.ENTITY_CARD_CHANGE
                                            && paymentInfo.getTradeBusinessType() != BusinessType.ANONYMOUS_ENTITY_CARD_RECHARGE) {
                                        doPrint(paymentInfo, paymentInfo.getTradeVo().getTrade().getUuid(), !paymentInfo.isOrderCenter(), !paymentInfo.isOrderCenter(), true, isPintPayTick);
                                        paymentInfo.setPrintedOk(true);
                                    }
                                } else {
                                    //换卡收银不打印,交预付金不打印(add v8.14)
                                    if (paymentInfo.getTradeBusinessType() != BusinessType.ENTITY_CARD_CHANGE && paymentInfo.getPayScene() != PayScene.SCENE_CODE_BOOKING_DEPOSIT) {
                                        doPrint(paymentInfo, paymentInfo.getTradeVo().getTrade().getUuid(), !paymentInfo.isOrderCenter(), !paymentInfo.isOrderCenter(), true, isPintPayTick);
                                        paymentInfo.setPrintedOk(true);
                                    }
                                }
                            }
                            AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_CHECK_OUT, trade.getId(), trade.getUuid(), trade.getClientUpdateTime());
                        }

                    } else {// 继续收银
                        //如果正餐支付中不能改单
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
                            EventBus.getDefault().post(memberPayChargeEvent);//发送EVENTBUS到会员支付界面MemberPayFragment
                        }
                    }
                    //金诚充值打印
                    if (ServerSettingCache.getInstance().isJinChBusiness()
                            && (paymentInfo.getTradeBusinessType() == BusinessType.ONLINE_RECHARGE
                            || paymentInfo.getTradeBusinessType() == BusinessType.ANONYMOUS_ENTITY_CARD_RECHARGE)) {
                        EventPayResult payResult = new EventPayResult(true, paymentInfo.getTradeBusinessType());
                        payResult.setContent(result);
                        EventBus.getDefault().post(payResult);
                    }
                    //打印储值消费单
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
                                            /*PLog.d(PLog.TAG_CALLPRINT_KEY,
                                                    "info:收银调用储值消费单打印接口printCardOrMemberCharge（）;tradeUuid:" + paymentInfo.getTradeVo().getTrade()
                                                            .getUuid()
                                                            + ",position:" + TAG + "->ChargingPayPrint()");*/
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

                    //打印押金单 add 20170707 start
                    if (paymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT && paymentInfo.getTradeBusinessType() == BusinessType.BUFFET) {
                        DepositPayOver depositPayOver = new DepositPayOver(result);
                        EventBus.getDefault().post(depositPayOver);
                        //DinnerPrintHelper.getInstance().printDepositTicket(paymentInfo.getTradeVo().getTrade().getUuid());
                        paymentInfo.setPrintedOk(true);
                    }
                    //打印押金单 add 20170707 end
                    //未完成的订单或者要开电子发票刷新已收金额
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
                                // 通知ui更新
                                paymentInfo.setPaidPaymentRecords(data);
                                //显示收银结果
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

    //快餐(微信，支付宝)支付成功提醒
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
