package com.zhongmei.bty.cashier.ordercenter.manager;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.bty.basemodule.auth.application.FastFoodApplication;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerShopBiz;
import com.zhongmei.bty.basemodule.commonbusiness.manager.SystemSettingsManager;
import com.zhongmei.yunfu.resp.data.GatewayTransferResp;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.inventory.message.InventoryItemReq;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.bty.basemodule.trade.bean.DeliveryOrderVo;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.basemodule.trade.entity.TradeExtraSecrecyPhone;
import com.zhongmei.bty.basemodule.trade.entity.TradeInvoice;
import com.zhongmei.yunfu.db.entity.trade.TradeReturnInfo;
import com.zhongmei.bty.basemodule.trade.message.BindOrderReq;
import com.zhongmei.bty.basemodule.trade.message.BindOrderResp;
import com.zhongmei.bty.basemodule.trade.message.CancelDeliveryOrderReq;
import com.zhongmei.bty.basemodule.trade.message.CancelDeliveryOrderResp;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderDispatchReq;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderDispatchResp;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderListReq;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderListResp;
import com.zhongmei.bty.basemodule.trade.message.DepositRefundResp;
import com.zhongmei.bty.basemodule.trade.message.QueryDeliveryFeeReq;
import com.zhongmei.bty.basemodule.trade.message.QueryDeliveryFeeResp;
import com.zhongmei.bty.basemodule.trade.message.TakeDishResp;
import com.zhongmei.bty.basemodule.trade.message.TradePaymentResp;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.cashier.ordercenter.bean.LoadResult;
import com.zhongmei.bty.commonmodule.database.entity.DeliveryOrder;
import com.zhongmei.yunfu.db.enums.DeliveryPlatform;
import com.zhongmei.yunfu.db.enums.TradeReturnInfoReturnStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单中心订单详情快餐Model，业务处理类主要负责数据库操作、网络操作
 */

public class SnackOrderCenterDetailManager extends OrderCenterDetailManager {
    private static final String TAG = SnackOrderCenterDetailManager.class.getSimpleName();

    public SnackOrderCenterDetailManager() {
        super();
    }

    @Override
    public void takeDish(TradeExtra tradeExtra, FragmentActivity activity, CalmResponseListener<ResponseObject<TakeDishResp>> listener) {
        mTradeOperates.takeDish(tradeExtra, activity, listener);
    }

    @Override
    public void acceptOrder(TradeVo tradeVo, ResponseListener<TradeResp> listener) {
        mTradeOperates.accept(tradeVo, listener);
    }

    @Override
    public void refuseOrder(TradeVo tradeVo, Reason reason, ResponseListener<TradeResp> listener) {
        mTradeOperates.refuse(tradeVo, reason, listener);
    }

    @Override
    public void returnConfirm(TradeVo tradeVo, TradeReturnInfoReturnStatus returnStatus, boolean isReturnInventory, String requestUuid, Reason reason, ResponseListener<Object> listener) {
        mTradeOperates.returnConfirm(tradeVo.getTrade().getId(), returnStatus, isReturnInventory, requestUuid, reason, listener);
    }

    @Override
    public void recisionOrder(TradeVo tradeVo, Reason reason, List<InventoryItemReq> returnInventoryItems, ResponseListener<TradeResp> listener) {
        mTradeOperates.recision(tradeVo, reason, returnInventoryItems, listener);
    }

    @Override
    public void refundOrder(TradeVo tradeVo, Reason reason, boolean isReturnInventory, List<InventoryItemReq> returnInventoryItems, ResponseListener<TradePaymentResp> listener) {
        mTradeOperates.refund(tradeVo, reason, returnInventoryItems, listener);
    }

    @Override
    public void depositRefund(Long tradeId, BigDecimal depositRefund, Reason reason, ResponseListener<DepositRefundResp> listener) {
        mTradeOperates.depositRefund(tradeId, depositRefund, PayModeId.CASH.value(), reason, listener);
    }


