package com.zhongmei.bty.pay.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.zhongmei.beauty.entity.UserVo;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsVo;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.bty.basemodule.shoppingcart.BaseShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.SeparateShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.message.TradeUnbindCouponReq;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterOperateDialogFragment;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCart;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.data.operates.impl.TradeOperatesImpl;
import com.zhongmei.bty.mobilepay.IPayConstParame;
import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.bean.PayModelItem;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.utils.DoPayUtils;
import com.zhongmei.bty.pay.manager.DoPayManager;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.ThreadUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeScenceType;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.util.ToastUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



public class PayUtils {
    private static final String TAG = PayUtils.class.getSimpleName();

        public static boolean isNegativeTradeAmount(TradeVo tradeVo) {
        boolean result = false;
        if (tradeVo != null && tradeVo.getTrade() != null && tradeVo.getTrade().getTradeAmount() != null) {
            if (tradeVo.getTrade().getTradeAmount().compareTo(BigDecimal.ZERO) < 0) {
                result = true;
            }
        }
        return result;
    }

        public static boolean isConditionAsync(TradeVo tradeVo) {
        if (tradeVo == null) {
            return false;
        }
                if (tradeVo.getTrade() != null && tradeVo.getTrade().getTradeType() == TradeType.SPLIT) {
            return false;
        }
        List<TradeCustomer> customerList = tradeVo.getTradeCustomerList();
                if (customerList != null && customerList.size() > 0) {
            for (TradeCustomer customer : customerList) {
                if ((CustomerType.MEMBER == customer.getCustomerType() || CustomerType.CUSTOMER == customer.getCustomerType() || CustomerType.CARD == customer.getCustomerType())
                        && customer.getStatusFlag() == StatusFlag.VALID) {
                    return false;
                }
            }
        }
        List<WeiXinCouponsVo> weiXinCouponsVoList = tradeVo.getmWeiXinCouponsVo();
                if (weiXinCouponsVoList != null && weiXinCouponsVoList.size() > 0) {
            for (WeiXinCouponsVo wxcVo : weiXinCouponsVoList) {
                if (wxcVo.getmTradePrivilege() != null && wxcVo.getmTradePrivilege().getStatusFlag() == StatusFlag.VALID) {
                    return false;
                }
            }
        }
        List<TradeItemPlanActivity> planActivityList = tradeVo.getTradeItemPlanActivityList();
        if (planActivityList != null && planActivityList.size() > 0) {
            for (TradeItemPlanActivity planActivity : planActivityList) {
                if (planActivity.getStatusFlag() == StatusFlag.VALID) {
                    return false;
                }
            }
        }
        return true;
    }

        public static boolean isOnlyCashPayment(PaymentVo vo) {
        if (vo != null && vo.getPaymentItemList() != null && vo.getPaymentItemList().size() == 1) {
            Long modeId = vo.getPaymentItemList().get(0).getPayModeId();
            return PayModeId.CASH.value().equals(modeId);
        } else {
            return false;
        }
    }

