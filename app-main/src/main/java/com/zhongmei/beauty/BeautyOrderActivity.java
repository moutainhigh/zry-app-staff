package com.zhongmei.beauty;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.beauty.event.EventOrderCleanRBChecked;
import com.zhongmei.beauty.events.OrderDishMaskingEvent;
import com.zhongmei.beauty.interfaces.BeautyOrderOperatorListener;
import com.zhongmei.beauty.interfaces.ITableChoice;
import com.zhongmei.beauty.order.BeautyOrderLeftFragment;
import com.zhongmei.beauty.order.BeautyOrderLeftFragment_;
import com.zhongmei.beauty.order.BeautyOrderManager;
import com.zhongmei.beauty.order.BeautyOrderProductFragment;
import com.zhongmei.beauty.order.BeautyOrderTopFragment;
import com.zhongmei.beauty.order.BeautySearchFragment;
import com.zhongmei.beauty.order.BeautySearchFragment_;
import com.zhongmei.beauty.order.BeautySetmealFragment;
import com.zhongmei.beauty.order.BeautySetmealFragment_;
import com.zhongmei.beauty.order.event.BeautyCustmoerEvent;
import com.zhongmei.beauty.order.event.BeautyOrderCustomerEvent;
import com.zhongmei.beauty.order.event.BeautyShopCartLoadEvent;
import com.zhongmei.beauty.order.util.IChangeMiddlePageListener;
import com.zhongmei.beauty.utils.BeautyOrderConstants;
import com.zhongmei.beauty.widgets.BeautyTablePopWindow;
import com.zhongmei.bty.base.MainBaseActivity;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.event.ActionCloseOrderDishActivity;
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListerTag;
import com.zhongmei.bty.common.view.LoadingView;
import com.zhongmei.bty.commonmodule.database.enums.CardRechagingStatus;
import com.zhongmei.bty.dinner.cash.DinnerDishCustomerLogin;
import com.zhongmei.bty.dinner.cash.DinnerDishCustomerLogin_;
import com.zhongmei.bty.dinner.orderdish.DinnerDishCommentFragment;
import com.zhongmei.bty.dinner.orderdish.DishLeftFragment;
import com.zhongmei.bty.dinner.vo.LoadingFinish;
import com.zhongmei.bty.mobilepay.event.ActionClose;
import com.zhongmei.bty.snack.event.EventEditModle;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.ValueEnums;

import java.util.List;

import de.greenrobot.event.EventBus;


public class BeautyOrderActivity extends MainBaseActivity implements View.OnClickListener, ITableChoice, PopupWindow.OnDismissListener {

    private final static String TAG = BeautyOrderActivity.class.getSimpleName();

    public static final String IS_DEFAULT_SETTLE = "isDefaultSettlePage";

    private FragmentManager mFragmentManager;

    private BeautyOrderProductFragment mDishHomePageFragment;

    private BeautySearchFragment mDinnerDishSearchFragment;
    private BeautySetmealFragment mDinnerDishSetmealFragment;
    private DinnerDishCommentFragment mDinnerDishCommentFragment;
    private DinnerDishCustomerLogin mCustomerLogin;



    private int SHOWINDEX;
    private String lastDishUUID = "";

    private DinnerShoppingCart mShoppingCart;


    public LoadingView rightLoading;
    private int lastMiddlePage = IChangeMiddlePageListener.DEFAULT_PAGE;


    private BeautyTablePopWindow tablePopuwindow;

