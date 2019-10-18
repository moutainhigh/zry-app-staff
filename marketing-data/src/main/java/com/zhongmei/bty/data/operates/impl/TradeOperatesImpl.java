package com.zhongmei.bty.data.operates.impl;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.beauty.operates.message.BeautyTradeDeleteReq;
import com.zhongmei.beauty.utils.BeautyServerAddressUtil;
import com.zhongmei.bty.basemodule.async.listener.AsyncOpenTableResponseListener;
import com.zhongmei.bty.basemodule.async.manager.AsyncNetworkManager;
import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.auth.permission.message.AuthLogReq;
import com.zhongmei.bty.basemodule.auth.permission.message.AuthLogResp;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.entity.TaxRateInfo;
import com.zhongmei.bty.basemodule.commonbusiness.listener.SimpleResponseListener;
import com.zhongmei.bty.basemodule.commonbusiness.message.BusinessChargeReq;
import com.zhongmei.bty.basemodule.commonbusiness.message.BusinessChargeResp;
import com.zhongmei.bty.basemodule.commonbusiness.message.GatewayTransferReq;
import com.zhongmei.bty.basemodule.commonbusiness.operates.SystemSettingDal;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.database.entity.pay.PaymentDevice;
import com.zhongmei.bty.basemodule.database.entity.pay.PaymentItemUnionpay;
import com.zhongmei.bty.basemodule.devices.mispos.data.bean.ReturnCardDataModel.CardNumber;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.AnonymousCardStoreResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardRechargeReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.SalesCardReturnReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.SalesCardReturnResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.SalesReturnTradePaymentReq;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsVo;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.discount.message.CouponUrlResp;
import com.zhongmei.bty.basemodule.discount.message.UsePrivilegeReq;
import com.zhongmei.bty.basemodule.discount.message.UsePrivilegeResp;
import com.zhongmei.bty.basemodule.inventory.message.InventoryChangeReq;
import com.zhongmei.bty.basemodule.inventory.message.InventoryItemReq;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryUtils;
import com.zhongmei.bty.basemodule.orderdish.bean.AddItemVo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.orderdish.entity.AddItemRecord;
import com.zhongmei.bty.basemodule.orderdish.entity.KdsTradeItem;
import com.zhongmei.bty.basemodule.orderdish.entity.KdsTradeItemPart;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.bty.basemodule.orderdish.message.DishServiceReq;
import com.zhongmei.bty.basemodule.orderdish.message.DishServiceV2Req;
import com.zhongmei.bty.basemodule.orderdish.message.DishServiceV2Resp;
import com.zhongmei.bty.basemodule.orderdish.message.TradeItemResp;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGroupon;
import com.zhongmei.bty.basemodule.pay.message.PayResp;
import com.zhongmei.bty.basemodule.pay.message.PaymentItemTo;
import com.zhongmei.bty.basemodule.pay.message.PaymentReq;
import com.zhongmei.bty.basemodule.pay.message.PaymentResp;
import com.zhongmei.bty.basemodule.pay.message.PaymentTo;
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
import com.zhongmei.bty.basemodule.shoppingcart.BaseShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShoppingCartTool;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableState;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertable;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TableTradeVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeGroupInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.DeliveryOrderRecord;
import com.zhongmei.bty.basemodule.trade.entity.Invoice;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.bty.basemodule.trade.entity.TradeDepositPayRelation;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRel;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRelExtra;
import com.zhongmei.bty.basemodule.trade.entity.TradeMainSubRelation;
import com.zhongmei.bty.basemodule.trade.entity.TradeReceiveLog;
import com.zhongmei.bty.basemodule.trade.entity.TradeStatusLog;
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager;
import com.zhongmei.bty.basemodule.trade.message.AddFeeReq;
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
import com.zhongmei.bty.basemodule.trade.message.DepositRefundReq;
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
import com.zhongmei.bty.basemodule.trade.message.ModifyTradeMemoReq;
import com.zhongmei.bty.basemodule.trade.message.ModifyTradeMemoResp;
import com.zhongmei.bty.basemodule.trade.message.PrePayRefundReq;
import com.zhongmei.bty.basemodule.trade.message.PrePayTradeReq;
import com.zhongmei.bty.basemodule.trade.message.QueryDeliveryFeeReq;
import com.zhongmei.bty.basemodule.trade.message.QueryDeliveryFeeResp;
import com.zhongmei.bty.basemodule.trade.message.RecisionDinnerReq;
import com.zhongmei.bty.basemodule.trade.message.RefundCheckRequest;
import com.zhongmei.bty.basemodule.trade.message.RefundCheckResp;
import com.zhongmei.bty.basemodule.trade.message.RefundStatusReq;
import com.zhongmei.bty.basemodule.trade.message.RefundStatusResp;
import com.zhongmei.bty.basemodule.trade.message.RefundSubmitRequest;
import com.zhongmei.bty.basemodule.trade.message.RefundSubmitResp;
import com.zhongmei.bty.basemodule.trade.message.ReturnConfirmReq;
import com.zhongmei.bty.basemodule.trade.message.TakeDishReq;
import com.zhongmei.bty.basemodule.trade.message.TakeDishResp;
import com.zhongmei.bty.basemodule.trade.message.TradeBatchOpsReq;
import com.zhongmei.bty.basemodule.trade.message.TradeBatchUnbindCouponReq;
import com.zhongmei.bty.basemodule.trade.message.TradeDeleteReq;
import com.zhongmei.bty.basemodule.trade.message.TradeFinishReq;
import com.zhongmei.bty.basemodule.trade.message.TradeFinishResp;
import com.zhongmei.bty.basemodule.trade.message.TradeItemReq;
import com.zhongmei.bty.basemodule.trade.message.TradeNewReq;
import com.zhongmei.bty.basemodule.trade.message.TradeOpsReq;
import com.zhongmei.bty.basemodule.trade.message.TradePayStateReq;
import com.zhongmei.bty.basemodule.trade.message.TradePayStateResp;
import com.zhongmei.bty.basemodule.trade.message.TradePaymentReq;
import com.zhongmei.bty.basemodule.trade.message.TradePaymentResp;
import com.zhongmei.bty.basemodule.trade.message.TradeRefundReq;
import com.zhongmei.bty.basemodule.trade.message.TradeRepayReq;
import com.zhongmei.bty.basemodule.trade.message.TradeReq;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.message.TradeSplitPayReq;
import com.zhongmei.bty.basemodule.trade.message.TradeSplitReq;
import com.zhongmei.bty.basemodule.trade.message.TradeUnbindCouponReq;
import com.zhongmei.bty.basemodule.trade.message.TradeUnionDeleteReq;
import com.zhongmei.bty.basemodule.trade.message.TradeUnionModifyMainWarpReq;
import com.zhongmei.bty.basemodule.trade.message.TradeUnionModifySubWarpReq;
import com.zhongmei.bty.basemodule.trade.message.TransferDinnertableReq;
import com.zhongmei.bty.basemodule.trade.message.TransferDinnertableResp;
import com.zhongmei.bty.basemodule.trade.message.UnionTradeItemOperationReq;
import com.zhongmei.bty.basemodule.trade.message.VerifyPayResp;
import com.zhongmei.bty.basemodule.trade.message.WriteoffOnlineResultReq;
import com.zhongmei.bty.basemodule.trade.message.WriteoffOnlineResultResp;
import com.zhongmei.bty.basemodule.trade.message.WriteoffReq;
import com.zhongmei.bty.basemodule.trade.message.WriteoffResp;
import com.zhongmei.bty.basemodule.trade.message.uniontable.BuffetCreateMenuReq;
import com.zhongmei.bty.basemodule.trade.message.uniontable.BuffetUnionTradeCancelReq;
import com.zhongmei.bty.basemodule.trade.message.uniontable.BuffetUnionTradeCancelResp;
import com.zhongmei.bty.basemodule.trade.message.uniontable.BuffetUnionTradeReq;
import com.zhongmei.bty.basemodule.trade.message.uniontable.UnionAndModifyUnionTradeReq;
import com.zhongmei.bty.basemodule.trade.message.uniontable.UnionTradeReq;
import com.zhongmei.bty.basemodule.trade.message.uniontable.UnionTradeSplitReq;
import com.zhongmei.bty.basemodule.trade.message.uniontable.buffet.BuffetMainTradeModifyReq;
import com.zhongmei.bty.basemodule.trade.message.uniontable.buffet.BuffetMergeUnionReq;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.basemodule.trade.processor.GetTaxNoProcessor;
import com.zhongmei.bty.basemodule.trade.processor.PayRespProcessor;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.AuthorizedLog;
import com.zhongmei.bty.commonmodule.database.entity.DeliveryOrder;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.bty.commonmodule.database.entity.TradeItemLog;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpType;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.bty.commonmodule.util.Standard;
import com.zhongmei.bty.data.operates.message.content.AddItemReq;
import com.zhongmei.bty.data.operates.message.content.BuffetDepositRefundReq;
import com.zhongmei.bty.data.operates.message.content.BuffetFinishDepositRefundReq;
import com.zhongmei.bty.data.operates.message.content.ClearBillReq;
import com.zhongmei.bty.data.operates.message.content.DeliveredPaymentReq;
import com.zhongmei.bty.data.operates.message.content.DeliveredPaymentReq.DeliveredPayment;
import com.zhongmei.bty.data.operates.message.content.DinnerSetTableAndAcceptReq;
import com.zhongmei.bty.data.operates.message.content.ExChangeTableReq;
import com.zhongmei.bty.data.operates.message.content.KouBeiVerifyReq;
import com.zhongmei.bty.data.operates.message.content.KouBeiVerifyResp;
import com.zhongmei.bty.data.operates.message.content.MainTradeModifyTradeItemPrintStatusReq;
import com.zhongmei.bty.data.operates.message.content.MergeDinnerReq;
import com.zhongmei.bty.data.operates.message.content.ModifyTradeItemPrintStatusReq;
import com.zhongmei.bty.data.operates.message.content.ModuleItemReq;
import com.zhongmei.bty.data.operates.message.content.MoveDishReq;
import com.zhongmei.bty.data.operates.message.content.NullReq;
import com.zhongmei.bty.data.operates.message.content.OperationDishReq;
import com.zhongmei.bty.data.operates.message.content.PayDinnerReq;
import com.zhongmei.bty.data.operates.message.content.PaymentDinnerTo;
import com.zhongmei.bty.data.operates.message.content.PaymentInfo;
import com.zhongmei.bty.data.operates.message.content.PaymentOfRepeatReq;
import com.zhongmei.bty.data.operates.message.content.SubTradeModifyTradeItemPrintStatusReq;
import com.zhongmei.bty.data.operates.message.content.TradeInfo;
import com.zhongmei.bty.data.operates.message.content.TradeItemOperationReq;
import com.zhongmei.bty.data.operates.message.content.TradeItemOperationReq.InnerTradeItem;
import com.zhongmei.bty.data.operates.message.content.VirtualCardRechargeReq;
import com.zhongmei.bty.db.entity.VerifyKoubeiOrder;
import com.zhongmei.bty.snack.offline.Snack;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.discount.TradePromotion;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.PaymentItemExtra;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCreditLog;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradePrivilegeApplet;
import com.zhongmei.yunfu.db.entity.trade.TradePrivilegeLimitNumCard;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.db.enums.ActionType;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.OperateType;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PayModelGroup;
import com.zhongmei.yunfu.db.enums.PayType;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.PrintStatus;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.db.enums.TakeDishStatus;
import com.zhongmei.yunfu.db.enums.TradePayForm;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeReturnInfoReturnStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.http.CalmNetWorkRequest;
import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.yunfu.http.OpsRequest.SaveDatabaseResponseProcessor;
import com.zhongmei.yunfu.http.QSOpsRequest;
import com.zhongmei.yunfu.http.QSResponseListener;
import com.zhongmei.yunfu.http.processor.CalmDatabaseProcessor;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.resp.data.GatewayTransferResp;
import com.zhongmei.yunfu.resp.data.MindTransferResp;
import com.zhongmei.yunfu.resp.data.TransferReq;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.util.ValueEnums;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;


public class TradeOperatesImpl extends AbstractOpeartesImpl implements TradeOperates {

    public TradeOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void insert(TradeVo tradeVo, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void sellCardsInsert(TradeVo tradeVo, ResponseListener<TradeResp> listener) {


    }

    @Override
    public void sellCardsPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                             ResponseListener<PaymentResp> listener) {

    }