    @Override
    public void cancelDeliveryOrder(TradePaymentVo tradePaymentVo, ResponseListener<GatewayTransferResp<CancelDeliveryOrderResp>> listener) {
        mTradeOperates.cancelDeliveryOrder(toCancelOrderInGateWayReq(tradePaymentVo), listener);
    }

    private CancelDeliveryOrderReq toCancelOrderInGateWayReq(TradePaymentVo tradePaymentVo) {
        CancelDeliveryOrderReq cancelDeliveryOrderReq = new CancelDeliveryOrderReq();
        cancelDeliveryOrderReq.setOrderId(tradePaymentVo.getTradeVo().getTrade().getId());
        cancelDeliveryOrderReq.setOrderNo(tradePaymentVo.getTradeVo().getTrade().getTradeNo());
        cancelDeliveryOrderReq.setShopIdenty(tradePaymentVo.getTradeVo().getTrade().getShopIdenty());
        DeliveryPlatform deliveryPlatform = getDeliveryPlatform(tradePaymentVo);
        if (deliveryPlatform == null) {
            cancelDeliveryOrderReq.setDeliveryPlatform(tradePaymentVo.getTradeVo().getTradeExtra().getDeliveryPlatform().value());
        } else {
            cancelDeliveryOrderReq.setDeliveryPlatform(deliveryPlatform.value());
        }
        cancelDeliveryOrderReq.setBrandIdenty(tradePaymentVo.getTradeVo().getTrade().getBrandIdenty());
        return cancelDeliveryOrderReq;
    }

    @Override
    public void queryDeliveryFee(Long tradeId, String tradeNo, String thirdTranNo, Integer deliveryPlatform, ResponseListener<GatewayTransferResp<QueryDeliveryFeeResp>> listener) {
        QueryDeliveryFeeReq req = toQueryDeliveryFeeReq(tradeId, tradeNo, thirdTranNo, deliveryPlatform);
        mTradeOperates.queryDeliveryFee(req, listener);
    }

    private QueryDeliveryFeeReq toQueryDeliveryFeeReq(Long tradeId, String tradeNo, String thirdTranNo, Integer deliveryPlatform) {
        QueryDeliveryFeeReq req = new QueryDeliveryFeeReq();
        req.setBrandId(MainApplication.getInstance().getBrandIdenty());
        req.setShopId(MainApplication.getInstance().getShopIdenty());
        req.setTradeId(tradeId);
        req.setTradeNo(tradeNo);
        req.setThirdTranNo(thirdTranNo);
        req.setDeliveryPlatform(deliveryPlatform);

        return req;
    }

    @Override
    public void deliveryOrderDispatch(TradePaymentVo tradePaymentVo, BigDecimal deliveryFee, Integer deliveryPlatform, ResponseListener<GatewayTransferResp<DeliveryOrderDispatchResp>> listener) {
        DeliveryOrderDispatchReq req = toDeliveryOrderDispatchReq(tradePaymentVo, deliveryFee, deliveryPlatform);
        mTradeOperates.deliveryOrderDispatch(req, listener);
    }

    private DeliveryOrderDispatchReq toDeliveryOrderDispatchReq(TradePaymentVo tradePaymentVo, BigDecimal deliveryFee, Integer deliveryPlatform) {
        DeliveryOrderDispatchReq req = new DeliveryOrderDispatchReq();
        req.setOrders(toDispatchOrder(req, tradePaymentVo, deliveryFee));
        req.setBrandId(MainApplication.getInstance().getBrandIdenty());
        req.setShopId(MainApplication.getInstance().getShopIdenty());
        req.setDeliveryPlatform(deliveryPlatform);
        return req;
    }

    private List<DeliveryOrderDispatchReq.DispatchOrder> toDispatchOrder(DeliveryOrderDispatchReq req, TradePaymentVo tradePaymentVo, BigDecimal deliveryFee) {
        DeliveryOrderDispatchReq.DispatchOrder order = new DeliveryOrderDispatchReq.DispatchOrder();
        order.setOrderId(tradePaymentVo.getTradeVo().getTrade().getId());
        order.setOrderNo(tradePaymentVo.getTradeVo().getTrade().getTradeNo());
        order.setIsResend(0);
        order.setDeliveryFee(deliveryFee);
        DeliveryPlatform deliveryPlatform = getDeliveryPlatform(tradePaymentVo);
        if (deliveryPlatform != null) {
            order.setLastDeliveryPlatform(deliveryPlatform.value());
        }
        order.setOrderType(1);
        return Utils.asList(order);
    }

