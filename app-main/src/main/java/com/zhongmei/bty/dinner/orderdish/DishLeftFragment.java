package com.zhongmei.bty.dinner.orderdish;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.commonbusiness.constants.IntentNo;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.basemodule.trade.utils.DinnerUtils;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListener;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListerTag;
import com.zhongmei.bty.commonmodule.util.SpendTimeFormater;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.dinner.Listener.DishOptListener;
import com.zhongmei.bty.dinner.action.ActionBatchOperationItems;
import com.zhongmei.bty.dinner.action.ActionDinnerChangePage;
import com.zhongmei.bty.dinner.manager.DinnerTradeItemManager;
import com.zhongmei.bty.dinner.orderdish.view.NumberAndWaiterView;
import com.zhongmei.bty.dinner.shopcart.adapter.DinnerShopCartAdapter;
import com.zhongmei.bty.dinner.table.view.DinnertableTradeView;
import com.zhongmei.bty.dinner.vo.LoadingFinish;
import com.zhongmei.bty.router.RouteIntent;
import com.zhongmei.bty.snack.event.EventChoice;
import com.zhongmei.bty.snack.event.EventEditModle;
import com.zhongmei.bty.snack.orderdish.view.QuantityEditPopupWindow;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.util.ThreadUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.ui.base.MobclickAgentFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment.CommonDialogFragmentBuilder;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.UserActionCode;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import de.greenrobot.event.EventBus;

import static com.zhongmei.yunfu.db.enums.PrintOperationOpType.BATCH_OPERATION;
import static com.zhongmei.yunfu.db.enums.PrintOperationOpType.REMIND_DISH;

@EFragment(R.layout.dinner_left)
abstract public class DishLeftFragment extends MobclickAgentFragment implements BasicFragment.OnActivityTouchListener {

    private final static String TAG = DishLeftFragment.class.getSimpleName();

    @ViewById(R.id.main)
    protected RelativeLayout main;

    @ViewById(R.id.order_dish_bottom)
    protected LinearLayout orderDishBottom;

    @ViewById(R.id.view_bottom_shadow)
    protected View viewBottomShadow;

    @ViewById(R.id.cashView)
    protected LinearLayout cashView;

    @ViewById(R.id.rl_save_print)
    protected LinearLayout rlSavePrint;
    @ViewById(R.id.btn_pre_pay)
    protected Button btnPrePay;
    @ViewById(R.id.order_button_panel_first)
    protected RelativeLayout orderButtonPanelFirst;

    @ViewById(R.id.rl_back)
    protected RelativeLayout rlBack;

        @ViewById(R.id.setMemo)
    protected ImageButton btn_memeo;

    @ViewById(R.id.cus_dtv_trade)
    protected DinnertableTradeView cus_dtTrade;

    @ViewById(R.id.pager)
    protected FrameLayout pager;

    @ViewById(R.id.fl_num_waiter)
    protected FrameLayout numWaiterLayout;

        @ViewById(R.id.deskNo)
    protected TextView tv_deskNo;

        @ViewById(R.id.floor)
    protected TextView tv_floor;

        @ViewById(R.id.peoplecount)
    protected TextView tv_peoplecount;

        @ViewById(R.id.dinner_bill_no)
    protected TextView tv_billNo;

        @ViewById(R.id.delaytime)
    protected TextView tv_delaytime;

        @ViewById(R.id.dish_operate_checkbar)
    protected LinearLayout dishOperateCheckbar;

        @ViewById(R.id.dish_operate_bottombar_ll)
    protected LinearLayout dishOperateBottombarLL;

        @ViewById(R.id.cancel_btn)
    protected Button btnCancel;


        @ViewById(R.id.allcheck_cb)
    public CheckBox allCheckCb;

        @ViewById(R.id.check_number_tv)
    public TextView checkNumberTv;

        @ViewById(R.id.operate_name_tv)
    protected TextView operateNameTv;

        @ViewById(R.id.ll_number_and_waiter)
    protected LinearLayout llNumberAndWaiter;

        @ViewById(R.id.iv_number_and_waiter_tip)
    protected ImageView ivNumberAndWaiterTip;

    @ViewById(R.id.tv_customer_count)
    protected TextView tvCustomerCount;

    @ViewById(R.id.tv_waiter_name)
    protected TextView tvWaiterName;

        protected NumberAndWaiterView viewNumberAndWaiter;

        private QuantityEditPopupWindow mPopupWindow;

    private int currentPagerIndex = 0;

    private final int FLAG_NORMAL = 0;

    private final int FLAG_PRESS = 1;

