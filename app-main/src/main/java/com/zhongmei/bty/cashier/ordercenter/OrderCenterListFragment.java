package com.zhongmei.bty.cashier.ordercenter;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zhongmei.beauty.ordercenter.BeautyOrderCenterListPresenter;
import com.zhongmei.bty.basemodule.auth.application.FastFoodApplication;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerShopBiz;
import com.zhongmei.bty.basemodule.database.utils.DbQueryConstant;
import com.zhongmei.bty.basemodule.notifycenter.enums.NotificationType;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.bty.basemodule.trade.entity.TradeDepositPayRelation;
import com.zhongmei.bty.basemodule.trade.message.BatchDeliveryFee;
import com.zhongmei.bty.cashier.ordercenter.adapter.OrderCenterChildTabAdapter;
import com.zhongmei.bty.cashier.ordercenter.adapter.OrderCenterListAdapter;
import com.zhongmei.bty.cashier.ordercenter.bean.DispatchFailOrder;
import com.zhongmei.bty.cashier.ordercenter.bean.FilterCondition;
import com.zhongmei.bty.cashier.ordercenter.bean.FilterData;
import com.zhongmei.bty.cashier.ordercenter.dialog.VerificationDialog;
import com.zhongmei.bty.cashier.ordercenter.dialog.VerificationDialog_;
import com.zhongmei.bty.cashier.ordercenter.presenter.BuffetOrderCenterListPresenter;
import com.zhongmei.bty.cashier.ordercenter.presenter.DinnerOrderCenterListPresenter;
import com.zhongmei.bty.cashier.ordercenter.presenter.GroupOrderCenterListPresenter;
import com.zhongmei.bty.cashier.ordercenter.presenter.IOrderCenterListPresenter;
import com.zhongmei.bty.cashier.ordercenter.presenter.SnackOrderCenterListPresenter;
import com.zhongmei.bty.cashier.ordercenter.view.DeliveryFeeDialogFragment;
import com.zhongmei.bty.cashier.ordercenter.view.DeliveryPlatformPopupWindow;
import com.zhongmei.bty.cashier.ordercenter.view.DispatchFailOrderListFragment;
import com.zhongmei.bty.cashier.ordercenter.view.FilterFragment;
import com.zhongmei.bty.cashier.ordercenter.view.FilterFragment_;
import com.zhongmei.bty.cashier.ordercenter.view.IOrderCenterListView;
import com.zhongmei.bty.cashier.ordercenter.view.ItemDivider;
import com.zhongmei.bty.cashier.ordercenter.view.LoadingSmallView;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterChildTabView;
import com.zhongmei.bty.cashier.ordercenter.view.SearchTypeFragment;
import com.zhongmei.bty.cashier.ordercenter.view.SearchTypeFragment_;
import com.zhongmei.bty.cashier.ordercenter.view.SelectSenderDialogFragment;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.DeliveryOrder;
import com.zhongmei.bty.commonmodule.database.enums.DeliveryOrderStatus;
import com.zhongmei.bty.commonmodule.database.enums.DeliveryOrderSubStatus;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.constants.OCConstant;
import com.zhongmei.bty.data.db.common.OrderNotify;
import com.zhongmei.bty.data.operates.CallDishNotifyDal;
import com.zhongmei.bty.db.entity.VerifyKoubeiOrder;
import com.zhongmei.bty.entity.enums.NotifyOrderType;
import com.zhongmei.bty.manager.RecyclerGridLayoutManager;
import com.zhongmei.bty.snack.event.EventSelectOrder;
import com.zhongmei.bty.splash.login.UserDialog;
import com.zhongmei.bty.splash.login.adapter.UserGridAdapter;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeReturnInfo;
import com.zhongmei.yunfu.db.enums.DeliveryStatus;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PaySource;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.ui.view.recycler.RecyclerLinearLayoutManager;
import com.zhongmei.yunfu.util.ResourceUtils;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.ValueEnum;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 订单中心订单列表
 */
@EFragment(R.layout.frg_order_center_list)
public class OrderCenterListFragment extends BasicFragment implements IOrderCenterListView, UserGridAdapter.OnUserSelectedListener {
    private static final String TAG = OrderCenterListFragment.class.getSimpleName();

    @ViewById(R.id.layout_titlebar)
    protected LinearLayout layout_titlebar;

    //菜单按钮可呼出侧边栏
    @ViewById(R.id.cashier_title_bar_menu_btn)
    protected ImageView mMenu;

    @ViewById(R.id.bind_delivery_user_and_send)
    protected RadioGroup rgProcessDeliveryUserAndSend;

    @ViewById(R.id.process_display_group)
    protected RadioGroup rgProcessDisplayGroup;

    @ViewById(R.id.order_refresh)
    protected RelativeLayout mRefresh;

    @ViewById(R.id.view_notify_tip)
    protected TextView vNotifyCenterTip;

    @ViewById(R.id.view_notify_tip_other)
    protected TextView viewNotifyCenterOtherTip;

    @ViewById(R.id.tv_list_title)
    protected TextView tv_listTitle;

    @ViewById(R.id.process_delivery_user)
    RadioButton mProcessDeliveryUser;

    @ViewById(R.id.process_send)
    RadioButton mProcessSend;

    @ViewById(R.id.process_btn)
    RadioButton mProcessBtn;

    @ViewById(R.id.sales_note_btn)
    RadioButton mSalesNoteBtn;

    @ViewById(R.id.order_center_child_tab_view)
    RecyclerView mChildTabView;

    @ViewById(R.id.order_search_btn)
    ImageView mSearchBtn;

    @ViewById(R.id.order_filter_btn)
    ImageView mFilterBtn;

    @ViewById(R.id.order_center_search_layout)
    LinearLayout mSearchLayout;

    @ViewById(R.id.order_center_search_type)
    TextView mSearchType;

    @ViewById(R.id.order_center_search_content)
    EditText mSearchContent;

    @Bean
    OrderCenterChildTabAdapter mChildTabAdapter;

    @ViewById(R.id.order_center_list_view)
    RecyclerView mListView;

    @Bean
    OrderCenterListAdapter mCenterListAdapter;

    @ViewById(R.id.square_account_layout)
    RelativeLayout mSquareAccountLayout;
    /**
     * 用于点击了清账按钮后,显示额外的筛选布局
     */
    @ViewById(R.id.square_account_filter_layout)
    LinearLayout mSquareAccountFilterLayout;

    @ViewById(R.id.order_center_list_child_tab_layout)
    ViewGroup mChildTabLayout;
    @ViewById(R.id.tv_square_account_filter_tip)
    TextView tvSquareAccountFilterTip;

    @ViewById(R.id.square_account)
    TextView mSquareAccount;

    @ViewById(R.id.rl_bind_delivery_user)
    RelativeLayout rlBindDeliveryUser;

    @ViewById(R.id.tv_bind_delivery_user_and_send)
    TextView tvBindDeliveryUserAndSend;

    @ViewById(R.id.order_center_delivery_user_select_all_chk)
    CheckBox chkDeliveryUserSelectAll;

    @ViewById(R.id.order_center_delivery_user_select_all_tip_tv)
    TextView tvDeliveryUserSelectAllTip;

    @ViewById(R.id.order_center_delivery_user_bind_confirm_tv)
    TextView tvDeliveryUserBindConfirm;

    @ViewById(R.id.order_center_delivery_user_bind_cancel_tv)
    TextView tvDeliveryUserBindCancel;

    @ViewById(R.id.waiter_group)
    RadioGroup waiterGroup;

    @ViewById(R.id.order_center_select_check_box)
    CheckBox mSelectCheckBox;

    @ViewById(R.id.order_center_select_tip)
    TextView mSelectTip;

    @ViewById(R.id.order_center_tips)
    TextView mTips;

    @ViewById(R.id.order_center_cancel)
    TextView mCancel;

    @ViewById(R.id.confirm_square_account)
    TextView mConfirm;

    @ViewById(R.id.take_meal_notice_button)
    ToggleButton mTakeMealNotice;

    @ViewById(R.id.tak_meal_notice_tip)
    TextView mTakeMealNoticeTip;

    @ViewById(R.id.filter_tip_layout)
    RelativeLayout mFilterTipLayout;

