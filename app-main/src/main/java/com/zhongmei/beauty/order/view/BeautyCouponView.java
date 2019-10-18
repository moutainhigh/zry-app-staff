package com.zhongmei.beauty.order.view;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.LinearLayout;

import com.zhongmei.yunfu.bean.YFResponseList;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.manager.SystemSettingsManager;
import com.zhongmei.yunfu.bean.req.CustomerCouponResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.customer.bean.coupon.CouponVo;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.yunfu.db.enums.CouponType;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.bean.ShoppingCartVo;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.beauty.order.event.ActionClearShopcart;
import com.zhongmei.bty.cashier.orderdishmanager.CouponsManager;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.ui.view.CalmLoadingDialogFragment;
import com.zhongmei.bty.dinner.action.ActionSeparateDeleteCoupon;
import com.zhongmei.bty.snack.event.EventCouponVoResult;
import com.zhongmei.bty.snack.orderdish.view.CouponsLayoutView;
import com.zhongmei.bty.snack.orderdish.view.CouponsLayoutView_;
import com.zhongmei.yunfu.resp.YFResponseListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


@EViewGroup(R.layout.beauty_coupons)
public class BeautyCouponView extends LinearLayout implements CouponsLayoutView.CouponSelectedCallback {

    private static final String TAG = BeautyCouponView.class.getSimpleName();
    private int CASH_COUPON_COUNT = 1;

    @ViewById(R.id.coupons_view)
    LinearLayout mCouponsLL;
    @ViewById(R.id.include_empty_status)
    View emptyView;

    private Context mContext;

    private final int COUPON_TYPE_GENERAL = 0;

    private final int COUPON_TYPE_CUSTOMER = 1;

    private final int COUPON_PAGE_1 = 1;
    private final int COUPON_PAGE_2 = 2;
    private final int COUPON_PAGE_3 = 3;
    private CouponsLayoutView rebateView;
    private CouponsLayoutView discountView;
    private CouponsLayoutView giftView;
    private CouponsLayoutView cashView;

    private Set<CouponVo> mSelectedCpupons = new HashSet<CouponVo>();
    private CustomerOperates operates;

    private CouponsManager mCouponsManager;


    private int mCouponType = COUPON_TYPE_CUSTOMER;

    private int mCouponsPage = COUPON_PAGE_2;

    private FragmentManager mFragmentManager;
    private FragmentActivity mActivity;
    private CalmLoadingDialogFragment mDialogFragment = null;

    public BeautyCouponView(Context context) {
        super(context);
        mActivity = (FragmentActivity) context;
        mContext = context;
    }

    @AfterViews
    void init() {

        mFragmentManager = mActivity.getSupportFragmentManager();
        operates = OperatesFactory.create(CustomerOperates.class);
        mCouponsManager = new CouponsManager();
        CASH_COUPON_COUNT = SystemSettingsManager.getCashCouponNum();
        loadCoupons();
    }


    public void onEventMainThread(ActionSeparateDeleteCoupon event) {
        if (event.id != null) {
            Iterator<CouponVo> iterator = mSelectedCpupons.iterator();
            while (iterator.hasNext()) {
                CouponVo currentVo = iterator.next();
                if (event.id.equals(currentVo.getCouponInfo().getId())) {
                    currentVo.setSelected(false);
                    updateSelectedView(currentVo);
                    iterator.remove();
                }
            }
        }
    }


    public void onEventMainThread(ActionClearShopcart event) {
        Iterator<CouponVo> iterator = mSelectedCpupons.iterator();
        while (iterator.hasNext()) {
            CouponVo currentVo = iterator.next();
            currentVo.setSelected(false);
            updateSelectedView(currentVo);
            iterator.remove();
        }
    }

    private void removeLastCouponFromShoppingCart(CouponVo currentVo) {
        updateSelectedView(currentVo);
        DinnerShoppingCart dinnerShoppingCart = DinnerShopManager.getInstance().getShoppingCart();
        List<Long> promeIdList = new ArrayList<Long>();
        promeIdList.add(currentVo.getCouponInfo().getCustomerCouponId());
        dinnerShoppingCart.removeCouponPrivilege(dinnerShoppingCart.getShoppingCartVo(), promeIdList, true);
        mSelectedCpupons.remove(currentVo);
    }

    private void removeAllTradeCoupons() {
        Iterator<CouponVo> iterator = mSelectedCpupons.iterator();
        while (iterator.hasNext()) {
            CouponVo currentVo = iterator.next();
            if (currentVo.getCouponInfo().getCouponType() == CouponType.GIFT) {
                if (!isTradeItemGiftCoupon(currentVo)) {
                    currentVo.setSelected(false);
                    updateSelectedView(currentVo);
                    iterator.remove();
                }
            } else {
                currentVo.setSelected(false);
                updateSelectedView(currentVo);
                iterator.remove();
            }
        }
        DinnerShoppingCart dinnerShoppingCart = DinnerShopManager.getInstance().getShoppingCart();
        dinnerShoppingCart.removeAllCouponPrivilege(dinnerShoppingCart.getShoppingCartVo(), true);
    }

