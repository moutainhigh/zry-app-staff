package com.zhongmei.bty.cashier.ordercenter.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerShopBiz;
import com.zhongmei.bty.basemodule.database.utils.DbQueryConstant;
import com.zhongmei.bty.basemodule.devices.mispos.manager.DinnerPosManager;
import com.zhongmei.bty.basemodule.discount.bean.BanquetVo;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.IntegralCashPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsVo;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.pay.bean.ElectronicInvoiceVo;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGroupon;
import com.zhongmei.bty.basemodule.pay.entity.RefundExceptionReason;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.shopmanager.handover.manager.ServerSettingManager;
import com.zhongmei.bty.basemodule.trade.bean.DeliveryOrderVo;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.Invoice;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.bty.basemodule.trade.entity.TradeReceiveLog;
import com.zhongmei.bty.basemodule.trade.message.GetTaxNoReq;
import com.zhongmei.bty.basemodule.trade.message.GetTaxNoResp;
import com.zhongmei.bty.basemodule.trade.message.InvoiceQrcodeReq;
import com.zhongmei.bty.basemodule.trade.message.InvoiceQrcodeResp;
import com.zhongmei.bty.basemodule.trade.message.TradePayStateResp;
import com.zhongmei.bty.basemodule.trade.message.TradePaymentResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.cashier.ordercenter.bean.LoadResult;
import com.zhongmei.bty.cashier.ordercenter.manager.IOrderCenterDetailManager;
import com.zhongmei.bty.cashier.ordercenter.view.IOrderCenterDetailView;
import com.zhongmei.bty.common.util.ValueEnumsToStringUtils;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.DeliveryOrder;
import com.zhongmei.bty.commonmodule.database.enums.DeliveryOrderStatus;
import com.zhongmei.bty.commonmodule.database.enums.DeliveryOrderSubStatus;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.dinner.action.ActionReprintType;
import com.zhongmei.bty.dinner.ordercenter.bean.PayModeDetailsBean;
import com.zhongmei.bty.dinner.ordercenter.view.ColumnLayout;
import com.zhongmei.bty.pay.manager.DoPayManager;
import com.zhongmei.bty.snack.event.EventSelectOrderRefresh;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.discount.Coupon;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.discount.TradePromotion;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.entity.trade.TradeReturnInfo;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.db.enums.ActivityRuleEffective;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.DeliveryPlatform;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.OperateType;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.QuickDiscountType;
import com.zhongmei.yunfu.db.enums.RefundWay;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayForm;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.data.GatewayTransferResp;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.ValueEnums;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;



public abstract class OrderCenterDetailPresenter implements IOrderCenterDetailPresenter {
    public static final String TAG = OrderCenterDetailPresenter.class.getSimpleName();
    private static final Uri URI_INVOICE = DBHelperManager.getUri(Invoice.class);
    private OrderCenterChangeObserver mDataObserver;
    private String uuid;
    protected IOrderCenterDetailView mView;
    protected IOrderCenterDetailManager mManager;
        private LoadTradeAsyncTask mLoadTradeAsyncTask;
    protected LoadResult mLoadResult;
        protected int mCurrentTab = DbQueryConstant.UNPROCESSED_ALL;
    protected boolean isSquareAccountMode = false;
    protected boolean isBindDeliveryUserMode = false;

    public OrderCenterDetailPresenter(IOrderCenterDetailView view, IOrderCenterDetailManager manager) {
        mView = view;
        mManager = manager;
        registerDataObserver();
    }

    private void registerDataObserver() {
        if (mDataObserver == null) {
            mDataObserver = new OrderCenterDetailPresenter.OrderCenterChangeObserver();
        }
        DatabaseHelper.Registry.register(mDataObserver);
    }

    @Override
    public void destroy() {
        unRegisterDataObserver();
    }

    private void unRegisterDataObserver() {
        if (mDataObserver != null) {
            DatabaseHelper.Registry.unregister(mDataObserver);
            mDataObserver = null;
        }
    }

    @Override
    public void doAcceptReturn() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void doRefuseReturn() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendOrder(PartnerShopBiz partnerShopBiz, BigDecimal deliveryFee) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deliveryOrderDispatch(TradePaymentVo tradePaymentVo, BigDecimal deliveryFee, Integer deliveryPlatform) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void queryDeliveryFee(Long tradeId, String tradeNo, String thirdTranNo, PartnerShopBiz partnerShopBiz) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deliveryOrderList(TradeVo tradeVo) {
        throw new UnsupportedOperationException();
    }


    @Override
    public void cancelDeliveryOrder() {
        throw new UnsupportedOperationException();
    }

    private class ReReshStateResponseListener implements ResponseListener<TradePayStateResp> {
        private TradePayStatus currentTradePayStatus;
        private Context context;

        public ReReshStateResponseListener(Context context, TradePayStatus mTradePayStatus) {
            this.context = context;
            this.currentTradePayStatus = mTradePayStatus;
        }

        @Override
        public void onResponse(ResponseObject<TradePayStateResp> response) {
            String info;
            Integer nowTradePayStatus;
            TradePayStateResp resp = response.getContent();
            if (resp != null) {
                nowTradePayStatus = resp.getPaymentItems().get(0).getPayStatus().value();
                if (Utils.equals(currentTradePayStatus.value(), nowTradePayStatus)) {
                    info = context.getString(R.string.dinner_orderdish_staus_no_resh);
                } else {
                    info = context.getString(R.string.dinner_orderdish_staus_is,
                            ValueEnumsToStringUtils.gettradePayStatusNameToString(nowTradePayStatus));

                                        if (resp.getTrade() != null && resp.getTrade().getTradePayStatus() == TradePayStatus.PAID) {
                        EventBus.getDefault().post(new EventSelectOrderRefresh());
                    }
                }
            } else {
                info = context.getString(R.string.dinner_orderdish_staus_no_resh);
            }
            mView.showToast(info);
        }

        @Override
        public void onError(VolleyError error) {
            mView.showToast(error.getMessage());
        }
    }

    @Override
    public void doRefreshPayStatus(PaymentItem paymentItem) {
        ResponseListener<TradePayStateResp> listener = LoadingResponseListener
                .ensure(new ReReshStateResponseListener(mView.getViewActivity(), paymentItem.getPayStatus()), mView.getViewFragmentManager());
        mManager.refreshState(getTradeVo(), paymentItem, listener);
    }

    @Override
    public void doContinuePay() {
        DoPayManager.gotoPayActivity(mView.getViewActivity(), getTradeVo(), true);
    }

    @Override
    public void retryRefund() {
        PaymentItem posCardItem = getPaymentItem(getPaymentVoList(), PayModeId.POS_CARD);
        if (posCardItem != null) {
            if (posCardItem.getRefundWay() == RefundWay.AUTO_REFUND) {
                if (posCardItem.getPayStatus() == TradePayStatus.WAITING_REFUND
                        || posCardItem.getPayStatus() == TradePayStatus.REFUNDING
                        || posCardItem.getPayStatus() == TradePayStatus.REFUND_FAILED) {
                    posCardRefund(getTradeVo());
                } else {
                    ToastUtil.showShortToast(R.string.no_need_refund);
                }
            } else if (posCardItem.getRefundWay() == RefundWay.HAND_REFUND) {
                ToastUtil.showShortToast(R.string.please_hand_refund);
            } else if (posCardItem.getRefundWay() == RefundWay.NONEED_REFUND) {
                ToastUtil.showShortToast(R.string.no_need_refund);
            } else {
                ToastUtil.showShortToast(R.string.customer_data_error);
            }
        } else {
            retryRefund(mLoadResult.getOriTradePaymentVo().getTradeVo(), mLoadResult.getTradePaymentVo().getTradeVo());
        }
    }

    @Override
    public void loadData(String tradeUuid) {
        if (mLoadTradeAsyncTask != null) {
            mLoadTradeAsyncTask.cancel(true);
            mLoadTradeAsyncTask = null;
        }
        mLoadTradeAsyncTask = new LoadTradeAsyncTask(tradeUuid);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            mLoadTradeAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else {
            mLoadTradeAsyncTask.execute();
        }
    }

    @Override
    public void setCurrentTab(int currentTab) {
        mCurrentTab = currentTab;
    }

    @Override
    public void setIsSquareAccountMode(boolean isSquareAccountMode) {
        this.isSquareAccountMode = isSquareAccountMode;
    }

    @Override
    public void setIsBindDeliveryUserMode(boolean isBindDeliveryUserMode) {
        this.isBindDeliveryUserMode = isBindDeliveryUserMode;
    }

    protected PaymentItem getPaymentItem(List<PaymentVo> paymentVoList, PayModeId payModeId) {
        if (Utils.isNotEmpty(paymentVoList)) {
            for (PaymentVo paymentVo : paymentVoList) {
                List<PaymentItem> paymentItemList = paymentVo.getPaymentItemList();
                if (Utils.isNotEmpty(paymentItemList)) {
                    for (PaymentItem paymentItem : paymentItemList) {
                        if (paymentItem.getPayModeId().longValue() == payModeId.value().longValue()) {
                            return paymentItem;
                        }
                    }
                }
            }
        }

        return null;
    }

    protected List<PaymentItem> getPaymentItems(PayModeId payModeId) {
        List<PaymentVo> paymentVoList = getPaymentVoList();
        if (Utils.isNotEmpty(paymentVoList)) {
            List<PaymentItem> paymentItems = new ArrayList<PaymentItem>();
            for (PaymentVo paymentVo : paymentVoList) {
                List<PaymentItem> paymentItemList = paymentVo.getPaymentItemList();
                if (Utils.isNotEmpty(paymentItemList)) {
                    for (PaymentItem paymentItem : paymentItemList) {
                        if (paymentItem.getPayModeId().longValue() == payModeId.value().longValue()) {
                            paymentItems.add(paymentItem);
                        }
                    }
                }
            }

            return paymentItems;
        }

        return Collections.emptyList();
    }


    protected boolean isPayInJinCheng() {
                List<PaymentItem> jinChengPaymentItems = getPaymentItems(PayModeId.JIN_CHENG);
        if (Utils.isNotEmpty(jinChengPaymentItems)) {
            for (PaymentItem paymentItem : jinChengPaymentItems) {
                if (paymentItem.getPayStatus() == TradePayStatus.PAID) {
                    return true;
                }
            }
        }

                List<PaymentItem> memberCardPaymentItems = getPaymentItems(PayModeId.MEMBER_CARD);
        if (Utils.isNotEmpty(memberCardPaymentItems)
                && ServerSettingCache.getInstance().isJinChBusiness()) {
            for (PaymentItem paymentItem : memberCardPaymentItems) {
                if (paymentItem.getPayStatus() == TradePayStatus.PAID) {
                    return true;
                }
            }
        }
                List<PaymentItem> anonymousCardPaymentItems = getPaymentItems(PayModeId.ANONYMOUS_ENTITY_CARD);
        if (Utils.isNotEmpty(anonymousCardPaymentItems)
                && ServerSettingCache.getInstance().isJinChBusiness()) {
            for (PaymentItem paymentItem : anonymousCardPaymentItems) {
                if (paymentItem.getPayStatus() == TradePayStatus.PAID) {
                    return true;
                }
            }
        }
        return false;
    }