    @Override
    public void sellCardsInsertAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList,
                                      Map<String, String> memberPasswords, ResponseListener<TradePaymentResp> listener) {

    }

    @Override
    public void insertAnonymousCards(TradeVo tradeVo, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void sellAnonymousCardsPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords, ResponseListener<PaymentResp> listener) {

    }

    @Override
    public void sellAnonymousCardsinsertAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords, ResponseListener<TradePaymentResp> listener) {

    }

    @Override
    public void saleAndStoreAnonymousCard(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords, ResponseListener<TradePaymentResp> listener) {

    }

    @Override
    public void storeAnonymousCard(TradeVo tradeVo, int online, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords, ResponseListener<AnonymousCardStoreResp> listener) {

    }

    @Override
    public void storeEntityCard(TradeVo tradeVo, int online, long customerId, BigDecimal chargeMoney, BigDecimal sendMoney, List<PaymentVo> paymentVoList, ResponseListener<AnonymousCardStoreResp> listener) {

    }

        @Override
    public void storeVirtualCard(TradeVo tradeVo, int online, long customerId, BigDecimal chargeMoney, BigDecimal sendMoney, List<PaymentVo> paymentVoList, ResponseListener<AnonymousCardStoreResp> listener) {

    }

    @Override
    public void pay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                    ResponseListener<PaymentResp> listener) {

        pay(tradeVo, paymentVoList, memberPasswords, listener, false);
    }

    public void pay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                    ResponseListener<PaymentResp> listener, boolean isAsync) {

    }

    @Override
    public void adjust(PaymentVo paymentVo, ResponseListener<PaymentResp> listener) {

    }

    @Override
    public void insertAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                             ResponseListener<TradePaymentResp> listener, boolean isAsync) {

    }

    @Override
    public void insertAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                             ResponseListener<TradePaymentResp> listener) {

        insertAndPay(tradeVo, paymentVoList, memberPasswords, listener, false);
    }

    @Override
    public void modifyAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                             ResponseListener<PaymentResp> listener) {
        modifyAndPay(tradeVo, paymentVoList, memberPasswords, listener, false);
    }

    public void modifyAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                             ResponseListener<PaymentResp> listener, boolean isAsync) {

    }

    @Override
    public void getWechatPayUrl(WechatPayUrlReq req, ResponseListener<WechatPayUrlResp> listener) {

    }

    @Override
    public void wechatPay(WechatPayReq req, ResponseListener<WechatPayResp> listener) {

    }

    @Override
    public void verifyPay(long tradeId, ResponseListener<VerifyPayResp> listener) {

    }

    @Override
    public void accept(TradeVo tradeVo, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void receiveBatch(ActionType actionType, List<TradeVo> tradeVos, ResponseListener<TradeResp> listener) {

    }

    private TradeBatchOpsReq toTradeBatchOpsReq(ActionType actionType, Reason reason, List<TradeVo> tradeVos) {
        long currentTimeMillis = System.currentTimeMillis();

        TradeBatchOpsReq tradeBatchOpsReq = new TradeBatchOpsReq();
        tradeBatchOpsReq.setActionType(actionType);
        tradeBatchOpsReq.setClientUpdateTime(currentTimeMillis);
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            tradeBatchOpsReq.setUpdatorId(user.getId());
            tradeBatchOpsReq.setUpdatorName(user.getName());
        }
        if (reason != null) {
            tradeBatchOpsReq.setReasonId(reason.getId());
            tradeBatchOpsReq.setReasonContent(reason.getContent());
        }
        List<TradeOpsReq> list = new ArrayList<TradeOpsReq>();
        for (TradeVo tradeVo : tradeVos) {
            TradeOpsReq tradeOpsReq = new TradeOpsReq();
            tradeOpsReq.setTradeId(tradeVo.getTrade().getId());
            tradeOpsReq.setActionType(actionType);
            tradeOpsReq.setServerUpdateTime(tradeVo.getTrade().getServerUpdateTime());
            list.add(tradeOpsReq);
        }
        tradeBatchOpsReq.setList(list);
        return tradeBatchOpsReq;
    }

    @Override
    public void refuse(TradeVo tradeVo, Reason reason, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void refuseBatch(ActionType actionType, Reason reason, List<TradeVo> tradeVos, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void acceptDinner(Trade trade, ResponseListener<TradeResp> listener) {
        acceptDinner(trade, true, listener);
    }

    @Override
    public void acceptDinner(Trade trade, boolean genBatchNo, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void refuseDinner(TradeVo tradeVo, Reason reason, ResponseListener<TradeResp> listener) {


    }

    @Override
    public void recision(TradeVo tradeVo, Reason reason, List<InventoryItemReq> returnInventoryItems, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void refund(TradeVo tradeVo, Reason reason, List<InventoryItemReq> returnInventoryItems, ResponseListener<TradePaymentResp> listener) {

    }

    @Override
    public long pending(TradeVo tradeVo)
            throws Exception {
        TradeDal dal = new TradeDalImpl(getImplContext());
        dal.insert(tradeVo);
        return dal.countPending();
    }

    @Override
    public void deliveredPayment(List<TradeVo> tradeVoList, ResponseListener<TradePaymentResp> listener) {

    }

    @Override
    public void clearAccounts(List<TradeVo> tradeVoList, ResponseListener<TradePaymentResp> listener) {

    }

    private List<Long> toClearAccountsReq(List<TradeVo> tradeVoList) {
        List<Long> tradeIds = new ArrayList<Long>();
        for (TradeVo tradeVo : tradeVoList) {
            if (tradeVo.getTrade() != null && tradeVo.getTrade().getId() != null) {
                tradeIds.add(tradeVo.getTrade().getId());
            }
        }
        return tradeIds;
    }

    @Override
    public void batchunbindCoupon(Long tradeId, List<TradePrivilege> tradePrivileges, ResponseListener<TradeResp> listener) {

    }

    private TradeBatchUnbindCouponReq toTradeBatchUnbindCouponReq(Long tradeId, List<TradePrivilege> tradePrivileges) {
        List<Long> tradePrivilegeIds = new ArrayList<Long>();

        int size = tradePrivileges.size();
        for (int i = 0; i < size; i++) {
            Long tradePrivilegeId = tradePrivileges.get(i).getId();
            tradePrivilegeIds.add(tradePrivilegeId);
        }

        TradeBatchUnbindCouponReq req = new TradeBatchUnbindCouponReq();
        req.setTradeId(tradeId);
        req.setTradePrivilegeIds(tradePrivilegeIds);
        return req;
    }

    @Override
    public void unbindCoupon(TradeUnbindCouponReq req, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void unbindIntegral(TradeUnbindCouponReq req, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void getSendCouponUrl(ResponseListener<CouponUrlResp> listener) {

    }

    @Override
    public void salesRetuenPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Reason reason, boolean isReturnInventory,
                               Map<String, String> memberPasswords, ResponseListener<TradePaymentResp> listener) {

    }

    @Override
    public void returnSellCards(Trade srcTrade, TradeVo tradeVo, List<PaymentVo> paymentVoList,
                                List<CardNumber> cardList, Reason reason, boolean returnAll, ResponseListener<SalesCardReturnResp> listener) {

    }

    @Override
    public void insertDinner(TradeVo tradeVo, IDinnertable dinnertable, ResponseListener<TradeResp> listener) {
        insertDinner(tradeVo, dinnertable, listener, false);
    }

    @Override
    public void insertDinner(final TradeVo tradeVo, IDinnertable dinnertable, ResponseListener<TradeResp> listener, boolean isAsync) {

    }

    @Override
    public void insertBuffet(TradeVo tradeVo, IDinnertable dinnertable, ResponseListener<TradeResp> listener, boolean isAsync) {

    }

    @Override
    public void modifyDinner(TradeVo tradeVo, ResponseListener<TradeResp> listener) {
        modifyDinner(tradeVo, listener, false);
    }

    @Override
    public void modifyDinner(TradeVo tradeVo, ResponseListener<TradeResp> listener, boolean isAsync) {
        modifyDinner(tradeVo, true, listener, isAsync);
    }

    @Override
    public void modifyDinner(TradeVo tradeVo, boolean isUpdate, ResponseListener<TradeResp> listener, boolean isAsync) {

    }

    @Override
    public void modifyBuffet(TradeVo tradeVo, ResponseListener<TradeResp> listener) {
        modifyBuffet(tradeVo, true, listener, false);
    }

    @Override
    public void modifyBuffet(TradeVo tradeVo, boolean isUpdate, ResponseListener<TradeResp> listener, boolean isAsync) {

    }

    @Override
    public void transferDinnertable(List<TradeItemExtraDinner> tradeItemSeats, IDinnertableTrade orginal, IDinnertable dest,
                                    ResponseListener<TransferDinnertableResp> listener) {

    }

    @Override
    public void recisionDinner(Long tradeId, Long serverUpdateTime, List<DinnertableState> states, Reason mReason,
                               List<InventoryItemReq> inventoryItems, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void recisionBeauty(Long tradeId, Long serverUpdateTime, List<DinnertableState> states, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradeResp> listener) {
        String url = BeautyServerAddressUtil.deleteTrade();
        BeautyTradeDeleteReq deleteReq = new BeautyTradeDeleteReq();
        RecisionDinnerReq req = new RecisionDinnerReq();
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            req.setUpdatorId(user.getId());
            req.setUpdatorName(user.getName());
        }
        req.setTradeId(tradeId);
        req.setServerUpdateTime(serverUpdateTime);
        req.setTables(states);
        if (reason != null) {
            req.setReasonId(reason.getId());
            req.setReasonContent(reason.getContent());
        }

        deleteReq.setObsoleteRequest(req);
        deleteReq.setReturnInventoryItems(inventoryItems);
        OpsRequest.Executor<BeautyTradeDeleteReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(deleteReq).responseClass(TradeResp.class).responseProcessor(new TradeRespProcessor()).execute(
                listener, "recisionBeauty");
    }

    @Override
    public void refundDinner(TradeVo tradeVo, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradePaymentResp> listener) {

    }

    @Override
    public void mergeDinner(final List<TradeItemExtraDinner> tradeItemSeats, final IDinnertableTrade orginal, final IDinnertableTrade dest, final Long levelId,
                            final ResponseListener<TradeResp> listener) {

    }

    @Override
    public void refundBeauty(TradeVo tradeVo, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradePaymentResp> listener) {
        String url = BeautyServerAddressUtil.refundTrade();
        tradeVo.getTrade().validateUpdate();
        TradeRefundReq req = toTradeRefundReq(tradeVo.getTrade(), reason);
        req.setReturnInventoryItems(inventoryItems);
        req.setReviseStock(true);
        OpsRequest.Executor<TradeRefundReq, TradePaymentResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradePaymentResp.class)
                .responseProcessor(new TradePaymentRespProcessor())
                .execute(listener, "beautyRefund");
    }

    private MergeDinnerReq queryMergeDinnerReq(List<TradeItemExtraDinner> tradeItemSeats, DatabaseHelper helper, IDinnertableTrade orginal, IDinnertableTrade dest, Long levelId)
            throws Exception {
        Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
        Dao<TradeItem, String> itemDao = helper.getDao(TradeItem.class);
        Dao<TradeItemProperty, String> tipDao = helper.getDao(TradeItemProperty.class);
        Dao<TradeReasonRel, Long> trrDao = helper.getDao(TradeReasonRel.class);

        String orginalUuid = orginal.getTradeUuid();
        Trade orginalTrade = tradeDao.queryForId(orginalUuid);
        String destUuid = dest.getTradeUuid();
        Trade destTrade = tradeDao.queryForId(dest.getTradeUuid());

        Map<String, TradeItemExtraDinner> mapTradeItemSeats = new HashMap<String, TradeItemExtraDinner>();
        if (Utils.isNotEmpty(tradeItemSeats)) {
            for (TradeItemExtraDinner tradeItemSeat : tradeItemSeats) {
                mapTradeItemSeats.put(tradeItemSeat.getTradeItemUuid(), tradeItemSeat);
            }
        }

        List<TradeItem> destItemList = itemDao.queryForEq(TradeItem.$.tradeUuid, destUuid);
        int maxSort = 0;
        for (TradeItem item : destItemList) {
            if (item.getSort() != null && item.getSort() > maxSort) {
                maxSort = item.getSort();
            }
        }
        maxSort++;

        Map<Long, String> tradeItemRelations = new HashMap<Long, String>();
                Map<String, String> itemUuidMap = new HashMap<String, String>();
        List<Long> itemIds = new ArrayList<Long>();
        List<TradeItem> itemList = itemDao.queryForEq(TradeItem.$.tradeUuid, orginalUuid);
        for (int i = 0; i < itemList.size(); i++) {
            TradeItem item = itemList.get(i);
            TradeItemExtraDinner seat = mapTradeItemSeats.get(item.getUuid());

                        item.setRelateTradeItemId(item.getId());
            item.setRelateTradeItemUuid(item.getUuid());

            String newUuid = SystemUtils.genOnlyIdentifier();
            if (item.getId() != null) {
                tradeItemRelations.put(item.getId(), newUuid);
            }
            itemIds.add(item.getId());
            itemUuidMap.put(item.getUuid(), newUuid);
            item.validateUpdate();
            item.setId(null);
            item.setUuid(newUuid);
            item.setTradeId(destTrade.getId());
            item.setTradeUuid(destTrade.getUuid());
            item.setTradeTableId(dest.getId());
            item.setTradeTableUuid(dest.getUuid());
            item.setSort(i + maxSort);
            item.setServerCreateTime(null);
            item.setServerUpdateTime(null);

            if (seat != null && seat.isValid()) {
                seat.setTradeItemId(item.getId());
                seat.setTradeItemUuid(item.getUuid());
                seat.setId(null);
                seat.setServerCreateTime(null);
                seat.setServerUpdateTime(null);
            }


        }

                for (TradeItem item : itemList) {
            if (!TextUtils.isEmpty(item.getParentUuid())) {
                item.setParentId(null);
                item.setParentUuid(itemUuidMap.get(item.getParentUuid()));
            }
                    }

                QueryBuilder<TradeItem, String> orginalSubQb = itemDao.queryBuilder();
        orginalSubQb.selectColumns(TradeItem.$.uuid).where().eq(TradeItem.$.tradeUuid, orginalUuid);
        List<TradeItemProperty> tipList =
                tipDao.queryBuilder().where().in(TradeItemProperty.$.tradeItemUuid, orginalSubQb).query();
        for (TradeItemProperty tip : tipList) {
            tip.validateUpdate();
            tip.setId(null);
            tip.setUuid(SystemUtils.genOnlyIdentifier());
            tip.setTradeItemId(null);
            tip.setTradeItemUuid(itemUuidMap.get(tip.getTradeItemUuid()));
            tip.setServerCreateTime(null);
            tip.setServerUpdateTime(null);
        }

                orginalSubQb = itemDao.queryBuilder();
        orginalSubQb.selectColumns(TradeItem.$.uuid)
                .where()
                .eq(TradeItem.$.tradeUuid, orginalUuid)
                .and()
                .eq(TradeItem.$.statusFlag, StatusFlag.INVALID)
                .and()
                .eq(TradeItem.$.invalidType, InvalidType.RETURN_QTY);
        List<TradeReasonRel> trrList = trrDao.queryBuilder()
                .where()
                .eq(TradeReasonRel.$.operateType, OperateType.ITEM_RETURN_QTY)
                .and()
                .in(TradeReasonRel.$.relateUuid, orginalSubQb)
                .query();
        for (TradeReasonRel trr : trrList) {
            trr.validateUpdate();
            trr.setId(null);
            trr.setRelateId(null);
            trr.setRelateUuid(itemUuidMap.get(trr.getRelateUuid()));
            trr.setServerCreateTime(null);
            trr.setServerUpdateTime(null);
        }

                orginalTrade.validateUpdate();
        orginalTrade.setStatusFlag(StatusFlag.INVALID);

        TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
        TradeVo destTradeVo = tradeDal.findTrade(destTrade);
        TradeCustomer tradeCustomer = CustomerManager.getInstance().getValidMemberOrCardCustomer(destTradeVo.getTradeCustomerList());
        if (tradeCustomer != null) {
            tradeCustomer.levelId = levelId;
        }
        TradeVo orginalTradeVo = tradeDal.findTrade(orginalTrade);
                List<IShopcartItem> originalShopcatItemList = DinnertableTradeInfo.buildShopcartItem(orginalTradeVo.getTradeItemList(), "", orginalTradeVo.getDeskCount());
        List<IShopcartItem> destShopcartItemList = DinnertableTradeInfo.buildShopcartItem(destTradeVo.getTradeItemList(), "", destTradeVo.getDeskCount());
        if (originalShopcatItemList != null) {
            for (IShopcartItem item : originalShopcatItemList) {
                if (item.getPrivilege() != null && item.getPrivilege().isValid()) {
                    item.getPrivilege().setStatusFlag(StatusFlag.INVALID);
                    item.getPrivilege().validateUpdate();
                }
                if (item.getCouponPrivilegeVo() != null && item.getCouponPrivilegeVo().isPrivilegeValid()) {
                    item.getCouponPrivilegeVo().getTradePrivilege().setStatusFlag(StatusFlag.INVALID);
                    item.getCouponPrivilegeVo().getTradePrivilege().validateUpdate();
                }
            }
        }
                if (destShopcartItemList == null) {
            destShopcartItemList = new ArrayList<IShopcartItem>();
        }
        CustomerResp customer = DinnerCashManager.getTradeVoCustomer(destTradeVo);
        boolean isDestHasBanquet = BaseShoppingCart.isHasValidBanquet(destTradeVo);
        if (originalShopcatItemList != null) {
            destShopcartItemList.addAll(originalShopcatItemList);
                        for (IShopcartItem iShopcartItem : originalShopcatItemList) {
                                if (customer != null && !isDestHasBanquet) {
                    DinnerShoppingCart.getInstance().setDishMemberPrivilege(destTradeVo, iShopcartItem, customer, false);
                }
            }
        }

                Dao<TradeTable, String> tradeTableDao = helper.getDao(TradeTable.class);
        TradeTable orginalTradeTable = tradeTableDao.queryForId(orginal.getUuid());
        TradeTable destTradeTable = tradeTableDao.queryForId(dest.getUuid());
        Integer orginalPeopleCount = orginalTradeTable.getTablePeopleCount();
        if (orginalPeopleCount == null) {
            orginalPeopleCount = 0;
        }
                Integer destCount = destTradeTable.getTablePeopleCount();
        if (destCount == null) {
            destCount = 0;
        }
        destTradeTable.setTablePeopleCount(destCount + orginalPeopleCount);
                Integer destSum = destTrade.getTradePeopleCount();
        if (destSum == null) {
            destSum = 0;
        }
        destSum = destSum + orginalPeopleCount;
        destTrade.setTradePeopleCount(destSum);
                destTradeVo.getTrade().setTradePeopleCount(destSum);
        destTrade.validateUpdate();
                MathShoppingCartTool.mathTotalPrice(destShopcartItemList, destTradeVo);
        BaseShoppingCart.CheckGiftCouponIsActived(destShopcartItemList, destTradeVo);
        Trade trade = destTradeVo.getTrade();
        destTrade.setSaleAmount(trade.getSaleAmount());
        destTrade.setTradeAmount(trade.getTradeAmount());

                MergeDinnerReq req = new MergeDinnerReq();
        req.setTrades(Arrays.asList(orginalTrade, destTrade));
        req.setTradeItems(itemList);
        req.setTradeItemProperties(tipList);
        req.setTradeItemExtraDinners(tradeItemSeats);
        if (destTradeVo.getTradePrivileges() != null) {
            req.setTradePrivileges(destTradeVo.getTradePrivileges());
        } else {
            req.setTradePrivileges(new ArrayList<TradePrivilege>());
        }

        List<TradePrivilege> tradePrivileges = req.getTradePrivileges();
        if (destShopcartItemList != null) {
            for (IShopcartItem item : destShopcartItemList) {
                TradePrivilege tradePrivilege = item.getPrivilege();
                if (tradePrivilege != null && tradePrivilege.isChanged()) {
                    if (tradePrivilege.getTradeId() == null) {
                        tradePrivilege.setTradeId(destTrade.getId());
                        tradePrivilege.setTradeUuid(destTrade.getUuid());
                                                tradePrivilege.setTradeItemUuid(itemUuidMap.get(item.getUuid()));
                    }
                    tradePrivileges.add(tradePrivilege);
                }
            }
        }

                if (Utils.isNotEmpty(destTradeVo.getCouponPrivilegeVoList())) {
            for (CouponPrivilegeVo couponPrivilegeVo : destTradeVo.getCouponPrivilegeVoList()) {
                TradePrivilege tp = couponPrivilegeVo.getTradePrivilege();
                if (tp.getId() != null || tp.getStatusFlag() == StatusFlag.VALID) {
                    tp.setTradeId(destTradeVo.getTrade().getId());
                    tradePrivileges.add(tp);
                }
            }
        }
                if (destTradeVo.getBanquetVo() != null && destTradeVo.getBanquetVo().getTradePrivilege() != null) {
            TradePrivilege tp = destTradeVo.getBanquetVo().getTradePrivilege();
            if (tp.getId() != null || tp.getStatusFlag() == StatusFlag.VALID) {
                tp.setTradeId(destTradeVo.getTrade().getId());
                tradePrivileges.add(tp);
            }
        }
        if (destTradeVo.getIntegralCashPrivilegeVo() != null) {
            TradePrivilege tp = destTradeVo.getIntegralCashPrivilegeVo().getTradePrivilege();
            if (tp.getId() != null || tp.getStatusFlag() == StatusFlag.VALID) {
                tp.setTradeId(destTradeVo.getTrade().getId());
                tradePrivileges.add(tp);
            }
        }
        List<WeiXinCouponsVo> listWXC = destTradeVo.getmWeiXinCouponsVo();
        if (listWXC != null) {
            for (WeiXinCouponsVo mWeiXinCouponsVo : listWXC) {
                TradePrivilege tp = mWeiXinCouponsVo.getmTradePrivilege();
                if (tp.getId() != null || mWeiXinCouponsVo.isActived()) {
                    tp.setTradeId(destTradeVo.getTrade().getId());
                    tradePrivileges.add(tp);
                }
            }

        }
        req.setTradeReasonRels(trrList);

        req.setTradeTables(Arrays.asList(destTradeTable));

                DinnertableState orgnalDinnertable = new DinnertableState();
        orgnalDinnertable.setId(orginal.getDinnertable().getId());
        orgnalDinnertable.setModifyDateTime(orginal.getDinnertable().getServerUpdateTime());
        if (orginal.getDinnertable().getTradeCount() == 1) {
            orgnalDinnertable.setTableStatus(TableStatus.EMPTY);
        } else {
            orgnalDinnertable.setTableStatus(orginal.getDinnertable().getTableStatus());
        }
        DinnertableState destDinnertable = new DinnertableState();
        destDinnertable.setId(dest.getDinnertable().getId());
        destDinnertable.setModifyDateTime(dest.getDinnertable().getServerUpdateTime());
        destDinnertable.setTableStatus(TableStatus.OCCUPIED);
        req.setTables(Arrays.asList(orgnalDinnertable, destDinnertable));

                Set<Long> tradeItemIds = tradeItemRelations.keySet();
        if (Utils.isNotEmpty(tradeItemIds)) {

        }
        AuthLogManager.getInstance().flush(OrderActionEnum.JOIN_TABLES, orginalTrade.getId(), orginalTrade.getUuid(), orginalTrade.getClientUpdateTime());
        return req;
    }



    @Override
    public void weixinRefund(TradeVo tradeVo, Reason reason, ResponseListener<TradePaymentResp> listener) {

    }

    @Override
    public void tradeSplitDinner(TradeVo source, TradeVo target, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void tradeSplitPayDinner(TradeVo source, TradeVo target, List<PaymentVo> paymentVoList,
                                    Map<String, String> memberPasswords, ResponseListener<PaymentResp> listener) {

    }

    @Override
    public void tradeRepay(TradeVo tradeVo, Reason reason, ResponseListener<PaymentResp> listener) {

    }

    @Override
    public void beautyTradeRepay(TradeRepayReq tradeRepayReq, ResponseListener<PaymentResp> listener) {
        String url = BeautyServerAddressUtil.repayTrade();
        OpsRequest.Executor<TradeRepayReq, PaymentResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(tradeRepayReq)
                .responseClass(PaymentResp.class)
                .responseProcessor(new PaymentRespProcessor())
                .interceptEnable(true)
                .execute(listener, "tradeRepayBeauty");
    }

    @Override
    public void modifyServiceDish(DishServiceReq dishServiceReq, ResponseListener<TradeItemResp> listener) {

    }

    @Override
    public void modifyServiceDishV2(DishServiceV2Req dishServiceReq, CalmResponseListener<ResponseObject<DishServiceV2Resp>> listener) {

    }

    @Override
    public void doCloseBill(CloseBillReq closeBillReq, ResponseListener<CloseBillResp> listener) {

    }


    @Override
    public void queryHandOverHistory(ResponseListener<ClosingHandOverResp> listener) {

    }

    @Override
    public void queryHandOverHistory(ClosingReq req, ResponseListener<ClosingHandOverResp> listener) {

    }

    @Override
    public void closeDetail(CloseDetailReq closeDetailReq, ResponseListener<CloseBillDataInfo> listener) {

    }

    @Override
    public void closeDetail(TransferReq transferReq, ResponseListener<TransferCloseBillData> listener) {

    }


    @Override
    public void queryCloseHistoryList(CloseHistoryReq closeHistoryReq, ResponseListener<CloseHistoryResp> listener) {

    }

    private TradeRepayReq toTradeRepayReq(TradeVo tradeVo, Reason reason) {
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


    public static TradeReq toTradeReq(TradeVo tradeVo) {
        return toTradeReq(tradeVo, false);
    }



    private AddItemReq toAddItemReq(AddItemVo addItemVo) {
        AddItemReq addItemReq = new AddItemReq();

        AuthUser loginUser = Session.getAuthUser();

        if (addItemVo.getmAddItemBatch() != null) {
            addItemReq.setBatchId(addItemVo.getmAddItemBatch().getId());
            addItemReq.setTableId(addItemVo.getmAddItemBatch().getTableId());
            addItemReq.setTradeId(addItemVo.getmAddItemBatch().getTradeId());
        }
        if (loginUser != null) {
            addItemReq.setUpdatorId(loginUser.getId());
            addItemReq.setUpdatorName(loginUser.getName());
        }
        if (Utils.isEmpty(addItemVo.getmAddItemRecords())) {
            return addItemReq;
        }

        List<AddItemRecord> listAddItems = new ArrayList<AddItemRecord>();
        for (AddItemRecord addItemRecord : addItemVo.getmAddItemRecords()) {
            AddItemRecord tmpRecord = new AddItemRecord();
            tmpRecord.setId(addItemRecord.getId());
            tmpRecord.setServerUpdateTime(addItemRecord.getServerUpdateTime());
            listAddItems.add(tmpRecord);
        }

        if (Utils.isNotEmpty(listAddItems)) {
            addItemReq.setWeixinAddItemInfo(listAddItems);
        }

        return addItemReq;
    }


    public static TradeReq toTradeReq(TradeVo tradeVo, boolean isFilterChangeAndPrint) {
        TradeReq req = new TradeReq();
        copyProperties(tradeVo.getTrade(), req);
        List<TradeItemReq> tradeItems = new ArrayList<TradeItemReq>();
        List<TradePrivilege> tradePrivileges = new ArrayList<TradePrivilege>();
        List<TradeItemProperty> tradeItemProperties = new ArrayList<TradeItemProperty>();
        List<TradeItemLog> tradeItemLogs = new ArrayList<TradeItemLog>();
        List<TradeReasonRel> tradeReasonRels = new ArrayList<TradeReasonRel>();
        List<TradeItemOperation> tradeItemOperations = new ArrayList<TradeItemOperation>();
        List<TradePlanActivity> tradePlanActivities = new ArrayList<TradePlanActivity>();
        List<TradeItemPlanActivity> tradeItemPlanActivities = new ArrayList<TradeItemPlanActivity>();
        List<TradeBuffetPeople> tradeBuffetPeoples = new ArrayList<TradeBuffetPeople>();
        List<TradeItemExtraDinner> tradeItemExtraDinners = new ArrayList<>();

        if (tradeVo.getRelatedId() != null) {
            req.setRelatedId(tradeVo.getRelatedId());
        }
        if (tradeVo.getRelatedType() != null) {
            req.setRelatedType(tradeVo.getRelatedType());
        }


        if (Utils.isNotEmpty(tradeVo.getTradeReasonRelList())) {
            tradeReasonRels.addAll(tradeVo.getTradeReasonRelList());
        }
                if (Utils.isNotEmpty(tradeVo.getTradePlanActivityList())) {
            tradePlanActivities.addAll(tradeVo.getTradePlanActivityList());
        }
        if (Utils.isNotEmpty(tradeVo.getTradeItemPlanActivityList())) {
            tradeItemPlanActivities.addAll(tradeVo.getTradeItemPlanActivityList());
        }

                if (Utils.isNotEmpty(tradeVo.getTradeCustomerList())) {
                        List<TradeCustomer> tradeCustomerList = new ArrayList<TradeCustomer>();
            TradeCustomer bookingTradeCustomer = null;
            TradeCustomer memberTradeCustomer = null;
            for (TradeCustomer tc : tradeVo.getTradeCustomerList()) {
                tradeCustomerList.add(tc);
                if (tc.isValid()) {
                    switch (tc.getCustomerType()) {
                        case MEMBER:
                            memberTradeCustomer = tc;
                            break;
                        case BOOKING:
                            bookingTradeCustomer = tc;
                            break;
                        default:
                            break;
                    }
                }
            }
            if (memberTradeCustomer != null && bookingTradeCustomer == null) {
                bookingTradeCustomer = new TradeCustomer();
                copyProperties(memberTradeCustomer, bookingTradeCustomer);
                bookingTradeCustomer.setId(null);
                bookingTradeCustomer.setServerUpdateTime(null);
                bookingTradeCustomer.setUuid(SystemUtils.genOnlyIdentifier());
                bookingTradeCustomer.setCustomerType(CustomerType.BOOKING);
                tradeCustomerList.add(bookingTradeCustomer);
            }

            req.setTradeCustomers(tradeCustomerList);
        }

                if (tradeVo.getTradePrivileges() != null) {
            for (TradePrivilege tp : tradeVo.getTradePrivileges()) {
                tp.setTradeId(tradeVo.getTrade().getId());
                tradePrivileges.add(tp);
            }
        }
                if (tradeVo.getCouponPrivilegeVoList() != null) {
            for (CouponPrivilegeVo couponPrivilegeVo : tradeVo.getCouponPrivilegeVoList()) {
                if (couponPrivilegeVo != null) {
                    TradePrivilege tp = couponPrivilegeVo.getTradePrivilege();
                    if (!TextUtils.isEmpty(tp.getUuid()) || tp.getStatusFlag() == StatusFlag.VALID) {
                        tp.setTradeId(tradeVo.getTrade().getId());
                    }
                    tradePrivileges.add(tp);
                }
            }
        }
                if (tradeVo.getBanquetVo() != null && tradeVo.getBanquetVo().getTradePrivilege() != null) {
            TradePrivilege tp = tradeVo.getBanquetVo().getTradePrivilege();
            if (tp.getId() != null || tp.getStatusFlag() == StatusFlag.VALID) {
                tp.setTradeId(tradeVo.getTrade().getId());
                tradePrivileges.add(tp);
            }
        }
        if (tradeVo.getIntegralCashPrivilegeVo() != null) {
            TradePrivilege tp = tradeVo.getIntegralCashPrivilegeVo().getTradePrivilege();
                                    if (!TextUtils.isEmpty(tp.getUuid()) || tp.getStatusFlag() == StatusFlag.VALID) {
                tp.setTradeId(tradeVo.getTrade().getId());
                tradePrivileges.add(tp);
            }
        }
        List<WeiXinCouponsVo> listWXC = tradeVo.getmWeiXinCouponsVo();
        if (listWXC != null) {
            for (WeiXinCouponsVo mWeiXinCouponsVo : listWXC) {
                TradePrivilege tp = mWeiXinCouponsVo.getmTradePrivilege();
                if (tp.getId() != null || mWeiXinCouponsVo.isActived()) {
                    tp.setTradeId(tradeVo.getTrade().getId());
                    tradePrivileges.add(tp);
                }
            }

        }

                if (tradeVo.getMealShellVo() != null) {
            if (tradeVo.getTradeItemList() == null) {
                tradeVo.setTradeItemList(new ArrayList<TradeItemVo>());
            }
            TradeItem shellTradeItem = tradeVo.getMealShellVo().getTradeItem();
            if (shellTradeItem != null && shellTradeItem.isChanged()) {
                TradeItemVo tradeItemVo = new TradeItemVo();
                tradeItemVo.setTradeItem(shellTradeItem);
                tradeVo.getTradeItemList().add(tradeItemVo);
            }
        }

        if (tradeVo.getTradeDeposit() != null) {
            tradeVo.getTradeDeposit().setTradeId(tradeVo.getTrade().getId());
            tradeVo.getTradeDeposit().setTradeUuid(tradeVo.getTrade().getUuid());
            tradeVo.getTradeDeposit().setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
            tradeVo.getTradeDeposit().setShopIdenty(BaseApplication.sInstance.getShopIdenty());
            tradeVo.getTradeDeposit().setUuid(SystemUtils.genOnlyIdentifier());
        }

                if (tradeVo.getTradeItemList() != null) {
            for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
                TradeItem tradeItem = tradeItemVo.getTradeItem();
                TradeItemReq tradeItemReq = new TradeItemReq();
                copyProperties(tradeItem, tradeItemReq);
                tradeItemReq.setCardSaleInfos(tradeItemVo.getCardSaleInfos());
                tradeItems.add(tradeItemReq);
                boolean invalid = (tradeItem.getStatusFlag() != StatusFlag.VALID);
                if (tradeItemVo.getTradeItemPrivilege() != null) {
                    TradePrivilege tp = tradeItemVo.getTradeItemPrivilege();
                                        if (tp.getStatusFlag() == StatusFlag.VALID || tp.getId() != null || !TextUtils.isEmpty(tp.getUuid())) {
                                                if (invalid && tp.getStatusFlag() != StatusFlag.INVALID) {
                            tp.setStatusFlag(StatusFlag.INVALID);
                            tp.setChanged(true);
                        }

                        tp.setTradeId(tradeVo.getTrade().getId());
                        tradePrivileges.add(tp);
                    }
                }
                if (tradeItemVo.getTradeItemPropertyList() != null) {
                    List<TradeItemProperty> itemProperties = tradeItemVo.getTradeItemPropertyList();
                    for (TradeItemProperty ip : itemProperties) {
                                                if (invalid && ip.getStatusFlag() != StatusFlag.INVALID) {
                            ip.setStatusFlag(StatusFlag.INVALID);
                            ip.setChanged(true);
                        }
                    }
                    tradeItemProperties.addAll(itemProperties);
                }
                List<TradeReasonRel> reasonRelList = tradeItemVo.getReasonRelList();
                if (reasonRelList != null) {
                    tradeReasonRels.addAll(reasonRelList);
                }
                if (tradeItemVo.getTradeItemOperations() != null && !tradeItemVo.getTradeItemOperations().isEmpty()) {
                    for (TradeItemOperation tradeItemOperation : tradeItemVo.getTradeItemOperations()) {
                        tradeItemOperation.setTradeItemUuid(tradeItem.getUuid());
                        tradeItemOperations.add(tradeItemOperation);
                    }
                }

                                if (tradeItemVo.getCouponPrivilegeVo() != null && tradeItemVo.getCouponPrivilegeVo().getTradePrivilege() != null) {
                    TradePrivilege tp = tradeItemVo.getCouponPrivilegeVo().getTradePrivilege();
                    if (tradeItemVo.getCouponPrivilegeVo().isActived()
                            || tp.getId() != null) {
                                                if (invalid && tp.getStatusFlag() != StatusFlag.INVALID) {
                            tp.setStatusFlag(StatusFlag.INVALID);
                            tp.setChanged(true);
                        }
                        tp.setTradeId(tradeVo.getTrade().getId());
                        tp.setTradeUuid(tradeVo.getTrade().getUuid());
                        tradePrivileges.add(tp);
                    }

                }

                if (tradeItemVo.getTradeItemExtraDinner() != null)
                    tradeItemExtraDinners.add(tradeItemVo.getTradeItemExtraDinner());
            }
        }

                if (Utils.isNotEmpty(tradeVo.getTradeBuffetPeoples())) {
            for (int i = tradeVo.getTradeBuffetPeoples().size() - 1; i >= 0; i--) {
                TradeBuffetPeople tradeBuffetPeople = tradeVo.getTradeBuffetPeoples().get(i);
                if (tradeBuffetPeople.isChanged()) {
                    tradeBuffetPeople.setTradeId(req.getId());
                    tradeBuffetPeople.setTradeUuid(req.getUuid());
                    tradeBuffetPeoples.add(tradeBuffetPeople);
                }
            }
        }
        req.setTradeBuffetPeoples(tradeBuffetPeoples);


        TradeTable tradeTable = Utils.isNotEmpty(tradeVo.getTradeTableList()) ? tradeVo.getTradeTableList().get(0) : null;

                if (isFilterChangeAndPrint) {
            for (int i = tradeItems.size() - 1; i >= 0; i--) {
                                if (!tradeItems.get(i).isChanged()) {
                    tradeItems.remove(i);
                } else {
                    if (tradeTable != null) {
                        tradeItems.get(i).setTradeTableId(tradeTable.getId());
                        tradeItems.get(i).setTradeTableUuid(tradeTable.getUuid());
                    }
                }
            }
            for (int i = tradeItemProperties.size() - 1; i >= 0; i--) {
                if (!tradeItemProperties.get(i).isChanged()) {
                    tradeItemProperties.remove(i);
                }
            }
            for (int i = tradeReasonRels.size() - 1; i >= 0; i--) {
                if (!tradeReasonRels.get(i).isChanged()) {
                    tradeReasonRels.remove(i);
                }
            }

            for (int i = tradePlanActivities.size() - 1; i >= 0; i--) {
                if (!tradePlanActivities.get(i).isChanged()) {
                    tradePlanActivities.remove(i);
                }
            }
            for (int i = tradeItemPlanActivities.size() - 1; i >= 0; i--) {
                TradeItemPlanActivity tradeItemPlanActivity = tradeItemPlanActivities.get(i);
                if (!tradeItemPlanActivity.isChanged()) {
                    tradeItemPlanActivities.remove(i);
                } else if (tradeItemPlanActivity.getTradeUuid() == null) {
                                        tradeItemPlanActivity.setTradeUuid(tradeVo.getTrade().getUuid());
                }
            }


        }
        req.setTradeTables(tradeVo.getTradeTableList());
        req.setTradeItems(tradeItems);
        if (!tradeItemProperties.isEmpty()) {
            req.setTradeItemProperties(tradeItemProperties);
        }
        if (!tradeReasonRels.isEmpty()) {
            req.setTradeReasonRels(tradeReasonRels);
        }
        if (!tradePrivileges.isEmpty()) {
            req.setTradePrivileges(tradePrivileges);
        }
        if (!tradeItemLogs.isEmpty()) {
            req.setTradeItemLogs(tradeItemLogs);
        }
        if (!tradeItemOperations.isEmpty()) {
            req.setTradeItemOperations(tradeItemOperations);
        }
        List<TradeExtra> tradeExtraList = new ArrayList<TradeExtra>();
        if (tradeVo.getTradeExtra() != null) {
            if (isFilterChangeAndPrint) {                if (tradeVo.getTradeExtra().isChanged()) {
                    tradeExtraList.add(tradeVo.getTradeExtra());
                    req.setTradeExtra(tradeVo.getTradeExtra());
                }
            } else {
                tradeExtraList.add(tradeVo.getTradeExtra());
                req.setTradeExtra(tradeVo.getTradeExtra());
            }
        }
        req.setTradeExtras(tradeExtraList);
        if (!tradePlanActivities.isEmpty()) {
            req.setTradePlanActivitys(tradePlanActivities);
        }
        if (!tradeItemPlanActivities.isEmpty()) {
            req.setTradeItemPlanActivitys(tradeItemPlanActivities);
        }
                if (tradeVo.getTrade().getBusinessType() == BusinessType.BUFFET) {
            if (!tradeVo.isPaidTradeposit()) {                req.setTradeDeposit(tradeVo.getTradeDeposit());
            }
        } else {
            req.setTradeDeposit(tradeVo.getTradeDeposit());
        }
        req.setTradeBuffetPeoples(tradeVo.getTradeBuffetPeoples());
                if (tradeVo.getTradeItemExtraList() != null) {
            req.setTradeItemExtras(tradeVo.getTradeItemExtraList());
        }
        req.setTradeGroup(tradeVo.getTradeGroup());                 if (isFilterChangeAndPrint) {
            if (tradeVo.getTradeUser() != null && tradeVo.getTradeUser().isChanged())
                req.setTradeUser(tradeVo.getTradeUser());
        } else {
            req.setTradeUser(tradeVo.getTradeUser());
        }
        req.setTradeUsers(tradeVo.getTradeUsers());
        req.setTradeItemExtraDinners(tradeItemExtraDinners);

        if (Utils.isNotEmpty(tradeVo.getTradeTaxs())) {
            req.setTradeTaxs(tradeVo.getTradeTaxs());
        }

        return req;
    }


    private PaymentReq toAdjustPaymentReq(PaymentVo paymentVo) {
        PaymentReq req = new PaymentReq();
        List<PaymentTo> paymentToList = new ArrayList<PaymentTo>();
        PaymentTo to = new PaymentTo();
        copyProperties(paymentVo.getPayment(), to);
        List<PaymentItemTo> itemList = new ArrayList<PaymentItemTo>();
        for (PaymentItem paymentItem : paymentVo.getPaymentItemList()) {
            PaymentItemTo itemTo = new PaymentItemTo();
            copyProperties(paymentItem, itemTo);
            itemList.add(itemTo);
        }
        to.setPaymentItems(itemList);
        paymentToList.add(to);
        req.setPaymentType(PaymentType.TRADE_SELL);
        if (paymentVo.getPayment().getRelateId() != null) {
            req.setRelateId(paymentVo.getPayment().getRelateId());
            req.setRelateUuid(paymentVo.getPayment().getRelateUuid());
        }
        req.setPayments(paymentToList);
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            req.setUpdatorId(user.getId());
            req.setUpdatorName(user.getName());
        }
        return req;
    }


    private PaymentReq toPaymentReq(TradeVo tradeVo, List<PaymentVo> paymentVoList,
                                    Map<String, String> memberPasswords) {
        PaymentReq req = new PaymentReq();
        List<PaymentTo> paymentToList = new ArrayList<PaymentTo>();
        PaymentType paymentType = null;        for (PaymentVo vo : paymentVoList) {
            if (paymentType == null) {
                paymentType = vo.getPayment().getPaymentType();
            }
            PaymentTo to = new PaymentTo();
            copyProperties(vo.getPayment(), to);
            List<PaymentItemTo> itemList = new ArrayList<PaymentItemTo>();
            for (PaymentItem paymentItem : vo.getPaymentItemList()) {
                PaymentItemTo itemTo = new PaymentItemTo();
                copyProperties(paymentItem, itemTo);
                if (memberPasswords != null) {
                    itemTo.setPassword(memberPasswords.get(itemTo.getUuid()));
                }
                itemList.add(itemTo);
            }
            to.setPaymentCards(vo.getPaymentCards());                        to.setPaymentItems(itemList);
            paymentToList.add(to);
        }
        if (paymentType == null) {
            paymentType = PaymentType.TRADE_SELL;
        }
        req.setPaymentType(paymentType);
        req.setRelateId(tradeVo.getTrade().getId());
        req.setRelateUuid(tradeVo.getTrade().getUuid());
        req.setServerUpdateTime(tradeVo.getTrade().getServerUpdateTime());
        req.setPayments(paymentToList);
        req.setTradePayForm(1L);
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            req.setUpdatorId(user.getId());
            req.setUpdatorName(user.getName());
        }
        return req;
    }

    private TradePaymentReq toTradePaymentReq(TradeVo tradeVo, List<PaymentVo> paymentVoList,
                                              Map<String, String> memberPasswords) {
        TradePaymentReq req = new TradePaymentReq();
        req.setTrade(toTradeReq(tradeVo));
        req.setPayment(toPaymentReq(tradeVo, paymentVoList, memberPasswords));
        return req;
    }

    private PaymentOfRepeatReq toPaymentOfRepeatReq(TradeVo tradeVoRef, PayDinnerReq payDinnerReq) {
        PaymentOfRepeatReq req = new PaymentOfRepeatReq();
        req.setActual(payDinnerReq);
        if (tradeVoRef.getTradeItemList() != null) {
                        List<TradeItemVo> itemVos = new ArrayList<TradeItemVo>();
            for (TradeItemVo itemVo : tradeVoRef.getTradeItemList()) {
                TradeItem tradeItem = itemVo.getTradeItem();
                if (tradeItem.getStatusFlag() == StatusFlag.INVALID && tradeItem.isChanged()) {
                    itemVos.add(itemVo);
                }
            }
            if (!itemVos.isEmpty()) {
                Trade tradeRef = tradeVoRef.getTrade();

                                Trade tradeSell = new Trade();
                tradeSell.validateCreate();
                tradeSell.validateUpdate();
                tradeSell.setActionType(tradeRef.getActionType());
                tradeSell.setBizDate(tradeRef.getBizDate());
                tradeSell.setBusinessType(tradeRef.getBusinessType());
                tradeSell.setDeliveryType(DeliveryType.HERE);
                tradeSell.setDomainType(tradeRef.getDomainType());
                tradeSell.setRelateTradeId(tradeRef.getRelateTradeId());
                tradeSell.setRelateTradeUuid(tradeRef.getRelateTradeUuid());
                tradeSell.setServerCreateTime(null);
                tradeSell.setServerUpdateTime(null);
                tradeSell.setSource(tradeRef.getSource());
                tradeSell.setSourceChild(tradeRef.getSourceChild());
                tradeSell.setTradeNo(tradeRef.getTradeNo());
                tradeSell.setTradePayForm(TradePayForm.OFFLINE);
                tradeSell.setTradeType(TradeType.SELL_FOR_REVERSAL);
                tradeSell.setTradePayStatus(TradePayStatus.PAID);
                tradeSell.setTradeStatus(TradeStatus.RETURNED);
                tradeSell.setTradeTime(System.currentTimeMillis());
                tradeSell.setUuid(SystemUtils.genOnlyIdentifier());
                tradeSell.setSaleAmount(BigDecimal.ZERO);
                tradeSell.setPrivilegeAmount(BigDecimal.ZERO);
                tradeSell.setTradeAmount(BigDecimal.ZERO);
                tradeSell.setTradePeopleCount(0);
                tradeSell.setDishKindCount(0);

                TradeVo tradeVoSell = new TradeVo();
                tradeVoSell.setTrade(tradeSell);
                tradeVoSell.setTradeItemList(new ArrayList<TradeItemVo>());

                                Trade tradeRefund = new Trade();
                copyProperties(tradeSell, tradeRefund);
                tradeRefund.setUuid(SystemUtils.genOnlyIdentifier());
                tradeRefund.setTradeType(TradeType.REFUND_FOR_REVERSAL);
                tradeRefund.setTradeStatus(TradeStatus.FINISH);
                tradeRefund.setTradePayStatus(TradePayStatus.REFUNDED);

                TradeVo tradeVoRefund = new TradeVo();
                tradeVoRefund.setTrade(tradeRefund);
                tradeVoRefund.setTradeItemList(new ArrayList<TradeItemVo>());

                                Set<String> skuUuids = new HashSet<String>();
                Map<String, String> sellItemUuidMap = new HashMap<String, String>();
                Map<String, String> refundItemUuidMap = new HashMap<String, String>();
                for (TradeItemVo itemVoRef : itemVos) {
                    TradeItem itemRef = itemVoRef.getTradeItem();

                                        TradeItem itemSell = new TradeItem();
                    copyProperties(itemRef, itemSell);
                    itemSell.validateCreate();                    itemSell.setId(null);
                    itemSell.setUuid(SystemUtils.genOnlyIdentifier());
                    itemSell.setTradeId(null);
                    itemSell.setTradeUuid(tradeSell.getUuid());
                    itemSell.setParentId(null);
                    itemSell.setServerCreateTime(null);
                    itemSell.setServerUpdateTime(null);
                    itemSell.setRelateTradeItemId(null);
                    itemSell.setRelateTradeItemUuid(null);
                    itemSell.setTradeTableId(null);
                    itemSell.setTradeTableUuid(null);
                    TradeItemVo itemVoSell = new TradeItemVo();
                    itemVoSell.setTradeItem(itemSell);
                    if (itemVoRef.getTradeItemPropertyList() != null) {
                        itemVoSell.setTradeItemPropertyList(new ArrayList<TradeItemProperty>());
                        for (TradeItemProperty ipRef : itemVoRef.getTradeItemPropertyList()) {
                            TradeItemProperty ip = new TradeItemProperty();
                            copyProperties(ipRef, ip);
                            ip.validateCreate();                            ip.setId(null);
                            ip.setUuid(SystemUtils.genOnlyIdentifier());
                            ip.setTradeItemId(null);
                            ip.setTradeItemUuid(itemSell.getUuid());
                            ip.setServerCreateTime(null);
                            ip.setServerUpdateTime(null);
                            itemVoSell.getTradeItemPropertyList().add(ip);
                        }
                    }
                    tradeVoSell.getTradeItemList().add(itemVoSell);
                                        if ((itemSell.getType() == DishType.SINGLE && TextUtils.isEmpty(itemSell.getParentUuid()))
                            || itemSell.getType() == DishType.COMBO) {
                        tradeSell.setSaleAmount(MathDecimal.add(tradeSell.getSaleAmount(), itemSell.getActualAmount()));
                    }

                                        TradeItemVo itemVoRefund = itemVoSell.clone();
                    TradeItem itemRefund = itemVoRefund.getTradeItem();
                    itemRefund.setUuid(SystemUtils.genOnlyIdentifier());
                    itemRefund.setTradeUuid(tradeRefund.getUuid());
                    itemRefund.setQuantity(MathDecimal.negate(itemRefund.getQuantity()));
                    itemRefund.setAmount(MathDecimal.negate(itemRefund.getAmount()));
                    itemRefund.setPropertyAmount(MathDecimal.negate(itemRefund.getPropertyAmount()));
                    itemRefund.setFeedsAmount(MathDecimal.negate(itemRefund.getFeedsAmount()));
                    itemRefund.setActualAmount(MathDecimal.negate(itemRefund.getActualAmount()));
                    if (itemVoRefund.getTradeItemPropertyList() != null) {
                        for (TradeItemProperty ip : itemVoRefund.getTradeItemPropertyList()) {
                            ip.setUuid(SystemUtils.genOnlyIdentifier());
                            ip.setTradeItemUuid(itemRefund.getUuid());
                            ip.setQuantity(MathDecimal.negate(ip.getQuantity()));
                            ip.setAmount(MathDecimal.negate(ip.getAmount()));
                        }
                    }
                    tradeVoRefund.getTradeItemList().add(itemVoRefund);

                    skuUuids.add(itemRef.getSkuUuid());
                                        sellItemUuidMap.put(itemRef.getUuid(), itemSell.getUuid());
                    refundItemUuidMap.put(itemRef.getUuid(), itemRefund.getUuid());
                }
                                for (TradeItemVo itemVo : tradeVoSell.getTradeItemList()) {
                    TradeItem item = itemVo.getTradeItem();
                    item.setParentUuid(sellItemUuidMap.get(item.getParentUuid()));
                }
                for (TradeItemVo itemVo : tradeVoRefund.getTradeItemList()) {
                    TradeItem item = itemVo.getTradeItem();
                    item.setParentUuid(refundItemUuidMap.get(item.getParentUuid()));
                }

                                tradeSell.setTradeAmount(tradeSell.getSaleAmount());
                tradeSell.setDishKindCount(skuUuids.size());
                tradeRefund.setSaleAmount(MathDecimal.negate(tradeSell.getSaleAmount()));
                tradeRefund.setTradeAmount(MathDecimal.negate(tradeSell.getTradeAmount()));
                tradeRefund.setDishKindCount(tradeSell.getDishKindCount());

                String payModeName = PaySettingCache.getPayModeNameByModeId(PayModeId.CASH.value());
                                Payment paymentSell = new Payment();
                paymentSell.validateCreate();
                paymentSell.validateUpdate();
                paymentSell.setUuid(SystemUtils.genOnlyIdentifier());
                paymentSell.setBizDate(tradeSell.getBizDate());
                paymentSell.setIsPaid(Bool.YES);
                paymentSell.setPaymentTime(System.currentTimeMillis());
                paymentSell.setPaymentType(PaymentType.TRADE_SELL);
                paymentSell.setRelateUuid(tradeSell.getUuid());
                paymentSell.setReceivableAmount(tradeSell.getTradeAmount());
                paymentSell.setExemptAmount(BigDecimal.ZERO);
                paymentSell.setActualAmount(paymentSell.getReceivableAmount());
                PaymentItem paymentItemSell = new PaymentItem();
                paymentItemSell.validateCreate();
                paymentItemSell.setUuid(SystemUtils.genOnlyIdentifier());
                paymentItemSell.setPaymentUuid(paymentSell.getUuid());
                paymentItemSell.setPayModeId(PayModeId.CASH.value());
                paymentItemSell.setPayModelGroup(PayModelGroup.CASH);
                paymentItemSell.setPayModeName(payModeName);
                paymentItemSell.setPayStatus(TradePayStatus.PAID);
                paymentItemSell.setFaceAmount(paymentSell.getActualAmount());
                paymentItemSell.setUsefulAmount(paymentItemSell.getFaceAmount());
                paymentItemSell.setChangeAmount(BigDecimal.ZERO);
                PaymentVo paymentVoSell = new PaymentVo();
                paymentVoSell.setPayment(paymentSell);
                paymentVoSell.setPaymentItemList(Arrays.asList(paymentItemSell));

                Payment paymentRefund = new Payment();
                copyProperties(paymentSell, paymentRefund);
                paymentRefund.setUuid(SystemUtils.genOnlyIdentifier());
                paymentRefund.setPaymentType(PaymentType.TRADE_REFUND);
                paymentRefund.setRelateUuid(tradeRefund.getUuid());
                paymentRefund.setReceivableAmount(tradeRefund.getTradeAmount());
                paymentRefund.setExemptAmount(BigDecimal.ZERO);
                paymentRefund.setActualAmount(paymentRefund.getReceivableAmount());
                PaymentItem paymentItemRefund = new PaymentItem();
                copyProperties(paymentItemSell, paymentItemRefund);
                paymentItemRefund.setUuid(SystemUtils.genOnlyIdentifier());
                paymentItemRefund.setPaymentUuid(paymentRefund.getUuid());
                paymentItemRefund.setPayStatus(TradePayStatus.REFUNDED);
                paymentItemRefund.setFaceAmount(paymentRefund.getActualAmount());
                paymentItemRefund.setUsefulAmount(paymentItemRefund.getFaceAmount());
                paymentItemRefund.setChangeAmount(BigDecimal.ZERO);
                PaymentVo paymentVoRefund = new PaymentVo();
                paymentVoRefund.setPayment(paymentRefund);
                paymentVoRefund.setPaymentItemList(Arrays.asList(paymentItemRefund));

                req.setRepay4Sell(toTradePaymentReq(tradeVoSell, Arrays.asList(paymentVoSell), null));
                req.setRepay4Refund(toTradePaymentReq(tradeVoRefund, Arrays.asList(paymentVoRefund), null));
            }
        }
        return req;
    }


    private PayDinnerReq toPayDinnerReq(TradeVo tradeVo, List<PaymentVo> paymentVoList,
                                        Map<String, String> memberPasswords) {
        PayDinnerReq payDinnerReq = new PayDinnerReq();
        TradeReq tradeReq = toTradeReq(tradeVo, true);
        Long tradeId = null;
        if (tradeVo.getTrade() != null) {
            tradeId = tradeVo.getTrade().getId();
        }
        List<TradeItemReq> tradeItems = tradeReq.getTradeItems();
        if (tradeItems != null) {
            for (TradeItemReq item : tradeItems) {
                if (item.getTradeId() == null) {
                    item.setTradeId(tradeId);
                }
            }
        }
        List<TradeCustomer> tradeCustomers = tradeReq.getTradeCustomers();
        if (tradeCustomers != null) {
            for (TradeCustomer tradeCustomer : tradeCustomers) {
                if (tradeCustomer.getTradeId() == null) {
                    tradeCustomer.setTradeId(tradeId);
                }
            }
        }
        List<TradePrivilege> privilegeList = tradeReq.getTradePrivileges();
        if (privilegeList != null) {
            for (TradePrivilege privilege : privilegeList) {
                if (privilege.getTradeId() == null) {
                    privilege.setTradeId(tradeId);
                }
            }
        }

        payDinnerReq.setTrade(tradeReq);
        PaymentReq paymentReq = toPaymentReq(tradeVo, paymentVoList, memberPasswords);
        PaymentDinnerTo pTo = new PaymentDinnerTo();
        pTo.setPayments(paymentReq.getPayments());
        pTo.setPaymentTime(new Date().getTime());
        pTo.setPaymentType(paymentReq.getPaymentType());
        pTo.setRelateUuid(paymentReq.getRelateUuid());
        pTo.setUpdatorId(tradeReq.getUpdatorId());
        pTo.setUpdatorName(tradeReq.getUpdatorName());
        payDinnerReq.setPayment(pTo);

        return payDinnerReq;
    }


    private @Nullable
    TradeOpsReq.TradeTableRequest createTradeTableRequest(@NonNull TradeVo tradeVo) {
        if (Utils.isEmpty(tradeVo.getTradeTableList())) {
            return null;
        }
        TradeTable tradeTable = tradeVo.getTradeTableList().get(0);
        TradeOpsReq.TradeTableRequest tradeTableRequest = new TradeOpsReq.TradeTableRequest();
        tradeTableRequest.creatorId = Session.getAuthUser().getId();
        tradeTableRequest.creatorName = Session.getAuthUser().getName();
        tradeTableRequest.memo = "无";
        tradeTableRequest.tableId = tradeTable.getTableId();
        tradeTableRequest.tableName = tradeTable.getTableName();
        tradeTableRequest.tablePeopleCount = tradeTable.getTablePeopleCount();
        tradeTableRequest.waiterId = Session.getAuthUser().getId();
        tradeTableRequest.waiterName = Session.getAuthUser().getName();
        tradeTableRequest.tradeId = tradeVo.getTrade().getId();
        tradeTableRequest.tradeUuid = tradeVo.getTrade().getUuid();
        tradeTableRequest.updatorId = Session.getAuthUser().getId();
        tradeTableRequest.updatorName = Session.getAuthUser().getName();
        tradeTableRequest.brandIdenty = BaseApplication.getInstance().getBrandIdenty();
        tradeTableRequest.deviceIdenty = BaseApplication.getInstance().getDeviceIdenty();
        tradeTableRequest.clientCreateTime = System.currentTimeMillis();
        tradeTableRequest.clientUpdateTime = System.currentTimeMillis();
        tradeTableRequest.serverCreateTime = System.currentTimeMillis();
        tradeTableRequest.serverUpdateTime = System.currentTimeMillis();
        tradeTableRequest.shopIdenty = BaseApplication.getInstance().getShopIdenty();
        tradeTableRequest.uuid = SystemUtils.genOnlyIdentifier();
        tradeTableRequest.changed = false;
        return tradeTableRequest;
    }

    private TradeOpsReq toTradeOpsReq(TradeVo tradeVo, TradeStatus tradeStatus, Reason reason) {
        TradeOpsReq req = toTradeOpsReq(tradeVo.getTrade(), tradeStatus, reason);
        TradeOpsReq.TradeTableRequest tradeTableRequest = createTradeTableRequest(tradeVo);
        if (tradeTableRequest != null) {
            req.setTradeTableRequest(tradeTableRequest);
        }
        return req;
    }


    private TradeOpsReq toTradeOpsReq(Trade trade, TradeStatus tradeStatus, Reason reason) {
        TradeOpsReq req = new TradeOpsReq();
        req.setTradeId(trade.getId());
        req.setTradeUuid(trade.getUuid());
        req.setTradeStatus(tradeStatus);
        req.setClientCreateTime(trade.getClientCreateTime());
        req.setClientUpdateTime(System.currentTimeMillis());
        req.setServerUpdateTime(trade.getServerUpdateTime());

        AuthUser user = Session.getAuthUser();
        if (user != null) {
            req.setUpdatorId(user.getId());
            req.setUpdatorName(user.getName());
        }
        if (trade.getActionType() == null) {
            req.setActionType(ActionType.PASSIVE);
        } else {
            req.setActionType(trade.getActionType());
        }
        if (reason != null) {
            req.setReasonId(reason.getId());
            req.setReasonContent(reason.getContent());
            req.setRefuseCode(reason.getContentCode());
        }
        return req;
    }


    private TradeRefundReq toTradeRefundReq(Trade relateTrade, Reason reason) {
        TradeRefundReq req = new TradeRefundReq();
        req.setTradeId(relateTrade.getId());
        req.setTradeUuid(relateTrade.getUuid());

        AuthUser user = Session.getAuthUser();
        if (user != null) {
            req.setOperateId(user.getId());
            req.setOperateName(user.getName());
        }

        if (reason != null) {
            req.setReasonId(reason.getId());
            req.setReasonContent(reason.getContent());
            req.setCancelCode(reason.getContentCode());
        }
        return req;
    }


    private DeliveredPaymentReq toDeliveredPaymentReq(List<TradeVo> tradeVoList) {
        DeliveredPaymentReq deliveredPaymentReq = new DeliveredPaymentReq();
        List<PaymentReq> deliveredPayments = new ArrayList<PaymentReq>();
        deliveredPaymentReq.setDeliveredPayments(deliveredPayments);
        for (TradeVo tradeVo : tradeVoList) {
            DeliveredPayment req = new DeliveredPayment();
            List<PaymentTo> paymentToList = new ArrayList<PaymentTo>();
            if (tradeVo.getTrade().getTradePayStatus() == TradePayStatus.PAID) {                req.setDeliveredStatus(1);
            } else {                req.setDeliveredStatus(0);
                PaymentTo paymenTo = new PaymentTo();
                paymenTo.validateCreate();
                Trade trade = tradeVo.getTrade();
                paymenTo.setReceivableAmount(trade.getTradeAmount());                paymenTo.setActualAmount(trade.getTradeAmount());                paymenTo.setExemptAmount(BigDecimal.valueOf(0));                paymenTo.setRelateUuid(trade.getUuid());                paymenTo.setRelateId(trade.getId());                paymenTo.setPaymentType(PaymentType.TRADE_SELL);                paymenTo.setUuid(SystemUtils.genOnlyIdentifier());
                paymenTo.setCreatorId(trade.getCreatorId());                paymenTo.setCreatorName(trade.getCreatorName());                paymenTo.setUpdatorId(trade.getCreatorId());                paymenTo.setUpdatorName(trade.getCreatorName());                                List<PaymentItemTo> paymentItemList = new ArrayList<PaymentItemTo>();
                PaymentItemTo paymentItem = new PaymentItemTo();
                paymentItem.validateCreate();
                paymentItem.setUuid(SystemUtils.genOnlyIdentifier());
                paymentItem.setPaymentUuid(paymenTo.getUuid());
                paymentItem.setPayModeId(PayModeId.CASH.value());
                paymentItem.setPayModelGroup(PayModelGroup.CASH);
                paymentItem.setFaceAmount(trade.getTradeAmount());                paymentItem.setChangeAmount(BigDecimal.valueOf(0));                paymentItem.setUsefulAmount(trade.getTradeAmount());                paymentItem.setCreatorId(trade.getCreatorId());                paymentItem.setCreatorName(trade.getCreatorName());                paymentItemList.add(paymentItem);
                paymenTo.setPaymentItems(paymentItemList);
                paymentToList.add(paymenTo);
            }
            req.setPayments(paymentToList);

            req.setPaymentType(PaymentType.TRADE_SELL);
            req.setRelateId(tradeVo.getTrade().getId());
            req.setRelateUuid(tradeVo.getTrade().getUuid());
            req.setServerUpdateTime(tradeVo.getTrade().getServerUpdateTime());
            AuthUser user = Session.getAuthUser();
            if (user != null) {
                req.setUpdatorId(user.getId());
                req.setUpdatorName(user.getName());
            }
            deliveredPayments.add(req);
        }

        return deliveredPaymentReq;
    }

    private TradeOpsReq toRefundToTradeOpsReq(Trade trade, TradeStatus tradeStatus, Reason reason) {
        TradeOpsReq req = new TradeOpsReq();
        req.setTradeId(trade.getId());
        req.setClientUpdateTime(System.currentTimeMillis());
        req.setServerUpdateTime(trade.getServerUpdateTime());
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            req.setUpdatorId(user.getId());
            req.setUpdatorName(user.getName());
        }
        if (reason != null) {
            req.setReasonId(reason.getId());
            req.setReasonContent(reason.getContent());
        }
        return req;
    }

    public static void saveTradeResp(DatabaseHelper helper, TradeResp resp)
            throws Exception {
        DBHelperManager.saveEntities(helper, Trade.class, resp.getTrade());
        DBHelperManager.saveEntities(helper, TradeExtra.class, resp.getTradeExtras());
        DBHelperManager.saveEntities(helper, TradeExtra.class, resp.getTradeExtra());
        DBHelperManager.saveEntities(helper, TradePrivilege.class, resp.getTradePrivileges());
        DBHelperManager.saveEntities(helper, TradeCustomer.class, resp.getTradeCustomers());
        DBHelperManager.saveEntities(helper, TradeTable.class, resp.getTradeTables());
        DBHelperManager.saveEntities(helper, TradeItem.class, resp.getTradeItems());
        DBHelperManager.saveEntities(helper, TradeItemProperty.class, resp.getTradeItemPropertys());
        DBHelperManager.saveEntities(helper, TradeStatusLog.class, resp.getTradeStatusLogs());
        DBHelperManager.saveEntities(helper, TradeItemLog.class, resp.getTradeItemLogs());
        DBHelperManager.saveEntities(helper, Tables.class, resp.getTables());
        DBHelperManager.saveEntities(helper, TradeReasonRel.class, resp.getTradeReasonRels());
        DBHelperManager.saveEntities(helper, DishShop.class, resp.getDishShops());
        DBHelperManager.saveEntities(helper, TradeItemOperation.class, resp.getTradeItemOperations());
        DBHelperManager.saveEntities(helper, PrintOperation.class, resp.getPrintOperations());
        DBHelperManager.saveEntities(helper, TradeDeposit.class, resp.getTradeDeposit());
        DBHelperManager.saveEntities(helper, TradeDeposit.class, resp.getTradeDeposits());
        DBHelperManager.saveEntities(helper, TradePlanActivity.class, resp.getTradePlanActivitys());
        DBHelperManager.saveEntities(helper, TradeItemPlanActivity.class, resp.getTradeItemPlanActivitys());
        DBHelperManager.saveEntities(helper, TradeItemExtra.class, resp.getTradeItemExtras());
        DBHelperManager.saveEntities(helper, TradePromotion.class, resp.getTradePromotions());        DBHelperManager.saveEntities(helper, Trade.class, resp.getTrades());
        DBHelperManager.saveEntities(helper, TradeReceiveLog.class, resp.getTradeReceiveLogs());
        DBHelperManager.saveEntities(helper, TradeBuffetPeople.class, resp.getTradeBuffetPeoples());
        DBHelperManager.saveEntities(helper, TradeBuffetPeople.class, resp.getBuffetPeoples());
        DBHelperManager.saveEntities(helper, TradeGroupInfo.class, resp.getTradeGroup());         DBHelperManager.saveEntities(helper, TradeUser.class, resp.getTradeUser());         DBHelperManager.saveEntities(helper, TradeUser.class, resp.getTradeUsers());         DBHelperManager.saveEntities(helper, TradeMainSubRelation.class, resp.getTradeMainSubRelations());         DBHelperManager.saveEntities(helper, TradeItemMainBatchRel.class, resp.getTradeItemMainBatchRels());         DBHelperManager.saveEntities(helper, TradeItemMainBatchRelExtra.class, resp.getTradeItemMainBatchRelExtras());
        DBHelperManager.saveEntities(helper, TradeItemExtraDinner.class, resp.getTradeItemExtraDinners());
        DBHelperManager.saveEntities(helper, TradeTax.class, resp.getTradeTaxs());
        DBHelperManager.saveEntities(helper, TradeInitConfig.class, resp.getTradeInitConfigs());
    }


    private static class TradeRespProcessor extends SaveDatabaseResponseProcessor<TradeResp> {

        private final boolean mGenSn;

        TradeRespProcessor() {
            this(false);
        }

        TradeRespProcessor(boolean genSn) {
            this.mGenSn = genSn;
        }

        @Override
        public ResponseObject<TradeResp> process(final ResponseObject<TradeResp> response) {
            if (isSuccessful(response) && mGenSn) {
                                            }
            return super.process(response);
        }

        @Override
        public void saveToDatabase(TradeResp resp)
                throws Exception {
            super.saveToDatabase(resp);
                    }

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final TradeResp resp) {
            return new Callable<Void>() {

                @Override
                public Void call()
                        throws Exception {
                    saveTradeResp(helper, resp);
                    return null;
                }
            };
        }

    }


    private static class PaymentRespProcessor extends SaveDatabaseResponseProcessor<PaymentResp> {

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final PaymentResp resp) {
            return new Callable<Void>() {

                @Override
                public Void call()
                        throws Exception {
                    saveTradeResp(helper, resp);
                    DBHelperManager.saveEntities(helper, Payment.class, resp.getPayments());
                    DBHelperManager.saveEntities(helper, PaymentItem.class, resp.getPaymentItems());
                    DBHelperManager.saveEntities(helper, PaymentItemExtra.class, resp.getPaymentItemExtras());
                    DBHelperManager.saveEntities(helper, PaymentItemUnionpay.class, resp.getPaymentItemUnionpays());
                    DBHelperManager.saveEntities(helper, PaymentDevice.class, resp.getPaymentDevices());
                    DBHelperManager.saveEntities(helper, PaymentItemGroupon.class, resp.getPaymentItemGroupons());                    DBHelperManager.saveEntities(helper, TradeDeposit.class, resp.getTradeDeposit());                    DBHelperManager.saveEntities(helper, TradeItemExtraDinner.class, resp.getTradeItemExtraDinners());                    DBHelperManager.saveEntities(helper, TradeUser.class, resp.getTradeItemUsers());                    DBHelperManager.saveEntities(helper, TradePrivilegeLimitNumCard.class, resp.getTradePrivilegeLimitNumCards());                    DBHelperManager.saveEntities(helper, TradePrivilegeApplet.class, resp.getTradePrivilegeApplets());
                    return null;

                }
            };
        }

    }


    private static class TradePaymentRespProcessor extends SaveDatabaseResponseProcessor<TradePaymentResp> {

        private final boolean mGenSn;

        TradePaymentRespProcessor() {
            this(false);
        }

        TradePaymentRespProcessor(boolean genSn) {
            this.mGenSn = genSn;
        }

        @Override
        public ResponseObject<TradePaymentResp> process(final ResponseObject<TradePaymentResp> response) {
            if (isSuccessful(response) && mGenSn) {
                                            }
            return super.process(response);
        }

        @Override
        public void saveToDatabase(TradePaymentResp resp)
                throws Exception {
            super.saveToDatabase(resp);
                    }

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final TradePaymentResp resp) {
            return new Callable<Void>() {

                @Override
                public Void call()
                        throws Exception {
                    saveTradeResp(helper, resp);
                    DBHelperManager.saveEntities(helper, Payment.class, resp.getPayments());
                    DBHelperManager.saveEntities(helper, PaymentItem.class, resp.getPaymentItems());
                    DBHelperManager.saveEntities(helper, PaymentItemExtra.class, resp.getPaymentItemExtras());
                    DBHelperManager.saveEntities(helper, PaymentItemUnionpay.class, resp.getPaymentItemUnionpays());
                    DBHelperManager.saveEntities(helper, PaymentDevice.class, resp.getPaymentDevices());
                                       DBHelperManager.saveEntities(helper, PaymentItemGroupon.class, resp.getPaymentItemGroupons());                    return null;
                }
            };
        }

    }


    public static void saveVerifyPayResp(VerifyPayResp resp) {
        try {
            new VerifyPayRespProcessor().saveToDatabase(resp);
        } catch (Exception e) {
                        e.printStackTrace();
        }
    }

    public static PaymentRespProcessor getPaymentRespProcessor() {

        return new PaymentRespProcessor();
    }

    public static TradeRespProcessor getTradeRespProcessor() {

        return new TradeRespProcessor();
    }


    private static class VerifyPayRespProcessor extends SaveDatabaseResponseProcessor<VerifyPayResp> {

        private final TradePaymentRespProcessor mProcessor;

        VerifyPayRespProcessor() {
            mProcessor = new TradePaymentRespProcessor();
        }

        @Override
        public void saveToDatabase(VerifyPayResp resp)
                throws Exception {
                                     if (resp.isPaid()) {
                super.saveToDatabase(resp);
            }
        }

        @Override
        protected Callable<Void> getCallable(DatabaseHelper helper, VerifyPayResp resp) {

                        return mProcessor.getCallable(helper, resp);
        }

    }

    private static class AnonymousCardStoreRespProcessor extends SaveDatabaseResponseProcessor<AnonymousCardStoreResp> {

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final AnonymousCardStoreResp resp) throws Exception {

            return new Callable<Void>() {

                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, PrintOperation.class, resp.getPrintOperations());
                    DBHelperManager.saveEntities(helper, Trade.class, resp.getTrade());
                    DBHelperManager.saveEntities(helper, Payment.class, resp.getPayment());
                    DBHelperManager.saveEntities(helper, PaymentItem.class, resp.getPaymentItems());
                    return null;
                }
            };
                    }
    }


    private static class TransfertableRespProcessor extends SaveDatabaseResponseProcessor<TransferDinnertableResp> {

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final TransferDinnertableResp resp) {
            return new Callable<Void>() {

                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, Tables.class, resp.getTables());
                    DBHelperManager.saveEntities(helper, TradeTable.class, resp.getTradeTables());
                    DBHelperManager.saveEntities(helper, TradeItemExtraDinner.class, resp.getTradeItemExtraDinners());
                    return null;
                }
            };
        }

    }

    private static class ChangeTradeItemPrintStatusProcessor extends SaveDatabaseResponseProcessor<TradeItemResp> {

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final TradeItemResp resp) {
            return new Callable<Void>() {

                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, TradeItem.class, resp.getTradeItems());
                    DBHelperManager.saveEntities(helper, TradeExtra.class, resp.getTradeExtras());
                    return null;
                }
            };
        }
    }


    private static class TradeItemRespProcessor extends SaveDatabaseResponseProcessor<TradeItemResp> {

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final TradeItemResp resp) {
            return new Callable<Void>() {

                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, Trade.class, resp.getTrades());
                    DBHelperManager.saveEntities(helper, TradeItem.class, resp.getTradeItems());
                    DBHelperManager.saveEntities(helper, PrintOperation.class, resp.getPrintOperations());
                    DBHelperManager.saveEntities(helper, TradeItemOperation.class, resp.getTradeItemOperations());
                    DBHelperManager.saveEntities(helper, TradeItemProperty.class, resp.getTradeItemProperties());
                    DBHelperManager.saveEntities(helper, TradeExtra.class, resp.getTradeExtras());
                    DBHelperManager.saveEntities(helper, TradeItemMainBatchRelExtra.class, resp.getTradeItemMainBatchRelExtras());
                    DBHelperManager.saveEntities(helper, TradeItemMainBatchRel.class, resp.getTradeItemMainBatchRels());
                    return null;
                }
            };
        }

    }


    private static class TradePayStateResqProcessor extends SaveDatabaseResponseProcessor<TradePayStateResp> {


        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper,
                                             final TradePayStateResp resp) {
            return new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    DBHelperManager.saveEntities(helper, Trade.class, resp.getTrades());
                    DBHelperManager.saveEntities(helper, Payment.class, resp.getPayments());
                    DBHelperManager.saveEntities(helper, PaymentItem.class, resp.getPaymentItems());
                    DBHelperManager.saveEntities(helper, PaymentItemExtra.class, resp.getPaymentItemExtras());

                    return null;
                }
            };
        }

    }

    @Override
    public void queryPaymentItem(String uuid, ResponseListener<PaymentItem> listener) {

    }

    @Override
    public void changeRefundStatus(RefundStatusReq req, ResponseListener<RefundStatusResp> listener) {

    }

    private static class UnionpayRefundStatusRespProcessor extends SaveDatabaseResponseProcessor<RefundStatusResp> {

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final RefundStatusResp resp) {
            return new Callable<Void>() {

                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, Trade.class, resp.getTrades());
                    DBHelperManager.saveEntities(helper, Payment.class, resp.getPayments());
                    DBHelperManager.saveEntities(helper, PaymentItem.class, resp.getPaymentItems());
                    DBHelperManager.saveEntities(helper, PaymentItemUnionpay.class, resp.getPaymentItemUnionpays());
                    return null;
                }
            };
        }

    }



    private static class PrintOperationProcessor extends SaveDatabaseResponseProcessor<PrintOperation> {

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final PrintOperation printOperation) {
            return new Callable<Void>() {

                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, PrintOperation.class, printOperation);
                    return null;
                }
            };
        }

    }

    @Override
    public void returnConfirm(Long tradeId, TradeReturnInfoReturnStatus returnStatus, boolean isReturnInventory, String requestUuid, Reason reason,
                              ResponseListener<Object> listener) {

    }

    private ReturnConfirmReq toReturnConfirmReq(Long tradeId, TradeReturnInfoReturnStatus returnStatus,
                                                String requestUuid, Reason reason) {
        ReturnConfirmReq req = new ReturnConfirmReq();
        req.setTradeId(tradeId);
        req.setReturnStatus(returnStatus);
        req.setRequestUuid(requestUuid);
        if (reason != null) {
            req.setReason(reason.getContent());
            req.setCancelCode(reason.getContentCode());
        }
                String operatorName = "";
        long operatorId = 0;
        if (Session.getAuthUser() != null) {
            operatorName = Session.getAuthUser().getName();
            operatorId = Session.getAuthUser().getId();
        }
        req.setOperatorId(operatorId);
        req.setOperatorName(operatorName);

        return req;
    }

    @Override
    public void remindDish(Trade trade, List<Long> tradeItemIds, ResponseListener<TradeItemResp> listener) {

    }

    @Override
    public void riseDish(Trade trade, List<Long> tradeItemIds, ResponseListener<TradeItemResp> listener) {

    }

    private void tradeItemOperation(String url, Trade trade, List<Long> tradeItemIds,
                                    ResponseListener<TradeItemResp> listener, String tag) {
        String deviceIdenty = SystemUtils.getMacAddress();
        AuthUser user = Session.getAuthUser();
        List<InnerTradeItem> innerTradeItems = new ArrayList<InnerTradeItem>();
        for (Long tradeItemId : tradeItemIds) {
            if (tradeItemId != null) {
                innerTradeItems.add(
                        new InnerTradeItem(tradeItemId, PrintStatus.PRINTING, deviceIdenty, user.getId(), user.getName()));
            }
        }
        TradeItemOperationReq req =
                new TradeItemOperationReq(trade.getId(), trade.getServerUpdateTime(), innerTradeItems);
        OpsRequest.Executor<TradeItemOperationReq, TradeItemResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new TradeItemRespProcessor())
                .responseClass(TradeItemResp.class)
                .execute(listener, tag);
    }

    @Override
    public void operationDish(Long tradeId, PrintOperationOpType opType, List<DishDataItem> selectedItems, ResponseListener<TradeItemResp> listener) {

    }

    @Override
    public void refreshState(Long tradeId, Long tradeItemId, ResponseListener<TradePayStateResp> listener) {
        TradePayStateReq payStateReq = new TradePayStateReq();
        payStateReq.setTradeId(tradeId);
        payStateReq.setPaymentItemId(tradeItemId);
        String url = ServerAddressUtil.getInstance().refreshStateUrl();
        OpsRequest.Executor<TradePayStateReq, TradePayStateResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(payStateReq)
                .responseClass(TradePayStateResp.class)
                .responseProcessor(new TradePayStateResqProcessor())
                .execute(listener, "refreshState");
    }

    @Override
    public void refreshReturnState(TradeVo tradeVo, ResponseListener<TradePayStateResp> listener) {

    }

    @Override
    public void dinnerSetTableAndAccept(TradeVo tradeVo, Tables table, Bool genBatchNo, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void modifyPrintStatus(List<TradeItem> tradeItems, List<PrintOperation> printOperations,
                                  List<TradeItemOperation> tradeItemOperations, ResponseListener<TradeItemResp> listener) {

    }

    @Override
    public void modifyMainTradePrintStatus(Long mainTradeId, List<TradeItem> tradeItems, List<PrintOperation> printOperations,
                                           List<TradeItemOperation> tradeItemOperations, ResponseListener<TradeItemResp> listener) {

    }

    @Override
    public void modifySubTradePrintStatus(Long mainTradeId, Long subTradeId, List<TradeItem> tradeItems, List<PrintOperation> printOperations,
                                          List<TradeItemOperation> tradeItemOperations, ResponseListener<TradeItemResp> listener) {

    }

    @Override
    public void tradeFinish(Long tradeId, ResponseListener<TradeFinishResp> listener) {

    }

    @Override
    public void buffetDepositRefund(Long tradeId, BigDecimal depositRefund, Long paymentItemId, Reason reason, ResponseListener<PayResp> listener) {

    }


    @Override
    public void buffetFinishAndDepositRefund(Long tradeId, BigDecimal depositRefund, Long paymentItemId, Reason reason, ResponseListener<PayResp> listener) {

    }

    private BuffetDepositRefundReq toBuffetDepositRefundReq(Long tradeId, BigDecimal depositRefund, Long paymentItemId, Reason reason) {
        BuffetDepositRefundReq refundReq = new BuffetDepositRefundReq();
                String operatorName = "";
        long operatorId = 0;
        if (Session.getAuthUser() != null) {
            operatorName = Session.getAuthUser().getName();
            operatorId = Session.getAuthUser().getId();
        }
        refundReq.setOperateId(operatorId);
        refundReq.setOperateName(operatorName);
        refundReq.setPaymentItemId(paymentItemId);
        refundReq.setTradeId(tradeId);
        refundReq.setRefundFee(depositRefund);
        if (reason != null) {
            refundReq.setReasonId(reason.getId());
            refundReq.setReasonContent(reason.getContent());
        }
        return refundReq;
    }

    @Override
    public void depositRefund(Long tradeId, BigDecimal depositRefund, Long payModeId, Reason reason,
                              ResponseListener<DepositRefundResp> listener) {

    }

    private DepositRefundReq toDepositRefundReq(Long tradeId, BigDecimal depositRefund, Long payModeId, Reason reason) {
        DepositRefundReq req = new DepositRefundReq(tradeId, depositRefund);
        req.setPayModeId(payModeId);
        req.setPayModeName(PaySettingCache.getPayModeNameByModeId(payModeId));
        if (reason != null) {
            req.setReasonId(reason.getId());
            req.setReasonContent(reason.getContent());
        }
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            req.setUpdatorId(user.getId());
            req.setUpdatorName(user.getName());
        }
        req.setClientUpdateTime(System.currentTimeMillis());

        return req;
    }

    private class DepositRefundProcessor extends SaveDatabaseResponseProcessor<DepositRefundResp> {
        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final DepositRefundResp resp) {
            return new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, Trade.class, resp.getTrades());
                    DBHelperManager.saveEntities(helper, TradeDeposit.class, resp.getTradeDeposit());
                    DBHelperManager.saveEntities(helper, Payment.class, resp.getPayments());
                    DBHelperManager.saveEntities(helper, PaymentItem.class, resp.getPaymentItems());
                    DBHelperManager.saveEntities(helper, Tables.class, resp.getTables());
                    DBHelperManager.saveEntities(helper, TradeReasonRel.class, resp.getTradeReasonRels());
                    DBHelperManager.saveEntities(helper, TradeStatusLog.class, resp.getTradeStatusLogs());
                    return null;
                }
            };
        }
    }

    private class TradeFinishRespProcessor extends SaveDatabaseResponseProcessor<TradeFinishResp> {

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final TradeFinishResp resp) {
            return new Callable<Void>() {

                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, Tables.class, resp.getTables());
                    DBHelperManager.saveEntities(helper, Trade.class, resp.getTrades());
                    DBHelperManager.saveEntities(helper, TradeStatusLog.class, resp.getTradeStatusLogs());
                    DBHelperManager.saveEntities(helper, TradeTable.class, resp.getTradeTables());
                    return null;
                }
            };
        }

    }


    private class ReturnDepositRespProcessor extends SaveDatabaseResponseProcessor<PayResp> {

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final PayResp resp) {
            return new Callable<Void>() {

                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, Trade.class, resp.getTrades());
                    DBHelperManager.saveEntities(helper, TradeStatusLog.class, resp.getTradeStatusLogs());
                    DBHelperManager.saveEntities(helper, Tables.class, resp.getTables());
                    DBHelperManager.saveEntities(helper, TradeTable.class, resp.getTradeTables());
                    DBHelperManager.saveEntities(helper, Payment.class, resp.getPayments());
                    DBHelperManager.saveEntities(helper, PaymentItem.class, resp.getPaymentItems());
                    DBHelperManager.saveEntities(helper, TradeDepositPayRelation.class, resp.getTradeDepositPayRelations());
                    DBHelperManager.saveEntities(helper, TradeDeposit.class, resp.getTradeDeposit());
                    return null;
                }
            };
        }

    }

    @Override
    public void moveDish(TradeVo sourceTradeVo, TradeVo targetTradeVo, List<TradeItem> tradeItems,
                         ResponseListener<TradeResp> listener, Integer actionType, Integer moveAdd) {

    }

    @Override
    public void doLag(TradeVo tradeVo, LagReq lagReq, ResponseListener<LagResp> listener) {

    }



    private static class LagRespProcessor extends SaveDatabaseResponseProcessor<LagResp> {

        LagRespProcessor() {
        }

        @Override
        public ResponseObject<LagResp> process(final ResponseObject<LagResp> response) {
            return super.process(response);
        }

        protected boolean isSuccessful(ResponseObject<LagResp> response) {
            return response.getContent().getModifyResponse() != null ? true : false;
        }

        @Override
        public void saveToDatabase(LagResp resp)
                throws Exception {
            super.saveToDatabase(resp);
        }

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final LagResp resp) {
            return new Callable<Void>() {

                @Override
                public Void call()
                        throws Exception {
                    saveTradeResp(helper, resp.getModifyResponse());
                    DBHelperManager.saveEntities(helper, TradeCreditLog.class, resp.getModifyResponse().getTradeCreditLogs());
                    return null;
                }
            };
        }

    }

    @Override
    public void getGoodsSaleRank(GoodsSellRankPrintReq req, ResponseListener<GoodsSellRankResp> listener) {

    }

    @Override
    public void getGoodsSaleRankPrint(TransferReq<GoodsSellRankPrintReq> req, ResponseListener<MindTransferResp<GoodsSellRankPrintResp>> listener) {

    }

    @Override
    public void getBusinessCharge(TransferReq<BusinessChargeReq> req, ResponseListener<MindTransferResp<BusinessChargeResp>> listener) {

    }

    @Override
    public void batchUploadAuthLog(List<AuthorizedLog> authorizedLogList, ResponseListener<AuthLogResp> listener) {


    }

    @Override
    public void acceptAddItem(TradeVo tradeVo, AddItemVo addItemVo, boolean isAcceptAutoTransferOpen, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void refuseAddItem(AddItemVo addItemVo, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void sendOrder(DispatchOrderReq req, ResponseListener<TradeResp> listener) {

    }


    @Override
    public void cancelDeliveryOrder(CancelDeliveryOrderReq req, ResponseListener<GatewayTransferResp<CancelDeliveryOrderResp>> listener) {

    }

    private static class CancelDeliveryOrderRespProcessor extends SaveDatabaseResponseProcessor<GatewayTransferResp<CancelDeliveryOrderResp>> {

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final GatewayTransferResp<CancelDeliveryOrderResp> resp) throws Exception {
            return new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    DBHelperManager.saveEntities(helper, DeliveryOrder.class, resp.getResult().getDeliveryOrder());
                    return null;
                }
            };
        }
    }

    @Override
    public void exChangeTable(TableTradeVo oldTableTradeVo, Tables newTable, ResponseListener<ExChangeTableResp> listener) {

    }


    private static class ExChangeableRespProcessor extends SaveDatabaseResponseProcessor<ExChangeTableResp> {

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final ExChangeTableResp resp) {
            return new Callable<Void>() {

                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, Tables.class, resp.getTables());
                    DBHelperManager.saveEntities(helper, TradeTable.class, resp.getTradeTables());
                    return null;
                }
            };
        }

    }

    @Override
    public void invoiceQrcode(InvoiceQrcodeReq req, ResponseListener<GatewayTransferResp<InvoiceQrcodeResp>> listener) {

    }

    @Override
    public void invoiceFHQrcode(FHInvoiceQrcodeReq req, ResponseListener<GatewayTransferResp<FHInvoiceQrcodeResp>> listener) {

    }


    @Override
    public void queryBalanceFH(FHQueryBalanceReq req, ResponseListener<GatewayTransferResp<FHQueryBalanceResp>> listener) {

    }


    private static class InvoiceQrcodeRespProcessor extends SaveDatabaseResponseProcessor<GatewayTransferResp<InvoiceQrcodeResp>> {

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final GatewayTransferResp<InvoiceQrcodeResp> resp) {
            return new Callable<Void>() {

                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, Invoice.class, resp.getResult().getInvoice());
                    return null;
                }
            };
        }

    }

    @Override
    public void invoiceRevoke(InvoiceRevokeReq req, ResponseListener<GatewayTransferResp<Invoice>> listener) {

    }

    @Override
    public void queryDeliveryFee(QueryDeliveryFeeReq req, ResponseListener<GatewayTransferResp<QueryDeliveryFeeResp>> listener) {

    }

    @Override
    public void batchQueryDeliveryFee(BatchQueryDeliveryFeeReq req, ResponseListener<GatewayTransferResp<JsonArray>> listener) {

    }

    @Override
    public void deliveryOrderDispatch(DeliveryOrderDispatchReq req, ResponseListener<GatewayTransferResp<DeliveryOrderDispatchResp>> listener) {

    }

    @Override
    public void deliveryOrderList(DeliveryOrderListReq req, ResponseListener<GatewayTransferResp<DeliveryOrderListResp>> listener) {

    }


    private static class DeliveryOrderListRespProcessor extends SaveDatabaseResponseProcessor<GatewayTransferResp<DeliveryOrderListResp>> {

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final GatewayTransferResp<DeliveryOrderListResp> resp) {
            return new Callable<Void>() {

                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, DeliveryOrder.class, resp.getResult().getOrders());
                    DBHelperManager.saveEntities(helper, DeliveryOrderRecord.class, resp.getResult().getOrderRecords());
                    return null;
                }
            };
        }

    }

    @Override
    public void refundCheck(Long tradeId, Long paymentItemId, SimpleResponseListener<RefundCheckResp> listener) {

    }

    @Override
    public void refundPayment(Long tradeId, Long paymentItemId, SimpleResponseListener<PayResp> listener) {
        String url = ServerAddressUtil.getInstance().getRefundPayment(paymentItemId);
        OpsRequest.Executor<Void, PayResp> executor = OpsRequest.Executor.create(url);
        executor.responseProcessor(new PayRespProcessor())
                .responseClass(PayResp.class)
                                .execute(listener, "refundPayment");
    }

    @Override
    public void refundSubmit(Long tradeId, Long paymentItemId, BigDecimal refundFee, SimpleResponseListener<RefundSubmitResp> listener) {

    }

    @Override
    public void takeDish(TradeExtra tradeExtra, FragmentActivity activity, CalmResponseListener<ResponseObject<TakeDishResp>> listener) {

    }

    @Override
    public void insertBuffetNoTable(TradeVo tradeVo, AuthUser waiter, ResponseListener<BuffetNoTableTradeResp> listener) {

    }

    @Override
    public void usePrivilege(Trade trade, BusinessType businessType, Long customerId, String entityCardNo, Collection<TradePrivilege> tradePrivileges, QSResponseListener<UsePrivilegeResp> listener) {

    }

    private UsePrivilegeReq toUsePrivilegeReq(Trade trade, BusinessType businessType, Long customerId, String entityCardNo, Collection<TradePrivilege> tradePrivileges) {
        String tradeUuid = trade.getUuid();
        UsePrivilegeReq req = new UsePrivilegeReq();

        UsePrivilegeReq.BaseInfo baseInfo = req.new BaseInfo();
        AuthUser authUser = Session.getAuthUser();
        if (authUser != null) {
            baseInfo.setOperateId(authUser.getId());
            baseInfo.setOperateName(authUser.getName());
        }
        baseInfo.setTradeUuid(tradeUuid);
        SystemSettingDal dal = OperatesFactory.create(SystemSettingDal.class);
        baseInfo.setBizDate(dal.getCurrentBizDate().getTime());
        baseInfo.setBizType(businessType);

                List<UsePrivilegeReq.IntegralInfo> integralInfos = new ArrayList<UsePrivilegeReq.IntegralInfo>();
                List<UsePrivilegeReq.PromoInfo> promoInfos = new ArrayList<UsePrivilegeReq.PromoInfo>();
                List<UsePrivilegeReq.WeixinCardInfo> weixinCardInfos = new ArrayList<UsePrivilegeReq.WeixinCardInfo>();
        if (tradePrivileges != null) {
            for (TradePrivilege tradePrivilege : tradePrivileges) {
                                if (tradePrivilege.getPrivilegeType() == PrivilegeType.INTEGRALCASH) {
                    UsePrivilegeReq.IntegralInfo integralInfo = req.new IntegralInfo();
                    integralInfo.setCustomerId(customerId);
                    integralInfo.setEntityCardNo(entityCardNo);
                    integralInfo.setPrivilegeType(tradePrivilege.getPrivilegeType());
                    integralInfo.setPrivilegeAmount(tradePrivilege.getPrivilegeAmount());
                    integralInfo.setPrivilegeValue(tradePrivilege.getPrivilegeValue());
                    integralInfo.setUuid(tradePrivilege.getUuid());
                    integralInfos.add(integralInfo);
                }
                                if (tradePrivilege.getPrivilegeType() == PrivilegeType.COUPON) {
                    UsePrivilegeReq.PromoInfo promoInfo = req.new PromoInfo();
                    promoInfo.setCustomerId(customerId);
                    promoInfo.setPrivilegeType(tradePrivilege.getPrivilegeType());
                    promoInfo.setPrivilegeAmount(tradePrivilege.getPrivilegeAmount());
                    promoInfo.setPromoId(tradePrivilege.getPromoId());
                    promoInfo.setUuid(tradePrivilege.getUuid());
                    promoInfos.add(promoInfo);
                }
                                if (tradePrivilege.getPrivilegeType() == PrivilegeType.WECHAT_CARD_COUPONS) {
                    UsePrivilegeReq.WeixinCardInfo weixinCardInfo = req.new WeixinCardInfo();
                    weixinCardInfo.setPrivilegeType(tradePrivilege.getPrivilegeType());
                    weixinCardInfo.setPrivilegeAmount(tradePrivilege.getPrivilegeAmount());
                    weixinCardInfo.setPromoId(tradePrivilege.getPromoId());
                    weixinCardInfo.setUuid(tradePrivilege.getUuid());
                    weixinCardInfo.setTradeAmount(trade.getTradeAmount());
                    weixinCardInfos.add(weixinCardInfo);
                }
            }
        }

        req.setBase(baseInfo);
        req.setIntegrals(integralInfos);
        req.setPromos(promoInfos);
        req.setWeixinCards(weixinCardInfos);
        return req;
    }

    @Override
    public void requestAddFee(Long deliveryOrderId, Integer deliveryPlatform, Double amount, ResponseListener<GatewayTransferResp<AddFeeResp>> listener) {

    }

    @Override
    public void pay4QS(UsePayReq req, QSResponseListener<UsePayResp> listener) {

    }

    private static class UsePayRespProcessor extends QSOpsRequest.SaveDatabaseResponseProcessor<UsePayResp> {

        @Override
        protected Callable<Void> getCallable(DatabaseHelper helper, UsePayResp resp) throws Exception {
            List<UsePayResp.PaymentItemResult> paymentItemResults = resp.getPaymentItemResults();
            for (UsePayResp.PaymentItemResult paymentItemResult : paymentItemResults) {
                Dao<PaymentItem, String> dao = helper.getDao(PaymentItem.class);
                PaymentItem paymentItem = dao.queryForId(paymentItemResult.getPaymentItemUuid());
                if (paymentItem != null) {
                                        PayModeId payModeId = ValueEnums.toEnum(PayModeId.class, paymentItem.getPayModeId());
                    if (payModeId == PayModeId.ANONYMOUS_ENTITY_CARD
                            || payModeId == PayModeId.ENTITY_CARD
                            || payModeId == PayModeId.MEMBER_CARD) {
                        if (paymentItemResult.getResultStatus() == 1000) {
                            paymentItem.setPayStatus(TradePayStatus.PAID);
                            paymentItem.setServerUpdateTime(Standard.Time.currentTime());
                        } else {
                            paymentItem.setPayStatus(TradePayStatus.UNPAID);
                            paymentItem.setServerUpdateTime(Standard.Time.currentTime());
                        }
                        DBHelperManager.saveEntities(helper, PaymentItem.class, true, paymentItem);
                    } else if (payModeId == PayModeId.BAINUO_TUANGOU
                            || payModeId == PayModeId.MEITUAN_TUANGOU) {
                        UsePayResp.PaymentItemAddition addition = paymentItemResult.getAddition();
                        if (addition != null) {
                            long currentTime = Standard.Time.currentTime();
                                                        PaymentItem paymentItem1 = addition.getPaymentItem();
                            paymentItem1.setServerUpdateTime(currentTime);
                            DBHelperManager.saveEntities(helper, PaymentItem.class, true, paymentItem1);
                                                        PaymentItemGroupon paymentItemGroupon = addition.getPaymentItemGroupon();
                            paymentItemGroupon.setServerUpdateTime(currentTime);
                            DBHelperManager.saveEntities(helper, PaymentItemGroupon.class, true, addition.getPaymentItemGroupon());
                                                        TradePromotion tradePromotion = addition.getTradePromotion();
                            tradePromotion.setServerUpdateTime(currentTime);
                            DBHelperManager.saveEntities(helper, TradePromotion.class, true, addition.getTradePromotion());
                        }
                    }
                }
            }
            return null;
        }
    }


    private static class AddFeeRespProcessor extends SaveDatabaseResponseProcessor<GatewayTransferResp<AddFeeResp>> {

        @Override
        protected Callable<Void> getCallable(DatabaseHelper helper, GatewayTransferResp<AddFeeResp> resp
        ) throws Exception {
            DBHelperManager.saveEntities(helper, DeliveryOrderRecord.class, resp.getResult().getDeliveryOrderRecord());
            return null;
        }
    }


    private static class InvoiceRevokeRespProcessor extends SaveDatabaseResponseProcessor<GatewayTransferResp<Invoice>> {

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final GatewayTransferResp<Invoice> resp) {
            return new Callable<Void>() {

                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, Invoice.class, resp.getResult());
                    return null;
                }
            };
        }

    }

    @Override
    public void bindDeliveryUser(BindOrderReq req, ResponseListener<BindOrderResp> listener) {

    }

    @Override
    public void creatUnionTrade(UnionTradeReq req, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void splitUnionTrade(UnionTradeSplitReq req, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void modifyUnionMainTrade(TradeVo tradeVo, TradeUnionModifyMainWarpReq req, ResponseListener<TradeResp> listener, boolean isAsync) {

    }

    @Override
    public void modifyUnionSubTrade(TradeVo tradeVo, TradeUnionModifySubWarpReq req, ResponseListener<TradeResp> listener, boolean isAsync) {

    }

    @Override
    public void unionAndModifyUnionTrade(UnionAndModifyUnionTradeReq req, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void unionOperationDish(UnionTradeItemOperationReq req, ResponseListener<TradeItemResp> listener) {

    }


    private static class BindOrderRespProcessor extends SaveDatabaseResponseProcessor<BindOrderResp> {

        @Override
        protected Callable<Void> getCallable(DatabaseHelper helper, BindOrderResp resp) throws Exception {
            List<BindOrderResp.ResultInfo> resultInfos = resp.getResultInfos();
            if (Utils.isNotEmpty(resultInfos)) {
                List<TradeExtra> tradeExtras = new ArrayList<TradeExtra>();

                for (BindOrderResp.ResultInfo resultInfo : resultInfos) {
                    Integer resultStatus = resultInfo.getResultStatus();
                    if (resultStatus != null && resultStatus == 1) {
                        TradeExtra tradeExtra = resultInfo.getTradeExtra();
                        tradeExtras.add(tradeExtra);
                    }
                }

                DBHelperManager.saveEntities(helper, TradeExtra.class, tradeExtras);
            }
            return null;
        }
    }


    @Override
    public void modifyServiceUnionDish(DishServiceV2Req dishServiceReq, CalmResponseListener<ResponseObject<DishServiceV2Resp>> listener, TradeType type) {

    }

        public static void saveTradeResp(TradeResp resp) {

        try {
            new TradeRespProcessor().saveToDatabase(resp);
        } catch (Exception e) {
                        e.printStackTrace();
        }
    }

    @Override
    public void modifyTradeMemo(Trade trade, String newMemo, ResponseListener<ModifyTradeMemoResp> listener) {

    }

    @Override
    public void buffetUnionTableCreate(BuffetUnionTradeReq req, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void buffetModifyUnionSubTrade(TradeVo tradeVo, TradeUnionModifySubWarpReq req, ResponseListener<TradeResp> listener, boolean isAsync) {

    }

    public void buffetSplitTrade(BuffetUnionTradeCancelReq req, ResponseListener<BuffetUnionTradeCancelResp> listener) {

    }


    private static class BuffetUnionTradeCancelRespProcessor extends OpsRequest.DatabaseResponseProcessor<BuffetUnionTradeCancelResp> {

        @Override
        protected void transactionCallable(DatabaseHelper helper, BuffetUnionTradeCancelResp resp) throws Exception {
            DBHelperManager.saveEntities(helper, Trade.class, resp.getTrades());
            DBHelperManager.saveEntities(helper, TradeTax.class, resp.getTradeTaxs());
            DBHelperManager.saveEntities(helper, TradeInitConfig.class, resp.getTradeInitConfigs());
            DBHelperManager.saveEntities(helper, TradeDeposit.class, resp.getTradeDeposits());
            DBHelperManager.saveEntities(helper, TradeBuffetPeople.class, resp.getTradeBuffetPeoples());
            DBHelperManager.saveEntities(helper, TradeMainSubRelation.class, resp.getTradeMainSubRelations());
            DBHelperManager.saveEntities(helper, TradeTable.class, resp.getTradeTable());
            DBHelperManager.saveEntities(helper, TradeItem.class, resp.getTradeItems());
        }
    }

    @Override
    public void buffetUnionMainTradeModify(BuffetMainTradeModifyReq req, ResponseListener<TradeResp> listener) {


    }

    @Override
    public void buffetUnionFinish(BuffetMergeUnionReq req, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void buffetCreateMenu(TradeVo tradeVo, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void memberWriteoff(WriteoffReq req, ResponseListener<WriteoffResp> listener) {

    }

    @Override
    public void queryInvoiceNo(GetTaxNoReq req, ResponseListener<GetTaxNoResp> listener) {

    }

    @Override
    public void createPrePayTrade(PrePayTradeReq req, ResponseListener<TradeResp> listener) {

    }

    @Override
    public void deletePrePayTrade(DeletePrePayTradeReq req, ResponseListener<TradeResp> listener) {

    }


    public void koubeiVerification(String code, ResponseListener<KouBeiVerifyResp> listener) {

    }


    private static class KoubeiVerifyProcessor extends SaveDatabaseResponseProcessor<KouBeiVerifyResp> {
        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final KouBeiVerifyResp resp) throws Exception {
            return new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    if (resp.verifyInfo != null) {
                        DBHelperManager.saveEntities(helper, VerifyKoubeiOrder.class, resp.verifyInfo);
                    }
                    return null;
                }
            };
        }
    }

    public void getWriteOffOnlinePayResult(String tradeNo, PayType payType, ResponseListener<WriteoffOnlineResultResp> listener) {

    }

    public void useEarnestDeduct(EarnestDeductReq req, ResponseListener<PayResp> listener) {

    }

    @Override
    public void bookingRrePayRefund(PrePayRefundReq req, ResponseListener<PayResp> listener) {

    }

    @Override
    public void recisionUnionTrade(Long tradeId, Long serverUpdateTime, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradeResp> listener) {

    }

}