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

/**
 * Created by demo on 2018/12/15
 * 支付工具
 */

public class DoPayUtils {
    private static final String TAG = DoPayUtils.class.getSimpleName();

    //判断是否支持组合支付工具
    public static boolean isSupportGroupPay(IPaymentInfo paymentInfo, boolean isSetToSupportGroupPay) {
        //如果是金诚商户 不支持分步支付
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
                        return paymentInfo.isGroupPay() && isSetToSupportGroupPay;//modify v8.9
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

    // 判断订单是否支付完成
    public static boolean isTradePaidOver(Trade trade) {
        if (trade == null || trade.getBusinessType() == null || trade.getTradePayStatus() == null) {
            return false;
        }
        switch (trade.getBusinessType()) {
            case BUFFET://自助预结状态为支付完成
                return (trade.getTradePayStatus() == TradePayStatus.PREPAID || trade.getTradeStatus() == TradeStatus.FINISH);
            default:
                return trade.getTradeStatus() == TradeStatus.FINISH;
        }
    }

    //判断订单是否有消费税
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

    // add  20180523 发票最大金额 ： 实付-押金
    public static BigDecimal getMaxInvoiceAmount(TradeVo tradeVo, List<PaymentItem> paymentItems) {
        BigDecimal paidAmount = BigDecimal.ZERO;
        //计算用户实付：sum（面值- 找零）
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
        //发票最大金额 ： 实付-押金
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
            // 通知ui更新
            paymentInfo.setPaidPaymentRecords(list);
        }
    }

    //生产订单菜品UUid（不包含套餐子菜）
    public static List<String> getTradeDishIds(TradeVo tradeVo) {
        Set<String> dishIdSet = new HashSet<String>();
        if (tradeVo != null && tradeVo.getTradeItemList() != null) {
            TradeItem tradeItem = null;
            for (TradeItemVo itemVo : tradeVo.getTradeItemList()) {
                tradeItem = itemVo.getTradeItem();
                //modify v8.5不包含套餐子菜
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
                    // 通知ui更新
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

    //跳转到快餐支付界面
    public static void gotoPayActivity(final Activity context, DoPayApi doPayApi, final TradeVo tradeVo, boolean isOrderCenter) {
        gotoPayActivity(context, doPayApi, tradeVo, isOrderCenter, PayScene.SCENE_CODE_SHOP, -1, -1);//默认是消费付款场景
    }

    //跳转到快餐支付界面
    public static void gotoPayActivity(final Activity context, DoPayApi doPayApi, final TradeVo tradeVo, boolean isOrderCenter, int defaultPayMenuType) {
        gotoPayActivity(context, doPayApi, tradeVo, isOrderCenter, PayScene.SCENE_CODE_SHOP, defaultPayMenuType, -1);//默认是消费付款场景
    }

    //跳转到快餐支付界面
    public static void gotoPayActivity(final Activity context, DoPayApi doPayApi, final TradeVo tradeVo, boolean isOrderCenter, PayScene payScene) {
        gotoPayActivity(context, doPayApi, tradeVo, isOrderCenter, payScene, -1, -1);//默认是消费付款场景
    }

    // 跳转到快捷支付界面
    public static void gotoPayActivity(final Activity context, DoPayApi doPayApi, final TradeVo tradeVo, int defaultPayMenuType, int quickPayType) {
        gotoPayActivity(context, doPayApi, tradeVo, false, PayScene.SCENE_CODE_SHOP, defaultPayMenuType, quickPayType);
    }

    //跳转到快餐支付界面
    public static void gotoPayActivity(final Activity context, DoPayApi doPayApi, final TradeVo tradeVo, boolean isOrderCenter, PayScene payScene, int defaultPayMenuType, int quickPayType) {
        gotoPayActivity(context, doPayApi, tradeVo, isOrderCenter, payScene, defaultPayMenuType, quickPayType, true);
    }

    //跳转到快餐支付界面
    public static void gotoPayActivity(final Activity context, DoPayApi doPayApi, final TradeVo tradeVo, boolean isOrderCenter, PayScene payScene, int defaultPayMenuType, int quickPayType, boolean isGroupPay) {
        gotoPayActivity(context, doPayApi, tradeVo, isOrderCenter, payScene, defaultPayMenuType, quickPayType, isGroupPay, 0);
    }

    //跳转到快餐支付界面
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
        // PayActivity.setTradeVo(tradeVo);//modify 20170327 快餐不用购物车对象，通过反序列化生成
        intent.putExtra("payScene", payScene);//add 20170704
        intent.putExtra("tradeVo", tradeVo);
        if (doPayApi != null)
            intent.putExtra("doPayApi", doPayApi.getClass().getSimpleName());//add v8.14
        intent.putExtra("isOrderCenter", isOrderCenter);
        intent.putExtra("paymenutype", defaultPayMenuType);
        intent.putExtra("isGroupPay", isGroupPay);
        intent.putExtra(IPayConstParame.EXTRA_QUICK_PAY_TYPE, quickPayType);
        // v8.15.0 雅座储值支付显示参数
        intent.putExtra(IPayConstParame.EXTRA_MENU_DISPLAY_TYPE, menuDisplayType);
        context.startActivityForResult(intent, IntentNo.ORDER_PAY);
    }

    //显示支付成功界面
    public static void showPayOkDialog(FragmentActivity context, DoPayApi doPayApi, IPaymentInfo paymentInfo, boolean isAsync, int operateType) {
        try {
            if (context != null && !context.isDestroyed()) {
                PayOkDialog dialog = new PayOkDialog(context, paymentInfo, isAsync, operateType);
                dialog.setDoPayApi(doPayApi);//add 8.10 临时方案
                dialog.show();
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    //显示支付成功查询税号界面
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

    //显示支付失败界面
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
