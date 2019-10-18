package com.zhongmei.bty.dinner.cash;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.manager.SystemSettingsManager;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.customer.bean.coupon.CouponVo;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.message.MemberCouponsResp;
import com.zhongmei.bty.basemodule.customer.message.MemberCouponsVoResp;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.yunfu.db.enums.CouponType;
import com.zhongmei.bty.basemodule.discount.event.ActionDinnerPrilivige;
import com.zhongmei.bty.basemodule.discount.event.ActionDinnerPrilivige.DinnerPriviligeType;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.bean.ShoppingCartVo;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.cashier.orderdishmanager.CouponsManager;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.ui.view.CalmLoadingDialogFragment;
import com.zhongmei.bty.dinner.action.ActionSeparateDeleteCoupon;
import com.zhongmei.bty.snack.event.EventCouponVoResult;
import com.zhongmei.bty.snack.orderdish.view.CouponsLayoutView;
import com.zhongmei.bty.snack.orderdish.view.CouponsLayoutView.CouponSelectedCallback;
import com.zhongmei.bty.snack.orderdish.view.CouponsLayoutView_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.greenrobot.event.EventBus;

import static com.zhongmei.yunfu.db.enums.CouponType.DISCOUNT;


@EFragment(R.layout.fragment_dinner_coupons)
public class DinnerCouponsFragment extends BasicFragment implements CouponSelectedCallback {
    private static final String TAG = DinnerCouponsFragment.class.getSimpleName();
    private int CASH_COUPON_COUNT = 1;


    @ViewById(R.id.btn_close)
    Button closeBT;



    @ViewById(R.id.coupons_view)
    LinearLayout mCouponsLL;
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

    private Set<CouponVo> mSelectedCpupons = new HashSet<CouponVo>();    private CustomerOperates operates;

    private CouponsManager mCouponsManager;


    private int mCouponType = COUPON_TYPE_CUSTOMER;

    private int mCouponsPage = COUPON_PAGE_2;

    private FragmentManager mFragmentManager;

    private CalmLoadingDialogFragment mDialogFragment = null;

    @AfterViews
    void init() {
        mContext = getActivity();
        mFragmentManager = getActivity().getSupportFragmentManager();
                operates = OperatesFactory.create(CustomerOperates.class);
        mCouponsManager = new CouponsManager();
        CASH_COUPON_COUNT = SystemSettingsManager.getCashCouponNum();

        registerEventBus();
        loadCoupons();
    }











