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

/**
 * 交易相关的操作
 */
public interface TradeOperates extends IOperates {

    /**
     * 下单。此方法不阻塞调用线程
     *
     * @param tradeVo
     * @param listener
     */
    void insert(TradeVo tradeVo, ResponseListener<TradeResp> listener);

    /**
     * 正餐下单。此方法不阻塞调用线程
     *
     * @param tradeVo
     * @param dinnertable
     * @param listener
     */
    void insertDinner(TradeVo tradeVo, IDinnertable dinnertable, ResponseListener<TradeResp> listener);

    /**
     * 正餐下单。
     *
     * @param tradeVo
     * @param dinnertable
     * @param listener
     * @param isAsync     是否异步
     */
    void insertDinner(TradeVo tradeVo, IDinnertable dinnertable, ResponseListener<TradeResp> listener, boolean isAsync);


    /**
     * 自助餐下单。
     *
     * @param tradeVo
     * @param dinnertable
     * @param listener
     * @param isAsync     是否异步
     */
    void insertBuffet(TradeVo tradeVo, IDinnertable dinnertable, ResponseListener<TradeResp> listener, boolean isAsync);

    /**
     * 正餐改单。此方法不阻塞调用线程
     *
     * @param tradeVo
     * @param listener
     */
    void modifyDinner(TradeVo tradeVo, ResponseListener<TradeResp> listener);

    /**
     * 正餐改单。此方法不阻塞调用线程
     *
     * @param tradeVo
     * @param listener
     * @param isAsync
     */
    void modifyDinner(TradeVo tradeVo, ResponseListener<TradeResp> listener, boolean isAsync);

    /**
     * 正餐改单。此方法不阻塞调用线程
     *
     * @param tradeVo
     * @param isUpdate 是否更新基本信息，时间戳和操作员等
     * @param listener
     */
    void modifyDinner(TradeVo tradeVo, boolean isUpdate, ResponseListener<TradeResp> listener, boolean isAsync);


    /**
     * 自助餐改单
     *
     * @param tradeVo
     * @param listener
     */
    void modifyBuffet(TradeVo tradeVo, boolean isUpdate, ResponseListener<TradeResp> listener, boolean isAsync);

    void modifyBuffet(TradeVo tradeVo, ResponseListener<TradeResp> listener);

