package com.zhongmei.bty.cashier.ordercenter.manager;

import android.support.v4.app.FragmentActivity;

import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerShopBiz;
import com.zhongmei.yunfu.resp.data.GatewayTransferResp;
import com.zhongmei.bty.basemodule.inventory.message.InventoryItemReq;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.pay.message.PaymentResp;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.trade.bean.DeliveryOrderVo;
import com.zhongmei.bty.basemodule.trade.bean.DinnerTableInfo;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableState;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.Invoice;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.basemodule.trade.entity.TradeExtraSecrecyPhone;
import com.zhongmei.bty.basemodule.trade.entity.TradeInvoice;
import com.zhongmei.yunfu.db.entity.trade.TradeReturnInfo;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.basemodule.trade.message.BindOrderResp;
import com.zhongmei.bty.basemodule.trade.message.CancelDeliveryOrderResp;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderDispatchResp;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderListResp;
import com.zhongmei.bty.basemodule.trade.message.DepositRefundResp;
import com.zhongmei.bty.basemodule.trade.message.InvoiceQrcodeReq;
import com.zhongmei.bty.basemodule.trade.message.InvoiceQrcodeResp;
import com.zhongmei.bty.basemodule.trade.message.QueryDeliveryFeeResp;
import com.zhongmei.bty.basemodule.trade.message.TakeDishResp;
import com.zhongmei.bty.basemodule.trade.message.TradePayStateResp;
import com.zhongmei.bty.basemodule.trade.message.TradePaymentResp;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.cashier.ordercenter.bean.LoadResult;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.TradeReturnInfoReturnStatus;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;

import java.math.BigDecimal;
import java.util.List;



public interface IOrderCenterDetailManager {

    void acceptOrder(TradeVo tradeVo, ResponseListener<TradeResp> listener);


    void refuseOrder(TradeVo tradeVo, Reason reason, ResponseListener<TradeResp> listener);


    void returnConfirm(TradeVo tradeVo, TradeReturnInfoReturnStatus returnStatus, boolean isReturnInventory, String requestUuid, Reason reason, ResponseListener<Object> listener);


    void recisionOrder(TradeVo tradeVo, Reason reason, List<InventoryItemReq> returnInventoryItems, ResponseListener<TradeResp> listener);


    void refundOrder(TradeVo tradeVo, Reason reason, boolean isReturnInventory, List<InventoryItemReq> returnInventoryItems, ResponseListener<TradePaymentResp> listener);


    void depositRefund(Long tradeId, BigDecimal depositRefund, Reason reason, ResponseListener<DepositRefundResp> listener);


    void repayOrder(TradeVo tradeVo, Reason reason, ResponseListener<PaymentResp> listener);



    void retryRefund(TradeVo relateTradeVo, TradeVo tradeVo, ResponseListener<TradePaymentResp> listener);



    void cancelDeliveryOrder(TradePaymentVo tradePaymentVo, ResponseListener<GatewayTransferResp<CancelDeliveryOrderResp>> listener);


    void invoiceQrcode(InvoiceQrcodeReq req, ResponseListener<GatewayTransferResp<InvoiceQrcodeResp>> listener);


    void invoiceRevoke(Invoice invoice, ResponseListener<GatewayTransferResp<Invoice>> listener);


    void queryDeliveryFee(Long tradeId, String tradeNo, String thirdTranNo, Integer deliveryPlatform, ResponseListener<GatewayTransferResp<QueryDeliveryFeeResp>> listener);


    void deliveryOrderDispatch(TradePaymentVo tradePaymentVo, BigDecimal deliveryFee, Integer deliveryPlatform, ResponseListener<GatewayTransferResp<DeliveryOrderDispatchResp>> listener);


    void deliveryOrderList(TradeVo tradeVo, ResponseListener<GatewayTransferResp<DeliveryOrderListResp>> listener);


    void rebindDeliveryUser(TradeVo tradeVo, User authUser, ResponseListener<BindOrderResp> listener);


    void refreshState(TradeVo tradeVo, PaymentItem paymentItem, ResponseListener<TradePayStateResp> listener);


    void acceptDinner(Trade trade, ResponseListener<TradeResp> listener);


    void acceptDinner(Trade trade, boolean genBatchNo, ResponseListener<TradeResp> listener);


    void dinnerSetTableAndAccept(TradeVo tradeVo, Tables table, ResponseListener<TradeResp> listener);


    void dinnerSetTableAndAccept(TradeVo tradeVo, Tables table, boolean isSendKitchen, ResponseListener<TradeResp> listener);


    void refundDinner(TradeVo tradeVo, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradePaymentResp> listener);


    void recisionDinner(Long tradeId, Long serverUpdateTime, List<DinnertableState> states, Reason reason, List<InventoryItemReq> inventoryItems,
                        ResponseListener<TradeResp> listener);


    void refuseDinner(TradeVo tradeVo, Reason reason, ResponseListener<TradeResp> listener);


    void takeDish(TradeExtra tradeExtra, FragmentActivity activity, CalmResponseListener<ResponseObject<TakeDishResp>> listener);


    LoadResult loadData(String tradeUuid);


    TradePaymentVo findTradePaymentVo(String tradeUuid);


    List<TradePaymentVo> listRepeatTradePaymentVo(String tradeUuid);


    TradeReturnInfo findTradeReturnInfo(Long tradeId);


    Invoice findInvoice(Long tradeId);


    Invoice findInvoice(String uuid);


    List<PartnerShopBiz> queryDeliverylatformPartnerShopBiz();

    List<TradeItemVo> getValidTradeItemList(List<TradeItemVo> tradeItemList);


    List<Tables> getTablesByTradeTable(List<TradeTable> tradeTableList);


    TradeVo findTrade(String tradeUuid, boolean onlyValid);


    TradeVo findTrade(String tradeUuid);


    TradePaymentVo findTradePaymentVo(String tradeUuid, boolean onlyValid);


    List<Trade> listSplitTrades(String sourceTradeUuid) throws Exception;


    List<DeliveryOrderVo> listDeliveryOrderVo(String tradeUuid);


    TradeInvoice getTradeInvoice(Long tradeId);


    DinnerTableInfo getDinnerTable(TradeVo tradeVo) throws Exception;


    TradeExtraSecrecyPhone findTradeExtraSecrecyPhone(Long tradeExtraId);


    void getSenders(GetSendersListener GetSendersListener);


    void recisionBeauty(Long tradeId, Long serverUpdateTime, List<DinnertableState> states, Reason reason, List<InventoryItemReq> inventoryItems,
                        ResponseListener<TradeResp> listener);


    void refundBeauty(TradeVo tradeVo, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradePaymentResp> listener);



    void repayBeautyOrder(TradeVo tradeVo, Reason reason, ResponseListener<PaymentResp> listener);

    interface GetSendersListener {
        void onSuccess(List<User> authUsers);

        void onFail();
    }
}
