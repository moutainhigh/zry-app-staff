package com.zhongmei.beauty.pay;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.yunfu.beauty.R;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;

import java.util.List;



public class BeautyCheckDialog {
    private static String TAG = "BeautyCheckDialog";

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
                                updateServerData(paymentInfo);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "", e);
                        }
                    }
                })
                .build()
                .show(context.getSupportFragmentManager(), "RemoveDinnerCouponDialog");

    }

        public static void showRemoveDinnerIntegralDialog(final FragmentActivity context, String title, final IPaymentInfo paymentInfo) {
        new CommonDialogFragment.CommonDialogFragmentBuilder(context).title(title)
                .iconType(CommonDialogFragment.ICON_WARNING)
                .positiveText(R.string.pay_unbind_Integral)
                .positiveLinstner(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        try {
                                                        DinnerShopManager.getInstance().getShoppingCart().removeIntegralCash();
                            updateServerData(paymentInfo);
                        } catch (Exception e) {
                            Log.e(TAG, "", e);
                        }
                    }
                })
                .build()
                .show(context.getSupportFragmentManager(), "RemoveDinnerIntegralDialog");
    }

        public static void showDinnerRemoveWeixinCouponDilog(final FragmentActivity context, String title, final List<Long> promoIds, final IPaymentInfo paymentInfo) {

        new CommonDialogFragment.CommonDialogFragmentBuilder(context).title(title)
                .iconType(CommonDialogFragment.ICON_WARNING)
                .positiveText(R.string.pay_unbind_Wechat_coupon)
                .positiveLinstner(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        try {
                                                        DinnerShopManager.getInstance().getShoppingCart().removeWeiXinCoupons(promoIds);
                            updateServerData(paymentInfo);
                        } catch (Exception e) {
                            Log.e(TAG, "", e);
                        }
                    }
                })
                .build()
                .show(context.getSupportFragmentManager(), "RemoveDinnerCouponDialog");

    }


    public static void updateServerData(IPaymentInfo paymentInfo) {
        paymentInfo.setTradeVo(DinnerShoppingCart.getInstance().getOrder());
                paymentInfo.setOrdered(false);
    }
}
