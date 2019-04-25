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

/**
 * @version: 1.0
 * @date 2015年4月15日
 */
public class TradeOperatesImpl extends AbstractOpeartesImpl implements TradeOperates {

    public TradeOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void insert(TradeVo tradeVo, ResponseListener<TradeResp> listener) {
       /* String url = ServerAddressUtil.getInstance().tradeInsert();
        tradeVo.getTrade().validateCreate();
        // 正餐不生成流水号
        boolean genSn = false;
        TradeReq tradeReq = toTradeReq(tradeVo);
        boolean interceptEnable = Snack.netWorkUnavailable()
                && Snack.isSnackBusiness(tradeVo)
                && Snack.isOfflineEnable();
        OpsRequest.Executor<TradeReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(tradeReq)
                .responseClass(TradeResp.class)
                .timeout(20000)
                .responseProcessor(new TradeRespProcessor(genSn))
                .interceptEnable(interceptEnable)
                .execute(listener, "tradeInsert");*/
    }

    @Override
    public void sellCardsInsert(TradeVo tradeVo, ResponseListener<TradeResp> listener) {

        /*String url = ServerAddressUtil.getInstance().getSellCardsInsertUrl();
        tradeVo.getTrade().validateCreate();
        TradeReq tradeReq = toTradeReq(tradeVo);
        // boolean genSn = false;
        OpsRequest.Executor<TradeReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(tradeReq)
                .responseClass(TradeResp.class)
                // .responseProcessor(new
                // TradeRespProcessor(genSn))
                .execute(listener, "saleCardsInsert");*/
    }

