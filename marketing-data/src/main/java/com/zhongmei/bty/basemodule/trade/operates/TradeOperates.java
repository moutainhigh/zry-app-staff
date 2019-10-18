package com.zhongmei.bty.basemodule.trade.operates;

import android.support.v4.app.FragmentActivity;

import com.google.gson.JsonArray;
import com.zhongmei.bty.basemodule.auth.permission.message.AuthLogResp;
import com.zhongmei.bty.basemodule.commonbusiness.listener.SimpleResponseListener;
import com.zhongmei.bty.basemodule.commonbusiness.message.BusinessChargeReq;
import com.zhongmei.bty.basemodule.commonbusiness.message.BusinessChargeResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.bean.ReturnCardDataModel;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.AnonymousCardStoreResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.SalesCardReturnResp;
import com.zhongmei.bty.basemodule.discount.message.CouponUrlResp;
import com.zhongmei.bty.basemodule.discount.message.UsePrivilegeResp;
import com.zhongmei.bty.basemodule.inventory.message.InventoryItemReq;
import com.zhongmei.bty.basemodule.orderdish.bean.AddItemVo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.bty.basemodule.orderdish.message.DishServiceReq;
import com.zhongmei.bty.basemodule.orderdish.message.DishServiceV2Req;
import com.zhongmei.bty.basemodule.orderdish.message.DishServiceV2Resp;
import com.zhongmei.bty.basemodule.orderdish.message.TradeItemResp;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.pay.message.PayResp;
import com.zhongmei.bty.basemodule.pay.message.PaymentResp;
import com.zhongmei.bty.basemodule.pay.message.UsePayReq;
import com.zhongmei.bty.basemodule.pay.message.UsePayResp;
import com.zhongmei.bty.basemodule.pay.message.WechatPayReq;
import com.zhongmei.bty.basemodule.pay.message.WechatPayResp;
import com.zhongmei.bty.basemodule.pay.message.WechatPayUrlReq;
import com.zhongmei.bty.basemodule.pay.message.WechatPayUrlResp;
import com.zhongmei.bty.basemodule.print.entity.PrintOperation;
import com.zhongmei.bty.basemodule.reportcenter.message.GoodsSellRankPrintReq;
import com.zhongmei.bty.basemodule.reportcenter.message.GoodsSellRankPrintResp;
import com.zhongmei.bty.basemodule.reportcenter.message.GoodsSellRankResp;
import com.zhongmei.bty.basemodule.shopmanager.bean.TransferCloseBillData;
import com.zhongmei.bty.basemodule.shopmanager.closing.bean.CloseBillDataInfo;
import com.zhongmei.bty.basemodule.shopmanager.closing.message.CloseBillReq;
import com.zhongmei.bty.basemodule.shopmanager.closing.message.CloseBillResp;
import com.zhongmei.bty.basemodule.shopmanager.closing.message.CloseHistoryReq;
import com.zhongmei.bty.basemodule.shopmanager.closing.message.CloseHistoryResp;
import com.zhongmei.bty.basemodule.shopmanager.closing.message.ClosingHandOverResp;
import com.zhongmei.bty.basemodule.shopmanager.handover.data.ClosingReq;
import com.zhongmei.bty.basemodule.shopmanager.message.CloseDetailReq;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableState;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertable;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TableTradeVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.Invoice;
import com.zhongmei.bty.basemodule.trade.message.AddFeeResp;
import com.zhongmei.bty.basemodule.trade.message.BatchQueryDeliveryFeeReq;
import com.zhongmei.bty.basemodule.trade.message.BindOrderReq;
import com.zhongmei.bty.basemodule.trade.message.BindOrderResp;
import com.zhongmei.bty.basemodule.trade.message.BuffetNoTableTradeResp;
import com.zhongmei.bty.basemodule.trade.message.CancelDeliveryOrderReq;
import com.zhongmei.bty.basemodule.trade.message.CancelDeliveryOrderResp;
import com.zhongmei.bty.basemodule.trade.message.DeletePrePayTradeReq;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderDispatchReq;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderDispatchResp;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderListReq;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderListResp;
import com.zhongmei.bty.basemodule.trade.message.DepositRefundResp;
import com.zhongmei.bty.basemodule.trade.message.DispatchOrderReq;
import com.zhongmei.bty.basemodule.trade.message.EarnestDeductReq;
import com.zhongmei.bty.basemodule.trade.message.ExChangeTableResp;
import com.zhongmei.bty.basemodule.trade.message.FHInvoiceQrcodeReq;
import com.zhongmei.bty.basemodule.trade.message.FHInvoiceQrcodeResp;
import com.zhongmei.bty.basemodule.trade.message.FHQueryBalanceReq;
import com.zhongmei.bty.basemodule.trade.message.FHQueryBalanceResp;
import com.zhongmei.bty.basemodule.trade.message.GetTaxNoReq;
import com.zhongmei.bty.basemodule.trade.message.GetTaxNoResp;
import com.zhongmei.bty.basemodule.trade.message.InvoiceQrcodeReq;
import com.zhongmei.bty.basemodule.trade.message.InvoiceQrcodeResp;
import com.zhongmei.bty.basemodule.trade.message.InvoiceRevokeReq;
import com.zhongmei.bty.basemodule.trade.message.LagReq;
import com.zhongmei.bty.basemodule.trade.message.LagResp;
import com.zhongmei.bty.basemodule.trade.message.ModifyTradeMemoResp;
import com.zhongmei.bty.basemodule.trade.message.PrePayRefundReq;
import com.zhongmei.bty.basemodule.trade.message.PrePayTradeReq;
import com.zhongmei.bty.basemodule.trade.message.QueryDeliveryFeeReq;
import com.zhongmei.bty.basemodule.trade.message.QueryDeliveryFeeResp;
import com.zhongmei.bty.basemodule.trade.message.RefundCheckResp;
import com.zhongmei.bty.basemodule.trade.message.RefundStatusReq;
import com.zhongmei.bty.basemodule.trade.message.RefundStatusResp;
import com.zhongmei.bty.basemodule.trade.message.RefundSubmitResp;
import com.zhongmei.bty.basemodule.trade.message.TakeDishResp;
import com.zhongmei.bty.basemodule.trade.message.TradeFinishResp;
import com.zhongmei.bty.basemodule.trade.message.TradePayStateResp;
import com.zhongmei.bty.basemodule.trade.message.TradePaymentResp;
import com.zhongmei.bty.basemodule.trade.message.TradeRepayReq;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.message.TradeUnbindCouponReq;
import com.zhongmei.bty.basemodule.trade.message.TradeUnionModifyMainWarpReq;
import com.zhongmei.bty.basemodule.trade.message.TradeUnionModifySubWarpReq;
import com.zhongmei.bty.basemodule.trade.message.TransferDinnertableResp;
import com.zhongmei.bty.basemodule.trade.message.UnionTradeItemOperationReq;
import com.zhongmei.bty.basemodule.trade.message.VerifyPayResp;
import com.zhongmei.bty.basemodule.trade.message.WriteoffOnlineResultResp;
import com.zhongmei.bty.basemodule.trade.message.WriteoffReq;
import com.zhongmei.bty.basemodule.trade.message.WriteoffResp;
import com.zhongmei.bty.basemodule.trade.message.uniontable.BuffetUnionTradeCancelReq;
import com.zhongmei.bty.basemodule.trade.message.uniontable.BuffetUnionTradeCancelResp;
import com.zhongmei.bty.basemodule.trade.message.uniontable.BuffetUnionTradeReq;
import com.zhongmei.bty.basemodule.trade.message.uniontable.UnionAndModifyUnionTradeReq;
import com.zhongmei.bty.basemodule.trade.message.uniontable.UnionTradeReq;
import com.zhongmei.bty.basemodule.trade.message.uniontable.UnionTradeSplitReq;
import com.zhongmei.bty.basemodule.trade.message.uniontable.buffet.BuffetMainTradeModifyReq;
import com.zhongmei.bty.basemodule.trade.message.uniontable.buffet.BuffetMergeUnionReq;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.commonmodule.database.entity.AuthorizedLog;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.enums.ActionType;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.PayType;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.TradeReturnInfoReturnStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.http.QSResponseListener;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.data.GatewayTransferResp;
import com.zhongmei.yunfu.resp.data.MindTransferResp;
import com.zhongmei.yunfu.resp.data.TransferReq;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public interface TradeOperates extends IOperates {


    void insert(TradeVo tradeVo, ResponseListener<TradeResp> listener);


    void insertDinner(TradeVo tradeVo, IDinnertable dinnertable, ResponseListener<TradeResp> listener);


    void insertDinner(TradeVo tradeVo, IDinnertable dinnertable, ResponseListener<TradeResp> listener, boolean isAsync);



    void insertBuffet(TradeVo tradeVo, IDinnertable dinnertable, ResponseListener<TradeResp> listener, boolean isAsync);


    void modifyDinner(TradeVo tradeVo, ResponseListener<TradeResp> listener);


    void modifyDinner(TradeVo tradeVo, ResponseListener<TradeResp> listener, boolean isAsync);


    void modifyDinner(TradeVo tradeVo, boolean isUpdate, ResponseListener<TradeResp> listener, boolean isAsync);



    void modifyBuffet(TradeVo tradeVo, boolean isUpdate, ResponseListener<TradeResp> listener, boolean isAsync);

    void modifyBuffet(TradeVo tradeVo, ResponseListener<TradeResp> listener);


    void pay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
             ResponseListener<PaymentResp> listener);

    void pay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
             ResponseListener<PaymentResp> listener, boolean isAsync);


    void adjust(PaymentVo paymentVo, ResponseListener<PaymentResp> listener);


    void insertAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                      ResponseListener<TradePaymentResp> listener);

    void insertAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                      ResponseListener<TradePaymentResp> listener, boolean isAsync);


    void modifyAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                      ResponseListener<PaymentResp> listener);

    void modifyAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                      ResponseListener<PaymentResp> listener, boolean isAsync);


    void salesRetuenPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Reason reason, boolean isReturnInventory,
                        Map<String, String> memberPasswords, ResponseListener<TradePaymentResp> listener);


    void getWechatPayUrl(WechatPayUrlReq req, ResponseListener<WechatPayUrlResp> listener);


    void wechatPay(WechatPayReq req, ResponseListener<WechatPayResp> listener);


    void verifyPay(long tradeId, ResponseListener<VerifyPayResp> listener);


    void accept(TradeVo tradeVo, ResponseListener<TradeResp> listener);


    void receiveBatch(ActionType actionType, List<TradeVo> tradeVos, ResponseListener<TradeResp> listener);


    void refuse(TradeVo tradeVo, Reason reason, ResponseListener<TradeResp> listener);


    void refuseBatch(ActionType actionType, Reason reason, List<TradeVo> tradeVos, ResponseListener<TradeResp> listener);


    void acceptDinner(Trade trade, ResponseListener<TradeResp> listener);

    void acceptDinner(Trade trade, boolean genBatchNo, ResponseListener<TradeResp> listener);


    void refuseDinner(TradeVo tradeVo, Reason reason, ResponseListener<TradeResp> listener);


    void recision(TradeVo tradeVo, Reason reason, List<InventoryItemReq> returnInventoryItems, ResponseListener<TradeResp> listener);


    void refund(TradeVo tradeVo, Reason reason, List<InventoryItemReq> returnInventoryItems, ResponseListener<TradePaymentResp> listener);


    long pending(TradeVo tradeVo) throws Exception;


    void deliveredPayment(List<TradeVo> tradeVoList, ResponseListener<TradePaymentResp> listener);


    void clearAccounts(List<TradeVo> tradeVoList, ResponseListener<TradePaymentResp> listener);


    void getSendCouponUrl(ResponseListener<CouponUrlResp> listener);


    void batchunbindCoupon(Long tradeId, List<TradePrivilege> tradePrivileges, ResponseListener<TradeResp> listener);


    void unbindCoupon(TradeUnbindCouponReq req, ResponseListener<TradeResp> listener);


    void unbindIntegral(TradeUnbindCouponReq req, ResponseListener<TradeResp> listener);


    void transferDinnertable(List<TradeItemExtraDinner> tradeItemSeats, IDinnertableTrade orginal, IDinnertable dest,
                             ResponseListener<TransferDinnertableResp> listener);


    void recisionDinner(Long tradeId, Long serverUpdateTime, List<DinnertableState> states, Reason reason, List<InventoryItemReq> inventoryItems,
                        ResponseListener<TradeResp> listener);


    void recisionBeauty(Long tradeId, Long serverUpdateTime, List<DinnertableState> states, Reason reason, List<InventoryItemReq> inventoryItems,
                        ResponseListener<TradeResp> listener);


    void refundDinner(TradeVo tradeVo, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradePaymentResp> listener);


    void refundBeauty(TradeVo tradeVo, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradePaymentResp> listener);


    void mergeDinner(List<TradeItemExtraDinner> tradeItemSeats, IDinnertableTrade orginal, IDinnertableTrade dest, final Long levelId, ResponseListener<TradeResp> listener);




    void weixinRefund(TradeVo tradeVo, Reason reason, ResponseListener<TradePaymentResp> listener);


    void tradeSplitDinner(TradeVo source, TradeVo target, ResponseListener<TradeResp> listener);


    void tradeSplitPayDinner(TradeVo source, TradeVo target, List<PaymentVo> paymentVoList,
                             Map<String, String> memberPasswords, ResponseListener<PaymentResp> listener);


    void tradeRepay(TradeVo tradeVo, Reason Reason, ResponseListener<PaymentResp> listener);



    void beautyTradeRepay(TradeRepayReq tradeRepayReq, ResponseListener<PaymentResp> listener);


    void doCloseBill(CloseBillReq closeBillReq, ResponseListener<CloseBillResp> listener);


    @Deprecated
    void queryHandOverHistory(ResponseListener<ClosingHandOverResp> listener);


    void queryHandOverHistory(ClosingReq req, ResponseListener<ClosingHandOverResp> listener);



    void closeDetail(CloseDetailReq closeDetailReq, ResponseListener<CloseBillDataInfo> listener);


    void closeDetail(TransferReq transferReq, ResponseListener<TransferCloseBillData> listener);


    void queryCloseHistoryList(CloseHistoryReq closeHistoryReq, ResponseListener<CloseHistoryResp> listener);

    void modifyServiceDish(DishServiceReq dishServiceReq, ResponseListener<TradeItemResp> listener);


    void modifyServiceDishV2(DishServiceV2Req dishServiceReq, CalmResponseListener<ResponseObject<DishServiceV2Resp>> listener);


    void queryPaymentItem(String uuid, ResponseListener<PaymentItem> listener);


    void changeRefundStatus(RefundStatusReq req, ResponseListener<RefundStatusResp> listener);




    void sellCardsInsert(TradeVo tradeVo, ResponseListener<TradeResp> listener);


    void sellCardsPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                      ResponseListener<PaymentResp> listener);


    void sellCardsInsertAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                               ResponseListener<TradePaymentResp> listener);


    void insertAnonymousCards(TradeVo tradeVo, ResponseListener<TradeResp> listener);


    void sellAnonymousCardsPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords, ResponseListener<PaymentResp> listener);


    void sellAnonymousCardsinsertAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords, ResponseListener<TradePaymentResp> listener);


    void saleAndStoreAnonymousCard(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords, ResponseListener<TradePaymentResp> listener);


    void storeAnonymousCard(TradeVo tradeVo, int online, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords, ResponseListener<AnonymousCardStoreResp> listener);


    void storeEntityCard(TradeVo tradeVo, int online, long customerId, BigDecimal chargeMoney, BigDecimal sendMoney, List<PaymentVo> paymentVoList, ResponseListener<AnonymousCardStoreResp> listener);


    void storeVirtualCard(TradeVo tradeVo, int online, long customerId, BigDecimal chargeMoney, BigDecimal sendMoney, List<PaymentVo> paymentVoList, ResponseListener<AnonymousCardStoreResp> listener);


    void returnConfirm(Long tradeId, TradeReturnInfoReturnStatus returnStatus, boolean isReturnInventory, String requestUuid, Reason reason, ResponseListener<Object> listener);

    void returnSellCards(Trade srcTrade, TradeVo tradeVo, List<PaymentVo> paymentVoList, List<ReturnCardDataModel.CardNumber> cardList, Reason reason, boolean returnAll, ResponseListener<SalesCardReturnResp> listener);


    void remindDish(Trade trade, List<Long> tradeItemIds, ResponseListener<TradeItemResp> listener);


    void riseDish(Trade trade, List<Long> tradeItemIds, ResponseListener<TradeItemResp> listener);


    void operationDish(Long tradeId, PrintOperationOpType opType, List<DishDataItem> selectedItems, ResponseListener<TradeItemResp> listener);


    void refreshState(Long traudeId, Long paymentItemId, ResponseListener<TradePayStateResp> listener);



    void refreshReturnState(TradeVo tradeVo, ResponseListener<TradePayStateResp> listener);


    void dinnerSetTableAndAccept(TradeVo tradeVo, Tables table, Bool genBatchNo, ResponseListener<TradeResp> listener);


    void modifyPrintStatus(List<TradeItem> tradeItems, List<PrintOperation> printOperations,
                           List<TradeItemOperation> tradeItemOperations, ResponseListener<TradeItemResp> listener);


    void modifyMainTradePrintStatus(Long mainTradeId, List<TradeItem> tradeItems, List<PrintOperation> printOperations,
                                    List<TradeItemOperation> tradeItemOperations, ResponseListener<TradeItemResp> listener);


    void modifySubTradePrintStatus(Long mainTradeId, Long subTradeId, List<TradeItem> tradeItems, List<PrintOperation> printOperations,
                                   List<TradeItemOperation> tradeItemOperations, ResponseListener<TradeItemResp> listener);


    void tradeFinish(Long tradeId, ResponseListener<TradeFinishResp> listener);



    void depositRefund(Long tradeId, BigDecimal depositRefund, Long payModeId, Reason reason, ResponseListener<DepositRefundResp> listener);



    void buffetDepositRefund(Long tradeId, BigDecimal depositRefund, Long paymentItemId, Reason reason, ResponseListener<PayResp> listener);



    void buffetFinishAndDepositRefund(Long tradeId, BigDecimal depositRefund, Long paymentItemId, Reason reason, ResponseListener<PayResp> listener);


    void moveDish(TradeVo sourceTradeVo, TradeVo targetTradeVo, List<TradeItem> tradeItems, ResponseListener<TradeResp> listener, Integer actionType, Integer moveAdd);



    void doLag(TradeVo tradeVo, LagReq lagReq, ResponseListener<LagResp> listener);


    void getGoodsSaleRank(GoodsSellRankPrintReq req, ResponseListener<GoodsSellRankResp> listener);


    void getGoodsSaleRankPrint(TransferReq<GoodsSellRankPrintReq> req, ResponseListener<MindTransferResp<GoodsSellRankPrintResp>> listener);



    void getBusinessCharge(TransferReq<BusinessChargeReq> req, ResponseListener<MindTransferResp<BusinessChargeResp>> listener);


    void batchUploadAuthLog(List<AuthorizedLog> authorizedLogList, ResponseListener<AuthLogResp> listener);



    void sendOrder(DispatchOrderReq req, ResponseListener<TradeResp> listener);



    void cancelDeliveryOrder(CancelDeliveryOrderReq req, ResponseListener<GatewayTransferResp<CancelDeliveryOrderResp>> listener);


    void acceptAddItem(TradeVo tradeVo, AddItemVo addItemVo, boolean hasAllPrintedKitchen, ResponseListener<TradeResp> listener);


    void refuseAddItem(AddItemVo addItemVo, ResponseListener<TradeResp> listener);


    void exChangeTable(TableTradeVo oldTableTradeVo, Tables newTable, ResponseListener<ExChangeTableResp> listener);


    void invoiceQrcode(InvoiceQrcodeReq req, ResponseListener<GatewayTransferResp<InvoiceQrcodeResp>> listener);


    void invoiceFHQrcode(FHInvoiceQrcodeReq req, ResponseListener<GatewayTransferResp<FHInvoiceQrcodeResp>> listener);


    void queryBalanceFH(FHQueryBalanceReq req, ResponseListener<GatewayTransferResp<FHQueryBalanceResp>> listener);


    void invoiceRevoke(InvoiceRevokeReq req, ResponseListener<GatewayTransferResp<Invoice>> listener);


    void queryDeliveryFee(QueryDeliveryFeeReq req, ResponseListener<GatewayTransferResp<QueryDeliveryFeeResp>> listener);


    void batchQueryDeliveryFee(BatchQueryDeliveryFeeReq req, ResponseListener<GatewayTransferResp<JsonArray>> listener);


    void deliveryOrderDispatch(DeliveryOrderDispatchReq req, ResponseListener<GatewayTransferResp<DeliveryOrderDispatchResp>> listener);


    void deliveryOrderList(DeliveryOrderListReq req, ResponseListener<GatewayTransferResp<DeliveryOrderListResp>> listener);


    void refundCheck(Long tradeId, Long paymentItemId, SimpleResponseListener<RefundCheckResp> listener);

    void refundPayment(Long tradeId, Long paymentItemId, SimpleResponseListener<PayResp> listener);


    void refundSubmit(Long tradeId, Long paymentItemId, BigDecimal refundFee, SimpleResponseListener<RefundSubmitResp> listener);

    void takeDish(TradeExtra tradeExtra, FragmentActivity activity, CalmResponseListener<ResponseObject<TakeDishResp>> listener);


    void insertBuffetNoTable(TradeVo tradeVo, AuthUser waiter, ResponseListener<BuffetNoTableTradeResp> listener);



    void usePrivilege(Trade trade, BusinessType businessType, Long customerId, String entityCardNo, Collection<TradePrivilege> tradePrivileges, QSResponseListener<UsePrivilegeResp> listener);


    void requestAddFee(Long deliveryOrderId, Integer deliveryPlatform, Double amount, ResponseListener<GatewayTransferResp<AddFeeResp>> listener);


    void pay4QS(UsePayReq req, QSResponseListener<UsePayResp> listener);


    void bindDeliveryUser(BindOrderReq req, ResponseListener<BindOrderResp> listener);


    void creatUnionTrade(UnionTradeReq req, ResponseListener<TradeResp> listener);


    void splitUnionTrade(UnionTradeSplitReq req, ResponseListener<TradeResp> listener);


    void modifyUnionMainTrade(TradeVo tradeVo, TradeUnionModifyMainWarpReq req, ResponseListener<TradeResp> listener, boolean isAsync);


    void modifyUnionSubTrade(TradeVo tradeVo, TradeUnionModifySubWarpReq req, ResponseListener<TradeResp> listener, boolean isAsync);


    void unionAndModifyUnionTrade(UnionAndModifyUnionTradeReq req, ResponseListener<TradeResp> listener);


    void modifyServiceUnionDish(DishServiceV2Req dishServiceReq, CalmResponseListener<ResponseObject<DishServiceV2Resp>> listener, TradeType type);


    void unionOperationDish(UnionTradeItemOperationReq req, ResponseListener<TradeItemResp> listener);


    void modifyTradeMemo(Trade trade, String newMemo, ResponseListener<ModifyTradeMemoResp> listener);

    void buffetUnionTableCreate(BuffetUnionTradeReq req, ResponseListener<TradeResp> listener);



    void buffetModifyUnionSubTrade(TradeVo tradeVo, TradeUnionModifySubWarpReq req, ResponseListener<TradeResp> listener, boolean isAsync);


    void buffetSplitTrade(BuffetUnionTradeCancelReq req, ResponseListener<BuffetUnionTradeCancelResp> listener);


    void buffetUnionMainTradeModify(BuffetMainTradeModifyReq req, ResponseListener<TradeResp> listener);


    void buffetCreateMenu(TradeVo tradeVo, ResponseListener<TradeResp> listener);


    void buffetUnionFinish(BuffetMergeUnionReq req, ResponseListener<TradeResp> listener);


    void memberWriteoff(WriteoffReq req, ResponseListener<WriteoffResp> listener);

    void queryInvoiceNo(GetTaxNoReq req, ResponseListener<GetTaxNoResp> listener);


    void createPrePayTrade(PrePayTradeReq req, ResponseListener<TradeResp> listener);



    void deletePrePayTrade(DeletePrePayTradeReq req, ResponseListener<TradeResp> listener);

    void getWriteOffOnlinePayResult(String tradeNo, PayType payType, ResponseListener<WriteoffOnlineResultResp> listener);

    void useEarnestDeduct(EarnestDeductReq req, ResponseListener<PayResp> listener);


    void bookingRrePayRefund(PrePayRefundReq req, ResponseListener<PayResp> listener);


    void recisionUnionTrade(Long tradeId, Long serverUpdateTime, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradeResp> listener);
}
