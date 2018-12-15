package com.zhongmei.bty.cashier.ordercenter.manager;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerShopBiz;
import com.zhongmei.bty.basemodule.commonbusiness.manager.SystemSettingsManager;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRel;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRelExtra;
import com.zhongmei.bty.basemodule.trade.message.TradeRepayReq;
import com.zhongmei.yunfu.resp.data.GatewayTransferResp;
import com.zhongmei.bty.basemodule.commonbusiness.operates.SystemSettingDal;
import com.zhongmei.bty.basemodule.inventory.message.InventoryItemReq;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.pay.message.PaymentResp;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.trade.bean.DeliveryOrderVo;
import com.zhongmei.bty.basemodule.trade.bean.DinnerTableInfo;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableState;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeUnionType;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.Invoice;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.basemodule.trade.entity.TradeExtraSecrecyPhone;
import com.zhongmei.bty.basemodule.trade.entity.TradeInvoice;
import com.zhongmei.bty.basemodule.trade.entity.TradeMainSubRelation;
import com.zhongmei.yunfu.db.entity.trade.TradeReturnInfo;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.basemodule.trade.message.BindOrderResp;
import com.zhongmei.bty.basemodule.trade.message.CancelDeliveryOrderResp;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderDispatchResp;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderListResp;
import com.zhongmei.bty.basemodule.trade.message.DepositRefundResp;
import com.zhongmei.bty.basemodule.trade.message.InvoiceQrcodeReq;
import com.zhongmei.bty.basemodule.trade.message.InvoiceQrcodeResp;
import com.zhongmei.bty.basemodule.trade.message.InvoiceRevokeReq;
import com.zhongmei.bty.basemodule.trade.message.QueryDeliveryFeeResp;
import com.zhongmei.bty.basemodule.trade.message.TakeDishResp;
import com.zhongmei.bty.basemodule.trade.message.TradePayStateResp;
import com.zhongmei.bty.basemodule.trade.message.TradePaymentResp;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.operates.TablesDal;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.cashier.ordercenter.bean.LoadResult;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradeReturnInfoReturnStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 订单中心订单详情Model，业务处理类主要负责数据库操作、网络操作
 */

public class OrderCenterDetailManager implements IOrderCenterDetailManager {
    private static final String TAG = OrderCenterDetailManager.class.getSimpleName();
    protected TradeOperates mTradeOperates;
    protected TradeDal mTradeDal;
    protected TablesDal dal;

    public OrderCenterDetailManager() {
        mTradeOperates = OperatesFactory.create(TradeOperates.class);
        mTradeDal = OperatesFactory.create(TradeDal.class);
        dal = OperatesFactory.create(TablesDal.class);
    }