    @Override
    public void sellCardsPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                             ResponseListener<PaymentResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getSellCardsPayUrl();
        PaymentReq paymentReq = toPaymentReq(tradeVo, paymentVoList, memberPasswords);
        OpsRequest.Executor<PaymentReq, PaymentResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(paymentReq).responseClass(PaymentResp.class).execute(listener, "saleCardsPay");*/
    }

    @Override
    public void sellCardsInsertAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList,
                                      Map<String, String> memberPasswords, ResponseListener<TradePaymentResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getSellCardsInsertAndPayUrl();
        tradeVo.getTrade().validateCreate();
        TradePaymentReq req = toTradePaymentReq(tradeVo, paymentVoList, memberPasswords);
        OpsRequest.Executor<TradePaymentReq, TradePaymentResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(TradePaymentResp.class).execute(listener, "saleCardsInsertAndPay");*/
    }

    @Override
    public void insertAnonymousCards(TradeVo tradeVo, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getInsertAnonymousCardsUrl();
        tradeVo.getTrade().validateCreate();
        TradeReq tradeReq = toTradeReq(tradeVo);
        OpsRequest.Executor<TradeReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(tradeReq).responseClass(TradeResp.class).execute(listener, "insertAnonymousCards");*/
    }

    @Override
    public void sellAnonymousCardsPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords, ResponseListener<PaymentResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getSellAnonymousCardsPayUrl();
        PaymentReq paymentReq = toPaymentReq(tradeVo, paymentVoList, memberPasswords);
        OpsRequest.Executor<PaymentReq, PaymentResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(paymentReq).responseClass(PaymentResp.class).execute(listener, "sellAnonymousCardsPay");*/
    }

    @Override
    public void sellAnonymousCardsinsertAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords, ResponseListener<TradePaymentResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getAnonymousCardsinsertAndPayUrl();
        tradeVo.getTrade().validateCreate();
        TradePaymentReq req = toTradePaymentReq(tradeVo, paymentVoList, memberPasswords);
        OpsRequest.Executor<TradePaymentReq, TradePaymentResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(TradePaymentResp.class).execute(listener, "sellAnonymousCardsinsertAndPay");*/
    }

    @Override
    public void saleAndStoreAnonymousCard(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords, ResponseListener<TradePaymentResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getAnonymousCardSaleAndStoreUrl();
        tradeVo.getTrade().validateCreate();
        TradePaymentReq req = toTradePaymentReq(tradeVo, paymentVoList, memberPasswords);
        OpsRequest.Executor<TradePaymentReq, TradePaymentResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(TradePaymentResp.class).execute(listener, "saleAndStoreAnonymousCard");*/
    }

    @Override
    public void storeAnonymousCard(TradeVo tradeVo, int online, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords, ResponseListener<AnonymousCardStoreResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getAnonymousCardStoreUrl();
        CardRechargeReq rechargeReq = new CardRechargeReq();
        rechargeReq.setClientCreateTime(System.currentTimeMillis());
        rechargeReq.setClientUpdateTime(System.currentTimeMillis());
        rechargeReq.setCreatorId(Session.getAuthUser().getId());
        rechargeReq.setCreatorName(Session.getAuthUser().getName());
        //req.setCustomerId(mCustomer.getLong(Customer.ID_KEY));
        rechargeReq.setUpdatorId(Session.getAuthUser().getId());
        rechargeReq.setUpdatorName(Session.getAuthUser().getName());
        rechargeReq.setUuid(SystemUtils.genOnlyIdentifier());
        rechargeReq.setTradeNo(tradeVo.getTrade().getTradeNo());
        rechargeReq.setTradeId(tradeVo.getTrade().getId());
        rechargeReq.setOnlinePay(online);

        if (paymentVoList != null && !paymentVoList.isEmpty()) {
            rechargeReq.setPaymentCards(paymentVoList.get(0).getPaymentCards());
            rechargeReq.setPaymentItems(paymentVoList.get(0).getPaymentItemList());
        }

        rechargeReq.setCardNum(tradeVo.getTradeItemList().get(0).getCardSaleInfos().get(0).getCardNum());
        rechargeReq.setAddValue(tradeVo.getTrade().getTradeAmount());
        rechargeReq.setSendValue(new BigDecimal(0));
        OpsRequest.Executor<CardRechargeReq, AnonymousCardStoreResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(rechargeReq).responseClass(AnonymousCardStoreResp.class)
                .responseProcessor(new AnonymousCardStoreRespProcessor())
                .execute(listener, "storeAnonymousCard");*/
    }

    @Override
    public void storeEntityCard(TradeVo tradeVo, int online, long customerId, BigDecimal chargeMoney, BigDecimal sendMoney, List<PaymentVo> paymentVoList, ResponseListener<AnonymousCardStoreResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getEntityCardStoreUrl();
        CardRechargeReq rechargeReq = new CardRechargeReq();
        rechargeReq.setTradeUser(tradeVo.getTradeUser());//add 20170916 v8.1 销售员
        rechargeReq.setTradeUsers(tradeVo.getTradeUsers());
        rechargeReq.setClientCreateTime(System.currentTimeMillis());
        rechargeReq.setClientUpdateTime(System.currentTimeMillis());
        rechargeReq.setCreatorId(Session.getAuthUser().getId());
        rechargeReq.setCreatorName(Session.getAuthUser().getName());
        rechargeReq.setCustomerId(customerId);//会员id
        rechargeReq.setUpdatorId(Session.getAuthUser().getId());
        rechargeReq.setUpdatorName(Session.getAuthUser().getName());
        rechargeReq.setUuid(SystemUtils.genOnlyIdentifier());
        rechargeReq.setTradeNo(tradeVo.getTrade().getTradeNo());
        rechargeReq.setTradeUuid(tradeVo.getTrade().getUuid());
        rechargeReq.setTradeId(tradeVo.getTrade().getId());
        rechargeReq.setOnlinePay(online);

        if (paymentVoList != null && !paymentVoList.isEmpty()) {
            rechargeReq.setUuid(paymentVoList.get(0).getPayment().getUuid());//如果paymentuuid 不为空，就取值
            rechargeReq.setPaymentCards(paymentVoList.get(0).getPaymentCards());//银行卡支付
            rechargeReq.setPaymentItems(paymentVoList.get(0).getPaymentItemList());
        }
        rechargeReq.setCardNum(tradeVo.getTradeItemList().get(0).getCardSaleInfos().get(0).getCardNum());//充值会员卡号
        rechargeReq.setAddValue(chargeMoney);
        rechargeReq.setSendValue(sendMoney);
        OpsRequest.Executor<CardRechargeReq, AnonymousCardStoreResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(rechargeReq).responseClass(AnonymousCardStoreResp.class)
                .responseProcessor(new AnonymousCardStoreRespProcessor())
                .execute(listener, "storeEntityCard");*/
    }

    //会员虚拟卡充值
    @Override
    public void storeVirtualCard(TradeVo tradeVo, int online, long customerId, BigDecimal chargeMoney, BigDecimal sendMoney, List<PaymentVo> paymentVoList, ResponseListener<AnonymousCardStoreResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getVirtualcardCardStoreUrl();
        VirtualCardRechargeReq req = new VirtualCardRechargeReq();
        req.setCustomerId(customerId);
        req.setOnlinePay(online);
        req.setTradeUser(tradeVo.getTradeUser());//add 20170916 销售员
        req.setTradeUsers(tradeVo.getTradeUsers());
        TradeInfo tradeInfo = new TradeInfo();
        long currenttime = System.currentTimeMillis();
        tradeInfo.setClientCreateTime(currenttime);
        tradeInfo.setClientUpdateTime(currenttime);
        tradeInfo.setCreatorId(Session.getAuthUser().getId());
        tradeInfo.setCreatorName(Session.getAuthUser().getName());
        tradeInfo.setUpdatorId(Session.getAuthUser().getId());
        tradeInfo.setUpdatorName(Session.getAuthUser().getName());
        tradeInfo.setTradeNo(tradeVo.getTrade().getTradeNo());
        tradeInfo.setUuid(tradeVo.getTrade().getUuid());
        tradeInfo.setSource(tradeVo.getTrade().getSource().value());
        tradeInfo.setTotalValueCard(chargeMoney);// 充值金额
        if (paymentVoList != null && !paymentVoList.isEmpty()) {
            PaymentInfo paymentInfo = new PaymentInfo();
            req.setPaymentRequest(paymentInfo);
            paymentInfo.setClientCreateTime(currenttime);
            paymentInfo.setClientUpdateTime(currenttime);
            paymentInfo.setActualAmount(paymentVoList.get(0).getPayment().getActualAmount());
            paymentInfo.setExemptAmount(BigDecimal.ZERO);
            paymentInfo.setReceivableAmount(paymentVoList.get(0).getPayment().getReceivableAmount());
            paymentInfo.setUuid(paymentVoList.get(0).getPayment().getUuid());//如果paymentuuid 不为空，就取值
            paymentInfo.setPaymentCards(paymentVoList.get(0).getPaymentCards());//银行卡支付
            paymentInfo.setPaymentItems(paymentVoList.get(0).getPaymentItemList());
        }
        req.setTradeInfo(tradeInfo);
        OpsRequest.Executor<VirtualCardRechargeReq, AnonymousCardStoreResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(AnonymousCardStoreResp.class)
                .responseProcessor(new AnonymousCardStoreRespProcessor())
                .execute(listener, "storeVirtualCard");*/
    }

    @Override
    public void pay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                    ResponseListener<PaymentResp> listener) {
       /* String url = ServerAddressUtil.getInstance().tradePay();
        PaymentReq paymentReq = toPaymentReq(tradeVo, paymentVoList, memberPasswords);
        OpsRequest.Executor<PaymentReq, PaymentResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(paymentReq)
                .responseClass(PaymentResp.class)
                .responseProcessor(new PaymentRespProcessor())
                .timeout(15000);
        if (isOnlyCashPayment(paymentVoList)) {
            AsyncNetworkManager.getInstance().addRequest(tradeVo, AsyncHttpType.CASHER, executor, listener, "tradePay");
        } else {
            executor.execute(listener, "tradePay");
        }*/
        pay(tradeVo, paymentVoList, memberPasswords, listener, false);
    }

    public void pay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                    ResponseListener<PaymentResp> listener, boolean isAsync) {
        /*String url = ServerAddressUtil.getInstance().tradePay();
        PaymentReq paymentReq = toPaymentReq(tradeVo, paymentVoList, memberPasswords);
        OpsRequest.Executor<PaymentReq, PaymentResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(paymentReq)
                .responseClass(PaymentResp.class)
                .responseProcessor(new PaymentRespProcessor())
                .timeout(15000);
        if (isAsync) {
            AsyncNetworkManager.getInstance().addRequest(tradeVo, AsyncHttpType.CASHER, executor, listener, "tradePay");
        } else {
            executor.execute(listener, "tradePay");
        }*/
    }

    @Override
    public void adjust(PaymentVo paymentVo, ResponseListener<PaymentResp> listener) {
        /*String url = ServerAddressUtil.getInstance().adjustPay();
        PaymentReq paymentReq = toAdjustPaymentReq(paymentVo);
        OpsRequest.Executor<PaymentReq, PaymentResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(paymentReq)
                .responseClass(PaymentResp.class)
                .responseProcessor(new PaymentRespProcessor())
                .execute(listener, "adjustPay");*/
    }

    @Override
    public void insertAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                             ResponseListener<TradePaymentResp> listener, boolean isAsync) {
        /*String url = ServerAddressUtil.getInstance().tradeInsertAndPay();
        tradeVo.getTrade().validateCreate();
        // 正餐不生成流水号
        boolean genSn = false;
        *//*if (tradeVo.getTrade().getBusinessType() != BusinessType.DINNER) {
            tradeVo.getTradeExtra().setSerialNumber(PrHelper.getDefault().getSerialNumber());
			genSn = true;
		}*//*
        boolean interceptEnable = Snack.isSnackBusiness(tradeVo)
                && Snack.isOfflineEnable()
                && Snack.netWorkUnavailable();
        TradePaymentReq req = toTradePaymentReq(tradeVo, paymentVoList, memberPasswords);
        OpsRequest.Executor<TradePaymentReq, TradePaymentResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradePaymentResp.class)
                .responseProcessor(new TradePaymentRespProcessor(genSn))
                .interceptEnable(interceptEnable)
                .timeout(15000);
        if (isAsync) {
            AsyncNetworkManager.getInstance().addRequest(tradeVo, AsyncHttpType.CASHER, executor, listener, "tradeInsertAndPay");
        } else {
            executor.execute(listener, "tradeInsertAndPay");
        }*/
    }

    @Override
    public void insertAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                             ResponseListener<TradePaymentResp> listener) {
       /* String url = ServerAddressUtil.getInstance().tradeInsertAndPay();
        tradeVo.getTrade().validateCreate();
        // 正餐不生成流水号
        boolean genSn = false;
        *//*if (tradeVo.getTrade().getBusinessType() != BusinessType.DINNER) {
            tradeVo.getTradeExtra().setSerialNumber(PrHelper.getDefault().getSerialNumber());
			genSn = true;
		}*//*
        TradePaymentReq req = toTradePaymentReq(tradeVo, paymentVoList, memberPasswords);
        OpsRequest.Executor<TradePaymentReq, TradePaymentResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradePaymentResp.class)
                .responseProcessor(new TradePaymentRespProcessor(genSn))
                .timeout(15000)
                .execute(listener, "tradeInsertAndPay");*/
        insertAndPay(tradeVo, paymentVoList, memberPasswords, listener, false);
    }

    @Override
    public void modifyAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                             ResponseListener<PaymentResp> listener) {
        modifyAndPay(tradeVo, paymentVoList, memberPasswords, listener, false);
    }

    public void modifyAndPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Map<String, String> memberPasswords,
                             ResponseListener<PaymentResp> listener, boolean isAsync) {
        /*PayDinnerReq payDinnerReq = toPayDinnerReq(tradeVo, paymentVoList, memberPasswords);
        List<TradeTable> tradeTableList = payDinnerReq.getTrade().getTradeTables();
        *//*if (tradeTableList != null) {
            for (TradeTable tradeTable : tradeTableList) {
                tradeTable.setSelfTableStatus(TableStatus.EMPTY);
            }
        }*//*
        if (tradeVo.getTrade().getTradeType() == TradeType.SELL_FOR_REPEAT) {
            PaymentOfRepeatReq req = toPaymentOfRepeatReq(tradeVo, payDinnerReq);
            //需要将tradeExtra存在list中 ps:只针对反结
            if (tradeVo.getTradeExtra() != null) {
                List<TradeExtra> temp = new ArrayList<TradeExtra>();
                temp.add(tradeVo.getTradeExtra());
                req.getActual().getTrade().setTradeExtras(temp);
                req.getActual().getTrade().setTradeExtra(null);
            }

            String url = ServerAddressUtil.getInstance().modifyAndPayOfRepeat();
            OpsRequest.Executor<PaymentOfRepeatReq, PaymentResp> executor = OpsRequest.Executor.create(url);
            executor.requestValue(req)
                    .responseClass(PaymentResp.class)
                    .responseProcessor(new PaymentRespProcessor())
                    .timeout(15000);
            if (isAsync) {
                AsyncNetworkManager.getInstance().addRequest(tradeVo, AsyncHttpType.CASHER, executor, listener, "modifyAndPay");
            } else {
                executor.execute(listener, "modifyAndPay");
            }
        } else {
            String url = ServerAddressUtil.getInstance().modifyAndPay();
            OpsRequest.Executor<PayDinnerReq, PaymentResp> executor = OpsRequest.Executor.create(url);
            executor.requestValue(payDinnerReq)
                    .responseClass(PaymentResp.class)
                    .responseProcessor(new PaymentRespProcessor())
                    .timeout(15000);
            if (isAsync) {
                AsyncNetworkManager.getInstance().addRequest(tradeVo, AsyncHttpType.CASHER, executor, listener, "modifyAndPay");
            } else {
                executor.execute(listener, "modifyAndPay");
            }
        }*/
    }

    @Override
    public void getWechatPayUrl(WechatPayUrlReq req, ResponseListener<WechatPayUrlResp> listener) {
        /*String url = ServerAddressUtil.getInstance().wechatPayUrl();
        OpsRequest.Executor<WechatPayUrlReq, WechatPayUrlResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(WechatPayUrlResp.class).timeout(30000).execute(listener,
                "getWechatPayUrl");*/
    }

    @Override
    public void wechatPay(WechatPayReq req, ResponseListener<WechatPayResp> listener) {
        /*String url = ServerAddressUtil.getInstance().wechatPay();
        OpsRequest.Executor<WechatPayReq, WechatPayResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(WechatPayResp.class).timeout(12000).execute(listener, "wechatPay");*/
    }

    @Override
    public void verifyPay(long tradeId, ResponseListener<VerifyPayResp> listener) {
        /*String url = ServerAddressUtil.getInstance().verifyPay();
        OpsRequest.Executor<Long, VerifyPayResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(tradeId)
                .responseClass(VerifyPayResp.class)
                .responseProcessor(new VerifyPayRespProcessor())
                .execute(listener, "verifyPay");*/
    }

    @Override
    public void accept(TradeVo tradeVo, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().tradeAccept();
        tradeVo.getTrade().validateCreate();
        // 正餐不生成流水号
        boolean genSn = false;
        if (tradeVo.getTrade().getBusinessType() != BusinessType.DINNER) {
            if (tradeVo.getTradeExtra() == null) {
                TradeExtra tradeExtra = new TradeExtra();
                tradeExtra.validateCreate();
                tradeExtra.setTradeId(tradeVo.getTrade().getId());
                tradeExtra.setTradeUuid(tradeVo.getTrade().getUuid());
                tradeVo.setTradeExtra(tradeExtra);
            }
            *//*if (TextUtils.isEmpty(tradeVo.getTradeExtra().getSerialNumber())) {
                tradeVo.getTradeExtra().setSerialNumber(PrHelper.getDefault().getSerialNumber());
				genSn = true;
			}*//*
        }
        TradeOpsReq req = toTradeOpsReq(tradeVo, TradeStatus.CONFIRMED, null);
        OpsRequest.Executor<TradeOpsReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradeResp.class)
                .responseProcessor(new TradeRespProcessor(genSn))
                .execute(listener, "tradeAccept");*/
    }

    @Override
    public void receiveBatch(ActionType actionType, List<TradeVo> tradeVos, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().tradeReceiveBatch();
        TradeBatchOpsReq req = toTradeBatchOpsReq(actionType, null, tradeVos);
        OpsRequest.Executor<TradeBatchOpsReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradeResp.class)
                .responseProcessor(new TradeRespProcessor(false))
                .execute(listener, "tradeReceiveBatch");*/
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
        /*String url = ServerAddressUtil.getInstance().tradeRefuse();
        tradeVo.getTrade().validateCreate();
        tradeVo.getTrade().validateUpdate();
        TradeOpsReq req = toTradeOpsReq(tradeVo, TradeStatus.REFUSED, reason);
        OpsRequest.Executor<TradeOpsReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(TradeResp.class).responseProcessor(new TradeRespProcessor()).execute(
                listener, "tradeRefuse");*/
    }

    @Override
    public void refuseBatch(ActionType actionType, Reason reason, List<TradeVo> tradeVos, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().tradeRefuseBatch();
        TradeBatchOpsReq req = toTradeBatchOpsReq(actionType, reason, tradeVos);
        OpsRequest.Executor<TradeBatchOpsReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(TradeResp.class).responseProcessor(new TradeRespProcessor()).execute(
                listener, "tradeRefuseBatch");*/
    }

    @Override
    public void acceptDinner(Trade trade, ResponseListener<TradeResp> listener) {
        acceptDinner(trade, true, listener);
    }

    @Override
    public void acceptDinner(Trade trade, boolean genBatchNo, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().dinnerAccept();
        trade.validateCreate();
        TradeOpsReq req = toTradeOpsReq(trade, TradeStatus.CONFIRMED, null);
        req.setGenBatchNo(genBatchNo ? Bool.YES : Bool.NO);//自动生成批次号
        //设置服务员信息
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            req.waiterId = user.getId();
            req.waiterName = user.getName();
        }
        OpsRequest.Executor<TradeOpsReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(TradeResp.class).responseProcessor(new TradeRespProcessor()).execute(
                listener, "tradeDinnerAccept");*/
    }

    @Override
    public void refuseDinner(TradeVo tradeVo, Reason reason, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().dinnerRefuse();
        tradeVo.getTrade().validateCreate();
        tradeVo.getTrade().validateUpdate();
        TradeOpsReq req = toTradeOpsReq(tradeVo.getTrade(), TradeStatus.REFUSED, reason);
        OpsRequest.Executor<TradeOpsReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(TradeResp.class).responseProcessor(new TradeRespProcessor()).execute(
                listener, "tradeDinnerRefuse");*/

    }

    @Override
    public void recision(TradeVo tradeVo, Reason reason, List<InventoryItemReq> returnInventoryItems, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().tradeRecision();
        tradeVo.getTrade().validateUpdate();
        TradeOpsReq req = toTradeOpsReq(tradeVo.getTrade(), TradeStatus.INVALID, reason);
        req.setReturnInventoryItems(returnInventoryItems);
        boolean interceptEnable = Snack.isSnackBusiness(tradeVo)
                && Snack.isOfflineTrade(tradeVo);
        OpsRequest.Executor<TradeOpsReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradeResp.class)
                .responseProcessor(new TradeRespProcessor())
                .interceptEnable(interceptEnable)
                .execute(listener, "tradeRecision");*/
    }

    @Override
    public void refund(TradeVo tradeVo, Reason reason, List<InventoryItemReq> returnInventoryItems, ResponseListener<TradePaymentResp> listener) {
        /*String url = ServerAddressUtil.getInstance().tradeRefund();
        tradeVo.getTrade().validateUpdate();
        TradeRefundReq req = toTradeRefundReq(tradeVo.getTrade(), reason);
        req.setReturnInventoryItems(returnInventoryItems);
        boolean interceptEnable = Snack.isSnackBusiness(tradeVo)
                && Snack.isOfflineTrade(tradeVo);
        OpsRequest.Executor<TradeRefundReq, TradePaymentResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradePaymentResp.class)
                .responseProcessor(new TradePaymentRespProcessor())
                .interceptEnable(interceptEnable)
                .execute(listener, "tradeRefund");*/
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
        /*String url = ServerAddressUtil.getInstance().deliveredPayment();
        DeliveredPaymentReq req = toDeliveredPaymentReq(tradeVoList);
        OpsRequest.Executor<DeliveredPaymentReq, TradePaymentResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradePaymentResp.class)
                .responseProcessor(new TradePaymentRespProcessor())
                .execute(listener, "deliveredPayment");*/
    }

    @Override
    public void clearAccounts(List<TradeVo> tradeVoList, ResponseListener<TradePaymentResp> listener) {
        /*String url = ServerAddressUtil.getInstance().clearAccounts();
        ClearBillReq req = new ClearBillReq();
        List<Long> tradeIdList = toClearAccountsReq(tradeVoList);
        req.setTradeIds(tradeIdList);
        OpsRequest.Executor<ClearBillReq, TradePaymentResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradePaymentResp.class)
                .responseProcessor(new TradePaymentRespProcessor())
                .execute(listener, "clearAccounts");*/
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
        /*String url = ServerAddressUtil.getInstance().batchunbindCoupon();
        TradeBatchUnbindCouponReq req = toTradeBatchUnbindCouponReq(tradeId, tradePrivileges);
        OpsRequest.Executor<TradeBatchUnbindCouponReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradeResp.class)
                .responseProcessor(new TradeRespProcessor())
                .execute(listener, "batchunbindCoupon");*/
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
        /*String url = ServerAddressUtil.getInstance().unbindCoupon();
        OpsRequest.Executor<TradeUnbindCouponReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(TradeResp.class).responseProcessor(new TradeRespProcessor()).execute(
                listener, "unbindCoupon");*/
    }

    @Override
    public void unbindIntegral(TradeUnbindCouponReq req, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().unbindIntegral();
        OpsRequest.Executor<TradeUnbindCouponReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(TradeResp.class).responseProcessor(new TradeRespProcessor()).execute(
                listener, "unbindIntegral");*/
    }

    @Override
    public void getSendCouponUrl(ResponseListener<CouponUrlResp> listener) {
        /*String url = ServerAddressUtil.getInstance().sendCouponUrl();
        OpsRequest.Executor<String, CouponUrlResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue("{}").responseClass(CouponUrlResp.class).execute(
                listener, "sendCouponUrl");*/
    }

    @Override
    public void salesRetuenPay(TradeVo tradeVo, List<PaymentVo> paymentVoList, Reason reason, boolean isReturnInventory,
                               Map<String, String> memberPasswords, ResponseListener<TradePaymentResp> listener) {
        /*String url = ServerAddressUtil.getInstance().salesReturnUrl();
        tradeVo.getTrade().validateCreate();
        // 正餐不生成流水号
        boolean genSn = false;
        *//*if (tradeVo.getTrade().getBusinessType() != BusinessType.DINNER) {
            tradeVo.getTradeExtra().setSerialNumber(PrHelper.getDefault().getSerialNumber());
			genSn = true;
		}*//*
        SalesReturnTradePaymentReq req = new SalesReturnTradePaymentReq();
        TradeReq tradeReq = toTradeReq(tradeVo);
        PaymentReq paymentReq = toPaymentReq(tradeVo, paymentVoList, memberPasswords);
        req.setTrade(tradeReq);
        req.setPayment(paymentReq);
        if (reason != null) {
            req.setReasonId(reason.getId());
            req.setReasonContent(reason.getContent());
        }
        req.setReviseStock(isReturnInventory);
        OpsRequest.Executor<SalesReturnTradePaymentReq, TradePaymentResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradePaymentResp.class)
                .responseProcessor(new TradePaymentRespProcessor(genSn))
                .execute(listener, "salesReturn");*/
    }

    @Override
    public void returnSellCards(Trade srcTrade, TradeVo tradeVo, List<PaymentVo> paymentVoList,
                                List<CardNumber> cardList, Reason reason, boolean returnAll, ResponseListener<SalesCardReturnResp> listener) {
        // TODO Auto-generated method stub
        /*String url = ServerAddressUtil.getInstance().returnCardsUrl();
        tradeVo.getTrade().validateCreate();
        SalesCardReturnReq req = new SalesCardReturnReq();
        if (returnAll) {
            req.setRefundWay(1L);
        } else {
            req.setRefundWay(2L);
        }
        req.setSrcTradeId(srcTrade.getId());
        req.setSrcServerUpdateTime(srcTrade.getServerUpdateTime());

        if (reason != null) {
            req.setReasonId(reason.getId());
            req.setReasonContent(reason.getContent());
        }
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            req.setCreatorId(user.getId());
            req.setCreatorName(user.getName());
        }
        req.setCardList(cardList);

        if (!returnAll) {
            TradePaymentReq tradePaymentReq = new TradePaymentReq();
            TradeReq tradeReq = toTradeReq(tradeVo);
            PaymentReq paymentReq = toPaymentReq(tradeVo, paymentVoList, null);
            tradePaymentReq.setTrade(tradeReq);
            tradePaymentReq.setPayment(paymentReq);
            req.setPartRefundContent(tradePaymentReq);
        }
        OpsRequest.Executor<SalesCardReturnReq, SalesCardReturnResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(SalesCardReturnResp.class).execute(listener, "tradeInsertDinner");*/
    }

    @Override
    public void insertDinner(TradeVo tradeVo, IDinnertable dinnertable, ResponseListener<TradeResp> listener) {
        insertDinner(tradeVo, dinnertable, listener, false);
    }

    @Override
    public void insertDinner(final TradeVo tradeVo, IDinnertable dinnertable, ResponseListener<TradeResp> listener, boolean isAsync) {
        /*String url = ServerAddressUtil.getInstance().tradeInsertDinner();
        tradeVo.getTrade().validateCreate();
        TradeReq tradeReq = toTradeReq(tradeVo);
        DinnertableState dinnertableState = new DinnertableState();
        dinnertableState.setId(dinnertable.getId());
        dinnertableState.setModifyDateTime(dinnertable.getServerUpdateTime());
        dinnertableState.setTableStatus(TableStatus.OCCUPIED);
        tradeReq.setTables(Arrays.asList(dinnertableState));
        //增加默认税率
        TaxRateInfo taxRateInfo = ServerSettingCache.getInstance().getmTaxRateInfo();
        if (taxRateInfo != null && taxRateInfo.isTaxSupplyOpen()) {
            TradeTax tradeTax = taxRateInfo.toTradeTax(null);
            tradeReq.setTradeTaxs(Arrays.asList(tradeTax));
        }

        //加入服务费
        ExtraCharge serviceExtraCharge = ServerSettingCache.getInstance().getmServiceExtraCharge();
        if (serviceExtraCharge != null && serviceExtraCharge.isAutoJoinTrade()) {
            tradeReq.setTradeInitConfigs(Arrays.asList(serviceExtraCharge.toTradeInitConfig()));
        }

        OpsRequest.Executor<TradeReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(tradeReq)
                .responseClass(TradeResp.class)
                .responseProcessor(new TradeRespProcessor());
        if (isAsync) {
            AsyncNetworkManager.getInstance().addRequest(tradeVo, AsyncHttpType.OPENTABLE, executor, new AsyncOpenTableResponseListener(UserActionEvent.DINNER_TABLE_OPEN) {
                @Override
                public void onResponse(ResponseObject<TradeResp> response) {
                    super.onResponse(response);
                    if (ResponseObject.isOk(response)) {
                        if (response.getContent() != null) {
                            tradeVo.setTradeTaxs(response.getContent().getTradeTaxs());
                            tradeVo.setTradeInitConfigs(response.getContent().getTradeInitConfigs());
                        }
                    }
                }
            }, "tradeInsertDinner");
        } else {
            executor.execute(listener, "tradeInsertDinner");
        }*/
    }

    @Override
    public void insertBuffet(TradeVo tradeVo, IDinnertable dinnertable, ResponseListener<TradeResp> listener, boolean isAsync) {
        /*String url = ServerAddressUtil.getInstance().tradeInsertBuffet();
        tradeVo.getTrade().validateCreate();
        TradeReq tradeReq = toTradeReq(tradeVo);
        DinnertableState dinnertableState = new DinnertableState();
        dinnertableState.setId(dinnertable.getId());
        dinnertableState.setModifyDateTime(dinnertable.getServerUpdateTime());
        dinnertableState.setTableStatus(TableStatus.OCCUPIED);
        tradeReq.setTables(Arrays.asList(dinnertableState));
        //增加默认税率
        TaxRateInfo taxRateInfo = ServerSettingCache.getInstance().getmTaxRateInfo();
        if (taxRateInfo != null && taxRateInfo.isTaxSupplyOpen()) {
            TradeTax tradeTax = taxRateInfo.toTradeTax(null);
            tradeReq.setTradeTaxs(Arrays.asList(tradeTax));
        }
        //加入服务费
        ExtraCharge serviceExtraCharge = ServerSettingCache.getInstance().getmServiceExtraCharge();
        if (serviceExtraCharge != null && serviceExtraCharge.isAutoJoinTrade()) {
            tradeReq.setTradeInitConfigs(Arrays.asList(serviceExtraCharge.toTradeInitConfig()));
        }
        OpsRequest.Executor<TradeReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(tradeReq)
                .responseClass(TradeResp.class)
                .responseProcessor(new TradeRespProcessor());
        if (isAsync) {
            AsyncNetworkManager.getInstance().addRequest(tradeVo, AsyncHttpType.OPENTABLE, executor, new AsyncOpenTableResponseListener(UserActionEvent.DINNER_TABLE_OPEN), "tradeInsertDinner");
        } else {
            executor.execute(listener, "tradeInsertBuffet");
        }*/
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
        /*String url = ServerAddressUtil.getInstance().newTradeModifyDinner();
        if (isUpdate) {
            tradeVo.getTrade().validateUpdate();
        }
        boolean interceptEnable = Snack.isOfflineTrade(tradeVo);
        TradeReq tradeReq = toTradeReq(tradeVo, true);
        InventoryUtils.setInventoryVoValue(tradeVo);
        InventoryChangeReq inventoryChangeReq = InventoryUtils.makeInventoryChangeReq(tradeVo.inventoryVo);
        TradeNewReq tradeNewReq = new TradeNewReq(tradeReq, inventoryChangeReq);
        OpsRequest.Executor<TradeNewReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(tradeNewReq)
                .responseClass(TradeResp.class)
                .interceptEnable(interceptEnable)
                .responseProcessor(new TradeRespProcessor());
        if (isAsync) {
            AsyncNetworkManager.getInstance().addRequest(tradeVo, AsyncHttpType.MODIFYTRADE, executor, listener, "tradeModifyDinner");
        } else {
            executor.execute(listener, "tradeModifyDinner");
        }*/
    }

    @Override
    public void modifyBuffet(TradeVo tradeVo, ResponseListener<TradeResp> listener) {
        modifyBuffet(tradeVo, true, listener, false);
    }

    @Override
    public void modifyBuffet(TradeVo tradeVo, boolean isUpdate, ResponseListener<TradeResp> listener, boolean isAsync) {
        /*String url = ServerAddressUtil.getInstance().tradeModifyBuffet();
        if (isUpdate) {
            tradeVo.getTrade().validateUpdate();
        }
        //如果押金是0元且有效，直接改成无效
        TradeDeposit tradeDeposit = tradeVo.getTradeDeposit();
        if (tradeDeposit != null && tradeDeposit.isValid() && tradeDeposit.getDepositPay() != null && tradeDeposit.getDepositPay().compareTo(BigDecimal.ZERO) == 0) {
            tradeDeposit.setStatusFlag(StatusFlag.INVALID);
            tradeDeposit.setChanged(true);
        }
        TradeReq tradeReq = toTradeReq(tradeVo, true);
        InventoryUtils.setInventoryVoValue(tradeVo);
        InventoryChangeReq inventoryChangeReq = InventoryUtils.makeInventoryChangeReq(tradeVo.inventoryVo);
        TradeNewReq tradeNewReq = new TradeNewReq(tradeReq, inventoryChangeReq);
        OpsRequest.Executor<TradeNewReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(tradeNewReq)
                .responseClass(TradeResp.class)
                .responseProcessor(new TradeRespProcessor());
        if (isAsync) {
            AsyncNetworkManager.getInstance().addRequest(tradeVo, AsyncHttpType.MODIFYTRADE, executor, listener, "tradeModifyDinner");
        } else {
            executor.execute(listener, "tradeModifyBuffet");
        }*/
    }

    @Override
    public void transferDinnertable(List<TradeItemExtraDinner> tradeItemSeats, IDinnertableTrade orginal, IDinnertable dest,
                                    ResponseListener<TransferDinnertableResp> listener) {
        /*String url = ServerAddressUtil.getInstance().transferDinnertable();
        TransferDinnertableReq req = new TransferDinnertableReq();
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            req.setUpdatorId(user.getId());
            req.setUpdatorName(user.getName());
        }
        req.setTradeTableId(orginal.getId());
        req.setTradeTableUpdateTime(orginal.getServerUpdateTime());
        req.setTradeItemExtraDinners(tradeItemSeats);
        // 原桌台上只有一单时将原桌台状态改为空桌
        DinnertableState orgnalDinnertable = new DinnertableState();
        orgnalDinnertable.setId(orginal.getDinnertable().getId());
        orgnalDinnertable.setModifyDateTime(orginal.getDinnertable().getServerUpdateTime());
        if (orginal.getDinnertable().getTradeCount() == 1) {
            orgnalDinnertable.setTableStatus(TableStatus.EMPTY);
        } else {
            orgnalDinnertable.setTableStatus(orginal.getDinnertable().getTableStatus());
        }
        DinnertableState destDinnertable = new DinnertableState();
        destDinnertable.setId(dest.getId());
        destDinnertable.setModifyDateTime(dest.getServerUpdateTime());
        destDinnertable.setTableStatus(TableStatus.OCCUPIED);
        req.setTables(Arrays.asList(orgnalDinnertable, destDinnertable));

        req.setFromTableUpdateTime(orgnalDinnertable.getModifyDateTime());
        req.setToTableId(dest.getId());
        req.setToTableUpdateTime(dest.getServerUpdateTime());
        OpsRequest.Executor<TransferDinnertableReq, TransferDinnertableResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TransferDinnertableResp.class)
                .responseProcessor(new TransfertableRespProcessor())
                .execute(listener, "transferDinnertable");
        AuthLogManager.getInstance().flush(OrderActionEnum.TRANSFER_TABLES, orginal.getTradeId(), orginal.getTradeUuid(), orginal.getTradeClientUpdateTime());*/
    }

    @Override
    public void recisionDinner(Long tradeId, Long serverUpdateTime, List<DinnertableState> states, Reason mReason,
                               List<InventoryItemReq> inventoryItems, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().recisionDinner();
        TradeDeleteReq deleteReq = new TradeDeleteReq();
        RecisionDinnerReq req = new RecisionDinnerReq();
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            req.setUpdatorId(user.getId());
            req.setUpdatorName(user.getName());
        }
        req.setTradeId(tradeId);
        req.setServerUpdateTime(serverUpdateTime);
        req.setTables(states);
        if (mReason != null) {
            req.setReasonId(mReason.getId());
            req.setReasonContent(mReason.getContent());
        }

        deleteReq.setTradeDinnerDeleteRequest(req);
        deleteReq.setReturnInventoryItems(inventoryItems);
        OpsRequest.Executor<TradeDeleteReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(deleteReq).responseClass(TradeResp.class).responseProcessor(new TradeRespProcessor()).execute(
                listener, "recisionDinner");*/
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
        /*String url = ServerAddressUtil.getInstance().tradeRefundDinner();
        tradeVo.getTrade().validateUpdate();
        TradeRefundReq req = toTradeRefundReq(tradeVo.getTrade(), reason);
        req.setReturnInventoryItems(inventoryItems);
        req.setReviseStock(true);
        OpsRequest.Executor<TradeRefundReq, TradePaymentResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradePaymentResp.class)
                .responseProcessor(new TradePaymentRespProcessor())
                .execute(listener, "tradeRefund");*/
    }

    @Override
    public void mergeDinner(final List<TradeItemExtraDinner> tradeItemSeats, final IDinnertableTrade orginal, final IDinnertableTrade dest, final Long levelId,
                            final ResponseListener<TradeResp> listener) {
        /*new AsyncTask<Object, Object, MergeDinnerReq>() {

            private Exception exception = null;

            @Override
            protected MergeDinnerReq doInBackground(Object... params) {
                DatabaseHelper helper = DBHelperManager.getHelper();
                try {
                    MergeDinnerReq req = queryMergeDinnerReq(tradeItemSeats, helper, orginal, dest, levelId);
                    AuthUser user = Session.getAuthUser();
                    if (user != null) {
                        req.setUpdatorId(user.getId());
                        req.setUpdatorName(user.getName());
                    }
                    return req;
                } catch (Exception e) {
                    exception = e;
                    return null;
                } finally {
                    DBHelperManager.releaseHelper(helper);
                }
            }

            @Override
            protected void onPostExecute(MergeDinnerReq result) {
                if (result == null) {
                    listener.onError(new VolleyError(exception));
                } else {
                    String url = ServerAddressUtil.getInstance().mergeDinner();
                    OpsRequest.Executor<MergeDinnerReq, TradeResp> executor = OpsRequest.Executor.create(url);
                    executor.requestValue(result)
                            .responseClass(TradeResp.class)
                            .responseProcessor(new TradeRespProcessor())
                            .execute(listener, "mergeDinner");
                }
            }
        }.execute();*/
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

        Map<Long, String> tradeItemRelations = new HashMap<Long, String>();//原单菜品id和目标单菜品uuid的关联关系

        // 更改TradeItem的外键
        Map<String, String> itemUuidMap = new HashMap<String, String>();
        List<Long> itemIds = new ArrayList<Long>();
        List<TradeItem> itemList = itemDao.queryForEq(TradeItem.$.tradeUuid, orginalUuid);
        for (int i = 0; i < itemList.size(); i++) {
            TradeItem item = itemList.get(i);
            TradeItemExtraDinner seat = mapTradeItemSeats.get(item.getUuid());

            //对接KDS
            item.setRelateTradeItemId(item.getId());
            item.setRelateTradeItemUuid(item.getUuid());
            //add end;

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

        // 更改parentUuid、relateTradeItemUuid
        for (TradeItem item : itemList) {
            if (!TextUtils.isEmpty(item.getParentUuid())) {
                item.setParentId(null);
                item.setParentUuid(itemUuidMap.get(item.getParentUuid()));
            }
            //对接KDS 需要用到一下字段数据，update by dzb
//            if (!TextUtils.isEmpty(item.getRelateTradeItemUuid())) {
//                item.setRelateTradeItemId(null);
//                item.setRelateTradeItemUuid(itemUuidMap.get(item.getRelateTradeItemUuid()));
//            }
        }

        // 更改TradeItemProperty的外键
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

        // 更改退菜原因TradeReasonRel
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

        // 将原单删除
        orginalTrade.validateUpdate();
        orginalTrade.setStatusFlag(StatusFlag.INVALID);

        TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
        TradeVo destTradeVo = tradeDal.findTrade(destTrade);
        TradeCustomer tradeCustomer = CustomerManager.getInstance().getValidMemberOrCardCustomer(destTradeVo.getTradeCustomerList());
        if (tradeCustomer != null) {
            tradeCustomer.levelId = levelId;
        }
        TradeVo orginalTradeVo = tradeDal.findTrade(orginalTrade);
        //原单的菜品列表
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
        //合并原单、新单的shopcartItem
        if (destShopcartItemList == null) {
            destShopcartItemList = new ArrayList<IShopcartItem>();
        }
        CustomerResp customer = DinnerCashManager.getTradeVoCustomer(destTradeVo);
        boolean isDestHasBanquet = BaseShoppingCart.isHasValidBanquet(destTradeVo);
        if (originalShopcatItemList != null) {
            destShopcartItemList.addAll(originalShopcatItemList);
            //重新计算合并的目标单的会员价
            for (IShopcartItem iShopcartItem : originalShopcatItemList) {
                //目标单有宴请不带入会员，因为会员价和宴请不共存
                if (customer != null && !isDestHasBanquet) {
                    DinnerShoppingCart.getInstance().setDishMemberPrivilege(destTradeVo, iShopcartItem, customer, false);
                }
            }
        }

        // 修改人数
        Dao<TradeTable, String> tradeTableDao = helper.getDao(TradeTable.class);
        TradeTable orginalTradeTable = tradeTableDao.queryForId(orginal.getUuid());
        TradeTable destTradeTable = tradeTableDao.queryForId(dest.getUuid());
        Integer orginalPeopleCount = orginalTradeTable.getTablePeopleCount();
        if (orginalPeopleCount == null) {
            orginalPeopleCount = 0;
        }
        // 修改目标桌台上的就餐人数
        Integer destCount = destTradeTable.getTablePeopleCount();
        if (destCount == null) {
            destCount = 0;
        }
        destTradeTable.setTablePeopleCount(destCount + orginalPeopleCount);
        // 修改目标单的人数
        Integer destSum = destTrade.getTradePeopleCount();
        if (destSum == null) {
            destSum = 0;
        }
        destSum = destSum + orginalPeopleCount;
        destTrade.setTradePeopleCount(destSum);
        //用于本地计算
        destTradeVo.getTrade().setTradePeopleCount(destSum);
        destTrade.validateUpdate();
        //重新计算价格
        MathShoppingCartTool.mathTotalPrice(destShopcartItemList, destTradeVo);
        BaseShoppingCart.CheckGiftCouponIsActived(destShopcartItemList, destTradeVo);
        Trade trade = destTradeVo.getTrade();
        destTrade.setSaleAmount(trade.getSaleAmount());
        destTrade.setTradeAmount(trade.getTradeAmount());

        // 只上传有变动的记录（即被合到新单的记录）
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
                        //上传的是新建的tradeItem，uuid需要关联到新创建的
                        tradePrivilege.setTradeItemUuid(itemUuidMap.get(item.getUuid()));
                    }
                    tradePrivileges.add(tradePrivilege);
                }
            }
        }

        //优惠劵
        if (Utils.isNotEmpty(destTradeVo.getCouponPrivilegeVoList())) {
            for (CouponPrivilegeVo couponPrivilegeVo : destTradeVo.getCouponPrivilegeVoList()) {
                TradePrivilege tp = couponPrivilegeVo.getTradePrivilege();
                if (tp.getId() != null || tp.getStatusFlag() == StatusFlag.VALID) {
                    tp.setTradeId(destTradeVo.getTrade().getId());
                    tradePrivileges.add(tp);
                }
            }
        }
        //宴请
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

        // 原桌台上只有一单时将原桌台状态改为空桌
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

        //设置原单菜品操作记录和目标单菜品uuid的关联关系
        Set<Long> tradeItemIds = tradeItemRelations.keySet();
        if (Utils.isNotEmpty(tradeItemIds)) {
            //原单菜品的操作记录
            /*PrintOperationDal printOperationDal = OperatesFactory.create(PrintOperationDal.class);
            List<TradeItemOperation> tradeItemOperations = printOperationDal.findTradeItemOperationByTradeItemIds(tradeItemIds);
            if (Utils.isNotEmpty(tradeItemOperations)) {
                List<MergeDinnerReq.OperationRelation> relations = new ArrayList<MergeDinnerReq.OperationRelation>();
                for (TradeItemOperation tradeItemOperation : tradeItemOperations) {
                    Long srcTradeItemId = tradeItemOperation.getTradeItemId();
                    String tgtTradeItemUuid = tradeItemRelations.get(srcTradeItemId);
                    MergeDinnerReq.OperationRelation relation = new MergeDinnerReq.OperationRelation(tradeItemOperation.getId(), tgtTradeItemUuid);
                    relations.add(relation);
                }
                req.setTradeItemOperations(relations);
            }*/
        }
        AuthLogManager.getInstance().flush(OrderActionEnum.JOIN_TABLES, orginalTrade.getId(), orginalTrade.getUuid(), orginalTrade.getClientUpdateTime());
        return req;
    }

    /*@Override
    public void changeTradeItemPrintStatus(List<ItemStaus> itemStausList, ResponseListener<TradeItemResp> listener) {
        String url = ServerAddressUtil.getInstance().changePrintStatus();
        ChangeTradeItemPrintStatusReq req = new ChangeTradeItemPrintStatusReq();
        req.setTradeItemList(itemStausList);

        OpsRequest.Executor<ChangeTradeItemPrintStatusReq, TradeItemResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradeItemResp.class)
                .responseProcessor(new ChangeTradeItemPrintStatusProcessor())
                .execute(listener, "changeTradeItemPrintStatus");
    }*/

    @Override
    public void weixinRefund(TradeVo tradeVo, Reason reason, ResponseListener<TradePaymentResp> listener) {
        /*String url = ServerAddressUtil.getInstance().weixinRefundUrl();
        tradeVo.getTrade().validateUpdate();
        TradeOpsReq req = toRefundToTradeOpsReq(tradeVo.getTrade(), TradeStatus.TOREFUND, reason);
        OpsRequest.Executor<TradeOpsReq, TradePaymentResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradePaymentResp.class)
                .responseProcessor(new TradePaymentRespProcessor())
                .execute(listener, "toRefund");*/
    }

    @Override
    public void tradeSplitDinner(TradeVo source, TradeVo target, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().tradeSplitUrl();
        source.getTrade().validateUpdate();
        target.getTrade().validateCreate();

        TradeSplitReq req = new TradeSplitReq();
        TradeReq tradeSourceReq = toTradeReq(source, true);
        TradeReq tradeTargetReq = toTradeReq(target);
        req.setSource(tradeSourceReq);
        req.setTarget(tradeTargetReq);

        OpsRequest.Executor<TradeSplitReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(TradeResp.class).responseProcessor(new TradeRespProcessor()).execute(
                listener, "tradeSplitDinner");*/
    }

    @Override
    public void tradeSplitPayDinner(TradeVo source, TradeVo target, List<PaymentVo> paymentVoList,
                                    Map<String, String> memberPasswords, ResponseListener<PaymentResp> listener) {
        /*String url = ServerAddressUtil.getInstance().tradeSplitPayUrl();
        source.getTrade().validateUpdate();
        target.getTrade().validateCreate();
        TradeSplitPayReq splitPayReq = new TradeSplitPayReq();
        splitPayReq.setSource(toTradeReq(source, true));

        TradePaymentReq req = new TradePaymentReq();
        TradeReq tradeReq = toTradeReq(target);
        PaymentReq paymentReq = toPaymentReq(target, paymentVoList, memberPasswords);
        req.setTrade(tradeReq);
        req.setPayment(paymentReq);
        splitPayReq.setTarget(req);

        OpsRequest.Executor<TradeSplitPayReq, PaymentResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(splitPayReq)
                .responseClass(PaymentResp.class)
                .responseProcessor(new PaymentRespProcessor())
                .execute(listener, "tradeSplitAndPayDinner");*/
    }

    @Override
    public void tradeRepay(TradeVo tradeVo, Reason reason, ResponseListener<PaymentResp> listener) {
        /*TradeRepayReq tradeRepayReq = toTradeRepayReq(tradeVo, reason);
        boolean interceptEnable = Snack.isOfflineTrade(tradeVo);
        String url = ServerAddressUtil.getInstance().tradeRepayUrl();
        OpsRequest.Executor<TradeRepayReq, PaymentResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(tradeRepayReq)
                .responseClass(PaymentResp.class)
                .responseProcessor(new PaymentRespProcessor())
                .interceptEnable(interceptEnable)
                .execute(listener, "tradeRepayDinner");*/
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
        /*String url = ServerAddressUtil.getInstance().modifyServiceDish();
        OpsRequest.Executor<DishServiceReq, TradeItemResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(dishServiceReq)
                .responseClass(TradeItemResp.class)
                .responseProcessor(new TradeItemRespProcessor())
                .execute(listener, "modifyServiceDish");*/
    }

    @Override
    public void modifyServiceDishV2(DishServiceV2Req dishServiceReq, CalmResponseListener<ResponseObject<DishServiceV2Resp>> listener) {
        /*CalmNetWorkRequest.with(BaseApplication.sInstance)
                .url(ServerAddressUtil.getInstance().modifyServiceDishV2())
                .requestContent(dishServiceReq)
                .responseClass(DishServiceV2Resp.class)
                .responseProcessor(new CalmDatabaseProcessor<DishServiceV2Resp>() {
                    @Override
                    protected void transactionCallable(DatabaseHelper helper, DishServiceV2Resp resp) throws Exception {
                        DBHelperManager.saveEntities(helper, TradeItem.class, resp.tradeItems);
                        DBHelperManager.saveEntities(helper, TradeExtra.class, resp.tradeExtras);
                        DBHelperManager.saveEntities(helper, KdsTradeItem.class, resp.kdsTradeItems);
                        DBHelperManager.saveEntities(helper, KdsTradeItemPart.class, resp.kdsTradeItemParts);
                    }
                })
                .successListener(listener)
                .errorListener(listener)
                .tag("modifyServiceDishV2")
                .create();*/
    }

    @Override
    public void doCloseBill(CloseBillReq closeBillReq, ResponseListener<CloseBillResp> listener) {
        /*String url = ServerAddressUtil.getInstance().doCloseBill();
        OpsRequest.Executor<CloseBillReq, CloseBillResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(closeBillReq).responseClass(CloseBillResp.class).timeout(30000).execute(listener,
                "doCloseBill");*/
    }


    @Override
    public void queryHandOverHistory(ResponseListener<ClosingHandOverResp> listener) {
        /*String url = ServerAddressUtil.getInstance().queryCloseHandoverhistory();
        OpsRequest.Executor<NullReq, ClosingHandOverResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(new NullReq()).responseClass(ClosingHandOverResp.class).timeout(30000).execute(listener,
                "queryHandoverHistory");*/
    }

    @Override
    public void queryHandOverHistory(ClosingReq req, ResponseListener<ClosingHandOverResp> listener) {
        /*String url = ServerAddressUtil.getInstance().queryCloseHandoverhistory();
        OpsRequest.Executor<ClosingReq, ClosingHandOverResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(ClosingHandOverResp.class).timeout(30000).execute(listener,
                "queryHandoverHistory");*/
    }

    @Override
    public void closeDetail(CloseDetailReq closeDetailReq, ResponseListener<CloseBillDataInfo> listener) {
        /*String url = ServerAddressUtil.getInstance().doCloseDetail();
        OpsRequest.Executor<CloseDetailReq, CloseBillDataInfo> executor = OpsRequest.Executor.create(url);
        executor.requestValue(closeDetailReq).responseClass(CloseBillDataInfo.class).timeout(15000).execute(listener,
                "docloseDetail");*/
    }

    @Override
    public void closeDetail(TransferReq transferReq, ResponseListener<TransferCloseBillData> listener) {
        /*String url = ServerAddressUtil.getInstance().mindTransfer();
        OpsRequest.Executor<TransferReq, TransferCloseBillData> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq).responseClass(TransferCloseBillData.class).timeout(30000).execute(listener,
                "doTransferCloseBill");*/
    }


    @Override
    public void queryCloseHistoryList(CloseHistoryReq closeHistoryReq, ResponseListener<CloseHistoryResp> listener) {
        /*String url = ServerAddressUtil.getInstance().queryCloseHistoryList();
        OpsRequest.Executor<CloseHistoryReq, CloseHistoryResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(closeHistoryReq).responseClass(CloseHistoryResp.class).execute(listener,
                "queryCloseHistory");*/
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

    /**
     * 将TradeVo转成TradeReq
     */
    public static TradeReq toTradeReq(TradeVo tradeVo) {
        return toTradeReq(tradeVo, false);
    }


    /**
     * 将AdditemVo转换未AddItemReq
     */
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

    /**
     * 将TradeVo转成TradeReq
     *
     * @param isFilterChangeAndPrint 为true时保留有改动（包括新增和删除）的菜和需要打印的菜（打印状态为ISSUING
     *                               ）
     */
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
        // 营销活动
        if (Utils.isNotEmpty(tradeVo.getTradePlanActivityList())) {
            tradePlanActivities.addAll(tradeVo.getTradePlanActivityList());
        }
        if (Utils.isNotEmpty(tradeVo.getTradeItemPlanActivityList())) {
            tradeItemPlanActivities.addAll(tradeVo.getTradeItemPlanActivityList());
        }

        // 会员
        if (Utils.isNotEmpty(tradeVo.getTradeCustomerList())) {
            // 由于后台统计需要下单顾客(customerType为1的记录)，所以有登录会员时，如果没有下单顾客，就用登录会员补上
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

        // 优惠
        if (tradeVo.getTradePrivileges() != null) {
            for (TradePrivilege tp : tradeVo.getTradePrivileges()) {
                tp.setTradeId(tradeVo.getTrade().getId());
                tradePrivileges.add(tp);
            }
        }
        //优惠劵
        if (tradeVo.getCouponPrivilegeVoList() != null) {
            for (CouponPrivilegeVo couponPrivilegeVo : tradeVo.getCouponPrivilegeVoList()) {
                if (couponPrivilegeVo != null) {
                    TradePrivilege tp = couponPrivilegeVo.getTradePrivilege();
//                    if (tp.getId() != null || tp.getStatusFlag() == StatusFlag.VALID) {
//                        tp.setTradeId(tradeVo.getTrade().getId());
//                    }
                    if (!TextUtils.isEmpty(tp.getUuid()) || tp.getStatusFlag() == StatusFlag.VALID) {
                        tp.setTradeId(tradeVo.getTrade().getId());
                    }
                    tradePrivileges.add(tp);
                }
            }
        }
        //宴请
        if (tradeVo.getBanquetVo() != null && tradeVo.getBanquetVo().getTradePrivilege() != null) {
            TradePrivilege tp = tradeVo.getBanquetVo().getTradePrivilege();
            if (tp.getId() != null || tp.getStatusFlag() == StatusFlag.VALID) {
                tp.setTradeId(tradeVo.getTrade().getId());
                tradePrivileges.add(tp);
            }
        }
        if (tradeVo.getIntegralCashPrivilegeVo() != null) {
            TradePrivilege tp = tradeVo.getIntegralCashPrivilegeVo().getTradePrivilege();
            //            if (tp.getId() != null || tp.getStatusFlag() == StatusFlag.VALID) {
//                tp.setTradeId(tradeVo.getTrade().getId());
//                tradePrivileges.add(tp);
//            }
            //异步改为使用uuid判断
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

        //设置套餐模版tradeid
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

        // 菜品
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
                    //add 20161102
                    if (tp.getStatusFlag() == StatusFlag.VALID || tp.getId() != null || !TextUtils.isEmpty(tp.getUuid())) {
                        // 无效的商品对应的优惠也置为无效
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
                        // 无效的商品对应的属性也置为无效
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

                //礼品券
                if (tradeItemVo.getCouponPrivilegeVo() != null && tradeItemVo.getCouponPrivilegeVo().getTradePrivilege() != null) {
                    TradePrivilege tp = tradeItemVo.getCouponPrivilegeVo().getTradePrivilege();
                    if (tradeItemVo.getCouponPrivilegeVo().isActived()
                            || tp.getId() != null) {
                        // 无效的商品对应的优惠也置为无效
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

        //过滤
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

        // 过滤掉没有改变的菜和属性，只保留有改动的
        if (isFilterChangeAndPrint) {
            for (int i = tradeItems.size() - 1; i >= 0; i--) {
                // 未修改的
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
            /*
            for (int i = tradeItemOperations.size() - 1; i >= 0; i--) {
                if (!tradeItemOperations.get(i).isChanged()) {
                    tradeItemOperations.remove(i);
                }
            }
            */
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
                    // 处理点菜界面传送后厨时无tradeUuid问题
                    tradeItemPlanActivity.setTradeUuid(tradeVo.getTrade().getUuid());
                }
            }

            //过滤掉未改变的优惠
//            for (int i = tradePrivileges.size() - 1; i >= 0; i--) {
//                if (!tradePrivileges.get(i).isChanged()) {
//                    tradePrivileges.remove(i);
//                }
//            }

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
            if (isFilterChangeAndPrint) {//modify v8.4 过滤没改变的
                if (tradeVo.getTradeExtra().isChanged()) {
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
        //过滤已支付的自助押金 add 20170708
        if (tradeVo.getTrade().getBusinessType() == BusinessType.BUFFET) {
            if (!tradeVo.isPaidTradeposit()) {//如果没有支付，可以修改
                req.setTradeDeposit(tradeVo.getTradeDeposit());
            }
        } else {
            req.setTradeDeposit(tradeVo.getTradeDeposit());
        }
        req.setTradeBuffetPeoples(tradeVo.getTradeBuffetPeoples());
        //菜品打包
        if (tradeVo.getTradeItemExtraList() != null) {
            req.setTradeItemExtras(tradeVo.getTradeItemExtraList());
        }
        req.setTradeGroup(tradeVo.getTradeGroup()); // v 7.15 添加团餐请求
        //v8.1 销售员
        if (isFilterChangeAndPrint) {
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

    /**
     * 转成调账PaymentReq对象
     */
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

    /**
     * 转成PaymentReq对象
     *
     * @param tradeVo
     * @param paymentVoList
     * @return
     */
    private PaymentReq toPaymentReq(TradeVo tradeVo, List<PaymentVo> paymentVoList,
                                    Map<String, String> memberPasswords) {
        PaymentReq req = new PaymentReq();
        List<PaymentTo> paymentToList = new ArrayList<PaymentTo>();
        PaymentType paymentType = null;//add 20160914
        for (PaymentVo vo : paymentVoList) {
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
            to.setPaymentCards(vo.getPaymentCards());// add
            // 20160218
            to.setPaymentItems(itemList);
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
            // 获取反结账后被删除的商品生成两张新单
            List<TradeItemVo> itemVos = new ArrayList<TradeItemVo>();
            for (TradeItemVo itemVo : tradeVoRef.getTradeItemList()) {
                TradeItem tradeItem = itemVo.getTradeItem();
                if (tradeItem.getStatusFlag() == StatusFlag.INVALID && tradeItem.isChanged()) {
                    itemVos.add(itemVo);
                }
            }
            if (!itemVos.isEmpty()) {
                Trade tradeRef = tradeVoRef.getTrade();

                // 创建冲账售货单
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

                // 根据冲账售货单克隆生成冲账退货单
                Trade tradeRefund = new Trade();
                copyProperties(tradeSell, tradeRefund);
                tradeRefund.setUuid(SystemUtils.genOnlyIdentifier());
                tradeRefund.setTradeType(TradeType.REFUND_FOR_REVERSAL);
                tradeRefund.setTradeStatus(TradeStatus.FINISH);
                tradeRefund.setTradePayStatus(TradePayStatus.REFUNDED);

                TradeVo tradeVoRefund = new TradeVo();
                tradeVoRefund.setTrade(tradeRefund);
                tradeVoRefund.setTradeItemList(new ArrayList<TradeItemVo>());

                // 处理商品列表
                Set<String> skuUuids = new HashSet<String>();
                Map<String, String> sellItemUuidMap = new HashMap<String, String>();
                Map<String, String> refundItemUuidMap = new HashMap<String, String>();
                for (TradeItemVo itemVoRef : itemVos) {
                    TradeItem itemRef = itemVoRef.getTradeItem();

                    // 根据反结售货单克隆生成冲账销货单中的商品
                    TradeItem itemSell = new TradeItem();
                    copyProperties(itemRef, itemSell);
                    itemSell.validateCreate();// 此方法会将StatusFlag置为1(有效)
                    itemSell.setId(null);
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
                            ip.validateCreate();// 此方法会将StatusFlag置为1(有效)
                            ip.setId(null);
                            ip.setUuid(SystemUtils.genOnlyIdentifier());
                            ip.setTradeItemId(null);
                            ip.setTradeItemUuid(itemSell.getUuid());
                            ip.setServerCreateTime(null);
                            ip.setServerUpdateTime(null);
                            itemVoSell.getTradeItemPropertyList().add(ip);
                        }
                    }
                    tradeVoSell.getTradeItemList().add(itemVoSell);
                    // 由于actualAmount包含了子菜及加料的金额，所以统计单菜或套餐的actualAmount即可
                    if ((itemSell.getType() == DishType.SINGLE && TextUtils.isEmpty(itemSell.getParentUuid()))
                            || itemSell.getType() == DishType.COMBO) {
                        tradeSell.setSaleAmount(MathDecimal.add(tradeSell.getSaleAmount(), itemSell.getActualAmount()));
                    }

                    // 根据冲账售货单克隆生成冲账退货单中的商品，并将数量与金额取反
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
                    // 记录下原TradeItem与新TradeItem的uuid对应关系
                    sellItemUuidMap.put(itemRef.getUuid(), itemSell.getUuid());
                    refundItemUuidMap.put(itemRef.getUuid(), itemRefund.getUuid());
                }
                // 按之前记录的uuid映射将parentUuid改为新TradeItem的uuid
                for (TradeItemVo itemVo : tradeVoSell.getTradeItemList()) {
                    TradeItem item = itemVo.getTradeItem();
                    item.setParentUuid(sellItemUuidMap.get(item.getParentUuid()));
                }
                for (TradeItemVo itemVo : tradeVoRefund.getTradeItemList()) {
                    TradeItem item = itemVo.getTradeItem();
                    item.setParentUuid(refundItemUuidMap.get(item.getParentUuid()));
                }

                // 计算单据总额
                tradeSell.setTradeAmount(tradeSell.getSaleAmount());
                tradeSell.setDishKindCount(skuUuids.size());
                tradeRefund.setSaleAmount(MathDecimal.negate(tradeSell.getSaleAmount()));
                tradeRefund.setTradeAmount(MathDecimal.negate(tradeSell.getTradeAmount()));
                tradeRefund.setDishKindCount(tradeSell.getDishKindCount());
                //yutang modify 20170309 begin 直接从缓存获取数据
               /* PaymentModeDal dal = OperatesFactory.create(PaymentModeDal.class);
                String payModeName = dal.findPaymentModeNameByErpModeId(PayModeId.CASH.value());*/
                String payModeName = PaySettingCache.getPayModeNameByModeId(PayModeId.CASH.value());
                //yutang modify 20170309  end 直接从缓存获取数据
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

    /**
     * 转成正餐支付对象
     *
     * @Title: toPayDinnerReq
     * @Description:
     * @Param @param tradeVo
     * @Param @param paymentVoList
     * @Param @param memberPasswords
     * @Param @return
     * @Return PayDinnerReq 返回类型
     */
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

    /**
     * 获得第一个桌台
     *
     * @param tradeVo
     */
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

    /**
     * 转成TradeOpsReq对象
     *
     * @param trade
     * @param tradeStatus
     * @param reason
     * @return
     */
    private TradeOpsReq toTradeOpsReq(Trade trade, TradeStatus tradeStatus, Reason reason) {
        TradeOpsReq req = new TradeOpsReq();
        req.setTradeId(trade.getId());
        req.setTradeUuid(trade.getUuid());
        req.setTradeStatus(tradeStatus);
        req.setClientCreateTime(trade.getClientCreateTime());
        req.setClientUpdateTime(System.currentTimeMillis());
        req.setServerUpdateTime(trade.getServerUpdateTime());
        // 接受订单时设置流水号 正餐接受不需要流水号
        /*if (TradeStatus.CONFIRMED == tradeStatus && trade.getBusinessType() != BusinessType.DINNER) {
            req.setSerialNumber(PrHelper.getDefault().getSerialNumber());
		}*/
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

    /**
     * TradeRefundReq
     *
     * @param relateTrade 原单
     * @param reason      reason
     * @return
     */
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

    /**
     * 转成deliveredPaymentReq对象
     *
     * @param tradeVoList
     * @return
     */
    private DeliveredPaymentReq toDeliveredPaymentReq(List<TradeVo> tradeVoList) {
        DeliveredPaymentReq deliveredPaymentReq = new DeliveredPaymentReq();
        List<PaymentReq> deliveredPayments = new ArrayList<PaymentReq>();
        deliveredPaymentReq.setDeliveredPayments(deliveredPayments);
        for (TradeVo tradeVo : tradeVoList) {
            DeliveredPayment req = new DeliveredPayment();
            List<PaymentTo> paymentToList = new ArrayList<PaymentTo>();
            if (tradeVo.getTrade().getTradePayStatus() == TradePayStatus.PAID) {// 已支付
                req.setDeliveredStatus(1);
            } else {// 未支付，创建收银
                req.setDeliveredStatus(0);
                PaymentTo paymenTo = new PaymentTo();
                paymenTo.validateCreate();
                Trade trade = tradeVo.getTrade();
                paymenTo.setReceivableAmount(trade.getTradeAmount());// 可收金额
                paymenTo.setActualAmount(trade.getTradeAmount());// 实际收金额
                paymenTo.setExemptAmount(BigDecimal.valueOf(0));// 抹零金额
                paymenTo.setRelateUuid(trade.getUuid());// 主单uuid
                paymenTo.setRelateId(trade.getId());// 关联id
                paymenTo.setPaymentType(PaymentType.TRADE_SELL);// 交易支付
                paymenTo.setUuid(SystemUtils.genOnlyIdentifier());
                paymenTo.setCreatorId(trade.getCreatorId());// 收银员id
                paymenTo.setCreatorName(trade.getCreatorName());// 收银员名字
                paymenTo.setUpdatorId(trade.getCreatorId());// 收银员id
                paymenTo.setUpdatorName(trade.getCreatorName());// 收银员名字
                // 现金方式
                List<PaymentItemTo> paymentItemList = new ArrayList<PaymentItemTo>();
                PaymentItemTo paymentItem = new PaymentItemTo();
                paymentItem.validateCreate();
                paymentItem.setUuid(SystemUtils.genOnlyIdentifier());
                paymentItem.setPaymentUuid(paymenTo.getUuid());
                paymentItem.setPayModeId(PayModeId.CASH.value());
                paymentItem.setPayModelGroup(PayModelGroup.CASH);
                paymentItem.setFaceAmount(trade.getTradeAmount());// 票面金额
                paymentItem.setChangeAmount(BigDecimal.valueOf(0));// 找零金额
                paymentItem.setUsefulAmount(trade.getTradeAmount());// 实付款
                paymentItem.setCreatorId(trade.getCreatorId());// 收银员id
                paymentItem.setCreatorName(trade.getCreatorName());// 收银员名字
                paymentItemList.add(paymentItem);
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
        DBHelperManager.saveEntities(helper, TradePromotion.class, resp.getTradePromotions());//add 20161117
        DBHelperManager.saveEntities(helper, Trade.class, resp.getTrades());
        DBHelperManager.saveEntities(helper, TradeReceiveLog.class, resp.getTradeReceiveLogs());
        DBHelperManager.saveEntities(helper, TradeBuffetPeople.class, resp.getTradeBuffetPeoples());
        DBHelperManager.saveEntities(helper, TradeBuffetPeople.class, resp.getBuffetPeoples());
        DBHelperManager.saveEntities(helper, TradeGroupInfo.class, resp.getTradeGroup()); // v7.15 添加团餐信息表
        DBHelperManager.saveEntities(helper, TradeUser.class, resp.getTradeUser()); // v8.1 添加销售员
        DBHelperManager.saveEntities(helper, TradeUser.class, resp.getTradeUsers()); // v8.10 补充
        DBHelperManager.saveEntities(helper, TradeMainSubRelation.class, resp.getTradeMainSubRelations()); // v8.3 主单与子单关联关系
        DBHelperManager.saveEntities(helper, TradeItemMainBatchRel.class, resp.getTradeItemMainBatchRels()); // v8.3 联台批量菜关联
        DBHelperManager.saveEntities(helper, TradeItemMainBatchRelExtra.class, resp.getTradeItemMainBatchRelExtras());
        DBHelperManager.saveEntities(helper, TradeItemExtraDinner.class, resp.getTradeItemExtraDinners());
        DBHelperManager.saveEntities(helper, TradeTax.class, resp.getTradeTaxs());
        DBHelperManager.saveEntities(helper, TradeInitConfig.class, resp.getTradeInitConfigs());
    }

    /**
     * 将TradeResp保存到数据库的处理器
     *
     * @version: 1.0
     * @date 2015年4月15日
     */
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
                // 生成一个新的流水号
                //PrHelper.getDefault().generateSerialNumber();
            }
            return super.process(response);
        }

        @Override
        public void saveToDatabase(TradeResp resp)
                throws Exception {
            super.saveToDatabase(resp);
            //saveBaseInfo(Customer.class, resp.getCustomers());
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

    /**
     * 将PaymentResp保存到数据库的处理器
     *
     * @version: 1.0
     * @date 2015年4月15日
     */
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
                    DBHelperManager.saveEntities(helper, PaymentItemGroupon.class, resp.getPaymentItemGroupons());//add 20160927
                    DBHelperManager.saveEntities(helper, TradeDeposit.class, resp.getTradeDeposit());//add 20160927
                    DBHelperManager.saveEntities(helper, TradeItemExtraDinner.class, resp.getTradeItemExtraDinners());//add 20170927
                    DBHelperManager.saveEntities(helper, TradeUser.class, resp.getTradeItemUsers());//add 20170927
                    DBHelperManager.saveEntities(helper, TradePrivilegeLimitNumCard.class, resp.getTradePrivilegeLimitNumCards());//add 20170927
//                    DBHelperManager.saveEntities(helper, TradePrivilegeLimitNumCardSku.class, resp.getTradePrivilegeLimitNumCardSkus());//add 20170927
                    DBHelperManager.saveEntities(helper, TradePrivilegeApplet.class, resp.getTradePrivilegeApplets());//add 20170927

                    return null;

                }
            };
        }

    }

    /**
     * 将TradePaymentResp保存到数据库的处理器
     *
     * @version: 1.0
     * @date 2015年4月22日
     */
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
                // 生成一个新的流水号
                //PrHelper.getDefault().generateSerialNumber();
            }
            return super.process(response);
        }

        @Override
        public void saveToDatabase(TradePaymentResp resp)
                throws Exception {
            super.saveToDatabase(resp);
            //saveBaseInfo(Customer.class, resp.getCustomers());
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
                   /* DBHelperManager.saveEntities(helper, TradePlanActivity.class, resp.getTradePlanActivitys());
                    DBHelperManager.saveEntities(helper, TradeItemPlanActivity.class, resp.getTradeItemPlanActivitys());*///removed 20170221
                    DBHelperManager.saveEntities(helper, PaymentItemGroupon.class, resp.getPaymentItemGroupons());//add 20160927
                    return null;
                }
            };
        }

    }

    /**
     * 同步第三方支付保存到数据库
     *
     * @Title: saveVerifyPayResp
     * @Description: TODO
     * @Param @param resp TODO
     * @Return void 返回类型
     */
    public static void saveVerifyPayResp(VerifyPayResp resp) {
        try {
            new VerifyPayRespProcessor().saveToDatabase(resp);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static PaymentRespProcessor getPaymentRespProcessor() {

        return new PaymentRespProcessor();
    }

    public static TradeRespProcessor getTradeRespProcessor() {

        return new TradeRespProcessor();
    }

    /**
     * 将VerifyPayResp保存到数据库的处理器
     *
     * @version: 1.0
     * @date 2015年6月5日
     */
    private static class VerifyPayRespProcessor extends SaveDatabaseResponseProcessor<VerifyPayResp> {

        private final TradePaymentRespProcessor mProcessor;

        VerifyPayRespProcessor() {
            mProcessor = new TradePaymentRespProcessor();
        }

        @Override
        public void saveToDatabase(VerifyPayResp resp)
                throws Exception {
            // 支付成功才会有Trade和Payment记录
            /*DatabaseHelper helper = DBHelperManager.getHelper();
            DBHelperManager.saveEntities(helper, PrintOperation.class, resp.getPrintOperations());*/ //modify 20170221 移除重复支付
            if (resp.isPaid()) {
                super.saveToDatabase(resp);
            }
        }

        @Override
        protected Callable<Void> getCallable(DatabaseHelper helper, VerifyPayResp resp) {
            //modify 20170221 统一调用事务保存 begin
           /* try {
                DBHelperManager.saveEntities(helper, PrintOperation.class, resp.getPrintOperations());
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            //modify 20170221 统一调用事务保存 end
            return mProcessor.getCallable(helper, resp);
        }

    }

    private static class AnonymousCardStoreRespProcessor extends SaveDatabaseResponseProcessor<AnonymousCardStoreResp> {

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final AnonymousCardStoreResp resp) throws Exception {
            //modify 20170221 统一调用事务保存 begin
           /* try {
                DBHelperManager.saveEntities(helper, PrintOperation.class, resp.getPrintOperations());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;*/
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
            //modify 20170221 统一调用事务保存 end
        }
    }

    /**
     * 将TransferDinnertableResp保存到数据库的处理器
     *
     * @version: 1.0
     * @date 2015年9月23日
     */
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

    /**
     * @Date：2015年11月23日 @Description:处理修改上菜状态返回数据
     * @Version: 1.0
     * <p>
     * rights reserved.
     */
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

    /**
     * @Description:处理更新支付状态的保存到数据库
     * @Version: 1.0
     * <p>
     * rights reserved.
     */
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
        /*String url = ServerAddressUtil.getInstance().queryModuleItem();
        ModuleItemReq moduleItemReq = new ModuleItemReq("paymentItem", uuid);
        OpsRequest.Executor<ModuleItemReq, PaymentItem> executor = OpsRequest.Executor.create(url);
        executor.requestValue(moduleItemReq).responseClass(PaymentItem.class).execute(listener, "queryPaymentItem");*/
    }

    @Override
    public void changeRefundStatus(RefundStatusReq req, ResponseListener<RefundStatusResp> listener) {
        /*String url = ServerAddressUtil.getInstance().newChangeRefundStatus();
        OpsRequest.Executor<RefundStatusReq, RefundStatusResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(RefundStatusResp.class)
                .responseProcessor(new UnionpayRefundStatusRespProcessor())
                .execute(listener, "changeRefundStatus");*/
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

    /*@Override
    public void updatePrinted(UpdatePrintedReq req, ResponseListener<PrintOperation> listener) {
        String url = ServerAddressUtil.getInstance().updatePrinted();
        OpsRequest.Executor<UpdatePrintedReq, PrintOperation> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(PrintOperation.class)
                .responseProcessor(new PrintOperationProcessor())
                .execute(listener, "updatePrinted");
    }*/

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
        /*ReturnConfirmReq req = toReturnConfirmReq(tradeId, returnStatus, requestUuid, reason);
        req.setReviseStock(isReturnInventory);
        String url = ServerAddressUtil.getInstance().returnConfirm();
        OpsRequest.Executor<ReturnConfirmReq, Object> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(Object.class).execute(listener, "returnConfirm");*/
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
        // 收银员
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
        /*String url = ServerAddressUtil.getInstance().remindDish();
        tradeItemOperation(url, trade, tradeItemIds, listener, "remindDish");*/
    }

    @Override
    public void riseDish(Trade trade, List<Long> tradeItemIds, ResponseListener<TradeItemResp> listener) {
        /*String url = ServerAddressUtil.getInstance().riseDish();
        tradeItemOperation(url, trade, tradeItemIds, listener, "riseDish");*/
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
        /*String url = ServerAddressUtil.getInstance().optDish();
        //AuthUser user = AuthUserCache.getAuthUser();
        OperationDishReq req = new OperationDishReq();
        req.setTradeId(tradeId);
        List<TradeItemOperation> optList = new ArrayList<>();
        for (DishDataItem item : selectedItems) {
            if ((item.getType() == ItemType.WEST_CHILD || item.getType() == ItemType.CHILD) && item.getBase() != null
                    && item.getBase().getTradeItemOperations() != null) {

                for (TradeItemOperation operation : item.getBase().getTradeItemOperations()) {
                    if (item.getBase().getId() != null)
                        operation.setTradeItemId(item.getBase().getId());
                    optList.add(operation);
                }

            } else if ((item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO)
                    && item.getItem() != null && item.getItem().getTradeItemOperations() != null) {

                for (TradeItemOperation operation : item.getItem().getTradeItemOperations()) {
                    if (item.getItem().getId() != null)
                        operation.setTradeItemId(item.getItem().getId());
                    optList.add(operation);
                }

                if (item.getType() == ItemType.COMBO) {
                    IShopcartItem iShopcartItem = item.getItem();
                    List<? extends ISetmealShopcartItem> iSetmealShopcartItems = iShopcartItem.getSetmealItems();
                    for (ISetmealShopcartItem iSetmealShopcartItem : iSetmealShopcartItems) {
                        if (iSetmealShopcartItem != null && iSetmealShopcartItem.getTradeItemOperations() != null) {
                            for (TradeItemOperation operation : iSetmealShopcartItem.getTradeItemOperations()) {
                                if (iSetmealShopcartItem.getId() != null)
                                    operation.setTradeItemId(iSetmealShopcartItem.getId());
                                optList.add(operation);
                            }
                        }
                    }
                }

            }
        }
        req.setTradeItemOperations(optList);
        OpsRequest.Executor<OperationDishReq, TradeItemResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseProcessor(new TradeItemRespProcessor()).responseClass(TradeItemResp.class).execute(listener, "operationDish");*/
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
        // TODO Auto-generated method stub
        /*TradePayStateReq payStateReq = new TradePayStateReq();
        payStateReq.setTradeId(tradeVo.getTrade().getId());
        TradePayStatus tradePayStatus = tradeVo.getTrade().getTradePayStatus();
        String url = ServerAddressUtil.getInstance().refreshReturnStateUrl();

        OpsRequest.Executor<TradePayStateReq, TradePayStateResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(payStateReq)
                .responseClass(TradePayStateResp.class)
                .responseProcessor(new TradePayStateResqProcessor())
                .execute(listener, "refreshReturnState");*/
    }

    @Override
    public void dinnerSetTableAndAccept(TradeVo tradeVo, Tables table, Bool genBatchNo, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().dinnerSetTableAndAccept();

        Trade trade = tradeVo.getTrade();
        TradeExtra tradeExtra = tradeVo.getTradeExtra();
        AuthUser user = Session.getAuthUser();
        List<TradeTable> tradeTables = new ArrayList<TradeTable>();
        TradeTable tradeTable = new TradeTable();
        if (Utils.isNotEmpty(tradeVo.getTradeTableList())) {
            // 拷贝原来tradetable的id和uuid
            TradeTable oldTradeTable = tradeVo.getTradeTableList().get(0);
            tradeTable.setId(oldTradeTable.getId());
            tradeTable.setUuid(oldTradeTable.getUuid());
        } else {
            tradeTable.setUuid(SystemUtils.genOnlyIdentifier());
        }
        tradeTable.setTableId(table.getId());
        tradeTable.setTableName(table.getTableName());
        tradeTable.setTradeId(tradeVo.getTrade().getId());
        tradeTable.setTradeUuid(tradeVo.getTrade().getUuid());
        tradeTable.setTablePeopleCount(table.getTablePersonCount());
        tradeTable.setMemo("");
        //设置服务员
        if (user != null) {
            tradeTable.setWaiterId(user.getId());
            tradeTable.setWaiterName(user.getName());
        }
        tradeTable.validateCreate();
        tradeTables.add(tradeTable);
        String serialNumber = "";
        if (tradeExtra != null) {
            serialNumber = tradeExtra.getSerialNumber();
        }
        DinnerSetTableAndAcceptReq req = new DinnerSetTableAndAcceptReq(trade.getId(), trade.getClientUpdateTime(),
                trade.getServerUpdateTime(), serialNumber, tradeTables);
        //设置更新者
        if (user != null) {
            req.setUpdatorId(user.getId());
            req.setUpdatorName(user.getName());
        }
        req.setGenBatchNo(genBatchNo);
        OpsRequest.Executor<DinnerSetTableAndAcceptReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradeResp.class)
                .responseProcessor(new TradeRespProcessor(false))
                .execute(listener, "dinnerSetTableAndAccept");*/
    }

    @Override
    public void modifyPrintStatus(List<TradeItem> tradeItems, List<PrintOperation> printOperations,
                                  List<TradeItemOperation> tradeItemOperations, ResponseListener<TradeItemResp> listener) {
        /*String url = ServerAddressUtil.getInstance().modifyPrintStatus();
        ModifyTradeItemPrintStatusReq req =
                new ModifyTradeItemPrintStatusReq(tradeItems, printOperations, tradeItemOperations);
        OpsRequest.Executor<ModifyTradeItemPrintStatusReq, TradeItemResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradeItemResp.class)
                .responseProcessor(new TradeItemRespProcessor())
                .timeout(20000)//超时改为20秒 add 20170515
                .execute(listener, "modifyPrintStatus");*/
    }

    @Override
    public void modifyMainTradePrintStatus(Long mainTradeId, List<TradeItem> tradeItems, List<PrintOperation> printOperations,
                                           List<TradeItemOperation> tradeItemOperations, ResponseListener<TradeItemResp> listener) {
        /*String url = ServerAddressUtil.getInstance().modifyMainTradePrintStatus();
        MainTradeModifyTradeItemPrintStatusReq req =
                new MainTradeModifyTradeItemPrintStatusReq(mainTradeId, tradeItems, printOperations, tradeItemOperations);
        OpsRequest.Executor<MainTradeModifyTradeItemPrintStatusReq, TradeItemResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradeItemResp.class)
                .responseProcessor(new TradeItemRespProcessor())
                .timeout(20000)//超时改为20秒 add 20170515
                .execute(listener, "modifyPrintStatus");*/
    }

    @Override
    public void modifySubTradePrintStatus(Long mainTradeId, Long subTradeId, List<TradeItem> tradeItems, List<PrintOperation> printOperations,
                                          List<TradeItemOperation> tradeItemOperations, ResponseListener<TradeItemResp> listener) {
        /*String url = ServerAddressUtil.getInstance().modifySubTradePrintStatus();
        SubTradeModifyTradeItemPrintStatusReq req =
                new SubTradeModifyTradeItemPrintStatusReq(mainTradeId, subTradeId, tradeItems, printOperations, tradeItemOperations);
        OpsRequest.Executor<SubTradeModifyTradeItemPrintStatusReq, TradeItemResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradeItemResp.class)
                .responseProcessor(new TradeItemRespProcessor())
                .timeout(20000)//超时改为20秒 add 20170515
                .execute(listener, "modifyPrintStatus");*/
    }

    @Override
    public void tradeFinish(Long tradeId, ResponseListener<TradeFinishResp> listener) {
        /*String url = ServerAddressUtil.getInstance().tradeFinish();
        AuthUser user = Session.getAuthUser();
        TradeFinishReq req = new TradeFinishReq(tradeId, user.getId(), user.getName());
        OpsRequest.Executor<TradeFinishReq, TradeFinishResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradeFinishResp.class)
                .responseProcessor(new TradeFinishRespProcessor())
                .execute(listener, "tradeFinish");*/
    }

    @Override
    public void buffetDepositRefund(Long tradeId, BigDecimal depositRefund, Long paymentItemId, Reason reason, ResponseListener<PayResp> listener) {
        /*String url = ServerAddressUtil.getInstance().buffetBackDepositUrl();
        OpsRequest.Executor<BuffetDepositRefundReq, PayResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(toBuffetDepositRefundReq(tradeId, depositRefund, paymentItemId, reason))
                .responseClass(PayResp.class)
                .responseProcessor(new ReturnDepositRespProcessor())
                .execute(listener, "buffetDepositRefund");*/
    }


    @Override
    public void buffetFinishAndDepositRefund(Long tradeId, BigDecimal depositRefund, Long paymentItemId, Reason reason, ResponseListener<PayResp> listener) {
        /*String url = ServerAddressUtil.getInstance().buffetFinishAndDepositRefund();
        OpsRequest.Executor<BuffetFinishDepositRefundReq, PayResp> executor = OpsRequest.Executor.create(url);

        BuffetFinishDepositRefundReq req = new BuffetFinishDepositRefundReq();
        req.setTradeId(tradeId);
        req.setRefundDeposit(toBuffetDepositRefundReq(tradeId, depositRefund, paymentItemId, reason));
        if (Session.getAuthUser() != null) {
            req.setOperateName(Session.getAuthUser().getName());
            req.setOperateId(Session.getAuthUser().getId());
        }
        executor.requestValue(req)
                .responseClass(PayResp.class)
                .responseProcessor(new ReturnDepositRespProcessor())
                .execute(listener, "buffetDepositRefund");*/
    }

    private BuffetDepositRefundReq toBuffetDepositRefundReq(Long tradeId, BigDecimal depositRefund, Long paymentItemId, Reason reason) {
        BuffetDepositRefundReq refundReq = new BuffetDepositRefundReq();
        // 收银员
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
        /*String url = ServerAddressUtil.getInstance().backDepositUrl();
        OpsRequest.Executor<DepositRefundReq, DepositRefundResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(toDepositRefundReq(tradeId, depositRefund, payModeId, reason))
                .responseClass(DepositRefundResp.class)
                .responseProcessor(new DepositRefundProcessor())
                .execute(listener, "depositRefund");*/
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
        /*if (Utils.isEmpty(sourceTradeVo.getTradeItemList())) {
            return;
        }

        String url = ServerAddressUtil.getInstance().moveDish();
        *//*
         * List<TradeItem> tradeItems = new
         * ArrayList<TradeItem>(); for(TradeItemVo
         * tradeItemVo : sourceTradeVo.getTradeItemList()){
         * tradeItems.add(tradeItemVo.getTradeItem()); }
         *//*
        List<TradePrivilege> tradePrivilegeList = sourceTradeVo.getTradePrivileges();
        if (tradePrivilegeList == null) {
            tradePrivilegeList = new ArrayList<TradePrivilege>();
            sourceTradeVo.setTradePrivileges(tradePrivilegeList);
        }

        if (sourceTradeVo.getIntegralCashPrivilegeVo() != null && sourceTradeVo.getIntegralCashPrivilegeVo().getTradePrivilege() != null) {
            tradePrivilegeList.add(sourceTradeVo.getIntegralCashPrivilegeVo().getTradePrivilege());
        }

        if (sourceTradeVo.getBanquetVo() != null && sourceTradeVo.getBanquetVo().getTradePrivilege() != null) {
            tradePrivilegeList.add(sourceTradeVo.getBanquetVo().getTradePrivilege());
        }
        if (Utils.isNotEmpty(sourceTradeVo.getCouponPrivilegeVoList())) {
            for (CouponPrivilegeVo couponPrivilegeVo : sourceTradeVo.getCouponPrivilegeVoList()) {
                if (couponPrivilegeVo.getTradePrivilege() != null) {
                    tradePrivilegeList.add(couponPrivilegeVo.getTradePrivilege());
                }
            }
        }

        if (sourceTradeVo.getTradeItemList() != null) {
            for (TradeItemVo tradeItemVo : sourceTradeVo.getTradeItemList()) {
                if (tradeItemVo.getTradeItemPrivilege() != null) {
                    tradePrivilegeList.add(tradeItemVo.getTradeItemPrivilege());
                }
                if (tradeItemVo.isHasCouponPrivileage()) {
                    tradePrivilegeList.add(tradeItemVo.getCouponPrivilegeVo().getTradePrivilege());
                }
            }
        }

        List<TradePrivilege> targetTradePrivilegeList = targetTradeVo.getTradePrivileges();
        if (targetTradePrivilegeList == null) {
            targetTradePrivilegeList = new ArrayList<TradePrivilege>();
            targetTradeVo.setTradePrivileges(targetTradePrivilegeList);
        }

        if (targetTradeVo.getIntegralCashPrivilegeVo() != null && targetTradeVo.getIntegralCashPrivilegeVo().getTradePrivilege() != null) {
            targetTradePrivilegeList.add(targetTradeVo.getIntegralCashPrivilegeVo().getTradePrivilege());
        }

        if (targetTradeVo.getBanquetVo() != null && targetTradeVo.getBanquetVo().getTradePrivilege() != null) {
            targetTradePrivilegeList.add(targetTradeVo.getBanquetVo().getTradePrivilege());
        }
        if (Utils.isNotEmpty(targetTradeVo.getCouponPrivilegeVoList())) {
            for (CouponPrivilegeVo couponPrivilegeVo : targetTradeVo.getCouponPrivilegeVoList()) {
                if (couponPrivilegeVo.getTradePrivilege() != null) {
                    targetTradePrivilegeList.add(couponPrivilegeVo.getTradePrivilege());
                }
            }
        }
        if (targetTradeVo.getTradeItemList() != null) {
            for (TradeItemVo tradeItemVo : targetTradeVo.getTradeItemList()) {
                if (tradeItemVo.getTradeItemPrivilege() != null) {
                    tradeItemVo.getTradeItemPrivilege().setTradeId(targetTradeVo.getTrade().getId());
                    tradeItemVo.getTradeItemPrivilege().setTradeUuid(targetTradeVo.getTrade().getUuid());
                    targetTradePrivilegeList.add(tradeItemVo.getTradeItemPrivilege());
                }

                if (tradeItemVo.isHasCouponPrivileage()) {
                    targetTradePrivilegeList.add(tradeItemVo.getCouponPrivilegeVo().getTradePrivilege());
                }
            }
        }
        MoveDishReq.Source source =
                new MoveDishReq.Source(sourceTradeVo.getTrade(), tradeItems, tradePrivilegeList,
                        sourceTradeVo.getTradePlanActivityList(), sourceTradeVo.getTradeItemPlanActivityList());
        MoveDishReq.Target target = new MoveDishReq.Target(targetTradeVo.getTrade(), targetTradePrivilegeList,
                targetTradeVo.getTradeTableList(), targetTradeVo.getTradeExtra(), targetTradeVo.getTradeItemExtraDinners());
        target.setTradeTaxs(targetTradeVo.getTradeTaxs());
        target.setTradeInitConfigs(targetTradeVo.getTradeInitConfigs());

        Long updatorId = Session.getAuthUser() == null ? null
                : Session.getAuthUser().getId();
        String updatorName = Session.getAuthUser() == null ? null
                : Session.getAuthUser().getName();// 当前操作人的名称

        MoveDishReq req = new MoveDishReq(source, target, updatorId, updatorName, actionType, moveAdd);
        OpsRequest.Executor<MoveDishReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradeResp.class)
                .responseProcessor(new TradeRespProcessor())
                .execute(listener, "moveDish");*/
    }

    @Override
    public void doLag(TradeVo tradeVo, LagReq lagReq, ResponseListener<LagResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getLagUrl();
        TradeReq tradeReq = toTradeReq(tradeVo, true);
        lagReq.setTradeModifyRequest(tradeReq);
        OpsRequest.Executor<LagReq, LagResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(lagReq)
                .responseClass(LagResp.class)
                .responseProcessor(new LagRespProcessor())
                .execute(listener, "doLag");*/
    }


    /**
     * 挂账保存数据到数据库
     */
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
        /*String url = ServerAddressUtil.getInstance().getGoodsSaleRank();
        OpsRequest.Executor<GoodsSellRankPrintReq, GoodsSellRankResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(GoodsSellRankResp.class)
                .execute(listener, "getGoodsSaleRank");*/
    }

    @Override
    public void getGoodsSaleRankPrint(TransferReq<GoodsSellRankPrintReq> req, ResponseListener<MindTransferResp<GoodsSellRankPrintResp>> listener) {
        /*String url = ServerAddressUtil.getInstance().mindTransfer();
        OpsRequest.Executor<TransferReq<GoodsSellRankPrintReq>, MindTransferResp<GoodsSellRankPrintResp>> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseType(OpsRequest.getContentResponseType(MindTransferResp.class, GoodsSellRankPrintResp.class))
                .interceptEnable(true)
                .addRouter("/mind/innerApi/pos/print")
                .execute(listener, "getGoodsSaleRankPrint");*/
    }

    @Override
    public void getBusinessCharge(TransferReq<BusinessChargeReq> req, ResponseListener<MindTransferResp<BusinessChargeResp>> listener) {
        /*String url = ServerAddressUtil.getInstance().mindTransfer();
        OpsRequest.Executor<TransferReq<BusinessChargeReq>, MindTransferResp<BusinessChargeResp>> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseType(OpsRequest.getContentResponseType(MindTransferResp.class, BusinessChargeResp.class)).execute(listener, "getBusinessCharge");*/
    }

    @Override
    public void batchUploadAuthLog(List<AuthorizedLog> authorizedLogList, ResponseListener<AuthLogResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getAuthLogUploadUrl();
        AuthLogReq logReq = new AuthLogReq();
        logReq.setAuthorizedLogs(authorizedLogList);
        OpsRequest.Executor<AuthLogReq, AuthLogResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(logReq)
                .responseClass(AuthLogResp.class)
                .execute(listener, "batchUploadAuthLog");*/

    }

    @Override
    public void acceptAddItem(TradeVo tradeVo, AddItemVo addItemVo, boolean isAcceptAutoTransferOpen, ResponseListener<TradeResp> listener) {
        /*if (addItemVo == null) {
            return;
        }

        String url = ServerAddressUtil.getInstance().acceptAddItem();

        AddItemReq addItemReq = toAddItemReq(addItemVo);
        TradeReq tradeReq = toTradeReq(tradeVo, true);

        addItemReq.setTrade(tradeReq);
        if (isAcceptAutoTransferOpen) {
            addItemReq.genBatchNo = Bool.YES.value();
        } else {
            addItemReq.genBatchNo = Bool.NO.value();
        }

        OpsRequest.Executor<AddItemReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(addItemReq)
                .responseClass(TradeResp.class)
                .responseProcessor(new TradeRespProcessor());

        executor.execute(listener, "acceptAddItem");*/
    }

    @Override
    public void refuseAddItem(AddItemVo addItemVo, ResponseListener<TradeResp> listener) {
        /*if (addItemVo == null) {
            return;
        }
        String url = ServerAddressUtil.getInstance().refuseAddItem();

        AddItemReq addItemReq = toAddItemReq(addItemVo);

        OpsRequest.Executor<AddItemReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(addItemReq)
                .responseClass(TradeResp.class)
                .responseProcessor(new TradeRespProcessor());

        executor.execute(listener, "refuseAddItem");*/
    }

    @Override
    public void sendOrder(DispatchOrderReq req, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getSendOrderUrl();
        OpsRequest.Executor<DispatchOrderReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TradeResp.class)
                .responseProcessor(new TradeRespProcessor())
                .execute(listener, "sendOrder");*/
    }


    @Override
    public void cancelDeliveryOrder(CancelDeliveryOrderReq req, ResponseListener<GatewayTransferResp<CancelDeliveryOrderResp>> listener) {
        /*String transferUrl = ServerAddressUtil.getInstance().getGatewayTransfer();
        String cancelOrderUrl = ServerAddressUtil.getInstance().getCancelDeliveryOrderUrl();
        TransferReq<GatewayTransferReq<CancelDeliveryOrderReq>> transferReq = new TransferReq<GatewayTransferReq<CancelDeliveryOrderReq>>(cancelOrderUrl, new GatewayTransferReq<CancelDeliveryOrderReq>(req));
        OpsRequest.Executor<TransferReq, GatewayTransferResp<CancelDeliveryOrderResp>> executor = OpsRequest.Executor.create(transferUrl);
        executor.requestValue(transferReq)
                .responseType(OpsRequest.getContentResponseType(GatewayTransferResp.class, CancelDeliveryOrderResp.class))
                .responseProcessor(new CancelDeliveryOrderRespProcessor())
                .execute(listener, transferUrl);*/
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
        /*String url = ServerAddressUtil.getInstance().transferDinnertable();
        ExChangeTableReq req = new ExChangeTableReq();
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            req.setUpdatorId(user.getId());
            req.setUpdatorName(user.getName());
        }
        req.setTradeTableId(oldTableTradeVo.getTradeTable().getId());
        req.setTradeTableUpdateTime(oldTableTradeVo.getTradeTable().getServerUpdateTime());

        //原桌台
        DinnertableState orgnalDinnertable = new DinnertableState();
        orgnalDinnertable.setId(oldTableTradeVo.getTable().getId());
        orgnalDinnertable.setModifyDateTime(oldTableTradeVo.getTable().getModifyDateTime());
        orgnalDinnertable.setTableStatus(TableStatus.EMPTY);

        //新桌台
        DinnertableState destDinnertable = new DinnertableState();
        destDinnertable.setId(newTable.getId());
        destDinnertable.setModifyDateTime(newTable.getModifyDateTime());
        destDinnertable.setTableStatus(TableStatus.OCCUPIED);
        req.setTables(Arrays.asList(orgnalDinnertable, destDinnertable));

        req.setFromTableUpdateTime(orgnalDinnertable.getModifyDateTime());
        req.setToTableId(newTable.getId());
        req.setToTableUpdateTime(newTable.getModifyDateTime());
        OpsRequest.Executor<ExChangeTableReq, ExChangeTableResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(ExChangeTableResp.class)
                .responseProcessor(new ExChangeableRespProcessor())
                .execute(listener, "exChangetable");*/
    }

    /**
     * 将ExChangeTableResp保存到数据库的处理器
     */
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
        /*String transferUrl = ServerAddressUtil.getInstance().getGatewayTransfer();
        String invoiceQrcodeUrl = ServerAddressUtil.getInstance().invoiceQrcode();
        TransferReq<GatewayTransferReq<InvoiceQrcodeReq>> transferReq = new TransferReq<GatewayTransferReq<InvoiceQrcodeReq>>(invoiceQrcodeUrl, new GatewayTransferReq<InvoiceQrcodeReq>(req));
        OpsRequest.Executor<TransferReq, GatewayTransferResp<InvoiceQrcodeResp>> executor = OpsRequest.Executor.create(transferUrl);
        executor.requestValue(transferReq)
                .responseType(OpsRequest.getContentResponseType(GatewayTransferResp.class, InvoiceQrcodeResp.class))
                .responseProcessor(new InvoiceQrcodeRespProcessor())
                .execute(listener, invoiceQrcodeUrl);//用gateway的url作为请求的tag*/
    }

    @Override
    public void invoiceFHQrcode(FHInvoiceQrcodeReq req, ResponseListener<GatewayTransferResp<FHInvoiceQrcodeResp>> listener) {
        /*String transferUrl = ServerAddressUtil.getInstance().getGatewayTransfer();
        String invoiceFHQrcodeUrl = ServerAddressUtil.getInstance().invoiceFHQrcode();
        TransferReq<GatewayTransferReq<FHInvoiceQrcodeReq>> transferReq = new TransferReq<GatewayTransferReq<FHInvoiceQrcodeReq>>(invoiceFHQrcodeUrl, new GatewayTransferReq<FHInvoiceQrcodeReq>(req));
        OpsRequest.Executor<TransferReq, GatewayTransferResp<FHInvoiceQrcodeResp>> executor = OpsRequest.Executor.create(transferUrl);
        executor.requestValue(transferReq)
                .responseType(OpsRequest.getContentResponseType(GatewayTransferResp.class, FHInvoiceQrcodeResp.class))
                .execute(listener, invoiceFHQrcodeUrl);//用gateway的url作为请求的tag*/
    }

    /**
     * 峰火查询余额接口
     *
     * @param req
     * @param listener
     */
    @Override
    public void queryBalanceFH(FHQueryBalanceReq req, ResponseListener<GatewayTransferResp<FHQueryBalanceResp>> listener) {
        /*String transferUrl = ServerAddressUtil.getInstance().getGatewayTransfer();
        String queryBalanceFHUrl = ServerAddressUtil.getInstance().queryBalanceFH();
        TransferReq<GatewayTransferReq<FHQueryBalanceReq>> transferReq = new TransferReq<GatewayTransferReq<FHQueryBalanceReq>>(queryBalanceFHUrl, new GatewayTransferReq<FHQueryBalanceReq>(req));
        OpsRequest.Executor<TransferReq, GatewayTransferResp<FHQueryBalanceResp>> executor = OpsRequest.Executor.create(transferUrl);
        executor.requestValue(transferReq)
                .responseType(OpsRequest.getContentResponseType(GatewayTransferResp.class, FHQueryBalanceResp.class))
                .execute(listener, queryBalanceFHUrl);//用gateway的url作为请求的tag*/
    }

    /**
     * 将InvoiceQrcodeResp保存到数据库的处理器
     */
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
        /*String transferUrl = ServerAddressUtil.getInstance().getGatewayTransfer();
        String invoiceRevokeUrl = ServerAddressUtil.getInstance().invoiceRevoke();
        TransferReq<GatewayTransferReq<InvoiceRevokeReq>> transferReq = new TransferReq<GatewayTransferReq<InvoiceRevokeReq>>(invoiceRevokeUrl, new GatewayTransferReq<InvoiceRevokeReq>(req));
        OpsRequest.Executor<TransferReq, GatewayTransferResp<Invoice>> executor = OpsRequest.Executor.create(transferUrl);
        executor.requestValue(transferReq)
                .responseType(OpsRequest.getContentResponseType(GatewayTransferResp.class, Invoice.class))
                .responseProcessor(new InvoiceRevokeRespProcessor())
                .execute(listener, invoiceRevokeUrl);//用gateway的url作为请求的tag*/
    }

    @Override
    public void queryDeliveryFee(QueryDeliveryFeeReq req, ResponseListener<GatewayTransferResp<QueryDeliveryFeeResp>> listener) {
        /*String transferUrl = ServerAddressUtil.getInstance().getGatewayTransfer();
        String queryDeliveryFeeUrl = ServerAddressUtil.getInstance().queryDeliveryFee();
        TransferReq<GatewayTransferReq<QueryDeliveryFeeReq>> transferReq = new TransferReq<GatewayTransferReq<QueryDeliveryFeeReq>>(queryDeliveryFeeUrl, new GatewayTransferReq<QueryDeliveryFeeReq>(req));
        OpsRequest.Executor<TransferReq, GatewayTransferResp<QueryDeliveryFeeResp>> executor = OpsRequest.Executor.create(transferUrl);
        executor.requestValue(transferReq)
                .responseType(OpsRequest.getContentResponseType(GatewayTransferResp.class, QueryDeliveryFeeResp.class))
                .execute(listener, queryDeliveryFeeUrl);//用gateway的url作为请求的tag*/
    }

    @Override
    public void batchQueryDeliveryFee(BatchQueryDeliveryFeeReq req, ResponseListener<GatewayTransferResp<JsonArray>> listener) {
        /*String transferUrl = ServerAddressUtil.getInstance().getGatewayTransfer();
        String batchQueryDeliveryFeeUrl = ServerAddressUtil.getInstance().batchQueryDeliveryFee();
        TransferReq<GatewayTransferReq<BatchQueryDeliveryFeeReq>> transferReq = new TransferReq<>(batchQueryDeliveryFeeUrl, new GatewayTransferReq<BatchQueryDeliveryFeeReq>(req));
        OpsRequest.Executor<TransferReq, GatewayTransferResp<JsonArray>> executor = OpsRequest.Executor.create(transferUrl);
        executor.requestValue(transferReq)
                .responseType(OpsRequest.getContentResponseType(GatewayTransferResp.class, JsonArray.class))
                .execute(listener, batchQueryDeliveryFeeUrl);*/
    }

    @Override
    public void deliveryOrderDispatch(DeliveryOrderDispatchReq req, ResponseListener<GatewayTransferResp<DeliveryOrderDispatchResp>> listener) {
        /*String transferUrl = ServerAddressUtil.getInstance().getGatewayTransfer();
        String deliveryOrderDispatchUrl = ServerAddressUtil.getInstance().deliveryOrderDispatch();
        TransferReq<GatewayTransferReq<DeliveryOrderDispatchReq>> transferReq = new TransferReq<GatewayTransferReq<DeliveryOrderDispatchReq>>(deliveryOrderDispatchUrl, new GatewayTransferReq<DeliveryOrderDispatchReq>(req));
        OpsRequest.Executor<TransferReq, GatewayTransferResp<DeliveryOrderDispatchResp>> executor = OpsRequest.Executor.create(transferUrl);
        executor.requestValue(transferReq)
                .responseType(OpsRequest.getContentResponseType(GatewayTransferResp.class, DeliveryOrderDispatchResp.class))
                .execute(listener, deliveryOrderDispatchUrl);//用gateway的url作为请求的tag*/
    }

    @Override
    public void deliveryOrderList(DeliveryOrderListReq req, ResponseListener<GatewayTransferResp<DeliveryOrderListResp>> listener) {
        /*String transferUrl = ServerAddressUtil.getInstance().getGatewayTransfer();
        String deliveryOrderListUrl = ServerAddressUtil.getInstance().deliveryOrderList();
        TransferReq<GatewayTransferReq<DeliveryOrderListReq>> transferReq = new TransferReq<GatewayTransferReq<DeliveryOrderListReq>>(deliveryOrderListUrl, new GatewayTransferReq<DeliveryOrderListReq>(req));
        OpsRequest.Executor<TransferReq, GatewayTransferResp<DeliveryOrderListResp>> executor = OpsRequest.Executor.create(transferUrl);
        executor.requestValue(transferReq)
                .responseType(OpsRequest.getContentResponseType(GatewayTransferResp.class, DeliveryOrderListResp.class))
                .responseProcessor(new DeliveryOrderListRespProcessor())
                .execute(listener, deliveryOrderListUrl);//用gateway的url作为请求的tag*/
    }

    /**
     * 将DeliveryOrderListResp保存到数据库的处理器
     */
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
        /*String url = ServerAddressUtil.getInstance().getRefundCheck();
        RefundCheckRequest req = new RefundCheckRequest(tradeId, paymentItemId);
        OpsRequest.Executor<RefundCheckRequest, RefundCheckResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(RefundCheckResp.class)
                .execute(listener, "refundCheck");*/
    }

    @Override
    public void refundPayment(Long tradeId, Long paymentItemId, SimpleResponseListener<PayResp> listener) {
        String url = ServerAddressUtil.getInstance().getRefundPayment(paymentItemId);
        OpsRequest.Executor<Void, PayResp> executor = OpsRequest.Executor.create(url);
        executor.responseProcessor(new PayRespProcessor())
                .responseClass(PayResp.class)
                //.interceptEnable(enable)
                .execute(listener, "refundPayment");
    }

    @Override
    public void refundSubmit(Long tradeId, Long paymentItemId, BigDecimal refundFee, SimpleResponseListener<RefundSubmitResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getRefundSubmit();
        RefundSubmitRequest req = new RefundSubmitRequest(tradeId, paymentItemId, refundFee);
        OpsRequest.Executor<RefundSubmitRequest, RefundSubmitResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(RefundSubmitResp.class)
                .responseProcessor(new OpsRequest.DatabaseResponseProcessor<RefundSubmitResp>() {
                    @Override
                    protected void transactionCallable(DatabaseHelper helper, RefundSubmitResp resp) throws Exception {
                        DBHelperManager.saveEntities(helper, Payment.class, resp.payments);
                        DBHelperManager.saveEntities(helper, PaymentItem.class, resp.paymentItems);
                    }
                })
                .execute(listener, "refundSubmit");*/
    }

    @Override
    public void takeDish(TradeExtra tradeExtra, FragmentActivity activity, CalmResponseListener<ResponseObject<TakeDishResp>> listener) {
        /*TakeDishReq req = new TakeDishReq();
        req.setTradeId(tradeExtra.getTradeId());
        req.setTakeDishStatus(Integer.valueOf(TakeDishStatus.HAVE_TAKE_DISH.value()));
        req.setClientUpdateTime(System.currentTimeMillis());
        req.setServerUpdateTime(tradeExtra.getServerUpdateTime());
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            req.setOperatorId(user.getId());
            req.setOperatorName(user.getName());
        }

        new CalmNetWorkRequest.Builder<TakeDishReq, TradeExtra>()
                .with(activity)
                .url(ServerAddressUtil.getInstance().getChangeTakeDishStatusUrl())
                .requestContent(req)
                .responseClass(TradeExtra.class)
                .showLoading()
                .successListener(listener)
                .errorListener(listener)
                .responseProcessor(new CalmDatabaseProcessor<TradeExtra>() {
                    @Override
                    protected void transactionCallable(DatabaseHelper helper, TradeExtra resp) throws Exception {
                        DBHelperManager.saveEntities(helper, TradeExtra.class, resp);
                    }
                })
                .tag(ServerAddressUtil.getInstance().getChangeTakeDishStatusUrl())
                .create();*/
    }

    @Override
    public void insertBuffetNoTable(TradeVo tradeVo, AuthUser waiter, ResponseListener<BuffetNoTableTradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().buffetNoTabletradeInsert();
        tradeVo.getTrade().validateCreate();
        TradeReq tradeReq = toTradeReq(tradeVo);
        //增加默认税率
        TaxRateInfo taxRateInfo = ServerSettingCache.getInstance().getmTaxRateInfo();
        if (taxRateInfo != null && taxRateInfo.isTaxSupplyOpen()) {
            TradeTax tradeTax = taxRateInfo.toTradeTax(null);
            tradeReq.setTradeTaxs(Arrays.asList(tradeTax));
        }
        //加入服务费
        ExtraCharge serviceExtraCharge = ServerSettingCache.getInstance().getmServiceExtraCharge();
        if (serviceExtraCharge != null && serviceExtraCharge.isAutoJoinTrade()) {
            tradeReq.setTradeInitConfigs(Arrays.asList(serviceExtraCharge.toTradeInitConfig()));
        }
        OpsRequest.Executor<TradeReq, BuffetNoTableTradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(tradeReq)
                .responseClass(BuffetNoTableTradeResp.class)
                .responseProcessor(new OpsRequest.DatabaseResponseProcessor<BuffetNoTableTradeResp>() {
                    @Override
                    protected void transactionCallable(DatabaseHelper helper, BuffetNoTableTradeResp resp) throws Exception {
                        DBHelperManager.saveEntities(helper, Trade.class, resp.getTrade());
                        DBHelperManager.saveEntities(helper, TradeExtra.class, resp.getTradeExtra());
                        DBHelperManager.saveEntities(helper, TradeItem.class, resp.getTradeItems());
                        DBHelperManager.saveEntities(helper, TradeCustomer.class, resp.getTradeCustomers());
                        DBHelperManager.saveEntities(helper, TradeBuffetPeople.class, resp.getTradeBuffetPeoples());
                        DBHelperManager.saveEntities(helper, TradeDeposit.class, resp.getTradeDeposit());
                        DBHelperManager.saveEntities(helper, TradeTax.class, resp.getTradeTaxs());
                        DBHelperManager.saveEntities(helper, TradeInitConfig.class, resp.getTradeInitConfigs());
                    }
                })
                .execute(listener, "buffetNoTableTradeCreate");*/
    }

    @Override
    public void usePrivilege(Trade trade, BusinessType businessType, Long customerId, String entityCardNo, Collection<TradePrivilege> tradePrivileges, QSResponseListener<UsePrivilegeResp> listener) {
        /*String usePrivilegeUrl = ServerAddressUtil.getInstance().getUsePrivilegeUrl();
        UsePrivilegeReq req = toUsePrivilegeReq(trade, businessType, customerId, entityCardNo, tradePrivileges);
        QSOpsRequest.Executor<UsePrivilegeReq, UsePrivilegeResp> executor = QSOpsRequest.Executor.create(usePrivilegeUrl);
        executor.requestValue(req)
                .responseClass(UsePrivilegeResp.class)
                .execute(listener, "use_privilege");*/
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

        //积分抵现
        List<UsePrivilegeReq.IntegralInfo> integralInfos = new ArrayList<UsePrivilegeReq.IntegralInfo>();
        //优惠券
        List<UsePrivilegeReq.PromoInfo> promoInfos = new ArrayList<UsePrivilegeReq.PromoInfo>();
        //微信卡券
        List<UsePrivilegeReq.WeixinCardInfo> weixinCardInfos = new ArrayList<UsePrivilegeReq.WeixinCardInfo>();
        if (tradePrivileges != null) {
            for (TradePrivilege tradePrivilege : tradePrivileges) {
                //积分抵现
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
                //优惠券
                if (tradePrivilege.getPrivilegeType() == PrivilegeType.COUPON) {
                    UsePrivilegeReq.PromoInfo promoInfo = req.new PromoInfo();
                    promoInfo.setCustomerId(customerId);
                    promoInfo.setPrivilegeType(tradePrivilege.getPrivilegeType());
                    promoInfo.setPrivilegeAmount(tradePrivilege.getPrivilegeAmount());
                    promoInfo.setPromoId(tradePrivilege.getPromoId());
                    promoInfo.setUuid(tradePrivilege.getUuid());
                    promoInfos.add(promoInfo);
                }
                //微信卡券
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
        /*AddFeeReq req = new AddFeeReq();
        req.setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        req.setDeliveryOrderId(deliveryOrderId);
        req.setDeliveryPlatform(deliveryPlatform);
        req.setAmount(amount);
        req.setOperaterName(Session.getAuthUser().getName());
        req.setOperaterNo(Session.getAuthUser().getId());

        String transferUrl = ServerAddressUtil.getInstance().getGatewayTransfer();
        String addFee = ServerAddressUtil.getInstance().addFee();
        TransferReq<GatewayTransferReq<AddFeeReq>> transferReq = new TransferReq<GatewayTransferReq<AddFeeReq>>(addFee,
                new GatewayTransferReq<>(req));
        OpsRequest.Executor<TransferReq, GatewayTransferResp<AddFeeResp>> executor = OpsRequest.Executor.create(transferUrl);
        executor.requestValue(transferReq)
                .responseType(OpsRequest.getContentResponseType(GatewayTransferResp.class, AddFeeResp.class))
                .responseProcessor(new AddFeeRespProcessor())
                .execute(listener, addFee);//用gateway的url作为请求的tag*/
    }

    @Override
    public void pay4QS(UsePayReq req, QSResponseListener<UsePayResp> listener) {
        /*String pay4QSUrl = ServerAddressUtil.getInstance().getPay4QSUrl();
        QSOpsRequest.Executor<UsePayReq, UsePayResp> executor = QSOpsRequest.Executor.create(pay4QSUrl);
        executor.requestValue(req)
                .responseClass(UsePayResp.class)
                .responseProcessor(new UsePayRespProcessor())
                .execute(listener, "pay4QS");*/
    }

    private static class UsePayRespProcessor extends QSOpsRequest.SaveDatabaseResponseProcessor<UsePayResp> {

        @Override
        protected Callable<Void> getCallable(DatabaseHelper helper, UsePayResp resp) throws Exception {
            List<UsePayResp.PaymentItemResult> paymentItemResults = resp.getPaymentItemResults();
            for (UsePayResp.PaymentItemResult paymentItemResult : paymentItemResults) {
                Dao<PaymentItem, String> dao = helper.getDao(PaymentItem.class);
                PaymentItem paymentItem = dao.queryForId(paymentItemResult.getPaymentItemUuid());
                if (paymentItem != null) {
                    //获取支付方式
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
                            //保存PaymentItem
                            PaymentItem paymentItem1 = addition.getPaymentItem();
                            paymentItem1.setServerUpdateTime(currentTime);
                            DBHelperManager.saveEntities(helper, PaymentItem.class, true, paymentItem1);
                            //保存PaymentItemGroupon
                            PaymentItemGroupon paymentItemGroupon = addition.getPaymentItemGroupon();
                            paymentItemGroupon.setServerUpdateTime(currentTime);
                            DBHelperManager.saveEntities(helper, PaymentItemGroupon.class, true, addition.getPaymentItemGroupon());
                            //保存tradePromotion
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

    /**
     * 将AddFeeResp保存到数据库的处理器
     */
    private static class AddFeeRespProcessor extends SaveDatabaseResponseProcessor<GatewayTransferResp<AddFeeResp>> {

        @Override
        protected Callable<Void> getCallable(DatabaseHelper helper, GatewayTransferResp<AddFeeResp> resp
        ) throws Exception {
            DBHelperManager.saveEntities(helper, DeliveryOrderRecord.class, resp.getResult().getDeliveryOrderRecord());
            return null;
        }
    }

    /**
     * 将InvoiceQrcodeResp保存到数据库的处理器
     */
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
        /*String url = ServerAddressUtil.getInstance().getBindOrderUrl();
        OpsRequest.Executor<BindOrderReq, BindOrderResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new BindOrderRespProcessor())
                .responseClass(BindOrderResp.class)
                .execute(listener, url);*/
    }

    @Override
    public void creatUnionTrade(UnionTradeReq req, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getCreatUnionTradesUrl();
        OpsRequest.Executor<UnionTradeReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new TradeRespProcessor())
                .responseClass(TradeResp.class)
                .execute(listener, url);*/
    }

    @Override
    public void splitUnionTrade(UnionTradeSplitReq req, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getSplitUnionTradesUrl();
        OpsRequest.Executor<UnionTradeSplitReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new TradeRespProcessor())
                .responseClass(TradeResp.class)
                .execute(listener, url);*/
    }

    @Override
    public void modifyUnionMainTrade(TradeVo tradeVo, TradeUnionModifyMainWarpReq req, ResponseListener<TradeResp> listener, boolean isAsync) {
        /*String url = ServerAddressUtil.getInstance().getModifyUnionMainTrade();
        OpsRequest.Executor<TradeUnionModifyMainWarpReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new TradeRespProcessor())
                .responseClass(TradeResp.class);

        if (isAsync) {
            AsyncNetworkManager.getInstance().addRequest(tradeVo, AsyncHttpType.UNION_MAIN_MODIFYTRADE, executor, listener, "modifyUnionMainTrade");
        } else {
            executor.execute(listener, url);
        }*/
    }

    @Override
    public void modifyUnionSubTrade(TradeVo tradeVo, TradeUnionModifySubWarpReq req, ResponseListener<TradeResp> listener, boolean isAsync) {
        /*String url = ServerAddressUtil.getInstance().getModifyUnionSubTrade();
        OpsRequest.Executor<TradeUnionModifySubWarpReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new TradeRespProcessor())
                .responseClass(TradeResp.class);
        if (isAsync) {
            AsyncNetworkManager.getInstance().addRequest(tradeVo, AsyncHttpType.UNION_SUB_MODIFYTRADE, executor, listener, "modifyUnionMainTrade");
        } else {
            executor.execute(listener, url);
        }*/
    }

    @Override
    public void unionAndModifyUnionTrade(UnionAndModifyUnionTradeReq req, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getUnionAndModifyUrl();
        OpsRequest.Executor<UnionAndModifyUnionTradeReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new TradeRespProcessor())
                .responseClass(TradeResp.class)
                .execute(listener, url);*/
    }

    @Override
    public void unionOperationDish(UnionTradeItemOperationReq req, ResponseListener<TradeItemResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getUnionTradeItemOperation();
        OpsRequest.Executor<UnionTradeItemOperationReq, TradeItemResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new TradeItemRespProcessor())
                .responseClass(TradeItemResp.class)
                .execute(listener, url);*/
    }

    /**
     * 将BindOrderResp保存到数据库的处理器
     */
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
        /*String url;
        if (type == TradeType.UNOIN_TABLE_MAIN) {
            url = ServerAddressUtil.getInstance().modifyServiceUnionMainDish(); // 主单地址
        } else {
            url = ServerAddressUtil.getInstance().modifyServiceUnionSubDish(); // 子单地址
        }
        CalmNetWorkRequest.with(BaseApplication.sInstance)
                .url(url)
                .requestContent(dishServiceReq)
                .responseClass(DishServiceV2Resp.class)
                .responseProcessor(new CalmDatabaseProcessor<DishServiceV2Resp>() {
                    @Override
                    protected void transactionCallable(DatabaseHelper helper, DishServiceV2Resp resp) throws Exception {
                        DBHelperManager.saveEntities(helper, TradeItem.class, resp.tradeItems);
                        DBHelperManager.saveEntities(helper, TradeExtra.class, resp.tradeExtras);
                        DBHelperManager.saveEntities(helper, KdsTradeItem.class, resp.kdsTradeItems);
                        DBHelperManager.saveEntities(helper, KdsTradeItemPart.class, resp.kdsTradeItemParts);
                        DBHelperManager.saveEntities(helper, TradeItemMainBatchRel.class, resp.tradeItemMainBatchRels);
                        DBHelperManager.saveEntities(helper, TradeItemMainBatchRelExtra.class, resp.tradeItemMainBatchRelExtras);
                    }
                })
                .successListener(listener)
                .errorListener(listener)
                .tag("modifyServiceUnionDish")
                .create();*/
    }

    // add v8.5 by yutang for openplatform tradeInfo
    public static void saveTradeResp(TradeResp resp) {

        try {
            new TradeRespProcessor().saveToDatabase(resp);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void modifyTradeMemo(Trade trade, String newMemo, ResponseListener<ModifyTradeMemoResp> listener) {
        /*final ModifyTradeMemoReq req = new ModifyTradeMemoReq();
        req.tradeId = trade.getId();
        req.serverUpdateTime = trade.getServerUpdateTime();
        req.tradeMemo = newMemo;
        IAuthUser user = IAuthUser.Holder.get();
        if (user != null) {
            req.updatorId = user.getId();
            req.updatorName = user.getName();
        }
        req.clientUpdateTime = System.currentTimeMillis();

        String url = ServerAddressUtil.getInstance().modifyTradeMemo();
        OpsRequest.Executor<ModifyTradeMemoReq, ModifyTradeMemoResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new SaveDatabaseResponseProcessor<ModifyTradeMemoResp>() {
                    @Override
                    protected Callable<Void> getCallable(DatabaseHelper helper, ModifyTradeMemoResp resp) throws Exception {
                        if (resp.trade != null) {
                            DBHelperManager.saveEntities(helper, Trade.class, Utils.asList(resp.trade));
                        }
                        return null;
                    }
                }).responseClass(ModifyTradeMemoResp.class)
                .execute(listener, url);*/
    }

    @Override
    public void buffetUnionTableCreate(BuffetUnionTradeReq req, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().buffetUnionTableUrl();
        OpsRequest.Executor<BuffetUnionTradeReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new TradeRespProcessor())
                .responseClass(TradeResp.class)
                .execute(listener, url);*/
    }

    @Override
    public void buffetModifyUnionSubTrade(TradeVo tradeVo, TradeUnionModifySubWarpReq req, ResponseListener<TradeResp> listener, boolean isAsync) {
        /*String url = ServerAddressUtil.getInstance().buffetModifyUnionSubTrade();
        OpsRequest.Executor<TradeUnionModifySubWarpReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new TradeRespProcessor())
                .responseClass(TradeResp.class);
        if (isAsync) {
            AsyncNetworkManager.getInstance().addRequest(tradeVo, AsyncHttpType.UNION_SUB_MODIFYTRADE, executor, listener, "modifyUnionMainTrade");
        } else {
            executor.execute(listener, url);
        }*/
    }

    public void buffetSplitTrade(BuffetUnionTradeCancelReq req, ResponseListener<BuffetUnionTradeCancelResp> listener) {
        /*String url = ServerAddressUtil.getInstance().buffetSplitTradeUrl();
        OpsRequest.Executor<BuffetUnionTradeCancelReq, BuffetUnionTradeCancelResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new BuffetUnionTradeCancelRespProcessor())
                .responseClass(BuffetUnionTradeCancelResp.class)
                .execute(listener, url);*/
    }

    /**
     * 将TradeResp保存到数据库的处理器
     *
     * @version: 1.0
     * @date 2015年4月15日
     */
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
        /*String url = ServerAddressUtil.getInstance().buffetModifyUnionMainTrade();
        OpsRequest.Executor<BuffetMainTradeModifyReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new TradeRespProcessor())
                .responseClass(TradeResp.class)
                .execute(listener, url);*/

    }

    @Override
    public void buffetUnionFinish(BuffetMergeUnionReq req, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().buffetUnionFinish();
        OpsRequest.Executor<BuffetMergeUnionReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new TradeRespProcessor())
                .responseClass(TradeResp.class)
                .execute(listener, url);*/
    }

    @Override
    public void buffetCreateMenu(TradeVo tradeVo, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().buffetCreateMenu();
        BuffetCreateMenuReq req = new BuffetCreateMenuReq();
        req.setMainTrade(tradeVo.getTrade());
        TradeItem menuTradeItem = tradeVo.getMealShellVo().getTradeItem();
        menuTradeItem.setIsChangePrice(Bool.NO);
        req.setMenuTradeItem(menuTradeItem);
        List<TradeUser> tradeUserList = null;
        if (tradeVo.getTradeUser() != null) {
            tradeUserList = new ArrayList<>();
            tradeUserList.add(tradeVo.getTradeUser());
        }
        req.setTradeUsers(tradeUserList);
        //过滤
        List<TradeBuffetPeople> tradeBuffetPeoples = null;
        if (Utils.isNotEmpty(tradeVo.getTradeBuffetPeoples())) {
            tradeBuffetPeoples = new ArrayList<>();
            for (int i = tradeVo.getTradeBuffetPeoples().size() - 1; i >= 0; i--) {
                TradeBuffetPeople tradeBuffetPeople = tradeVo.getTradeBuffetPeoples().get(i);
                if (tradeBuffetPeople.isChanged()) {
                    tradeBuffetPeople.setTradeId(tradeVo.getTrade().getId());
                    tradeBuffetPeople.setTradeUuid(tradeVo.getTrade().getUuid());
                    tradeBuffetPeoples.add(tradeBuffetPeople);
                }
            }
        }
        req.setTradeBuffetPeoples(tradeBuffetPeoples);
        req.setTradeBuffetPeoples(tradeVo.getTradeBuffetPeoples());
        //如果押金是0元且有效，直接改成无效
        TradeDeposit tradeDeposit = tradeVo.getTradeDeposit();
        if (tradeDeposit != null && tradeDeposit.isValid() && tradeDeposit.getDepositPay() != null && tradeDeposit.getDepositPay().compareTo(BigDecimal.ZERO) == 0) {
            tradeDeposit.setStatusFlag(StatusFlag.INVALID);
            tradeDeposit.setChanged(true);
        }
        req.setTradeDeposit(tradeDeposit);
        OpsRequest.Executor<BuffetCreateMenuReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new TradeRespProcessor())
                .responseClass(TradeResp.class)
                .execute(listener, url);*/
    }

    @Override
    public void memberWriteoff(WriteoffReq req, ResponseListener<WriteoffResp> listener) {
        /*String url = ServerAddressUtil.getInstance().writeoffUrl();
        OpsRequest.Executor<WriteoffReq, WriteoffResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(WriteoffResp.class)
                .execute(listener, url);*/
    }

    @Override
    public void queryInvoiceNo(GetTaxNoReq req, ResponseListener<GetTaxNoResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getTaxNoUrl();
        OpsRequest.Executor<GetTaxNoReq, GetTaxNoResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new GetTaxNoProcessor())
                .timeout(6000)
                .responseClass(GetTaxNoResp.class)
                .execute(listener, url);*/
    }

    @Override
    public void createPrePayTrade(PrePayTradeReq req, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getPrePayTrade();
        OpsRequest.Executor<PrePayTradeReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new TradeRespProcessor())
                .responseClass(TradeResp.class)
                .execute(listener, url);*/
    }

    @Override
    public void deletePrePayTrade(DeletePrePayTradeReq req, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().deletePrePayTrade();
        OpsRequest.Executor<DeletePrePayTradeReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new TradeRespProcessor())
                .responseClass(TradeResp.class)
                .execute(listener, url);*/
    }

    /**
     * v8.12.0 口碑验证
     */
    public void koubeiVerification(String code, ResponseListener<KouBeiVerifyResp> listener) {
        /*String url = ServerAddressUtil.getInstance().verifyKoubei();
        KouBeiVerifyReq verifyReq = new KouBeiVerifyReq();
        verifyReq.verifyOrderId = code;
        verifyReq.shopId = ShopInfoCfg.getInstance().shopId;
        OpsRequest.Executor<KouBeiVerifyReq, KouBeiVerifyResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(verifyReq)
                .responseClass(KouBeiVerifyResp.class)
                .responseProcessor(new KoubeiVerifyProcessor())
                .execute(listener, url);*/
    }

    /**
     * 口碑核销结果
     */
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
        /*WriteoffOnlineResultReq req = new WriteoffOnlineResultReq();
        req.outTradeNo = tradeNo;
        String url = null;
        if (payType == PayType.QCODE) {//顾客主扫
            url = ServerAddressUtil.getInstance().writeoffZSResultUrl();
        } else {
            url = ServerAddressUtil.getInstance().writeoffBSResultUrl();
        }
        OpsRequest.Executor<WriteoffOnlineResultReq, WriteoffOnlineResultResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(WriteoffOnlineResultResp.class)
                .execute(listener, url);*/
    }

    public void useEarnestDeduct(EarnestDeductReq req, ResponseListener<PayResp> listener) {
        /*String url = ServerAddressUtil.getInstance().getEarnestToPayUrl();
        OpsRequest.Executor<EarnestDeductReq, PayResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new PayRespProcessor())
                .responseClass(PayResp.class)
                .execute(listener, url);*/
    }

    @Override
    public void bookingRrePayRefund(PrePayRefundReq req, ResponseListener<PayResp> listener) {
        /*String url = ServerAddressUtil.getInstance().prePayRefund();
        OpsRequest.Executor<PrePayRefundReq, PayResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new PayRespProcessor())
                .responseClass(PayResp.class)
                .execute(listener, url);*/
    }

    @Override
    public void recisionUnionTrade(Long tradeId, Long serverUpdateTime, Reason reason, List<InventoryItemReq> inventoryItems, ResponseListener<TradeResp> listener) {
        /*String url = ServerAddressUtil.getInstance().unionTradeRecision();

        TradeUnionDeleteReq req = new TradeUnionDeleteReq();
        req.tradeId = tradeId;
        req.serverUpdateTime = serverUpdateTime;
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            req.updatorId = user.getId();
            req.updatorName = user.getName();
        }
        if (reason != null) {
            req.reasonId = reason.getId();
            req.reasonContent = reason.getContent();
        }
        req.returnInventoryItems = inventoryItems;

        OpsRequest.Executor<TradeUnionDeleteReq, TradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new TradeRespProcessor())
                .responseClass(TradeResp.class)
                .execute(listener, url);*/
    }

}