    @ViewById(R.id.order_center_filter_tip)
    TextView mFilterTips;

    @ViewById(R.id.order_center_filter_close)
    ImageView mFilterClose;
    @ViewById(R.id.order_center_list_empty_view)
    View emptyView;
    @ViewById(R.id.select_waiter)
    RadioButton selectWaiter;
    @ViewById(R.id.all_waiter)
    RadioButton allWaiter;
    @ViewById(R.id.loading_view)
    LoadingSmallView mLoadingSmallView;

    // v8.12.0 口碑
    @ViewById(R.id.tv_order_center_koubei_verification)
    TextView mKoubeiVerfication;

    private int mChildTab = -1;

    private int mSelectedProcessDelivery = 1;

    /**
     * 是否已经处理了外部传入的intenttab
     */
    private boolean hasProcessIntentTab = false;

    /**
     * 子标签
     */
    private List<Pair<String, Integer>> childTabs;
    private UserDialog userDialog;
    List<User> allUserList;
    User user;
    UserDialog.OnDialogCloseListener mCloseListener = new UserDialog.OnDialogCloseListener() {
        @Override
        public void unSelected(Boolean unSelected) {
            if (selectWaiter != null && selectWaiter.isChecked()) {
                if (user == null) {
                    if (allWaiter != null) {
                        allWaiter.setChecked(true);
                    }
                }
            }
        }
    };

    /**
     * 筛选条件
     */
    private List<android.util.Pair<String, ValueEnum>> filterConditions;

    private IOrderCenterListPresenter mPresenter;

    private int mFromType = OCConstant.FromType.FROM_TYPE_SNACK;

    private int mTargetTab = DbQueryConstant.UNPROCESSED_ALL;

    private boolean mSquareModEnable = false;

    private NotificationType mNotificationType;
    private List<ValueEnum> filterCond;

    private static final Uri URI_TRADE = DBHelperManager.getUri(Trade.class);

    private static final Uri URI_TRADE_ITEM = DBHelperManager.getUri(TradeItem.class);

    private static final Uri URI_TRADE_EXTRA = DBHelperManager.getUri(TradeExtra.class);

    private static final Uri URI_ORDER_NOTIFY = DBHelperManager.getUri(OrderNotify.class);

    public static final Uri URI_TRADE_RETURN_INFO = DBHelperManager.getUri(TradeReturnInfo.class);

    public static final Uri URI_TRADE_DEPOSIT = DBHelperManager.getUri(TradeDeposit.class);

    public static final Uri URI_PAYMENTITEM = DBHelperManager.getUri(PaymentItem.class);
    public static final Uri URI_TRADEDEPOSITPAYRELATION = DBHelperManager.getUri(TradeDepositPayRelation.class);
    public static final Uri URI_DELIVERY_ORDER = DBHelperManager.getUri(DeliveryOrder.class);
    public static final Uri URI_VERIFY_KOUBEI_ORDER = DBHelperManager.getUri(VerifyKoubeiOrder.class);
    private OrderCenterChangeObserver mDataObserver;
    private OrderNotifyChangeObserver mNotifyObserver;
    /**
     * 是否处于搜索
     */
    private boolean isInSearchMode = false;
    /**
     * 是否是在清账模式
     */
    private boolean isInSquareAccountMode = false;

    //是否处于派单/下发模式
    private boolean isInDeliveryMode = false;

    public static String KEY_TAKE_MEAL_NOTICE = "key_take_meal_notice";

    /**
     * 第一类的最后点击项
     */
    private int firstClickTab = DbQueryConstant.UNPROCESSED_ALL;
    /**
     * 第二类的最后点击项
     */
    private int secondClickTab = DbQueryConstant.SALES_ALL;
    /**
     * 搜索范围选中的item位置
     */
    private int searchPosition;
    private DeliveryPlatformPopupWindow mDeliveryPlatformPopupWindow;

    /**
     * 数据改变监听器
     */
    private class OrderCenterChangeObserver implements DatabaseHelper.DataChangeObserver {

        @Override
        public void onChange(Collection<Uri> uris) {
            if (mPresenter == null) {
                return;
            }
            if (uris.contains(URI_TRADE)
                    || uris.contains(URI_TRADE_ITEM)
                    || uris.contains(URI_TRADE_EXTRA)
                    || uris.contains(URI_TRADE_RETURN_INFO)
                    || uris.contains(URI_TRADE_DEPOSIT)
                    || uris.contains(URI_PAYMENTITEM)
                    || uris.contains(URI_TRADEDEPOSITPAYRELATION)
                    || uris.contains(URI_DELIVERY_ORDER)
                    || uris.contains(URI_VERIFY_KOUBEI_ORDER)) {
                if (isInSearchMode) {
                    String keyword = mSearchContent.getText().toString();
                    if (TextUtils.isEmpty(keyword)) {
                        mPresenter.loadData(getCurrentAllTab(), getFilterCondition(), null);
                    } else {
                        mPresenter.search(getCurrentParentTab(), searchPosition, keyword, getFilterCondition(), null);
                    }
                } else {
                    mPresenter.loadData(mChildTab, getFilterCondition(), null);
                }
                countOrder();
            }
        }

    }

    private class OrderNotifyChangeObserver implements DatabaseHelper.DataChangeObserver {

        @Override
        public void onChange(Collection<Uri> uris) {
            if (uris.contains(URI_ORDER_NOTIFY)) {
                queryOrderNotify();
            }
        }

    }

