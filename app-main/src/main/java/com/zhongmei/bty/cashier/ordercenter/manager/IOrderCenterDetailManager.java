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

/**
 * 订单中心订单详情Model，业务处理类主要负责数据库操作、网络操作
 */

public interface IOrderCenterDetailManager {
    /**
     * 接受订单
     *
     * @param tradeVo  订单数据
     * @param listener 响应回调
     */
    void acceptOrder(TradeVo tradeVo, ResponseListener<TradeResp> listener);

    /**
     * 拒绝订单
     *
     * @param tradeVo  订单数据
     * @param reason   拒绝原因
     * @param listener 响应回调
     */
    void refuseOrder(TradeVo tradeVo, Reason reason, ResponseListener<TradeResp> listener);

    /**
     * 用户提出退单申请后，由商户进行确认操作（同意/拒绝）
     *
     * @param tradeVo      订单数据
     * @param returnStatus 退单状态
     * @param requestUuid  requestUuid
     * @param reason       原因
     * @param listener     响应回调
     */
    void returnConfirm(TradeVo tradeVo, TradeReturnInfoReturnStatus returnStatus, boolean isReturnInventory, String requestUuid, Reason reason, ResponseListener<Object> listener);

    /**
     * 作废订单
     *
     * @param tradeVo              订单数据
     * @param reason               作废原因
     * @param returnInventoryItems
     * @param listener             响应回调
     */
    void recisionOrder(TradeVo tradeVo, Reason reason, List<InventoryItemReq> returnInventoryItems, ResponseListener<TradeResp> listener);

    /**
     * 退货订单
     *
     * @param tradeVo              订单数据
     * @param reason               退货原因
     * @param returnInventoryItems
     * @param listener             响应回调
     */
    void refundOrder(TradeVo tradeVo, Reason reason, boolean isReturnInventory, List<InventoryItemReq> returnInventoryItems, ResponseListener<TradePaymentResp> listener);

    /**
     * 退押金
     *
     * @param tradeId       订单Id
     * @param depositRefund 退还金额
     * @param reason        原因
     * @param listener      响应回调
     */
    void depositRefund(Long tradeId, BigDecimal depositRefund, Reason reason, ResponseListener<DepositRefundResp> listener);

    /**
     * 反结账订单
     *
     * @param tradeVo  订单数据
     * @param reason   原因
     * @param listener 响应回调
     */
    void repayOrder(TradeVo tradeVo, Reason reason, ResponseListener<PaymentResp> listener);


    /**
     * 二次退款
     *
     * @param relateTradeVo 原单订单数据,由于二次退款操作的是退货单，但退款必须取原单,故此处取原单
     * @param tradeVo       当前单（退货单）
     * @param listener      响应回调
     */
    void retryRefund(TradeVo relateTradeVo, TradeVo tradeVo, ResponseListener<TradePaymentResp> listener);


    /**
     * 从配送平台取消订单配送
     *
     * @param tradePaymentVo
     * @param listener
     */
    void cancelDeliveryOrder(TradePaymentVo tradePaymentVo, ResponseListener<GatewayTransferResp<CancelDeliveryOrderResp>> listener);

    /**
     * 电子发票获取开票二维码
     *
     * @param req
     * @param listener
     */
    void invoiceQrcode(InvoiceQrcodeReq req, ResponseListener<GatewayTransferResp<InvoiceQrcodeResp>> listener);

    /**
     * 电子发票冲红
     *
     * @param invoice
     * @param listener
     */
    void invoiceRevoke(Invoice invoice, ResponseListener<GatewayTransferResp<Invoice>> listener);

    /**
     * 查询配送费
     *
     * @param tradeId          订单Id
     * @param tradeNo          订单号
     * @param thirdTranNo      第三方订单号
     * @param deliveryPlatform 目标配送平台
     * @param listener
     */
    void queryDeliveryFee(Long tradeId, String tradeNo, String thirdTranNo, Integer deliveryPlatform, ResponseListener<GatewayTransferResp<QueryDeliveryFeeResp>> listener);

