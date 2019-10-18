package com.zhongmei.bty.basemodule.devices.mispos.manager;

import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.bty.basemodule.database.entity.pay.PaymentItemUnionpay;
import com.zhongmei.bty.basemodule.devices.mispos.dialog.UionPayDialogFragment.PosOvereCallback;
import com.zhongmei.bty.basemodule.devices.mispos.dialog.UionPayDialogFragment.UionPayDialogFragmentBuilder;
import com.zhongmei.bty.basemodule.devices.mispos.dialog.UionPayDialogFragment.UionPayStaus;
import com.zhongmei.bty.basemodule.devices.liandipos.NewLDResponse;
import com.zhongmei.bty.basemodule.devices.liandipos.NewLiandiposManager;
import com.zhongmei.bty.basemodule.devices.liandipos.NewLiandiposManager.RefundRef;
import com.zhongmei.bty.basemodule.devices.liandipos.PosConnectManager;
import com.zhongmei.bty.basemodule.pay.bean.PaymentItemUnionpayVo;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.bty.basemodule.pay.operates.PaymentItemDal;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.enums.RefundWay;
import com.zhongmei.bty.basemodule.trade.message.RefundStatusReq;
import com.zhongmei.bty.basemodule.trade.message.RefundStatusReq.RefundPaymentItem;
import com.zhongmei.bty.basemodule.trade.message.RefundStatusResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.commonmodule.database.entity.local.PosTransLog;
import com.zhongmei.bty.commonmodule.database.enums.PosBusinessType;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.ArrayList;
import java.util.List;


public class DinnerPosManager {

    private final static String TAG = DinnerPosManager.class.getSimpleName();

    public final static int UNIONPAY_REFUND_SUCCESS = 1;

    public final static int UNIONPAY_REFUND_FAIL = 2;


