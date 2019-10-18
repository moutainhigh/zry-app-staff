package com.zhongmei.bty.mobilepay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.async.event.ActionExitLoginRemind;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.constants.SettingConstant;
import com.zhongmei.bty.basemodule.commonbusiness.utils.BusinessTypeUtils;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.devices.liandipos.NewLiandiposManager;
import com.zhongmei.bty.basemodule.pay.bean.ElectronicInvoiceVo;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.SeparateShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeInvoiceNo;
import com.zhongmei.bty.basemodule.trade.event.ActionCloseOrderDishActivity;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.basemodule.trade.message.FHInvoiceQrcodeReq;
import com.zhongmei.bty.basemodule.trade.message.FHInvoiceQrcodeResp;
import com.zhongmei.bty.basemodule.trade.message.GetTaxNoReq;
import com.zhongmei.bty.basemodule.trade.message.GetTaxNoResp;
import com.zhongmei.bty.basemodule.trade.message.InvoiceQrcodeReq;
import com.zhongmei.bty.basemodule.trade.message.InvoiceQrcodeResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.data.operate.OperatesRetailFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.mobilepay.IPayConstParame;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.bean.PayModelItem;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.enums.PayActionPage;
import com.zhongmei.bty.mobilepay.event.SeparateEvent;
import com.zhongmei.bty.mobilepay.fragment.InvoiceFragment;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.mobilepay.utils.DoPayUtils;
import com.zhongmei.bty.mobilepay.v1.event.EventPayResult;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.data.GatewayTransferResp;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.util.ToastUtil;

import java.math.BigDecimal;

import de.greenrobot.event.EventBus;



public class GetTaxNoPayOkDialog extends Dialog implements IPayConstParame {
    private String TAG = PayOkDialog.class.getSimpleName();
    private FragmentActivity mContext;
    private ImageView mPayOkIV;    private TextView mPayAmountTV;    private TextView mRestAmountTV;    private TextView mOkAlertTV;    private ImageView mCloseBT;    private IPaymentInfo mPaymentInfo;    private boolean isFinishPay;    private final int MSG_WAT_DISMISS = 1;
    private DoPayApi mDoPayApi;    private TaxNoViewHolder viewHolder;
    private int mOperateType = OP_TYPE_DOPAY;    private boolean isPrinted = false;
    private long startTimeMills = System.currentTimeMillis();
    private final long WAITE_TIME_LIMIT = 4000;
    public GetTaxNoPayOkDialog(Context context, int theme) {
        super(context, theme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pay_ok_get_taxno_dialog_layout);    }

    public GetTaxNoPayOkDialog(FragmentActivity context, DoPayApi doPayApi, IPaymentInfo paymentInfo, int operateType) {
        this(context, R.style.custom_alert_dialog);
        this.mContext = context;
        this.mDoPayApi = doPayApi;
        this.mPaymentInfo = paymentInfo;
        this.isFinishPay = DoPayUtils.isTradePaidOver(paymentInfo.getTradeVo().getTrade());        this.mOperateType = operateType;
        this.findViews();        this.setOnDismissListener(dismissListener);
        this.initView(paymentInfo);    }

    private void delayDismiss(long delayMillis) {
        Message ms = mHandler.obtainMessage();
        ms.what = MSG_WAT_DISMISS;
        mHandler.sendMessageDelayed(ms, delayMillis);
    }