    /**
     * 将订单下发到配送平台
     *
     * @param tradePaymentVo   订单信息
     * @param deliveryFee      配送费
     * @param deliveryPlatform 目标配送平台
     * @param listener
     */
    void deliveryOrderDispatch(TradePaymentVo tradePaymentVo, BigDecimal deliveryFee, Integer deliveryPlatform, ResponseListener<GatewayTransferResp<DeliveryOrderDispatchResp>> listener);

    /**
     * 配送订单查询
     *
     * @param tradeVo  查询配送订单的订单
     * @param listener
     */
    void deliveryOrderList(TradeVo tradeVo, ResponseListener<GatewayTransferResp<DeliveryOrderListResp>> listener);

    /**
     * 重新绑定配送员
     *
     * @param tradeVo  需重新指派配送员的订单
     * @param authUser 新的配送员
     * @param listener listener
     */
    void rebindDeliveryUser(TradeVo tradeVo, User authUser, ResponseListener<BindOrderResp> listener);

    /**
     * 更新支付状态
     *
     * @param tradeVo  订单数据
     * @param listener 响应回调
     */
    void refreshState(TradeVo tradeVo, PaymentItem paymentItem, ResponseListener<TradePayStateResp> listener);

    /**
     * 接受正餐订单
     *
     * @param trade
     * @param listener
     */
    void acceptDinner(Trade trade, ResponseListener<TradeResp> listener);

    /**
     * 接受正餐订单
     *
     * @param trade
     * @param genBatchNo
     * @param listener
     */
    void acceptDinner(Trade trade, boolean genBatchNo, ResponseListener<TradeResp> listener);

    /**
     * 正餐设置桌台并接受
     *
     * @param tradeVo
     * @param table
     * @param listener
     */
    void dinnerSetTableAndAccept(TradeVo tradeVo, Tables table, ResponseListener<TradeResp> listener);

    /**
     * 正餐设置桌台并接受
     *
     * @param tradeVo
     * @param table
     * @param isSendKitchen
     * @param listener
     */
    void dinnerSetTableAndAccept(TradeVo tradeVo, Tables table, boolean isSendKitchen, ResponseListener<TradeResp> listener);

    /**
     * 正餐退货接口
     *
     * @param tradeVo
     * @param reason
     * @param listener
     */
    void refundDinner(TradeVo tradeVo, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradePaymentResp> listener);

    /**
     * 正餐作废单据
     *
     * @param tradeId          trade.id
     * @param serverUpdateTime trade.serverUpdateTime
     * @param states
     * @param listener
     */
    void recisionDinner(Long tradeId, Long serverUpdateTime, List<DinnertableState> states, Reason reason, List<InventoryItemReq> inventoryItems,
                        ResponseListener<TradeResp> listener);

    /**
     * 拒绝正餐订单。此方法不阻塞调用线程
     *
     * @param tradeVo
     * @param listener
     */
    void refuseDinner(TradeVo tradeVo, Reason reason, ResponseListener<TradeResp> listener);

    /**
     * 取餐
     *
     * @param tradeExtra 订单额外信息
     * @param activity   上下文
     * @param listener   响应回调
     */
    void takeDish(TradeExtra tradeExtra, FragmentActivity activity, CalmResponseListener<ResponseObject<TakeDishResp>> listener);

    /**
     * 根据订单UUID查询订单详情需要的数据(此方法会阻塞调用线程)
     *
     * @param tradeUuid 订单UUID
     * @return 订单详情需要的数据
     */
    LoadResult loadData(String tradeUuid);

    /**
     * 根据tradeUuid查询Trade、Payment相关数据(此方法会阻塞调用线程)
     *
     * @param tradeUuid 订单UUID
     * @return 查询Trade、Payment相关数据
     */
    TradePaymentVo findTradePaymentVo(String tradeUuid);

    /**
     * 根据tradeUuid查询关联的已反结账单数据(此方法会阻塞调用线程)
     *
     * @param tradeUuid tradeUuid
     * @return List<TradePaymentVo>
     */
    List<TradePaymentVo> listRepeatTradePaymentVo(String tradeUuid);

