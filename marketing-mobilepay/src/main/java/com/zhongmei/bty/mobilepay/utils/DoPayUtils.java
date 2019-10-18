package com.zhongmei.bty.mobilepay.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;

import com.zhongmei.bty.mobilepay.IPayConstParame;
import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.dialog.GetTaxNoPayOkDialog;
import com.zhongmei.bty.mobilepay.dialog.PayErrorDialog;
import com.zhongmei.bty.mobilepay.dialog.PayOkDialog;
import com.zhongmei.bty.mobilepay.event.ExemptEventUpdate;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.constants.IntentNo;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.greenrobot.event.EventBus;


public class DoPayUtils {
    private static final String TAG = DoPayUtils.class.getSimpleName();

    public static boolean isSupportGroupPay(IPaymentInfo paymentInfo, boolean isSetToSupportGroupPay) {
        if (ServerSettingCache.getInstance().isJinChBusiness()) {
            return false;
        }
        if (paymentInfo != null) {
            if (paymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT || paymentInfo.getPayScene() == PayScene.SCENE_CODE_BAKERY_BOOKING || paymentInfo.getPayScene() == PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT) {
                return false;
            } else {
                switch (paymentInfo.getTradeBusinessType()) {
                    case GROUP:
                        if (Utils.isNotEmpty(paymentInfo.getTradeVo().getTradeEarnestMoneys())) {
                            return true;
                        } else {
                            return false;
                        }

                    default:
                        return paymentInfo.isGroupPay() && isSetToSupportGroupPay;
                }
            }
        }
        return isSetToSupportGroupPay;
    }

