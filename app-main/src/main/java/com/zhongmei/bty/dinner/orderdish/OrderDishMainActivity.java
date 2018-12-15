package com.zhongmei.bty.dinner.orderdish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.base.MainBaseActivity;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.listerner.ShoppingAsyncListener;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.bty.common.view.LoadingView;
import com.zhongmei.bty.dinner.action.ActionBatchOperationItems;
import com.zhongmei.bty.mobilepay.event.ActionClose;
import com.zhongmei.bty.basemodule.trade.event.ActionCloseOrderDishActivity;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.dinner.cash.DinnerDishCustomerLogin;
import com.zhongmei.bty.dinner.cash.DinnerDishCustomerLogin_;
import com.zhongmei.bty.dinner.shopcart.adapter.DinnerShopCartAdapter;
import com.zhongmei.bty.dinner.table.view.OpentablePopWindow;
import com.zhongmei.bty.dinner.vo.LoadingFinish;
import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.snack.event.EventEditModle;

import java.util.List;

import de.greenrobot.event.EventBus;

public abstract class OrderDishMainActivity extends MainBaseActivity implements OnClickListener {
    private final static String TAG = OrderDishMainActivity.class.getSimpleName();

    // 默认进入结算界面
    public static final String IS_DEFAULT_SETTLE = "isDefaultSettlePage";

    protected View shadowView;// 遮罩层
    protected LinearLayout orderDishMiddle;
    protected ImageButton btn_close;

    private FragmentManager mFragmentManager;

    protected DishLeftFragment mLeftFragment;

    private DishHomePageFragment mDishHomePageFragment;

    private DinnerDishSearchFragment mDinnerDishSearchFragment;// 菜品搜索界面

    private DinnerDishSetmealFragment mDinnerDishSetmealFragment;// 菜品子菜列表界面

    private DinnerDishCommentFragment mDinnerDishCommentFragment;// 整单备注界面

    private DinnerDishCustomerLogin mCustomerLogin;// 会员登录界面

    private TitleBarFragment titleBarFragment;// title bar

    //中间操作栏
    private DinnerDishMiddleFragment middleFragment;

    private int SHOWINDEX;//当前加载Fragment编号

    private String lastDishUUID = "";

    private DinnerShoppingCart mShoppingCart;

    // 是否默认进入点菜界面 false进入点菜界面，true 进入结算界面
    private boolean isFromDesk = false;
    private boolean isInit = true;

    public LoadingView leftLoading;
    public LoadingView rightLoading;
    private boolean isComboEditMode = false;

    protected abstract DishHomePageFragment getDishPageFragment();

    protected abstract DishLeftFragment getLeftFragment();

    protected abstract int getLayoutRes();

