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
    //购物车界面所处页面
    public static final String DISHSHOPCART_PAGE = "shopcart_page";
    public static final int PAGE_TABLE = 1;//桌台页
    public static final int PAGE_DISH = 2;//点单页
    public static final int PAGE_CASH = 3;//结算页
    public static final int PAGE_BOOKING = 4;//预定页
    public static final int PAGE_GROUP = 5;//团餐页

    @ViewById(R.id.lv_selected_dish)
    SlideListView orderDishListView;//

    @ViewById(R.id.haveNoDishLayout)
    RelativeLayout haveNoDishLayout;

    @ViewById(R.id.haveNoDishImage)
    ImageView haveNoDishImage;

    @ViewById(R.id.goods_total_number)
    TextView allDishCountTV;//商品总数

    @ViewById(R.id.tv_trade_extra_label)
    TextView tvTradeExtraLabel;

    /*@ViewById(R.id.tv_tarde_extra_amount)
    TextView extraAmountTV;//附加费

    @ViewById(R.id.tv_tarde_service_amount)
    TextView serviceAmountTV;//服务费

    @ViewById(R.id.tv_tarde_tax_amount)
    TextView taxAmountTV;//消费税

    @ViewById(R.id.tv_trade_privilege_amount)
    TextView allPrivilegeAmountTV;//总优惠*/

    /*@ViewById(R.id.dinner_prievlege_layout)
    ViewGroup privilegeRL;*/
    @ViewById(R.id.trade_amount_line)
    View lineView;
    @ViewById(R.id.tv_tarde_amount)
    TextView tradeAmountTV;//应收金额
    protected DinnerShopCartAdapter selectedDishAdapter;// 适配器

    private int mCurrentPosition;// 当前定位

    private TradePrivilege bartchPrivilege;

    protected DinnerShoppingCart mShoppingCart;

    private TradeVo tradeVo;

    //订单中的催菜数目
    private long tradeItemOperationCount = 0;

    // 是否是桌台的显示，true：是
    private boolean isInDesk = false;

    private static boolean isMoveDishDragMode = false;//是否是移菜拖动模式


    // 是否显示退菜、改菜后无效的数据
    private boolean isShowDelete = true;

    private ChangePageListener mChangePageListener;

    private boolean isDishServingModify = false;// 是否是上菜修改状态

    private DatabaseHelper.DataChangeObserver observer;

    private Timer issueTimeRefreshtimer;//出单时间定时器

    private Handler uiHandler = new Handler();

    private int mCurrentPage = PAGE_TABLE;//默认桌台页

    private boolean enableSlide = true;//是否允许滑动
    //是否是配菜
    private boolean isSlideMode = false;
    //是否正在进行批量操作
    public boolean isBatchOperating = false;
    //西餐
    private boolean isWest = false;

    private BusinessType mBusinessType = BusinessType.DINNER;
    //选择操作
    private Map<IShopcartItem, PrintOperationOpType> mOpItemOperation = new HashMap<>();
    //初始菜品缓存
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
        //  DatabaseHelper.Registry.register(observer);//modify 20170209
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
        // DatabaseHelper.Registry.unregister(observer);//modify 20170209
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
                selectedDishAdapter.setInDesk(isInDesk);//设置是否在桌台
                break;
            case GROUP:
                selectedDishAdapter = new GroupShopCartAdapter(getActivity());
                break;
            default:
                //预定和正餐使用此购物车adapter
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
                selectedDishAdapter.setInDesk(isInDesk);//设置是否在桌台
                break;
        }

        selectedDishAdapter.setSlideDish(isSlideMode);
        selectedDishAdapter.setParentView(orderDishListView);
        orderDishListView.setAdapter(selectedDishAdapter);
//        if (isReadOnly)
//            selectedDishAdapter.isShowFreeDiscount(false);
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
        DatabaseHelper.Registry.register(observer);//add 20170209
        Log.i(TAG, "onResume: DatabaseHelper.Registry.register");
