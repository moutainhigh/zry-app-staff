package com.zhongmei.beauty.order.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zhongmei.beauty.order.util.IChangeMiddlePageListener;
import com.zhongmei.beauty.order.util.IOperateListener;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderExtra;
import com.zhongmei.bty.cashier.orderdishmanager.entity.BatchDishPropertyVo;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishPropertiesVo;
import com.zhongmei.bty.snack.orderdish.buinessview.BeautyExtraView;
import com.zhongmei.bty.snack.orderdish.buinessview.BeautyExtraView_;
import com.zhongmei.bty.snack.orderdish.buinessview.ExtraInfo;
import com.zhongmei.bty.snack.orderdish.buinessview.ExtraView;
import com.zhongmei.bty.snack.orderdish.buinessview.ExtraView_;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderDish;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderProperty;
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
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.orderdish.manager.DishSetmealManager;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.util.ViewUtil;
import com.zhongmei.beauty.order.view.BeautyRemarkView;
import com.zhongmei.beauty.order.view.BeautyRemarkView_;
import com.zhongmei.bty.cashier.orderdishmanager.DishPropertyManager;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishStandardVo;
import com.zhongmei.bty.cashier.shoppingcart.vo.PropertyGroupVo;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.dinner.manager.DinnerTradeItemManager;
import com.zhongmei.bty.entity.event.orderdishevent.EventDishPropertiesNotice;
import com.zhongmei.bty.snack.orderdish.InterfaceListener.OrderDishListenerImp;
import com.zhongmei.bty.snack.orderdish.buinessview.CustomStandardView;
import com.zhongmei.bty.snack.orderdish.buinessview.CustomStandardView_;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by demo on 2018/12/15
 */

public class BeautyPropertyUtil {

    /**
     * 规格页
     */
    private CustomStandardView mCustomStandardView;
    /**
     * 备注页
     */
    private BeautyRemarkView mRemarkView;

    //购物车条目UUID
    private String mUuid;
    //购物车父条目UUID，可为空
    private String mParentUuid;

    private boolean isFirst = true;
    private List<PropertyGroupVo<DishStandardVo>> standardGroupList;
    private ArrayList<OrderProperty> properties;
    private DishPropertyManager mDishPropertyManager;

    private Button btnStandard;
    private Button btnRemark;
    private Button btnTradeItemRemark;

    /**
     * 加料页
     */
    private BeautyExtraView mExtraView;

    /**
     * 加料列表
     */
    private List<ExtraInfo> extraInfoList = new ArrayList<>();

    /**
     * 当前内容
     */
    private View mCurrentContentView;

    protected BigDecimal mSelectedQty;

    private TradePrivilege mTradePrivilege;
    private TradeReasonRel mDiscountReason;

    //若当前菜品不为空，那么此DishSetmealManager不为空
    protected DishSetmealManager mDishSetmealManager;

    private int mTargetPageNo = -1;
    Context mContext;
    private IOperateListener iOperateListener;
    private IChangeMiddlePageListener mChangeListener;

    private AsyncTask mAsyncTask;

    protected DinnerShoppingCart mShoppingCart;
    protected DishDataItem mDishDataItem;
    protected ShopcartItem mShopcartItem;
    protected SetmealShopcartItem mSetmealShopcartItem;
    protected ShopcartItemBase<?> mRealItemBase;
    private IShopcartItemBase mShopcartItemBase;
    private String mMemo;
    // 当前是否有菜品加入
    protected boolean added = true;

    private boolean isOrderRemark = true;
    private LinearLayout vActionBar;
    private ChangePageListener mChangePageLisener;

    public BeautyPropertyUtil(Context context, ChangePageListener changePageListener, IOperateListener iOperateListener, IChangeMiddlePageListener listener) {
        mContext = context;
        this.iOperateListener = iOperateListener;
        mChangeListener = listener;
        mChangePageLisener = changePageListener;
        mShoppingCart = DinnerShoppingCart.getInstance();
    }

    public void initView(LinearLayout vAnchor, Button btnStandard, Button btnRemark,Button btnTradeItemRemark) {
        this.btnStandard = btnStandard;
        this.btnRemark = btnRemark;
        this.btnTradeItemRemark=btnTradeItemRemark;
        vActionBar = vAnchor;
    }

