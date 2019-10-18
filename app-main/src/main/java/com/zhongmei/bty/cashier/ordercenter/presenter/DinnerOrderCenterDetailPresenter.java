package com.zhongmei.bty.cashier.ordercenter.presenter;

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
import com.zhongmei.bty.basemodule.async.operates.AsyncDal;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonSource;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.bty.basemodule.database.utils.DbQueryConstant;
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
import com.zhongmei.bty.cashier.ordercenter.manager.DinnerOrderCenterDetailManager;
import com.zhongmei.bty.cashier.ordercenter.view.IOrderCenterDetailView;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterOperateDialogFragment;
import com.zhongmei.bty.cashier.util.TradeSourceUtils;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.util.DateUtil;
import com.zhongmei.bty.dinner.ordercenter.view.DinnerChooseTableDialogFragment;
import com.zhongmei.bty.dinner.ordercenter.view.DinnerChooseTableDialogFragment_;
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
import com.zhongmei.yunfu.db.entity.trade.TradeReturnInfo;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.DeliveryPlatform;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.db.enums.TradePayForm;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeReturnInfoReturnStatus;
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

import static com.zhongmei.bty.basemodule.auth.application.DinnerApplication.PERMISSION_DINNER_REFUND;


public class DinnerOrderCenterDetailPresenter extends OrderCenterDetailPresenter {
    private PayAsyncTask payAsyncTask;
    private RecisionAsyncTask recisionAsyncTask;
    private ContinueRepayAsyncTask continueRepayAsyncTask;