    //获取配送平台
    private DeliveryPlatform getDeliveryPlatform(TradePaymentVo tradePaymentVo) {
        DeliveryPlatform deliveryPlatform = null;
        DeliveryOrderVo deliveryOrderVo = getDeliveryOrderVo(tradePaymentVo.getDeliveryOrderVoList());
        if (deliveryOrderVo != null) {
            deliveryPlatform = deliveryOrderVo.getDeliveryOrder().getDeliveryPlatform();
        }

        return deliveryPlatform;
    }

    private DeliveryOrderVo getDeliveryOrderVo(List<DeliveryOrderVo> deliveryOrderVos) {
        if (Utils.isNotEmpty(deliveryOrderVos)) {
            for (DeliveryOrderVo deliveryOrderVo : deliveryOrderVos) {
                DeliveryOrder deliveryOrder = deliveryOrderVo.getDeliveryOrder();
                if (deliveryOrder.getEnableFlag() == YesOrNo.YES) {
                    return deliveryOrderVo;
                }
            }
        }

        return null;
    }

    @Override
    public void deliveryOrderList(TradeVo tradeVo, ResponseListener<GatewayTransferResp<DeliveryOrderListResp>> listener) {
        DeliveryOrderListReq req = toDeliveryOrderListReq(tradeVo);
        mTradeOperates.deliveryOrderList(req, listener);
    }

    private DeliveryOrderListReq toDeliveryOrderListReq(TradeVo tradeVo) {
        DeliveryOrderListReq req = new DeliveryOrderListReq();
        req.setBrandId(MainApplication.getInstance().getBrandIdenty());
        req.setShopId(MainApplication.getInstance().getShopIdenty());
        req.setOrderIds(Utils.asList(tradeVo.getTrade().getId()));
        return req;
    }

    @Override
    public void rebindDeliveryUser(TradeVo tradeVo, User authUser, ResponseListener<BindOrderResp> listener) {
        mTradeOperates.bindDeliveryUser(toBindOrderReq(tradeVo, authUser), listener);
    }

    private BindOrderReq toBindOrderReq(TradeVo tradeVo, User authUser) {
        BindOrderReq req = new BindOrderReq();
        if (authUser != null) {
            req.setUserId(authUser.getId());
            req.setUserName(authUser.getName());
        }
        if (tradeVo != null) {
            req.setTradeInfos(Utils.entity2List(tradeVo));
        }
        return req;
    }

