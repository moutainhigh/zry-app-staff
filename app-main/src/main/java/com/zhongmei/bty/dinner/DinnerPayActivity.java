package com.zhongmei.bty.dinner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.base.MainBaseActivity;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.discount.event.ActionDinnerPrilivige;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.mobilepay.enums.PayActionPage;
import com.zhongmei.bty.mobilepay.event.SeparateEvent;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.SeparateShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.listerner.ShoppingAsyncListener;
import com.zhongmei.bty.basemodule.trade.bean.TakeOutInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.trade.event.ActionCloseOrderDishActivity;
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.yunfu.context.util.JsonUtil;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListener;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListerTag;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.util.UserActionCode;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.view.CalmLoadingDialogFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.bty.customer.customerarrive.CouponListFragment;
import com.zhongmei.bty.customer.customerarrive.CouponListFragment_;
import com.zhongmei.bty.dinner.action.ActionDinnerBatchDiscount;
import com.zhongmei.bty.dinner.action.ActionRefreshDinnerCustomer;
import com.zhongmei.bty.dinner.cash.DinnerCouponsFragment_;
import com.zhongmei.bty.dinner.cash.DinnerCustomerLoginDialogFragment_;
import com.zhongmei.bty.dinner.cash.DinnerCustomerLoginDialogV2;
import com.zhongmei.bty.dinner.cash.DinnerDiscountFragment_;
import com.zhongmei.bty.dinner.cash.DinnerExtraChargeFragment;
import com.zhongmei.bty.dinner.cash.DinnerExtraChargeFragment_;
import com.zhongmei.bty.dinner.cash.DinnerIntegralToCashFragment_;
import com.zhongmei.bty.dinner.cash.DinnerMarketActivityFragment;
import com.zhongmei.bty.dinner.cash.DinnerMarketActivityFragment_;
import com.zhongmei.bty.dinner.cash.DinnerPayTotalFragment;
import com.zhongmei.bty.dinner.cash.DinnerPayTotalFragment_;
import com.zhongmei.bty.dinner.cash.DinnerPriviligeItemsFragment;
import com.zhongmei.bty.dinner.cash.DinnerPriviligeItemsFragment_;
import com.zhongmei.bty.dinner.cash.DinnerRemarkFragment;
import com.zhongmei.bty.dinner.cash.DinnerRemarkFragment_;
import com.zhongmei.bty.dinner.cash.DinnerSwitchCardFragment;
import com.zhongmei.bty.dinner.cash.DinnerSwitchCardFragment_;
import com.zhongmei.bty.dinner.cash.DinnerWeixinCouponCodeFragment;
import com.zhongmei.bty.dinner.cash.DinnerWeixinCouponCodeFragment_;
import com.zhongmei.bty.dinner.cash.PayMainLeftFragment;
import com.zhongmei.bty.dinner.cash.PayMainLeftFragment_;
import com.zhongmei.bty.dinner.orderdish.TitleBarFragment;
import com.zhongmei.bty.dinner.orderdish.TitleBarFragment_;
import com.zhongmei.bty.mobilepay.IPayConstParame;
import com.zhongmei.bty.pay.fragment.MainPayFragment;
import com.zhongmei.bty.pay.fragment.MainPayFragment_;
import com.zhongmei.bty.pay.utils.PayUtils;
import com.zhongmei.bty.mobilepay.v1.event.ClosePayViewEvent;
import com.zhongmei.bty.mobilepay.v1.event.EventPayResult;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 正餐支付
 *
 * @date:2015年9月18日下午2:21:18
 */
@EActivity(R.layout.dinner_pay_activity)
public class DinnerPayActivity extends MainBaseActivity implements IPayConstParame {

    private final static String TAG = DinnerPayActivity.class.getSimpleName();

    @ViewById(R.id.view_shape)
    View view_shape;
    //    支付单的遮罩
    @ViewById(R.id.view_left_shape)
    View view_left_shape;
    //总单的遮罩
    @ViewById(R.id.view_privilege_page_shape)
    View view_privilege_shape;

    private PayMainLeftFragment payMainLeftFragment;

    private DinnerPriviligeItemsFragment payPrivilegeFragment;

    private MainPayFragment mPayFragment;
    //总单显示的fragment
    private DinnerPayTotalFragment totalFragment;

