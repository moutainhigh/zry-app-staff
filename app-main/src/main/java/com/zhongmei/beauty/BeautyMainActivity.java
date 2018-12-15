package com.zhongmei.beauty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.zhongmei.beauty.customer.BeautyCustomerContentFragment;
import com.zhongmei.beauty.customer.BeautyCustomerContentFragment_;
import com.zhongmei.beauty.interfaces.IBeautyAnchor;
import com.zhongmei.bty.base.MainBaseActivity;
import com.zhongmei.bty.basemodule.auth.application.CustomerApplication;
import com.zhongmei.bty.basemodule.customer.enums.CustomerAppConfig;
import com.zhongmei.bty.basemodule.database.utils.DbQueryConstant;
import com.zhongmei.bty.basemodule.reportcenter.ReportWebViewFragment;
import com.zhongmei.bty.basemodule.shopmanager.ShopManagementWebViewFragment;
import com.zhongmei.bty.cashier.ordercenter.OrderCenterMainFragment;
import com.zhongmei.bty.constants.OCConstant;
import com.zhongmei.bty.dinner.orderdish.TitleBarFragment;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.init.sync.SyncCheckListener;
import com.zhongmei.yunfu.init.sync.SyncServiceManager;
import com.zhongmei.yunfu.init.sync.SyncServiceUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;

/**
 * btn_anchor_member
 * Created by demo on 2018/12/15
 */
@EActivity(R.layout.beauty_main_activity)
public class BeautyMainActivity extends MainBaseActivity implements IBeautyAnchor, DrawerLayout.DrawerListener {

    @ViewById(R.id.dl_drawer)
    protected DrawerLayout mDrawerLayout;

    //@ViewById(R.id.ncv_notify_center)
    //protected NotifyCenterView mNotifyView;

    @FragmentById(R.id.fragment_titlebar)
    protected TitleBarFragment mTitleBarFragment;

    @FragmentById(R.id.fragment_anchor)
    protected BeautyMainAnchorFragment mAnchorFragment;

    public static void start(Context context) {
        context.startActivity(new Intent(context, BeautyMainActivity_.class));
    }

    @Override
    protected void onCreate(Bundle arg0) {
        CustomerApplication.mCustomerBussinessType = CustomerAppConfig.CustomerBussinessType.BEAUTY;
        super.onCreate(arg0);
    }

    @AfterViews
    protected void initView() {
        mAnchorFragment.setBeautyAnchor(this);
        mTitleBarFragment.setBgColor(R.color.bg_beauty_main);

        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerLayout.setScrimColor(getResources().getColor(R.color.shadow_bg));
        mDrawerLayout.setFocusableInTouchMode(false);
        mDrawerLayout.setDrawerListener(this);

        //mNotifyView.setActivity(this);
        //mNotifyView.setEntranceType(EntranceType.BUFFET);

        toCashier();
    }

    @Override
    public void toCashier() {
        BeautyMainFragment beautyMainFragment = BeautyMainFragment_.builder().build();
        replaceFragment(R.id.layout_contain_content, beautyMainFragment, BeautyMainFragment.class.getSimpleName());
    }

    @Override
    public void toReserverManager() {
        BeautyReserverManagerFragment beautyReserverManagerFragment = BeautyReserverManagerFragment_.builder().build();
        replaceFragment(R.id.layout_contain_content, beautyReserverManagerFragment, BeautyReserverManagerFragment.class.getSimpleName());
    }

    @Override
    public void toTradeCenter() {
        int childTabType = DbQueryConstant.SALES_ALL;
        OrderCenterMainFragment mOrderCenterFragment = OrderCenterMainFragment.newInstance(OCConstant.FromType.FROM_TYPE_BEAUTY, childTabType);
        replaceFragment(R.id.layout_contain_content, mOrderCenterFragment, OrderCenterMainFragment.class.getSimpleName());
    }

    @Override
    public void toMemberCenter() {
        //Intent intent = new Intent(this, BeautyCustomerActivity_.class);
        //startActivity(intent);
        BeautyCustomerContentFragment beautyCustomerContentFragment = BeautyCustomerContentFragment_.builder().build();
        replaceFragment(R.id.layout_contain_content, beautyCustomerContentFragment, BeautyCustomerContentFragment.class.getSimpleName());
    }

    @Override
    public void toReportCenter() {
        Bundle bundle = new Bundle();
        bundle.putInt("fromType", OCConstant.FromType.FROM_TYPE_BEAUTY);
        ReportWebViewFragment mReportFormFragment = new ReportWebViewFragment();
        mReportFormFragment.setArguments(bundle);
        replaceFragment(R.id.layout_contain_content, mReportFormFragment, ReportWebViewFragment.class.getSimpleName());
    }

    @Override
    public void toTechniciaManage() {
        BeautyEmployeeFragment beautyEmployeeFragment = BeautyEmployeeFragment_.builder().build();
        replaceFragment(R.id.layout_contain_content, beautyEmployeeFragment, BeautyEmployeeFragment.class.getSimpleName());

    }

    @Override
    public void toNotifycationCenter() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT, true);
        } else {
            mDrawerLayout.openDrawer(Gravity.LEFT, true);
        }
    }

    @Override
    public void toShopManagerCenter() {
//        int tab = getIntent().getIntExtra(ShopManagementMainFragment.KEY_TAB, ShopManagementMainFragment.TAB_BALANCE);
//        ShopManagementMainFragment mShopManagerFragment =ShopManagementMainFragment.newInstance(OCConstant.FromType.FROM_TYPE_BEAUTY, tab);
//        replaceFragment(R.id.layout_contain_content, mShopManagerFragment, ShopManagementMainFragment.class.getSimpleName());

        ShopManagementWebViewFragment mShopManagerFragment = new ShopManagementWebViewFragment();
        replaceFragment(R.id.layout_contain_content, mShopManagerFragment, ShopManagementWebViewFragment.class.getSimpleName());
    }

    @Override
    public void toShopSyncRefresh(View v) {
        final View viewById = v.findViewById(R.id.rb_anchor_refresh_icon);
        viewById.clearAnimation();
        final RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(500);
        animation.setRepeatCount(Animation.INFINITE);
        viewById.setAnimation(animation);
        animation.startNow();

        SyncServiceUtil.startService(this);
        SyncServiceManager.addCheckListener(new SyncCheckListener() {
            @Override
            public void onSyncCheck(boolean success, String errorMsg, Throwable err) {
                SyncServiceManager.removeCheckListener(this);
                viewById.clearAnimation();
            }
        });
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        //mNotifyView.open();
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        //mNotifyView.hide();
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    protected void onResume() {
        //mNotifyView.resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //mNotifyView.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        //mNotifyView.stop();
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        //mNotifyView.destroy();
        super.onDestroy();
    }
}
