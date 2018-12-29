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
abstract public class DishLeftFragment extends MobclickAgentFragment implements BasicFragment.OnActivityTouchListener/*, DishOperatePopWindow.OperateChangeListener*/ {

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
    protected LinearLayout rlSavePrint;// 下单及打印

    @ViewById(R.id.btn_pre_pay)
    protected Button btnPrePay;// 预结算

    @ViewById(R.id.order_button_panel_first)
    protected RelativeLayout orderButtonPanelFirst;

    @ViewById(R.id.rl_back)
    protected RelativeLayout rlBack;

    // 设置备注
    @ViewById(R.id.setMemo)
    protected ImageButton btn_memeo;

    @ViewById(R.id.cus_dtv_trade)
    protected DinnertableTradeView cus_dtTrade;

    @ViewById(R.id.pager)
    protected FrameLayout pager;

    @ViewById(R.id.fl_num_waiter)
    protected FrameLayout numWaiterLayout;

    // 桌号
    @ViewById(R.id.deskNo)
    protected TextView tv_deskNo;

    // 楼层
    @ViewById(R.id.floor)
    protected TextView tv_floor;

    // 人数
    @ViewById(R.id.peoplecount)
    protected TextView tv_peoplecount;

    // 单号流水号
    @ViewById(R.id.dinner_bill_no)
    protected TextView tv_billNo;

    // 改单据当前状态的持续时间
    @ViewById(R.id.delaytime)
    protected TextView tv_delaytime;

    // check顶部栏
    @ViewById(R.id.dish_operate_checkbar)
    protected LinearLayout dishOperateCheckbar;

    // 选择模式底部栏
    @ViewById(R.id.dish_operate_bottombar_ll)
    protected LinearLayout dishOperateBottombarLL;

    // 菜品操作下方的取消按钮
    @ViewById(R.id.cancel_btn)
    protected Button btnCancel;

    // tab栏
//	@ViewById(R.id.dinner_left_tablayout)
//	FrameLayout dinnerLeftTablayout;

    // 全选按钮
    @ViewById(R.id.allcheck_cb)
    public CheckBox allCheckCb;

    // 选择数目展示
    @ViewById(R.id.check_number_tv)
    public TextView checkNumberTv;

    // 菜品操作标题
    @ViewById(R.id.operate_name_tv)
    protected TextView operateNameTv;

    //展示人数和服务员的蓝色背景条
    @ViewById(R.id.ll_number_and_waiter)
    protected LinearLayout llNumberAndWaiter;

    //展示修改人数和服务员时的三角tip
    @ViewById(R.id.iv_number_and_waiter_tip)
    protected ImageView ivNumberAndWaiterTip;

    @ViewById(R.id.tv_customer_count)
    protected TextView tvCustomerCount;

    @ViewById(R.id.tv_waiter_name)
    protected TextView tvWaiterName;

    //@ViewById(R.id.view_number_and_waiter)
    protected NumberAndWaiterView viewNumberAndWaiter;

    // 扫码对话框
    private QuantityEditPopupWindow mPopupWindow;

    private int currentPagerIndex = 0;

    private final int FLAG_NORMAL = 0;

    private final int FLAG_PRESS = 1;

    private int mClearBtnflag = 0;

    protected DinnerShoppingCart mShoppingCart;

    //public List<OrderMemo> orderMemoList = new ArrayList<OrderMemo>();

    private DinnerDishTradeInfoFragment tradeInfoFragment;
    private DinnerDishSequenceFragment dinnerDishSequenceFragment;

//	private TableInfoOrderInfoFragment_ tableInfoFragment;

    public TradePrivilege privilege;

    public String SUCCESSFULL = "successfully";

    public String PAYSTATUS = "payStatus";

    private ChangePageListener mChangePageListener;

    private FragmentManager mFragmentManager;

    private boolean isOrderDishMode = true;// true为点菜，false为预结账（只有在点菜时点返回才下单，需要用此属性区分）

    // 是否是从桌台进来
    private boolean isFromDesk = false;

    //是否快速开台
    private boolean isQuickOpenTable = false;