//        updateOrderDishList(mShoppingCart.getShoppingCartDish(), mShoppingCart.getOrder());
    }

    @Override
    public void onPause() {
        super.onPause();
        DatabaseHelper.Registry.unregister(observer);//add  20170209
        Log.i(TAG, "onPause: DatabaseHelper.Registry.unregister");
    }

    public void updateOrderDishList(List<IShopcartItem> list, TradeVo tradeVo, ShopcartItem shopcartItem, ShoppingCartListener.OperationStatus operationStatus) {
        updateOrderDishList(list, tradeVo);
        if ((mBusinessType == BusinessType.DINNER || mBusinessType == BusinessType.GROUP || mBusinessType == BusinessType.BUFFET)
//                && isInDesk
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

    /**
     * 获取选择的菜的uuid
     *
     * @param shopcartItem
     * @return
     */
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

    /**
     * 购物车有变更时更新界面
     *
     * @Title: updateView
     * @Description: TODO
     * @Param @param list
     * @Param @param tradeVo TODO
     * @Param isSlide 是否允许滑动
     * @Param isGroupCheckMode  是否是分组显示
     * @Return void 返回类型
     */
    public void updateOrderDishList(List<IShopcartItem> list, TradeVo tradeVo, boolean isSlide, boolean isGroupCheckMode, boolean isInit) {
        this.tradeVo = tradeVo;
        currentShopcartList = new ArrayList<>();
        if (selectedDishAdapter == null) {
            return;
        }
        //避免联台主单子单切换后slidePositionList未变
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
        //等叫、起菜、催菜、批量备注都采用按中类分类显示,复制移菜不采用分组
        if (isGroupCheckMode || selectedDishAdapter.isDishCheckMode() && !isMoveDishDragMode) {
            selectedDishAdapter.updateGroupSelectData(currentShopcartList, tradeVo);
        } else {
            selectedDishAdapter.updateData(currentShopcartList, tradeVo, true);
        }
        orderDishListView.setSelection(this.mCurrentPosition);
        // 滑菜开关打开并且在桌台页时
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
        //刷新统计金额
        updateAllTradeInfo();
        // 刷新商品总数
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

    //隐藏统计金额
    private void hideAllTradeInfo() {
        tvTradeExtraLabel.setVisibility(View.GONE);
        lineView.setVisibility(View.GONE);
        tradeAmountTV.setVisibility(View.GONE);
        EventBus.getDefault().post(new EventTradeAmount(""));//容错，在某些特殊情况下，结账按钮上的金额与订单tab上的金额对不上，统一使用"结账文案"
    }

    //显示统计金额
    private void showAllTradeInfo() {
        double tradeExtra = 0;
        double tradeAmount = 0;
        double tradePrivilegeAmount = 0;
        if (tradeVo != null) {
            //统计附加费
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
                //获取应收
                if (trade.getTradeAmount() != null) {
                    tradeAmount = trade.getTradeAmount().doubleValue();
                }
                if (trade.getPrivilegeAmount() != null) {
                    tradePrivilegeAmount = trade.getPrivilegeAmount().doubleValue();
                }
            }
            /*extraAmountTV.setText(getString(R.string.extra_charge) + " " + Utils.formatPrice(tradeExtra));
            serviceAmountTV.setText(getString(R.string.service_charge) + " " + Utils.formatPrice(tradeVo.getServiceAmount()));
            taxAmountTV.setText(getString(R.string.tax_charge) + " " + Utils.formatPrice(tradeVo.getTradeTaxAmount()));
            allPrivilegeAmountTV.setText(getString(R.string.privilege) + " " + Utils.formatPrice(tradePrivilegeAmount));*/
            tvTradeExtraLabel.setText(getString(R.string.trade_extra_label,
                    Utils.formatPrice(tradeExtra),
                    Utils.formatPrice(tradeVo.getServiceAmount()),
                    Utils.formatPrice(tradeVo.getTradeTaxAmount()),
                    Utils.formatPrice(tradePrivilegeAmount)));
            tradeAmountTV.setText(getString(R.string.receivable) + " " + Utils.formatPrice(tradeAmount));
            EventBus.getDefault().post(new EventTradeAmount(Utils.formatPrice(tradeAmount), tradeVo));
            tvTradeExtraLabel.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);
            //extraAmountTV.setVisibility(View.VISIBLE);
            //allPrivilegeAmountTV.setVisibility(View.VISIBLE);
            tradeAmountTV.setVisibility(View.GONE);
        }
    }

    //更新统计信息
    private void updateAllTradeInfo() {
        switch (mCurrentPage) {
            case PAGE_TABLE://桌台页
                if (isMoveDishDragMode || getFragmentManager() == null) {//编辑模式不展示
                    hideAllTradeInfo();
                    return;
                }

                //加菜模式需要隐藏金额显示
                TableInfoFragment tableInfoFragment = (TableInfoFragment) getFragmentManager()
                        .findFragmentByTag(DinnerTableConstant.CONTROL_FRAGMENT_TAG);
                if (tableInfoFragment.isAddDishMode()) {
                    hideAllTradeInfo();
                    return;
                }

                break;
            case PAGE_DISH://点单页
            case PAGE_CASH://结算页
            case PAGE_GROUP:
            case PAGE_BOOKING:
                hideAllTradeInfo();
                return;
            default:
                break;
        }
        if (selectedDishAdapter != null && selectedDishAdapter.getCount() > 0) {
           /* if(mCurrentPage==PAGE_TABLE){

            }else{

            }*/
            showAllTradeInfo();

        } else {
            hideAllTradeInfo();
        }
    }

    //更新商品总数
    private void updateAllDishCount() {
        if (getActivity() == null) {
            return;
        }
        if (selectedDishAdapter == null || isMoveDishDragMode)//编辑模式不展示商品总数
        {
            allDishCountTV.setVisibility(View.GONE);
            return;
        }
        selectedDishAdapter.updateCountView(getActivity(), allDishCountTV);
    }


    public void setDishCheckMode(boolean isCheckMode) {
        selectedDishAdapter.setDishCheckMode(isCheckMode);
    }


    private void itemClicked(int position) {
        //批量操作时，不允许点击其他菜品
        if (isBatchOperating) {
            ToastUtil.showShortToast(R.string.dinner_finish_batch_operate_first);
            return;
        }

        mCurrentPosition = orderDishListView.getSelectedItemPosition();
        DishDataItem dishDataItem = selectedDishAdapter.getItem(position);
        if (dishDataItem == null || (dishDataItem.getBase() == null) && !dishDataItem.isCategory()) {
            return;
        }

        //增加菜品操作check
        if (selectedDishAdapter.isDishCheckMode()) {
            //只有单菜和套餐才可以选择操作
            if (dishDataItem.getType() != ItemType.SINGLE && dishDataItem.getType() != ItemType.COMBO && !dishDataItem.isCategory()) {
                return;
            }

            //桌台信息页中选择菜品
            if (isInDesk) {
                if (isMoveDishDragMode) {//移菜或者复制菜品
                    if (dishDataItem.getCheckStatus() == DishCheckStatus.NOT_CHECK) {
                        dishDataItem.setCheckStatus(DishCheckStatus.CHECKED);
                    } else if (dishDataItem.getCheckStatus() == DishCheckStatus.CHECKED) {
                        dishDataItem.setCheckStatus(DishCheckStatus.NOT_CHECK);
                    } else if (dishDataItem.getCheckStatus() == DishCheckStatus.INVALIATE_CHECK) {
                        return;
                    }
                    //刷新拖动订单显示
                    TableInfoFragment tableInfoFragment = (TableInfoFragment) getFragmentManager().findFragmentByTag(DinnerTableConstant.CONTROL_FRAGMENT_TAG);
                    tableInfoFragment.refreshMovedishDragView();
                    tableInfoFragment.initialDishAllCheckBar(true);
                    selectedDishAdapter.notifyDataSetChanged();
                    return;
                } else {//崔菜，起菜
                    chooseItemDishOperate(dishDataItem);
                    return;
                }

            }

            choosedOperate(dishDataItem);
            return;
        }
        //桌台信息页不允许点击
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

        // 已下单的子菜,且不是西餐，不允许操作
//        if (dishDataItem.getBase() instanceof ReadonlySetmealShopcartItem&&!isWest) {
//            return;
//        }
        // 未打印出单的就被删除的菜，不能做任何操作
        if (dishDataItem.getBase() == null || TextUtils.isEmpty(dishDataItem.getBase().getBatchNo())
                && dishDataItem.getBase().getStatusFlag() == StatusFlag.INVALID) {
            return;
        }
        // 主单中的子单菜品不允许操作
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
                || event.getStatus() == BottomBarStatus.NEW_TRADE_OPERATE) {// 取消上菜选择
            if (event.isUpdateShop()) {
                selectedDishAdapter.cancelServingStatusOperate();
            }
            setDishServingModify(false);
        }
    }