    private AsyncTask initTask;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(getLayoutRes());
        initData();
        init();
        Intent intent = getIntent();
        registerEventBus();
    }

    private int getLayoutRes() {
        return R.layout.beauty_order_layout;
    }

    public void init() {
        rightLoading = (LoadingView) findViewById(R.id.rightLoading);
        mFragmentManager = getSupportFragmentManager();
        buildView();

        tablePopuwindow = new BeautyTablePopWindow(this, BusinessType.BEAUTY);
        tablePopuwindow.setiTableChoiceListener(this);
        tablePopuwindow.setOnDismissListener(this);

    }

    private void initData() {
        mShoppingCart = DinnerShoppingCart.getInstance();
        mShoppingCart.unRegisterListener();

        final boolean isEdit = getIntent().getBooleanExtra(BeautyOrderConstants.IS_ORDER_EDIT, false);
        final Tables tables = (Tables) getIntent().getSerializableExtra(BeautyOrderConstants.ORDER_EDIT_TABLE);
        final Long tradeId = getIntent().getLongExtra(BeautyOrderConstants.ORDER_EDIT_TRADEID, -1);
        initTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void[] objects) {
                BeautyOrderManager.initOrder(isEdit, tradeId, tables, BusinessType.BEAUTY);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.e("BeautyOrderActivity","加载购物车。。。");
                initTableView();
                EventBus.getDefault().post(new BeautyShopCartLoadEvent()); //购物车加载完成
            }
        }.execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initTableView() {
        if (DinnerShoppingCart.getInstance().getOrder().getTradeTableList() != null && mDishHomePageFragment!=null) {
            mDishHomePageFragment.setTables(DinnerShoppingCart.getInstance().getOrder().getTradeTableList());
        }
    }

    public void onEventMainThread(BeautyOrderCustomerEvent event) {
        if (event.mEventFlag == BeautyOrderCustomerEvent.EventFlag.LOGIN) {
            new DinnerCashManager().updateIntegralCash(event.mCustomerNew);
            if (event.mCustomerNew.card == null || event.mCustomerNew.card != null && event.mCustomerNew.card.getRightStatus() == CardRechagingStatus.EFFECTIVE) {
                DinnerShopManager.getInstance().getShoppingCart().memberPrivilege(true, true);
            }
        }
    }

    public void buildView() {
        buildFragment();
    }


    public void buildFragment() {
        mDishHomePageFragment = new BeautyOrderProductFragment();

        mDishHomePageFragment.registerListener(mChangePageListener);

        mDishHomePageFragment.registerLoadingListener(mRightLoadingFinish);

        SHOWINDEX = ChangePageListener.ORDERDISHLIST;
        replaceFragment(R.id.orderDishRightView, mDishHomePageFragment, mDishHomePageFragment.getClass().getName(), false);
    }




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
            if (SHOWINDEX == pageNo && itemUUid.equals(lastDishUUID)) {
                return;
            }

            switch (pageNo) {
                case ChangePageListener.ORDERDISHLIST:
                    showDishListFragment(mDishHomePageFragment);
                    break;

                case ChangePageListener.SEARCH:
                    EventBus.getDefault().post(new EventEditModle(true));
                    mDinnerDishSearchFragment = new BeautySearchFragment_();
                    mDinnerDishSearchFragment.registerListener(mChangePageListener);
                    changeFragment(R.id.orderDishRightView, mDinnerDishSearchFragment);
                    break;

                case ChangePageListener.DISHCOMBO:
                    EventBus.getDefault().post(new EventEditModle(true));
                    removeFragment();
                    mDinnerDishSetmealFragment = new BeautySetmealFragment_();
                    mDinnerDishSetmealFragment.setArguments(bundle);
                    mDinnerDishSetmealFragment.registerListener(mChangePageListener);
                    changeFragment(R.id.orderDishRightView, mDinnerDishSetmealFragment);
                    DishDataItem dishDataItem = null;
                    break;

                case ChangePageListener.SAVE_BACK:
                    if (SHOWINDEX != ChangePageListener.ORDERDISHLIST) {
                        showDishListFragment(mDishHomePageFragment);
                        pageNo = ChangePageListener.ORDERDISHLIST;
                    } else {
                        BeautyOrderActivity.this.finish();
                    }
                    break;
                case ChangePageListener.PAGE_TABLE_HOME:
                    BeautyOrderActivity.this.finish();
                    break;

                case ChangePageListener.REMOBER_LOGIN:
                    mCustomerLogin = new DinnerDishCustomerLogin_();
                    mCustomerLogin.registerListener(mChangePageListener);
                    changeFragment(R.id.orderDishRightView, mCustomerLogin);
                    break;

                case ChangePageListener.DISH_CUSTOMER_COUPONS:
                    break;
                case ChangePageListener.PAGE_CANCEL_MARKET:
                    EventBus.getDefault().post(new EventEditModle(false));
                    break;
                default:
                    break;
            }
            mShoppingCart.setIndexPage(pageNo);
            SHOWINDEX = pageNo;
            lastDishUUID = itemUUid;
        }

        @Override
        public void clearShoppingCart() {
        }
    };


    private void changeFragment(int containerViewId, Fragment fragment) {
        if (SHOWINDEX == ChangePageListener.ORDERDISHLIST) {
            hideFragment(mDishHomePageFragment);
            addFragment(containerViewId, fragment, fragment.getClass().getName());
        } else {
            removeFragment();
            addFragment(containerViewId, fragment, fragment.getClass().getName());
        }
    }


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
        EventBus.getDefault().post(new EventEditModle(false));
    }

    private void removeFragment() {
        switch (SHOWINDEX) {

            case ChangePageListener.ORDERDISHLIST:
                break;

            case ChangePageListener.SEARCH:
                removeFragment(mDinnerDishSearchFragment, mDinnerDishSearchFragment.getClass().getName());
                break;

            case ChangePageListener.DISHCOMBO:
                removeFragment(mDinnerDishSetmealFragment, mDinnerDishSetmealFragment.getClass().getName());
                break;

            case ChangePageListener.SAVE_BACK:
                break;
            case ChangePageListener.PAGE_TABLE_HOME:
                break;
            case ChangePageListener.ORDER_COMMENTS:
                removeFragment(mDinnerDishCommentFragment, mDinnerDishCommentFragment.getClass().getName());
                break;

            case ChangePageListener.REMOBER_LOGIN:
                removeFragment(mCustomerLogin, mCustomerLogin.getClass().getName());
                break;

            case ChangePageListener.DISH_CUSTOMER_COUPONS:
                break;
            default:
                break;


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
            case R.id.view_shadow:
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            clearShopCart();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterEventBus();
        super.onDestroy();
    }

    public void onEventMainThread(ActionCloseOrderDishActivity event) {
        this.finish();
        //晴空购物车
        clearShopCart();
    }

    public void clearShopCart(){
        Log.e("BeautyOrderActivity","清空购物车。。。");
        initTask.cancel(true);
        DinnerShoppingCart.getInstance().unRegisterListenerByTag(ShoppingCartListerTag.ORDER_DISH_LEFT);
        BeautyOrderManager.clearShopcart();
    }



    public void setTables(List<TradeTable> tradeTables) {
        if (Utils.isEmpty(tradeTables)) {
            return;
        }
        String tablesNameTmp = "";
        for (TradeTable tb : tradeTables) {
            tablesNameTmp += tb.getTableName() + ",";
        }
        String tableName = tablesNameTmp.substring(0, tablesNameTmp.length() - 1);
        if (tablePopuwindow != null) {
            tablePopuwindow.settables(tradeTables);
        }
    }

    @Override
    public void choiceTables(List<Tables> tables) {
        String tableTmp = "";
        if (tables != null) {
            for (Tables t : tables) {
                tableTmp += t.getTableName() + ",";
            }
            DinnerShoppingCart.getInstance().updateOrCreateTables(tables, true);
            String tableName = tableTmp.substring(0, tableTmp.length() - 1);
        }
    }

    @Override
    public void onDismiss() {
        EventBus.getDefault().post(new OrderDishMaskingEvent(false));
    }
}