    //DishOperatePopWindow dishOperatePopWindow;// 菜品操作弹框
    private Integer tableTotalCount = null;

    protected LoadingFinish mLoadingFinish;
    private DinnerDishMiddleFragment.IChangePageListener middleChangeListener;

    private boolean isInit = true;

    public abstract NumberAndWaiterView getViewNumberAndWaiter();

    /**
     * 是否都是单点的菜品
     *
     * @return
     */
    protected abstract boolean isOnlySingleAddDish(List<String> uuids);

    /**
     * 是否打印标签（目前只有正餐支持）
     *
     * @return
     */
    protected abstract boolean isPrintLabel();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getActivity().getSupportFragmentManager();
        setRetainInstance(true);

    }

    /**
     * 设置新加的菜品被选中
     *
     * @param item
     */
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

    /**
     * 清除选中状态
     */
    public void clearAllSelected() {
        tradeInfoFragment.getSelectedDishAdapter().clearAllSelected();
    }

    /**
     * 设置默认进入点餐 还是结算界面
     *
     * @Title: setIsFromTable
     * @Return void 返回类型
     */
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

//		mShoppingCart.setDinnerOrderType(DeliveryType.HERE);
        setDeskInfo();

        if (mShoppingCart.getOrder().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN
                || mShoppingCart.getOrder().getTrade().getTradeType() == TradeType.UNOIN_TABLE_SUB) {
            btn_memeo.setVisibility(View.GONE);
        } else
            btn_memeo.setVisibility(View.VISIBLE);

        // add zhubo 2016-4-26全选按钮响应
        allCheckCb.setOnClickListener(clickLisenter);
        loadTradeView();
