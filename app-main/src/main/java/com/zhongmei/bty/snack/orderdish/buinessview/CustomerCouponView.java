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

/**
 * Created by demo on 2018/12/15
 */
/*@EViewGroup(R.layout.customer_coupons_fragment)*/
public class CustomerCouponView extends LinearLayout implements CouponsLayoutView.CouponSelectedCallback, View.OnClickListener {
    private static final String TAG = CustomerCouponView.class.getSimpleName();

    /*  @ViewById(R.id.coupons_view)*/
    LinearLayout mCouponsLL;// 优惠券显示 布局

    /* @ViewById(R.id.coupon_nothing_alter)*/
    LinearLayout couponNothingAlter;

    private CustomerOperates operates;

    private CustomerResp mCustomer;

    private ChangePageListener mChangePageListener;

    private CouponsManager mCouponsManager;

    private Context mContext;

    private CouponsLayoutView rebateView;// 满减券

    private CouponsLayoutView discountView;// 折扣券

    private CouponsLayoutView giftView;// 礼品券

    private CouponsLayoutView cashView;// 现金券

    private CouponVo mLastSeletedCouponVo;

    private FragmentManager mFragmentManager;

    /*private CouponVo mLastSeletedCouponVo;*/
    private Set<CouponVo> mSelectedCpupons = new HashSet<CouponVo>();//已经选择的券

