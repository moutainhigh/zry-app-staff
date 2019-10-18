package com.zhongmei.bty.dinner.orderdish;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.beauty.utils.BeautyOrderConstants;
import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealVo;
import com.zhongmei.bty.router.RouteIntent;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryInfo;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryCacheUtil;
import com.zhongmei.bty.basemodule.log.RLog;
import com.zhongmei.bty.basemodule.orderdish.bean.DishAndStandards;
import com.zhongmei.bty.basemodule.orderdish.bean.DishInfo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishSetmeal;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.orderdish.entity.DishUnitDictionary;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.bty.basemodule.orderdish.event.EventDishChangedNotice;
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.listerner.ModifyShoppingCartListener;
import com.zhongmei.bty.basemodule.shoppingcart.utils.CreateItemTool;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListener;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListerTag;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.ui.base.MobclickAgentFragment;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.util.UserActionCode;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment.CommonDialogFragmentBuilder;
import com.zhongmei.bty.data.operates.DishDal;
import com.zhongmei.bty.dinner.Listener.BrandTypeListener;
import com.zhongmei.bty.dinner.adapter.DinnerDishListPagerAdapter;
import com.zhongmei.bty.dinner.vo.LoadingFinish;
import com.zhongmei.bty.snack.orderdish.OrderDishClearStatusFragment;
import com.zhongmei.bty.snack.orderdish.OrderDishClearStatusFragment_;
import com.zhongmei.bty.snack.orderdish.adapter.OrderDishAdapter;
import com.zhongmei.bty.snack.orderdish.adapter.OrderDishListPagerAdapter;
import com.zhongmei.bty.snack.orderdish.view.OnCloseListener;
import com.zhongmei.bty.snack.orderdish.view.QuantityEditPopupWindow;
import com.zhongmei.bty.snack.orderdish.view.QuantityEditPopupWindow.DataChangeListener;
import com.zhongmei.yunfu.util.ValueEnums;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public abstract class DishHomePageFragment extends MobclickAgentFragment implements OnPageChangeListener, BrandTypeListener, OnClickListener {

    private final static String TAG = DishHomePageFragment.class.getSimpleName();
        private static final int MAX_INDEX_COUNT = 7;

    protected ChangePageListener mChangePageListener;

    protected DishManager mDishManager;

    protected OrderDishListPagerAdapter mAdapter;

    private DinnerShoppingCart mShoppingCart;

    private int mCurrentIndex = 0;

    private LoadDishTask mTask;

    private DishBrandType mSelectDishType;

        private QuantityEditPopupWindow mPopupWindow;

    private LoadingFinish mLoadingFinish;
        protected boolean isAllowAddTempDish = false;

    private TempDishFragment tempDishFragment = null;

        private String mCarteUuid = null;
    private boolean mIsSingle = false;

    private RelativeLayout mMainView;
    private LinearLayout mVBrandType;
    private View mVBrandBorder;
    private RelativeLayout mVSearchAndIndex;
    protected Button mBtClearDishListShow;
    protected Button mBtnScanCode;
    protected Button mBtnTempDish;
    private HorizontalScrollView mHsvDots;
    private LinearLayout mLlDots;
    private ViewPager mVpDishList;
    private ImageView mIvDishTypeEmpty;
    private View mViewShadow;

    private InventoryCacheUtil mInventoryCacheUtil;

    protected BusinessType businessTypeValue = BusinessType.BEAUTY;

    protected void assignViews(View view) {
        mMainView = (RelativeLayout) view.findViewById(R.id.mainView);
        mVBrandType = (LinearLayout) view.findViewById(R.id.v_brand_type);
        mVBrandBorder = view.findViewById(R.id.v_brand_border);
        mVSearchAndIndex = (RelativeLayout) view.findViewById(R.id.v_search_and_index);
        mBtClearDishListShow = (Button) view.findViewById(R.id.bt_clear_dish_list_show);
        mBtnScanCode = (Button) view.findViewById(R.id.btn_scan_code);
        mBtnTempDish = (Button) view.findViewById(R.id.btn_temp_dish);
        mHsvDots = (HorizontalScrollView) view.findViewById(R.id.hsv_dots);
        mLlDots = (LinearLayout) view.findViewById(R.id.ll_dots);
        mVpDishList = (ViewPager) view.findViewById(R.id.vp_dish_list);
        mIvDishTypeEmpty = (ImageView) view.findViewById(R.id.iv_dish_type_empty);
        mViewShadow = view.findViewById(R.id.view_shadow);
        mBtClearDishListShow.setOnClickListener(this);
        mBtnScanCode.setOnClickListener(this);
        mBtnTempDish.setOnClickListener(this);

         int busInteger = getActivity().getIntent().getIntExtra(BeautyOrderConstants.ORDER_BUSINESSTYPE, ValueEnums.toValue(BusinessType.BEAUTY));
        businessTypeValue=ValueEnums.toEnum(BusinessType.class,busInteger);
    }

    protected boolean isBuyServerBusiness(){
        return ValueEnums.equalsValue(businessTypeValue,BusinessType.CARD_TIME.value());
    }




    abstract public View getBrandTypeView();


    private ModifyShoppingCartListener mShoppingCartListener = new ShoppingCartListener() {
        public void addToShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, ShopcartItem mShopcartItem) {
            refreshPartVisibleGridViews(mShopcartItem);
        }

        public void updateDish(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
            refreshAllVisibleGridViews();
        }


        public void clearShoppingCart() {
            refreshAllVisibleGridViews();
        }


        public void removeShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo,
                                       IShopcartItemBase mShopcartItemBase) {
            refreshPartVisibleGridViews(mShopcartItemBase);
        }

        public void resetOrder(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
            refreshPartVisibleGridViews(listOrderDishshopVo);
        }

        public void exception(String message) {
            if (getActivity() == null || getActivity().isDestroyed()) {
                return;
            }
            new CommonDialogFragmentBuilder(MainApplication.getInstance()).title(getResources().getString(R.string.invalidLogin))
                    .iconType(R.drawable.commonmodule_dialog_icon_warning)
                    .negativeText(R.string.reLogin)
                    .negativeLisnter(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            RouteIntent.startLogin(getActivity(), Intent.FLAG_ACTIVITY_NEW_TASK, null);
                        }
                    })
                    .build()
                    .show(getActivity().getSupportFragmentManager(), TAG);
        }
    };


    private void refreshPartVisibleGridViews(IShopcartItemBase iShopcartItemBase) {
        if (mAdapter != null && iShopcartItemBase != null) {
            GridView gridView = (GridView) mVpDishList.findViewById(mCurrentIndex);
            mAdapter.updateItemView(gridView, iShopcartItemBase.getSkuUuid());

                        GridView prevGridView = (GridView) mVpDishList.findViewById(mCurrentIndex - 1);
            mAdapter.updateItemView(prevGridView, iShopcartItemBase.getSkuUuid());

                        GridView nextGridView = (GridView) mVpDishList.findViewById(mCurrentIndex + 1);
            mAdapter.updateItemView(nextGridView, iShopcartItemBase.getSkuUuid());
        }
    }


    private void refreshPartVisibleGridViews(List<IShopcartItem> iShopcartItems) {
        if (mAdapter != null) {
            List<String> dishUuids = new ArrayList<String>();
            if (Utils.isNotEmpty(iShopcartItems)) {
                for (IShopcartItem iShopcartItem : iShopcartItems) {
                    dishUuids.add(iShopcartItem.getSkuUuid());
                }
            }

            GridView gridView = (GridView) mVpDishList.findViewById(mCurrentIndex);
            mAdapter.updateItemView(gridView, dishUuids);

                        GridView prevGridView = (GridView) mVpDishList.findViewById(mCurrentIndex - 1);
            mAdapter.updateItemView(prevGridView, dishUuids);

                        GridView nextGridView = (GridView) mVpDishList.findViewById(mCurrentIndex + 1);
            mAdapter.updateItemView(nextGridView, dishUuids);
        }
    }


    private void refreshPartVisibleGridViews(Map<String, DishShop> dishShopMap) {
        if (mAdapter != null) {
            GridView gridView = (GridView) mVpDishList.findViewById(mCurrentIndex);
            mAdapter.updateItemView(gridView, dishShopMap);

                        GridView prevGridView = (GridView) mVpDishList.findViewById(mCurrentIndex - 1);
            mAdapter.updateItemView(prevGridView, dishShopMap);

                        GridView nextGridView = (GridView) mVpDishList.findViewById(mCurrentIndex + 1);
            mAdapter.updateItemView(nextGridView, dishShopMap);
        }
    }


    private void refreshAllVisibleGridViews() {
        GridView gridView = (GridView) mVpDishList.findViewById(mCurrentIndex);
        if (gridView != null && gridView.getAdapter() != null) {
            ((BaseAdapter) gridView.getAdapter()).notifyDataSetChanged();
        }

                GridView prevGridView = (GridView) mVpDishList.findViewById(mCurrentIndex - 1);
        if (prevGridView != null && prevGridView.getAdapter() != null) {
            ((BaseAdapter) prevGridView.getAdapter()).notifyDataSetChanged();
        }

                GridView nextGridView = (GridView) mVpDishList.findViewById(mCurrentIndex + 1);
        if (nextGridView != null && nextGridView.getAdapter() != null) {
            ((BaseAdapter) nextGridView.getAdapter()).notifyDataSetChanged();
        }
    }

    public void registerListener(ChangePageListener changePageListener) {
        this.mChangePageListener = changePageListener;
    }

    public void registerLoadingListener(LoadingFinish mLoadingFinish) {
        this.mLoadingFinish = mLoadingFinish;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
        mShoppingCart = DinnerShoppingCart.getInstance();
        mShoppingCart.registerListener(ShoppingCartListerTag.DINNER_DISH_HOME_PAGE, mShoppingCartListener);
        mDishManager = new DishManager();
        mDishManager.registerObserver();
        mInventoryCacheUtil = InventoryCacheUtil.getInstance();
        mInventoryCacheUtil.registerListener(ChangePageListener.PAGE_ORDER_DISH, mInventoryListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = loadView(inflater);
        assignViews(view);
        return view;
    }

    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_dish_home_page, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onDestroy() {
        unregisterEventBus();
        if (mDishManager != null) {
            mDishManager.unregisterObserver();
        }
        mShoppingCart.unRegisterListenerByTag(ShoppingCartListerTag.DINNER_DISH_HOME_PAGE);
        mInventoryCacheUtil.unRegisterListener(ChangePageListener.PAGE_ORDER_DISH);
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            mShoppingCart.unRegisterListenerByTag(ShoppingCartListerTag.DINNER_DISH_HOME_PAGE);
            mInventoryCacheUtil.unRegisterListener(ChangePageListener.PAGE_ORDER_DISH);
        } else {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
            mShoppingCart.registerListener(ShoppingCartListerTag.DINNER_DISH_HOME_PAGE, mShoppingCartListener);
            mInventoryCacheUtil.registerListener(ChangePageListener.PAGE_ORDER_DISH, mInventoryListener);
        }
    }


    public void onTouch(final DishVo dishVo) {
        if (mAdapter != null) {
            if (dishVo.isClear()) {
                return;
            }

            dealDishVo(dishVo);
        }
    }

    private void dealDishVo(DishVo dishVo) {
                boolean isUnionMainTrade = mShoppingCart.getOrder().isUnionMainTrade();
                if (dishVo.isContainProperties() && dishVo.getDishShop().getClearStatus() == ClearStatus.CLEAR) {
            DishShop dishShop;
            if (isUnionMainTrade) {
                                dishShop = dishVo.getLeastUnweighResidueFromOtherDishs();
            } else {
                dishShop = dishVo.getLeastResidueFromOtherDishs();
            }

            addOtherStandardSingleDish(dishVo, dishShop);
        } else {
            if (dishVo.getDishShop().getSaleType() == SaleType.WEIGHING) {
                if (isUnionMainTrade) {
                                        DishShop dishShop = dishVo.getLeastUnweighResidueFromOtherDishs();
                    if (dishShop != null) {
                        addOtherStandardSingleDish(dishVo, dishShop);
                    } else {
                        ToastUtil.showShortToast(R.string.union_main_trade_not_allow_weighing_dish);
                    }
                } else {
                    addSingleDish(dishVo);
                }
            } else {
                if (dishVo.isCombo()) {                    addUnweighCombo(dishVo);
                } else if(dishVo.isServerComBoPart()){
                    addServerComboPart(dishVo);
                }else {                    addSingleDish(dishVo);
                }
            }
        }
    }


    private void addSingleDish(DishVo dishVo) {
        if (dishVo.getDishShop().getSaleType() == SaleType.WEIGHING) {
            ShopcartItem shopcartItem = CreateItemTool.createShopcartItem(dishVo);
            QuantityEditPopupWindow popupWindow = new QuantityEditPopupWindow(getActivity(), shopcartItem, dishVo.isContainProperties(), dataChangeListener);
            popupWindow.setDinner(true);
            popupWindow.setLeftZoneWidth(getQuanPopLeftWidth());
            popupWindow.showAtLocation(mVpDishList, Gravity.NO_GRAVITY, 0, 0);
        } else {
            final ShopcartItem shopcartItem = CreateItemTool.createShopcartItem(dishVo);
            mShoppingCart.addDishToShoppingCart(shopcartItem, true);
        }
    }


    protected int getQuanPopLeftWidth() {
        return -1;
    }


    private void addUnweighCombo(DishVo dishVo) {
        if (mChangePageListener != null) {
                        ShopcartItem shopcartItem = CreateItemTool.createShopcartItem(dishVo);
            shopcartItem.setSetmealItems(new ArrayList<SetmealShopcartItem>());
            mShoppingCart.addDishToShoppingCart(shopcartItem, false);

            Bundle bundle = new Bundle();
            bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, shopcartItem.getUuid());
            bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST);
            bundle.putBoolean(Constant.NONEEDCHECK, true);
            mChangePageListener.changePage(ChangePageListener.DISHCOMBO, bundle);
        }
    }



    private void addServerComboPart(DishVo dishVo) {
        if (mChangePageListener != null) {
                        ShopcartItem shopcartItem = CreateItemTool.createShopcartItem(dishVo);
            shopcartItem.setSetmealItems(new ArrayList<SetmealShopcartItem>());

                        List<DishSetmeal> dishSetmeals = DishCache.getSetmealHolder().getDishSetmealByDishId(dishVo.getDishShop().getId());

            if(Utils.isNotEmpty(dishSetmeals)){
                                List<SetmealShopcartItem> dishChildMeals=new ArrayList<>();
                for (DishSetmeal dishSetmeal : dishSetmeals) {
                    DishShop dishShop=DishCache.getDishHolder().get(dishSetmeal.getChildDishId());
                    if(dishShop==null){
                        continue;
                    }

                    DishSetmealVo setMealVo=new DishSetmealVo(dishShop,dishSetmeal,null);

                    SetmealShopcartItem setmealShopcartItem = CreateItemTool.createSetmealShopcartItem(shopcartItem, setMealVo);
                    BigDecimal leastCellNum = MathDecimal.trimZero(setMealVo.getSetmeal().getLeastCellNum());
                    setmealShopcartItem.changeQty(leastCellNum);
                    dishChildMeals.add(setmealShopcartItem);
                }
                shopcartItem.setServerSetmealItems(dishChildMeals);
            }

            mShoppingCart.addDishToShoppingCart(shopcartItem, false);
        }
    }


    private void addOtherStandardSingleDish(DishVo dishVo, DishShop dishShop) {
        if (dishShop == null) {
            return;
        }

        Set<DishProperty> standards = DishManager.filterStandards(dishShop);
        DishUnitDictionary unit = DishCache.getUnitHolder().get(dishShop.getUnitId());
        DishAndStandards dishAndStandards = new DishAndStandards(dishShop, standards, unit);
        if (dishShop.getSaleType() == SaleType.WEIGHING) {
            ShopcartItem shopcartItem = CreateItemTool.createShopcartItem(dishAndStandards);
            mPopupWindow = new QuantityEditPopupWindow(getActivity(), shopcartItem, dishVo.isContainProperties(), dataChangeListener);
            mPopupWindow.setDinner(true);
            mPopupWindow.setLeftZoneWidth(getQuanPopLeftWidth());
            mPopupWindow.showAtLocation(mVpDishList, Gravity.NO_GRAVITY, 0, 0);
        } else {
            ShopcartItem shopcartItem = CreateItemTool.createShopcartItem(dishAndStandards);
            mShoppingCart.addDishToShoppingCart(shopcartItem, true);
        }
    }

    public void myGridItemLongClicked(final DishVo dishVo) {
        if (dishVo.isContainProperties()) {
                        OrderDishClearStatusFragment orderDishClearStatusFragment = new OrderDishClearStatusFragment_();
            orderDishClearStatusFragment.setData(dishVo);
            orderDishClearStatusFragment.setOnCloseListener(new OnCloseListener() {

                @Override
                public void onClose(boolean isEnsure, Object obj) {
                    reloadDishData();
                }
            });
            orderDishClearStatusFragment.show(getFragmentManager(), "clearStatusFragment");
        } else {
            int title = dishVo.isClear() ? R.string.checkUClearDishStatus : R.string.checkClearDishStatus;
            new CommonDialogFragmentBuilder(MainApplication.getInstance()).title(title)
                    .iconType(CommonDialogFragment.ICON_WARNING)
                    .negativeText(R.string.common_cancel)
                    .positiveText(R.string.common_submit)
                    .positiveLinstner(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestClearStatus(dishVo, dishVo.isClear() ? ClearStatus.SALE : ClearStatus.CLEAR);
                        }
                    })
                    .build()
                    .show(getActivity().getSupportFragmentManager(), "clearDishStatus");
        }
    }


    private void requestClearStatus(final DishVo dishVo, final ClearStatus newValue) {
        List<String> dishUuids = new ArrayList<String>();
        dishUuids.add(dishVo.getDishShop().getUuid());


        ResponseListener<Boolean> listener = new ResponseListener<Boolean>() {

            @Override
            public void onResponse(ResponseObject<Boolean> response) {
                if (ResponseObject.isOk(response) && response.getContent()) {
                    dishVo.getDishShop().setClearStatus(newValue);
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }


                    String name = dishVo.getDishShop().getName();
                    if (TextUtils.isEmpty(name)) {
                        name = "";
                    }
                    if (newValue == ClearStatus.SALE) {

                        String info = name + getString(R.string.nClearstatus);
                        ToastUtil.showLongToast(info);
                    } else {
                        String info = name + getString(R.string.tClearstatus);
                        ToastUtil.showLongToast(info);
                    }

                } else if (1100 == response.getStatusCode()) {
                    if (newValue == ClearStatus.SALE) {
                        ToastUtil.showLongToast(R.string.nClearstatusNeedSync);
                    } else {
                        ToastUtil.showLongToast(R.string.yClearstatusNeedSync);
                    }
                } else {
                    if (newValue == ClearStatus.SALE) {
                        ToastUtil.showLongToast(R.string.nClearstatusFailure);
                    } else {
                        ToastUtil.showLongToast(R.string.yClearstatusFailure);
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                if (newValue == ClearStatus.SALE) {
                    ToastUtil.showLongToast(BaseApplication.sInstance.getString(R.string.nClearstatusFailure) + error.getMessage());
                } else {
                    ToastUtil.showLongToast(BaseApplication.sInstance.getString(R.string.yClearstatusFailure) + error.getMessage());
                }
            }

        };

        DishDal dishDal = OperatesFactory.create(DishDal.class);
        dishDal.clearStatus(newValue, dishUuids, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_clear_dish_list_show:
                clickShowClearListFragment();
                break;
            case R.id.btn_scan_code:
                showQuantityEditPopupWindow(v);
                break;
            case R.id.btn_temp_dish:
                addTempDish();
                break;
        }
    }

    private void clickShowClearListFragment() {
        if (!ClickManager.getInstance().isClicked()) {
            DinnerDishClearListDialogFragment dinnerDishClearListDialogFragment = new DinnerDishClearListDialogFragment_();
            dinnerDishClearListDialogFragment.setOnCloseListener(new OnCloseListener() {
                @Override
                public void onClose(boolean isEnsure, Object obj) {
                    reloadDishData();
                }
            });
            dinnerDishClearListDialogFragment.show(getFragmentManager(), "DinnerDishClearListDialogFragment");
            MobclickAgentEvent.onEvent(UserActionCode.ZC020010);
        }
    }

    @Override
    public void onBrandTypeChange(DishBrandType dishBrandType, String carteUuid, boolean single) {
        mSelectDishType = dishBrandType;
        mCarteUuid = carteUuid;
        mIsSingle = single;
        if ((mCarteUuid == null || mIsSingle) && isAllowAddTempDish)
            mBtnTempDish.setVisibility(View.VISIBLE);
        else
            mBtnTempDish.setVisibility(View.GONE);
        RLog.i(RLog.DISH_KEY_TAG, "## 正餐商品界面根据商品中类:" + dishBrandType.getName() + " 获取商品信息" + "position : DishHomePageFragment -> onBrandTypeChange()");
        reloadDishData();
    }

    private void reloadDishData() {
        if (mSelectDishType == null)
            return;
        if (mTask != null) {
            mTask.cancel(true);
        }
        mTask = new LoadDishTask(mSelectDishType, mCarteUuid);
        mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class LoadDishTask extends AsyncTask<Void, Void, DishInfo> {

        DishBrandType dishBrandType;
        String cartUuid;

        LoadDishTask(DishBrandType vDishBrandType, String cartUuid) {
            this.dishBrandType = vDishBrandType;
            this.cartUuid = cartUuid;
        }

        @Override
        protected DishInfo doInBackground(Void... params) {
            if (cartUuid == null) {
                RLog.i(RLog.DISH_KEY_TAG, "## 正餐商品界面根据商品中类:" + dishBrandType.getName() + "调用mDishManager->switchType获取商品信息");
                return mDishManager.switchType(dishBrandType);
            } else {
                RLog.i(RLog.DISH_KEY_TAG, "## 正餐商品界面根据商品中类:" + dishBrandType.getName() + "调用mDishManager->buffetSwitchTypes获取商品信息");
                return mDishManager.buffetSwitchTypes(mIsSingle, cartUuid, dishBrandType);
            }
        }

        @Override
        protected void onPostExecute(DishInfo dishInfo) {
            if (isAdded() && dishInfo != null && mAdapter != null && dishInfo.dishType != null) {
                if (Utils.isNotEmpty(dishInfo.dishList)) {
                    mAdapter.setDataSet(dishInfo.dishList);
                    createIndex(mCurrentIndex, mAdapter.getCount());
                                        mVpDishList.setCurrentItem(0, false);
                    mVpDishList.setVisibility(View.VISIBLE);
                    mLlDots.setVisibility(View.VISIBLE);
                    mIvDishTypeEmpty.setVisibility(View.GONE);
                } else {
                    mVpDishList.setVisibility(View.GONE);
                    mLlDots.setVisibility(View.GONE);
                    mIvDishTypeEmpty.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void initView() {
        UserActionEvent.start(UserActionEvent.DINNER_DISH_DISPLAY);

        mVpDishList.setOnPageChangeListener(this);

        initAdapter();
        mAdapter.setGridHeight(calculateGridHeight());
        mAdapter.setDishCardType(getDishCardResType());
        mVpDishList.setAdapter(mAdapter);
        setTempDishBtn();
        mVBrandType.addView(getBrandTypeView());

        if (mLoadingFinish != null)
            mLoadingFinish.loadingFinish();


        mVBrandType.setVisibility(isBuyServerBusiness()?View.GONE:View.VISIBLE);
        UserActionEvent.end(UserActionEvent.DINNER_DISH_DISPLAY);
    }

    protected void initAdapter() {
        mAdapter = new DinnerDishListPagerAdapter(getActivity(), new ArrayList<DishVo>()) {

            @Override
            public void doItemTouch(DishVo dishVo) {
                onTouch(dishVo);
            }

            @Override
            public void doItemLongClick(DishVo dishVo) {
                myGridItemLongClicked(dishVo);
            }

        };
    }

    protected int getDishCardResType() {
        return OrderDishAdapter.DEFAULT_DISH_CARD_BG;
    }

    private void setTempDishBtn() {
                isAllowAddTempDish = mDishManager.isHasTempDish();
        if (isAllowAddTempDish) {
            mBtnTempDish.setVisibility(View.VISIBLE);
        } else {
            mBtnTempDish.setVisibility(View.GONE);
        }
    }


    protected int calculateGridHeight() {
        int height = getResources().getDisplayMetrics().heightPixels;
        int titlebarHeight = getResources().getDimensionPixelSize(R.dimen.status_bar_height);
        int indexFieldHeight = getResources().getDimensionPixelSize(R.dimen.order_dish_index_field_height);
        int marginTop = getResources().getDimensionPixelSize(R.dimen.order_dish_margin_top);
        return height - titlebarHeight - indexFieldHeight - marginTop;
    }


    @Override
    public void onSearchClick() {
        if (mChangePageListener != null) {
            mChangePageListener.changePage(ChangePageListener.SEARCH, null);
        }
        MobclickAgentEvent.onEvent(getActivity(), MobclickAgentEvent.dinnerOrderDishSearch);
    }


    public void onEventMainThread(EventDishChangedNotice event) {
        if (event != null) {
            refreshPartVisibleGridViews(event.residueTotalChangedMap);
        }
    }



    private void createIndex(int currentIndex, int totalSize) {
        mLlDots.removeAllViews();

        for (int i = 0; i < totalSize; i++) {
            LinearLayout.LayoutParams indexParams =
                    new LinearLayout.LayoutParams(DensityUtil.dip2px(MainApplication.getInstance(), 29),
                            DensityUtil.dip2px(MainApplication.getInstance(), 25));
            if (totalSize == 1) {
                indexParams.setMargins(0, 0, 0, 0);
            } else {
                if (i == totalSize - 1) {
                    indexParams.setMargins(0, 0, 0, 0);
                } else {
                    indexParams.setMargins(0, 0, DensityUtil.dip2px(MainApplication.getInstance(), 10), 0);
                }
            }

            TextView textView = new TextView(getActivity());
            textView.setLayoutParams(indexParams);
            textView.setBackgroundResource(getIndexBg());
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);
            textView.setText(String.valueOf(i + 1));
            textView.setTag(i);
            textView.setTextColor(getResources().getColorStateList(getIndexTextColor()));
            textView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v.getTag() != null)
                        mVpDishList.setCurrentItem((Integer) (v.getTag()));
                }
            });
            mLlDots.addView(textView, i);
        }

        if (currentIndex < totalSize) {
            mLlDots.getChildAt(currentIndex).setSelected(true);
        }

                ViewGroup.LayoutParams layoutParams = mHsvDots.getLayoutParams();
        if (totalSize > MAX_INDEX_COUNT) {
                        layoutParams.width = DensityUtil.dip2px(MainApplication.getInstance(), 29) * MAX_INDEX_COUNT +
                    DensityUtil.dip2px(MainApplication.getInstance(), 10) * MAX_INDEX_COUNT;
        } else {
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        mHsvDots.setLayoutParams(layoutParams);
    }

    protected int getIndexBg() {
        return R.drawable.dish_list_index_bg_selector;
    }

    protected int getIndexTextColor() {
        return R.color.dinner_orderdish_label_text_selector;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    private int columnSelectIndex = 0;
    private int maxWidth = DensityUtil.dip2px(MainApplication.getInstance(), 29) * MAX_INDEX_COUNT +
            DensityUtil.dip2px(MainApplication.getInstance(), 10) * (MAX_INDEX_COUNT - 1);

    @Override
    public void onPageSelected(int position) {
        columnSelectIndex = position;
        for (int i = 0; i < mLlDots.getChildCount(); i++) {
                        View checkView = mLlDots.getChildAt(position);
            if (checkView == null) continue;

            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - maxWidth / 2;
                        mHsvDots.smoothScrollTo(i2, 0);
        }
                for (int j = 0; j < mLlDots.getChildCount(); j++) {
            View checkView = mLlDots.getChildAt(j);
            boolean isCheck;
            if (j == position) {
                isCheck = true;
            } else {
                isCheck = false;
            }
            checkView.setSelected(isCheck);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private DataChangeListener dataChangeListener = new DataChangeListener() {

        @Override
        public void dataChange(ShopcartItem shopcartItem, boolean isContainProperties) {
                        if (shopcartItem.getOrderDish().isCombo()) {                if (mChangePageListener != null) {
                                        shopcartItem.setSetmealItems(new ArrayList<SetmealShopcartItem>());
                    mShoppingCart.addDishToShoppingCart(shopcartItem, false);

                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, shopcartItem.getUuid());
                    bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST);
                    bundle.putBoolean(Constant.NONEEDCHECK, true);
                    mChangePageListener.changePage(ChangePageListener.DISHCOMBO, bundle);
                }
            } else {
                mShoppingCart.addDishToShoppingCart(shopcartItem, true);
            }
        }
    };


    private void showQuantityEditPopupWindow(View v) {
        MobclickAgentEvent.onEvent(UserActionCode.ZC020011);
        mPopupWindow = new QuantityEditPopupWindow(getActivity(), new DataChangeListener() {

            @Override
            public void dataChange(ShopcartItem shopcartItem, boolean isContainProperties) {
                if (shopcartItem.getDishShop().getClearStatus() == ClearStatus.CLEAR
                        && !mShoppingCart.getIsSalesReturn()) {
                    ToastUtil.showShortToast(R.string.order_dish_sold_out);
                    return;
                }
                if (shopcartItem.getSaleType() == SaleType.WEIGHING && DinnerShoppingCart.getInstance().getOrder().isUnionMainTrade()) {
                    ToastUtil.showShortToast(R.string.union_main_trade_not_allow_weighing_dish);
                    return;
                }
                mShoppingCart.addDishToShoppingCart(shopcartItem, true);

            }
        });
        mPopupWindow.showAtLocation(v, Gravity.NO_GRAVITY, 0, 0);
    }


    private void addTempDish() {
        if (ClickManager.getInstance().isClicked(R.id.btn_temp_dish) || getActivity().isFinishing()) {
            return;
        }
        if (tempDishFragment != null && tempDishFragment.isVisible()) {
            return;
        }
        MobclickAgentEvent.onEvent(UserActionCode.ZC020012);
                tempDishFragment = new TempDishFragment_();
        tempDishFragment.setDishVo(mDishManager.getTempDishVo());
        tempDishFragment.show(getFragmentManager(), "TempDishFragment");
    }


    private void dismissQuantityEditPopupWindow() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }


    protected void hideControlBtn() {
        isAllowAddTempDish = false;
        mBtnTempDish.setVisibility(View.GONE);
        mBtClearDishListShow.setVisibility(View.GONE);
        mBtnScanCode.setVisibility(View.GONE);
    }

    private InventoryCacheUtil.InventoryDataChangeListener mInventoryListener = new InventoryCacheUtil.InventoryDataChangeListener() {
        @Override
        public void dataChange(List<InventoryInfo> data) {
            if (mAdapter != null) {
                if (Utils.isNotEmpty(data)) {
                    Map<String, InventoryInfo> inventoryInfoMap = new HashMap<String, InventoryInfo>();
                    for (InventoryInfo inventoryInfo : data) {
                        inventoryInfoMap.put(inventoryInfo.getUuid(), inventoryInfo);
                    }

                    View view = mVpDishList.findViewById(mCurrentIndex);
                    if (view != null) {
                        GridView gridView = (GridView) view;
                        mAdapter.updateItemInventory(gridView, inventoryInfoMap);
                    }

                                        View prevView = mVpDishList.findViewById(mCurrentIndex - 1);
                    if (prevView != null) {
                        GridView prevGridView = (GridView) prevView;
                        mAdapter.updateItemInventory(prevGridView, inventoryInfoMap);
                    }

                                        View nextView = mVpDishList.findViewById(mCurrentIndex + 1);
                    if (nextView != null) {
                        GridView nextGridView = (GridView) nextView;
                        mAdapter.updateItemInventory(nextGridView, inventoryInfoMap);
                    }
                } else {
                    refreshAllVisibleGridViews();
                }
            }
        }
    };

}