    private void queryOrderNotify() {
        AsyncTask<Void, Void, List<OrderNotify>> asyncTask = new AsyncTask<Void, Void, List<OrderNotify>>() {

            @Override
            protected List<OrderNotify> doInBackground(Void... params) {
                List<OrderNotify> orderNotifyList = new ArrayList<OrderNotify>();
                try {
                    CallDishNotifyDal callDishNotifyDal =
                            OperatesFactory.create(CallDishNotifyDal.class);
                    orderNotifyList = callDishNotifyDal.queryOrderNotify(NotifyOrderType.TRADE);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                return orderNotifyList;
            }

            protected void onPostExecute(List<OrderNotify> result) {
                if (mCenterListAdapter != null) {
                    mCenterListAdapter.setOrderNotifyList(result);
                }
            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    /**
     * @param fromType   来源：-1为快餐，1为正餐 2为自助餐
     * @param currentTab 当前Tab栏位
     */
    public static OrderCenterListFragment newInstance(int fromType, int currentTab) {
        return newInstance(fromType, currentTab, false);
    }

    public static OrderCenterListFragment newInstance(int fromType, int currentTab, boolean isSquareAccountMod) {
        return newInstance(fromType, currentTab, isSquareAccountMod, null);
    }

    /**
     * 添加配送取消的通知类型
     */
    public static OrderCenterListFragment newInstance(int fromType, int currentTab, boolean isSquareAccountMod, NotificationType notificationType) {
        return newInstance(fromType, currentTab, isSquareAccountMod, notificationType, null);
    }

    public static OrderCenterListFragment newInstance(int fromType, int currentTab, boolean isSquareAccountMod, NotificationType notificationType, List<ValueEnum> filterCond) {
        Bundle args = new Bundle();
        args.putInt(Constant.EXTRA_FROM_TYPE, fromType);
        args.putInt(Constant.EXTRA_CURRENT_TAB, currentTab);
        args.putBoolean(Constant.EXTRA_SQUARE_MOD, isSquareAccountMod);
        if (notificationType != null) {
            //args.putSerializable(NotifyCenterUtil.EXTRA_NOTIFY_TYPE, notificationType);
        }
        args.putSerializable("filterCond", (Serializable) filterCond);
        OrderCenterListFragment orderCenterListFragment = new OrderCenterListFragment_();
        orderCenterListFragment.setArguments(args);
        return orderCenterListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mFromType = arguments.getInt(Constant.EXTRA_FROM_TYPE);
            mTargetTab = arguments.getInt(Constant.EXTRA_CURRENT_TAB);
            mSquareModEnable = arguments.getBoolean(Constant.EXTRA_SQUARE_MOD);
            //mNotificationType = (NotificationType) arguments.getSerializable(NotifyCenterUtil.EXTRA_NOTIFY_TYPE);
            filterCond = (List<ValueEnum>) arguments.getSerializable("filterCond");
        }
        initPresenter();
        registerDataObserver();
        registerNotifyObserver();
    }

    private void initTitleBarBg() {
        switch (mFromType) {
            case OCConstant.FromType.FROM_TYPE_SNACK://快餐
            case OCConstant.FromType.FROM_TYPE_DINNER://正餐
            case OCConstant.FromType.FROM_TYPE_BUFFET://自助餐
            case OCConstant.FromType.FROM_TYPE_GROUP://团餐
            case OCConstant.FromType.FROM_TYPE_RETAIL://零售
                layout_titlebar.setBackgroundColor(getResources().getColor(R.color.snack_title_bg));
                mProcessBtn.setBackgroundResource(R.drawable.radiobutton_left);
                mSalesNoteBtn.setBackgroundResource(R.drawable.radiobutton_right);
                mProcessBtn.setTextColor(getResources().getColorStateList(R.color.handover_textcolor_selector));
                mSalesNoteBtn.setTextColor(getResources().getColorStateList(R.color.handover_textcolor_selector));
                rgProcessDisplayGroup.setVisibility(View.VISIBLE);
                tv_listTitle.setVisibility(View.GONE);
                break;
            case OCConstant.FromType.FROM_TYPE_BEAUTY://美业
                layout_titlebar.setBackgroundColor(getResources().getColor(R.color.beauty_bg_white));
                mProcessBtn.setBackgroundResource(R.drawable.beauty_ordercenter_tab_left_selector);
                mSalesNoteBtn.setBackgroundResource(R.drawable.beauty_ordercenter_tab_right_selector);
                mSalesNoteBtn.setTextColor(getResources().getColorStateList(R.color.beauty_titlebar_tab_selector));
                mProcessBtn.setTextColor(getResources().getColorStateList(R.color.beauty_titlebar_tab_selector));
                rgProcessDisplayGroup.setVisibility(View.GONE);
                tv_listTitle.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initPresenter() {
        switch (mFromType) {
            case OCConstant.FromType.FROM_TYPE_SNACK://快餐
                mPresenter = new SnackOrderCenterListPresenter(this);
                break;
            case OCConstant.FromType.FROM_TYPE_DINNER://正餐
                mPresenter = new DinnerOrderCenterListPresenter(this);
                break;
            case OCConstant.FromType.FROM_TYPE_BUFFET://自助餐
                mPresenter = new BuffetOrderCenterListPresenter(this);
                break;
            case OCConstant.FromType.FROM_TYPE_GROUP://团餐
                mPresenter = new GroupOrderCenterListPresenter(this);
                break;
            case OCConstant.FromType.FROM_TYPE_RETAIL://零售
                //mPresenter = new RetailOrderCenterListPresenter(this);
                break;
            case OCConstant.FromType.FROM_TYPE_BEAUTY://美业
                mPresenter = new BeautyOrderCenterListPresenter(this);
                break;
        }
    }

    /**
     * 判断当前是否为快餐模块
     *
     * @return 为true表示快餐模块，false表示正餐模块
     */
    private boolean isFromSnack() {
        return mFromType == OCConstant.FromType.FROM_TYPE_SNACK || mFromType == OCConstant.FromType.FROM_TYPE_RETAIL;
    }

    @AfterViews
    protected void initView() {
        if (mPresenter.showBackButton()) {
            //initBackBtn();
        } else {
            //mBackPage.setVisibility(View.GONE);
        }

        if (mPresenter.showMenuButton()) {
            mMenu.setVisibility(View.VISIBLE);
        } else {
            mMenu.setVisibility(View.GONE);
        }

        if (mPresenter.showOrderRefreshButton()) {
            mRefresh.setVisibility(View.VISIBLE);
        } else {
            mRefresh.setVisibility(View.GONE);
        }

        //刷新通知中心角标
        allWaiter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    allWaiterSelected();
                }
            }
        });

        //defaultCheck
        mProcessBtn.setChecked(true);
        fetchChildTab();

        //设置过滤条件
        if (filterCond != null) {
            if (filterConditions == null) {
                filterConditions = new ArrayList<>();
            }

            List<FilterData> filterData = mPresenter.getFilterData();
            for (FilterData data : filterData) {
                List<android.util.Pair<String, ValueEnum>> childList = data.getChildList();
                for (android.util.Pair<String, ValueEnum> pair : childList) {
                    if (filterCond.contains(pair.second)) {
                        filterConditions.add(pair);
                    }
                }
            }

            filterOrderConditions(filterConditions);
        }

        mChildTabAdapter.setCheck(mProcessBtn.isChecked());
        mChildTabView.setAdapter(mChildTabAdapter);
        mChildTabAdapter.setOnItemClickListener(new OrderCenterChildTabAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mCenterListAdapter.clear();
                Pair<String, Integer> item = (Pair<String, Integer>) mChildTabAdapter.getItem(position);
                mChildTab = item.second;
                saveClickStateToRestore();
                mCenterListAdapter.setChildTab(mChildTab);
                isSalesPaid();
                refreshBindDeliveryUserLayout();
                mPresenter.loadData(mChildTab, getFilterCondition(), null);
            }
        });
        final LinearLayoutManager manager = new RecyclerLinearLayoutManager(getActivity());
        mListView.setLayoutManager(manager);
        mListView.setAdapter(mCenterListAdapter);
        mListView.addItemDecoration(new ItemDivider(getActivity(), R.drawable.divide_line));
        mCenterListAdapter.setOnItemClickListener(new OrderCenterListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Object object = mCenterListAdapter.getItem(position);
                if (object != null) {
                    TradePaymentVo tradePaymentVo = (TradePaymentVo) mCenterListAdapter.getItem(position);
                    setSelectCheckBoxState();
                    fireEventBus(tradePaymentVo.getTradeVo());
                }
            }

            @Override
            public void onCheckBoxClick() {
                setSelectCheckBoxState();
            }

