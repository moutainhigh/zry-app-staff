package com.zhongmei.beauty.customer;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.zhongmei.beauty.customer.constants.BeautyCustomerConstants;
import com.zhongmei.beauty.widgets.DrawableCenterTextView;
import com.zhongmei.bty.base.MainBaseActivity;
import com.zhongmei.bty.basemodule.auth.application.CustomerApplication;
import com.zhongmei.bty.basemodule.customer.enums.CustomerAppConfig;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.commonmodule.event.OrderRefreshClickEvent;
import com.zhongmei.bty.commonmodule.event.SideMenuClickEvent;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.customer.CustomerOrdercenterFragment;
import com.zhongmei.bty.customer.CustomerOrdercenterFragment.OrderCategory;
import com.zhongmei.bty.customer.CustomerOrdercenterFragment.WindowToken;
import com.zhongmei.bty.customer.CustomerOrdercenterFragment_;
import com.zhongmei.bty.dinner.orderdish.TitleBarFragment;
import com.zhongmei.yunfu.LoginActivity;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.UserActionCode;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.beauty_customer)
public class BeautyCustomerActivity extends MainBaseActivity {
    private static final String ACTIVITY_NAME = BeautyCustomerActivity.class.getSimpleName();

    @BeautyCustomerConstants.CustomerPage
    private int lastPage = BeautyCustomerConstants.CustomerPage.UNKONW;

    @BeautyCustomerConstants.CustomerStartFlag
    private int mFlag = BeautyCustomerConstants.CustomerStartFlag.CUSTOMER_LIST;

    public final static String BUNDER_FLAG = "flag";

    @FragmentById(R.id.fragment_title_bar)
    TitleBarFragment fragment_titleBar;

    @ViewById(R.id.customer_edit)
    ImageView ivEdit;

    @ViewById(R.id.customer_title_layout)
    TextView customer_title_layout;

    @ViewById(R.id.customer_ordercenter_titletab_rg)
    RadioGroup ordercenterTitletabRg;// 订单中心tab

    @ViewById(R.id.ivCreate_Customer)
    DrawableCenterTextView ivCreateCustomer;

    @ViewById(R.id.card_sell_rb)
    RadioButton cardSellRb;

    @ViewById(R.id.member_store_value_rb)
    RadioButton memberStoreValueRb;

    @ViewById(R.id.card_store_value_rb)
    RadioButton cardStoreValueRb;

    @ViewById(R.id.titleRightView)
    RelativeLayout titleRightView;

    @ViewById(R.id.customer_title_right)
    TextView tvTitleRight;

    @ViewById(R.id.iv_title_bar_menu)
    ImageView ivMenu;

    @ViewById(R.id.iv_queue_notify_center)
    ImageView ivNotifyCenter;

    @ViewById(R.id.viewSwitcher)
    ViewSwitcher switcher;

    @ViewById(R.id.printer_title)
    LinearLayout mPrinterTitleLayout;

    @ViewById(R.id.drawer)
    DrawerLayout drawer;

    @ViewById(R.id.customer_manager)
    RadioButton customerManager;

    @ViewById(R.id.card_manager)
    RadioButton cardManager;

    @ViewById(R.id.card_order)
    RadioButton cardOrder;

    @ViewById(R.id.shop_management)
    RadioButton shopManagement;

    // 遮罩层
    @ViewById(R.id.view_shadow)
    protected View viewShadow;

    // 左侧菜单
    @ViewById
    protected View left_menu;

    @ViewById(R.id.slideView)
    protected FrameLayout slideView;

    // 主窗体
    @ViewById(R.id.customer_content)
    protected RelativeLayout main_frame;

    // 当前登录名称
    @ViewById(R.id.tv_current_name)
    protected TextView tvCurrentName;

    // 当前Id
    @ViewById(R.id.tv_current_id)
    protected TextView tvCurrentId;

    @ViewById(R.id.tip_img)
    protected ImageView mTipImg;

    // 通知上的订单数
    @ViewById(R.id.view_notify_tip)
    TextView viewNotifyCenterTip;

    // 通知上的其他数
    @ViewById(R.id.view_notify_tip_other)
    TextView viewNotifyCenterOtherTip;

    private boolean isLeftShown = false;