    public void initData(DishDataItem dishDataItem) {
        this.mDishDataItem = dishDataItem;
        if (mDishDataItem == null) {
            initValue();
            return;
        }
        initData();
    }

    protected void initData() {
//        if (mTargetPageNo == -1 && TextUtils.isEmpty(mDishDataItem.getBase().getUuid())
//                || DinnerTradeItemManager.getInstance().isSaved(mDishDataItem)) {
//            initSavedData();
//            showRemarkView();
//        } else {
//            initSelectData();
//            showDefaultView();
//        }

        initSelectData();
        showDefaultView();
    }

    protected void initSavedData() {
        initValue();
        mShopcartItemBase = mDishDataItem.getBase();
        mUuid = mDishDataItem.getBase().getUuid();
        mParentUuid = mDishDataItem.getBase().getParentUuid();

        ViewUtil.setButtonEnabled(btnStandard, false);
        ViewUtil.setButtonEnabled(btnRemark, true);
//        if (isSavedValidSingleOrComboItem()) {
////            btnStandard.setText(R.string.dish_combo_modify);
//            btnStandard.setEnabled(false);
//            btnRemark.setEnabled(false);
//        } else {
//            btnStandard.setText(R.string.dish_standard);
//            btnStandard.setEnabled(false);
//        }
//        btnStandard.setAlpha(0.5f);
        //刷新内容页
        iOperateListener.showCustomContentView(null);
    }

    /**
     * 修改按钮激活
     */
    public void doActiveModifyBtn() {
        btnStandard.setAlpha(1);
        btnStandard.setText(R.string.dish_combo_modify);
        ViewUtil.setButtonEnabled(btnStandard, true);
        ViewUtil.setButtonSelected(vActionBar, btnStandard);
    }

    /**
     * 是否为已保存的有效单菜或套餐外壳（全退的0条目除外）
     *
     * @return
     */
    public boolean isSavedValidSingleOrComboItem() {
        if (mDishDataItem.getBase() == null) {
            return false;
        }

        return mDishDataItem.getBase().getStatusFlag() == StatusFlag.VALID
                && DinnerTradeItemManager.getInstance().isSaved(mDishDataItem)
                && !DinnerTradeItemManager.getInstance().isDishReturnAll(mDishDataItem)//全退菜品不能修改
                && !DinnerTradeItemManager.getInstance().isUnsavedReadonly(mDishDataItem.getBase())//未生效的readonlyshopcartitem不能修改
                && !(mDishDataItem.getBase() instanceof ISetmealShopcartItem);//套餐子菜不能修改
    }

    protected void initSelectData() {
        initCommonData(mDishDataItem);
        //加载数据
        mDishPropertyManager = new DishPropertyManager();
        //套餐不加载属性
        if (mRealItemBase != null && !DinnerTradeItemManager.getInstance().isCombo(mRealItemBase)) {
            loadProperties();
            mDishSetmealManager = mShopcartItem.getSetmealManager();
        }else{
            //设置加项目不可用，同时开启备注
            initSavedData();
            showRemarkView();
        }
//        if (mDishDataItem.getBase() != null && mDishDataItem.getBase().getStatusFlag() == StatusFlag.VALID
//                && mDishDataItem.getBase().getDishShop() != null && mDishDataItem.getBase().getDishShop().getClearStatus()
//                == ClearStatus.SALE
//                ) {
//            ViewUtil.setButtonEnabled(btnRemark, true);
//        } else {
//            ViewUtil.setButtonEnabled(btnRemark, false);
//        }
    }

