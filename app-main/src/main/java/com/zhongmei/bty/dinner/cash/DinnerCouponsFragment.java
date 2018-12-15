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

/**
 * @Date：2015-10-30 下午1:55:11
 * @Description: 预支付界面优惠券
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
@EFragment(R.layout.fragment_dinner_coupons)
public class DinnerCouponsFragment extends BasicFragment implements CouponSelectedCallback {
    private static final String TAG = DinnerCouponsFragment.class.getSimpleName();
    private int CASH_COUPON_COUNT = 1;
    /*@ViewById(R.id.btn_general_coupon)
    Button generalCouponBT;

	@ViewById(R.id.btn_customer_coupon)
	Button customerCouponBT;*/

    @ViewById(R.id.btn_close)
    Button closeBT;

	/*@ViewById(R.id.ll_coupon_status_unenable)
    LinearLayout unEnableLL;// 不可用
	
	@ViewById(R.id.ll_coupon_status_enable)
	LinearLayout enableLL;// 可用
	
	@ViewById(R.id.ll_coupon_status_timeout)
	LinearLayout timeOutLL;// 失效*/

    @ViewById(R.id.coupons_view)
    LinearLayout mCouponsLL;// 优惠券显示 布局

    private Context mContext;

    private final int COUPON_TYPE_GENERAL = 0;

    private final int COUPON_TYPE_CUSTOMER = 1;

    private final int COUPON_PAGE_1 = 1;// 不可用

    private final int COUPON_PAGE_2 = 2;// 可用

    private final int COUPON_PAGE_3 = 3;// 失效

    private CouponsLayoutView rebateView;// 满减券

    private CouponsLayoutView discountView;// 折扣券

    private CouponsLayoutView giftView;// 礼品券

    private CouponsLayoutView cashView;// 现金券

    /*private CouponVo mLastSeletedCouponVo;*/
    private Set<CouponVo> mSelectedCpupons = new HashSet<CouponVo>();//已经选择的券
    private CustomerOperates operates;

    private CouponsManager mCouponsManager;

    // private DinnerShoppingCart mDinnerShoppingCart;

    private int mCouponType = COUPON_TYPE_CUSTOMER;

    private int mCouponsPage = COUPON_PAGE_2;

    private FragmentManager mFragmentManager;

    private CalmLoadingDialogFragment mDialogFragment = null;

    @AfterViews
    void init() {
        mContext = getActivity();
        mFragmentManager = getActivity().getSupportFragmentManager();
        // mDinnerShoppingCart = DinnerShopManager.getInstance().getShoppingCart();// SeparateShoppingCart.getInstance();
        operates = OperatesFactory.create(CustomerOperates.class);
        mCouponsManager = new CouponsManager();
        CASH_COUPON_COUNT = SystemSettingsManager.getCashCouponNum();
       /* updateCouponsType();
        updatePagerTitle();*/
        registerEventBus();
        loadCoupons();
    }

   /* private void updateCouponsType() {
        if (mCouponType == COUPON_TYPE_GENERAL) {
            showGenneralCoupons();
        } else {
            showCustomerCoupons();
        }
    }*/

	/*private void updatePagerTitle() {
        if (mCouponsPage == COUPON_PAGE_1) {
			unEnableLL.setSelected(true);
			enableLL.setSelected(false);
			timeOutLL.setSelected(false);
			
		} else if (mCouponsPage == COUPON_PAGE_2) {
			unEnableLL.setSelected(false);
			enableLL.setSelected(true);
			timeOutLL.setSelected(false);
			
		} else if (mCouponsPage == COUPON_PAGE_3) {
			unEnableLL.setSelected(false);
			enableLL.setSelected(false);
			timeOutLL.setSelected(true);
		}
	}*/

