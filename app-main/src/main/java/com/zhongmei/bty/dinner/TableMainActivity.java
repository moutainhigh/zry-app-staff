package com.zhongmei.bty.dinner;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRouter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.zhongmei.bty.base.BusinessMainActivity;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.auth.permission.dialog.VerifyPermissionsDialogFragment.PermissionVerify;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.database.enums.EntranceType;
import com.zhongmei.bty.basemodule.database.utils.DbQueryConstant;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.devices.led.LedControl;
import com.zhongmei.bty.basemodule.notifycenter.event.EventSelectDinnertableNotice;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.slidemenu.listener.SlideMenuListener;
import com.zhongmei.bty.basemodule.slidemenu.listener.TradeSlideMenuListener;
import com.zhongmei.bty.basemodule.slidemenu.view.SlideMenuView;
import com.zhongmei.bty.basemodule.slidemenu.view.TradeSlideMenuView;
import com.zhongmei.bty.basemodule.trade.action.EventMaskShowStatus;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.cashier.ordercenter.OrderCenterMainFragment;
import com.zhongmei.bty.common.view.LoadingView;
import com.zhongmei.bty.common.view.ui.widget.DinnerLeftMenuView;
import com.zhongmei.bty.dinner.table.TableInfoFragment;
import com.zhongmei.bty.dinner.table.view.TableFragmentBase;
import com.zhongmei.bty.dinner.vo.LoadingFinish;
import com.zhongmei.bty.dinner.vo.SwitchTableTradeVo;
import com.zhongmei.bty.mobilepay.v1.event.ShowCouponUrlEvent;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ValueEnum;

import java.util.List;

public abstract class TableMainActivity extends BusinessMainActivity implements PermissionVerify {

    public static final String TAG = DinnerMainActivity.class.getSimpleName();

    private int FROMDINNER = 1;// 来源于正餐

    private DinnerLeftMenuView mDlvMenu;//侧边菜单

    private LoadingView tableLoading;//加载框

    protected TableFragmentBase mDinnertableFragment;

    private int pageTag = -1;

    protected int lastPage = -1;

    //private DisplayQRCodePresentation presentation; //领劵二维码

    private CountDownTimer timer;

    private View viewMask;//popupwindow辅助背景，销售排行榜选择项时使用

    public TableMainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        slideMenuType = SlideMenuView.SLIDE_MENU_TYPE_TRADE;

        super.onCreate(savedInstanceState);
        setupLeftMenuView();
        initBusinessView();