    public PaymentItem getPosPaymentItem(String tradeUuid) {
        try {
            TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
            List<PaymentVo> paymentVoList = tradeDal.listPayment(tradeUuid);
            return getPosPaymentItem(paymentVoList);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return null;
    }


    public PaymentItem getPosPaymentItem(List<PaymentVo> paymentVoList) {
        if (Utils.isNotEmpty(paymentVoList)) {
            for (PaymentVo paymentVo : paymentVoList) {
                List<PaymentItem> paymentItemList = paymentVo.getPaymentItemList();
                if (Utils.isNotEmpty(paymentItemList)) {
                    for (PaymentItem paymentItem : paymentItemList) {
                        if (PayModeId.POS_CARD.value().equals(paymentItem.getPayModeId())) {
                            return paymentItem;
                        }
                    }
                }
            }
        }

        return null;
    }


    public static boolean IsMixPayHavePos(TradePaymentVo tradePaymentVo) {
        if (!Utils.isEmpty(tradePaymentVo.getPaymentVoList())) {
            for (PaymentVo paymentVo : tradePaymentVo.getPaymentVoList()) {
                if (!Utils.isEmpty(paymentVo.getPaymentItemList())) {
                    for (PaymentItem paymentItem : paymentVo.getPaymentItemList()) {
                        if (PayModeId.POS_CARD.value().equals(paymentItem.getPayModeId())) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }


    public void changeRefundStatus(Trade sellTrade, TradePayStatus payStatus, PaymentItem refundPaymentItem,
                                   RefundWay refundWay, final PosTransLog log, PaymentItemUnionpay paymentItemUnionpay,
                                   FragmentManager fragmentManager) {
        final TradeOperates operates = OperatesFactory.create(TradeOperates.class);
        AuthUser authUser = Session.getAuthUser();
        List<RefundPaymentItem> refundPaymentItems = new ArrayList<RefundPaymentItem>();
        refundPaymentItems.add(new RefundPaymentItem(refundPaymentItem.getId(), refundWay, payStatus));

        List<PaymentItemUnionpay> refundPaymentItemUnionpays = new ArrayList<PaymentItemUnionpay>();
        if (paymentItemUnionpay != null) {
                        paymentItemUnionpay.setId(null);
            paymentItemUnionpay.setUuid(SystemUtils.genOnlyIdentifier());
            paymentItemUnionpay.setPaymentItemId(refundPaymentItem.getId());
            paymentItemUnionpay.setPaymentItemUuid(refundPaymentItem.getUuid());
            paymentItemUnionpay.setTransType(PosBusinessType.REPEAL);
            paymentItemUnionpay.validateCreate();
                        if (log != null) {
                paymentItemUnionpay.setBatchNumber(log.getBatchNumber());
                paymentItemUnionpay.setPosTraceNumber(log.getPosTraceNumber());
            }
            refundPaymentItemUnionpays.add(paymentItemUnionpay);
        }

        RefundStatusReq req =
                new RefundStatusReq(sellTrade.getId(), authUser.getId(), authUser.getName(),
                        sellTrade.getServerUpdateTime(), payStatus, refundPaymentItems, refundPaymentItemUnionpays);
        ResponseListener<RefundStatusResp> listener =
                LoadingResponseListener.ensure(new ResponseListener<RefundStatusResp>() {

                    @Override
                    public void onResponse(ResponseObject<RefundStatusResp> response) {
                        if (ResponseObject.isOk(response)) {
                            if (log != null) {
                                NewLiandiposManager.getInstance().completeRefund(log.getUuid());                            }
                        } else {
                            ToastUtil.showShortToast(response.getMessage());
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        ToastUtil.showShortToast(error.getMessage());
                    }
                }, fragmentManager);
        operates.changeRefundStatus(req, listener);
    }


    public void posRefund(final Trade sellTrade, final Trade refundTrade, final FragmentManager fragmentManager) throws Exception {
                final PaymentItem paymentItem = getPosPaymentItem(sellTrade.getUuid());        final PaymentItem refundPaymentItem = getPosPaymentItem(refundTrade.getUuid());
        if (paymentItem == null) {
            return;
        }

                if (!PosConnectManager.isPosConnected()) {
            showRefundFailAndOfflineRefundDialog(sellTrade,
                    refundPaymentItem,
                    R.string.no_pos_connected,
                    fragmentManager);
            return;
        }

                PaymentItemDal paymentItemDal = OperatesFactory.create(PaymentItemDal.class);
        final PaymentItemUnionpayVo vo = paymentItemDal.findPaymentItemUnionpayVoByPaymentItemId(paymentItem.getId());

                if (!NewLiandiposManager.getInstance().canRepeal(RefundRef.valueOf(vo.getPaymentItemUnionpay(),
                vo.getPaymentDevice()))) {
            showRefundFailAndOfflineRefundDialog(sellTrade,
                    refundPaymentItem,
                    R.string.cannot_refund_becauseof_settled,
                    fragmentManager);
            return;
        }

        new UionPayDialogFragmentBuilder().setNum(vo.getPaymentDevice().getDeviceNumber())
                .buildReturn(paymentItem.getUsefulAmount(),
                        sellTrade.getTradeNo(),
                        refundPaymentItem.getUuid(),
                        vo.getPaymentItemUnionpay().getPosTraceNumber(),
                        new PosOvereCallback() {

                            @Override
                            public void onFinished(UionPayStaus status, boolean issuccess, final PosTransLog log,
                                                   NewLDResponse ldResponse) {
                                if (issuccess) {
                                    changeRefundStatus(sellTrade,
                                            TradePayStatus.REFUNDED,
                                            refundPaymentItem,
                                            RefundWay.AUTO_REFUND,
                                            log,
                                            vo.getPaymentItemUnionpay(),
                                            fragmentManager);
                                } else {
                                    showRefundFailAndOfflineRefundDialog(sellTrade,
                                            refundPaymentItem,
                                            R.string.mark_offline_refund_title,
                                            fragmentManager);
                                }

                            }
                        })
                .show(fragmentManager, "refund_pos");
    }


    public void retryPosRefund(TradeVo refundTradeVo, final FragmentManager fragmentManager) throws Exception {
        final Trade refundTrade = refundTradeVo.getTrade();

                String renfundTradeUuid = refundTrade.getUuid();
        final PaymentItem refundPaymentItem = new DinnerPosManager().getPosPaymentItem(renfundTradeUuid);
                PaymentItem paymentItem = new DinnerPosManager().getPosPaymentItem(refundTrade.getRelateTradeUuid());        PaymentItemDal paymentItemDal = OperatesFactory.create(PaymentItemDal.class);
        final PaymentItemUnionpay paymentItemUnionpay = paymentItemDal.findByPaymentItemId(paymentItem.getId());

                TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);
        ResponseListener<PaymentItem> listener = LoadingResponseListener.ensure(new ResponseListener<PaymentItem>() {

                                                                                    @Override
                                                                                    public void onResponse(ResponseObject<PaymentItem> response) {
                                                                                        if (ResponseObject.isOk(response)) {
                                                                                            PaymentItem item = response.getContent();
                                                                                            if (item != null && item.getPayStatus() == TradePayStatus.REFUNDED) {
                                                                                                                                                                                                ToastUtil.showShortToast(R.string.pos_refund_successed);
                                                                                                NewLiandiposManager.getInstance().completeRefund(refundPaymentItem.getUuid());
                                                                                            } else {
                                                                                                                                                                                                try {
                                                                                                                                                                                                        Trade sellTrade = DBHelperManager.queryById(Trade.class, refundTrade.getRelateTradeUuid());

                                                                                                    PosTransLog posTransLog =
                                                                                                            DBHelperManager.queryById(PosTransLog.class, refundPaymentItem.getUuid());
                                                                                                    if (posTransLog != null) {
                                                                                                                                                                                                                ToastUtil.showShortToast(R.string.pos_refund_unsuccessed);
                                                                                                        changeRefundStatus(sellTrade,
                                                                                                                TradePayStatus.REFUNDED,
                                                                                                                refundPaymentItem,
                                                                                                                RefundWay.AUTO_REFUND,
                                                                                                                posTransLog,
                                                                                                                paymentItemUnionpay,
                                                                                                                fragmentManager);
                                                                                                    } else {
                                                                                                                                                                                                                ToastUtil.showShortToast(R.string.pos_refund_uncard);
                                                                                                        posRefund(sellTrade, refundTrade, fragmentManager);
                                                                                                    }
                                                                                                } catch (Exception e) {
                                                                                                    Log.e(TAG, e.getMessage(), e);
                                                                                                }
                                                                                            }
                                                                                        } else {
                                                                                            ToastUtil.showShortToast(response.getMessage());
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onError(VolleyError error) {
                                                                                        ToastUtil.showShortToast(error.getMessage());
                                                                                    }

                                                                                },
                fragmentManager);
        tradeOperates.queryPaymentItem(refundPaymentItem.getUuid(), listener);
    }


    public void posRefundForRepay(final Trade sellTrade, Trade refundTrade, final FragmentManager fragmentManager,
                                  final OnPosUIListener listener) throws Exception {
                final DinnerPosManager posManager = new DinnerPosManager();
        final PaymentItem paymentItem = posManager.getPosPaymentItem(refundTrade.getRelateTradeUuid());        final PaymentItem refundPaymentItem = posManager.getPosPaymentItem(refundTrade.getUuid());
        if (paymentItem == null) {            listener.onFail();
            return;
        }

                if (!PosConnectManager.isPosConnected()) {
            ToastUtil.showShortToast(R.string.no_pos_connected);
            listener.onFail();
            return;
        }

                PaymentItemDal paymentItemDal = OperatesFactory.create(PaymentItemDal.class);
        final PaymentItemUnionpayVo vo = paymentItemDal.findPaymentItemUnionpayVoByPaymentItemId(paymentItem.getId());
        final PaymentItemUnionpay refundPaymentItemUnionpay =
                paymentItemDal.findByPaymentItemId(refundPaymentItem.getId());

        new UionPayDialogFragmentBuilder().setNum(vo.getPaymentDevice().getDeviceNumber())
                .buildReturn(paymentItem.getUsefulAmount(),
                        sellTrade.getTradeNo(),
                        refundPaymentItem.getUuid(),
                        vo.getPaymentItemUnionpay().getPosTraceNumber(),
                        new PosOvereCallback() {

                            @Override
                            public void onFinished(UionPayStaus status, boolean issuccess, final PosTransLog log,
                                                   NewLDResponse ldResponse) {
                                final DinnerPosManager posManager = new DinnerPosManager();
                                if (issuccess) {
                                    changeRefundStatus(sellTrade,
                                            TradePayStatus.REFUNDED,
                                            refundPaymentItem,
                                            RefundWay.AUTO_REFUND,
                                            log,
                                            refundPaymentItemUnionpay,
                                            fragmentManager);
                                    listener.onSuccess();
                                } else {
                                    posManager.changeRefundStatus(sellTrade,
                                            TradePayStatus.REFUND_FAILED,
                                            refundPaymentItem,
                                            RefundWay.AUTO_REFUND,
                                            log,
                                            refundPaymentItemUnionpay,
                                            fragmentManager);
                                    listener.onFail();
                                }

                            }
                        })
                .show(fragmentManager, "refund_pos");
    }


    public void showRefundFailAndOfflineRefundDialog(final Trade sellTrade, final PaymentItem refundPaymentItem,
                                                     int titleResId, final FragmentManager fragmentManager) {
        DialogUtil.showWarnConfirmDialog(fragmentManager,
                titleResId,
                R.string.refund_failed,
                R.string.offline_refund,
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        changeRefundStatus(sellTrade,
                                TradePayStatus.REFUND_FAILED,
                                refundPaymentItem,
                                RefundWay.AUTO_REFUND,
                                null,
                                null,
                                fragmentManager);
                    }
                },
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                                                changeRefundStatus(sellTrade,
                                TradePayStatus.REFUNDING,
                                refundPaymentItem,
                                RefundWay.HAND_REFUND,
                                null,
                                null,
                                fragmentManager);
                    }
                },
                "offline_refund");
    }


    public static Trade getTradeByTradeType(List<Trade> trades, TradeType tradeType) {
        for (Trade trade : trades) {
            if (trade.getTradeType() == tradeType) {
                return trade;
            }
        }
        return null;
    }


    public static Trade getTradeByTradeStatus(List<Trade> trades, TradeStatus tradeStatus) {
        for (Trade trade : trades) {
            if (trade.getTradeStatus() == tradeStatus) {
                return trade;
            }
        }
        return null;
    }

    public interface OnPosUIListener {
        void onSuccess();
        void onFail();    }
}
