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

/**
 * pos相关业务层的封装类
 *
 * @Date：2016-2-17 下午3:35:04
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class DinnerPosManager {

    private final static String TAG = DinnerPosManager.class.getSimpleName();

    public final static int UNIONPAY_REFUND_SUCCESS = 1;

    public final static int UNIONPAY_REFUND_FAIL = 2;

    /**
     * 获取订单中对应的pos支付记录，不存在时返回null
     *
     * @Title: getPosPaymentitem
     * @Param @param tradeUuid
     * @Return PaymentItem 返回类型
     */
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

    /**
     * 获取支付中对应的pos支付记录，不存在时返回null
     *
     * @Title: getPosPaymentItem
     * @Param @param tradePaymentVo
     * @Return PaymentItem 返回类型
     */
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

    /**
     * @Title: IsMixPayHavePos 判断组合支付中是否有银行卡支付。
     * @Param @param tradePaymentVo
     * @Return PaymentItem 返回类型
     */
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

    /**
     * 修改银联退款状态
     *
     * @param sellTrade           销货单
     * @param payStatus           退款状态 4:REFUNDING:退款中 5:REFUNDED:已退款
     *                            6:REFUND_FAILED:退款失败
     * @param refundPaymentItem   退货支付信息
     * @param refundWay           退款方式，1：无需退款，2：自动退款，3：手动退款
     * @param log                 退款对应的pos刷卡记录（需要在修改退款状态成功后删除）
     * @param paymentItemUnionpay 支付交易的银联明细，经过修改过生成一条退货交易的明细
     * @Title: changeUnionpayRefundStatus
     * @Return void 返回类型
     */
    public void changeRefundStatus(Trade sellTrade, TradePayStatus payStatus, PaymentItem refundPaymentItem,
                                   RefundWay refundWay, final PosTransLog log, PaymentItemUnionpay paymentItemUnionpay,
                                   FragmentManager fragmentManager) {
        final TradeOperates operates = OperatesFactory.create(TradeOperates.class);
        AuthUser authUser = Session.getAuthUser();
        List<RefundPaymentItem> refundPaymentItems = new ArrayList<RefundPaymentItem>();
        refundPaymentItems.add(new RefundPaymentItem(refundPaymentItem.getId(), refundWay, payStatus));

        List<PaymentItemUnionpay> refundPaymentItemUnionpays = new ArrayList<PaymentItemUnionpay>();
        if (paymentItemUnionpay != null) {
            //以支付交易的银联明细为基础，生成退货的交易明细
            paymentItemUnionpay.setId(null);
            paymentItemUnionpay.setUuid(SystemUtils.genOnlyIdentifier());
            paymentItemUnionpay.setPaymentItemId(refundPaymentItem.getId());
            paymentItemUnionpay.setPaymentItemUuid(refundPaymentItem.getUuid());
            paymentItemUnionpay.setTransType(PosBusinessType.REPEAL);
            paymentItemUnionpay.validateCreate();
            //拷贝退款log里的流水号和批次号信息
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
                                NewLiandiposManager.getInstance().completeRefund(log.getUuid());// 当POS刷卡成功的时候log才有值，失败的时候log=null;
                            }
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

    /**
     * 进行pos刷卡退款
     *
     * @param sellTrade 退货订单
     * @throws Exception
     * @Title: posRefund
     * @Return void 返回类型
     */
    public void posRefund(final Trade sellTrade, final Trade refundTrade, final FragmentManager fragmentManager) throws Exception {
        // 查询支付类型为pos刷卡的paymentitem，并发起pos退款
        final PaymentItem paymentItem = getPosPaymentItem(sellTrade.getUuid());// 使用原单的支付信息
        final PaymentItem refundPaymentItem = getPosPaymentItem(refundTrade.getUuid());
        if (paymentItem == null) {
            return;
        }

        // pos机未连接时，直接返回
        if (!PosConnectManager.isPosConnected()) {
            showRefundFailAndOfflineRefundDialog(sellTrade,
                    refundPaymentItem,
                    R.string.no_pos_connected,
                    fragmentManager);
            return;
        }

        // 查询原单pos刷卡信息
        PaymentItemDal paymentItemDal = OperatesFactory.create(PaymentItemDal.class);
        final PaymentItemUnionpayVo vo = paymentItemDal.findPaymentItemUnionpayVoByPaymentItemId(paymentItem.getId());

        // 不能撤销时，直接提示是否线下退款
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

    /**
     * pos二次退款
     *
     * @param refundTradeVo 退货单
     * @throws Exception
     * @Title: retryPosRefund
     * @Return void 返回类型
     */
    public void retryPosRefund(TradeVo refundTradeVo, final FragmentManager fragmentManager) throws Exception {
        final Trade refundTrade = refundTradeVo.getTrade();

        // 1.验证是否为修改已退款状态的下行失败
        String renfundTradeUuid = refundTrade.getUuid();
        final PaymentItem refundPaymentItem = new DinnerPosManager().getPosPaymentItem(renfundTradeUuid);// 退货单的支付信息

        // 查询销货单信息
        PaymentItem paymentItem = new DinnerPosManager().getPosPaymentItem(refundTrade.getRelateTradeUuid());// 原单支付信息
        PaymentItemDal paymentItemDal = OperatesFactory.create(PaymentItemDal.class);
        final PaymentItemUnionpay paymentItemUnionpay = paymentItemDal.findByPaymentItemId(paymentItem.getId());

        // 从服务器验证支付信息是否为已退款
        TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);
        ResponseListener<PaymentItem> listener = LoadingResponseListener.ensure(new ResponseListener<PaymentItem>() {

                                                                                    @Override
                                                                                    public void onResponse(ResponseObject<PaymentItem> response) {
                                                                                        if (ResponseObject.isOk(response)) {
                                                                                            PaymentItem item = response.getContent();
                                                                                            if (item != null && item.getPayStatus() == TradePayStatus.REFUNDED) {
                                                                                                // 订单已经退款成功，删除本地pos刷卡记录
                                                                                                ToastUtil.showShortToast(R.string.pos_refund_successed);
                                                                                                NewLiandiposManager.getInstance().completeRefund(refundPaymentItem.getUuid());
                                                                                            } else {
                                                                                                // 2.查询是否已经刷卡
                                                                                                try {
                                                                                                    // 销货单
                                                                                                    Trade sellTrade = DBHelperManager.queryById(Trade.class, refundTrade.getRelateTradeUuid());

                                                                                                    PosTransLog posTransLog =
                                                                                                            DBHelperManager.queryById(PosTransLog.class, refundPaymentItem.getUuid());
                                                                                                    if (posTransLog != null) {
                                                                                                        // 修改退款状态为已退款
                                                                                                        ToastUtil.showShortToast(R.string.pos_refund_unsuccessed);
                                                                                                        changeRefundStatus(sellTrade,
                                                                                                                TradePayStatus.REFUNDED,
                                                                                                                refundPaymentItem,
                                                                                                                RefundWay.AUTO_REFUND,
                                                                                                                posTransLog,
                                                                                                                paymentItemUnionpay,
                                                                                                                fragmentManager);
                                                                                                    } else {
                                                                                                        // 3.进行完整的刷卡退款流程
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

    /**
     * 进行pos刷卡退款(反结账)
     *
     * @param sellTrade   原销货单
     * @param refundTrade 退货单
     * @throws Exception
     * @Title: posRefund
     * @Return void 返回类型
     */
    public void posRefundForRepay(final Trade sellTrade, Trade refundTrade, final FragmentManager fragmentManager,
                                  final OnPosUIListener listener) throws Exception {
        // 查询支付类型为pos刷卡的paymentitem，并发起pos退款
        final DinnerPosManager posManager = new DinnerPosManager();
        final PaymentItem paymentItem = posManager.getPosPaymentItem(refundTrade.getRelateTradeUuid());// 使用原单的支付信息
        final PaymentItem refundPaymentItem = posManager.getPosPaymentItem(refundTrade.getUuid());
        if (paymentItem == null) {// 如果没有银行卡刷卡那么就走POS刷卡
            listener.onFail();
            return;
        }

        // pos机未连接时，直接返回
        if (!PosConnectManager.isPosConnected()) {
            ToastUtil.showShortToast(R.string.no_pos_connected);
            listener.onFail();
            return;
        }

        // 查询pos刷卡信息
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

    /**
     * 弹出退款失败和线下退款的对话框
     *
     * @Title: showRefundFailAndOfflineRefundDialog
     * @Param @param sellTrade 原单
     * @Param @param refundPaymentItem 退货单支付信息
     * @Param @param titleResId
     * @Return void 返回类型
     */
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
                        // 点击线下退款，状态修改为线下退款中
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

    /**
     * 根据TradeType返回类型，没有则返回NULL
     *
     * @Title: getTradeByTradeType
     * @Param @param trades
     * @Param @param tradeType
     * @Return Trade 返回类型
     */
    public static Trade getTradeByTradeType(List<Trade> trades, TradeType tradeType) {
        for (Trade trade : trades) {
            if (trade.getTradeType() == tradeType) {
                return trade;
            }
        }
        return null;
    }

    /**
     * 根据TradeType返回类型，没有则返回NULL
     *
     * @Title: getTradeByTradeStatus
     * @Param @param trades
     * @Param @param tradeType
     * @Return Trade 返回类型
     */
    public static Trade getTradeByTradeStatus(List<Trade> trades, TradeStatus tradeStatus) {
        for (Trade trade : trades) {
            if (trade.getTradeStatus() == tradeStatus) {
                return trade;
            }
        }
        return null;
    }

    public interface OnPosUIListener {
        void onSuccess();// POS刷卡成功

        void onFail();// POS刷卡失败
    }
}