    private void modifyCombo() {
        if (mChangePageLisener != null) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, mUuid);
            bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST);
            bundle.putBoolean(Constant.NONEEDCHECK, false);
            mChangePageLisener.changePage(ChangePageListener.DISHCOMBO, bundle);
        }
        if (mChangeListener != null) {
            mChangeListener.showCombo(mShopcartItem);
        }
        doActiveModifyBtn();
    }

    /**
     * 不加载属性
     *
     * @param dishDataItem
     */
    public void initCommonData(DishDataItem dishDataItem) {
        mDishDataItem = dishDataItem;
        initValue();
        if (mDishDataItem == null) {
            return;
        }

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

        mShopcartItemBase = mRealItemBase;
    }


    private void initValue() {
        mShopcartItemBase = null;
        added = true;
        isFirst = true;
        mUuid = null;
        mShopcartItem = null;
        mSetmealShopcartItem = null;
        mRealItemBase = null;
        mDishPropertyManager = null;
        standardGroupList = new ArrayList<>();
        properties = new ArrayList<>();
        extraInfoList.clear();
    }

    private void loadProperties() {
        mAsyncTask = new AsyncTask<Void, Void, EventDishPropertiesNotice>() {

            @Override
            protected EventDishPropertiesNotice doInBackground(Void... params) {
                return mDishPropertyManager.loadData(mRealItemBase, mShoppingCart.getIsSalesReturn(), true);
            }

            @Override
            protected void onPostExecute(EventDishPropertiesNotice eventDishPropertiesNotice) {
//                refreshView(eventDishPropertiesNotice);
                setExtra(eventDishPropertiesNotice.dishPropertiesVo);
            }
        }.execute();
    }

    public void stopAsyncTask() {
        if (mAsyncTask != null && !mAsyncTask.isCancelled()) {
            mAsyncTask.cancel(true);
        }
    }

    /**
     * 刷新菜品属性内容
     *
     * @param eventDishPropertiesNotice
     */
    private void refreshView(EventDishPropertiesNotice eventDishPropertiesNotice) {
        if (eventDishPropertiesNotice != null && eventDishPropertiesNotice.dishPropertiesVo != null && eventDishPropertiesNotice.uuid.equals(mUuid)) {
            // 规格（规格切换不成功时，直接返回）
            if (!setStandard(eventDishPropertiesNotice)) {
                return;
            }

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
            //菜品加入购物车
            addOrderDishToCart(eventDishPropertiesNotice);
        }
    }

    //设置加料
    private void setExtra(DishPropertiesVo batchDishPropertyVo) {
        extraInfoList.clear();

        if (batchDishPropertyVo.getExtraList() != null) {
            for (OrderExtra orderExtra : batchDishPropertyVo.getExtraList()) {
                ExtraInfo extraInfo = new ExtraInfo(orderExtra, false);
                extraInfo.setQty(BigDecimal.ZERO);
                extraInfoList.add(extraInfo);
            }
        }

        if (Utils.isEmpty(extraInfoList)) {
            showRemarkView();
            return;
        }

        if (mCurrentContentView != null && mCurrentContentView instanceof BeautyExtraView) {
            mExtraView.setListForBatchOperation(extraInfoList);
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
            btnStandard.setAlpha(1);
            ViewUtil.setButtonEnabled(btnStandard, true);
        } else {
            btnStandard.setText(R.string.dish_standard);
            boolean isHasStandard = Utils.isNotEmpty(standardGroupList);
            ViewUtil.setButtonEnabled(btnStandard, isHasStandard);
            if (!isHasStandard) {
                btnStandard.setAlpha(0.4f);
            }
            if (mCurrentContentView != null && mCurrentContentView instanceof CustomStandardView) {
                ((CustomStandardView) mCurrentContentView).setData(standardGroupList, mDishPropertyManager, mShoppingCart);
            }
        }
        ViewUtil.setViewVisibility(btnStandard, View.VISIBLE);
        if (DinnerTradeItemManager.getInstance().isSaved(mDishDataItem)) {
            btnStandard.setAlpha(0.4f);
            ViewUtil.setViewVisibility(btnStandard, View.GONE);
        }

        return true;
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
                    if (!DinnerTradeItemManager.getInstance().isCombo(mRealItemBase)) {
                        if (btnStandard.isEnabled()) {
                            showStandardView();
                        } else if (btnRemark.isEnabled()) {
                            showRemarkView();
                        } else {
                            iOperateListener.showCustomContentView(null);
                        }
                    } else {
                        iOperateListener.showCustomContentView(null);
                    }
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
                if (shopcartItemType == ShopcartItemType.MAINBATCH && increaseUnit != null) {
                    increaseUnit = increaseUnit.multiply(mShoppingCart.getOrder().getSubTradeCount());
                }
                // 拷贝对应属性
                mShopcartItem.changeQty(increaseUnit);
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
                mShoppingCart.addDishToShoppingCart(mShopcartItem, false, false);
                if (mChangeListener != null) {
                    mChangeListener.closePage(null);
                }
                added = true;
            }
        } else {
            isFirst = false;
            added = false;
        }
    }

    public void showDefaultView() {
        if (mDishDataItem == null) {
            return;
        }
        if (isSavedValidSingleOrComboItem()) {
            btnStandard.setAlpha(0.4f);
            showRemarkView();
//        } else if (DinnerTradeItemManager.getInstance().isCombo(mRealItemBase)) {
//            doActiveModifyBtn();
        } else if (DinnerTradeItemManager.getInstance().isCombo(mRealItemBase)) {
            modifyCombo();
        } else {
            btnStandard.setAlpha(1);
//            showStandardView();
            showExtra();
        }
    }


    /**
     * 展示规格页
     */
    public void showStandardView() {
        ViewUtil.setButtonSelected(vActionBar, btnStandard);
        if (mCustomStandardView == null) {
            mCustomStandardView = CustomStandardView_.build(mContext);
        }
        mCurrentContentView = mCustomStandardView;

        mCustomStandardView.setData(standardGroupList, mDishPropertyManager, mShoppingCart);
        mCustomStandardView.setListener(new CustomStandardView.PropertListener() {
            @Override
            public void propertyData(EventDishPropertiesNotice eventDishPropertiesNotice) {
                refreshView(eventDishPropertiesNotice);
            }
        });
        iOperateListener.showCustomContentView(mCustomStandardView);
        mChangeListener.changePage(IChangeMiddlePageListener.DEFAULT_PAGE, mUuid);
    }

    /**
     * 展示备注页
     *
     * @param
     */
    public void showRemarkView() {
        ViewUtil.setButtonSelected(vActionBar, mShopcartItemBase==null?btnRemark:btnTradeItemRemark);
        mRemarkView = BeautyRemarkView_.build(mContext, mShopcartItemBase, mShoppingCart.getShoppingCartVo().getmTradeVo().getTrade().getTradeMemo(), true);
        mRemarkView.setListener(new OrderDishListenerImp() {
            @Override
            public void onMemoChanged(String memo, boolean isOrderRemark) {
                if (isOrderRemark) {
                    mShoppingCart.setDinnerRemarks(memo);
                } else {
                    mMemo = memo;
                    if (added) {
                        mShopcartItemBase.setMemo(memo);
                        mShopcartItemBase.setChanged(true);
                        mShoppingCart.updateDinnerDish(mShopcartItemBase, false);
                    }
                }
            }
        });
        iOperateListener.showCustomContentView(mRemarkView);
        mChangeListener.changePage(IChangeMiddlePageListener.DEFAULT_PAGE, mUuid);
        Log.e("BeautyPropertyUtils", "showRemark.....>");
    }

    /**
     * 显示加项
     */
    public void showExtra() {
        ViewUtil.setButtonEnabled(btnStandard,true);
        ViewUtil.setButtonSelected(vActionBar, btnStandard);

        if (mExtraView == null) {
            mExtraView = BeautyExtraView_.build(mContext);
            mExtraView.setListener(new OrderDishListenerImp() {
//                @Override
//                public void onAddMaterial(ExtraView.ExtraInfo extraInfo, BigDecimal qty) {
//                    if(extraInfo != null && extraInfo.orderExtra != null){
//                        if(qty == null){
//                            qty = BigDecimal.ZERO;
//                        }
//                        extraInfo.orderExtra.setQty(qty, qty);
//                    }
//                }

                @Override
                public void onAddMaterial(ExtraInfo extraInfo, BigDecimal count) {
                    if (mShopcartItem == null) {
                        return;
                    }

                    try {
                        OrderExtra orderExtra = extraInfo.orderExtra.clone();
                        BigDecimal qty = mShopcartItem.getExtraQty(orderExtra);
                        mShopcartItem.setExtra(orderExtra, count);
                        mShoppingCart.updateDinnerDish(mShopcartItem, false);
                    } catch (Exception e) {
                        Log.e("BeautyPropertyUtils", e.getMessage(), e);
                    }
                }
            });
        }
        mCurrentContentView = mExtraView;
        mExtraView.setListForBatchOperation(extraInfoList);
        iOperateListener.showCustomContentView(mExtraView);
        mChangeListener.changePage(IChangeMiddlePageListener.DEFAULT_PAGE, mUuid);
    }


    /**
     * 显示属性（加项）
     */
    public void showProperty() {
        if (DinnerTradeItemManager.getInstance().isCombo(mRealItemBase)) {
            modifyCombo();
        } else {
            showStandardView();
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
}