    /**
     * 根据订单Id查询对应的退单信息(此方法会阻塞调用线程)
     *
     * @param tradeId
     * @return
     */
    TradeReturnInfo findTradeReturnInfo(Long tradeId);

    /**
     * 根据订单Id查询对应的开发票记录(此方法会阻塞调用线程)
     *
     * @param tradeId tradeId
     * @return
     */
    Invoice findInvoice(Long tradeId);

    /**
     * 根据uuid查询对应的开发票记录(此方法会阻塞调用线程)
     *
     * @param uuid
     * @return
     */
    Invoice findInvoice(String uuid);

    /**
     * 查询商户开通的第三方配送平台数据,此处包含在后台已经关闭的数据(此方法会阻塞调用线程)
     */
    List<PartnerShopBiz> queryDeliverylatformPartnerShopBiz();

    List<TradeItemVo> getValidTradeItemList(List<TradeItemVo> tradeItemList);

    /**
     * 根据tradeTable获取Tables信息(此方法会阻塞调用线程)
     */
    List<Tables> getTablesByTradeTable(List<TradeTable> tradeTableList);

    /**
     * 根据uuid查询TradeVo(此方法会阻塞调用线程)
     */
    TradeVo findTrade(String tradeUuid, boolean onlyValid);

    /**
     * 根据uuid查询TradeVo(此方法会阻塞调用线程)
     */
    TradeVo findTrade(String tradeUuid);

    /**
     * 根据tradeUuid查询Trade、Payment相关数据(此方法会阻塞调用线程)
     *
     * @param tradeUuid tradeUuid
     * @return TradePaymentVo
     */
    TradePaymentVo findTradePaymentVo(String tradeUuid, boolean onlyValid);

    /**
     * 根据sourceTradeUuid获取拆单信息(此方法会阻塞调用线程)
     */
    List<Trade> listSplitTrades(String sourceTradeUuid) throws Exception;

    /**
     * 根据订单UUID获取配送相关数据(此方法会阻塞调用线程)
     *
     * @param tradeUuid 订单UUID
     * @return 配送相关数据
     */
    List<DeliveryOrderVo> listDeliveryOrderVo(String tradeUuid);

    /**
     * 根据订单ID获取订单发票信息(此方法会阻塞调用线程)
     *
     * @param tradeId 订单ID
     * @return 订单发票信息
     */
    TradeInvoice getTradeInvoice(Long tradeId);

    /**
     * 通过订单数据获取桌台数据(此方法会阻塞调用线程)
     *
     * @param tradeVo 订单数据
     * @return 桌台数据
     * @throws Exception
     */
    DinnerTableInfo getDinnerTable(TradeVo tradeVo) throws Exception;

    /**
     * 根据TradeExtra Id查找第三方隐私手机号
     *
     * @param tradeExtraId
     * @return
     */
    TradeExtraSecrecyPhone findTradeExtraSecrecyPhone(Long tradeExtraId);

    /**
     * 获得配送员
     */
    void getSenders(GetSendersListener GetSendersListener);

    /**
     * 美业订单作废
     *
     * @param tradeId
     * @param serverUpdateTime
     * @param states
     * @param reason
     * @param inventoryItems
     * @param listener
     */
    void recisionBeauty(Long tradeId, Long serverUpdateTime, List<DinnertableState> states, Reason reason, List<InventoryItemReq> inventoryItems,
                        ResponseListener<TradeResp> listener);

    /**
     * 美业退货接口
     *
     * @param tradeVo
     * @param reason
     * @param listener
     */
    void refundBeauty(TradeVo tradeVo, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradePaymentResp> listener);


    /**
     * 美业订单反结账
     *
     * @param tradeVo  订单数据
     * @param reason   原因
     * @param listener 响应回调
     */
    void repayBeautyOrder(TradeVo tradeVo, Reason reason, ResponseListener<PaymentResp> listener);

    interface GetSendersListener {
        void onSuccess(List<User> authUsers);

        void onFail();
    }
}