    private int mClearBtnflag = 0;

    protected DinnerShoppingCart mShoppingCart;


    private DinnerDishTradeInfoFragment tradeInfoFragment;
    private DinnerDishSequenceFragment dinnerDishSequenceFragment;


    public TradePrivilege privilege;

    public String SUCCESSFULL = "successfully";

    public String PAYSTATUS = "payStatus";

    private ChangePageListener mChangePageListener;

    private FragmentManager mFragmentManager;

    private boolean isOrderDishMode = true;
        private boolean isFromDesk = false;

        private boolean isQuickOpenTable = false;

        private Integer tableTotalCount = null;

    protected LoadingFinish mLoadingFinish;
    private DinnerDishMiddleFragment.IChangePageListener middleChangeListener;

    private boolean isInit = true;

    public abstract NumberAndWaiterView getViewNumberAndWaiter();


    protected abstract boolean isOnlySingleAddDish(List<String> uuids);


    protected abstract boolean isPrintLabel();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getActivity().getSupportFragmentManager();
        setRetainInstance(true);

    }


    public DishDataItem doSelected(ShopcartItem item) {
        if (item == null) {
            return null;
        }
        DishDataItem dishDataItem = null;
        try {
            dishDataItem = tradeInfoFragment.getSelectedDishAdapter().doSelectedItem(item.getUuid());
            tradeInfoFragment.goToPosition(tradeInfoFragment.getSelectedDishAdapter().mSelectPostions.get(0));
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
        return dishDataItem;
    }


    public void clearAllSelected() {
        tradeInfoFragment.getSelectedDishAdapter().clearAllSelected();
    }


    public void setIsFromTable(boolean isFromDesk) {
        this.isFromDesk = isFromDesk;
    }

    public void setQuickOpenTable(boolean quickOpenTable) {
        isQuickOpenTable = quickOpenTable;
    }

    public void registerListener(ChangePageListener mChangePageListener, DinnerDishMiddleFragment.IChangePageListener middleChangeListener) {
        this.mChangePageListener = mChangePageListener;
        this.middleChangeListener = middleChangeListener;
    }

    public void registerLoadingListener(LoadingFinish mLoadingFinish) {
        this.mLoadingFinish = mLoadingFinish;
    }

    @AfterViews
    protected void init() {

        mShoppingCart = DinnerShoppingCart.getInstance();

        mShoppingCart.registerListener(ShoppingCartListerTag.ORDER_DISH_LEFT, mModifyShoppingCartListener);
        initViewPager();

        setDeskInfo();

        if (mShoppingCart.getOrder().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN
                || mShoppingCart.getOrder().getTrade().getTradeType() == TradeType.UNOIN_TABLE_SUB) {
            btn_memeo.setVisibility(View.GONE);
        } else
            btn_memeo.setVisibility(View.VISIBLE);

                allCheckCb.setOnClickListener(clickLisenter);
        loadTradeView();
        if (mLoadingFinish != null)
            mLoadingFinish.loadingFinish();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (numWaiterLayout != null)
            initNumberAndWaiter();
    }


    public void initNumberAndWaiter() {
        viewNumberAndWaiter = getViewNumberAndWaiter();
        if (viewNumberAndWaiter != null)
            numWaiterLayout.addView(viewNumberAndWaiter, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                                        TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
                    if (tradeVo != null && Utils.isNotEmpty(tradeVo.getTradeTableList())) {
                        TradeTable tradeTable = tradeVo.getTradeTableList().get(0);
                        Tables tables = DBHelperManager.queryById(Tables.class, tradeTable.getTableId());
                        if (tables != null) {
                            tableTotalCount = tables.getTablePersonCount();
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                updateNumberAndWaiter();
            }
        }.execute();

        if (isBuffetOpenTable() || isQuickOpenTable) {
                        numWaiterLayout.setVisibility(View.VISIBLE);
            ivNumberAndWaiterTip.setVisibility(View.VISIBLE);
        } else {
                        numWaiterLayout.setVisibility(View.GONE);
            ivNumberAndWaiterTip.setVisibility(View.GONE);
        }
        if (viewNumberAndWaiter != null) {
            viewNumberAndWaiter.setStatusChangeListener(new NumberAndWaiterView.StatusChangeListener() {
                @Override
                public void onDataChanged() {
                    updateNumberAndWaiter();
                }

                @Override
                public void onSubmit() {
                    numWaiterLayout.setVisibility(View.GONE);
                    ivNumberAndWaiterTip.setVisibility(View.GONE);
                }

                @Override
                public void onCancel() {
                    numWaiterLayout.setVisibility(View.GONE);
                    ivNumberAndWaiterTip.setVisibility(View.GONE);
                }
            });
        }
    }

    public void loadTradeView() {
        cus_dtTrade.setEnabledDrag(false);
        cus_dtTrade.enableTradeMoneyDishpay(false);
        cus_dtTrade.enableHttpRecord(false);
        if (mShoppingCart != null && mShoppingCart.getDinnertableTradeInfo() != null) {
            cus_dtTrade.setModel(mShoppingCart.getDinnertableTradeInfo().getiDinnertableTrade());
        }
    }

    public boolean isRangeOfNumWaiterLayout(MotionEvent event) {
        if (event != null) {
            return DensityUtil.isRangeOfView(numWaiterLayout, event) || DensityUtil.isRangeOfView(llNumberAndWaiter, event);
        }
        return false;
    }

    public boolean isRangeOfBackView(MotionEvent event) {
        if (event != null) {
            return DensityUtil.isRangeOfView(rlBack, event);
        }
        return false;
    }

    public void setNumberWaiterGone() {
        if (numWaiterLayout.getVisibility() == View.VISIBLE && !isBuffetOpenTable()) {
                        numWaiterLayout.setVisibility(View.GONE);
            ivNumberAndWaiterTip.setVisibility(View.GONE);
        }
    }

    public boolean isBuffetOpenTable() {
        if (mShoppingCart.getOrder().getTrade().getBusinessType() == BusinessType.BUFFET
                && (mShoppingCart.getOrder().getTradeBuffetPeoples() == null || mShoppingCart.getOrder().getTradeBuffetPeoples().isEmpty())
                && !TradeType.hasUnionTable(mShoppingCart.getOrder().getTrade().getTradeType()))
            return true;
        else
            return false;
    }


    public void onActivityTouched(MotionEvent event) {
        try {
            if (numWaiterLayout != null && numWaiterLayout.getVisibility() == View.VISIBLE) {
                if (event != null && !isRangeOfNumWaiterLayout(event)) {
                                        numWaiterLayout.setVisibility(View.GONE);
                    ivNumberAndWaiterTip.setVisibility(View.GONE);
                }
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }


    private void updateNumberAndWaiter() {
                TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
        String waiterName = "";
        String customerCount = "";
        if (tradeVo != null && Utils.isNotEmpty(tradeVo.getTradeTableList())) {
            llNumberAndWaiter.setVisibility(View.VISIBLE);
            TradeTable tradeTable = tradeVo.getTradeTableList().get(0);
            customerCount = String.valueOf(tradeTable.getTablePeopleCount());

            waiterName = tradeTable.getWaiterName();
            if (!TextUtils.isEmpty(waiterName)) {
                waiterName = Utils.getDisplayName(waiterName);
            } else {
                waiterName = "";
            }

        } else {
            waiterName = Session.getAuthUser().getName();
            customerCount = String.valueOf(tradeVo.getTrade().getTradePeopleCount());
        }

        String count = TextUtils.isEmpty(customerCount) ? "" : (customerCount + (tableTotalCount == null ? "" : "/" + tableTotalCount));
        tvCustomerCount.setText(MainApplication.getInstance().getString(R.string.dinner_guests_hint, count));
        tvWaiterName.setText(String.format(getString(R.string.dinner_waiter_hint), waiterName));
    }

    @Override
    public void onResume() {
        super.onResume();
        registerEventBus();
        if (!isInit) {
            if (tradeInfoFragment != null) {
                tradeInfoFragment.updateOrderDishList(mShoppingCart.getShoppingCartDish(), mShoppingCart.getOrder());
            }
        } else {
            isInit = false;
        }
    }

    void initViewPager() {
        currentPagerIndex = 0;
        tradeInfoFragment = new DinnerDishTradeInfoFragment_();
        Bundle bundle = new Bundle();
        bundle.putInt(DinnerDishTradeInfoFragment.DISHSHOPCART_PAGE, DinnerDishTradeInfoFragment.PAGE_DISH);
        tradeInfoFragment.setArguments(bundle);
        tradeInfoFragment.setBusinessType(DinnerShoppingCart.getInstance().getOrder().getTrade().getBusinessType());
        tradeInfoFragment.registerListener(mChangePageListener);
        tradeInfoFragment.setCurrentPage(DinnerDishTradeInfoFragment.PAGE_DISH);
        replaceFragment(R.id.pager, tradeInfoFragment, "tradeInfoFragment");
        showOrderDishView();
    }

    public void showOrderDishView() {
        if (mShoppingCart.getOrder() != null) {
                        updateShopCartView(mShoppingCart.getShoppingCartDish(), mShoppingCart.getOrder());
        }
    }


    ShoppingCartListener mModifyShoppingCartListener = new ShoppingCartListener() {

        @Override
        public void addToShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, ShopcartItem mShopcartItem) {
            updateShopCartView(listOrderDishshopVo, mTradeVo, mShopcartItem, OperationStatus.addToShoppingCart);
        }

        public void updateDish(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
            updateShopCartView(listOrderDishshopVo, mTradeVo);
        }

        @Override
        public void removeShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo,
                                       IShopcartItemBase mShopcartItemBase) {
            updateShopCartView(listOrderDishshopVo, mTradeVo);

        }

        @Override
        public void addTempSetmealData(IShopcartItem mShopcartItem) {
        }

        @Override
        public void removeSetmealData() {
        }

        @Override
        public void removeSetmealChild(IShopcartItem OrderDishshopVo) {
            showPayButton();

            restClearShoppingCartBut();
        }

        @Override
        public void removeSetmealRemark(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo,
                                        ISetmealShopcartItem setmeal) {
            updateShopCartView(listOrderDishshopVo, mTradeVo);
        }


        @Override
        public void resetOrder(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
            if (mChangePageListener != null) {
                mChangePageListener.clearShoppingCart();
            }
            showOrderDishView();
        }

        @Override
        public void clearShoppingCart() {

            showPayButton();

                        CustomerManager.getInstance().setDinnerLoginCustomer(null);
                        CustomerManager.getInstance().clearCurrentCustomer();

            setClearBtnNormal();




        }

        @Override
        public void setCardNo(String cardNo) {

        }

        @Override
        public void orderDiscount(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
            updateShopCartView(listOrderDishshopVo, mTradeVo);
        }

        @Override
        public void removeDiscount(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo,
                                   IShopcartItem mShopcartItem) {
            updateShopCartView(listOrderDishshopVo, mTradeVo);
        }

        @Override
        public void setRemark(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
            updateShopCartView(listOrderDishshopVo, mTradeVo);
        }

        @Override
        public void removeRemark(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, IShopcartItem mShopcartItem) {
            updateShopCartView(listOrderDishshopVo, mTradeVo);
        }

        @Override
        public void batchPrivilege(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {

            updateShopCartView(listOrderDishshopVo, mTradeVo);
        }

        @Override
        public void exception(String message) {
            new CommonDialogFragmentBuilder(MainApplication.getInstance()).title(getResources().getString(R.string.invalidLogin))
                    .iconType(R.drawable.commonmodule_dialog_icon_warning)
                    .negativeText(R.string.reLogin)
                    .negativeLisnter(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            RouteIntent.startLogin(getActivity());
                        }
                    })
                    .build()
                    .show(getActivity().getSupportFragmentManager(), TAG);
        }

        ;

    };

    public void updateShopCartView(List<IShopcartItem> list, TradeVo tradeVo) {
        updateShopCartView(list, tradeVo, null, ShoppingCartListener.OperationStatus.None);
    }


    @UiThread
    public void updateShopCartView(List<IShopcartItem> list, TradeVo tradeVo, ShopcartItem shopcartItem, ShoppingCartListener.OperationStatus operationStatus) {
        try {
            UserActionEvent.start(UserActionEvent.DINNER_DISH_SHOPCART_DISPLAY);
            if (this.isAdded()) {
                if (tradeInfoFragment != null) {
                    tradeInfoFragment.updateOrderDishList(list, tradeVo, shopcartItem, operationStatus);
                }
                showPayButton();

                restClearShoppingCartBut();
                UserActionEvent.end(UserActionEvent.DINNER_DISH_SHOPCART_DISPLAY);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public void showPayButton() {
        Trade trade = mShoppingCart.getOrder().getTrade();
        if (mShoppingCart.isReturnCash()) {
                        rlSavePrint.setVisibility(View.GONE);
        } else {
            rlSavePrint.setVisibility(View.VISIBLE);
        }


        if (mShoppingCart.getSelectDishCount() > 0 || Utils.isNotEmpty(mShoppingCart.getOrder().getTradeBuffetPeoples())) {
            if (trade != null && mShoppingCart.getOrderAmount() != null) {
                String amount = MathDecimal.toDecimalFormatString(mShoppingCart.getOrderAmount());
                String price = ShopInfoCfg.getInstance().getCurrencySymbol() + amount;

                btnPrePay.setText(price);
                if (price.length() >= 9) {
                    btnPrePay.setTextSize(24);
                } else {
                    btnPrePay.setTextSize(28);
                }
            }
        } else {
            btnPrePay.setText(ShopInfoCfg.getInstance().getCurrencySymbol() + "0");
            btnPrePay.setTextSize(28);
        }


        List<IShopcartItem> tempList = mShoppingCart.getAllValidShopcartItem(mShoppingCart.getShoppingCartDish());
        if (tempList != null && tempList.size() != 0) {
                        rlSavePrint.setBackgroundResource(R.drawable.btn_green_selector);

        } else {
                        rlSavePrint.setBackgroundResource(R.drawable.btn_gray_disabled_shape);

        }

    }

    @Click(R.id.rl_back)
    protected void clickSaveback() {
        if (!ClickManager.getInstance().isClicked()) {
            if (isFromDesk) {
                DinnerShopCartAdapter.setDishCheckMode(false);
                getActivity().finish();
            } else if (isOrderDishMode) {
                                if (mShoppingCart.isReturnCash()) {
                    showQuiteRepayDialog();
                                    } else {
                    DinnerShopManager.getInstance().verifyDishInventory(getActivity(), mShoppingCart.getShoppingCartVo().getListOrderDishshopVo(), new Runnable() {
                        @Override
                        public void run() {
                            mChangePageListener.changePage(ChangePageListener.PAGE_TABLE_HOME, null);
                                                        if (mShoppingCart.getOrder().isUnionMainTrade()) {
                                mShoppingCart.removeAllUnionTradeVoPrivileges();
                            }
                            ;
                            final TradeVo tradeVo = mShoppingCart.createOrder();
                                                        ThreadUtils.runOnWorkThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }
                    });
                }
                MobclickAgentEvent.onEvent(UserActionCode.ZC020001);
            }
        }
    }

    private void showQuiteRepayDialog() {
        DialogUtil.showHintConfirmDialog(mFragmentManager,
                R.string.pause_repay,
                R.string.ok,
                R.string.cancel,
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                                                mChangePageListener.changePage(ChangePageListener.PAGE_TABLE_HOME, null);
                    }
                },
                null,
                "cancel_repay");
    }


    private void saveTrade(final TradeVo tradeVo, final boolean isPrintCustom) {
        if (tradeVo == null) {
            return;
        }

        DinnerTradeItemManager.getInstance().bindTradeId(tradeVo);


    }


    private void sendKitchen(final TradeVo tradeVo, final boolean isPrintCustom,
                             final boolean isPrintKitchen, final boolean isPrintLabel) {
        if (tradeVo == null) {
            return;
        }

        Log.i(TAG, String.format("tradeId=%s, isPrintCustom=%s, isPrintKitchen=%s, isPrintLabel=%s",
                tradeVo.getTrade().getId(), isPrintCustom, isPrintKitchen, isPrintLabel));
        DinnerTradeItemManager.getInstance().bindTradeId(tradeVo);

    }



    @Click(R.id.main)
    protected void clickMainView() {
        if (!ClickManager.getInstance().isClicked()) {
            if (mClearBtnflag == FLAG_PRESS) {
                setClearBtnNormal();

                restClearShoppingCartBut();
            }
        }
    }


    @Click(R.id.setMemo)
    public void gotoSetRemark() {
        MobclickAgentEvent.onEvent(getActivity(), MobclickAgentEvent.dinnerOrderDishOpenOperation);

        if (!ClickManager.getInstance().isClicked()) {
                        if (currentPagerIndex == 1) {
            }
            if (middleChangeListener != null) {
                middleChangeListener.closePage(null);
            }

        }
    }

    @Click(R.id.ll_number_and_waiter)
    public void clickLLNumberAndwaiter(View v) {
        MobclickAgentEvent.onEvent(UserActionCode.ZC020004);
        if (viewNumberAndWaiter == null)
            return;

        if (!ClickManager.getInstance().isClicked()) {
            if (numWaiterLayout.getVisibility() == View.VISIBLE && !isBuffetOpenTable()) {
                                numWaiterLayout.setVisibility(View.GONE);
                ivNumberAndWaiterTip.setVisibility(View.GONE);
            } else if (numWaiterLayout.getVisibility() == View.GONE) {
                                viewNumberAndWaiter.updateNumberAndWaiter();
                numWaiterLayout.setVisibility(View.VISIBLE);
                ivNumberAndWaiterTip.setVisibility(View.VISIBLE);
            }
        }
    }




    @Click(R.id.rl_save_print)
    protected void saveAndPrintClick(View v) {
        if (!ClickManager.getInstance().isClicked()) {
            MobclickAgentEvent.onEvent(UserActionCode.ZC020002);
            List<IShopcartItem> tempList = mShoppingCart.getAllValidShopcartItem(mShoppingCart.getShoppingCartDish());
            if (tempList == null || tempList.size() == 0) {
                ToastUtil.showShortToast(getResources().getString(R.string.haveDishNeedPrint));
                return;
            }
            DinnerShopManager.getInstance().verifyDishInventory(getActivity(), mShoppingCart.getShoppingCartVo().getListOrderDishshopVo(), new Runnable() {
                @Override
                public void run() {
                    UserActionEvent.start(UserActionEvent.DINNER_DISHES_TRANSFER_KITCHEN);
                    mChangePageListener.changePage(ChangePageListener.PAGE_TABLE_HOME, null);
                                        if (mShoppingCart.getOrder().isUnionMainTrade()) {
                        mShoppingCart.removeAllUnionTradeVoPrivileges();
                    }
                                        if (mShoppingCart.getOrder().isUnionSubTrade()) {
                        for (IShopcartItem shopcartItem : mShoppingCart.getShoppingCartVo().getListIShopcatItem()) {
                            if (shopcartItem.getShopcartItemType() == ShopcartItemType.SUBBATCH) {
                                ShopcartItemUtils.splitBatchItem(shopcartItem);
                            }
                        }
                    }
                    final TradeVo tradeVo = mShoppingCart.createOrder();
                    ThreadUtils.runOnWorkThread(new Runnable() {
                        @Override
                        public void run() {



                        }
                    });                 }
            });
        }
    }

    @Click(R.id.btn_pre_pay)
    protected void prePayClick(View v) {

        toPay();

    }


    private void toPay() {
        MobclickAgentEvent.onEvent(UserActionCode.ZC020003);
                TradeVo tradeVo = mShoppingCart.getOrder();
        if (tradeVo.isUnionSubTrade()) {
            ToastUtil.showLongToast(MainApplication.getInstance().getResources().getString(R.string.dinner_union_sub_cannot_pay));
            return;
        }
        if (!mShoppingCart.hasValidItems() && Utils.isEmpty(tradeVo.getTradeBuffetPeoples())) {
            ToastUtil.showLongToast(MainApplication.getInstance().getResources().getString(R.string.dinner_no_item_hint));
            return;
        }

        DinnerShopManager.getInstance().verifyDishInventory(getActivity(), mShoppingCart.getShoppingCartVo().getListOrderDishshopVo(), new Runnable() {
            @Override
            public void run() {
                if (middleChangeListener != null) {
                    middleChangeListener.closePage(null);
                }
                DinnerShoppingCart.getInstance().onlyMath();


                DinnerCashManager.removeInValidAuthLog(mShoppingCart.getOrder());
            }
        });
    }

    public void onEventMainThread(ActionDinnerChangePage action) {
        DishDataItem item = action.getItem();
        if (item != null && item.getBase() != null) {
            if (item.getType() == ItemType.COMBO) {                if (mChangePageListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, item.getItem().getUuid());
                    bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST);
                    bundle.putBoolean(Constant.NONEEDCHECK, false);
                    mChangePageListener.changePage(ChangePageListener.DISHCOMBO, bundle);
                }
            } else {
                if (mChangePageListener != null) {
                    Bundle bundle = new Bundle();
                                        if (item.getType() == ItemType.CHILD || item.getType() == ItemType.CHILD_MEMO) {
                                                bundle.putString(Constant.EXTRA_SHOPCART_ITEM_PARENT_UUID, item.getItem().getUuid());
                    }
                                        bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, item.getBase().getUuid());
                    bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST);
                    bundle.putBoolean(Constant.NONEEDCHECK, false);
                    mChangePageListener.changePage(ChangePageListener.DISHPROPERTY, bundle);
                }
            }
        }
    }

    public void mClearShoppingCart() {
                mShoppingCart.clearShoppingCart();

        mChangePageListener.clearShoppingCart();

    }


    private void setClearBtnNormal() {
        mClearBtnflag = FLAG_NORMAL;
    }

    private void setClearBtnPress() {
        mClearBtnflag = FLAG_PRESS;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case IntentNo.ORDER_PAY:
                String payStatus = data.getExtras().getString(PAYSTATUS);
                if (SUCCESSFULL.equals(payStatus)) {
                    mClearShoppingCart();
                }
                break;

            default:
                break;
        }

    }




    private void restClearShoppingCartBut() {
        if (mClearBtnflag == FLAG_PRESS) {
            setClearBtnNormal();
        }
    }

    public void onEventMainThread(EventEditModle event) {
        if (event != null && event.isEditModle) {
            viewBottomShadow.setVisibility(View.VISIBLE);
            rlBack.setEnabled(false);
            rlSavePrint.setEnabled(false);
            btnPrePay.setEnabled(false);
            btn_memeo.setEnabled(false);
        } else {
            viewBottomShadow.setVisibility(View.GONE);
            rlBack.setEnabled(true);
            rlSavePrint.setEnabled(true);
            btnPrePay.setEnabled(true);
            btn_memeo.setEnabled(true);
        }

        if (middleChangeListener != null) {
            middleChangeListener.onEditModeChange(event.isEditModle);
        }

    }

    public void onEventMainThread(EventChoice eventChoice) {
        checkNumberTv.setText(eventChoice.getCount());
        allCheckCb.setChecked(eventChoice.isCheckAll());
    }


    private void setDeskInfo() {
        DinnertableTradeInfo info = mShoppingCart.getDinnertableTradeInfo();
        if (info != null && info.getTradeType() != TradeType.UNOIN_TABLE_MAIN) {
                        tv_billNo.setText(info.getSerialNumber());

            String tableName = info.getTableName();
            if (!TextUtils.isEmpty(tableName)) {
                switch (tableName.length()) {
                    case 5:
                        tv_deskNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                        break;
                    case 6:
                        tv_deskNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        break;
                    default:
                        tv_deskNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                        break;
                }
                tv_deskNo.setText(tableName);
            }
                        if (info.getNumberOfMeals() > 0) {
                tv_peoplecount.setText("" + info.getNumberOfMeals());
            }

                        String zoneName = info.getTableZoneName();
            if (zoneName != null) {
                                if (zoneName.length() > 5) {
                    zoneName = zoneName.substring(0, 5);
                }
                tv_floor.setText(zoneName);
            }
        } else {
            tv_deskNo.setVisibility(View.INVISIBLE);
            tv_floor.setVisibility(View.INVISIBLE);
        }
        try {
            Long time = mShoppingCart.getOrder().getTrade().getServerCreateTime();
            if (time != null) {
                tv_delaytime.setText(SpendTimeFormater.format((int) ((System.currentTimeMillis() - time) / 1000 / 60)));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            tv_delaytime.setText("0min");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mShoppingCart.unRegisterListenerByTag(ShoppingCartListerTag.ORDER_DISH_LEFT);
        super.onDestroy();
    }


    public void showCheckMode(boolean show, PrintOperationOpType opType) {
        if (show) {
            orderButtonPanelFirst.setVisibility(View.GONE);
            dishOperateCheckbar.setVisibility(View.VISIBLE);
            cashView.setVisibility(View.GONE);
            dishOperateBottombarLL.setVisibility(View.VISIBLE);
            llNumberAndWaiter.setVisibility(View.GONE);
                        if (opType == PrintOperationOpType.WAKE_UP) {
                btnCancel.setVisibility(View.VISIBLE);
                operateNameTv.setText(getString(R.string.dinner_orderdish_dish_prepare));
            } else if (opType == PrintOperationOpType.RISE_DISH) {
                btnCancel.setVisibility(View.VISIBLE);
                operateNameTv.setText(getString(R.string.dinner_orderdish_dish_make));
            } else if (opType == REMIND_DISH) {
                btnCancel.setVisibility(View.VISIBLE);
                operateNameTv.setText(getString(R.string.dinner_orderdish_dish_urge));
            } else if (opType == BATCH_OPERATION) {
                btnCancel.setVisibility(View.VISIBLE);
                operateNameTv.setText(getString(R.string.dinner_orderdish_batch_operation));
            } else if (opType == PrintOperationOpType.WAKE_UP_CANCEL) {
                btnCancel.setVisibility(View.VISIBLE);
                operateNameTv.setText(getString(R.string.dinner_orderdish_dish_cancel_prepare));
            } else if (opType == PrintOperationOpType.RISE_DISH_CANCEL) {
                btnCancel.setVisibility(View.VISIBLE);
                operateNameTv.setText(getString(R.string.dinner_orderdish_cancel_dish_make));
            }

                        DinnerShopCartAdapter adapter = tradeInfoFragment.getSelectedDishAdapter();
            allCheckCb.setChecked(adapter.isCheckedAll());
            checkNumberTv.setText(getString(R.string.dinner_orderdish_dishcheck_number,
                    String.valueOf(adapter.getCheckedNumber())));

        } else {
            orderButtonPanelFirst.setVisibility(View.VISIBLE);
            dishOperateCheckbar.setVisibility(View.GONE);
            cashView.setVisibility(View.VISIBLE);
            dishOperateBottombarLL.setVisibility(View.GONE);
            llNumberAndWaiter.setVisibility(View.VISIBLE);
        }
    }


    public boolean isDishCheckMode() {
        return dishOperateCheckbar != null && dishOperateCheckbar.getVisibility() == View.VISIBLE;
    }


    @Click({R.id.cancel_btn, R.id.done_btn})
    protected void dishOperateBottomBarClick(View v) {
        final DinnerShopCartAdapter adapter = tradeInfoFragment.getSelectedDishAdapter();

        final PrintOperationOpType opType = adapter.getOpType();
        if (v.getId() == R.id.cancel_btn) {            if (opType == null) {
                return;
            }
                        switch (opType) {
                case WAKE_UP:
                case RISE_DISH:
                case REMIND_DISH:
                case BATCH_OPERATION:
                case WAKE_UP_CANCEL:
                case RISE_DISH_CANCEL:

                    break;
                default:
                    break;
            }

            showCheckMode(false, null);
            adapter.showDishCheckMode(false, null);


        } else if (v.getId() == R.id.done_btn) {            if (opType == null) {
                return;
            }
            final TradeVo[] tradeVo = {DinnerShoppingCart.getInstance().getOrder()};
            if (tradeVo[0] == null || tradeVo[0].getTrade() == null) {
                return;
            }
                        final List<DishDataItem> selectedItems = adapter.getCheckItems();
            if (Utils.isEmpty(selectedItems)) {
                ToastUtil.showShortToast(R.string.please_select_dish);
                return;
            }

            switch (opType) {
                case WAKE_UP:
                case RISE_DISH:
                case WAKE_UP_CANCEL:
                case RISE_DISH_CANCEL:
                case REMIND_DISH:
                    DinnerShopManager.getInstance().verifyDishInventory(getActivity(), mShoppingCart.getShoppingCartVo().getListOrderDishshopVo(), new Runnable() {
                        @Override
                        public void run() {
                            DinnerTradeItemManager.dishOperationfun(tradeVo[0], opType, selectedItems, mFragmentManager, adapter, new DishOperationListener());
                        }
                    });
                    break;
                case BATCH_OPERATION:
                    if (selectedItems.size() < 2) {
                        ToastUtil.showShortToast(R.string.please_select_dish_more_than_1);
                        return;
                    }
                    startBatchOperation();
                    adapter.doSelectedItems(DinnerUtils.getSingleAndComboUuids(selectedItems));
                    EventBus.getDefault().post(new ActionBatchOperationItems(selectedItems, false));
                    break;
                default:
                    break;
            }
            showCheckMode(false, null);
            adapter.showDishCheckMode(false, null);



        }
        showOrderDishView();
    }

    private final class DishOperationListener implements DishOptListener {
        @Override
        public void onSuccess(PrintOperationOpType type, List<DishDataItem> dataItems) {

        }

        @Override
        public void onFail(PrintOperationOpType type, List<DishDataItem> dataItems) {
            if (tradeInfoFragment.removeSelectedTradeItemOperations(dataItems, type)) {
                tradeInfoFragment.getSelectedDishAdapter().notifyDataSetChanged();
            }
        }
    }

    private OnClickListener clickLisenter = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.allcheck_cb) {                tradeInfoFragment.getSelectedDishAdapter().checkAllDish(allCheckCb.isChecked());
                checkNumberTv.setText(getString(R.string.dinner_orderdish_dishcheck_number,
                        String.valueOf(tradeInfoFragment.getSelectedDishAdapter().getCheckedNumber())));
            }
        }
    };




    private void startBatchOperation() {
        if (tradeInfoFragment != null && tradeInfoFragment.isVisible()) {
            tradeInfoFragment.isBatchOperating = true;
        }
    }


    public void finishBatchOperation() {
        if (tradeInfoFragment != null && tradeInfoFragment.isVisible()) {
            tradeInfoFragment.isBatchOperating = false;
        }
    }


    public boolean isBatchOperating() {
        if (tradeInfoFragment != null && tradeInfoFragment.isVisible()) {
            return tradeInfoFragment.isBatchOperating;
        } else {
            return false;
        }
    }



    public abstract void refreshShopCart();

    protected abstract BusinessType getBusinessType();

    protected abstract boolean isDinner();


    protected boolean isReturnCash() {
        return DinnerShoppingCart.getInstance().isReturnCash();
    }

}
