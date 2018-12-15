package com.zhongmei.bty.dinner.util;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.snack.orderdish.buinessview.ExtraInfo;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryItem;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryCacheUtil;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderDish;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderExtra;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlySetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.bty.basemodule.orderdish.entity.DishBrandProperty;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishPropertyType;
import com.zhongmei.yunfu.db.entity.dish.DishSetmeal;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.enums.ChildDishType;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.orderdish.manager.DishSetmealManager;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.util.ViewUtil;
import com.zhongmei.bty.cashier.inventory.view.ReturnInventoryDialogFragment;
import com.zhongmei.bty.cashier.orderdishmanager.DishPropertyManager;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListener;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListerTag;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishPropertyVo;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishStandardVo;
import com.zhongmei.bty.cashier.shoppingcart.vo.PropertyGroupVo;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.util.UserActionCode;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.dinner.manager.DinnerTradeItemManager;
import com.zhongmei.bty.dinner.orderdish.DinnerDishMiddleFragment;
import com.zhongmei.bty.dinner.orderdish.view.DinnerCustomChangePriceView;
import com.zhongmei.bty.dinner.orderdish.view.DinnerCustomChangePriceView_;
import com.zhongmei.bty.dinner.orderdish.view.DinnerRemarkView;
import com.zhongmei.bty.dinner.orderdish.view.DinnerRemarkView_;
import com.zhongmei.bty.entity.event.orderdishevent.EventDishPropertiesNotice;
import com.zhongmei.bty.snack.orderdish.InterfaceListener.OrderDishListenerImp;
import com.zhongmei.bty.snack.orderdish.buinessview.CustomChangePriceView;
import com.zhongmei.bty.snack.orderdish.buinessview.CustomEmptyView;
import com.zhongmei.bty.snack.orderdish.buinessview.CustomEmptyView_;
import com.zhongmei.bty.snack.orderdish.buinessview.CustomStandardView;
import com.zhongmei.bty.snack.orderdish.buinessview.CustomStandardView_;
import com.zhongmei.bty.snack.orderdish.buinessview.ExtraView;
import com.zhongmei.bty.snack.orderdish.buinessview.ExtraView_;
import com.zhongmei.bty.snack.orderdish.buinessview.RemarkView;
import com.zhongmei.bty.snack.orderdish.buinessview.TasteView;
import com.zhongmei.bty.snack.orderdish.buinessview.TasteView_;
import com.zhongmei.bty.snack.orderdish.view.CustomDishQuantityView;
import com.zhongmei.bty.snack.orderdish.view.QuantityEditPopupWindow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by demo on 2018/12/15
 */
