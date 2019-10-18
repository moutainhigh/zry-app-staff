package com.zhongmei.bty.dinner.orderdish;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.bty.basemodule.async.manager.AsyncNetworkManager;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem.DishCheckStatus;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.orderdish.message.DishServiceV2Req;
import com.zhongmei.bty.basemodule.orderdish.message.DishServiceV2Resp;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShopcartItemTool;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.constants.DinnerTableConstant;
import com.zhongmei.bty.basemodule.trade.event.ActionTableInfoBottomBarStatus;
import com.zhongmei.bty.basemodule.trade.event.ActionTableInfoBottomBarStatus.BottomBarStatus;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.basemodule.trade.utils.DinnerUtils;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListener;
import com.zhongmei.bty.cashier.util.TradeSourceUtils;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.data.operates.impl.KdsTradeDal;
import com.zhongmei.bty.dinner.action.ActionBatchOperationItems;
import com.zhongmei.bty.dinner.action.ActionSaveDishService;
import com.zhongmei.bty.dinner.orderdish.view.SlideListView;
import com.zhongmei.bty.dinner.orderdish.view.SlideListView.RemoveDirection;
import com.zhongmei.bty.dinner.orderdish.view.SlideListView.RemoveListener;
import com.zhongmei.bty.dinner.shopcart.adapter.BuffetDeskShopCartAdapter;
import com.zhongmei.bty.dinner.shopcart.adapter.BuffetShopCartAdapter;
import com.zhongmei.bty.dinner.shopcart.adapter.DinnerDeskShopcartAdapter;
import com.zhongmei.bty.dinner.shopcart.adapter.DinnerShopCartAdapter;
import com.zhongmei.bty.dinner.shopcart.adapter.DinnerWestShopcartAdapter;
import com.zhongmei.bty.dinner.shopcart.adapter.GroupShopCartAdapter;
import com.zhongmei.bty.dinner.shopcart.adapter.WestDeskShopCartAdapter;
import com.zhongmei.bty.dinner.table.TableFragment;
import com.zhongmei.bty.dinner.table.TableInfoFragment;
import com.zhongmei.bty.dinner.table.event.EventTradeAmount;
import com.zhongmei.bty.snack.event.EventChoice;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.ServingStatus;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.yunfu.net.builder.NetError;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.dinner_dish_tradeinfo_fragment)
public class DinnerDishTradeInfoFragment extends BasicFragment {

    private static final String TAG = DinnerDishTradeInfoFragment.class.getSimpleName();
        public static final String DISHSHOPCART_PAGE = "shopcart_page";
    public static final int PAGE_TABLE = 1;    public static final int PAGE_DISH = 2;    public static final int PAGE_CASH = 3;    public static final int PAGE_BOOKING = 4;    public static final int PAGE_GROUP = 5;
    @ViewById(R.id.lv_selected_dish)
    SlideListView orderDishListView;
    @ViewById(R.id.haveNoDishLayout)
    RelativeLayout haveNoDishLayout;

    @ViewById(R.id.haveNoDishImage)
    ImageView haveNoDishImage;

    @ViewById(R.id.goods_total_number)
    TextView allDishCountTV;
    @ViewById(R.id.tv_trade_extra_label)
    TextView tvTradeExtraLabel;




    @ViewById(R.id.trade_amount_line)
    View lineView;
    @ViewById(R.id.tv_tarde_amount)
    TextView tradeAmountTV;    protected DinnerShopCartAdapter selectedDishAdapter;
    private int mCurrentPosition;
    private TradePrivilege bartchPrivilege;

    protected DinnerShoppingCart mShoppingCart;

    private TradeVo tradeVo;

        private long tradeItemOperationCount = 0;

        private boolean isInDesk = false;

    private static boolean isMoveDishDragMode = false;

        private boolean isShowDelete = true;

    private ChangePageListener mChangePageListener;

    private boolean isDishServingModify = false;
    private DatabaseHelper.DataChangeObserver observer;

    private Timer issueTimeRefreshtimer;
    private Handler uiHandler = new Handler();

    private int mCurrentPage = PAGE_TABLE;
    private boolean enableSlide = true;        private boolean isSlideMode = false;
        public boolean isBatchOperating = false;
        private boolean isWest = false;

    private BusinessType mBusinessType = BusinessType.DINNER;
        private Map<IShopcartItem, PrintOperationOpType> mOpItemOperation = new HashMap<>();
        private Map<String, List<TradeItemOperation>> mUnOpItemOperation = new HashMap<>();

