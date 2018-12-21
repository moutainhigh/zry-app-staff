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

/**
 * Created by demo on 2018/12/15
 */

public class PayUtils {
    private static final String TAG = PayUtils.class.getSimpleName();

    //判断订单金额是否为负
    public static boolean isNegativeTradeAmount(TradeVo tradeVo) {
        boolean result = false;
        if (tradeVo != null && tradeVo.getTrade() != null && tradeVo.getTrade().getTradeAmount() != null) {
            if (tradeVo.getTrade().getTradeAmount().compareTo(BigDecimal.ZERO) < 0) {
                result = true;
            }
        }
        return result;
    }

    //判断订单是否满足异步
    public static boolean isConditionAsync(TradeVo tradeVo) {
        if (tradeVo == null) {
            return false;
        }
        //拆出来新单不支持异步
        if (tradeVo.getTrade() != null && tradeVo.getTrade().getTradeType() == TradeType.SPLIT) {
            return false;
        }
        List<TradeCustomer> customerList = tradeVo.getTradeCustomerList();
        //如果有会员不走异步
        if (customerList != null && customerList.size() > 0) {
            for (TradeCustomer customer : customerList) {
                if ((CustomerType.MEMBER == customer.getCustomerType() || CustomerType.CUSTOMER == customer.getCustomerType() || CustomerType.CARD == customer.getCustomerType())
                        && customer.getStatusFlag() == StatusFlag.VALID) {
                    return false;
                }
            }
        }
        List<WeiXinCouponsVo> weiXinCouponsVoList = tradeVo.getmWeiXinCouponsVo();
        //如果有微信卡卷不走异步
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

    //判断是否只有现金支付
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

   /* public static void findSourceTradePaymentVos(final IPaymentInfo paymentInfo) {
        if (paymentInfo.getSourceTradeVo() != null) {
            Trade trade = paymentInfo.getSourceTradeVo().getTrade();
            if (trade != null && trade.getId() != null) {
                findTradePayments(paymentInfo, trade);
            }
        }
    }*/

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


    //显示支付成功界面
    public static void showPayOkDialog(FragmentActivity context, IPaymentInfo paymentInfo, boolean isAsync) {
        try {
            //add v8.13 begin 预付金抵扣
            int opType = IPayConstParame.OP_TYPE_DOPAY;//收银
            if (paymentInfo.getOtherPay().getAllPayModelItems() != null && !paymentInfo.getOtherPay().getAllPayModelItems().isEmpty()) {
                PayModelItem payModelItem = paymentInfo.getOtherPay().getAllPayModelItems().get(0);
                if (payModelItem != null && payModelItem.getPayMode() == PayModeId.EARNEST_DEDUCT) {
                    if (paymentInfo.getTradeVo().getTradeEarnestMoney() > (paymentInfo.getTradeAmount() - paymentInfo.getExemptAmount())) {
                        opType = IPayConstParame.OP_TYPE_DEDUCTION_REFUND;//订金抵扣退款
                    } else {
                        opType = IPayConstParame.OP_TYPE_DEDUCTION;//订金抵扣退款
                    }
                }
            }
            //add v8.13 end 预付金抵扣
            //如果包含消费税且支付完成，显示获取消费税号界面 modify v8.11
            if (DoPayUtils.isHaveTradeTax(paymentInfo.getTradeVo()) && DoPayUtils.isTradePaidOver(paymentInfo.getTradeVo().getTrade())) {
                DoPayUtils.showGetTaxNoPayOkDialog(context, DoPayManager.getInstance(), paymentInfo, opType);
            } else {
                DoPayUtils.showPayOkDialog(context, DoPayManager.getInstance(), paymentInfo, isAsync, opType);
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    //显示支付成功界面
    public static void showPayOkDialog(FragmentActivity context, IPaymentInfo paymentInfo, boolean isAsync, int operateType) {
        try {
            //如果包含消费税且支付完成，显示获取消费税号界面 modify v8.11
            if (DoPayUtils.isHaveTradeTax(paymentInfo.getTradeVo()) && DoPayUtils.isTradePaidOver(paymentInfo.getTradeVo().getTrade())) {
                DoPayUtils.showGetTaxNoPayOkDialog(context, DoPayManager.getInstance(), paymentInfo, operateType);
            } else {//原来支付成功界面
                DoPayUtils.showPayOkDialog(context, DoPayManager.getInstance(), paymentInfo, isAsync, operateType);
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    public static void showPayOkDialog(FragmentActivity context, IPaymentInfo paymentInfo) {
        showPayOkDialog(context, paymentInfo, false);
    }

    //显示支付失败界面
    public static void showPayErrorDialog(FragmentActivity context, IPaymentInfo paymentInfo, String errorReason, IPayOverCallback callBack, int errorCode) {
        DoPayUtils.showPayErrorDialog(context, DoPayManager.getInstance(), paymentInfo, errorReason, callBack, errorCode);
    }

    public static void showPayErrorDialog(FragmentActivity context, IPaymentInfo paymentInfo, String errorReason, IPayOverCallback callBack) {
        showPayErrorDialog(context, paymentInfo, errorReason, callBack, 0);
    }

    /**
     * @Description: 移除优惠劵或作废订单对话
     * @Param context Activity
     * @Param title String
     * @Param tradeVo TradeVo
     * @Param isDinner boolean
     * @Param isOrderCenter boolean
     * @Return void 返回类型
     */
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
                                            // 替换trade对象
                                            String tradeUuid = paymentInfo.getTradeVo().getTrade().getUuid();
                                            //更新tradeVo
                                            TradeVo tradeVo = mTradeDal.findTrade(tradeUuid);
                                            paymentInfo.setTradeVo(tradeVo);
                                            if (paymentInfo.isDinner() && !paymentInfo.isOrderCenter()) { //如果正餐收银界面
                                                SeparateShoppingCart.getInstance().resetShopcartItemFromDB(null);
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

    /**
     * @Title: recisionTradeRequest
     * @Description: 作废订单操作交付
     * @Param @param fragmentManager
     * @Param @param reason
     * @Param @param tradeVo
     * @Return void 返回类型
     */
    public static void recisionTradeRequest(final FragmentActivity context, Reason reason, final TradeVo tradeVo, final boolean isPrintKitchen) {
        TradeOperates tradeOperate = OperatesFactory.create(TradeOperates.class);
        // 作废订单操作
        tradeOperate.recision(tradeVo, reason, null,
                LoadingResponseListener.ensure(new ResponseListener<TradeResp>() {

                                                   @Override
                                                   public void onResponse(ResponseObject<TradeResp> response) {
                                                       try {
                                                           ToastUtil.showLongToast(response.getMessage());

                                                           if (ResponseObject.isOk(response)) {
                                                               Trade trade = tradeVo.getTrade();
                                                               if (trade.getBusinessType() == BusinessType.SNACK && trade.getTradePayStatus() == TradePayStatus.UNPAID && trade.getSource() == SourceId.POS && trade.getDeliveryType() == DeliveryType.HERE) {

                                                                   //不打作废单
                                                               } else if (trade.getBusinessType() == BusinessType.SNACK && trade.getTradeType() == TradeType.SELL_FOR_REPEAT) {
                                                                   //不打作废单
                                                               } else {
                                                                   //CashierPrintManager manager = new CashierPrintManager();
                                                                   //manager.printTrade(tradeVo.getTrade().getUuid(), Calm.PRINT_TYPE_DESTROY, null, true, false, isPrintKitchen);
                                                               }
                                                               //关闭快餐支付界面
                                                               /*if (context instanceof PayActivity) {
                                                                   context.finish();
                                                               }*/
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

    /**
     * @Title: showErrorDialog
     * @Description: 显示错误对话框
     * @Param @param fragmentManager
     * @Param @param errortitle
     * @Return void 返回类型
     */
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

    /**
     * 根据promoId获取对应优惠
     *
     * @param promoIds
     * @return
     */
    public static List<TradePrivilege> getWeiXinCouponsPrivileges(IPaymentInfo paymentInfo, List<Long> promoIds) {
        List<TradePrivilege> weiXinCouponsPrivileges = new ArrayList<TradePrivilege>();

        for (WeiXinCouponsVo weiXinCouponsVo : paymentInfo.getTradeVo().getmWeiXinCouponsVo()) {
            if (promoIds.contains(weiXinCouponsVo.getmTradePrivilege().getPromoId())) {
                weiXinCouponsPrivileges.add(weiXinCouponsVo.getmTradePrivilege());
            }
        }
        return weiXinCouponsPrivileges;
    }

    //显示快餐移除微信卡卷或作废订单对话框
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
                            // 移除优惠劵操作
                            tradeOperate.batchunbindCoupon(paymentInfo.getTradeVo().getTrade().getId(), tradePrivileges,
                                    LoadingResponseListener.ensure(new ResponseListener<TradeResp>() {

                                        @Override
                                        public void onResponse(ResponseObject<TradeResp> response) {
                                            try {
                                                if (ResponseObject.isOk(response)) {
                                                    TradeDal mTradeDal = OperatesFactory.create(TradeDal.class);
                                                    // 替换trade对象
                                                    String tradeUuid = paymentInfo.getTradeVo().getTrade().getUuid();
                                                    //更新tradeVo
                                                    TradeVo tradeVo = mTradeDal.findTrade(tradeUuid);
                                                    paymentInfo.setTradeVo(tradeVo);
                                                    if (paymentInfo.isDinner() && !paymentInfo.isOrderCenter()) { //如果正餐收银界面
                                                        SeparateShoppingCart.getInstance().resetShopcartItemFromDB(null);
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

    //显示正餐移除优惠劵对话框
    public static void showDinnerRemoveCouponDilog(final FragmentActivity context, final IPaymentInfo paymentInfo, String title, final List<Long> promoIds) {

        new CommonDialogFragment.CommonDialogFragmentBuilder(context).title(title)
                .iconType(CommonDialogFragment.ICON_WARNING)
                .positiveText(R.string.pay_unbind_coupon)
                .positiveLinstner(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        try {
                            //移除优惠劵包括单品礼品劵
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

    //显示正餐移除微信卡卷对话框
    public static void showDinnerRemoveWeixinCouponDilog(final FragmentActivity context, String title, final List<Long> promoIds) {

        new CommonDialogFragment.CommonDialogFragmentBuilder(context).title(title)
                .iconType(CommonDialogFragment.ICON_WARNING)
                .positiveText(R.string.pay_unbind_Wechat_coupon)
                .positiveLinstner(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        try {
                            //移除微信劵
                            DinnerShopManager.getInstance().getShoppingCart().removeWeiXinCoupons(promoIds);
                        } catch (Exception e) {
                            Log.e(TAG, "", e);
                        }
                    }
                })
                .build()
                .show(context.getSupportFragmentManager(), "RemoveDinnerCouponDialog");

    }

    //快餐直接购物车移除优惠劵
    public static void showRemoveCouponDialog(final FragmentActivity context, String title, final List<Long> coupIds) {
        try {
            new CommonDialogFragment.CommonDialogFragmentBuilder(context).title(title)
                    .iconType(CommonDialogFragment.ICON_WARNING)
                    .positiveText(R.string.pay_unbind_coupon)
                    .positiveLinstner(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            try {
                                // 从购物车移除优惠劵
                                ShoppingCart.getInstance().removeGiftCouponePrivilege(coupIds, ShoppingCart.getInstance().getShoppingCartVo(), false);
                                ShoppingCart.getInstance().removeCouponPrivilege(ShoppingCart.getInstance().fastFootShoppingCartVo, coupIds, true);
                                //关闭快餐支付界面
                                /*if (context instanceof PayActivity) {
                                    context.finish();
                                }*/
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

    //快餐直接购物车移除微信卡券
    public static void showRemoveWeiXinCouponsDialog(final FragmentActivity context, String title, final List<TradePrivilege> tradePrivileges) {
        try {
            new CommonDialogFragment.CommonDialogFragmentBuilder(context).title(title)
                    .iconType(CommonDialogFragment.ICON_WARNING)
                    .positiveText(R.string.pay_unbind_coupon)
                    .positiveLinstner(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            try {
                                // 从购物车移除优惠劵
                                if (tradePrivileges != null) {
                                    ShoppingCart.getInstance().removeWeiXinCouponsPrivilege(tradePrivileges);
                                }
                                //关闭快餐支付界面
                                /*if (context instanceof PayActivity) {
                                    context.finish();
                                }*/
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

    //正餐移除购物车积分
    public static void showRemoveDinnerIntegralDialog(final FragmentActivity context, String title) {
        new CommonDialogFragment.CommonDialogFragmentBuilder(context).title(title)
                .iconType(CommonDialogFragment.ICON_WARNING)
                .positiveText(R.string.pay_unbind_Integral)
                .positiveLinstner(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        try {
                            // 从购物车移除积分
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
                        // 移除积分操作
                        tradeOperate.unbindIntegral(req, LoadingResponseListener.ensure(new ResponseListener<TradeResp>() {

                            @Override
                            public void onResponse(ResponseObject<TradeResp> response) {
                                try {
                                    if (ResponseObject.isOk(response)) {
                                        TradeDal mTradeDal = OperatesFactory.create(TradeDal.class);
                                        // 替换trade对象
                                        String tradeUuid = paymentInfo.getTradeVo().getTrade().getUuid();
                                        //更新tradeVo
                                        TradeVo tradeVo = mTradeDal.findTrade(tradeUuid);
                                        paymentInfo.setTradeVo(tradeVo);
                                        if (paymentInfo.isDinner() && !paymentInfo.isOrderCenter()) { //如果正餐收银界面
                                            SeparateShoppingCart.getInstance().resetShopcartItemFromDB(null);
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

    //快餐直接购物车移除积分
    public static void showRemoveIntegralDialog(Context context, final FragmentManager fragmentManager, String title) {
        try {
            new CommonDialogFragment.CommonDialogFragmentBuilder(context).title(title)
                    .iconType(CommonDialogFragment.ICON_WARNING)
                    .positiveText(R.string.pay_unbind_Integral)
                    .positiveLinstner(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            try {
                                ShoppingCart.getInstance().removeIntegralCash();// 从购物车移除积分

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

    //判断是否是储值支付
    public static boolean isMemberPay(List<PaymentItem> paidItems) {
        //同一订单不允许相同账号重复支付
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

    /*public static double formatInputCash(String inputValue) {
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
    }*/

   /* //判断是否支持组合支付工具
    public static boolean isSupportGroupPay(IPaymentInfo paymentInfo, boolean isSetToSupportGroupPay) {
        //如果是金诚商户 不支持分步支付
        if (ServerSettingCache.getInstance().isJinChBusiness()) {
            return false;
        }
        if (paymentInfo != null) {
            if (paymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT) {
                return false;
            } else {
                switch (paymentInfo.getTradeBusinessType()) {
                    case GROUP:
                        return false;
                    default:
                        return isSetToSupportGroupPay;
                }
            }
        }
        return isSetToSupportGroupPay;
    }*/

    public static TradeUser creatTradeUser(Trade trade, AuthUser authUser) {
        if (trade == null || authUser == null) {
            return null;
        }
        TradeUser tradeUser = new TradeUser();
        tradeUser.setChanged(true);
        tradeUser.setUserId(authUser.getId());
        tradeUser.setUserName(authUser.getName());
        tradeUser.setTradeId(trade.getId());
        tradeUser.setStatusFlag(StatusFlag.VALID);//默认有效
        if (trade.getBusinessType() != null) {
            switch (trade.getBusinessType()) {
                case CARD://售卡
                case ANONYMOUS_ENTITY_CARD_SELL:
                    tradeUser.setType(TradeScenceType.SALECARD);
//                    tradeUser.setUserType(TradeUserType.MARKETMAN);//默认服务员
                    break;
                case ONLINE_RECHARGE://充值
                case CARD_RECHARGE:
                    tradeUser.setType(TradeScenceType.STORAGE_RECHARGE);
//                    tradeUser.setUserType(TradeUserType.MARKETMAN);//默认服务员
                    break;
                case ANONYMOUS_ENTITY_CARD_SELL_AND_RECHARGE://售卡及 充值
                case ANONYMOUS_ENTITY_CARD_RECHARGE://临时卡售卡及 充值
                    tradeUser.setType(TradeScenceType.SALECARD);
//                    tradeUser.setUserType(TradeUserType.MARKETMAN);//默认服务员
                    break;
                default:
                    tradeUser.setType(TradeScenceType.SALEDISH);//卖菜
//                    tradeUser.setUserType(TradeUserType.SALESMAN);//默认销售员
                    break;
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
        tradeUser.setStatusFlag(StatusFlag.VALID);//默认有效
        if (trade.getBusinessType() != null) {
            switch (trade.getBusinessType()) {
                case CARD://售卡
                case ANONYMOUS_ENTITY_CARD_SELL:
                    tradeUser.setType(TradeScenceType.SALECARD);
//                    tradeUser.setUserType(userVo.getTradeUserType());//默认服务员
                    break;
                case ONLINE_RECHARGE://充值
                case CARD_RECHARGE:
                    tradeUser.setType(TradeScenceType.STORAGE_RECHARGE);
                    break;
                case ANONYMOUS_ENTITY_CARD_SELL_AND_RECHARGE://售卡及 充值
                case ANONYMOUS_ENTITY_CARD_RECHARGE://临时卡售卡及 充值
                    tradeUser.setType(TradeScenceType.SALECARD);
                    break;
                default:
                    tradeUser.setType(TradeScenceType.SALEDISH);//卖菜
                    break;
            }
//            tradeUser.setUserType(userVo.getTradeUserType());//默认服务员
        }
        return tradeUser;
    }


    // 判断订单是否有会员价
    public static boolean isTradeWithMemberPrice(TradeVo tradeVo) {
        if (tradeVo != null && tradeVo.getTradeItemList() != null) {
            TradePrivilege tp = null;
            TradeItem tradeItem = null;
            for (TradeItemVo itemVo : tradeVo.getTradeItemList()) {
                tp = itemVo.getTradeItemPrivilege();
                tradeItem = itemVo.getTradeItem();
                if (tradeItem != null && tradeItem.isValid() && tp != null && tp.isValid() && (tp.getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT || tp.getPrivilegeType() == PrivilegeType.MEMBER_PRICE) && tp.getPrivilegeAmount() != null && tp.getPrivilegeAmount().compareTo(BigDecimal.ZERO) != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    //获取订单中的登录会员
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


    //判断是否挂账
    public static boolean isSupportLagPay(IPaymentInfo paymentInfo, boolean isSupportLag) {
        if (paymentInfo != null) {
            if (paymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT) {//自助交押金不支持挂账
                return false;
            } else {
                switch (paymentInfo.getTradeBusinessType()) {
                    case GROUP:
                    case DINNER:
                        if (paymentInfo.getTradeVo().getTradeEarnestMoney() > 0) {//add v8.13 预定金不支持挂账
                            return false;
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

    // 只有调用通用PayActivity 才会调用此方法 add v8.14
    public static DoPayApi doPayApiFactory(String className) {
        if (DoPayManager.class.getSimpleName().equals(className)) {
            return DoPayManager.getInstance();
        } else {
            return DoPayManager.getInstance();
        }
    }
}