    private void retryRefund(TradeVo relateTradeVo, TradeVo tradeVo) {
        ResponseListener<TradePaymentResp> listener = new ResponseListener<TradePaymentResp>() {
            @Override
            public void onResponse(ResponseObject<TradePaymentResp> response) {
                mView.showToast(response.getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                mView.showToast(error.getMessage());
            }
        };

        mManager.retryRefund(relateTradeVo, tradeVo, LoadingResponseListener.ensure(listener, mView.getViewFragmentManager()));
    }


    protected void posCardRefund(TradeVo tradeVo) {
        try {
            new DinnerPosManager().retryPosRefund(tradeVo, mView.getViewFragmentManager());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }


    private class LoadTradeAsyncTask extends AsyncTask<String, Void, LoadResult> {
        private String mTradeUuid;

        public LoadTradeAsyncTask(String tradeUuid) {
            mTradeUuid = tradeUuid;
            uuid = mTradeUuid;
        }

        @Override
        protected LoadResult doInBackground(String... params) {
            return mManager.loadData(mTradeUuid);
        }

        @Override
        protected void onPostExecute(LoadResult result) {
            mLoadResult = result;
            if (mView.isAdd()) {
                mView.dismissLoadingView();
                TradePaymentVo tradePaymentVo = result.getTradePaymentVo();
                if (tradePaymentVo == null) {
                    mView.showEmptyView(true);
                } else {
                    mView.showEmptyView(false);
                    TradeVo tradeVo = tradePaymentVo.getTradeVo();
                    mView.setTradeVo(tradeVo);
                    if (tradeVo.getTrade().getTradeType() == TradeType.REFUND ||
                            tradeVo.getTrade().getTradeType() == TradeType.REFUND_FOR_REPEAT ||
                            tradeVo.getTrade().getTradeType() == TradeType.REFUND_FOR_REVERSAL) {
                        if (result.getOriTradePaymentVo() != null) {
                            mView.showPayInfo(tradePaymentVo, result.getOriTradePaymentVo(), true);
                            mView.showPrivilegeInfo(result.getOriTradePaymentVo(), true);
                        } else {
                            mView.showPayInfo(tradePaymentVo, result.getOriTradePaymentVo(), true);
                            mView.showPrivilegeInfo(tradePaymentVo, true);
                        }
                    } else {
                        mView.showPayInfo(tradePaymentVo, result.getOriTradePaymentVo(), false);
                        mView.showPrivilegeInfo(tradePaymentVo, false);

                    }
                    mView.showSpliteTrades(mLoadResult.getmSpliteTradePaymentVos());
                    mView.showRejectGoodsInfo(tradeVo);
                    mView.showDetailTitle(tradePaymentVo);
                    refreshDeliveryInfoView(tradePaymentVo);                    mView.showDeliveryInfo(tradePaymentVo);
                    mView.showHandlerButton();
                    mView.showGoodsInfo(tradePaymentVo, mManager.getValidTradeItemList(tradeVo.getTradeItemList()), result.getOriTradePaymentVo());
                    mView.showAdditionaltPrivilegeInfo(tradeVo);
                    mView.showServicePrivilegeInfo(tradeVo);
                    mView.showBillInfo(tradeVo, result.getOriTradePaymentVo().getTradeVo());
                    mView.showOperateReasonInfo(tradeVo);
                    mView.showRePayedTrades(result.getRepeatTradePaymentVos());
                    mView.showTaxInfo(tradeVo);
                    refreshDeliveryCancelTip();                 }
            }
        }
    }

    private void refreshDeliveryInfoView(TradePaymentVo tradePaymentVo) {
        TradeVo tradeVo = tradePaymentVo.getTradeVo();
        Trade trade = tradeVo.getTrade();
        TradeExtra tradeExtra = tradeVo.getTradeExtra();
        if (showDeliveryInfoView(trade, tradeExtra)) {
                        mView.refreshDeliveryUserInfoView(tradePaymentVo);
                        mView.refreshDeliveryPlatformInfoView(tradePaymentVo);
                        mView.refreshTipInfoView(tradePaymentVo);
            mView.showDeliveryInfoView(true);
        } else {
            mView.showDeliveryInfoView(false);
        }
    }

        private boolean showDeliveryInfoView(Trade trade, TradeExtra tradeExtra) {
        if (trade != null && tradeExtra != null) {
            if (trade.getBusinessType() == BusinessType.TAKEAWAY
                    && trade.getDeliveryType() == DeliveryType.SEND
                    && (trade.getTradeStatus() == TradeStatus.CONFIRMED || trade.getTradeStatus() == TradeStatus.FINISH)) {
                DeliveryPlatform deliveryPlatform = getDeliveryPlatform();
                String deliveryUserId = tradeExtra.getDeliveryUserId();
                if (deliveryPlatform != null
                        && (deliveryPlatform != DeliveryPlatform.MERCHANT || !TextUtils.isEmpty(deliveryUserId))) {
                    return true;
                }
            }
        }

        return false;
    }

        protected DeliveryPlatform getDeliveryPlatform() {
        DeliveryPlatform deliveryPlatform = null;
        DeliveryOrder deliveryOrder = getDeliveryOrder();
        if (deliveryOrder != null) {
            deliveryPlatform = deliveryOrder.getDeliveryPlatform();
        }

        if (getTradeExtra() != null) {
            deliveryPlatform = getTradeExtra().getDeliveryPlatform();
        }

        return deliveryPlatform;
    }

        private void refreshDeliveryCancelTip() {
        if (isDeliveryCancelOrder()) {
            DeliveryOrder deliveryOrder = getDeliveryOrder();
            String formatDateTime = DateTimeUtils.formatDateTime(deliveryOrder.getServerUpdateTime());
            String text = mView.getViewActivity().getString(R.string.order_center_detail_delivery_cancel_alert, formatDateTime);
            if (deliveryOrder.getSubDeliveryStatus() == DeliveryOrderSubStatus.DELIVERY_ERROR_RECREATE_FORBID) {
                text = mView.getViewActivity().getString(R.string.order_center_detail_delivery_cancel_alert1, formatDateTime);
            }
            mView.showDeliveryCancelTip(text);
        } else {
            mView.hideDeliveryCancelTip();
        }
    }

        private boolean isDeliveryCancelOrder() {
        DeliveryOrder deliveryOrder = getDeliveryOrder();
        return deliveryOrder != null
                && deliveryOrder.getDeliveryStatus() == DeliveryOrderStatus.DELIVERY_CANCEL
                && (deliveryOrder.getSubDeliveryStatus() == DeliveryOrderSubStatus.DELIVERY_ERROR_RECREATE_ALLOW || deliveryOrder.getSubDeliveryStatus() == DeliveryOrderSubStatus.DELIVERY_MAN_CANCEL || deliveryOrder.getSubDeliveryStatus() == DeliveryOrderSubStatus.DELIVERY_ERROR_RECREATE_FORBID);
    }

    @Override
    public void doCall() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void doRepay() {
        final TradePaymentVo tradePaymentVo = getTradePaymentVo().clone();
        VerifyHelper.verifyAlert(mView.getViewActivity(), getRepayPermissionCode(),
                new VerifyHelper.Callback() {
                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                        performRepayOrder(tradePaymentVo);
                    }
                });
    }

        protected abstract String getRepayPermissionCode();

        protected abstract void performRepayOrder(TradePaymentVo tradePaymentVo);

    @Override
    public void doContinueRepay() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void doRePrint(String uuid, int printType, ActionReprintType actionReprintType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void invoiceQrcode() {
        final TradePaymentVo tradePaymentVo = getTradePaymentVo().clone();
        VerifyHelper.verifyAlert(mView.getViewActivity(), getInvoicePermissionCode(),
                new VerifyHelper.Callback() {
                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                        ElectronicInvoiceVo electronicInvoiceVo = getElectronicInvoiceVo();
                        BigDecimal actualAmount = getInvoiceQrcodeMaxAmount(tradePaymentVo);
                        mView.showInvoiceQrcodeDialog(tradePaymentVo.getTradeVo(), electronicInvoiceVo, actualAmount);
                    }
                });
    }

        private BigDecimal getInvoiceQrcodeMaxAmount(TradePaymentVo tradePaymentVo) {
        TradeVo tradeVo = tradePaymentVo.getTradeVo();
        BigDecimal actualAmount = getShiFuAmount(tradePaymentVo.getPaymentVoList());
        if (tradeVo.getTradeDeposit() != null && tradeVo.getTradeDeposit().getDepositPay() != null) {
            actualAmount = actualAmount.subtract(tradeVo.getTradeDeposit().getDepositPay());
        }
        return actualAmount;
    }

    @Override
    public void performInvoiceQrcode(InvoiceQrcodeReq req, final TradeVo tradeVo) {
        mManager.invoiceQrcode(req, LoadingResponseListener.ensure(new ResponseListener<GatewayTransferResp<InvoiceQrcodeResp>>() {
            @Override
            public void onResponse(ResponseObject<GatewayTransferResp<InvoiceQrcodeResp>> response) {
                if (ResponseObject.isOk(response)) {
                    if (response.getContent() != null && !TextUtils.isEmpty(response.getContent().getMessage())) {
                        mView.showToast(response.getContent().getMessage());
                    }
                    if (response.getContent().isOk()) {
                        String url = response.getContent().getResult().getQrcodeUrl();
                        String tradeNo = tradeVo.getTrade().getTradeNo();
                        String deskNo;
                        if (Utils.isNotEmpty(tradeVo.getTradeTableList())) {
                            deskNo = tradeVo.getTradeTableList().get(0).getTableName();
                        } else if (tradeVo.getTradeExtra() != null && !TextUtils.isEmpty(tradeVo.getTradeExtra().getNumberPlate())) {
                            deskNo = tradeVo.getTradeExtra().getNumberPlate();
                        } else {
                            deskNo = tradeVo.getTradeExtra() == null ? "" : tradeVo.getTradeExtra().getSerialNumber();
                        }
                        String amount = String.valueOf(response.getContent().getResult().getInvoice().getTotalAmount());
                                            }
                    AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_INVOICE_QRCODE, getTrade().getId(), getTrade().getUuid(), getTrade().getClientUpdateTime());
                } else {
                    mView.showToast(response.getMessage());
                    AuthLogManager.getInstance().clear();
                }
            }

            @Override
            public void onError(VolleyError error) {
                mView.showToast(error.getMessage());
                AuthLogManager.getInstance().clear();
            }
        }, mView.getViewFragmentManager()));
    }