    @Extra("isOrderCenter")
    protected boolean isOrderCenter;
    @Extra("isOrdered")
    protected boolean isOrdered;//是否已经下单
    private DinnerShoppingCart mShoppingCart;

    private SeparateShoppingCart mSeparateShoppingCart;
    private SeparateEvent separateEvent;
    private boolean isFromOpenPlatform = false;//是否来自开放平台 add v8.5
    private String mPartnerId;//是否来自开放平台 add v8.5
//    @ViewById(R.id.rightLoading)
//    public LoadingView rightPageLoading;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// 隐藏软键盘
        }
        MobclickAgentEvent.openActivityDurationTrack(false);
        mShoppingCart = DinnerShoppingCart.getInstance();
        mSeparateShoppingCart = SeparateShoppingCart.getInstance();
        DinnerShopManager.getInstance()._register(asyncListener);
        payMainLeftFragment = new PayMainLeftFragment_();
        payPrivilegeFragment = new DinnerPriviligeItemsFragment_();
        mPayFragment = new MainPayFragment_();
    }

    ShoppingAsyncListener asyncListener = new ShoppingAsyncListener() {

        @Override
        public void onMultiTerminalUpdate(String hint) {
            DialogUtil.showConfirmDialog(getSupportFragmentManager(), CommonDialogFragment.ICON_WARNING, hint,
                    R.string.dinner_back_desk, new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().post(new ActionCloseOrderDishActivity());
                            DinnerPayActivity.this.finish();
                        }
                    }, true, "MultiTerminalUpdate");
        }

        @Override
        public void onAsyncUpdate() {
        }
    };

    @AfterViews
    void initView() {
        //先设置登录会员
        DinnerShopManager.getInstance().setLoginCustomerFromShoppingCart();
        registerEventBus();
        //modify v8.5 begin  对接开放平台结账
        Bundle extras_ = getIntent().getExtras();
        if (extras_ != null) {
            if (extras_.containsKey(EXTRA_PARTNER_ID)) {
                this.mPartnerId = extras_.getString(EXTRA_PARTNER_ID);
            }
            if (extras_.containsKey(EXTRA_TRADE_INFO)) {
                String tradeVo_json = extras_.getString(EXTRA_TRADE_INFO);
                initViewByTradeInfo(tradeVo_json);
            } else if (extras_.containsKey(EXTRA_TRADE_ID)) {
                Long tradeId = extras_.getLong(EXTRA_TRADE_ID);
                initViewByTradeId(tradeId);
            } else {
                showViews();
            }
            setActivityResult();
        } else {
            showViews();
        }
        //modify v8.5 end  对接开放平台结账
    }

    /**
     * 是否是自助订单
     *
     * @return
     */
    private boolean isBuffet() {
        Trade trade = mShoppingCart.getOrder().getTrade();
        if ((trade.getBusinessType() == BusinessType.BUFFET) && (trade.getTradePayStatus() == TradePayStatus.PREPAID)) {
            return true;
        }
        return false;
    }

    //    @Override