    public static void updatePayEnable(Context context, Button payButton, Boolean isEnabled) {
        if (context == null) {
            return;
        }
        if (isEnabled) {
            payButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.pay_erase_selected_noborder_bg));
            if (payButton.isEnabled() == false) {
                payButton.setEnabled(true);
            }

        } else {
            payButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.pay_dopay_btn_bg));
            if (payButton.isEnabled() == true) {
                payButton.setEnabled(false);
            }
        }
    }

    public static double formatInputCash(String inputValue) {
        double value = 0;
        while (!TextUtils.isEmpty(inputValue) && !Character.isDigit(inputValue.charAt(0))) {
            inputValue = inputValue.substring(1);
        }
        if (!TextUtils.isEmpty(inputValue)) {
            try {
                value = Double.valueOf(inputValue);
            } catch (NumberFormatException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return value;
    }

    public static boolean isTradePaidOver(Trade trade) {
        if (trade == null || trade.getBusinessType() == null || trade.getTradePayStatus() == null) {
            return false;
        }
        switch (trade.getBusinessType()) {
            case BUFFET:
                return (trade.getTradePayStatus() == TradePayStatus.PREPAID || trade.getTradeStatus() == TradeStatus.FINISH);
            default:
                return trade.getTradeStatus() == TradeStatus.FINISH;
        }
    }

    public static boolean isHaveTradeTax(TradeVo tradeVo) {
        boolean isHaveTradeTax = false;
        if (tradeVo != null) {
            List<TradeTax> taxList = tradeVo.getTradeTaxs();
            if (taxList != null && taxList.size() > 0) {
                for (TradeTax tradeTax : taxList) {
                    if (tradeTax.isValid())
                        return true;
                }
            }
        }
        return isHaveTradeTax;
    }

    public static BigDecimal getMaxInvoiceAmount(TradeVo tradeVo, List<PaymentItem> paymentItems) {
        BigDecimal paidAmount = BigDecimal.ZERO;
        if (paymentItems != null) {
            for (PaymentItem item : paymentItems) {
                if (item.getPayStatus() == TradePayStatus.PAID && item.getStatusFlag() == StatusFlag.VALID) {
                    if (item.getFaceAmount() != null) {
                        paidAmount = paidAmount.add(item.getFaceAmount());
                        if (item.getChangeAmount() != null) {
                            paidAmount = paidAmount.subtract(item.getChangeAmount());
                        }
                    }
                }
            }
        }
        if (tradeVo != null && tradeVo.getTradeDeposit() != null && tradeVo.getTradeDeposit().getDepositPay() != null) {
            paidAmount = paidAmount.subtract(tradeVo.getTradeDeposit().getDepositPay());
        }
        return paidAmount;
    }

    public static void findSourceTradePaymentVos(final IPaymentInfo paymentInfo) {
        if (paymentInfo.getSourceTradeVo() != null) {
            Trade trade = paymentInfo.getSourceTradeVo().getTrade();
            if (trade != null && trade.getId() != null) {
                findTradePayments(paymentInfo, trade);
            }
        }
    }

    private static void findTradePayments(final IPaymentInfo paymentInfo, Trade trade) {
        if (paymentInfo != null && trade.getUuid() != null) {
            TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
            List<PaymentVo> list = null;
            try {
                list = tradeDal.listPayment(trade.getUuid());
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
            paymentInfo.setPaidPaymentRecords(list);
        }
    }

    public static List<String> getTradeDishIds(TradeVo tradeVo) {
        Set<String> dishIdSet = new HashSet<String>();
        if (tradeVo != null && tradeVo.getTradeItemList() != null) {
            TradeItem tradeItem = null;
            for (TradeItemVo itemVo : tradeVo.getTradeItemList()) {
                tradeItem = itemVo.getTradeItem();
                if (tradeItem != null && tradeItem.isValid() && TextUtils.isEmpty(tradeItem.getParentUuid())) {
                    dishIdSet.add(tradeItem.getSkuUuid());
                }
            }
        }
        return new ArrayList<String>(dishIdSet);
    }

    public static void findPaymentVoAsync(final IPaymentInfo paymentInfo) {
        findPaymentVoAsync(paymentInfo, false);
    }

    public static void findPaymentVoAsync(final IPaymentInfo paymentInfo, final boolean isPostUpdateInput) {
        final Trade trade = paymentInfo.getTradeVo().getTrade();
        if (trade != null && trade.getId() != null) {
            final TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
            new AsyncTask<Void, Void, List<PaymentVo>>() {
                @Override
                protected List<PaymentVo> doInBackground(Void... params) {
                    List<PaymentVo> list = null;
                    try {
                        list = tradeDal.listPayment(trade.getUuid());
                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                    }
                    return list;
                }

                protected void onPostExecute(List<PaymentVo> data) {
                    paymentInfo.setPaidPaymentRecords(data);
                    if (isPostUpdateInput)
                        EventBus.getDefault().post(new ExemptEventUpdate(paymentInfo.getEraseType()));
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }
    }

    public static boolean checkPayModeSettings(final Context context) {
        if (!PaySettingCache.isContainsPayModes()) {

            ToastUtil.showShortToast(context.getString(R.string.pay_no_paytype_set));
            return false;
        }
        return true;
    }

    public static void gotoPayActivity(final Activity context, DoPayApi doPayApi, final TradeVo tradeVo, boolean isOrderCenter) {
        gotoPayActivity(context, doPayApi, tradeVo, isOrderCenter, PayScene.SCENE_CODE_SHOP, -1, -1);
    }

    public static void gotoPayActivity(final Activity context, DoPayApi doPayApi, final TradeVo tradeVo, boolean isOrderCenter, int defaultPayMenuType) {
        gotoPayActivity(context, doPayApi, tradeVo, isOrderCenter, PayScene.SCENE_CODE_SHOP, defaultPayMenuType, -1);
    }

    public static void gotoPayActivity(final Activity context, DoPayApi doPayApi, final TradeVo tradeVo, boolean isOrderCenter, PayScene payScene) {
        gotoPayActivity(context, doPayApi, tradeVo, isOrderCenter, payScene, -1, -1);
    }

    public static void gotoPayActivity(final Activity context, DoPayApi doPayApi, final TradeVo tradeVo, int defaultPayMenuType, int quickPayType) {
        gotoPayActivity(context, doPayApi, tradeVo, false, PayScene.SCENE_CODE_SHOP, defaultPayMenuType, quickPayType);
    }

    public static void gotoPayActivity(final Activity context, DoPayApi doPayApi, final TradeVo tradeVo, boolean isOrderCenter, PayScene payScene, int defaultPayMenuType, int quickPayType) {
        gotoPayActivity(context, doPayApi, tradeVo, isOrderCenter, payScene, defaultPayMenuType, quickPayType, true);
    }

    public static void gotoPayActivity(final Activity context, DoPayApi doPayApi, final TradeVo tradeVo, boolean isOrderCenter, PayScene payScene, int defaultPayMenuType, int quickPayType, boolean isGroupPay) {
        gotoPayActivity(context, doPayApi, tradeVo, isOrderCenter, payScene, defaultPayMenuType, quickPayType, isGroupPay, 0);
    }

    public static void gotoPayActivity(final Activity context, DoPayApi doPayApi, final TradeVo tradeVo, boolean isOrderCenter, PayScene payScene, int defaultPayMenuType, int quickPayType, boolean isGroupPay, int menuDisplayType) {
        if (!checkPayModeSettings(context)) {
            return;
        }
        Uri uri;
        if (payScene == PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT) {
            uri = Uri.parse("clam://pay.justpay.deposit.view");
        } else {
            uri = Uri.parse("clam://pay.justpay.view");
        }

        Intent intent = new Intent();
        intent.setData(uri);
        intent.putExtra("payScene", payScene);
        intent.putExtra("tradeVo", tradeVo);
        if (doPayApi != null)
            intent.putExtra("doPayApi", doPayApi.getClass().getSimpleName());
        intent.putExtra("isOrderCenter", isOrderCenter);
        intent.putExtra("paymenutype", defaultPayMenuType);
        intent.putExtra("isGroupPay", isGroupPay);
        intent.putExtra(IPayConstParame.EXTRA_QUICK_PAY_TYPE, quickPayType);
        intent.putExtra(IPayConstParame.EXTRA_MENU_DISPLAY_TYPE, menuDisplayType);
        context.startActivityForResult(intent, IntentNo.ORDER_PAY);
    }

    public static void showPayOkDialog(FragmentActivity context, DoPayApi doPayApi, IPaymentInfo paymentInfo, boolean isAsync, int operateType) {
        try {
            if (context != null && !context.isDestroyed()) {
                PayOkDialog dialog = new PayOkDialog(context, paymentInfo, isAsync, operateType);
                dialog.setDoPayApi(doPayApi);
                dialog.show();
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    public static void showGetTaxNoPayOkDialog(FragmentActivity context, DoPayApi doPayApi, IPaymentInfo paymentInfo, int operateType) {
        try {
            if (context != null && !context.isDestroyed()) {
                GetTaxNoPayOkDialog dialog = new GetTaxNoPayOkDialog(context, doPayApi, paymentInfo, operateType);
                dialog.show();
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    public static void showPayErrorDialog(FragmentActivity context, DoPayApi doPayApi, IPaymentInfo paymentInfo, String errorReason, IPayOverCallback callBack, int errorCode) {
        try {
            if (context != null && !context.isDestroyed()) {
                PayErrorDialog dialog = new PayErrorDialog(context, doPayApi, paymentInfo, errorReason, callBack, errorCode);
                dialog.show();
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }
}