    protected abstract int getOrderDishMode();

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(getLayoutRes());
        init();
        MobclickAgentEvent.openActivityDurationTrack(false);
        Intent intent = getIntent();
        if (intent != null) {
            isFromDesk = intent.getBooleanExtra(IS_DEFAULT_SETTLE, false);
        }
        registerEventBus();
        DinnerShopManager.getInstance()._register(asyncListener);

    }

    ShoppingAsyncListener asyncListener = new ShoppingAsyncListener() {

        @Override
        public void onMultiTerminalUpdate(final String hint) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DialogUtil.showConfirmDialog(getSupportFragmentManager(), CommonDialogFragment.ICON_WARNING, hint,
                            R.string.dinner_back_desk, new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    OrderDishMainActivity.this.finish();
                                }
                            }, true, "MultiTerminalUpdate");
                }
            });
        }

        @Override
        public void onAsyncUpdate() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //刷新流水号
                    if (mLeftFragment != null && mLeftFragment.isVisible()) {
                        mLeftFragment.loadTradeView();
                    }
                }
            });
        }
    };

    public void init() {
        shadowView = findViewById(R.id.view_shadow);
        orderDishMiddle = (LinearLayout) findViewById(R.id.orderDishMiddle);
        btn_close = (ImageButton) findViewById(R.id.btn_close);
        leftLoading = (LoadingView) findViewById(R.id.leftLoading);
        rightLoading = (LoadingView) findViewById(R.id.rightLoading);
        btn_close.setOnClickListener(this);
        shadowView.setOnClickListener(this);

        mFragmentManager = getSupportFragmentManager();
        buildView();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//		if (isFromDesk && isInit) {
//			SHOWINDEX = ChangePageListener.SETTLEMENT;
//			mSettlementMainFragment = new SettlementMainFragment_();
//			replaceFragment(R.id.orderDishRightView, mSettlementMainFragment, mSettlementMainFragment.getClass().getName());
//			isInit=false;
//		}
        MobclickAgentEvent.onResumePageStart(this, TAG);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgentEvent.onPausePageEnd(this, TAG);
    }

    @Override
    protected void onDestroy() {
//		if (EventBus.getDefault().isRegistered(this)) {
//			EventBus.getDefault().unregister(this);
//		}
//		AuthLogManager.getInstance().clear();
        DinnerShopCartAdapter.setDishCheckMode(false);
        unregisterEventBus();
        DinnerShopManager.getInstance()._unRegisterListener(asyncListener);
        super.onDestroy();
    }

    /**
     * @Title: onEventMainThread
     * @Description: TODO
     * @Param @param event TODO
     * @Return void 返回类型
     */
    public void onEventMainThread(ActionCloseOrderDishActivity event) {
        this.finish();
    }

    /**
     * 显示中间的操作
     */
    public void onEventMainThread(DishDataItem item) {
        showMiddleFragment(item);
    }

    public void onEventMainThread(ActionBatchOperationItems action) {
        showMiddleFragment(action.items, action.isCategory);
    }

    public void onEventMainThread(ActionClose closeMiddView) {
        doClose();
    }

    public void buildView() {
        titleBarFragment = new TitleBarFragment_();
        addFragment(R.id.dinner_title_bar_layout, titleBarFragment, titleBarFragment.getClass().getName(), false);
        buildFragment();
    }

    /**
     * 加载界面中购物车Fragment和点菜界面Fragment
     */
    public void buildFragment() {
        mShoppingCart = DinnerShoppingCart.getInstance();
        mShoppingCart.unRegisterListener();

        mLeftFragment = getLeftFragment();
        //mDinnerDishHomePageFragment = new DinnerDishHomePageFragment_();
        mDishHomePageFragment = getDishPageFragment();
        mLeftFragment.setIsFromTable(isFromDesk);
        boolean isQuickOpenTable = false;
        if (getIntent() != null) {
            isQuickOpenTable = getIntent().getBooleanExtra(OpentablePopWindow.QUICK_OPEN_TABLE, false);
        }
        mLeftFragment.setQuickOpenTable(isQuickOpenTable);
        mLeftFragment.registerLoadingListener(mLeftLoadingFinish);
        mLeftFragment.registerListener(mChangePageListener, middleChangeListener);
        addFragment(R.id.orderDishLeftView, mLeftFragment, DishLeftFragment.class.getName(), false);

        mDishHomePageFragment.registerListener(mChangePageListener);

        mDishHomePageFragment.registerLoadingListener(mRightLoadingFinish);

        //不是从桌台直接进入结算结算界面的操作，才替换右侧的点菜界面
        if (!isFromDesk) {
            SHOWINDEX = ChangePageListener.ORDERDISHLIST;
            replaceFragment(R.id.orderDishRightView, mDishHomePageFragment, mDishHomePageFragment.getClass().getName(), false);

        }


    }

    DinnerDishMiddleFragment.IChangePageListener middleChangeListener = new DinnerDishMiddleFragment.IChangePageListener() {
        @Override
        public void changePage(View contentView) {

        }

        @Override
        public void closePage(ShopcartItem item) {
            doClose();
            if (item != null) {
                DishDataItem dishDataItem = mLeftFragment.doSelected(item);
                if (dishDataItem != null) {
                    showMiddleFragment(dishDataItem);
                }
            } else {
                mLeftFragment.clearAllSelected();
            }
        }

        @Override
        public void onEditModeChange(boolean isEditMode) {
            isComboEditMode = isEditMode;
        }
    };

    private void showMiddleFragment(DishDataItem item) {
        orderDishMiddle.setVisibility(View.VISIBLE);
        btn_close.setVisibility(View.VISIBLE);
        shadowView.setVisibility(View.VISIBLE);
        middleFragment = new DinnerDishMiddleFragment_();
        middleFragment.setCurrentMode(getOrderDishMode());
        middleFragment.setComboEditMode(isComboEditMode);
        middleFragment.registerListener(mChangePageListener);
        middleFragment.registerListener(middleChangeListener);
        middleFragment.doSelect(item);
        replaceFragment(R.id.orderDishMiddle, middleFragment, middleFragment.getClass().getName(), false);
    }

    private void showMiddleFragment(List<DishDataItem> items, boolean isCategory) {
        orderDishMiddle.setVisibility(View.VISIBLE);
        btn_close.setVisibility(View.VISIBLE);
        shadowView.setVisibility(View.VISIBLE);
        middleFragment = new DinnerDishMiddleFragment_();
        middleFragment.registerListener(mChangePageListener);
        middleFragment.registerListener(middleChangeListener);
        middleFragment.doSelect(items, isCategory);
        replaceFragment(R.id.orderDishMiddle, middleFragment, middleFragment.getClass().getName(), false);
    }

    public LoadingFinish mLeftLoadingFinish = new LoadingFinish() {
        @Override
        public void loadingFinish() {
            leftLoading.setVisibility(View.GONE);
        }
    };

    public LoadingFinish mRightLoadingFinish = new LoadingFinish() {
        @Override
        public void loadingFinish() {
            rightLoading.setVisibility(View.GONE);
        }
    };


    public ChangePageListener mChangePageListener = new ChangePageListener() {

        @Override
        public void changePage(int pageNo, Bundle bundle) {
            Boolean noNeedCheck = bundle != null ? bundle.getBoolean(Constant.NONEEDCHECK) : true;
            String itemUUid = bundle != null ? bundle.getString(Constant.EXTRA_SHOPCART_ITEM_UUID) : "";
            if (itemUUid == null) {
                itemUUid = "";
            }
            if (!noNeedCheck) {
                // 切换界面时验证当前套餐子菜选择是否满足规则

                ShopcartItem mShopcartItem = mShoppingCart.getDinnerShopcartItem(itemUUid);

                if (!mShoppingCart.checkDishIsVaild(mShoppingCart.getShoppingCartVo(), mShopcartItem)) {
                    mShoppingCart.showCheckDialog(mShoppingCart.getShoppingCartVo(),
                            pageNo,
                            mChangePageListener,
                            getSupportFragmentManager(),
                            mShopcartItem,
                            null);
                    return;
                }
            }
            // 如果是当前界面并且是同一个菜品或子菜则不切换界面
            if (SHOWINDEX == pageNo && itemUUid.equals(lastDishUUID)) {
                return;
            }

            // 设置为编辑状态
            EventBus.getDefault().post(new EventEditModle(true));
            switch (pageNo) {
                case ChangePageListener.ORDERDISHLIST:
                    showDishListFragment(mDishHomePageFragment);
                    break;

                case ChangePageListener.SEARCH:
                    mDinnerDishSearchFragment = new DinnerDishSearchFragment_();
                    mDinnerDishSearchFragment.registerListener(mChangePageListener);
                    changeFragment(R.id.orderDishRightView, mDinnerDishSearchFragment);
                    break;

                case ChangePageListener.DISHPROPERTY:
//					mDinnerDishPropertyFragment = DinnerDishPropertyFragment_.getInstance(bundle);
//					mDinnerDishPropertyFragment.registerListener(mChangePageListener);
//					changeFragment(R.id.orderDishRightView, mDinnerDishPropertyFragment);
                    break;

                case ChangePageListener.DISHCOMBO:
                    removeFragment();
                    mDinnerDishSetmealFragment = new DinnerDishSetmealFragment_();
                    mDinnerDishSetmealFragment.setArguments(bundle);
                    mDinnerDishSetmealFragment.registerListener(mChangePageListener);
                    changeFragment(R.id.orderDishRightView, mDinnerDishSetmealFragment);
                    break;

//				case ChangePageListener.SETTLEMENT:
//					mSettlementMainFragment = new SettlementMainFragment_();
//					changeFragment(R.id.orderDishRightView, mSettlementMainFragment);
//					break;

                case ChangePageListener.SAVE_BACK:
                    if (SHOWINDEX != ChangePageListener.ORDERDISHLIST) {
                        showDishListFragment(mDishHomePageFragment);
                        pageNo = ChangePageListener.ORDERDISHLIST;
                    } else {
                        OrderDishMainActivity.this.finish();
                    }
                    break;
                case ChangePageListener.PAGE_TABLE_HOME:
                    OrderDishMainActivity.this.finish();
                    break;
                case ChangePageListener.ORDER_COMMENTS:// 显示备注信息
                    mDinnerDishCommentFragment = new DinnerDishCommentFragment_();
                    mDinnerDishCommentFragment.setAdapter();
                    mDinnerDishCommentFragment.registerListener(mChangePageListener);
                    changeFragment(R.id.orderDishRightView, mDinnerDishCommentFragment);
                    break;

                case ChangePageListener.REMOBER_LOGIN:// 显示会员登录界面
                    mCustomerLogin = new DinnerDishCustomerLogin_();
                    mCustomerLogin.registerListener(mChangePageListener);
                    changeFragment(R.id.orderDishRightView, mCustomerLogin);
                    break;

                case ChangePageListener.DISH_CUSTOMER_COUPONS:// 显示会员登录信息界面
                    break;
                default:
                    break;
            }
            // 通知购物车当前所处界面
            mShoppingCart.setIndexPage(pageNo);
            SHOWINDEX = pageNo;
            lastDishUUID = itemUUid;
        }

        @Override
        public void clearShoppingCart() {
            // TODO Auto-generated method stub

        }
    };

    /**
     * @param containerViewId
     * @param fragment
     */
    private void changeFragment(int containerViewId, Fragment fragment) {
        if (SHOWINDEX == ChangePageListener.ORDERDISHLIST) {
            hideFragment(mDishHomePageFragment);
            addFragment(containerViewId, fragment, fragment.getClass().getName());
        } else {

            removeFragment();

            addFragment(containerViewId, fragment, fragment.getClass().getName());

            //replaceFragment(containerViewId, fragment, fragment.getClass().getName());
        }
    }

    /**
     * @Title: showDishList
     * @Description: 展示碎片
     * @Param @param mFragment TODO
     * @Return void 返回类型
     */
    private void showDishListFragment(Fragment mFragment) {
        if (isDestroyed()) {
            return;
        }
        if (mDishHomePageFragment != null) {
            removeFragment();
            showFragment(mDishHomePageFragment, true);
        } else {

            replaceFragment(R.id.orderDishRightView, mFragment, mFragment.getClass().getName());
        }
        // 设置为非编辑状态
        EventBus.getDefault().post(new EventEditModle(false));
    }

    private void removeFragment() {
        switch (SHOWINDEX) {

            case ChangePageListener.ORDERDISHLIST:
                break;

            case ChangePageListener.SEARCH:
                removeFragment(mDinnerDishSearchFragment, mDinnerDishSearchFragment.getClass().getName());
//			removeFragmentByTag(mDinnerDishSearchFragment.getClass().getName());
                break;

            case ChangePageListener.DISHPROPERTY:
//			removeFragment(mDinnerDishPropertyFragment, mDinnerDishPropertyFragment.getClass().getName());
//			removeFragmentByTag(mDinnerDishPropertyFragment.getClass().getName());
                break;

            case ChangePageListener.DISHCOMBO:
                removeFragment(mDinnerDishSetmealFragment, mDinnerDishSetmealFragment.getClass().getName());
//			removeFragmentByTag(mDinnerDishSetmealFragment.getClass().getName());
                break;

            case ChangePageListener.SAVE_BACK:
                break;
            case ChangePageListener.PAGE_TABLE_HOME:
                break;
            case ChangePageListener.ORDER_COMMENTS:
                removeFragment(mDinnerDishCommentFragment, mDinnerDishCommentFragment.getClass().getName());
//			removeFragmentByTag(mDinnerDishCommentFragment.getClass().getName());
                break;

            case ChangePageListener.REMOBER_LOGIN:
                removeFragment(mCustomerLogin, mCustomerLogin.getClass().getName());
//			removeFragmentByTag(mCustomerLogin.getClass().getName());
                break;

            case ChangePageListener.DISH_CUSTOMER_COUPONS:
                break;
            default:
                break;


        }
    }

    @Override
    public void onBackPressed() {
        //反结账的单子在点菜界面点击返回，弹出取消反结账的确认框
        if (DinnerShoppingCart.getInstance().isReturnCash()) {
            DialogUtil.showHintConfirmDialog(mFragmentManager,
                    R.string.pause_repay,
                    R.string.ok,
                    R.string.cancel,
                    new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // 返回桌台
                            mChangePageListener.changePage(ChangePageListener.PAGE_TABLE_HOME, null);
                        }
                    },
                    null,
                    "cancel_repay");
        } else {
            super.onBackPressed();
            DinnerShopCartAdapter.setDishCheckMode(false);
        }
        AuthLogManager.getInstance().clear();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//		if(mLeftFragment != null && mLeftFragment.isVisible()){
//			mLeftFragment.onActivityTouched(ev);
//		}

        return super.dispatchTouchEvent(ev);
    }

    public void showShadow(boolean isShow) {
        //菜品属性操作条显示时不隐藏
        if (middleFragment != null && middleFragment.isVisible()) {
            return;
        }
        shadowView.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
            case R.id.view_shadow:
                if (!mLeftFragment.isDishCheckMode()) {
                    doClose();
                    mLeftFragment.clearAllSelected();
                }
                break;
        }
    }

    /**
     * 关闭中间的操作条处理
     */
    protected void doClose() {
        if (mLeftFragment != null && mLeftFragment.isVisible() && mLeftFragment.isBatchOperating()) {
            mLeftFragment.finishBatchOperation();
        }
        if (middleFragment != null) {
            removeFragmentByTag(middleFragment.getClass().getName());
        }
        orderDishMiddle.setVisibility(View.GONE);
        shadowView.setVisibility(View.GONE);
        btn_close.setVisibility(View.GONE);
    }
}