//    protected void onInit() {
//        replaceTitleLayout();//延迟加载title 20170120
//    }
    @UiThread
    public void buildView() {
        if (!payMainLeftFragment.isAdded()) {
            addFragment(R.id.orderDishLeftView, payMainLeftFragment, payMainLeftFragment.getClass().getSimpleName());
        } else {
            replaceFragment(R.id.orderDishLeftView, payMainLeftFragment, payMainLeftFragment.getClass().getSimpleName(), false);
        }

        if (!payPrivilegeFragment.isAdded()) {
            addFragment(R.id.orderDishPrivilegeView, payPrivilegeFragment, payPrivilegeFragment.getClass().getSimpleName());
        } else {
            replaceFragment(R.id.orderDishPrivilegeView, payPrivilegeFragment, payPrivilegeFragment.getClass().getSimpleName(), false);
        }

        // 支付界面

        if (!mPayFragment.isAdded()) {
            addFragment(R.id.orderDishRightView, mPayFragment, mPayFragment.getClass().getSimpleName());
        } else {
            replaceFragment(R.id.orderDishRightView, mPayFragment, mPayFragment.getClass().getSimpleName(), false);
        }

        mShoppingCart.registerListener(ShoppingCartListerTag.DINNER_DOPAY_SPLIT, mModifyShoppingCartListener);
        mSeparateShoppingCart.registerListener(ShoppingCartListerTag.DINNER_DOPAY_SPLIT, mModifyShoppingCartListener);
    }

    //支付界面初始化参数
    private Bundle createArguments() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isOrderCenter", isOrderCenter);
        bundle.putBoolean("isOrdered", isOrdered);
        bundle.putBoolean("isSplit", false);
        bundle.putBoolean("isFromOpenPlatform", isFromOpenPlatform);
        bundle.putSerializable("tradeVo", DinnerShoppingCart.getInstance().createOrder());
        bundle.putSerializable(EXTRA_PAY_ACTION_PAGE, PayActionPage.BALANCE);  //add 20180309
        return bundle;
    }
    //add v8.5 begin  对接开放平台结账

    private void showViews() {
        Bundle bundle = createArguments();
        mPayFragment.setArguments(bundle);
        if (isFromOpenPlatform) {//开放平台不支持拆单
            payMainLeftFragment.setEnableSplitTrade(false);
        }
        buildView();
        //自助订单运行分布支付后操作
        if (isPaying() || isBuffet()) {
            showPayingView();
        }
        replaceTitleLayout();
    }


    //根据tradeVo json 来初始化结算界面
    private void initViewByTradeInfo(String tradeInfo) {
        try {
            TradeVo tradeVo = JsonUtil.jsonToObject(tradeInfo, TradeVo.class);
            if (tradeVo != null) {
                isFromOpenPlatform = true;
                mShoppingCart.clearShoppingCart();
                mShoppingCart.updateTradeVoNoTradeInfo(tradeVo);
                showViews();
                PayUtils.saveTradeVoAsync(tradeVo);
            } else {
                errorAlert();
            }
        } catch (Exception e) {
            errorAlert();
        }
    }

    //根据tradeid 先查询tradeVo 再始化结算界面
    private void initViewByTradeId(final Long tradeId) {
        new AsyncTask<Void, Void, TradeVo>() {
            private TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
            private CalmLoadingDialogFragment mDialogFragment = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //显示咖啡杯
                mDialogFragment = CalmLoadingDialogFragment.show(getSupportFragmentManager());
            }

            @Override
            protected TradeVo doInBackground(Void... params) {

                TradeVo tradeVo = null;
                try {
                    tradeVo = tradeDal.findTrade(tradeId);
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
                return tradeVo;
            }

            protected void onPostExecute(TradeVo tradeVo) {
                if (tradeVo != null) {
                    isFromOpenPlatform = true;
                    mShoppingCart.clearShoppingCart();
                    mShoppingCart.updateTradeVoNoTradeInfo(tradeVo);
                    showViews();
                    CalmLoadingDialogFragment.hide(mDialogFragment);
                } else {
                    errorAlert();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void errorAlert() {
        ToastUtil.showLongToast(getString(R.string.toast_data_error));
        this.finish();
    }
    //add v8.5 end
    /*
    public LoadingFinish leftLoadingFinish = new LoadingFinish() {
        @Override
        public void loadingFinish() {
            leftPageLoading.setVisibility(View.GONE);
        }
    };
    */

//    public LoadingFinish rightLoadingFinish = new LoadingFinish() {
//        @Override
//        public void loadingFinish() {
//            rightPageLoading.setVisibility(View.GONE);
//        }
//    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgentEvent.onResumePageStart(this, TAG);
    }

    /**
     * @Title: replaceTitleLayout
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    public void replaceTitleLayout() {
        TitleBarFragment titleBarFragment = new TitleBarFragment_();
        replaceFragment(R.id.dinner_pay_title_bar_layout, titleBarFragment, titleBarFragment.getClass().getSimpleName(), false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgentEvent.onPausePageEnd(this, TAG);
    }

    @Click({R.id.view_left_shape, R.id.view_privilege_page_shape})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_left_shape:
                showPayingDialog();
                break;
            case R.id.view_privilege_page_shape:
                showPayingDialog();
                break;
        }
    }

    /**
     * 支付中提示
     */
    private void showPayingDialog() {
        if (separateEvent != null && separateEvent.getStatus() == SeparateEvent.EVENT_SEPARATE_SAVE) {
            ToastUtil.showLongToast(R.string.dinner_split_paying);
        } else {
            ToastUtil.showLongToast(R.string.dinner_subsetup_paying);
        }
    }

    @Override
    protected void onStop() {
        setActivityResult();
        super.onStop();
    }

    private void setActivityResult() {
        if (mPayFragment != null && this.isFromOpenPlatform) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_PARTNER_ID, this.mPartnerId);
            this.setResult(mPayFragment.getPayResult(), intent);
        }
    }

    @Override
    protected void onDestroy() {
        //注销会员登录
        DinnerShopManager.getInstance().clearCustomer();
        DisplayServiceManager.doCancel(getApplicationContext());
        unregisterEventBus();
        mShoppingCart.unRegisterListenerByTag(ShoppingCartListerTag.DINNER_DOPAY_SPLIT);
        mSeparateShoppingCart.unRegisterListenerByTag(ShoppingCartListerTag.DINNER_DOPAY_SPLIT);
        DinnerShopManager.getInstance()._unRegisterListener(asyncListener);
        super.onDestroy();
    }

    //拆单支付成功接收消息
    public void onEventMainThread(SeparateEvent result) {
        separateEvent = result;
        payMainLeftFragment.dealSplitPay(result);
        if (result.getStatus() == SeparateEvent.EVENT_SEPARATE_PAYED) {
            updatePayInfo();
            view_left_shape.setVisibility(View.GONE);
            view_privilege_shape.setVisibility(View.GONE);
        } else if (result.getStatus() == SeparateEvent.EVENT_SEPARATE_SAVE) {
            view_left_shape.setVisibility(View.VISIBLE);
            view_privilege_shape.setVisibility(View.VISIBLE);
        } else if (result.getStatus() == SeparateEvent.EVENT_RESOURCE_PAYING || result.getStatus() == SeparateEvent.EVENT_SEPARATE_PAYING) {
            showPayingView();
        }
    }

    /**
     * 分布支付中界面展示
     */
    private void showPayingView() {
//        view_left_shape.setVisibility(View.VISIBLE);
        view_privilege_shape.setVisibility(View.VISIBLE);
    }

    /**
     * 订单是否在支付中,或者预支付
     *
     * @return
     */
    private boolean isPaying() {
        return DinnerShopManager.getInstance().isPaying(mShoppingCart.getOrder());
    }

    /**
     * 支付成功时触发
     *
     * @Title: onEventMainThread
     * @Description: TODO
     * @Param @param event TODO
     * @Return void 返回类型
     */
    public void onEventMainThread(EventPayResult result) {
        if (result.getType() == BusinessType.DINNER) {
            this.finish();
        }
    }

    /**
     * @Title: onEventMainThread
     * @Description: TODO
     * @Param @param closeEvent TODO
     * @Return void 返回类型
     */
    public void onEventMainThread(ClosePayViewEvent closeEvent) {
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                MobclickAgentEvent.onEvent(UserActionCode.ZC030001);
                if (separateEvent != null) {
                    if (separateEvent.getStatus() == SeparateEvent.EVENT_SEPARATE_PAYING || separateEvent.getStatus() == SeparateEvent.EVENT_RESOURCE_PAYING) {
                        //分布支付直接返回桌台
                        EventBus.getDefault().post(new ActionCloseOrderDishActivity());
                    }
                }
                if (payMainLeftFragment != null)
                    payMainLeftFragment.doBack();
                return super.onKeyDown(keyCode, event);
            case KeyEvent.KEYCODE_HOME:
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    public void onEventMainThread(ActionDinnerPrilivige action) {
        switch (action.getType()) {
            case PRIVILIGE_ITEMS:
                replaceFragment(R.id.orderDishPrivilegeView,
                        new DinnerPriviligeItemsFragment_(),
                        DinnerPriviligeItemsFragment_.class.getSimpleName(), false);

                // 通知购物车刷新视图
                EventBus.getDefault().post(new ActionDinnerBatchDiscount(false, false));
                break;
            case DISCOUNT:
                replaceFragment(R.id.orderDishPrivilegeView,
                        new DinnerDiscountFragment_(),
                        DinnerDiscountFragment_.class.getSimpleName(), false);
                break;
            case INTEGRAL:
                if (DinnerShopManager.getInstance().getLoginCustomer() == null) {
                    showLoginDialog();
                } else {
                    replaceFragment(R.id.orderDishPrivilegeView,
                            new DinnerIntegralToCashFragment_(),
                            DinnerIntegralToCashFragment_.class.getSimpleName(), false);
                }
                break;
            case COUPON:
                if (DinnerShopManager.getInstance().getLoginCustomer() == null) {
                    showLoginDialog();
                } else {
                    replaceFragment(R.id.orderDishPrivilegeView,
                            new DinnerCouponsFragment_(),
                            DinnerCouponsFragment_.class.getSimpleName(), false);
                }
                break;
            case EXTRA_CHARGE:
                DinnerExtraChargeFragment extraFragment = new DinnerExtraChargeFragment_();
                replaceFragment(R.id.orderDishPrivilegeView, extraFragment, extraFragment.getClass().getSimpleName(), false);
                break;
            case COUPON_CODE:
                DinnerWeixinCouponCodeFragment weixinCouponCodeFragment = new DinnerWeixinCouponCodeFragment_();
                weixinCouponCodeFragment.show(getSupportFragmentManager(), weixinCouponCodeFragment.getClass().getSimpleName());
                break;
            case LOGIN:
                showLoginDialog();
                break;
            case MARKET_ACTIVITY:
                replaceFragment(R.id.orderDishPrivilegeView,
                        new DinnerMarketActivityFragment_(),
                        DinnerMarketActivityFragment.class.getSimpleName(), false);
                break;
            case SWITCH_CARD:
                DinnerSwitchCardFragment switchCardFragment = new DinnerSwitchCardFragment_();
                Bundle bundle = new Bundle();
                bundle.putString(DinnerCashManager.SOURCE, action.getSource());
                if (Utils.isNotEmpty(action.getCards())) {
                    bundle.putSerializable(DinnerCashManager.CARDS, (Serializable) action.getCards());
                }
                switchCardFragment.setArguments(bundle);
                replaceFragment(R.id.orderDishPrivilegeView, switchCardFragment, switchCardFragment.getClass().getSimpleName(), false);
                break;
            case SHOWTAOTALPAGE:
                view_shape.setVisibility(View.VISIBLE);
                totalFragment = new DinnerPayTotalFragment_();
                replaceFragment(R.id.dinner_pay_toatl_page, totalFragment, totalFragment.getClass().getSimpleName(), false);
                break;
            case CLOSETOTALPAGE:
                if (totalFragment != null) {
                    view_shape.setVisibility(View.GONE);
                    removeFragmentByTag(totalFragment.getClass().getSimpleName());
                    totalFragment = null;
                }
                break;
            case CUSTOMER_LIKE_REMARK:
                DinnerRemarkFragment dinnerRemarkFragment = new DinnerRemarkFragment_();
                replaceFragment(R.id.orderDishPrivilegeView, dinnerRemarkFragment, dinnerRemarkFragment.getClass().getSimpleName(), false);
                break;
            case CUSTOMER_COUPON: // 发券
                CouponListFragment couponListFragment = new CouponListFragment_();
                couponListFragment.setArguments(couponListFragment.createArguments(CouponListFragment.LAUNCHMODE_PRIVLIGES, action.getCustomer()));
                couponListFragment.setCloseCallback(new CouponListFragment.SendCouponCallback() {
                    @Override
                    public void cancel() {
                        // 取消
                    }

                    @Override
                    public void onSendCoupon(int count) {
                        DinnerShopManager.getInstance().getLoginCustomer().coupCount = count;
                        EventBus.getDefault().post(new ActionRefreshDinnerCustomer());
                        EventBus.getDefault().post(new ActionDinnerPrilivige(ActionDinnerPrilivige.DinnerPriviligeType.PRIVILIGE_ITEMS));
                    }
                });
                replaceFragment(R.id.orderDishPrivilegeView, couponListFragment, couponListFragment.getClass().getSimpleName(), false);
                break;
            default:
                break;
        }
    }

    private DinnerCustomerLoginDialogV2 mLoginDialog;

    /**
     * 弹出登录框
     */
    private void showLoginDialog() {
        /*if (mLoginDialog != null && mLoginDialog.isResumed()) {
            return;
        } else {
            mLoginDialog = new DinnerCustomerLoginDialogV2();
        }
        mLoginDialog.show(getSupportFragmentManager(), "DinnerCustomerLoginDialog");*/
        new DinnerCustomerLoginDialogFragment_().show(getSupportFragmentManager(), "DinnerCustomerLoginDialog");

    }

    // 刷新支付信息
    public void updatePayInfo() {
        if (mPayFragment != null && mPayFragment.isAdded()) {
            boolean isSeparte = DinnerShopManager.getInstance().isSepartShopCart();
            if (isSeparte) {
                this.mPayFragment.updatePayUI(mShoppingCart.createOrder(), mSeparateShoppingCart.createSeparateOrder(), true);
            } else {
                this.mPayFragment.updatePayUI(null, mShoppingCart.createOrder(), false);
            }
        }
    }

    ShoppingCartListener mModifyShoppingCartListener = new ShoppingCartListener() {

        @Override
        public void addToShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, ShopcartItem mShopcartItem) {
            updatePayInfo();

        }

        @Override
        public void addTempSetmealData(IShopcartItem mShopcartItem) {
            updatePayInfo();
        }

        @Override
        public void removeSetmealData() {
            updatePayInfo();
        }

        @Override
        public void removeSetmealChild(IShopcartItem OrderDishshopVo) {
            updatePayInfo();
        }

        @Override
        public void removeShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo,
                                       IShopcartItemBase mShopcartItemBase) {
            updatePayInfo();
        }

        @Override
        public void removeShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, IShopcartItem mShopcartItem) {
            updatePayInfo();
        }

        @Override
        public void orderDiscount(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
            updatePayInfo();
        }

        @Override
        public void removeDiscount(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, IShopcartItem mShopcartItem) {
            updatePayInfo();
        }

        @Override
        public void updateDish(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
            updatePayInfo();
        }

        @Override
        public void clearShoppingCart() {
            // updatePayInfo();
        }

        @Override
        public void setRemark(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
            updatePayInfo();
        }

        @Override
        public void removeRemark(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, IShopcartItem mShopcartItem) {
            updatePayInfo();
        }

        @Override
        public void removeCustomerPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
            updatePayInfo();
        }

        @Override
        public void removeSetmealRemark(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo,
                                        ISetmealShopcartItem setmeal) {
            updatePayInfo();
        }

        @Override
        public void setCardNo(String cardNo) {
            updatePayInfo();
        }

        @Override
        public void setOrderUserMessage(TakeOutInfo entity) {
            updatePayInfo();
        }

        @Override
        public void setCustomer(TradeCustomer customer) {
            updatePayInfo();
        }

        @Override
        public void batchPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
            updatePayInfo();
        }

        @Override
        public void resetOrder(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
            updatePayInfo();
        }

        @Override
        public void setCouponPrivi1lege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
            updatePayInfo();
        }

        @Override
        public void removeCouponPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
            updatePayInfo();
        }

        @Override
        public void setIntegralCash(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
            updatePayInfo();
        }

        @Override
        public void removeIntegralCash(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
            updatePayInfo();
        }

        @Override
        public void updateShoppingcartData() {
            updatePayInfo();
        }

        @Override
        public void separateOrder(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
            updatePayInfo();
        }

        @Override
        public void addExtraCharge(TradeVo mTradeVo, Map<Long, ExtraCharge> arrayExtraCharge) {
            updatePayInfo();
        }

        @Override
        public void removeExtraCharge(TradeVo mTradeVo, Long extraChargeId) {
            updatePayInfo();
        }

        @Override
        public void removeDeposit(TradeVo mTradeVo) {
            updatePayInfo();
        }

        @Override
        public void addMarketActivity(TradeVo mTradeVo) {
            updatePayInfo();
        }

        @Override
        public void removeMarketActivity(TradeVo mTradeVo) {
            updatePayInfo();
        }

        @Override
        public void exception(String message) {
            updatePayInfo();
        }

        @Override
        public void addWeiXinCouponsPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
            updatePayInfo();
        }

        @Override
        public void removeWeiXinCouponsPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
            updatePayInfo();
        }

        @Override
        public void doBanquet(TradeVo mTradeVo) {
            updatePayInfo();
        }

        @Override
        public void removeBanquet(TradeVo mTradeVo) {
            updatePayInfo();
        }

        @Override
        public void setTradePeopleCount(Integer tradePeopleCount) {
            updatePayInfo();
        }

        @Override
        public void setOrderType(DeliveryType deliveryType) {
            updatePayInfo();
        }
    };
}