    private int mCashNum = 1;//代金券可使用数量

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
        //获取代金券数量设置项
        LoadSettingeAsyncTask loadSettingeAsyncTask = new LoadSettingeAsyncTask();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            loadSettingeAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else {
            loadSettingeAsyncTask.execute();
        }

    }

    /**
     * 获取设置项
     */
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

    /**
     * @Title: onEventMainThread
     * @Description: 从购物车删除优惠劵, 刷新UI
     * @Param
     * @Return void 返回类型
     */
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

    /**
     * @Title: onEventMainThread
     * @Description: 从购物车删除积分抵现, 刷新UI
     * @Param
     * @Return void 返回类型
     */
    public void onEventMainThread(EventShopcartDelIntergral event) {
//		tvIntergralToCash.setSelected(false);
    }

    /**
     * CouponsManager 返回查询结果
     *
     * @Title: onEventMainThread
     * @Description: TODO
     * @Param @param eventdata TODO
     * @Return void 返回类型
     */
    public void onEventMainThread(EventCouponVoResult eventdata) {
        mLastSeletedCouponVo = eventdata.getSelectedCouponVo();
        mCouponsLL.removeAllViews();
        mSelectedCpupons.clear();
        mSelectedCpupons.addAll(eventdata.getSelectedCoupons());
        // 如果有代金卷
        if (eventdata.getCashCoupons() != null) {
            cashView = CouponsLayoutView_.build(mContext, this, CouponType.CASH);
            cashView.setData(eventdata.getCashCoupons(), 1);
            mCouponsLL.addView(cashView);
        } // 如果有满减劵
        if (eventdata.getRebateCoupons() != null) {
            rebateView = CouponsLayoutView_.build(mContext, this, CouponType.REBATE);
            rebateView.setData(eventdata.getRebateCoupons(), 1);
            mCouponsLL.addView(rebateView);
        } // 如果有折扣卷
        if (eventdata.getDiscountCoupons() != null) {
            discountView = CouponsLayoutView_.build(mContext, this, CouponType.DISCOUNT);
            discountView.setData(eventdata.getDiscountCoupons(), 1);
            mCouponsLL.addView(discountView);
        } // 如果有礼品卷
        if (eventdata.getGiftCoupons() != null) {
            giftView = CouponsLayoutView_.build(mContext, this, CouponType.GIFT);
            giftView.setData(eventdata.getGiftCoupons(), 1);
            mCouponsLL.addView(giftView);
        }
        if (mCouponsLL.getChildCount() == 0) {
            couponNothingAlter.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 判断是否是礼品券
     */
    private boolean isTradeItemGiftCoupon(CouponVo currentVo) {
        if (currentVo.getDishId() != null && currentVo.getDishId().longValue() != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 移除上次选择的优惠劵从购物车
     *
     * @param currentVo
     */
    private void removeLastCouponFromShoppingCart(CouponVo currentVo) {
        currentVo.setSelected(false);
        updateSelectedView(currentVo);
        // 从购物车移除
        ShoppingCart.getInstance().removeCouponPrivilege(mCouponsManager.getCouponPrivilegeVo(currentVo), true);
        mSelectedCpupons.remove(currentVo);
    }

    //判断礼品券的商品是否可用
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

    /**
     * 移除整单类型券
     */
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
        // 从购物车移除
        ShoppingCart.getInstance().removeCouponPrivilege();
    }


    /*
     * 点击item 时回调
     */
    @Override
    public void onCouponSelected(CouponVo currentVo, CouponType type) {
        if (ShoppingCart.getInstance().getIsSalesReturn()) {
            ToastUtil.showShortToast(R.string.refeund_cannot_user_coupon);
            return;
        }

        //如果已经选择做删除操作
        if (mSelectedCpupons.contains(currentVo)) {
            // 如果是单菜礼品券
            if (isTradeItemGiftCoupon(currentVo)) {
                currentVo.setSelected(false);
                updateSelectedView(currentVo);
                ShoppingCart.getInstance().removeGiftCouponePrivilege(currentVo.getCouponInfo().getId(), ShoppingCart.getInstance().getShoppingCartVo(), false);
                mSelectedCpupons.remove(currentVo);
            } else {
                removeLastCouponFromShoppingCart(currentVo);
            }

        } else {
//            String boundWxCardNumber = currentVo.getCouponInfo().getBoundWxCardNumber();
//            if (!TextUtils.isEmpty(boundWxCardNumber) && ShoppingCart.getInstance().checkIsHaveWXC(boundWxCardNumber)) {
//                ToastUtil.showShortToast(R.string.coupons_has_existed);
//                return;
//            }

            //如果没有就新加
            if (mSelectedCpupons.isEmpty()) {
                if (currentVo.isSelected()) {
                    if (currentVo.getCouponInfo().getCouponType() == CouponType.GIFT && isTradeItemGiftCoupon(currentVo)) {
                        CouponPrivilegeVo couponPrivilegeVo = mCouponsManager.getCouponPrivilegeVo(currentVo);
                        //礼品券对应的商品不可用
                        if (!isGiftCouponEnable(currentVo, couponPrivilegeVo)) {
                            return;
                        }
                        ShoppingCart.getInstance().setCouponPrivilege(mCouponsManager.getCouponPrivilegeVo(currentVo));
                        mSelectedCpupons.add(currentVo);
                    } else {
                        // 添加到购物车
                        ShoppingCart.getInstance().setCouponPrivilege(mCouponsManager.getCouponPrivilegeVo(currentVo));
                        mSelectedCpupons.add(currentVo);
                    }
                }
                updateSelectedView(currentVo);
            } else {//切换券
                if (currentVo.getCouponInfo().getCouponType() == CouponType.GIFT && isTradeItemGiftCoupon(currentVo)) {
                    if (currentVo.isSelected()) {
                        CouponPrivilegeVo couponPrivilegeVo = mCouponsManager.getCouponPrivilegeVo(currentVo);
                        //礼品券对应的商品不可用
                        if (!isGiftCouponEnable(currentVo, couponPrivilegeVo)) {
                            return;
                        }
                        ShoppingCart.getInstance().setCouponPrivilege(mCouponsManager.getCouponPrivilegeVo(currentVo));
                        mSelectedCpupons.add(currentVo);

                        updateSelectedView(currentVo);
                    }
                } else {
                    if (!ShoppingCart.getInstance().isAllowAddCoupon(mCouponsManager.getCouponPrivilegeVo(currentVo))) {
                        //移除整单优惠券
                        removeAllTradeCoupons();
                    }
                    if (currentVo.isSelected()) {
                        if (currentVo.getCouponInfo().getCouponType() == CouponType.CASH) {
                            if (mSelectedCpupons.size() < mCashNum) {//判断数量上限
                                // 添加到购物车
                                ShoppingCart.getInstance().setCouponPrivilege(mCouponsManager.getCouponPrivilegeVo(currentVo));

                                mSelectedCpupons.add(currentVo);
                            } else {
                                ToastUtil.showLongToast(R.string.coupon_over_count_error);
                                currentVo.setSelected(false);
                            }
                        } else {
                            // 添加到购物车
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

    /**
     * 根据item 更新gridview
     *
     * @Title: updateSelectedView
     * @Description: TODO
     * @Param @param vo TODO
     * @Return void 返回类型
     */
    private void updateSelectedView(CouponVo vo) {
        if (vo == null)
            return;
        switch (vo.getCouponInfo().getCouponType()) {
            case REBATE:// 满减券
                if (rebateView != null)
                    rebateView.updateViews();
                break;

            case DISCOUNT:// 折扣券
                if (discountView != null) {
                    discountView.updateViews();
                }
                break;
            case GIFT:// 礼品券
                if (giftView != null) {
                    giftView.updateViews();
                }
                break;
            case CASH:// 代金卷
                if (cashView != null) {
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

    /**
     * 根据手机号查询会员优惠劵信息
     *
     * @Title: getCouponInfoS
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */

    private void getCouponInfoS() {

        ResponseListener<MemberCouponsVoResp> listener = new ResponseListener<MemberCouponsVoResp>() {

            @Override
            public void onResponse(ResponseObject<MemberCouponsVoResp> response) {
                try {
                    if (ResponseObject.isOk(response) && MemberCouponsVoResp.isOk(response.getContent())) {
                        MemberCouponsResp resp = response.getContent().getResult();
                        // 异步加载界面
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
            } else {//优惠券VIEW显示时才更新
                if (this.getVisibility() == VISIBLE) {
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