    List<IShopcartItem> currentShopcartList = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentPage = getArguments().getInt(DISHSHOPCART_PAGE, PAGE_TABLE);
        }
        mShoppingCart = DinnerShoppingCart.getInstance();
        observer = new TradeItemOperationDataChangeObserver();
        isWest = DinnerUtils.isWestStyle();
            }

    public void setIsGroup(boolean isSlide) {
        isSlideMode = isSlide;
    }

    public void setBusinessType(BusinessType businessType) {
        this.mBusinessType = businessType;
    }

    public void registerListener(ChangePageListener mChangePageListener) {
        this.mChangePageListener = mChangePageListener;
    }


    public void setEnableSlide(boolean enableSlide) {
        this.enableSlide = enableSlide;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        unregisterEventBus();
                if (issueTimeRefreshtimer != null) {
            issueTimeRefreshtimer.cancel();
        }
        issueTimeRefreshtimer = null;
        super.onDestroy();
    }

    @AfterViews
    void init() {
        boolean isBooking = false;
        if (mCurrentPage == PAGE_TABLE) {
            isInDesk = true;
        } else if (mCurrentPage == PAGE_BOOKING) {
            isBooking = true;
        }
        switch (mBusinessType) {
            case BUFFET:
                if (isInDesk) {
                    selectedDishAdapter = new BuffetDeskShopCartAdapter(getActivity());
                } else {
                    selectedDishAdapter = new BuffetShopCartAdapter(getActivity());
                }
                selectedDishAdapter.setInDesk(isInDesk);                break;
            case GROUP:
                selectedDishAdapter = new GroupShopCartAdapter(getActivity());
                break;
            default:
                                if (!isWest || isBooking) {
                    if (isInDesk) {
                        selectedDishAdapter = new DinnerDeskShopcartAdapter(getActivity());
                    } else {
                        selectedDishAdapter = new DinnerShopCartAdapter(getActivity());
                    }
                } else {
                    if (isInDesk) {
                        selectedDishAdapter = new WestDeskShopCartAdapter(getActivity());
                    } else {
                        selectedDishAdapter = new DinnerWestShopcartAdapter(getActivity());
                    }
                }
                selectedDishAdapter.setInDesk(isInDesk);                break;
        }

        selectedDishAdapter.setSlideDish(isSlideMode);
        selectedDishAdapter.setParentView(orderDishListView);
        orderDishListView.setAdapter(selectedDishAdapter);
        orderDishListView.setItemsCanFocus(false);
        orderDishListView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        orderDishListView.setRemoveListener(removeListener);
        bindItemListener();

        registerEventBus();
        if (isInDesk)
            refreshIssueTime();
    }

    private boolean isDinner() {
        return mBusinessType == BusinessType.DINNER;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int currentPage) {
        mCurrentPage = currentPage;
    }

    @Override
    public void onResume() {
        super.onResume();
        DatabaseHelper.Registry.register(observer);        Log.i(TAG, "onResume: DatabaseHelper.Registry.register");
    }

    @Override
    public void onPause() {
        super.onPause();
        DatabaseHelper.Registry.unregister(observer);        Log.i(TAG, "onPause: DatabaseHelper.Registry.unregister");
    }

    public void updateOrderDishList(List<IShopcartItem> list, TradeVo tradeVo, ShopcartItem shopcartItem, ShoppingCartListener.OperationStatus operationStatus) {
        updateOrderDishList(list, tradeVo);
        if ((mBusinessType == BusinessType.DINNER || mBusinessType == BusinessType.GROUP || mBusinessType == BusinessType.BUFFET)
                && SpHelper.getDefault().getBoolean(SpHelper.DINNER_ORDER_DISH_OPEN_OPERATION)
                && operationStatus == ShoppingCartListener.OperationStatus.addToShoppingCart) {
            String shopcartItemUuid = getShopcartItemUuid(shopcartItem);
            if (shopcartItemUuid != null) {
                final int position = selectedDishAdapter.getItemOfShopchartUuid(shopcartItemUuid);
                itemClicked(position);
                orderDishListView.post(new Runnable() {
                    @Override
                    public void run() {
                        orderDishListView.smoothScrollToPosition(position);
                    }
                });
            }
        }
    }


    private String getShopcartItemUuid(ShopcartItem shopcartItem) {
        if (shopcartItem.getType() == DishType.COMBO) {
            List<SetmealShopcartItem> setmealShopcartItems = shopcartItem.getSetmealItems();
            if (setmealShopcartItems != null && setmealShopcartItems.size() > 0) {
                return setmealShopcartItems.get(setmealShopcartItems.size() - 1).getUuid();
            }
        } else {
            return shopcartItem.getUuid();
        }

        return null;
    }

    public void updateOrderDishList(List<IShopcartItem> list, TradeVo tradeVo) {
        updateOrderDishList(list, tradeVo, true, false, false);
    }


    public void updateOrderDishList(List<IShopcartItem> list, TradeVo tradeVo, boolean isSlide, boolean isGroupCheckMode, boolean isInit) {
        this.tradeVo = tradeVo;
        currentShopcartList = new ArrayList<>();
        if (selectedDishAdapter == null) {
            return;
        }
                orderDishListView.removeSlideItems();
        if (list != null && list.size() != 0) {
            if (isShowDelete) {
                currentShopcartList = mShoppingCart.getAllValidShopcartItem(list);
            } else {
                currentShopcartList = mShoppingCart.filterDishList(list);
            }

            if ((currentShopcartList == null || currentShopcartList.size() == 0) && Utils.isEmpty(tradeVo.getTradeBuffetPeoples())) {
                haveNoDishLayout.setVisibility(View.VISIBLE);
                orderDishListView.setVisibility(View.GONE);
                EventBus.getDefault().post(new EventTradeAmount(""));
            } else {
                haveNoDishLayout.setVisibility(View.GONE);
                orderDishListView.setVisibility(View.VISIBLE);
            }
        } else {
            if (Utils.isNotEmpty(tradeVo.getTradeBuffetPeoples())) {
                haveNoDishLayout.setVisibility(View.GONE);
                orderDishListView.setVisibility(View.VISIBLE);
            } else if (tradeVo.isBuffetUnionMainTrade() || tradeVo.isBuffetUnionSubTrade()) {
                haveNoDishLayout.setVisibility(View.GONE);
                orderDishListView.setVisibility(View.VISIBLE);
            } else {
                haveNoDishLayout.setVisibility(View.VISIBLE);
                orderDishListView.setVisibility(View.GONE);
                EventBus.getDefault().post(new EventTradeAmount(""));
            }
        }
                if (isGroupCheckMode || selectedDishAdapter.isDishCheckMode() && !isMoveDishDragMode) {
            selectedDishAdapter.updateGroupSelectData(currentShopcartList, tradeVo);
        } else {
            selectedDishAdapter.updateData(currentShopcartList, tradeVo, true);
        }
        orderDishListView.setSelection(this.mCurrentPosition);
                if (TableInfoFragment.isEnableServing()
                && isInDesk
                && !selectedDishAdapter.isDishCheckMode()
                && !TradeSourceUtils.isTradeUnProcessed(tradeVo, SourceId.WECHAT)
                && !AsyncNetworkManager.getInstance().queryNotSuccessNotTip(tradeVo.getTrade().getId())
                && isSlide) {
            orderDishListView.enableSlide(true);
        } else {
            orderDishListView.enableSlide(false);
        }

        if (!selectedDishAdapter.isDishCheckMode()) {
            selectedDishAdapter.refreshSelectedItems();
        }
                updateAllTradeInfo();
                updateAllDishCount();
        selectedDishAdapter.notifyDataSetChanged();
        orderDishListView.postInvalidate();
        if (isInit) {
            mUnOpItemOperation.clear();
            mOpItemOperation.clear();
            if (Utils.isNotEmpty(currentShopcartList)) {
                for (IShopcartItem iShopcartItem : currentShopcartList) {
                    if (Utils.isNotEmpty(iShopcartItem.getTradeItemOperations()))
                        mUnOpItemOperation.put(iShopcartItem.getUuid(), new ArrayList<>(iShopcartItem.getTradeItemOperations()));
                    else
                        mUnOpItemOperation.put(iShopcartItem.getUuid(), new ArrayList<TradeItemOperation>());

                    if (iShopcartItem.getType() == DishType.COMBO && Utils.isNotEmpty(iShopcartItem.getSetmealItems())) {
                        List<? extends ISetmealShopcartItem> iSetmealShopcartItems = iShopcartItem.getSetmealItems();
                        for (ISetmealShopcartItem iSetmealShopcartItem : iSetmealShopcartItems) {
                            if (iSetmealShopcartItem != null && !mUnOpItemOperation.containsKey(iSetmealShopcartItem.getUuid())) {
                                if (Utils.isNotEmpty(iSetmealShopcartItem.getTradeItemOperations()))
                                    mUnOpItemOperation.put(iSetmealShopcartItem.getUuid(), new ArrayList<>(iSetmealShopcartItem.getTradeItemOperations()));
                                else
                                    mUnOpItemOperation.put(iSetmealShopcartItem.getUuid(), new ArrayList<TradeItemOperation>());
                            }
                        }
                    }
                }
            }
        }
    }

        private void hideAllTradeInfo() {
        tvTradeExtraLabel.setVisibility(View.GONE);
        lineView.setVisibility(View.GONE);
        tradeAmountTV.setVisibility(View.GONE);
        EventBus.getDefault().post(new EventTradeAmount(""));    }

        private void showAllTradeInfo() {
        double tradeExtra = 0;
        double tradeAmount = 0;
        double tradePrivilegeAmount = 0;
        if (tradeVo != null) {
                        if (tradeVo.getTradePrivileges() != null) {
                List<TradePrivilege> tradeprivileges = tradeVo.getTradePrivileges();
                for (TradePrivilege tradePrivilege : tradeprivileges) {
                    if (tradePrivilege.getStatusFlag() == StatusFlag.VALID && tradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL) {

                        if (tradePrivilege.getPrivilegeAmount() != null) {
                            tradeExtra += tradePrivilege.getPrivilegeAmount().doubleValue();
                        }
                    }
                }
            }
            if (tradeVo.getTrade() != null) {
                Trade trade = tradeVo.getTrade();
                                if (trade.getTradeAmount() != null) {
                    tradeAmount = trade.getTradeAmount().doubleValue();
                }
                if (trade.getPrivilegeAmount() != null) {
                    tradePrivilegeAmount = trade.getPrivilegeAmount().doubleValue();
                }
            }

            tvTradeExtraLabel.setText(getString(R.string.trade_extra_label,
                    Utils.formatPrice(tradeExtra),
                    Utils.formatPrice(tradeVo.getServiceAmount()),
                    Utils.formatPrice(tradeVo.getTradeTaxAmount()),
                    Utils.formatPrice(tradePrivilegeAmount)));
            tradeAmountTV.setText(getString(R.string.receivable) + " " + Utils.formatPrice(tradeAmount));
            EventBus.getDefault().post(new EventTradeAmount(Utils.formatPrice(tradeAmount), tradeVo));
            tvTradeExtraLabel.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);
                                    tradeAmountTV.setVisibility(View.GONE);
        }
    }

        private void updateAllTradeInfo() {
        switch (mCurrentPage) {
            case PAGE_TABLE:                if (isMoveDishDragMode || getFragmentManager() == null) {                    hideAllTradeInfo();
                    return;
                }

                                TableInfoFragment tableInfoFragment = (TableInfoFragment) getFragmentManager()
                        .findFragmentByTag(DinnerTableConstant.CONTROL_FRAGMENT_TAG);
                if (tableInfoFragment.isAddDishMode()) {
                    hideAllTradeInfo();
                    return;
                }

                break;
            case PAGE_DISH:            case PAGE_CASH:            case PAGE_GROUP:
            case PAGE_BOOKING:
                hideAllTradeInfo();
                return;
            default:
                break;
        }
        if (selectedDishAdapter != null && selectedDishAdapter.getCount() > 0) {

            showAllTradeInfo();

        } else {
            hideAllTradeInfo();
        }
    }

        private void updateAllDishCount() {
        if (getActivity() == null) {
            return;
        }
        if (selectedDishAdapter == null || isMoveDishDragMode)        {
            allDishCountTV.setVisibility(View.GONE);
            return;
        }
        selectedDishAdapter.updateCountView(getActivity(), allDishCountTV);
    }


    public void setDishCheckMode(boolean isCheckMode) {
        selectedDishAdapter.setDishCheckMode(isCheckMode);
    }


    private void itemClicked(int position) {
                if (isBatchOperating) {
            ToastUtil.showShortToast(R.string.dinner_finish_batch_operate_first);
            return;
        }

        mCurrentPosition = orderDishListView.getSelectedItemPosition();
        DishDataItem dishDataItem = selectedDishAdapter.getItem(position);
        if (dishDataItem == null || (dishDataItem.getBase() == null) && !dishDataItem.isCategory()) {
            return;
        }

                if (selectedDishAdapter.isDishCheckMode()) {
                        if (dishDataItem.getType() != ItemType.SINGLE && dishDataItem.getType() != ItemType.COMBO && !dishDataItem.isCategory()) {
                return;
            }

                        if (isInDesk) {
                if (isMoveDishDragMode) {                    if (dishDataItem.getCheckStatus() == DishCheckStatus.NOT_CHECK) {
                        dishDataItem.setCheckStatus(DishCheckStatus.CHECKED);
                    } else if (dishDataItem.getCheckStatus() == DishCheckStatus.CHECKED) {
                        dishDataItem.setCheckStatus(DishCheckStatus.NOT_CHECK);
                    } else if (dishDataItem.getCheckStatus() == DishCheckStatus.INVALIATE_CHECK) {
                        return;
                    }
                                        TableInfoFragment tableInfoFragment = (TableInfoFragment) getFragmentManager().findFragmentByTag(DinnerTableConstant.CONTROL_FRAGMENT_TAG);
                    tableInfoFragment.refreshMovedishDragView();
                    tableInfoFragment.initialDishAllCheckBar(true);
                    selectedDishAdapter.notifyDataSetChanged();
                    return;
                } else {                    chooseItemDishOperate(dishDataItem);
                    return;
                }

            }

            choosedOperate(dishDataItem);
            return;
        }
                if (isInDesk) {
            return;
        }
        if (dishDataItem.getType() == ItemType.TITLE_ITEM && isWest && isDinner() && !DinnerShoppingCart.getInstance().isReturnCash()) {
            List<DishDataItem> selectedItems = selectedDishAdapter.getDishDataItemsByCate(dishDataItem);
            if (Utils.isNotEmpty(selectedItems))
                EventBus.getDefault().post(new ActionBatchOperationItems(selectedItems, true));
            selectedDishAdapter.doSelectedTitleItme(dishDataItem.getDishTypeId());
            return;
        }

                        if (dishDataItem.getBase() == null || TextUtils.isEmpty(dishDataItem.getBase().getBatchNo())
                && dishDataItem.getBase().getStatusFlag() == StatusFlag.INVALID) {
            return;
        }
                if (dishDataItem.getBase().getShopcartItemType() == ShopcartItemType.MAINSUB) {
            ToastUtil.showShortToast(R.string.union_main_trade_not_allow_operate_sub_dish);
            return;
        }

        if (dishDataItem.isSelected()) {
            return;
        }
        if (dishDataItem.getType() == ItemType.SINGLE || dishDataItem.getType() == ItemType.COMBO || dishDataItem.getType() == ItemType.CHILD
                || dishDataItem.getType() == ItemType.WEST_CHILD || dishDataItem.getType() == ItemType.CHILD
                ) {
            if (dishDataItem.getBase() != null) {
                selectedDishAdapter.doSelectedItem(dishDataItem.getBase().getUuid());
            }
            EventBus.getDefault().post(dishDataItem);
        }
    }

    private void bindItemListener() {
        orderDishListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong) {
                itemClicked(position);
            }
        });
    }


    public void setShowDelete(boolean isShowDelete) {
        this.isShowDelete = isShowDelete;
        if (mShoppingCart != null)
            updateOrderDishList(mShoppingCart.getShoppingCartDish(), mShoppingCart.getOrder());
    }

    private RemoveListener removeListener = new RemoveListener() {

        @Override
        public void removeItem(RemoveDirection direction, int position) {
            Log.i(TAG, "remove positon:" + position);
            boolean result = selectedDishAdapter.refreshItem(position);
            if (!isDishServingModify() && result) {
                setDishServingModify(true);
                post(new ActionTableInfoBottomBarStatus(BottomBarStatus.DISH_SERVING_MODIFY));
            }

        }
    };

    public boolean isDishServingModify() {
        return isDishServingModify;
    }

    public void setDishServingModify(boolean isDishServingModify) {
        this.isDishServingModify = isDishServingModify;
    }

    public void onEventMainThread(ActionTableInfoBottomBarStatus event) {
        if (event.getStatus() == BottomBarStatus.TRADE_OPERATE || event.getStatus() == BottomBarStatus.TRADE_FINISH
                || event.getStatus() == BottomBarStatus.NEW_TRADE_OPERATE) {            if (event.isUpdateShop()) {
                selectedDishAdapter.cancelServingStatusOperate();
            }
            setDishServingModify(false);
        }
    }



    private CalmResponseListener<ResponseObject<DishServiceV2Resp>> submitModityServerResponseListner = new CalmResponseListener<ResponseObject<DishServiceV2Resp>>(UserActionEvent.DINNER_SCRATCH_DISH) {

        @Override
        public void onSuccess(ResponseObject<DishServiceV2Resp> data) {
            dismissLoadingProgressDialog();
            if (ResponseObject.isOk(data)) {
                ToastUtil.showLongToast(data.getMessage());
                                post(new ActionTableInfoBottomBarStatus(BottomBarStatus.TRADE_OPERATE, false));
            } else {
                ToastUtil.showLongToast(data.getMessage());
            }
        }

        @Override
        public void onError(NetError error) {
            dismissLoadingProgressDialog();
            ToastUtil.showLongToast(error.getVolleyError().getMessage());
        }
    };


    private void modifyServiceDish() {
        final List<DishDataItem> data = selectedDishAdapter.getAllData();
        if (data == null || data.size() == 0)
            return;
        showLoadingProgressDialog();
        TaskContext.bind(this).execute(new SimpleAsyncTask<DishServiceV2Req>() {
            @Override
            public DishServiceV2Req doInBackground(Void... params) {
                DishServiceV2Req dishServiceReq = new DishServiceV2Req();
                try {
                    List<IShopcartItem> shopcartItemList = new ArrayList<>();
                    for (DishDataItem item : data) {
                        if (item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO) {
                            final IShopcartItemBase shopcartItem = item.getBase();
                            ServingStatus dishServing = item.isDishServing() ? ServingStatus.SERVING : ServingStatus.UNSERVING;
                            if (shopcartItem.getServingStatus() != dishServing) {
                                if (tradeVo.isUnionMainTrade()) {                                     dishServiceReq.addUionMainItemServings(shopcartItem.getId(), dishServing.value(), shopcartItem.getServerUpdateTime());
                                    shopcartItemList.add(item.getItem());                                 } else if (tradeVo.isUnionSubTrade()) {                                     if (shopcartItem.getShopcartItemType() == ShopcartItemType.SUBBATCH || shopcartItem.getShopcartItemType() == ShopcartItemType.SUBBATCHMODIFY) {                                         ShopcartItemUtils.splitBatchItem((ReadonlyShopcartItem) shopcartItem);                                         dishServiceReq.addUionSubItemServings(shopcartItem.getBatchId(), dishServing.value(), shopcartItem.getServerUpdateTime(), DishServiceV2Req.Type.MAINBATCH);
                                    } else {                                         dishServiceReq.addUionSubItemServings(shopcartItem.getId(), dishServing.value(), shopcartItem.getServerUpdateTime(), DishServiceV2Req.Type.SUB);
                                    }
                                    shopcartItemList.add(item.getItem());
                                } else {                                     dishServiceReq.addTradeItem(shopcartItem.getId(), dishServing.value(), OperatesFactory.create(KdsTradeDal.class).getTradeItem(shopcartItem.getId()));
                                }
                            }
                        }
                    }
                    buildDishServiceReq(dishServiceReq);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                return dishServiceReq;
            }

            @Override
            public void onPostExecute(DishServiceV2Req dishServiceReq) {
                super.onPostExecute(dishServiceReq);
                if (tradeVo.isUnionMainTrade() || tradeVo.isUnionSubTrade()) {
                    if (dishServiceReq.itemServingsSize() == 0) {
                        dismissLoadingProgressDialog();
                        ToastUtil.showShortToast(R.string.dinner_table_info_dishservice_toast);
                        return;
                    }
                } else {
                    if (dishServiceReq.size() == 0) {
                        dismissLoadingProgressDialog();
                        ToastUtil.showShortToast(R.string.dinner_table_info_dishservice_toast);
                        return;
                    }
                }
                submitMofifyServiceDish(dishServiceReq);
            }
        });
    }


    private void buildDishServiceReq(DishServiceV2Req dishServiceReq) {
        if (tradeVo.isUnionMainTrade()) {             dishServiceReq.tradeId = tradeVo.getTrade().getId();
        } else if (tradeVo.isUnionSubTrade()) {                         TradeVo mainTradeVo = null;
            if (mShoppingCart != null && mShoppingCart.getMainTradeInfo() != null && mShoppingCart.getMainTradeInfo().getTradeVo() != null) {
                mainTradeVo = mShoppingCart.getMainTradeInfo().getTradeVo();
            }
            MathShopcartItemTool.mathMainShopcartItemsAmount(mainTradeVo, mShoppingCart.getMainTradeInfo().getItems());
            MathShopcartItemTool.mathSubShopcartItemsAmount(tradeVo, currentShopcartList);             dishServiceReq.mainTrade = mainTradeVo.getTrade();
            dishServiceReq.subTrade = tradeVo.getTrade();
        }
    }


    private void submitMofifyServiceDish(DishServiceV2Req dishServiceReq) {
        showLoadingProgressDialog();
        TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);
        if (tradeVo.isUnionMainTrade()) {             tradeOperates.modifyServiceUnionDish(dishServiceReq, submitModityServerResponseListner, TradeType.UNOIN_TABLE_MAIN);
        } else if (tradeVo.isUnionSubTrade()) {             tradeOperates.modifyServiceUnionDish(dishServiceReq, submitModityServerResponseListner, TradeType.UNOIN_TABLE_SUB);
        } else {             tradeOperates.modifyServiceDishV2(dishServiceReq, submitModityServerResponseListner);
        }
    }


    public void onEventMainThread(ActionSaveDishService event) {
        modifyServiceDish();
    }

    private class TradeItemOperationDataChangeObserver implements DatabaseHelper.DataChangeObserver {

        @Override
        public void onChange(Collection<Uri> uris) {
            if (uris.contains(DBHelperManager.getUri(TradeItemOperation.class))) {
                if (tradeVo != null && tradeVo.getTrade() != null) {

                }
            }
        }

    }

    public DinnerShopCartAdapter getSelectedDishAdapter() {
        return selectedDishAdapter;
    }

    public void setSelectedDishAdapter(DinnerShopCartAdapter selectedDishAdapter) {
        this.selectedDishAdapter = selectedDishAdapter;
    }

    public static void setMoveDishDragMode(boolean moveDishDrag) {
        isMoveDishDragMode = moveDishDrag;
    }

    public static boolean isMoveDishDragMode() {
        return isMoveDishDragMode;
    }


    public void enableSlideDishItem() {
                if (TableInfoFragment.isEnableServing() && isInDesk && !selectedDishAdapter.isDishCheckMode() && enableSlide) {
            orderDishListView.enableSlide(true);
        } else {
            orderDishListView.enableSlide(false);
        }
    }


    private void refreshIssueTime() {
        if (issueTimeRefreshtimer == null) {
            issueTimeRefreshtimer = new Timer();
        }

        issueTimeRefreshtimer.schedule(new TimerTask() {
            @Override
            public void run() {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TableInfoFragment tableInfoFragment = (TableInfoFragment) getFragmentManager().findFragmentByTag(DinnerTableConstant.CONTROL_FRAGMENT_TAG);
                            boolean isShowIssueTime = SharedPreferenceUtil.getSpUtil().getBoolean(TableFragment.SHOW_ISSUE_TIME_KEY, false);
                            if (tableInfoFragment != null && tableInfoFragment.getZoneView().ismShowingControl() && isShowIssueTime) {
                                selectedDishAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                });

            }
        }, 60 * 1000, 60 * 1000);
    }

    private void choosedOperate(DishDataItem dishDataItem) {
        if (dishDataItem.isCategory()) {
            dealCategory(dishDataItem);
        } else {
            moveChoosedOperate(dishDataItem);
        }
    }

    private void dealCategory(DishDataItem dishDataItem) {
        if (dishDataItem.getCheckStatus() == DishCheckStatus.CHECKED) {
            dishDataItem.setCheckStatus(DishCheckStatus.NOT_CHECK);
        } else {
            dishDataItem.setCheckStatus(DishCheckStatus.CHECKED);
        }
        selectedDishAdapter.doSelectedByType(dishDataItem);
        chnageCheckAllText();
        selectedDishAdapter.notifyDataSetChanged();
    }


    private void moveChoosedOperate(DishDataItem dishDataItem) {


        if (dishDataItem.getCheckStatus() == DishCheckStatus.NOT_CHECK) {
            dishDataItem.setCheckStatus(DishCheckStatus.CHECKED);

        } else if (dishDataItem.getCheckStatus() == DishCheckStatus.CHECKED) {
            dishDataItem.setCheckStatus(DishCheckStatus.NOT_CHECK);

        } else if (dishDataItem.getCheckStatus() == DishCheckStatus.INVALIATE_CHECK) {
            return;
        }
        if (getFragmentManager() == null) {
            return;
        }
        chnageCheckAllText();
        selectedDishAdapter.changeCateSelecteCount(dishDataItem);
        selectedDishAdapter.notifyDataSetChanged();
    }


    private void chnageCheckAllText() {
        EventChoice eventChoice = new EventChoice();
        eventChoice.setCount(getString(R.string.dinner_orderdish_dishcheck_number, String.valueOf(selectedDishAdapter.getCheckedNumber())));
        eventChoice.setCheckAll(selectedDishAdapter.isCheckedAll());
        EventBus.getDefault().post(eventChoice);
    }

    void chooseItemDishOperate(DishDataItem dishDataItem) {
        if (dishDataItem.isCategory()) {
            dealCategory(dishDataItem);
            changeTableCheck(true);
            return;
        }


        if (dishDataItem.getCheckStatus() == DishCheckStatus.NOT_CHECK) {
            dishDataItem.setCheckStatus(DishCheckStatus.CHECKED);

        } else if (dishDataItem.getCheckStatus() == DishCheckStatus.CHECKED) {
            dishDataItem.setCheckStatus(DishCheckStatus.NOT_CHECK);
                                } else if (dishDataItem.getCheckStatus() == DishCheckStatus.INVALIATE_CHECK) {
            return;
        }
        changeTableCheck(true);
        selectedDishAdapter.changeCateSelecteCount(dishDataItem);
        selectedDishAdapter.notifyDataSetChanged();
        return;
    }


    private void changeTableCheck(boolean isChecked) {
        if (getFragmentManager() == null) {
            return;
        }
                TableInfoFragment tableInfoFragment = (TableInfoFragment) getFragmentManager().findFragmentByTag(DinnerTableConstant.CONTROL_FRAGMENT_TAG);
        tableInfoFragment.initialDishAllCheckBar(true);
    }

    public void goToPosition(int position) {
        if (orderDishListView == null) {
            return;
        }
        if (orderDishListView.getChildCount() > 0) {
            orderDishListView.setSelection(position);
        }
    }




    public void removeTradeItemOperations(Map<IShopcartItemBase, PrintOperationOpType> map) {
        if (map == null || map.size() <= 0) {
            return;
        }

        for (Entry<IShopcartItemBase, PrintOperationOpType> entry : map.entrySet()) {
            IShopcartItemBase item = entry.getKey();
            List<TradeItemOperation> tmp = mUnOpItemOperation.get(item.getUuid());
            if (tmp != null)
                item.setTradeItemOperations(tmp);
        }
    }


    public boolean removeSelectedTradeItemOperations(List<DishDataItem> dishDataItems, PrintOperationOpType opType) {
        Map<IShopcartItemBase, PrintOperationOpType> map = new HashMap<>();
        for (DishDataItem item : dishDataItems) {
            if ((item.getType() == ItemType.WEST_CHILD || item.getType() == ItemType.CHILD) && item.getBase() != null) {
                map.put(item.getBase(), opType);
            } else if (item.getType() == ItemType.SINGLE && item.getItem() != null) {
                map.put(item.getItem(), opType);
            } else if (item.getType() == ItemType.COMBO && item.getItem() != null) {
                IShopcartItem iShopcartItem = item.getItem();
                map.put(iShopcartItem, opType);
                List<? extends ISetmealShopcartItem> iSetmealShopcartItems = iShopcartItem.getSetmealItems();
                for (ISetmealShopcartItem iSetmealShopcartItem : iSetmealShopcartItems) {
                    if (iSetmealShopcartItem != null && !map.containsKey(iSetmealShopcartItem))
                        map.put(iSetmealShopcartItem, opType);
                }
            }
        }
        if (!map.isEmpty()) {
            removeTradeItemOperations(map);
            return true;
        } else {
            return false;
        }
    }


    public boolean removeUnCheckedWakeUpOpt() {
        for (DishDataItem item : selectedDishAdapter.getAllData()) {
            if ((item.getType() != ItemType.SINGLE && item.getType() != ItemType.COMBO) || item.getItem() == null) {
                continue;
            }
            if (item.getCheckStatus() == DishCheckStatus.NOT_CHECK) {
                if (item.getType() == ItemType.SINGLE)
                    item.getItem().removeOperation(PrintOperationOpType.WAKE_UP);
                else if (item.getType() == ItemType.COMBO) {
                    IShopcartItem shopcartItem = item.getItem();
                    shopcartItem.removeOperation(PrintOperationOpType.WAKE_UP);
                    List<? extends ISetmealShopcartItem> iSetmealShopcartItems = shopcartItem.getSetmealItems();
                    for (ISetmealShopcartItem iSetmealShopcartItem : iSetmealShopcartItems) {
                        iSetmealShopcartItem.removeOperation(PrintOperationOpType.WAKE_UP);
                    }
                }
            }
        }
        return true;
    }
}
