package com.zhongmei.bty.snack.orderdish.buinessview;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.cashier.orderdishmanager.CouponsManager;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCart;
import com.zhongmei.yunfu.bean.req.CustomerCouponResp;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.basemodule.customer.message.MemberCouponsResp;
import com.zhongmei.bty.basemodule.customer.message.MemberCouponsVoResp;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.yunfu.db.enums.CouponType;
import com.zhongmei.bty.entity.event.orderdishevent.EventShopcartDelIntergral;
import com.zhongmei.bty.entity.event.orderdishevent.EventShopcatDelCoupon;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.commonbusiness.manager.SystemSettingsManager;
import com.zhongmei.bty.snack.event.EventCouponVoResult;
import com.zhongmei.bty.basemodule.customer.action.EventMemeberIsLogin;
import com.zhongmei.bty.basemodule.customer.bean.coupon.CouponVo;
import com.zhongmei.bty.snack.orderdish.view.CouponsLayoutView;
import com.zhongmei.bty.snack.orderdish.view.CouponsLayoutView_;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.greenrobot.event.EventBus;



public class CustomerCouponView extends LinearLayout implements CouponsLayoutView.CouponSelectedCallback, View.OnClickListener {
    private static final String TAG = CustomerCouponView.class.getSimpleName();


    LinearLayout mCouponsLL;

    LinearLayout couponNothingAlter;

    private CustomerOperates operates;

    private CustomerResp mCustomer;

    private ChangePageListener mChangePageListener;

    private CouponsManager mCouponsManager;

    private Context mContext;

    private CouponsLayoutView rebateView;
    private CouponsLayoutView discountView;
    private CouponsLayoutView giftView;
    private CouponsLayoutView cashView;
    private CouponVo mLastSeletedCouponVo;

    private FragmentManager mFragmentManager;


    private Set<CouponVo> mSelectedCpupons = new HashSet<CouponVo>();
    private int mCashNum = 1;
    public CustomerCouponView(Context context) {
        super(context);
        mContext = context;
    }