    public String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Extra("phone")
    protected String mCustomerPhone;

    private BeautyCustomerContentFragment mCustomerContentFragment;

    // 实体卡入口选择界面
    //private EntityCardManagementFragment mEntityCardManagementFragment;

    // 单据中心
    private CustomerOrdercenterFragment mCustomerOrderFragment;

    /**
     * 侧边菜单按钮点击
     *
     * @param event
     */
    @SuppressLint("WrongConstant")
    public void onEventMainThread(SideMenuClickEvent event) {
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        }
        switchLeftMenu();
    }

    /**
     * 订单刷新按钮点击
     *
     * @param event
     */
    @SuppressLint("WrongConstant")
    public void onEventMainThread(OrderRefreshClickEvent event) {
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        } else {
            drawer.openDrawer(Gravity.START);
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        CustomerApplication.mCustomerBussinessType = CustomerAppConfig.CustomerBussinessType.BEAUTY;
        super.onCreate(arg0);
    }

    @AfterViews
    protected void onInitView() {
        if (MainApplication.getInstance() == null || ShopInfoCfg.getInstance().authUser == null) {
            ToastUtil.showLongToast(R.string.uninit);
            finish();
        }
        initView();
        initNotifyCenter();
        initLoginUser();
        initLeftMenu();
        if (mFlag == getIntent().getIntExtra(BUNDER_FLAG, BeautyCustomerConstants.CustomerStartFlag.CUSTOMER_LIST)) {
            showCustomerListFragment();
        } else {
            showCustomerCardFragment();
        }
        customerManager.setChecked(true);
        shopManagement.setVisibility(View.GONE);

        initTitleBarColor();
        registerEventBus();
    }

    private void initView() {
        ivCreateCustomer.setVisibility(View.VISIBLE);
    }

    private void initLeftMenu() {
        customerManager.setOnClickListener(leftMenuClickListener);
        cardManager.setOnClickListener(leftMenuClickListener);
        cardOrder.setOnClickListener(leftMenuClickListener);
    }

    private OnClickListener leftMenuClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!ClickManager.getInstance().isClicked()) {
                switch (v.getId()) {
                    case R.id.customer_manager:
                        MobclickAgentEvent.onEvent(UserActionCode.GK030001);
//                        titleRightView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f));
                        showCustomerListFragment();
                        mPrinterTitleLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.card_manager:
                        MobclickAgentEvent.onEvent(UserActionCode.GK030002);
                        showCustomerCardFragment();
                        mPrinterTitleLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.card_order:
                        MobclickAgentEvent.onEvent(UserActionCode.GK030003);
//                        titleRightView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f));
                        showCustomerOrderFragment();
                        mPrinterTitleLayout.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
                setLeftMenu(false);
            } else {
                return;
            }
        }

    };

    /**
     * @Title: showCustomerListFragment
     * @Description: 会员管理界面
     * @Param TODO
     * @Return void 返回类型
     */
    private void showCustomerListFragment() {
        if (lastPage == BeautyCustomerConstants.CustomerPage.PAGE_CUSTOMER) {
            return;
        }
        titleRightView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f));
        customer_title_layout.setText(getResources().getString(R.string.customer_customer_info_manage));
        mCustomerContentFragment = new BeautyCustomerContentFragment_();
        replaceFragment(R.id.customer_content, mCustomerContentFragment, mCustomerContentFragment.getClass().getName());
        customer_title_layout.setVisibility(View.VISIBLE);
        ordercenterTitletabRg.setVisibility(View.GONE);
        lastPage = BeautyCustomerConstants.CustomerPage.PAGE_CUSTOMER;
    }

    /**
     * @Title: showCustomerCardFragment
     * @Description: 会员实体卡界面
     * @Param TODO
     * @Return void 返回类型
     */
    private void showCustomerCardFragment() {
        if (lastPage == BeautyCustomerConstants.CustomerPage.PAGE_CARD) {
            return;
        }
        titleRightView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 0f));
        customer_title_layout.setText(getResources().getString(R.string.slide_menu_card_manager));
        //mEntityCardManagementFragment = new EntityCardManagementFragment_();
        //replaceFragment(R.id.customer_content, mEntityCardManagementFragment, mEntityCardManagementFragment.getClass().getName());
        customer_title_layout.setVisibility(View.VISIBLE);
        ordercenterTitletabRg.setVisibility(View.GONE);
        lastPage = BeautyCustomerConstants.CustomerPage.PAGE_CARD;
    }

    /**
     * @Date 2016年3月24日
     * @Description: 展示订单界面
     * @Return void
     */
    private void showCustomerOrderFragment() {
        titleRightView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f));
        if (lastPage == BeautyCustomerConstants.CustomerPage.PAGE_ORDER) {
            return;
        }
        customer_title_layout.setText(getResources().getString(R.string.slide_menu_card_order));
        mCustomerOrderFragment = new CustomerOrdercenterFragment_();
        // 获取会员订单中心选中tab
        switch (ordercenterTitletabRg.getCheckedRadioButtonId()) {
            case R.id.card_sell_rb:
                mCustomerOrderFragment.setArguments(createOrderFragmentArguments(WindowToken.CARD_SALE));
                setTabSelected(R.id.card_sell_rb);
                break;
            case R.id.member_store_value_rb:
                mCustomerOrderFragment.setArguments(createOrderFragmentArguments(WindowToken.MEMBER_STORE_VALUE));
                setTabSelected(R.id.member_store_value_rb);
                break;
            case R.id.card_store_value_rb:
                mCustomerOrderFragment.setArguments(createOrderFragmentArguments(WindowToken.CARD_STORE_VALUE));
                setTabSelected(R.id.card_store_value_rb);
                break;
            default:
                break;
        }
        replaceFragment(R.id.customer_content, mCustomerOrderFragment, "CustomerOrdercenterFragment");
        customer_title_layout.setVisibility(View.GONE);
        ordercenterTitletabRg.setVisibility(View.VISIBLE);
        ordercenterTitletabRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.card_sell_rb:
                        mCustomerOrderFragment.update(WindowToken.CARD_SALE, OrderCategory.PAYED);
                        setTabSelected(R.id.card_sell_rb);
                        break;
                    case R.id.member_store_value_rb:
                        mCustomerOrderFragment.update(WindowToken.MEMBER_STORE_VALUE, OrderCategory.PAYED);
                        setTabSelected(R.id.member_store_value_rb);
                        break;
                    case R.id.card_store_value_rb:
                        mCustomerOrderFragment.update(WindowToken.CARD_STORE_VALUE, OrderCategory.PAYED);
                        setTabSelected(R.id.card_store_value_rb);
                        break;
                    default:
                        break;
                }
            }
        });
        lastPage = BeautyCustomerConstants.CustomerPage.PAGE_ORDER;
    }

    private Bundle createOrderFragmentArguments(WindowToken windowToken) {
        Bundle bundle = new Bundle();
        if (windowToken != null) {
            bundle.putInt("window_token", windowToken.value());
        }
        return bundle;
    }

    /**
     * @Title: setTabSelected
     * @Description: 设置tab背景选择
     * @Param @param viewId TODO
     * @Return void 返回类型
     */
    private void setTabSelected(int viewId) {
        switch (viewId) {
            case R.id.card_sell_rb:
                cardSellRb.setBackgroundResource(R.drawable.beauty_customer_title_order_left);
                cardSellRb.setTextColor(getResources().getColor(R.color.beauty_color_333333));
                memberStoreValueRb.setBackgroundResource(0);
                memberStoreValueRb.setTextColor(getResources().getColor(R.color.beauty_color_DEDEDE));
                cardStoreValueRb.setBackgroundResource(0);
                cardStoreValueRb.setTextColor(getResources().getColor(R.color.beauty_color_DEDEDE));
                break;
            case R.id.member_store_value_rb:
                cardSellRb.setBackgroundResource(0);
                cardSellRb.setTextColor(getResources().getColor(R.color.beauty_color_DEDEDE));
                memberStoreValueRb.setBackgroundResource(R.drawable.beauty_customer_title_order_middle);
                memberStoreValueRb.setTextColor(getResources().getColor(R.color.beauty_color_333333));
                cardStoreValueRb.setBackgroundResource(0);
                cardStoreValueRb.setTextColor(getResources().getColor(R.color.beauty_color_DEDEDE));
                break;
            case R.id.card_store_value_rb:
                cardSellRb.setBackgroundResource(0);
                cardSellRb.setTextColor(getResources().getColor(R.color.beauty_color_DEDEDE));
                memberStoreValueRb.setBackgroundResource(0);
                memberStoreValueRb.setTextColor(getResources().getColor(R.color.beauty_color_DEDEDE));
                cardStoreValueRb.setBackgroundResource(R.drawable.beauty_customer_title_order_right);
                cardStoreValueRb.setTextColor(getResources().getColor(R.color.beauty_color_333333));
                break;
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
            return;
        }
        super.onBackPressed();
    }

    @Click(R.id.ll_back)
    void onBack() {
        this.onBackPressed();
    }

    /*
     * 处理菜单侧边栏的显示和隐藏
     */
    @SuppressLint("WrongConstant")
    @Click(R.id.iv_title_bar_menu)
    void onClickMenu() {
        // 点击标题栏菜单按钮
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        }
        switchLeftMenu();
    }

    /*
     * 处理通知中心的显示隐藏
     */
    @SuppressLint("WrongConstant")
    @Click(R.id.iv_queue_notify_center)
    void onClickNotifyCenter() {
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        } else {
            drawer.openDrawer(Gravity.START);
        }
    }

    // 点击遮罩
    @Click(R.id.view_shadow)
    void clickViewShadow() {
        setLeftMenu(false);
    }

    // 退出
    @Click(R.id.tv_current_name)
    void clickLoginOut() {
        Session.unbind();
        Intent intent = new Intent(BeautyCustomerActivity.this, LoginActivity.class);
        intent.putExtra("activityName", ACTIVITY_NAME);
        startActivity(intent);
        BeautyCustomerActivity.this.finish();
    }

    @Click(R.id.ivCreate_Customer)
    void onClickCreate() {
        MobclickAgentEvent.onEvent(UserActionCode.GK010002);
        VerifyHelper.verifyAlert(this, CustomerApplication.PERMISSION_CUSTOMER_CREATE,
                new VerifyHelper.Callback() {
                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                        BeautyCustomerEditActivity_.IntentBuilder_ senderOrder = BeautyCustomerEditActivity_.intent(BeautyCustomerActivity.this);
                        senderOrder.start();
                    }
                });
    }


    /**
     * 显示或隐藏左侧菜单
     */
    private void switchLeftMenu() {
        setLeftMenu(!isLeftShown);
    }

    private void initNotifyCenter() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawer.setFocusableInTouchMode(false);
        drawer.setScrimColor(getResources().getColor(R.color.shadow_bg));
        drawer.setDrawerListener(new DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (mTipImg.getVisibility() != View.VISIBLE) {
                    mTipImg.setVisibility(View.VISIBLE);
                }
                mTipImg.setTranslationX((slideOffset - 1));
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void setLeftMenu(boolean visible) {
        if (visible) {
            if (!isLeftShown) {
                left_menu.setVisibility(View.VISIBLE);
                doAnimation(this, slideView, R.animator.fragment_slide_right_exit);
                doAnimation(this, left_menu, R.animator.fragment_slide_left_enter);
                // 展开左侧菜单时，显示遮罩
                viewShadow.setVisibility(View.VISIBLE);
            }
        } else {
            if (isLeftShown) {
                doAnimation(this, slideView, R.animator.fragment_slide_right_enter);
                doAnimation(this, left_menu, R.animator.fragment_slide_left_exit);
                // 关闭左侧菜单时，隐藏遮罩
                viewShadow.setVisibility(View.GONE);
            }
        }
        isLeftShown = visible;
    }

    private void doAnimation(Context context, View view, int id) {
        AnimatorSet objectAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(context, id);
        objectAnimator.setTarget(view);
        objectAnimator.start();
    }

    private void initLoginUser() {
        if (null != tvCurrentName && Session.getAuthUser() != null) {
            tvCurrentName.setText(Session.getAuthUser().getName());
        }
        tvCurrentId.setText("NO:" + /*no[1]*/ ShopInfoCfg.getInstance().getTabletNumberFormat());
    }

    public void initTitleBarColor() {
        fragment_titleBar.setBgColor(R.color.beauty_bg_titlebar);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (drawer != null && drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        }
    }


    @Override
    protected void onDestroy() {
        unregisterEventBus();
        super.onDestroy();
    }
}