        init();
    }

    /**
     * 初始化业态自己的view
     */
    private void initBusinessView() {
        tableLoading = (LoadingView) findViewById(R.id.tableLoading);
        viewMask = findViewById(R.id.view_mask);
    }

    private void setupLeftMenuView() {
        mDlvMenu = (DinnerLeftMenuView) findViewById(R.id.dlvMenu);
        mDlvMenu.setEntranceType(getEntranceType());
    }

    protected void init() {
        Log.i(TAG, "init");
        // 监听数据库变化

        DinnerShoppingCart.getInstance().clearShoppingCart();
        initMainFrame();

        registerEventBus();
        pageTag = getIntent().getIntExtra(INIT_PAGE, -1);//获取传入指定展示界面;
        if (pageTag == -1) {
            tableLoading.setVisibility(View.VISIBLE);
            changePageManager(ChangePageListener.PAGE_TABLE_HOME, getIntent().getExtras());
        } else {
            changePageManager(pageTag, getIntent().getExtras());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        deelInputKeyboard();
        // 2016-1-26解决收银成功回到桌面后偶先键盘bug
        MobclickAgentEvent.onResume(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "onNewIntent() <===> intent = " + intent);
        if (intent != null) {
            /*int state = intent.getIntExtra(PayManager.CASH_PAY_STATE, -1);
            if (state == PayManager.CASH_STATE_OK) {
                changePageManager(ChangePageListener.PAGE_TABLE_HOME, null);
            }*/

            Long tableId = intent.getLongExtra("tableId", -1);
            if (tableId > 0 && mDinnertableFragment != null && lastPage == ChangePageListener.PAGE_TABLE_HOME) {
                //通知栏点击通知，选择桌台
                SwitchTableTradeVo switchTableVo = new SwitchTableTradeVo(null, tableId);
                mDinnertableFragment.selectTrade(switchTableVo);
            }

            pageTag = intent.getIntExtra(INIT_PAGE, -1);//获取传入指定展示界面;
            if (pageTag >= 0 && lastPage != pageTag) {
                changePageManager(pageTag, intent.getExtras());
            }
        }

        closeDrawer();
    }

    private void initMainFrame() {
    }

    @Override
    protected SlideMenuListener getSlideMenuListener() {
        return new TradeSlideMenuListener() {
            @Override
            public boolean switchToTrade() {
                setLeftMenu(false);
                showDinnertableManager(null);

                return true;
            }

            @Override
            public boolean switchToOrderCenter() {
                setLeftMenu(false);
                showOrderCenter(DbQueryConstant.UNPROCESSED_NEW_ORDER);

                return true;
            }

            @Override
            public boolean switchToShopManagement() {
                setLeftMenu(false);
                showShopManagementMainFragment();

                return true;
            }

            @Override
            public boolean switchToFormCenter() {
                setLeftMenu(false);
                showReportForm();

                return true;
            }

            @Override
            public boolean backHome() {
                finish();
                return true;
            }
        };
    }

    public ChangePageListener mChangePageListener = new ChangePageListener() {

        @Override
        public void changePage(int pageNo, Bundle bundle) {
            changePageManager(pageNo, bundle);
        }

        @Override
        public void clearShoppingCart() {

        }

    };

    /**
     * 监听要求选中指定桌台的通知
     * ßßß
     *
     * @param event
     */
    public void onEventMainThread(EventSelectDinnertableNotice event) {
        closeDrawer();
        SwitchTableTradeVo switchTableTradeVo = new SwitchTableTradeVo(event.zoneId, event.dinnertableId, event.tradeId, event.isCloseTableInfoFragment);
        if (mDinnertableFragment != null && mDinnertableFragment == getSupportFragmentManager().findFragmentById(R.id.main_frame)) {
            mDinnertableFragment.selectTrade(switchTableTradeVo);
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(SwitchTableTradeVo.KEY, switchTableTradeVo);
            showDinnertableManager(bundle);
        }
    }

    /**
     * 显示蒙板
     *
     * @param action
     */
    public void onEventMainThread(EventMaskShowStatus action) {
        if (action.isVisiable()) {
            viewMask.setVisibility(View.VISIBLE);
        } else {
            viewMask.setVisibility(View.GONE);
        }
    }

    /**
     * @Title: showDinnertableManaager
     * @Description: 展示桌台界面
     * @Return void 返回类型
     */
    public void showDinnertableManager(Bundle bundle) {
        if (lastPage == ChangePageListener.PAGE_TABLE_HOME) {
            return;
        }
        lastPage = ChangePageListener.PAGE_TABLE_HOME;

        DinnerShoppingCart.getInstance().clearShoppingCart();
        mDinnertableFragment = getTableFragment();
        mDinnertableFragment.registerLoadingListener(tableLoadingFinish);

        if (bundle != null) {
            mDinnertableFragment.setArguments(bundle);
        }
        replaceFragment(R.id.main_frame, mDinnertableFragment, TableFragmentBase.TAG_TABLE_FRAGMENT);
        onModelChecked(TradeSlideMenuView.PAGE_TRADE);
    }

    public LoadingFinish tableLoadingFinish = new LoadingFinish() {
        @Override
        public void loadingFinish() {
            tableLoading.setVisibility(View.GONE);
        }
    };

    /**
     * @param orderType 跳转单据中心后要展示的单据类型，－1为默认
     * @Title: showOrderCenter
     * @Description: 展示票据中心
     * @Return void 返回类型
     */
    protected void showOrderCenter(int orderType) {
        showOrderCenter(orderType, null);
    }

    protected void showOrderCenter(int orderType, List<ValueEnum> filterCond) {
        Log.e(this.getClass().getSimpleName(), "showOrderCenter() <====> orderType = " + orderType);
        lastPage = ChangePageListener.PAGE_ORDER_CENTER;

        DinnerShoppingCart.getInstance().clearShoppingCart();
        OrderCenterMainFragment mOrderCenterFragment = OrderCenterMainFragment.newInstance(FROMDINNER, orderType, false, null, filterCond);
        replaceFragment(R.id.main_frame, mOrderCenterFragment, mOrderCenterFragment.getClass().getName());
        onModelChecked(TradeSlideMenuView.PAGE_ORDER_CENTER);

    }

    public void changePageManager(int pageTag, Bundle bundle) {

        Log.e(this.getClass().getSimpleName(), "changePageManager() <===> pageTag = " + pageTag);
        if (pageTag == ChangePageListener.PAGE_ORDER_DISH) {

            showOrderDish();
//        } else if (pageTag == ChangePageListener.PAGE_HANDOVER) {//交接在门店管理内部处理
//
//            showHandOver();
        } else if (pageTag == ChangePageListener.PAGE_ORDER_CENTER) {//进入订单中心
            int childTabType = DbQueryConstant.UNPROCESSED_NEW_ORDER;
            if (bundle != null) {
                childTabType = bundle.getInt(Constant.EXTRA_CURRENT_TAB, DbQueryConstant.UNPROCESSED_NEW_ORDER);
            }
            showOrderCenter(childTabType);

        } else if (pageTag == ChangePageListener.PAGE_TABLE_HOME) {// 进入桌台

            /*final EventSelectDinnertableNotice event = (EventSelectDinnertableNotice) this.getIntent().getSerializableExtra("EventSelectDinnertable");
            if (event != null) {
                onEventMainThread(event);
            } else {

                Bundle bundle_checkTable = null;
                if (bundle != null) {
                    Long tableId = bundle.getLong("tableId", -1);//通知中心点击通知，数据传递
                    if (tableId > 0) {
                        bundle_checkTable = new Bundle();
                        bundle_checkTable.putLong("tableId", tableId);
                    }
                }
                showDinnertableManager(bundle_checkTable);
            }*/
        } else if (pageTag == ChangePageListener.SHOP_MANAGEMENT_MAIN_FRAGMENT) {
            showShopManagementMainFragment();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        if (!closeDrawer()) {
            super.onBackPressed();
        }
    }

    /*public void onEventMainThread(ActionShowTrade actionShowTrade) {
        if (!ActivityUtils.isForeground(this, getClass().getName())) {
            return;
        }

        closeDrawer();

        // 正餐通知中心只展示正餐的单据，直接跳转到正餐单据中心
        showOrderCenter(actionShowTrade.getOrderType(), actionShowTrade.getFilterConditions());
    }*/


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgentEvent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        /*if(presentation != null){
            presentation.dismiss();
            presentation = null;
        }*/

        // 退出时，关闭所有led灯
        LedControl.getInstance().setLed(LedControl.Event.NEW_TRADE_COMING, false);
        LedControl.getInstance().setLed(LedControl.Event.PRINT_FAIL, false);

        CustomerManager.getInstance().setDinnerLoginCustomer(null);
        DinnerShoppingCart.getInstance().clearShoppingCart();

        //清除本地记录的预结单打印次数
        DinnerShopManager.getInstance().clearLocalPrepayPrintCount();

        unregisterEventBus();
        DinnerShopManager.getInstance()._unRegisterObserver();
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void verify(String permission, boolean success) {
        if (success) {
            //PRTPrintContentQueue.getCommonPrintQueue().openMoneyBox(null);
            //IPrintHelper.Holder.getInstance().openMoneyBox();
        }
    }

    /**
     * 显示副屏二维码领券
     *
     * @param event
     */
    public void onEventMainThread(ShowCouponUrlEvent event) {
        DisplayServiceManager.doCancel(this);
        if (event.getUrl() == null) {
            /*if(presentation != null && presentation.isShowing()){
                presentation.dismiss();
                presentation = null;
            }*/
        } else {
            MediaRouter mMediaRouter = (MediaRouter) getSystemService(Context.MEDIA_ROUTER_SERVICE);
            MediaRouter.RouteInfo route = mMediaRouter.getSelectedRoute(
                    MediaRouter.ROUTE_TYPE_LIVE_VIDEO);
            Display presentationDisplay = route != null ? route.getPresentationDisplay() : null;
            /*if (presentation != null && presentation.getDisplay() != presentationDisplay) {
                presentation.dismiss();
                presentation = null;
            }
            if (presentation == null && presentationDisplay != null) {
                presentation = new DisplayQRCodePresentation(getApplicationContext(), presentationDisplay, event.getUrl());
                WindowManager.LayoutParams lp = presentation.getWindow().getAttributes();
                lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                presentation.getWindow().setAttributes(lp);
                try {
                    presentation.show();
                    presentation.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                            }
                        }
                    });
                    timer = new CountDownTimer(15 * 1000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            timer = null;
                            if(presentation != null){
                                presentation.dismiss();
                                presentation = null;
                            }
                        }
                    };
                    timer.start();
                } catch (WindowManager.InvalidDisplayException e) {
                    Log.e(TAG, e.getMessage(), e);
                    presentation = null;
                }
            }*/
        }
    }

    /**
     * @Date 2016/6/30
     * @Description 显示报表界面
     * @Param
     * @Return
     */
    protected void showReportForm() {
        MobclickAgentEvent.onEvent(this,
                MobclickAgentEvent.ReportFormMainFragmentShowReportForm);
        if (lastPage == ChangePageListener.REPORT_FORM_FRAGMENT) {
            return;
        }
        VerifyHelper.verifyAlert(this, DinnerApplication.PERMISSION_DINNER_REPORT_FORM,
                new VerifyHelper.Callback() {
                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                        lastPage = ChangePageListener.REPORT_FORM_FRAGMENT;
                        //ReportFormMainFragment mReportFormFragment = new ReportFormMainFragment();
                        //replaceFragment(R.id.main_frame, mReportFormFragment, ReportFormMainFragment.class.getSimpleName());
                        onModelChecked(TradeSlideMenuView.PAGE_FORM_CENTER);
                    }
                });
    }


    private void showShopManagementMainFragment() {
        if (lastPage == ChangePageListener.SHOP_MANAGEMENT_MAIN_FRAGMENT) {
            return;
        }
        lastPage = ChangePageListener.SHOP_MANAGEMENT_MAIN_FRAGMENT;

        //int tab = getIntent().getIntExtra(ShopManagementMainFragment.KEY_TAB, ShopManagementMainFragment.TAB_BALANCE);
        //ShopManagementMainFragment mShopManagerFragment = ShopManagementMainFragment.newInstance(FROMDINNER, tab);
        //mShopManagerFragment.registerListener(mChangePageListener);
        //replaceFragment(R.id.main_frame, mShopManagerFragment, ShopManagementMainFragment.class.getSimpleName());
        onModelChecked(TradeSlideMenuView.PAGE_SHOP_MANAGEMENT);
    }

    /**
     * 根据业务权限是否显示排队
     */
    private boolean isShowQueue() {
//        return AuthBusinessManager.isExistAuthBusiness(AuthBusinessCode.BUSINESS_QUEUE);
        return true;
    }

    /**
     * 根据业务权限是否显示预定
     */
    private boolean isShowBooing() {
//        return AuthBusinessManager.isExistAuthBusiness(AuthBusinessCode.BUSINESS_BOOKING);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        TableInfoFragment tableInfoFragment = getTableInfoFragment();
        if (tableInfoFragment != null && tableInfoFragment.isVisible()) {
            if (tableInfoFragment.hideNumberAndWaiterWindow(ev)
                    || tableInfoFragment.hideTradeGridWindow(ev)
                    || tableInfoFragment.hideOpenTableWindow(ev)) {
                return false;
            }
        }


        if (mDinnertableFragment != null && mDinnertableFragment.isVisible()) {
            if (mDinnertableFragment.hideMoreMenuPop(ev)) {
                return false;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 获取桌台Fragment
     *
     * @return
     */
    public abstract TableFragmentBase getTableFragment();

    /**
     * 获取业务类型
     *
     * @return
     */
    public abstract EntranceType getEntranceType();

    /**
     * 获取桌台详情UI
     *
     * @return
     */
    public abstract TableInfoFragment getTableInfoFragment();

    /**
     * 跳转到点餐页面
     */
    public abstract void showOrderDish();

    @NonNull
    @Override
    protected TextView getViewNotifyCenterTip() {
        return (TextView) findViewById(R.id.view_notify_tip);
    }

    @NonNull
    @Override
    protected TextView getViewNotifyCenterOtherTip() {
        return (TextView) findViewById(R.id.view_notify_tip_other);
    }
}