    /**
     * 收银。此方法不阻塞调用线程
     *
     * @param tradeVo
     * @param paymentVoList   由于有AA制支付，所以要传入List
     * @param memberPasswords 会员余额支付时密码。key为PaymentItem.uuid，value为密码
     * @param listener
     */
    void pay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
             ResponseListener<PaymentResp> listener);

    void pay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
             ResponseListener<PaymentResp> listener, boolean isAsync);

    /**
     * 调账。此方法不阻塞调用线程
     *
     * @param paymentVo
     * @param listener
     */
    void adjust(PaymentVo paymentVo, ResponseListener<PaymentResp> listener);

    /**
     * 下单并收银。此方法不阻塞调用线程
     *
     * @param tradeVo
     * @param paymentVoList   由于有预支付和AA制支付，所以要传入List
     * @param memberPasswords 会员余额支付时密码。key为PaymentItem.uuid，value为密码
     * @param listener
     */
    void insertAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                      ResponseListener<TradePaymentResp> listener);

    void insertAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                      ResponseListener<TradePaymentResp> listener, boolean isAsync);

    /**
     * 正餐改单并收银 参数含义同上
     *
     * @Title: modifyAndPay
     * @Description:
     * @Param @param tradeVo
     * @Param @param paymentVoList
     * @Param @param memberPasswords
     * @Param @param listener
     * @Return void 返回类型
     */
    void modifyAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                      ResponseListener<PaymentResp> listener);

    void modifyAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                      ResponseListener<PaymentResp> listener, boolean isAsync);

    /**
     * @Title: salesRetuenPay
     * @Description: 无单退货
     * @Param @param tradeVo
     * @Param @param paymentVoList
     * @Param @param memberPasswords
     * @Param @param listener
     * @Return void 返回类型
     */
    void salesRetuenPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Reason reason, boolean isReturnInventory,
                        Map<String, String> memberPasswords, ResponseListener<TradePaymentResp> listener);

    /**
     * 获取微信支付URL。此方法不阻塞调用线程
     *
     * @param req
     * @param listener
     */
    void getWechatPayUrl(WechatPayUrlReq req, ResponseListener<WechatPayUrlResp> listener);

    /**
     * @微信被扫支付，此方法不阻塞调用线程
     * @Param @param req
     * @Param @param listener
     */
    void wechatPay(WechatPayReq req, ResponseListener<WechatPayResp> listener);

    /**
     * 验证单据的支付状态。此方法不阻塞调用线程
     *
     * @param tradeId
     * @param listener
     */
    void verifyPay(long tradeId, ResponseListener<VerifyPayResp> listener);

    /**
     * 接受订单。此方法不阻塞调用线程
     *
     * @param tradeVo
     * @param listener
     */
    void accept(TradeVo tradeVo, ResponseListener<TradeResp> listener);

    /**
     * 批量接受订单。此方法不阻塞调用线程
     *
     * @param actionType 操作类型
     * @param tradeVos   订单列表
     * @param listener   回调
     */
    void receiveBatch(ActionType actionType, List<TradeVo> tradeVos, ResponseListener<TradeResp> listener);

    /**
     * 拒绝订单。此方法不阻塞调用线程
     *
     * @param tradeVo
     * @param listener
     */
    void refuse(TradeVo tradeVo, Reason reason, ResponseListener<TradeResp> listener);

    /**
     * 批量拒绝订单。此方法不阻塞调用线程
     *
     * @param actionType 操作类型
     * @param reason     原因
     * @param tradeVos   订单列表
     * @param listener   回调
     */
    void refuseBatch(ActionType actionType, Reason reason, List<TradeVo> tradeVos, ResponseListener<TradeResp> listener);

    /**
     * 接受正餐订单。此方法不阻塞调用线程
     *
     * @param trade
     * @param listener
     */
    void acceptDinner(Trade trade, ResponseListener<TradeResp> listener);

    void acceptDinner(Trade trade, boolean genBatchNo, ResponseListener<TradeResp> listener);

    /**
     * 拒绝正餐订单。此方法不阻塞调用线程
     *
     * @param tradeVo
     * @param listener
     */
    void refuseDinner(TradeVo tradeVo, Reason reason, ResponseListener<TradeResp> listener);

    /**
     * 作废。此方法不阻塞调用线程
     *
     * @param tradeVo
     * @param returnInventoryItems
     * @param listener
     */
    void recision(TradeVo tradeVo, Reason reason, List<InventoryItemReq> returnInventoryItems, ResponseListener<TradeResp> listener);

    /**
     * 退货。此方法不阻塞调用线程
     *
     * @param tradeVo
     * @param returnInventoryItems
     * @param listener
     */
    void refund(TradeVo tradeVo, Reason reason, List<InventoryItemReq> returnInventoryItems, ResponseListener<TradePaymentResp> listener);

    /**
     * 挂单。此方法会阻塞调用线程
     *
     * @param tradeVo 要挂单的交易记录
     * @return 返回挂单总数
     * @throws Exception
     */
    long pending(TradeVo tradeVo) throws Exception;

    /**
     * 批量清账。此方法不阻塞调用线程
     *
     * @param tradeVoList 需要清账的单据列表
     * @param listener
     */
    void deliveredPayment(List<TradeVo> tradeVoList, ResponseListener<TradePaymentResp> listener);

    /**
     * 正餐批量清账
     *
     * @param tradeVoList 需要清账的单据列表
     * @param listener
     */
    void clearAccounts(List<TradeVo> tradeVoList, ResponseListener<TradePaymentResp> listener);

    /**
     * 获取二维码发券接口
     *
     * @param listener
     */
    void getSendCouponUrl(ResponseListener<CouponUrlResp> listener);

    /**
     * 订单批量解绑优惠券接口
     *
     * @param tradeId         订单Id
     * @param tradePrivileges 订单优惠
     * @param listener
     */
    void batchunbindCoupon(Long tradeId, List<TradePrivilege> tradePrivileges, ResponseListener<TradeResp> listener);

    /**
     * @Title: unbindCoupon
     * @Description: 订单和优惠劵解绑
     * @Param listener
     * @Return void 返回类型
     */
    void unbindCoupon(TradeUnbindCouponReq req, ResponseListener<TradeResp> listener);

    /**
     * @Title: unbindIntegral
     * @Description: 取消积分抵现
     * @Param @param req
     * @Param @param listener
     * @Return void 返回类型
     */
    void unbindIntegral(TradeUnbindCouponReq req, ResponseListener<TradeResp> listener);

    /**
     * 正餐换桌
     *
     * @param orginal
     * @param dest
     * @param listener
     */
    void transferDinnertable(List<TradeItemExtraDinner> tradeItemSeats, IDinnertableTrade orginal, IDinnertable dest,
                             ResponseListener<TransferDinnertableResp> listener);

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
     * 正餐退货接口
     *
     * @param tradeVo
     * @param reason
     * @param listener
     */
    void refundDinner(TradeVo tradeVo, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradePaymentResp> listener);

    /**
     * 美业退货接口
     *
     * @param tradeVo
     * @param reason
     * @param listener
     */
    void refundBeauty(TradeVo tradeVo, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradePaymentResp> listener);

    /**
     * 正餐合单
     *
     * @param orginal
     * @param dest
     * @param listener
     */
    void mergeDinner(List<TradeItemExtraDinner> tradeItemSeats, IDinnertableTrade orginal, IDinnertableTrade dest, final Long levelId, ResponseListener<TradeResp> listener);

    /**
     * @Title: changeTradeItemPrintStatus
     * @Description:
     * @Param itemStausList
     * @Return void 返回类型
     */
    //void changeTradeItemPrintStatus(List<ItemStaus> itemStausList, ResponseListener<TradeItemResp> listener);

    /**
     * @param tradeVo
     * @param reason
     * @param listener
     * @Title: weixinRefund
     * @Description: 微信退款
     * @Return void 返回类型
     */
    void weixinRefund(TradeVo tradeVo, Reason reason, ResponseListener<TradePaymentResp> listener);

    /**
     * @param source
     * @param target
     * @param listener
     * @Title: tradeSplit
     * @Description: 正餐拆单
     * @Return void 返回类型
     */
    void tradeSplitDinner(TradeVo source, TradeVo target, ResponseListener<TradeResp> listener);

    /**
     * @param source
     * @param target
     * @param paymentVoList
     * @param memberPasswords
     * @param listener
     * @Title: tradeSplitPayDinner
     * @Description: 正餐拆单及支付
     * @Return void 返回类型
     */
    void tradeSplitPayDinner(TradeVo source, TradeVo target, List<PaymentVo> paymentVoList,
                             Map<String, String> memberPasswords, ResponseListener<PaymentResp> listener);

    /**
     * @param tradeVo
     * @param Reason
     * @param listener
     * @Title: rePay
     * @Description: 正餐反结账
     * @Return void 返回类型
     */
    void tradeRepay(TradeVo tradeVo, Reason Reason, ResponseListener<PaymentResp> listener);


    /**
     * @param tradeRepayReq
     * @param listener
     * @Title: rePay
     * @Description: 美业反结账
     * @Return void 返回类型
     */
    void beautyTradeRepay(TradeRepayReq tradeRepayReq, ResponseListener<PaymentResp> listener);

    /**
     * 执行关账接口
     *
     * @param closeBillReq
     * @param listener
     * @Title: doCloseBill
     * @Description:
     * @Return void 返回类型
     */
    void doCloseBill(CloseBillReq closeBillReq, ResponseListener<CloseBillResp> listener);

    /**
     * 查询关账历史
     *
     * @Title: queryHandOverHistory
     * @Description: TODO
     * @Param @param listener TODO
     * @Return void 返回类型
     */
    @Deprecated
    void queryHandOverHistory(ResponseListener<ClosingHandOverResp> listener);

    /**
     * 查询关账历史
     *
     * @Title: queryHandOverHistory
     * @Description: TODO
     * @Param @param listener TODO
     * @Return void 返回类型
     */
    void queryHandOverHistory(ClosingReq req, ResponseListener<ClosingHandOverResp> listener);


    /**
     * @param
     * @return void 返回类型
     * @Description: 查看关账详情接口
     */
    void closeDetail(CloseDetailReq closeDetailReq, ResponseListener<CloseBillDataInfo> listener);

    /**
     * Erp透传接口
     *
     * @param transferReq
     * @param listener
     */
    void closeDetail(TransferReq transferReq, ResponseListener<TransferCloseBillData> listener);

    /**
     * 关账历史列表查询
     *
     * @param closeHistoryReq
     * @param listener
     * @return void 返回类型
     * @title: queryCloseHistoryList
     * @description:
     */
    void queryCloseHistoryList(CloseHistoryReq closeHistoryReq, ResponseListener<CloseHistoryResp> listener);

    void modifyServiceDish(DishServiceReq dishServiceReq, ResponseListener<TradeItemResp> listener);

    /**
     * 划菜与取消菜单接口
     *
     * @param dishServiceReq
     * @param listener
     */
    void modifyServiceDishV2(DishServiceV2Req dishServiceReq, CalmResponseListener<ResponseObject<DishServiceV2Resp>> listener);

    /**
     * 根据uuid查询指定的paymentitem
     *
     * @Title: queryPaymentItem
     * @Param @param uuid
     * @Return void 返回类型
     */
    void queryPaymentItem(String uuid, ResponseListener<PaymentItem> listener);

    /**
     * 修改银联退款状态
     *
     * @Title: changeUnionpayRefundStatus
     * @Param @param req
     * @Return void 返回类型
     */
    void changeRefundStatus(RefundStatusReq req, ResponseListener<RefundStatusResp> listener);

    /**
     * 修改打印记录表的打印状态
     *
     * @Title: updatePrinted
     * @Description: TODO
     * @Param @param req
     * @Param @param listener TODO
     * @Return void 返回类型
     */
    //void updatePrinted(UpdatePrintedReq req, ResponseListener<PrintOperation> listener);

    /**
     * @Title: saleCardInsert
     * @Description: 售卡下单接口
     * @Param tradeVo
     * @Param listener
     * @Return void 返回类型
     */
    void sellCardsInsert(TradeVo tradeVo, ResponseListener<TradeResp> listener);

    /**
     * @Title: saleCardsPay
     * @Description: 售卡收银接口
     * @Param tradeVo
     * @Param paymentVoList
     * @Param memberPasswords
     * @Param listener
     * @Return void 返回类型
     */
    void sellCardsPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                      ResponseListener<PaymentResp> listener);

    /**
     * @Title: saleCardsPay
     * @Description: 售卡下单收银接口
     * @Param tradeVo
     * @Param paymentVoList
     * @Param memberPasswords
     * @Param listener
     * @Return void 返回类型
     */
    void sellCardsInsertAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                               ResponseListener<TradePaymentResp> listener);

    /**
     * @Description: 临时卡售卡下单接口
     */
    void insertAnonymousCards(TradeVo tradeVo, ResponseListener<TradeResp> listener);

    /**
     * @Description: 临时卡售卡收银
     */
    void sellAnonymousCardsPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords, ResponseListener<PaymentResp> listener);

    /**
     * @Description: 临时卡售卡下单并收银
     */
    void sellAnonymousCardsinsertAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords, ResponseListener<TradePaymentResp> listener);

    /**
     * @Description: 临时卡售卡并储值接口
     */
    void saleAndStoreAnonymousCard(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords, ResponseListener<TradePaymentResp> listener);

    /**
     * @Description: 临时卡储值接口
     */
    void storeAnonymousCard(TradeVo tradeVo, int online, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords, ResponseListener<AnonymousCardStoreResp> listener);

    /**
     * @Description: 会员实体卡储值
     */
    void storeEntityCard(TradeVo tradeVo, int online, long customerId, BigDecimal chargeMoney, BigDecimal sendMoney, List<PaymentVo> paymentVoList, ResponseListener<AnonymousCardStoreResp> listener);

    /**
     * @Description: 会员虚拟卡储值
     */
    void storeVirtualCard(TradeVo tradeVo, int online, long customerId, BigDecimal chargeMoney, BigDecimal sendMoney, List<PaymentVo> paymentVoList, ResponseListener<AnonymousCardStoreResp> listener);

    /**
     * 饿了么提出退单申请后，由商户进行确认操作（同意/拒绝）
     *
     * @param tradeId
     * @param returnStatus
     * @param requestUuid
     * @param reason
     */
    void returnConfirm(Long tradeId, TradeReturnInfoReturnStatus returnStatus, boolean isReturnInventory, String requestUuid, Reason reason, ResponseListener<Object> listener);

    void returnSellCards(Trade srcTrade, TradeVo tradeVo, List<PaymentVo> paymentVoList, List<ReturnCardDataModel.CardNumber> cardList, Reason reason, boolean returnAll, ResponseListener<SalesCardReturnResp> listener);

    /**
     * 催菜
     *
     * @Title: remindDish
     * @Return void 返回类型
     */
    void remindDish(Trade trade, List<Long> tradeItemIds, ResponseListener<TradeItemResp> listener);

    /**
     * 起菜
     *
     * @Title: riseDish
     * @Return void 返回类型
     */
    void riseDish(Trade trade, List<Long> tradeItemIds, ResponseListener<TradeItemResp> listener);

    /**
     * 菜品操作
     */
    void operationDish(Long tradeId, PrintOperationOpType opType, List<DishDataItem> selectedItems, ResponseListener<TradeItemResp> listener);

    /**
     * 更新支付状态
     *
     * @Title: refreshState
     * @Description: TODO
     * @Param @param tradeVo
     * @Param @param listener TODO
     * @Return void 返回类型
     */
    void refreshState(Long traudeId, Long paymentItemId, ResponseListener<TradePayStateResp> listener);


    /**
     * 更新在线支付退货单的支付状态
     *
     * @Title: refreshReturnState
     * @Description: TODO
     * @Param @param tradeVo
     * @Param @param listener TODO
     * @Return void 返回类型
     */
    void refreshReturnState(TradeVo tradeVo, ResponseListener<TradePayStateResp> listener);

    /**
     * 正餐设置桌台并接受
     *
     * @Title: dinnerSetTableAndAccept
     * @Param @param tradeVo
     * @Param @param table
     * @Return void 返回类型
     */
    void dinnerSetTableAndAccept(TradeVo tradeVo, Tables table, Bool genBatchNo, ResponseListener<TradeResp> listener);

    /**
     * 修改普通订单菜品打印状态
     *
     * @Title: modifyPrintStatus
     * @Param @param tradeItems
     * @Param @param printOperations
     * @Return void 返回类型
     */
    void modifyPrintStatus(List<TradeItem> tradeItems, List<PrintOperation> printOperations,
                           List<TradeItemOperation> tradeItemOperations, ResponseListener<TradeItemResp> listener);

    /**
     * 修改联台主单菜品打印状态
     *
     * @param mainTradeId
     * @param tradeItems
     * @param printOperations
     * @param tradeItemOperations
     * @param listener
     */
    void modifyMainTradePrintStatus(Long mainTradeId, List<TradeItem> tradeItems, List<PrintOperation> printOperations,
                                    List<TradeItemOperation> tradeItemOperations, ResponseListener<TradeItemResp> listener);

    /**
     * 修改联台子单菜品打印状态
     *
     * @param mainTradeId
     * @param subTradeId
     * @param tradeItems
     * @param printOperations
     * @param tradeItemOperations
     * @param listener
     */
    void modifySubTradePrintStatus(Long mainTradeId, Long subTradeId, List<TradeItem> tradeItems, List<PrintOperation> printOperations,
                                   List<TradeItemOperation> tradeItemOperations, ResponseListener<TradeItemResp> listener);

    /**
     * 大众点评完成交易
     *
     * @Title: tradeFinish
     * @Param @param tradeId
     * @Return void 返回类型
     */
    void tradeFinish(Long tradeId, ResponseListener<TradeFinishResp> listener);


    /**
     * 退押金
     *
     * @param tradeId       订单Id
     * @param depositRefund 退押金退的金额
     * @param payModeId     退押金支付方式
     * @Return void 返回类型
     */
    void depositRefund(Long tradeId, BigDecimal depositRefund, Long payModeId, Reason reason, ResponseListener<DepositRefundResp> listener);


    /**
     * 自助餐退押金
     *
     * @param tradeId
     * @param depositRefund
     * @param paymentItemId
     * @param reason
     * @param listener
     */
    void buffetDepositRefund(Long tradeId, BigDecimal depositRefund, Long paymentItemId, Reason reason, ResponseListener<PayResp> listener);


    /**
     * 自助餐订单完成及退押金
     *
     * @param tradeId
     * @param depositRefund
     * @param paymentItemId
     * @param reason
     * @param listener
     */
    void buffetFinishAndDepositRefund(Long tradeId, BigDecimal depositRefund, Long paymentItemId, Reason reason, ResponseListener<PayResp> listener);

    /**
     * 移菜
     *
     * @param sourceTradeVo
     * @param targetTradeVo
     * @param listener
     */
    void moveDish(TradeVo sourceTradeVo, TradeVo targetTradeVo, List<TradeItem> tradeItems, ResponseListener<TradeResp> listener, Integer actionType, Integer moveAdd);


    /**
     * 挂账请求
     *
     * @param tradeVo
     * @param lagReq
     */
    void doLag(TradeVo tradeVo, LagReq lagReq, ResponseListener<LagResp> listener);

    /**
     * 获取商品排行榜
     *
     * @param req
     * @param listener
     */
    void getGoodsSaleRank(GoodsSellRankPrintReq req, ResponseListener<GoodsSellRankResp> listener);

    /**
     * 获取商品排行榜打印信息
     *
     * @param req
     * @param listener
     */
    void getGoodsSaleRankPrint(TransferReq<GoodsSellRankPrintReq> req, ResponseListener<MindTransferResp<GoodsSellRankPrintResp>> listener);


    /**
     * 获取实时概况
     *
     * @param req
     * @param listener
     */
    void getBusinessCharge(TransferReq<BusinessChargeReq> req, ResponseListener<MindTransferResp<BusinessChargeResp>> listener);

    /**
     * 批量上传授权记录
     *
     * @param authorizedLogList
     */
    void batchUploadAuthLog(List<AuthorizedLog> authorizedLogList, ResponseListener<AuthLogResp> listener);


    /**
     * 派送订单到配送平台
     *
     * @param req      req
     * @param listener listener
     */
    void sendOrder(DispatchOrderReq req, ResponseListener<TradeResp> listener);


    /**
     * 将订单从配送平台取消派送(通过gateway)
     *
     * @param req
     * @param listener
     */
    void cancelDeliveryOrder(CancelDeliveryOrderReq req, ResponseListener<GatewayTransferResp<CancelDeliveryOrderResp>> listener);

    /**
     * 接受微信加菜
     *
     * @param tradeVo
     * @param hasAllPrintedKitchen //是否所有菜品都已传送后厨，为true时加菜要生成批次号并打印后厨，为false则不生成，只打印客看单
     * @param listener
     */
    void acceptAddItem(TradeVo tradeVo, AddItemVo addItemVo, boolean hasAllPrintedKitchen, ResponseListener<TradeResp> listener);

    /**
     * 拒绝微信加菜
     *
     * @param addItemVo
     * @param listener
     */
    void refuseAddItem(AddItemVo addItemVo, ResponseListener<TradeResp> listener);

    /**
     * 快餐换桌
     *
     * @param oldTableTradeVo
     * @param newTable
     * @param listener
     */
    void exChangeTable(TableTradeVo oldTableTradeVo, Tables newTable, ResponseListener<ExChangeTableResp> listener);

    /**
     * 电子发票获取开票二维码
     *
     * @param req
     * @param listener
     */
    void invoiceQrcode(InvoiceQrcodeReq req, ResponseListener<GatewayTransferResp<InvoiceQrcodeResp>> listener);

    /**
     * 峰火电子发票获取开票二维码
     *
     * @param req
     * @param listener
     */
    void invoiceFHQrcode(FHInvoiceQrcodeReq req, ResponseListener<GatewayTransferResp<FHInvoiceQrcodeResp>> listener);

    /**
     * 峰火查询余额接口
     *
     * @param req
     * @param listener
     */
    void queryBalanceFH(FHQueryBalanceReq req, ResponseListener<GatewayTransferResp<FHQueryBalanceResp>> listener);

    /**
     * 电子发票冲红
     *
     * @param req
     * @param listener
     */
    void invoiceRevoke(InvoiceRevokeReq req, ResponseListener<GatewayTransferResp<Invoice>> listener);

    /**
     * 查询配送费
     *
     * @param req
     * @param listener
     */
    void queryDeliveryFee(QueryDeliveryFeeReq req, ResponseListener<GatewayTransferResp<QueryDeliveryFeeResp>> listener);

    /**
     * 批量查询配送费
     *
     * @param req
     * @param listener
     */
    void batchQueryDeliveryFee(BatchQueryDeliveryFeeReq req, ResponseListener<GatewayTransferResp<JsonArray>> listener);

    /**
     * 订单下发配送平台
     *
     * @param req
     * @param listener
     */
    void deliveryOrderDispatch(DeliveryOrderDispatchReq req, ResponseListener<GatewayTransferResp<DeliveryOrderDispatchResp>> listener);

    /**
     * 配送订单批量查询
     *
     * @param req
     * @param listener
     */
    void deliveryOrderList(DeliveryOrderListReq req, ResponseListener<GatewayTransferResp<DeliveryOrderListResp>> listener);

    /**
     * v3版溢收订单退款检查
     *
     * @param tradeId
     * @param paymentItemId
     */
    void refundCheck(Long tradeId, Long paymentItemId, SimpleResponseListener<RefundCheckResp> listener);

    void refundPayment(Long tradeId, Long paymentItemId, SimpleResponseListener<PayResp> listener);

    /**
     * 版溢收订单退款
     *
     * @param tradeId
     * @param paymentItemId
     * @param refundFee
     * @param listener
     */
    void refundSubmit(Long tradeId, Long paymentItemId, BigDecimal refundFee, SimpleResponseListener<RefundSubmitResp> listener);

    void takeDish(TradeExtra tradeExtra, FragmentActivity activity, CalmResponseListener<ResponseObject<TakeDishResp>> listener);


    void insertBuffetNoTable(TradeVo tradeVo, AuthUser waiter, ResponseListener<BuffetNoTableTradeResp> listener);


    /**
     * 校验并核销优惠券/积分/微信优惠券,此方法在构建请求数据时会查询数据库，因此会阻塞调用线程
     *
     * @param trade           trade
     * @param businessType    订单业务类型
     * @param customerId      顾客或会员id
     * @param entityCardNo    实体卡卡号
     * @param tradePrivileges tradePrivileges
     * @param listener        listener
     */
    void usePrivilege(Trade trade, BusinessType businessType, Long customerId, String entityCardNo, Collection<TradePrivilege> tradePrivileges, QSResponseListener<UsePrivilegeResp> listener);

    /**
     * Gateway 请求添加消费
     * v8.1
     */
    void requestAddFee(Long deliveryOrderId, Integer deliveryPlatform, Double amount, ResponseListener<GatewayTransferResp<AddFeeResp>> listener);

    /**
     * 针对快餐异步机制的收银,此方法在构建请求数据时会查询数据库，因此会阻塞调用线程
     *
     * @param req      req
     * @param listener listener
     */
    void pay4QS(UsePayReq req, QSResponseListener<UsePayResp> listener);

    /**
     * 绑定配送员
     *
     * @param req      req
     * @param listener listener
     */
    void bindDeliveryUser(BindOrderReq req, ResponseListener<BindOrderResp> listener);

    /**
     * 创建联台订单
     *
     * @param req      req
     * @param listener listener
     */
    void creatUnionTrade(UnionTradeReq req, ResponseListener<TradeResp> listener);

    /**
     * 取消联台子单
     *
     * @param req      req
     * @param listener listener
     */
    void splitUnionTrade(UnionTradeSplitReq req, ResponseListener<TradeResp> listener);

    /**
     * 联台主单改单
     *
     * @param req
     * @param listener
     */
    void modifyUnionMainTrade(TradeVo tradeVo, TradeUnionModifyMainWarpReq req, ResponseListener<TradeResp> listener, boolean isAsync);

    /**
     * 联台子单改单
     *
     * @param req
     * @param listener
     */
    void modifyUnionSubTrade(TradeVo tradeVo, TradeUnionModifySubWarpReq req, ResponseListener<TradeResp> listener, boolean isAsync);

    /**
     * 联台订单合单并改单
     *
     * @param req
     * @param listener
     */
    void unionAndModifyUnionTrade(UnionAndModifyUnionTradeReq req, ResponseListener<TradeResp> listener);

    /**
     * 主单批量划菜/撤销划菜
     *
     * @param dishServiceReq
     * @param listener
     */
    void modifyServiceUnionDish(DishServiceV2Req dishServiceReq, CalmResponseListener<ResponseObject<DishServiceV2Resp>> listener, TradeType type);

    /**
     * 联台菜品操作
     *
     * @param req
     * @param listener
     */
    void unionOperationDish(UnionTradeItemOperationReq req, ResponseListener<TradeItemResp> listener);

    /**
     * 修改订单备注
     *
     * @param trade
     * @param newMemo
     * @param listener
     */
    void modifyTradeMemo(Trade trade, String newMemo, ResponseListener<ModifyTradeMemoResp> listener);

    void buffetUnionTableCreate(BuffetUnionTradeReq req, ResponseListener<TradeResp> listener);


    /**
     * 自助联台子单改单
     *
     * @param req
     * @param listener
     */
    void buffetModifyUnionSubTrade(TradeVo tradeVo, TradeUnionModifySubWarpReq req, ResponseListener<TradeResp> listener, boolean isAsync);

    /**
     * 取消自助联台
     *
     * @param req
     * @param listener
     */
    void buffetSplitTrade(BuffetUnionTradeCancelReq req, ResponseListener<BuffetUnionTradeCancelResp> listener);


    void buffetUnionMainTradeModify(BuffetMainTradeModifyReq req, ResponseListener<TradeResp> listener);

    /**
     * 自助餐连台主单添加餐标，添加/修改人数、押金、销售员
     *
     * @param tradeVo
     * @param listener
     */
    void buffetCreateMenu(TradeVo tradeVo, ResponseListener<TradeResp> listener);

    /**
     * 自助联台主单完成功能
     *
     * @param req
     * @param listener
     */
    void buffetUnionFinish(BuffetMergeUnionReq req, ResponseListener<TradeResp> listener);

    /**
     * 会员销账接口
     *
     * @param req
     * @param listener
     */
    void memberWriteoff(WriteoffReq req, ResponseListener<WriteoffResp> listener);

    void queryInvoiceNo(GetTaxNoReq req, ResponseListener<GetTaxNoResp> listener);

    /**
     * 创建预付单接口
     */
    void createPrePayTrade(PrePayTradeReq req, ResponseListener<TradeResp> listener);


    /**
     * 作废预付单
     *
     * @param req
     * @param listener
     */
    void deletePrePayTrade(DeletePrePayTradeReq req, ResponseListener<TradeResp> listener);

    void getWriteOffOnlinePayResult(String tradeNo, PayType payType, ResponseListener<WriteoffOnlineResultResp> listener);

    void useEarnestDeduct(EarnestDeductReq req, ResponseListener<PayResp> listener);

    /**
     * 预订预付金抵扣多付退款
     */
    void bookingRrePayRefund(PrePayRefundReq req, ResponseListener<PayResp> listener);

    /**
     * 联台订单作废，支持自助餐和正餐业态
     *
     * @param tradeId
     * @param serverUpdateTime
     * @param reason
     * @param inventoryItems
     * @param listener
     */
    void recisionUnionTrade(Long tradeId, Long serverUpdateTime, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradeResp> listener);
}