//		initNumberAndWaiter();//update by dzb 20170327 偶现activity not attach
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
                    //查询桌台可容纳的总人数
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
            //viewNumberAndWaiter.show();
            numWaiterLayout.setVisibility(View.VISIBLE);
            ivNumberAndWaiterTip.setVisibility(View.VISIBLE);
        } else {
            //viewNumberAndWaiter.hide();
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
            //viewNumberAndWaiter.hide();
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
                    //viewNumberAndWaiter.hide();
                    numWaiterLayout.setVisibility(View.GONE);
                    ivNumberAndWaiterTip.setVisibility(View.GONE);
                }
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * 更新人数和服务员信息
     */
    private void updateNumberAndWaiter() {
//		if(!this.isVisible()){
//			return;
//		}
        //查询当前订单人数和服务员
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
//		showPayButton();
        if (mShoppingCart.getOrder() != null) {
            // 展示购车菜品列表
            updateShopCartView(mShoppingCart.getShoppingCartDish(), mShoppingCart.getOrder());
        }
    }

    /**
     * 监听购物车操作事件
     */
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

        /*
         * 挂单时回调
         */
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

            // 注销登录会员
            CustomerManager.getInstance().setDinnerLoginCustomer(null);
            // 清除当前呼入电话客户信息
            CustomerManager.getInstance().clearCurrentCustomer();

            setClearBtnNormal();


            // 将选择过得整单备注清除
			/*for (OrderMemo memo : orderMemoList) {
				memo.setTag(null);
			}*/

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

    /**
     * 购物车有变更时更新界面
     *
     * @Title: updateView
     * @Description:
     * @Param @param list
     * @Param @param tradeVo
     * @Return void 返回类型
     */
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
            // 反结账和必胜客的单据，隐藏传送后厨按钮
            rlSavePrint.setVisibility(View.GONE);
        } else {
            rlSavePrint.setVisibility(View.VISIBLE);
        }


        if (mShoppingCart.getSelectDishCount() > 0 || Utils.isNotEmpty(mShoppingCart.getOrder().getTradeBuffetPeoples())) {
            if (trade != null && mShoppingCart.getOrderAmount() != null) {
                String amount = MathDecimal.toDecimalFormatString(mShoppingCart.getOrderAmount());
                String price = ShopInfoCfg.getInstance().getCurrencySymbol() + amount;
                //modify by zhubo 2016-8-9按钮显示订单实际金额
//				String price = getString(R.string.dinner_money_symbol) + mShoppingCart.getOrder().getTrade().getTradeAmount().toString();

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

        /**
         * 根据购物车中是否有菜品设置按钮的不同状态
         */
        List<IShopcartItem> tempList = mShoppingCart.getAllValidShopcartItem(mShoppingCart.getShoppingCartDish());
        if (tempList != null && tempList.size() != 0) {
            //设置传送后厨按钮背景色设置为可点击背景色
            rlSavePrint.setBackgroundResource(R.drawable.btn_green_selector);

        } else {
            //设置传送后厨按钮背景色设置为不可点击背景色
            rlSavePrint.setBackgroundResource(R.drawable.btn_gray_disabled_shape);

        }

    }

    @Click(R.id.rl_back)
    protected void clickSaveback() {
        if (!ClickManager.getInstance().isClicked()) {
//			CLog.i(CLog.FS_TAG_KEY, "改单保存");
            if (isFromDesk) {
                DinnerShopCartAdapter.setDishCheckMode(false);
                getActivity().finish();
            } else if (isOrderDishMode) {
                // 反结账
                if (mShoppingCart.isReturnCash()) {
                    showQuiteRepayDialog();
                    // 正常点菜
                } else {
                    DinnerShopManager.getInstance().verifyDishInventory(getActivity(), mShoppingCart.getShoppingCartVo().getListOrderDishshopVo(), new Runnable() {
                        @Override
                        public void run() {
                            mChangePageListener.changePage(ChangePageListener.PAGE_TABLE_HOME, null);
                            //联台主单改单不保存优惠
                            if (mShoppingCart.getOrder().isUnionMainTrade()) {
                                mShoppingCart.removeAllUnionTradeVoPrivileges();
                            }
                            ;
                            final TradeVo tradeVo = mShoppingCart.createOrder();
                            //modify v8.1 通用线程工具替换
                            ThreadUtils.runOnWorkThread(new Runnable() {
                                @Override
                                public void run() {
                                    /*boolean isNeedPrintCustom = DinnerPrintUtil.isNeedPrintCustom(tradeVo);
                                    if (tradeVo.isChanged() || isNeedPrintCustom) {
                                        saveTrade(tradeVo, isNeedPrintCustom);
                                    }*/
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
                        // 返回桌台
                        mChangePageListener.changePage(ChangePageListener.PAGE_TABLE_HOME, null);
                    }
                },
                null,
                "cancel_repay");
    }

    /**
     * 保存订单（出客看单）
     *
     * @param tradeVo
     * @param isPrintCustom
     */
    private void saveTrade(final TradeVo tradeVo, final boolean isPrintCustom) {
        if (tradeVo == null) {
            return;
        }

        DinnerTradeItemManager.getInstance().bindTradeId(tradeVo);

        /*DinnerModifyPrintBean dinnerModifyPrintBean = DinnerPrintDataBuildUtil.
                buildModifyPrintBean(tradeVo, isPrintCustom, false, false);
        modifyTradeAndPrint(tradeVo, dinnerModifyPrintBean, false);*/
    }

    /**
     * 传送后厨
     *
     * @param tradeVo        订单
     * @param isPrintCustom  是否打印客看单
     * @param isPrintKitchen 是否打印厨打单
     */
    private void sendKitchen(final TradeVo tradeVo, final boolean isPrintCustom,
                             final boolean isPrintKitchen, final boolean isPrintLabel) {
        if (tradeVo == null) {
            return;
        }

        Log.i(TAG, String.format("tradeId=%s, isPrintCustom=%s, isPrintKitchen=%s, isPrintLabel=%s",
                tradeVo.getTrade().getId(), isPrintCustom, isPrintKitchen, isPrintLabel));
        DinnerTradeItemManager.getInstance().bindTradeId(tradeVo);
        /*DinnerModifyPrintBean dinnerModifyPrintBean = DinnerPrintDataBuildUtil.
                buildModifyPrintBean(tradeVo, isPrintCustom, isPrintKitchen, isPrintLabel);
        modifyTradeAndPrint(tradeVo, dinnerModifyPrintBean, true);*/
    }

    /*
     private void modifyTradeAndPrint(final TradeVo tradeVo, final DinnerModifyPrintBean dinnerModifyPrintBean, final boolean isKitchen) {

        AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_CHANGE_ORDER, tradeVo.getTrade().getId(), tradeVo.getTrade().getUuid(),
                tradeVo.getTrade().getServerUpdateTime());
        AsyncModifyResponseListener oldListener = new AsyncModifyResponseListener(UserActionEvent.DINNER_DISHES_TRANSFER_KITCHEN) {
            @Override
            public void onResponse(ResponseObject<TradeResp> response) {
                super.onResponse(response);
                UserActionEvent.end(UserActionEvent.DINNER_DISHES_TRANSFER_KITCHEN);

                if (isKitchen && ResponseObject.isOk(response)) {
                    boolean isExitLogin = SpHelper.getDefault().getBoolean(SettingConstant.DINNER_EXIT_KITCHEN_SWITCH);
                    if (isExitLogin) {
                        AppDialog.showExitLoginRemindDialog();
                    }
                }
            }
        };
        oldListener.isDinner = isDinner();

        oldListener.printBean = dinnerModifyPrintBean;

        AsyncBatchModifyResponseListener unoinMainListener = new AsyncBatchModifyResponseListener(UserActionEvent.DINNER_DISHES_TRANSFER_KITCHEN) {
            @Override
            public void onResponse(ResponseObject<TradeResp> response) {
                super.onResponse(response);
                if (isKitchen && ResponseObject.isOk(response)) {
                    boolean isExitLogin = SpHelper.getDefault().getBoolean(SettingConstant.DINNER_EXIT_KITCHEN_SWITCH);
                    if (isExitLogin) {
                        AppDialog.showExitLoginRemindDialog();
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
                ToastUtil.showShortToast(error.getMessage());
            }
        };
        unoinMainListener.printBean = dinnerModifyPrintBean;

        TradeOperates op = OperatesFactory.create(TradeOperates.class);
        if (isDinner()) {
            if (mShoppingCart.getOrder().isUnionMainTrade()) {
                DinnerUnionManager.modifyUnionMainTrade(tradeVo, mShoppingCart.getShoppingCartDish(), unoinMainListener, true, op);
            } else if (mShoppingCart.getOrder().isUnionSubTrade()) {
                DinnerUnionManager.modifyUnionSubTrade(tradeVo, mShoppingCart.getShoppingCartDish(), mShoppingCart.getMainTradeInfo(), oldListener, true, op);
            } else {
                op.modifyDinner(tradeVo, oldListener, true);
            }
        } else {

            if (mShoppingCart.getOrder().isUnionMainTrade()) {
                BuffetUnionManager.modifyUnionMainTrade(tradeVo, mShoppingCart.getShoppingCartDish(), unoinMainListener, true, op);
            } else if (mShoppingCart.getOrder().isUnionSubTrade()) {
                BuffetUnionManager.modifyUnionSubTrade(getActivity(), tradeVo, mShoppingCart.getShoppingCartDish(), mShoppingCart.getMainTradeInfo(), oldListener, true, op);
            } else {
                op.modifyBuffet(tradeVo, true, oldListener, true);
            }
        }

		*//*BusinessType businessType = getBusinessType();
		switch (businessType) {
			case DINNER:

		}*//*

    }*/

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
            // 如果当前在开台信息页则先切换到商品信息页
            if (currentPagerIndex == 1) {
//				goodsInfoOnClick();
            }
            if (middleChangeListener != null) {
                middleChangeListener.closePage(null);
            }
            /*if (dishOperatePopWindow == null) {
                Log.i("zhubo", "信息页宽度：" + getView().getWidth());
                OrderDishMainActivity activity = (OrderDishMainActivity) getActivity();
                boolean isAllowBatchOperation = false;
                //西餐不支持批量操作
                if (!DinnerUtils.isWestStyle() && isDinner()
                        && (mShoppingCart.getCurrentTradeInfo() != null && mShoppingCart.getCurrentTradeInfo().getTradeType() != TradeType.UNOIN_TABLE_MAIN)) {
                    isAllowBatchOperation = true;
                }
                dishOperatePopWindow = new DishOperatePopWindow(activity, this, btn_memeo, getView().getWidth(), isAllowBatchOperation, isDinner());
                dishOperatePopWindow.isReturnCash = isReturnCash();
                dishOperatePopWindow.setChangePageListener(mChangePageListener);
                dishOperatePopWindow.setAdapter(tradeInfoFragment.getSelectedDishAdapter());
                dishOperatePopWindow.show();
            } else {
                if (dishOperatePopWindow.isShowing()) {
                    dishOperatePopWindow.hide();
                } else {
                    dishOperatePopWindow.isReturnCash = isReturnCash();
                    dishOperatePopWindow.show();
                }
            }*/
        }
    }

    @Click(R.id.ll_number_and_waiter)
    public void clickLLNumberAndwaiter(View v) {
        MobclickAgentEvent.onEvent(UserActionCode.ZC020004);
        if (viewNumberAndWaiter == null)
            return;

        if (!ClickManager.getInstance().isClicked()) {
            if (numWaiterLayout.getVisibility() == View.VISIBLE && !isBuffetOpenTable()) {
                //viewNumberAndWaiter.hide();
                numWaiterLayout.setVisibility(View.GONE);
                ivNumberAndWaiterTip.setVisibility(View.GONE);
            } else if (numWaiterLayout.getVisibility() == View.GONE) {
                //viewNumberAndWaiter.show();
                viewNumberAndWaiter.updateNumberAndWaiter();
                numWaiterLayout.setVisibility(View.VISIBLE);
                ivNumberAndWaiterTip.setVisibility(View.VISIBLE);
            }
        }
    }

    /*@Click(R.id.image_customer)
    protected void customerOnClick() {

    }*/


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
                    //联台主单改单不保存优惠
                    if (mShoppingCart.getOrder().isUnionMainTrade()) {
                        mShoppingCart.removeAllUnionTradeVoPrivileges();
                    }
                    //联台子单传送后厨时，先将未出单的主单菜拆菜
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
                            /*boolean isNeedPrintCustom = DinnerPrintUtil.isNeedPrintCustom(tradeVo);
                            boolean isNeedPrintKitchen = DinnerPrintUtil.isNeedPrintKitchen(tradeVo);
                            boolean isNeedPrintLabel = DinnerPrintUtil.isNeedPrintLabel(tradeVo);
                            boolean hasKitchenAddDish = DinnerPrintUtil.hasKitchenAddDish(tradeVo);*/

                            // 打印客看单和厨打加菜时，需要调用改单接口分别进行客看单状态修改和厨打状态修改
                            /*if (tradeVo.isChanged() || isNeedPrintCustom || hasKitchenAddDish) {
                                sendKitchen(tradeVo, isNeedPrintCustom, isNeedPrintKitchen, isPrintLabel() && isNeedPrintLabel);
                            } else {
                                UserActionEvent.end(UserActionEvent.DINNER_DISHES_TRANSFER_KITCHEN);
                            }*/
                        }
                    }); //modify v8.1 通用线程工具替换
                }
            });
        }
    }

    @Click(R.id.btn_pre_pay)
    protected void prePayClick(View v) {
//		if (!ClickManager.getInstance().isClicked(R.id.btn_pre_pay)) {
//			mChangePageListener.changePage(ChangePageListener.SETTLEMENT, null);
//			goPrePayMode();

//			boolean isOk = VerifyPermissionUtils.alertVerifyPermissInputDialog(getActivity(),
//					DinnerApplication.PERMISSION_DINNER_CASH, AuthType.TYPE_CHECK_OUT, AuthType.TYPE_CHECK_OUT.getDesc(),
//					new VerifyPermissionsDialogFragment.PermissionVerify() {
//						@Override
//						public void verify(String permission, boolean success) {
//							if (success) {
//								toPay();
//							}
//						}
//					}, null);
//			if (isOk) {
        toPay();
//			}
//		}

    }

    /**
     * 跳转到支付界面
     *
     * @Title: toPay
     * @Return void 返回类型
     */
    private void toPay() {
        MobclickAgentEvent.onEvent(UserActionCode.ZC020003);
        // 没有可结算品项不能跳转
        TradeVo tradeVo = mShoppingCart.getOrder();
        if (tradeVo.isUnionSubTrade()) {
            ToastUtil.showLongToast(MainApplication.getInstance().getResources().getString(R.string.dinner_union_sub_cannot_pay));
            return;
        }
        if (!mShoppingCart.hasValidItems() && Utils.isEmpty(tradeVo.getTradeBuffetPeoples())) {
            ToastUtil.showLongToast(MainApplication.getInstance().getResources().getString(R.string.dinner_no_item_hint));
            return;
        }

//		if (tradeVo.getTrade() != null && tradeVo.getTrade().getTradeAmount().compareTo(BigDecimal.ZERO) == -1) {
//			ToastUtil
//					.showLongToast(MainApplication.getInstance().getResources().getString(R.string.dinner_privilege_over));
//			return;
//		}
        DinnerShopManager.getInstance().verifyDishInventory(getActivity(), mShoppingCart.getShoppingCartVo().getListOrderDishshopVo(), new Runnable() {
            @Override
            public void run() {
                if (middleChangeListener != null) {
                    middleChangeListener.closePage(null);
                }
//		EventBus.getDefault().post(new ActionDinnerPrilivige(ActionDinnerPrilivige.DinnerPriviligeType.PRIVILIGE_ITEMS));
                DinnerShoppingCart.getInstance().onlyMath();

//		PayManager.gotoDinnerPayUI(this.getActivity(), null,null,false, false);
                /*Intent intent = new Intent(getActivity(), DinnerPayActivity_.class);
                getActivity().startActivity(intent);*/
                DinnerCashManager.removeInValidAuthLog(mShoppingCart.getOrder());
            }
        });
    }

    public void onEventMainThread(ActionDinnerChangePage action) {
        DishDataItem item = action.getItem();
        if (item != null && item.getBase() != null) {
            if (item.getType() == ItemType.COMBO) {// 套餐
                if (mChangePageListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, item.getItem().getUuid());
                    bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST);
                    bundle.putBoolean(Constant.NONEEDCHECK, false);
                    mChangePageListener.changePage(ChangePageListener.DISHCOMBO, bundle);
                }
            } else {
                if (mChangePageListener != null) {
                    Bundle bundle = new Bundle();
                    // 子菜或子菜属性
                    if (item.getType() == ItemType.CHILD || item.getType() == ItemType.CHILD_MEMO) {
                        // 套餐外壳uuid
                        bundle.putString(Constant.EXTRA_SHOPCART_ITEM_PARENT_UUID, item.getItem().getUuid());
                    }
                    // 子菜uuid
                    bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, item.getBase().getUuid());
                    bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST);
                    bundle.putBoolean(Constant.NONEEDCHECK, false);
                    mChangePageListener.changePage(ChangePageListener.DISHPROPERTY, bundle);
                }
            }
        }
    }

    public void mClearShoppingCart() {
        // 清除购物车
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

    /*@Click(R.id.clearShoppingCart)
    protected void clearShoppingCartInfo() {
        if (!ClickManager.getInstance().isClicked()) {
            // 如果购物车中无信息，则清除按钮不可用
            if (mShoppingCart.getOrder() == null) {
                return;
            }
            if (mClearBtnflag == FLAG_NORMAL) {
                setClearBtnPress();
            } else if (mClearBtnflag == FLAG_PRESS) {
                mClearShoppingCart();
            }
        }
    }*/

    /**
     * @Title: restClearShoppingCartBut
     * @Description: 取消清除购物车按钮选中状态
     * @Param
     * @Return void 返回类型
     */
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

    /**
     * 设置桌台信息
     *
     * @Title: setDeskInfo
     * @Description:
     * @Param
     * @Return void 返回类型
     */
    private void setDeskInfo() {
        DinnertableTradeInfo info = mShoppingCart.getDinnertableTradeInfo();
        if (info != null && info.getTradeType() != TradeType.UNOIN_TABLE_MAIN) {
            // 订单业务号
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
            // 就餐人数
            if (info.getNumberOfMeals() > 0) {
                tv_peoplecount.setText("" + info.getNumberOfMeals());
            }

            // 楼层名称
            String zoneName = info.getTableZoneName();
            if (zoneName != null) {
                // 最多显示5个字
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
//		AuthLogManager.getInstance().clear();
//		dismissQuantityEditPopupWindow();
        mShoppingCart.unRegisterListenerByTag(ShoppingCartListerTag.ORDER_DISH_LEFT);
        super.onDestroy();
    }

    /**
     * @param show
     * @Date 2016年4月25日
     * @Description: 是否显示选择模式
     * @Return void
     */
    public void showCheckMode(boolean show, PrintOperationOpType opType) {
        if (show) {
            orderButtonPanelFirst.setVisibility(View.GONE);
//			dinnerLeftTablayout.setVisibility(View.GONE);
            dishOperateCheckbar.setVisibility(View.VISIBLE);
            cashView.setVisibility(View.GONE);
            dishOperateBottombarLL.setVisibility(View.VISIBLE);
            llNumberAndWaiter.setVisibility(View.GONE);//不展示人数和服务员显示条

            // 等叫时，不需要取消按钮
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

            // 初始化选择数量，是否全选
            DinnerShopCartAdapter adapter = tradeInfoFragment.getSelectedDishAdapter();
            allCheckCb.setChecked(adapter.isCheckedAll());
            checkNumberTv.setText(getString(R.string.dinner_orderdish_dishcheck_number,
                    String.valueOf(adapter.getCheckedNumber())));

        } else {
            orderButtonPanelFirst.setVisibility(View.VISIBLE);
//			dinnerLeftTablayout.setVisibility(View.VISIBLE);
            dishOperateCheckbar.setVisibility(View.GONE);
            cashView.setVisibility(View.VISIBLE);
            dishOperateBottombarLL.setVisibility(View.GONE);
            llNumberAndWaiter.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 是否是起菜、催菜选择界面
     *
     * @return
     */
    public boolean isDishCheckMode() {
        return dishOperateCheckbar != null && dishOperateCheckbar.getVisibility() == View.VISIBLE;
    }

    /**
     * @param v
     * @Date 2016年4月26日
     * @Description: 菜品check模式，取消、完成按钮
     * @Return void
     */
    @Click({R.id.cancel_btn, R.id.done_btn})
    protected void dishOperateBottomBarClick(View v) {
        final DinnerShopCartAdapter adapter = tradeInfoFragment.getSelectedDishAdapter();

        final PrintOperationOpType opType = adapter.getOpType();
        if (v.getId() == R.id.cancel_btn) {// 取消
            if (opType == null) {
                return;
            }
            // 催菜和起菜点击取消按钮时，移除已选择条目中添加在内存中的起菜和催菜
            switch (opType) {
                case WAKE_UP:
                case RISE_DISH:
                case REMIND_DISH:
                case BATCH_OPERATION:
                case WAKE_UP_CANCEL:
                case RISE_DISH_CANCEL:
					/*
					List<DishDataItem> selectedItems = adapter.getCheckItems();
					if (tradeInfoFragment.removeSelectedTradeItemOperations(selectedItems, opType)) {
						adapter.notifyDataSetChanged();
					}
					*/
                    break;
                default:
                    break;
            }

            showCheckMode(false, null);
            adapter.showDishCheckMode(false, null);
            // 隐藏遮罩层
            /*OrderDishMainActivity activity = (OrderDishMainActivity) getActivity();
            if (activity != null) {
                activity.showShadow(false);
            }*/

        } else if (v.getId() == R.id.done_btn) {// 完成
            if (opType == null) {
                return;
            }
            final TradeVo[] tradeVo = {DinnerShoppingCart.getInstance().getOrder()};
            if (tradeVo[0] == null || tradeVo[0].getTrade() == null) {
                return;
            }
            //tradeInfoFragment.addTradeItemOperations();
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

            // 隐藏遮罩层
            /*OrderDishMainActivity activity = (OrderDishMainActivity) getActivity();
            if (activity != null && opType != BATCH_OPERATION) {//批量操作不需要隐藏遮罩层
                activity.showShadow(false);
            }*/

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
            if (v.getId() == R.id.allcheck_cb) {// 全选按钮
                tradeInfoFragment.getSelectedDishAdapter().checkAllDish(allCheckCb.isChecked());
                checkNumberTv.setText(getString(R.string.dinner_orderdish_dishcheck_number,
                        String.valueOf(tradeInfoFragment.getSelectedDishAdapter().getCheckedNumber())));
            }
        }
    };

    /*@Override
    public void changeDishCheckMode(PrintOperationOpType type) {
        if (type == DISH_SEQUENCE) {
            dinnerDishSequenceFragment = DinnerDishSequenceFragment.show(getFragmentManager(), R.id.rootDishOrderView,
                    mShoppingCart, new DinnerDishSequenceFragment.DishSequenceCallback() {
                        @Override
                        public void onDishSequence(List<DinnerDishSequenceFragment.DishSequenceBean> dishSequenceBeans) {
                            for (DinnerDishSequenceFragment.DishSequenceBean dishSequenceBean : dishSequenceBeans) {
                                if (dishSequenceBean.shopcartItem != null) {
                                    IShopcartItemBase shopcartItemBase = dishSequenceBean.shopcartItem;
                                    TradeItemExtraDinner tradeItemExtraDinner = shopcartItemBase.getTradeItemExtraDinner();
                                    if (tradeItemExtraDinner == null) {
                                        tradeItemExtraDinner = new TradeItemExtraDinner();
                                        tradeItemExtraDinner.validateCreate();
                                        tradeItemExtraDinner.setClientCreateTime(System.currentTimeMillis());
                                        tradeItemExtraDinner.setTradeItemId(shopcartItemBase.getId());
                                        tradeItemExtraDinner.setTradeItemUuid(shopcartItemBase.getUuid());
                                    } else {
                                        tradeItemExtraDinner.validateUpdate();
                                        tradeItemExtraDinner.setStatusFlag(StatusFlag.VALID);
                                        tradeItemExtraDinner.setClientUpdateTime(System.currentTimeMillis());
                                    }

                                    tradeItemExtraDinner.setServingOrder(dishSequenceBean.sort);
                                    shopcartItemBase.setTradeItemExtraDinner(tradeItemExtraDinner);
                                    mShoppingCart.updateDinnerDish(shopcartItemBase, false);
                                }
                            }
                        }
                    });
        } else {
            DinnerShopCartAdapter adapter = tradeInfoFragment.getSelectedDishAdapter();
            adapter.setDishCheckMode(true);
            adapter.setOpType(type);
            tradeInfoFragment.updateOrderDishList(mShoppingCart.getShoppingCartDish(), mShoppingCart.getOrder(), true, true, true);
//		adapter.initialDishCheckStatus();
//		adapter.notifyDataSetChanged();
            showCheckMode(true, type);
        }
    }*/

    /**
     * 设置批量操作属性，进行批量操作时，不允许点击其他菜品
     */
    private void startBatchOperation() {
        if (tradeInfoFragment != null && tradeInfoFragment.isVisible()) {
            tradeInfoFragment.isBatchOperating = true;
        }
    }

    /**
     * 结束批量操作
     */
    public void finishBatchOperation() {
        if (tradeInfoFragment != null && tradeInfoFragment.isVisible()) {
            tradeInfoFragment.isBatchOperating = false;
        }
    }

    /**
     * 是否正在进行批量操作
     *
     * @return
     */
    public boolean isBatchOperating() {
        if (tradeInfoFragment != null && tradeInfoFragment.isVisible()) {
            return tradeInfoFragment.isBatchOperating;
        } else {
            return false;
        }
    }


    /**
     * 调用更新购物车
     * 当要使用改单时，调用该方法来刷新数据
     */
    public abstract void refreshShopCart();

    protected abstract BusinessType getBusinessType();

    protected abstract boolean isDinner();

    /**
     * 是否为反结账订单
     *
     * @return
     */
    protected boolean isReturnCash() {
        return DinnerShoppingCart.getInstance().isReturnCash();
    }

}