    public static void findPaymentVos(final IPaymentInfo paymentInfo) {
        Trade trade = paymentInfo.getTradeVo().getTrade();
        if (trade != null && trade.getId() != null) {
            findTradePayments(paymentInfo, trade);
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


        public static void showPayOkDialog(FragmentActivity context, IPaymentInfo paymentInfo, boolean isAsync) {
        try {
                        int opType = IPayConstParame.OP_TYPE_DOPAY;            if (paymentInfo.getOtherPay().getAllPayModelItems() != null && !paymentInfo.getOtherPay().getAllPayModelItems().isEmpty()) {
                PayModelItem payModelItem = paymentInfo.getOtherPay().getAllPayModelItems().get(0);
                if (payModelItem != null && payModelItem.getPayMode() == PayModeId.EARNEST_DEDUCT) {
                    if (paymentInfo.getTradeVo().getTradeEarnestMoney() > (paymentInfo.getTradeAmount() - paymentInfo.getExemptAmount())) {
                        opType = IPayConstParame.OP_TYPE_DEDUCTION_REFUND;                    } else {
                        opType = IPayConstParame.OP_TYPE_DEDUCTION;                    }
                }
            }
                                    if (DoPayUtils.isHaveTradeTax(paymentInfo.getTradeVo()) && DoPayUtils.isTradePaidOver(paymentInfo.getTradeVo().getTrade())) {
                DoPayUtils.showGetTaxNoPayOkDialog(context, DoPayManager.getInstance(), paymentInfo, opType);
            } else {
                DoPayUtils.showPayOkDialog(context, DoPayManager.getInstance(), paymentInfo, isAsync, opType);
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

        public static void showPayOkDialog(FragmentActivity context, IPaymentInfo paymentInfo, boolean isAsync, int operateType) {
        try {
                        if (DoPayUtils.isHaveTradeTax(paymentInfo.getTradeVo()) && DoPayUtils.isTradePaidOver(paymentInfo.getTradeVo().getTrade())) {
                DoPayUtils.showGetTaxNoPayOkDialog(context, DoPayManager.getInstance(), paymentInfo, operateType);
            } else {                DoPayUtils.showPayOkDialog(context, DoPayManager.getInstance(), paymentInfo, isAsync, operateType);
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    public static void showPayOkDialog(FragmentActivity context, IPaymentInfo paymentInfo) {
        showPayOkDialog(context, paymentInfo, false);
    }

        public static void showPayErrorDialog(FragmentActivity context, IPaymentInfo paymentInfo, String errorReason, IPayOverCallback callBack, int errorCode) {
        DoPayUtils.showPayErrorDialog(context, DoPayManager.getInstance(), paymentInfo, errorReason, callBack, errorCode);
    }

    public static void showPayErrorDialog(FragmentActivity context, IPaymentInfo paymentInfo, String errorReason, IPayOverCallback callBack) {
        showPayErrorDialog(context, paymentInfo, errorReason, callBack, 0);
    }


    public static void showRemoveCouponOrDelTradeDialog(final FragmentActivity context, final IPaymentInfo paymentInfo, String title, final List<Long> promoIdList) {
        try {
            CommonDialogFragment.CommonDialogFragmentBuilder dialogBuilder = new CommonDialogFragment.CommonDialogFragmentBuilder(context).title(title)
                    .iconType(CommonDialogFragment.ICON_WARNING)
                    .positiveText(R.string.pay_unbind_coupon)
                    .positiveLinstner(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            TradeOperates tradeOperate = OperatesFactory.create(TradeOperates.class);
                            Long tradeId = paymentInfo.getTradeVo().getTrade().getId();
                            List<TradePrivilege> tradePrivilegeList = BaseShoppingCart.getTradePrivilegeByPromeIds(promoIdList, paymentInfo.getTradeVo());
                            if (Utils.isEmpty(tradePrivilegeList)) {
                                return;
                            }
                            tradeOperate.batchunbindCoupon(tradeId, tradePrivilegeList, LoadingResponseListener.ensure(new ResponseListener<TradeResp>() {

                                @Override
                                public void onResponse(ResponseObject<TradeResp> response) {
                                    try {
                                        if (ResponseObject.isOk(response)) {
                                            TradeDal mTradeDal = OperatesFactory.create(TradeDal.class);
                                                                                        String tradeUuid = paymentInfo.getTradeVo().getTrade().getUuid();
                                                                                        TradeVo tradeVo = mTradeDal.findTrade(tradeUuid);
                                            paymentInfo.setTradeVo(tradeVo);
                                            if (paymentInfo.isDinner() && !paymentInfo.isOrderCenter()) {                                                 SeparateShoppingCart.getInstance().resetShopcartItemFromDB(null);
                                                DinnerShoppingCart.getInstance().resetShopcartItemFromDB(null);
                                            }
                                            paymentInfo.setOrdered(true);
                                        } else {
                                            showErrorDialog(context, context.getSupportFragmentManager(), response.getMessage());
                                        }
                                    } catch (Exception e) {
                                        Log.e(TAG, "", e);
                                    }
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    try {
                                        ToastUtil.showShortToast(error.getMessage());

                                        showErrorDialog(context, context.getSupportFragmentManager(), error.getMessage());
                                    } catch (Exception e) {
                                        Log.e(TAG, "", e);
                                    }
                                }

                            }, context.getSupportFragmentManager()));
                        }
                    })
                    .negativeText(R.string.pay_delete_trade)
                    .negativeLisnter(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            OrderCenterOperateDialogFragment dialog = new OrderCenterOperateDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("type", ReasonType.TRADE_INVALID.value());
                            dialog.setArguments(bundle);
                            dialog.registerListener(new OrderCenterOperateDialogFragment.OperateListener() {
                                @Override
                                public boolean onSuccess(OrderCenterOperateDialogFragment.OperateResult result) {
                                    recisionTradeRequest(context, result.reason, paymentInfo.getTradeVo(), result.isPrintChecked);
                                    return true;
                                }
                            });
                            dialog.show(context.getSupportFragmentManager(), "destroy");
                        }
                    });

            dialogBuilder.build().show(context.getSupportFragmentManager(), "RemoveCouponDialog2");

        } catch (Exception e) {

            Log.e(TAG, "", e);
        }
    }


    public static void recisionTradeRequest(final FragmentActivity context, Reason reason, final TradeVo tradeVo, final boolean isPrintKitchen) {
        TradeOperates tradeOperate = OperatesFactory.create(TradeOperates.class);
                tradeOperate.recision(tradeVo, reason, null,
                LoadingResponseListener.ensure(new ResponseListener<TradeResp>() {

                                                   @Override
                                                   public void onResponse(ResponseObject<TradeResp> response) {
                                                       try {
                                                           ToastUtil.showLongToast(response.getMessage());

                                                           if (ResponseObject.isOk(response)) {
                                                               Trade trade = tradeVo.getTrade();
                                                               if (trade.getBusinessType() == BusinessType.SNACK && trade.getTradePayStatus() == TradePayStatus.UNPAID && trade.getSource() == SourceId.POS && trade.getDeliveryType() == DeliveryType.HERE) {

                                                                                                                                  } else if (trade.getBusinessType() == BusinessType.SNACK && trade.getTradeType() == TradeType.SELL_FOR_REPEAT) {
                                                                                                                                  } else {
                                                                                                                                                                                                     }

                                                           } else {

                                                               showErrorDialog(context, context.getSupportFragmentManager(), response.getMessage());
                                                           }
                                                       } catch (Exception e) {
                                                           Log.e(TAG, "", e);
                                                       }
                                                   }

                                                   @Override
                                                   public void onError(VolleyError error) {
                                                       try {
                                                           ToastUtil.showLongToast(error.getMessage());

                                                           showErrorDialog(context, context.getSupportFragmentManager(), error.getMessage());

                                                       } catch (Exception e) {
                                                           Log.e(TAG, "", e);
                                                       }
                                                   }
                                               },
                        context.getSupportFragmentManager()));
    }


    public static void showErrorDialog(Context context, final FragmentManager fragmentManager, String errortitle) {
        try {
            new CommonDialogFragment.CommonDialogFragmentBuilder(context).title(errortitle)
                    .iconType(CommonDialogFragment.ICON_WARNING)
                    .positiveText(R.string.know)
                    .positiveLinstner(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {

                        }
                    })
                    .build()
                    .show(fragmentManager, "showErrorDialog");

        } catch (Exception e) {

            Log.e(TAG, "", e);
        }
    }


    public static List<TradePrivilege> getWeiXinCouponsPrivileges(IPaymentInfo paymentInfo, List<Long> promoIds) {
        List<TradePrivilege> weiXinCouponsPrivileges = new ArrayList<TradePrivilege>();

        for (WeiXinCouponsVo weiXinCouponsVo : paymentInfo.getTradeVo().getmWeiXinCouponsVo()) {
            if (promoIds.contains(weiXinCouponsVo.getmTradePrivilege().getPromoId())) {
                weiXinCouponsPrivileges.add(weiXinCouponsVo.getmTradePrivilege());
            }
        }
        return weiXinCouponsPrivileges;
    }

        public static void showRemoveWeiXinCouponsOrDelTradeDialog(final FragmentActivity context, final IPaymentInfo paymentInfo, String title, List<Long> promoIds) {

        try {
            final List<TradePrivilege> tradePrivileges = PayUtils.getWeiXinCouponsPrivileges(paymentInfo, promoIds);
            CommonDialogFragment.CommonDialogFragmentBuilder dialogBuilder = new CommonDialogFragment.CommonDialogFragmentBuilder(context).title(title)
                    .iconType(CommonDialogFragment.ICON_WARNING)
                    .positiveText(R.string.pay_unbind_Wechat_coupon)
                    .positiveLinstner(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            if (Utils.isEmpty(tradePrivileges)) {
                                return;
                            }

                            TradeOperates tradeOperate = OperatesFactory.create(TradeOperates.class);
                                                        tradeOperate.batchunbindCoupon(paymentInfo.getTradeVo().getTrade().getId(), tradePrivileges,
                                    LoadingResponseListener.ensure(new ResponseListener<TradeResp>() {

                                        @Override
                                        public void onResponse(ResponseObject<TradeResp> response) {
                                            try {
                                                if (ResponseObject.isOk(response)) {
                                                    TradeDal mTradeDal = OperatesFactory.create(TradeDal.class);
                                                                                                        String tradeUuid = paymentInfo.getTradeVo().getTrade().getUuid();
                                                                                                        TradeVo tradeVo = mTradeDal.findTrade(tradeUuid);
                                                    paymentInfo.setTradeVo(tradeVo);
                                                    if (paymentInfo.isDinner() && !paymentInfo.isOrderCenter()) {                                                         SeparateShoppingCart.getInstance().resetShopcartItemFromDB(null);
                                                        DinnerShoppingCart.getInstance().resetShopcartItemFromDB(null);
                                                    }
                                                    paymentInfo.setOrdered(true);
                                                } else {
                                                    showErrorDialog(context, context.getSupportFragmentManager(), response.getMessage());
                                                }
                                            } catch (Exception e) {
                                                Log.e(TAG, "", e);
                                            }
                                        }

                                        @Override
                                        public void onError(VolleyError error) {
                                            try {
                                                ToastUtil.showShortToast(error.getMessage());

                                                showErrorDialog(context, context.getSupportFragmentManager(), error.getMessage());
                                            } catch (Exception e) {
                                                Log.e(TAG, "", e);
                                            }
                                        }

                                    }, context.getSupportFragmentManager()));
                        }
                    })
                    .negativeText(R.string.pay_delete_trade)
                    .negativeLisnter(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            OrderCenterOperateDialogFragment dialog = new OrderCenterOperateDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("type", ReasonType.TRADE_INVALID.value());
                            dialog.setArguments(bundle);
                            dialog.registerListener(new OrderCenterOperateDialogFragment.OperateListener() {
                                @Override
                                public boolean onSuccess(OrderCenterOperateDialogFragment.OperateResult result) {
                                    recisionTradeRequest(context, result.reason, paymentInfo.getTradeVo(), result.isPrintChecked);
                                    return true;
                                }
                            });
                            dialog.show(context.getSupportFragmentManager(), "destroy");

                        }
                    });

            dialogBuilder.build().show(context.getSupportFragmentManager(), "RemoveWeixinCouponDialog2");

        } catch (Exception e) {

            Log.e(TAG, "", e);
        }
    }

        public static void showDinnerRemoveCouponDilog(final FragmentActivity context, final IPaymentInfo paymentInfo, String title, final List<Long> promoIds) {

        new CommonDialogFragment.CommonDialogFragmentBuilder(context).title(title)
                .iconType(CommonDialogFragment.ICON_WARNING)
                .positiveText(R.string.pay_unbind_coupon)
                .positiveLinstner(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        try {
                                                        if (promoIds != null) {
                                DinnerShoppingCart dinnerShoppingCart = DinnerShopManager.getInstance().getShoppingCart();
                                dinnerShoppingCart.removeCouponPrivilege(dinnerShoppingCart.getShoppingCartVo(), promoIds, true);
                                DinnerShopManager.getInstance().getShoppingCart().removeGiftCouponePrivilege(promoIds, dinnerShoppingCart.getShoppingCartVo(), true);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "", e);
                        }
                    }
                })
                .build()
                .show(context.getSupportFragmentManager(), "RemoveDinnerCouponDialog");

    }

        public static void showDinnerRemoveWeixinCouponDilog(final FragmentActivity context, String title, final List<Long> promoIds) {

        new CommonDialogFragment.CommonDialogFragmentBuilder(context).title(title)
                .iconType(CommonDialogFragment.ICON_WARNING)
                .positiveText(R.string.pay_unbind_Wechat_coupon)
                .positiveLinstner(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        try {
                                                        DinnerShopManager.getInstance().getShoppingCart().removeWeiXinCoupons(promoIds);
                        } catch (Exception e) {
                            Log.e(TAG, "", e);
                        }
                    }
                })
                .build()
                .show(context.getSupportFragmentManager(), "RemoveDinnerCouponDialog");

    }

        public static void showRemoveCouponDialog(final FragmentActivity context, String title, final List<Long> coupIds) {
        try {
            new CommonDialogFragment.CommonDialogFragmentBuilder(context).title(title)
                    .iconType(CommonDialogFragment.ICON_WARNING)
                    .positiveText(R.string.pay_unbind_coupon)
                    .positiveLinstner(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            try {
                                                                ShoppingCart.getInstance().removeGiftCouponePrivilege(coupIds, ShoppingCart.getInstance().getShoppingCartVo(), false);
                                ShoppingCart.getInstance().removeCouponPrivilege(ShoppingCart.getInstance().fastFootShoppingCartVo, coupIds, true);

                            } catch (Exception e) {

                                Log.e(TAG, "", e);
                            }
                        }
                    })
                    .build()
                    .show(context.getSupportFragmentManager(), "RemoveCouponDialog1");

        } catch (Exception e) {

            Log.e(TAG, "", e);
        }
    }

        public static void showRemoveWeiXinCouponsDialog(final FragmentActivity context, String title, final List<TradePrivilege> tradePrivileges) {
        try {
            new CommonDialogFragment.CommonDialogFragmentBuilder(context).title(title)
                    .iconType(CommonDialogFragment.ICON_WARNING)
                    .positiveText(R.string.pay_unbind_coupon)
                    .positiveLinstner(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            try {
                                                                if (tradePrivileges != null) {
                                    ShoppingCart.getInstance().removeWeiXinCouponsPrivilege(tradePrivileges);
                                }

                            } catch (Exception e) {

                                Log.e(TAG, "", e);
                            }
                        }
                    })
                    .build()
                    .show(context.getSupportFragmentManager(), "RemoveWeiXinCouponsDialog");

        } catch (Exception e) {

            Log.e(TAG, "", e);
        }
    }

        public static void showRemoveDinnerIntegralDialog(final FragmentActivity context, String title) {
        new CommonDialogFragment.CommonDialogFragmentBuilder(context).title(title)
                .iconType(CommonDialogFragment.ICON_WARNING)
                .positiveText(R.string.pay_unbind_Integral)
                .positiveLinstner(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        try {
                                                        DinnerShopManager.getInstance().getShoppingCart().removeIntegralCash();
                        } catch (Exception e) {
                            Log.e(TAG, "", e);
                        }
                    }
                })
                .build()
                .show(context.getSupportFragmentManager(), "RemoveDinnerIntegralDialog");
    }

    public static void showRemoveIntegralOrDelTradeDialog(final FragmentActivity context, final IPaymentInfo paymentInfo, String title) {
        CommonDialogFragment.CommonDialogFragmentBuilder dialogBuilder = new CommonDialogFragment.CommonDialogFragmentBuilder(context).title(title)
                .iconType(CommonDialogFragment.ICON_WARNING)
                .positiveText(R.string.pay_unbind_Integral)
                .positiveLinstner(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        TradeUnbindCouponReq req = new TradeUnbindCouponReq();
                        if (paymentInfo.getTradeVo() != null && paymentInfo.getTradeVo().getIntegralCashPrivilegeVo() != null && paymentInfo.getTradeVo().getIntegralCashPrivilegeVo().isActived()) {
                            req.setTradeId(paymentInfo.getTradeVo().getTrade().getId());
                            if (paymentInfo.getTradeVo().getIntegralCashPrivilegeVo().getTradePrivilege() != null) {
                                req.setTradePrivilegeId(paymentInfo.getTradeVo().getIntegralCashPrivilegeVo().getTradePrivilege().getId());
                            }
                        }
                        TradeOperates tradeOperate = OperatesFactory.create(TradeOperates.class);
                                                tradeOperate.unbindIntegral(req, LoadingResponseListener.ensure(new ResponseListener<TradeResp>() {

                            @Override
                            public void onResponse(ResponseObject<TradeResp> response) {
                                try {
                                    if (ResponseObject.isOk(response)) {
                                        TradeDal mTradeDal = OperatesFactory.create(TradeDal.class);
                                                                                String tradeUuid = paymentInfo.getTradeVo().getTrade().getUuid();
                                                                                TradeVo tradeVo = mTradeDal.findTrade(tradeUuid);
                                        paymentInfo.setTradeVo(tradeVo);
                                        if (paymentInfo.isDinner() && !paymentInfo.isOrderCenter()) {                                             SeparateShoppingCart.getInstance().resetShopcartItemFromDB(null);
                                            DinnerShoppingCart.getInstance().resetShopcartItemFromDB(null);
                                        }
                                        paymentInfo.setOrdered(true);
                                    } else {
                                        showErrorDialog(context, context.getSupportFragmentManager(), response.getMessage());
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "", e);
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                try {
                                    ToastUtil.showShortToast(error.getMessage());

                                    showErrorDialog(context, context.getSupportFragmentManager(), error.getMessage());
                                } catch (Exception e) {
                                    Log.e(TAG, "", e);
                                }
                            }

                        }, context.getSupportFragmentManager()));
                    }
                })
                .negativeText(R.string.pay_delete_trade)
                .negativeLisnter(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        OrderCenterOperateDialogFragment dialog = new OrderCenterOperateDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", ReasonType.TRADE_INVALID.value());
                        dialog.setArguments(bundle);
                        dialog.registerListener(new OrderCenterOperateDialogFragment.OperateListener() {
                            @Override
                            public boolean onSuccess(OrderCenterOperateDialogFragment.OperateResult result) {
                                recisionTradeRequest(context, result.reason, paymentInfo.getTradeVo(), result.isPrintChecked);
                                return true;
                            }
                        });
                        dialog.show(context.getSupportFragmentManager(), "destroy");

                    }
                });

        dialogBuilder.build().show(context.getSupportFragmentManager(), "RemoveIntegralOrDelTradeDialog");
    }

        public static void showRemoveIntegralDialog(Context context, final FragmentManager fragmentManager, String title) {
        try {
            new CommonDialogFragment.CommonDialogFragmentBuilder(context).title(title)
                    .iconType(CommonDialogFragment.ICON_WARNING)
                    .positiveText(R.string.pay_unbind_Integral)
                    .positiveLinstner(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            try {
                                ShoppingCart.getInstance().removeIntegralCash();
                            } catch (Exception e) {

                                Log.e(TAG, "", e);
                            }
                        }
                    })
                    .build()
                    .show(fragmentManager, "RemoveCouponDialog1");

        } catch (Exception e) {

            Log.e(TAG, "", e);
        }
    }

        public static boolean isMemberPay(List<PaymentItem> paidItems) {
                if (!Utils.isEmpty(paidItems)) {
            for (PaymentItem item : paidItems) {
                if (PayModeId.MEMBER_CARD.value().equals(item.getPayModeId()) ||
                        PayModeId.ENTITY_CARD.value().equals(item.getPayModeId())
                        || PayModeId.ANONYMOUS_ENTITY_CARD.value().equals(item.getPayModeId())
                        ) {
                    return true;
                }
            }
        }
        return false;
    }





    public static TradeUser creatTradeUser(Trade trade, AuthUser authUser) {
        if (trade == null || authUser == null) {
            return null;
        }
        TradeUser tradeUser = new TradeUser();
        tradeUser.setChanged(true);
        tradeUser.setUserId(authUser.getId());
        tradeUser.setUserName(authUser.getName());
        tradeUser.setTradeId(trade.getId());
        tradeUser.setStatusFlag(StatusFlag.VALID);        if (trade.getBusinessType() != null) {
            switch (trade.getBusinessType()) {
                case CARD:                case ANONYMOUS_ENTITY_CARD_SELL:
                    tradeUser.setType(TradeScenceType.SALECARD);
                    break;
                case ONLINE_RECHARGE:                case CARD_RECHARGE:
                    tradeUser.setType(TradeScenceType.STORAGE_RECHARGE);
                    break;
                case ANONYMOUS_ENTITY_CARD_SELL_AND_RECHARGE:                case ANONYMOUS_ENTITY_CARD_RECHARGE:                    tradeUser.setType(TradeScenceType.SALECARD);
                    break;
                default:
                    tradeUser.setType(TradeScenceType.SALEDISH);                    break;
            }
        }
        return tradeUser;
    }

    public static TradeUser creatTradeUser(Trade trade, UserVo userVo) {
        if (trade == null || userVo == null || userVo.getUser() == null) {
            return null;
        }
        User user = userVo.getUser();
        TradeUser tradeUser = new TradeUser();
        tradeUser.setChanged(true);
        tradeUser.setUserId(user.getId());
        tradeUser.setUserName(user.getName());
        tradeUser.setTradeId(trade.getId());
        tradeUser.setStatusFlag(StatusFlag.VALID);        if (trade.getBusinessType() != null) {
            switch (trade.getBusinessType()) {
                case CARD:                case ANONYMOUS_ENTITY_CARD_SELL:
                    tradeUser.setType(TradeScenceType.SALECARD);
                    break;
                case ONLINE_RECHARGE:                case CARD_RECHARGE:
                    tradeUser.setType(TradeScenceType.STORAGE_RECHARGE);
                    break;
                case ANONYMOUS_ENTITY_CARD_SELL_AND_RECHARGE:                case ANONYMOUS_ENTITY_CARD_RECHARGE:                    tradeUser.setType(TradeScenceType.SALECARD);
                    break;
                default:
                    tradeUser.setType(TradeScenceType.SALEDISH);                    break;
            }
        }
        return tradeUser;
    }


        public static boolean isTradeWithMemberPrice(TradeVo tradeVo) {
        if (tradeVo != null && tradeVo.getTradeItemList() != null) {
            TradePrivilege tp = null;
            TradeItem tradeItem = null;
            for (TradeItemVo itemVo : tradeVo.getTradeItemList()) {
                tp = itemVo.getTradeItemPrivilege();
                tradeItem = itemVo.getTradeItem();
                if (tradeItem != null && tradeItem.isValid() && tp != null && tp.isValid() && (tp.getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT || tp.getPrivilegeType() == PrivilegeType.MEMBER_PRICE || tp.getPrivilegeType() == PrivilegeType.MEMBER_REBATE)  && tp.getPrivilegeAmount() != null && tp.getPrivilegeAmount().compareTo(BigDecimal.ZERO) != 0) {
                    return true;
                }
            }
        }
        return false;
    }

        public static TradeCustomer getTradeCustomer(TradeVo tradeVo) {
        if (tradeVo != null && tradeVo.getTradeCustomerList() != null) {
            List<TradeCustomer> tradeCustomerList = tradeVo.getTradeCustomerList();
            for (TradeCustomer tradeCustomer : tradeCustomerList) {
                if (tradeCustomer.isValid() && (tradeCustomer.getCustomerType() == CustomerType.MEMBER || tradeCustomer.getCustomerType() == CustomerType.CARD)) {
                    return tradeCustomer;
                }
            }
        }
        return null;
    }


        public static boolean isSupportLagPay(IPaymentInfo paymentInfo, boolean isSupportLag) {
        if (paymentInfo != null) {
            if (paymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT) {                return false;
            } else {
                switch (paymentInfo.getTradeBusinessType()) {
                    case GROUP:
                    case DINNER:
                        if (paymentInfo.getTradeVo().getTradeEarnestMoney() > 0) {                            return false;
                        }
                        return isSupportLag;
                    default:
                        return false;
                }
            }
        }
        return isSupportLag;
    }

    public static void saveTradeVoAsync(final TradeVo tradeVo) {
        if (tradeVo != null) {
            ThreadUtils.runOnWorkThread(new Runnable() {
                @Override
                public void run() {
                    TradeOperatesImpl.saveTradeResp(tradeVo.toTradeResp());
                }
            });
        }
    }

        public static DoPayApi doPayApiFactory(String className) {
        if (DoPayManager.class.getSimpleName().equals(className)) {
            return DoPayManager.getInstance();
        } else {
            return DoPayManager.getInstance();
        }
    }
}
