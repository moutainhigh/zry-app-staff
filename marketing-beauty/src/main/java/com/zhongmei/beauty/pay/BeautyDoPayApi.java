package com.zhongmei.beauty.pay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;

import com.zhongmei.beauty.order.BeautyOrderManager;
import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.manager.QueuePlayServiceManager;
import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.bty.basemodule.pay.message.PayResp;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.data.operates.impl.NewTradeOperatesImpl;
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
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.mobilepay.utils.DoPayUtils;
import com.zhongmei.bty.mobilepay.v1.event.EventPayResult;
import com.zhongmei.yunfu.bean.YFResponse;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.util.EncodingHandler;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.PaymentItemExtra;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.yunfu.db.enums.SourceChild;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.http.CalmImageRequest;
import com.zhongmei.yunfu.beauty.R;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.resp.YFResponseListener;
import com.zhongmei.yunfu.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;


/**
 * 美业收银工具类
 * Created by demo on 2018/12/15
 */

public class BeautyDoPayApi extends DoPayApi<PayResp> {
    private static final String TAG = "BeautyDoPayApi";

    public static BeautyDoPayApi getNewInstance() {
        return new BeautyDoPayApi();
    }

    private static class LazySingletonHolder {
        private static final BeautyDoPayApi INSTANCE = new BeautyDoPayApi();
    }

    public static BeautyDoPayApi getInstance() {
        return LazySingletonHolder.INSTANCE;
    }