    @Click({R.id.btn_close})
    void click(View v) {
        switch (v.getId()) {


            case R.id.btn_close:
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.PRIVILIGE_ITEMS));
                break;


        }
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

        private void removeLastCouponFromShoppingCart(CouponVo currentVo) {
        updateSelectedView(currentVo);
                DinnerShoppingCart dinnerShoppingCart = DinnerShopManager.getInstance().getShoppingCart();
        List<Long> promeIdList = new ArrayList<Long>();
        promeIdList.add(currentVo.getCouponInfo().getId());
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
                if (currentVo.getCouponInfo().getCouponType() == CouponType.GIFT && isTradeItemGiftCoupon(currentVo)) {
            Trade trade = DinnerShopManager.getInstance().getShoppingCart().getShoppingCartVo().getmTradeVo().getTrade();
            if (trade != null && trade.getBusinessType() == BusinessType.BUFFET && trade.getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
                ToastUtil.showShortToast(R.string.union_trade_not_suport);
                return;
            }
        }
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

                                                if (currentVo.getCouponInfo().getCouponType() == DISCOUNT) {
                if (mSelectedCpupons.isEmpty()) {
                                        DinnerShoppingCart dinnerShoppingCart = DinnerShopManager.getInstance().getShoppingCart();
                    if (BigDecimal.ZERO.compareTo(dinnerShoppingCart.getShoppingCartVo().getmTradeVo().getTrade().getTradeAmount()) == 0) {
                        ToastUtil.showLongToast(R.string.dinner_order_amount_zero_error);
                        currentVo.setSelected(false);
                        return;
                    }
                    DinnerShopManager.getInstance().getShoppingCart().setCouponPrivilege(mCouponsManager.getCouponPrivilegeVo(currentVo), true, true);
                    mSelectedCpupons.add(currentVo);
                } else {
                    if (hasDiscountCoupon()) {                         CouponPrivilegeVo couponPrivilegeVo = mCouponsManager.getCouponPrivilegeVo(currentVo);
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
                    } else {
                        currentVo.setSelected(false);
                        ToastUtil.showLongToast(this.getString(R.string.dinner_coupon_select_alert));
                        return;
                    }
                }
                updateSelectedView(currentVo);
            } else {
                if (hasDiscountCoupon()) {
                    currentVo.setSelected(false);
                    ToastUtil.showLongToast(this.getString(R.string.dinner_coupon_select_alert));
                    return;
                }
                if (mSelectedCpupons.size() >= CASH_COUPON_COUNT) {
                    currentVo.setSelected(false);
                    ToastUtil.showLongToast(R.string.coupon_over_count_error);
                    return;
                }

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
                            ToastUtil.showLongToast(this.getString(R.string.dinner_shopcart_Coupon_canot_use));
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
            }
        }

            }

    private boolean hasDiscountCoupon() {
        boolean has = false;
        if (mSelectedCpupons.isEmpty())
            return has;
        for (CouponVo vo : mSelectedCpupons) {
            if (vo.getCouponInfo().getCouponType() == DISCOUNT)
                has = true;
        }
        return has;
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

            case REBATE:                if (rebateView != null)
                    rebateView.updateViews();
                break;

            case DISCOUNT:                if (discountView != null) {
                    discountView.updateViews();
                }
                break;
            case GIFT:                if (giftView != null) {
                    giftView.updateViews();
                }
                break;
            case CASH:                if (cashView != null) {
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
        }         if (eventdata.getRebateCoupons() != null) {
            isNothing = false;
            rebateView = CouponsLayoutView_.build(mContext, this, CouponType.REBATE);
            rebateView.setData(eventdata.getRebateCoupons(), 1);
            mCouponsLL.addView(rebateView);
        }         if (eventdata.getDiscountCoupons() != null) {
            isNothing = false;
            discountView = CouponsLayoutView_.build(mContext, this, DISCOUNT);
            discountView.setData(eventdata.getDiscountCoupons(), 1);
            mCouponsLL.addView(discountView);
        }         if (eventdata.getGiftCoupons() != null) {
            isNothing = false;
            giftView = CouponsLayoutView_.build(mContext, this, CouponType.GIFT);
            giftView.setData(eventdata.getGiftCoupons(), 1);
            mCouponsLL.addView(giftView);
        }
                if (isNothing) {
            mCouponsLL.addView(getNothingAlertView());
        }
        mCouponsLL.post(new Runnable() {
            @Override
            public void run() {
                dismissLoadingProgressDialog();
            }
        });
    }

    private View getNothingAlertView() {
        TextView view = new TextView(mContext);
        view.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams diyWh =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(diyWh);
        diyWh.setMargins(0, 40, 0, 0);
        view.setTextSize(mContext.getResources().getDimension(R.dimen.text_22));
        view.setTextColor(mContext.getResources().getColor(R.color.text_discount_yellow));
        view.setText(mContext.getString(R.string.coupon_nothing_alter_text));

        return view;
    }

    private void loadCoupons() {
        CustomerResp customer = DinnerShopManager.getInstance().getLoginCustomer();
        mCouponsManager.setDinner(true);
        if (mCouponType == COUPON_TYPE_CUSTOMER && mCouponsPage == COUPON_PAGE_2) {
            if (customer != null) {
                mCouponsLL.removeAllViews();
                getCustomerCoupons(customer);
            } else {
                mCouponsLL.removeAllViews();
                mCouponsLL.addView(getNothingAlertView());
            }

        } else {
            mCouponsLL.removeAllViews();
            mCouponsLL.addView(getNothingAlertView());
        }
    }

    @UiThread
    public void getCustomerCoupons(final CustomerResp customer) {

        if (customer == null || customer.customerId == null) {
            return;
        }

        CustomerManager.getInstance().getCustomerCoupons(customer.customerId, 1, Integer.MAX_VALUE, new ResponseListener<MemberCouponsVoResp>() {
            @Override
            public void onResponse(ResponseObject<MemberCouponsVoResp> response) {
                if (ResponseObject.isOk(response) && response.getContent().isOk()) {
                    MemberCouponsResp resp = response.getContent().getResult();
                    if (customer.isMember()) {
                                                Long integral = customer.integral;
                        if (integral == null) {
                            integral = 0L;
                        }

                    }
                                        mCouponsManager.loadData(resp.getItems());
                } else {
                    dismissLoadingProgressDialog();
                    ToastUtil.showShortToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                dismissLoadingProgressDialog();
                ToastUtil.showShortToast(error.getMessage());
            }
        });

        showLoadingProgressDialog();



    }

    @Override
    public void onResume() {
        super.onResume();
            }
}
