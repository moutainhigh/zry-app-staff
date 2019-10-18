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

public abstract class BusinessMainActivity extends MainBaseActivity {

    protected int slideMenuType = SlideMenuView.SLIDE_MENU_TYPE_TRADE;

    private SlideMenuView slideMenuView;

    private RelativeLayout rlRootView;
    private FrameLayout flSlideView;
        private View viewShadow;
        private DrawerLayout dlDrawer;
        private ViewSwitcher vsNotifyBubble;

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

    public void switchDrawer() {
        if (dlDrawer.isDrawerOpen(Gravity.LEFT)) {
            dlDrawer.closeDrawer(Gravity.LEFT);
        } else {
            dlDrawer.openDrawer(Gravity.LEFT);
        }
    }

    public boolean closeDrawer() {
        if (dlDrawer != null && dlDrawer.isDrawerOpen(Gravity.LEFT)) {
            dlDrawer.closeDrawer(Gravity.LEFT);

            return true;
        }

        return false;
    }

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

                                viewShadow.setVisibility(View.VISIBLE);
            }
        } else {
            if (isLeftShown) {
                doAnimation(this, flSlideView, R.animator.fragment_slide_right_enter);
                doAnimation(this, slideMenuView, R.animator.fragment_slide_left_exit);

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

    protected abstract SlideMenuListener getSlideMenuListener();

    public abstract EntranceType getEntranceType();

    protected abstract View getBusinessContentView();

    protected abstract @NonNull
    TextView getViewNotifyCenterTip();

    protected abstract @NonNull
    TextView getViewNotifyCenterOtherTip();

    protected void onModelChecked(int pageNo) {
        if (slideMenuView != null) {
            slideMenuView.onModelClick(pageNo);
        }
    }

}