//    /**
//     * @param dishServiceReq

//     * @Date 2015年9月22日
//     * @Description: 提交菜品状态
//     * @Return void
//     */
//    void sumitModifyServiceDish(DishServiceV2Req dishServiceReq) {
//        showLoadingProgressDialog();
//        TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);
//        tradeOperates.modifyServiceDishV2(dishServiceReq, submitModityServerResponseListner);
//    }

    private CalmResponseListener<ResponseObject<DishServiceV2Resp>> submitModityServerResponseListner = new CalmResponseListener<ResponseObject<DishServiceV2Resp>>(UserActionEvent.DINNER_SCRATCH_DISH) {

        @Override
        public void onSuccess(ResponseObject<DishServiceV2Resp> data) {
            dismissLoadingProgressDialog();
            if (ResponseObject.isOk(data)) {
                ToastUtil.showLongToast(data.getMessage());
                //setDishServingModify(false);
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

    /**
     * @Date 2015年11月24日
     * @Description: 获取菜品状态后提交
     * @Return void
     */
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
//                                long serverUpdateTime = 0L;
                                if (tradeVo.isUnionMainTrade()) { // 联台主单
                                    dishServiceReq.addUionMainItemServings(shopcartItem.getId(), dishServing.value(), shopcartItem.getServerUpdateTime());
                                    shopcartItemList.add(item.getItem()); //
                                } else if (tradeVo.isUnionSubTrade()) { // 联台子单
                                    if (shopcartItem.getShopcartItemType() == ShopcartItemType.SUBBATCH || shopcartItem.getShopcartItemType() == ShopcartItemType.SUBBATCHMODIFY) { // 子单修改划主单菜时  传递batch_id
                                        ShopcartItemUtils.splitBatchItem((ReadonlyShopcartItem) shopcartItem); // 子单修改主单菜时，需要先调用拆菜
                                        dishServiceReq.addUionSubItemServings(shopcartItem.getBatchId(), dishServing.value(), shopcartItem.getServerUpdateTime(), DishServiceV2Req.Type.MAINBATCH);
                                    } else { // 子单修改划子菜时  传递当前菜id
                                        dishServiceReq.addUionSubItemServings(shopcartItem.getId(), dishServing.value(), shopcartItem.getServerUpdateTime(), DishServiceV2Req.Type.SUB);
                                    }
                                    shopcartItemList.add(item.getItem());
                                } else { // 普通正餐单
//                                    KdsTradeDal kdsTradeDal = OperatesFactory.create(KdsTradeDal.class);
//                                    serverUpdateTime = kdsTradeDal.getTradeItem(shopcartItem.getId());
                                    dishServiceReq.addTradeItem(shopcartItem.getId(), dishServing.value(), OperatesFactory.create(KdsTradeDal.class).getTradeItem(shopcartItem.getId()));
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

    /**
     * 联台tradevo上行数据构建 ,计算主单子单金额
     *
     * @param dishServiceReq
     */
    private void buildDishServiceReq(DishServiceV2Req dishServiceReq) {
        if (tradeVo.isUnionMainTrade()) { // 联台主单
//            MathShopcartItemTool.mathMainShopcartItemsAmount(tradeVo , mShoppingCart.getShoppingCartDish());
            dishServiceReq.tradeId = tradeVo.getTrade().getId();
        } else if (tradeVo.isUnionSubTrade()) { // 联台子单，如果修改子单，ifconfig
            // 需要重新计算主单和子单
            TradeVo mainTradeVo = null;
            if (mShoppingCart != null && mShoppingCart.getMainTradeInfo() != null && mShoppingCart.getMainTradeInfo().getTradeVo() != null) {
                mainTradeVo = mShoppingCart.getMainTradeInfo().getTradeVo();
            }
            MathShopcartItemTool.mathMainShopcartItemsAmount(mainTradeVo, mShoppingCart.getMainTradeInfo().getItems());
            MathShopcartItemTool.mathSubShopcartItemsAmount(tradeVo, currentShopcartList); // 子单
            dishServiceReq.mainTrade = mainTradeVo.getTrade();
            dishServiceReq.subTrade = tradeVo.getTrade();
        }
    }

    /**
     * 联台划菜/撤销划菜，保存菜品状态
     *
     * @param dishServiceReq
     */
    private void submitMofifyServiceDish(DishServiceV2Req dishServiceReq) {
        showLoadingProgressDialog();
        TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);
        if (tradeVo.isUnionMainTrade()) { // 联台主单
            tradeOperates.modifyServiceUnionDish(dishServiceReq, submitModityServerResponseListner, TradeType.UNOIN_TABLE_MAIN);
        } else if (tradeVo.isUnionSubTrade()) { // 联台子单
            tradeOperates.modifyServiceUnionDish(dishServiceReq, submitModityServerResponseListner, TradeType.UNOIN_TABLE_SUB);
        } else { // 普通正餐单
            tradeOperates.modifyServiceDishV2(dishServiceReq, submitModityServerResponseListner);
        }
    }

    /**
     * @param event
     * @Date 2015年11月24日
     * @Description: 保存滑菜状态
     * @Return void
     */
    public void onEventMainThread(ActionSaveDishService event) {
        modifyServiceDish();
    }

    private class TradeItemOperationDataChangeObserver implements DatabaseHelper.DataChangeObserver {

        @Override
        public void onChange(Collection<Uri> uris) {
            if (uris.contains(DBHelperManager.getUri(TradeItemOperation.class))) {
                if (tradeVo != null && tradeVo.getTrade() != null) {
                    //PrintOperationDal dal = OperatesFactory.create(PrintOperationDal.class);
                    /*try {
                        List<TradeItemOperation> tradeItemOperations = dal.findTradeItemOperation(tradeVo.getTrade().getUuid());
                        if (tradeItemOperationCount != tradeItemOperations.size()) {
                            if (!isInDesk) {//点菜界面
                                Map<Long, List<TradeItemOperation>> tradeItemOperationIdMap = new HashMap<Long, List<TradeItemOperation>>();
                                for (TradeItemOperation tradeItemOperation : tradeItemOperations) {
                                    List<TradeItemOperation> tios = tradeItemOperationIdMap.get(tradeItemOperation.getTradeItemId());
                                    if (tios == null) {
                                        tios = new ArrayList<TradeItemOperation>();
                                        tradeItemOperationIdMap.put(tradeItemOperation.getTradeItemId(), tios);
                                    }
                                    tios.add(tradeItemOperation);
                                }

                                //将id转换成uuid
                                final Map<String, List<TradeItemOperation>> tradeItemOperationUuidMap = new HashMap<String, List<TradeItemOperation>>();
                                TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
                                for (Entry<Long, List<TradeItemOperation>> entry : tradeItemOperationIdMap.entrySet()) {
                                    TradeItem tradeItem = tradeDal.findTradeItem(entry.getKey());
                                    tradeItemOperationUuidMap.put(tradeItem.getUuid(), entry.getValue());
                                }
                                if (!tradeItemOperationUuidMap.isEmpty()) {
                                    getActivity().runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            mShoppingCart.refreshTradeItemOperations(tradeItemOperationUuidMap);
                                        }
                                    });
                                }
                            }
                        }
                        tradeItemOperationCount = tradeItemOperations.size();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }*/
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

    /**
     * @Date 2016/6/16
     * @Description:是否允许菜品滑动
     * @Param
     * @Return
     */
    public void enableSlideDishItem() {
        // 滑菜开关打开并且在桌台页时
        if (TableInfoFragment.isEnableServing() && isInDesk && !selectedDishAdapter.isDishCheckMode() && enableSlide) {
            orderDishListView.enableSlide(true);
        } else {
            orderDishListView.enableSlide(false);
        }
    }

    /**
     * @Date 2016/9/2
     * @Description:刷新出单时间
     * @Param
     * @Return
     */
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

    /**
     * 选中时显示标签
     */
    private void moveChoosedOperate(DishDataItem dishDataItem) {
        //Map<IShopcartItem, PrintOperationOpType> map = new HashMap<>();
        /*
        PrintOperationOpType type = getSelectedDishAdapter().getOpType();
        if (type == PrintOperationOpType.WAKE_UP) {
            mOpItemOperation.put(dishDataItem.getItem(), PrintOperationOpType.WAKE_UP);
        } else if (type == PrintOperationOpType.RISE_DISH) {
            mOpItemOperation.put(dishDataItem.getItem(), PrintOperationOpType.RISE_DISH);
        } else if (type == PrintOperationOpType.REMIND_DISH) {
            mOpItemOperation.put(dishDataItem.getItem(), PrintOperationOpType.REMIND_DISH);
        } else if(type == PrintOperationOpType.WAKE_UP_CANCEL){
            mOpItemOperation.put(dishDataItem.getItem(), PrintOperationOpType.WAKE_UP_CANCEL);
        } else if(type == PrintOperationOpType.RISE_DISH_CANCEL){
            mOpItemOperation.put(dishDataItem.getItem(), PrintOperationOpType.RISE_DISH_CANCEL);
        }
        */

        if (dishDataItem.getCheckStatus() == DishCheckStatus.NOT_CHECK) {
            dishDataItem.setCheckStatus(DishCheckStatus.CHECKED);
            //DinnerShoppingCart.getInstance().addTradeItemOperations(map);//添加标签

        } else if (dishDataItem.getCheckStatus() == DishCheckStatus.CHECKED) {
            dishDataItem.setCheckStatus(DishCheckStatus.NOT_CHECK);
            //DinnerShoppingCart.getInstance().removeTradeItemOperations(map);//移除标签
            //removeTradeItemOperations(map);

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

    /**
     * 点选一项后改变全选按钮的选中状态
     */
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
        //选中时显示标签
        //Map<IShopcartItem, PrintOperationOpType> map = new HashMap<>();
        /*
        PrintOperationOpType type = getSelectedDishAdapter().getOpType();
        if (type == PrintOperationOpType.WAKE_UP) {
            mOpItemOperation.put(dishDataItem.getItem(), PrintOperationOpType.WAKE_UP);
        } else if (type == PrintOperationOpType.RISE_DISH) {
            mOpItemOperation.put(dishDataItem.getItem(), PrintOperationOpType.RISE_DISH);
        } else if (type == PrintOperationOpType.REMIND_DISH) {
            mOpItemOperation.put(dishDataItem.getItem(), PrintOperationOpType.REMIND_DISH);
        } else if (type == PrintOperationOpType.BATCH_OPERATION) {
            mOpItemOperation.put(dishDataItem.getItem(), PrintOperationOpType.BATCH_OPERATION);
        }
        */

        if (dishDataItem.getCheckStatus() == DishCheckStatus.NOT_CHECK) {
            dishDataItem.setCheckStatus(DishCheckStatus.CHECKED);
            //DinnerShoppingCart.getInstance().addTradeItemOperations(map);//添加标签

        } else if (dishDataItem.getCheckStatus() == DishCheckStatus.CHECKED) {
            dishDataItem.setCheckStatus(DishCheckStatus.NOT_CHECK);
            //DinnerShoppingCart.getInstance().removeTradeItemOperations(map);//移除标签
            //removeTradeItemOperations(map);
        } else if (dishDataItem.getCheckStatus() == DishCheckStatus.INVALIATE_CHECK) {
            return;
        }
        changeTableCheck(true);
        selectedDishAdapter.changeCateSelecteCount(dishDataItem);
        selectedDishAdapter.notifyDataSetChanged();
        return;
    }

    /**
     * 改变桌台上起菜时全选按钮
     *
     * @param isChecked
     */
    private void changeTableCheck(boolean isChecked) {
        if (getFragmentManager() == null) {
            return;
        }
        //刷新拖动订单显示
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

    /*
    public void addTradeItemOperations(){
        DinnerShoppingCart.getInstance().addTradeItemOperations(mOpItemOperation);
    }
    */

    /**
     * 恢复菜品
     *
     * @param map
     */
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

    /**
     * 批量恢复菜品
     *
     * @param dishDataItems
     * @param opType
     * @return
     */
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