    @Override
    public TradeReturnInfo findTradeReturnInfo(Long tradeId) {
        try {
            return mTradeDal.findTradeReturnInfo(tradeId);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return null;
    }

    @Override
    public void getSenders(final GetSendersListener GetSendersListener) {
        new AsyncTask<Void, Void, List<User>>() {
            @Override
            protected List<User> doInBackground(Void... params) {
                try {
                    return Session.getFunc(UserFunc.class).getUsers(FastFoodApplication.PERMISSION_FASTFOOD_SC);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<User> authUsers) {
                if (authUsers != null && !authUsers.isEmpty()) {
                    GetSendersListener.onSuccess(authUsers);
                } else {
                    GetSendersListener.onFail();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public LoadResult loadData(String tradeUuid) {
        LoadResult loadResult = new LoadResult();

        Long lastClosingTime = SystemSettingsManager.queryLastClosingAccountRecord();
        loadResult.setLastClosingTime(lastClosingTime);

        TradePaymentVo tradePaymentVo = findTradePaymentVo(tradeUuid, false);
        if (tradePaymentVo != null && tradePaymentVo.getTradeVo() != null) {
            //配送信息
            tradePaymentVo.setDeliveryOrderVoList(listDeliveryOrderVo(tradeUuid));
            //发票信息
            TradeInvoice tradeInvoice = getTradeInvoice(tradePaymentVo.getTradeVo().getTrade().getId());
            tradePaymentVo.getTradeVo().setTradeInvoice(tradeInvoice);

            TradeExtra tradeExtra = tradePaymentVo.getTradeVo().getTradeExtra();
            if (tradeExtra != null && tradeExtra.getId() != null) {
                TradeExtraSecrecyPhone tradeExtraSecrecyPhone = findTradeExtraSecrecyPhone(tradeExtra.getId());
                tradePaymentVo.getTradeVo().setTradeExtraSecrecyPhone(tradeExtraSecrecyPhone);
            }
        }

        loadResult.setTradePaymentVo(tradePaymentVo);
        loadResult.setOriTradePaymentVo(tradePaymentVo);
        loadResult.setRepeatTradePaymentVos(listRepeatTradePaymentVo(tradeUuid));
        if (tradePaymentVo != null && tradePaymentVo.getTradeVo() != null) {
            loadResult.setTradeReturnInfo(findTradeReturnInfo(tradePaymentVo.getTradeVo().getTrade().getId()));
            loadResult.setInvoice(findInvoice(tradePaymentVo.getTradeVo().getTrade().getId()));
            //查询原单，主要针对于退货单
            if (tradePaymentVo.getTradeVo().getTrade().getTradeType() == TradeType.REFUND ||
                    tradePaymentVo.getTradeVo().getTrade().getTradeType() == TradeType.REFUND_FOR_REPEAT ||
                    tradePaymentVo.getTradeVo().getTrade().getTradeType() == TradeType.REFUND_FOR_REVERSAL) {
                String oriTradeUuid = tradePaymentVo.getTradeVo().getTrade().getRelateTradeUuid();
                if (!TextUtils.isEmpty(oriTradeUuid)) {
                    TradePaymentVo oriVo = findTradePaymentVo(oriTradeUuid, false);
                    if (oriVo != null && oriVo.getTradeVo() != null) {
                        loadResult.setOriTradePaymentVo(oriVo);
                    }
                }
            }
        }
        loadResult.setHasDaDaSwitchOn(ServerSettingCache.getInstance().getDadaSwitchEnable());
        loadResult.setElectronicInvoiceVo(PaySettingCache.getElectronicInvoiceVo());
        //查询商户开通的外卖配送平台
        List<PartnerShopBiz> allDeliveryPlatformPartnerShopBizs = queryDeliverylatformPartnerShopBiz();
        SparseArray<String> deliveryPlatformPartnerShopBizSparseArray = new SparseArray<String>();
        Map<Integer, PartnerShopBiz> deliveryPlatformPartnerShopBizMap = new HashMap<Integer, PartnerShopBiz>();
        if (Utils.isNotEmpty(allDeliveryPlatformPartnerShopBizs)) {
            for (PartnerShopBiz partnerShopBiz : allDeliveryPlatformPartnerShopBizs) {
                deliveryPlatformPartnerShopBizSparseArray.put(partnerShopBiz.getSource(), partnerShopBiz.getSourceName());
                if (partnerShopBiz.getEnableFlag() == YesOrNo.YES) {
                    deliveryPlatformPartnerShopBizMap.put(partnerShopBiz.getSource(), partnerShopBiz);
                }
            }
        }
        loadResult.setDeliverylatformPartnerShopBizSparseArray(deliveryPlatformPartnerShopBizSparseArray);
        loadResult.setmDeliverylatformPartnerShopBizMap(deliveryPlatformPartnerShopBizMap);
        return loadResult;
    }

    @Override
    public TradeExtraSecrecyPhone findTradeExtraSecrecyPhone(Long tradeExtraId) {
        try {
            return mTradeDal.findTradeExtraSecrecyPhone(tradeExtraId);
        } catch (Exception e) {
            Log.e(TAG, "findTradeExtraSecrecyPhone error " + e.toString());
        }

        return null;
    }
}