public class TradeItemPropertyUtil implements CustomChangePriceView.OnPriceChangeListener, View.OnClickListener,
        CustomDishQuantityView.IDishQuantityListener {
    private static final String TAG = TradeItemPropertyUtil.class.getSimpleName();

    //操作栏位
    protected LinearLayout vActionBar;
    //内容栏位
    private FrameLayout vContent;


    //菜品数量
    protected CustomDishQuantityView vDishQuantity;
    //座位号
    protected Button btnSeat;
    //规格
    private Button btnStandard;
    //做法
    protected Button btnProperty;
    //单品备注
    private Button btnRemark;
    //加料
    protected Button btnExtra;
    //变价
    private Button btnChangePrice;
    //变价标示
    private ImageView btnChagepriceAnchor;
    //分割线
    private View vDivideLine;

    /**
     * 当前内容
     */
    private View mCurrentContentView;
    /**
     * 空态页
     */
    private CustomEmptyView mCustomEmptyView;
    /**
     * 规格页
     */
    private CustomStandardView mCustomStandardView;
    /**
     * 做法页
     */
    private TasteView mTasteView;
    /**
     * 加料页
     */
    private ExtraView mExtraView;
    /**
     * 变价页
     */
    private DinnerCustomChangePriceView mCustomChangePriceView;
    /**
     * 备注页
     */
    private DinnerRemarkView mRemarkView;

    private View mParentView;

    protected DishDataItem mDishDataItem;

    protected FragmentActivity mActivity;

    protected DinnerShoppingCart mShoppingCart;
    protected ShopcartItem mShopcartItem;
    protected SetmealShopcartItem mSetmealShopcartItem;
    protected ShopcartItemBase<?> mRealItemBase;

    //购物车条目UUID
    private String mUuid;
    //购物车父条目UUID，可为空
    private String mParentUuid;

    // 判读是否是第一次进入 第一次进入就不去add到ShoppingCart
    private boolean isFirst = true;

    // 当前是否有菜品加入
    protected boolean added = true;

    private String mMemo;

    private int mTargetPageNo = -1;

    private DishPropertyManager mDishPropertyManager;
    //若当前菜品不为空，那么此DishSetmealManager不为空
    protected DishSetmealManager mDishSetmealManager;

    /**
     * 规格类属性分组列表
     */
    private List<PropertyGroupVo<DishStandardVo>> standardGroupList = new ArrayList<>();
    private ArrayList<PropertyGroupVo<DishPropertyVo>> tasteGroupVo = new ArrayList<>();
    private ArrayList<OrderProperty> properties = new ArrayList<>();
    /**
     * 加料列表
     */
    private List<OrderExtra> extraList = new ArrayList<>();

    private AsyncTask mAsyncTask;

    protected BigDecimal mSelectedQty;

    private TradePrivilege mTradePrivilege;
    private TradeReasonRel mDiscountReason;

    //数量编辑弹框
    private QuantityEditPopupWindow mQuantityEditPopupWindow;

    protected DinnerDishMiddleFragment.IChangePageListener mListener;
    private ChangePageListener mChangePageListener;

    //当前是有什么场景调用
    protected int mCurrentMode = DinnerDishMiddleFragment.DINNER_ORDER_MODE;

    public void setCurrentMode(int currentMode) {
        this.mCurrentMode = currentMode;
    }

    public void setPropertyButton(FragmentActivity activity, DishDataItem item, View parentView,
                                  DinnerDishMiddleFragment.IChangePageListener listener, ChangePageListener changePageListener) {
        mActivity = activity;
        mParentView = parentView;
        mListener = listener;
        mChangePageListener = changePageListener;
        mShoppingCart = DinnerShoppingCart.getInstance();
        mShoppingCart.registerListener(ShoppingCartListerTag.ORDER_DISH_MIDDLE, mModifyShoppingCartListener);
        initView();
        setDishDataItem(item);
    }

    private ShoppingCartListener mModifyShoppingCartListener = new ShoppingCartListener() {
        @Override
        public void removeRemark(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, IShopcartItem mShopcartItem) {
            if (isRemarkView()) {
                mRemarkView.setDishRemark("");
            }
        }

        @Override
        public void removeSetmealRemark(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, ISetmealShopcartItem setmeal) {
            if (isRemarkView()) {
                mRemarkView.setDishRemark("");
            }
        }

    };

    private boolean isRemarkView() {
        return mCurrentContentView != null && mCurrentContentView instanceof RemarkView;
    }

    private void initView() {
        vActionBar = (LinearLayout) mParentView.findViewById(R.id.v_action_bar);
        vContent = (FrameLayout) mParentView.findViewById(R.id.v_content);
        vDishQuantity = (CustomDishQuantityView) mParentView.findViewById(R.id.v_dish_quantity);
        btnSeat = (Button) mParentView.findViewById(R.id.btn_seat);
        btnStandard = (Button) mParentView.findViewById(R.id.btn_standard);
        btnProperty = (Button) mParentView.findViewById(R.id.btn_property);
        btnRemark = (Button) mParentView.findViewById(R.id.btn_remark);
        btnExtra = (Button) mParentView.findViewById(R.id.btn_extra);
        btnChangePrice = (Button) mParentView.findViewById(R.id.btn_change_price);
        btnChagepriceAnchor = (ImageView) mParentView.findViewById(R.id.btn_chageprice_anchor);
        vDivideLine = mParentView.findViewById(R.id.v_divide_line);

        vDishQuantity.setListener(this);
        btnStandard.setOnClickListener(this);
        btnProperty.setOnClickListener(this);
        btnRemark.setOnClickListener(this);
        btnExtra.setOnClickListener(this);
        btnChangePrice.setOnClickListener(this);
        btnSeat.setEnabled(false);
        btnSeat.setVisibility(View.GONE);
        btnSeat.setOnClickListener(this);
        if (mShoppingCart.getOrder().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN)
            vDishQuantity.setTradeNmb(mShoppingCart.getSubTradeCount());
    }

    private void setDishDataItem(DishDataItem dishDataItem) {
        this.mDishDataItem = dishDataItem;
        if (mDishDataItem != null) {
            initData();
        }
    }

    protected void initData() {
        if (mTargetPageNo == -1 && TextUtils.isEmpty(mDishDataItem.getBase().getUuid())
                || DinnerTradeItemManager.getInstance().isSaved(mDishDataItem)
                || DinnerTradeItemManager.getInstance().isReturnDish(mDishDataItem.getBase())
                || DinnerTradeItemManager.getInstance().isUnsavedReadonly(mDishDataItem.getBase())) {//标识没有属性项被选中
            initUnselectData();
        } else {
            initSelectData();
        }
    }

    /**
     * 是否是配菜
     *
     * @return
     */
    protected boolean isSlideDish() {
        return mCurrentMode == DinnerDishMiddleFragment.GROUP_SLIDE_MODE;
    }

    protected void initUnselectData() {
        mUuid = mDishDataItem.getBase().getUuid();
        mParentUuid = mDishDataItem.getBase().getParentUuid();
        mShopcartItem = null;
        mSetmealShopcartItem = null;
        mRealItemBase = null;
        added = false;
        isFirst = true;

        //清除选中按钮
        clearButtonSelected();

        //刷新菜品操作按钮状态
        vDishQuantity.setData(null, true);
        vDishQuantity.setAlpha(0.5f);
        ViewUtil.setButtonEnabled(btnStandard, true);
        ViewUtil.setButtonEnabled(btnProperty, false);
        ViewUtil.setButtonEnabled(btnExtra, false);
        ViewUtil.setButtonEnabled(btnRemark, false);
        ViewUtil.setButtonEnabled(btnChangePrice, false);
        ViewUtil.setButtonEnabled(btnSeat, false);
        if (isSavedValidSingleOrComboItem()) {
            //团餐不允许改
            if (!isCanModify()) {
                ViewUtil.setButtonEnabled(btnStandard, false);
            }
            btnStandard.setText(R.string.dish_combo_modify);
            btnSeat.setVisibility(View.GONE);
            btnProperty.setVisibility(View.GONE);
            btnExtra.setVisibility(View.GONE);
            btnRemark.setVisibility(View.GONE);
            btnChangePrice.setVisibility(View.GONE);
            vDivideLine.setVisibility(View.GONE);
            if (isGroup() && mDishDataItem.getBase() instanceof ReadonlyShopcartItemBase) {
                vDishQuantity.setText(MathDecimal.toTrimZeroString(((ReadonlyShopcartItemBase) mDishDataItem.getBase()).getSigleDeskQuantity()));
            } else if (mDishDataItem.getType() == ItemType.WEST_CHILD) {
                //西餐子菜显示实际的数量
                vDishQuantity.setText(MathDecimal.toTrimZeroString(mDishDataItem.getBase().getTotalQty()));
            } else {
                vDishQuantity.setText(MathDecimal.toTrimZeroString(mDishDataItem.getBase().getSingleQty()));
            }
        } else {
            btnStandard.setText(R.string.dish_standard);
            ViewUtil.setButtonEnabled(btnStandard, false);
            if (mDishDataItem.getBase() != null) {
                if (mDishDataItem.getType() == ItemType.WEST_CHILD) {
                    vDishQuantity.setText(MathDecimal.toTrimZeroString(mDishDataItem.getBase().getTotalQty()));
                } else {
                    vDishQuantity.setText(MathDecimal.toTrimZeroString(mDishDataItem.getBase().getSingleQty()));
                }
            }
        }
        //刷新内容页
        showCustomContentView(null);
    }

    /**
     * 是否为已保存的有效单菜或套餐外壳（全退的0条目除外）
     *
     * @return
     */
    protected boolean isSavedValidSingleOrComboItem() {
        if (mDishDataItem.getBase() == null) {
            return false;
        }

        return mDishDataItem.getBase().getStatusFlag() == StatusFlag.VALID
                && DinnerTradeItemManager.getInstance().isSaved(mDishDataItem)
                && !DinnerTradeItemManager.getInstance().isDishReturnAll(mDishDataItem)//全退菜品不能修改
                && !DinnerTradeItemManager.getInstance().isUnsavedReadonly(mDishDataItem.getBase())//未生效的readonlyshopcartitem不能修改
                && !(mDishDataItem.getBase() instanceof ISetmealShopcartItem);//套餐子菜不能修改
    }

    /**
     * 是否不允许修改的菜品（目前是团餐餐标菜品和联台主单菜品）
     *
     * @return
     */
    protected boolean isCanModify() {
        if (mDishDataItem.getBase() == null) {
            return false;
        }

        if (mDishDataItem.getBase().isGroupDish()
                || mDishDataItem.getBase().getShopcartItemType() == ShopcartItemType.MAINBATCH) {
            return false;
        }

        return true;
    }

    /**
     * 是否是团餐餐标下的菜
     *
     * @return
     */
    protected boolean isGroup() {
        if (mDishDataItem.getBase() != null && mDishDataItem.getBase().isGroupDish()) {
            return true;
        }
        return false;
    }

    /**
     * 清除所有选中
     */
    public void clearButtonSelected() {
        ViewUtil.setButtonSelected(vActionBar, null);
    }

    protected void initSelectData() {
        added = true;
        isFirst = true;
        mShopcartItem = null;
        mSetmealShopcartItem = null;
        mRealItemBase = null;
        vDishQuantity.setData(null, true);
        properties.clear();
        mUuid = mDishDataItem.getBase().getUuid();
        mParentUuid = mDishDataItem.getBase().getParentUuid();

        if (!TextUtils.isEmpty(mParentUuid)) {
            mShopcartItem = mShoppingCart.getShopcartItemByUUID(mShoppingCart.getShoppingCartVo(), mParentUuid);
            int size = mShopcartItem.getSetmealItems() == null ? 0 : mShopcartItem.getSetmealItems().size();
            for (int i = 0; i < size; i++) {
                if (mShopcartItem.getSetmealItems().get(i).getUuid().equals(mUuid)) {
                    mSetmealShopcartItem = mShopcartItem.getSetmealItems().get(i);
                    mRealItemBase = mSetmealShopcartItem;
                    break;
                }
            }
        } else {
            mShopcartItem = mShoppingCart.getShopcartItem(mShoppingCart.getShoppingCartVo(), mUuid);
            mRealItemBase = mShopcartItem;
        }
        if (mRealItemBase != null) {
            mSelectedQty = mRealItemBase.getOrderDish().getSingleQty();
            mTradePrivilege = mRealItemBase.getPrivilege();
            mDiscountReason = mRealItemBase.getDiscountReasonRel();
            mMemo = mRealItemBase.getMemo();
        }

        //加载数据
        mDishPropertyManager = new DishPropertyManager();
        if (mRealItemBase != null) {
            loadProperties();
            mDishSetmealManager = mShopcartItem.getSetmealManager();
        }
        if (mDishDataItem.getBase() != null && mDishDataItem.getBase().getStatusFlag() == StatusFlag.VALID
                && mDishDataItem.getBase().getDishShop() != null && mDishDataItem.getBase().getDishShop().getClearStatus()
                == ClearStatus.SALE
                ) {
            ViewUtil.setButtonEnabled(btnRemark, true);
            ViewUtil.setButtonEnabled(btnChangePrice, true);
        } else {
            ViewUtil.setButtonEnabled(btnRemark, false);
            ViewUtil.setButtonEnabled(btnChangePrice, false);
            setClearView();
        }
    }

    /**
     * 估清状态界面显示
     */
    private void setClearView() {
        vContent.removeAllViews();
        TextView textView = new TextView(mActivity);
        textView.setText(mActivity.getString(R.string.order_dish_sold_out));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(mActivity.getResources().getDimension(R.dimen.text_24));
        vContent.addView(textView);
        setCurrentContentView(textView);
    }

    private void loadProperties() {
        mAsyncTask = new AsyncTask<Void, Void, EventDishPropertiesNotice>() {

            @Override
            protected EventDishPropertiesNotice doInBackground(Void... params) {
                return mDishPropertyManager.loadData(mRealItemBase, mShoppingCart.getIsSalesReturn(), true);
            }

            @Override
            protected void onPostExecute(EventDishPropertiesNotice eventDishPropertiesNotice) {
                refreshView(eventDishPropertiesNotice);
            }
        }.execute();
    }

    /**
     * 刷新菜品属性内容
     *
     * @param eventDishPropertiesNotice
     */
    private void refreshView(EventDishPropertiesNotice eventDishPropertiesNotice) {
        if (eventDishPropertiesNotice != null && eventDishPropertiesNotice.dishPropertiesVo != null && eventDishPropertiesNotice.uuid.equals(mUuid)) {
            if (mShoppingCart.getOrder().isUnionMainTrade() && eventDishPropertiesNotice.dishPropertiesVo.getOrderDish() != null && eventDishPropertiesNotice.dishPropertiesVo.getOrderDish().getSaleType() == SaleType.WEIGHING) {
                ToastUtil.showShortToast(R.string.union_main_trade_not_allow_weighing_dish);
                return;
            }

            //数量
            vDishQuantity.setData(eventDishPropertiesNotice.dishPropertiesVo.getOrderDish(), true);
            //西餐子菜显示实际的数量
            if (mDishDataItem.getType() == ItemType.WEST_CHILD) {
                vDishQuantity.setText(MathDecimal.toTrimZeroString(mDishDataItem.getBase().getTotalQty()));
            }
            // 规格（规格切换不成功时，直接返回）
            if (!setStandard(eventDishPropertiesNotice)) {
                return;
            }
            //做法
            setRecipe(eventDishPropertiesNotice);

            OrderDish orderDish = eventDishPropertiesNotice.dishPropertiesVo.getOrderDish();
            if (orderDish != null
                    && (orderDish.getDishShop().getClearStatus() == ClearStatus.SALE || mShoppingCart.getIsSalesReturn())) {
                // 清除老的规格属性,并添加新的规格属性
                ArrayList<OrderProperty> newProperties = new ArrayList<OrderProperty>();
                for (OrderProperty orderProperty : properties) {
                    if (orderProperty.getPropertyKind() != PropertyKind.STANDARD) {
                        newProperties.add(orderProperty);
                    }
                }
                Set<DishProperty> set = orderDish.getStandards();
                if (set != null) {
                    for (DishProperty dishProperty : set) {
                        DishPropertyType dishPropertyType =
                                DishCache.getPropertyTypeHolder().get(dishProperty.getPropertyTypeId());
                        newProperties.add(new OrderProperty(dishPropertyType, dishProperty));
                    }
                }
                properties.clear();
                properties.addAll(newProperties);
            } else {
                properties.clear();
            }

            //加料
            setExtra(eventDishPropertiesNotice);
            //变价
            setChangePrice(eventDishPropertiesNotice);
            if (eventDishPropertiesNotice.dishPropertiesVo.getOrderDish() == null) {
                ViewUtil.setButtonEnabled(btnRemark, false);
            }
            //菜品加入购物车
            addOrderDishToCart(eventDishPropertiesNotice);
        }
    }

    //设置规格
    protected boolean setStandard(EventDishPropertiesNotice eventDishPropertiesNotice) {
        standardGroupList.clear();
        if (eventDishPropertiesNotice.dishPropertiesVo.getStandardGroupList() != null) {
            standardGroupList.addAll(eventDishPropertiesNotice.dishPropertiesVo.getStandardGroupList());
        }

        //如果当前菜品为套餐壳子，不展示规格（套餐壳子也没有规格）
        if (DinnerTradeItemManager.getInstance().isCombo(mRealItemBase)) {
            btnStandard.setText(R.string.dish_combo_modify);
            ViewUtil.setButtonEnabled(btnStandard, true);
        } else {
            btnStandard.setText(R.string.dish_standard);
            ViewUtil.setButtonEnabled(btnStandard, Utils.isNotEmpty(standardGroupList));
            if (mCurrentContentView != null && mCurrentContentView instanceof CustomStandardView) {
                ((CustomStandardView) mCurrentContentView).setData(standardGroupList, mDishPropertyManager, mShoppingCart);
            }
        }
        ViewUtil.setViewVisibility(btnStandard, View.VISIBLE);
        if (DinnerTradeItemManager.getInstance().isSaved(mDishDataItem) && !isCanModify()) {
            ViewUtil.setViewVisibility(btnStandard, View.GONE);
        }

        return true;
    }

    //设置做法
    private void setRecipe(EventDishPropertiesNotice eventDishPropertiesNotice) {
        if (isSlideDish()) {
            return;
        }
        tasteGroupVo.clear();

        OrderDish orderDish = eventDishPropertiesNotice.dishPropertiesVo.getOrderDish();
        if (orderDish != null
                && (orderDish.getDishShop().getClearStatus() == ClearStatus.SALE || mShoppingCart.getIsSalesReturn())) {

            if (eventDishPropertiesNotice.dishPropertiesVo.getPropertyGroupList() != null) {
                int size = eventDishPropertiesNotice.dishPropertiesVo.getPropertyGroupList().size();
                for (int i = 0; i < size; i++) {
                    DishPropertyType type =
                            eventDishPropertiesNotice.dishPropertiesVo.getPropertyGroupList().get(i).getPropertyType();
                    if (type != null && type.getPropertyKind() == PropertyKind.PROPERTY) {
                        tasteGroupVo.add(eventDishPropertiesNotice.dishPropertiesVo.getPropertyGroupList().get(i));
                    }
                }
            }

        }
        if (DinnerTradeItemManager.getInstance().isCombo(mRealItemBase)) {
//            btnProperty.setText("");
            ViewUtil.setButtonEnabled(btnProperty, false);
        } else {
            btnProperty.setText(R.string.dish_property);
            ViewUtil.setButtonEnabled(btnProperty, Utils.isNotEmpty(tasteGroupVo));
        }
        ViewUtil.setViewVisibility(btnProperty, View.VISIBLE);
    }

    //设置加料
    private void setExtra(EventDishPropertiesNotice eventDishPropertiesNotice) {
        if (isSlideDish()) {
            return;
        }
        extraList.clear();
        OrderDish orderDish = eventDishPropertiesNotice.dishPropertiesVo.getOrderDish();
        if (orderDish != null
                && (orderDish.getDishShop().getClearStatus() == ClearStatus.SALE || mShoppingCart.getIsSalesReturn())) {
            if (eventDishPropertiesNotice.dishPropertiesVo.getExtraList() != null) {
                extraList.addAll(eventDishPropertiesNotice.dishPropertiesVo.getExtraList());
            }
        }

        if (DinnerTradeItemManager.getInstance().isCombo(mRealItemBase)) {
//            btnExtra.setText("");
            ViewUtil.setButtonEnabled(btnExtra, false);
        } else {
            btnExtra.setText(R.string.dish_extra);
            ViewUtil.setButtonEnabled(btnExtra, Utils.isNotEmpty(extraList));
        }

        ViewUtil.setViewVisibility(btnExtra, View.VISIBLE);
    }

    //设置变价
    private void setChangePrice(EventDishPropertiesNotice eventDishPropertiesNotice) {
//        isCombo()||
        if (eventDishPropertiesNotice.dishPropertiesVo.getOrderDish() == null) {
//            btnChangePrice.setText("");
            ViewUtil.setButtonEnabled(btnChangePrice, false);
        } else {
            btnChagepriceAnchor.setVisibility(View.VISIBLE);
            OrderDish orderDish = eventDishPropertiesNotice.dishPropertiesVo.getOrderDish();
            if (orderDish != null && orderDish.getIsChangePrice() == Bool.YES) {
                ViewUtil.setButtonEnabled(btnChangePrice, true);
                btnChagepriceAnchor.setVisibility(View.VISIBLE);
            } else {
                btnChangePrice.setText(R.string.dish_detail);
                btnChagepriceAnchor.setVisibility(View.GONE);
            }
        }
        ViewUtil.setViewVisibility(btnChangePrice, View.VISIBLE);
    }

    /**
     * 菜品加入购物车
     */
    private void addOrderDishToCart(EventDishPropertiesNotice eventDishPropertiesNotice) {
        OrderDish orderDish = eventDishPropertiesNotice.dishPropertiesVo.getOrderDish();
        if (orderDish != null
                && (orderDish.getDishShop().getClearStatus() == ClearStatus.SALE || mShoppingCart.getIsSalesReturn())) {
            if (isFirst) {
                isFirst = false;
                if (mTargetPageNo == -1) {
                    clearButtonSelected();
                    if (!DinnerTradeItemManager.getInstance().isCombo(mRealItemBase)) {
                        if (btnSeat.isEnabled()) {
                            showSeatView();
                        } else if (btnStandard.isEnabled()) {
                            showStandardView();
                        } else if (btnProperty.isEnabled()) {
                            showPropertyView();
                        } else if (btnExtra.isEnabled()) {
                            showExtraView();
                        } else if (btnRemark.isEnabled()) {
                            showRemarkView();
                        } else if (btnChangePrice.isEnabled()) {
                            showChangePriceView();
                        } else {
                            showCustomContentView(null);
                        }
                    } else {
                        showCustomContentView(null);
                    }
                } else if (mTargetPageNo == ChangePageListener.PAGE_STANDARD) {
                    showStandardView();
                } else if (mTargetPageNo == ChangePageListener.PAGE_PROPERTY) {
                    showPropertyView();
                } else if (mTargetPageNo == ChangePageListener.PAGE_EXTRA) {
                    showExtraView();
                } else if (mTargetPageNo == ChangePageListener.PAGE_CHANGE_PRICE) {
                    showChangePriceView();
                }
            } else {
                //数量
                BigDecimal singleQty = mShopcartItem.getSingleQty();
                // 获取原来的关联原菜id和uuid
                Long relateId = null;
                String relateUuid = null;
                if (mShopcartItem != null) {
                    relateId = mShopcartItem.getRelateTradeItemId();
                    relateUuid = mShopcartItem.getRelateTradeItemUuid();
                }

                //加料
                List<ExtraShopcartItem> extraItems = new ArrayList<>();
                DishShop dishShop = orderDish.getDishShop();//新选的dishshop
                if (dishShop == null) {
                    return;
                }
                // 获取到商品的加料信息
                DishPropertyManager.DishExtraFilter dishExtraFilter = new DishPropertyManager.DishExtraFilter(dishShop);
                List<DishSetmeal> dishExtraList = DishCache.getDishExtraHolder().filter(dishExtraFilter);
                //菜品加料为空，使用的是公共库，所有加料都可以直接加进去
                if (Utils.isEmpty(dishExtraList)) {
                    extraItems.addAll(mShopcartItem.getExtraItems());
                } else {
                    //缓存菜品的私有加料库
                    Map<Long, DishSetmeal> setmealMap = new HashMap<>();
                    for (DishSetmeal setmeal : dishExtraList) {
                        if (setmeal.getChildDishType() == ChildDishType.EXTRA) {
                            setmealMap.put(setmeal.getChildDishId(), setmeal);
                        }
                    }

                    for (ExtraShopcartItem extraItem : mShopcartItem.getExtraItems()) {
                        if (extraItem.getOrderDish() == null || extraItem.getOrderDish().getSetmeal() == null) {
                            continue;
                        }

                        //判断菜品私有加料库，有没有当前加料
                        Long extraId = extraItem.getOrderDish().getSetmeal().getChildDishId();//加料对应菜品的品牌菜id
                        DishSetmeal setmeal = setmealMap.get(extraId);
                        if (setmeal != null) {
                            extraItem.getOrderDish().setSetmeal(setmeal);
                            extraItems.add(extraItem);
                        }
                    }
                }

                //属性
                //先移除所有口味做法，避免重复
                if (Utils.isNotEmpty(properties)) {
                    for (int i = properties.size() - 1; i >= 0; i--) {
                        OrderProperty property = properties.get(0);
                        if (property.getPropertyKind() == PropertyKind.PROPERTY) {
                            properties.remove(i);
                        }
                    }
                }
                // 获取到商品的属性信息
                DishPropertyFilter dishPropertyFilter = new DishPropertyFilter(dishShop);
                List<DishBrandProperty> dishBrandPropertyList = DishCache.getDishPropertyHolder().filter(dishPropertyFilter);
                //菜品属性信息为空，使用的是公共库，所有属性都可以直接加进去
                boolean hasDishProperty = false;//是否有口味做法
                for (DishBrandProperty brandProperty : dishBrandPropertyList) {
                    if (brandProperty.getPropertyKind() == PropertyKind.PROPERTY) {
                        hasDishProperty = true;
                    }
                }
                if (!hasDishProperty) {
                    for (OrderProperty property : mShopcartItem.getProperties()) {
                        if (property.getPropertyKind() == PropertyKind.PROPERTY) {
                            properties.add(property);
                        }
                    }
                } else {
                    //缓存菜品的私有属性库
                    Map<Long, DishBrandProperty> brandPropertyMap = new HashMap<>();
                    for (DishBrandProperty brandProperty : dishBrandPropertyList) {
                        if (brandProperty.getPropertyKind() == PropertyKind.PROPERTY) {
                            brandPropertyMap.put(brandProperty.getPropertyId(), brandProperty);
                        }
                    }

                    for (OrderProperty property : mShopcartItem.getProperties()) {
                        if (property.getProperty() == null || property.getPropertyKind() != PropertyKind.PROPERTY) {
                            continue;
                        }

                        //判断菜品私有属性库，有没有当前属性
                        Long propertyId = property.getProperty().getId();//加料对应菜品的品牌菜id
                        DishBrandProperty brandProperty = brandPropertyMap.get(propertyId);
                        if (brandProperty != null) {
                            properties.add(property);
                        }
                    }
                }

                //备注
                String memo = mShopcartItem.getMemo();
                boolean isGroupDish = mShopcartItem.isGroupDish();
                ShopcartItemType shopcartItemType = mShopcartItem.getShopcartItemType();
                // 因为orderDish对象已经变了，所以要创建新的条目
                mShopcartItem = new ShopcartItem(mShopcartItem.getUuid(), orderDish);
                BigDecimal increaseUnit = MathDecimal.trimZero(orderDish.getDishShop().getDishIncreaseUnit());
                BigDecimal stepNum = MathDecimal.trimZero(orderDish.getDishShop().getStepNum());
                if (shopcartItemType == ShopcartItemType.MAINBATCH && increaseUnit != null) {
                    increaseUnit = increaseUnit.multiply(mShoppingCart.getOrder().getSubTradeCount());
                    stepNum = stepNum.multiply(mShoppingCart.getOrder().getSubTradeCount());
                }

                // 拷贝对应属性
                mShopcartItem.setRelateInfo(relateId, relateUuid);
                mShopcartItem.setExtraItems(extraItems);
                mShopcartItem.setProperties(properties);
                mShopcartItem.setMemo(memo);
                mShopcartItem.setPrivilege(mTradePrivilege);
                mShopcartItem.setDiscountReasonRel(mDiscountReason);
                mShopcartItem.setMemo(mMemo);
                mShopcartItem.setIsGroupDish(isGroupDish);
                mShopcartItem.setShopcartItemType(shopcartItemType);
                mRealItemBase = mShopcartItem;

                BigDecimal oldQty = mDishDataItem.getItem().getSingleQty();

                int sub = oldQty.intValue() - increaseUnit.intValue();
                if (sub >= 0 && (sub % stepNum.intValue()) == 0) {
                    mShopcartItem.changeQty(oldQty);
                } else {
                    mShopcartItem.changeQty(increaseUnit);
                }

                //称重商品
                //if (mShopcartItem.getSaleType() == SaleType.WEIGHING) {
                //    showQuantityEditPopupWindow();
                //} else {
                mShoppingCart.addDishToShoppingCart(mShopcartItem, false, false);
                if (mListener != null) {
                    mListener.closePage(null);
                }
                added = true;
                //}
            }
        } else {
            isFirst = false;
            added = false;
        }
    }

    protected void showSeatView() {

    }

    private void clickStandardButton() {
        if (!ClickManager.getInstance().isClicked(R.id.btn_standard)) {
            if (DinnerTradeItemManager.getInstance().isSaved(mDishDataItem)
                    || DinnerTradeItemManager.getInstance().isReturnDish(mDishDataItem.getBase())) {
                if (mCurrentMode == DinnerDishMiddleFragment.GROUP_ORDER_MODE) {
                    ViewUtil.setButtonEnabled(btnStandard, false);
                    return;
                }
                if (mDishDataItem.getBase().getSingleQty().compareTo(BigDecimal.ZERO) <= 0) {
                    ToastUtil.showShortToast(R.string.dish_must_more_than_0);
                    return;
                }

                boolean existKds = ShopInfoCfg.getInstance().isExistKdsDevice();
                if (existKds) {
                    if (mDishDataItem.getBase() != null && !TextUtils.isEmpty(mDishDataItem.getBase().getBatchNo())
                            && mDishDataItem.getBase() instanceof ReadonlyShopcartItemBase) {//已出单的菜，才判断kds的状态限制
                        if (!((ReadonlyShopcartItemBase) mDishDataItem.getBase()).isWaiting()) {
                            ToastUtil.showShortToast(R.string.dish_has_maked);
                            return;
                        }
                    }
                }

                String modifyDishPermissionCode = getModifyDishPermissionCode();
                if (modifyDishPermissionCode != null) {
                    VerifyHelper.verifyAlert(mActivity, modifyDishPermissionCode,
                            new VerifyHelper.Callback() {
                                @Override
                                public void onPositive(User user, String code, Auth.Filter filter) {
                                    super.onPositive(user, code, filter);
                                    modifyDish();
                                }
                            });
                } else {//没有code就表示不需要权限，预点菜界面的修改不需要权限就传的null
                    modifyDish();
                }
            } else if (DinnerTradeItemManager.getInstance().isCombo(mRealItemBase)) {
                if (mChangePageListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, mUuid);
                    bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST);
                    bundle.putBoolean(Constant.NONEEDCHECK, false);
                    mChangePageListener.changePage(ChangePageListener.DISHCOMBO, bundle);
                }
                if (mListener != null)
                    mListener.closePage(null);
            } else {
                showStandardView();
            }
        }
    }

    /**
     * 退菜
     */
    private void modifyDish() {
        MobclickAgentEvent.onEvent(UserActionCode.ZC020015);
        //未出单或者已出单的菜品重新生成新菜
        ShopcartItem shopcartItem = null;
        try {
            if (mDishDataItem.getBase() instanceof ReadonlyShopcartItem) {
                ReadonlyShopcartItem oldShopcartItem = (ReadonlyShopcartItem) mDishDataItem.getBase();
                ShopcartItemUtils.splitBatchItem(oldShopcartItem);
                shopcartItem = oldShopcartItem.modifyDish();
                if (shopcartItem != null) {
                    DinnerShoppingCart.getInstance().modifyDish(shopcartItem);
                    //套餐修改完成后，跳转到套餐点菜界面
                    if (shopcartItem.getOrderDish().isCombo() && mChangePageListener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, shopcartItem.getUuid());
                        bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST);
                        bundle.putBoolean(Constant.NONEEDCHECK, false);
                        mChangePageListener.changePage(ChangePageListener.DISHCOMBO, bundle);
                    }
                }

                //处理原菜库存
                if (isNeedInventory()) {
                    switch (oldShopcartItem.getType()) {
                        case SINGLE:
                        case COMBO:
                            InventoryItem inventoryItem = new InventoryItem(oldShopcartItem.tradeItem,
                                    oldShopcartItem.getTotalQty(), InventoryItem.TAG_INVENTORY_NUM);

                            //套餐外壳添加子菜列表
                            if (oldShopcartItem.getType() == DishType.COMBO && Utils.isNotEmpty(oldShopcartItem.getSetmealItems())) {
                                List<TradeItem> childTradeItemList = new ArrayList<>();
                                //添加子菜
                                for (ReadonlySetmealShopcartItem childShopcartItem : oldShopcartItem.getSetmealItems()) {
                                    childTradeItemList.add(childShopcartItem.tradeItem);
                                }
                                inventoryItem.setChildTradeItem(childTradeItemList);
                            }

                            boolean isInventorySwitch = InventoryCacheUtil.getInstance().getSaleSwitch();//是否是展示库存模式
                            if (isInventorySwitch) {
                                ReturnInventoryDialogFragment fragment = new ReturnInventoryDialogFragment();
                                fragment.setInventoryItemList(Utils.asList(inventoryItem));
                                fragment.setTitle(R.string.order_center_return_inventory_title2);
                                fragment.setReturnDishDataListener(new ReturnInventoryDialogFragment.ReturnDishDataListener() {
                                    @Override
                                    public void setDishData(List<InventoryItem> inventoryItemList) {
                                        DinnerShoppingCart.getInstance().addReturnInventoryList(inventoryItemList);
                                    }
                                });
                                fragment.setReturnCloseListener(new ReturnInventoryDialogFragment.ReturnCloseListener() {
                                    @Override
                                    public void onClose(List<InventoryItem> inventoryItemList) {
                                        //关闭退库存弹框时，将库存退回量置为0
                                        if (Utils.isNotEmpty(inventoryItemList)) {
                                            for (InventoryItem item : inventoryItemList) {
                                                item.setReturnInventoryNum(BigDecimal.ZERO);
                                            }
                                        }
                                        DinnerShoppingCart.getInstance().addReturnInventoryList(inventoryItemList);
                                    }
                                });
                                fragment.show(mActivity.getSupportFragmentManager(), "returnInventory");
                            } else {
                                //非展示库存模式下，库存全退
                                DinnerShoppingCart.getInstance().addReturnInventoryList(Utils.asList(inventoryItem));
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) {
            if (e != null && e.getMessage() != null)
                Log.e(TAG, e.getMessage());
        }
        final ShopcartItem tempShopcartItem = shopcartItem;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (mListener != null) {
                    mListener.closePage(tempShopcartItem);
                }
            }
        });
    }

    /**
     * 是否需要处理库存
     * 注：预点菜和团餐配置菜单不需要
     *
     * @return
     */
    protected boolean isNeedInventory() {
        return true;
    }

    /**
     * 展示规格页
     */
    private void showStandardView() {
        ViewUtil.setButtonSelected(vActionBar, btnStandard);

        if (mCustomStandardView == null) {
            mCustomStandardView = CustomStandardView_.build(mActivity);
        }
        mCustomStandardView.setData(standardGroupList, mDishPropertyManager, mShoppingCart);
        mCustomStandardView.setListener(new CustomStandardView.PropertListener() {
            @Override
            public void propertyData(EventDishPropertiesNotice eventDishPropertiesNotice) {
                refreshView(eventDishPropertiesNotice);
            }
        });
        showCustomContentView(mCustomStandardView);
    }

    /**
     * 展示做法页
     */
    private void showPropertyView() {
        ViewUtil.setButtonSelected(vActionBar, btnProperty);

        if (mTasteView == null) {
            mTasteView = TasteView_.build(mActivity);
            mTasteView.setUnFold(false);
            mTasteView.setListener(new OrderDishListenerImp() {

                @Override
                public void addExperience(OrderProperty property, boolean needAdd) {
                    for (int i = 0; i < properties.size(); i++) {
                        if (property.getProperty().equals(properties.get(i).getProperty()))
                            return;
                    }
                    properties.add(property);
                    if (mSetmealShopcartItem != null) {
                        mSetmealShopcartItem.setProperties(new ArrayList<OrderProperty>(properties));
                    } else {
                        mShopcartItem.setProperties(new ArrayList<OrderProperty>(properties));
                    }

                    if (needAdd) {
                        mShoppingCart.updateDinnerDish(mShopcartItem, false);
                    }
                }

                @Override
                public void deleteExperience(OrderProperty property) {
                    int index = -1;
                    for (int i = 0; i < properties.size(); i++) {
                        if (property.getProperty().equals(properties.get(i).getProperty())) {
                            index = i;
                            break;
                        }
                    }
                    if (index != -1) {
                        properties.remove(index);
                        if (mSetmealShopcartItem != null) {
                            mSetmealShopcartItem.setProperties(new ArrayList<OrderProperty>(properties));
                        } else {
                            mShopcartItem.setProperties(new ArrayList<OrderProperty>(properties));
                        }
                        mShoppingCart.updateDinnerDish(mShopcartItem, false);
                    }
                }
            });
        }
        mTasteView.setList(tasteGroupVo);
        showCustomContentView(mTasteView);
    }

    /**
     * 展示加料页
     */
    private void showExtraView() {
        ViewUtil.setButtonSelected(vActionBar, btnExtra);

        if (mExtraView == null) {
            mExtraView = ExtraView_.build(mActivity);
            mExtraView.setListener(new OrderDishListenerImp() {
                @Override
                public void onAddMaterial(ExtraInfo extraInfo, BigDecimal qty) {
                    if (mSetmealShopcartItem != null) {
                        mSetmealShopcartItem.setExtra(extraInfo.orderExtra, qty);
                    } else {
                        mShopcartItem.setExtra(extraInfo.orderExtra, qty);
                    }
                    mShoppingCart.updateDinnerDish(mShopcartItem, false);
                }
            });
        }
        mExtraView.setList(extraList);
        showCustomContentView(mExtraView);
    }

    /**
     * 展示变价页
     */
    private void showChangePriceView() {
        ViewUtil.setButtonSelected(vActionBar, btnChangePrice);

        if (mCustomChangePriceView == null) {
            mCustomChangePriceView = DinnerCustomChangePriceView_.build(mActivity);
            mCustomChangePriceView.setFramentActivity(mActivity);
            mCustomChangePriceView.setOnPriceChangeListener(this);
        }
        if (mRealItemBase != null)
            mCustomChangePriceView.setData(mRealItemBase.getOrderDish());
        showCustomContentView(mCustomChangePriceView);
    }

    /**
     * @param customContentView
     */
    protected void showCustomContentView(View customContentView) {
        vContent.removeAllViews();
        if (customContentView == null) {
            if (mCustomEmptyView == null) {
                mCustomEmptyView = CustomEmptyView_.build(mActivity);
            }
            vContent.addView(mCustomEmptyView);
            setCurrentContentView(mCustomEmptyView);
        } else {
            vContent.addView(customContentView);
            setCurrentContentView(customContentView);
        }
    }

    /**
     * 展示数量编辑框
     */
    private void showQuantityEditPopupWindow() {
        if (mQuantityEditPopupWindow != null && mQuantityEditPopupWindow.isShowing()) {
            return;
        }
        QuantityEditPopupWindow.DataChangeListener dataChangeListener = new QuantityEditPopupWindow.DataChangeListener() {

            @Override
            public void dataChange(ShopcartItemBase shopcartItem, boolean isContainProperties) {
                mShoppingCart.updateDinnerDish(shopcartItem, false);
                added = true;
                if (mListener != null) {
                    mListener.closePage(null);
                }
            }
        };
        mQuantityEditPopupWindow = new QuantityEditPopupWindow(mActivity, getShopcartItem(), true, true, dataChangeListener);
        mQuantityEditPopupWindow.setDinner(true);
        mQuantityEditPopupWindow.showAtLocation(vActionBar, Gravity.NO_GRAVITY, 0, 0);
    }

    /**
     * 关闭数量弹框
     */
    public void dismissQuantityEditPopupWindow() {
        if (mQuantityEditPopupWindow != null && mQuantityEditPopupWindow.isShowing()) {
            mQuantityEditPopupWindow.dismiss();
            mQuantityEditPopupWindow = null;
        }
    }

    /**
     * 单品或套餐的属性数据过滤器
     *
     * @version: 1.0
     * @date 2015年7月15日
     */
    private static class DishPropertyFilter implements DishCache.DataFilter<DishBrandProperty> {

        private final DishShop dishShop;

        DishPropertyFilter(DishShop dishShop) {
            this.dishShop = dishShop;
        }

        @Override
        public boolean accept(DishBrandProperty entity) {
            return dishShop.getBrandDishId().equals(entity.getDishId());
        }

    }

    /**
     * 展示备注页
     */
    private void showRemarkView() {
        if (ClickManager.getInstance().isClicked(R.id.btn_remark)) {
            return;
        }
        ViewUtil.setButtonSelected(vActionBar, btnRemark);

        mRemarkView = DinnerRemarkView_.build(mActivity, mRealItemBase, mShoppingCart.getShoppingCartVo().getmTradeVo().getTrade().getTradeMemo(), false);
        mRemarkView.setListener(new OrderDishListenerImp() {
            @Override
            public void onMemoChanged(String memo, boolean isOrderRemark) {
                mMemo = memo;
                if (added) {
                    if (mSetmealShopcartItem != null) {
                        mSetmealShopcartItem.setMemo(memo);
                    } else {
                        mShopcartItem.setMemo(memo);
                    }

                    mShoppingCart.updateDinnerDish(mShopcartItem, false);
                }
            }
        });
        showCustomContentView(mRemarkView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_standard:
                clickStandardButton();
                break;
            case R.id.btn_change_price:
                if (!ClickManager.getInstance().isClicked(R.id.btn_change_price)) {
                    showChangePriceView();
                }
                break;
            case R.id.btn_extra:
                if (!ClickManager.getInstance().isClicked(R.id.btn_extra)) {
                    showExtraView();
                }
                break;
            case R.id.btn_property:
                if (!ClickManager.getInstance().isClicked(R.id.btn_property)) {
                    showPropertyView();
                }
                break;
            case R.id.btn_remark:
                showRemarkView();
                break;
            default:
                break;
        }
    }

    public ShopcartItemBase getShopcartItem() {
        if (mSetmealShopcartItem != null) {
            return mSetmealShopcartItem;
        }
        return mShopcartItem;
    }

    @Override
    public void onSelectedQtyChanged(BigDecimal selectedQty) {
        if (mSelectedQty.compareTo(selectedQty) == 0) {
            return;
        }
        if (added) {
            doQtyChange(selectedQty);
        }
    }

    protected void doQtyChange(BigDecimal selectedQty) {
        if (mSetmealShopcartItem != null) {
            if (mDishSetmealManager != null) {
                switch (mDishSetmealManager.modifySetmeal(mSetmealShopcartItem, selectedQty)) {
                    case SUCCESSFUL:
                        mSetmealShopcartItem.changeQty(selectedQty);
                        mSelectedQty = selectedQty;
                        break;
                    case FAILED_MULTI:
                        ToastUtil.showShortToast(R.string.order_dish_setmeal_modify_failed_multi);
                        vDishQuantity.setTextWithoutListener(mSelectedQty.toString());
                        return;
                    case FAILED_GREATER_THAN_MAX:
                        ToastUtil.showShortToast(R.string.order_dish_setmeal_count_exceed);
                        vDishQuantity.setTextWithoutListener(mSelectedQty.toString());
                        return;
                    default:
                        ToastUtil.showShortToast(R.string.order_dish_setmeal_modify_failed);
                        vDishQuantity.setTextWithoutListener(mSelectedQty.toString());
                        return;
                }
            }
        } else {
            mShopcartItem.changeQty(selectedQty);
            mSelectedQty = selectedQty;
        }
        mShoppingCart.updateDinnerDish(mShopcartItem, false);
    }

    @Override
    public void onOnClicked() {
        //套餐子菜称重商品不使用弹框
        /*if (mSetmealShopcartItem != null) {
            return;
        }*/
        showQuantityEditPopupWindow();
    }

    @Override
    public void onPriceChanged(BigDecimal price) {
        if (mSetmealShopcartItem != null) {
            mSetmealShopcartItem.changePrice(price);
        } else {
            mShopcartItem.changePrice(price);
        }
        mShoppingCart.updateDinnerDish(mShopcartItem, false);
    }

    public void setCurrentContentView(View currentContentView) {
        mCurrentContentView = currentContentView;
    }

    public void stopAsyncTask() {
        if (mAsyncTask != null && !mAsyncTask.isCancelled()) {
            mAsyncTask.cancel(true);
        }
    }

    public void unregisterShoppingCartListener() {
        if (mShoppingCart != null) {
            mShoppingCart.unRegisterListenerByTag(ShoppingCartListerTag.ORDER_DISH_MIDDLE);
        }
    }

    protected String getModifyDishPermissionCode() {
        return DinnerApplication.PERMISSION_DINNER_MODIFY_DISH;
//        return null;
    }

}