    private void findViews() {
        mPayOkIV = (ImageView) findViewById(R.id.pay_ok_image);
        mPayAmountTV = (TextView) findViewById(R.id.pay_amount_text);
        mRestAmountTV = (TextView) findViewById(R.id.pay_rest_amount_text);
        mOkAlertTV = (TextView) findViewById(R.id.pay_ok_alert_title);
        mCloseBT = (ImageView) findViewById(R.id.pay_ok_close_button);
                viewHolder = new TaxNoViewHolder(mContext);
        viewHolder.payOkLL = (LinearLayout) findViewById(R.id.pay_ok_ll);
        viewHolder.buttonLL = (LinearLayout) findViewById(R.id.pay_over_taxno_button);
        viewHolder.getTaxLL = (LinearLayout) findViewById(R.id.pay_over_taxno_ll);
        viewHolder.closeIV = (ImageView) findViewById(R.id.pay_over_taxno_close);
        viewHolder.getTaxIcon = (ImageView) findViewById(R.id.pay_over_taxno_image);
        viewHolder.alterTV = (TextView) findViewById(R.id.pay_over_taxno_alert_title);
        viewHolder.reTryBT = (Button) findViewById(R.id.reget_button);
        viewHolder.printBT = (Button) findViewById(R.id.pint_button);
                mCloseBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPrintPayTick();
                dismiss();
            }
        });
                viewHolder.closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPrintPayTick();
                dismiss();
            }
        });
                viewHolder.printBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPrintPayTick();
                dismiss();
            }
        });
                viewHolder.reTryBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGetTaxNo(50);
            }
        });
    }

    private void initView(IPaymentInfo paymentInfo) {
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                String okText = mContext.getString(R.string.pay_success);
        double payAmount = paymentInfo.getOtherPay().getGroupAmount();
        switch (mOperateType) {
            case OP_TYPE_REFUND:                okText = mContext.getString(R.string.salesreturn_success);
                break;
            case OP_TYPE_BOOKING:                okText = mContext.getString(R.string.book_success);
                break;
            case OP_TYPE_LAG:                okText = mContext.getString(R.string.lag_success);
                payAmount = paymentInfo.getTradeAmount();
                break;
            case OP_TYPE_DEDUCTION:                okText = mContext.getString(R.string.deduction_earnest_success);
                break;
            case OP_TYPE_DEDUCTION_REFUND:                okText = mContext.getString(R.string.return_earnest_success);
                payAmount = CashInfoManager.floatSubtract(paymentInfo.getTradeVo().getTradeEarnestMoney(), paymentInfo.getOtherPay().getGroupAmount());
                break;
        }
                mOkAlertTV.setText(okText);
                mPayAmountTV.setText(CashInfoManager.formatCash(payAmount));

                double restAmount = 0;
        if (mPaymentInfo.getTradeVo().getTrade().getBusinessType() == BusinessType.BOOKING_LIST) {
            if (mPaymentInfo.getPayScene() != PayScene.SCENE_CODE_BOOKING_DEPOSIT)
                restAmount = mPaymentInfo.getTradeVo().getTrade().getTradeAmount().doubleValue() - mPaymentInfo.getTradeVo().getBakeryBookingAmount().doubleValue();
        } else if (mPaymentInfo.getTradeVo().getTrade().getBusinessType() == BusinessType.TAILING_LIST) {
            restAmount = 0;
                    } else {
            restAmount = paymentInfo.getActualAmount();
        }
                if (mOperateType == OP_TYPE_BOOKING || (!isFinishPay && restAmount > 0 && mOperateType == OP_TYPE_DOPAY)) {
            mRestAmountTV.setVisibility(View.VISIBLE);
            mRestAmountTV.setText(mContext.getString(R.string.pay_need_to_pay_text) + CashInfoManager.formatCash(restAmount));
            mPayOkIV.setImageResource(R.drawable.pay_scuess_part_icon);
            mPayAmountTV.setTextColor(getContext().getResources().getColor(R.color.text_gray));
            mOkAlertTV.setTextColor(getContext().getResources().getColor(R.color.text_gray));
            mPayAmountTV.setTextSize(22);
            mOkAlertTV.setTextSize(22);
                    }
                if ((isFinishPay) && paymentInfo.getOtherPay().isContainsPayModel(PayModeId.CASH)) {
            PayModelItem cashItem = paymentInfo.getOtherPay().getPayModelItemByPayModeId(PayModeId.CASH);
            if (cashItem != null && cashItem.getChangeAmount().compareTo(BigDecimal.ZERO) > 0) {
                mRestAmountTV.setVisibility(View.VISIBLE);
                mRestAmountTV.setTextSize(26);
                mRestAmountTV.setText(mContext.getString(R.string.pay_odd_change) + CashInfoManager.formatCash(cashItem.getChangeAmount().doubleValue()));
                            }
        } else {
            double morePayAmount = paymentInfo.getOtherPay().getMorePayAmount();                        if (morePayAmount > 0 && isFinishPay) {
                mRestAmountTV.setVisibility(View.VISIBLE);
                mRestAmountTV.setTextSize(26);
                mRestAmountTV.setText(String.format(mContext.getString(R.string.more_pay_cash_text), CashInfoManager.formatCash(morePayAmount)));
                            }
        }
                        deletePosTransLogAsync(paymentInfo);
                paymentInfo.getOtherPay().clear();
                if (isFinishPay && (paymentInfo.getTradeBusinessType() == BusinessType.DINNER
                || paymentInfo.getTradeBusinessType() == BusinessType.BUFFET)) {
            printKitchenAfterPay(paymentInfo.getTradeVo());
        }
                if (isFinishPay && ShopInfoCfg.getInstance().isMonitorCode(ShopInfoCfg.ShopMonitorCode.MONITOR_CODE_FHXM)) {
            performFHInvoiceQrcode(mPaymentInfo.getTradeVo());
        }
                new AsyncTask<Void, Void, TradeInvoiceNo>() {

            @Override
            protected TradeInvoiceNo doInBackground(Void... params) {
                TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
                TradeInvoiceNo tradeInvoiceNo = null;
                try {
                    tradeInvoiceNo = tradeDal.findTradeInvoiceNoByTradeId(mPaymentInfo.getTradeVo().getTrade().getId());
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
                return tradeInvoiceNo;
            }

            protected void onPostExecute(TradeInvoiceNo data) {
                if (data != null) {
                    doPrintPayTick();
                    delayDismiss(1000);
                } else {
                    startGetTaxNo(1200);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WAT_DISMISS:
                    try {
                        dismiss();
                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    OnDismissListener dismissListener = new OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            try {
                Trade trade = mPaymentInfo.getTradeVo().getTrade();
                                doJinChengBusiness(trade);                                if (isFinishPay || mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT || mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT) {                                        if (mPaymentInfo.isOpenElectronicInvoice()) {
                        if (ShopInfoCfg.getInstance().isMonitorCode(ShopInfoCfg.ShopMonitorCode.MONITOR_CODE_FHXM)) {
                                                        doFinish();
                                                    } else {
                            ElectronicInvoiceVo electronicInvoiceVo = PaySettingCache.getElectronicInvoiceVo();
                            showInvoiceQrcodeDialog(mPaymentInfo.getTradeVo(), electronicInvoiceVo, DoPayUtils.getMaxInvoiceAmount(mPaymentInfo.getTradeVo(), mPaymentInfo.getPaidPaymentItems()));
                        }
                                            } else {
                        doFinish();
                    }
                                        if (trade.getTradeType() != TradeType.SELL_FOR_REPEAT) {
                                            }
                } else if (trade.getTradeStatus() == TradeStatus.CREDIT) {
                } else {
                                        DisplayServiceManager.updateDisplayPay(mContext.getApplicationContext(), mPaymentInfo.getActualAmount());
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            } finally {
                mHandler.removeCallbacksAndMessages(null);
            }
        }
    };

    private void doFinish() {
                if (mPaymentInfo.getPayActionPage() == PayActionPage.BALANCE) {            if (mPaymentInfo.isSplit()) {                showDialog(mContext);
            } else {                DinnerShoppingCart.getInstance().clearShoppingCart();
                DinnerShopManager.getInstance().clearCustomer();

                mPaymentInfo.doFinish();
                EventBus.getDefault().post(new ActionCloseOrderDishActivity());
                exitLoginRemind();
            }

        } else if (mPaymentInfo.getPayActionPage() == PayActionPage.COMPAY) {            mPaymentInfo.doFinish();

        } else {
            mPaymentInfo.doFinish();
            DisplayServiceManager.doCancel(mContext.getApplicationContext());
        }
    }

    private void showDialog(final FragmentActivity context) {
        if (!context.isDestroyed()) {
            try {
                new CommonDialogFragment.CommonDialogFragmentBuilder(context)
                        .title(context.getResources().getString(R.string.pay_success))
                        .iconType(CommonDialogFragment.ICON_SUCCESS)
                        .positiveText(R.string.pay_goto_table)
                        .positiveLinstner(new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                                                try {
                                    SeparateShoppingCart.getInstance().clearShoppingCart();
                                    DinnerShoppingCart.getInstance().clearShoppingCart();
                                    DinnerShopManager.getInstance().clearCustomer();
                                    mPaymentInfo.doFinish();
                                    EventBus.getDefault().post(new ActionCloseOrderDishActivity());
                                } catch (Exception e) {
                                    Log.e(TAG, "", e);
                                }
                            }
                        })
                        .negativeText(R.string.pay_goon_paying)
                        .negativeLisnter(new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                try {
                                                                        mPaymentInfo.clearData();
                                    DoPayUtils.findSourceTradePaymentVos(mPaymentInfo);                                                                        EventBus.getDefault().post(new SeparateEvent(SeparateEvent.EVENT_SEPARATE_PAYED));
                                    CustomerResp customer = CustomerManager.getInstance().getDinnerLoginCustomer();
                                    SeparateShoppingCart.getInstance().clearShoppingCart();
                                    DinnerShoppingCart.getInstance().resetShopcartItemFromDB(null);
                                    mPaymentInfo.setSplit(false);
                                    if (customer != null) {
                                        CustomerManager.getInstance().setDinnerLoginCustomer(customer);
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "", e);
                                }
                            }
                        })
                        .build()
                        .show(context.getSupportFragmentManager(), "backhome");

            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    }

        private void showInvoiceQrcodeDialog(TradeVo tradeVo, ElectronicInvoiceVo electronicInvoiceVo, BigDecimal actualAmount) {
        InvoiceFragment.Callback callback = new InvoiceFragment.Callback() {
            @Override
            public void callback(boolean flag, InvoiceQrcodeReq invoiceQrcodeReq, TradeVo tradeVo) {
                if (flag) {                     performInvoiceQrcode(invoiceQrcodeReq, tradeVo);
                } else {                    doFinish();                }
            }
        };
        InvoiceFragment.show(mContext.getSupportFragmentManager(), tradeVo, electronicInvoiceVo, actualAmount, callback);
    }

    private void performInvoiceQrcode(InvoiceQrcodeReq req, final TradeVo tradeVo) {
        TradeOperates tradeOperates = BusinessTypeUtils.isRetail() ? OperatesRetailFactory.create(TradeOperates.class) : OperatesFactory.create(TradeOperates.class);
        tradeOperates.invoiceQrcode(req, LoadingResponseListener.ensure(new ResponseListener<GatewayTransferResp<InvoiceQrcodeResp>>() {
            @Override
            public void onResponse(ResponseObject<GatewayTransferResp<InvoiceQrcodeResp>> response) {
                if (ResponseObject.isOk(response)) {
                    if (response.getContent() != null && !TextUtils.isEmpty(response.getContent().getMessage())) {
                        ToastUtil.showShortToast(response.getContent().getMessage());
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
                } else {
                    ToastUtil.showShortToast(response.getMessage());
                }
                                doFinish();
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
                                doFinish();
            }
        }, mContext.getSupportFragmentManager()));
    }

        private void performFHInvoiceQrcode(final TradeVo tradeVo) {
        FHInvoiceQrcodeReq req = new FHInvoiceQrcodeReq();
        req.deviceIdenty = ShopInfoCfg.getInstance().deviceID;
        req.shopId = ShopInfoCfg.getInstance().shopId == null ? 0 : Long.parseLong(ShopInfoCfg.getInstance().shopId);
        req.tradeId = tradeVo.getTrade().getId();
        req.tradeNo = tradeVo.getTrade().getTradeNo();

        TradeOperates tradeOperates = BusinessTypeUtils.isRetail() ? OperatesRetailFactory.create(TradeOperates.class) : OperatesFactory.create(TradeOperates.class);
        tradeOperates.invoiceFHQrcode(req, new ResponseListener<GatewayTransferResp<FHInvoiceQrcodeResp>>() {
            @Override
            public void onResponse(ResponseObject<GatewayTransferResp<FHInvoiceQrcodeResp>> response) {
                if (ResponseObject.isOk(response)) {
                    if (response.getContent().isOk()) {
                        String url = response.getContent().getResult().getQrCode();
                        String tradeNo = tradeVo.getTrade().getTradeNo();
                        String deskNo;
                        if (Utils.isNotEmpty(tradeVo.getTradeTableList())) {
                            deskNo = tradeVo.getTradeTableList().get(0).getTableName();
                        } else if (tradeVo.getTradeExtra() != null && !TextUtils.isEmpty(tradeVo.getTradeExtra().getNumberPlate())) {
                            deskNo = tradeVo.getTradeExtra().getNumberPlate();
                        } else {
                            deskNo = tradeVo.getTradeExtra() == null ? "" : tradeVo.getTradeExtra().getSerialNumber();
                        }
                        String amount = String.valueOf(mPaymentInfo.getPaidAmount());
                                            }
                } else {
                    ToastUtil.showShortToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        });
    }

        private void doJinChengBusiness(Trade trade) {
        boolean isJinCh = ServerSettingCache.getInstance().isJinChBusiness();
        if (isJinCh && isFinishPay) {
                        if (mPaymentInfo.getTradeBusinessType() == BusinessType.CARD
                    || mPaymentInfo.getTradeBusinessType() == BusinessType.ENTITY_CARD_CHANGE
                    || mPaymentInfo.getTradeBusinessType() == BusinessType.ANONYMOUS_ENTITY_CARD_RECHARGE) {
                EventBus.getDefault().post(new EventPayResult(true, mPaymentInfo.getTradeBusinessType()));
            }
        } else {
            if (isFinishPay && mPaymentInfo.getTradeBusinessType() == BusinessType.ENTITY_CARD_CHANGE) {
                EventBus.getDefault().post(new EventPayResult(true, BusinessType.ENTITY_CARD_CHANGE));
            }
        }
    }

        private void deletePosTransLogAsync(IPaymentInfo paymentInfo) {
        if (paymentInfo.getOtherPay().isContainsPayModel(PayModeId.POS_CARD)) {
            PayModelItem posItem = paymentInfo.getOtherPay().getPayModelItemByPayModeId(PayModeId.POS_CARD);

            if (posItem != null) {
                NewLiandiposManager.getInstance().deletePosTransLogAsync(posItem.getUuid());
            }
        }
    }

        private void printKitchenAfterPay(TradeVo tradeVo) {
        if (SpHelper.getDefault().getBoolean(Constant.PRINT_KITCHEN_AFTER_PAY, false)) {
                    }
    }

    private void exitLoginRemind() {
        if (isFinishPay) {
            BusinessType tradeBusinessType = mPaymentInfo.getTradeBusinessType();
            switch (tradeBusinessType) {
                case DINNER:
                case BUFFET:
                case GROUP:
                    boolean isExitLogin = SpHelper.getDefault().getBoolean(SettingConstant.DINNER_EXIT_SETTLE_SWITCH);
                    if (isExitLogin) {
                        EventBus.getDefault().post(new ActionExitLoginRemind());
                    }
                    break;
            }
        }
    }

            private void doPrintPayTick() {
        if (!isPrinted) {
            isPrinted = true;
            if (mDoPayApi != null)
                mDoPayApi.doPrint(mPaymentInfo, mPaymentInfo.getTradeVo().getTrade().getUuid(), false, false, false, true);
        }
    }

    private void startGetTaxNo(long delayMillis) {
        startTimeMills = System.currentTimeMillis();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                viewHolder.showGetingTaxNo();
            }
        };
        getTaxNoByNet();
        mHandler.postDelayed(runnable, delayMillis);
    }

    private void showGetTaxNoFailed() {
        viewHolder.showGetTaxNoFail();
    }

    private void checkGetTaxNoTimeOut() {
        if (System.currentTimeMillis() - startTimeMills >= WAITE_TIME_LIMIT) {
            showGetTaxNoFailed();
        } else {
            getTaxNoByNet();
        }
    }

    private void getTaxNoByNet() {
        TradeOperates tradeOperates = BusinessTypeUtils.isRetail() ? OperatesRetailFactory.create(TradeOperates.class) : OperatesFactory.create(TradeOperates.class);
        GetTaxNoReq req = new GetTaxNoReq();
        req.tradeId = mPaymentInfo.getTradeVo().getTrade().getId();
        tradeOperates.queryInvoiceNo(req, getTaxNoListenner);
    }

    ResponseListener getTaxNoListenner = new ResponseListener<GetTaxNoResp>() {
        @Override
        public void onResponse(ResponseObject<GetTaxNoResp> response) {
            if (ResponseObject.isOk(response) && response.getContent() != null && response.getContent().getCode() != null && response.getContent().getCode().getCode() != null) {
                doPrintPayTick();
                delayDismiss(50);
            } else {
                checkGetTaxNoTimeOut();
            }
        }

        @Override
        public void onError(VolleyError error) {
            checkGetTaxNoTimeOut();
        }
    };

        private static class TaxNoViewHolder {
        Context mContext;

        public TaxNoViewHolder(Context context) {
            mContext = context;
        }

        LinearLayout payOkLL;
        LinearLayout getTaxLL;
        LinearLayout buttonLL;
        ImageView closeIV;
        ImageView getTaxIcon;
        TextView alterTV;
        Button reTryBT;
        Button printBT;

        public void showGetTaxNoFail() {
            payOkLL.setVisibility(View.GONE);
            getTaxLL.setVisibility(View.VISIBLE);
            buttonLL.setVisibility(View.VISIBLE);
            if (getTaxIcon != null) {
                getTaxIcon.setImageResource(R.drawable.pay_get_invoice_no_error);
            }
            if (alterTV != null) {
                alterTV.setText(mContext.getText(R.string.get_invoiceno_fail_text));
            }
        }

        public void showGetingTaxNo() {
            payOkLL.setVisibility(View.GONE);
            buttonLL.setVisibility(View.GONE);
            getTaxLL.setVisibility(View.VISIBLE);
            if (getTaxIcon != null) {
                getTaxIcon.setImageResource(R.drawable.pay_get_invoice_no_doing);
            }
            if (alterTV != null) {
                alterTV.setText(mContext.getText(R.string.get_invoiceno_doing_text));
            }
        }
    }
    }
