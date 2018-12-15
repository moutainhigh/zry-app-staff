package com.zhongmei.bty.base;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.zhongmei.bty.basemodule.database.enums.EntranceType;
import com.zhongmei.bty.basemodule.database.utils.DbQueryConstant;
import com.zhongmei.bty.basemodule.slidemenu.listener.SlideMenuListener;
import com.zhongmei.bty.basemodule.slidemenu.view.SlideMenuView;
import com.zhongmei.bty.basemodule.slidemenu.view.TradeSlideMenuView_;
import com.zhongmei.bty.cashier.ordercenter.OrderCenterMainFragment;
import com.zhongmei.bty.dinner.orderdish.TitleBarFragment;
import com.zhongmei.bty.dinner.orderdish.TitleBarFragment_;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.DensityUtil;

/**
 * Created by demo on 2018/12/15
 */
public abstract class BusinessMainActivity extends MainBaseActivity {

    protected int slideMenuType = SlideMenuView.SLIDE_MENU_TYPE_TRADE;

    private SlideMenuView slideMenuView;

    private RelativeLayout rlRootView;
    private FrameLayout flSlideView;
    // 遮罩层
    private View viewShadow;
    // 通知中心抽屉
    private DrawerLayout dlDrawer;
    // 滚动气泡
    private ViewSwitcher vsNotifyBubble;

    //业态视图容器
    private FrameLayout flMainFrame;