    @Override
    public void refuseDinner(TradeVo tradeVo, Reason reason, ResponseListener<TradeResp> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void recisionDinner(Long tradeId, Long serverUpdateTime, List<DinnertableState> states, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradeResp> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refundDinner(TradeVo tradeVo, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradePaymentResp> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DinnerTableInfo getDinnerTable(TradeVo tradeVo) throws Exception {
        return null;
    }

    @Override
    public TradeExtraSecrecyPhone findTradeExtraSecrecyPhone(Long tradeExtraId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refreshState(TradeVo tradeVo, PaymentItem paymentItem, ResponseListener<TradePayStateResp> listener) {
        mTradeOperates.refreshState(tradeVo.getTrade().getId(), paymentItem.getId(), listener);
    }

    @Override
    public void acceptDinner(Trade trade, ResponseListener<TradeResp> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void acceptDinner(Trade trade, boolean genBatchNo, ResponseListener<TradeResp> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void dinnerSetTableAndAccept(TradeVo tradeVo, Tables table, ResponseListener<TradeResp> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void dinnerSetTableAndAccept(TradeVo tradeVo, Tables table, boolean isSendKitchen, ResponseListener<TradeResp> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void acceptOrder(TradeVo tradeVo, ResponseListener<TradeResp> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refuseOrder(TradeVo tradeVo, Reason reason, ResponseListener<TradeResp> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void returnConfirm(TradeVo tradeVo, TradeReturnInfoReturnStatus returnStatus, boolean isReturnInventory, String requestUuid, Reason reason, ResponseListener<Object> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void recisionOrder(TradeVo tradeVo, Reason reason, List<InventoryItemReq> returnInventoryItems, ResponseListener<TradeResp> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refundOrder(TradeVo tradeVo, Reason reason, boolean isReturnInventory, List<InventoryItemReq> returnInventoryItems, ResponseListener<TradePaymentResp> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void depositRefund(Long tradeId, BigDecimal depositRefund, Reason reason, ResponseListener<DepositRefundResp> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void repayOrder(TradeVo tradeVo, Reason reason, ResponseListener<PaymentResp> listener) {
        mTradeOperates.tradeRepay(tradeVo, reason, listener);
    }

    protected TradeRepayReq toTradeRepayReq(TradeVo tradeVo, Reason reason) {
        TradeRepayReq tradeRepayReq = new TradeRepayReq();

        Trade trade = tradeVo.getTrade();
        if (trade != null) {
            tradeRepayReq.setTradeId(trade.getId());
            tradeRepayReq.setTradeUuid(trade.getUuid());
            tradeRepayReq.setServerUpdateTime(trade.getServerUpdateTime());
        }

        AuthUser user = Session.getAuthUser();
        if (user != null) {
            tradeRepayReq.setUpdatorId(user.getId());
            tradeRepayReq.setUpdatorName(user.getName());
        }

        if (reason != null) {
            tradeRepayReq.setReasonId(reason.getId());
            tradeRepayReq.setReasonContent(reason.getContent());
            tradeRepayReq.setCancelCode(reason.getContentCode());
        }

        return tradeRepayReq;
    }

    @Override
    public void retryRefund(TradeVo relateTradeVo, TradeVo tradeVo, ResponseListener<TradePaymentResp> listener) {
        TradeStatus tradeStatus = tradeVo.getTrade().getTradeStatus();
        if (tradeStatus == TradeStatus.RETURNED) {//当前订单状态是已退货，说明是退货或反结账产生的退货单，使用退货接口进行退款
            //退货接口退款，使用原单
            mTradeOperates.refund(relateTradeVo, new Reason(), null, listener);
        } else {
            //微信退款接口进行退款，使用退货单
            mTradeOperates.weixinRefund(tradeVo, new Reason(), listener);
        }
    }


    @Override
    public void cancelDeliveryOrder(TradePaymentVo tradePaymentVo, ResponseListener<GatewayTransferResp<CancelDeliveryOrderResp>> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void invoiceQrcode(InvoiceQrcodeReq req, ResponseListener<GatewayTransferResp<InvoiceQrcodeResp>> listener) {
        mTradeOperates.invoiceQrcode(req, listener);
    }

    @Override
    public void invoiceRevoke(Invoice invoice, ResponseListener<GatewayTransferResp<Invoice>> listener) {
        mTradeOperates.invoiceRevoke(toInvoiceRevokeReq(invoice), listener);
    }

    private InvoiceRevokeReq toInvoiceRevokeReq(Invoice invoice) {
        InvoiceRevokeReq req = new InvoiceRevokeReq();
        req.setShopIdenty(MainApplication.getInstance().getShopIdenty());
        req.setUuid(invoice.getUuid());
        req.setOrderId(invoice.getOrderId());
        req.setType(invoice.getType());
        req.setSource(invoice.getSource());
        req.setReason("xxx");
        AuthUser authUser = Session.getAuthUser();
        if (authUser != null) {
            req.setDrawerNo(String.valueOf(authUser.getId()));
            req.setDrawer(authUser.getName());
        }
        return req;
    }

    @Override
    public void queryDeliveryFee(Long tradeId, String tradeNo, String thirdTranNo, Integer deliveryPlatform, ResponseListener<GatewayTransferResp<QueryDeliveryFeeResp>> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deliveryOrderDispatch(TradePaymentVo tradePaymentVo, BigDecimal deliveryFee, Integer deliveryPlatform, ResponseListener<GatewayTransferResp<DeliveryOrderDispatchResp>> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deliveryOrderList(TradeVo tradeVo, ResponseListener<GatewayTransferResp<DeliveryOrderListResp>> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void rebindDeliveryUser(TradeVo tradeVo, User authUser, ResponseListener<BindOrderResp> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TradePaymentVo findTradePaymentVo(String tradeUuid) {
        try {
            return mTradeDal.findTradPayment(tradeUuid);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return null;
    }

    @Override
    public List<TradePaymentVo> listRepeatTradePaymentVo(String tradeUuid) {
        LinkedList<TradePaymentVo> list = new LinkedList<TradePaymentVo>();

        TradePaymentVo tradePaymentVo = findTradePaymentVo(tradeUuid);
        while (tradePaymentVo != null && tradePaymentVo.getTradeVo() != null
                && tradePaymentVo.getTradeVo().getTrade() != null
                && tradePaymentVo.getTradeVo().getTrade().getTradeType() == TradeType.SELL_FOR_REPEAT
                && !TextUtils.isEmpty(tradePaymentVo.getTradeVo().getTrade().getRelateTradeUuid())) {

            tradePaymentVo =
                    findTradePaymentVo(tradePaymentVo.getTradeVo().getTrade().getRelateTradeUuid());
            if (tradePaymentVo != null && tradePaymentVo.getTradeVo() != null) {
                list.addFirst(tradePaymentVo);
            }
        }

        return list;
    }

    @Override
    public TradeReturnInfo findTradeReturnInfo(Long tradeId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Invoice findInvoice(Long tradeId) {
        try {
            return mTradeDal.findInvoice(tradeId);
        } catch (Exception e) {
            Log.e(TAG, "" + e.toString());
        }

        return null;
    }

    @Override
    public Invoice findInvoice(String uuid) {
        try {
            return mTradeDal.findInvoice(uuid);
        } catch (Exception e) {
            Log.e(TAG, "" + e.toString());
        }

        return null;
    }

    @Override
    public List<PartnerShopBiz> queryDeliverylatformPartnerShopBiz() {
        try {
            SystemSettingDal systemSettingDal = OperatesFactory.create(SystemSettingDal.class);
            return systemSettingDal.queryPartnerShopBiz(3, false);//业务类型（1-外卖，2-点餐，3-配送，4-发票
        } catch (Exception e) {
            Log.e(TAG, "" + e.toString());
        }

        return null;
    }

    @Override
    public List<TradeItemVo> getValidTradeItemList(List<TradeItemVo> tradeItemList) {
        if (Utils.isNotEmpty(tradeItemList)) {
            List<TradeItemVo> validTradeItemList = new ArrayList<TradeItemVo>();
            for (TradeItemVo tradeItemVo : tradeItemList) {
                TradeItem tradeItem = tradeItemVo.getTradeItem();
                if (tradeItem.getStatusFlag() == StatusFlag.VALID
                        && tradeItem.getQuantity().compareTo(BigDecimal.ZERO) != 0) {
                    validTradeItemList.add(tradeItemVo);
                }
            }
            return validTradeItemList;
        }
        return Collections.emptyList();
    }

    @Override
    public List<Tables> getTablesByTradeTable(List<TradeTable> tradeTableList) {
        List<Tables> tablesList = new ArrayList<Tables>();
        if (tradeTableList != null) {
            TablesDal tablesDal = OperatesFactory.create(TablesDal.class);
            for (TradeTable tradeTable : tradeTableList) {
                try {
                    Tables tables = tablesDal.findTablesById(tradeTable.getTableId());
                    tablesList.add(tables);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
        return tablesList;
    }

    @Override
    public TradeVo findTrade(String tradeUuid, boolean onlyValid) {
        TradeVo tradeVo = null;
        try {
            tradeVo = mTradeDal.findTrade(tradeUuid, onlyValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tradeVo;
    }

    @Override
    public TradeVo findTrade(String tradeUuid) {
        TradeVo tradeVo = null;
        try {
            tradeVo = mTradeDal.findTrade(tradeUuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tradeVo;
    }

    @Override
    public TradePaymentVo findTradePaymentVo(String tradeUuid, boolean onlyValid) {
        try {
            return mTradeDal.findTradPayment(tradeUuid, onlyValid);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }

        return null;
    }

    @Override
    public List<Trade> listSplitTrades(String sourceTradeUuid) throws Exception {
        return mTradeDal.listSplitTrades(sourceTradeUuid);
    }

    @Override
    public List<DeliveryOrderVo> listDeliveryOrderVo(String tradeUuid) {
        try {
            return mTradeDal.listDeliveryOrderVo(tradeUuid);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return Collections.emptyList();
    }

    @Override
    public TradeInvoice getTradeInvoice(Long tradeId) {
        try {
            return mTradeDal.getTradeInvoiceById(tradeId);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    @Override
    public void takeDish(TradeExtra tradeExtra, FragmentActivity activity, CalmResponseListener<ResponseObject<TakeDishResp>> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getSenders(GetSendersListener GetSendersListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void recisionBeauty(Long tradeId, Long serverUpdateTime, List<DinnertableState> states, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradeResp> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refundBeauty(TradeVo tradeVo, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradePaymentResp> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void repayBeautyOrder(TradeVo tradeVo, Reason reason, ResponseListener<PaymentResp> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LoadResult loadData(String tradeUuid) {
        LoadResult loadResult = new LoadResult();

        Long lastClosingTime = SystemSettingsManager.queryLastClosingAccountRecord();
        loadResult.setLastClosingTime(lastClosingTime);

        TradePaymentVo tradePaymentVo = findTradePaymentVo(tradeUuid, false);
        if (tradePaymentVo != null && tradePaymentVo.getTradeVo() != null) {
            Trade trade = tradePaymentVo.getTradeVo().getTrade();
            TradeUnionType tradeUnionType = DinnerOrderCenterListManager.getTradeUnionType(trade);
            tradePaymentVo.setTradeUnionType(tradeUnionType);
            if (tradeUnionType == TradeUnionType.UNION_SUB) {
                setUnionMainTradeItemToSubTradeItem(tradePaymentVo, trade);
            }

            //配送信息
            tradePaymentVo.setDeliveryOrderVoList(listDeliveryOrderVo(tradeUuid));
            //发票信息
            TradeInvoice tradeInvoice = getTradeInvoice(tradePaymentVo.getTradeVo().getTrade().getId());
            tradePaymentVo.getTradeVo().setTradeInvoice(tradeInvoice);
        }

        loadResult.setTradePaymentVo(tradePaymentVo);
        loadResult.setOriTradePaymentVo(tradePaymentVo);
        loadResult.setmSpliteTradePaymentVos(selectSplitedTrades(tradeUuid));
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

    private void setUnionMainTradeItemToSubTradeItem(TradePaymentVo tradePaymentVo, Trade trade) {
        try {
            TradeMainSubRelation tradeRelation = mTradeDal.getTradeMainSubRelationBySubTrade(trade);
            List<TradeItemMainBatchRel> tradeItemRelList = mTradeDal.getTradeItemMainBatchRelListBySubTradeId(trade.getId());
            if (Utils.isEmpty(tradeItemRelList)) {
                return;
            }

            Map<Long, TradeItemMainBatchRel> tradeItemRelMap = new HashMap<>();
            for (TradeItemMainBatchRel tradeItemMainBatchRel : tradeItemRelList) {
                tradeItemRelMap.put(tradeItemMainBatchRel.getMainItemId(), tradeItemMainBatchRel);
            }

            List<TradeItemMainBatchRelExtra> tradeItemRelExtraList = mTradeDal.getTradeItemMainBatchRelExtraListBySubTradeId(trade.getId());
            Map<Long, TradeItemMainBatchRelExtra> tradeItemRelExtraMap = new HashMap<>();
            for (TradeItemMainBatchRelExtra tradeItemMainBatchRelExtra : tradeItemRelExtraList) {
                tradeItemRelExtraMap.put(tradeItemMainBatchRelExtra.getMainId(), tradeItemMainBatchRelExtra);
            }

            TradeVo mainTradeVo = mTradeDal.findTrade(tradeRelation.getMainTradeId());
            if (mainTradeVo == null || Utils.isEmpty(mainTradeVo.getTradeItemList())) {
                return;
            }

            TradeVo tradeVo = tradePaymentVo.getTradeVo();
            List<TradeItemVo> mainTradeItemVoList = mainTradeVo.getTradeItemList();
            for (int i = mainTradeItemVoList.size() - 1; i >= 0; i--) {
                TradeItemVo mainTradeItemVo = mainTradeItemVoList.get(i);
                TradeItemMainBatchRel tradeItemRel = tradeItemRelMap.get(mainTradeItemVo.getTradeItem().getId());
                if (tradeItemRel == null) {
                    mainTradeItemVoList.remove(i);//移除子单已经不存在的批量菜

                    continue;
                } else {
                    BigDecimal itemCount = tradeItemRel.getTradeItemNum();
                    BigDecimal itemRate = mainTradeItemVo.getTradeItem().getQuantity().divide(itemCount);
                    //菜品
                    mainTradeItemVo.getTradeItem().setQuantity(itemCount);
                    mainTradeItemVo.getTradeItem().setAmount(mainTradeItemVo.getTradeItem().getAmount().divide(itemRate));
                    mainTradeItemVo.getTradeItem().setActualAmount(mainTradeItemVo.getTradeItem().getActualAmount().divide(itemRate));
                    mainTradeItemVo.getTradeItem().setFeedsAmount(mainTradeItemVo.getTradeItem().getFeedsAmount().divide(itemRate));
                    mainTradeItemVo.getTradeItem().setPropertyAmount(mainTradeItemVo.getTradeItem().getPropertyAmount().divide(itemRate));
                    //属性
                    List<TradeItemProperty> mainTradeItemPropertyList = mainTradeItemVo.getTradeItemPropertyList();
                    if (Utils.isNotEmpty(mainTradeItemPropertyList)) {
                        for (TradeItemProperty mainTradeItemProperty : mainTradeItemPropertyList) {
                            TradeItemMainBatchRelExtra tradeItemPropertyRel = tradeItemRelExtraMap.get(mainTradeItemProperty.getId());
                            if (tradeItemPropertyRel != null) {
                                mainTradeItemProperty.setQuantity(mainTradeItemProperty.getQuantity().divide(itemRate));
                                mainTradeItemProperty.setAmount(mainTradeItemProperty.getAmount().divide(itemRate));
                            }
                        }
                    }
                }
            }
            if (tradeVo.getTradeItemList() == null) {
                tradeVo.setTradeItemList(new ArrayList<TradeItemVo>());
            }
            tradeVo.getTradeItemList().addAll(mainTradeItemVoList);

            tradePaymentVo.setTradeVo(tradeVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<TradePaymentVo> selectSplitedTrades(String sourceTradeUuid) {
        LinkedList<TradePaymentVo> list = new LinkedList<TradePaymentVo>();
        try {
            List<Trade> splitedTrades = listSplitTrades(sourceTradeUuid);
            if (splitedTrades != null && !splitedTrades.isEmpty()) {
                TradePaymentVo tradePaymentVo = null;
                for (Trade trade : splitedTrades) {
                    tradePaymentVo = findTradePaymentVo(trade.getUuid(), false);
                    if (tradePaymentVo != null) {
                        list.addFirst(tradePaymentVo);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "selectSplitedTrades", e);
        }
        return list;
    }
}
