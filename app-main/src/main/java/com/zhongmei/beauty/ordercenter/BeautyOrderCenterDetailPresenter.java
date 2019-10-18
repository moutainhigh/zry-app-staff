package com.zhongmei.beauty.ordercenter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.zhongmei.ZMIntent;
import com.zhongmei.beauty.BeautyOrderActivity;
import com.zhongmei.beauty.utils.BeautyOrderConstants;
import com.zhongmei.bty.basemodule.async.operates.AsyncDal;
import com.zhongmei.bty.basemodule.auth.application.BeautyApplication;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.basemodule.devices.liandipos.NewLiandiposManager;
import com.zhongmei.bty.basemodule.devices.mispos.manager.DinnerPosManager;
import com.zhongmei.bty.basemodule.pay.bean.PaymentItemUnionpayVo;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.pay.message.PaymentResp;
import com.zhongmei.bty.basemodule.pay.operates.PaymentItemDal;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.shopmanager.handover.manager.ServerSettingManager;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableState;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeUnionType;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.Invoice;
import com.zhongmei.bty.basemodule.trade.enums.InvoiceStatus;
import com.zhongmei.bty.basemodule.trade.message.TradePaymentResp;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.operates.TablesDal;
import com.zhongmei.bty.cashier.ordercenter.presenter.OrderCenterDetailPresenter;
import com.zhongmei.bty.cashier.ordercenter.view.IOrderCenterDetailView;
import com.zhongmei.bty.cashier.util.TradeSourceUtils;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.dinner.ordercenter.view.DinnerChooseTableDialog;
import com.zhongmei.bty.dinner.ordercenter.view.DinnerChooseTableDialog_;
import com.zhongmei.bty.entity.enums.InventoryShowType;
import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.bty.pay.manager.DoPayManager;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.DeliveryPlatform;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.util.ToastUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BeautyOrderCenterDetailPresenter extends OrderCenterDetailPresenter {
    private PayAsyncTask payAsyncTask;
    private RecisionAsyncTask recisionAsyncTask;
    private ContinueRepayAsyncTask continueRepayAsyncTask;

    public BeautyOrderCenterDetailPresenter(IOrderCenterDetailView view) {
        super(view, new BeautyOrderCenterDetailManager());
    }

    @Override
    public void doPay() {
        if (payAsyncTask != null) {
            payAsyncTask.cancel(true);
            payAsyncTask = null;
        }
        payAsyncTask = new PayAsyncTask();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            payAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else {
            payAsyncTask.execute();
        }
    }

    private void goPay() {
                if (getTradeVo().getTrade().getTradeAmount().compareTo(BigDecimal.ZERO) == 0) {
            DoPayManager.getInstance().fastCashPay(mView.getViewActivity(), getTradeVo(), true, new IPayOverCallback() {

                @Override
                public void onFinished(boolean isOK, int statusCode) {

                }
            });
        } else {

            ZMIntent.pay(mView.getViewActivity(), getTradeVo());
        }
    }

    @Override
    protected String getAcceptPermissionCode() {
        return DinnerApplication.PERMISSION_DINNER_ACCEPT;
    }

    @Override
    public void performAcceptOrder(TradePaymentVo tradePaymentVo) {
        TradeVo tradeVo = tradePaymentVo.getTradeVo();
        List<TradeTable> tradeTables = tradeVo.getTradeTableList();
        if (Utils.isEmpty(tradeTables)) {
                        setTableAndAcceptDinner(tradeVo);
        } else {
            Long tableId = tradeTables.get(0).getTableId();
            try {
                Tables table = DBHelperManager.queryById(Tables.class, tableId);
                if (table != null && table.isValid()) {
                    Trade trade = tradeVo.getTrade();
                                        if (trade.getSource() == SourceId.DIANPING && !isTableUseful(table)) {
                        setTableAndAcceptDinner(tradeVo);
                    } else {
                        acceptDinner(tradeVo);
                    }
                } else {
                                        setTableAndAcceptDinner(tradeVo);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }


    private boolean isTableUseful(Tables table) {
        return table.getTableStatus() == TableStatus.EMPTY
                || (ServerSettingManager.allowMultiTradesOnTable()
                && table.getTableStatus() == TableStatus.OCCUPIED);
    }

    private void acceptDinner(final TradeVo tradeVo) {
        final boolean isAcceptAutoTransferOpen = TradeSourceUtils.isTradeUnProcessed(tradeVo, SourceId.WECHAT) ? SpHelper.getDefault().getBoolean(Constant.WEIXIN_ACCEPT_TRANSFER_KITCHEN) : true;
        mManager.acceptDinner(tradeVo.getTrade(), isAcceptAutoTransferOpen, LoadingResponseListener.ensure(new ResponseListener<TradeResp>() {

            @Override
            public void onResponse(ResponseObject<TradeResp> response) {
                mView.showToast(response.getMessage());
                if (ResponseObject.isOk(response)) {
                    try {
                                                String tradeUuid = tradeVo.getTrade().getUuid();
                        final TradeVo newTradeVo = mManager.findTrade(tradeUuid);
                        Trade newTrade = newTradeVo.getTrade();
                        if (newTrade.getTradePayStatus() == TradePayStatus.PAID) {
                                                                                                           } else {
                                                    }
                        if (isAcceptAutoTransferOpen) {
                                                                                }
                        AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_ACCPET_ORDER, newTrade.getId(), newTrade.getUuid(), newTrade.getClientUpdateTime());
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                } else {
                    AuthLogManager.getInstance().clear();
                    mView.showToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                AuthLogManager.getInstance().clear();
                mView.showToast(error.getMessage());
            }

        }, mView.getViewFragmentManager()));
    }

    private void setTableAndAcceptDinner(final TradeVo tradeVo) {
        DinnerChooseTableDialog chooseTableDialog = DinnerChooseTableDialog_.builder().build();
        chooseTableDialog.setOnTableSelectedConfirmListener(new DinnerChooseTableDialog.OnTableSelectedConfirmListener() {

            @Override
            public void onConfirm(Tables table) {
                if (table == null) {
                    mView.showToast(R.string.please_select_table);
                    return;
                }
                mManager.dinnerSetTableAndAccept(tradeVo,
                        table,
                        LoadingResponseListener.ensure(new ResponseListener<TradeResp>() {

                            @Override
                            public void onResponse(ResponseObject<TradeResp> response) {
                                mView.showToast(response.getMessage());
                                if (ResponseObject.isOk(response)) {
                                    try {
                                                                                String tradeUuid = tradeVo.getTrade().getUuid();
                                        final TradeVo newTradeVo = mManager.findTrade(tradeUuid);
                                        Trade newTrade = newTradeVo.getTrade();
                                        if (newTrade.getTradePayStatus() == TradePayStatus.PAID) {

                                                                                    } else {
                                                                                    }
                                                                            } catch (Exception e) {
                                        Log.e(TAG, e.getMessage(), e);
                                    }
                                } else {
                                    mView.showToast(response.getMessage());
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                mView.showToast(error.getMessage());
                            }

                        }, mView.getViewFragmentManager()));
            }
        });
        chooseTableDialog.show(mView.getViewFragmentManager(), "dinner_choose_table");
    }

    @Override
    public void doRefuse() {
        final TradePaymentVo tradePaymentVo = getTradePaymentVo().clone();
        VerifyHelper.verifyAlert(mView.getViewActivity(), BeautyApplication.PERMISSION_BEAUTY_ACCEPT,
                new VerifyHelper.Callback() {
                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                        doDialogByType(ReasonType.TRADE_REFUSED.value().intValue(), tradePaymentVo);
                    }
                });
    }

    @Override
    public void doRefund() {
        TradePaymentVo tradePaymentVo = getTradePaymentVo().clone();
        verifyPermissAndRefundOrder(tradePaymentVo);
    }

    private void verifyPermissAndRefundOrder(final TradePaymentVo tradePaymentVo) {
        VerifyHelper.verifyAlert(mView.getViewActivity(), BeautyApplication.PERMISSION_BEAUTY_REFUND, new VerifyHelper.Callback() {

            @Override
            public void onPositive(User user, String code, Auth.Filter filter) {
                PaymentItem bnItem = getPaymentItem(tradePaymentVo.getPaymentVoList(), PayModeId.BAINUO_TUANGOU);
                if (bnItem != null && (bnItem.getPayStatus() == TradePayStatus.PAID || bnItem.getPayStatus() == TradePayStatus.PAYING)) {                    String title = mView.getViewActivity().getString(R.string.refund_alert_bainuo_exist);
                    CommonDialogFragment.CommonDialogFragmentBuilder commonDialogFragmentBuilder = new CommonDialogFragment.CommonDialogFragmentBuilder(MainApplication.getInstance());
                    commonDialogFragmentBuilder.iconType(CommonDialogFragment.ICON_WARNING)
                            .title(title)
                            .negativeText(R.string.know)
                            .negativeLisnter(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AuthLogManager.getInstance().clear();
                                }
                            })
                            .build()
                            .show(mView.getViewFragmentManager(), "refund_alert");
                    return;
                }
                                bnItem = getPaymentItem(tradePaymentVo.getPaymentVoList(), PayModeId.KOUBEI_TUANGOU);
                if (bnItem != null && (bnItem.getPayStatus() == TradePayStatus.PAID || bnItem.getPayStatus() == TradePayStatus.PAYING)) {                    String title = mView.getViewActivity().getString(R.string.refund_alert_koubei_exist);
                    CommonDialogFragment.CommonDialogFragmentBuilder commonDialogFragmentBuilder = new CommonDialogFragment.CommonDialogFragmentBuilder(MainApplication.getInstance());
                    commonDialogFragmentBuilder.iconType(CommonDialogFragment.ICON_WARNING)
                            .title(title)
                            .negativeText(R.string.know)
                            .negativeLisnter(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AuthLogManager.getInstance().clear();
                                }
                            })
                            .build()
                            .show(mView.getViewFragmentManager(), "refund_alert");
                    return;
                }
                                final DinnerPosManager posManager = new DinnerPosManager();
                final PaymentItem paymentItem = posManager.getPosPaymentItem(tradePaymentVo.getPaymentVoList());                if (paymentItem != null) {
                    try {
                        PaymentItemDal paymentItemDal = OperatesFactory.create(PaymentItemDal.class);
                                                PaymentItemUnionpayVo vo =
                                paymentItemDal.findPaymentItemUnionpayVoByPaymentItemId(paymentItem.getId());

                                                if (vo != null && vo.getPaymentDevice() != null && vo.getPaymentItemUnionpay() != null
                                && !NewLiandiposManager.getInstance()
                                .canRepeal(NewLiandiposManager.RefundRef.valueOf(vo.getPaymentItemUnionpay(), vo.getPaymentDevice()))) {
                            DialogUtil.showWarnConfirmDialog(mView.getViewFragmentManager(),
                                    R.string.continue_refund_with_settled,
                                    R.string.refuse_refund,
                                    R.string.continue_refund,
                                    null,
                                    new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            doDialogByType(ReasonType.TRADE_RETURNED.value().intValue(), tradePaymentVo);
                                        }
                                    },
                                    "is_continue_refund");
                        } else {
                            doDialogByType(ReasonType.TRADE_RETURNED.value().intValue(), tradePaymentVo);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                } else {
                    doDialogByType(ReasonType.TRADE_RETURNED.value().intValue(), tradePaymentVo);
                }
            }
        });
    }

    @Override
    public void doRecision() {
        if (recisionAsyncTask != null) {
            recisionAsyncTask.cancel(true);
            recisionAsyncTask = null;
        }
        recisionAsyncTask = new RecisionAsyncTask();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            recisionAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else {
            recisionAsyncTask.execute();
        }
    }

    private void verifyPermissAndRecisionOrder(final TradePaymentVo tradePaymentVo) {
        VerifyHelper.verifyAlert(mView.getViewActivity(), BeautyApplication.PERMISSION_BEAUTY_INVALID,
                new VerifyHelper.Callback() {
                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                        doDialogByType(ReasonType.TRADE_INVALID.value().intValue(), tradePaymentVo);
                    }
                });
    }

    @Override
    protected String getRepayPermissionCode() {
        return BeautyApplication.PERMISSION_BEAUTY_REPAY;
    }

    @Override
    protected void performRepayOrder(TradePaymentVo tradePaymentVo) {
                PaymentItem bnItem = getPaymentItem(tradePaymentVo.getPaymentVoList(), PayModeId.KOUBEI_TUANGOU);
        if (bnItem != null && (bnItem.getPayStatus() == TradePayStatus.PAID || bnItem.getPayStatus() == TradePayStatus.PAYING)) {            String title = mView.getViewActivity().getString(R.string.repay_alert_koubei_exist);
            CommonDialogFragment.CommonDialogFragmentBuilder commonDialogFragmentBuilder = new CommonDialogFragment.CommonDialogFragmentBuilder(MainApplication.getInstance());
            commonDialogFragmentBuilder.iconType(CommonDialogFragment.ICON_WARNING)
                    .title(title)
                    .negativeText(R.string.know)
                    .negativeLisnter(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AuthLogManager.getInstance().clear();
                        }
                    })
                    .build()
                    .show(mView.getViewFragmentManager(), "repay_alert");
            return;
        }
                bnItem = getPaymentItem(tradePaymentVo.getPaymentVoList(), PayModeId.BAINUO_TUANGOU);
        if (bnItem != null && (bnItem.getPayStatus() == TradePayStatus.PAID || bnItem.getPayStatus() == TradePayStatus.PAYING)) {            String title = mView.getViewActivity().getString(R.string.repay_alert_bainuo_exist);
            CommonDialogFragment.CommonDialogFragmentBuilder commonDialogFragmentBuilder = new CommonDialogFragment.CommonDialogFragmentBuilder(MainApplication.getInstance());
            commonDialogFragmentBuilder.iconType(CommonDialogFragment.ICON_WARNING)
                    .title(title)
                    .negativeText(R.string.know)
                    .negativeLisnter(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AuthLogManager.getInstance().clear();
                        }
                    })
                    .build()
                    .show(mView.getViewFragmentManager(), "repay_alert");
        } else {
            doDialogByType(ReasonType.TRADE_REPEATED.value().intValue(), tradePaymentVo);
        }
    }

    @Override
    public void doContinueRepay() {
        if (continueRepayAsyncTask != null) {
            continueRepayAsyncTask.cancel(true);
            continueRepayAsyncTask = null;
        }
        continueRepayAsyncTask = new ContinueRepayAsyncTask();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            continueRepayAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else {
            continueRepayAsyncTask.execute();
        }
    }


    @Override
    public void doCall() {
        String phoneNumber = null;
        if (getTradeVo() != null) {
            List<TradeCustomer> customers = getTradeVo().getTradeCustomerList();
            if (customers != null && customers.size() > 0) {
                phoneNumber = customers.get(0).getCustomerPhone();
            }
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            mView.showToast(R.string.trade_center_call_no_phone);
        } else {
            try {
                Intent intent = new Intent("com.zhongmei.bty.phone.call");
                intent.putExtra("phone", phoneNumber);
                mView.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, e.getMessage(), e);
                mView.showToast(R.string.trade_center_call_no_apk);
            }
        }
    }

    @Override
    public void doPrint(String tag) {
        TradeVo tradeVo = getTradeVo();
                if (tradeVo == null || tradeVo.getTrade() == null || tradeVo.getTrade().getTradeStatus() == TradeStatus.REFUSED) {
            ToastUtil.showLongToast(R.string.order_center_detail_can_not_print);
            return;
        }
                if (Utils.isEmpty(tradeVo.getTradeItemList())) {
            ToastUtil.showLongToast(R.string.no_menu);
            return;
        }

        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            UpdateBuilder<Trade, String> updateBuilder = tradeDao.updateBuilder();
            updateBuilder.where().eq(Trade.$.uuid, getTrade().getUuid());
            AuthUser user = Session.getAuthUser();
            if (user != null) {
                updateBuilder.updateColumnValue(Trade.$.updatorName, user.getName());
                updateBuilder.updateColumnValue(Trade.$.updatorId, user.getId());
            }
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        mView.showDialog(getTrade());
    }

    private void doDialogByType(final int reasonType, final TradePaymentVo tradePaymentVo) {
        BeautyOrderCenterOperateDialogFragment dialog = new BeautyOrderCenterOperateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", reasonType);
        dialog.setArguments(bundle);
        dialog.setmInventoryStyle(InventoryShowType.ONLY_SHOW_INVENTORY.value());
        dialog.registerListener(new BeautyOrderCenterOperateDialogFragment.OperateListener() {

            @Override
            public boolean onSuccess(BeautyOrderCenterOperateDialogFragment.OperateResult result) {
                return disposeTheRequest(result, reasonType, tradePaymentVo.getTradeVo());
            }
        });
        dialog.setPayment(tradePaymentVo.getPaymentVoList());
        if (reasonType == ReasonType.TRADE_RETURNED.value()) {
            dialog.setTradeVo(tradePaymentVo.getTradeVo());
            dialog.show(mView.getViewFragmentManager(), "refund");
        } else if (reasonType == ReasonType.TRADE_REFUSED.value()) {
            dialog.show(mView.getViewFragmentManager(), "refuse");
        } else if (reasonType == ReasonType.TRADE_INVALID.value()) {
            dialog.setTradeVo(tradePaymentVo.getTradeVo());
            dialog.show(mView.getViewFragmentManager(), "destroy");
        } else if (reasonType == ReasonType.TRADE_REPEATED.value()) {
            dialog.show(mView.getViewFragmentManager(), "repay");
        }
    }

    private boolean disposeTheRequest(BeautyOrderCenterOperateDialogFragment.OperateResult result, int reasonType, TradeVo tradeVo) {
        if (ReasonType.TRADE_RETURNED.equalsValue(reasonType)) {
            doRefundRequest(result, tradeVo);
        } else if (ReasonType.TRADE_INVALID.equalsValue(reasonType)) {
            doRecisionRequest(result, tradeVo);
        } else if (ReasonType.TRADE_REFUSED.equalsValue(reasonType)) {
            doRefuseRequest(result, tradeVo);
        } else if (ReasonType.TRADE_REPEATED.equalsValue(reasonType)) {
            doRepayRequest(result, tradeVo);
        }
        return false;
    }


    private void doRepayRequest(final BeautyOrderCenterOperateDialogFragment.OperateResult result, TradeVo tradeVo) {
        if (tradeVo == null || result.reason == null) {
            return;
        }
        final Trade trade = tradeVo.getTrade();
        mManager.repayBeautyOrder(tradeVo, result.reason, LoadingResponseListener.ensure(new ResponseListener<PaymentResp>() {

            @Override
            public void onResponse(ResponseObject<PaymentResp> response) {
                if (ResponseObject.isOk(response)) {
                    AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_ANTI_SETTLEMENT, trade.getId(), trade.getUuid(), trade.getClientUpdateTime());
                    List<Trade> trades = response.getContent().getTrades();
                    RefundNewTradAndToDish(trades);
                } else {
                    ToastUtil.showShortToast(response.getMessage());
                    AuthLogManager.getInstance().clear();
                }
            }

            @Override
            public void onError(VolleyError error) {
                AuthLogManager.getInstance().clear();
                ToastUtil.showLongToast(error.getMessage());
            }
        }, mView.getViewFragmentManager()));
    }


    private void RefundNewTradAndToDish(List<Trade> trades) {
                Trade refundTrade = DinnerPosManager.getTradeByTradeType(trades, TradeType.REFUND_FOR_REPEAT);        Trade sellTrade = DinnerPosManager.getTradeByTradeStatus(trades, TradeStatus.REPEATED);        final Trade newTrade = DinnerPosManager.getTradeByTradeStatus(trades, TradeStatus.CONFIRMED);        if (refundTrade != null) {
                                                if (new DinnerPosManager().getPosPaymentItem(refundTrade.getRelateTradeUuid()) != null) {
                                try {
                    new DinnerPosManager().posRefundForRepay(sellTrade,
                            refundTrade,
                            mView.getViewFragmentManager(),
                            new DinnerPosManager.OnPosUIListener() {

                                @Override
                                public void onSuccess() {
                                    RefundNewToDish(newTrade);
                                }

                                @Override
                                public void onFail() {
                                    RefundNewToDish(newTrade);
                                }
                            });
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            } else {
                RefundNewToDish(newTrade);
            }
        }
    }


    private void RefundNewToDish(Trade sell_Trade) {
        if (sell_Trade != null && sell_Trade.getTradeStatus() == TradeStatus.CONFIRMED) {
            try {
                TradeVo tradeVo = mManager.findTrade(sell_Trade.getUuid(), false);
                goToDishWindow(tradeVo);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }


    private void doRefundRequest(final BeautyOrderCenterOperateDialogFragment.OperateResult result, TradeVo tradeVo) {
        if (tradeVo == null) {
            return;
        }
        tradeVo.getTrade().validateUpdate();
        ResponseListener<TradePaymentResp> listener = new ResponseListener<TradePaymentResp>() {

            @Override
            public void onResponse(ResponseObject<TradePaymentResp> response) {
                ToastUtil.showLongToast(response.getMessage());
                if (ResponseObject.isOk(response)) {
                                            Trade refundTrade = response.getContent().getTrade();
                        if (refundTrade != null) {
                                                    }
                                                if (isPayInCash(response.getContent().getPaymentItems())) {
                                                    }

                        if(Utils.isNotEmpty(response.getContent().getPaymentItems())){
                            refreshPayStatus(refundTrade,response.getContent().getPaymentItems());
                        }



                } else {
                    AuthLogManager.getInstance().clear();
                }
            }

            @Override
            public void onError(VolleyError error) {
                AuthLogManager.getInstance().clear();
                mView.showToast(error.getMessage());
            }
        };
        mManager.refundBeauty(tradeVo, result.reason, result.returnInventoryItemReqs,
                LoadingResponseListener.ensure(listener, mView.getViewFragmentManager()));
    }



    private void refreshPayStatus(Trade trade, List<PaymentItem> paymentItems) {
        if (paymentItems == null) {
            return;
        }

        for (PaymentItem paymentItem : paymentItems) {
            if (paymentItem.isValid() && isRefundingInThird(paymentItem)) {
                                Intent intent = new Intent("com.yunfu.beauty.order.action.PaystatusSyncService");
                intent.putExtra("url", ServerAddressUtil.getRemoteHost());
                intent.putExtra("tradeId", trade.getId());
                intent.putExtra("paymentItemId", paymentItem.getId());
                MainApplication.getInstance().startService(intent);
                Log.e("SyncPayStatus","启动退款状态查询。。。。");
            }
        }

    }


    private void doRecisionRequest(final BeautyOrderCenterOperateDialogFragment.OperateResult result, TradeVo tradeVo) {
        if (tradeVo == null) {
            return;
        }
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            final Trade trade = tradeVo.getTrade();
            List<DinnertableState> dinnertableStates = new ArrayList<DinnertableState>();
            for (TradeTable tradeTable : tradeVo.getTradeTableList()) {
                DinnertableState dinnertableState = new DinnertableState();
                dinnertableState.setId(tradeTable.getTableId());

                TablesDal tablesDal = OperatesFactory.create(TablesDal.class);
                List<Trade> trades = tablesDal.findTradesInTable(tradeTable);
                dinnertableState.setTableStatus(trades.size() > 1 ? TableStatus.OCCUPIED : TableStatus.EMPTY);

                                Tables table = DBHelperManager.queryById(Tables.class, tradeTable.getTableId());
                if (table != null) {
                    dinnertableState.setModifyDateTime(table.getModifyDateTime());
                }

                dinnertableStates.add(dinnertableState);
            }

            ResponseListener<TradeResp> listener_destroy = new ResponseListener<TradeResp>() {

                @Override
                public void onResponse(ResponseObject<TradeResp> response) {
                    if (!ResponseObject.isOk(response)) {
                        String msg = response.getMessage();
                        if (!TextUtils.isEmpty(msg)) {
                            ToastUtil.showShortToast(msg);
                        } else {
                            ToastUtil.showShortToast(R.string.dinner_recision_failed);
                        }
                    } else {
                                                Trade trade = response.getContent().trade;
                        if (trade != null) {
                            String uuid = trade.getUuid();
                                                        AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_CANCEL_ORDER, trade.getId(), trade.getUuid(), trade.getClientUpdateTime());
                        }
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    String msg = mView.getViewActivity().getString(R.string.dinner_recision_failed) + ": " + error.getMessage();
                    mView.showToast(msg);
                }

            };
            mManager.recisionBeauty(trade.getId(),
                    trade.getServerUpdateTime(),
                    dinnertableStates,
                    result.reason,
                    result.returnInventoryItemReqs,
                    LoadingResponseListener.ensure(listener_destroy, mView.getViewFragmentManager()));
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }


    private void doRefuseRequest(final BeautyOrderCenterOperateDialogFragment.OperateResult result, TradeVo tradeVo) {
        if (tradeVo == null) {
            return;
        }
        final Long tradeId = tradeVo.getTrade().getId();
        final Long updateTime = tradeVo.getTrade().getClientUpdateTime();
        final String uuid = tradeVo.getTrade().getUuid();
        mManager.refuseDinner(tradeVo,
                result.reason,
                LoadingResponseListener.ensure(new ResponseListener<TradeResp>() {
                    @Override
                    public void onResponse(ResponseObject<TradeResp> response) {
                        mView.showToast(response.getMessage());
                        if (ResponseObject.isOk(response)) {
                            AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_REJECT_ORDER, tradeId, uuid, updateTime);
                        } else {
                            AuthLogManager.getInstance().clear();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        AuthLogManager.getInstance().clear();
                        mView.showToast(error.getMessage());
                    }
                }, mView.getViewFragmentManager()));
    }

    private void goToDishWindow(TradeVo tradeVo) {
                try {

                        Intent mIntent = new Intent();
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.setClass(mView.getViewActivity(), BeautyOrderActivity.class);
            mIntent.putExtra(BeautyOrderConstants.ORDER_EDIT_TRADEID, tradeVo.getTrade().getId());
            mIntent.putExtra(BeautyOrderConstants.IS_ORDER_EDIT, true);
            mView.getViewActivity().startActivity(mIntent);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    @Override
    protected String getInvoicePermissionCode() {
        return DinnerApplication.PERMISSION_DINNER_CASH;
    }

    @Override
    public boolean showRefuseReturn() {
        return false;
    }

    @Override
    public boolean showAcceptReturn() {
        return false;
    }

    @Override
    public boolean showContinueRepay() {
        if (isSquareAccountMode) {
            return false;
        }
        Trade trade = getTrade();
        if (trade != null) {
            TradeStatus tradeStatus = trade.getTradeStatus();
            TradePayStatus tradePayStatus = trade.getTradePayStatus();
            TradeType tradeType = trade.getTradeType();
            if (tradeStatus == TradeStatus.CONFIRMED
                    && tradePayStatus == TradePayStatus.UNPAID
                    && tradeType == TradeType.SELL_FOR_REPEAT) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean showDepositRefund() {
        return false;
    }

    @Override
    public boolean showAccept() {

        return false;
    }

    @Override
    public boolean showRepay() {
        if (isSquareAccountMode) {
            return false;
        }
        if (isPayInJinCheng()) {
            return false;
        }
        Trade trade = getTrade();
        if (trade != null) {
            TradeStatus tradeStatus = trade.getTradeStatus();
            TradeType tradeType = trade.getTradeType();
            TradePayStatus tradePayStatus = trade.getTradePayStatus();
            if ((tradeType == TradeType.SELL || tradeType == TradeType.SELL_FOR_REPEAT || tradeType == TradeType.SPLIT)
                    && (tradeStatus == TradeStatus.FINISH || tradeStatus == TradeStatus.CONFIRMED || tradeStatus == TradeStatus.CREDIT)
                    && tradePayStatus == TradePayStatus.PAID) {
                if (trade.getSource() == SourceId.BAIDU_RICE) {
                    return false;
                }
                return !isClosedOrder();
            }
        }
        return false;
    }


    private boolean isClosedOrder() {
        Long lastClosingTime = mLoadResult.getLastClosingTime();
        return getTradePayTime() <= lastClosingTime;
    }


    private Long getTradePayTime() {
        List<PaymentVo> paymentVoList = getPaymentVoList();
        if (Utils.isNotEmpty(paymentVoList)) {
            PaymentVo paymentVo = paymentVoList.get(0);
            if (paymentVo != null && paymentVo.getPayment() != null) {
                return paymentVo.getPayment().getServerUpdateTime();
            }
        }

        return System.currentTimeMillis();
    }


    private boolean isThirdPartyOrder() {
        Trade trade = getTrade();
        if (trade != null) {
            SourceId sourceId = trade.getSource();
            return sourceId == SourceId.BAIDU_MAP
                    || sourceId == SourceId.BAIDU_TAKEOUT
                    || sourceId == SourceId.ELEME
                    || sourceId == SourceId.MEITUAN_TAKEOUT
                    || sourceId == SourceId.OPEN_PLATFORM;
        }

        return true;
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
    public boolean showRefund() {
        if (isSquareAccountMode) {
            return false;
        }
        if (isPayInJinCheng()) {
            return false;
        }
        Trade trade = getTrade();
        if (trade != null) {
            TradeStatus tradeStatus = trade.getTradeStatus();
            TradeType tradeType = trade.getTradeType();
            TradePayStatus tradePayStatus = trade.getTradePayStatus();
            if ((tradeType == TradeType.SELL || tradeType == TradeType.SELL_FOR_REPEAT || tradeType == TradeType.SPLIT)
                    && !trade.getSource().equalsValue(SourceId.WECHAT.value())
                    && (tradeStatus == TradeStatus.FINISH || tradeStatus == TradeStatus.CONFIRMED || tradeStatus == TradeStatus.CREDIT)
                    && tradePayStatus == TradePayStatus.PAID) {
                if (trade.getSource() == SourceId.BAIDU_RICE) {
                    return false;
                }
                return !isClosedOrder();
            }
        }
        return false;
    }

    @Override
    public boolean showPrint() {
        Trade trade = getTrade();
        return trade.getTradePayStatus() == TradePayStatus.PAID;
    }

    @Override
    public boolean showRecision() {
        if (isSquareAccountMode) {
            return false;
        }
        Trade trade = getTrade();
        if (trade != null) {
            TradeStatus tradeStatus = trade.getTradeStatus();
            TradePayStatus tradePayStatus = trade.getTradePayStatus();
            if (!trade.getSource().equalsValue(SourceId.WECHAT.value()) && (tradeStatus == TradeStatus.CONFIRMED) && tradePayStatus == TradePayStatus.UNPAID && trade.getTradeType() != TradeType.UNOIN_TABLE_MAIN && trade.getTradeType() != TradeType.UNOIN_TABLE_SUB) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean showPay() {
        if (isSquareAccountMode) {
            return false;
        }

        Trade trade = getTrade();
        TradeExtra tradeExtra = getTradeExtra();
        if (trade != null) {
            TradeStatus tradeStatus = trade.getTradeStatus();
            TradePayStatus tradePayStatus = trade.getTradePayStatus();
            TradeType tradeType = trade.getTradeType();
            return tradeStatus == TradeStatus.CONFIRMED
                    && !trade.getSource().equalsValue(SourceId.WECHAT.value())
                    && tradePayStatus == TradePayStatus.UNPAID
                    && Utils.isNotEmpty(getTradeVo().getTradeItemList())
                    && (tradeType == TradeType.SPLIT || (tradeType == TradeType.SELL || getTradePaymentVo().getTradeUnionType() == TradeUnionType.UNION_MAIN))
                    ;
        }

        return false;
    }

    @Override
    public boolean showRefuse() {

        return false;
    }

    @Override
    public boolean showInvoice() {
        return isPaid()
                && mLoadResult.getElectronicInvoiceVo().isSwitchOn()
                && mLoadResult.getElectronicInvoiceVo().getElectronicInvoice() != null
                && Utils.isNotEmpty(mLoadResult.getElectronicInvoiceVo().getInvoiceTaxRates())
                && needInvoice()
                && canInvoice();
    }

    private boolean needInvoice() {
        return !isThirdPartyOrder() || (getTradeExtra() != null && !TextUtils.isEmpty(getTradeExtra().getInvoiceTitle()));
    }

    private boolean canInvoice() {
        Invoice invoice = getInvoice();
        return invoice == null || invoice.getStatus() == InvoiceStatus.INVOICE_REVOKE_SUCCESS || invoice.getStatus() == InvoiceStatus.INVOICE_FAILED;
    }

    @Override
    public boolean showInvoiceRevoke() {
        return isPaid()
                && mLoadResult.getElectronicInvoiceVo().isSwitchOn()
                && canInvoiceRed();
    }

    @Override
    public boolean showTakeDish() {
        return false;
    }

    @Override
    public void doTakeDish() {
        throw new UnsupportedOperationException();
    }

    private boolean canInvoiceRed() {
        Invoice invoice = getInvoice();
        return invoice != null
                && (invoice.getStatus() == InvoiceStatus.INVOICE_ALREADY_APPLIED
                || invoice.getStatus() == InvoiceStatus.INVOICE_SUCCESS
                || (invoice.getStatus() == InvoiceStatus.INVOICE_REVOKE_FAILED && invoice.getReIssue() != null && invoice.getReIssue() == YesOrNo.YES));
    }

    private class ContinueRepayAsyncTask extends AsyncTask<String, Void, List<AsyncHttpRecord>> {
        private AsyncDal asyncDal;
        private Long tradeId;

        @Override
        protected void onPreExecute() {
            asyncDal = OperatesFactory.create(AsyncDal.class);
            tradeId = getTradeVo().getTrade().getId();
        }

        @Override
        protected List<AsyncHttpRecord> doInBackground(String... params) {
            try {
                return asyncDal.queryNotSuccess(tradeId);
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<AsyncHttpRecord> result) {
            if (Utils.isNotEmpty(result)) {
                mView.showToast(R.string.asnc_operation_not_success);
            } else {
                VerifyHelper.verifyAlert(mView.getViewActivity(), BeautyApplication.PERMISSION_BEAUTY_REPAY,
                        new VerifyHelper.Callback() {
                            @Override
                            public void onPositive(User user, String code, Auth.Filter filter) {
                                super.onPositive(user, code, filter);
                                try {
                                    goToDishWindow(getTradeVo().clone());                                } catch (Exception e) {
                                    Log.e(TAG, "clickContinueRepay", e);
                                }
                            }
                        });
            }
        }
    }

    private class RecisionAsyncTask extends AsyncTask<String, Void, List<AsyncHttpRecord>> {
        private AsyncDal asyncDal;
        private TradePaymentVo tradePaymentVo;
        private Long tradeId;

        @Override
        protected void onPreExecute() {
            asyncDal = OperatesFactory.create(AsyncDal.class);
            tradePaymentVo = getTradePaymentVo().clone();
            tradeId = tradePaymentVo.getTradeVo().getTrade().getId();
        }

        @Override
        protected List<AsyncHttpRecord> doInBackground(String... params) {
            try {
                return asyncDal.queryNotSuccess(tradeId);
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<AsyncHttpRecord> result) {
            if (Utils.isNotEmpty(result)) {
                mView.showToast(R.string.asnc_operation_not_success);
            } else {
                verifyPermissAndRecisionOrder(tradePaymentVo);
            }
        }
    }

    private class PayAsyncTask extends AsyncTask<String, Void, List<AsyncHttpRecord>> {
        private AsyncDal asyncDal;
        private Long tradeId;

        @Override
        protected void onPreExecute() {
            asyncDal = OperatesFactory.create(AsyncDal.class);
            tradeId = getTradeVo().getTrade().getId();
        }

        @Override
        protected List<AsyncHttpRecord> doInBackground(String... params) {
            try {
                return asyncDal.queryNotSuccess(tradeId);
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<AsyncHttpRecord> result) {
            if (Utils.isNotEmpty(result)) {
                mView.showToast(R.string.asnc_operation_not_success);
            } else {
                VerifyHelper.verifyAlert(mView.getViewActivity(), BeautyApplication.PERMISSION_BEAUTY_CASH,
                        new VerifyHelper.Callback() {
                            @Override
                            public void onPositive(User user, String code, Auth.Filter filter) {
                                super.onPositive(user, code, filter);
                                goPay();
                            }
                        });
            }
        }
    }
}