    private boolean isTradeItemGiftCoupon(CouponVo currentVo) {
        if (currentVo.getDishId() != null && currentVo.getDishId().longValue() != 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isGiftCouponEnable(CouponVo currentVo, CouponPrivilegeVo couponPrivilegeVo) {
        if (couponPrivilegeVo.getShopcartItem() == null) {
            ToastUtil.showShortToast(R.string.the_goods_of_gift_coupon_is_not_exist);
            currentVo.setSelected(false);
            updateSelectedView(currentVo);
            return false;
        }
        if (couponPrivilegeVo.getShopcartItem().getDishShop().getClearStatus() == ClearStatus.CLEAR) {
            ToastUtil.showShortToast(R.string.the_goods_of_gift_coupon_has_been_clear);
            currentVo.setSelected(false);
            updateSelectedView(currentVo);
            return false;
        }
        return true;
    }


    @Override
    public void onCouponSelected(CouponVo currentVo, CouponType type) {
        if (mSelectedCpupons.contains(currentVo)) {
            if (isTradeItemGiftCoupon(currentVo)) {
                ShoppingCartVo shoppingCartVo = DinnerShopManager.getInstance().getShoppingCart().getShoppingCartVo();
                DinnerShopManager.getInstance().getShoppingCart().removeGiftCouponePrivilege(mCouponsManager.getCouponPrivilegeVo(currentVo).getTradePrivilege().getPromoId(), shoppingCartVo, true);
                updateSelectedView(currentVo);
                mSelectedCpupons.remove(currentVo);
            } else {
                removeLastCouponFromShoppingCart(currentVo);
            }

        } else {
            if (mSelectedCpupons.isEmpty()) {
                if (currentVo.isSelected()) {
                    if (currentVo.getCouponInfo().getCouponType() == CouponType.GIFT && isTradeItemGiftCoupon(currentVo)) {
                        CouponPrivilegeVo couponPrivilegeVo = mCouponsManager.getCouponPrivilegeVo(currentVo);
                        if (!isGiftCouponEnable(currentVo, couponPrivilegeVo)) {
                            return;
                        }

                        boolean isOk = DinnerShopManager.getInstance().getShoppingCart().setGiftCouponPrivilege(couponPrivilegeVo);
                        if (isOk) {
                            mSelectedCpupons.add(currentVo);
                        } else {
                            currentVo.setSelected(false);
                            ToastUtil.showLongToast(mActivity.getString(R.string.dinner_shopcart_Coupon_canot_use));
                        }
                    } else {
                        DinnerShoppingCart dinnerShoppingCart = DinnerShopManager.getInstance().getShoppingCart();
                        if (BigDecimal.ZERO.compareTo(dinnerShoppingCart.getShoppingCartVo().getmTradeVo().getTrade().getTradeAmount()) == 0) {
                            ToastUtil.showLongToast(R.string.dinner_order_amount_zero_error);
                            currentVo.setSelected(false);
                            return;
                        }
                        DinnerShopManager.getInstance().getShoppingCart().setCouponPrivilege(mCouponsManager.getCouponPrivilegeVo(currentVo), true, true);
                        mSelectedCpupons.add(currentVo);
                    }
                }
                updateSelectedView(currentVo);
            } else {
                if (currentVo.getCouponInfo().getCouponType() == CouponType.GIFT && isTradeItemGiftCoupon(currentVo)) {
                    if (currentVo.isSelected()) {
                        CouponPrivilegeVo couponPrivilegeVo = mCouponsManager.getCouponPrivilegeVo(currentVo);
                        if (!isGiftCouponEnable(currentVo, couponPrivilegeVo)) {
                            return;
                        }
                        boolean isOk = DinnerShopManager.getInstance().getShoppingCart().setGiftCouponPrivilege(couponPrivilegeVo);
                        if (isOk) {
                            mSelectedCpupons.add(currentVo);
                        } else {
                            currentVo.setSelected(false);
                            ToastUtil.showLongToast(mActivity.getString(R.string.dinner_shopcart_Coupon_canot_use));
                        }
                        updateSelectedView(currentVo);
                    }
                } else {

                    CouponPrivilegeVo couponPrivilegeVo = mCouponsManager.getCouponPrivilegeVo(currentVo);
                    if (couponPrivilegeVo.getCoupon() == null) {
                        return;
                    }
                    DinnerShoppingCart dinnerShoppingCart = DinnerShopManager.getInstance().getShoppingCart();
                    if (BigDecimal.ZERO.compareTo(dinnerShoppingCart.getShoppingCartVo().getmTradeVo().getTrade().getTradeAmount()) == 0) {
                        ToastUtil.showLongToast(R.string.dinner_order_amount_zero_error);
                        currentVo.setSelected(false);
                        return;
                    }
                    boolean isAllowAdd = dinnerShoppingCart.isAllowAddCoupon(dinnerShoppingCart.getShoppingCartVo(), couponPrivilegeVo);
                    if (!isAllowAdd) {
                        removeAllTradeCoupons();
                    }
                    if (couponPrivilegeVo.getCoupon().getCouponType() == CouponType.CASH) {
                        if (getDefineCouponCount() >= CASH_COUPON_COUNT) {
                            currentVo.setSelected(false);
                            ToastUtil.showLongToast(R.string.coupon_over_count_error);
                            return;
                        }
                    }
                    if (currentVo.isSelected()) {
                        DinnerShopManager.getInstance().getShoppingCart().setCouponPrivilege(couponPrivilegeVo, true, true);

                        mSelectedCpupons.add(currentVo);
                    }
                    updateSelectedView(currentVo);
                }
            }
        }
    }


    private int getDefineCouponCount() {
        Iterator iterator = mSelectedCpupons.iterator();
        int defineCouponCount = 0;
        while (iterator.hasNext()) {
            CouponVo couponVo = (CouponVo) iterator.next();
            if (!isTradeItemGiftCoupon(couponVo)) {
                defineCouponCount++;
            }
        }
        return defineCouponCount;
    }


    private void updateSelectedView(CouponVo vo) {
        if (vo == null)
            return;

        switch (vo.getCouponInfo().getCouponType()) {

            case REBATE:
                if (rebateView != null)
                    rebateView.updateViews();
                break;

            case DISCOUNT:
                if (discountView != null) {
                    discountView.updateViews();
                }
                break;
            case GIFT:
                if (giftView != null) {
                    giftView.updateViews();
                }
                break;
            case CASH:
                if (cashView != null) {
                    cashView.updateViews();
                }
                break;
            default:
                break;
        }
    }


    public void onEventMainThread(EventCouponVoResult eventdata) {

        mCouponsLL.removeAllViews();
        mSelectedCpupons.clear();
        mSelectedCpupons.addAll(eventdata.getSelectedCoupons());
        boolean isNothing = true;
        if (eventdata.getCashCoupons() != null) {
            isNothing = false;
            cashView = CouponsLayoutView_.build(mContext, this, CouponType.CASH);
            cashView.setData(eventdata.getCashCoupons(), 1);
            mCouponsLL.addView(cashView);
        }
        if (eventdata.getRebateCoupons() != null) {
            isNothing = false;
            rebateView = CouponsLayoutView_.build(mContext, this, CouponType.REBATE);
            rebateView.setData(eventdata.getRebateCoupons(), 1);
            mCouponsLL.addView(rebateView);
        }
        if (eventdata.getDiscountCoupons() != null) {
            isNothing = false;
            discountView = CouponsLayoutView_.build(mContext, this, CouponType.DISCOUNT);
            discountView.setData(eventdata.getDiscountCoupons(), 1);
            mCouponsLL.addView(discountView);
        }
        if (eventdata.getGiftCoupons() != null) {
            isNothing = false;
            giftView = CouponsLayoutView_.build(mContext, this, CouponType.GIFT);
            giftView.setData(eventdata.getGiftCoupons(), 1);
            mCouponsLL.addView(giftView);
        }
        if (isNothing) {
            mCouponsLL.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    private void loadCoupons() {
        CustomerResp customer = CustomerManager.getInstance().getDinnerLoginCustomer();
        mCouponsManager.setDinner(true);
        if (mCouponType == COUPON_TYPE_CUSTOMER && mCouponsPage == COUPON_PAGE_2) {
            if (customer != null) {
                mCouponsLL.removeAllViews();
                getCustomerCoupons(customer);
            } else {
                mCouponsLL.removeAllViews();
                mCouponsLL.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }

        } else {
            mCouponsLL.removeAllViews();
            mCouponsLL.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @UiThread
    public void getCustomerCoupons(final CustomerResp customer) {

        if (customer == null || customer.customerId == null) {
            return;
        }

        CustomerManager.getInstance().getCustomerCoupons(customer.customerId, 1, Integer.MAX_VALUE, new YFResponseListener<YFResponseList<CustomerCouponResp>>() {
            @Override
            public void onResponse(YFResponseList<CustomerCouponResp> response) {
                if (YFResponseList.isOk(response)) {
                    mCouponsManager.loadData(response.getContent());
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

}