    @Override
    public void doPay(final FragmentActivity context, final IPaymentInfo paymentInfo, IPayOverCallback callback) {
        final ResponseListener payListener = new BeautyPayListener(this, context, paymentInfo, callback, false);
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
        if (paymentInfo.getTradeVo().getTrade().getTradePayStatus() == TradePayStatus.PAYING) {
            //美业使用新的接口
            tradeOperates.beautyPay(paymentInfo.getTradeVo().getTrade(), new PaymentReqTool(paymentInfo).creatPaymentReq(),
                    LoadingResponseListener.ensure(payListener, context.getSupportFragmentManager()));
        } else {
            ISavedCallback savedCallback = new ISavedCallback() {

                @Override
                public void onSaved(boolean isOk) {
                    if (isOk) {//v3 收银接口
                        //美业使用新的接口
                        tradeOperates.beautyPay(paymentInfo.getTradeVo().getTrade(), new PaymentReqTool(paymentInfo).creatPaymentReq(),
                                LoadingResponseListener.ensure(payListener, context.getSupportFragmentManager()));
                    }
                }
            };
            //保存订单数据
            saveTrade(context, paymentInfo, callback, savedCallback);
        }
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

    @Override
    public void saveTrade(FragmentActivity context, IPaymentInfo paymentInfo, IPayOverCallback callback, ISavedCallback savedCallback) {
        if (DinnerShopManager.getInstance().hasUnactivedPrivilege(paymentInfo.getTradeVo())) {
            ToastUtil.showShortToast(R.string.privilege_ineffective);
            if (callback != null) {
                callback.onFinished(false, 0);
            }
            return;
        }
        BeautyOrderManager.doBeautyTradePayRequest(this, paymentInfo, context, callback, savedCallback);
    }

    @Override
    public void showOnlinePayDialog(FragmentActivity context, PayModelItem payModelItem, IPaymentInfo paymentInfo, int scanType, IOnlinePayBreakCallback callback) {
        com.zhongmei.bty.mobilepay.fragment.onlinepay.OnlinePayDialog payDialog = com.zhongmei.bty.mobilepay.fragment.onlinepay.OnlinePayDialog.newInstance(this, paymentInfo, payModelItem, IPayConstParame.ONLIEN_SCAN_TYPE_ACTIVE);
        payDialog.setOnPayStopListener(callback);
        payDialog.show(context.getSupportFragmentManager(), "OnlinePayDialog");
    }

    @Override
    public void doPrint(IPaymentInfo paymentInfo, String tradeUuid, boolean isPrintLabel, boolean isPrintKitchen, boolean isOpenMoneyBox, boolean isPintPayTick) {
        //如果有现金支付开弹钱箱
        /*if (isOpenMoneyBox && paymentInfo.getOtherPay().isContainsPayModel(PayModeId.CASH)) {
            //PRTPrintContentQueue.getCommonPrintQueue().openMoneyBox(null);
            IPrintHelper.Holder.getInstance().openMoneyBox();
        }
        if (isPintPayTick) {
                //modify v8.8 替换新接口
                IPrintHelper.Holder.getInstance().printDinnerPayTicket(tradeUuid, paymentInfo.getMemberResp(), false, false, new PRTBatchOnSimplePrintListener(PrintTicketTypeEnum.CASH));
            PLog.d(PLog.TAG_CALLPRINT_KEY, "info:正餐收银调用结账单打印接口printDinnerPayTicket（）;tradeUuid:" + tradeUuid + ",position:" + TAG + "->doPrint()");
        }*/
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
                                DinnerShoppingCart.getInstance().updateDataWithTrade(trade);
                            }
                        }
                        //打印押金单 add 20170707 end
                        switch (payModelItem.getPayMode()) {

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

                            default:
                                // 生成二维码
                                bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                                bitmap = EncodingHandler.addQRCodeLogo(bitmap, logo);
                                onlinePayCallback.onBarcodeScuess(paymentItem.getPaymentItemId(), bitmap, codeUrl, true);
                                break;
                        }
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
                    if (ResponseObject.isOkExisted(response) && !Utils.isEmpty(resp.getPaymentItems())) {
                        //获取支付明细uuid,用于推送
                        final PaymentItem paymentItem = resp.getPaymentItems().get(0);
                        setOnlinePaymentItemUuid(paymentItem.getUuid());
                        onlinePayCallback.onAuthCodeScuess(paymentItem.getId());
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

    //金城充值卡充值,看美业是否接入?
    private void doJCValueCardScanPay(final FragmentActivity context, final IPaymentInfo paymentInfo, String authCode) {

    }

    @Override
    public void getOnlinePayState(final FragmentActivity context, final IPaymentInfo paymentInfo, long paymentItemId, final String paymentItemUuid, final IOnlinePayOverCallback onlinePayOverCallback) {
        ResponseListener<PayResp> listener = getPayRespResponseListener(context, paymentInfo, paymentItemUuid, onlinePayOverCallback);
        NewTradeOperatesImpl tradeOperates = OperatesFactory.create(NewTradeOperatesImpl.class);
        tradeOperates.getOnlinePayStatus(paymentInfo.getTradeVo(), paymentItemId, paymentItemUuid, listener);
    }

    @Override
    public void getOnlinePayStateOfThird(final FragmentActivity context, final IPaymentInfo paymentInfo, final long paymentItemId, final String paymentItemUuid, final IOnlinePayOverCallback onlinePayOverCallback) {
        ResponseListener<PayResp> listener = getPayRespResponseListener(context, paymentInfo, paymentItemUuid, onlinePayOverCallback);
        NewTradeOperatesImpl tradeOperates = OperatesFactory.create(NewTradeOperatesImpl.class);
        tradeOperates.getOnlinePayStatusOfThird(paymentInfo.getTradeVo(), paymentItemId, paymentItemUuid, listener);
    }

    private ResponseListener<PayResp> getPayRespResponseListener(final FragmentActivity context, final IPaymentInfo paymentInfo, final String paymentItemUuid, final IOnlinePayOverCallback onlinePayOverCallback) {
        return new ResponseListener<PayResp>() {

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

    @Override
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

        PaymentItem paymentItem = null;
        for (final PaymentItem item : paymentItems) {
            if (item.getPayStatus() == TradePayStatus.PAID) {
                paymentItem = item;
                break;
            } else {
                if (paymentItem == null || item.getServerUpdateTime() > paymentItem.getServerUpdateTime()) {
                    paymentItem = item;
                }
            }
        }

        if (paymentItem != null) {
            final PaymentItem item = paymentItem;
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
                            //换卡收银不打印,交预付金不打印(add v8.14)
                            if (paymentInfo.getTradeBusinessType() != BusinessType.ENTITY_CARD_CHANGE && paymentInfo.getPayScene() != PayScene.SCENE_CODE_BOOKING_DEPOSIT) {
                                doPrint(paymentInfo, paymentInfo.getTradeVo().getTrade().getUuid(), !paymentInfo.isOrderCenter(), !paymentInfo.isOrderCenter(), true, isPintPayTick);
                                paymentInfo.setPrintedOk(true);
                            }
                        }
                        AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_CHECK_OUT, trade.getId(), trade.getUuid(), trade.getClientUpdateTime());
                    }

                } else {// 继续收银
                    //如果正餐支付中不能改单
                    if (paymentInfo.getPayScene() != PayScene.SCENE_CODE_BUFFET_DEPOSIT) {
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

                //打印储值消费单
                if (paymentInfo.getOtherPay().isContainsPayModel(PayModeId.MEMBER_CARD)
                        && Utils.isNotEmpty(result.getPaymentItemExtras())) {
                    PaymentItemExtra paymentItemExtra = result.getPaymentItemExtras().get(0);
                    Long customerId = paymentItemExtra.getCustomerId();
                    if (customerId != null) {
                        YFResponseListener<YFResponse<CustomerLoginResp>> listener = new YFResponseListener<YFResponse<CustomerLoginResp>>() {
                            @Override
                            public void onResponse(YFResponse<CustomerLoginResp> response) {
                                if (YFResponse.isOk(response)) {

                                    CustomerLoginResp resp = response.getContent();
                                    if (resp.customerIsDisable()) {
                                        return;
                                    }
                                    CustomerResp customerNew = resp.getCustomer();
                                    try {
                                        BeautyPayPrintUtil.memberPayPrint(customerNew, null, result);
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
                                .customerLogin(CustomerLoginType.MEMBER_ID, String.valueOf(customerId), null, false, false, listener);
                    }
                }

                //打印押金单 add 20170707 start
                if (paymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT && paymentInfo.getTradeBusinessType() == BusinessType.BUFFET) {
                    DepositPayOver depositPayOver = new DepositPayOver(result);
                    EventBus.getDefault().post(depositPayOver);
                    BeautyPayPrintUtil.printDepositTicket(paymentInfo.getTradeVo().getTrade().getUuid());
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
                            DoPayUtils.showPayOkDialog(context, BeautyDoPayApi.this, paymentInfo, false, IPayConstParame.OP_TYPE_DOPAY);
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    if (payOverCallback != null) {
                        payOverCallback.onPayResult(item.getId(), item.getPayStatus().value());
                    }
                    DoPayUtils.showPayOkDialog(context, this, paymentInfo, false, IPayConstParame.OP_TYPE_DOPAY);
                }

            } else if (item.getPayStatus() == TradePayStatus.PAID_FAIL) {
                String resultMsg = TextUtils.isEmpty(result.getTopPaymentItemResultMsg()) ? result.getMessage() : result.getTopPaymentItemResultMsg();
                if (payOverCallback != null) {
                    payOverCallback.onPayResult(item.getId(), item.getPayStatus().value());
                }
                DoPayUtils.showPayErrorDialog(context, this, paymentInfo, resultMsg, null, result.getStatus());

            } else {
                if (payOverCallback != null) {
                    payOverCallback.onPayResult(item.getId(), item.getPayStatus().value());
                }
            }
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


    public void payErrorHandler(final FragmentActivity context, ResponseObject<PayResp> response, final IPaymentInfo paymentInfo, IOnlinePayOverCallback callback) {
        AuthLogManager.getInstance().clear();
        if (callback != null)
            callback.onPayResult(null, TradePayStatus.PAID_FAIL.value());
        switch (response.getStatusCode()) {
            case ResponseObject.COUPON_CHECK_FAILED:
            case ResponseObject.COUPON_FAILED:
                //如果正餐， 本地移除
                if (paymentInfo.getPayActionPage() == PayActionPage.BALANCE) {//modify 20180130
                    BeautyCheckDialog.showDinnerRemoveCouponDilog(context, paymentInfo, response.getMessage(), response.getContent().getPromoIds());

                } else {
                    ToastUtil.showLongToast(response.getMessage());
                }
                break;
            case ResponseObject.WEIXINCODE_CHECK_FAILED:
            case ResponseObject.WEIXIN_COUPON_FAILED:
                //如果正餐，关闭收银界面, 本地移除微信优惠券
                if (paymentInfo.getPayActionPage() == PayActionPage.BALANCE) {//modify 20180130
                    BeautyCheckDialog.showDinnerRemoveWeixinCouponDilog(context, response.getMessage(), response.getContent().getPromoIds(), paymentInfo);

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

                        DinnerShoppingCart.getInstance().updateDataFromTradeVo(paymentInfo.getTradeVo());
                        paymentInfo.setOrdered(true);//add 20180130

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
                BeautyCheckDialog.updateServerData(paymentInfo);
                break;
            default:
                showErrorToast(response);
                break;

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
}