    @Override
    public void invoiceRevoke() {
        VerifyHelper.verifyAlert(mView.getViewActivity(), getInvoicePermissionCode(),
                new VerifyHelper.Callback() {

                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                        Invoice invoice = getInvoice();
                        if (invoice != null) {
                            mView.showInvoiceRevokeAlert(invoice.getUuid());
                        }
                    }
                });
    }

        protected abstract String getInvoicePermissionCode();

    @Override
    public void performInvoiceRevoke(String invoiceUuid) {
        Invoice invoice = mManager.findInvoice(invoiceUuid);
        if (invoice != null) {
            mManager.invoiceRevoke(invoice, LoadingResponseListener.ensure(new ResponseListener<GatewayTransferResp<Invoice>>() {
                @Override
                public void onResponse(ResponseObject<GatewayTransferResp<Invoice>> response) {
                    if (ResponseObject.isOk(response)) {
                        if (response.getContent().isOk()) {
                            mView.showToast(R.string.order_center_invoice_revoke_success);
                        } else {
                            mView.showToast(response.getContent().getMessage());
                        }
                        AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_INVOICE_REVOKE, getTrade().getId(), getTrade().getUuid(), getTrade().getClientUpdateTime());
                    } else {
                        mView.showToast(response.getMessage());
                        AuthLogManager.getInstance().clear();
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    mView.showToast(error.getMessage());
                    AuthLogManager.getInstance().clear();
                }
            }, mView.getViewFragmentManager()));
        }
    }

    @Override
    public void doRebindDeliveryUser() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void performRebindDeliveryUser(User authUser) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TradeVo compareTradeVo(String uuid, TradeVo tradeVo) {
        if (tradeVo != null && tradeVo.getTrade().getUuid().equals(uuid)) {
            return tradeVo;
        } else {
            return mManager.findTrade(uuid, true);
        }
    }

    @Override
    public boolean showPrivilegeInfo(TradePaymentVo tradePaymentVo) {
        boolean showPrivilegeFlag = showPrivilegeInfo(tradePaymentVo.getTradeVo());
        return tradePaymentVo != null
                && showPrivilegeFlag
                || (!showPrivilegeFlag && getExemptAmount(tradePaymentVo.getPaymentVoList()).compareTo(BigDecimal.ZERO) != 0
                && tradePaymentVo.getTradeVo().getTrade().getTradePayStatus() != TradePayStatus.UNPAID
                && tradePaymentVo.getTradeVo().getTrade().getTradeStatus() != TradeStatus.CREDIT);
    }

    @Override
    public boolean showPrivilegeInfo(TradeVo tradeVo) {
        if (tradeVo != null) {
                        TradePrivilege tradePrivilege = tradeVo.getTradePrivilege();
            if (tradePrivilege != null && tradePrivilege.getPrivilegeAmount().compareTo(BigDecimal.ZERO) != 0) {
                return true;
            }

                        TradePrivilege chargePrivilege = tradeVo.getTradeChargePrivilege();
            if (chargePrivilege != null && chargePrivilege.getPrivilegeAmount().compareTo(BigDecimal.ZERO) != 0) {
                return true;
            }

                        List<TradePromotion> tradePromotions = tradeVo.getTradePromotions();
            if (Utils.isNotEmpty(tradePromotions)) {
                for (TradePromotion tradePromotion : tradePromotions) {
                    if (tradePromotion != null
                            && (QuickDiscountType.MEI_TUAN_QUICK_DISCOUNT == tradePromotion.getSource()
                            || QuickDiscountType.WEIXIN_PAY == tradePromotion.getSource()
                            || QuickDiscountType.ALI_PAY == tradePromotion.getSource())
                            && tradePromotion.getMount().compareTo(BigDecimal.ZERO) != 0) {
                        return true;
                    }
                }
            }

                        List<TradeItemVo> tradeItemVos = getValidTradeItemList(tradeVo.getTradeItemList());
            if (Utils.isNotEmpty(tradeItemVos)) {
                for (TradeItemVo tradeItemVo : tradeItemVos) {
                    if (tradeItemVo.getTradeItemPrivilege() != null
                            && tradeItemVo.getTradeItemPrivilege().getPrivilegeAmount().compareTo(BigDecimal.ZERO) != 0) {
                        return true;
                    } else if (tradeItemVo.getCouponPrivilegeVo() != null && tradeItemVo.getCouponPrivilegeVo().getTradePrivilege() != null) {
                        return true;
                    }
                }
            }

                        List<CouponPrivilegeVo> couponPrivilegeVoList = tradeVo.getCouponPrivilegeVoList();
            if (couponPrivilegeVoList != null) {
                for (CouponPrivilegeVo couponPrivilegeVo : couponPrivilegeVoList) {
                    if (couponPrivilegeVo != null) {
                        return true;
                    }
                }
            }

                        if (tradeVo.getBanquetVo() != null && tradeVo.getBanquetVo().getTradePrivilege().getPrivilegeAmount().compareTo(BigDecimal.ZERO) != 0) {
                return true;
            }

                        IntegralCashPrivilegeVo integralCashPrivilegeVo = tradeVo.getIntegralCashPrivilegeVo();
            if (integralCashPrivilegeVo != null && integralCashPrivilegeVo.getTradePrivilege().getPrivilegeAmount().compareTo(BigDecimal.ZERO) != 0) {
                return true;
            }

                        if (null != tradeVo.getmWeiXinCouponsVo()) {
                for (WeiXinCouponsVo weiXinCouponsVo : tradeVo.getmWeiXinCouponsVo()) {
                    TradePrivilege tradePrivilege1 = weiXinCouponsVo.getmTradePrivilege();
                    if (tradePrivilege1 != null && tradePrivilege1.getPrivilegeAmount().compareTo(BigDecimal.ZERO) != 0) {
                        return true;
                    }
                }
            }

                        List<TradePlanActivity> list = tradeVo.getTradePlanActivityList();
            if (null != list) {
                for (TradePlanActivity tradePlanActivity : list) {
                    if (tradePlanActivity != null && tradePlanActivity.getStatusFlag() == StatusFlag.VALID
                            && tradePlanActivity.getRuleEffective() == ActivityRuleEffective.VALID
                            && tradePlanActivity.getOfferValue().compareTo(BigDecimal.ZERO) != 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private List<TradeItemVo> getValidTradeItemList(List<TradeItemVo> tradeItemList) {
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
    public View createPrivilegeInfoItem(Context context, TradeVo tradeVo, boolean isRefund) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_center_detail_privilegeinfo_item, null);

        if (tradeVo != null) {
            List<String> dataSet02 = new ArrayList<String>();

            List<String> dataSet04 = new ArrayList<String>();
                        List<TradePlanActivity> list = tradeVo.getTradePlanActivityList();
            if (null != list) {
                for (TradePlanActivity tradePlanActivity : list) {
                    if (tradePlanActivity != null && tradePlanActivity.getStatusFlag() == StatusFlag.VALID
                            && tradePlanActivity.getRuleEffective() == ActivityRuleEffective.VALID
                            && tradePlanActivity.getOfferValue().compareTo(BigDecimal.ZERO) != 0) {
                        BigDecimal offerValue;
                        if (isRefund) {
                            offerValue = tradePlanActivity.getOfferValue().negate();
                        } else {
                            offerValue = tradePlanActivity.getOfferValue();
                        }
                        String ruleName;
                        String activityTitle = context.getResources().getString(R.string.activity_title);
                        if (tradePlanActivity.getRuleName() != null) {
                            ruleName = activityTitle + tradePlanActivity.getRuleName() + " (" + Utils.formatPrice(offerValue.doubleValue()) + ")";                        } else {
                            ruleName = activityTitle + " (" + Utils.formatPrice(offerValue.doubleValue()) + ")";
                        }
                        dataSet04.add(ruleName);
                    }
                }
            }
                        String goodsDiscountString =
                    getGoodsPrivilegeString(context, tradeVo.getTradeItemList(), PrivilegeType.DISCOUNT, isRefund);
            if (!TextUtils.isEmpty(goodsDiscountString)) {
                dataSet02.add(goodsDiscountString);
            }

                        String platformPrivilegeString = getPlatformPrivilegeString(context, tradeVo.getTradePromotions(), isRefund);
            if (!TextUtils.isEmpty(platformPrivilegeString)) {
                dataSet02.add(platformPrivilegeString);
            }

                        String goodsRebateString =
                    getGoodsPrivilegeString(context, tradeVo.getTradeItemList(), PrivilegeType.REBATE, isRefund);
            if (!TextUtils.isEmpty(goodsRebateString)) {
                dataSet02.add(goodsRebateString);
            }
                        String goodsCouponString =
                    getGoodsPrivilegeString(context, tradeVo.getTradeItemList(), PrivilegeType.COUPON, isRefund);
            if (!TextUtils.isEmpty(goodsCouponString)) {
                dataSet02.add(goodsCouponString);
            }

                        for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
                if (tradeItemVo.getCouponPrivilegeVo() != null && tradeItemVo.getCouponPrivilegeVo().getTradePrivilege() != null) {
                    String goodsGiftCouponString =
                            getGoodsGiftPrivilegeString(context, tradeItemVo, PrivilegeType.COUPON, isRefund);
                    if (!TextUtils.isEmpty(goodsGiftCouponString)) {
                        dataSet04.add(goodsGiftCouponString);
                    }
                }
            }

                        String goodsFreeString = getGoodsPrivilegeString(context, tradeVo.getTradeItemList(), PrivilegeType.FREE, isRefund);
            if (!TextUtils.isEmpty(goodsFreeString)) {
                dataSet02.add(goodsFreeString);
            }

                        String goodsGiveString = getGoodsPrivilegeString(context, tradeVo.getTradeItemList(), PrivilegeType.GIVE, isRefund);
            if (!TextUtils.isEmpty(goodsGiveString)) {
                dataSet02.add(goodsGiveString);
            }

                        String goodsAutoDiscountString =
                    getGoodsPrivilegeString(context, tradeVo.getTradeItemList(), PrivilegeType.AUTO_DISCOUNT, isRefund);
            if (!TextUtils.isEmpty(goodsAutoDiscountString)) {
                dataSet02.add(goodsAutoDiscountString);
            }

                        String goodsMemberPriceString =
                    getGoodsPrivilegeString(context, tradeVo.getTradeItemList(), PrivilegeType.MEMBER_PRICE, isRefund);
            if (!TextUtils.isEmpty(goodsMemberPriceString)) {
                dataSet02.add(goodsMemberPriceString);
            }

                        String problemDishesString = getGoodsPrivilegeString(context, tradeVo.getTradeItemList(), PrivilegeType.PROBLEM, isRefund);
            if (!TextUtils.isEmpty(problemDishesString)) {
                dataSet02.add(problemDishesString);
            }

                        List<String> orderDiscountStrings = getOrderPrivilegeStrings(tradeVo, PrivilegeType.DISCOUNT, isRefund);
            if (Utils.isNotEmpty(orderDiscountStrings)) {
                dataSet02.addAll(orderDiscountStrings);
            }

                        List<String> orderRebateStrings = getOrderPrivilegeStrings(tradeVo, PrivilegeType.REBATE, isRefund);
            if (Utils.isNotEmpty(orderRebateStrings)) {
                dataSet02.addAll(orderRebateStrings);
            }

                        if (null != tradeVo.getmWeiXinCouponsVo()) {
                for (WeiXinCouponsVo weiXinCouponsVo : tradeVo.getmWeiXinCouponsVo()) {
                    TradePrivilege tradePrivilege = weiXinCouponsVo.getmTradePrivilege();
                    if (tradePrivilege != null) {
                        BigDecimal totalPrivilegeAmount;
                        if (isRefund) {
                            totalPrivilegeAmount = tradePrivilege.getPrivilegeAmount().negate();
                        } else {
                            totalPrivilegeAmount = tradePrivilege.getPrivilegeAmount();
                        }
                        String orderPrivilegeAmountString =
                                context.getString(R.string.dinner_order_center_wechat_card_coupons,
                                        Utils.formatPrice(totalPrivilegeAmount.doubleValue()));
                        dataSet04.add(orderPrivilegeAmountString);
                    }

                }
            }

                        List<String> orderFreeStrings = getOrderPrivilegeStrings(tradeVo, PrivilegeType.FREE, isRefund);
            if (Utils.isNotEmpty(orderFreeStrings)) {
                dataSet02.addAll(orderFreeStrings);
            }

                        List<String> orderPlatformStrings = getOrderPrivilegeStrings(tradeVo, PrivilegeType.PLATFORM, isRefund);
            if (Utils.isNotEmpty(orderPlatformStrings)) {
                dataSet02.addAll(orderPlatformStrings);
            }

                        List<String> orderBusinessStrings = getOrderPrivilegeStrings(tradeVo, PrivilegeType.BUSINESS, isRefund);
            if (Utils.isNotEmpty(orderBusinessStrings)) {
                dataSet02.addAll(orderBusinessStrings);
            }

                        List<String> orderVipStrings = getOrderPrivilegeStrings(tradeVo, PrivilegeType.VIP, isRefund);
            if (Utils.isNotEmpty(orderVipStrings)) {
                dataSet02.addAll(orderVipStrings);
            }

            List<String> dataSetReason = new ArrayList<String>();

            String orderFreeReasonString = getTradeReasonRel(context, tradeVo);
            if (!TextUtils.isEmpty(orderFreeReasonString)) {
                dataSetReason.add(orderFreeReasonString);
            }
            ColumnLayout cl_reason = (ColumnLayout) view.findViewById(R.id.cl_pri_reason);
            cl_reason.setData(dataSetReason);

            ColumnLayout clPri02 = (ColumnLayout) view.findViewById(R.id.cl_pri_02);
            clPri02.setData(dataSet02);
            view.findViewById(R.id.v_line_pri_02).setVisibility(Utils.isNotEmpty(dataSet02) ? View.VISIBLE : View.GONE);
            List<String> dataSet03 = new ArrayList<String>();
                        List<CouponPrivilegeVo> couponPrivilegeVoList = tradeVo.getCouponPrivilegeVoList();
            if (couponPrivilegeVoList != null) {
                for (CouponPrivilegeVo couponPrivilegeVo : couponPrivilegeVoList) {
                    String orderCouponString = getCouponPrivilegeString(context, couponPrivilegeVo, isRefund);
                    if (!TextUtils.isEmpty(orderCouponString)) {
                        dataSet03.add(orderCouponString);
                    }
                }
            }
                        String orderIntegralcashString =
                    getIntegralcashPrivilegeString(context, tradeVo.getIntegralCashPrivilegeVo(), isRefund);
            if (!TextUtils.isEmpty(orderIntegralcashString)) {
                dataSet03.add(orderIntegralcashString);
            }
                        if (tradeVo.getBanquetVo() != null && tradeVo.getBanquetVo().getTradePrivilege() != null) {
                String banquetPrivilegeAmountString;
                if (isRefund) {
                    banquetPrivilegeAmountString = context.getString(R.string.dinner_order_center_order_banquet,
                            Utils.formatPrice(tradeVo.getBanquetVo().getTradePrivilege().getPrivilegeAmount().negate().doubleValue()));
                } else {
                    banquetPrivilegeAmountString = context.getString(R.string.dinner_order_center_order_banquet,
                            Utils.formatPrice(tradeVo.getBanquetVo().getTradePrivilege().getPrivilegeAmount().doubleValue()));
                }
                dataSet03.add(banquetPrivilegeAmountString);

                List<String> dataBanquetSetreason = new ArrayList<String>();
                String banquetReasonString = getBanquetReason(context, tradeVo);
                if (!TextUtils.isEmpty(banquetReasonString)) {
                    dataBanquetSetreason.add(banquetReasonString);
                }
                ColumnLayout cl_reason01 = (ColumnLayout) view.findViewById(R.id.cl_pri_reason01);
                cl_reason01.setData(dataBanquetSetreason);
            }

            ColumnLayout clPri03 = (ColumnLayout) view.findViewById(R.id.cl_pri_03);
            clPri03.setData(dataSet03);

            if (Utils.isNotEmpty(dataSet03)) {
                view.findViewById(R.id.v_line_pri_03).setVisibility(View.VISIBLE);
                view.findViewById(R.id.v_line_pri_02).setVisibility(View.GONE);

            } else {
                view.findViewById(R.id.v_line_pri_03).setVisibility(View.GONE);
            }

            ColumnLayout clPri04 = (ColumnLayout) view.findViewById(R.id.cl_pri_04);
            clPri04.setData(dataSet04);

            if (Utils.isNotEmpty(dataSet04)) {
                view.findViewById(R.id.v_line_pri_04).setVisibility(View.VISIBLE);

            } else {
                view.findViewById(R.id.v_line_pri_04).setVisibility(View.GONE);
            }


                        TextView tv_pri_actual = (TextView) view.findViewById(R.id.tv_pri_actual);
            TextView tv_pri_actual_amount = (TextView) view.findViewById(R.id.tv_pri_actual_amount);
            BigDecimal totalPrivilegeAmount;
            if (isRefund) {
                totalPrivilegeAmount = getTotalPrivilegeAmount(tradeVo).negate();
            } else {
                totalPrivilegeAmount = getTotalPrivilegeAmount(tradeVo);
            }
            String total = context.getString(R.string.dinner_order_center_total_privilege, "");
            tv_pri_actual.setVisibility(View.VISIBLE);
            tv_pri_actual.setText(total);
            tv_pri_actual_amount.setVisibility(View.VISIBLE);
            tv_pri_actual_amount.setText(Utils.formatPrice(totalPrivilegeAmount.doubleValue()));
        }

        return view;
    }

    @Override
    public View createPrivilegeInfoItem(Context context, TradePaymentVo tradePaymentVo, boolean isRefund) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_center_detail_privilegeinfo_item, null);
        TradeVo tradeVo = tradePaymentVo.getTradeVo();

        if (tradeVo != null) {
            List<String> dataSet02 = new ArrayList<String>();

            List<String> dataSet04 = new ArrayList<String>();
                        List<TradePlanActivity> list = tradeVo.getTradePlanActivityList();
            if (null != list) {
                for (TradePlanActivity tradePlanActivity : list) {
                    if (tradePlanActivity != null && tradePlanActivity.getStatusFlag() == StatusFlag.VALID
                            && tradePlanActivity.getRuleEffective() == ActivityRuleEffective.VALID
                            && tradePlanActivity.getOfferValue().compareTo(BigDecimal.ZERO) != 0) {
                        BigDecimal offerValue;
                        if (isRefund) {
                            offerValue = tradePlanActivity.getOfferValue().negate();
                        } else {
                            offerValue = tradePlanActivity.getOfferValue();
                        }
                        String ruleName;
                        String activityTitle = context.getResources().getString(R.string.activity_title);
                        if (tradePlanActivity.getRuleName() != null) {
                            ruleName = activityTitle + tradePlanActivity.getRuleName() + " (" + Utils.formatPrice(offerValue.doubleValue()) + ")";                        } else {
                            ruleName = activityTitle + " (" + Utils.formatPrice(offerValue.doubleValue()) + ")";
                        }
                        dataSet04.add(ruleName);
                    }
                }
            }
                        String goodsDiscountString =
                    getGoodsPrivilegeString(context, tradeVo.getTradeItemList(), PrivilegeType.DISCOUNT, isRefund);
            if (!TextUtils.isEmpty(goodsDiscountString)) {
                dataSet02.add(goodsDiscountString);
            }

                        String platformPrivilegeString = getPlatformPrivilegeString(context, tradeVo.getTradePromotions(), isRefund);
            if (!TextUtils.isEmpty(platformPrivilegeString)) {
                dataSet02.add(platformPrivilegeString);
            }

                        String goodsRebateString =
                    getGoodsPrivilegeString(context, tradeVo.getTradeItemList(), PrivilegeType.REBATE, isRefund);
            if (!TextUtils.isEmpty(goodsRebateString)) {
                dataSet02.add(goodsRebateString);
            }

                        String goodsCouponString =
                    getGoodsPrivilegeString(context, tradeVo.getTradeItemList(), PrivilegeType.COUPON, isRefund);
            if (!TextUtils.isEmpty(goodsCouponString)) {
                dataSet02.add(goodsCouponString);
            }

                        for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
                if (tradeItemVo.getCouponPrivilegeVo() != null && tradeItemVo.getCouponPrivilegeVo().getTradePrivilege() != null) {
                    String goodsGiftCouponString =
                            getGoodsGiftPrivilegeString(context, tradeItemVo, PrivilegeType.COUPON, isRefund);
                    if (!TextUtils.isEmpty(goodsGiftCouponString)) {
                        dataSet04.add(goodsGiftCouponString);
                    }
                }


                                if (tradeItemVo.getCardServicePrivilegeVo() != null && tradeItemVo.getCardServicePrivilegeVo().getTradePrivilege() != null) {
                    String cardString = getCountServicePrivilegeString(context, tradeItemVo, PrivilegeType.CARD_SERVICE, isRefund);
                    if (!TextUtils.isEmpty(cardString)) {
                        dataSet04.add(cardString);
                    }
                }
            }

                        String goodsFreeString = getGoodsPrivilegeString(context, tradeVo.getTradeItemList(), PrivilegeType.FREE, isRefund);
            if (!TextUtils.isEmpty(goodsFreeString)) {
                dataSet02.add(goodsFreeString);
            }

                        String goodsGiveString = getGoodsPrivilegeString(context, tradeVo.getTradeItemList(), PrivilegeType.GIVE, isRefund);
            if (!TextUtils.isEmpty(goodsGiveString)) {
                dataSet02.add(goodsGiveString);
            }

                        String goodsAutoDiscountString =
                    getGoodsPrivilegeString(context, tradeVo.getTradeItemList(), PrivilegeType.AUTO_DISCOUNT, isRefund);
            if (!TextUtils.isEmpty(goodsAutoDiscountString)) {
                dataSet02.add(goodsAutoDiscountString);
            }

                        String goodsMemberRebateString =
                    getGoodsPrivilegeString(context, tradeVo.getTradeItemList(), PrivilegeType.MEMBER_REBATE, isRefund);
            if (!TextUtils.isEmpty(goodsMemberRebateString)) {
                dataSet02.add(goodsMemberRebateString);
            }

                        String goodsMemberPriceString =
                    getGoodsPrivilegeString(context, tradeVo.getTradeItemList(), PrivilegeType.MEMBER_PRICE, isRefund);
            if (!TextUtils.isEmpty(goodsMemberPriceString)) {
                dataSet02.add(goodsMemberPriceString);
            }

                        String problemDishesString = getGoodsPrivilegeString(context, tradeVo.getTradeItemList(), PrivilegeType.PROBLEM, isRefund);
            if (!TextUtils.isEmpty(problemDishesString)) {
                dataSet02.add(problemDishesString);
            }

                        List<String> orderDiscountStrings = getOrderPrivilegeStrings(tradeVo, PrivilegeType.DISCOUNT, isRefund);
            if (Utils.isNotEmpty(orderDiscountStrings)) {
                dataSet02.addAll(orderDiscountStrings);
            }

                        List<String> memberChargeDiscountStrings = getOrderPrivilegeStrings(tradeVo, PrivilegeType.CHARGE_DISCOUNT, isRefund);
            if (Utils.isNotEmpty(memberChargeDiscountStrings)) {
                dataSet02.addAll(memberChargeDiscountStrings);
            }

                        List<String> memberChargeRebateStrings = getOrderPrivilegeStrings(tradeVo, PrivilegeType.CHARGE_REBATE, isRefund);
            if (Utils.isNotEmpty(memberChargeRebateStrings)) {
                dataSet02.addAll(memberChargeRebateStrings);
            }

                        List<String> orderRebateStrings = getOrderPrivilegeStrings(tradeVo, PrivilegeType.REBATE, isRefund);
            if (Utils.isNotEmpty(orderRebateStrings)) {
                dataSet02.addAll(orderRebateStrings);
            }

                        if (null != tradeVo.getmWeiXinCouponsVo()) {
                for (WeiXinCouponsVo weiXinCouponsVo : tradeVo.getmWeiXinCouponsVo()) {
                    TradePrivilege tradePrivilege = weiXinCouponsVo.getmTradePrivilege();
                    if (tradePrivilege != null) {
                        BigDecimal totalPrivilegeAmount;
                        if (isRefund) {
                            totalPrivilegeAmount = tradePrivilege.getPrivilegeAmount().negate();
                        } else {
                            totalPrivilegeAmount = tradePrivilege.getPrivilegeAmount();
                        }
                        String orderPrivilegeAmountString =
                                context.getString(R.string.dinner_order_center_wechat_card_coupons,
                                        Utils.formatPrice(totalPrivilegeAmount.doubleValue()));
                        dataSet04.add(orderPrivilegeAmountString);
                    }

                }
            }

            if (tradeVo.getTrade().getTradeStatus() != TradeStatus.CREDIT) {
                String exemptAmountStr = getExemptAmountString(context, tradePaymentVo.getPaymentVoList(), isRefund);
                if (!TextUtils.isEmpty(exemptAmountStr)) {
                    dataSet04.add(exemptAmountStr);
                }
            }

                        List<String> orderFreeStrings = getOrderPrivilegeStrings(tradeVo, PrivilegeType.FREE, isRefund);
            if (Utils.isNotEmpty(orderFreeStrings)) {
                dataSet02.addAll(orderFreeStrings);
            }

                        List<String> orderPlatformStrings = getOrderPrivilegeStrings(tradeVo, PrivilegeType.PLATFORM, isRefund);
            if (Utils.isNotEmpty(orderPlatformStrings)) {
                dataSet02.addAll(orderPlatformStrings);
            }

                        List<String> orderBusinessStrings = getOrderPrivilegeStrings(tradeVo, PrivilegeType.BUSINESS, isRefund);
            if (Utils.isNotEmpty(orderBusinessStrings)) {
                dataSet02.addAll(orderBusinessStrings);
            }

                        List<String> orderVipStrings = getOrderPrivilegeStrings(tradeVo, PrivilegeType.VIP, isRefund);
            if (Utils.isNotEmpty(orderVipStrings)) {
                dataSet02.addAll(orderVipStrings);
            }

            List<String> dataSetReason = new ArrayList<String>();

            String orderFreeReasonString = getTradeReasonRel(context, tradeVo);
            if (!TextUtils.isEmpty(orderFreeReasonString)) {
                dataSetReason.add(orderFreeReasonString);
            }
            ColumnLayout cl_reason = (ColumnLayout) view.findViewById(R.id.cl_pri_reason);
            cl_reason.setData(dataSetReason);

            ColumnLayout clPri02 = (ColumnLayout) view.findViewById(R.id.cl_pri_02);
            clPri02.setData(dataSet02);
            view.findViewById(R.id.v_line_pri_02).setVisibility(Utils.isNotEmpty(dataSet02) ? View.VISIBLE : View.GONE);
            List<String> dataSet03 = new ArrayList<String>();
                        List<CouponPrivilegeVo> couponPrivilegeVoList = tradeVo.getCouponPrivilegeVoList();
            if (couponPrivilegeVoList != null) {
                for (CouponPrivilegeVo couponPrivilegeVo : couponPrivilegeVoList) {
                    String orderCouponString = getCouponPrivilegeString(context, couponPrivilegeVo, isRefund);
                    if (!TextUtils.isEmpty(orderCouponString)) {
                        dataSet03.add(orderCouponString);
                    }
                }
            }
                        String orderIntegralcashString =
                    getIntegralcashPrivilegeString(context, tradeVo.getIntegralCashPrivilegeVo(), isRefund);
            if (!TextUtils.isEmpty(orderIntegralcashString)) {
                dataSet03.add(orderIntegralcashString);
            }
                        if (tradeVo.getBanquetVo() != null && tradeVo.getBanquetVo().getTradePrivilege() != null) {
                String banquetPrivilegeAmountString;
                if (isRefund) {
                    banquetPrivilegeAmountString = context.getString(R.string.dinner_order_center_order_banquet,
                            Utils.formatPrice(tradeVo.getBanquetVo().getTradePrivilege().getPrivilegeAmount().negate().doubleValue()));
                } else {
                    banquetPrivilegeAmountString = context.getString(R.string.dinner_order_center_order_banquet,
                            Utils.formatPrice(tradeVo.getBanquetVo().getTradePrivilege().getPrivilegeAmount().doubleValue()));
                }
                dataSet03.add(banquetPrivilegeAmountString);

                List<String> dataBanquetSetreason = new ArrayList<String>();
                String banquetReasonString = getBanquetReason(context, tradeVo);
                if (!TextUtils.isEmpty(banquetReasonString)) {
                    dataBanquetSetreason.add(banquetReasonString);
                }
                ColumnLayout cl_reason01 = (ColumnLayout) view.findViewById(R.id.cl_pri_reason01);
                cl_reason01.setData(dataBanquetSetreason);
            }

            ColumnLayout clPri03 = (ColumnLayout) view.findViewById(R.id.cl_pri_03);
            clPri03.setData(dataSet03);

            if (Utils.isNotEmpty(dataSet03)) {
                view.findViewById(R.id.v_line_pri_03).setVisibility(View.VISIBLE);
                view.findViewById(R.id.v_line_pri_02).setVisibility(View.GONE);

            } else {
                view.findViewById(R.id.v_line_pri_03).setVisibility(View.GONE);
            }

            ColumnLayout clPri04 = (ColumnLayout) view.findViewById(R.id.cl_pri_04);
            clPri04.setData(dataSet04);

            if (Utils.isNotEmpty(dataSet04)) {
                view.findViewById(R.id.v_line_pri_04).setVisibility(View.VISIBLE);

            } else {
                view.findViewById(R.id.v_line_pri_04).setVisibility(View.GONE);
            }


                        TextView tv_pri_actual = (TextView) view.findViewById(R.id.tv_pri_actual);
            TextView tv_pri_actual_amount = (TextView) view.findViewById(R.id.tv_pri_actual_amount);

            BigDecimal totalPrivilegeAmount;
            if (isRefund) {
                totalPrivilegeAmount = getTotalPrivilegeAmount(tradeVo).negate();
            } else {
                totalPrivilegeAmount = getTotalPrivilegeAmount(tradeVo);
            }
            if (tradeVo.getTrade().getTradeStatus() != TradeStatus.CREDIT) {
                BigDecimal exemptAmount = getExemptAmount(tradePaymentVo.getPaymentVoList()).negate();
                totalPrivilegeAmount = totalPrivilegeAmount.add(exemptAmount);
            }
            String total = context.getString(R.string.dinner_order_center_total_privilege, "");
            tv_pri_actual.setVisibility(View.VISIBLE);
            tv_pri_actual.setText(total);
            tv_pri_actual_amount.setVisibility(View.VISIBLE);
            tv_pri_actual_amount.setText(Utils.formatPrice(totalPrivilegeAmount.doubleValue()));
        }

        return view;
    }

    private String getPlatformPrivilegeString(Context context, List<TradePromotion> tradePromotions, boolean isRefund) {
        String platformPrivilegeString = "";
        BigDecimal platformPrivilegeAmount = getPlatformPrivilegeAmount(tradePromotions, isRefund);
        if (platformPrivilegeAmount != null && platformPrivilegeAmount.compareTo(BigDecimal.ZERO) != 0) {
            platformPrivilegeString = context.getString(R.string.dinner_order_center_order_platform, platformPrivilegeAmount);
        }

        return platformPrivilegeString;
    }


    private String getCountServicePrivilegeString(Context context, TradeItemVo tradeItemVo,
                                                  PrivilegeType privilegeType, boolean isrefund) {
        String countServicePrivilegeAmountString = "";
        BigDecimal countServicePrivilegeAmount = BigDecimal.ZERO;
        if (tradeItemVo.getCardServicePrivilegeVo() != null && tradeItemVo.getCardServicePrivilegeVo().getTradePrivilege().getPrivilegeType() == privilegeType) {
            countServicePrivilegeAmount = tradeItemVo.getCardServicePrivilegeVo().getTradePrivilege().getPrivilegeAmount();
        }
        if (isrefund) {
            countServicePrivilegeAmount = countServicePrivilegeAmount.negate();
        }
        countServicePrivilegeAmountString = context.getString(R.string.beauty_order_center_count_service, Utils.formatPrice(countServicePrivilegeAmount.doubleValue()), getCountServiceName(tradeItemVo));
        return countServicePrivilegeAmountString;
    }

    private BigDecimal getPlatformPrivilegeAmount(List<TradePromotion> tradePromotions, boolean isRefund) {
        BigDecimal platformPrivilegeAmount = BigDecimal.ZERO;
        if (Utils.isNotEmpty(tradePromotions)) {
            for (TradePromotion tradePromotion : tradePromotions) {
                if (tradePromotion != null
                        && (QuickDiscountType.MEI_TUAN_QUICK_DISCOUNT == tradePromotion.getSource()
                        || QuickDiscountType.WEIXIN_PAY == tradePromotion.getSource()
                        || QuickDiscountType.ALI_PAY == tradePromotion.getSource())
                        && tradePromotion.getMount().compareTo(BigDecimal.ZERO) != 0) {
                    platformPrivilegeAmount = platformPrivilegeAmount.add(tradePromotion.getMount());
                }
            }

            if (isRefund) {
                platformPrivilegeAmount = platformPrivilegeAmount.negate();
            }
        }

        return platformPrivilegeAmount;
    }



    private static String getCountServiceName(TradeItemVo tradeItemVo) {
        if (tradeItemVo.getCardServicePrivilegeVo() != null
                && tradeItemVo.getCardServicePrivilegeVo().getTradePrivilege().getPrivilegeType() == PrivilegeType.CARD_SERVICE
                && tradeItemVo.getCardServicePrivilegeVo().getTradePrivilege() != null) {
            TradePrivilege countService = tradeItemVo.getCardServicePrivilegeVo().getTradePrivilege();
            return countService.getPrivilegeName();
        }

        return "";
    }


    private String getGoodsPrivilegeString(Context context, List<TradeItemVo> tradeItemVos,
                                           PrivilegeType privilegeType, Boolean isRefund) {
        BigDecimal goodsPrivilegeAmount = getGoodsPrivilegeAmount(tradeItemVos, privilegeType, isRefund);
        String goodsPrivilegeAmountString = "";
        if (goodsPrivilegeAmount != null && goodsPrivilegeAmount.compareTo(BigDecimal.ZERO) != 0) {
            switch (privilegeType) {
                case DISCOUNT:
                    goodsPrivilegeAmountString = context.getString(R.string.dinner_order_center_goods_discount,
                            Utils.formatPrice(goodsPrivilegeAmount.doubleValue()));
                    break;
                case REBATE:
                    goodsPrivilegeAmountString = context.getString(R.string.dinner_order_center_goods_rebate,
                            Utils.formatPrice(goodsPrivilegeAmount.doubleValue()));
                    break;
                case FREE:
                case GIVE:
                    goodsPrivilegeAmountString = context.getString(R.string.dinner_order_center_goods_free,
                            Utils.formatPrice(goodsPrivilegeAmount.doubleValue()));
                    break;
                case AUTO_DISCOUNT:
                    goodsPrivilegeAmountString = context.getString(R.string.dinner_order_center_goods_auto_discount,
                            Utils.formatPrice(goodsPrivilegeAmount.doubleValue()));
                    break;
                case MEMBER_PRICE:
                    goodsPrivilegeAmountString = context.getString(R.string.dinner_order_center_goods_member_price,
                            Utils.formatPrice(goodsPrivilegeAmount.doubleValue()));
                case MEMBER_REBATE:
                    goodsPrivilegeAmountString = context.getString(R.string.dinner_order_center_goods_member_rebate,
                            Utils.formatPrice(goodsPrivilegeAmount.doubleValue()));
                    break;
                case PROBLEM:
                    goodsPrivilegeAmountString = context.getString(R.string.dinner_order_center_problem_dishes,
                            Utils.formatPrice(goodsPrivilegeAmount.doubleValue()));
                    break;
                default:
                    break;
            }
        }
        return goodsPrivilegeAmountString;
    }

    private BigDecimal getGoodsPrivilegeAmount(List<TradeItemVo> tradeItemVos,
                                               PrivilegeType privilegeType, Boolean isRefund) {
        BigDecimal goodsPrivilegeAmount;
        if (isRefund) {
            goodsPrivilegeAmount = getGoodsPrivilegeAmount(tradeItemVos, privilegeType).negate();
        } else {
            goodsPrivilegeAmount = getGoodsPrivilegeAmount(tradeItemVos, privilegeType);
        }
        return goodsPrivilegeAmount;
    }


    private String getGoodsGiftPrivilegeString(Context context, TradeItemVo tradeItemVo,
                                               PrivilegeType privilegeType, boolean isrefund) {
        String goodsPrivilegeAmountString = "";
        BigDecimal goodsPrivilegeAmount = BigDecimal.ZERO;
        if (tradeItemVo.getCouponPrivilegeVo() != null && tradeItemVo.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeType() == privilegeType) {
            goodsPrivilegeAmount = tradeItemVo.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeAmount();
        }
        if (isrefund) {
            goodsPrivilegeAmount = goodsPrivilegeAmount.negate();
        }
        goodsPrivilegeAmountString = context.getString(R.string.order_center_coupon_gift,
                Utils.formatPrice(goodsPrivilegeAmount.doubleValue()), getCouponName(tradeItemVo));
        return goodsPrivilegeAmountString;
    }


    private static String getCouponName(TradeItemVo tradeItemVo) {
        if (tradeItemVo.getCouponPrivilegeVo() != null
                && tradeItemVo.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeType() == PrivilegeType.COUPON
                && tradeItemVo.getCouponPrivilegeVo().getCoupon() != null) {
            Coupon coupon = tradeItemVo.getCouponPrivilegeVo().getCoupon();
            return coupon.getName();
        }

        return "";
    }

    private static BigDecimal getGoodsPrivilegeAmount(List<TradeItemVo> tradeItemVos, PrivilegeType privilegeType) {
        BigDecimal goodsPrivilegeAmount = BigDecimal.ZERO;
        for (TradeItemVo tradeItemVo : tradeItemVos) {
            TradePrivilege tradePrivilege = tradeItemVo.getTradeItemPrivilege();
            if (tradePrivilege != null && tradePrivilege.getPrivilegeType() == privilegeType) {
                goodsPrivilegeAmount = goodsPrivilegeAmount.add(tradePrivilege.getPrivilegeAmount());
            }
            if (tradeItemVo.getCouponPrivilegeVo() != null && tradeItemVo.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeType() == privilegeType) {
                tradePrivilege = tradeItemVo.getCouponPrivilegeVo().getTradePrivilege();
                goodsPrivilegeAmount = goodsPrivilegeAmount.add(tradePrivilege.getPrivilegeAmount());
            }
        }

        return goodsPrivilegeAmount;
    }

        private List<String> getOrderPrivilegeStrings(TradeVo tradeVo, PrivilegeType privilegeType, boolean isRefund) {
                List<TradePrivilege> tradePrivileges = getTradePrivileges(tradeVo, privilegeType);
        if (Utils.isNotEmpty(tradePrivileges)) {
            List<String> orderPrivilegeStrings = new ArrayList<String>();
            for (TradePrivilege tradePrivilege : tradePrivileges) {
                BigDecimal orderPrivilegeAmount;
                if (isRefund) {
                    orderPrivilegeAmount = tradePrivilege.getPrivilegeAmount().negate();
                } else {
                    orderPrivilegeAmount = tradePrivilege.getPrivilegeAmount();
                }

                String orderPrivilegeString = "";
                if (orderPrivilegeAmount.compareTo(BigDecimal.ZERO) != 0) {
                    switch (privilegeType) {
                        case DISCOUNT:
                            orderPrivilegeString = mView.getViewActivity().getString(R.string.dinner_order_center_order_discount,
                                    Utils.formatPrice(orderPrivilegeAmount.doubleValue()));
                            break;
                        case REBATE:
                            orderPrivilegeString = mView.getViewActivity().getString(R.string.dinner_order_center_order_rebate,
                                    Utils.formatPrice(orderPrivilegeAmount.doubleValue()));
                            break;
                        case FREE:
                            orderPrivilegeString = mView.getViewActivity().getString(R.string.dinner_order_center_order_free,
                                    Utils.formatPrice(orderPrivilegeAmount.doubleValue()));
                            break;
                        case BUSINESS:
                            orderPrivilegeString = mView.getViewActivity().getString(R.string.dinner_order_center_order_business,
                                    Utils.formatPrice(orderPrivilegeAmount.doubleValue()));
                            break;
                        case PLATFORM:
                            orderPrivilegeString = mView.getViewActivity().getString(R.string.dinner_order_center_order_platform,
                                    Utils.formatPrice(orderPrivilegeAmount.doubleValue()));
                            break;
                        case VIP:
                            orderPrivilegeString = mView.getViewActivity().getString(R.string.dinner_order_center_order_vip,
                                    Utils.formatPrice(orderPrivilegeAmount.doubleValue()));
                            break;
                        case WECHAT_CARD_COUPONS:
                            orderPrivilegeString = mView.getViewActivity().getString(R.string.dinner_order_center_wechat_card_coupons,
                                    Utils.formatPrice(orderPrivilegeAmount.doubleValue()));
                            break;
                        case CHARGE_DISCOUNT:
                            orderPrivilegeString = mView.getViewActivity().getString(R.string.dinner_order_center_order_member_discount,
                                    Utils.formatPrice(orderPrivilegeAmount.doubleValue()));
                            break;
                        case CHARGE_REBATE:
                            orderPrivilegeString = mView.getViewActivity().getString(R.string.dinner_order_center_order_member_rebate,
                                    Utils.formatPrice(orderPrivilegeAmount.doubleValue()));
                            break;
                        default:
                            break;
                    }
                }

                if (!TextUtils.isEmpty(orderPrivilegeString)) {
                    orderPrivilegeStrings.add(orderPrivilegeString);
                }
            }

            return orderPrivilegeStrings;
        }

        return Collections.emptyList();
    }

        private List<TradePrivilege> getTradePrivileges(TradeVo tradeVo, PrivilegeType privilegeType) {
        if (tradeVo != null) {
            List<TradePrivilege> tradePrivileges = tradeVo.getTradePrivileges();
            if (tradePrivileges != null) {
                                List<TradePrivilege> specifiedTradePrivileges = new ArrayList<TradePrivilege>();

                for (TradePrivilege tradePrivilege : tradePrivileges) {
                    if (tradePrivilege.getPrivilegeType() == privilegeType) {
                        specifiedTradePrivileges.add(tradePrivilege);
                    }
                }

                return specifiedTradePrivileges;
            }
        }

        return Collections.emptyList();
    }

        private String getTradeReasonRel(Context context, TradeVo tradeVo) {
        if (hasTradePrivilege(tradeVo, PrivilegeType.FREE)) {
            TradeReasonRel operateReason;
            if (mView.isFromSnack()) {
                operateReason = tradeVo.getOperateReason(OperateType.TRADE_FASTFOOD_FREE);
            } else {
                operateReason = tradeVo.getOperateReason(OperateType.TRADE_DINNER_FREE);
            }
            if (operateReason == null) {
                return null;
            }
            String reason =
                    context.getString(R.string.dinner_order_center_order_free_reason, operateReason.getReasonContent());
            return reason;
        } else if (hasTradePrivilege(tradeVo, PrivilegeType.DISCOUNT)) {
            TradeReasonRel operateReason = tradeVo.getOperateReason(OperateType.TRADE_DISCOUNT);
            if (operateReason == null) {
                return null;
            }
            String reason =
                    context.getString(R.string.dinner_order_center_order_discount_reason, operateReason.getReasonContent());
            return reason;
        } else if (hasTradePrivilege(tradeVo, PrivilegeType.REBATE)) {
            TradeReasonRel operateReason = tradeVo.getOperateReason(OperateType.TRADE_REBATE);
            if (operateReason == null) {
                return null;
            }
            String reason =
                    context.getString(R.string.dinner_order_center_order_rebate_reason, operateReason.getReasonContent());
            return reason;
        } else {
            return null;
        }
    }

        private static boolean hasTradePrivilege(TradeVo tradeVo, PrivilegeType privilegeType) {
        List<TradePrivilege> tradePrivileges = tradeVo.getTradePrivileges();
        if (Utils.isNotEmpty(tradePrivileges)) {
            for (TradePrivilege tradePrivilege : tradePrivileges) {
                if (tradePrivilege.getPrivilegeType() == privilegeType) {
                    return true;
                }
            }
        }

        return false;
    }


    private static String getCouponPrivilegeString(Context context, CouponPrivilegeVo couponPrivilegeVo, boolean isRefund) {
        String couponPrivilegeAmountString = "";
        if (couponPrivilegeVo != null) {
            BigDecimal couponPrivilegeAmount = BigDecimal.ZERO;
            TradePrivilege tradePrivilege = couponPrivilegeVo.getTradePrivilege();
            if (tradePrivilege != null) {
                if (isRefund) {
                    couponPrivilegeAmount = tradePrivilege.getPrivilegeAmount().negate();
                } else {
                    couponPrivilegeAmount = tradePrivilege.getPrivilegeAmount();
                }
            }

            Coupon coupon = couponPrivilegeVo.getCoupon();
            if (coupon != null) {
                switch (coupon.getCouponType()) {
                    case REBATE:
                        couponPrivilegeAmountString = context.getString(R.string.order_center_coupon_rebate,
                                Utils.formatPrice(couponPrivilegeAmount.doubleValue()), coupon.getName());
                        break;
                    case DISCOUNT:
                        couponPrivilegeAmountString = context.getString(R.string.order_center_coupon_discount,
                                Utils.formatPrice(couponPrivilegeAmount.doubleValue()), coupon.getName());
                        break;
                    case GIFT:
                        couponPrivilegeAmountString = context.getString(R.string.order_center_order_coupon_gift, coupon.getName());
                        break;
                    case CASH:
                        couponPrivilegeAmountString = context.getString(R.string.order_center_coupon_cash,
                                Utils.formatPrice(couponPrivilegeAmount.doubleValue()), coupon.getName());
                        break;
                    default:
                        break;
                }
            } else if (tradePrivilege != null) {
                if (tradePrivilege.getPrivilegeType() == PrivilegeType.COUPON) {
                    couponPrivilegeAmountString = context.getString(R.string.keyat_yaz_order_center_coupon,
                            Utils.formatPrice(couponPrivilegeAmount.doubleValue()), tradePrivilege.getPrivilegeName());
                }
            }
        }
        return couponPrivilegeAmountString;
    }


    public static String getIntegralcashPrivilegeString(Context context,
                                                        IntegralCashPrivilegeVo integralCashPrivilegeVo, boolean isRefund) {
        String integralcashPrivilegeAmountString = "";
        if (integralCashPrivilegeVo != null) {
            BigDecimal integralcashPrivilegeAmount = BigDecimal.ZERO;
            TradePrivilege tradePrivilege = integralCashPrivilegeVo.getTradePrivilege();
            if (tradePrivilege != null) {
                if (isRefund) {
                    integralcashPrivilegeAmount = tradePrivilege.getPrivilegeAmount().negate();
                } else {
                    integralcashPrivilegeAmount = tradePrivilege.getPrivilegeAmount();
                }
            }

            if (integralcashPrivilegeAmount.compareTo(BigDecimal.ZERO) != 0) {
                integralcashPrivilegeAmountString = context.getString(R.string.dinner_order_center_integralcash,
                        Utils.formatPrice(integralcashPrivilegeAmount.doubleValue()),
                        MathDecimal.trimZero(tradePrivilege.getPrivilegeValue()));
            }
        }

        return integralcashPrivilegeAmountString;
    }

        private static String getBanquetReason(Context context, TradeVo tradeVo) {
        if (tradeVo.getBanquetVo() != null && tradeVo.getBanquetVo().getTradePrivilege() != null && tradeVo.getBanquetVo().getTradePrivilege().isValid()) {
            TradeReasonRel operateReason = tradeVo.getOperateReason(OperateType.TRADE_BANQUET);
            if (operateReason == null) {
                return null;
            }
            String reason =
                    context.getString(R.string.dinner_order_center_order_banquet_reason, operateReason.getReasonContent());
            return reason;
        }
        return null;
    }


    public static BigDecimal getTotalPrivilegeAmount(TradeVo tradeVo) {
        BigDecimal totalPrivilegeAmount = BigDecimal.ZERO;

        List<TradePrivilege> tradePrivileges = tradeVo.getTradePrivileges();
        if (tradePrivileges != null) {

            for (TradePrivilege tradePrivilege1 : tradePrivileges) {
                switch (tradePrivilege1.getPrivilegeType()) {                                        case DISCOUNT:
                    case REBATE:
                    case FREE:
                    case PLATFORM:
                    case BUSINESS:
                    case VIP:
                    case CHARGE_DISCOUNT:
                    case CHARGE_REBATE:
                    case MEMBER_PRICE:
                    case MEMBER_REBATE:
                    case AUTO_DISCOUNT:
                        totalPrivilegeAmount = totalPrivilegeAmount.add(tradePrivilege1.getPrivilegeAmount());
                    default:
                        break;
                }
            }
        }

                List<TradePromotion> tradePromotions = tradeVo.getTradePromotions();
        if (Utils.isNotEmpty(tradePromotions)) {
            for (TradePromotion tradePromotion : tradePromotions) {
                if (tradePromotion != null
                        && (QuickDiscountType.MEI_TUAN_QUICK_DISCOUNT == tradePromotion.getSource()
                        || QuickDiscountType.WEIXIN_PAY == tradePromotion.getSource()
                        || QuickDiscountType.ALI_PAY == tradePromotion.getSource())
                        && tradePromotion.getMount().compareTo(BigDecimal.ZERO) != 0) {
                    totalPrivilegeAmount = totalPrivilegeAmount.add(tradePromotion.getMount());
                }
            }
        }

                List<CouponPrivilegeVo> couponPrivilegeVoList = tradeVo.getCouponPrivilegeVoList();
        if (couponPrivilegeVoList != null && couponPrivilegeVoList.size() > 0) {
            for (CouponPrivilegeVo couponPrivilegeVo : couponPrivilegeVoList) {
                if (couponPrivilegeVo != null && couponPrivilegeVo.getTradePrivilege() != null) {
                    totalPrivilegeAmount = totalPrivilegeAmount.add(couponPrivilegeVo.getTradePrivilege().getPrivilegeAmount());
                }
            }
        }
                BanquetVo bVo = tradeVo.getBanquetVo();
        if (bVo != null) {
            totalPrivilegeAmount = totalPrivilegeAmount.add(bVo.getTradePrivilege().getPrivilegeAmount());
        }
                IntegralCashPrivilegeVo integralCashPrivilegeVo = tradeVo.getIntegralCashPrivilegeVo();
        if (integralCashPrivilegeVo != null && integralCashPrivilegeVo.getTradePrivilege() != null) {
            totalPrivilegeAmount = totalPrivilegeAmount.add(integralCashPrivilegeVo.getTradePrivilege().getPrivilegeAmount());
        }
                List<TradeItemVo> tradeItemVos = tradeVo.getTradeItemList();
        if (tradeItemVos != null) {
            for (TradeItemVo tradeItemVo : tradeItemVos) {
                if (tradeItemVo != null && tradeItemVo.getTradeItemPrivilege() != null) {
                    totalPrivilegeAmount = totalPrivilegeAmount.add(tradeItemVo.getTradeItemPrivilege().getPrivilegeAmount());
                }
                                if (tradeItemVo != null && tradeItemVo.getCouponPrivilegeVo() != null) {
                    totalPrivilegeAmount = totalPrivilegeAmount.add(tradeItemVo.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeAmount());
                }
            }
        }

        if (null != tradeVo.getmWeiXinCouponsVo()) {
            for (WeiXinCouponsVo weiXinCouponsVo : tradeVo.getmWeiXinCouponsVo()) {
                TradePrivilege tradePrivilege1 = weiXinCouponsVo.getmTradePrivilege();
                if (tradePrivilege1 != null) {
                    totalPrivilegeAmount = totalPrivilegeAmount.add(tradePrivilege1.getPrivilegeAmount());
                }

            }
        }
                List<TradePlanActivity> list = tradeVo.getTradePlanActivityList();
        if (null != list) {
            for (TradePlanActivity tradePlanActivity : list) {
                if (tradePlanActivity != null && tradePlanActivity.getStatusFlag() == StatusFlag.VALID
                        && tradePlanActivity.getRuleEffective() == ActivityRuleEffective.VALID
                        && tradePlanActivity.getOfferValue().compareTo(BigDecimal.ZERO) != 0) {
                    totalPrivilegeAmount = totalPrivilegeAmount.add(tradePlanActivity.getOfferValue());
                }
            }
        }
        return totalPrivilegeAmount;
    }

    @Override
    public boolean showPayInfo(TradeVo tradeVo, List<PaymentVo> paymentVos) {
        if (Utils.isNotEmpty(paymentVos)) {
            return true;
        }

        if (tradeVo != null) {

            Trade trade = tradeVo.getTrade();
            if (tradeVo.getTradeEarnestMoney() > 0) {                return true;
            }
            if (null != trade && trade.getTradePayStatus() != TradePayStatus.PAID) {
                return false;
            }
                        TradePrivilege tradePrivilege = tradeVo.getTradePrivilege();
            if (tradePrivilege != null) {
                return true;
            }

            List<TradePromotion> tradePromotions = tradeVo.getTradePromotions();
            if (Utils.isNotEmpty(tradePromotions)) {
                return true;
            }

                        List<TradeCustomer> tradeCustomers = tradeVo.getTradeCustomerList();
            if (Utils.isNotEmpty(tradeCustomers)) {
                for (TradeCustomer tradeCustomer : tradeCustomers) {
                    if (tradeCustomer.getCustomerType() == CustomerType.MEMBER) {
                        return true;
                    }
                }
            }

                        List<TradeItemVo> tradeItemVos = getValidTradeItemList(tradeVo.getTradeItemList());
            if (Utils.isNotEmpty(tradeItemVos)) {
                for (TradeItemVo tradeItemVo : tradeItemVos) {
                    if (tradeItemVo.getTradeItemPrivilege() != null) {
                        return true;
                    }
                }
            }

                        List<CouponPrivilegeVo> couponPrivilegeVoList = tradeVo.getCouponPrivilegeVoList();
            if (couponPrivilegeVoList != null && couponPrivilegeVoList.size() > 0) {
                return true;
            }

                        IntegralCashPrivilegeVo integralCashPrivilegeVo = tradeVo.getIntegralCashPrivilegeVo();
            if (integralCashPrivilegeVo != null) {
                return true;
            }
        }

        return false;
    }

    @Override
    public View createPayInfoItem(Context context, TradePaymentVo tradePaymentVo, TradePaymentVo oriTradePaymentVo, boolean isRefund) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_center_detail_payinfo_item, null);

        TradeVo tradeVo = tradePaymentVo.getTradeVo();

        if (tradeVo.getTradeDeposit() != null && isRefund) {            tradeVo.getTradeDeposit().setDepositPay(tradeVo.getTradeDeposit().getDepositPay().multiply(new BigDecimal(-1)));
            tradeVo.getTradeDeposit().setDepositRefund(tradeVo.getTradeDeposit().getDepositRefund().multiply(new BigDecimal(-1)));
        }

        List<PaymentVo> paymentVos = tradePaymentVo.getPaymentVoList();

        ColumnLayout cl00 = (ColumnLayout) view.findViewById(R.id.cl_00);
        List<String> dataSet0 = new ArrayList<String>();
                String goodsAmountString = getGoodsAmountString(context, tradeVo, isRefund);
        if (!TextUtils.isEmpty(goodsAmountString)) {
            dataSet0.add(goodsAmountString);
        }
                String exchargeAmountString = getExchargeAmountString(context, tradeVo, isRefund);
        if (!TextUtils.isEmpty(exchargeAmountString)) {
            dataSet0.add(exchargeAmountString);
        }
                BigDecimal totalPrivilegeAmount;
        if (isRefund) {
            totalPrivilegeAmount = getTotalPrivilegeAmount(tradeVo).negate();
        } else {
            totalPrivilegeAmount = getTotalPrivilegeAmount(tradeVo);
        }
        if (totalPrivilegeAmount.compareTo(BigDecimal.ZERO) != 0) {
                        if (Utils.isEmpty(dataSet0)) {
                dataSet0.add("");
            }
            dataSet0.add(context.getString(R.string.dinner_order_center_total_privilege,
                    Utils.formatPrice(totalPrivilegeAmount.doubleValue())));
        }
                String carryRuleString = getCarryRuleAmountString(context, tradeVo);
        if (!TextUtils.isEmpty(carryRuleString)) {
            dataSet0.add(carryRuleString);
        }
        cl00.setData(dataSet0);
        view.findViewById(R.id.v_line_00).setVisibility(Utils.isNotEmpty(dataSet0) ? View.VISIBLE : View.GONE);
                ColumnLayout cl10 = (ColumnLayout) view.findViewById(R.id.cl_10);
        List<String> dataSet10 = new ArrayList<String>();
        String depositAmountString = getDepositAmountString(context, tradeVo);
        if (!TextUtils.isEmpty(depositAmountString)) {
            cl10.setVisibility(View.VISIBLE);
            view.findViewById(R.id.v_line_11).setVisibility(View.VISIBLE);
            dataSet10.add(depositAmountString);
            cl10.setData(dataSet10);
        }
                if (tradeVo.getTradeEarnestMoney() > 0) {
            String earnestAmountString = getEarnestAmountString(context, tradeVo);
            if (!TextUtils.isEmpty(earnestAmountString)) {
                cl10.setVisibility(View.VISIBLE);
                view.findViewById(R.id.v_line_11).setVisibility(View.VISIBLE);
                dataSet10.add(earnestAmountString);
                cl10.setData(dataSet10);
            }
        }
        List<String> dataSet05 = new ArrayList<String>();
                String yingShouAmountString = getYingShouAmountString(context, paymentVos, tradeVo, isRefund);
        if (!TextUtils.isEmpty(yingShouAmountString)) {
            dataSet05.add(yingShouAmountString);
        }
                String exemptAmountString = getExemptAmountString(context, paymentVos, isRefund);
        if (!TextUtils.isEmpty(exemptAmountString)) {
            dataSet05.add(exemptAmountString);
        }
        ColumnLayout cl05 = (ColumnLayout) view.findViewById(R.id.cl_05);
        cl05.setData(dataSet05);
        view.findViewById(R.id.v_line_05).setVisibility(Utils.isNotEmpty(dataSet05) ? View.VISIBLE : View.GONE);
                ColumnLayout cl06 = (ColumnLayout) view.findViewById(R.id.cl_06);
        List<String> dataSet06 = new ArrayList<String>();
        TradeDeposit tradeDeposit = tradeVo.getTradeDeposit();
        if (tradeDeposit != null && tradeDeposit.getDepositPay() != null) {
            if (tradeDeposit.getDepositRefund() != null) {
                dataSet06.add(context.getString(R.string.dinner_order_center_payment_refund,
                        Utils.formatPrice(tradeDeposit.getDepositRefund().abs().doubleValue())));
                BigDecimal bigDecimal = tradeDeposit.getDepositPay().add(tradeDeposit.getDepositRefund());
                if (bigDecimal.compareTo(BigDecimal.ZERO) != 0) {
                    dataSet06.add(context.getString(R.string.dinner_order_center_payment_refund_residue,
                            Utils.formatPrice(bigDecimal.doubleValue())));
                }
                TradeReasonRel operateReason = tradeVo.getOperateReason(OperateType.TRADE_DEPOSIT);
                if (operateReason != null && !TextUtils.isEmpty(operateReason.getReasonContent())) {
                    dataSet06.add(context.getString(R.string.dinner_order_center_payment_refund_reason,
                            operateReason.getReasonContent()));
                }
                cl06.setData(dataSet06);
                cl06.setVisibility(View.VISIBLE);
                view.findViewById(R.id.v_line_06).setVisibility(View.VISIBLE);
            }
        }
                List<PayModeDetailsBean> payModeList = getPayModeList(context, paymentVos, tradeVo);
        ColumnLayout cl01 = (ColumnLayout) view.findViewById(R.id.cl_01);
        cl01.setPresenter(this);
        cl01.setPaymentModes(tradeVo, payModeList, isRefundAmount(isRefund, tradeVo, paymentVos));
        view.findViewById(R.id.v_line_01).setVisibility(View.GONE);

        List<String> dataSet33 = new ArrayList<String>();
                String extraAmountString = getExtraAmountString(context, paymentVos, tradeVo);
        if (!TextUtils.isEmpty(extraAmountString)) {
            dataSet33.add(extraAmountString);
        }
                String changeAmountString = getChangeAmountString(context, paymentVos, tradeVo);
        if (!TextUtils.isEmpty(changeAmountString)) {
            dataSet33.add(changeAmountString);
        }
        ColumnLayout cl33 = (ColumnLayout) view.findViewById(R.id.cl_33);
        cl33.setData(dataSet33);
        view.findViewById(R.id.v_line_33).setVisibility(Utils.isNotEmpty(dataSet33) ? View.VISIBLE : View.GONE);
                TextView tvPayee = (TextView) view.findViewById(R.id.tv_payee);
        String payeeName = getPayee(paymentVos);
        if (!TextUtils.isEmpty(payeeName)) {
            tvPayee.setText(context.getString(R.string.dinner_order_center_payee, payeeName));
            tvPayee.setVisibility(View.VISIBLE);
        } else {
            tvPayee.setVisibility(View.INVISIBLE);
        }

        TextView tvActualAmount = (TextView) view.findViewById(R.id.tv_actual_amount);
        TextView tvActualName = (TextView) view.findViewById(R.id.tv_actual);
        BigDecimal actualAmount = getFaceOrUsefullAmount(paymentVos);
        if (isRefund) {
            tvActualName.setText(context.getString(R.string.dinner_real_refund));
            tvActualAmount.setText(Utils.formatPrice(actualAmount.doubleValue()));
        } else {
            tvActualName.setText(context.getString(R.string.dinner_real_pay));
            tvActualAmount.setText(Utils.formatPrice(actualAmount.doubleValue()));
        }
        LinearLayout ll_06 = (LinearLayout) view.findViewById(R.id.ll_06);
        if (paymentVos != null && paymentVos.size() > 0) {
            ll_06.setVisibility(View.VISIBLE);
        } else {
            ll_06.setVisibility(View.GONE);
        }

        return view;
    }

    public boolean isRefundAmount(boolean isRefund, TradeVo tradeVo, List<PaymentVo> paymentVos) {
        if (tradeVo.getTrade().getBusinessType() == BusinessType.BUFFET && tradeVo.getTrade().getTradeStatus() != TradeStatus.FINISH) {
            return false;
        }

        if (!isRefund) {
            BigDecimal tradeAmount = tradeVo.getTrade().getTradeAmount();
            BigDecimal actualAmount = getFaceOrUsefullAmount(paymentVos);
            BigDecimal exemptAmount = getExemptAmount(paymentVos);
                        BigDecimal extraAmount = getExtraAmount(paymentVos);
            return actualAmount.subtract(extraAmount).doubleValue() > tradeAmount.subtract(exemptAmount).doubleValue();
        }
        return false;
    }

        private String getGoodsAmountString(Context context, TradeVo tradeVo, boolean isRefund) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal exemptAmount = BigDecimal.ZERO;
        BigDecimal actualAmount = tradeVo.getTrade().getSaleAmount();
        String discountAmount =
                Utils.transferDot2(tradeVo.getTrade().getPrivilegeAmount().add(exemptAmount.negate()).toString());
        if (!TextUtils.isEmpty(discountAmount)) {
            List<TradePrivilege> tradePrivileges = tradeVo.getTradePrivileges();
                        if (tradePrivileges != null) {
                for (TradePrivilege tradePrivilege : tradeVo.getTradePrivileges()) {
                    if (tradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL) {
                        BigDecimal privilegeAmount = tradePrivilege.getPrivilegeAmount();
                        actualAmount = actualAmount.subtract(privilegeAmount);
                    }
                }
            }
            totalAmount = actualAmount;
            TradeDeposit tradeDeposit = tradeVo.getTradeDeposit();
            if (!isRefund && tradeDeposit != null && tradeDeposit.getDepositPay() != null) {
                totalAmount = totalAmount.subtract(tradeDeposit.getDepositPay());
            }
        }
        if (isRefund) {
            return context.getString(R.string.dinner_order_center_goods_amount, Utils.formatPrice(totalAmount.negate().doubleValue()));
        } else {
            return context.getString(R.string.dinner_order_center_goods_amount, Utils.formatPrice(totalAmount.doubleValue()));
        }

    }

        private String getEarnestAmountString(Context context, TradeVo tradeVo) {
        if (tradeVo != null && tradeVo.getTradeEarnestMoney() > 0) {
            return context.getString(R.string.dinner_order_center_earnest_amount, Utils.formatPrice(tradeVo.getTradeEarnestMoney()));
        }
        return "";
    }

        private String getExchargeAmountString(Context context, TradeVo tradeVo, boolean isRefund) {
        BigDecimal exemptAmount = BigDecimal.ZERO;
        List<TradePrivilege> tradePrivileges = tradeVo.getTradePrivileges();
        if (tradePrivileges == null) {
            return "";
        }
        for (TradePrivilege tradePrivilege : tradePrivileges) {
            if (tradePrivilege != null && tradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL) {
                exemptAmount = exemptAmount.add(tradePrivilege.getPrivilegeAmount());
            }
        }
        if (exemptAmount.compareTo(BigDecimal.ZERO) != 0) {
            if (isRefund) {
                return context.getString(R.string.dinner_order_center_excharge_amount, Utils.formatPrice(exemptAmount.negate().doubleValue()));
            } else {
                return context.getString(R.string.dinner_order_center_excharge_amount, Utils.formatPrice(exemptAmount.doubleValue()));
            }
        }
        return "";
    }

        private String getCarryRuleAmountString(Context context, TradeVo tradeVo) {
        BigDecimal before = tradeVo.getTrade().getTradeAmountBefore();
        BigDecimal after = tradeVo.getTrade().getTradeAmount();
        if (before == null) {            before = after;
            return "";
        }
        BigDecimal sub = after.subtract(before);
        if (sub.compareTo(new BigDecimal(0)) == 0) {
            return "";

        }
        return context.getResources().getString(R.string.dinner_billcenter_carry, Utils.formatPrice(sub.negate().doubleValue()));
    }


    private String getYingShouAmountString(Context context, List<PaymentVo> paymentVos, TradeVo tradeVo, boolean isRefund) {
        BigDecimal yingShouAmount = BigDecimal.ZERO;
        int name;
        if (isRefund) {
            yingShouAmount = tradeVo.getTrade().getTradeAmount().subtract(getExemptAmount(paymentVos));
            name = R.string.dinner_order_center_payinfo_refund;
        } else {
            yingShouAmount = tradeVo.getTrade().getTradeAmount();
            name = R.string.dinner_order_center_payinfo_receivable;
        }

        String yingShouAmountString = "";
        if (yingShouAmount.compareTo(BigDecimal.ZERO) != 0) {
            if (isRefund) {
                yingShouAmountString = context.getString(name, Utils.formatPrice(yingShouAmount.negate().doubleValue()));
            } else {
                yingShouAmountString = context.getString(name, Utils.formatPrice(yingShouAmount.doubleValue()));
            }
        }

        return yingShouAmountString;
    }


    private String getExemptAmountString(Context context, List<PaymentVo> paymentVos, boolean isRefund) {
        BigDecimal exemptAmount = getExemptAmount(paymentVos);
        String exemptAmountString = "";
        if (exemptAmount.compareTo(BigDecimal.ZERO) != 0) {
            if (isRefund) {
                exemptAmountString = context.getString(R.string.dinner_order_center_payinfo_exempt,
                        Utils.formatPrice(exemptAmount.negate().doubleValue()));
            } else {
                exemptAmountString = context.getString(R.string.dinner_order_center_payinfo_exempt,
                        Utils.formatPrice(exemptAmount.negate().doubleValue()));
            }
        }

        return exemptAmountString;
    }


    public BigDecimal getExemptAmount(List<PaymentVo> paymentVos) {
        BigDecimal exemptAmount = BigDecimal.ZERO;
        for (PaymentVo paymentVo : paymentVos) {
            if (paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_SELL
                    || paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_REFUND) {
                if (paymentVo.getPayment().getExemptAmount() != null) {
                    exemptAmount = exemptAmount.add(paymentVo.getPayment().getExemptAmount());
                }
            }
        }
        return exemptAmount;
    }

    static class PaymentItemWrapper {
        public PaymentType paymentType;
        public List<PaymentItem> paymentItemList;

        public PaymentItemWrapper(PaymentType paymentType) {
            this.paymentType = paymentType;
            this.paymentItemList = new ArrayList<>();
        }

        public void add(PaymentItem paymentItem) {
            paymentItemList.add(paymentItem);
        }
    }


    private List<PayModeDetailsBean> getPayModeList(Context context, List<PaymentVo> paymentVos, TradeVo tradeVo) {
        LongSparseArray<PaymentItemWrapper> paymentItemArray = new LongSparseArray<>();
        Map<Long, List<RefundExceptionReason>> allRefundExceptionReasonMap = new HashMap<>();
        if (Utils.isNotEmpty(paymentVos)) {
            for (PaymentVo paymentVo : paymentVos) {
                if (paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_SELL
                        || paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_REFUND) {
                                        Map<Long, List<RefundExceptionReason>> refundExceptionReasonMap = paymentVo.getRefundExceptionReasonMap();
                    if (refundExceptionReasonMap != null && !refundExceptionReasonMap.isEmpty()) {
                        allRefundExceptionReasonMap.putAll(refundExceptionReasonMap);
                    }

                    List<PaymentItem> paymentItems = paymentVo.getPaymentItemList();
                    if (Utils.isNotEmpty(paymentItems)) {
                        for (PaymentItem paymentItem : paymentItems) {
                            Long payModeId = paymentItem.getPayModeId();
                            PaymentItemWrapper tmpPaymentItems = paymentItemArray.get(payModeId);
                            if (tmpPaymentItems == null) {
                                tmpPaymentItems = new PaymentItemWrapper(paymentVo.getPayment().getPaymentType());
                                paymentItemArray.put(payModeId, tmpPaymentItems);
                            }
                            tmpPaymentItems.add(paymentItem);
                        }
                    }
                }
            }
        }

        List<PayModeDetailsBean> payModeDetailsBeanList = new ArrayList<PayModeDetailsBean>();
        if (paymentItemArray.size() > 0) {
            for (int i = 0; i < paymentItemArray.size(); i++) {
                PaymentItemWrapper paymentItemWrapper = paymentItemArray.valueAt(i);
                List<PaymentItem> paymentItemList = paymentItemWrapper.paymentItemList;
                BigDecimal augend = BigDecimal.ZERO;

                for (PaymentItem paymentItem : paymentItemList) {
                    BigDecimal payModeAmount = BigDecimal.ZERO;
                    List<PaymentItemGroupon> listDiscountTickets = new ArrayList<PaymentItemGroupon>();
                    String payModeName = paymentItem.getPayModeName();
                    if (tradeVo.getTrade().getTradeType() == TradeType.REFUND
                            || tradeVo.getTrade().getTradeType() == TradeType.REFUND_FOR_REPEAT) {
                        augend = paymentItem.getFaceAmount().subtract(paymentItem.getChangeAmount()).abs();
                    } else {
                        augend = paymentItem.getFaceAmount();
                    }
                    payModeAmount = payModeAmount.add(augend);

                    PaymentItemGroupon tmpPaymentItemGroupon = paymentItem.getPaymentItemGroupon();
                    if (PayModeId.MEITUAN_TUANGOU.value().equals(paymentItem.getPayModeId())
                            && tmpPaymentItemGroupon != null) {
                        if (tmpPaymentItemGroupon.getUsedSerialNo() != null) {
                            String[] ticketNos = tmpPaymentItemGroupon.getUsedSerialNo().split(",");
                            for (String ticketNo : ticketNos) {
                                PaymentItemGroupon paymentItemGroupon = new PaymentItemGroupon();
                                paymentItemGroupon.setUsedSerialNo(ticketNo);
                                paymentItemGroupon.setDealTitle(
                                        tmpPaymentItemGroupon.getDealTitle());
                                paymentItemGroupon.setMarketPrice(
                                        tmpPaymentItemGroupon.getMarketPrice());
                                listDiscountTickets.add(paymentItemGroupon);
                            }
                        }
                    }

                    if (TextUtils.isEmpty(payModeName)) {
                        payModeName = getPayModeName(paymentItemArray.keyAt(i));
                    }

                    TradePayStatus paymentItemTradePayStatus = paymentItem.getPayStatus();

                    if (paymentItemTradePayStatus != TradePayStatus.WAITING_REFUND) {
                        PayModeDetailsBean payModeDetailsBean = new PayModeDetailsBean(paymentItemWrapper.paymentType,
                                payModeName, payModeAmount, listDiscountTickets, paymentItem);
                        payModeDetailsBean.setRefundExceptionReasonList(allRefundExceptionReasonMap.get(paymentItem.getId()));
                        payModeDetailsBeanList.add(payModeDetailsBean);
                    }
                }
            }
        }

        return payModeDetailsBeanList;
    }

    private String getPayModeName(long payModeId) {
        return PaySettingCache.getPayModeNameByModeId(payModeId);
    }

    private BigDecimal getFaceOrUsefullAmount(List<PaymentVo> paymentVos) {
        BigDecimal faceAmount = BigDecimal.ZERO;
        for (PaymentVo paymentVo : paymentVos) {
            if (paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_SELL
                    || paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_REFUND) {
                List<PaymentItem> paymentItems = paymentVo.getPaymentItemList();
                for (PaymentItem paymentItem : paymentItems) {
                    if (paymentItem.getPayStatus() == TradePayStatus.PAID || paymentItem.getPayStatus() == TradePayStatus.REFUNDED
                            || paymentItem.getPayStatus() == TradePayStatus.REPEAT_PAID) {
                        if (PayModeId.EARNEST_DEDUCT.value().equals(paymentItem.getPayModeId()))
                            continue;
                        faceAmount = faceAmount.add(paymentItem.getFaceAmount().subtract(paymentItem.getChangeAmount()));
                    }
                }
            }
        }

        return faceAmount;
    }


    private String getExtraAmountString(Context context, List<PaymentVo> paymentVos, TradeVo tradeVo) {
        BigDecimal extraAmount = getExtraAmount(paymentVos);

        String extraAmountString = "";
        if (tradeVo.getTrade().getTradeType() != TradeType.REFUND
                && tradeVo.getTrade().getTradeType() != TradeType.REFUND_FOR_REPEAT
                && extraAmount.compareTo(BigDecimal.ZERO) > 0) {
            extraAmountString = context.getString(R.string.dinner_order_center_payinfo_extra,
                    Utils.formatPrice(extraAmount.doubleValue()));
        }
        return extraAmountString;
    }


    private BigDecimal getExtraAmount(List<PaymentVo> paymentVos) {
        BigDecimal extraAmount = BigDecimal.ZERO;
        for (PaymentVo paymentVo : paymentVos) {
            if (paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_SELL
                    || paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_REFUND) {
                List<PaymentItem> paymentItems = paymentVo.getPaymentItemList();
                for (PaymentItem paymentItem : paymentItems) {
                    if (paymentItem.getPayStatus() == TradePayStatus.PAID || paymentItem.getPayStatus() == TradePayStatus.REFUNDED) {
                        BigDecimal augend = paymentItem.getFaceAmount().subtract(paymentItem.getUsefulAmount());
                        if (augend.doubleValue() > 0) {
                            augend = augend.subtract(paymentItem.getChangeAmount());
                            extraAmount = extraAmount.add(augend);
                        }
                    }
                }
            }
        }
        return extraAmount;
    }

        private String getDepositAmountString(Context context, TradeVo tradeVo) {
        TradeDeposit tradeDeposit = tradeVo.getTradeDeposit();
        String str = "";
        if (tradeDeposit != null && tradeDeposit.getDepositPay() != null) {
            str = context.getString(R.string.dinner_order_center_payinfo_deposit,
                    Utils.formatPrice(tradeDeposit.getDepositPay().doubleValue()));
        }

        if (tradeVo.getTradeDepositPaymentItem() != null)
            str += "  " + tradeVo.getTradeDepositPaymentItem().getPayModeName() + mView.getViewActivity().getResources().getString(R.string.order_center_pay);

        return str;
    }


    private String getChangeAmountString(Context context, List<PaymentVo> paymentVos, TradeVo tradeVo) {
        String changeAmountString = "";
        BigDecimal changeAmount = getChangeAmount(paymentVos, tradeVo);
        if (changeAmount.compareTo(BigDecimal.ZERO) != 0) {
            changeAmountString = context.getString(R.string.dinner_order_center_payinfo_change,
                    Utils.formatPrice(changeAmount.doubleValue()));
        }

        return changeAmountString;
    }


    private BigDecimal getChangeAmount(List<PaymentVo> paymentVos, TradeVo tradeVo) {
        BigDecimal changeAmount = BigDecimal.ZERO;
        TradeType tradeType = tradeVo.getTrade().getTradeType();
        if (tradeType != TradeType.REFUND && tradeType != TradeType.REFUND_FOR_REPEAT) {
            for (PaymentVo paymentVo : paymentVos) {
                if (paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_SELL
                        || paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_REFUND) {
                    List<PaymentItem> paymentItems = paymentVo.getPaymentItemList();
                    for (PaymentItem paymentItem : paymentItems) {
                        if (paymentItem.getPayStatus() == TradePayStatus.PAID) {
                            changeAmount = changeAmount.add(paymentItem.getChangeAmount());
                        }
                    }
                }
            }
        }
        return changeAmount;
    }


    private String getPayee(List<PaymentVo> paymentVos) {
        if (paymentVos != null && !paymentVos.isEmpty()) {
            PaymentVo paymentVo = paymentVos.get(0);
            if (paymentVo != null && paymentVo.getPayment() != null && paymentVo.getPayment().getCreatorName() != null) {
                return paymentVo.getPayment().getCreatorName().trim();
            }
            return "";
        }

        return "";
    }


    private BigDecimal getShiShouAmount(TradeVo tradeVo, List<PaymentVo> paymentVos, boolean isRefund) {
        TradePayStatus tradePayStatus = tradeVo.getTrade().getTradePayStatus();
        BigDecimal shiShouAmount = BigDecimal.ZERO;
        for (PaymentVo paymentVo : paymentVos) {
            if (paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_SELL
                    || paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_REFUND) {
                if (isRefund) {
                    if (paymentVo.getPaymentItemList() != null && paymentVo.getPaymentItemList().size() > 0) {
                        shiShouAmount = shiShouAmount.add(paymentVo.getPayment().getShopActualAmount().negate());
                    }
                } else {
                    if (tradePayStatus == TradePayStatus.PAYING
                            || paymentVo.getPayment().getIsPaid() == Bool.YES) {
                        shiShouAmount =
                                shiShouAmount.add(paymentVo.getPayment().getShopActualAmount());
                    }

                }
            }
        }
        return shiShouAmount;
    }

    protected BigDecimal getShiShouAmount(TradeVo tradeVo) {
        List<PaymentVo> paymentVoList = mLoadResult.getPaymentVoList();
        List<PaymentVo> paymentTempVoList = new ArrayList<PaymentVo>();
        if (Utils.isNotEmpty(paymentVoList)) {
            for (PaymentVo paymentVo : paymentVoList) {
                if (PaymentType.ADJUST != paymentVo.getPayment().getPaymentType()) {
                    paymentTempVoList.add(paymentVo);
                }
            }
        }

        return getShiShouAmount(tradeVo, paymentTempVoList, false);
    }

    @Override
    public void doPrint(String tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void doRefund() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void doRecision() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void doPay() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void doAccept() {
        final TradePaymentVo tradePaymentVo = getTradePaymentVo().clone();
        VerifyHelper.verifyAlert(mView.getViewActivity(), getAcceptPermissionCode(),
                new VerifyHelper.Callback() {
                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                        performAcceptOrder(tradePaymentVo);
                    }
                });
    }

        protected abstract String getAcceptPermissionCode();

        protected abstract void performAcceptOrder(TradePaymentVo tradePaymentVo);

    @Override
    public void doRefuse() {
        throw new UnsupportedOperationException();
    }

    protected TradePaymentVo getTradePaymentVo() {
        if (mLoadResult != null) {
            return mLoadResult.getTradePaymentVo();
        }

        return null;
    }

    protected TradeVo getTradeVo() {
        if (mLoadResult != null && mLoadResult.getTradePaymentVo() != null) {
            return mLoadResult.getTradePaymentVo().getTradeVo();
        }

        return null;
    }

    protected List<DeliveryOrderVo> getDeliveryOrderVos() {
        if (mLoadResult != null && mLoadResult.getTradePaymentVo() != null) {
            return mLoadResult.getTradePaymentVo().getDeliveryOrderVoList();
        }

        return null;
    }

    protected ElectronicInvoiceVo getElectronicInvoiceVo() {
        if (mLoadResult != null) {
            return mLoadResult.getElectronicInvoiceVo();
        }

        return null;
    }

    protected Invoice getInvoice() {
        if (mLoadResult != null) {
            return mLoadResult.getInvoice();
        }

        return null;
    }

    private void setInvoice(Invoice invoice) {
        if (mLoadResult != null) {
            mLoadResult.setInvoice(invoice);
        }
    }

    protected List<PaymentVo> getPaymentVoList() {
        if (mLoadResult != null && mLoadResult.getTradePaymentVo() != null) {
            return mLoadResult.getTradePaymentVo().getPaymentVoList();
        }

        return null;
    }

    protected TradeReturnInfo getTradeReturnInfo() {
        if (mLoadResult != null) {
            return mLoadResult.getTradeReturnInfo();
        }

        return null;
    }

    @Override
    public boolean showAccept() {
        return false;
    }

    @Override
    public boolean showRefuse() {
        return false;
    }

    @Override
    public boolean showCall() {
        if (isSquareAccountMode || isBindDeliveryUserMode) {
            return false;
        }

                        return isUnprocessedNewOrder();
    }

    @Override
    public boolean showPay() {
        return false;
    }

    @Override
    public boolean showRecision() {
        return false;
    }

    @Override
    public boolean showRefund() {
        return false;
    }

    @Override
    public boolean showPrint() {
        if (isSquareAccountMode || isBindDeliveryUserMode || isUnionSubOrderType()) {
            return false;
        }

                                                return !isUnprocessedNewOrder()
                && !isSalesPaying()
                && !isUnprocessedInvalid();
    }


    private boolean isUnionSubOrderType() {
        Trade trade = getTrade();
        return trade != null && trade.getTradeType() == TradeType.UNOIN_TABLE_SUB;
    }

        private boolean isUnprocessedNewOrder() {
        Trade trade = getTrade();
        if (trade != null) {
            TradeStatus tradeStatus = trade.getTradeStatus();
            TradePayStatus tradePayStatus = trade.getTradePayStatus();
            TradePayForm tradePayForm = trade.getTradePayForm();
            return TradeStatus.UNPROCESSED == tradeStatus
                    && (TradePayStatus.PAID == tradePayStatus || TradePayForm.OFFLINE == tradePayForm);
        }

        return false;
    }

        private boolean isSalesPaying() {
        Trade trade = getTrade();
        if (trade != null) {
            TradeStatus tradeStatus = trade.getTradeStatus();
            TradePayStatus tradePayStatus = trade.getTradePayStatus();
            return TradePayStatus.PAYING == tradePayStatus
                    && (TradeStatus.CONFIRMED == tradeStatus || TradeStatus.CREDIT == tradeStatus);
        }

        return false;
    }

        private boolean isUnprocessedInvalid() {
        Trade trade = getTrade();
        if (trade != null) {
            SourceId sourceId = trade.getSource();
            TradeStatus tradeStatus = trade.getTradeStatus();
            TradePayStatus tradePayStatus = trade.getTradePayStatus();
            TradePayForm tradePayForm = trade.getTradePayForm();
            return SourceId.POS != sourceId
                    && (TradeStatus.REFUSED == tradeStatus || (TradeStatus.CANCELLED == tradeStatus && (TradePayForm.OFFLINE == tradePayForm || TradePayStatus.REFUNDED == tradePayStatus || TradePayStatus.PAID == tradePayStatus)));
        }

        return false;
    }

    @Override
    public boolean showRetryRefund() {
        if (isSquareAccountMode || isBindDeliveryUserMode) {
            return false;
        }

        Trade trade = getTrade();
        if (trade != null) {
            TradePayStatus tradePayStatus = trade.getTradePayStatus();
            return tradePayStatus == TradePayStatus.WAITING_REFUND
                    || tradePayStatus == TradePayStatus.REFUNDING
                    || tradePayStatus == TradePayStatus.REFUND_FAILED;
        }
        return false;
    }

    @Override
    public boolean showRepay() {
        return false;
    }

    @Override
    public boolean showContinueRepay() {
        return false;
    }

    @Override
    public boolean showContinuePay() {
        Trade trade = getTrade();
        if (trade != null) {
            TradePayStatus tradePayStatus = trade.getTradePayStatus();
            if (tradePayStatus == TradePayStatus.PAYING) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean showSendOrder() {
        return false;
    }

    @Override
    public boolean showCancelOrder() {
        return false;
    }

    @Override
    public boolean showAcceptReturn() {
        return false;
    }

    @Override
    public boolean showRefuseReturn() {
        return false;
    }

    @Override
    public boolean showDepositRefund() {
        return false;
    }

    @Override
    public boolean showInvoice() {
        return false;
    }

    @Override
    public boolean showInvoiceRevoke() {
        return false;
    }

    @Override
    public boolean showRebindDeliveryUser() {
        return false;
    }

    protected Trade getTrade() {
        TradeVo tradeVo = getTradeVo();
        if (tradeVo != null) {
            return tradeVo.getTrade();
        }

        return null;
    }

    protected TradeExtra getTradeExtra() {
        TradeVo tradeVo = getTradeVo();
        if (tradeVo != null) {
            return tradeVo.getTradeExtra();
        }

        return null;
    }

    @Override
    public TradeCustomer getTradeCustomer() {
        TradeVo tradeVo = getTradeVo();
        List<TradeCustomer> tradeCustomers = tradeVo.getTradeCustomerList();
        if(Utils.isEmpty(tradeCustomers)){
            return null;
        }

        for (TradeCustomer tradeCustomer : tradeCustomers) {
            if(ValueEnums.equalsValue(tradeCustomer.getCustomerType(),CustomerType.MEMBER.value())){
                return tradeCustomer;
            }
        }

        return null;
    }

    protected DeliveryOrder getDeliveryOrder() {
        List<DeliveryOrderVo> deliveryOrderVos = getDeliveryOrderVos();
        if (Utils.isNotEmpty(deliveryOrderVos)) {
            for (DeliveryOrderVo deliveryOrderVo : deliveryOrderVos) {
                DeliveryOrder deliveryOrder = deliveryOrderVo.getDeliveryOrder();
                if (deliveryOrder.getEnableFlag() == YesOrNo.YES) {
                    return deliveryOrder;
                }
            }
        }

        return null;
    }

    protected String getUuid() {
        return uuid;
    }


    public boolean isPayInCash(List<PaymentItem> paymentItems) {
        if (Utils.isNotEmpty(paymentItems)) {
            for (PaymentItem paymentItem : paymentItems) {
                if (paymentItem.getPayStatus() == TradePayStatus.PAID && PayModeId.CASH.equalsValue(paymentItem.getPayModeId())) {
                    return true;
                }
            }
        }

        return false;
    }



    public boolean isRefundingInThird(PaymentItem paymentItem) {
        if (paymentItem != null) {
            if (paymentItem.getPayStatus() == TradePayStatus.REFUNDING && PayModeId.WEIXIN_PAY.equalsValue(paymentItem.getPayModeId()) || PayModeId.ALIPAY.equalsValue(paymentItem.getPayModeId())) {
                return true;
            }
        }

        return false;
    }


    public boolean isPaid() {
        Trade trade = getTrade();
        if (trade != null) {
            TradeStatus tradeStatus = trade.getTradeStatus();
            TradeType tradeType = trade.getTradeType();
            TradePayStatus tradePayStatus = trade.getTradePayStatus();
            return (tradeType == TradeType.SELL || tradeType == TradeType.SELL_FOR_REPEAT || tradeType == TradeType.SPLIT)
                    && (tradePayStatus == TradePayStatus.PAID)
                    && (tradeStatus == TradeStatus.CONFIRMED || tradeStatus == TradeStatus.FINISH || tradeStatus == TradeStatus.CREDIT || tradeStatus == TradeStatus.WRITEOFF || tradeStatus == TradeStatus.SQUAREUP);
        }

        return false;
    }



    public boolean isUnPaidOrPaid() {
        Trade trade = getTrade();
        if (trade != null) {
            TradeStatus tradeStatus = trade.getTradeStatus();
            TradeType tradeType = trade.getTradeType();
            TradePayStatus tradePayStatus = trade.getTradePayStatus();
            return (tradeType == TradeType.SELL || tradeType == TradeType.SELL_FOR_REPEAT || tradeType == TradeType.SPLIT)
                    && (tradePayStatus == TradePayStatus.PAID || tradePayStatus == TradePayStatus.UNPAID)
                    && (tradeStatus == TradeStatus.CONFIRMED || tradeStatus == TradeStatus.FINISH || tradeStatus == TradeStatus.CREDIT || tradeStatus == TradeStatus.WRITEOFF || tradeStatus == TradeStatus.SQUAREUP);
        }
        return false;
    }

    @Override
    public String getDeliveryPlatformName(DeliveryPlatform deliveryPlatform) {
        if (mLoadResult != null && mLoadResult.getDeliverylatformPartnerShopBizSparseArray() != null && deliveryPlatform != null) {
            return mLoadResult.getDeliverylatformPartnerShopBizSparseArray().get(deliveryPlatform.value());
        }

        return null;
    }

    @Override
    public Map<Integer, PartnerShopBiz> getDeliveryPlatformPartnerShopBizMap() {
        if (mLoadResult != null && mLoadResult.getmDeliverylatformPartnerShopBizMap() != null) {
            return mLoadResult.getmDeliverylatformPartnerShopBizMap();
        }

        return null;
    }

    @Override
    public void showRefundDialog(Long tradeId, Long paymentItemId, BigDecimal usefulAmount) {
        if (mView != null) {
            mView.showRefundDialog(tradeId, paymentItemId, usefulAmount);
        }
    }

    @Override
    public List<TradePrivilege> getOrderPrivilegeList(TradeVo tradeVo, PrivilegeType privilegeType) {
        List<TradePrivilege> tradePrivileges = tradeVo.getTradePrivileges();
        if (tradePrivileges == null) {
            return Collections.emptyList();
        }
        List<TradePrivilege> choosetradePrivileges = new ArrayList<TradePrivilege>();
        for (TradePrivilege tradePrivilege : tradePrivileges) {

            if (tradePrivilege != null && tradePrivilege.getPrivilegeType() == privilegeType) {
                choosetradePrivileges.add(tradePrivilege);
            }
        }

        return choosetradePrivileges;
    }

    @Override
    public String getTradeSource(Context context, Trade trade) {
        if (trade == null || trade.getSourceChild() == null
                || trade.getSourceChild().value() == null) {
            return null;
        }

        String sourceStr;
        if (trade.getTradeType() == TradeType.SPLIT) {
            sourceStr = context.getResources().getString(R.string.dinner_order_center_tables_split_name);
        } else {
            sourceStr = ServerSettingManager.getOrderSourceName(trade.getSourceChild().value());
        }

        return sourceStr;
    }

    @Override
    public String getTradeAcceptSource(TradeReceiveLog tradeReceiveLog) {
        if (tradeReceiveLog != null) {
            return ServerSettingManager.getOrderSourceName(tradeReceiveLog.getOpSourceId());
        }
        return null;
    }

    @Override
    public String getSplideOrigonTradeNumber(Trade trade) {
        if (trade != null && trade.getTradeType() == TradeType.SPLIT) {
            if (!TextUtils.isEmpty(trade.getRelateTradeUuid())) {
                try {
                    TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
                    TradeVo tradeVo = tradeDal.findTrade(trade.getRelateTradeUuid(), false);
                    if (tradeVo.getTrade() != null) {
                        return tradeVo.getTrade().getTradeNo();
                    }

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }

        return null;
    }

    @SuppressLint("InflateParams")
    @Override
    public View createOrderCenterPayInfoItem(LayoutInflater layoutInflater, Context context, TradeVo tradeVo,
                                             List<PaymentVo> paymentVos, boolean isRefund) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_center_detail_payinfo_item, null);

        ColumnLayout cl00 = (ColumnLayout) view.findViewById(R.id.cl_00);
        List<String> dataSet0 = new ArrayList<String>();
                String goodsAmountString = getGoodsAmountString(context, tradeVo, isRefund);
        if (!TextUtils.isEmpty(goodsAmountString)) {
            dataSet0.add(goodsAmountString);
        }
                String exchargeAmountString = getExchargeAmountString(context, tradeVo, isRefund);
        if (!TextUtils.isEmpty(exchargeAmountString)) {
            dataSet0.add(exchargeAmountString);
        }
                BigDecimal totalPrivilegeAmount;
        if (isRefund) {
            totalPrivilegeAmount = getTotalPrivilegeAmount(tradeVo).negate();
        } else {
            totalPrivilegeAmount = getTotalPrivilegeAmount(tradeVo);
        }
        if (totalPrivilegeAmount.compareTo(BigDecimal.ZERO) != 0) {
                        if (Utils.isEmpty(dataSet0)) {
                dataSet0.add("");
            }
            dataSet0.add(context.getString(R.string.dinner_order_center_total_privilege,
                    Utils.formatPrice(totalPrivilegeAmount.doubleValue())));
        }
                String carryRuleString = getCarryRuleAmountString(context, tradeVo);
        if (!TextUtils.isEmpty(carryRuleString)) {
            dataSet0.add(carryRuleString);
        }
        cl00.setData(dataSet0);
        view.findViewById(R.id.v_line_00).setVisibility(Utils.isNotEmpty(dataSet0) ? View.VISIBLE : View.GONE);
                ColumnLayout cl10 = (ColumnLayout) view.findViewById(R.id.cl_10);
        List<String> dataSet10 = new ArrayList<String>();
        String depositAmountString = getDepositAmountString(context, tradeVo);
        if (!TextUtils.isEmpty(depositAmountString)) {
            cl10.setVisibility(View.VISIBLE);
            view.findViewById(R.id.v_line_11).setVisibility(View.VISIBLE);
            dataSet10.add(depositAmountString);
            cl10.setData(dataSet10);
        }
        List<String> dataSet05 = new ArrayList<String>();
                String yingShouAmountString = getYingShouAmountString(context, paymentVos, tradeVo, isRefund);
        if (!TextUtils.isEmpty(yingShouAmountString)) {
            dataSet05.add(yingShouAmountString);
        }
                String exemptAmountString = getExemptAmountString(context, paymentVos, isRefund);
        if (!TextUtils.isEmpty(exemptAmountString) && tradeVo.getTrade().getTradePayStatus() != TradePayStatus.UNPAID) {
            dataSet05.add(exemptAmountString);
        }
        ColumnLayout cl05 = (ColumnLayout) view.findViewById(R.id.cl_05);
        cl05.setData(dataSet05);
        view.findViewById(R.id.v_line_05).setVisibility(Utils.isNotEmpty(dataSet05) ? View.VISIBLE : View.GONE);
                ColumnLayout cl06 = (ColumnLayout) view.findViewById(R.id.cl_06);
        List<String> dataSet06 = new ArrayList<String>();
        TradeDeposit tradeDeposit = tradeVo.getTradeDeposit();
        if (tradeDeposit != null && tradeDeposit.getDepositPay() != null) {
            if (tradeDeposit.getDepositRefund() != null) {
                dataSet06.add(context.getString(R.string.dinner_order_center_payment_refund,
                        Utils.formatPrice(tradeDeposit.getDepositRefund().abs().doubleValue())));
                BigDecimal bigDecimal = tradeDeposit.getDepositPay().add(tradeDeposit.getDepositRefund());
                if (bigDecimal.compareTo(BigDecimal.ZERO) != 0) {
                    dataSet06.add(context.getString(R.string.dinner_order_center_payment_refund_residue,
                            Utils.formatPrice(bigDecimal.doubleValue())));
                }
                TradeReasonRel operateReason = tradeVo.getOperateReason(OperateType.TRADE_DEPOSIT);
                if (operateReason != null && !TextUtils.isEmpty(operateReason.getReasonContent())) {
                    dataSet06.add(context.getString(R.string.dinner_order_center_payment_refund_reason,
                            operateReason.getReasonContent()));
                }
                cl06.setData(dataSet06);
                cl06.setVisibility(View.VISIBLE);
                view.findViewById(R.id.v_line_06).setVisibility(View.VISIBLE);
            }
        }
                List<PayModeDetailsBean> payModeList = getPayModeList(context, paymentVos, tradeVo);
        ColumnLayout cl01 = (ColumnLayout) view.findViewById(R.id.cl_01);
        cl01.setPaymentModes(tradeVo, payModeList);
        view.findViewById(R.id.v_line_01).setVisibility(View.GONE);

        List<String> dataSet33 = new ArrayList<String>();
                String extraAmountString = getExtraAmountString(context, paymentVos, tradeVo);
        if (!TextUtils.isEmpty(extraAmountString)) {
            dataSet33.add(extraAmountString);
        }
                String changeAmountString = getChangeAmountString(context, paymentVos, tradeVo);
        if (!TextUtils.isEmpty(changeAmountString)) {
            dataSet33.add(changeAmountString);
        }
        ColumnLayout cl33 = (ColumnLayout) view.findViewById(R.id.cl_33);
        cl33.setData(dataSet33);
        view.findViewById(R.id.v_line_33).setVisibility(Utils.isNotEmpty(dataSet33) ? View.VISIBLE : View.GONE);
                TextView tvPayee = (TextView) view.findViewById(R.id.tv_payee);
        String payeeName = getPayee(paymentVos);
        if (!TextUtils.isEmpty(payeeName)) {
            tvPayee.setText(context.getString(R.string.dinner_order_center_payee, payeeName));
            tvPayee.setVisibility(View.VISIBLE);
        } else {
            tvPayee.setVisibility(View.INVISIBLE);
        }

        TextView tvActualAmount = (TextView) view.findViewById(R.id.tv_actual_amount);
        TextView tvActualName = (TextView) view.findViewById(R.id.tv_actual);

        BigDecimal actualAmount;
        actualAmount = getShiShouAmount(paymentVos, isRefund);
        if (isRefund) {
            tvActualName.setText(context.getString(R.string.dinner_real_refund));
            tvActualAmount.setText(Utils.formatPrice(actualAmount.doubleValue()));
        } else {
            tvActualName.setText(context.getString(R.string.dinner_real_pay));
            tvActualAmount.setText(Utils.formatPrice(actualAmount.doubleValue()));
        }

        LinearLayout ll_06 = (LinearLayout) view.findViewById(R.id.ll_06);
        if (TextUtils.isEmpty(payeeName) && actualAmount.compareTo(BigDecimal.ZERO) == 0) {
            ll_06.setVisibility(View.GONE);
        } else {
            ll_06.setVisibility(View.VISIBLE);
        }

        return view;
    }


    private BigDecimal getShiShouAmount(List<PaymentVo> paymentVos, boolean isRefund) {
        BigDecimal shiShouAmount = BigDecimal.ZERO;
        for (PaymentVo paymentVo : paymentVos) {
            if (paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_SELL
                    || paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_REFUND) {
                if (isRefund) {
                    if (paymentVo.getPaymentItemList() != null && paymentVo.getPaymentItemList().size() > 0) {
                        shiShouAmount = shiShouAmount.add(paymentVo.getPayment().getShopActualAmount().negate());
                    }
                } else {
                    if (paymentVo.getPayment().getIsPaid() == Bool.YES) {
                        shiShouAmount = shiShouAmount.add(paymentVo.getPayment().getShopActualAmount());
                    }

                }
            }
        }
        return shiShouAmount;
    }

        protected BigDecimal getShiFuAmount(List<PaymentVo> paymentVos) {
        BigDecimal shiFuAmount = BigDecimal.ZERO;
        if (Utils.isNotEmpty(paymentVos)) {
            for (PaymentVo paymentVo : paymentVos) {
                if (paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_SELL) {
                    List<PaymentItem> paymentItems = paymentVo.getPaymentItemList();
                    if (Utils.isNotEmpty(paymentItems)) {
                        for (PaymentItem paymentItem : paymentItems) {
                            if (paymentItem.getPayStatus() == TradePayStatus.PAID) {
                                BigDecimal tempAmount = paymentItem.getFaceAmount().subtract(paymentItem.getChangeAmount());
                                shiFuAmount = shiFuAmount.add(tempAmount);
                            }
                        }
                    }
                }
            }
        }

        return shiFuAmount;
    }

    @Override
    public List<TradeItemVo> getInvalidTradeItemList(List<TradeItemVo> tradeItemList, InvalidType invalidType) {
        if (Utils.isNotEmpty(tradeItemList)) {
            List<TradeItemVo> invalidTradeItemList = new ArrayList<TradeItemVo>();
            for (TradeItemVo tradeItemVo : tradeItemList) {
                TradeItem tradeItem = tradeItemVo.getTradeItem();
                if (tradeItem.getStatusFlag() == StatusFlag.INVALID && tradeItem.getInvalidType() == invalidType
                        && tradeItem.getQuantity().compareTo(BigDecimal.ZERO) != 0) {
                    invalidTradeItemList.add(tradeItemVo);
                }
            }

            return invalidTradeItemList;
        }

        return Collections.emptyList();
    }

    @Override
    public BigDecimal getShiShouAmountString(TradeVo tradeVo, List<PaymentVo> paymentVos, Boolean isRefund) {
        TradePayStatus tradePayStatus = tradeVo.getTrade().getTradePayStatus();
        BigDecimal shiShouAmount = BigDecimal.ZERO;
        for (PaymentVo paymentVo : paymentVos) {
            if (paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_SELL
                    || paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_REFUND) {
                if (isRefund) {
                    if (paymentVo.getPaymentItemList() != null && paymentVo.getPaymentItemList().size() > 0) {
                        shiShouAmount = shiShouAmount.add(paymentVo.getPayment().getShopActualAmount().negate());
                    }
                } else {
                    if (tradePayStatus == TradePayStatus.PAYING
                            || paymentVo.getPayment().getIsPaid() == Bool.YES) {
                        shiShouAmount =
                                shiShouAmount.add(paymentVo.getPayment().getShopActualAmount());
                    }

                }
            }
        }
        return shiShouAmount;
    }

    @Override
    public boolean showTakeDish() {
        return false;
    }

    @Override
    public void doTakeDish() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void passiveAddTip() {
        throw new UnsupportedOperationException();
    }


    private class OrderCenterChangeObserver implements DatabaseHelper.DataChangeObserver {

        @Override
        public void onChange(Collection<Uri> uris) {
            if (mView != null && mView.isAdd()) {
                onDataChanged(uris);
            }
        }

    }

    protected void onDataChanged(Collection<Uri> uris) {
        if (uris.contains(URI_INVOICE)) {
            Trade trade = getTrade();
            if (trade != null && trade.getId() != null) {
                Invoice invoice = mManager.findInvoice(trade.getId());
                setInvoice(invoice);

                mView.getViewActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.showHandlerButton();
                    }
                });
            }
        }
    }

    @Override
    public void flushAuthLog(OrderActionEnum orderAction, Long tradeId, String tradeUuid, Long updateTime) {
        AuthLogManager.getInstance().flush(orderAction, tradeId, tradeUuid, updateTime);
    }

    @Override
    public void clearAuthLog() {
        AuthLogManager.getInstance().clear();
    }

    @Override
    public void getTaxNoByTradeId(Long tradeId) {        TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);
        GetTaxNoReq req = new GetTaxNoReq();
        req.tradeId = tradeId;
        ResponseListener getTaxNoListenner = new ResponseListener<GetTaxNoResp>() {
            @Override
            public void onResponse(ResponseObject<GetTaxNoResp> response) {

            }

            @Override
            public void onError(VolleyError error) {

            }
        };
        tradeOperates.queryInvoiceNo(req, getTaxNoListenner);
    }

    @Override
    public String getDisplayExpectTime() {
        Trade trade = getTrade();
        TradeExtra tradeExtra = getTradeExtra();
        if (trade != null && tradeExtra != null) {
            DeliveryType deliveryType = trade.getDeliveryType();
            Long expectTime = tradeExtra.getExpectTime();
            if (DeliveryType.SEND == deliveryType) {
                if (expectTime != null) {
                    return mView.getViewActivity().getString(R.string.order_send_delivery_time,
                            DateTimeUtils.getDisplayTime(expectTime));
                } else {
                    return mView.getViewActivity().getString(R.string.order_send_delivery_time,
                            mView.getViewActivity().getString(R.string.order_delivery_time_now));
                }
            } else if (DeliveryType.TAKE == deliveryType) {
                if (expectTime != null) {
                    return mView.getViewActivity().getString(R.string.order_take_time,
                            DateTimeUtils.getDisplayTime(expectTime));
                } else {
                    return mView.getViewActivity().getString(R.string.take_goods_as_fast);
                }
            }

        }

        return "";
    }

    @Override
    public String getDisplayTakeNumber() {
        return "";
    }

    @Override
    public boolean showCreateDoc() {
        Trade trade = getTrade();
        TradeCustomer tradeCustomer= getTradeCustomer();
        return ValueEnums.equalsValue(trade.getTradeStatus(),TradeStatus.FINISH.value()) && tradeCustomer!=null;
    }

    @Override
    public boolean showCreateTask() {
                Trade trade = getTrade();
        return ValueEnums.equalsValue(trade.getTradeStatus(),TradeStatus.FINISH.value());
    }
}