    public DinnerOrderCenterDetailPresenter(IOrderCenterDetailView view) {
        super(view, new DinnerOrderCenterDetailManager());
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
            DoPayManager.gotoPayActivity(mView.getViewActivity(), getTradeVo(), true);
        }
    }

    @Override
    protected String getAcceptPermissionCode() {
        return DinnerApplication.PERMISSION_DINNER_ACCEPT;
    }

    @Override
    public void performAcceptOrder(TradePaymentVo tradePaymentVo) {
        TradeVo tradeVo = tradePaymentVo.getTradeVo();
        if (tradeVo.getTrade().getSource() == SourceId.KOU_BEI) {
            performAcceptKouBeiOrder(tradeVo);
        } else {
            performAcceptOrder(tradeVo);
        }
    }

    private void performAcceptOrder(TradeVo tradeVo) {
        Trade trade = tradeVo.getTrade();
        List<TradeTable> tradeTables = tradeVo.getTradeTableList();
        if (Utils.isEmpty(tradeTables)) {
                        setTableAndAcceptDinner(tradeVo);
        } else {
            Long tableId = tradeTables.get(0).getTableId();
            try {
                Tables table = DBHelperManager.queryById(Tables.class, tableId);
                if (table != null && table.isValid()) {
                                        if ((trade.getSource() == SourceId.DIANPING ) && !isTableUseful(table)) {
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

    private void performAcceptKouBeiOrder(final TradeVo tradeVo) {
        Trade trade = tradeVo.getTrade();
        TradeExtra tradeExtra = tradeVo.getTradeExtra();
        boolean allowMultTrades = ServerSettingManager.allowMultiTradesOnTable();
        if (trade.getDeliveryType() == DeliveryType.HERE) {            if (!tradeVo.isAppointmentOrder()) {                kouBeiOrderHereAccept(tradeVo);
            } else {                String expectTime = DateUtil.format(tradeExtra.getExpectTime());
                String hint = allowMultTrades ?
                        String.format(mView.getViewActivity().getString(R.string.koubei_accept_order_appointment_hint2), expectTime) : String.format(mView.getViewActivity().getString(R.string.koubei_accept_order_appointment_hint), expectTime);
                DialogUtil.showHintConfirmDialog(mView.getViewFragmentManager(),
                        hint,
                        R.string.common_cancel,
                        R.string.order_center_detail_confirm_accept_order,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                kouBeiOrderHereAccept(tradeVo);
                            }
                        }, "");
            }
        } else if (trade.getDeliveryType() == DeliveryType.TAKE) {            if (!tradeVo.isAppointmentOrder()) {                acceptDinner(tradeVo);
            } else {                String expectTime = DateUtil.format(tradeExtra.getExpectTime());
                String hint =
                        String.format(mView.getViewActivity().getString(R.string.koubei_accept_order_appointment_hint3), expectTime);
                DialogUtil.showHintConfirmDialog(mView.getViewFragmentManager(),
                        hint,
                        R.string.common_cancel,
                        R.string.order_center_detail_confirm_accept_order,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                acceptDinner(tradeVo);
                            }
                        }, "");
            }
        }
    }


    private void kouBeiOrderHereAccept(TradeVo tradeVo) {
        List<TradeTable> tradeTables = tradeVo.getTradeTableList();
        if (Utils.isEmpty(tradeTables)) {            setTableAndAcceptDinner(tradeVo);
        } else {            try {
                Long tableId = tradeTables.get(0).getTableId();
                Tables table = DBHelperManager.queryById(Tables.class, tableId);
                if (table != null && table.isValid()) {
                    if (!isTableUseful(table)) {
                        setTableAndAcceptDinner(tradeVo, table);
                    } else {
                        acceptDinner(tradeVo);
                    }
                } else {
                    setTableAndAcceptDinner(tradeVo, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
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
        final boolean isPaid = tradeVo.getTrade().getTradePayStatus() == TradePayStatus.PAID;
        final boolean isSendKitchen = isAcceptAutoTransferOpen || isPaid;
        mManager.acceptDinner(tradeVo.getTrade(), isSendKitchen, LoadingResponseListener.ensure(new ResponseListener<TradeResp>() {

            @Override
            public void onResponse(ResponseObject<TradeResp> response) {
                mView.showToast(response.getMessage());
                if (ResponseObject.isOk(response)) {
                    try {
                                                String tradeUuid = tradeVo.getTrade().getUuid();
                        final TradeVo newTradeVo = mManager.findTrade(tradeUuid);
                        Trade newTrade = newTradeVo.getTrade();

                        Long expectTime = tradeVo.getTradeExtra() == null ? null : tradeVo.getTradeExtra().getExpectTime();

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
        setTableAndAcceptDinner(tradeVo, null);
    }

    private void setTableAndAcceptDinner(final TradeVo tradeVo, Tables orderTable) {
        final boolean isAcceptAutoTransferOpen = TradeSourceUtils.isTradeFromWeixinUnproccess(tradeVo) ? SpHelper.getDefault().getBoolean(Constant.WEIXIN_ACCEPT_TRANSFER_KITCHEN) : true;
        final boolean isPaid = tradeVo.getTrade().getTradePayStatus() == TradePayStatus.PAID;
        final boolean isSendKitchen = isAcceptAutoTransferOpen || isPaid;

        DinnerChooseTableDialogFragment chooseTableDialogFragment = new DinnerChooseTableDialogFragment_();
        chooseTableDialogFragment.setTables(orderTable);
        chooseTableDialogFragment.setOnTableSelectedConfirmListener(new DinnerChooseTableDialogFragment.OnTableSelectedConfirmListener() {
            @Override
            public void onConfirm(Tables tables) {
                mManager.dinnerSetTableAndAccept(tradeVo,
                        tables,
                        LoadingResponseListener.ensure(new ResponseListener<TradeResp>() {

                            @Override
                            public void onResponse(ResponseObject<TradeResp> response) {
                                mView.showToast(response.getMessage());
                                if (ResponseObject.isOk(response)) {
                                    try {
                                                                                String tradeUuid = tradeVo.getTrade().getUuid();
                                        final TradeVo newTradeVo = mManager.findTrade(tradeUuid);

                                        Long expectTime = tradeVo.getTradeExtra() == null ? null : tradeVo.getTradeExtra().getExpectTime();

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
        chooseTableDialogFragment.show(mView.getViewFragmentManager(), "dinner_choose_table");
    }


    @Override
    public void doRefuse() {
        final TradePaymentVo tradePaymentVo = getTradePaymentVo().clone();
        VerifyHelper.verifyAlert(mView.getViewActivity(), DinnerApplication.PERMISSION_DINNER_ACCEPT,
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
        final TradePaymentVo tradePaymentVo = getTradePaymentVo().clone();
        final Trade trade = tradePaymentVo.getTradeVo().getTrade();
        if (trade.getSource() == SourceId.XIN_MEI_DA && trade.getTradePayStatus() == TradePayStatus.PAID && trade.getTradePayForm() == TradePayForm.ONLINE) {
            DialogUtil.showWarnConfirmDialog(mView.getViewFragmentManager(),
                    R.string.order_center_refund_title,
                    R.string.order_center_refund_ok,
                    R.string.order_center_refund_cancel,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            verifyPermissAndRefundOrder(tradePaymentVo);
                        }
                    },
                    null,
                    "offline_refund");
        } else {
            if (tradePaymentVo.getTradeVo().getTradeEarnestMoney() > 0) {                DialogUtil.showWarnConfirmDialog(mView.getViewFragmentManager(),
                        R.string.dinner_order_center_refund_earnest_not_fefund,
                        R.string.order_center_refund_ok,
                        R.string.order_center_refund_cancel,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                verifyPermissAndRefundOrder(tradePaymentVo);
                            }
                        },
                        null,
                        "earnest_refund");
            } else {
                verifyPermissAndRefundOrder(tradePaymentVo);
            }
        }
    }

    private void verifyPermissAndRefundOrder(final TradePaymentVo tradePaymentVo) {
        VerifyHelper.verifyAlert(mView.getViewActivity(), DinnerApplication.PERMISSION_DINNER_REFUND, new VerifyHelper.Callback() {

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
        TradeVo tradeVo = getTradeVo();
        if (tradeVo.getTradeEarnestMoney() > 0) {            DialogUtil.showWarnConfirmDialog(mView.getViewFragmentManager(),
                    R.string.dinner_order_center_recision_earnest_not_fefund,
                    R.string.dinner_ok,
                    R.string.dinner_cancel,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recision();
                        }
                    },
                    null,
                    "doRecision");
        } else {
            recision();
        }
    }

    private void recision() {        if (recisionAsyncTask != null) {
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
        VerifyHelper.verifyAlert(mView.getViewActivity(), DinnerApplication.PERMISSION_DINNER_INVALID,
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
        return DinnerApplication.PERMISSION_DINNER_REPAY;
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
        doDialogByType(reasonType, tradePaymentVo, null);
    }

    private void doDialogByType(final int reasonType, final TradePaymentVo tradePaymentVo, final String requestUuid) {
        final SourceId sourceId = tradePaymentVo.getTradeVo().getTrade().getSource();
        ReasonSource reasonSource = getReasonSourceWithKouBei(sourceId);
        OrderCenterOperateDialogFragment dialog = new OrderCenterOperateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", reasonType);
        bundle.putInt(OrderCenterOperateDialogFragment.EXTRA_SOURCE, reasonSource.value());
        dialog.setArguments(bundle);
        dialog.registerListener(new OrderCenterOperateDialogFragment.OperateListener() {
            @Override
            public boolean onSuccess(OrderCenterOperateDialogFragment.OperateResult result) {
                return disposeTheRequest(result, reasonType, tradePaymentVo.getTradeVo(), requestUuid);
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
        } else if (reasonType == ReasonType.AGREE_RETURN.value()) {
            dialog.show(mView.getViewFragmentManager(), "agree_return");
        } else {
            dialog.show(mView.getViewFragmentManager(), "otherAction");
        }
    }

    private boolean disposeTheRequest(OrderCenterOperateDialogFragment.OperateResult result, int reasonType, TradeVo tradeVo, final String requestUuid) {
        if (ReasonType.TRADE_RETURNED.equalsValue(reasonType)) {
            doRefundRequest(result, tradeVo);
        } else if (ReasonType.TRADE_INVALID.equalsValue(reasonType)) {
            doRecisionRequest(result, tradeVo);
        } else if (ReasonType.TRADE_REFUSED.equalsValue(reasonType)) {
            doRefuseRequest(result, tradeVo);
        } else if (ReasonType.TRADE_REPEATED.equalsValue(reasonType)) {
            doRepayRequest(result, tradeVo);
        }
        if (ReasonType.REFUSE_RETURN.equalsValue(reasonType) || ReasonType.AGREE_RETURN.equalsValue(reasonType)) {              doReturnRequest(result, tradeVo, reasonType, requestUuid);
        }
        return false;
    }


    private void doRepayRequest(final OrderCenterOperateDialogFragment.OperateResult result, TradeVo tradeVo) {
        if (tradeVo == null || result == null) {
            return;
        }
        final Trade trade = tradeVo.getTrade();
        mManager.repayOrder(tradeVo, result.reason, LoadingResponseListener.ensure(new ResponseListener<PaymentResp>() {

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
                mView.goToDishWindow(tradeVo);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }


    private void doRefundRequest(final OrderCenterOperateDialogFragment.OperateResult result, TradeVo tradeVo) {
        if (tradeVo == null) {
            return;
        }
        tradeVo.getTrade().validateUpdate();
        ResponseListener<TradePaymentResp> listener = new ResponseListener<TradePaymentResp>() {

            @Override
            public void onResponse(ResponseObject<TradePaymentResp> response) {
                ToastUtil.showLongToast(response.getMessage());
                if (ResponseObject.isOk(response)) {
                                        List<Trade> trades = response.getContent().getTrades();
                    if (Utils.isNotEmpty(trades)) {
                        Trade refundTrade = DinnerPosManager.getTradeByTradeType(trades, TradeType.REFUND);
                        if (refundTrade != null) {
                                                        if (result.isPrintChecked) {
                                                                                            }
                        }
                                                if (isPayInCash(response.getContent().getPaymentItems())) {
                                                                                }

                        Trade sellTrade = null;
                        for (Trade trade : trades) {
                            if (trade.getTradeType() != TradeType.REFUND) {                                sellTrade = trade;
                            }
                        }
                        try {
                            if (sellTrade != null && refundTrade != null) {
                                new DinnerPosManager().posRefund(sellTrade, refundTrade, mView.getViewFragmentManager());
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                        AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_RETURN_REFUND, sellTrade.getId(), sellTrade.getUuid(), sellTrade.getClientUpdateTime());
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
        mManager.refundDinner(tradeVo, result.reason, result.returnInventoryItemReqs,
                LoadingResponseListener.ensure(listener, mView.getViewFragmentManager()));
    }


    private void doRecisionRequest(final OrderCenterOperateDialogFragment.OperateResult result, TradeVo tradeVo) {
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
            mManager.recisionDinner(trade.getId(),
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


    private void doRefuseRequest(final OrderCenterOperateDialogFragment.OperateResult result, TradeVo tradeVo) {
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

    @Override
    protected String getInvoicePermissionCode() {
        return DinnerApplication.PERMISSION_DINNER_CASH;
    }

    @Override
    public boolean showRefuseReturn() {
        if (isSquareAccountMode || isBindDeliveryUserMode) {
            return false;
        }

        TradeReturnInfo tradeReturnInfo = getTradeReturnInfo();
        return (mCurrentTab == DbQueryConstant.UNPROCESSED_ALL || mCurrentTab == DbQueryConstant.UNPROCESSED_CANCEL_REQUEST)
                && tradeReturnInfo != null && (tradeReturnInfo.getSequenceNo() == null || tradeReturnInfo.getSequenceNo() == 1);
    }

    @Override
    public boolean showAcceptReturn() {
        if (isSquareAccountMode || isBindDeliveryUserMode) {
            return false;
        }

        return (mCurrentTab == DbQueryConstant.UNPROCESSED_ALL || mCurrentTab == DbQueryConstant.UNPROCESSED_CANCEL_REQUEST)
                && getTradeReturnInfo() != null;
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
        if (isSquareAccountMode) {
            return false;
        }

        Trade trade = getTrade();
        if (trade != null) {
            TradeStatus tradeStatus = trade.getTradeStatus();
            TradePayStatus tradePayStatus = trade.getTradePayStatus();
            TradePayForm tradePayForm = trade.getTradePayForm();
            SourceId sourceId = trade.getSource();
            return (tradePayForm == TradePayForm.OFFLINE || tradePayStatus == TradePayStatus.PAID)
                    && tradeStatus == TradeStatus.UNPROCESSED
                    && sourceId != SourceId.POS;
        }

        return false;
    }

    @Override
    public boolean showRepay() {
        if (isSquareAccountMode || mCurrentTab == DbQueryConstant.UNPROCESSED_CANCEL_REQUEST) {
            return false;
        }
        if (isPayInJinCheng()) {
            return false;
        }
        TradeVo tradeVo = getTradeVo();
        if (tradeVo != null && tradeVo.getTradeEarnestMoney() > 0) {            return false;
        }
        Trade trade = getTrade();
        if (trade != null) {
            TradeStatus tradeStatus = trade.getTradeStatus();
            TradeType tradeType = trade.getTradeType();
            TradePayStatus tradePayStatus = trade.getTradePayStatus();
            if ((tradeType == TradeType.SELL || tradeType == TradeType.SELL_FOR_REPEAT || tradeType == TradeType.SPLIT)
                    && (tradeStatus == TradeStatus.FINISH || tradeStatus == TradeStatus.CONFIRMED || tradeStatus == TradeStatus.CREDIT)
                    && tradePayStatus == TradePayStatus.PAID) {
                if (trade.getSource() == SourceId.BAIDU_RICE || trade.getSource() == SourceId.XIN_MEI_DA) {
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
        if (isSquareAccountMode || mCurrentTab == DbQueryConstant.UNPROCESSED_CANCEL_REQUEST) {
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
                double exceptAmount = 0;                TradePaymentVo tradePaymentVo = getTradePaymentVo().clone();
                if (tradePaymentVo != null && tradePaymentVo.getPaymentVoList() != null && tradePaymentVo.getPaymentVoList().size() > 0) {
                    for (PaymentVo paymentVo : tradePaymentVo.getPaymentVoList()) {
                        if (paymentVo.getPayment() != null && paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_SELL && paymentVo.getPayment().getExemptAmount() != null && paymentVo.getPayment().getExemptAmount().doubleValue() > 0) {
                            exceptAmount = paymentVo.getPayment().getExemptAmount().doubleValue();
                        }
                    }
                }
                TradeVo tradeVo = getTradeVo();
                if (tradeVo != null && tradeVo.getTradeEarnestMoney() > 0 && tradeVo.getTradeEarnestMoney() >= (trade.getTradeAmount().doubleValue() - exceptAmount)) {                    return false;
                }
                return !isClosedOrder();
            }
        }
        return false;
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
            if ((tradeStatus == TradeStatus.CONFIRMED) && tradePayStatus == TradePayStatus.UNPAID && trade.getTradeType() != TradeType.UNOIN_TABLE_MAIN && trade.getTradeType() != TradeType.UNOIN_TABLE_SUB) {
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
                    && tradePayStatus == TradePayStatus.UNPAID
                    && (tradeType == TradeType.SPLIT || (tradeType == TradeType.SELL && getTradePaymentVo().getTradeUnionType() == TradeUnionType.UNION_MAIN))
                    && (tradeExtra == null
                    || tradeExtra.getDeliveryPlatform() != DeliveryPlatform.MERCHANT
                    || TextUtils.isEmpty(tradeExtra.getDeliveryUserId()))
                    && !isClosedOrder();
        }

        return false;
    }

    @Override
    public boolean showRefuse() {
        if (isSquareAccountMode) {
            return false;
        }
        Trade trade = getTrade();
        if (trade != null) {
            TradeStatus tradeStatus = trade.getTradeStatus();
            TradePayStatus tradePayStatus = trade.getTradePayStatus();
            TradePayForm tradePayForm = trade.getTradePayForm();
            SourceId sourceId = trade.getSource();
            if (tradePayStatus == TradePayStatus.PAID && (trade.getSource() == SourceId.DIANPING )
                    && trade.getTradeStatus() == TradeStatus.UNPROCESSED) {
                return false;
            }
            return (tradePayForm == TradePayForm.OFFLINE || tradePayStatus == TradePayStatus.PAID)
                    && tradeStatus == TradeStatus.UNPROCESSED
                    && sourceId != SourceId.POS;
        }

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
                VerifyHelper.verifyAlert(mView.getViewActivity(), DinnerApplication.PERMISSION_DINNER_REPAY,
                        new VerifyHelper.Callback() {
                            @Override
                            public void onPositive(User user, String code, Auth.Filter filter) {
                                super.onPositive(user, code, filter);
                                try {
                                    mView.goToDishWindow(getTradeVo().clone());                                } catch (Exception e) {
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
                VerifyHelper.verifyAlert(mView.getViewActivity(), DinnerApplication.PERMISSION_DINNER_CASH,
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



    @Override
    public void doAcceptReturn() {
        final TradePaymentVo tradePaymentVo = getTradePaymentVo().clone();
        VerifyHelper.verifyAlert(mView.getViewActivity(), PERMISSION_DINNER_REFUND,
                new VerifyHelper.Callback() {
                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                        doDialogByType(ReasonType.AGREE_RETURN.value().intValue(), tradePaymentVo, getRequestUuid(mLoadResult.getTradeReturnInfo()));
                    }
                });
    }


    @Override
    public void doRefuseReturn() {
        TradePaymentVo tradePaymentVo = getTradePaymentVo().clone();
        doDialogByType(ReasonType.REFUSE_RETURN.value().intValue(), tradePaymentVo, getRequestUuid(mLoadResult.getTradeReturnInfo()));
    }



    private void doReturnRequest(final OrderCenterOperateDialogFragment.OperateResult result, final TradeVo tradeVo, int reasonType, String requestUuid) {
        if (tradeVo == null) {
            return;
        }
        final TradeReturnInfoReturnStatus returnStatus = ReasonType.REFUSE_RETURN.equalsValue(reasonType) ? TradeReturnInfoReturnStatus.REFUSE : TradeReturnInfoReturnStatus.AGREE;
        mManager.returnConfirm(tradeVo, returnStatus, result.isReturnInvetory, requestUuid, result.reason, LoadingResponseListener.ensure(new ResponseListener<Object>() {

            @Override
            public void onResponse(ResponseObject<Object> response) {
                mView.showToast(response.getMessage());
                if (ResponseObject.isOk(response) && returnStatus == TradeReturnInfoReturnStatus.AGREE) {
                    Trade trade = tradeVo.getTrade();
                                        if (trade.getTradePayStatus() == TradePayStatus.PAID) {
                        if (trade != null) {
                                                        if (result.isPrintChecked) {
                                                                                            }
                        }
                    } else if (trade.getTradePayStatus() == TradePayStatus.UNPAID) {                                                                     }

                                        Long expectTime = null;
                    if (tradeVo.getTradeExtra().getExpectTime() != null) {
                        expectTime = tradeVo.getTradeExtra().getExpectTime();
                    }
                                    }
            }

            @Override
            public void onError(VolleyError error) {
                mView.showToast(error.getMessage());
            }

        }, mView.getViewFragmentManager()));
    }


    public String getRequestUuid(TradeReturnInfo tradeReturnInfo) {
        if (tradeReturnInfo != null) {
            return tradeReturnInfo.getUuid();
        }

        return "";
    }

    private ReasonSource getReasonSourceWithKouBei(SourceId sourceId) {
        return sourceId == SourceId.BAIDU_TAKEOUT ? ReasonSource.BAIDU_TAKEOUT
                : sourceId == SourceId.KOU_BEI ? ReasonSource.KOUBEI : ReasonSource.ZHONGMEI;
    }
    }