/*	private void onClickEnable(boolean enabled) {
        unEnableLL.setEnabled(enabled);
		enableLL.setEnabled(enabled);
		timeOutLL.setEnabled(enabled);
		generalCouponBT.setEnabled(enabled);
		customerCouponBT.setEnabled(enabled);
	}*/

	/*private void showGenneralCoupons() {
        generalCouponBT.setSelected(true);
		customerCouponBT.setSelected(false);
	}*/

	/*private void showCustomerCoupons() {
        generalCouponBT.setSelected(false);
		customerCouponBT.setSelected(true);
	}*/

    @Click({R.id.btn_close})
    void click(View v) {
        switch (v.getId()) {
            /*case R.id.btn_general_coupon:
                mCouponType = COUPON_TYPE_GENERAL;
				updateCouponsType();
				loadCoupons();
				break;
			
			case R.id.btn_customer_coupon:
				mCouponType = COUPON_TYPE_CUSTOMER;
				updateCouponsType();
				loadCoupons();
				break;*/

            case R.id.btn_close:
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.PRIVILIGE_ITEMS));
                break;

            /*case R.id.ll_coupon_status_unenable:
                mCouponsPage = COUPON_PAGE_1;
                updatePagerTitle();
                loadCoupons();
                break;
            case R.id.ll_coupon_status_enable:
                mCouponsPage = COUPON_PAGE_2;
                updatePagerTitle();
                loadCoupons();
                break;
            case R.id.ll_coupon_status_timeout:
                mCouponsPage = COUPON_PAGE_3;
               // updatePagerTitle();
                loadCoupons();
                break;*/
        }
    }

    /**
     * @Title: onEventMainThread
     * @Description: 从购物车删除优惠劵, 刷新UI
     * @Param
     * @Return void 返回类型
     */
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

    //移除上次选择的优惠劵从购物车
    private void removeLastCouponFromShoppingCart(CouponVo currentVo) {
        updateSelectedView(currentVo);
        // 从购物车移除
        DinnerShoppingCart dinnerShoppingCart = DinnerShopManager.getInstance().getShoppingCart();
        List<Long> promeIdList = new ArrayList<Long>();
        promeIdList.add(currentVo.getCouponInfo().getId());
        dinnerShoppingCart.removeCouponPrivilege(dinnerShoppingCart.getShoppingCartVo(), promeIdList, true);
        mSelectedCpupons.remove(currentVo);
    }

    //移除整单类型券
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

    /*
     * 点击item 时回调
     */
    @Override
    public void onCouponSelected(CouponVo currentVo, CouponType type) {
        //add v9.0 start 自助联台单暂不支持商品券
        if (currentVo.getCouponInfo().getCouponType() == CouponType.GIFT && isTradeItemGiftCoupon(currentVo)) {
            Trade trade = DinnerShopManager.getInstance().getShoppingCart().getShoppingCartVo().getmTradeVo().getTrade();
            if (trade != null && trade.getBusinessType() == BusinessType.BUFFET && trade.getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
                ToastUtil.showShortToast(R.string.union_trade_not_suport);
                return;
            }
        }
        //add v9.0 start end
        //如果已经选择做删除炒作
        if (mSelectedCpupons.contains(currentVo)) {
            // 如果是单菜礼品券
            if (isTradeItemGiftCoupon(currentVo)) {
                ShoppingCartVo shoppingCartVo = DinnerShopManager.getInstance().getShoppingCart().getShoppingCartVo();
                DinnerShopManager.getInstance().getShoppingCart().removeGiftCouponePrivilege(mCouponsManager.getCouponPrivilegeVo(currentVo).getTradePrivilege().getPromoId(), shoppingCartVo, true);
                updateSelectedView(currentVo);
                mSelectedCpupons.remove(currentVo);
            } else {
                removeLastCouponFromShoppingCart(currentVo);
            }

        } else {
//            String boundWxCardNumber = currentVo.getCouponInfo().getBoundWxCardNumber();
//            if (!TextUtils.isEmpty(boundWxCardNumber) && DinnerShoppingCart.getInstance().checkIsHaveWXC(boundWxCardNumber)) {
//                ToastUtil.showShortToast(R.string.coupons_has_existed);
//                return;
//            }

            //如果没有就新加
            //if (mSelectedCpupons.isEmpty())
            //{
            if (currentVo.getCouponInfo().getCouponType() == DISCOUNT) {
                if (mSelectedCpupons.isEmpty()) {
                    // 添加到购物车
                    DinnerShoppingCart dinnerShoppingCart = DinnerShopManager.getInstance().getShoppingCart();
                    if (BigDecimal.ZERO.compareTo(dinnerShoppingCart.getShoppingCartVo().getmTradeVo().getTrade().getTradeAmount()) == 0) {
                        ToastUtil.showLongToast(R.string.dinner_order_amount_zero_error);
                        currentVo.setSelected(false);
                        return;
                    }
                    DinnerShopManager.getInstance().getShoppingCart().setCouponPrivilege(mCouponsManager.getCouponPrivilegeVo(currentVo), true, true);
                    mSelectedCpupons.add(currentVo);
                } else {
                    if (hasDiscountCoupon()) { //切换券
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
                            //移除整单优惠券
                            removeAllTradeCoupons();
                        }
                        if (couponPrivilegeVo.getCoupon().getCouponType() == CouponType.CASH) {
                            //如果是代金券判断张数
                            if (getDefineCouponCount() >= CASH_COUPON_COUNT) {
                                currentVo.setSelected(false);
                                ToastUtil.showLongToast(R.string.coupon_over_count_error);
                                return;
                            }
                        }
                        if (currentVo.isSelected()) {
                            // 添加到购物车
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
                        //礼品券对应的商品不可用
                        if (!isGiftCouponEnable(currentVo, couponPrivilegeVo)) {

                            return;
                        }
                        boolean isOk = DinnerShopManager.getInstance().getShoppingCart().setGiftCouponPrivilege(couponPrivilegeVo);
                        //如果添加成功
                        if (isOk) {
                            mSelectedCpupons.add(currentVo);
                        } else {
                            currentVo.setSelected(false);
                            ToastUtil.showLongToast(this.getString(R.string.dinner_shopcart_Coupon_canot_use));
                        }
                    } else {
                        // 添加到购物车
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
            /*
            else {//切换券
                if (currentVo.getCoupon().getCouponType() == CouponType.GIFT && isTradeItemGiftCoupon(currentVo)) {
                    if (currentVo.isUsed()) {
                        CouponPrivilegeVo couponPrivilegeVo = mCouponsManager.getCouponPrivilegeVo(currentVo);
                        //礼品券对应的商品不可用
                        if (!isGiftCouponEnable(currentVo, couponPrivilegeVo)) {

                            return;
                        }
                        boolean isOk = DinnerShopManager.getInstance().getShoppingCart().setGiftCouponPrivilege(couponPrivilegeVo);
                        //如果不是赠菜只能使用一张
                        if (isOk) {
                            mSelectedCpupons.add(currentVo);
                        } else {
                            currentVo.setSelected(false);
                            ToastUtil.showLongToast(this.getString(R.string.dinner_shopcart_Coupon_canot_use));
                        }
                        updateSelectedView(currentVo);
                    }
                } else {

                    CouponPrivilegeVo couponPrivilegeVo=mCouponsManager.getCouponPrivilegeVo(currentVo);
                    if(couponPrivilegeVo.getCoupon()==null){
                        return;
                    }
                    DinnerShoppingCart dinnerShoppingCart=DinnerShopManager.getInstance().getShoppingCart();
                    if(BigDecimal.ZERO.compareTo(dinnerShoppingCart.getShoppingCartVo().getmTradeVo().getTrade().getTradeAmount())==0){
                        ToastUtil.showLongToast(R.string.dinner_order_amount_zero_error);
                        currentVo.setSelected(false);
                        return;
                    }
                    boolean isAllowAdd=dinnerShoppingCart.isAllowAddCoupon(dinnerShoppingCart.getShoppingCartVo(),couponPrivilegeVo);
                    if(!isAllowAdd){
                    //移除整单优惠券
                    removeAllTradeCoupons();
                    }
                    if(couponPrivilegeVo.getCoupon().getCouponType()==CouponType.CASH){
                        //如果是代金券判断张数
                        if(getDefineCouponCount()>=CASH_COUPON_COUNT){
                            currentVo.setSelected(false);
                            ToastUtil.showLongToast(R.string.coupon_over_count_error);
                            return;
                        }
                    }
                    if (currentVo.isUsed()) {
                        // 添加到购物车
                        DinnerShopManager.getInstance().getShoppingCart().setCouponPrivilege(couponPrivilegeVo, true, true);

                        mSelectedCpupons.add(currentVo);
                    }
                    updateSelectedView(currentVo);
                }
            }*/
        //}
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

    /**
     * 获取整单优惠劵选择的数量
     */
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

    /**
     * CouponsManager 返回查询结果
     *
     * @Title: onEventMainThread
     * @Description: TODO
     * @Param @param eventdata TODO
     * @Return void 返回类型
     */
    public void onEventMainThread(EventCouponVoResult eventdata) {
        // onClickEnable(true);

        mCouponsLL.removeAllViews();
        mSelectedCpupons.clear();
        mSelectedCpupons.addAll(eventdata.getSelectedCoupons());
        boolean isNothing = true;
        // 如果有代金卷
        if (eventdata.getCashCoupons() != null) {
            isNothing = false;
            cashView = CouponsLayoutView_.build(mContext, this, CouponType.CASH);
            cashView.setData(eventdata.getCashCoupons(), 1);
            mCouponsLL.addView(cashView);
        } // 如果有满减劵
        if (eventdata.getRebateCoupons() != null) {
            isNothing = false;
            rebateView = CouponsLayoutView_.build(mContext, this, CouponType.REBATE);
            rebateView.setData(eventdata.getRebateCoupons(), 1);
            mCouponsLL.addView(rebateView);
        } // 如果有折扣卷
        if (eventdata.getDiscountCoupons() != null) {
            isNothing = false;
            discountView = CouponsLayoutView_.build(mContext, this, DISCOUNT);
            discountView.setData(eventdata.getDiscountCoupons(), 1);
            mCouponsLL.addView(discountView);
        } // 如果有礼品卷
        if (eventdata.getGiftCoupons() != null) {
            isNothing = false;
            giftView = CouponsLayoutView_.build(mContext, this, CouponType.GIFT);
            giftView.setData(eventdata.getGiftCoupons(), 1);
            mCouponsLL.addView(giftView);
        }
        // 如果没有优惠劵，提醒用户
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
                        //显示第二屏信息
                        Long integral = customer.integral;
                        if (integral == null) {
                            integral = 0L;
                        }
                        /*DisplayUserInfo uinfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW, customer, integral, false, 0);
                        DisplayServiceManager.updateDisplay(mContext, uinfo);*/
                    }
                    // 异步加载界面
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


//
//        ResponseListener<CustomerInfoResp> listener = new ResponseListener<CustomerInfoResp>() {
//
//            @Override
//            public void onResponse(ResponseObject<CustomerInfoResp> response) {
//                try {
//                    if (ResponseObject.isOk(response)) {
//                        CustomerInfoResp resp = response.getContent();
//                        if(customer.isMember()) {
//                            //显示第二屏信息
//                            DisplayUserInfo uinfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW, customer, resp == null ? 0 : resp.getIntegral(), false, 0);
//                            DisplayServiceManager.updateDisplay(mContext, uinfo);
//                        }
//                        // 异步加载界面
//                        mCouponsManager.loadData(resp.getCoupons());
//                    } else {
//                        //onClickEnable(true);
//                        dismissLoadingDialog();
//                        ToastUtil.showShortToast(response.getMessage());
//                    }
//                } catch (Exception e) {
//                    //onClickEnable(true);
//                    dismissLoadingDialog();
//                    Log.e(TAG, "", e);
//                }
//            }
//
//            @Override
//            public void onError(VolleyError error) {
//                dismissLoadingDialog();
//                // onClickEnable(true);
//                ToastUtil.showShortToast(error.getMessage());
//            }
//        };
//        if (customer != null) {
//            CustomerInfoReq req = new CustomerInfoReq();
//            if(!TextUtils.isEmpty(customer.get(Customer.MOBILE_KEY))){
//                req.setMobile(customer.get(Customer.MOBILE_KEY));
//            }else{
//                Long customerId = customer.getLong(Customer.ID_KEY);
//                Customer tempCustomer =  CustomerManager.getInstance().getCustomerById(customerId.toString());
//                if(tempCustomer!=null && tempCustomer.get(Customer.CUSTOMER_MAIN_ID_KEY)!=null){
//                    req.setCustomerId(tempCustomer.getLong(Customer.CUSTOMER_MAIN_ID_KEY));
//                }else{
//                    req.setCustomerId(customerId);
//                }
//            }
//            req.setIsCustomer(customer.isMember()? 2 : 1);
//            // onClickEnable(false);
//            operates.findCustomerInfo(req, listener);
//            showLoadingDialog();

//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // loadCoupons();
    }
}