    public CustomerCouponView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mCouponsManager = new CouponsManager();
        init();
    }

    public CustomerCouponView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick(View v) {

    }

    protected void init() {
        LayoutInflater.from(mContext).inflate(R.layout.customer_coupons_fragment, this);
        mFragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        couponNothingAlter = (LinearLayout) findViewById(R.id.coupon_nothing_alter);
        mCouponsLL = (LinearLayout) findViewById(R.id.coupons_view);
        operates = OperatesFactory.create(CustomerOperates.class);
                LoadSettingeAsyncTask loadSettingeAsyncTask = new LoadSettingeAsyncTask();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            loadSettingeAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else {
            loadSettingeAsyncTask.execute();
        }

    }


    class LoadSettingeAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            mCashNum = SystemSettingsManager.getCashCouponNum();
            return null;
        }

        @Override
        protected void onPostExecute(Void Void) {

            super.onPostExecute(Void);
        }
    }


    public void onEventMainThread(EventShopcatDelCoupon event) {
        if (event.mIds != null) {
            Iterator<CouponVo> iterator = mSelectedCpupons.iterator();
            while (iterator.hasNext()) {
                CouponVo currentVo = iterator.next();
                for (Long id : event.mIds) {
                    if (id.equals(currentVo.getCouponInfo().getId())) {
                        currentVo.setSelected(false);
                        updateSelectedView(currentVo);
                        iterator.remove();
                    }
                }
            }
        } else {
            removeAllTradeCoupons();
        }
    }


    public void onEventMainThread(EventShopcartDelIntergral event) {
    }


    public void onEventMainThread(EventCouponVoResult eventdata) {
        mLastSeletedCouponVo = eventdata.getSelectedCouponVo();
        mCouponsLL.removeAllViews();
        mSelectedCpupons.clear();
        mSelectedCpupons.addAll(eventdata.getSelectedCoupons());
                if (eventdata.getCashCoupons() != null) {
            cashView = CouponsLayoutView_.build(mContext, this, CouponType.CASH);
            cashView.setData(eventdata.getCashCoupons(), 1);
            mCouponsLL.addView(cashView);
        }         if (eventdata.getRebateCoupons() != null) {
            rebateView = CouponsLayoutView_.build(mContext, this, CouponType.REBATE);
            rebateView.setData(eventdata.getRebateCoupons(), 1);
            mCouponsLL.addView(rebateView);
        }         if (eventdata.getDiscountCoupons() != null) {
            discountView = CouponsLayoutView_.build(mContext, this, CouponType.DISCOUNT);
            discountView.setData(eventdata.getDiscountCoupons(), 1);
            mCouponsLL.addView(discountView);
        }         if (eventdata.getGiftCoupons() != null) {
            giftView = CouponsLayoutView_.build(mContext, this, CouponType.GIFT);
            giftView.setData(eventdata.getGiftCoupons(), 1);
            mCouponsLL.addView(giftView);
        }
        if (mCouponsLL.getChildCount() == 0) {
            couponNothingAlter.setVisibility(View.VISIBLE);
        }
    }


    private boolean isTradeItemGiftCoupon(CouponVo currentVo) {
        if (currentVo.getDishId() != null && currentVo.getDishId().longValue() != 0) {
            return true;
        } else {
            return false;
        }
    }


    private void removeLastCouponFromShoppingCart(CouponVo currentVo) {
        currentVo.setSelected(false);
        updateSelectedView(currentVo);
                ShoppingCart.getInstance().removeCouponPrivilege(mCouponsManager.getCouponPrivilegeVo(currentVo), true);
        mSelectedCpupons.remove(currentVo);
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
                ShoppingCart.getInstance().removeCouponPrivilege();
    }



    @Override
    public void onCouponSelected(CouponVo currentVo, CouponType type) {
        if (ShoppingCart.getInstance().getIsSalesReturn()) {
            ToastUtil.showShortToast(R.string.refeund_cannot_user_coupon);
            return;
        }

                if (mSelectedCpupons.contains(currentVo)) {
                        if (isTradeItemGiftCoupon(currentVo)) {
                currentVo.setSelected(false);
                updateSelectedView(currentVo);
                ShoppingCart.getInstance().removeGiftCouponePrivilege(currentVo.getCouponInfo().getId(), ShoppingCart.getInstance().getShoppingCartVo(), false);
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
                        ShoppingCart.getInstance().setCouponPrivilege(mCouponsManager.getCouponPrivilegeVo(currentVo));
                        mSelectedCpupons.add(currentVo);
                    } else {
                                                ShoppingCart.getInstance().setCouponPrivilege(mCouponsManager.getCouponPrivilegeVo(currentVo));
                        mSelectedCpupons.add(currentVo);
                    }
                }
                updateSelectedView(currentVo);
            } else {                if (currentVo.getCouponInfo().getCouponType() == CouponType.GIFT && isTradeItemGiftCoupon(currentVo)) {
                    if (currentVo.isSelected()) {
                        CouponPrivilegeVo couponPrivilegeVo = mCouponsManager.getCouponPrivilegeVo(currentVo);
                                                if (!isGiftCouponEnable(currentVo, couponPrivilegeVo)) {
                            return;
                        }
                        ShoppingCart.getInstance().setCouponPrivilege(mCouponsManager.getCouponPrivilegeVo(currentVo));
                        mSelectedCpupons.add(currentVo);

                        updateSelectedView(currentVo);
                    }
                } else {
                    if (!ShoppingCart.getInstance().isAllowAddCoupon(mCouponsManager.getCouponPrivilegeVo(currentVo))) {
                                                removeAllTradeCoupons();
                    }
                    if (currentVo.isSelected()) {
                        if (currentVo.getCouponInfo().getCouponType() == CouponType.CASH) {
                            if (mSelectedCpupons.size() < mCashNum) {                                                                ShoppingCart.getInstance().setCouponPrivilege(mCouponsManager.getCouponPrivilegeVo(currentVo));

                                mSelectedCpupons.add(currentVo);
                            } else {
                                ToastUtil.showLongToast(R.string.coupon_over_count_error);
                                currentVo.setSelected(false);
                            }
                        } else {
                                                        ShoppingCart.getInstance().setCouponPrivilege(mCouponsManager.getCouponPrivilegeVo(currentVo));
                            mSelectedCpupons.add(currentVo);
                        }

                    } else {
                        removeLastCouponFromShoppingCart(currentVo);
                    }
                    updateSelectedView(currentVo);
                }
            }
        }
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

    public void registerListener(ChangePageListener mChangePageListener) {
        this.mChangePageListener = mChangePageListener;
    }



    private void getCouponInfoS() {

        ResponseListener<MemberCouponsVoResp> listener = new ResponseListener<MemberCouponsVoResp>() {

            @Override
            public void onResponse(ResponseObject<MemberCouponsVoResp> response) {
                try {
                    if (ResponseObject.isOk(response) && MemberCouponsVoResp.isOk(response.getContent())) {
                        MemberCouponsResp resp = response.getContent().getResult();
                                                List<CustomerCouponResp> couponInfo = resp.getItems();
                        if (couponInfo != null && !couponInfo.isEmpty()) {
                            mCouponsManager.loadData(couponInfo);
                            couponNothingAlter.setVisibility(GONE);
                            return;
                        }
                    } else {
                        ToastUtil.showShortToast(response.getMessage());
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                couponNothingAlter.setVisibility(VISIBLE);
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
                couponNothingAlter.setVisibility(VISIBLE);
            }
        };
        if (mCustomer != null) {
            CustomerManager.getInstance().getCustomerCoupons(mCustomer.customerId, 1, Integer.MAX_VALUE, listener);
        }
    }

    public void lodata() {
        mCouponsLL.removeAllViews();
        mCustomer = CustomerManager.getInstance().getLoginCustomer();
        couponNothingAlter.setVisibility(GONE);
        if (mCustomer != null) {
            getCouponInfoS();
        } else {
            couponNothingAlter.setVisibility(VISIBLE);
        }
    }

    public void onEventMainThread(EventMemeberIsLogin event) {
        if (event != null) {
            if (!event.isLogin()) {
                mCouponsLL.removeAllViews();
                couponNothingAlter.setVisibility(VISIBLE);
            } else {                if (this.getVisibility() == VISIBLE) {
                    lodata();
                }
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        registerEventBus();
    }

    @Override
    protected void onDetachedFromWindow() {
        unregisterEventBus();
        super.onDetachedFromWindow();
    }


    protected void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    protected void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