    private boolean isLeftShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_main);
        initView();
    }

    private void initView() {
        rlRootView = (RelativeLayout) findViewById(R.id.rl_root_view);
        flSlideView = (FrameLayout) findViewById(R.id.fl_slide_view);
        viewShadow = findViewById(R.id.view_shadow);
        dlDrawer = (DrawerLayout) findViewById(R.id.dl_drawer);
        vsNotifyBubble = (ViewSwitcher) findViewById(R.id.vs_notify_bubble);
        flMainFrame = (FrameLayout) findViewById(R.id.fl_main_frame);

        viewShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLeftMenu(false);
            }
        });
        vsNotifyBubble.setOnClickListener(notifyBubbleClickListener);

        initMainFrame();
        initStateBar();
        initSlideMenu();
        initNotifyCenter();
    }

    private void initMainFrame() {
        flMainFrame.addView(getBusinessContentView(),
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void initStateBar() {
        replaceFragment(R.id.fl_state_bar_container, new TitleBarFragment_(), TitleBarFragment.class.getSimpleName());
    }

    private void initSlideMenu() {
        switch (slideMenuType) {
            case SlideMenuView.SLIDE_MENU_TYPE_TRADE:
            case SlideMenuView.SLIDE_MENU_TYPE_GROUP:
                slideMenuView = TradeSlideMenuView_.build(this, slideMenuType);
                break;
        }

        if (slideMenuView != null) {
            slideMenuView.setVisibility(View.GONE);
            slideMenuView.slideMenuListener = getSlideMenuListener();
            rlRootView.addView(slideMenuView, new RelativeLayout.LayoutParams(DensityUtil.dip2px(this, 190),
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    private void initNotifyCenter() {
        dlDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        dlDrawer.setScrimColor(getResources().getColor(R.color.shadow_bg));
        dlDrawer.setFocusableInTouchMode(false);
        dlDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }
        });
    }

    private View.OnClickListener notifyBubbleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!dlDrawer.isDrawerOpen(Gravity.LEFT)) {
                dlDrawer.openDrawer(Gravity.LEFT);
            } else {

            }
        }
    };

    /**
     * 打开或关闭通知中心
     */
    public void switchDrawer() {
        if (dlDrawer.isDrawerOpen(Gravity.LEFT)) {
            dlDrawer.closeDrawer(Gravity.LEFT);
        } else {
            dlDrawer.openDrawer(Gravity.LEFT);
        }
    }

    /**
     * 关闭抽屉
     *
     * @return
     */
    public boolean closeDrawer() {
        if (dlDrawer != null && dlDrawer.isDrawerOpen(Gravity.LEFT)) {
            dlDrawer.closeDrawer(Gravity.LEFT);

            return true;
        }

        return false;
    }

    /**
     * 显示或隐藏左侧菜单
     */
    public void switchLeftMenu() {
        setLeftMenu(!isLeftShown);
    }

    public void setLeftMenu(boolean visible) {
        closeDrawer();

        if (visible) {
            if (!isLeftShown) {
                slideMenuView.setVisibility(View.VISIBLE);

                doAnimation(this, flSlideView, R.animator.fragment_slide_right_exit);
                doAnimation(this, slideMenuView, R.animator.fragment_slide_left_enter);

                // 展开左侧菜单时，显示遮罩
                viewShadow.setVisibility(View.VISIBLE);
            }
        } else {
            if (isLeftShown) {
                doAnimation(this, flSlideView, R.animator.fragment_slide_right_enter);
                doAnimation(this, slideMenuView, R.animator.fragment_slide_left_exit);

                // 关闭左侧菜单时，隐藏遮罩
                viewShadow.setVisibility(View.GONE);
            }
        }
        isLeftShown = visible;
    }

    private void doAnimation(Context context, View view, int id) {
        AnimatorSet objectAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(this, id);
        objectAnimator.setTarget(view);
        objectAnimator.start();
    }

    /**
     * 获取侧边栏菜单的监听
     *
     * @return
     */
    protected abstract SlideMenuListener getSlideMenuListener();

    /**
     * 获取业态对应的业务类型
     *
     * @return
     */
    public abstract EntranceType getEntranceType();

    /**
     * 返回各业态主入口的视图
     */
    protected abstract View getBusinessContentView();

    /**
     * 通知订单数目显示的TextView
     *
     * @return
     */
    protected abstract @NonNull
    TextView getViewNotifyCenterTip();

    /**
     * 通知其他消息数目显示的TextView
     *
     * @return
     */
    protected abstract @NonNull
    TextView getViewNotifyCenterOtherTip();

    protected void showOrderCenterFragment(int from) {
        int childTabType = DbQueryConstant.UNPROCESSED_NEW_ORDER;
        OrderCenterMainFragment mOrderCenterFragment = OrderCenterMainFragment.newInstance(from, childTabType);
        replaceFragment(R.id.main_frame, mOrderCenterFragment, OrderCenterMainFragment.class.getSimpleName());
    }

    /**
     * @Date 2016/6/30
     * @Description 显示报表界面
     * @Param
     * @Return
     */
    /*protected void showReportForm() {
        MobclickAgentEvent.onEvent(this, MobclickAgentEvent.ReportFormMainFragmentShowReportForm);
        VerifyHelper.verifyAlert(this, DinnerApplication.PERMISSION_DINNER_REPORT_FORM,
                new VerifyHelper.Callback() {
                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                        ReportFormMainFragment mReportFormFragment = new ReportFormMainFragment();
                        replaceFragment(R.id.main_frame, mReportFormFragment, ReportFormMainFragment.class.getSimpleName());
                    }
                });
    }*/


    /*protected void showShopManagementMainFragment(int from, ChangePageListener mChangePageListener) {
        int tab = getIntent().getIntExtra(ShopManagementMainFragment.KEY_TAB, ShopManagementMainFragment.TAB_BALANCE);
        ShopManagementMainFragment mShopManagerFragment = ShopManagementMainFragment.newInstance(from, tab);
        mShopManagerFragment.registerListener(mChangePageListener);
        replaceFragment(R.id.main_frame, mShopManagerFragment, ShopManagementMainFragment.class.getSimpleName());
    }*/

    /**
     * 设置当前选中的fragment标识
     *
     * @param pageNo
     */
    protected void onModelChecked(int pageNo) {
        if (slideMenuView != null) {
            slideMenuView.onModelClick(pageNo);
        }
    }

}