            @Override
            public void selectBoxChange(List<TradeVo> selectedOrders) {
                if (mTips != null && mTips.getVisibility() == View.VISIBLE && mPresenter != null) {
                    mTips.setText(mPresenter.calculateOrderAmount(selectedOrders));
                }
            }
        });
        if (isFromSnack()) {
            mCenterListAdapter.setFromDinner(false);
        } else {
            mCenterListAdapter.setFromDinner(true);
        }
        mCenterListAdapter.setPresenter(mPresenter);
        mListView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState
            ) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mCenterListAdapter.getItemCount()) {
                    TradePaymentVo tradePaymentVo = (TradePaymentVo) mCenterListAdapter.getItem(mCenterListAdapter.getItemCount() - 1);
                    if (tradePaymentVo != null) {//add v8.3
                        TradeVo tradeVo = tradePaymentVo.getTradeVo();
                        if (isInSearchMode) {
                            String keyword = mSearchContent.getText().toString();
                            if (keyword.isEmpty()) {
                                mPresenter.loadData(getCurrentAllTab(), getFilterCondition(), tradeVo.getTrade());
                            } else {
                                mPresenter.search(getCurrentParentTab(), searchPosition, keyword, getFilterCondition(), tradeVo.getTrade());
                            }
                        } else {
                            mPresenter.loadData(mChildTab, getFilterCondition(), tradeVo.getTrade());
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = manager.findLastVisibleItemPosition();
            }
        });
        mSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mSearchLayout.getVisibility() == View.VISIBLE) {
                    String keyword = mSearchContent.getText().toString();
                    if (keyword.isEmpty()) {
                        if (isInSearchMode) {
                            mPresenter.loadData(getCurrentAllTab(), getFilterCondition(), null);
                        } else {
                            mPresenter.loadData(mChildTab, getFilterCondition(), null);
                        }
                    } else {
                        mPresenter.search(getCurrentParentTab(), searchPosition, keyword, getFilterCondition(), null);
                    }
                }
            }
        });
        //从通知中心 配送跳转到订单中心 直接进入待配送 下发
        if (mNotificationType != null && NotificationType.DELIVERY_WAITING.equals(mNotificationType)) {
            bindDeliveryUserButtonClick();
        }
        initTitleBarBg();
    }

    /**
     * 保存点击状态为了后续恢复
     */
    private void saveClickStateToRestore() {
        if (mProcessBtn.isChecked()) {
            firstClickTab = mChildTab;
        } else {
            secondClickTab = mChildTab;
        }
    }

    private void setSelectCheckBoxState() {
        if (!isInSquareAccountMode && !isInDeliveryMode) {
            return;
        }
        List<TradeVo> selectedOrders = mCenterListAdapter.getSelectedOrders();
        if (selectedOrders.size() == mCenterListAdapter.getItemCount()) {
            mSelectCheckBox.setChecked(true);
            chkDeliveryUserSelectAll.setChecked(true);
        } else {
            mSelectCheckBox.setChecked(false);
            chkDeliveryUserSelectAll.setChecked(false);
        }
    }

    private void countOrder() {
        if (mProcessBtn.isChecked()) {
            mPresenter.countOrder();
        }
    }

    private void fireEventBus(TradeVo tradeVo) {
        EventBus.getDefault().post(new EventSelectOrder(mChildTab, tradeVo.getTrade().getUuid(), isInSquareAccountMode, isInDeliveryMode, mFromType));
    }

    private void fireEmptyBus() {
        EventBus.getDefault().post(new EventSelectOrder(mChildTab, "", isInSquareAccountMode, isInDeliveryMode, mFromType));
    }

    /**
     * 判断来源
     */
    private boolean processIntentData() {
        if (!hasProcessIntentTab) {//只有第一次进来才没有值
            List<Pair<String, Integer>> processTab = new ArrayList<Pair<String, Integer>>();
            mPresenter.addProcessTab(processTab);
            List<Pair<String, Integer>> saleTab = new ArrayList<Pair<String, Integer>>();
            mPresenter.addSaleNoteTab(saleTab);
            int index = 0;
            for (int i = 0; i < processTab.size(); i++) {
                Pair<String, Integer> pair = processTab.get(i);
                if (pair.second == mTargetTab) {
                    index = i;
                    mProcessBtn.setChecked(true);
                    mCenterListAdapter.setCheckTab(DbQueryConstant.UNPROCESSED);
                    childTabs = processTab;
                    firstClickTab = mTargetTab;
                    break;
                }
            }
            for (int i = 0; i < saleTab.size(); i++) {
                Pair<String, Integer> pair = saleTab.get(i);
                if (pair.second == mTargetTab) {
                    index = i;
                    mSalesNoteBtn.setChecked(true);
                    mCenterListAdapter.setCheckTab(DbQueryConstant.SALES);
                    childTabs = saleTab;
                    secondClickTab = mTargetTab;
                    break;
                }
            }
            mChildTabAdapter.setCheck(mProcessBtn.isChecked());
            hasProcessIntentTab = true;
            //上面方法有清除查询条件的操作，所以这边重新组装查询条件
            if (mNotificationType != null && mNotificationType == NotificationType.DELIVERY_CANCEL) {
                if (filterConditions == null) {
                    filterConditions = new ArrayList<>();
                }
                // 非清帐模式
                if (!isInSquareAccountMode) {
                    filterConditions.add(new android.util.Pair<String, ValueEnum>(getString(R.string.order_center_detail_delivery_cancel), DeliveryOrderStatus.DELIVERY_CANCEL));
                    filterConditions.add(new android.util.Pair<String, ValueEnum>(getString(R.string.order_center_list_delivery_man_cancel), DeliveryOrderSubStatus.DELIVERY_MAN_CANCEL));
                    filterConditions.add(new android.util.Pair<String, ValueEnum>(getString(R.string.order_center_list_delivery_error_recreate_allow), DeliveryOrderSubStatus.DELIVERY_ERROR_RECREATE_ALLOW));
                    filterConditions.add(new android.util.Pair<String, ValueEnum>(getString(R.string.order_center_list_delivery_error_recreate_forbid), DeliveryOrderSubStatus.DELIVERY_ERROR_RECREATE_FORBID));
                }
                setFilterTipsByConditions();
            }
            if (!Utils.isEmpty(childTabs)) {
                setChildTabAndFillListData(index);
                return true;
            } else {
                Log.e(TAG, "processIntentData: can't find target tab");
                return false;
            }
        } else {
            return false;
        }
    }

    private void fetchChildTab() {
        if (childTabs == null) {
            childTabs = new ArrayList<Pair<String, Integer>>();
        } else {
            childTabs.clear();
        }
        closeSearchAndFilterLayout();
        if (processIntentData()) {
            return;
        }
        if (mProcessBtn.isChecked()) {
            mCenterListAdapter.setCheckTab(DbQueryConstant.UNPROCESSED);
            mPresenter.addProcessTab(childTabs);
        } else if (mSalesNoteBtn.isChecked()) {
            mCenterListAdapter.setCheckTab(DbQueryConstant.SALES);
            mPresenter.addSaleNoteTab(childTabs);
        }
        mCenterListAdapter.clear();
        setChildTabAndFillListData(isInChildTab());
    }

    /**
     * 判断当前tab里之前是不是存在,用于切换tab后的恢复
     */
    private int isInChildTab() {
        for (int i = 0; i < childTabs.size(); i++) {
            Pair<String, Integer> pair = childTabs.get(i);
            if (mProcessBtn.isChecked()) {
                if (pair.second == firstClickTab) {
                    return i;
                }
            } else {
                if (pair.second == secondClickTab) {
                    return i;
                }
            }

        }
        return 0;
    }

    private int getCurrentAllTab() {
        if (mProcessBtn.isChecked()) {
            return DbQueryConstant.UNPROCESSED_ALL;
        } else {
            return DbQueryConstant.SALES_ALL;
        }
    }

    private int getCurrentParentTab() {
        return mProcessBtn.isChecked() ? DbQueryConstant.UNPROCESSED : DbQueryConstant.SALES;
    }

    /**
     * 关闭搜索筛选面板
     */
    private void closeSearchAndFilterLayout() {
        searchIcon();
        mFilterTipLayout.setVisibility(View.GONE);
        if (filterConditions != null) {
            filterConditions.clear();
        }
    }

    private void setChildTabAndFillListData(int index) {
        mChildTabView.setLayoutManager(new RecyclerGridLayoutManager(getActivity().getApplicationContext(), childTabs.size()));
        mChildTabAdapter.setItems(childTabs);
        //设置第一项选中
        mChildTabAdapter.setSelectItem(index);
        Pair<String, Integer> item = (Pair<String, Integer>) mChildTabAdapter.getItem(index);
        mChildTab = item.second;
        saveClickStateToRestore();
        mCenterListAdapter.setChildTab(mChildTab);
        isSalesPaid();
        refreshBindDeliveryUserLayout();
        if (!isIntoSquareMod()) {
            mPresenter.loadData(mChildTab, getFilterCondition(), null);
        }
        countOrder();
    }

    private boolean isIntoSquareMod() {
        if (mTargetTab == DbQueryConstant.SALES_PAID
                && mSquareModEnable) {
            squareAccountClickMode();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        unRegisterDataObserver();
        unRegisterNotifyObserver();
        destroyPresenter();
        super.onDestroy();
    }

    private void destroyPresenter() {
        if (mPresenter != null) {
            mPresenter.destroy();
            mPresenter = null;
        }
    }

    @Click({R.id.process_btn, R.id.sales_note_btn})
    void listTabClick() {
        if (selectWaiter != null && selectWaiter.isChecked()) {
            user = null;
            if (isFromSnack()) {
                selectWaiter.setText(R.string.select_deliverer);
            } else {
                selectWaiter.setText(R.string.select_waiter);
            }
            allWaiter.setChecked(true);
            mPresenter.loadData(mChildTab, getFilterCondition(0, null), null);
        }

        mChildTabAdapter.setCheck(mProcessBtn.isChecked());
        fetchChildTab();
    }

    /**
     * 获取除京东外的所有来源
     *
     * @return
     */
    public List<SourceId> getAllSourceNotJD() {
        List<SourceId> sourceIds = new ArrayList<>(Arrays.asList(SourceId.values()));
        sourceIds.remove(SourceId.JD_HOME);
        return sourceIds;
    }

    @Click({R.id.process_delivery_user, R.id.process_send})
    void listTabDeliveryUserAndSendClick(View view) {
        FilterCondition filterCondition = getFilterCondition();
        if (view.getId() == R.id.process_delivery_user) {
            tvDeliveryUserBindConfirm.setText(R.string.order_center_list_bind_delivery_user);
            mSelectedProcessDelivery = 1;
            List<SourceId> sourceIds = filterCondition.getSourceIds();
            if (sourceIds == null) {
                filterCondition.setSourceIds(getAllSourceNotJD());
            } else {
                sourceIds.remove(SourceId.JD_HOME);
            }
        } else {
            tvDeliveryUserBindConfirm.setText(R.string.order_center_create_send);
            mSelectedProcessDelivery = 2;
        }
        chkDeliveryUserSelectAll.setChecked(false);
        mPresenter.loadData(mChildTab, filterCondition, null);
    }

    @Click(R.id.order_search_btn)
    void searchClick() {
        Object tag = mSearchBtn.getTag();
        if (tag == null) {
            searchMode();
            mPresenter.loadData(getCurrentAllTab(), getFilterCondition(), null);
        } else {
            switch ((int) tag) {
                case 0:
                    searchMode();
                    mPresenter.loadData(getCurrentAllTab(), getFilterCondition(), null);
                    break;
                case 1:
                    searchIcon();
                    mPresenter.loadData(mChildTab, getFilterCondition(), null);
                    break;
            }
        }
    }

    /**
     * 显示搜索按钮
     */
    private void searchIcon() {
        mSearchBtn.setImageResource(R.drawable.order_center_search);
        mSearchBtn.setTag(0);
        mChildTabView.setVisibility(View.VISIBLE);
        mSearchLayout.setVisibility(View.GONE);
        countOrder();
        isInSearchMode = false;
        mSearchContent.setText("");
        isSalesPaid();
        refreshBindDeliveryUserLayout();
    }

    /**
     * 显示搜索面板和关闭图标
     */
    private void searchMode() {
        mSearchBtn.setImageResource(R.drawable.close_icon);
        mSearchBtn.setTag(1);
        mSearchContent.requestFocus();
        showSoftKeyboard(mSearchContent);
        mChildTabView.setVisibility(View.GONE);
        mSearchLayout.setVisibility(View.VISIBLE);
        isInSearchMode = true;
        mSquareAccountLayout.setVisibility(View.GONE);
        rlBindDeliveryUser.setVisibility(View.GONE);
        mSearchType.setText(getString(R.string.all));
        searchPosition = 0;
        setSearchContentHitAll();
    }

    /**
     * 搜索全部按钮点击
     */
    @Click(R.id.order_center_search_type)
    void searchTypeClick() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag("search_type");
        if (fragment != null) {
            ft.remove(fragment);
        }
        SearchTypeFragment searchTypeFragment = new SearchTypeFragment_();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFromSnack", isFromSnack());
        searchTypeFragment.setArguments(bundle);
        String[] contents;
        if (mProcessBtn.isChecked()) {
            if (isFromSnack()) {
                contents = getResources().getStringArray(R.array.order_center_process_search_type);
            } else {
                contents = getResources().getStringArray(R.array.order_center_process_dinner_search_type);
            }
        } else {
            if (isFromSnack()) {
                contents = getResources().getStringArray(R.array.order_center_sales_search_type);
            } else {
                contents = getResources().getStringArray(R.array.order_center_sales_dinner_search_type);
            }
        }
        searchTypeFragment.setContent(contents, searchPosition);
        searchTypeFragment.showAsDown(ft, "search_type", mSearchType);
        searchTypeFragment.setOnItemClickListener(new SearchTypeFragment.OnItemClickListener() {
            @Override
            public void onItemClick(String content, int position) {
                mSearchType.setText(content);
                if (position == 0) {
                    setSearchContentHitAll();
                } else {
                    mSearchContent.setHint(content);
                }
                mSearchContent.setText("");
                searchPosition = position;
            }
        });
    }

    private void setSearchContentHitAll() {
        if (mProcessBtn.isChecked()) {
            if (isFromSnack()) {
                mSearchContent.setHint(getString(R.string.search_process_hint));
            } else {
                mSearchContent.setHint(getString(R.string.search_process_dinner_hint));
            }
        } else {
            if (isFromSnack()) {
                mSearchContent.setHint(getString(R.string.search_sales_note_hint));
            } else {
                mSearchContent.setHint(getString(R.string.search_sales_note_dinner_hint));
            }
        }
    }

    @Click(R.id.order_filter_btn)
    void filterClick() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag("filter");
        if (fragment != null) {
            ft.remove(fragment);
        }
        FilterFragment filterFragment = new FilterFragment_();
        filterFragment.setFilterCondition(filterConditions);
        filterFragment.setFilterData(mPresenter.getFilterData());
        filterFragment.showAsDown(ft, "filter", mFilterBtn);
        filterFragment.setOnFilterClickListener(new FilterFragment.OnFilterClickListener() {
            @Override
            public void filterOkClick(List<android.util.Pair<String, ValueEnum>> values) {
                filterOrderConditions(values);
            }
        });
    }

    private void filterOrderConditions(List<android.util.Pair<String, ValueEnum>> values) {
        if (values.isEmpty()) {
            filterCloseClick();
        } else {
            filterConditions = values;
            setFilterTipsByConditions();
            String keyword = mSearchContent.getText().toString();
            if (isInSearchMode) {
                mPresenter.search(getCurrentParentTab(), searchPosition, keyword, getFilterCondition(), null);
            } else {
                mPresenter.loadData(mChildTab, getFilterCondition(), null);
            }
            mSelectCheckBox.setChecked(false);
        }
    }

    private void setFilterTipsByConditions() {
        String condition = selectFilterCondition();
        if (TextUtils.isEmpty(condition)) {
            setFilterTipsEmpty();
        } else {
            mFilterTips.setText(getString(R.string.filter_condition) + condition);
            mFilterTipLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 取出筛选条件,用于展示
     */
    private String selectFilterCondition() {
        StringBuilder builder = new StringBuilder();
        for (android.util.Pair<String, ValueEnum> condition : filterConditions) {
            if (condition.second instanceof SourceId) {
                builder.append(condition.first + "/");
            }
        }
        //绑定外卖员模式下，移除配送方式
        if (isInDeliveryMode) {
            for (int i = filterConditions.size() - 1; i >= 0; i--) {
                android.util.Pair<String, ValueEnum> pair = filterConditions.get(i);
                if (pair.second instanceof DeliveryType) {
                    filterConditions.remove(pair);
                }
            }
        } else {
            for (android.util.Pair<String, ValueEnum> condition : filterConditions) {
                if (condition.second instanceof DeliveryType) {
                    builder.append(condition.first + "/");
                }
            }
        }
        //清账模式或派单模式下,移除配送状态
        if (isInSquareAccountMode || isInDeliveryMode) {
            for (int i = filterConditions.size() - 1; i >= 0; i--) {
                android.util.Pair<String, ValueEnum> pair = filterConditions.get(i);
                if (pair.second instanceof DeliveryStatus
                        || pair.second instanceof DeliveryOrderStatus
                        || pair.second instanceof DeliveryOrderSubStatus) {
                    filterConditions.remove(pair);
                }
            }
        } else {
            for (android.util.Pair<String, ValueEnum> condition : filterConditions) {
                if (condition.second instanceof DeliveryStatus || condition.second instanceof DeliveryOrderStatus) {
                    builder.append(condition.first + "/");
                }
            }
        }
        String str = builder.toString();
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str.substring(0, str.length() - 1);
    }

    @Click(R.id.order_center_filter_close)
    void filterCloseClick() {
        if (filterConditions != null) {
            filterConditions.clear();
        }
        setFilterTipsEmpty();
        String keyword = mSearchContent.getText().toString();
        if (isInSearchMode) {
            mPresenter.search(getCurrentParentTab(), searchPosition, keyword, getFilterCondition(), null);
        } else {
            mPresenter.loadData(mChildTab, getFilterCondition(), null);
        }
        mSelectCheckBox.setChecked(false);
    }

    private void setFilterTipsEmpty() {
        mFilterTipLayout.setVisibility(View.GONE);
        mFilterTips.setText("");
    }

    private void isSalesPaid() {
        mCenterListAdapter.setChildTab(mChildTab);
        if (mChildTab == DbQueryConstant.SALES_PAID
                || mChildTab == DbQueryConstant.SALES_UNPAID) {
//            salesPaidCommonMode(); update by dzb  清帐状态不显示
            if (mChildTab == DbQueryConstant.SALES_UNPAID) {
                mSquareAccount.setVisibility(View.GONE);
            }
            //读取设置项,不存在则打开
            boolean takeMealNotice = SharedPreferenceUtil.getSpUtil().getBoolean(KEY_TAKE_MEAL_NOTICE, true);
            if (takeMealNotice) {
                mTakeMealNotice.setChecked(true);
            } else {
                mTakeMealNotice.setChecked(false);
            }
            if (!isTakeMealNotice() && mChildTab == DbQueryConstant.SALES_UNPAID) {
                mCenterListAdapter.setCheckBoxVisibility(false);
                mSquareAccountLayout.setVisibility(View.GONE);
            }
        } else {
            mCenterListAdapter.setCheckBoxVisibility(false);
            mSquareAccountLayout.setVisibility(View.GONE);
        }
        isInSquareAccountMode = false;
        mSquareAccountFilterLayout.setVisibility(View.GONE);
        mSearchBtn.setVisibility(View.VISIBLE);
        registerDataObserver();
    }

    /**
     * 清账
     */
    @Click(R.id.square_account)
    void squareAccountClickMode() {
        mSquareAccountLayout.setVisibility(View.VISIBLE);
        tvSquareAccountFilterTip.setText(R.string.order_center_list_square_account_tip);
        mSquareAccountFilterLayout.setVisibility(View.VISIBLE);
        if (isFromSnack()) {
            waiterGroup.setVisibility(View.VISIBLE);
            selectWaiter.setText(getString(R.string.select_deliverer));
            allWaiter.setText(getString(R.string.all_deliverer));
        } else {
            waiterGroup.setVisibility(View.VISIBLE);
        }
        mTips.setVisibility(View.VISIBLE);
        mChildTabView.setVisibility(View.GONE);
        mCenterListAdapter.setCheckBoxVisibility(true);
        mSquareAccount.setVisibility(View.GONE);
        mSelectCheckBox.setVisibility(View.VISIBLE);
        mSelectCheckBox.setChecked(false);
        mSelectTip.setVisibility(View.VISIBLE);
        mCancel.setVisibility(View.VISIBLE);
        mConfirm.setVisibility(View.VISIBLE);
        mTakeMealNotice.setVisibility(View.GONE);
        mTakeMealNoticeTip.setVisibility(View.GONE);
        isInSquareAccountMode = true;
        if (Utils.isNotEmpty(filterConditions)) {
            setFilterTipsByConditions();
        } else {
            setFilterTipsEmpty();
        }
        mPresenter.loadData(mChildTab, getFilterCondition(), null);
        mSearchBtn.setVisibility(View.GONE);
        unRegisterDataObserver();
    }

    @Override
    public boolean isInSquareAccountMode() {
        return isInSquareAccountMode;
    }

    @Override
    @UiThread
    public void hideEmptyAndListView() {
        if (emptyView != null) {
            emptyView.setVisibility(View.GONE);
        }
        if (mListView != null) {
            mListView.setVisibility(View.GONE);
        }
    }

    @Override
    @UiThread
    public void showLoadingView() {
        if (mLoadingSmallView != null) {
            mLoadingSmallView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    @UiThread
    public void dismissLoadingView() {
        if (mLoadingSmallView != null) {
            mLoadingSmallView.setVisibility(View.GONE);
        }
    }

    /**
     * 取消清账
     */
    @Click(R.id.order_center_cancel)
    void cancelClick() {
        if (user != null) {
            user = null;
            allWaiter.setChecked(true);
        }
        mSearchBtn.setVisibility(View.VISIBLE);
        mSquareAccountFilterLayout.setVisibility(View.GONE);
        waiterGroup.setVisibility(View.GONE);
        mChildTabView.setVisibility(View.VISIBLE);
        isInSquareAccountMode = false;
        mPresenter.loadData(mChildTab, getFilterCondition(), null);
        salesPaidCommonMode();
        registerDataObserver();
    }

    private void salesPaidCommonMode() {
        mSquareAccountLayout.setVisibility(View.VISIBLE);
        mCenterListAdapter.setCheckBoxVisibility(false);
        mSquareAccount.setVisibility(View.VISIBLE);
        if (isTakeMealNotice()) {
            mTakeMealNotice.setVisibility(View.VISIBLE);
            mTakeMealNoticeTip.setVisibility(View.VISIBLE);
        } else {
            mTakeMealNotice.setVisibility(View.GONE);
            mTakeMealNoticeTip.setVisibility(View.GONE);
        }
        mSelectCheckBox.setVisibility(View.GONE);
        mSelectTip.setVisibility(View.GONE);
        mTips.setVisibility(View.GONE);
        mCancel.setVisibility(View.GONE);
        mConfirm.setVisibility(View.GONE);
    }

    /**
     * 是否支持广播叫号
     *
     * @return
     */
    private boolean isTakeMealNotice() {
        return isFromSnack() || mFromType == OCConstant.FromType.FROM_TYPE_DINNER;
    }

    /**
     * 确认清账
     */
    @Click(R.id.confirm_square_account)
    void confirmSquareAccountButtonClick() {
        mPresenter.deliveryPayment(mCenterListAdapter.getSelectedOrders());
    }

    @Click(R.id.take_meal_notice_button)
    void takeMealNotice() {
        if (mTakeMealNotice.isChecked()) {
            mCenterListAdapter.setTakeMealNoticeVisible(true);
        } else {
            mCenterListAdapter.setTakeMealNoticeVisible(false);
        }
        SharedPreferenceUtil.getSpUtil().putBoolean(KEY_TAKE_MEAL_NOTICE, mTakeMealNotice.isChecked());
    }

    @Click(R.id.order_center_select_check_box)
    void checkBoxClick() {
        if (mSelectCheckBox.isChecked()) {
            mCenterListAdapter.selectAll();
        } else {
            mCenterListAdapter.cancelAll();
        }
    }

    @Click(R.id.cashier_title_bar_menu_btn)
    protected void menuButtonClick() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.openSideMenu();
        }
    }

    @Click(R.id.order_refresh)
    protected void orderRefreshClick() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.openNotifyCenter();
        }
    }

    //所有服务员被选中
    private void allWaiterSelected() {
        user = null;
        allWaiter.setChecked(true);
        selectWaiter.setChecked(false);
        if (isFromSnack()) {
            selectWaiter.setText(R.string.select_deliverer);
        } else {
            selectWaiter.setText(R.string.select_waiter);
        }
        mPresenter.loadData(mChildTab, getFilterCondition(0, null), null);
        mSelectCheckBox.setChecked(false);
    }

    @Click(R.id.select_waiter)
    protected void selectWaiterClick() {
        selectWaiterSelected();
    }

    //部分服务员被选中
    private void selectWaiterSelected() {
        int titleResId;
        if (isFromSnack()) {
            allUserList = Session.getFunc(UserFunc.class).getUsers(FastFoodApplication.PERMISSION_FASTFOOD_SC);
            titleResId = R.string.select_deliverer_label;
        } else {
            allUserList = Session.getFunc(UserFunc.class).getUsers();
            titleResId = R.string.select_waiter_label;
        }
        SharedPreferenceUtil.getSpUtil().putBoolean("IsLoginActivity", false);
        selectWaiter.setChecked(true);
        userDialog = new UserDialog(getActivity(), titleResId, allUserList, user, this);
        userDialog.setDialogCloseListener(mCloseListener);
        userDialog.show();
    }

    @Override
    public void refreshNotifyCenterTip() {
        Activity activity = getActivity();
        /*if (activity instanceof MainActivity) {
            NotifyCenterView notifyCenterView = ((MainActivity) activity).getNotifyCenterView();
            NotifyCenterManager.getInstance().refreshNotifyCenterTip(getActivity(), notifyCenterView, vNotifyCenterTip);
        }*/ /*else if (activity instanceof RetailMainActivity) {
            NotifyCenterView notifyCenterView = ((RetailMainActivity) activity).getNotifyCenterView();
            NotifyCenterManager.getInstance().refreshNotifyCenterTip(getActivity(), notifyCenterView, vNotifyCenterTip);
        }else if (activity instanceof BakeryMainActivity) {
            NotifyCenterView notifyCenterView = ((BakeryMainActivity) activity).getNotifyCenterView();
            NotifyCenterManager.getInstance().refreshNotifyCenterTip(getActivity(), notifyCenterView, vNotifyCenterTip);
        }*/
    }

    @Override
    public FragmentManager getViewFragmentManager() {
        return getFragmentManager();
    }

    @Override
    public FragmentActivity getViewActivity() {
        return getActivity();
    }

    @Override
    public void showToast(String message) {
        ToastUtil.showShortToast(message);
    }

    @Override
    public void refreshList(List<TradePaymentVo> tradePaymentVos, int type) {
        if (!isDestroyView()) {
            if (type == 0) {
                dismissLoadingView();
                if (!tradePaymentVos.isEmpty()) {
                    mCenterListAdapter.setItems(tradePaymentVos);
                    showEmptyView(false);
                    fireEventBus(tradePaymentVos.get(0).getTradeVo());
                } else {
                    mCenterListAdapter.clear();
                    showEmptyView(true);
                    fireEmptyBus();
                }
            } else {//1为加载更多模式
                if (!tradePaymentVos.isEmpty()) {
                    mCenterListAdapter.addItem(tradePaymentVos);
                }
            }
        }

    }

    @Override
    public void squareAccountFinish() {
        mSelectCheckBox.setChecked(false);
        mPresenter.loadData(mChildTab, getFilterCondition(), null);
    }

    @Override
    public void batchBindDeliveryUserFinish() {
        chkDeliveryUserSelectAll.setChecked(false);
        mPresenter.loadData(mChildTab, getFilterCondition(), null);
    }

    @Override
    public void refreshOrderCount(int tab, long orderCount) {
        if (mProcessBtn.isChecked()) {
            for (int i = 0; i < childTabs.size(); i++) {
                Pair<String, Integer> pair = childTabs.get(i);
                if (pair != null && pair.second == tab) {
                    OrderCenterChildTabView view = (OrderCenterChildTabView) mChildTabView.getChildAt(i);
                    if (view != null) {
                        view.setNum(orderCount);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public String getOriginTip() {
        return getString(R.string.order_tips, 0) + ResourceUtils.getString(R.string.zero);
    }

    public void showEmptyView(boolean show) {
        if (show) {
            emptyView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
    }


    private FilterCondition getFilterCondition() {
        return getFilterCondition(0, user);
    }

    private FilterCondition getFilterCondition(long Id, User user) {
        FilterCondition condition = new FilterCondition();
        if (isInSquareAccountMode) {
            if (isFromSnack()) {
                condition.setHasBindDeliveryUser(true);
                condition.setDeliveryStatus(DeliveryStatus.REAL_DELIVERY);
                condition.setPayModeId(PayModeId.CASH);
                condition.setPaySource(PaySource.ON_MOBILE);
                if (user != null) {//配送员信息
                    String[] userInfo = {user.getId().toString(), user.getAccount()};
                    condition.setDeliveryInfo(userInfo);
                }
            } else {
                condition.setTradeStatus(Utils.entity2List(TradeStatus.SQUAREUP));
                condition.setPayModeId(PayModeId.CASH);
                if (Id != 0) {//服务员id
                    condition.setUpdatorId(Id);
                }
                if (user != null && user.getId() != 0) {
                    condition.setUpdatorId(user.getId());
                }
            }
        } else if (isInDeliveryMode && isFromSnack()) {
            List<TradeStatus> tradeStatus = new ArrayList<>();
            tradeStatus.add(TradeStatus.CONFIRMED);
            tradeStatus.add(TradeStatus.FINISH);
            condition.setTradeStatus(tradeStatus);
            condition.setDeliveryTypes(Utils.entity2List(DeliveryType.SEND));
            List<DeliveryOrderStatus> deliveryOrderStatuses = new ArrayList<>();
            deliveryOrderStatuses.add(DeliveryOrderStatus.WAITING_CREATE);
            condition.setDeliveryOrderStatuses(deliveryOrderStatuses);
            if (mProcessDeliveryUser.isChecked()) {//派单模式
                condition.setExcludeOfflineOrder(true);
            } else { //下发模式
                condition.setExcludePosOrder(true);
            }
        } else {
            condition.setUpdatorId(0);
        }
        if (Utils.isEmpty(filterConditions)) {
            return condition;
        }
        List<SourceId> sourceIdList = new ArrayList<>();
        List<DeliveryType> deliveryTypeList = new ArrayList<>();
        List<DeliveryOrderStatus> deliveryOrderStatuses = new ArrayList<DeliveryOrderStatus>();
        List<DeliveryOrderSubStatus> deliveryOrderSubStatuses = new ArrayList<DeliveryOrderSubStatus>();
        for (android.util.Pair<String, ValueEnum> pair : filterConditions) {
            if (pair.second instanceof SourceId) {
                sourceIdList.add((SourceId) pair.second);
            } else if (pair.second instanceof DeliveryType) {
                deliveryTypeList.add((DeliveryType) pair.second);
            } else if (pair.second instanceof DeliveryOrderStatus) {
                deliveryOrderStatuses.add((DeliveryOrderStatus) pair.second);
            } else if (pair.second instanceof DeliveryOrderSubStatus) {
                deliveryOrderSubStatuses.add((DeliveryOrderSubStatus) pair.second);
            }
        }
        condition.setSourceIds(sourceIdList);
        condition.setDeliveryTypes(deliveryTypeList);
        condition.setDeliveryOrderStatuses(deliveryOrderStatuses);
        condition.setDeliveryOrderSubStatuses(deliveryOrderSubStatuses);
        return condition;
    }

    private void registerDataObserver() {
        if (mDataObserver == null) {
            mDataObserver = new OrderCenterChangeObserver();
        }
        if (!DatabaseHelper.Registry.isRegistered(mDataObserver)) {
            DatabaseHelper.Registry.register(mDataObserver);
        }
    }

    private void registerNotifyObserver() {
        if (mNotifyObserver == null) {
            mNotifyObserver = new OrderNotifyChangeObserver();
        }
        if (!DatabaseHelper.Registry.isRegistered(mNotifyObserver)) {
            DatabaseHelper.Registry.register(mNotifyObserver);
        }
    }

    private void unRegisterDataObserver() {
        if (mDataObserver != null && DatabaseHelper.Registry.isRegistered(mDataObserver)) {
            DatabaseHelper.Registry.unregister(mDataObserver);
            mDataObserver = null;
        }
    }

    private void unRegisterNotifyObserver() {
        if (mNotifyObserver != null && DatabaseHelper.Registry.isRegistered(mNotifyObserver)) {
            DatabaseHelper.Registry.unregister(mNotifyObserver);
            mNotifyObserver = null;
        }
    }

    @Override
    public void onSelected(User item, Long userId, String userName) {
        mSelectCheckBox.setChecked(false);
        if (allUserList == null || allUserList.size() == 0 || userId == null) {
            return;
        }
        int size = allUserList.size();
        for (int i = 0; i < size; i++) {
            User authUser = allUserList.get(i);
            if (authUser.getId() != null && userId.equals(authUser.getId())) {
                user = authUser;
                if (isFromSnack()) {
                    mPresenter.loadData(mChildTab, getFilterCondition(0, user), null);
                } else {
                    mPresenter.loadData(mChildTab, getFilterCondition(user.getId(), null), null);
                }
                selectWaiter.setText(authUser.getName());
                return;
            }
        }
    }

    @Click(R.id.tv_bind_delivery_user_and_send)
    void bindDeliveryUserButtonClick() {
        if (!ClickManager.getInstance().isClicked()) {
            rgProcessDisplayGroup.setVisibility(View.GONE);
            rgProcessDeliveryUserAndSend.setVisibility(View.VISIBLE);
            mChildTabLayout.setVisibility(View.GONE);
            waiterGroup.setVisibility(View.GONE);
            mTips.setVisibility(View.VISIBLE);
            mChildTabView.setVisibility(View.GONE);
            mFilterBtn.setVisibility(View.GONE);
            mCenterListAdapter.setCheckBoxVisibility(true);
            tvBindDeliveryUserAndSend.setVisibility(View.GONE);
            mKoubeiVerfication.setVisibility(View.GONE);
            chkDeliveryUserSelectAll.setVisibility(View.VISIBLE);
            chkDeliveryUserSelectAll.setChecked(false);
            tvDeliveryUserSelectAllTip.setVisibility(View.VISIBLE);
            tvDeliveryUserBindConfirm.setVisibility(View.VISIBLE);
            tvDeliveryUserBindCancel.setVisibility(View.VISIBLE);
            mSearchBtn.setVisibility(View.GONE);
            isInDeliveryMode = true;
            mCenterListAdapter.setInBindDeliveryUserMode(isInDeliveryMode);
            if (Utils.isNotEmpty(filterConditions)) {
                setFilterTipsByConditions();
            } else {
                setFilterTipsEmpty();
            }

            FilterCondition filterCondition = getFilterCondition();
            if (mSelectedProcessDelivery == 1) {
                List<SourceId> sourceIds = filterCondition.getSourceIds();
                if (sourceIds == null) {
                    filterCondition.setSourceIds(getAllSourceNotJD());
                } else {
                    sourceIds.remove(SourceId.JD_HOME);
                }
            }

            mPresenter.loadData(mChildTab, filterCondition, null);
            unRegisterDataObserver();
        }
    }

    @Click(R.id.order_center_delivery_user_select_all_chk)
    void deliveryUserSelectAllButtonClick() {
        if (chkDeliveryUserSelectAll.isChecked()) {
            mCenterListAdapter.selectAll();
        } else {
            mCenterListAdapter.cancelAll();
        }
    }

    @Click(R.id.order_center_delivery_user_bind_confirm_tv)
    void deliveryUserBindConfirmButtonClick() {
        if (!ClickManager.getInstance().isClicked()) {
            if (Utils.isEmpty(mCenterListAdapter.getSelectedOrders())) {
                ToastUtil.showShortToast(R.string.order_center_list_no_selected_orders);
                return;
            }
            if (mProcessDeliveryUser.isChecked()) {
                mPresenter.batchBindDeliveryUser();
            } else {
                List<TradeVo> selectedOrders = mCenterListAdapter.getSelectedOrders();
                mPresenter.showDeliveryPlatformChoose(selectedOrders);
            }
        }
    }

    @Click(R.id.order_center_delivery_user_bind_cancel_tv)
    void deliveryUserBindCancelButtonClick() {
        if (!ClickManager.getInstance().isClicked()) {
            rgProcessDisplayGroup.setVisibility(View.VISIBLE);
            rgProcessDeliveryUserAndSend.setVisibility(View.GONE);
            tvSquareAccountFilterTip.setVisibility(View.VISIBLE);
            mChildTabLayout.setVisibility(View.VISIBLE);
            mSearchBtn.setVisibility(View.VISIBLE);
            mSquareAccountFilterLayout.setVisibility(View.GONE);
            waiterGroup.setVisibility(View.GONE);
            mChildTabView.setVisibility(View.VISIBLE);
            mTips.setVisibility(View.GONE);
            refreshBindDeliveryUserLayout();
            mPresenter.loadData(mChildTab, getFilterCondition(), null);
            registerDataObserver();
        }
    }

    private void refreshBindDeliveryUserLayout() {
        //当前子标签是否销货单-全部
        if (DbQueryConstant.SALES_ALL == mChildTab && isFromSnack()) {
            mCenterListAdapter.setCheckBoxVisibility(false);
            rlBindDeliveryUser.setVisibility(View.VISIBLE);
            tvBindDeliveryUserAndSend.setVisibility(View.VISIBLE);
            displayKouBeiScan();
            chkDeliveryUserSelectAll.setVisibility(View.GONE);
            tvDeliveryUserSelectAllTip.setVisibility(View.GONE);
            tvDeliveryUserBindConfirm.setVisibility(View.GONE);
            tvDeliveryUserBindCancel.setVisibility(View.GONE);
            mTips.setVisibility(View.GONE);
        } else {
            mCenterListAdapter.setCheckBoxVisibility(false);
            rlBindDeliveryUser.setVisibility(View.GONE);
            if (DbQueryConstant.SALES_ALL == mChildTab && mFromType == OCConstant.FromType.FROM_TYPE_DINNER) {
                rlBindDeliveryUser.setVisibility(View.VISIBLE);
                tvBindDeliveryUserAndSend.setVisibility(View.GONE);
                displayKouBeiScan();
            }
        }
        mFilterBtn.setVisibility(View.GONE);//update by dzb
        isInDeliveryMode = false;
        mCenterListAdapter.setInBindDeliveryUserMode(isInDeliveryMode);
    }

    @Override
    public void showDeliveryUserChooseDialog(List<User> authUsers) {
        final List<TradeVo> selectedOrders = mCenterListAdapter.getSelectedOrders();
        if (Utils.isNotEmpty(selectedOrders)) {
            SelectSenderDialogFragment.OnSelectAuthUserListener listener = new SelectSenderDialogFragment.OnSelectAuthUserListener() {
                @Override
                public void onSelect(User authUser) {
                    mPresenter.bindDeliveryUser(selectedOrders, authUser);
                }
            };
            SelectSenderDialogFragment selectSenderDialogFragment = SelectSenderDialogFragment.newInstance(authUsers, null);
            selectSenderDialogFragment.setListener(listener);
            selectSenderDialogFragment.show(getActivity().getFragmentManager(), SelectSenderDialogFragment.class.getSimpleName());
        } else {
            showToast(getString(R.string.order_center_list_no_selected_orders));
        }
    }

    @Override
    public void showDeliveryPlatformChooseView(List<PartnerShopBiz> partnerShopBizs) {
        final List<TradeVo> selectedOrders = mCenterListAdapter.getSelectedOrders();
        if (Utils.isNotEmpty(selectedOrders)) {
            mDeliveryPlatformPopupWindow = new DeliveryPlatformPopupWindow(getActivity(), (int) (tvDeliveryUserBindConfirm.getWidth() * 1.5), partnerShopBizs, new DeliveryPlatformPopupWindow.OnDeliveryPlatformSelectedListener() {
                @Override
                public void OnDeliveryPlatformSelected(PartnerShopBiz partnerShopBiz) {
                    mPresenter.prepareDeliveryOrderDispatch(selectedOrders, partnerShopBiz);
                }
            });
            mDeliveryPlatformPopupWindow.showAlignRight(tvDeliveryUserBindConfirm);
        } else {
            showToast(getString(R.string.order_center_list_no_selected_orders));
        }
    }

    @Override
    public void showDeliveryFeeView(final List<TradeVo> tradeVos, final List<BatchDeliveryFee> deliveryFees, final PartnerShopBiz partnerShopBiz) {
        DeliveryFeeDialogFragment deliveryFeeDialogFragment = DeliveryFeeDialogFragment.newInstance(deliveryFees, tradeVos);
        deliveryFeeDialogFragment.show(getActivity().getFragmentManager(), DeliveryFeeDialogFragment.class.getSimpleName());
        deliveryFeeDialogFragment.setListener(new DeliveryFeeDialogFragment.OnOkListener() {
            @Override
            public void onOkClick() {
                mPresenter.deliveryOrderDispatch(tradeVos, partnerShopBiz, deliveryFees);
            }
        });
    }

    @Override
    public void showDispatchFailOrderListAlert(List<DispatchFailOrder> dispatchFailOrders) {
        if (Utils.isNotEmpty(dispatchFailOrders)) {
            DispatchFailOrderListFragment fragment = DispatchFailOrderListFragment.newInstance(dispatchFailOrders);
            fragment.show(getViewFragmentManager(), DispatchFailOrderListFragment.class.getSimpleName());
        }
    }

    @Click(R.id.tv_order_center_koubei_verification)
    public void onVerificationClick() {
        if (!ClickManager.getInstance().isClicked()) {
            VerificationDialog verification = new VerificationDialog_();
            verification.show(getFragmentManager());
        }
    }

    /**
     * 口碑
     */
    private void displayKouBeiScan() {
        // v8.12.0 口碑业务开通显示按钮
        mKoubeiVerfication.setVisibility(mPresenter.isShowKoubeiVerification() ? View.VISIBLE : View.GONE);
    }
}
