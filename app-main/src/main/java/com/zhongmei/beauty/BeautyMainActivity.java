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
import android.widget.ImageView;
import android.widget.TextView;

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
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.beauty_main_activity)
public class BeautyMainActivity extends MainBaseActivity implements IBeautyAnchor, DrawerLayout.DrawerListener {

    public static final int PAGE_CASHIER=0x01;
    public static final int PAGE_RESERVER=0x02;
    public static final int PAGE_TRADE_CENTER=0x03;
    public static final int PAGE_MEMBER_CENTER=0x04;
    public static final int PAGE_SHOP_MANAGE=0x05;
    public static final int PAGE_REPORT_CENTER =0x06;
    public static final int PAGE_TASK_CENTER =0x07;

    @ViewById(R.id.back_btn)
    protected ImageView btn_back;

    @ViewById(R.id.title_name)
    protected TextView tv_title;

    @ViewById(R.id.dl_drawer)
    protected DrawerLayout mDrawerLayout;



//    @FragmentById(R.id.fragment_anchor)
//    protected BeautyMainAnchorFragment mAnchorFragment;

    private int currentPage=PAGE_CASHIER;
    private String moduleTitle="";

    public static void start(Context context) {
        context.startActivity(new Intent(context, BeautyMainActivity_.class));
    }

    @Click(R.id.back_btn)
    public void onBackClick(View v){
        this.finish();
    }


    @Override
    protected void onCreate(Bundle arg0) {
        CustomerApplication.mCustomerBussinessType = CustomerAppConfig.CustomerBussinessType.BEAUTY;
        super.onCreate(arg0);
    }

    @AfterViews
    protected void initView() {
//        mAnchorFragment.setBeautyAnchor(this);

        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerLayout.setScrimColor(getResources().getColor(R.color.shadow_bg));
        mDrawerLayout.setFocusableInTouchMode(false);
        mDrawerLayout.setDrawerListener(this);


//        toCashier();
          initPage();
    }


    private void initPage(){
        Intent intent=getIntent();
        if(intent!=null){
            currentPage=intent.getIntExtra("page_no",PAGE_CASHIER);
            moduleTitle=intent.getStringExtra("module_name");
        }

        tv_title.setText(moduleTitle);

        switch (currentPage) {
            case PAGE_CASHIER:
                toCashier();
                break;
            case PAGE_RESERVER:
                toReserverManager();
                break;
            case PAGE_TRADE_CENTER:
                toTradeCenter();
                break;
            case PAGE_MEMBER_CENTER:
                toMemberCenter();
                break;
            case PAGE_SHOP_MANAGE:
                toShopManagerCenter();
                break;
            case PAGE_REPORT_CENTER:
                toReportCenter();
                break;
            case PAGE_TASK_CENTER:
                toTaskCenter();
                break;


        }
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
    public void toTaskCenter() {
                BeautyTaskFragment beautyTaskFragment = BeautyTaskFragment_.builder().build();
        replaceFragment(R.id.layout_contain_content, beautyTaskFragment, BeautyReserverManagerFragment.class.getSimpleName());
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

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    protected void onResume() {
                super.onResume();
    }

    @Override
    protected void onPause() {
                super.onPause();
    }

    @Override
    protected void onStop() {
                super.onStop();
    }


    @Override
    protected void onDestroy() {
                super.onDestroy();
    }
}
